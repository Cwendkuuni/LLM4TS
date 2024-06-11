package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Assert;
import org.junit.Test;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserializeEmptyArray() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser("[]");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(jsonParser, new DeserializationContext(jsonFactory, null, null, null));
        Assert.assertArrayEquals(new String[0], result);
    }

    @Test
    public void testDeserializeWithNullElement() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser("['null']");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(jsonParser, new DeserializationContext(jsonFactory, null, null, null));
        Assert.assertArrayEquals(new String[]{null}, result);
    }

    @Test
    public void testDeserializeWithCustomElementDeserializer() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser("[" +
                "{'name': 'element1', 'value': 'value1'}" +
                ",'null'" +
                ",'{'name': 'element3', 'value': 'value3'}" +
                "]");

        JsonDeserializer<String> customElementDeserializer = new StdDeserializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                String name = jp.getValueAsString();
                return name.substring(name.lastIndexOf(':') + 1);
            }
        };

        StringArrayDeserializer deserializer = new StringArrayDeserializer(customElementDeserializer);
        String[] result = deserializer.deserialize(jsonParser, new DeserializationContext(jsonFactory, null, null, null));
        Assert.assertArrayEquals(new String[]{"value1", null, "value3"}, result);
    }

    @Test
    public void testDeserializeWithTypeDeserializer() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser("[" +
                "{'class': 'com.example.SomeClass', 'value': 'someValue1'}" +
                ",'null'" +
                ",'{'class': 'com.example.AnotherClass', 'value': 'anotherValue1'}" +
                "]");

        TypeDeserializer typeDeserializer = new BeanPropertyTypeDeserializer() {
            @Override
            public BeanPropertyTypeDeserializer createContextual(DeserializationContext ctxt, BeanProperty property) {
                return new BeanPropertyTypeDeserializer() {
                    @Override
                    public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
                        String className = jp.getValueAsString();
                        try {
                            return ctxt.constructValue(Class.forName(className));
                        } catch (ClassNotFoundException e) {
                            throw ctxt.mappingException("Failed to instantiate class " + className);
                        }
                    }
                };
            }
        };

        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        Object[] result = deserializer.deserializeWithType(jsonParser, new DeserializationContext(jsonFactory, null, null, typeDeserializer), typeDeserializer);
        Assert.assertArrayEquals(new Object[]{
                new SomeClass("someValue1"),
                null,
                new AnotherClass("anotherValue1")}, result);
    }

    @Test
    public void testDeserializeWithNullElementAndNullElementDeserializer() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser("[" +
                "null" +
                ",'null'" +
                "]");

        JsonDeserializer<String> nullElementDeserializer = new StdDeserializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };

        StringArrayDeserializer deserializer = new StringArrayDeserializer(nullElementDeserializer);
        String[] result = deserializer.deserialize(jsonParser, new DeserializationContext(jsonFactory, null, null, null));
        Assert.assertArrayEquals(new String[]{null, null}, result);
    }

    @Test
    public void testCreateContextual() {
        JsonFactory jsonFactory = new JsonFactory();
        StringArrayDeserializer deserializer = new StringArrayDeserializer();

        // Test with null context
        Assert.assertNull(deserializer.createContextual(new DeserializationContext(jsonFactory, null, null, null), null));

        // Test with no custom value deserializer
        JsonDeserializer<String> customValueDeserializer = new StdDeserializer<String>(String.class);
        BeanProperty property = new BeanProperty("test", String[].class);
        Assert.assertNull(deserializer.createContextual(new DeserializationContext(jsonFactory, null, null, null), property));

        // Test with custom value deserializer
        JsonDeserializer<String> customElementDeserializer = new StdDeserializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return jp.getValueAsString();
            }
        };
        Assert.assertEquals(customElementDeserializer, deserializer.createContextual(new DeserializationContext(jsonFactory, null, null, null), property));

        // Test with a content converter
        JsonDeserializer<Number> numberDeserializer = new StdDeserializer<Number>(Number.class) {
            @Override
            public Number deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return jp.getValueAsInt();
            }
        };
        JsonDeserializer<String> stringNumberDeserializer = new std.StdDeserializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return Integer.toString(numberDeserializer.deserialize(jp, ctxt));
            }
        };
        Assert.assertEquals(stringNumberDeserializer, deserializer.createContextual(new DeserializationContext(jsonFactory, null, null, null), property));
    }
}