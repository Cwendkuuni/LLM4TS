package com.fasterxml.jackson.core.base;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.json.DupDetector;
import com.fasterxml.jackson.core.json.JsonWriteContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;

public class GeneratorBaseTest {

    // Test constructor
    @Test
    public void testGeneratorBaseConstructor() throws Exception {
        int features = Feature.AUTO_CLOSE_SOURCE.getMask() | Feature.WRITE_NUMBERS_AS_STRINGS.getMask();
        GeneratorBase gen = new GeneratorBase(features, new StandardSerializerProvider());
        assertNull(gen.getCodec());
        assertEquals(features, gen.getFeatureMask());
        assertTrue(gen.isEnabled(Feature.AUTO_CLOSE_SOURCE));
        assertTrue(gen.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    // Test isEnabled method
    @Test
    public void testIsEnabled() throws Exception {
        int features = Feature.AUTO_CLOSE_SOURCE.getMask() | Feature.WRITE_NUMBERS_AS_STRINGS.getMask()
                | Feature.ESCAPE_NON_ASCII.getMask();
        GeneratorBase gen = new GeneratorBase(features, new StandardSerializerProvider());
        assertTrue(gen.isEnabled(Feature.AUTO_CLOSE_SOURCE));
        assertTrue(gen.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(gen.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertFalse(gen.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
    }

    // Test enable and disable methods
    @Test
    public void testEnableDisable() throws Exception {
        int features = Feature.AUTO_CLOSE_SOURCE.getMask();
        GeneratorBase gen = new GeneratorBase(features, new StandardSerializerProvider());
        // Enable features
        gen.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        gen.enable(Feature.ESCAPE_NON_ASCII);
        assertTrue(gen.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(gen.isEnabled(Feature.ESCAPE_NON_ASCII));
        // Disable features
        gen.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        gen.disable(Feature.ESCAPE_NON_ASCII);
        assertFalse(gen.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(gen.isEnabled(Feature.ESCAPE_NON_ASCII));
    }

    // Test writeObject method with null value
    @Test
    public void testWriteNull() throws Exception {
        GeneratorBase gen = new GeneratorBase(Feature.AUTO_CLOSE_SOURCE.getMask(), new StandardSerializerProvider());
        gen.writeNull();
        assertTrue(gen.isClosed());
    }

    // Test writeString method
    @Test
    public void testWriteString() throws Exception {
        GeneratorBase gen = new GeneratorBase(Feature.AUTO_CLOSE_SOURCE.getMask(), new StandardSerializerProvider());
        String text = "Test string";
        gen.writeString(text);
        assertEquals(text, gen.getCurrentValueAsString());
        assertTrue(gen.isClosed());
    }

    // Test writeRawValue method
    @Test
    public void testWriteRawValue() throws Exception {
        GeneratorBase gen = new GeneratorBase(Feature.AUTO_CLOSE_SOURCE.getMask(), new StandardSerializerProvider());
        String text = "Test raw value";
        gen.writeRawValue(text);
        assertEquals(text, gen.getCurrentValueAsString());
        assertTrue(gen.isClosed());
    }

    // Test writeRawValue method with char array
    @Test
    public void testWriteRawValueCharArray() throws Exception {
        GeneratorBase gen = new GeneratorBase(Feature.AUTO_CLOSE_SOURCE.getMask(), new StandardSerializerProvider());
        String text = "Test raw value";
        gen.writeRawValue(text.toCharArray(), 0, text.length());
        assertEquals(text, gen.getCurrentValueAsString());
        assertTrue(gen.isClosed());
    }

    // Test writeFieldName method
    @Test
    public void testWriteFieldName() throws Exception {
        GeneratorBase gen = new GeneratorBase(Feature.AUTO_CLOSE_SOURCE.getMask(), new StandardSerializerProvider());
        String fieldName = "Test field name";
        gen.writeFieldName(fieldName);
        assertEquals(fieldName, gen.getCurrentValueAsString());
        assertTrue(gen.isClosed());
    }

    // Test writeFieldName method with SerializableString
    @Test
    public void testWriteFieldNameSerializableString() throws Exception {
        GeneratorBase gen = new GeneratorBase(Feature.AUTO_CLOSE_SOURCE.getMask(), new StandardSerializerProvider());
        SerializableString fieldName = new StringSerializable("Test field name");
        gen.writeFieldName(fieldName);
        assertEquals(fieldName.getValue(), gen.getCurrentValueAsString());
        assertTrue(gen.isClosed());
    }

    // Test writeObject method with null object
    @Test
    public void testWriteObjectNull() throws Exception {
        GeneratorBase gen = new GeneratorBase(Feature.AUTO_CLOSE_SOURCE.getMask(), new StandardSerializerProvider());
        gen.writeObject(null);
        assertEquals("null", gen.getCurrentValueAsString());
        assertTrue(gen.isClosed());
    }

    // Test writeObject method with String object
    @Test
    public void testWriteObjectString() throws Exception {
        GeneratorBase gen = new GeneratorBase(Feature.AUTO_CLOSE_SOURCE.getMask(), new StandardSerializerProvider());
        String obj = "Test string object";
        gen.writeObject(obj);
        assertEquals(obj, gen.getCurrentValueAsString());
        assertTrue(gen.isClosed());
    }

    // Test writeObject method with char array object
    @Test
    public void testWriteObjectCharArray() throws Exception {
        GeneratorBase gen = new GeneratorBase(Feature.AUTO_CLOSE_SOURCE.getMask(), new StandardSerializerProvider());
        char[] obj = {'T', 'e', 's', 't', ' ', 'c', 'h', 'a', 'r', ' ', 'a', 'r', 'r', 'a', 'y'};
        gen.writeObject(obj);
        assertEquals(new String(obj), gen.getCurrentValueAsString());
        assertTrue(gen.isClosed());
    }

    // Test writeObject method with custom Serializable class
    @Test
    public void testWriteObjectCustomClass() throws Exception {
        GeneratorBase gen = new GeneratorBase(Feature.AUTO_CLOSE_SOURCE.getMask(), new StandardSerializerProvider());
        CustomClass obj = new CustomClass("Test custom class");
        gen.writeObject(obj);
        assertEquals(obj.toString(), gen.getCurrentValueAsString());
        assertTrue(gen.isClosed());
    }

    // Test writeTree method with TreeNode object
    @Test
    public void testWriteTree() throws Exception {
        GeneratorBase gen = new GeneratorBase(Feature.AUTO_CLOSE_SOURCE.getMask(), new StandardSerializerProvider());
        TreeNode rootNode = new ObjectNode(new StandardObjectCodec());
        rootNode.put("name", "Test tree node");
        gen.writeTree(rootNode);
        assertEquals(rootNode.toString(), gen.getCurrentValueAsString());
        assertTrue(gen.isClosed());
    }

    // Test writeRaw method with unsupported base64 variant
    @Test(expected = IOException.class)
    public void testWriteRawUnsupportedBase64Variant() throws Exception {
        GeneratorBase gen = new GeneratorBase(Feature.AUTO_CLOSE_SOURCE.getMask(), new StandardSerializerProvider());
        InputStream data = new ByteArrayInputStream("Test data".getBytes());
        gen.writeBinary(Base64Variant.PGP, data, 10);
    }

    // Test writeNumber method with int value
    @Test
    public void testWriteNumberInt() throws Exception {
        GeneratorBase gen = new GeneratorBase(Feature.AUTO_CLOSE_SOURCE.getMask(), new StandardSerializerProvider());
        int number = 123;
        gen.writeNumber(number);
        assertEquals(Integer.toString(number), gen.getCurrentValueAsString());
        assertTrue(gen.isClosed());
    }

    // Test writeNumber method with long value
    @Test
    public void testWriteNumberLong() throws Exception {
        GeneratorBase gen = new GeneratorBase(Feature.AUTO_CLOSE_SOURCE.getMask(), new StandardSerializerProvider());
        long number = 9876543210L;
        gen.writeNumber(number);
        assertEquals(Long.toString(number), gen.getCurrentValueAsString());
        assertTrue(gen.isClosed());
    }

    // Test writeNumber method with double value
    @Test
    public void testWriteNumberDouble() throws Exception {
        GeneratorBase gen = new GeneratorBase(Feature.AUTO_CLOSE_SOURCE.getMask(), new StandardSerializerProvider());
        double number = 123.45;
        gen.writeNumber(number);
        assertEquals(Double.toString(number), gen.getCurrentValueAsString());
        assertTrue(gen.isClosed());
    }

    // Test writeNumber method with float value
    @Test
    public void testWriteNumberFloat() throws Exception {
        GeneratorBase gen = new GeneratorBase(Feature.AUTO_CLOSE_SOURCE.getMask(), new StandardSerializerProvider());
        float number = 123.45f;
        gen.writeNumber(number);
        assertEquals(Float.toString(number), gen.getCurrentValueAsString());
        assertTrue(gen.isClosed());
    }

    // Test writeNumber method with BigDecimal value
    @Test
    public void testWriteNumberBigDecimal() throws Exception {
        GeneratorBase gen = new GeneratorBase(Feature.AUTO_CLOSE_SOURCE.getMask() | Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), new StandardSerializerProvider());
        BigDecimal number = new BigDecimal("123.45");
        gen.writeNumber(number);
        assertEquals(number.toString(), gen.getCurrentValueAsString());
        assertTrue(gen.isClosed());
    }

    // Test writeBoolean method with boolean value
    @Test
    public void testWriteBoolean() throws Exception {
        GeneratorBase gen = new GeneratorBase(Feature.AUTO_CLOSE_SOURCE.getMask(), new StandardSerializerProvider());
        boolean booleanValue = true;
        gen.writeBoolean(booleanValue);
        assertEquals(Boolean.toString(booleanValue), gen.getCurrentValueAsString());
        assertTrue(gen.isClosed());
    }

    // Test writeNull method
    @Test
    public void testWriteNullMethod() throws Exception {
        GeneratorBase gen = new GeneratorBase(Feature.AUTO_CLOSE_SOURCE.getMask(), new StandardSerializerProvider());
        gen.writeNull();
        assertNull(gen.getCurrentValue());
        assertTrue(gen.isClosed());
    }

    // Test close method
    @Test
    public void testClose() throws Exception {
        GeneratorBase gen = new GeneratorBase(Feature.AUTO_CLOSE_SOURCE.getMask(), new StandardSerializerProvider());
        gen.close();
        assertTrue(gen.isClosed());
    }

    // Test isClosed method
    @Test
    public void testIsClosed() throws Exception {
        GeneratorBase gen = new GeneratorBase(Feature.AUTO_CLOSE_SOURCE.getMask(), new StandardSerializerProvider());
        assertTrue(gen.isClosed());
    }

    // Test _releaseBuffers method
    @Test
    public void testReleaseBuffers() throws Exception {
        GeneratorBase gen = new GeneratorBase(Feature.AUTO_CLOSE_SOURCE.getMask(), new StandardSerializerProvider());
        gen._releaseBuffers();
        assertTrue(gen.isClosed());
    }

    // Test _verifyValueWrite method with unsupported value write
    @Test(expected = IOException.class)
    public void testVerifyValueWriteUnsupportedValueWrite() throws Exception {
        GeneratorBase gen = new GeneratorBase(Feature.AUTO_CLOSE_SOURCE.getMask(), new StandardSerializerProvider());
        gen._verifyValueWrite("write binary value");
    }

    // Test _constructDefaultPrettyPrinter method
    @Test
    public void testConstructDefaultPrettyPrinter() throws Exception {
        GeneratorBase gen = new GeneratorBase(Feature.AUTO_CLOSE_SOURCE.getMask(), new StandardSerializerProvider());
        PrettyPrinter pp = gen._constructDefaultPrettyPrinter();
        assertSame(DefaultPrettyPrinter.class, pp.getClass());
    }
}