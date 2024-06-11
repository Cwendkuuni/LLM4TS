package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

public class JsonGeneratorImplTest {

    @Test
    public void testWriteStringField_ValidField_ValidValue() throws IOException {
        // Arrange
        JsonFactory jsonFactory = new JsonFactory();
        StringWriter writer = new StringWriter();
        JsonGenerator generator = jsonFactory.createGenerator(writer);
        JsonGeneratorImpl generatorImpl = new JsonGeneratorImpl(null, 0, null) {
            @Override
            protected void _flushBuffer() throws IOException {
                // Do nothing
            }
        };
        generatorImpl._rootValueSeparator = new DefaultPrettyPrinter().getRootValueSeparator();
        generatorImpl._outputEscapes = new int[128];
        generatorImpl._cfgUnqNames = false;
        generatorImpl._maximumNonEscapedChar = 127;

        String fieldName = "name";
        String fieldValue = "John Doe";

        // Act
        generatorImpl.writeStringField(fieldName, fieldValue);
        generator.flush();
        generator.close();

        // Assert
        String expectedOutput = "{\"name\":\"John Doe\"}";
        assertEquals(expectedOutput, writer.toString());
    }

    @Test
    public void testWriteStringField_NullField_ValidValue() throws IOException {
        // Arrange
        JsonFactory jsonFactory = new JsonFactory();
        StringWriter writer = new StringWriter();
        JsonGenerator generator = jsonFactory.createGenerator(writer);
        JsonGeneratorImpl generatorImpl = new JsonGeneratorImpl(null, 0, null) {
            @Override
            protected void _flushBuffer() throws IOException {
                // Do nothing
            }
        };
        generatorImpl._rootValueSeparator = new DefaultPrettyPrinter().getRootValueSeparator();
        generatorImpl._outputEscapes = new int[128];
        generatorImpl._cfgUnqNames = false;
        generatorImpl._maximumNonEscapedChar = 127;

        String fieldName = null;
        String fieldValue = "John Doe";

        // Act
        generatorImpl.writeStringField(fieldName, fieldValue);
        generator.flush();
        generator.close();

        // Assert
        String expectedOutput = "{\"null\":\"John Doe\"}";
        assertEquals(expectedOutput, writer.toString());
    }

    @Test
    public void testWriteStringField_ValidField_NullValue() throws IOException {
        // Arrange
        JsonFactory jsonFactory = new JsonFactory();
        StringWriter writer = new StringWriter();
        JsonGenerator generator = jsonFactory.createGenerator(writer);
        JsonGeneratorImpl generatorImpl = new JsonGeneratorImpl(null, 0, null) {
            @Override
            protected void _flushBuffer() throws IOException {
                // Do nothing
            }
        };
        generatorImpl._rootValueSeparator = new DefaultPrettyPrinter().getRootValueSeparator();
        generatorImpl._outputEscapes = new int[128];
        generatorImpl._cfgUnqNames = false;
        generatorImpl._maximumNonEscapedChar = 127;

        String fieldName = "name";
        String fieldValue = null;

        // Act
        generatorImpl.writeStringField(fieldName, fieldValue);
        generator.flush();
        generator.close();

        // Assert
        String expectedOutput = "{\"name\":null}";
        assertEquals(expectedOutput, writer.toString());
    }

    @Test
    public void testSetRootValueSeparator_ValidValue_ValidOutput() throws IOException {
        // Arrange
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonGenerator generator = new JsonFactory().createGenerator(outputStream, JsonEncoding.UTF8);
        JsonGeneratorImpl generatorImpl = new JsonGeneratorImpl(null, 0, null) {
            @Override
            protected void _flushBuffer() throws IOException {
                // Do nothing
            }
        };
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        SerializableString rootValueSeparator = prettyPrinter.getRootValueSeparator();
        generatorImpl._rootValueSeparator = rootValueSeparator;
        generatorImpl._outputEscapes = new int[128];
        generatorImpl._cfgUnqNames = false;
        generatorImpl._maximumNonEscapedChar = 127;

        String separator = "@@";

        // Act
        generatorImpl.setRootValueSeparator(prettyPrinter.getRootValueSeparator());
        generator.writeStartObject();
        generator.writeBooleanField("isEnabled", true);
        generator.writeNumberField("age", 25);
        generator.writeStringField("name", "John Doe");
        generator.writeEndObject();
        generator.flush();
        generator.close();

        // Assert
        String expectedOutput = "{\"isEnabled\":true}" + separator + "{\"age\":25}" + separator + "{\"name\":\"John Doe\"}";
        assertEquals(expectedOutput, outputStream.toString());
    }

    @Test
    public void testEnable_QUOTE_FIELD_NAMES_DisablesQuoting() throws IOException {
        // Arrange
        JsonFactory jsonFactory = new JsonFactory();
        StringWriter writer = new StringWriter();
        JsonGenerator generator = jsonFactory.createGenerator(writer);
        JsonGeneratorImpl generatorImpl = new JsonGeneratorImpl(null, JsonGenerator.Feature.QUOTE_FIELD_NAMES.getMask(), null) {
            @Override
            protected void _flushBuffer() throws IOException {
                // Do nothing
            }
        };
        generatorImpl._rootValueSeparator = new DefaultPrettyPrinter().getRootValueSeparator();
        generatorImpl._outputEscapes = new int[128];
        generatorImpl._cfgUnqNames = false;
        generatorImpl._maximumNonEscapedChar = 127;

        JsonGenerator.Feature feature = JsonGenerator.Feature.QUOTE_FIELD_NAMES;

        // Act
        generatorImpl.enable(feature);
        generatorImpl.writeStringField("name", "John Doe");
        generator.flush();
        generator.close();

        // Assert
        String expectedOutput = "{name:\"John Doe\"}";
        assertEquals(expectedOutput, writer.toString());
    }

