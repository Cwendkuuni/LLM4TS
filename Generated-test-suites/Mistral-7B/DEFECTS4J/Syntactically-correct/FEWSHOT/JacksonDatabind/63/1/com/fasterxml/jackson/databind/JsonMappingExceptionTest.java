package com.fasterxml.jackson.databind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonMappingExceptionTest {

    @Test
    public void testBasicConstruction() {
        JsonMappingException e = new JsonMappingException("Test Message");
        assertEquals("Test Message", e.getMessage());
        assertNull(e.getPath());
        assertNull(e.getProcessor());
    }

    @Test
    public void testConstructionWithMessageAndLocation() {
        JsonLocation loc = new JsonLocation(1, 1, "test.json");
        JsonMappingException e = new JsonMappingException("Test Message", loc);
        assertEquals("Test Message", e.getMessage());
        assertEquals(loc, e.getLocation());
        assertNull(e.getPath());
        assertNull(e.getProcessor());
    }

    @Test
    public void testConstructionWithMessageLocationAndRootCause() {
        JsonLocation loc = new JsonLocation(1, 1, "test.json");
        IOException rootCause = new IOException("Root Cause");
        JsonMappingException e = new JsonMappingException("Test Message", loc, rootCause);
        assertEquals("Test Message", e.getMessage());
        assertEquals(loc, e.getLocation());
        assertEquals(rootCause, e.getRootCause());
        assertNull(e.getPath());
        assertNull(e.getProcessor());
    }

    @Test
    public void testConstructionWithProcessor() {
        JsonParser parser = new ObjectMapper().getFactory().createParser("{}");
        JsonMappingException e = new JsonMappingException(parser, "Test Message");
        assertEquals("Test Message", e.getMessage());
        assertNotNull(e.getProcessor());
        assertNull(e.getLocation());
        assertNull(e.getRootCause());
        assertNull(e.getPath());
    }

    @Test
    public void testConstructionWithProcessorMessage() {
        JsonParser parser = new ObjectMapper().getFactory().createParser("{}");
        JsonMappingException e = new JsonMappingException(parser, "Test Message");
        assertEquals("Test Message", e.getMessage());
        assertNotNull(e.getProcessor());
        assertNull(e.getLocation());
        assertNull(e.getRootCause());
        assertNull(e.getPath());
    }

    @Test
    public void testConstructionWithProcessorMessageAndLocation() {
        JsonParser parser = new ObjectMapper().getFactory().createParser("{}");
        JsonMappingException e = new JsonMappingException(parser, "Test Message", new JsonLocation(1, 1, "test.json"));
        assertEquals("Test Message", e.getMessage());
        assertEquals(new JsonLocation(1, 1, "test.json"), e.getLocation());
        assertNotNull(e.getProcessor());
        assertNull(e.getRootCause());
        assertNull(e.getPath());
    }

    @Test
    public void testConstructionWithProcessorMessageLocationAndRootCause() {
        JsonParser parser = new ObjectMapper().getFactory().createParser("{}");
        IOException rootCause = new IOException("Root Cause");
        JsonMappingException e = new JsonMappingException(parser, "Test Message", new JsonLocation(1, 1, "test.json"), rootCause);
        assertEquals("Test Message", e.getMessage());
        assertEquals(new JsonLocation(1, 1, "test.json"), e.getLocation());
        assertEquals(rootCause, e.getRootCause());
        assertNotNull(e.getProcessor());
        assertNull(e.getPath());
    }

    @Test
    public void testGetPathWhenNoPath() {
        JsonMappingException e = new JsonMappingException("Test Message");
        assertNull(e.getPath());
    }

    @Test
    public void testGetPathWhenHasPath() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonParser parser = mapper.readTree("{ \"foo\": [1, 2, 3], \"bar\": { \"baz\": 4 } }");
        JsonMappingException e = JsonMappingException.from(parser, "Test Message");
        e.prependPath(parser.getCurrentName());
        e.prependPath("foo");
        e.prependPath(0);
        List<Reference> path = e.getPath();
        assertEquals(3, path.size());
        assertEquals("foo", path.get(0).getFieldName());
        assertEquals(0, path.get(0).getIndex());
        assertEquals("1", path.get(1).getFieldName());
        assertEquals(JsonParser.class, path.get(1).getFrom().getClass());
        assertEquals(parser, path.get(1).getFrom());
        assertEquals("[]", path.get(2).getDescription());
    }

    @Test
    public void testGetPathReference() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonParser parser = mapper.readTree("{ \"foo\": [1, 2, 3], \"bar\": { \"baz\": 4 } }");
        JsonMappingException e = JsonMappingException.from(parser, "Test Message");
        e.prependPath(parser.getCurrentName());
        e.prependPath("foo");
        e.prependPath(0);
        StringBuilder sb = new StringBuilder();
        e.getPathReference(sb);
        assertEquals("bar->foo->0", sb.toString());
    }

    @Test
    public void testWrapWithPath() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonParser parser = mapper.readTree("{ \"foo\": [1, 2, 3], \"bar\": { \"baz\": 4 } }");
        JsonMappingException e = new JsonMappingException("Test Message");
        String fieldName = "foo";
        int index = 0;
        JsonMappingException wrapped = JsonMappingException.wrapWithPath(e, parser, fieldName);
        assertEquals("Test Message", wrapped.getMessage());
        List<Reference> path = wrapped.getPath();
        assertEquals(1, path.size());
        assertEquals(fieldName, path.get(0).getFieldName());
        assertEquals(parser, path.get(0).getFrom());
        assertEquals("foo", path.get(0).getDescription());

        JsonMappingException wrappedIndex = JsonMappingException.wrapWithPath(e, parser, index);
        assertEquals("Test Message", wrappedIndex.getMessage());
        List<Reference> pathIndex = wrappedIndex.getPath();
        assertEquals(1, pathIndex.size());
        assertEquals(index, pathIndex.get(0).getIndex());
        assertEquals("[]", pathIndex.get(0).getDescription());
    }

    @Test
    public void testFromUnexpectedIOE() {
        IOException io = new IOException("Test IO Exception");
        JsonMappingException e = JsonMappingException.fromUnexpectedIOE(io);
        assertEquals("Unexpected IOException (of type IOException): Test IO Exception", e.getMessage());
    }
}