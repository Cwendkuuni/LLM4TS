package com.example;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

public class CollectionDeserializerTest {

    @Test
    public void testCreateContextual() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        JsonDeserializer<String> stringDeserializer = mapper.findValueDeserializer(String.class);
        JsonDeserializer<List<String>> collectionDeserializer = new CollectionDeserializer(javaType, stringDeserializer, null, null, null, null);

        BeanProperty property = new BeanProperty("testProperty", javaType, null);
        CollectionDeserializer contextualDeserializer = collectionDeserializer.createContextual(mockContext(), property);

        assertEquals(javaType, contextualDeserializer.getContentType());
        assertEquals(stringDeserializer, contextualDeserializer.getContentDeserializer());
        assertNull(contextualDeserializer.getDelegateDeserializer());
        assertNull(contextualDeserializer.getValueTypeDeserializer());
        assertNull(contextualDeserializer.getValueInstantiator());
        assertEquals(null, contextualDeserializer.unwrapSingle);
    }

    @Test
    public void testDeserializeWithDefaultParameters() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        JsonDeserializer<String> stringDeserializer = mapper.findValueDeserializer(String.class);
        JsonDeserializer<List<String>> collectionDeserializer = new CollectionDeserializer(javaType, stringDeserializer, null, null, null, null);

        JsonParser jsonParser = mockParser();
        DeserializationContext context = mockContext();

        List<String> result = new ArrayList<>();
        CollectionDeserializer deserializer = collectionDeserializer.withResolved(null, stringDeserializer, null);
        Collection<Object> deserializedCollection = deserializer.deserialize(jsonParser, context);

        assertEquals(javaType, deserializer.getContentType());
        assertEquals(stringDeserializer, deserializer.getContentDeserializer());
        assertNull(deserializer.getDelegateDeserializer());
        assertNull(deserializer.getValueTypeDeserializer());
        assertNull(deserializer.getValueInstantiator());
        assertNull(deserializer.unwrapSingle);

        assertEquals(javaType, deserializer.getContentType());
        assertEquals(List.class, deserializer.getType());
        assertNotNull(deserializer.getContentDeserializer());
        assertEquals(String.class, deserializer.getContentDeserializer().getType());

        assertTrue(deserializedCollection instanceof ArrayList);
        assertEquals(0, deserializedCollection.size());
    }

    @Test
    public void testDeserializeWithDelegateDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, Integer.class);
        JsonDeserializer<Integer> integerDeserializer = mapper.findValueDeserializer(Integer.class);
        JsonDeserializer<List<Integer>> collectionDeserializer = new CollectionDeserializer(javaType, null, null, null, integerDeserializer, null);

        JsonParser jsonParser = mockParser();
        DeserializationContext context = mockContext();
        JsonDeserializer<String> stringDeserializer = mapper.findValueDeserializer(String.class);

        List<Integer> result = new ArrayList<>();
        CollectionDeserializer deserializer = collectionDeserializer.withResolved(stringDeserializer, integerDeserializer, null);
        Collection<Object> deserializedCollection = deserializer.deserialize(jsonParser, context);

        assertEquals(javaType, deserializer.getContentType());
        assertEquals(stringDeserializer, deserializer.getContentDeserializer());
        assertEquals(integerDeserializer, deserializer.getDelegateDeserializer());
        assertNull(deserializer.getValueTypeDeserializer());
        assertNull(deserializer.getValueInstantiator());
        assertNull(deserializer.unwrapSingle);

        assertTrue(deserializedCollection instanceof ArrayList);
        assertEquals(0, deserializedCollection.size());

        JsonParser delegateParser = mockParser();
        JsonDeserializer<List<Integer>> deserializerWithDelegate = collectionDeserializer.withResolved(null, null, null, integerDeserializer);
        Collection<Object> deserializedCollectionWithDelegate = deserializerWithDelegate.deserialize(delegateParser, context);

        assertEquals(javaType, deserializerWithDelegate.getContentType());
        assertEquals(String.class, deserializerWithDelegate.getType());
        assertNotNull(deserializerWithDelegate.getContentDeserializer());
        assertEquals(Integer.class, deserializerWithDelegate.getContentDeserializer().getType());
        assertNotNull(deserializerWithDelegate.getDelegateDeserializer());
        assertEquals(integerDeserializer, deserializerWithDelegate.getDelegateDeserializer());

        assertEquals(0, deserializedCollectionWithDelegate.size());

        JsonParser jsonParserWithData = mockParserWithData("[1,2,3]");
        JsonDeserializer<List<Integer>> deserializerWithData = collectionDeserializer.withResolved(null, null, null, integerDeserializer);
        Collection<Object> deserializedCollectionWithData = deserializerWithData.deserialize(jsonParserWithData, context);

        assertEquals(javaType, deserializerWithData.getContentType());
        assertEquals(List.class, deserializerWithData.getType());
        assertNotNull(deserializerWithData.getContentDeserializer());
        assertEquals(Integer.class, deserializerWithData.getContentDeserializer().getType());
        assertNotNull(deserializerWithData.getDelegateDeserializer());
        assertEquals(integerDeserializer, deserializerWithData.getDelegateDeserializer());

        assertEquals(3, deserializedCollectionWithData.size());
        assertEquals(1, ((List<Integer>) deserializedCollectionWithData).get(0));
        assertEquals(2, ((List<Integer>) deserializedCollectionWithData).get(1));
        assertEquals(3, ((List<Integer>) deserializedCollectionWithData).get(2));
    }

    @Test
    public void testDeserializeWithValueTypeDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        JsonDeserializer<String> stringDeserializer = mapper.findValueDeserializer(String.class);
        JsonDeserializer<List<String>> collectionDeserializer = new CollectionDeserializer(javaType, stringDeserializer, new BaseJsonTypeDeserializer(), null);

        JsonParser jsonParser = mockParser();
        DeserializationContext context = mockContext();

        List<String> result = new ArrayList<>();
        CollectionDeserializer deserializer = collectionDeserializer.withResolved(null, stringDeserializer, new BaseJsonTypeDeserializer());
        Collection<Object> deserializedCollection = deserializer.deserialize(jsonParser, context);

        assertEquals(javaType, deserializer.getContentType());
        assertEquals(String.class, deserializer.getContentDeserializer().getType());
        assertNotNull(deserializer.getValueTypeDeserializer());
        assertEquals(BaseJsonTypeDeserializer.class, deserializer.getValueTypeDeserializer().getClass());

        assertTrue(deserializedCollection instanceof ArrayList);
        assertEquals(0, deserializedCollection.size());

        JsonParser jsonParserWithData = mockParserWithData("[1, 2.0, 3.5]");
        JsonDeserializer<List<Number>> numberDeserializer = mapper.findValueDeserializer(List.class);
        JsonDeserializer<Number> numberDeserializerWithType = mapper.findValueDeserializer(Number.class);
        TypeDeserializer valueTypeDeserializer = new BaseJsonTypeDeserializer();
        CollectionDeserializer deserializerWithType = new CollectionDeserializer(javaType, stringDeserializer, valueTypeDeserializer, null);
        Collection<Object> deserializedCollectionWithType = deserializerWithType.deserialize(jsonParserWithData, context);

        assertEquals(javaType, deserializerWithType.getContentType());
        assertEquals(List.class, deserializerWithType.getType());
        assertNotNull(deserializerWithType.getContentDeserializer());
        assertEquals(Number.class, deserializerWithType.getContentDeserializer().getType());
        assertNotNull(deserializerWithType.getValueTypeDeserializer());
        assertEquals(valueTypeDeserializer, deserializerWithType.getValueTypeDeserializer());

        assertEquals(3, deserializedCollectionWithType.size());
        assertTrue(deserializedCollectionWithType.get(0) instanceof Integer);
        assertTrue(deserializedCollectionWithType.get(1) instanceof Double);
        assertTrue(deserializedCollectionWithType.get(2) instanceof Double);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithInvalidInput() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        JsonDeserializer<String> stringDeserializer = mapper.findValueDeserializer(String.class);
        JsonDeserializer<List<String>> collectionDeserializer = new CollectionDeserializer(javaType, stringDeserializer, null, null, null, null);

        JsonParser jsonParser = mockParser();
        DeserializationContext context = mockContext();

        List<String> result = new ArrayList<>();
        CollectionDeserializer deserializer = collectionDeserializer.withResolved(null, stringDeserializer, null);
        Collection<Object> deserializedCollection = deserializer.deserialize(jsonParser, context);

        assertEquals(javaType, deserializer.getContentType());
        assertEquals(String.class, deserializer.getContentDeserializer().getType());
        assertNull(deserializer.getDelegateDeserializer());
        assertNull(deserializer.getValueTypeDeserializer());
        assertNull(deserializer.getValueInstantiator());
        assertNull(deserializer.unwrapSingle);

        assertEquals(javaType, deserializer.getContentType());
        assertEquals(List.class, deserializer.getType());
        assertNotNull(deserializer.getContentDeserializer());
        assertEquals(String.class, deserializer.getContentDeserializer().getType());

        // Invalid input: String instead of array
        jsonParser.nextToken();

        try {
            deserializer.deserialize(jsonParser, context);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Expected a JSON Array but was a JSON String instead", e.getMessage());
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithUnwrappingSingleValueAsArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        JsonDeserializer<String> stringDeserializer = mapper.findValueDeserializer(String.class);
        JsonDeserializer<List<String>> collectionDeserializer = new CollectionDeserializer(javaType, stringDeserializer, null, null, null, Boolean.TRUE);

        JsonParser jsonParser = mockParserWithData("\"test\"");
        DeserializationContext context = mockContext();

        try {
            CollectionDeserializer deserializer = collectionDeserializer.withResolved(null, stringDeserializer, null);
            Collection<Object> deserializedCollection = deserializer.deserialize(jsonParser, context);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Collection of size 1 was not an instance of [java.util.List<java.lang.String>], can not unwrap single value as array", e.getMessage());
        }
    }

    
}