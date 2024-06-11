package com.fasterxml.jackson.core.util;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Test class for DefaultPrettyPrinter
 */
public class DefaultPrettyPrinterTest {

    private JsonGenerator jsonGenerator;
    private DefaultPrettyPrinter prettyPrinter;

    @Before
    public void setup() throws IOException {
        JsonFactory jsonFactory = new ObjectMapper().getFactory();
        jsonGenerator = jsonFactory.createGenerator(new ByteArrayOutputStream(), null);
        prettyPrinter = new DefaultPrettyPrinter();
    }

    // Test for constructor(DefaultPrettyPrinter base)
    @Test
    public void testConstructorWithBase() {
        DefaultPrettyPrinter base = new DefaultPrettyPrinter();
        DefaultPrettyPrinter prettyPrinterWithBase = new DefaultPrettyPrinter(base);
        assertEquals(base, prettyPrinterWithBase);
    }

    // Test for constructor(DefaultPrettyPrinter base, SerializableString rootSeparator)
    @Test
    public void testConstructorWithBaseAndRootSeparator() {
        DefaultPrettyPrinter base = new DefaultPrettyPrinter();
        SerializedString rootSeparator = new SerializedString(" [ ] ");
        DefaultPrettyPrinter prettyPrinterWithBaseAndRootSeparator = new DefaultPrettyPrinter(base, rootSeparator);
        assertEquals(base, prettyPrinterWithBaseAndRootSeparator);
        assertEquals(rootSeparator, prettyPrinterWithBaseAndRootSeparator.getRootSeparator());
    }

    // Test for withRootSeparator() method
    @Test
    public void testWithRootSeparator() {
        prettyPrinter.withRootSeparator(new SerializedString("abc"));
        assertEquals("abc", prettyPrinter.getRootSeparator().getValueAsString());
    }

    // Test for writeRootValueSeparator() method
    @Test
    public void testWriteRootValueSeparator() throws JsonProcessingException {
        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        String expected = "abc";
        if (prettyPrinter.getRootSeparator() != null) {
            expected = prettyPrinter.getRootSeparator().getValueAsString();
        }
        jsonGenerator.flush();
        String actual = new String(jsonGenerator.getOutputBuffer().toString().getBytes());
        assertEquals(expected, actual.substring(0, expected.length()));
    }

    // Test for indentArraysWith() method
    @Test
    public void testIndentArraysWith() {
        Indenter indenter = new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("indentArraysWith: ");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        };
        prettyPrinter.indentArraysWith(indenter);
        prettyPrinter.writeStartArray(jsonGenerator);
        prettyPrinter.writeArrayValueSeparator(jsonGenerator);
        jsonGenerator.writeRaw("value1");
        prettyPrinter.writeArrayValueSeparator(jsonGenerator);
        jsonGenerator.writeRaw("value2");
        prettyPrinter.writeEndArray(jsonGenerator, 2);
        jsonGenerator.flush();
        String actual = new String(jsonGenerator.getOutputBuffer().toString().getBytes());
        String expected = "indentArraysWith: [\nindentArraysWith: \nindentArraysWith: value1\nindentArraysWith: \nindentArraysWith: value2\n";
        assertEquals(expected, actual);
    }

    // Test for indentObjectsWith() method
    @Test
    public void testIndentObjectsWith() {
        Indenter indenter = new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("indentObjectsWith: ");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        };
        prettyPrinter.indentObjectsWith(indenter);
        prettyPrinter.writeStartObject(jsonGenerator);
        prettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
        jsonGenerator.writeRaw("fieldName1:");
        prettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
        jsonGenerator.writeRaw("value1");
        prettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
        jsonGenerator.writeRaw("fieldName2:");
        prettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
        jsonGenerator.writeRaw("value2");
        prettyPrinter.writeObjectEntrySeparator(jsonGenerator);
        jsonGenerator.writeRaw("fieldName3:");
        prettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
        jsonGenerator.writeRaw("value3");
        prettyPrinter.writeEndObject(jsonGenerator, 3);
        jsonGenerator.flush();
        String actual = new String(jsonGenerator.getOutputBuffer().toString().getBytes());
        String expected = "indentObjectsWith: {\nindentObjectsWith: fieldName1:\nindentObjectsWith: \nindentObjectsWith: value1\nindentObjectsWith: fieldName2:\nindentObjectsWith: \nindentObjectsWith: value2\nindentObjectsWith: fieldName3:\nindentObjectsWith: \nindentObjectsWith: value3\n";
        assertEquals(expected, actual);
    }

    // Test for withSpacesInObjectEntries() method
    @Test
    public void testWithSpacesInObjectEntries() {
        DefaultPrettyPrinter prettyPrinterWithSpacesInObjectEntries = prettyPrinter.withSpacesInObjectEntries();
        prettyPrinterWithSpacesInObjectEntries.writeObjectFieldValueSeparator(jsonGenerator);
        jsonGenerator.writeRaw(":");
        prettyPrinterWithSpacesInObjectEntries.writeObjectEntrySeparator(jsonGenerator);
        jsonGenerator.flush();
        String actual = new String(jsonGenerator.getOutputBuffer().toString().getBytes());
        String expected = ": \n";
        assertEquals(expected, actual);

        DefaultPrettyPrinter prettyPrinterWithoutSpacesInObjectEntries = prettyPrinter.withoutSpacesInObjectEntries();
        prettyPrinterWithoutSpacesInObjectEntries.writeObjectFieldValueSeparator(jsonGenerator);
        jsonGenerator.writeRaw(":");
        prettyPrinterWithoutSpacesInObjectEntries.writeObjectEntrySeparator(jsonGenerator);
        jsonGenerator.flush();
        actual = new String(jsonGenerator.getOutputBuffer().toString().getBytes());
        assertEquals(":", actual);
    }

    // Test for withSeparators() method
    @Test
    public void testWithSeparators() {
        Separators separators = new Separators() {
            @Override
            public String getObjectEntrySeparator() {
                return "objEntrySeparator";
            }

            @Override
            public String getObjectFieldValueSeparator() {
                return "objFieldValueSeparator";
            }
        };
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.withSeparators(separators);
        prettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
        jsonGenerator.writeRaw(":");
        prettyPrinter.writeObjectEntrySeparator(jsonGenerator);
        jsonGenerator.flush();
        String actual = new String(jsonGenerator.getOutputBuffer().toString().getBytes());
        String expected = "objFieldValueSeparator: \nobjEntrySeparator\n";
        assertEquals(expected, actual);
    }
}