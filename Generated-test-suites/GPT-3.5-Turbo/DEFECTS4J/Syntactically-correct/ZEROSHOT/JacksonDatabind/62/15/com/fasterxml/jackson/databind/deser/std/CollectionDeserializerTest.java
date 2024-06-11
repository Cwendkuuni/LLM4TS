package com.fasterxml.jackson.databind.deser.std;

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
import com.fasterxml.jackson.databind.deser.std.ContainerDeserializerBase;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@JacksonStdImpl
public class CollectionDeserializerTest {

    private static JsonParser p;
    private static DeserializationContext ctxt;
    private static CollectionDeserializer collectionDeserializer;

    @BeforeClass
    public static void setup() throws IOException {
        p = mock(JsonParser.class);
        ctxt = mock(DeserializationContext.class);
        collectionDeserializer = new CollectionDeserializer(null, null, null, null);
    }

    @Test
    public void testClassName() {
        assertEquals(collectionDeserializer.getClass().getName(), "com.fasterxml.jackson.databind.deser.std.CollectionDeserializer");
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        BeanProperty property = mock(BeanProperty.class);
        CollectionDeserializer result = collectionDeserializer.createContextual(ctxt, property);
        assertEquals(result.getClass().getName(), "com.fasterxml.jackson.databind.deser.std.CollectionDeserializer");
    }

    @Test
    public void testGetContentType() {
        JavaType result = collectionDeserializer.getContentType();
        assertEquals(result, null);
    }

    @Test
    public void testGetContentDeserializer() {
        JsonDeserializer<Object> result = collectionDeserializer.getContentDeserializer();
        assertEquals(result, null);
    }

    @Test
    public void testDeserialize() throws IOException {
        Collection<Object> result = collectionDeserializer.deserialize(p, ctxt);
        assertEquals(result.getClass().getName(), "java.util.LinkedList");
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        Object result = collectionDeserializer.deserializeWithType(p, ctxt, null);
        assertEquals(result, null);
    }

    @Test
    public void testHandleNonArray() throws IOException {
        Collection<Object> result = collectionDeserializer.handleNonArray(p, ctxt, new LinkedList<>());
        assertEquals(result.getClass().getName(), "java.util.LinkedList");
    }
}