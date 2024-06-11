package com.example.test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.deser.std.ContainerDeserializerBase;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

@JacksonStdImpl
public class CollectionDeserializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();
    private CollectionDeserializer collectionDeserializer;

    @Before
    public void setup() throws JsonProcessingException {
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, Integer.class);
        JsonDeserializer<Integer> intDeserializer = mapper.deserializerForClass(Integer.class);
        collectionDeserializer = new CollectionDeserializer(javaType, intDeserializer, null, null, null, null);
    }

    // Basic test cases for each method
    @Test
    public void testCreateContextual() throws Exception {
        DeserializationContext context = createContext();
        CollectionDeserializer deserializer = collectionDeserializer.createContextual(context, null);
        assertNotNull(deserializer);
    }

    @Test
    public void testDeserialize() throws Exception {
        JsonParser parser = createParser("[]");
        Collection<Object> result = collectionDeserializer.deserialize(parser, null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testDeserializeWithType() throws Exception {
        JsonParser parser = createParser("[]");
        List<Integer> result = (List<Integer>) collectionDeserializer.deserializeWithType(parser, null, IntegerTypeDeserializer.instance);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testDeserializeWithType_SingleValue() throws Exception {
        JsonParser parser = createParser("[1]");
        List<Integer> result = (List<Integer>) collectionDeserializer.deserializeWithType(parser, null, IntegerTypeDeserializer.instance);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).intValue());
    }

    @Test
    public void testDeserializeWithType_ArrayValue() throws Exception {
        JsonParser parser = createParser("[1,2,3]");
        List<Integer> result = (List<Integer>) collectionDeserializer.deserializeWithType(parser, null, IntegerTypeDeserializer.instance);
        assertEquals(3, result.size());
        assertEquals(1, result.get(0).intValue());
        assertEquals(2, result.get(1).intValue());
        assertEquals(3, result.get(2).intValue());
    }

    @Test(expected = JsonProcessingException.class)
    public void testDeserializeWithError() throws Exception {
        JsonParser parser = createParser("\"Hello\"");
        collectionDeserializer.deserializeWithType(parser, null, IntegerTypeDeserializer.instance);
    }

    // Edge cases and exception handling scenarios tests
    @Test
    public void testDeserialize_NullInput() throws Exception {
        JsonParser parser = createParser(null);
        Collection<Object> result = collectionDeserializer.deserialize(parser, null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testDeserialize_EmptyStringInput() throws Exception {
        JsonParser parser = createParser("");
        Collection<Object> result = collectionDeserializer.deserialize(parser, null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testDeserialize_SingleValueInput() throws Exception {
        JsonParser parser = createParser("[1]");
        Collection<Object> result = collectionDeserializer.deserialize(parser, null);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).intValue());
    }

    @Test
    public void testDeserialize_ArrayInput() throws Exception {
        JsonParser parser = createParser("[1,2,3]");
        Collection<Object> result = collectionDeserializer.deserialize(parser, null);
        assertEquals(3, result.size());
        assertEquals(1, result.get(0).intValue());
        assertEquals(2, result.get(1).intValue());
        assertEquals(3, result.get(2).intValue());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserialize_NonArrayInput() throws Exception {
        JsonParser parser = createParser("\"Hello\"");
        collectionDeserializer.deserialize(parser, null);
    }

    @Test(expected = RuntimeException.class)
    public void testDeserialize_RuntimeException() throws Exception {
        JsonParser parser = createParser("[JavaException: TestException]");
        collectionDeserializer = new CollectionDeserializer(mapper.getTypeFactory().constructCollectionType(List.class, String.class),
                mapper.deserializerForClass(String.class), null, null, null, null);
        collectionDeserializer.deserialize(parser, null);
    }

    @Test
    public void testDeserialize_UnresovledForwardReference() throws Exception {
        JsonParser parser = createParser("[1,$ref]");
        DeserializationContext context = createContext();
        collectionDeserializer = new CollectionDeserializer(mapper.getTypeFactory().constructCollectionType(List.class, Integer.class),
                mapper.deserializerForClass(Integer.class), null, null, null, null);
        JsonDeserializer<List<Integer>> listDeserializer = new CollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, Integer.class),
                mapper.deserializerForClass(Integer.class), null, null,
                null, null);
        collectionDeserializer = listDeserializer.withResolved(collectionDeserializer, null, null, null);
        Collection<Object> result = collectionDeserializer.deserialize(parser, context);
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).intValue());
        assertEquals(1, result.get(1).intValue());
    }

    // Helper methods
    private JsonParser createParser(String content) throws IOException {
        return mapper.getFactory().createParser(new ByteArrayInputStream(content.getBytes()));
    }

    private DeserializationContext createContext() {
        return mapper.getDeserializationConfig().createContext();
    }

    private static class IntegerTypeDeserializer extends StdDeserializer<Integer> {
        static final IntegerTypeDeserializer instance = new IntegerTypeDeserializer();

        private IntegerTypeDeserializer() {
            this(null);
        }

        private IntegerTypeDeserializer(Class<?> vc) {
            super(Integer.class, vc);
        }

        @Override
        public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsInt();
        }
    }
}