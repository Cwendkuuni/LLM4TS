package com.fasterxml.jackson.core.base;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GeneratorBaseTest {

    

    private GeneratorBase generator;

    @SuppressWarnings("static-method")
    @Before
    public void setUp() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), JsonMapping.Builder().build());
    }

    // Test for constructor
    @Test
    public void testGeneratorBaseConstructor() {
        GeneratorBase generator = new GeneratorBase(0, new ObjectMapper().getFactory());
        assertNotNull(generator);
    }

    // Test for version() method
    @Test
    public void testVersion() {
        Version version = generator.version();
        assertNotNull(version);
    }

    // Test for isEnabled() method
    @Test
    public void testIsEnabled() {
        assertEquals(true, generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        generator.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertEquals(false, generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    // Test for getFeatureMask() method
    @Test
    public void testGetFeatureMask() {
        assertEquals(0, generator.getFeatureMask());
        generator.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertEquals(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), generator.getFeatureMask());
    }

    // Test for configure() method (deprecated)
    @Test
    public void testConfigure() {
        // Method is deprecated, so not testing it
    }

    // Test for enable() method
    @Test
    public void testEnable() {
        generator.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        generator.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        generator.enable(Feature.STRICT_DUPLICATE_DETECTION);
        assertTrue(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        generator.disable(Feature.STRICT_DUPLICATE_DETECTION);
        assertFalse(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
    }

    // Test for disable() method
    @Test
    public void testDisable() {
        generator.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        generator.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        generator.enable(Feature.STRICT_DUPLICATE_DETECTION);
        assertTrue(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        generator.disable(Feature.STRICT_DUPLICATE_DETECTION);
        assertFalse(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
    }

    // Test for setFeatureMask() method (deprecated)
    @Test
    public void testSetFeatureMask() {
        // Method is deprecated, so not testing it
    }

    // Test for overrideStdFeatures() method
    @Test
    public void testOverrideStdFeatures() {
        generator.overrideStdFeatures(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), 0);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        generator.overrideStdFeatures(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), Feature.STRICT_DUPLICATE_DETECTION.getMask());
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        generator.overrideStdFeatures(Feature.STRICT_DUPLICATE_DETECTION.getMask(), 0);
        assertFalse(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
    }

    // Test for _checkStdFeatureChanges() method (not public)
    @Test(expected = AssertionError.class)
    public void testCheckStdFeatureChanges() {
        generator.overrideStdFeatures(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), Feature.STRICT_DUPLICATE_DETECTION.getMask());
        generator.overrideStdFeatures(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
    }

    // Test for useDefaultPrettyPrinter() method
    @Test
    public void testUseDefaultPrettyPrinter() {
        generator.useDefaultPrettyPrinter();
        assertNotNull(generator.getPrettyPrinter());
        assertTrue(generator.getPrettyPrinter() instanceof DefaultPrettyPrinter);
    }

    // Test for setCodec() method
    @Test
    public void testSetCodec() {
        ObjectMapper mapper = new ObjectMapper();
        generator.setCodec(mapper.getFactory());
        assertEquals(mapper.getFactory(), generator.getCodec());
    }

    // Test for getOutputContext() method
    @Test
    public void testGetOutputContext() {
        JsonWriteContext context = generator.getOutputContext();
        assertNotNull(context);
    }

    // Test for writeNull() method
    @Test
    public void testWriteNull() throws JsonProcessingException {
        generator.writeNull();
        String json = generator.getOutputBuffer().toString();
        assertEquals("null", json);
    }

    // Test for writeString() method (with String value)
    @Test
    public void testWriteStringString() throws JsonProcessingException {
        generator.writeString("test");
        String json = generator.getOutputBuffer().toString();
        assertEquals("\"test\"", json);
    }

    // Test for writeString() method (with SerializableString value)
    @Test
    public void testWriteStringSerializableString() throws JsonProcessingException {
        generator.writeString(new SerializableString("test"));
        String json = generator.getOutputBuffer().toString();
        assertEquals("\"test\"", json);
    }

    // Test for writeRawValue() method (with String value)
    @Test
    public void testWriteRawValueString() throws JsonProcessingException, IOException {
        generator.writeRawValue("test");
        String json = generator.getOutputBuffer().toString();
        assertEquals("\"test\"", json);
    }

    // Test for writeRawValue() method (with char[] value)
    @Test
    public void testWriteRawValueCharArray() throws JsonProcessingException, IOException {
        char[] charArray = {'t', 'e', 's', 't'};
        generator.writeRawValue(charArray, 0, charArray.length);
        String json = generator.getOutputBuffer().toString();
        assertEquals("\"test\"", json);
    }

    // Test for writeRawValue() method (with SerializableString value)
    @Test
    public void testWriteRawValueSerializableString() throws JsonProcessingException, IOException {
        SerializableString serializableString = new SerializableString("test");
        generator.writeRawValue(serializableString);
        String json = generator.getOutputBuffer().toString();
        assertEquals("\"test\"", json);
    }

    // Test for writeRawValue() method (with Base64Variant and InputStream value)
    @Test(expected = UnsupportedOperationException.class)
    public void testWriteRawValueBase64VariantInputStream() throws JsonProcessingException, IOException {
        generator.writeRawValue(Base64Variant.STANDARD, new ByteArrayInputStream("test".getBytes()), 0, 4);
    }

    // Test for writeNumber() method (with int value)
    @Test
    public void testWriteNumberInt() throws JsonProcessingException {
        generator.writeNumber(1);
        String json = generator.getOutputBuffer().toString();
        assertEquals("1", json);
    }

    // Test for writeNumber() method (with long value)
    @Test
    public void testWriteNumberLong() throws JsonProcessingException {
        generator.writeNumber(1L);
        String json = generator.getOutputBuffer().toString();
        assertEquals("1", json);
    }

    // Test for writeNumber() method (with double value)
    @Test
    public void testWriteNumberDouble() throws JsonProcessingException {
        generator.writeNumber(1.0);
        String json = generator.getOutputBuffer().toString();
        assertEquals("1.0", json);
    }

    // Test for writeNumber() method (with float value)
    @Test
    public void testWriteNumberFloat() throws JsonProcessingException {
        generator.writeNumber(1.0f);
        String json = generator.getOutputBuffer().toString();
        assertEquals("1.0", json);
    }

    // Test for writeNumber() method (with BigDecimal value)
    @Test
    public void testWriteNumberBigDecimal() throws JsonProcessingException {
        BigDecimal bigDecimal = new BigDecimal("123.456");
        generator.writeNumber(bigDecimal);
        String json = generator.getOutputBuffer().toString();
        assertEquals("123.456", json);
    }

    // Test for writeBoolean() method
    @Test
    public void testWriteBoolean() throws JsonProcessingException {
        generator.writeBoolean(true);
        String json = generator.getOutputBuffer().toString();
        assertEquals("true", json);

        generator.writeBoolean(false);
        json = generator.getOutputBuffer().toString();
        assertEquals("false", json);
    }

    // Test for writeObject() method
    @Test
    public void testWriteObject() throws JsonProcessingException {
        String json = generator.writeValueAsString(new Object());
        assertNotNull(json);
    }

    // Test for writeTree() method
    @Test
    public void testWriteTree() throws JsonProcessingException {
        TreeNode treeNode = new ObjectMapper().readTree("{\"test\":1}");
        String json = generator.writeValueAsString(treeNode);
        assertNotNull(json);
    }

    // Test for flush() method
    @Test
    public void testFlush() throws JsonProcessingException, IOException {
        generator.writeString("test");
        generator.flush();
        String json = generator.getOutputBuffer().toString();
        assertEquals("\"test\"", json);
        generator.close();
    }

    // Test for close() method
    @Test
    public void testClose() throws JsonProcessingException, IOException {
        generator.writeString("test");
        generator.close();
        try {
            generator.writeString("test");
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    // Test for _releaseBuffers() method (not public)
    @Test
    public void testReleaseBuffers() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        generator = new GeneratorBase(0, new ObjectMapper().getFactory(), byteArrayOutputStream) {
            @Override
            protected void _releaseBuffers() {
                // do nothing
            }
        };
        generator.writeString("test");
        generator.close();
        assertNotNull(byteArrayOutputStream.toString());
    }

    // Test for _verifyValueWrite() method (not public)
    @Test(expected = JsonProcessingException.class)
    public void testVerifyValueWrite() throws JsonProcessingException {
        generator.writeString("test");
        generator._verifyValueWrite("write string");
    }

    // Test for _constructDefaultPrettyPrinter() method (not public)
    @Test
    public void testConstructDefaultPrettyPrinter() throws JsonProcessingException {
        PrettyPrinter prettyPrinter = generator._constructDefaultPrettyPrinter();
        assertTrue(prettyPrinter instanceof DefaultPrettyPrinter);
    }

    // Test for _decodeSurrogate() method (not public)
    @Test(expected = IOException.class)
    public void testDecodeSurrogate() throws IOException {
        generator._decodeSurrogate(0xD800, 0xDC00);
    }

    
}