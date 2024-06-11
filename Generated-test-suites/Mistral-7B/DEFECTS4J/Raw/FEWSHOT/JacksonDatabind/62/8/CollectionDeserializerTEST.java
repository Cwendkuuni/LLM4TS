package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.deser.std.ContainerDeserializerBase;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

public class CollectionDeserializerTest {

    @Test
    public void testCreateContextual() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        CollectionDeserializer deserializer = new CollectionDeserializer(javaType, null, null, null, null, null);
        DeserializationContext context = mock(DeserializationContext.class);
        BeanProperty property = mock(BeanProperty.class);
        CollectionDeserializer contextualDeserializer = deserializer.createContextual(context, property);
        assertEquals(deserializer, contextualDeserializer);
    }

    @Test
    public void testDeserialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class);
        CollectionDeserializer deserializer = new CollectionDeserializer(javaType, null, null, null, null, null);

        String json = "[\"foo\", \"bar\"]";
        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext context = mock(DeserializationContext.class);
        Collection<Object> result = deserializer.deserialize(parser, context);
        assertEquals(2, result.size());
        assertEquals("foo", result.get(0));
        assertEquals("bar", result.get(1));
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class);
        CollectionDeserializer deserializer = new CollectionDeserializer(javaType, null, null, null, null, null);

        String json = "[\"foo\", \"bar\"]";
        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext context = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        Collection<Object> result = deserializer.deserializeWithType(parser, context, typeDeserializer);
        assertEquals(2, result.size());
        assertEquals("foo", result.get(0));
        assertEquals("bar", result.get(1));
    }

    @Test
    public void testHandleNonArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class);
        CollectionDeserializer deserializer = new CollectionDeserializer(javaType, null, null, null, null, null);

        String json = "\"foo\"";
        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext context = mock(DeserializationContext.class);
        Collection<Object> result = deserializer.handleNonArray(parser, context, new ArrayList<>());
        assertEquals(1, result.size());
        assertEquals("foo", result.get(0));
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleNonArrayWithNoWrap() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class);
        CollectionDeserializer deserializer = new CollectionDeserializer(javaType, null, null, null, null, null);

        String json = "\"foo\"";
        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext context = mock(DeserializationContext.class);
        context.disable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        Collection<Object> result = deserializer.handleNonArray(parser, context, new ArrayList<>());
        fail();
    }

    @Test
    public void testIsCachable() {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class);
        CollectionDeserializer deserializer = new CollectionDeserializer(javaType, null, null, null, null, null);
        assertTrue(deserializer.isCachable());
    }

    @Test
    public void testDeserializeWithUnresolvedForwardReference()
            throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class);
        CollectionDeserializer deserializer = new CollectionDeserializer(javaType, null, null, null, null, null);

        String json = "[\"foo\", {\"$ref\": \"#/properties/bar\"}]";
        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext context = mock(DeserializationContext.class);
        Collection<Object> result = deserializer.deserialize(parser, context);
        assertEquals(2, result.size());
        assertEquals("foo", result.get(0));
        assertEquals("bar", result.get(1));

        UnresolvedForwardReference reference = new UnresolvedForwardReference(parser, context);
        CollectionReferring collectionReferring = (CollectionReferring) result.get(1);
        assertEquals(reference, collectionReferring.getForwardReference());
    }

    @Test
    public void testResolveForwardReference() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class);
        CollectionDeserializer deserializer = new CollectionDeserializer(javaType, null, null, null, null, null);

        String json = "[\"foo\"][{\"$ref\": \"#/properties/bar\"}]";
        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext context = mock(DeserializationContext.class);
        Collection<Object> result = deserializer.deserialize(parser, context);
        assertEquals(2, result.size());
        assertEquals("foo", result.get(0));
        CollectionReferring collectionReferring = (CollectionReferring) result.get(1);
        Object bar = new Object();
        deserializer.resolveForwardReference(collectionReferring.getForwardReference(), bar);
        assertEquals(1, result.size());
        assertEquals(bar, result.get(0));
    }
}