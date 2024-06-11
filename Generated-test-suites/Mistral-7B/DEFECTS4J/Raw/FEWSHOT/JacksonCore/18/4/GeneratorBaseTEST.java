package com.fasterxml.jackson.core.base;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonWriteContext;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;

public class GeneratorBaseTest {

    private static final int DERIVED_FEATURES_MASK =
            Feature.WRITE_NUMBERS_AS_STRINGS.getMask()
            | Feature.ESCAPE_NON_ASCII.getMask()
            | Feature.STRICT_DUPLICATE_DETECTION.getMask();

    @Test
    public void testIsEnabled() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(generator.isEnabled(Feature.WRITE_BOOLEAN_AS_INT));
        generator = new GeneratorBase(DERIVED_FEATURES_MASK, null);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertTrue(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
    }

    @Test
    public void testGetFeatureMask() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        assertEquals(0, generator.getFeatureMask());
        generator = new GeneratorBase(DERIVED_FEATURES_MASK, null);
        assertEquals(DERIVED_FEATURES_MASK, generator.getFeatureMask());
    }

    @Test
    public void testEnableWriteNumbersAsStrings() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        generator = new GeneratorBase(0, null);
        generator.overrideStdFeatures(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), 0);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void testDisableWriteNumbersAsStrings() throws IOException {
        GeneratorBase generator = new GeneratorBase(DERIVED_FEATURES_MASK, null);
        generator.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        generator = new GeneratorBase(DERIVED_FEATURES_MASK, null);
        generator.overrideStdFeatures(0, Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void testUseDefaultPrettyPrinter() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        assertNull(generator.getPrettyPrinter());
        generator.useDefaultPrettyPrinter();
        assertNotNull(generator.getPrettyPrinter());
        assertTrue(generator.getPrettyPrinter() instanceof DefaultPrettyPrinter);
    }

    @Test
    public void testSetCodec() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        assertNull(generator.getCodec());
        ObjectCodec codec = new ObjectMapper().getFactory().createObjectCodec();
        generator.setCodec(codec);
        assertEquals(codec, generator.getCodec());
    }

    @Test
    public void testWriteNumber() throws IOException {
        GeneratorBase generator = new GeneratorBase(DERIVED_FEATURES_MASK, null);
        assertNull(generator._writeContext);
        generator.writeNumber(1);
        assertNotNull(generator._writeContext);
    }

    @Test
    public void testWriteBoolean() throws IOException {
        GeneratorBase generator = new GeneratorBase(DERIVED_FEATURES_MASK, null);
        generator.writeBoolean(true);
    }

    @Test
    public void testWriteNull() throws IOException {
        GeneratorBase generator = new GeneratorBase(DERIVED_FEATURES_MASK, null);
        generator.writeNull();
    }

    @Test
    public void testWriteString() throws IOException, JsonProcessingException {
        GeneratorBase generator = new GeneratorBase(DERIVED_FEATURES_MASK, null);
        String str = "test";
        generator.writeString(str);
        assertEquals(str, generator.getCurrentValue());
    }

    @Test
    public void testWriteRawValue() throws IOException {
        GeneratorBase generator = new GeneratorBase(DERIVED_FEATURES_MASK, null);
        String raw = "test";
        generator.writeRawValue(raw);
        assertEquals(raw, generator.getCurrentValue());
    }

    @Test
    public void testWriteObject() throws IOException {
        GeneratorBase generator = new GeneratorBase(DERIVED_FEATURES_MASK, null);
        Object obj = new Object();
        generator.writeObject(obj);
        assertEquals(obj, generator.getCurrentValue());
    }

    @Test
    public void testWriteTree() throws IOException {
        GeneratorBase generator = new GeneratorBase(DERIVED_FEATURES_MASK, null);
        ObjectNode tree = new ObjectMapper().createObjectNode();
        tree.put("test", "value");
        generator.writeTree(tree);
        assertEquals(tree, generator.getCurrentValue());
    }

    @Test
    public void testFlush() throws IOException {
        GeneratorBase generator = new GeneratorBase(DERIVED_FEATURES_MASK, null);
        generator.flush();
    }

    @Test
    public void testClose() throws IOException {
        GeneratorBase generator = new GeneratorBase(DERIVED_FEATURES_MASK, null);
        generator.close();
        assertTrue(generator.isClosed());
    }

    @Test
    public void testWriteBinary() throws IOException {
        GeneratorBase generator = new GeneratorBase(DERIVED_FEATURES_MASK, null);
        generator.writeBinary(null, null, 0);
        _reportError("writeBinary not implemented at this level");
    }

    @Test
    public void testWriteNumberInt() throws IOException {
        GeneratorBase generator = new GeneratorBase(DERIVED_FEATURES_MASK, null);
        generator.writeNumber(1);
        assertEquals(1, generator.getCurrentValue().intValue());
    }

    @Test
    public void testWriteNumberLong() throws IOException {
        GeneratorBase generator = new GeneratorBase(DERIVED_FEATURES_MASK, null);
        generator.writeNumber(1L);
        assertEquals(1L, generator.getCurrentValue().longValue());
    }

    @Test
    public void testWriteNumberFloat() throws IOException {
        GeneratorBase generator = new GeneratorBase(DERIVED_FEATURES_MASK, null);
        generator.writeNumber(1.0f);
        assertEquals(1.0f, generator.getCurrentValue().floatValue(), 0.001f);
    }

    @Test
    public void testWriteNumberDouble() throws IOException {
        GeneratorBase generator = new GeneratorBase(DERIVED_FEATURES_MASK, null);
        generator.writeNumber(1.0);
        assertEquals(1.0, generator.getCurrentValue().doubleValue(), 0.001);
    }

    @Test
    public void testWriteNumberBigDecimal() throws IOException {
        GeneratorBase generator = new GeneratorBase(DERIVED_FEATURES_MASK, null);
        BigDecimal dec = new BigDecimal("123456.789");
        generator.writeNumber(dec);
        assertEquals(dec, generator.getCurrentValue());
    }

    @Test
    public void testWriteBooleanTrue() throws IOException {
        GeneratorBase generator = new GeneratorBase(DERIVED_FEATURES_MASK, null);
        generator.writeBoolean(true);
        assertEquals("true", generator.getCurrentValue().asText());
    }

    @Test
    public void testWriteBooleanFalse() throws IOException {
        GeneratorBase generator = new GeneratorBase(DERIVED_FEATURES_MASK, null);
        generator.writeBoolean(false);
        assertEquals("false", generator.getCurrentValue().asText());
    }

    @Test
    public void testWriteNullJson() throws IOException {
        GeneratorBase generator = new GeneratorBase(DERIVED_FEATURES_MASK, null);
        generator.writeNull();
        assertEquals("null", generator.getCurrentValue().asText());
    }

    @Test
    public void testWriteStringJson() throws IOException {
        GeneratorBase generator = new GeneratorBase(DERIVED_FEATURES_MASK, null);
        String str = "test";
        generator.writeString(str);
        assertEquals(str, generator.getCurrentValue().asText());
    }

    @Test
    public void testWriteRawValueJson() throws IOException {
        GeneratorBase generator = new GeneratorBase(DERIVED_FEATURES_MASK, null);
        String raw = "test";
        generator.writeRawValue(raw);
        assertEquals(raw, generator.getCurrentValue().asText());
    }

    @Test
    public void testWriteObjectJson() throws IOException {
        GeneratorBase generator = new GeneratorBase(DERIVED_FEATURES_MASK, null);
        Object obj = new Object();
        generator.writeObject(obj);
        assertEquals(obj.getClass().getName(), generator.getCurrentValue().asText());
    }

    @Test
    public void testWriteTreeJson() throws IOException {
        GeneratorBase generator = new GeneratorBase(DERIVED_FEATURES_MASK, null);
        ObjectNode tree = new ObjectMapper().createObjectNode();
        tree.put("test", new Object());
        generator.writeTree(tree);
        assertEquals(tree.toString(), generator.getCurrentValue().asText());
    }

    @Test
    public void testWriteBinaryJson() throws IOException {
        GeneratorBase generator = new GeneratorBase(DERIVED_FEATURES_MASK, null);
        byte[] binary = new byte[10];
        generator.writeBinary(null, new ByteArrayInputStream(binary), binary.length);
        assertEquals("", generator.getCurrentValue().asText());
    }

    @Test
    public void testWriteSurrogatePairJson() throws IOException {
        GeneratorBase generator = new GeneratorBase(DERIVED_FEATURES_MASK, null);
        char ch1 = '\uD800';
        char ch2 = '\uDC00';
        String result = new String(Character.toChars(_decodeSurrogate(0xD800, 0xDC00)));
        generator.writeString(new String(Character.toChars(_decodeSurrogate(0xD800, 0xDC00)))) ;
        assertEquals(result, generator.getCurrentValue().asText());
    }

    @Test
    public void testWriteValueWriteContext() throws IOException {
        GeneratorBase generator = new GeneratorBase(DERIVED_FEATURES_MASK, null);
        generator.writeString("test");
        JsonWriteContext context = generator.getOutputContext();
        assertEquals("test", context.getCurrentValue());
    }

    @Test
    public void testSetPrettyPrinter() throws IOException {
        GeneratorBase generator = new GeneratorBase(DERIVED_FEATURES_MASK, null);
        PrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        generator.setPrettyPrinter(prettyPrinter);
        assertNotNull(generator.getPrettyPrinter());
    }

    @Test
    public void testPrettyPrinterFormat() throws IOException {
        GeneratorBase generator = new GeneratorBase(DERIVED_FEATURES_MASK, null);
        ObjectNode tree = new ObjectMapper().createObjectNode();
        tree.put("test", new Object());
        generator.setPrettyPrinter(new DefaultPrettyPrinter().withIndentArrays(true).withIndentObjects(true));
        generator.writeTree(tree);
        String expected = "{\n" +
                "  \"test\": [\n" +
                "  ]\n" +
                "}";
        String result = generator.getCurrentValue().asText();
        assertEquals(expected, result);
    }

    @Test
    public void testPrettyPrinterFormatNumbers() throws IOException {
        GeneratorBase generator = new GeneratorBase(DERIVED_FEATURES_MASK | Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);
        ObjectNode tree = new ObjectMapper().createObjectNode();
        tree.put("test", 123456789012345L);
        generator.setPrettyPrinter(new DefaultPrettyPrinter().withIndentArrays(true).withIndentObjects(true));
        generator.writeTree(tree);
        String expected = "{\n" +
                "  \"test\": \"123456789012345\"\n" +
                "}";
        String result = generator.getCurrentValue().asText();
        assertEquals(expected, result);
    }

    private void _reportError(String message) throws IOException {
        throw new IOException(message);
    }

    private void _reportUnsupportedOperation() {
        throw new UnsupportedOperationException();
    }

}