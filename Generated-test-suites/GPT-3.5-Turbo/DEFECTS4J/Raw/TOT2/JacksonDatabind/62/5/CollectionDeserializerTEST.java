package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;

public class CollectionDeserializerTest {

    @Test
    public void testConstructor() {
        // Test constructor with context-free instances
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(JavaType.construct(Collection.class),
                new ValueDeserializer(), new TypeDeserializer(), new ValueInstantiator());
        assertNotNull(collectionDeserializer);
    }

    @Test
    public void testDeserialize() {
        // Test deserialization with empty input
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(JavaType.construct(Collection.class),
                new ValueDeserializer(), new TypeDeserializer(), new ValueInstantiator());
        JsonParser jsonParser = new JsonParser();
        DeserializationContext deserializationContext = new DeserializationContext();
        Collection<Object> result = collectionDeserializer.deserialize(jsonParser, deserializationContext);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testDeserializeWithType() {
        // Test deserialization with type information
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(JavaType.construct(Collection.class),
                new ValueDeserializer(), new TypeDeserializer(), new ValueInstantiator());
        JsonParser jsonParser = new JsonParser();
        DeserializationContext deserializationContext = new DeserializationContext();
        Object result = collectionDeserializer.deserializeWithType(jsonParser, deserializationContext, new TypeDeserializer());
        assertNotNull(result);
    }

    @Test
    public void testHandleNonArray() {
        // Test handling non-array input
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(JavaType.construct(Collection.class),
                new ValueDeserializer(), new TypeDeserializer(), new ValueInstantiator());
        JsonParser jsonParser = new JsonParser();
        DeserializationContext deserializationContext = new DeserializationContext();
        Collection<Object> result = collectionDeserializer.handleNonArray(jsonParser, deserializationContext, new ArrayList<>());
        assertNotNull(result);
        assertTrue(result.size() == 1);
    }

}