package com.fasterxml.jackson.core.base;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.Test;

public class GeneratorBaseTest {
    // Test constructor
    @Test
    public void testGeneratorBaseConstructor() {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        assertNotNull(generatorBase);

        GeneratorBase generatorBaseWithContext = new GeneratorBase(0, null, null);
        assertNotNull(generatorBaseWithContext);
    }

    // Test version method
    @Test
    public void testVersion() throws Exception {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        Version version = generatorBase.version();
        assertNotNull(version);
    }

    // Test getCurrentValue and setCurrentValue methods
    @Test
    public void testCurrentValue() throws Exception {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        Object currentValue = generatorBase.getCurrentValue();
        assertNull(currentValue);

        generatorBase.setCurrentValue("test");
        currentValue = generatorBase.getCurrentValue();
        assertEquals("test", currentValue);
    }

    // Test isEnabled method
    @Test
    public void testIsEnabled() throws Exception {
        Feature feature = Feature.WRITE_NUMBERS_AS_STRINGS;
        GeneratorBase generatorBase = new GeneratorBase(feature.getMask(), null);
        assertTrue(generatorBase.isEnabled(feature));

        generatorBase.disable(feature);
        assertFalse(generatorBase.isEnabled(feature));

        generatorBase.enable(feature);
        assertTrue(generatorBase.isEnabled(feature));
    }

    // Test writeString method
    @Test
    public void testWriteString() throws Exception {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        generatorBase.writeString("test");

        generatorBase = new GeneratorBase(0, null);
        generatorBase.writeString((SerializableString) "test");

        generatorBase = new GeneratorBase(0, null);
        generatorBase.writeRawValue("test");

        generatorBase = new GeneratorBase(0, null);
        generatorBase.writeRawValue((SerializableString) "test");
    }

    // Test writeRawValue method with String and char array
    @Test
    public void testWriteRawValueStringAndCharArray() throws Exception {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        generatorBase.writeRawValue("test");

        char[] charArray = {'t', 'e', 's', 't'};
        generatorBase.writeRawValue(charArray, 0, charArray.length);
    }

    // Test writeRawValue method with SerializableString
    @Test
    public void testWriteRawValueSerializableString() throws Exception {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        generatorBase.writeRawValue((SerializableString) "test");

        SerializableString serializableString = new SerializableString("test");
        generatorBase.writeRawValue(serializableString);
    }

    // Test writeObject method
    @Test
    public void testWriteObject() throws Exception {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        generatorBase.writeObject(new String("test"));

        generatorBase = new GeneratorBase(0, null);
        generatorBase.writeObject((SerializableString) new String("test"));

        generatorBase = new GeneratorBase(0, null);
        generatorBase.writeObject(new Object());

        generatorBase = new GeneratorBase(0, null);
        generatorBase.writeObject((SerializableString) new Object());
    }

    // Test writeTree method
    @Test
    public void testWriteTree() throws Exception {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        generatorBase.writeTree(new TreeNode());

        generatorBase = new GeneratorBase(0, null);
        generatorBase.writeTree((SerializableString) new TreeNode());
    }

    // Test flush and close methods
    @Test
    public void testFlushAndClose() throws Exception {
        GeneratorBase generatorBase = new GeneratorBase(0, null);

        generatorBase.flush();
        generatorBase.close();

        generatorBase = new GeneratorBase(0, null);
        generatorBase.close();

        generatorBase = new GeneratorBase(0, null);
        generatorBase.flush();
        generatorBase.close();
    }

    // Test _verifyValueWrite method
    @Test(expected = IOException.class)
    public void testVerifyValueWrite() throws Exception {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        generatorBase._verifyValueWrite("write a boolean value");
        generatorBase.writeBoolean(true);
    }

    // Test writeNumber method edge cases
    @Test(expected = IOException.class)
    public void testWriteNumberEdgeCases() throws Exception {
        GeneratorBase generatorBase = new GeneratorBase(0, null);

        generatorBase._verifyValueWrite("write number");
        generatorBase.writeNumber(-128);

        generatorBase._verifyValueWrite("write number");
        generatorBase.writeNumber(Integer.MAX_VALUE);

        generatorBase._verifyValueWrite("write number");
        generatorBase.writeNumber(Long.MAX_VALUE);

        generatorBase._verifyValueWrite("write number");
        generatorBase.writeNumber(1.0F / 0.0F);

        generatorBase._verifyValueWrite("write number");
        generatorBase.writeNumber(Double.POSITIVE_INFINITY);

        generatorBase._verifyValueWrite("write number");
        generatorBase.writeNumber(Double.NEGATIVE_INFINITY);

        generatorBase._verifyValueWrite("write number");
        generatorBase.writeNumber(new BigDecimal("1.0E308"));
    }

    // Test _decodeSurrogate method
    @Test(expected = IOException.class)
    public void testDecodeSurrogateInvalidSurrogate() throws Exception {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        generatorBase._decodeSurrogate(0xD800, 0xDBFF);
    }

    // Test _decodeSurrogate method
    @Test
    public void testDecodeSurrogateValidSurrogate() throws Exception {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        int charCode = generatorBase._decodeSurrogate(0xD800, 0xDC01);
        assertEquals(0x1037F, charCode);
    }
}