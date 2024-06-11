package com.fasterxml.jackson.core.base;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.json.*;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import org.junit.Test;

public class GeneratorBaseTest {
    @Test
    public void testConstants() {
        assertSame(SURR1_FIRST, 0xD800);
        assertSame(SURR1_LAST, 0xDBFF);
        assertSame(SURR2_FIRST, 0xDC00);
        assertSame(SURR2_LAST, 0xDFFF);
    }

    @Test
    public void testDerivedFeaturesMask() {
        assertTrue(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() == 0x0001);
        assertTrue(Feature.ESCAPE_NON_ASCII.getMask() == 0x0002);
        assertTrue(Feature.STRICT_DUPLICATE_DETECTION.getMask() == 0x0004);
        assertTrue(DERIVED_FEATURES_MASK == (Feature.WRITE_NUMBERS_AS_STRINGS.getMask()
                | Feature.ESCAPE_NON_ASCII.getMask()
                | Feature.STRICT_DUPLICATE_DETECTION.getMask()));
    }

    @Test
    public void testVersion() {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        assertEquals(VersionUtil.versionFor(GeneratorBase.class), generatorBase.version());
    }

    @Test
    public void testIsEnabled() {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        generatorBase.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(generatorBase.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        generatorBase.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(generatorBase.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void testEnable() {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        generatorBase.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(generatorBase.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        generatorBase.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(generatorBase.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        generatorBase.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(generatorBase.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void testDisable() {
        GeneratorBase generatorBase = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);
        assertTrue(generatorBase.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        generatorBase.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(generatorBase.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void testGetFeatureMask() {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        int mask = generatorBase.getFeatureMask();
        assertFalse(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() == mask);
        generatorBase.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        mask = generatorBase.getFeatureMask();
        assertTrue(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() == (mask & Feature.WRITE_NUMBERS_AS_STRINGS.getMask()));
    }

    @Test
    public void testUseDefaultPrettyPrinter() {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        assertNull(generatorBase.getPrettyPrinter());
        generatorBase.useDefaultPrettyPrinter();
        assertNotNull(generatorBase.getPrettyPrinter());
        PrettyPrinter prettyPrinter = generatorBase.getPrettyPrinter();
        assertTrue(prettyPrinter instanceof DefaultPrettyPrinter);
    }

    @Test
    public void testSetCodec() {
        ObjectCodec objectCodec = new ObjectMapper().getFactory().createObjectCodec();
        GeneratorBase generatorBase = new GeneratorBase(0, objectCodec);
        assertSame(objectCodec, generatorBase.getCodec());
        generatorBase.setCodec(null);
        assertNull(generatorBase.getCodec());
    }

    @Test
    public void testGetOutputContext() {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        assertNull(generatorBase.getOutputContext());
    }

    @Test
    public void testWriteFieldName() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        SerializableString name = new StringSerializable("testName");
        generatorBase.writeFieldName(name);
        assertEquals("testName", generatorBase.getCurrentValue());
    }

    @Test
    public void testWriteString() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        String text = "testString";
        generatorBase.writeString(text);
        assertEquals(text, generatorBase.getCurrentValue());
    }

    @Test
    public void testWriteRawValue() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        String text = "testRawString";
        generatorBase.writeRawValue(text);
        assertEquals(text, generatorBase.getCurrentValue());
    }

    @Test
    public void testWriteRawValueWithOffsetAndLength() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        String text = "testRawStringWithOffsetAndLength";
        generatorBase.writeRawValue(text, 0, text.length());
        assertEquals(text, generatorBase.getCurrentValue());
    }

    @Test
    public void testWriteNull() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        generatorBase.writeNull();
        assertNull(generatorBase.getCurrentValue());
    }

    @Test
    public void testWriteBoolean() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        generatorBase.writeBoolean(true);
        assertTrue(generatorBase.getCurrentValue().equals("true"));
        generatorBase.writeBoolean(false);
        assertTrue(generatorBase.getCurrentValue().equals("false"));
    }

    @Test
    public void testWriteNumber() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        generatorBase.writeNumber(123);
        assertEquals("123", generatorBase.getCurrentValue());
        generatorBase.writeNumber(456);
        assertEquals("456", generatorBase.getCurrentValue());
    }

    @Test
    public void testWriteNumberWithBigDecimal() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        BigDecimal bigDecimal = new BigDecimal("123.456");
        generatorBase.writeNumber(bigDecimal);
        assertEquals("123.456", generatorBase.getCurrentValue());
    }

    @Test
    public void testWriteObject() throws IOException {
        SerializableString testString = new StringSerializable("Test String");
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        generatorBase.writeObject(testString);
        assertEquals(testString.getValue(), generatorBase.getCurrentValue());
    }

    @Test
    public void testWriteTree() throws IOException {
        ObjectNode testObjectNode = new ObjectMapper().readTree("{\"test\": \"Test String\"}");
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        generatorBase.writeTree(testObjectNode);
        assertEquals(testObjectNode.toString(), generatorBase.getCurrentValue().asText());
    }

    @Test
    public void testFlush() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        generatorBase.writeString("testString");
        generatorBase.flush();
        assertNull(generatorBase.getCurrentValue());
    }

    @Test
    public void testClose() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        generatorBase.writeString("testString");
        generatorBase.close();
        assertNull(generatorBase.getCurrentValue());
        assertFalse(generatorBase.isOpen());
    }

    @Test
    public void testIsClosed() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        assertFalse(generatorBase.isClosed());
        generatorBase.close();
        assertTrue(generatorBase.isClosed());
    }
}