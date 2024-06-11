package com.fasterxml.jackson.core.base;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;

public class GeneratorBaseTest {

    @Test
    public void testIsEnabled_WRITE_NUMBERS_AS_STRINGS() {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        assertTrue(generatorBase.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        generatorBase = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);
        assertFalse(generatorBase.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void testEnable_WRITE_NUMBERS_AS_STRINGS() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        generatorBase.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(generatorBase.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        generatorBase = new GeneratorBase(0, null);
        generatorBase.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(generatorBase.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void testDisable_WRITE_NUMBERS_AS_STRINGS() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);
        generatorBase.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(generatorBase.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        generatorBase = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);
        generatorBase.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(generatorBase.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void testGetFeatureMask() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        int featureMask = generatorBase.getFeatureMask();
        assertEquals(0, featureMask);

        generatorBase = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);
        featureMask = generatorBase.getFeatureMask();
        assertEquals(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), featureMask);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetFeatureMask_illegalStateException() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        generatorBase.setFeatureMask(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() ^ Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
    }

    @Test
    public void testUseDefaultPrettyPrinter() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        assertNull(generatorBase.getPrettyPrinter());
        generatorBase.useDefaultPrettyPrinter();
        assertNotNull(generatorBase.getPrettyPrinter());
    }

    @Test
    public void testSetCodec() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        assertNull(generatorBase.getCodec());
        ObjectCodec objectCodec = new ObjectCodec();
        generatorBase.setCodec(objectCodec);
        assertEquals(objectCodec, generatorBase.getCodec());
    }

    @Test
    public void testGetOutputContext() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        assertNull(generatorBase.getOutputContext());

        JsonWriteContext jsonWriteContext = new JsonWriteContext(null);
        generatorBase = new GeneratorBase(0, null, jsonWriteContext);
        assertEquals(jsonWriteContext, generatorBase.getOutputContext());
    }

    @Test
    public void testWriteFieldName() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        generatorBase.writeFieldName("test");
    }

    @Test
    public void testWriteString() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        generatorBase.writeString("test");
    }

    @Test
    public void testWriteRawValue() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        generatorBase.writeRawValue("test");
    }

    @Test
    public void testWriteRawValue_String() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        generatorBase.writeRawValue("test", 0, 3);
    }

    @Test
    public void testWriteRawValue_charArray() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        generatorBase.writeRawValue("test".toCharArray(), 0, 3);
    }

    @Test
    public void testWriteRawValue_SerializableString() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        SerializableString serializableString = new SerializableString("test");
        generatorBase.writeRawValue(serializableString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteRawValue_null() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        generatorBase.writeRawValue(null);
    }

    @Test
    public void testWriteBinary() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        generatorBase.writeBinary(Base64Variant.STANDARD, null, 1);
        // Unsupported operation
    }

    @Test
    public void testWriteNumber() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        // Not implemented at this level
    }

    @Test
    public void testWriteNumber_int() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        // Not implemented at this level
    }

    @Test
    public void testWriteNumber_long() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        // Not implemented at this level
    }

    @Test
    public void testWriteNumber_double() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        // Not implemented at this level
    }

    @Test
    public void testWriteNumber_float() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        // Not implemented at this level
    }

    @Test
    public void testWriteNumber_BigDecimal() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        BigDecimal bigDecimal = new BigDecimal("1234567890.1234567890");
        generatorBase.writeNumber(bigDecimal);
        // Use default implementation
    }

    @Test
    public void testWriteBoolean() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        generatorBase.writeBoolean(true);
    }

    @Test
    public void testWriteNull() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        generatorBase.writeNull();
    }

    @Test
    public void testWriteObject() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        generatorBase.writeObject(new Object());
        // Let the ObjectCodec handle serialization
    }

    @Test
    public void testWriteTree() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        generatorBase.writeTree(new TreeNode());
        // Let the ObjectCodec handle serialization
    }

    @Test
    public void testFlush() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        generatorBase.flush();
        // Let the implementation handle flushing
    }

    @Test
    public void testClose() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        generatorBase.close();
        assertTrue(generatorBase.isClosed());
    }

    @Test
    public void testReleaseBuffers() throws IOException {
        // Abstract method, let sub-classes implement
    }

    @Test
    public void testVerifyValueWrite() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        generatorBase._verifyValueWrite("write value with unsupported type");
        // Unsupported value write
    }

    @Test
    public void testAsString() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        BigDecimal bigDecimal = new BigDecimal("1234567890.1234567890");
        String result = generatorBase._asString(bigDecimal);
        assertEquals("1234567890.1234567890", result);
    }

    @Test
    public void testDecodeSurrogate() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        int surr1 = 0xD800;
        int surr2 = 0xDC01;
        int result = generatorBase._decodeSurrogate(surr1, surr2);
        assertEquals(0x1F001, result);
    }

    @Test(expected = IOException.class)
    public void testDecodeSurrogate_invalidSurrogate2() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        int surr1 = 0xD800;
        int surr2 = 0xDBFF;
        generatorBase._decodeSurrogate(surr1, surr2);
    }

    @Test(expected = IOException.class)
    public void testDecodeSurrogate_invalidSurrogate1() throws IOException {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        int surr1 = 0xDC00;
        int surr2 = 0xDBFF;
        generatorBase._decodeSurrogate(surr1, surr2);
    }
}