package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.deser.std.ContainerDeserializerBase;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

@JacksonStdImpl
public class CollectionDeserializerTest {

    private CollectionDeserializer collectionDeserializer;

    @Before
    public void setup() {
        JavaType collectionType = new ObjectMapper().getTypeFactory().constructCollectionType(List.class, Object.class);
        collectionDeserializer = new CollectionDeserializer(collectionType, null, null, null);
    }

    @Test
    public void testDeserialize_EmptyArray_ReturnsEmptyCollection() throws IOException {
        JsonParser parser = new ObjectMapper().getFactory().createParser("[]");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        Collection<Object> result = collectionDeserializer.deserialize(parser, ctxt);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testDeserialize_NullValue_ReturnsNullValue() throws IOException {
        JsonParser parser = new ObjectMapper().getFactory().createParser("[null]");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        Collection<Object> result = collectionDeserializer.deserialize(parser, ctxt);
        assertTrue(result.contains(null));
    }

    @Test
    public void testDeserialize_SingleValue_ReturnsCollectionWithSingleValue() throws IOException {
        JsonParser parser = new ObjectMapper().getFactory().createParser("[1]");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        Collection<Object> result = collectionDeserializer.deserialize(parser, ctxt);
        assertEquals(1, result.size());
        assertTrue(result.contains(1));
    }

    @Test
    public void testDeserialize_MultipleValues_ReturnsCollectionWithAllValues() throws IOException {
        JsonParser parser = new ObjectMapper().getFactory().createParser("[1, 2, 3]");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        Collection<Object> result = collectionDeserializer.deserialize(parser, ctxt);
        assertEquals(3, result.size());
        assertTrue(result.contains(1));
        assertTrue(result.contains(2));
        assertTrue(result.contains(3));
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserialize_InvalidJson_ThrowsException() throws IOException {
        JsonParser parser = new ObjectMapper().getFactory().createParser("[1, 2,]");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        collectionDeserializer.deserialize(parser, ctxt);
    }
}