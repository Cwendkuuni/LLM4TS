
package com.fasterxml.jackson.databind;

import org.junit.Test;

public class JsonMappingExceptionTest {

    @Test
    public void testGetPath() {
        JsonMappingException exception = new JsonMappingException("Test");
        exception.prependPath(this, "fieldName");
        exception.getPath();
    }

    @Test
    public void testGetPathReference() {
        JsonMappingException exception = new JsonMappingException("Test");
        exception.prependPath(this, "fieldName");
        exception.getPathReference();
    }

    @Test
    public void testPrependPathWithFieldName() {
        JsonMappingException exception = new JsonMappingException("Test");
        exception.prependPath(this, "fieldName");
    }

    @Test
    public void testPrependPathWithIndex() {
        JsonMappingException exception = new JsonMappingException("Test");
        exception.prependPath(this, 1);
    }

    @Test
    public void testGetProcessor() {
        JsonMappingException exception = new JsonMappingException("Test");
        exception.getProcessor();
    }

    @Test
    public void testGetLocalizedMessage() {
        JsonMappingException exception = new JsonMappingException("Test");
        exception.getLocalizedMessage();
    }

    @Test
    public void testGetMessage() {
        JsonMappingException exception = new JsonMappingException("Test");
        exception.getMessage();
    }

    @Test
    public void testToString() {
        JsonMappingException exception = new JsonMappingException("Test");
        exception.toString();
    }

    @Test
    public void testWrapWithPath() {
        JsonMappingException exception = new JsonMappingException("Test");
        exception.wrapWithPath(new NullPointerException(), "fieldName");
    }

}
