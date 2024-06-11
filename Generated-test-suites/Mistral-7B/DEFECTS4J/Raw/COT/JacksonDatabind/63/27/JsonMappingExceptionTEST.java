package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonMappingExceptionTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testDefaultConstructor() {
        JsonMappingException e = new JsonMappingException("Test exception");
        assertEquals("Test exception", e.getMessage());
        assertNull(e.getPath());
        assertNull(e.getProcessor());
    }

    @Test
    public void testConstructorWithMessage() {
        JsonMappingException e = new JsonMappingException("Test exception", new IOException("IO error"));
        assertEquals("Test exception", e.getMessage());
        assertEquals("IO error", e.getCause().getMessage());
        assertNull(e.getPath());
        assertNull(e.getProcessor());
    }

    @Test
    public void testConstructorWithLocation() {
        JsonParser parser = mapper.getFactory().createParser(new StringReader("[]"));
        JsonMappingException e = JsonMappingException.from(parser, "Test exception");
        assertEquals("Test exception", e.getMessage());
        assertEquals(parser, e.getProcessor());
        assertNotNull(e.getLocation());
    }

    @Test
    public void testConstructorWithLocationAndMessage() {
        JsonParser parser = mapper.getFactory().createParser(new StringReader("[]"));
        JsonMappingException e = JsonMappingException.from(parser, "Test exception", new IOException("IO error"));
        assertEquals("Test exception", e.getMessage());
        assertEquals("IO error", e.getCause().getMessage());
        assertEquals(parser, e.getProcessor());
        assertNotNull(e.getLocation());
    }

    @Test
    public void testConstructorWithLocationAndMessageAndPath() {
        Object obj = new Object() {{
            String field1 = "field1";
            String field2 = new String[]{"field2"};
        }};

        JsonParser parser = mapper.getFactory().createParser(new StringReader("[{\"field1\":1,\"field2\":[\"field3\"]}]"));
        JsonMappingException e = JsonMappingException.from(parser, "Test exception", new IOException("IO error"),
                new Reference(obj, "field1"), new Reference(obj, "field2[0]"));
        assertEquals("Test exception", e.getMessage());
        assertEquals("IO error", e.getCause().getMessage());
        assertEquals(parser, e.getProcessor());
        List<Reference> path = e.getPath();
        assertEquals(2, path.size());
        assertEquals(Reference.class, path.get(0).getClass());
        assertEquals(Reference.class, path.get(1).getClass());
        assertEquals(obj, path.get(0).getFrom());
        assertEquals("field1", path.get(0).getFieldName());
        assertEquals(obj, path.get(1).getFrom());
        assertEquals("field2[0]", path.get(1).getFieldName());
    }

    @Test(expected = JsonMappingException.class)
    public void testMappingExceptionWithPathAndMessage() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        Writer writer = new StringWriter();
        JsonGenerator generator = mapper.getFactory().createGenerator(writer);
        generator.writeStartArray();
        generator.writeEndArray();
        generator.close();

        JsonMappingException e = new JsonMappingException("Test exception", new Reference("Test", "root"));
        throw e;
    }

    @Test
    public void testWrapWithPath() {
        IOException ioException = new IOException("IO error");
        Object obj = new Object() {{
            String field1 = "field1";
        }};
        Reference ref1 = new Reference(obj, "field1");

        JsonMappingException e1 = JsonMappingException.wrapWithPath(ioException, obj, "field1");
        assertEquals("IO error (was java.io.IOException)", e1.getMessage());
        assertEquals(ioException, e1.getCause());
        List<Reference> path = e1.getPath();
        assertEquals(1, path.size());
        assertEquals(ref1, path.get(0));

        ioException = new IOException("IO error");
        int index = 0;
        Reference ref2 = new Reference(new String[]{"test"}, index);

        JsonMappingException e2 = JsonMappingException.wrapWithPath(ioException, new String[]{"test"}, index);
        assertEquals("IO error (was java.io.IOException)", e2.getMessage());
        assertEquals(ioException, e2.getCause());
        path = e2.getPath();
        assertEquals(1, path.size());
        assertEquals(ref2, path.get(0));
    }
}