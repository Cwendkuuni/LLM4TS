package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultPrettyPrinterTest {

    @Test
    public void testWriteRootValueSeparator() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        CustomJsonGenerator generator = new CustomJsonGenerator();
        generator.setExpectedResult(" ");

        printer.writeRootValueSeparator(generator);
        assertEquals(generator.getGeneratedResult(), " ");
    }

    @Test
    public void testWriteStartObject() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        CustomJsonGenerator generator = new CustomJsonGenerator();
        generator.setExpectedResult("{");

        printer.writeStartObject(generator);
        assertEquals(generator.getGeneratedResult(), "{");
    }

    @Test
    public void testBeforeObjectEntries() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        CustomJsonGenerator generator = new CustomJsonGenerator();
        generator.setExpectedResult("");

        printer.beforeObjectEntries(generator);
        assertEquals(generator.getGeneratedResult(), "");
    }

    @Test
    public void testWriteObjectFieldValueSeparator() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        CustomJsonGenerator generator1 = new CustomJsonGenerator();
        generator1.setSpacesInObjectEntries(true);
        generator1.setExpectedResult(" : ");
        CustomJsonGenerator generator2 = new CustomJsonGenerator();
        generator2.setSpacesInObjectEntries(false);
        generator2.setExpectedResult(":");

        printer.writeObjectFieldValueSeparator(generator1);
        printer.writeObjectFieldValueSeparator(generator2);
        assertEquals(generator1.getGeneratedResult(), " : ");
        assertEquals(generator2.getGeneratedResult(), ":");
    }

    @Test
    public void testWriteObjectEntrySeparator() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        CustomJsonGenerator generator = new CustomJsonGenerator();
        generator.setExpectedResult(", ");

        printer.writeObjectEntrySeparator(generator);
        assertEquals(generator.getGeneratedResult(), ", ");
    }

    @Test
    public void testWriteEndObject() {
        DefaultPrettyPrinter printer1 = new DefaultPrettyPrinter();
        CustomJsonGenerator generator1 = new CustomJsonGenerator();
        generator1.setRetVal(0);
        generator1.setExpectedResult(" }");
        DefaultPrettyPrinter printer2 = new DefaultPrettyPrinter();
        CustomJsonGenerator generator2 = new CustomJsonGenerator();
        generator2.setRetVal(1);
        generator2.setExpectedResult("}");

        printer1.writeEndObject(generator1, 0);
        printer2.writeEndObject(generator2, 1);
        assertEquals(generator1.getGeneratedResult(), " }");
        assertEquals(generator2.getGeneratedResult(), "}");
    }

    @Test
    public void testWriteStartArray() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        CustomJsonGenerator generator = new CustomJsonGenerator();
        generator.setExpectedResult("[");

        printer.writeStartArray(generator);
        assertEquals(generator.getGeneratedResult(), "[");
    }

    @Test
    public void testBeforeArrayValues() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        CustomJsonGenerator generator = new CustomJsonGenerator();
        generator.setExpectedResult("");

        printer.beforeArrayValues(generator);
        assertEquals(generator.getGeneratedResult(), "");
    }

    @Test
    public void testWriteArrayValueSeparator() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        CustomJsonGenerator generator = new CustomJsonGenerator();
        generator.setExpectedResult(", ");

        printer.writeArrayValueSeparator(generator);
        assertEquals(generator.getGeneratedResult(), ", ");
    }

    @Test
    public void testWriteEndArray() {
        DefaultPrettyPrinter printer1 = new DefaultPrettyPrinter();
        CustomJsonGenerator generator1 = new CustomJsonGenerator();
        generator1.setRetVal(0);
        generator1.setExpectedResult(" ]");
        DefaultPrettyPrinter printer2 = new DefaultPrettyPrinter();
        CustomJsonGenerator generator2 = new CustomJsonGenerator();
        generator2.setRetVal(1);
        generator2.setExpectedResult("]");

        printer1.writeEndArray(generator1, 0);
        printer2.writeEndArray(generator2, 1);
        assertEquals(generator1.getGeneratedResult(), " ]");
        assertEquals(generator2.getGeneratedResult(), "]");
    }
}