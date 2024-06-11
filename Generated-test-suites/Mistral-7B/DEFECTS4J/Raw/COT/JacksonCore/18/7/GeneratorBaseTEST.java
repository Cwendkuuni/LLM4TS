package com.fasterxml.jackson.core.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.json.JsonWriteContext;

/**
 * Test class for GeneratorBase.
 */
public class GeneratorBaseTest {

    /**
     * Test for the constructor.
     */
    @Test
    public void testConstructor() {
        int features = GeneratorBase.DERIVED_FEATURES_MASK;
        ObjectCodec objectCodec = new ObjectMapper().getFactory().createObjectCodec();
        GeneratorBase generatorBase = new GeneratorBase(features, objectCodec);
        assertNotNull(generatorBase);
        assertEquals(features, generatorBase.getFeatureMask());
        assertEquals(objectCodec, generatorBase.getCodec());
    }

    /**
     * Test for the constructor with JsonWriteContext.
     */
    @Test
    public void testConstructorWithJsonWriteContext() {
        int features = GeneratorBase.DERIVED_FEATURES_MASK;
        ObjectCodec objectCodec = new ObjectMapper().getFactory().createObjectCodec();
        JsonWriteContext jsonWriteContext = new JsonWriteContext();
        GeneratorBase generatorBase = new GeneratorBase(features, objectCodec, jsonWriteContext);
        assertNotNull(generatorBase);
        assertEquals(features, generatorBase.getFeatureMask());
        assertEquals(objectCodec, generatorBase.getCodec());
        assertEquals(jsonWriteContext, generatorBase.getOutputContext());
    }

