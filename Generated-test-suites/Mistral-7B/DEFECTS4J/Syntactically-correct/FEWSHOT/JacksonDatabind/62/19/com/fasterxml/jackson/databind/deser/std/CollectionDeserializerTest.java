package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.deser.std.CollectionDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TestBase;

/**
 * Test cases for {@link CollectionDeserializer} class.
 */
@JacksonStdImpl
public class CollectionDeserializerTest extends TestBase {

    @Test
    public void testEmptyCollectionDeserialization() throws Exception {
        JavaType javaType = mapType(List.class, Object.class);
        JsonDeserializer<Object> valueDeserializer = createValueDeserializer();
        TypeDeserializer valueTypeDeserializer = createTypeDeserializer();
        CollectionDeserializer deserializer = new CollectionDeserializer(javaType, valueDeserializer, valueTypeDeserializer, null);

        ObjectReader reader = new MinimalObjectMapper().readerForValueType(javaType);
        Object readValue = reader.readValue(createContext(), new JsonString("[]"));
        assertEquals(Collections.emptyList(), readValue);
    }

    @Test
    public void testCollectionDeserializationWithValueDeserializer() throws Exception {
        JavaType javaType = mapType(List.class, Integer.class);
        JsonDeserializer<Integer> valueDeserializer = createValueDeserializer();
        TypeDeserializer valueTypeDeserializer = createTypeDeserializer();
        CollectionDeserializer deserializer = new CollectionDeserializer(javaType, valueDeserializer, valueTypeDeserializer, null);

        ObjectReader reader = new MinimalObjectMapper().readerForValueType(javaType);
        Object readValue = reader.readValue(createContext(), new JsonString("[1, 2, 3]"));
        List<Integer> expected = Arrays.asList(1, 2, 3);
        assertEquals(expected, readValue);
    }

    @Test
    public void testCollectionDeserializationWithTypeDeserializer() throws Exception {
        JavaType javaType = mapType(List.class, String.class);
        JsonDeserializer<String> valueDeserializer = createValueDeserializer();
        TypeDeserializer valueTypeDeserializer = new StringTypeDeserializer();
        CollectionDeserializer deserializer = new CollectionDeserializer(javaType, valueDeserializer, valueTypeDeserializer, null);

        ObjectReader reader = new MinimalObjectMapper().readerForValueType(javaType);
        Object readValue = reader.readValue(createContext(), new JsonString("[\"foo\", \"bar\"]"));
        List<String> expected = Arrays.asList("foo", "bar");
        assertEquals(expected, readValue);
    }

    @Test
    public void testCollectionDeserializationWithDelegateDeserializer() throws Exception {
        JavaType javaType = mapType(List.class, String.class);
        JsonDeserializer<String> valueDeserializer = createValueDeserializer();
        TypeDeserializer valueTypeDeserializer = createTypeDeserializer();
        JsonDeserializer<List<String>> delegateDeserializer = new CollectionDeserializer(javaType, valueDeserializer, valueTypeDeserializer, null);
        ValueInstantiator valueInstantiator = new StdValueInstantiator();
        CollectionDeserializer deserializer = new CollectionDeserializer(javaType, null, null, delegateDeserializer, null);

        ObjectReader reader = new MinimalObjectMapper().readerForValueType(javaType);
        Object readValue = reader.readValue(createContext(), new JsonString("[\"foo\", \"bar\"]"));
        List<String> expected = Arrays.asList("foo", "bar");
        assertEquals(expected, readValue);
    }

    @Test
    public void testCollectionDeserializationWithUnwrappedSingle() throws Exception {
        JavaType javaType = mapType(List.class, Integer.class);
        JsonDeserializer<Integer> valueDeserializer = createValueDeserializer();
        TypeDeserializer valueTypeDeserializer = createTypeDeserializer();
        CollectionDeserializer deserializer = new CollectionDeserializer(javaType, valueDeserializer, valueTypeDeserializer, null, null, Boolean.TRUE);

        ObjectReader reader = new MinimalObjectMapper().readerForValueType(javaType);
        Object readValue = reader.readValue(createContext(), new JsonString("1"));
        List<Integer> expected = Arrays.asList(1);
        assertEquals(expected, readValue);
    }

    @Test(expected = JsonMappingException.class)
    public void testCollectionDeserializationWithNonArray() throws Exception {
        JavaType javaType = mapType(List.class, Integer.class);
        JsonDeserializer<Integer> valueDeserializer = createValueDeserializer();
        TypeDeserializer valueTypeDeserializer = createTypeDeserializer();
        CollectionDeserializer deserializer = new CollectionDeserializer(javaType, valueDeserializer, valueTypeDeserializer, null);

        ObjectReader reader = new MinimalObjectMapper().readerForValueType(javaType);
        Object readValue = reader.readValue(createContext(), new JsonString("1"));
        List<Integer> expected = Arrays.asList(1);
        // This should throw an exception as the input is not an array
        assertEquals(expected, readValue);
    }

    @Test
    public void testCollectionDeserializationWithReferencing() throws Exception {
        JavaType javaType = mapType(List.class, Integer.class);
        JsonDeserializer<Integer> valueDeserializer = createValueDeserializer();
        TypeDeserializer valueTypeDeserializer = createTypeDeserializer();
        CollectionDeserializer deserializer = new CollectionDeserializer(javaType, valueDeserializer, valueTypeDeserializer, null);

        ObjectReader reader = new MinimalObjectMapper().readerForValueType(javaType);
        Object readValue1 = reader.readValue(createContext(), new JsonString("[1, {\"$ref\": \"$0\"}]"));
        Object readValue2 = reader.readValue(createContext(), new JsonString("1"));

        List<Object> list1 = (List<Object>) readValue1;
        List<Object> list2 = (List<Object>) readValue2;

        assertEquals(Collections.singletonList(list1.get(0)), list2);
    }

    private static JsonDeserializer<Object> createValueDeserializer() {
        return new StandardDeserializer<>(Object.class);
    }

    private static TypeDeserializer createTypeDeserializer() {
        return new StandardTypeDeserializer();
    }
}