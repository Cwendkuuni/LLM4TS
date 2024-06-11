package com.fasterxml.jackson.core.base;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GeneratorBaseTest {

    private JsonGenerator generator;
    private ObjectMapper mapper;

    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
        generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(1024), null);
        generator.enable(JsonGenerator.Feature.ESCAPE_NON_ASCII);
    }

    @Test
    public void testWriteString() throws JsonProcessingException {
        generator.writeString("Hello, World!");
        String actual = generator.getOutputBuffer().toString();
        String expected = "\"Hello, World!\"";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testWriteNull() throws JsonProcessingException {
        generator.writeNull();
        String actual = generator.getOutputBuffer().toString();
        String expected = "null";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testWriteBoolean() throws JsonProcessingException {
        generator.writeBoolean(true);
        String actual = generator.getOutputBuffer().toString();
        String expected = "true";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testWriteNumber() throws JsonProcessingException {
        generator.writeNumber(123);
        String actual = generator.getOutputBuffer().toString();
        String expected = "123";
        Assert.assertEquals(expected, actual);

        generator.writeNumber(1.23);
        actual = generator.getOutputBuffer().toString();
        expected = "1.23";
        Assert.assertEquals(expected, actual);

        generator.disable(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS);
        generator.writeNumber(123);
        actual = generator.getOutputBuffer().toString();
        Assert.assertArrayEquals(new byte[]{"1"," ","2","3"}, actual.getBytes());
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteInvalidJson() throws JsonProcessingException {
        generator.writeStartObject();
        generator.writeString("key");
        generator.writeFieldName("invalidKey");
        generator.close();
    }

    @Test
    public void testWriteRawValue() throws JsonProcessingException {
        generator.writeRawValue("\"Hello, World!\"");
        String actual = generator.getOutputBuffer().toString();
        String expected = "\"Hello, World!\"";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testWriteObject() throws JsonProcessingException {
        String input = "{\"name\":\"John\",\"age\":30}";
        Object obj = mapper.readValue(new ByteArrayInputStream(input.getBytes()), Object.class);
        generator.writeObject(obj);
        String actual = generator.getOutputBuffer().toString();
        String expected = input;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testWriteBinary() throws JsonProcessingException {
        // Unsupported, since this abstract class does not have a binary write method
    }

    @Test
    public void testUseDefaultPrettyPrinter() throws JsonProcessingException {
        generator.useDefaultPrettyPrinter();
        generator.writeValue("Hello, World!");
        String actual = generator.getOutputBuffer().toString();
        String expected = "{\"string\":\"Hello, World!\"}";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSetCodec() throws JsonProcessingException {
        ObjectCodec codec = mapper.getFactory().createObjectCodec();
        generator.setCodec(codec);
        generator.writeValue("Hello, World!");
        String actual = generator.getOutputBuffer().toString();
        String expected = "{\"string\":\"Hello, World!\"}";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetCodec() throws JsonProcessingException {
        Assert.assertEquals(mapper.getFactory(), generator.getCodec());
    }

    @Test
    public void testGetOutputContext() throws JsonProcessingException {
        Assert.assertNotNull(generator.getOutputContext());
    }

    @Test
    public void testIsEnabled() throws JsonProcessingException {
        Assert.assertTrue(generator.isEnabled(JsonGenerator.Feature.ESCAPE_NON_ASCII));
        generator.disable(JsonGenerator.Feature.ESCAPE_NON_ASCII);
        Assert.assertFalse(generator.isEnabled(JsonGenerator.Feature.ESCAPE_NON_ASCII));
    }

    @Test
    public void testVersion() throws JsonProcessingException {
        Version version = generator.version();
        Assert.assertEquals(mapper.getFactory().getClass().getPackage().getImplementationVersion(), version.getImplementationVersion());
        Assert.assertEquals(mapper.getFactory().getClass().getPackage().getSpecificationTitle(), version.getSpecificationTitle());
    }

    @Test
    public void testEnableFeatures() throws JsonProcessingException {
        generator.enable(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS);
        generator.writeNumber(123);
        String actual = generator.getOutputBuffer().toString();
        String expected = "123";
        Assert.assertEquals(expected, actual);

        generator.disable(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS);
        generator.writeNumber(123);
        actual = generator.getOutputBuffer().toString();
        Assert.assertArrayEquals(new byte[]{"1"," ","2","3"}, actual.getBytes());
    }

    @Test
    public void testDisableFeatures() throws JsonProcessingException {
        generator.enable(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS);
        generator.writeNumber(123);
        String actual = generator.getOutputBuffer().toString();
        String expected = "123";
        Assert.assertEquals(expected, actual);

        generator.disable(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS);
        generator.writeNumber(123);
        actual = generator.getOutputBuffer().toString();
        Assert.assertArrayEquals(new byte[]{"1"," ","2","3"}, actual.getBytes());
    }

    @Test
    public void testSetFeatureMask() throws JsonProcessingException {
        int oldMask = generator.getFeatureMask();
        int newMask = JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | JsonGenerator.Feature.ESCAPE_NON_ASCII.getMask();
        generator.setFeatureMask(newMask);
        generator.writeNumber(123);
        String actual = generator.getOutputBuffer().toString();
        String expected = "123";
        Assert.assertEquals(expected, actual);

        generator.setFeatureMask(oldMask);
        generator.writeNumber(123);
        actual = generator.getOutputBuffer().toString();
        Assert.assertArrayEquals(new byte[]{"1"," ","2","3"}, actual.getBytes());
    }

    @Test
    public void testWriteTree() throws JsonProcessingException {
        TreeNode tree = new TreeNode();
        tree.set("name", "John");
        tree.set("age", 30);
        generator.writeTree(tree);
        String actual = generator.getOutputBuffer().toString();
        String expected = "{\"name\":\"John\",\"age\":30}";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFlush() throws JsonProcessingException {
        generator.writeString("Hello, World!");
        generator.flush();
        String actual = generator.getOutputBuffer().toString();
        String expected = "\"Hello, World!\"";
        Assert.assertEquals(expected, actual);

        generator.close();
        Assert.assertFalse(generator.isClosed());
    }

    @Test
    public void testClose() throws JsonProcessingException {
        generator.writeString("Hello, World!");
        generator.close();
        String actual = generator.getOutputBuffer().toString();
        String expected = "\"Hello, World!\"";
        Assert.assertEquals(expected, actual);

        Assert.assertTrue(generator.isClosed());
    }

    @Test
    public void testIsClosed() throws JsonProcessingException {
        Assert.assertFalse(generator.isClosed());
    }
}