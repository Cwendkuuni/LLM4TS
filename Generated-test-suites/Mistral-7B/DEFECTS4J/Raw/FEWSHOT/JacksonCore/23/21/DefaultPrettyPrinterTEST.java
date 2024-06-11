package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.io.SerializedString;
import org.junit.Test;

public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultPrettyPrinter() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator generator = new TestJsonFactory().createGenerator(new StringWriter());
        prettyPrinter.writeRootValueSeparator(generator);
        prettyPrinter.writeStartObject(generator);
        prettyPrinter.beforeObjectEntries(generator);
        assertNull(generator.getOutputWritable()); // check no indentation was written yet

        prettyPrinter.writeObjectFieldName("name");
        assertNull(generator.getOutputWritable());

        prettyPrinter.writeObjectFieldValueSeparator(generator);
        assertNull(generator.getOutputWritable());

        prettyPrinter.writeObjectValue("John Doe");
        assertNull(generator.getOutputWritable());

        prettyPrinter.writeObjectEntrySeparator(generator);
        assertNull(generator.getOutputWritable());

        String actual = generator.toString();
        String expected = "{\n" +
                "  \"name\": \"John Doe\"\n" +
                "}";
        assertEquals(expected, actual);

        generator.close();
    }

    @Test
    public void testDefaultPrettyPrinterWithRootSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString("; "));
        JsonGenerator generator = new TestJsonFactory().createGenerator(new StringWriter());
        prettyPrinter.writeRootValueSeparator(generator);
        prettyPrinter.writeStartObject(generator);
        prettyPrinter.beforeObjectEntries(generator);

        String actual = generator.toString();
        String expected = "; {\n";
        assertEquals(expected, actual);

        generator.close();
    }

    @Test
    public void testIndentArraysWith() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator generator = new TestJsonFactory().createGenerator(new StringWriter());
        prettyPrinter.indentArraysWith(new FixedSpaceIndenter());
        prettyPrinter.writeStartArray(generator);
        prettyPrinter.beforeArrayValues(generator);

        prettyPrinter.writeValue(1);
        prettyPrinter.writeArrayValueSeparator(generator);
        prettyPrinter.writeValue(2);
        prettyPrinter.writeArrayValueSeparator(generator);
        prettyPrinter.writeValue(3);

        String actual = generator.toString();
        String expected = "[ 1,\n 2,\n 3]\n";
        assertEquals(expected, actual);

        generator.close();
    }

    @Test
    public void testIndentObjectsWith() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator generator = new TestJsonFactory().createGenerator(new StringWriter());
        prettyPrinter.indentObjectsWith(new DefaultIndenter.SYSTEM_LINEFEED_INSTANCE());
        prettyPrinter.writeStartObject(generator);
        prettyPrinter.beforeObjectEntries(generator);

        prettyPrinter.writeObjectFieldName("name");
        prettyPrinter.writeObjectFieldValueSeparator(generator);
        prettyPrinter.writeObjectValue("John Doe");
        prettyPrinter.writeObjectEntrySeparator(generator);

        String actual = generator.toString();
        String expected = "{\n" +
                "  \"name\": \"John Doe\"\n" +
                "}";
        assertEquals(expected, actual);

        generator.close();
    }

    @Test
    public void testWithSpacesInObjectEntries() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator generator = new TestJsonFactory().createGenerator(new StringWriter());
        DefaultPrettyPrinter withSpacesInObjectEntries = prettyPrinter.withSpacesInObjectEntries();
        withSpacesInObjectEntries.writeStartObject(generator);
        withSpacesInObjectEntries.beforeObjectEntries(generator);

        withSpacesInObjectEntries.writeObjectFieldName("name");
        withSpacesInObjectEntries.writeObjectFieldValueSeparator(generator);
        withSpacesInObjectEntries.writeObjectValue("John Doe");
        withSpacesInObjectEntries.writeObjectEntrySeparator(generator);

        String actual = generator.toString();
        String expected = "{\n" +
                "  \"name\": \"John Doe\"\n" +
                "}";
        assertEquals(expected, actual);

        generator.close();
    }

    @Test
    public void testWithoutSpacesInObjectEntries() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator generator = new TestJsonFactory().createGenerator(new StringWriter());
        DefaultPrettyPrinter withoutSpacesInObjectEntries = prettyPrinter.withoutSpacesInObjectEntries();
        withoutSpacesInObjectEntries.writeStartObject(generator);
        withoutSpacesInObjectEntries.beforeObjectEntries(generator);

        withoutSpacesInObjectEntries.writeObjectFieldName("name");
        withoutSpacesInObjectEntries.writeObjectFieldValueSeparator(generator);
        withoutSpacesInObjectEntries.writeObjectValue("John Doe");
        withoutSpacesInObjectEntries.writeObjectEntrySeparator(generator);

        String actual = generator.toString();
        String expected = "{\"name\":\"John Doe\"}";
        assertEquals(expected, actual);

        generator.close();
    }

    @Test
    public void testWithSeparators() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        Separators separators = new Separators(true, ",", ":", "[", "]");
        DefaultPrettyPrinter withSeparators = prettyPrinter.withSeparators(separators);
        JsonGenerator generator = new TestJsonFactory().createGenerator(new StringWriter());

        withSeparators.writeStartArray(generator);
        withSeparators.beforeArrayValues(generator);

        withSeparators.writeValue(1);
        withSeparators.writeArrayValueSeparator(generator);
        withSeparators.writeValue(2);
        withSeparators.writeArrayValueSeparator(generator);
        withSeparators.writeValue(3);

        String actual = generator.toString();
        String expected = "[1,\n  2,\n  3]\n";
        assertEquals(expected, actual);

        generator.close();
    }
}