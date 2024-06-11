package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CollectionDeserializerTest {

    private CollectionDeserializer collectionDeserializer;
    private Collection<Object> collection;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        JavaType contentType = objectMapper.constructType(Object.class);
        JavaType collectionType = objectMapper.getTypeFactory().constructCollectionType(Collection.class, contentType);
        collectionDeserializer = new CollectionDeserializer(collectionType, null, null, null);
        collection = new ArrayList<>();
    }

    @Test
    public void testDeserialize() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[1, 2, 3]");
        collectionDeserializer.deserialize(jsonParser, objectMapper.getDeserializationContext(), collection);
        assertEquals(3, collection.size());
    }

    @Test
    public void testHandleNonArray() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("1");
        collectionDeserializer.handleNonArray(jsonParser, objectMapper.getDeserializationContext(), collection);
        assertEquals(1, collection.size());
    }

    @Test
    public void testHandleNullArray() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[null, null, null]");
        collectionDeserializer.deserialize(jsonParser, objectMapper.getDeserializationContext(), collection);
        assertEquals(3, collection.size());
        assertTrue(collection.contains(null));
    }

    @Test(expected = UnresolvedForwardReference.class)
    public void testDeserializeWithReference() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[]");
        ObjectBuffer objectBuffer = new ObjectBuffer(null);
        collectionDeserializer.deserialize(jsonParser, objectMapper.getDeserializationContext(), objectBuffer);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[]");
        JsonDeserializer jsonDeserializer = objectMapper.getDeserializerProvider().findValueDeserializer(objectMapper.getDeserializationContext().getConfig(), Object.class);
        collectionDeserializer.deserializeWithType(jsonParser, objectMapper.getDeserializationContext(), jsonDeserializer);
    }
}