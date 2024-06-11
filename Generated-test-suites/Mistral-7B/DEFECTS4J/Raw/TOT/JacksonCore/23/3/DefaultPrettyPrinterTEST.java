package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.ObjectMapper;
import com.fasterxml.jackson.core.io.SerializedString;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for DefaultPrettyPrinter.
 */
public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultPrettyPrinter() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper(jsonFactory);

        // Test with default settings
        DefaultPrettyPrinter defaultPrettyPrinter = objectMapper.getDefaultPrettyPrinter();
        JsonGenerator jsonGenerator = defaultPrettyPrinter.getJsonGenerator();
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("name");
        jsonGenerator.writeValue("John Doe");
        jsonGenerator.writeFieldName("age");
        jsonGenerator.writeValue(30);
        jsonGenerator.writeEndObject();
        jsonGenerator.close();

        String actual = jsonFactory.readValue(new ByteArrayInputStream(jsonGenerator.getOutputBytes()), String.class);
        String expected = "{\n" +
                "  \"name\": \"John Doe\",\n" +
                "  \"age\": 30\n" +
                "}";
        Assert.assertEquals(expected, actual);

        // Test with custom root separator
        DefaultPrettyPrinter customRootSeparatorPrettyPrinter = objectMapper.getFactory()
                .createPrettyPrinter();
        customRootSeparatorPrettyPrinter.withRootSeparator(new SerializedString("---"));
        JsonGenerator customRootSeparatorJsonGenerator = customRootSeparatorPrettyPrinter.getJsonGenerator();
        customRootSeparatorJsonGenerator.writeStartObject();
        customRootSeparatorJsonGenerator.writeFieldName("name");
        customRootSeparatorJsonGenerator.writeValue("John Doe");
        customRootSeparatorJsonGenerator.writeFieldName("age");
        customRootSeparatorJsonGenerator.writeValue(30);
        customRootSeparatorJsonGenerator.writeEndObject();
        customRootSeparatorJsonGenerator.close();

        actual = jsonFactory.readValue(new ByteArrayInputStream(customRootSeparatorJsonGenerator.getOutputBytes()), String.class);
        expected = "---{\n" +
                "  \"name\": \"John Doe\",\n" +
                "  \"age\": 30\n" +
                "}";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIndentArraysWith() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper(jsonFactory);

        DefaultPrettyPrinter defaultPrettyPrinter = objectMapper.getDefaultPrettyPrinter();
        DefaultPrettyPrinter indentArraysWithPrettyPrinter = defaultPrettyPrinter.indentArraysWith(new FixedSpaceIndenter());

        JsonGenerator jsonGenerator = indentArraysWithPrettyPrinter.getJsonGenerator();
        jsonGenerator.writeStartArray();
        jsonGenerator.writeValue("apple");
        jsonGenerator.writeValue("banana");
        jsonGenerator.writeValue("orange");
        jsonGenerator.writeEndArray();
        jsonGenerator.close();

        String actual = jsonFactory.readValue(new ByteArrayInputStream(jsonGenerator.getOutputBytes()), String.class);
        String expected = "[ \"apple\",\n" +
                "  \"banana\",\n" +
                "  \"orange\" ]";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIndentObjectsWith() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper(jsonFactory);

        DefaultPrettyPrinter defaultPrettyPrinter = objectMapper.getDefaultPrettyPrinter();
        DefaultPrettyPrinter indentObjectsWithPrettyPrinter = defaultPrettyPrinter.indentObjectsWith(new DefaultIndenter.SYSTEM_LINEFEED_INSTANCE());

        JsonGenerator jsonGenerator = indentObjectsWithPrettyPrinter.getJsonGenerator();
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("name");
        jsonGenerator.writeValue("John Doe");
        jsonGenerator.writeFieldName("age");
        jsonGenerator.writeValue(30);
        jsonGenerator.writeEndObject();
        jsonGenerator.close();

        String actual = jsonFactory.readValue(new ByteArrayInputStream(jsonGenerator.getOutputBytes()), String.class);
        String expected = "{ \n" +
                "  \"name\": \"John Doe\",\n" +
                "  \"age\": 30\n" +
                "}";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testWithSpacesInObjectEntries() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper(jsonFactory);

        DefaultPrettyPrinter defaultPrettyPrinter = objectMapper.getDefaultPrettyPrinter();
        DefaultPrettyPrinter withSpacesInObjectEntriesPrettyPrinter = defaultPrettyPrinter.withSpacesInObjectEntries();

        JsonGenerator jsonGenerator = withSpacesInObjectEntriesPrettyPrinter.getJsonGenerator();
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("name");
        jsonGenerator.writeValue("John Doe");
        jsonGenerator.writeFieldName("age");
        jsonGenerator.writeValue(30);
        jsonGenerator.writeEndObject();
        jsonGenerator.close();

        String actual = jsonFactory.readValue(new ByteArrayInputStream(jsonGenerator.getOutputBytes()), String.class);
        String expected = "{ \n" +
                "  \"name\": \"John Doe\", \n" +
                "  \"age\": 30 \n" +
                "}";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testWithoutSpacesInObjectEntries() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper(jsonFactory);

        DefaultPrettyPrinter defaultPrettyPrinter = objectMapper.getDefaultPrettyPrinter();
        DefaultPrettyPrinter withoutSpacesInObjectEntriesPrettyPrinter = defaultPrettyPrinter.withoutSpacesInObjectEntries();

        JsonGenerator jsonGenerator = withoutSpacesInObjectEntriesPrettyPrinter.getJsonGenerator();
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("name");
        jsonGenerator.writeValue("John Doe");
        jsonGenerator.writeFieldName("age");
        jsonGenerator.writeValue(30);
        jsonGenerator.writeEndObject();
        jsonGenerator.close();

        String actual = jsonFactory.readValue(new ByteArrayInputStream(jsonGenerator.getOutputBytes()), String.class);
        String expected = "{ \"name\": \"John Doe\", \"age\": 30 }";
        Assert.assertEquals(expected, actual);
    }
}