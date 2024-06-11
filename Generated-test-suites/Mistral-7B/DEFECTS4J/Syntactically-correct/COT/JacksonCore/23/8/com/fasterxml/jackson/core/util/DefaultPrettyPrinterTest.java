package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Test class for DefaultPrettyPrinter.
 */
public class DefaultPrettyPrinterTest {

    /**
     * Test constructor DefaultPrettyPrinter().
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Test
    public void testDefaultConstructor() throws IOException, JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        assertNull(prettyPrinter._rootSeparator);
        jsonGenerator.close();
    }

    /**
     * Test constructor DefaultPrettyPrinter(String rootSeparator).
     * @throws IOException
     * @throws JsonGenerationException
     * @throws JsonProcessingException
     */
    @Test
    public void testStringRootSeparatorConstructor() throws IOException, JsonGenerationException, JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString("test"));
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        assertEquals("test", jsonGenerator.getOutputSaveStream().toString());
        jsonGenerator.close();
    }

    /**
     * Test constructor DefaultPrettyPrinter(SerializableString rootSeparator).
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Test
    public void testSerializableStringRootSeparatorConstructor() throws IOException, JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString("test"));
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        assertEquals("test", jsonGenerator.getOutputSaveStream().toString());
        jsonGenerator.close();
    }

    /**
     * Test constructor DefaultPrettyPrinter(DefaultPrettyPrinter base).
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Test
    public void testDefaultPrettyPrinterBaseConstructor() throws IOException, JsonProcessingException {
        DefaultPrettyPrinter base = new DefaultPrettyPrinter();
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(base);
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        JsonParser jsonParser = new ObjectMapper().getFactory().createParser(jsonGenerator);
        assertEquals(base.writeRootValueSeparator(jsonGenerator), jsonParser.getValueAsString());
        jsonParser.close();
        jsonGenerator.close();
    }

    /**
     * Test constructor DefaultPrettyPrinter(DefaultPrettyPrinter base, SerializableString rootSeparator).
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Test
    public void testDefaultPrettyPrinterBaseSerializableStringRootSeparatorConstructor() throws IOException, JsonProcessingException {
        DefaultPrettyPrinter base = new DefaultPrettyPrinter();
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(base, new SerializedString("test"));
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        assertEquals("test", jsonGenerator.getOutputSaveStream().toString());
        jsonGenerator.close();
    }

    /**
     * Test method withRootSeparator(SerializableString rootSeparator).
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Test
    public void testWithRootSeparator() throws IOException, JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
        DefaultPrettyPrinter newPrettyPrinter = prettyPrinter.withRootSeparator(new SerializedString("test"));
        newPrettyPrinter.writeRootValueSeparator(jsonGenerator);
        assertEquals("test", jsonGenerator.getOutputSaveStream().toString());
        jsonGenerator.close();

        JsonParser jsonParser = new ObjectMapper().getFactory().createParser(jsonGenerator);
        assertEquals("test", jsonParser.getValueAsString());
        jsonParser.close();
    }

    /**
     * Test method withRootSeparator(String rootSeparator).
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Test
    public void testWithRootSeparatorString() throws IOException, JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
        DefaultPrettyPrinter newPrettyPrinter = prettyPrinter.withRootSeparator("test");
        newPrettyPrinter.writeRootValueSeparator(jsonGenerator);
        assertEquals("test", jsonGenerator.getOutputSaveStream().toString());
        jsonGenerator.close();

        JsonParser jsonParser = new ObjectMapper().getFactory().createParser(jsonGenerator);
        assertEquals("test", jsonParser.getValueAsString());
        jsonParser.close();
    }

    /**
     * Test method withArrayIndenter(Indenter i).
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Test
    public void testWithArrayIndenter() throws IOException, JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);

        // Test default array separator
        prettyPrinter.writeStartArray(jsonGenerator);
        prettyPrinter.writeValueAsString(jsonGenerator, "test1");
        prettyPrinter.writeValueAsString(jsonGenerator, "test2");
        prettyPrinter.writeEndArray(jsonGenerator);
        assertEquals("[test1, test2]", jsonGenerator.getOutputSaveStream().toString());
        jsonGenerator.close();

        // Test custom array separator
        Indenter customIndenter = new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("[");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        };
        DefaultPrettyPrinter newPrettyPrinter = prettyPrinter.withArrayIndenter(customIndenter);
        JsonGenerator jsonGenerator2 = new JsonFactory().createGenerator(System.out);
        newPrettyPrinter.writeStartArray(jsonGenerator2);
        newPrettyPrinter.writeValueAsString(jsonGenerator2, "test1");
        newPrettyPrinter.writeValueAsString(jsonGenerator2, "test2");
        newPrettyPrinter.writeEndArray(jsonGenerator2);
        assertEquals("[test1,\n test2]", jsonGenerator2.getOutputSaveStream().toString());
        jsonGenerator2.close();
    }

    /**
     * Test method withObjectIndenter(Indenter i).
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Test
    public void testWithObjectIndenter() throws IOException, JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);

        // Test default object separator
        prettyPrinter.writeStartObject(jsonGenerator);
        prettyPrinter.writeKeyValueSeparator("key1", "value1");
        prettyPrinter.writeKeyValueSeparator("key2", "value2");
        prettyPrinter.writeEndObject(jsonGenerator, 2);
        assertEquals("{\n \"key1\": \"value1\",\n \"key2\": \"value2\"\n}", jsonGenerator.getOutputSaveStream().toString());
        jsonGenerator.close();

        // Test custom object separator
        Indenter customIndenter = new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("{\n ");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        };
        DefaultPrettyPrinter newPrettyPrinter = prettyPrinter.withObjectIndenter(customIndenter);
        JsonGenerator jsonGenerator2 = new JsonFactory().createGenerator(System.out);
        newPrettyPrinter.writeStartObject(jsonGenerator2);
        newPrettyPrinter.writeKeyValueSeparator("key1", "value1");
        newPrettyPrinter.writeKeyValueSeparator("key2", "value2");
        newPrettyPrinter.writeEndObject(jsonGenerator2, 2);
        assertEquals("{\n  \"key1\": \"value1\",\n  \"key2\": \"value2\"\n}", jsonGenerator2.getOutputSaveStream().toString());
        jsonGenerator2.close();
    }

    /**
     * Test method withSpacesInObjectEntries().
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Test
    public void testWithSpacesInObjectEntries() throws IOException, JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);

        // Test default object separator with spaces
        prettyPrinter.writeStartObject(jsonGenerator);
        prettyPrinter.writeKeyValueSeparator("key1", "value1");
        prettyPrinter.writeKeyValueSeparator("key2", "value2");
        prettyPrinter.writeEndObject(jsonGenerator, 2);
        assertEquals("{\n  \"key1\": \"value1\",\n  \"key2\": \"value2\"\n}", jsonGenerator.getOutputSaveStream().toString());
        jsonGenerator.close();

        // Test object separator with spaces enabled
        DefaultPrettyPrinter newPrettyPrinter = prettyPrinter.withSpacesInObjectEntries();
        JsonGenerator jsonGenerator2 = new JsonFactory().createGenerator(System.out);
        newPrettyPrinter.writeStartObject(jsonGenerator2);
        newPrettyPrinter.writeKeyValueSeparator("key1", "value1");
        newPrettyPrinter.writeKeyValueSeparator("key2", "value2");
        newPrettyPrinter.writeEndObject(jsonGenerator2, 2);
        assertEquals("{\n  \"key1\": \"value1\",\n  \"key2\": \"value2\"\n}", jsonGenerator2.getOutputSaveStream().toString());
        jsonGenerator2.close();

        // Test object separator with spaces disabled
        DefaultPrettyPrinter newPrettyPrinter2 = prettyPrinter.withoutSpacesInObjectEntries();
        JsonGenerator jsonGenerator3 = new JsonFactory().createGenerator(System.out);
        newPrettyPrinter2.writeStartObject(jsonGenerator3);
        newPrettyPrinter2.writeKeyValueSeparator("key1", "value1");
        newPrettyPrinter2.writeKeyValueSeparator("key2", "value2");
        newPrettyPrinter2.writeEndObject(jsonGenerator3, 2);
        assertEquals("{\"key1\": \"value1\",\"key2\": \"value2\"}", jsonGenerator3.getOutputSaveStream().toString());
        jsonGenerator3.close();
    }

    /**
     * Test method withSeparators(Separators separators).
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Test
    public void testWithSeparators() throws IOException, JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);

        // Test default separators
        prettyPrinter.writeStartObject(jsonGenerator);
        prettyPrinter.writeKeyValueSeparator("key1", "value1");
        prettyPrinter.writeKeyValueSeparator("key2", "value2");
        prettyPrinter.writeEndObject(jsonGenerator, 2);
        assertEquals("{\n  \"key1\": \"value1\",\n  \"key2\": \"value2\"\n}", jsonGenerator.getOutputSaveStream().toString());
        jsonGenerator.close();

        // Test custom separators
        Separators customSeparators = new Separators() {
            @Override
            public String getObjectEntrySeparator() {
                return "|";
            }

            @Override
            public String getObjectFieldValueSeparator() {
                return ":";
            }
        };
        DefaultPrettyPrinter newPrettyPrinter = prettyPrinter.withSeparators(customSeparators);
        JsonGenerator jsonGenerator2 = new JsonFactory().createGenerator(System.out);
        newPrettyPrinter.writeStartObject(jsonGenerator2);
        newPrettyPrinter.writeKeyValueSeparator("key1", "value1");
        newPrettyPrinter.writeKeyValueSeparator("key2", "value2");
        newPrettyPrinter.writeEndObject(jsonGenerator2, 2);
        assertEquals("{\n  \"key1\": \"value1\",\n  \"key2\": \"value2\"\n}", jsonGenerator2.getOutputSaveStream().toString());
        jsonGenerator2.close();

        // Test custom separators with spaces
        Separators customSeparatorsWithSpaces = new Separators() {
            @Override
            public String getObjectEntrySeparator() {
                return "|";
            }

            @Override
            public String getObjectFieldValueSeparator() {
                return ": ";
            }
        };
        DefaultPrettyPrinter newPrettyPrinter2 = prettyPrinter.withSeparators(customSeparatorsWithSpaces);
        JsonGenerator jsonGenerator3 = new JsonFactory().createGenerator(System.out);
        newPrettyPrinter2.writeStartObject(jsonGenerator3);
        newPrettyPrinter2.writeKeyValueSeparator("key1", "value1");
        newPrettyPrinter2.writeKeyValueSeparator("key2", "value2");
        newPrettyPrinter2.writeEndObject(jsonGenerator3, 2);
        assertEquals("{\n  \"key1\": \"value1\",\n  \"key2\": \"value2\"\n}", jsonGenerator3.getOutputSaveStream().toString());
        jsonGenerator3.close();

        // Test custom separators without spaces
        Separators customSeparatorsWithoutSpaces = new Separators() {
            @Override
            public String getObjectEntrySeparator() {
                return "|";
            }

            @Override
            public String getObjectFieldValueSeparator() {
                return ":";
            }
        };
        DefaultPrettyPrinter newPrettyPrinter3 = prettyPrinter.withSeparators(customSeparatorsWithoutSpaces);
        JsonGenerator jsonGenerator4 = new JsonFactory().createGenerator(System.out);
        newPrettyPrinter3.writeStartObject(jsonGenerator4);
        newPrettyPrinter3.writeKeyValueSeparator("key1", "value1");
        newPrettyPrinter3.writeKeyValueSeparator("key2", "value2");
        newPrettyPrinter3.writeEndObject(jsonGenerator4, 2);
        assertEquals("{\n  \"key1\":\"value1\",\n  \"key2\":\"value2\"\n}", jsonGenerator4.getOutputSaveStream().toString());
        jsonGenerator4.close();
    }

    
}