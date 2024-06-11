package com.example.junit; // replace with your test package name

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Currency;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.junit.Test;

public class FromStringDeserializerTest {

    @Test
    public void testDeserializeSupportedTypes() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectMapper mapperWithoutFailOnEmpty = new ObjectMapper();
        mapperWithoutFailOnEmpty.disable(DeserializationFeature.FAIL_ON_EMPTY_BEANS);

        Class<?>[] types = FromStringDeserializer.types();

        for (Class<?> type : types) {
            String json = mapStringToJson(type);

            // Test with mapper
            FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(type);
            String result = mapper.readValueAsString(json);
            assertEquals(type.getName(), result);

            // Test with mapper without fail on empty
            Object deserializedObject = mapperWithoutFailOnEmpty.readValue(JsonFactory.instance.createParser(json), type);
            assertEquals(type.getName(), deserializedObject.getClass().getName());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeWithEmptyString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        String json = "\"\"";
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class);
        deserializer.deserialize(JsonFactory.instance.createParser(json), new TestContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidFormat() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        String json = "InvalidFormat";
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class);
        deserializer.deserialize(JsonFactory.instance.createParser(json), new TestContext());
    }

    @Test
    public void testDeserializeEmptyStringForLocale() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        String json = "\"\"";
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        Locale locale = deserializer.deserialize(JsonFactory.instance.createParser(json), new TestContext());
        assertEquals("", locale.getLanguage());
        assertEquals("", locale.getCountry());
    }

    @Test
    public void testDeserializeWithNull() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        String json = "null";
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class);
        String result = deserializer.deserialize(JsonFactory.instance.createParser(json), new TestContext()).toString();
        assertEquals("null", result);
    }

    @Test
    public void testDeserializeWithArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        String json = "[\"hello\", \"world\"]";
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String[].class);
        String[] result = deserializer.deserialize(JsonFactory.instance.createParser(json), new TestContext());
        assertArrayEquals(new String[]{"hello", "world"}, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithInvalidArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        String json = "[\"hello\", null]";
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String[].class);
        deserializer.deserialize(JsonFactory.instance.createParser(json), new TestContext());
    }

    @Test
    public void testDeserializeEmbeddedObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        String json = "{\"value\": 123}";
        ObjectContext context = new ObjectContext();
        context.setContext(new TestContext());
        StdScalarDeserializer deserializer = new StdScalarDeserializer(Integer.class) {
            @Override
            protected Integer _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return super._deserialize(value, context);
            }
        };
        JsonDeserializer<Object> embeddedObjectDeserializer = new FromStringDeserializer.Std(Object.class, 0) {
            @Override
            protected Object _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return mapper.readValue(JsonFactory.instance.createParser(value), Map.class).get("value");
            }
        };
        mapper.setAnnotationIntrospector(new AnnotationIntrospector() {
            @Override
            public Object findValueAnnotations(Object value) {
                return Collections.singletonList(new JsonDeserialize(embeddedObjectDeserializer));
            }
        });

        Integer result = deserializer.deserialize(JsonFactory.instance.createParser(json), new TestContext());
        assertEquals(123, result.intValue());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeEmbeddedObjectWithNull() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        String json = "{\"value\": null}";
        ObjectContext context = new ObjectContext();
        context.setContext(new TestContext());
        StdScalarDeserializer deserializer = new StdScalarDeserializer(Integer.class) {
            @Override
            protected Integer _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return super._deserialize(value, context);
            }
        };
        JsonDeserializer<Object> embeddedObjectDeserializer = new FromStringDeserializer.Std(Object.class, 0) {
            @Override
            protected Object _deserialize(String value, DeserializationContext ctxt) throws IOException {
                throw new JsonMappingException("Cannot deserialize Object of type class [java.lang.Object] from String \"null\"");
            }
        };
        mapper.setAnnotationIntrospector(new AnnotationIntrospector() {
            @Override
            public Object findValueAnnotations(Object value) {
                return Collections.singletonList(new JsonDeserialize(embeddedObjectDeserializer));
            }
        });

        Integer result = deserializer.deserialize(JsonFactory.instance.createParser(json), new TestContext());
    }

    @Test
    public void testDeserializeWithEmptyArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        String json = "[]";
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(List.class);
        List<?> result = deserializer.deserialize(JsonFactory.instance.createParser(json), new TestContext());
        assertTrue(result.isEmpty());
    }

    @Test
    public void testDeserializeWithEmptyObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        String json = "{}";
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Map.class);
        Map<String, Object> result = deserializer.deserialize(JsonFactory.instance.createParser(json), new TestContext());
        assertTrue(result.isEmpty());
    }

    @Test
    public void testDeserializeWithCustomClass() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        String json = "{\"value\": 123}";
        ObjectContext context = new ObjectContext();
        context.setContext(new TestContext());
        StdScalarDeserializer deserializer = new StdScalarDeserializer(MyCustomClass.class) {
            @Override
            protected MyCustomClass _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return new MyCustomClass(Integer.parseInt(value));
            }
        };

        MyCustomClass result = deserializer.deserialize(JsonFactory.instance.createParser(json), new TestContext());
        assertEquals(123, result.getValue().intValue());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithInvalidJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        String json = "InvalidJson";
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class);
        deserializer.deserialize(JsonFactory.instance.createParser(json), new TestContext());
    }

    private String mapStringToJson(Class<?> type) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json;
        if (type.isPrimitive()) {
            switch (type.getName()) {
                case "int":
                    json = mapper.writeValueAsString(1);
                    break;
                case "boolean":
                    json = mapper.writeValueAsString(true);
                    break;
                case "float":
                    json = mapper.writeValueAsString(1.0f);
                    break;
                case "double":
                    json = mapper.writeValueAsString(1.0);
                    break;
                case "char":
                    json = mapper.writeValueAsString((char) 0);
                    break;
                case "byte":
                    json = mapper.writeValueAsString((byte) 0);
                    break;
                case "short":
                    json = mapper.writeValueAsString((short) 0);
                    break;
                case "long":
                    json = mapper.writeValueAsString(0L);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported primitive type: " + type.getName());
            }
        } else {
            json = mapper.writeValueAsString(new Object());
        }

        return '{' + json.substring(1) + '}'; // remove leading '{'
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public ObjectMapper getConfig() {
            return new ObjectMapper();
        }

        @Override
        public JavaType getTypeFactory() {
            return new ObjectMapper().getTypeFactory();
        }

        @Override
        public ObjectReader getRootValueFactory() {
            return JsonFactory.instance;
        }

        @Override
        public ObjectReader getValueTypeFactory() {
            return new ObjectMapper().getTypeFactory();
        }

        @Override
        public ObjectReader getValueInstantiator() {
            return new ObjectMapper().getDefaultInstance().getFactory();
        }

        @Override
        public Date parseDate(String configDateFormat, String dateString) throws ParseException {
            return new Date(); // for testing only
        }

        @Override
        public Date parseDate(String dateString) throws ParseException {
            return new Date(); // for testing only
        }

        @Override
        public ObjectReader _constructValueReader(JavaType type) {
            return new ObjectMapper().getFactory().valueReaderFor(type);
        }

        @Override
        public ObjectReader _constructValueReader(JavaType type, DeserializationFeature feature) {
            return new ObjectMapper().getFactory().valueReaderFor(type);
        }
    }

    private static class MyCustomClass {
        private int value;

        public MyCustomClass(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}