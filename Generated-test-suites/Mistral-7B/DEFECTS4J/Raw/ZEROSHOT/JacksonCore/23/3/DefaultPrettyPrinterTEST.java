package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

/**
 * Test class for DefaultPrettyPrinter.java.
 */
public class DefaultPrettyPrinterTest {

    /**
     * Test constructor with no root separator.
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Test
    public void testDefaultConstructor() throws IOException, JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(new OutputStreamWriter(byteArrayOutputStream));
        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("name");
        jsonGenerator.writeValue("John Doe");
        jsonGenerator.writeEndObject();
        jsonGenerator.close();
        String jsonString = byteArrayOutputStream.toString();
        assertEquals("{\n  \"name\" : \"John Doe\"\n}", jsonString);
    }

    /**
     * Test constructor with root separator.
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Test
    public void testRootSeparatorConstructor() throws IOException, JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString(": "));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(new OutputStreamWriter(byteArrayOutputStream));
        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("name");
        jsonGenerator.writeValue("John Doe");
        jsonGenerator.writeEndObject();
        jsonGenerator.close();
        String jsonString = byteArrayOutputStream.toString();
        assertEquals("{\n : \n  \"name\" : \"John Doe\"\n}", jsonString);
    }

    /**
     * Test writeStartObject method.
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Test
    public void testWriteStartObject() throws IOException, JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(new OutputStreamWriter(byteArrayOutputStream));
        prettyPrinter.writeStartObject(jsonGenerator);
        jsonGenerator.writeEndObject();
        jsonGenerator.close();
        String jsonString = byteArrayOutputStream.toString();
        assertEquals("{\n}", jsonString);
    }

    /**
     * Test writeEndObject method.
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Test
    public void testWriteEndObject() throws IOException, JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(new OutputStreamWriter(byteArrayOutputStream));
        jsonGenerator.writeStartObject();
        jsonGenerator.writeEndObject();
        jsonGenerator.close();
        String jsonString = byteArrayOutputStream.toString();
        assertEquals("{\n}", jsonString);
    }

    /**
     * Test writeObjectFieldValueSeparator method.
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Test
    public void testWriteObjectFieldValueSeparator() throws IOException, JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(new OutputStreamWriter(byteArrayOutputStream));
        prettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("name");
        jsonGenerator.writeValue("John Doe");
        jsonGenerator.writeEndObject();
        jsonGenerator.close();
        String jsonString = byteArrayOutputStream.toString();
        assertEquals("{\n  \"name\" : \"John Doe\"\n}", jsonString);
    }

    /**
     * Test writeObjectEntrySeparator method.
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Test
    public void testWriteObjectEntrySeparator() throws IOException, JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(new OutputStreamWriter(byteArrayOutputStream));
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("name");
        prettyPrinter.writeObjectEntrySeparator(jsonGenerator);
        jsonGenerator.writeValue("John Doe");
        jsonGenerator.writeEndObject();
        jsonGenerator.close();
        String jsonString = byteArrayOutputStream.toString();
        assertEquals("{\n  \"name\" : \"John Doe\"\n}", jsonString);
    }

    /**
     * Test writeArrayValueSeparator method.
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Test
    public void testWriteArrayValueSeparator() throws IOException, JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(new OutputStreamWriter(byteArrayOutputStream));
        jsonGenerator.writeStartArray();
        jsonGenerator.value("John Doe");
        jsonGenerator.value("Jane Doe");
        jsonGenerator.value("Bob Smith");
        jsonGenerator.close();
        String jsonString = byteArrayOutputStream.toString();
        assertEquals("[ \"John Doe\",\n  \"Jane Doe\",\n  \"Bob Smith\"\n]", jsonString);
    }

    /**
     * Test writeStartArray method.
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Test
    public void testWriteStartArray() throws IOException, JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(new OutputStreamWriter(byteArrayOutputStream));
        jsonGenerator.writeStartArray();
        jsonGenerator.value("John Doe");
        jsonGenerator.value("Jane Doe");
        jsonGenerator.value("Bob Smith");
        jsonGenerator.close();
        String jsonString = byteArrayOutputStream.toString();
        assertEquals("[ \"John Doe\",\n  \"Jane Doe\",\n  \"Bob Smith\"\n]", jsonString);
    }

    /**
     * Test writeEndArray method.
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Test
    public void testWriteEndArray() throws IOException, JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(new OutputStreamWriter(byteArrayOutputStream));
        jsonGenerator.writeStartArray();
        jsonGenerator.value("John Doe");
        jsonGenerator.value("Jane Doe");
        jsonGenerator.value("Bob Smith");
        jsonGenerator.close();
        String jsonString = byteArrayOutputStream.toString();
        assertEquals("[ \"John Doe\",\n  \"Jane Doe\",\n  \"Bob Smith\"\n]", jsonString);
    }

}