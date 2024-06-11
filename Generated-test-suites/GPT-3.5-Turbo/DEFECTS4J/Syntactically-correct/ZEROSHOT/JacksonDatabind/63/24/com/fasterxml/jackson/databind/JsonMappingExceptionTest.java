
package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;

import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class JsonMappingExceptionTest {
    private JsonMappingException exception;

    @Test
    public void testGetLocalizedMessage() {
        exception = new JsonMappingException("Test Message");
        assertEquals("Test Message", exception.getLocalizedMessage());
    }

    @Test
    public void testGetMessageWithoutPath() {
        exception = new JsonMappingException("Test Message");
        assertEquals("Test Message", exception.getMessage());
    }

    @Test
    public void testGetMessageWithPath() {
        exception = new JsonMappingException((Closeable) null, "Test Message");
        exception.prependPath("object", "property");
        assertEquals("Test Message (through reference chain: object[\"property\"])", exception.getMessage());
    }

    @Test
    public void testToString() {
        exception = new JsonMappingException("Test Message");
        assertEquals("class com.fasterxml.jackson.databind.JsonMappingException: Test Message", exception.toString());
    }
}
