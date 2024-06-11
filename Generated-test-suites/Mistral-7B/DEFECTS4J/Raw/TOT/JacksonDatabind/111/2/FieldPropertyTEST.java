package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Test;

public class FieldPropertyTest {

    static class TestClass {
        @JsonProperty(value = "name")
        private transient String _field;

        public String getName() {
            return _field;
        }

        public void setName(String name) {
            _field = name;
        }
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestClass instance = new TestClass();
        String value = "John Doe";
        ObjectNode node = mapper.createObjectNode();
        node.put("name", value);

        FieldProperty fieldProperty = new FieldProperty(
                BeanPropertyDefinition.standardPropertyDefinition(TestClass.class.getDeclaredField("name")),
                SimpleType.constructUnboundedType(String.class),
                null, null, new AnnotatedField(TestClass.class.getDeclaredField("name")))
        ;
        JsonParser parser = mapper.getFactory().createParser(node.toString());

        fieldProperty.deserializeAndSet(parser, new DeserializationContext(), instance);

        assertEquals(value, instance.getName());
    }

    @Test
    public void testDeserializeAndSetWithNullValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestClass instance = new TestClass();

        FieldProperty fieldProperty = new FieldProperty(
                BeanPropertyDefinition.standardPropertyDefinition(TestClass.class.getDeclaredField("name")),
                SimpleType.constructUnboundedType(String.class),
                null, null, new AnnotatedField(TestClass.class.getDeclaredField("name")))
        ;

        JsonParser parser = mapper.getFactory().createParser("null");

        fieldProperty.deserializeAndSet(parser, new DeserializationContext(), instance);

