package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ClassUtil;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Test cases for JsonMappingException class.
 */
public class JsonMappingExceptionTest {

    

    @org.junit.Test
    public void testJsonMappingException() {
        // Test the default constructor
        JsonMappingException jsonMappingException = new JsonMappingException("Test Message");
        assertEquals("Test Message", jsonMappingException.getMessage());
        assertNull(jsonMappingException.getFrom());
        assertNull(jsonMappingException.getLocation());
        assertNull(jsonMappingException.getProcessor());
        assertNotNull(jsonMappingException.getPath());
        assertTrue(jsonMappingException.getPath().isEmpty());

        // Test the constructor with message
        JsonMappingException jsonMappingExceptionWithMsg = new JsonMappingException("JsonParser", "Test Message");
        assertEquals("JsonParser: Test Message", jsonMappingExceptionWithMsg.getMessage());
        assertNull(jsonMappingExceptionWithMsg.getFrom());
        assertNull(jsonMappingExceptionWithMsg.getLocation());
        assertNotNull(jsonMappingExceptionWithMsg.getProcessor());
        assertNotNull(jsonMappingExceptionWithMsg.getLocation());
        assertTrue(jsonMappingExceptionWithMsg.getPath().isEmpty());

        // Test the constructor with message and root cause
        JsonParseException jsonParseException = new JsonParseException("Test Message");
        JsonMappingException jsonMappingExceptionWithRootCause = new JsonMappingException("JsonParser", jsonParseException);
        assertEquals("JsonParser: Test Message", jsonMappingExceptionWithRootCause.getMessage());
        assertNull(jsonMappingExceptionWithRootCause.getFrom());
        assertNull(jsonMappingExceptionWithRootCause.getLocation());
        assertNotNull(jsonMappingExceptionWithRootCause.getProcessor());
        assertNotNull(jsonMappingExceptionWithRootCause.getLocation());
        assertTrue(jsonMappingExceptionWithRootCause.getPath().isEmpty());
        assertEquals(jsonParseException, jsonMappingExceptionWithRootCause.getCause());

        // Test the constructor with message, location, and root cause
        JsonLocation jsonLocation = new JsonLocation(1, 1, "Test File");
        JsonMappingException jsonMappingExceptionWithLocation = new JsonMappingException("JsonParser", "Test Message", jsonLocation);
        assertEquals("JsonParser: Test Message", jsonMappingExceptionWithLocation.getMessage());
        assertNull(jsonMappingExceptionWithLocation.getFrom());
        assertNotNull(jsonMappingExceptionWithLocation.getLocation());
        assertNotNull(jsonMappingExceptionWithLocation.getProcessor());
        assertEquals(jsonLocation, jsonMappingExceptionWithLocation.getLocation());
        assertTrue(jsonMappingExceptionWithLocation.getPath().isEmpty());

        // Test the factory method fromUnexpectedIOE
        IOException ioException = new IOException("Test IOException Message");
        JsonMappingException jsonMappingExceptionFromUnexpectedIOE = JsonMappingException.fromUnexpectedIOE(ioException);
        assertEquals("Unexpected IOException (of type "+ioException.getClass().getName()+"): Test IOException Message", jsonMappingExceptionFromUnexpectedIOE.getMessage());
        assertNull(jsonMappingExceptionFromUnexpectedIOE.getFrom());
        assertNull(jsonMappingExceptionFromUnexpectedIOE.getLocation());
        assertNull(jsonMappingExceptionFromUnexpectedIOE.getProcessor());
        assertTrue(jsonMappingExceptionFromUnexpectedIOE.getPath().isEmpty());

        // Test the static method wrapWithPath
        JsonProcessingException jsonProcessingException = new JsonProcessingException("Test JsonProcessingException Message");
        ObjectMapper objectMapper = new ObjectMapper();
        JsonMappingException jsonMappingExceptionWrapWithPath = JsonMappingException.wrapWithPath(jsonProcessingException, "Test Object", "Test Field");
        assertEquals("JsonMappingException: Test JsonProcessingException Message (through reference chain: Test Object->Test Field)", jsonMappingExceptionWrapWithPath.getMessage());
        assertNull(jsonMappingExceptionWrapWithPath.getFrom());
        assertNull(jsonMappingExceptionWrapWithPath.getLocation());
        assertNull(jsonMappingExceptionWrapWithPath.getProcessor());
        assertNotNull(jsonMappingExceptionWrapWithPath.getPath());
        List<JsonMappingException.Reference> path = jsonMappingExceptionWrapWithPath.getPath();
        assertEquals(1, path.size());
        JsonMappingException.Reference reference = path.get(0);
        assertEquals("Test Object", reference.getFrom());
        assertEquals("Test Field", reference.getFieldName());
        assertEquals("Test Object", reference.getDescription());

        // Test the static method wrapWithPath with index
        Object testObject = new Object();
        List<Object> list = new ArrayList<>();
        list.add(testObject);
        JsonProcessingException jsonProcessingExceptionWithIndex = new JsonProcessingException("Test JsonProcessingException Message", new JsonLocation(1, 1, "Test File"));
        JsonMappingException jsonMappingExceptionWrapWithPathWithIndex = JsonMappingException.wrapWithPath(jsonProcessingExceptionWithIndex, testObject, 0);
        assertEquals("JsonMappingException: Test JsonProcessingException Message (through reference chain: Object[0]->Test Object)", jsonMappingExceptionWrapWithPathWithIndex.getMessage());
        assertNull(jsonMappingExceptionWrapWithPathWithIndex.getFrom());
        assertNull(jsonMappingExceptionWrapWithPathWithIndex.getLocation());
        assertNull(jsonMappingExceptionWrapWithPathWithIndex.getProcessor());
        assertNotNull(jsonMappingExceptionWrapWithPathWithIndex.getPath());
        List<JsonMappingException.Reference> pathWithIndex = jsonMappingExceptionWrapWithPathWithIndex.getPath();
        assertEquals(1, pathWithIndex.size());
        JsonMappingException.Reference referenceWithIndex = pathWithIndex.get(0);
        assertEquals(testObject, referenceWithIndex.getFrom());
        assertEquals(-1, referenceWithIndex.getIndex());
        assertEquals("Object[0]", referenceWithIndex.getDescription());

        
    }
}