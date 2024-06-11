package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

/**
 * This concrete sub-class implements property that is set
 * using regular "setter" method.
 */
public final class MethodPropertyTest {

    private AnnotatedMethod _annotated;
    private Method _setter;
    private Object _instance;
    private JsonParser _parser;
    private DeserializationContext _ctxt;
    private MethodProperty _property;
    private NullValueProvider _nullProvider;
    private JsonDeserializer<?> _valueDeserializer;
    private TypeDeserializer _valueTypeDeserializer;

    @Before
    public void setUp() {
        _annotated = mock(AnnotatedMethod.class);
        _setter = mock(Method.class);
        when(_annotated.getAnnotated()).thenReturn(_setter);
        _instance = mock(Object.class);
        _parser = mock(JsonParser.class);
        _ctxt = mock(DeserializationContext.class);
        _nullProvider = mock(NullValueProvider.class);
        _valueDeserializer = mock(JsonDeserializer.class);
        _valueTypeDeserializer = mock(TypeDeserializer.class);

        _property = new MethodProperty(new BeanPropertyDefinition(
                "testProperty", String.class),
                ObjectType.Std_Class, _valueTypeDeserializer,
                Annotations.empty(), _annotated) {
            private static final long serialVersionUID = 1;

            @Override
            protected boolean _skipNulls() {
                return false;
            }
        };
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        when(_parser.getValueAsString()).thenReturn("foo");
        when(_parser.nextToken()).thenReturn(JsonToken.END_OBJECT);

        _property.deserializeAndSet(_parser, _ctxt, _instance);

        verify(_setter).invoke(_instance, "foo");
    }

    @Test
    public void testDeserializeAndSet_NullValue() throws IOException {
        when(_parser.getValueAsString()).thenReturn(null);
        when(_parser.nextToken()).thenReturn(JsonToken.END_OBJECT);

        _property.deserializeAndSet(_parser, _ctxt, _instance);

        verify(_setter, never()).invoke(_instance, null);
        verify(_ctxt).handleNullValue(_instance, null, _property);
    }

