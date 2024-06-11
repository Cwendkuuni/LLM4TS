package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.BeanUtil;

import org.junit.Before;
import org.junit.Test;

public class SetterlessPropertyTest {
    private SettableBeanProperty _property;
    private Object _instance;
    private JsonParser _parser;
    private DeserializationContext _context;
    private SetterlessProperty _propertyUnderTest;
    private TypeDeserializer _typeDeserializer;
    private JsonDeserializer<Object> _valueDeserializer;
    private NullValueProvider _nullProvider;

    @Before
    public void setup() throws Exception {
        _instance = mock(Object.class);
        _parser = mock(JsonParser.class);
        _context = mock(DeserializationContext.class);
        _typeDeserializer = mock(TypeDeserializer.class);
        _valueDeserializer = mock(JsonDeserializer.class);
        _nullProvider = mock(NullValueProvider.class);

        when(_parser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);

        Object propertyDefinition = new Object() {};
        JavaType type = Object.class;
        _property = new SettableBeanProperty(propertyDefinition, type, _typeDeserializer, Annotations.empty(), null) {
            @Override
            public SettableBeanProperty withName(PropertyName newName) {
                return this;
            }

            @Override
            public SettableBeanProperty withValueDeserializer(JsonDeserializer<?> deser) {
                _valueDeserializer = deser;
                return this;
            }

            @Override
            public SettableBeanProperty withNullProvider(NullValueProvider nva) {
                _nullProvider = nva;
                return this;
            }

            @Override
            public Method getGetMethod() {
                Method getter = new Method("get", new Class[] {});
                getter.setAccessible(true);
                return getter;
            }
        };

        _propertyUnderTest = new SetterlessProperty(_property.getBeanPropertyDefinition(), type, _typeDeserializer,
                Annotations.empty(), (AnnotatedMethod) _property.getMember()) {
            @Override
            public SettableBeanProperty withName(PropertyName newName) {
                return this;
            }
        };
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        when(_property.getGetMethod().invoke(_instance, (Object[]) null)).thenReturn(new ArrayList<>());

        _propertyUnderTest.deserializeAndSet(_parser, _context, _instance);

        Object value = _propertyUnderTest.getValue(_instance);
        assertEquals(new ArrayList<>(), value);
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        when(_property.getGetMethod().invoke(_instance, (Object[]) null)).thenReturn(new ArrayList<>());

        Object result = _propertyUnderTest.deserializeSetAndReturn(_parser, _context, _instance);

        assertEquals(_instance, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws Exception {
        _propertyUnderTest.set(_instance, new Object());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturn() throws Exception {
        _propertyUnderTest.setAndReturn(_instance, new Object());
    }

    @Test
    public void testFixAccess() throws Exception {
        when(_property.getGetterAnnotation(MapperFeature.ACCESS.class)).thenReturn(new Access());

        _propertyUnderTest.fixAccess(_context);

        AnnotatedMember annotatedMember = _propertyUnderTest.getMember();
        assertEquals(Access.PROPERTY, annotatedMember.getRawValue());
    }

    @Test
    public void testGetAnnotation() throws Exception {
        when(_property.getGetterAnnotation(JsonValue.class)).thenReturn(new JsonValue());

        JsonValue jsonValue = (JsonValue) _propertyUnderTest.getAnnotation(JsonValue.class);

        assertEquals("", jsonValue.value());
    }

    @Test
    public void testGetMember() throws Exception {
        AnnotatedMember annotatedMember = _propertyUnderTest.getMember();
        assertEquals(_property.getMember(), annotatedMember);
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetNullValue() throws Exception {
        when(_property.getGetMethod().invoke(_instance, (Object[]) null)).thenReturn(null);

        _propertyUnderTest.deserializeAndSet(_parser, _context, _instance);

        Object value = _propertyUnderTest.getValue(_instance);
        assertNull(value);
    }

    @Test
    public void testDeserializeSetAndReturnNullValue() throws Exception {
        when(_property.getGetMethod().invoke(_instance, (Object[]) null)).thenReturn(null);

        Object result = _propertyUnderTest.deserializeSetAndReturn(_parser, _context, _instance);

        assertEquals(_instance, result);
    }

    @Test
    public void testWithValueDeserializer() throws Exception {
        StdSerializer<Object> deserializer = new StdSerializer<Object>(Object.class) {};
        _propertyUnderTest = new SetterlessProperty(_property.getBeanPropertyDefinition(), Object.class, _typeDeserializer,
                Annotations.empty(), (AnnotatedMethod) _property.getMember()) {
            @Override
            public SettableBeanProperty withValueDeserializer(JsonDeserializer<?> deser) {
                _valueDeserializer = deser;
                return new SetterlessProperty(_property, deserializer, _typeDeserializer, _contextAnnotations,
                        (AnnotatedMethod) _property.getMember());
            }
        };

        _propertyUnderTest.deserializeAndSet(_parser, _context, _instance);

        Object value = _propertyUnderTest.getValue(_instance);
        assertEquals(deserializer.deserializeValue(_parser, _context, null), value);
    }

    @Test
    public void testWithNullProvider() throws Exception {
        _propertyUnderTest = new SetterlessProperty(_property.getBeanPropertyDefinition(), Object.class, _typeDeserializer,
                Annotations.empty(), (AnnotatedMethod) _property.getMember()) {
            @Override
            public SettableBeanProperty withNullProvider(NullValueProvider nva) {
                _nullProvider = nva;
                return new SetterlessProperty(_property, _valueDeserializer, _nullProvider, _typeDeserializer,
                        (AnnotatedMethod) _property.getMember());
            }
        };

        when(_nullProvider.findNullValue(_parser, _context, Object.class)).thenReturn(new Object());

        Object result = _propertyUnderTest.deserializeSetAndReturn(_parser, _context, _instance);

        assertEquals(new Object(), result);
    }

    @Test
    public void testFixAccessWithAccessDenied() throws Exception {
        when(_property.getGetterAnnotation(MapperFeature.ACCESS.class)).thenReturn(new Access(Access.NONE));

        _propertyUnderTest.fixAccess(_context);

        AnnotatedMember annotatedMember = _propertyUnderTest.getMember();
        assertEquals(Access.NONE, annotatedMember.getRawValue());
    }

    @Test
    public void testDeserializeSetAndReturnWithAccessDenied() throws Exception {
        when(_property.getGetterAnnotation(MapperFeature.ACCESS.class)).thenReturn(new Access(Access.NONE));

        _propertyUnderTest = new SetterlessProperty(_property.getBeanPropertyDefinition(), Object.class, _typeDeserializer,
                Annotations.empty(), (AnnotatedMethod) _property.getMember()) {
            @Override
            public Object deserializeSetAndReturn(JsonParser p, DeserializationContext ctxt, Object instance)
                    throws IOException {
                Object value = _propertyUnderTest.deserializeValue(p, ctxt, null);
                _propertyUnderTest.setValue(instance, value);
                return instance;
            }
        };

        when(_property.getGetMethod().invoke(_instance, (Object[]) null)).thenReturn(new Object());

        Object result = _propertyUnderTest.deserializeSetAndReturn(_parser, _context, _instance);

        assertEquals(_instance, result);
    }

    @Test
    public void testDeserializeAndSetWithAccessDenied() throws Exception {
        when(_property.getGetterAnnotation(MapperFeature.ACCESS.class)).thenReturn(new Access(Access.NONE));

        _propertyUnderTest = new SetterlessProperty(_property.getBeanPropertyDefinition(), Object.class, _typeDeserializer,
                Annotations.empty(), (AnnotatedMethod) _property.getMember()) {
            @Override
            public void deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object instance)
                    throws IOException {
                Object value = _propertyUnderTest.deserializeValue(p, ctxt, null);
                _propertyUnderTest.setValue(instance, value);
            }
        };

        when(_property.getGetMethod().invoke(_instance, (Object[]) null)).thenReturn(new Object());

        _propertyUnderTest.deserializeAndSet(_parser, _context, _instance);

        Object value = _propertyUnderTest.getValue(_instance);
        assertEquals(new Object(), value);
    }

    @Test
    public void testDeserializeAndSetWithTypeDeserializer() throws Exception {
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(_property.getMember().getRawType()).thenReturn(Map.class);
        when(_typeDeserializer.findValueDeserializer(Map.class)).thenReturn(new MapDeserializer());
        when(_typeDeserializer.deserializeValue(_parser, _context, Map.class)).thenReturn(new HashMap<>());

        _propertyUnderTest = new SetterlessProperty(_property.getBeanPropertyDefinition(), Map.class, _typeDeserializer,
                Annotations.empty(), (AnnotatedMethod) _property.getMember()) {
            @Override
            public void deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object instance)
                    throws IOException {
                Object value = _typeDeserializer.deserializeValue(p, ctxt, Map.class);
                _propertyUnderTest.setValue(instance, value);
            }
        };

        _propertyUnderTest.deserializeAndSet(_parser, _context, _instance);

        Map<?> map = _propertyUnderTest.getValue(_instance);
        assertEquals(new HashMap<>(), map);
    }

    private static class Access {
        private final int _value;

        Access(int value) {
            _value = value;
        }

        Access() {
            this(Access.PROPERTY);
        }

        public int getValue() {
            return _value;
        }
    }

    private static class AccessAnnotation extends Annotation {}
}