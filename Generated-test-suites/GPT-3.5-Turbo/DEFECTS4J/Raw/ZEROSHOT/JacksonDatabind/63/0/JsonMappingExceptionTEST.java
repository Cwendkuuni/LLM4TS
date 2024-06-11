package com.fasterxml.jackson.databind;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class JsonMappingExceptionTest {

    private JsonMappingException jsonMappingException;

    @Before
    public void setUp() {
        jsonMappingException = new JsonMappingException("Test message");
    }

    @Test
    public void testGetMessage() {
        assertEquals("Test message", jsonMappingException.getMessage());
    }

    @Test
    public void testGetLocalizedMessage() {
        assertEquals("Test message", jsonMappingException.getLocalizedMessage());
    }

    @Test
    public void testToString() {
        assertEquals("com.fasterxml.jackson.databind.JsonMappingException: Test message", jsonMappingException.toString());
    }

    @Test
    public void testGetPath() {
        assertNull(jsonMappingException.getPath());
    }

    @Test
    public void testGetPathReference() {
        assertEquals("", jsonMappingException.getPathReference());
    }

    @Test
    public void testPrependPath() {
        jsonMappingException.prependPath("testObject", "fieldName");
        assertEquals("fieldName", jsonMappingException.getPath().get(0).getFieldName());
        assertEquals("testObject", jsonMappingException.getPath().get(0).getFrom());
    }

    @Test
    public void testPrependPathWithIndex() {
        jsonMappingException.prependPath("testObject", 0);
        assertEquals(0, jsonMappingException.getPath().get(0).getIndex());
        assertEquals("testObject", jsonMappingException.getPath().get(0).getFrom());
    }
}