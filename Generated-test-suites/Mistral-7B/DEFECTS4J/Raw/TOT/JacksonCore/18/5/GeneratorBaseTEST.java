package com.fasterxml.jackson.core.base;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.json.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * Test class for {@link GeneratorBase} class.
 */
public class GeneratorBaseTest {

    private JsonGenerator generator;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        JsonFactory factory = new JsonFactory();
        generator = factory.createGenerator(outputStream);
        generator.setCodec(factory);
    }

    @Test
    public void testWriteNull() throws IOException {
        generator.writeNull();
        String json = outputStream.toString();
        Assert.assertEquals("null", json);
    }

    @Test
    public void testWriteBoolean() throws IOException {
        generator.writeBoolean(true);
        String json = outputStream.toString();
        Assert.assertEquals("true", json);

        generator.writeBoolean(false);
        json = outputStream.toString();
        Assert.assertEquals("false", json);
    }

    @Test
    public void testWriteNumber() throws IOException {
        generator.writeNumber(42);
        String json = outputStream.toString();
        Assert.assertEquals("42", json);

        generator.writeNumber(1234567890L);
        json = outputStream.toString();
        Assert.assertEquals("1234567890", json);

        generator.writeNumber(3.14159265358979323846);
        json = outputStream.toString();
        Assert.assertEquals("3.14159265358979323846", json);
    }

    @Test
    public void testWriteString() throws IOException {
        generator.writeString("Hello, World!");
        String json = outputStream.toString();
        Assert.assertEquals("\"Hello, World!\"", json);
    }

    @Test
    public void testWriteRawValue() throws IOException {
        generator.writeRawValue("Hello, World!");
        String json = outputStream.toString();
        Assert.assertEquals("\"Hello, World!\"", json);
    }

    @Test
    public void testWriteRawValueWithOffsetAndLength() throws IOException {
        String input = "Hello, World!";
        generator.writeRawValue(input, 0, input.length());
        String json = outputStream.toString();
        Assert.assertEquals("\"Hello, World!\"", json);
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteObjectWithNullValue() throws IOException {
        generator.writeObject(null);
    }

    @Test
    public void testWriteObject() throws IOException {
        String input = "{\"name\":\"John Doe\",\"age\":30,\"city\":\"New York\"}";
        generator.writeStartObject();
        generator.writeFieldName("name");
        generator.writeString("John Doe");
        generator.writeFieldName("age");
        generator.writeNumber(30);
        generator.writeFieldName("city");
        generator.writeString("New York");
        generator.writeEndObject();

        String json = outputStream.toString();
        Assert.assertEquals(input, json);
    }

    @Test
    public void testWriteBigDecimal() throws IOException {
        BigDecimal bigDecimal = new BigDecimal("1234567890.123456");
        generator.writeNumber(bigDecimal);
        String json = outputStream.toString();
        Assert.assertEquals(bigDecimal.toString(), json);
    }

    @Test
    public void testWriteBinary() throws IOException {
        // Unsupported operation
        generator.writeBinary(JsonEncoding.BASE64_STANDARD, new ByteArrayInputStream("data".getBytes()), "data".getBytes().length);
    }

    @Test(expected = IOException.class)
    public void testCloseTwice() throws IOException {
        generator.close();
        generator.close();
    }

    @Test(expected = IOException.class)
    public void testWriteAfterClose() throws IOException {
        generator.close();
        generator.writeString("Hello, World!");
    }

    @Test(expected = IOException.class)
    public void testWriteNonPrintableCharacter() throws IOException {
        String input = "\u0000";
        generator.writeRawValue(input);
    }

    @Test
    public void testUseDefaultPrettyPrinter() throws IOException {
        generator.useDefaultPrettyPrinter();
        generator.writeStartObject();
        generator.writeFieldName("name");
        generator.writeString("John Doe");
        generator.writeFieldName("age");
        generator.writeNumber(30);
        generator.writeFieldName("city");
        generator.writeString("New York");
        generator.writeEndObject();

        String json = outputStream.toString();
        Assert.assertEquals(
                "{\n" +
                "  \"name\": \"John Doe\",\n" +
                "  \"age\": 30,\n" +
                "  \"city\": \"New York\"\n" +
                "}", json);
    }

    @Test
    public void testWriteSurrogateCharacters() throws IOException {
        // Note: Java 8 JDK does not support writing surrogate characters directly
        // Can be tested in Java 9 or later
        int surr1 = 0xD800;
        int surr2 = 0xDC01;
        String expected = "𐀀";

        generator.writeStartObject();
        generator.writeFieldName("char1");
        generator.writeRawValue(Character.toString((char) _decodeSurrogate(surr1, surr2)));
        generator.writeEndObject();

        String json = outputStream.toString();
        Assert.assertEquals(expected, json);
    }
}