package com.fasterxml.jackson.core.base;

import com.fasterxml.jackson.core.JsonGenerationSchema;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;

/**
 * Test cases for {@link GeneratorBase} class.
 */
public class GeneratorBaseTest {

    private GeneratorBase jsonGenerator;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerationSchema schema = mapper.getSerializationConfig().getDefaultSerializationSchema();
        jsonGenerator = schema.createGenerator(new OutputStreamWriter(new ByteArrayOutputStream()), null);
        outputStream = (ByteArrayOutputStream) jsonGenerator.getOutputFactory().createOutput(new OutputStreamWriter(outputStream), null);
    }

    /**
     * Test for {@link GeneratorBase#writeString(String)} method.
     */
    @Test
    public void testWriteString() throws IOException {
        jsonGenerator.writeString("Hello, World!");
        String actual = outputStream.toString();
        String expected = "\"Hello, World!\"";
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test for {@link GeneratorBase#writeRawValue(String)} method with valid UTF-8 encoded string.
     */
    @Test
    public void testWriteRawValueWithValidUTF8String() throws IOException {
        String text = "Hello, 世界!";
        jsonGenerator.writeRawValue(text);
        String actual = outputStream.toString();
        String expected = "\"Hello, 世界!\"".replace("\\u", "\\u00");
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test for {@link GeneratorBase#writeRawValue(String)} method with invalid UTF-8 encoded string.
     */
    @Test(expected = JsonProcessingException.class)
    public void testWriteRawValueWithInvalidUTF8String() throws IOException {
        String text = "Hello, 世界!\u0001";
        jsonGenerator.writeRawValue(text);
    }

    /**
     * Test for {@link GeneratorBase#writeNumber(int)} method.
     */
    @Test
    public void testWriteNumberInt() throws IOException {
        jsonGenerator.writeNumber(123);
        String actual = outputStream.toString();
        String expected = "123";
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test for {@link GeneratorBase#writeNumber(long)} method.
     */
    @Test
    public void testWriteNumberLong() throws IOException {
        jsonGenerator.writeNumber(1234567890123L);
        String actual = outputStream.toString();
        String expected = "1234567890123";
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test for {@link GeneratorBase#writeNumber(double)} method.
     */
    @Test
    public void testWriteNumberDouble() throws IOException {
        jsonGenerator.writeNumber(123.45);
        String actual = outputStream.toString();
        String expected = "123.45";
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test for {@link GeneratorBase#writeNumber(BigDecimal)} method.
     */
    @Test
    public void testWriteNumberBigDecimal() throws IOException {
        BigDecimal bigDecimal = new BigDecimal("123.45");
        jsonGenerator.writeNumber(bigDecimal);
        String actual = outputStream.toString();
        String expected = "123.45";
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test for {@link GeneratorBase#writeBoolean(boolean)} method.
     */
    @Test
    public void testWriteBoolean() throws IOException {
        jsonGenerator.writeBoolean(true);
        String actual = outputStream.toString();
        String expected = "true";
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test for {@link GeneratorBase#writeNull()} method.
     */
    @Test
    public void testWriteNull() throws IOException {
        jsonGenerator.writeNull();
        String actual = outputStream.toString();
        String expected = "null";
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test for {@link GeneratorBase#writeObject(Object)} method with valid object.
     */
    @Test
    public void testWriteObject() throws IOException, JsonProcessingException {
        Object obj = new Object() {{
            String name = "John Doe";
        }};
        jsonGenerator.writeObject(obj);
        String actual = outputStream.toString();
        String expected = "{\"name\":\"John Doe\"}";
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test for {@link GeneratorBase#writeObject(Object)} method with null object.
     */
    @Test
    public void testWriteObjectNull() throws IOException {
        jsonGenerator.writeObject(null);
        String actual = outputStream.toString();
        String expected = "null";
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test for {@link GeneratorBase#writeArray()}/{@link GeneratorBase#writeEndArray()} methods.
     */
    @Test
    public void testWriteArray() throws IOException {
        jsonGenerator.writeStartArray();
        jsonGenerator.writeNumber(1);
        jsonGenerator.writeNumber(2);
        jsonGenerator.writeNumber(3);
        jsonGenerator.writeEndArray();
        String actual = outputStream.toString();
        String expected = "[1,2,3]";
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test for {@link GeneratorBase#writeObject(Object)} method with empty array.
     */
    @Test
    public void testWriteEmptyArray() throws IOException {
        jsonGenerator.writeStartArray();
        jsonGenerator.writeEndArray();
        String actual = outputStream.toString();
        String expected = "[]";
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test for {@link GeneratorBase#writeObject(Object)} method with null array.
     */
    @Test
    public void testWriteNullArray() throws IOException {
        jsonGenerator.writeStartArray();
        jsonGenerator.writeEndArray();
        String actual = outputStream.toString();
        String expected = "null";
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test for {@link GeneratorBase#writeObject(Object)} method with nested objects.
     */
    @Test
    public void testWriteNestedObjects() throws IOException, JsonProcessingException {
        Object obj1 = new Object() {{
            String name = "Object 1";
            Object obj2 = new Object() {{
                String name = "Object 2";
            }};
        }};
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("obj1");
        jsonGenerator.writeObject(obj1);
        jsonGenerator.writeEndObject();
        String actual = outputStream.toString();
        String expected = "{\"obj1\":{\"name\":\"Object 1\",\"obj2\":{\"name\":\"Object 2\"}}}";
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test for {@link GeneratorBase#flush()} method.
     */
    @Test
    public void testFlush() throws IOException {
        jsonGenerator.writeString("Hello, World!");
        jsonGenerator.flush();
        String actual = outputStream.toString();
        String expected = "\"Hello, World!\"";
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test for {@link GeneratorBase#close()} method.
     */
    @Test
    public void testClose() throws IOException {
        jsonGenerator.writeString("Hello, World!");
        jsonGenerator.close();
        String actual = outputStream.toString();
        String expected = "\"Hello, World!\"";
        Assert.assertEquals(expected, actual);
    }
}