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
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

@JacksonStdImpl
public class CollectionDeserializer
        extends ContainerDeserializerBase<Collection<Object>>
        implements ContextualDeserializer
{
    // Existing code...

    /**
     * Test for deserializing an empty collection from JSON array
     */
    @Test
    public void testDeserializeEmptyArray() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser p = mapper.getFactory().createParser("[]");
        CollectionDeserializer deserializer = new CollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, Integer.class),
                null, null, null, null, null);
        List<Integer> list = deserializer.deserialize(p, null);
        assertEquals(Collections.emptyList(), list);
    }

    /**
     * Test for deserializing a single value taken as an array
     */
    @Test
    public void testDeserializeSingleValueAsArray() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser p = mapper.getFactory().createParser("[1]");
        CollectionDeserializer deserializer = new CollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, Integer.class),
                null, null, null, null, null);
        List<Integer> list = deserializer.deserialize(p, null);
        assertEquals(Collections.singletonList(1), list);
    }

    /**
     * Test for deserializing a JSON array with multiple values
     */
    @Test
    public void testDeserializeMultiValues() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser p = mapper.getFactory().createParser("[1, 2, 3]");
        CollectionDeserializer deserializer = new CollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, Integer.class),
                null, null, null, null, null);
        List<Integer> list = deserializer.deserialize(p, null);
        assertEquals(Arrays.asList(1, 2, 3), list);
    }

    /**
     * Test for deserializing a JSON array with null values
     */
    @Test
    public void testDeserializeNullValues() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser p = mapper.getFactory().createParser("[null, 1, 2, null]");
        CollectionDeserializer deserializer = new CollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, Integer.class),
                null, null, null, null, null);
        List<Integer> list = deserializer.deserialize(p, null);
        assertEquals(Arrays.asList(null, 1, 2, null), list);
    }

    /**
     * Test for deserializing a JSON array with unresolved forward references
     */
    @Test
    public void testDeserializeUnresolvedForwardReference() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser p = mapper.getFactory().createParser("[" + JsonToken.VALUE_STRING.asString() + ", []]");
        CollectionDeserializer deserializer = new CollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);

        try {
            List<String> list = deserializer.deserialize(p, null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }

        Object id = new Object();
        deserializer = new CollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);
        deserializer.handleUnresolvedForwardReference(id, "value");

        JsonParser p2 = mapper.getFactory().createParser("[\"value\", []]");
        List<String> list = deserializer.deserialize(p2, null);
        assertEquals(Arrays.asList("value", Collections.emptyList()), list);
    }

    /**
     * Test for deserializing a JSON array with delegated creators
     */
    @Test
    public void testDeserializeWithDelegatedCreator() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser p = mapper.getFactory().createParser("[1, 2, 3]");
        ValueInstantiator valueInstantiator = mapper.getPolymorphicValueInstantiator();

        CollectionDeserializer deserializer = new CollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, Integer.class),
                null, null, valueInstantiator, null, null);
        List<Integer> list = deserializer.deserialize(p, null);
        assertEquals(Arrays.asList(1, 2, 3), list);
    }
}