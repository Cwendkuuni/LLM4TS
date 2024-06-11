package com.fasterxml.jackson.core.base;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.Test;

public class GeneratorBaseTest {

    

    @Test
    public void testIsEnabled_WRITE_NUMBERS_AS_STRINGS() {
        GeneratorBase generator = new GeneratorBase(0, null);
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        GeneratorBase generatorWithEnabledFeature = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);
        assertTrue(generatorWithEnabledFeature.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test(expected = IllegalStateException.class)
    public void testClose_AlreadyClosed() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.close();
        generator.close();
    }

    @Test
    public void testWriteString() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.writeString("test");
    }

    @Test(expected = IOException.class)
    public void testWriteRawValue_NullText() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.writeRawValue(null);
    }

    @Test(expected = IOException.class)
    public void testWriteRawValue_EmptyText() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.writeRawValue("");
    }

    @Test
    public void testWriteObject_NullValue() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.writeNull();
    }

    @Test
    public void testWriteObject_StringValue() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        String value = "test";
        generator.writeObject(value);
        // Verify that the generated output matches the expected one
        // (This depends on the actual implementation of writeObject)
    }

    @Test
    public void testWriteObject_BigDecimalValue() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        BigDecimal value = new BigDecimal("1234567890.123456");
        generator.writeObject(value);
        // Verify that the generated output matches the expected one
        // (This depends on the actual implementation of writeObject)
    }

    @Test(expected = IOException.class)
    public void testWriteObject_InvalidValue() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        Object invalidValue = new Object(); // Replace with an invalid value
        generator.writeObject(invalidValue);
    }

    @Test
    public void testUseDefaultPrettyPrinter() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.useDefaultPrettyPrinter();
    }

    
}