package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JsonValueSerializerTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testSerializeNull() throws JsonProcessingException {
        JsonValueSerializer serializer = new JsonValueSerializer(null, null, null, false);
        serializer.serialize(null, mapper.getFactory().createGenerator(new StringWriter()), mapper.getSerializerProvider());
        mapper.getFactory().getJsonParser().parse("null"); // verify parsing reads null
    }

    @Test
    public void testSerializeBoolean() throws JsonProcessingException {
        JsonValueSerializer serializer = new JsonValueSerializer(Boolean.TYPE.getDeclaredMethod("booleanValue"), null, null, false);
        Object bean = Boolean.FALSE;
        serializer.serialize(bean, mapper.getFactory().createGenerator(new StringWriter()), mapper.getSerializerProvider());
        assertEquals("false", mapper.readTree(new StringReader(serializer.toString())).asText());

        bean = Boolean.TRUE;
        serializer.serialize(bean, mapper.getFactory().createGenerator(new StringWriter()), mapper.getSerializerProvider());
        assertEquals("true", mapper.readTree(new StringReader(serializer.toString())).asText());
    }

    @Test
    public void testSerializeInteger() throws JsonProcessingException {
        JsonValueSerializer serializer = new JsonValueSerializer(Integer.TYPE.getDeclaredMethod("intValue"), null, null, false);
        Object bean = 123;
        serializer.serialize(bean, mapper.getFactory().createGenerator(new StringWriter()), mapper.getSerializerProvider());
        assertEquals("123", mapper.readTree(new StringReader(serializer.toString())).asText());
    }

    @Test
    public void testSerializeString() throws JsonProcessingException {
        JsonValueSerializer serializer = new JsonValueSerializer(String.class.getDeclaredMethod("getValue"), null, null, false);
        Object bean = "hello";
        serializer.serialize(bean, mapper.getFactory().createGenerator(new StringWriter()), mapper.getSerializerProvider());
        assertEquals("\"hello\"", mapper.readTree(new StringReader(serializer.toString())).asText());
    }

    @Test
    public void testSerializeStringWithType() throws JsonProcessingException {
        JsonValueSerializer serializer = new JsonValueSerializer(String.class.getDeclaredMethod("getValue"), null, null, true);
        Object bean = "hello";
        SerializerProvider prov = mapper.getSerializerProvider();
        TypeSerializer typeSer = prov.findValueSerializer(String.class, null);

        serializer.serializeWithType(bean, mapper.getFactory().createGenerator(new StringWriter()), prov, typeSer);
        String json = mapper.readTree(new StringReader(serializer.toString())).asText();
        assertNull(json.substring(json.length() - 30, json.length())); // no type id
        assertEquals("\"hello\"", json.substring(0, json.length() - 2));
    }

    @Test
    public void testSerializeCustomType() throws JsonProcessingException {
        class MyClass {
            @JsonValue
            public int getValue() {
                return 42;
            }
        }

        JsonValueSerializer serializer = new JsonValueSerializer(MyClass.class.getDeclaredMethod("getValue"), null, null, false);
        Object bean = new MyClass();
        serializer.serialize(bean, mapper.getFactory().createGenerator(new StringWriter()), mapper.getSerializerProvider());
        assertEquals("42", mapper.readTree(new StringReader(serializer.toString())).asText());
    }

    @Test
    public void testSerializeCustomTypeWithType() throws JsonProcessingException {
        class MyClass {
            @JsonValue
            public int getValue() {
                return 42;
            }
        }

        JsonValueSerializer serializer = new JsonValueSerializer(MyClass.class.getDeclaredMethod("getValue"), null, null, true);
        Object bean = new MyClass();
        SerializerProvider prov = mapper.getSerializerProvider();
        TypeSerializer typeSer = prov.findValueSerializer(MyClass.class, null);

        serializer.serializeWithType(bean, mapper.getFactory().createGenerator(new StringWriter()), prov, typeSer);
        String json = mapper.readTree(new StringReader(serializer.toString())).asText();
        assertEquals("42", json);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeException() throws JsonProcessingException {
        JsonValueSerializer serializer = new JsonValueSerializer(new Throwable("test").getClass().getDeclaredMethod("toString"), null, null, false);
        Object bean = new Throwable("test");
        serializer.serialize(bean, mapper.getFactory().createGenerator(new StringWriter()), mapper.getSerializerProvider());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonProcessingException {
        JsonValueSerializer serializer = new JsonValueSerializer(String.class.getDeclaredMethod("getValue"), null, null, false);
        SerializerProvider prov = mapper.getSerializerProvider();
        JsonFormatVisitorWrapper visitor = prov.findValueSerializerProvider().getSchemaVisitorProvider().getSchemaVisitor(null);
        serializer.acceptJsonFormatVisitor(visitor, null);
    }

    @Test
    public void testAcceptJsonFormatVisitorForEnum() throws JsonProcessingException {
        enum TestEnum { A, B, C }

        JsonValueSerializer serializer = new JsonValueSerializer(TestEnum.class.getDeclaredMethod("name"), null, null, false);
        SerializerProvider prov = mapper.getSerializerProvider();
        JsonFormatVisitorWrapper visitor = prov.findValueSerializerProvider().getSchemaVisitorProvider().getSchemaVisitor(null);

        serializer.acceptJsonFormatVisitor(visitor, null);

        assertEquals("{\"type\": \"string\", \"items\": [\"A\", \"B\", \"C\"]}", mapper.writeValueAsString(visitor.getSchema(null)));
    }
}