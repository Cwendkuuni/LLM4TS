package com.fasterxml.jackson.core.base;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GeneratorBaseTest {

    private GeneratorBase generatorBase;

    @Before
    public void setUp() {
        generatorBase = new GeneratorBaseImpl();
    }

    @Test
    public void testWriteFieldName() {
        String fieldName = "testField";
        try {
            generatorBase.writeFieldName(fieldName);
            Assert.assertEquals(fieldName, generatorBase._writeContext.getCurrentName());
        } catch (Exception e) {
            Assert.fail("Exception should not be thrown");
        }
    }

    @Test
    public void testWriteFieldNameWithNullFieldName() {
        try {
            generatorBase.writeFieldName(null);
            Assert.assertNull(generatorBase._writeContext.getCurrentName());
        } catch (Exception e) {
            Assert.fail("Exception should not be thrown");
        }
    }

    @Test
    public void testWriteString() {
        String text = "testString";
        try {
            generatorBase.writeString(text);
            Assert.assertEquals(text, generatorBase._writeContext.getCurrentValue().toString());
        } catch (Exception e) {
            Assert.fail("Exception should not be thrown");
        }
    }

    @Test
    public void testWriteStringWithNullText() {
        try {
            generatorBase.writeString(null);
            Assert.assertNull(generatorBase._writeContext.getCurrentValue());
        } catch (Exception e) {
            Assert.fail("Exception should not be thrown");
        }
    }

    @Test
    public void testWriteRaw() {
        String rawValue = "testRawValue";
        try {
            generatorBase.writeRaw(rawValue);
            Assert.assertEquals(rawValue, generatorBase._writeContext.getCurrentValue().toString());
        } catch (Exception e) {
            Assert.fail("Exception should not be thrown");
        }
    }

    @Test
    public void testWriteRawWithNullText() {
        try {
            generatorBase.writeRaw(null);
            Assert.assertNull(generatorBase._writeContext.getCurrentValue());
        } catch (Exception e) {
            Assert.fail("Exception should not be thrown");
        }
    }

    @Test
    public void testWriteRawValue() {
        String rawValue = "testRawValue";
        try {
            generatorBase.writeRawValue(rawValue);
            Assert.assertEquals(rawValue, generatorBase._writeContext.getCurrentValue().toString());
        } catch (Exception e) {
            Assert.fail("Exception should not be thrown");
        }
    }

    @Test
    public void testWriteRawValueWithNullText() {
        try {
            generatorBase.writeRawValue(null);
            Assert.assertNull(generatorBase._writeContext.getCurrentValue());
        } catch (Exception e) {
            Assert.fail("Exception should not be thrown");
        }
    }

    private static class GeneratorBaseImpl extends GeneratorBase {

        GeneratorBaseImpl() {
            super(0, null);
        }

        @Override
        protected void _releaseBuffers() {
        }

        @Override
        protected void _verifyValueWrite(String typeMsg) throws IOException {
        }
    }
}