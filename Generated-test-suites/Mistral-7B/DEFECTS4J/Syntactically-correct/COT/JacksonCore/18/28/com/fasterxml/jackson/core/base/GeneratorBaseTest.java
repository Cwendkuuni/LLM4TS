package com.fasterxml.jackson.core.base;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.Test;

public class GeneratorBaseTest {

    

    @Test
    public void testIsEnabled() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        generator.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        generator.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void testGetFeatureMask() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        int featureMask = generator.getFeatureMask();
        assertTrue(featureMask == 0);

        generator.overrideStdFeatures(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
        featureMask = generator.getFeatureMask();
        assertTrue(featureMask == Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedWriteBinary() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.writeBinary(null, null, 0);
    }

    @Test
    public void testWriteString() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.writeString("test");
    }

    @Test
    public void testWriteRawValue() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.writeRawValue("test");
    }

    @Test
    public void testWriteRawValue_charArray() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        char[] text = {'t', 'e', 's', 't'};
        generator.writeRawValue(text, 0, text.length);
    }

    @Test
    public void testWriteRawValue_SerializableString() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        SerializableString text = new SerializableString("test");
        generator.writeRawValue(text);
    }

    @Test
    public void testWriteObject() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.writeObject("test");
    }

    @Test
    public void testWriteTree() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        TreeNode treeNode = new ObjectNode(JSONFactory.instance);
        treeNode.put("test", "test");
        generator.writeTree(treeNode);
    }

    @Test
    public void testFlush() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        // Add your test code here
        generator.flush();
    }

    @Test
    public void testClose() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.close();
        assertTrue(generator.isClosed());
    }

    @Test
    public void testIsClosed() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        assertFalse(generator.isClosed());
        generator.close();
        assertTrue(generator.isClosed());
    }

    @Test
    public void testWriteNumber_int() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.writeNumber(42);
    }

    @Test
    public void testWriteNumber_long() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.writeNumber(42L);
    }

    @Test
    public void testWriteNumber_double() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.writeNumber(42.0);
    }

    @Test
    public void testWriteNumber_float() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.writeNumber(42.0f);
    }

    @Test
    public void testWriteNumber_BigDecimal() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        BigDecimal decimal = new BigDecimal("42");
        generator.writeNumber(decimal);
    }

    @Test
    public void testWriteBoolean() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.writeBoolean(true);
    }

    @Test
    public void testWriteNull() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.writeNull();
    }

    
}