    @Test
    public void testGetHighestEscapedChar_DefaultValue_ReturnsZero() {
        // Arrange
        JsonGeneratorImpl generatorImpl = new JsonGeneratorImpl(null, 0, null) {
            @Override
            protected void _flushBuffer() throws IOException {
                // Do nothing
            }
        };

        // Act
        int result = generatorImpl.getHighestEscapedChar();

        // Assert
        assertEquals(0, result);
    }

    @Test
    public void testSetCharacterEscapes_NullEscapes_UseStandardEscapes() {
        // Arrange
        JsonGeneratorImpl generatorImpl = new JsonGeneratorImpl(null, 0, null) {
            @Override
            protected void _flushBuffer() throws IOException {
                // Do nothing
            }
        };
        int[] expectedOutput = CharTypes.get7BitOutputEscapes();

        // Act
        generatorImpl.setCharacterEscapes(null);
        int[] result = generatorImpl._outputEscapes;

        // Assert
        assertNotNull(result);
        assertEquals(expectedOutput.length, result.length);
        for (int i = 0; i < expectedOutput.length; i++) {
            assertEquals(expectedOutput[i], result[i]);
        }
    }

    @Test
    public void testSetCharacterEscapes_CustomEscapes_ValidOutput() throws IOException {
        // Arrange
        JsonFactory jsonFactory = new JsonFactory();
        StringWriter writer = new StringWriter();
        JsonGenerator generator = jsonFactory.createGenerator(writer);
        JsonGeneratorImpl generatorImpl = new JsonGeneratorImpl(null, 0, null) {
            @Override
            protected void _flushBuffer() throws IOException {
                // Do nothing
            }
        };
        generatorImpl._rootValueSeparator = new DefaultPrettyPrinter().getRootValueSeparator();
        generatorImpl._cfgUnqNames = false;
        generatorImpl._maximumNonEscapedChar = 127;

        CharacterEscapes escapes = new CharacterEscapes() {
            private static final long serialVersionUID = 1L;

            @Override
            public int[] getEscapeCodesForAscii() {
                int[] result = new int[128];
                for (int i = 0; i < 128; i++) {
                    result[i] = i;
                }
                return result;
            }

            @Override
            public SerializableString getEscapeSequence(int ch) {
                return null;
            }
        };

        int[] expectedOutput = escapes.getEscapeCodesForAscii();

        // Act
        generatorImpl.setCharacterEscapes(escapes);
        generatorImpl.writeString("Test String");
        generator.flush();
        generator.close();

        // Assert
        String expectedString = "\"Test String\"";
        String expectedOutput = expectedString.substring(0, expectedString.length() - 1) + "\\u0054" + expectedString.charAt(expectedString.length() - 1);
        assertEquals(expectedOutput, writer.toString());
    }

    @Test
    public void testVersion_DefaultValues_ValidOutput() {
        // Arrange
        JsonGeneratorImpl generatorImpl = new JsonGeneratorImpl(null, 0, null) {
            @Override
            protected void _flushBuffer() throws IOException {
                // Do nothing
            }
        };

        // Act
        Version result = generatorImpl.version();

        // Assert
        assertNotNull(result);
        assertEquals("2.1", result.toString());
    }

    @Test
    public void testWriteString_ValidValue_ValidOutput() throws IOException {
        // Arrange
        JsonFactory jsonFactory = new JsonFactory();
        StringWriter writer = new StringWriter();
        JsonGenerator generator = jsonFactory.createGenerator(writer);
        JsonGeneratorImpl generatorImpl = new JsonGeneratorImpl(null, 0, null) {
            @Override
            protected void _flushBuffer() throws IOException {
                // Do nothing
            }
        };
        generatorImpl._rootValueSeparator = new DefaultPrettyPrinter().getRootValueSeparator();
        generatorImpl._outputEscapes = new int[128];
        generatorImpl._cfgUnqNames = false;
        generatorImpl._maximumNonEscapedChar = 127;

        String value = "Test String";

        // Act
        generatorImpl.writeString(value);
        generator.flush();
        generator.close();

        // Assert
        String expectedOutput = "\"" + value + "\"";
        assertEquals(expectedOutput, writer.toString());
    }

    @Test
    public void test_stepByStep() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        StringWriter writer = new StringWriter();
        JsonGenerator generator = jsonFactory.createGenerator(writer);
        JsonGeneratorImpl generatorImpl = new JsonGeneratorImpl(null, 0, null) {
            @Override
            protected void _flushBuffer() throws IOException {
                // Do nothing
            }
        };
        generatorImpl._rootValueSeparator = new DefaultPrettyPrinter().getRootValueSeparator();
        generatorImpl._outputEscapes = new int[128];
        generatorImpl._cfgUnqNames = false;
        generatorImpl._maximumNonEscapedChar = 127;

        // First Test Case
        generatorImpl.writeStringField("name", "John Doe");
        generator.flush();
        generator.close();
        String expectedOutput1 = "{\"name\":\"John Doe\"}";
        assertEquals(expectedOutput1, writer.toString());

        // Second Test Case
        writer.getBuffer().setLength(0);
        generatorImpl.writeStartObject();
        generatorImpl.writeEndObject();
        generator.flush();
        generator.close();
        String expectedOutput2 = "{}";
        assertEquals(expectedOutput2, writer.toString());

        // Third Test Case
        writer.getBuffer().setLength(0);
        generatorImpl.writeString(null);
        generator.flush();
        generator.close();
        String expectedOutput3 = "null";
        assertEquals(expectedOutput3, writer.toString());
    }

}