    @Test
    public void testDeserializeAndSet_ValueDeserializer() throws IOException {
        Object value = mock(Object.class);
        when(_parser.getValueAsString()).thenReturn("bar");
        when(_valueDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn(value);
        when(_parser.nextToken()).thenReturn(JsonToken.END_OBJECT);

        _property.deserializeAndSet(_parser, _ctxt, _instance);

        verify(_setter).invoke(_instance, value);
    }

    @Test
    public void testDeserializeAndSet_ValueDeserializerWithType() throws IOException {
        Object value = mock(Object.class);
        when(_parser.getValueAsString()).thenReturn("baz");
        when(_valueDeserializer.deserializeWithType(any(JsonParser.class), any(DeserializationContext.class),
                eq(_valueTypeDeserializer))).thenReturn(value);
        when(_parser.nextToken()).thenReturn(JsonToken.END_OBJECT);

        _property.deserializeAndSet(_parser, _ctxt, _instance);

        verify(_setter).invoke(_instance, value);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        when(_parser.getValueAsString()).thenReturn("foo");
        when(_parser.nextToken()).thenReturn(JsonToken.END_OBJECT);

        Object result = _property.deserializeSetAndReturn(_parser, _ctxt, _instance);

        assertEquals(_instance, result);
        verify(_setter).invoke(_instance, "foo");
    }

    @Test
    public void testDeserializeSetAndReturn_NullValue() throws IOException {
        when(_parser.getValueAsString()).thenReturn(null);
        when(_parser.nextToken()).thenReturn(JsonToken.END_OBJECT);

        Object result = _property.deserializeSetAndReturn(_parser, _ctxt, _instance);

        assertNull(result);
        verify(_instance, never()).equals(null);
        verify(_ctxt).handleNullValue(_instance, null, _property);
    }

    @Test
    public void testDeserializeSetAndReturn_ValueDeserializer() throws IOException {
        Object value = mock(Object.class);
        when(_parser.getValueAsString()).thenReturn("bar");
        when(_valueDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn(value);
        when(_parser.nextToken()).thenReturn(JsonToken.END_OBJECT);

        Object result = _property.deserializeSetAndReturn(_parser, _ctxt, _instance);

        assertEquals(value, result);
        verify(_setter).invoke(_instance, value);
    }

    @Test
    public void testDeserializeSetAndReturn_ValueDeserializerWithType() throws IOException {
        Object value = mock(Object.class);
        when(_parser.getValueAsString()).thenReturn("baz");
        when(_valueDeserializer.deserializeWithType(any(JsonParser.class), any(DeserializationContext.class),
                eq(_valueTypeDeserializer))).thenReturn(value);
        when(_parser.nextToken()).thenReturn(JsonToken.END_OBJECT);

        Object result = _property.deserializeSetAndReturn(_parser, _ctxt, _instance);

        assertEquals(value, result);
        verify(_setter).invoke(_instance, value);
    }

    @Test
    public void testSet() throws IOException {
        when(_setter.invoke(_instance, anyObject())).thenAnswer(new ArgumentMatcher<Object>() {
            private Object _expected;

            @Override
            public boolean matches(Object argument) {
                _expected = argument;
                return true;
            }

            @Override
            public void describeTo(org.hamcrest.Description description) {
                description.value(_expected);
            }
        });

        _property.set(_instance, "foo");

        verify(_setter).invoke(_instance, "foo");
    }

    @Test
    public void testSet_NullValue() throws IOException {
        when(_setter.invoke(_instance, anyObject())).thenAnswer(new ArgumentMatcher<Object>() {
            private Object _expected;

            @Override
            public boolean matches(Object argument) {
                _expected = argument;
                return true;
            }

            @Override
            public void describeTo(org.hamcrest.Description description) {
                description.value(_expected);
            }
        });

        _property.set(_instance, null);

        verify(_setter, never()).invoke(_instance, null);
        verify(_ctxt).handleNullValue(_instance, null, _property);
    }

    @Test
    public void testSet_ValueDeserializer() throws IOException {
        Object value = mock(Object.class);
        _property.setValueDeserializer(_valueDeserializer);

        _property.set(_instance, "bar");
        verify(_valueDeserializer).deserialize(eq(_parser), any(DeserializationContext.class));
        verify(_setter).invoke(_instance, value);
    }

    @Test
    public void testSet_ValueDeserializerWithType() throws IOException {
        Object value = mock(Object.class);
        _property.setValueDeserializer(_valueDeserializer);
        _property.setValueTypeDeserializer(_valueTypeDeserializer);

        _property.set(_instance, "baz");
        verify(_valueDeserializer).deserializeWithType(eq(_parser), any(DeserializationContext.class),
                eq(_valueTypeDeserializer));
        verify(_setter).invoke(_instance, value);
    }

    @Test
    public void testWithName() {
        PropertyName newName = mock(PropertyName.class);

        SettableBeanProperty result = _property.withName(newName);

        assertEquals(_property.getName(), _property.getName());
        assertEquals(newName, result.getName());
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> newDeserializer = mock(JsonDeserializer.class);

        SettableBeanProperty result = _property.withValueDeserializer(newDeserializer);

        assertEquals(_property.getValueDeserializer(), _property.getValueDeserializer());
        assertEquals(newDeserializer, result.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider newNullProvider = mock(NullValueProvider.class);

        SettableBeanProperty result = _property.withNullProvider(newNullProvider);

        assertEquals(_property.getNullProvider(), _property.getNullProvider());
        assertEquals(newNullProvider, result.getNullProvider());
    }

    @Test
    public void testFixAccess() {
        _property.fixAccess(_ctxt);

        verify(_annotated).fixAccess(eq(_ctxt.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS)));
    }

    @Test
    public void testGetAnnotation() {
        when(_annotated.getAnnotation(eq(Annotation.class))).thenReturn(mock(Annotation.class));

        assertEquals(mock(Annotation.class), _property.getAnnotation(Annotation.class));
    }

    @Test
    public void testGetMember() {
        when(_annotated.getMember()).thenReturn(mock(AnnotatedMember.class));

        assertEquals(mock(AnnotatedMember.class), _property.getMember());
    }
}