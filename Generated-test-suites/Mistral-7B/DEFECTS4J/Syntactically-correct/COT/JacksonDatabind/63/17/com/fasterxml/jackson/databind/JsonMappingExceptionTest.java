package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class JsonMappingExceptionTest {

    @Test
    public void testJsonMappingExceptionBasic() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\":\"John\"}";
        JsonMappingException e = JsonMappingException.from(mapper, "Invalid Json: " + JsonMappingException.class.getName());
        assertEquals("Invalid Json: " + JsonMappingException.class.getName(), e.getMessage());
        assertNull(e.getPath());
    }

    @Test
    public void testJsonMappingExceptionWithPath() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1, 2, 3]";
        JsonMappingException e = JsonMappingException.from(mapper, JsonMappingException.class, 2, "list", "element");
        List<Reference> path = e.getPath();
        assertEquals(1, path.size());
        Reference reference = path.get(0);
        assertEquals("[1, 2, 3]", reference.getDescription());
        assertEquals(JsonMappingException.class, reference.getFrom().getClass());
        assertEquals("element", reference.getFieldName());
        assertEquals(2, reference.getIndex());
    }

    @Test(expected = JsonMappingException.class)
    public void testJsonMappingExceptionWithPathThrowsException() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1, 2, 3]";
        JsonMappingException e = JsonMappingException.from(mapper, JsonMappingException.class, 3, "list", "element");
        // The exception should be thrown here
    }

    @Test
    public void testJsonMappingExceptionToString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1, 2, 3]";
        JsonMappingException e = JsonMappingException.from(mapper, JsonMappingException.class, 2, "list", "element");
        String expected = "JsonMappingException: (through reference chain: list[2]->element)";
        assertEquals(expected, e.toString());
    }

    @Test
    public void testJsonMappingExceptionWithProcessor() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1, 2, 3]";
        Closeable processor = mapper.getFactory().getParser();
        JsonMappingException e = JsonMappingException.from(processor, JsonMappingException.class, "Invalid Json: " + JsonMappingException.class.getName());
        assertEquals(JsonMappingException.class, e.getClass());
        assertNotNull(e.getProcessor());
        assertEquals(processor.getClass(), e.getProcessor().getClass());
    }

    @Test
    public void testJsonMappingExceptionFromUnexpectedIOException() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1, 2, 3]";
        JsonProcessingException e = mapper.readValue(json, Integer[].class);
        JsonMappingException mappedException = JsonMappingException.fromUnexpectedIOE(e);
        String expected = "JsonMappingException: Unexpected IOException (of type java.io.IOException): " +
                "Unexpected value found at [Source: (ByteArrayInputStream); line: 1, column: 1]";
        assertEquals(expected, mappedException.getMessage());
    }

    @Test
    public void testJsonMappingExceptionMaxReferencesLimit() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100]";
        JsonMappingException e = JsonMappingException.from(mapper, JsonMappingException.class, 1, "array", "element");
        LinkedList<Reference> references = e.getPath();
        assertEquals(50, references.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJsonMappingExceptionNegativeIndex() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1, 2, 3]";
        JsonMappingException e = JsonMappingException.from(mapper, JsonMappingException.class, 1, "array", -1);
    }

    @Test(expected = NullPointerException.class)
    public void testJsonMappingExceptionNullReference() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1, 2, 3]";
        JsonMappingException e = JsonMappingException.from(mapper, JsonMappingException.class, 1, null, "element");
    }

    @Test(expected = NullPointerException.class)
    public void testJsonMappingExceptionNullFieldName() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1, 2, 3]";
        JsonMappingException e = JsonMappingException.from(mapper, JsonMappingException.class, 1, null, null);
    }
}