package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test class for DefaultPrettyPrinter.
 */
public class DefaultPrettyPrinterTest {

    private static final String DEFAULT_JSON = "{\"name\": \"John\", \"age\": 30, \"cities\": [\"New York\", \"Los Angeles\", \"Chicago\"]}";
    private static final String PRETTY_PRINTED_JSON = "{\n" +
            "  \"name\": \"John\",\n" +
            "  \"age\": 30,\n" +
            "  \"cities\": [\n" +
            "    \"New York\",\n" +
            "    \"Los Angeles\",\n" +
            "    \"Chicago\"\n" +
            "  ]\n" +
            "}";

    @Test
    public void testPrettyPrinting() throws JsonProcessingException, IOException {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(jsonFactory);
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(512), Version.unknownVersion());
        jsonGenerator.setPrettyPrinter(new DefaultPrettyPrinter());
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("name");
        jsonGenerator.writeValue("John");
        jsonGenerator.writeFieldName("age");
        jsonGenerator.writeNumber(30);
        jsonGenerator.writeFieldName("cities");
        jsonGenerator.writeStartArray();
        jsonGenerator.writeValue("New York");
        jsonGenerator.writeValue("Los Angeles");
        jsonGenerator.writeValue("Chicago");
        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();
        jsonGenerator.close();

        ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) jsonGenerator.getOutput();
        String generatedJson = new String(byteArrayOutputStream.toByteArray());
        Assert.assertEquals(PRETTY_PRINTED_JSON, generatedJson);
    }

    @Test
    public void testDefaultRootSeparator() throws JsonProcessingException, IOException {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(jsonFactory);
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(512), Version.unknownVersion());
        jsonGenerator.setPrettyPrinter(new DefaultPrettyPrinter());
        jsonGenerator.writeStartObject();
        jsonGenerator.writeRaw("root");
        jsonGenerator.writeEndObject();
        jsonGenerator.close();

        ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) jsonGenerator.getOutput();
        String generatedJson = new String(byteArrayOutputStream.toByteArray());
        Assert.assertEquals(DEFAULTPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR.getValueAsString(), " root");
        Assert.assertEquals(PRETTY_PRINTED_JSON.substring(0, PRETTY_PRINTED_JSON.indexOf("{\n")), generatedJson.substring(0, generatedJson.indexOf("{\n")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullWriter() {
        JsonGenerator jsonGenerator = null;
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(jsonFactory);
        mapper.setPrettyPrinter(new DefaultPrettyPrinter());
        mapper.writeValueAsString(new Object(), jsonGenerator);
    }

    @Test(expected = IOException.class)
    public void testNegativeNesting() throws JsonProcessingException, IOException {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(jsonFactory);
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(512), Version.unknownVersion());
        jsonGenerator.setPrettyPrinter(new DefaultPrettyPrinter());
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStartObject();
        jsonGenerator.writeEndObject();
        jsonGenerator.writeEndObject();
        jsonGenerator.writeEndObject();
        jsonGenerator.close();
    }
}