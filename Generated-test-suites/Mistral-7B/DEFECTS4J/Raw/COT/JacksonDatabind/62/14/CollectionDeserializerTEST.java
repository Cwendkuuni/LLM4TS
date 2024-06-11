package com.example.junit4;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.deser.ContainerDeserializerBase;
import com.fasterxml.jackson.databind.deser.ValueDeserializerProvider;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.deser.std.ContainerDeserializerBase.CollectionDeserializer;
import com.fasterxml.jackson.databind.deser.std.ContainerDeserializerBase.CollectionReferring;
import com.fasterxml.jackson.databind.deser.std.CollectionDeserializer.CollectionReferringAccumulator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.StdSerializer;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.util.*;
import static org.junit.Assert.*;

public class CollectionDeserializerTest {

    private CollectionDeserializer deserializer;
    private ObjectMapper objectMapper;

    @Before
    public void setup() throws Exception {
        JavaType collectionType = TypeFactory.defaultInstance().constructCollectionType(List.class, Object.class);
        JsonDeserializer<Object> valueDeserializer = new StdDeserializer<>() {};
        TypeDeserializer valueTypeDeserializer = null;
        ValueDeserializerProvider valueDeserializerProvider = new ValueDeserializerProvider();
        valueDeserializerProvider.setValueDeserializers(Collections.singletonMap(Object.class, valueDeserializer));
        if (valueTypeDeserializer != null) {
            valueDeserializerProvider.setTypeDeserializers(Collections.singletonMap(Object.class, valueTypeDeserializer));
        }
        deserializer = new CollectionDeserializer(collectionType, valueDeserializer, valueTypeDeserializer, null, null, null, valueDeserializerProvider);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testEmptyCollection() throws Exception {
        String json = "[]";
        List<Object> expected = new ArrayList<>();
        Object result = deserializer.deserialize(objectMapper.readerForListOf(Object.class), new TestContext());
        assertEquals(expected, result);
    }

    @Test
    public void testSingleElementCollection() throws Exception {
        String json = "[1]";
        Object expected = 1;
        List<Object> result = deserializer.deserialize(objectMapper.readerForListOf(Object.class), new TestContext());
        assertEquals(Collections.singletonList(expected), result);
    }

    @Test
    public void testSingleNullElementCollection() throws Exception {
        String json = "[null]";
        Object expected = null;
        List<Object> result = deserializer.deserialize(objectMapper.readerForListOf(Object.class), new TestContext());
        assertEquals(Collections.singletonList(expected), result);
    }

    @Test
    public void testMultipleElementCollection() throws Exception {
        String json = "[1, 2, 3]";
        List<Object> expected = Arrays.asList(1, 2, 3);
        List<Object> result = deserializer.deserialize(objectMapper.readerForListOf(Object.class), new TestContext());
        assertEquals(expected, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidJson() throws Exception {
        String json = "invalidJson";
        deserializer.deserialize(objectMapper.readerForListOf(Object.class), new TestContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testNullJson() throws Exception {
        String json = null;
        deserializer.deserialize(objectMapper.readerForListOf(Object.class), new TestContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testNullValue() throws Exception {
        String json = "[null]";
        deserializer.deserialize(objectMapper.readerForListOf(String.class), new TestContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testIncorrectTypeJson() throws Exception {
        String json = "[true]";
        deserializer.deserialize(objectMapper.readerForListOf(Integer.class), new TestContext());
    }

    @Test
    public void testValueDeserializerNull() throws Exception {
        String json = "[null]";
        CollectionDeserializer deserializerWithNullValueDeserializer = new CollectionDeserializer(collectionType, null, null, null, null, null, valueDeserializerProvider);
        List<Object> result = deserializerWithNullValueDeserializer.deserialize(objectMapper.readerForListOf(Object.class), new TestContext());
        assertTrue(result.isEmpty());
    }

    @Test(expected = JsonMappingException.class)
    public void testValueDeserializerThrowsException() throws Exception {
        String json = "[\"test\"]";
        JsonDeserializer<Object> exceptionThrowingValueDeserializer = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                throw new IOException("Test Exception");
            }
        };
        CollectionDeserializer deserializerWithExceptionThrowingValueDeserializer = new CollectionDeserializer(collectionType, exceptionThrowingValueDeserializer, null, null, null, null, valueDeserializerProvider);
        deserializerWithExceptionThrowingValueDeserializer.deserialize(objectMapper.readerForListOf(Object.class), new TestContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testValueDeserializerTypeMismatch() throws Exception {
        String json = "[1]";
        JsonDeserializer<Integer> integerValueDeserializer = new StdDeserializer<Integer>() {};
        CollectionDeserializer deserializerWithIntegerValueDeserializer = new CollectionDeserializer(collectionType, integerValueDeserializer, null, null, null, null, valueDeserializerProvider);
        deserializerWithIntegerValueDeserializer.deserialize(objectMapper.readerForListOf(Object.class), new TestContext());
    }

    @Test
    public void testValueDeserializerTypeDeserializer() throws Exception {
        String json = "[1]";
        TypeDeserializer integerTypeDeserializer = new StdTypeSerializer<Integer>() {};
        CollectionDeserializer deserializerWithIntegerTypeDeserializer = new CollectionDeserializer(collectionType, valueDeserializer, integerTypeDeserializer, null, null, null, valueDeserializerProvider);
        List<Object> result = deserializerWithIntegerTypeDeserializer.deserialize(objectMapper.readerForListOf(Object.class), new TestContext());
        assertEquals(Arrays.asList(1), result);
    }

    @Test(expected = JsonMappingException.class)
    public void testValueDeserializerWithReferring() throws Exception {
        String json = "[{\"@ref\": 1}]";
        CollectionDeserializer deserializerWithValueDeserializerWithReferring = new CollectionDeserializer(collectionType, valueDeserializer, null, null, null, null, valueDeserializerProvider);
        deserializerWithValueDeserializerWithReferring.deserialize(objectMapper.readerForListOf(Object.class), new TestContext());
    }

    @Test
    public void testValueDeserializerWithReferringResolution() throws Exception {
        String json = "[{\"@ref\": 1}, 1]";
        ObjectNode node = objectMapper.readValue(json, ObjectNode.class);
        ObjectIdReader idReader = new ObjectIdReader();
        CollectionDeserializer deserializerWithValueDeserializerWithReferring = new CollectionDeserializer(collectionType, valueDeserializer, null, null, null, null, valueDeserializerProvider);
        List<Object> result = deserializerWithValueDeserializerWithReferring.deserialize(objectMapper.readerForListOf(Object.class), new TestContextWithObjectIdReader(idReader, node));
        assertEquals(Arrays.asList(1, 1), result);
    }

    @Test(expected = JsonMappingException.class)
    public void testValueDeserializerWithUnresolvedForwardReference() throws Exception {
        String json = "[{\"@ref\": 1}]";
        CollectionDeserializer deserializerWithValueDeserializerWithUnresolvedForwardReference = new CollectionDeserializer(collectionType, valueDeserializer, null, null, null, null, valueDeserializerProvider);
        UnresolvedForwardReference reference = new UnresolvedForwardReference();
        deserializerWithValueDeserializerWithUnresolvedForwardReference.handleUnresolvedForwardReference(reference, 1);
    }

    private static class TestContext extends BaseTestContext {
    }

    private static class TestContextWithObjectIdReader extends TestContext {
        private final ObjectIdReader idReader;
        private final ObjectNode node;

        public TestContextWithObjectIdReader(ObjectIdReader idReader, ObjectNode node) {
            this.idReader = idReader;
            this.node = node;
        }

        @Override
        public ObjectIdReader getObjectIdReader() {
            return idReader;
        }

        @Override
        public ObjectNode getObjectNode() {
            return node;
        }
    }

    private static class BaseTestContext extends StdSerializer<Object> {
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return ctxt.emptyValueProvider().getValue(Object.class);
        }
    }
}