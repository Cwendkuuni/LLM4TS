package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test cases for {@link JsonMappingException} class.
 */
public class JsonMappingExceptionTest {

    private static ObjectMapper mapper = new ObjectMapper();

    /**
     * Test serialization with empty input.
     */
    @Test
    public void testEmptySerialization() throws Exception {
        Object emptyInput = new Object();
        String jsonInput = "{}";
        try {
            mapper.writeValueAsString(emptyInput);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
            assertEquals("", e.getPathReference());
        }
    }

    /**
     * Test serialization with complex input.
     */
    @Test
    public void testComplexSerialization() throws Exception {
        Object complexInput = new ComplexObject();
        try {
            mapper.writeValueAsString(complexInput);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
            assertEquals("ComplexObject.field1->ComplexObject.field2->ComplexObject.field3->field4", e.getPathReference());
        }
    }

    /**
     * Test deserialization with empty input.
     */
    @Test
    public void testEmptyDeserialization() throws Exception {
        String emptyJsonInput = "{}";
        try {
            mapper.readValue(emptyJsonInput, Object.class);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
            assertEquals("", e.getPathReference());
        }
    }

    /**
     * Test deserialization with invalid JSON input.
     */
    @Test
    public void testInvalidJsonDeserialization() throws Exception {
        String invalidJsonInput = "{\"field1\":";
        try {
            mapper.readValue(invalidJsonInput, Object.class);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
            assertEquals("{\"field1\":", e.getPathReference());
        }
    }

    /**
     * Test deserialization with complex input.
     */
    @Test
    public void testComplexDeserialization() throws Exception {
        String complexJsonInput = "{\"field1\":1,\"field2\":{\"field3\":[1,2,3],\"field4\":\"value\"}}";
        try {
            mapper.readValue(complexJsonInput, ComplexObject.class);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
            assertEquals("field2->field3[2]", e.getPathReference());
        }
    }

    /**
     * Test invalid reference chain length.
     */
    @Test
    public void testInvalidReferenceChainLength() {
        Object refChainInput = new Object();
        for (int i = 0; i < JsonMappingException.MAX_REFS_TO_LIST + 1; i++) {
            refChainInput = new Object() {{
                field = refChainInput;
            }};
        }
        try {
            mapper.writeValueAsString(refChainInput);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
            assertTrue(e.getMessage().contains("reference chain is too long"));
        }
    }

    private static class ComplexObject {
        Object field1;
        ComplexObject field2;
        int field3;
        String field4;
    }
}