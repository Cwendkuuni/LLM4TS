package com.fasterxml.jackson.databind.deser.impl;

import static com.fasterxml.jackson.core.JsonToken.*;
import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.test.BaseTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FieldPropertyTest extends BaseTest {

    private static class TestBean {
        @JsonProperty
        private int field;
    }

    private FieldProperty _property;
    private TestBean _instance;

    @Before
    public void setUp() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        _property = mapper.findBeanProperty("field", TestBean.class);
        _instance = mapper.readValue(createContext().text("{\"field\": 1}"), TestBean.class);
    }

    @After
    public void tearDown() throws Exception {
        _property = null;
        _instance = null;
    }

    @Test
    public void testGetters() {
        assertEquals("field", _property.getName());
        assertEquals(TestBean.class, _property.getType());
        assertEquals(FieldProperty.class, _property.getClass());
        assertNotNull(_property.getMember());
        assertNotNull(_property.getAnnotation(JsonProperty.class));
        assertFalse(_property.isWritable());
        assertFalse(_property.canOverride());
    }

    @Test
    public void testSetters() throws Exception {
        Object value = 42;
        _property.setValue(_instance, value);
        assertEquals(value, _instance.field);
    }

    @Test
    public void testDeserialize() throws Exception {
        JsonParser p = createParser("{\"field\": 42}");
        Object value = _property.deserializeAndSet(p, createContext(), _instance);
        assertEquals(42, _instance.field);
    }

    @Test
    public void testDeserializeAndReturn() throws Exception {
        JsonParser p = createParser("{\"field\": 42}");
        Object value = _property.deserializeSetAndReturn(p, createContext(), _instance);
        assertEquals(_instance, value);
        assertEquals(42, _instance.field);
    }

    @Test
    public void testSet() throws Exception {
        Object newValue = 123;
        _property.set(_instance, newValue);
        assertEquals(newValue, _instance.field);
    }

    @Test
    public void testSetAndReturn() throws Exception {
        Object newValue = 123;
        Object returnValue = _property.setAndReturn(_instance, newValue);
        assertEquals(_instance, returnValue);
        assertEquals(newValue, _instance.field);
    }

    @Test
    public void testReadResolve() throws Exception {
        Object resolved = _property.readResolve();
        assertEquals(FieldProperty.class, resolved.getClass());
    }

    @Test
    public void testFixAccess() throws Exception {
        _property.fixAccess(createContext().withSerializableFeatures(false));
        assertTrue(_fieldIsAccessible(_field));
    }

    @Test
    public void testDeserializeWithTypeDeserializer() throws Exception {
        SimpleType _intType = SimpleType.construct(int.class, null);
        TypeDeserializer _intDeserializer = new StandardDeserializer<>(int[].class);
        JsonDeserializer<Integer> _intJsonDeserializer = new JsonDeserializer<Integer>(Integer.class) {
            @Override
            public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getValueAsInt();
            }
        };
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerValueHandler(new StandardSerializableStringSerializer(null));
        mapper.registerModule(new SimpleModule() {
            @Override
            public void setupModule(SetupContext context) throws Exception {
                context.addHandler(new JavaTypeDescriptor.Base(int[].class) {});
                context.addValueHandler(new TypeSerializer.Std(
                        JavaType.construct(int[].class),
                        _intDeserializer,
                        new StdSerializer<int[]>(int[].class) {}));
                context.addValueHandler(new TypeSerializer.Std(
                        JavaType.construct(JavaType.construct(int.class), null),
                        _intJsonDeserializer,
                        new StdSerializer<Integer>(Integer.class) {}));
            }
        });
        ObjectType _intTypeDefinition = mapper.getTypeFactory().constructType(int[].class);
        FieldProperty _propertyWithTypeDeserializer = mapper.findBeanProperty("field", TestBean.class, _intTypeDefinition);
        JsonParser p = createParser("{\"field\": [1, 2, 3]}");
        Object value = _propertyWithTypeDeserializer.deserializeAndSet(p, createContext(), _instance);
        assertEquals(new int[]{1, 2, 3}, _instance.field);
    }

    @Test
    public void testDeserializeWithNullValueProvider() throws Exception {
        NullValueProvider _nullProvider = new StandardSerializableNullValueProvider();
        JsonParser p = createParser("\"null\"");
        Object value = _property.deserializeAndSet(p, createContext(), _instance, _nullProvider);
        assertNull(_instance.field);
    }
}