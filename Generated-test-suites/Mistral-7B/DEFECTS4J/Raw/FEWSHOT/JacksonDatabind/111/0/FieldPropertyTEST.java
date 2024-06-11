package com.fasterxml.jackson.databind.deser.impl;

import static com.fasterxml.jackson.core.JsonToken.VALUE_NULL;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Field;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class FieldPropertyTest {
    @Test
    public void testDeserializeAndSet() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser p = mapper.getFactory().createParser("[1]");

        FieldProperty property = new FieldProperty(new BeanPropertyDefinition(new AnnotatedField(new JsonProperty(), new FieldTest())) {
            @Override
            public AnnotatedMember getMember() {
                return null;
            }
        }, new SimpleType("Integer"), null, null, new AnnotatedField(new JsonProperty(), new FieldTest()));

        Object instance = new Object();
        property.deserializeAndSet(p, new DeserializationContext(), instance);
        assertEquals(1, property.getField().get(instance));
    }

    @Test
    public void testDeserializeAndSetWithNull() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser p = mapper.getFactory().createParser("null");

        FieldProperty property = new FieldProperty(new BeanPropertyDefinition(new AnnotatedField(new JsonProperty(), new FieldTest())) {
            @Override
            public AnnotatedMember getMember() {
                return null;
            }
        }, new SimpleType("Integer"), null, null, new AnnotatedField(new JsonProperty(), new FieldTest()));

        Object instance = new Object();
        property.deserializeAndSet(p, new DeserializationContext(), instance);
        assertNull(property.getField().get(instance));
    }

    @Test
    public void testDeserializeAndSetWithTypeDeserializer() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser p = mapper.getFactory().createParser("\"foo\"");

        FieldProperty property = new FieldProperty(new BeanPropertyDefinition(new AnnotatedField(new JsonProperty("name"), new FieldTest())) {
            @Override
            public AnnotatedMember getMember() {
                return null;
            }
        }, TextType.INSTANCE, new StringDeserializer(), null, new AnnotatedField(new JsonProperty("name"), new FieldTest()));

        Object instance = new Object();
        property.deserializeAndSet(p, new DeserializationContext(), instance);
        assertEquals("foo", property.getField().get(instance));
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser p = mapper.getFactory().createParser("[1]");

        FieldProperty property = new FieldProperty(new BeanPropertyDefinition(new AnnotatedField(new JsonProperty(), new FieldTest())) {
            @Override
            public AnnotatedMember getMember() {
                return null;
            }
        }, new SimpleType("Integer"), null, null, new AnnotatedField(new JsonProperty(), new FieldTest()));

        Object instance = new Object();
        Object result = property.deserializeSetAndReturn(p, new DeserializationContext(), instance);
        assertEquals(1, property.getField().get(instance));
        assertEquals(instance, result);
    }

    @Test
    public void testDeserializeSetAndReturnWithNull() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser p = mapper.getFactory().createParser("null");

        FieldProperty property = new FieldProperty(new BeanPropertyDefinition(new AnnotatedField(new JsonProperty(), new FieldTest())) {
            @Override
            public AnnotatedMember getMember() {
                return null;
            }
        }, new SimpleType("Integer"), null, null, new AnnotatedField(new JsonProperty(), new FieldTest()));

        Object instance = new Object();
        Object result = property.deserializeSetAndReturn(p, new DeserializationContext(), instance);
        assertNull(property.getField().get(instance));
        assertEquals(instance, result);
    }

    @Test
    public void testDeserializeSetAndReturnWithTypeDeserializer() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser p = mapper.getFactory().createParser("\"foo\"");

        FieldProperty property = new FieldProperty(new BeanPropertyDefinition(new AnnotatedField(new JsonProperty("name"), new FieldTest())) {
            @Override
            public AnnotatedMember getMember() {
                return null;
            }
        }, TextType.INSTANCE, new StringDeserializer(), null, new AnnotatedField(new JsonProperty("name"), new FieldTest()));

        Object instance = new Object();
        Object result = property.deserializeSetAndReturn(p, new DeserializationContext(), instance);
        assertEquals("foo", property.getField().get(instance));
        assertEquals(instance, result);
    }

    @Test
    public void testSet() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Object instance = new Object();

        propertySetField(mapper, instance, 1);
        assertEquals(1, propertyGetField(instance));
    }

    @Test
    public void testSetWithNull() {
        ObjectMapper mapper = new ObjectMapper();
        Object instance = new Object();

        propertySetField(mapper, instance, null);
        assertNull(propertyGetField(instance));
    }

    @Test
    public void testSetWithTypeDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Object instance = new Object();

        JsonParser p = mapper.getFactory().createParser("\"foo\"");
        propertySetFieldWithTypeDeserializer(mapper, instance, p);
        assertEquals("foo", propertyGetField(instance));
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Object instance = new Object();

        Object result = propertySetAndReturnField(mapper, instance, 1);
        assertEquals(1, propertyGetField(instance));
        assertEquals(instance, result);
    }

    @Test
    public void testSetAndReturnWithNull() {
        ObjectMapper mapper = new ObjectMapper();
        Object instance = new Object();

        Object result = propertySetAndReturnField(mapper, instance, null);
        assertNull(propertyGetField(instance));
        assertEquals(instance, result);
    }

    @Test
    public void testSetAndReturnWithTypeDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Object instance = new Object();

        JsonParser p = mapper.getFactory().createParser("\"foo\"");
        Object result = propertySetAndReturnFieldWithTypeDeserializer(mapper, instance, p);
        assertEquals("foo", propertyGetField(instance));
        assertEquals(instance, result);
    }

    @Test
    public void testFixAccess() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Object instance = new Object();

        Field field = Field.class.getDeclaredField("modifiers");
        field.setAccessible(true);
        field.setInt(instance, 0xFFFFFFFF & (~Modifier.PUBLIC));

        FieldProperty property = new FieldProperty(new BeanPropertyDefinition(new AnnotatedField(new JsonProperty(), new FieldTest())) {
            @Override
            public AnnotatedMember getMember() {
                return null;
            }
        }, new SimpleType("Integer"), null, null, new AnnotatedField(new JsonProperty(), new FieldTest()));

        property.fixAccess(new DeserializationConfig(mapper.getDeserializationConfig()));
        assertTrue(Field.class.isAccessible(instance));
    }

    @Test
    public void testGetAnnotation() throws Exception {
        FieldProperty property = new FieldProperty(new BeanPropertyDefinition(new AnnotatedField(new JsonProperty(), new FieldTest())) {
            @Override
            public AnnotatedMember getMember() {
                return null;
            }
        }, new SimpleType("Integer"), null, null, new AnnotatedField(new JsonProperty(), new FieldTest()));

        JsonProperty jsonProperty = property.getAnnotation(JsonProperty.class);
        assertNotNull(jsonProperty);
        assertEquals("test", jsonProperty.value());
    }

    @Test
    public void testGetMember() throws Exception {
        FieldProperty property = new FieldProperty(new BeanPropertyDefinition(new AnnotatedField(new JsonProperty(), new FieldTest())) {
            @Override
            public AnnotatedMember getMember() {
                return null;
            }
        }, new SimpleType("Integer"), null, null, new AnnotatedField(new JsonProperty(), new FieldTest()));

        AnnotatedMember member = property.getMember();
        assertNotNull(member);
        assertTrue(member instanceof AnnotatedField);
    }

    @Test
    public void testToString() {
        FieldProperty property = new FieldProperty(new BeanPropertyDefinition(new AnnotatedField(new JsonProperty(), new FieldTest())) {
            @Override
            public AnnotatedMember getMember() {
                return null;
            }
        }, new SimpleType("Integer"), null, null, new AnnotatedField(new JsonProperty(), new FieldTest()));

        assertEquals("FieldProperty(name='test', typeClass=class java.lang.Integer, beanPropertyDefinition={@JsonProperty(name='test')}, annotations=[], field={@FieldTest()})", property.toString());
    }

    private static void propertySetField(ObjectMapper mapper, Object instance, Object value) throws IOException {
        FieldProperty property = new FieldProperty(new BeanPropertyDefinition(new AnnotatedField(new JsonProperty(), new FieldTest())) {
            @Override
            public AnnotatedMember getMember() {
                return null;
            }
        }, new SimpleType("Integer"), null, null, new AnnotatedField(new JsonProperty(), new FieldTest()));
        property.set(instance, value);
    }

    private static Object propertyGetField(Object instance) {
        try {
            return Field.class.getDeclaredField("test").get(instance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void propertySetFieldWithTypeDeserializer(ObjectMapper mapper, Object instance, JsonParser p) throws IOException {
        FieldProperty property = new FieldProperty(new BeanPropertyDefinition(new AnnotatedField(new JsonProperty("name"), new FieldTest())) {
            @Override
            public AnnotatedMember getMember() {
                return null;
            }
        }, TextType.INSTANCE, new StringDeserializer(), null, new AnnotatedField(new JsonProperty("name"), new FieldTest()));
        property.deserializeAndSet(p, new DeserializationContext(), instance);
    }

    private static Object propertySetAndReturnField(ObjectMapper mapper, Object instance, Object value) throws IOException {
        FieldProperty property = new FieldProperty(new BeanPropertyDefinition(new AnnotatedField(new JsonProperty(), new FieldTest())) {
            @Override
            public AnnotatedMember getMember() {
                return null;
            }
        }, new SimpleType("Integer"), null, null, new AnnotatedField(new JsonProperty(), new FieldTest()));
        Object result = property.deserializeSetAndReturn(mapper.getFactory().createParser(String.valueOf(value)), new DeserializationContext(), instance);
        return result;
    }

    private static Object propertySetAndReturnFieldWithTypeDeserializer(ObjectMapper mapper, Object instance, JsonParser p) throws IOException {
        FieldProperty property = new FieldProperty(new BeanPropertyDefinition(new AnnotatedField(new JsonProperty("name"), new FieldTest())) {
            @Override
            public AnnotatedMember getMember() {
                return null;
            }
        }, TextType.INSTANCE, new StringDeserializer(), null, new AnnotatedField(new JsonProperty("name"), new FieldTest()));
        Object result = property.deserializeSetAndReturn(p, new DeserializationContext(), instance);
        return result;
    }
}

class FieldTest {
    @JsonProperty(value = "test")
    private int test;
}