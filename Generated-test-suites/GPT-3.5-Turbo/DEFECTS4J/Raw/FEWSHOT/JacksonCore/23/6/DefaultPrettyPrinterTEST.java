package com.fasterxml.jackson.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DefaultPrettyPrinterTest {

    @Test
    public void testWriteRootValueSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JSONGeneratorStub generator = new JSONGeneratorStub();

        prettyPrinter.writeRootValueSeparator(generator);
        assertEquals(' ', generator.getLastChar());
    }

    @Test
    public void testWriteStartObject() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JSONGeneratorStub generator = new JSONGeneratorStub();

        prettyPrinter.writeStartObject(generator);
        assertEquals('{', generator.getLastChar());
        assertEquals(1, prettyPrinter.getNesting());
    }

    @Test
    public void testBeforeObjectEntries() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JSONGeneratorStub generator = new JSONGeneratorStub();

        prettyPrinter.beforeObjectEntries(generator);
        assertEquals("", generator.getLastOutput());
    }

    @Test
    public void testWriteObjectFieldValueSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        separator.setObjectFieldValueSeparator(":");
        JSONGeneratorStub generator = new JSONGeneratorStub();

        prettyPrinter.writeObjectFieldValueSeparator(generator);
        assertEquals(" :", generator.getLastOutput());
    }

    @Test
    public void testWriteObjectEntrySeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        separator.setObjectEntrySeparator(",");
        JSONGeneratorStub generator = new JSONGeneratorStub();

        prettyPrinter.writeObjectEntrySeparator(generator);
        assertEquals(",", generator.getLastOutput());
    }

    @Test
    public void testWriteEndObject() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JSONGeneratorStub generator = new JSONGeneratorStub();

        prettyPrinter.writeEndObject(generator, 0);
        assertEquals(" }", generator.getLastOutput());
        assertEquals(0, prettyPrinter.getNesting());
    }

    @Test
    public void testWriteStartArray() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JSONGeneratorStub generator = new JSONGeneratorStub();

        prettyPrinter.writeStartArray(generator);
        assertEquals('[', generator.getLastChar());
        assertEquals(1, prettyPrinter.getNesting());
    }

    @Test
    public void testBeforeArrayValues() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JSONGeneratorStub generator = new JSONGeneratorStub();

        prettyPrinter.beforeArrayValues(generator);
        assertEquals("", generator.getLastOutput());
    }

    @Test
    public void testWriteArrayValueSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        separator.setArrayValueSeparator(",");
        JSONGeneratorStub generator = new JSONGeneratorStub();

        prettyPrinter.writeArrayValueSeparator(generator);
        assertEquals(",", generator.getLastOutput());
    }

    @Test
    public void testWriteEndArray() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JSONGeneratorStub generator = new JSONGeneratorStub();

        prettyPrinter.writeEndArray(generator, 0);
        assertEquals(" ]", generator.getLastOutput());
        assertEquals(0, prettyPrinter.getNesting());
    }


}