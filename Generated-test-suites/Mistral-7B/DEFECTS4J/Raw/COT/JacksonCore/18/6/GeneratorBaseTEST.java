package com.fasterxml.jackson.core.base;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
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
    }

    @Test
    public void testVersion() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        assertEquals("2.11.2", generator.version().toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetCurrentValue() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.getCurrentValue();
    }

    @Test
    public void testSetCurrentValue() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.setCurrentValue(null);
    }

    @Test
    public void testIsEnabledWithFeatures() throws IOException {
        GeneratorBase generator = new GeneratorBase(DerivableFeature.forBitMask(Feature.WRITE_NUMBERS_AS_STRINGS | Feature.ESCAPE_NON_ASCII | Feature.STRICT_DUPLICATE_DETECTION).getMask(), null);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertTrue(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
    }

    @Test
    public void testGetFeatureMask() throws IOException {
        GeneratorBase generator = new GeneratorBase(DerivableFeature.forBitMask(Feature.WRITE_NUMBERS_AS_STRINGS | Feature.ESCAPE_NON_ASCII | Feature.STRICT_DUPLICATE_DETECTION).getMask(), null);
        assertEquals(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), generator.getFeatureMask());
    }

    @Test
    public void testEnable() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        generator.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void testDisable() throws IOException {
        GeneratorBase generator = new GeneratorBase(DerivableFeature.forBitMask(Feature.WRITE_NUMBERS_AS_STRINGS).getMask(), null);
        generator.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        generator.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void testSetFeatureMask() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        int mask = Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask();
        generator.setFeatureMask(mask);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
    }

    @Test(expected = IllegalStateException.class)
    public void testSetFeatureMaskWithInvalidBit() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.setFeatureMask(Feature.ESCAPE_NON_ASCII.getMask() | 0xFF00);
    }

    @Test
    public void testOverrideStdFeatures() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.overrideStdFeatures(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        generator.overrideStdFeatures(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), 0);
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void testUseDefaultPrettyPrinter() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        assertNull(generator.getPrettyPrinter());
        generator.useDefaultPrettyPrinter();
        assertNotNull(generator.getPrettyPrinter());
    }

    @Test
    public void testSetCodec() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        assertNull(generator.getCodec());
        ObjectCodec objectCodec = new ObjectMapper().getFactory().createObjectCodec();
        generator.setCodec(objectCodec);
        assertEquals(objectCodec, generator.getCodec());
    }

    @Test(expected = NullPointerException.class)
    public void testSetCodecWithNullObjectCodec() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.setCodec(null);
    }

    @Test
    public void testGetOutputContext() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        assertNotNull(generator.getOutputContext());
    }

    @Test
    public void testWriteFieldName() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        SerializableString name = new SerializableString("name");
        generator.writeFieldName(name);
        assertEquals("name", generator.getCurrentValue());
    }

    @Test
    public void testWriteString() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GeneratorBase generator = new GeneratorBase(0, null) {
            @Override
            public void writeString(String text) throws IOException {
                super.writeString(text);
                outputStream.write(text.getBytes());
            }
        };
        generator.writeString("test");
        generator.close();
        assertEquals("test", new String(outputStream.toByteArray()));
    }

    @Test
    public void testWriteRawValueString() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GeneratorBase generator = new GeneratorBase(0, null) {
            @Override
            public void writeRawValue(String text) throws IOException {
                super.writeRawValue(text);
                outputStream.write(text.getBytes());
            }
        };
        generator.writeRawValue("test");
        generator.close();
        assertEquals("test", new String(outputStream.toByteArray()));
    }

    @Test
    public void testWriteRawValueRaw() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GeneratorBase generator = new GeneratorBase(0, null) {
            @Override
            public void writeRaw(char[] text, int offset, int len) throws IOException {
                super.writeRaw(text, offset, len);
                outputStream.write(text, offset, len);
            }
        };
        generator.writeRawValue("test".getBytes());
        generator.close();
        assertEquals("test", new String(outputStream.toByteArray()));
    }

    @Test
    public void testWriteRawValueSerializableString() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GeneratorBase generator = new GeneratorBase(0, null) {
            @Override
            public void writeRawValue(SerializableString text) throws IOException {
                super.writeRawValue(text);
                outputStream.write(text.getValue().getBytes());
            }
        };
        SerializableString serializableString = new SerializableString("test");
        generator.writeRawValue(serializableString);
        generator.close();
        assertEquals("test", new String(outputStream.toByteArray()));
    }

    @Test(expected = IOException.class)
    public void testWriteNumberInt() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator._verifyValueWrite("write number (int)");
        generator.writeNumber(5);
    }

    @Test(expected = IOException.class)
    public void testWriteNumberLong() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator._verifyValueWrite("write number (long)");
        generator.writeNumber(Long.MAX_VALUE);
    }

    @Test(expected = IOException.class)
    public void testWriteNumberDouble() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator._verifyValueWrite("write number (double)");
        generator.writeNumber(Double.POSITIVE_INFINITY);
    }

    @Test(expected = IOException.class)
    public void testWriteNumberFloat() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator._verifyValueWrite("write number (float)");
        generator.writeNumber(Float.POSITIVE_INFINITY);
    }

    @Test(expected = IOException.class)
    public void testWriteNumberBigDecimal() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator._verifyValueWrite("write number (BigDecimal)");
        generator.writeNumber(new BigDecimal("123456789012345678901234567890"));
    }

    @Test(expected = IOException.class)
    public void testWriteBoolean() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator._verifyValueWrite("write boolean");
        generator.writeBoolean(true);
    }

    @Test(expected = IOException.class)
    public void testWriteNull() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator._verifyValueWrite("write null");
        generator.writeNull();
    }

    @Test
    public void testWriteObject() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GeneratorBase generator = new GeneratorBase(0, null) {
            @Override
            public void writeObject(Object value) throws IOException {
                super.writeObject(value);
                if (value != null) {
                    outputStream.write(value.toString().getBytes());
                }
            }
        };
        generator.writeObject(new String("test"));
        generator.close();
        assertEquals("test", new String(outputStream.toByteArray()));
    }

    @Test
    public void testWriteTree() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GeneratorBase generator = new GeneratorBase(0, null) {
            @Override
            public void writeTree(TreeNode rootNode) throws IOException {
                super.writeTree(rootNode);
                if (rootNode != null) {
                    outputStream.write(rootNode.toString().getBytes());
                }
            }
        };
        TreeNode treeNode = new ObjectMapper().readTree("{\"name\": \"test\"}");
        generator.writeTree(treeNode);
        generator.close();
        assertEquals("{\"name\": \"test\"}", new String(outputStream.toByteArray()));
    }

    @Test
    public void testFlush() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GeneratorBase generator = new GeneratorBase(0, null) {
            @Override
            public void flush() throws IOException {
                super.flush();
                outputStream.flush();
            }
        };
        generator.writeString("test");
        generator.flush();
        generator.close();
        assertEquals("test", new String(outputStream.toByteArray()));
    }

    @Test
    public void testClose() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GeneratorBase generator = new GeneratorBase(0, null) {
            @Override
            public void close() throws IOException {
                super.close();
                outputStream.close();
            }
        };
        generator.writeString("test");
        generator.close();
        assertEquals("test", new String(outputStream.toByteArray()));
    }

    @Test
    public void test_releaseBuffers() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null) {
            @Override
            protected void _releaseBuffers() {
                //This method is not covered in this test file as it is abstract and should be implemented in the subclass.
            }
        };
        assertFalse(generator.isClosed());
        generator.close();
        assertTrue(generator.isClosed());
    }

    @Test(expected = IllegalStateException.class)
    public void test_verifyValueWriteWithNullValue() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator._verifyValueWrite("value is null");
        generator.writeNull();
    }

    @Test(expected = IOException.class)
    public void test_verifyValueWriteUnsupportedOperation() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator._verifyValueWrite("write unsupported operation");
    }

    @Test
    public void test_constructDefaultPrettyPrinter() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        PrettyPrinter prettyPrinter = generator._constructDefaultPrettyPrinter();
        assertNotNull(prettyPrinter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_decodeSurrogateWithInvalidSurrogate() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator._decodeSurrogate(0xD800, 0xDC00);
    }

    @Test
    public void test_decodeSurrogateWithValidSurrogate() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        int charCode = generator._decodeSurrogate(0xD800, 0xDC01);
        assertEquals(0x1037F, charCode);
    }

    @Test(expected = IOException.class)
    public void testWriteBinary() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator._verifyValueWrite("write binary");
        generator.writeBinary(Base64Variant.STANDARD, new ByteArrayInputStream(new byte[0]), 0);
    }

    
}