    /**
     * Test for the version method.
     */
    @Test
    public void testVersion() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        Version version = generatorBase.version();
        assertNotNull(version);
        assertTrue(version.getClass().equals(Version.class));
    }

    /**
     * Test for the getCurrentValue method.
     */
    @Test
    public void testGetCurrentValue() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        Object currentValue = generatorBase.getCurrentValue();
        assertNull(currentValue);
    }

    /**
     * Test for the setCurrentValue method.
     */
    @Test
    public void testSetCurrentValue() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        String value = "test value";
        generatorBase.setCurrentValue(value);
        Object currentValue = generatorBase.getCurrentValue();
        assertEquals(value, currentValue);
    }

    /**
     * Test for the isEnabled method.
     */
    @Test
    public void testIsEnabled() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        assertTrue(generatorBase.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        int features = GeneratorBase.DERIVED_FEATURES_MASK | Feature.WRITE_NUMBERS_AS_STRINGS.getMask();
        generatorBase = new GeneratorBase(features, null);
        assertFalse(generatorBase.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    /**
     * Test for the getFeatureMask method.
     */
    @Test
    public void testGetFeatureMask() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        int featureMask = generatorBase.getFeatureMask();
        assertEquals(0, featureMask);
        int features = GeneratorBase.DERIVED_FEATURES_MASK;
        generatorBase = new GeneratorBase(features, null);
        featureMask = generatorBase.getFeatureMask();
        assertEquals(features, featureMask);
    }

    /**
     * Test for the enable method.
     */
    @Test
    public void testEnable() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        generatorBase.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(generatorBase.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        int features = GeneratorBase.DERIVED_FEATURES_MASK | Feature.WRITE_NUMBERS_AS_STRINGS.getMask();
        generatorBase = new GeneratorBase(0, null);
        generatorBase.enable(features);
        assertEquals(features, generatorBase.getFeatureMask());
    }

    /**
     * Test for the disable method.
     */
    @Test
    public void testDisable() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK, null);
        generatorBase.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(generatorBase.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    /**
     * Test for the setFeatureMask method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFeatureMaskUnsupported() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        generatorBase.setFeatureMask(GeneratorBase.DERIVED_FEATURES_MASK);
    }

    /**
     * Test for the overrideStdFeatures method.
     */
    @Test
    public void testOverrideStdFeatures() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        int oldState = generatorBase.getFeatureMask();
        generatorBase.overrideStdFeatures(GeneratorBase.DERIVED_FEATURES_MASK | Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
        int newState = generatorBase.getFeatureMask();
        assertEquals(oldState | Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), newState);
        assertTrue(generatorBase.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        generatorBase.overrideStdFeatures(newState & ~Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
        newState = generatorBase.getFeatureMask();
        assertEquals(oldState, newState);
        assertFalse(generatorBase.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    /**
     * Test for the useDefaultPrettyPrinter method.
     */
    @Test
    public void testUseDefaultPrettyPrinter() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK, null);
        assertNull(generatorBase.getPrettyPrinter());
        generatorBase.useDefaultPrettyPrinter();
        PrettyPrinter prettyPrinter = generatorBase.getPrettyPrinter();
        assertTrue(prettyPrinter instanceof DefaultPrettyPrinter);
    }

    /**
     * Test for the setCodec method.
     */
    @Test
    public void testSetCodec() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        ObjectCodec objectCodec = new ObjectMapper().getFactory().createObjectCodec();
        generatorBase.setCodec(objectCodec);
        assertEquals(objectCodec, generatorBase.getCodec());
    }

    /**
     * Test for the getCodec method.
     */
    @Test
    public void testGetCodec() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        assertNull(generatorBase.getCodec());
    }

    /**
     * Test for the writeFieldName method.
     */
    @Test
    public void testWriteFieldName() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        String name = "test";
        generatorBase.writeFieldName(name);
        assertEquals(name, generatorBase.getCurrentValue());
    }

    /**
     * Test for the writeString method.
     */
    @Test
    public void testWriteString() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        String text = "test";
        generatorBase.writeString(text);
        assertEquals(text, generatorBase.getCurrentValue());
    }

    /**
     * Test for the writeRawValue method with a String.
     */
    @Test
    public void testWriteRawValueWithString() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        String text = "test";
        generatorBase.writeRawValue(text);
        assertNull(generatorBase.getCurrentValue());
    }

    /**
     * Test for the writeRawValue method with a char array.
     */
    @Test
    public void testWriteRawValueWithCharArray() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        char[] text = {'t', 'e', 's', 't'};
        generatorBase.writeRawValue(text, 0, text.length);
        assertNull(generatorBase.getCurrentValue());
    }

    /**
     * Test for the writeRawValue method with a SerializableString.
     */
    @Test
    public void testWriteRawValueWithSerializableString() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        SerializableString text = new UTF8String("test");
        generatorBase.writeRawValue(text);
        assertNull(generatorBase.getCurrentValue());
    }

    /**
     * Test for the writeObject method with a null value.
     */
    @Test
    public void testWriteObjectWithNullValue() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        generatorBase.writeObject(null);
        assertEquals("null", generatorBase.getCurrentValue());
    }

    /**
     * Test for the writeObject method with an Object value.
     */
    @Test
    public void testWriteObjectWithObjectValue() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        String value = "test value";
        generatorBase.writeObject(value);
        assertEquals(value, generatorBase.getCurrentValue());
    }

    /**
     * Test for the writeTree method with a null value.
     */
    @Test
    public void testWriteTreeWithNullValue() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        generatorBase.writeTree(null);
        assertEquals("null", generatorBase.getCurrentValue());
    }

    /**
     * Test for the writeTree method with a TreeNode value.
     */
    @Test
    public void testWriteTreeWithTreeNodeValue() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        TreeNode value = new SimpleTreeNode("test value");
        generatorBase.writeTree(value);
        assertEquals(value.toString(), generatorBase.getCurrentValue());
    }

    /**
     * Test for the writeBinary method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testWriteBinaryUnsupported() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        generatorBase.writeBinary(Base64Variant.STANDARD, new ByteArrayInputStream(new byte[0]), 0);
    }

    /**
     * Test for the writeNumber method with an int value.
     */
    @Test
    public void testWriteNumberWithIntValue() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        int value = 42;
        generatorBase.writeNumber(value);
        assertEquals(Integer.toString(value), generatorBase.getCurrentValue());
    }

    /**
     * Test for the writeNumber method with a long value.
     */
    @Test
    public void testWriteNumberWithLongValue() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        long value = 42L;
        generatorBase.writeNumber(value);
        assertEquals(Long.toString(value), generatorBase.getCurrentValue());
    }

    /**
     * Test for the writeNumber method with a double value.
     */
    @Test
    public void testWriteNumberWithDoubleValue() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        double value = 3.14;
        generatorBase.writeNumber(value);
        assertEquals(Double.toString(value), generatorBase.getCurrentValue());
    }

    /**
     * Test for the writeNumber method with a BigDecimal value.
     */
    @Test
    public void testWriteNumberWithBigDecimalValue() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        BigDecimal value = new BigDecimal("3.14159265358979323846264338327950288416");
        generatorBase.writeNumber(value);
        assertEquals(_asString(value), generatorBase.getCurrentValue());
    }

    /**
     * Test for the writeBoolean method with a boolean value.
     */
    @Test
    public void testWriteBooleanWithBooleanValue() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        boolean value = true;
        generatorBase.writeBoolean(value);
        assertEquals(Boolean.toString(value), generatorBase.getCurrentValue());
    }

    /**
     * Test for the writeNull method.
     */
    @Test
    public void testWriteNullMethod() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        generatorBase.writeNull();
        assertEquals("null", generatorBase.getCurrentValue());
    }

    /**
     * Test for the flush method.
     */
    @Test
    public void testFlush() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream output = outputStream;
        generatorBase.flush();
        assertEquals("", generatorBase.getCurrentValue());
        String result = outputStream.toString();
        assertTrue(result.isEmpty());
    }

    /**
     * Test for the close method.
     */
    @Test
    public void testClose() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        generatorBase.close();
        assertFalse(generatorBase.isClosed());
        generatorBase = new GeneratorBase(0, null);
        generatorBase.writeNumber(42);
        generatorBase.close();
        assertTrue(generatorBase.isClosed());
    }

    /**
     * Test for the _verifyValueWrite method.
     */
    @Test(expected = JsonGenerationException.class)
    public void testVerifyValueWrite() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        generatorBase._verifyValueWrite("write raw value");
    }

    /**
     * Test for the _decodeSurrogate method.
     */
    @Test(expected = IOException.class)
    public void testDecodeSurrogateInvalidSecondSurrogate() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        generatorBase._decodeSurrogate(0xDBFF, 0xA000);
    }

    /**
     * Test for the _decodeSurrogate method.
     */
    @Test
    public void testDecodeSurrogate() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        int surr1 = 0xDB40, surr2 = 0x0041;
        int codePoint = generatorBase._decodeSurrogate(surr1, surr2);
        assertEquals('\uD840\uDC41', codePoint);
    }
}