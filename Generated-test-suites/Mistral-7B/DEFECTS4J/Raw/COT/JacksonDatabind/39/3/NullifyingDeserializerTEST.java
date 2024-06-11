package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Test;

/**
 * Test cases for NullifyingDeserializer class
 */
public class NullifyingDeserializerTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testDeserialize_NullifyingDeserializer() throws Exception {
        JsonParser jsonParser = objectMapper.getFactory().createParser("{ \"name\": \"John\" }");
        DeserializationContext context = objectMapper.getDeserializationContext();
        TypeDeserializer typeDeserializer = context.getDefaultTyping();

        Object result = new NullifyingDeserializer().deserialize(jsonParser, context);
        assertNull(result);
    }

    @Test
    public void testDeserializeWithType_NullifyingDeserializer() throws Exception {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[]");
        DeserializationContext context = objectMapper.getDeserializationContext();
        TypeDeserializer typeDeserializer = context.getDefaultTyping();

        // Test with null typeDeserializer
        assertNull(new NullifyingDeserializer().deserializeWithType(jsonParser, context, null));

        // Test with valid typeDeserializer
        Object result = new NullifyingDeserializer().deserializeWithType(jsonParser, context, typeDeserializer);
        assertNull(result);
    }

    @Test
    public void testDeserializeWithType_NullifyingDeserializer_Array() throws Exception {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[1,2,3]");
        DeserializationContext context = objectMapper.getDeserializationContext();
        TypeDeserializer typeDeserializer = context.getTypeDeserializerForArray(int[].class);

        Object result = new NullifyingDeserializer().deserializeWithType(jsonParser, context, typeDeserializer);
        assertNull(result);
    }

    @Test
    public void testDeserializeWithType_NullifyingDeserializer_Object() throws Exception {
        JsonParser jsonParser = objectMapper.getFactory().createParser("{\"name\": \"John\", \"age\": 30}");
        DeserializationContext context = objectMapper.getDeserializationContext();
        TypeDeserializer typeDeserializer = context.getTypeDeserializerForBean(Person.class);

        Object result = new NullifyingDeserializer().deserializeWithType(jsonParser, context, typeDeserializer);
        assertNull(result);
    }

    @Test(expected = JsonMappingException.ClassMismatch.class)
    public void testDeserializeWithType_NullifyingDeserializer_InvalidType() throws Exception {
        JsonParser jsonParser = objectMapper.getFactory().createParser("{\"name\": \"John\", \"age\": 30}");
        DeserializationContext context = objectMapper.getDeserializationContext();
        TypeDeserializer typeDeserializer = context.getTypeDeserializerForBean(String.class);

        new NullifyingDeserializer().deserializeWithType(jsonParser, context, typeDeserializer);
    }

    @Test
    public void testDeserialize_NullParser() throws Exception {
        DeserializationContext context = objectMapper.getDeserializationContext();
        assertNull(new NullifyingDeserializer().deserialize(null, context));
    }

    @Test
    public void testDeserialize_NullContext() throws Exception {
        JsonParser jsonParser = objectMapper.getFactory().createParser("{ \"name\": \"John\" }");
        assertNull(new NullifyingDeserializer().deserialize(jsonParser, null));
    }

    @Test
    public void testDeserialize_EmptyString() throws Exception {
        JsonParser jsonParser = objectMapper.getFactory().createParser(" ");
        DeserializationContext context = objectMapper.getDeserializationContext();

        Object result = new NullifyingDeserializer().deserialize(jsonParser, context);
        assertNull(result);
    }

    @Test
    public void testDeserialize_NullObject() throws Exception {
        JsonParser jsonParser = objectMapper.getFactory().createParser("null");
        DeserializationContext context = objectMapper.getDeserializationContext();

        Object result = new NullifyingDeserializer().deserialize(jsonParser, context);
        assertNull(result);
    }

    @Test
    public void testDeserialize_InvalidJson() throws Exception {
        JsonParser jsonParser = objectMapper.getFactory().createParser("{ \"name\": \"John\" }}");
        DeserializationContext context = objectMapper.getDeserializationContext();

        Object result = new NullifyingDeserializer().deserialize(jsonParser, context);
        assertNull(result);
    }

    static class Person {
        private String name;
        private int age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}