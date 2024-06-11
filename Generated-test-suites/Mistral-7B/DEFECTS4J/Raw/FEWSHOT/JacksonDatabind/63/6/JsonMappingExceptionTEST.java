package com.fasterxml.jackson.databind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class JsonMappingExceptionTest {

    @Test
    public void testJsonMappingException() {
        JsonMappingException e = new JsonMappingException("Test Message");
        assertEquals("Test Message", e.getMessage());
        assertNull(e.getPath());
        assertNull(e.getProcessor());

        e = new JsonMappingException("Test Message", new IOException("IO Error"));
        assertEquals("Test Message", e.getMessage());
        assertEquals("IO Error", e.getCause().getMessage());
        assertNull(e.getPath());
        assertNull(e.getProcessor());

        e = new JsonMappingException(new IOException("IO Error"), "Test Message");
        assertEquals("Test Message", e.getMessage());
        assertEquals("IO Error", e.getCause().getMessage());
        assertNull(e.getPath());
        assertNull(e.getProcessor());
    }

    @Test
    public void testJsonMappingExceptionWithPath() {
        Closeable processor = new Object();
        JsonMappingException e = new JsonMappingException(processor, "Test Message"); ;
        assertEquals("Test Message", e.getMessage());
        assertEquals(processor, e.getProcessor());
        assertNull(e.getPath());

        Reference ref1 = new Reference(new Object(), "field1");
        JsonMappingException e1 = new JsonMappingException(processor, "Test Message", ref1);
        assertEquals("Test Message", e1.getMessage());
        assertEquals(processor, e1.getProcessor());
        assertEquals(Arrays.asList(ref1), e1.getPath());

        Reference ref2 = new Reference(new Object(), "field2");
        JsonMappingException e2 = new JsonMappingException(processor, "Test Message", ref1, ref2);
        assertEquals("Test Message", e2.getMessage());
        assertEquals(processor, e2.getProcessor());
        assertEquals(Arrays.asList(ref2, ref1), e2.getPath());

        Reference ref3 = new Reference(new Object(), 0);
        JsonMappingException e3 = new JsonMappingException(processor, "Test Message", ref1, ref3);
        assertEquals("Test Message", e3.getMessage());
        assertEquals(processor, e3.getProcessor());
        assertEquals(Arrays.asList(ref3, ref1), e3.getPath());

        Reference ref4 = new Reference(new Object(), "field4");
        JsonMappingException e4 = new JsonMappingException(processor, "Test Message", new LinkedList<>(Arrays.asList(ref1, ref2, ref3, ref4)));
        assertEquals("Test Message", e4.getMessage());
        assertEquals(processor, e4.getProcessor());
        assertEquals(Arrays.asList(ref4, ref3, ref2, ref1), e4.getPath());
    }

    @Test
    public void testFromJsonProcessingException() {
        JsonParser parser = new Object();
        JsonMappingException e1 = JsonMappingException.from(parser, "Test Message");
        assertEquals("Test Message", e1.getMessage());
        assertNull(e1.getProcessor());
        assertNull(e1.getPath());

        JsonProcessingException e2 = new JsonProcessingException("Test Message");
        JsonMappingException e3 = JsonMappingException.from(parser, e2);
        assertEquals("Test Message", e3.getMessage());
        assertEquals(parser, e3.getProcessor());
        assertNull(e3.getPath());

        JsonProcessingException e4 = new JsonProcessingException("Test Message", new IOException("IO Error"));
        JsonMappingException e5 = JsonMappingException.from(parser, e4);
        assertEquals("Test Message", e5.getMessage());
        assertEquals(parser, e5.getProcessor());
        assertNull(e5.getPath());

        JsonProcessingException e6 = new JsonProcessingException("Test Message", new IOException("IO Error"), new Object());
        JsonMappingException e7 = JsonMappingException.from(parser, e6);
        assertEquals("Test Message", e7.getMessage());
        assertEquals(parser, e7.getProcessor());
        assertNull(e7.getPath());
    }

    @Test
    public void testFromJsonParser() {
        JsonParser parser = new Object();
        assertEquals(parser, JsonMappingException.from(parser, "Test Message").getProcessor());
        JsonMappingException e1 = JsonMappingException.from(parser, "Test Message");
        assertEquals("Test Message", e1.getMessage());
        assertNull(e1.getPath());

        JsonMappingException e2 = JsonMappingException.from(parser, "Test Message", new IOException("IO Error"));
        assertEquals("Test Message", e2.getMessage());
        assertEquals("IO Error", e2.getCause().getMessage());
        assertNull(e2.getPath());
        assertNull(e2.getProcessor());

        JsonProcessingException e3 = new JsonProcessingException("Test Message", new IOException("IO Error"));
        JsonMappingException e4 = JsonMappingException.from(parser, e3);
        assertEquals("Test Message", e4.getMessage());
        assertEquals("IO Error", e4.getCause().getMessage());
        assertNull(e4.getPath());
        assertNull(e4.getProcessor());
    }

    @Test
    public void testFromJsonGenerator() {
        JsonGenerator generator = new Object();
        assertEquals(generator, JsonMappingException.from(generator, "Test Message").getProcessor());
        JsonMappingException e1 = JsonMappingException.from(generator, "Test Message");
        assertEquals("Test Message", e1.getMessage());
        assertNull(e1.getPath());

        JsonMappingException e2 = JsonMappingException.from(generator, "Test Message", new IOException("IO Error"));
        assertEquals("Test Message", e2.getMessage());
        assertEquals("IO Error", e2.getCause().getMessage());
        assertNull(e2.getPath());
        assertNull(e2.getProcessor());

        JsonProcessingException e3 = new JsonProcessingException("Test Message", new IOException("IO Error"));
        JsonMappingException e4 = JsonMappingException.from(generator, e3);
        assertEquals("Test Message", e4.getMessage());
        assertEquals("IO Error", e4.getCause().getMessage());
        assertNull(e4.getPath());
        assertNull(e4.getProcessor());
    }

    @Test
    public void testFromDeserializationContext() {
        JsonParser parser = new Object();
        JsonDeserializer<?> deserializer = new Object();
        DeserializationContext context = new Object();
        context.setParser(parser);
        context.setDeserializer(deserializer);

        assertEquals(parser, JsonMappingException.from(context, "Test Message").getProcessor());
        JsonMappingException e1 = JsonMappingException.from(context, "Test Message");
        assertEquals("Test Message", e1.getMessage());
        assertNull(e1.getPath());

        JsonMappingException e2 = JsonMappingException.from(context, "Test Message", new IOException("IO Error"));
        assertEquals("Test Message", e2.getMessage());
        assertEquals("IO Error", e2.getCause().getMessage());
        assertNull(e2.getPath());
        assertNull(e2.getProcessor());

        JsonProcessingException e3 = new JsonProcessingException("Test Message", new IOException("IO Error"));
        JsonMappingException e4 = JsonMappingException.from(context, e3);
        assertEquals("Test Message", e4.getMessage());
        assertEquals("IO Error", e4.getCause().getMessage());
        assertNull(e4.getPath());
        assertNull(e4.getProcessor());
    }

    @Test
    public void testFromSerializerProvider() {
        JsonGenerator generator = new Object();
        JsonSerializer<?> serializer = new Object();
        SerializerProvider provider = new Object();
        provider.setSerializer(serializer);
        provider.setSerializerProvider(provider);

        assertEquals(generator, JsonMappingException.from(provider, "Test Message").getProcessor());
        JsonMappingException e1 = JsonMappingException.from(provider, "Test Message");
        assertEquals("Test Message", e1.getMessage());
        assertNull(e1.getPath());

        JsonMappingException e2 = JsonMappingException.from(provider, "Test Message", new IOException("IO Error"));
        assertEquals("Test Message", e2.getMessage());
        assertEquals("IO Error", e2.getCause().getMessage());
        assertNull(e2.getPath());
        assertNull(e2.getProcessor());

        JsonProcessingException e3 = new JsonProcessingException("Test Message", new IOException("IO Error"));
        JsonMappingException e4 = JsonMappingException.from(provider, e3);
        assertEquals("Test Message", e4.getMessage());
        assertEquals("IO Error", e4.getCause().getMessage());
        assertNull(e4.getPath());
        assertNull(e4.getProcessor());
    }

    @Test
    public void testWrapWithPath() {
        Object referrer = new Object();
        JsonMappingException e1 = new JsonMappingException("Test Message");
        Reference ref1 = new Reference(referrer, "field1");
        JsonMappingException e2 = JsonMappingException.wrapWithPath(e1, ref1);
        assertEquals("Test Message (through reference chain: field1->)", e2.getMessage());
        assertEquals(Arrays.asList(ref1), e2.getPath());
        assertNull(e2.getProcessor());

        Object[] refs = new Object[] {referrer, new Object()};
        JsonMappingException e3 = new JsonMappingException("Test Message");
        JsonMappingException e4 = JsonMappingException.wrapWithPath(e3, refs);
        StringBuilder sb = new StringBuilder();
        e4.getPathReference(sb);
        assertEquals("Test Message (through reference chain: [field1->, 0->])", e4.getMessage());
        assertEquals(Arrays.asList(refs[1], refs[0]), e4.getPath());
        assertNull(e4.getProcessor());
    }

    @Test
    public void testFromUnexpectedIOE() {
        IOException e = new IOException("Test IO Error");
        JsonMappingException f = JsonMappingException.fromUnexpectedIOE(e);
        assertEquals("Unexpected IOException (of type java.io.IOException): Test IO Error", f.getMessage());
        assertNull(f.getPath());
        assertNull(f.getProcessor());
    }
}