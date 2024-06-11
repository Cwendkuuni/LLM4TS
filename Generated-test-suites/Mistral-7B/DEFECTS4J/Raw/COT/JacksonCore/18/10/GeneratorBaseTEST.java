package com.fasterxml.jackson.core.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

public class GeneratorBaseTest {

    private GeneratorBase generator;
    private JsonEncoding encoding;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws IOException {
        generator = new GeneratingBaseTestGenerator(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);
        encoding = JsonEncoding.UTF8;
        outputStream = new ByteArrayOutputStream();
        generator.setWriter(new OutputStreamWriter(outputStream, encoding.getCharacterEncodings().standardCharsets().UTF_8));
    }

    @Test
    public void testVersion() {
        Version version = generator.version();
        assertEquals("2.11.2", version.getVersion());
    }

    @Test
    public void testIsEnabled_WRITE_NUMBERS_AS_STRINGS() {
        assertTrue(generator.isEnabled(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void testIsEnabled_WRITE_NUMBER_AS_INT() {
        assertFalse(generator.isEnabled(JsonGenerator.Feature.WRITE_NUMBER_AS_INT));
    }

    @Test
    public void testSetFeatureMask_WRITE_NUMBERS_AS_STRINGS() throws IOException {
        generator.setFeatureMask(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
        assertTrue(generator.isEnabled(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetFeatureMask_INVALID_MASK() throws IOException {
        generator.setFeatureMask(-1);
    }

    @Test
    public void testUseDefaultPrettyPrinter() throws IOException {
        PrettyPrinter prettyPrinter = generator.useDefaultPrettyPrinter();
        assertNotNull(prettyPrinter);
    }

    @Test
    public void testSetCodec() throws IOException {
        ObjectCodec objectCodec = new ObjectMapper().getFactory().createObjectCodec();
        generator.setCodec(objectCodec);
        assertSame(objectCodec, generator.getCodec());
    }

    @Test
    public void testGetOutputContext() throws IOException {
        JsonWriteContext jsonWriteContext = generator.getOutputContext();
        assertNotNull(jsonWriteContext);
    }

    @Test
    public void testWriteNull() throws IOException {
        generator.writeNull();
        assertEquals("null", outputStream.toString());
    }

    @Test
    public void testWriteBoolean() throws IOException {
        generator.writeBoolean(true);
        assertEquals("true", outputStream.toString());

        generator.writeBoolean(false);
        assertEquals("false", outputStream.toString());
    }

    @Test
    public void testWriteNumber_int() throws IOException {
        generator.writeNumber(42);
        assertEquals("42", outputStream.toString());
    }

    @Test
    public void testWriteNumber_long() throws IOException {
        generator.writeNumber(Long.MAX_VALUE);
        assertEquals("9223372036854775807", outputStream.toString());
    }

    @Test
    public void testWriteNumber_float() throws IOException {
        generator.writeNumber(Float.POSITIVE_INFINITY);
        assertEquals("null", outputStream.toString());

        generator.writeNumber(Float.NaN);
        assertEquals("null", outputStream.toString());

        generator.writeNumber(3.14f);
        assertEquals("3.14", outputStream.toString());
    }

    @Test
    public void testWriteNumber_double() throws IOException {
        generator.writeNumber(Double.POSITIVE_INFINITY);
        assertEquals("null", outputStream.toString());

        generator.writeNumber(Double.NaN);
        assertEquals("null", outputStream.toString());

        generator.writeNumber(1.23456);
        assertEquals("1.23456", outputStream.toString());
    }

    @Test
    public void testWriteNumber_BigDecimal() throws IOException {
        BigDecimal bigDecimal = new BigDecimal("123456.789");
        generator.writeNumber(bigDecimal);
        assertEquals("123456.789", outputStream.toString());
    }

    @Test
    public void testWriteString() throws IOException {
        generator.writeString("Hello, World!");
        assertEquals("Hello, World!", outputStream.toString());
    }

    @Test
    public void testWriteRawValue_String() throws IOException {
        generator.writeRawValue("Hello, World!");
        assertEquals("Hello, World!", outputStream.toString());
    }

    @Test
    public void testWriteRawValue_String_with_offset_and_length() throws IOException {
        String str = "Hello, World!";
        generator.writeRawValue(str, 0, str.length());
        assertEquals("Hello, World!", outputStream.toString());
    }

    @Test
    public void testWriteRawValue_char_array() throws IOException {
        char[] arr = {'H', 'e', 'l', 'l', 'o', ',', ' ', 'W', 'o', 'r', 'l', 'd', '!'};
        generator.writeRawValue(arr, 0, arr.length);
        assertEquals(new String(arr), outputStream.toString());
    }

    @Test
    public void testWriteRawValue_SerializableString() throws IOException {
        SerializableString serializableString = new String("Hello, World!");
        generator.writeRawValue(serializableString);
        assertEquals("Hello, World!", outputStream.toString());
    }

    @Test
    public void testWriteRawValue_char_array_with_offset_and_length() throws IOException {
        char[] arr = {'H', 'e', 'l', 'l', 'o', ',', ' ', 'W', 'o', 'r', 'l', 'd', '!'};
        generator.writeRawValue(arr, 0, arr.length);
        assertEquals(new String(arr), outputStream.toString());
    }

    @Test
    public void testWriteRawValue_SerializableString_with_offset_and_length() throws IOException {
        SerializableString serializableString = new String("Hello, World!");
        generator.writeRawValue(serializableString, 0, serializableString.length());
        assertEquals(serializableString.getValue(), outputStream.toString());
    }

    @Test
    public void testWriteBinary() throws IOException {
        fail("Method writeBinary is not implemented in this test case.");
    }

    @Test
    public void testWriteObject() throws IOException {
        String str = "Hello, World!";
        generator.writeObject(str);
        assertEquals("Hello, World!", outputStream.toString());
    }

    @Test
    public void testWriteTree() throws IOException {
        String jsonString = "{\"name\": \"John\", \"age\": 30, \"city\": \"New York\"}";
        TreeNode treeNode = MapperFactory.getJsonFactory().readValue(jsonString, TreeNode.class);
        generator.writeTree(treeNode);
        assertEquals(jsonString, outputStream.toString());
    }

    @Test
    public void testFlush() throws IOException {
        generator.flush();
        assertEquals("", outputStream.toString());
    }

    @Test
    public void testClose() throws IOException {
        generator.close();
        assertFalse(generator.isClosed());

        generator = new GeneratingBaseTestGenerator(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);
        generator.setWriter(new OutputStreamWriter(outputStream, encoding.getCharacterEncodings().standardCharsets().UTF_8));
        generator.writeNumber(42);
        generator.close();
        assertTrue(generator.isClosed());
    }

    @Test
    public void testIsClosed() throws IOException {
        assertFalse(generator.isClosed());

        generator.close();
        assertTrue(generator.isClosed());
    }

    private static class GeneratingBaseTestGenerator extends GeneratorBase {
        protected GeneratingBaseTestGenerator(int featureMask, ObjectCodec objectCodec) {
            super(featureMask, objectCodec);
        }

        @Override
        protected void _releaseBuffers() throws IOException {
            // Empty implementation
        }

        @Override
        protected void _verifyValueWrite(String typeMsg) throws IOException {
            // Empty implementation
        }

        @Override
        protected PrettyPrinter _constructDefaultPrettyPrinter() {
            return new DefaultPrettyPrinter();
        }
    }
}