        assertNull(instance.getName());
    }

    @Test
    public void testDeserializeAndSetWithNullValueProvider() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestClass instance = new TestClass();
        NullValueProvider nva = new NullValueProvider() {
            @Override
            public Object getNullValue(DeserializationContext ctxt) throws IOException {
                return "defaultValue";
            }
        };

        FieldProperty fieldProperty = new FieldProperty(
                BeanPropertyDefinition.standardPropertyDefinition(TestClass.class.getDeclaredField("name")),
                SimpleType.constructUnboundedType(String.class),
                null, nva, new AnnotatedField(TestClass.class.getDeclaredField("name")))
        ;

        JsonParser parser = mapper.getFactory().createParser("null");

        fieldProperty.deserializeAndSet(parser, new DeserializationContext(), instance);

        assertEquals("defaultValue", instance.getName());
    }

    @Test
    public void testDeserializeAndSetWithValueDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestClass instance = new TestClass();
        JsonDeserializer<String> valueDeserializer = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                String value = p.getValueAsString();
                return value.toUpperCase();
            }
        };

        FieldProperty fieldProperty = new FieldProperty(
                BeanPropertyDefinition.standardPropertyDefinition(TestClass.class.getDeclaredField("name")),
                SimpleType.constructUnboundedType(String.class),
                valueDeserializer, null, new AnnotatedField(TestClass.class.getDeclaredField("name")))
        ;

        JsonParser parser = mapper.getFactory().createParser("{\"name\": \"john doe\"}");

        fieldProperty.deserializeAndSet(parser, new DeserializationContext(), instance);

        assertEquals("JOHN DOE", instance.getName());
    }

    @Test
    public void testDeserializeAndSetWithTypeDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestClass instance = new TestClass();
        JsonDeserializer<String> valueDeserializer = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                String value = p.getValueAsString();
                return new Integer(value.length());
            }
        };
        TypeDeserializer typeDeserializer = new TypeDeserializer() {
            @Override
            public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
                    throws IOException {
                Object deserialized = valueDeserializer.deserialize(p, ctxt);
                return new Integer((int) deserialized);
            }
        };

        FieldProperty fieldProperty = new FieldProperty(
                BeanPropertyDefinition.standardPropertyDefinition(TestClass.class.getDeclaredField("name")),
                SimpleType.constructUnboundedType(Integer.class),
                valueDeserializer, typeDeserializer, new AnnotatedField(TestClass.class.getDeclaredField("name")))
        ;

        JsonParser parser = mapper.getFactory().createParser("{\"name\": 5}");

        fieldProperty.deserializeAndSet(parser, new DeserializationContext(), instance);

        assertEquals(5, instance.getName().intValue());
    }

    @Test
    public void testSet() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestClass instance = new TestClass();
        String value = "John Doe";

        FieldProperty fieldProperty = new FieldProperty(
                BeanPropertyDefinition.standardPropertyDefinition(TestClass.class.getDeclaredField("name")),
                SimpleType.constructUnboundedType(String.class),
                null, null, new AnnotatedField(TestClass.class.getDeclaredField("name")))
        ;

        fieldProperty.set(instance, value);

        assertEquals(value, instance.getName());
    }

    @Test
    public void testSetWithNullValue() {
        TestClass instance = new TestClass();

        Field field = TestClass.class.getDeclaredField("name");
        field.setAccessible(true);
        field.set(instance, null);

        assertEquals(null, instance.getName());
    }

    @Test
    public void testSetWithValueDeserializerAndNullValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestClass instance = new TestClass();
        NullValueProvider nva = new NullValueProvider() {
            @Override
            public Object getNullValue(DeserializationContext ctxt) throws IOException {
                return "defaultValue";
            }
        };

        FieldProperty fieldProperty = new FieldProperty(
                BeanPropertyDefinition.standardPropertyDefinition(TestClass.class.getDeclaredField("name")),
                SimpleType.constructUnboundedType(String.class),
                null, nva, new AnnotatedField(TestClass.class.getDeclaredField("name")))
        ;

        JsonParser parser = mapper.getFactory().createParser("null");

        fieldProperty.set(instance, parser);

        assertNull(instance.getName());
    }

    @Test
    public void testSetWithValueDeserializerAndNullValueProvider() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestClass instance = new TestClass();
        JsonDeserializer<String> valueDeserializer = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                String value = p.getValueAsString();
                return value.toUpperCase();
            }
        };
        NullValueProvider nva = new NullValueProvider() {
            @Override
            public Object getNullValue(DeserializationContext ctxt) throws IOException {
                return "defaultValue";
            }
        };

        FieldProperty fieldProperty = new FieldProperty(
                BeanPropertyDefinition.standardPropertyDefinition(TestClass.class.getDeclaredField("name")),
                SimpleType.constructUnboundedType(String.class),
                valueDeserializer, nva, new AnnotatedField(TestClass.class.getDeclaredField("name")))
        ;

        JsonParser parser = mapper.getFactory().createParser("null");

        fieldProperty.set(instance, parser);

        assertEquals("defaultValue", instance.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetWithNullInstance() {
        TestClass instance = null;

        FieldProperty fieldProperty = new FieldProperty(
                BeanPropertyDefinition.standardPropertyDefinition(TestClass.class.getDeclaredField("name")),
                SimpleType.constructUnboundedType(String.class),
                null, null, new AnnotatedField(TestClass.class.getDeclaredField("name")))
        ;

        fieldProperty.set(instance, "John Doe");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetWithPrivateField() {
        TestClass instance = new TestClass();
        Field field = TestClass.class.getDeclaredField("name");
        field.setAccessible(true);
        field.setInt(instance, Modifier.PRIVATE);

        FieldProperty fieldProperty = new FieldProperty(
                BeanPropertyDefinition.standardPropertyDefinition(TestClass.class.getDeclaredField("name")),
                SimpleType.constructUnboundedType(String.class),
                null, null, new AnnotatedField(TestClass.class.getDeclaredField("name")))
        ;

        fieldProperty.set(instance, "John Doe");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetWithFinalField() {
        TestClass instance = new TestClass();
        Field field = TestClass.class.getDeclaredField("name");
        field.setAccessible(true);
        field.setFinal(true);

        FieldProperty fieldProperty = new FieldProperty(
                BeanPropertyDefinition.standardPropertyDefinition(TestClass.class.getDeclaredField("name")),
                SimpleType.constructUnboundedType(String.class),
                null, null, new AnnotatedField(TestClass.class.getDeclaredField("name")))
        ;

        fieldProperty.set(instance, "John Doe");
    }
}