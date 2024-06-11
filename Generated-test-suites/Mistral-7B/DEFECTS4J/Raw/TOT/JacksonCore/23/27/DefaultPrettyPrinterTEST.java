package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DefaultPrettyPrinterTest {

    @Test
    public void testBasicObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        PrettyPrinter prettyPrinter = factory.getDefaultPrettyPrinter();

        String jsonString = "{\"name\":\"John\",\"age\":30}";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(jsonString.getBytes());
        JsonParser parser = factory.createParser(inputStream);
        JsonGenerator generator = parser.getCodec().treeToValues(parser, prettyPrinter);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int read = 0;
        byte[] bytes = new byte[1024];
        while ((read = outputStream.size()) > 0) {
            outputStream.write(bytes, 0, read);
            generator.flush();
            generator.nextToken();
        }

        String prettyPrintedJson = outputStream.toString();
        Assert.assertEquals("{\"name\":\"John\",\"age\":30}", prettyPrintedJson);
    }

    @Test
    public void testEmptyObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        PrettyPrinter prettyPrinter = factory.getDefaultPrettyPrinter();

        String jsonString = "{}";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(jsonString.getBytes());
        JsonParser parser = factory.createParser(inputStream);
        JsonGenerator generator = parser.getCodec().treeToValues(parser, prettyPrinter);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int read = 0;
        byte[] bytes = new byte[1024];
        while ((read = outputStream.size()) > 0) {
            outputStream.write(bytes, 0, read);
            generator.flush();
            generator.nextToken();
        }

        String prettyPrintedJson = outputStream.toString();
        Assert.assertEquals("{}", prettyPrintedJson);
    }

    @Test
    public void testArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        PrettyPrinter prettyPrinter = factory.getDefaultPrettyPrinter();

        String jsonString = "[1,2,3]";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(jsonString.getBytes());
        JsonParser parser = factory.createParser(inputStream);
        JsonGenerator generator = parser.getCodec().treeToValues(parser, prettyPrinter);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int read = 0;
        byte[] bytes = new byte[1024];
        while ((read = outputStream.size()) > 0) {
            outputStream.write(bytes, 0, read);
            generator.flush();
            generator.nextToken();
        }

        String prettyPrintedJson = outputStream.toString();
        Assert.assertEquals("[1, 2, 3]", prettyPrintedJson);
    }

    @Test
    public void testEmptyArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        PrettyPrinter prettyPrinter = factory.getDefaultPrettyPrinter();

        String jsonString = "[]";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(jsonString.getBytes());
        JsonParser parser = factory.createParser(inputStream);
        JsonGenerator generator = parser.getCodec().treeToValues(parser, prettyPrinter);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int read = 0;
        byte[] bytes = new byte[1024];
        while ((read = outputStream.size()) > 0) {
            outputStream.write(bytes, 0, read);
            generator.flush();
            generator.nextToken();
        }

        String prettyPrintedJson = outputStream.toString();
        Assert.assertEquals("[]", prettyPrintedJson);
    }
}