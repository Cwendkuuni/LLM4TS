package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class JsonMappingExceptionTest {

    @Test
    public void testJsonMappingException() {
        // Test default constructor
        JsonMappingException e1 = new JsonMappingException("Default constructor");
        Assert.assertEquals("Default constructor", e1.getMessage());
        Assert.assertNull(e1.getPath());

        // Test constructor with message
        JsonMappingException e2 = new JsonMappingException(new JsonProcessingException.Value("Test message"), "Message");
        Assert.assertEquals("Message", e2.getMessage());
        Assert.assertNull(e2.getPath());

        // Test constructor with location
        JsonParser parser = createMockParser();
        JsonProcessingException jpe = new JsonProcessingException(parser, "Test location");
        JsonMappingException e3 = new JsonMappingException(jpe, "Message with location");
        Assert.assertEquals("Message with location", e3.getMessage());
        Assert.assertEquals(jpe.getLocation(), e3.getLocation());

        // Test constructor with processor and message
        Closeable processor = createMockParser();
        JsonMappingException e4 = new JsonMappingException(processor, "Message with processor");
        Assert.assertEquals("Message with processor", e4.getMessage());
        Assert.assertNull(e4.getLocation());
        Assert.assertEquals(processor, e4.getProcessor());

        // Test wrapper methods
        JsonParser parser2 = createMockParser();
        JsonProcessingException jpe2 = new JsonProcessingException(parser2, "Test JSON processing exception");
        Reference ref = new Reference(parser2, "fieldName");
        JsonMappingException e5 = JsonMappingException.wrapWithPath(jpe2, ref);
        Assert.assertEquals("Test JSON processing exception (through reference chain: [\"fieldName\"])", e5.getMessage());
        Assert.assertEquals(1, e5.getPath().size());
        Assert.assertEquals(parser2, e5.getProcessor());

        // Test from factory methods
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{'key': null}");
        JsonMappingException e6 = JsonMappingException.from(mapper.getFactory().constructParser(node), "Test from factory");
        Assert.assertEquals("Test from factory", e6.getMessage());
        Assert.assertEquals(node, e6.getProcessor());

        // Test fromJsonMappingException method
        JsonMappingException e7 = new JsonMappingException("Test JSON mapping exception");
        JsonMappingException e8 = JsonMappingException.fromJsonMappingException(e7);
        Assert.assertEquals("Test JSON mapping exception", e8.getMessage());
        Assert.assertEquals(e7, e8);

        // Test getPath method
        JsonMappingException e9 = new JsonMappingException(new JsonProcessingException.Value("Test message"), "Message");
        Reference ref2 = new Reference(parser2, "fieldName");
        JsonMappingException e10 = JsonMappingException.wrapWithPath(e9, ref2);
        List<Reference> path = e10.getPath();
        Assert.assertEquals(1, path.size());
        Assert.assertEquals(parser2, path.get(0).getFrom());
        Assert.assertEquals("fieldName", path.get(0).getFieldName());

        // Test getPathReference method
        StringBuilder sb = new StringBuilder();
        JsonMappingException e11 = new JsonMappingException(new JsonProcessingException.Value("Test message"), "Message");
        String pathReference = e11.getPathReference(sb);
        Assert.assertEquals("Message (through reference chain: [])", pathReference);

        // Test getPathReference method with custom StringBuilder
        StringBuilder sb2 = new StringBuilder();
        JsonMappingException e12 = new JsonMappingException(new JsonProcessingException.Value("Test message"), "Message");
        Reference ref3 = new Reference(parser2, "fieldName");
        JsonMappingException e13 = JsonMappingException.wrapWithPath(e12, ref3);
        String pathReference2 = e13.getPathReference(sb2);
        Assert.assertEquals("Message (through reference chain: [\"fieldName\"])", pathReference2);
    }

    private JsonParser createMockParser() {
        return mock(JsonParser.class);
    }
}