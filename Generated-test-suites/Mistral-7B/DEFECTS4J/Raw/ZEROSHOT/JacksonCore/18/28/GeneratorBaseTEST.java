package com.fasterxml.jackson.core.base;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.json.DupDetector;
import com.fasterxml.jackson.core.json.JsonWriteContext;
import com.fasterxml.jackson.core.util.VersionUtil;

public class GeneratorBaseTest {

    @Test
    public void testDefaultFeatures() throws IOException {
        GeneratorBase gen = new GeneratorBase(0, null);
        assertFalse(gen.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(gen.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertFalse(gen.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
    }

    @Test
    public void testFeatures() throws IOException {
        int features = Feature.WRITE_NUMBERS_AS_STRINGS.getMask()
                | Feature.ESCAPE_NON_ASCII.getMask()
                | Feature.STRICT_DUPLICATE_DETECTION.getMask();
        GeneratorBase gen = new GeneratorBase(features, null);
        assertTrue(gen.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(gen.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertTrue(gen.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
    }

    @Test
    public void testVersion() throws IOException {
        GeneratorBase gen = new GeneratorBase(0, null);
        assertEquals("unknown", gen.version().toString());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        gen.writeStartObject();
        gen.writeFieldName("version");
        gen.writeValue(VersionUtil.versionFor(GeneratorBase.class));
        gen.writeEndObject();
        String json = new String(out.toByteArray());
        JsonParser parser = new ObjectMapper().getFactory().createParser(new ByteArrayInputStream(json.getBytes()));
        Object result = parser.readValueAs(Object.class);
        assertEquals(VersionUtil.versionFor(GeneratorBase.class), result);
    }

    @Test
    public void testConfiguration() throws IOException {
        GeneratorBase gen = new GeneratorBase(0, null);
        assertFalse(gen.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        gen.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(gen.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        gen.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(gen.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void testWriteContext() throws IOException {
        DupDetector dupDetector = new DupDetector();
        GeneratorBase gen = new GeneratorBase(0, null, JsonWriteContext.createRootContext(dupDetector));
        assertNotNull(gen._writeContext.getDupDetector());
        JsonWriteContext contextWithNoDetector = JsonWriteContext.createRootContext(null);
        GeneratorBase gen2 = new GeneratorBase(0, null, contextWithNoDetector);
        assertNull(gen2._writeContext.getDupDetector());
    }

    @Test
    public void testClosed() throws IOException {
        GeneratorBase gen = new GeneratorBase(0, null);
        assertFalse(gen.isClosed());
        gen.close();
        assertTrue(gen.isClosed());
    }

    @Test
    public void testWriteString() throws IOException {
        GeneratorBase gen = new GeneratorBase(0, null);
        gen.writeString("test");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream input = new ByteArrayInputStream(gen.getOutputContext().getBufferedString().getBytes());
        JsonParser parser = new ObjectMapper().getFactory().createParser(input);
        assertEquals("test", parser.readValueAs(String.class));
    }

    @Test
    public void testWriteRawValue() throws IOException {
        GeneratorBase gen = new GeneratorBase(0, null);
        String text = "test";
        gen.writeRawValue(text);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream input = new ByteArrayInputStream(gen.getOutputContext().getBufferedString().getBytes());
        JsonParser parser = new ObjectMapper().getFactory().createParser(input);
        assertEquals(text, parser.readValueAs(String.class));
    }

    @Test
    public void testWriteBinary() throws IOException {
        // Not implemented at this level
        GeneratorBase gen = new GeneratorBase(0, null);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("test".getBytes());
        gen.writeBinary(null, input, 4);
        assertEquals(0, gen.getOutputContext().getNumberOfBytesWritten());
    }

    @Test
    public void testWriteNumber() throws IOException {
        // Not implemented at this level
        GeneratorBase gen = new GeneratorBase(0, null);
        gen.writeNumber(1);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream input = new ByteArrayInputStream(gen.getOutputContext().getBufferedString().getBytes());
        JsonParser parser = new ObjectMapper().getFactory().createParser(input);
        assertEquals(1, parser.readValueAs(Integer.class));
    }

    @Test
    public void testWriteBoolean() throws IOException {
        // Not implemented at this level
        GeneratorBase gen = new GeneratorBase(0, null);
        gen.writeBoolean(true);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream input = new ByteArrayInputStream(gen.getOutputContext().getBufferedString().getBytes());
        JsonParser parser = new ObjectMapper().getFactory().createParser(input);
        assertEquals(true, parser.readValueAs(Boolean.class));
    }

    @Test
    public void testWriteNull() throws IOException {
        GeneratorBase gen = new GeneratorBase(0, null);
        gen.writeNull();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream input = new ByteArrayInputStream(gen.getOutputContext().getBufferedString().getBytes());
        JsonParser parser = new ObjectMapper().getFactory().createParser(input);
        assertNull(parser.readValueAs(Object.class));
    }

    @Test
    public void testWriteObject() throws IOException {
        GeneratorBase gen = new GeneratorBase(0, null);
        String json = "{\"name\": \"test\", \"age\": 12}";
        gen.writeObject(new ObjectMapper().readValue(new ByteArrayInputStream(json.getBytes()), Object.class));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream input = new ByteArrayInputStream(gen.getOutputContext().getBufferedString().getBytes());
        JsonParser parser = new ObjectMapper().getFactory().createParser(input);
        Object result = parser.readValueAs(Object.class);
        assertEquals(new ObjectMapper().readValue(new ByteArrayInputStream(json.getBytes()), Object.class), result);
    }

    @Test
    public void testWriteTree() throws IOException {
        GeneratorBase gen = new GeneratorBase(0, null);
        String json = "{\"name\": \"test\", \"age\": 12}";
        TreeNode node = new ObjectMapper().readTree(new ByteArrayInputStream(json.getBytes()));
        gen.writeTree(node);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream input = new ByteArrayInputStream(gen.getOutputContext().getBufferedString().getBytes());
        JsonParser parser = new ObjectMapper().getFactory().createParser(input);
        Object result = parser.readValueAs(Object.class);
        assertEquals(new ObjectMapper().readValue(new ByteArrayInputStream(json.getBytes()), Object.class), result);
    }

    @Test
    public void testWriteValueWithDerivedFeatures() throws IOException {
        int features = Feature.WRITE_NUMBERS_AS_STRINGS.getMask()
                | Feature.ESCAPE_NON_ASCII.getMask()
                | Feature.STRICT_DUPLICATE_DETECTION.getMask();
        GeneratorBase gen = new GeneratorBase(features, null);
        gen.writeString("test");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream input = new ByteArrayInputStream(gen.getOutputContext().getBufferedString().getBytes());
        JsonParser parser = new ObjectMapper().getFactory().createParser(input);
        assertEquals("test", parser.readValueAs(String.class));
        gen.setHighestNonEscapedChar(128);
        gen.writeString("test");
        out.reset();
        gen.writeTo(out);
        String output = new String(out.toByteArray());
        assertTrue(output.contains("\\u0080"));
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        gen.writeNumber(123);
        assertFalse(parser.readValueAs(Number.class) instanceof Number);
        out2.reset();
        gen.writeTo(out2);
        String output2 = new String(out2.toByteArray());
        assertTrue(output2.contains("\"123\""));
    }

    @Test
    public void testWriteValueWithoutDerivedFeatures() throws IOException {
        GeneratorBase gen = new GeneratorBase(0, null);
        gen.writeString("test");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream input = new ByteArrayInputStream(gen.getOutputContext().getBufferedString().getBytes());
        JsonParser parser = new ObjectMapper().getFactory().createParser(input);
        assertEquals("test", parser.readValueAs(String.class));
        gen.setHighestNonEscapedChar(128);
        gen.writeString("test");
        out.reset();
        gen.writeTo(out);
        String output = new String(out.toByteArray());
        assertFalse(output.contains("\\u0080"));
        gen.writeNumber(123);
        assertEquals(123, parser.readValueAs(Number.class).longValue());
    }

    @Test
    public void testWriteValueWithWriteNumbersAsStrings() throws IOException {
        GeneratorBase gen = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);
        gen.writeNumber(123);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream input = new ByteArrayInputStream(gen.getOutputContext().getBufferedString().getBytes());
        JsonParser parser = new ObjectMapper().getFactory().createParser(input);
        assertEquals("123", parser.readValueAs(String.class));
    }

    @Test
    public void testWriteValueWithEscapeNonAscii() throws IOException {
        GeneratorBase gen = new GeneratorBase(Feature.ESCAPE_NON_ASCII.getMask(), null);
        gen.writeString("test");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream input = new ByteArrayInputStream(gen.getOutputContext().getBufferedString().getBytes());
        JsonParser parser = new ObjectMapper().getFactory().createParser(input);
        assertEquals("test", parser.readValueAs(String.class));
        gen.setHighestNonEscapedChar(128);
        gen.writeString("test");
        out.reset();
        gen.writeTo(out);
        String output = new String(out.toByteArray());
        assertTrue(output.contains("\\u0080"));
    }

    @Test
    public void testWriteValueWithStrictDuplicateDetection() throws IOException {
        GeneratorBase gen = new GeneratorBase(Feature.STRICT_DUPLICATE_Detection.getMask(), null, new DupDetector());
        String json = "[1, 1, 2, 1]";
        TreeNode root = new ObjectMapper().readTree(new ByteArrayInputStream(json.getBytes()));
        gen.writeTree(root);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream input = new ByteArrayInputStream(gen.getOutputContext().getBufferedString().getBytes());
        JsonParser parser = new ObjectMapper().getFactory().createParser(input);
        Object result = parser.readValueAs(Object.class);
        TreeNode expected = new ObjectMapper().readValue(new ByteArrayInputStream(json.getBytes()), TreeNode.class);
        assertEquals(expected, result);
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteValueWithStrictDuplicateDetectionThrowsException() throws IOException {
        GeneratorBase gen = new GeneratorBase(Feature.STRICT_DUPLICATE_Detection.getMask(), null, new DupDetector());
        String json = "[1, 1, 2, 1]";
        TreeNode root = new ObjectMapper().readTree(new ByteArrayInputStream(json.getBytes()));
        gen.writeTree(root);
        gen.writeTree(root);
    }
}