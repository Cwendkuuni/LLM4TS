package com.fasterxml.jackson.core.json;

import org.junit.Test;

import static org.junit.Assert.*;

public class JsonGeneratorImplTest {

    @Test
    public void writeStringFieldTest() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(new IOContext(), 0, null);
        String fieldName = "testField";
        String value = "testValue";

        try {
            generator.writeStringField(fieldName, value);
            // Assert that the field name was written correctly
            assertEquals(fieldName, generator.getCurrentName());
            // Assert that the value was written correctly
            assertEquals(value, generator.getText());
        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }
}