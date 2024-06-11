package com.fasterxml.jackson.databind;

import org.junit.Before;
import org.junit.Test;


public class JsonMappingExceptionTest {

    private JsonMappingException exceptionUnderTest;

    @Before
    public void setUp() {
        exceptionUnderTest = new JsonMappingException("");
    }

    @Test
    public void testWrapWithPath() {
        // Setup

        // Run the test
        final JsonMappingException result = JsonMappingException.wrapWithPath(new Throwable(), new Object(), new String());

        // Verify the results
    }

    @Test
    public void testWrapWithPath1() {
        // Setup

        // Run the test
        final JsonMappingException result = JsonMappingException.wrapWithPath(new Throwable(), new Object(), 0);

        // Verify the results
    }

    @Test
    public void testPrependPath() {
        // Setup

        // Run the test
        exceptionUnderTest.prependPath(new Object(), new String());

        // Verify the results
    }

    @Test
    public void testPrependPath1() {
        // Setup

        // Run the test
        exceptionUnderTest.prependPath(new Object(), 0);

        // Verify the results
    }

    @Test
    public void testGetProcessed() {
        // Setup

        // Run the test
        final Object result = exceptionUnderTest.getProcessor();

        // Verify the results
    }

    @Test
    public void testGetPath() {
        // Setup

        // Run the test
        final java.util.List<JsonMappingException.Reference> result = exceptionUnderTest.getPath();

        // Verify the results
    }

    @Test
    public void testToString() {
        // Setup

        // Run the test
        final String result = exceptionUnderTest.toString();

        // Verify the results
    }

    @Test
    public void testGetPathReference() {
        // Setup

        // Run the test
        final String result = exceptionUnderTest.getPathReference();

        // Verify the results
    }

    @Test
    public void testPrependPath2() {
        // Setup

        // Run the test
        exceptionUnderTest.prependPath(new JsonMappingException.Reference());

        // Verify the results
    }
}