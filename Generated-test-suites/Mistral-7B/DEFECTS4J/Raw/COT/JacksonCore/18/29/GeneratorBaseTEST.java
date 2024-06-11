import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonSerializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;

import org.junit.Test;

public class GeneratorBaseTest {

    

    @Test
    public void testIsEnabled() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);

        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertFalse(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        generator = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertFalse(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        generator = new GeneratorBase(Feature.ESCAPE_NON_ASCII.getMask() | Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertFalse(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        generator = new GeneratorBase(Feature.STRICT_DUPLICATE_DETECTION.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertTrue(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
    }

    @Test
    public void testGetFeatureMask() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        assertEquals(0, generator.getFeatureMask());

        generator = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);
        assertEquals(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), generator.getFeatureMask());

        generator = new GeneratorBase(Feature.ESCAPE_NON_ASCII.getMask() | Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);
        assertEquals(Feature.ESCAPE_NON_ASCII.getMask() | Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), generator.getFeatureMask());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFeatureMask() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.setFeatureMask(Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
    }

    @Test
    public void testEnableFeature() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);

        generator.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        generator.enable(Feature.ESCAPE_NON_ASCII);
        assertTrue(generator.isEnabled(Feature.ESCAPE_NON_ASCII));

        generator.enable(Feature.STRICT_DUPLICATE_DETECTION);
        assertTrue(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        generator.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertTrue(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
    }

    @Test
    public void testDisableFeature() throws IOException {
        GeneratorBase generator = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), null);

        generator.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        generator.disable(Feature.ESCAPE_NON_ASCII);
        assertFalse(generator.isEnabled(Feature.ESCAPE_NON_ASCII));

        generator.disable(Feature.STRICT_DUPLICATE_DETECTION);
        assertFalse(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        generator.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertFalse(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
    }

    @Test
    public void testUseDefaultPrettyPrinter() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.useDefaultPrettyPrinter();
    }

    @Test
    public void testSetCodec() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        ObjectCodec codec = new JsonSerializer.StdSerializer<Object>(Object.class);
        generator.setCodec(codec);
    }

    @Test
    public void testGetCodec() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        ObjectCodec codec = new JsonSerializer.StdSerializer<Object>(Object.class);
        generator.setCodec(codec);
        assertEquals(codec, generator.getCodec());
    }

    @Test
    public void testGetOutputContext() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        assertEquals(null, generator.getOutputContext());
    }

    @Test
    public void testWriteString() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(output);
        generator.setOutput(writer);

        generator.writeString("Test string");
        generator.flush();
        assertEquals("Test string", output.toString());
    }

    @Test
    public void testWriteRawValue() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(output);
        generator.setOutput(writer);

        generator.writeRawValue("Test raw value");
        generator.flush();
        assertEquals("Test raw value", output.toString());
    }

    @Test
    public void testWriteRawValueWithOffsetAndLength() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(output);
        generator.setOutput(writer);

        generator.writeRawValue("Test raw value", 0, "Test raw value".length());
        generator.flush();
        assertEquals("Test raw value", output.toString());
    }

    @Test
    public void testWriteRawValueWithCharArray() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(output);
        generator.setOutput(writer);

        generator.writeRawValue(new char[]{'T', 'e', 's', 't', ' ', 'r', 'a', 'w', ' ', 'v', 'a', 'l', 'u', 'e'}, 0, 13);
        generator.flush();
        assertEquals("Test raw value", output.toString());
    }

    @Test
    public void testWriteStringWithSerializableString() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(output);
        generator.setOutput(writer);

        generator.writeString(new com.fasterxml.jackson.core.io.SerializableString("Test serializable string"));
        generator.flush();
        assertEquals("Test serializable string", output.toString());
    }

    @Test(expected = IOException.class)
    public void testWriteBinary() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        generator.setOutput(output);

        generator.writeBinary(JsonGenerator.Base64Variant.STANDARD, new ByteArrayInputStream(new byte[0]), 0);
    }

    @Test
    public void testWriteNumber() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(output);
        generator.setOutput(writer);

        generator.writeNumber(123);
        generator.flush();
        assertEquals("123", output.toString());

        generator.writeNumber(456);
        generator.flush();
        assertEquals("456", output.toString());

        generator.writeNumber(123.45);
        generator.flush();
        assertEquals("123.45", output.toString());
    }

    @Test
    public void testWriteBoolean() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(output);
        generator.setOutput(writer);

        generator.writeBoolean(true);
        generator.flush();
        assertEquals("true", output.toString());

        generator.writeBoolean(false);
        generator.flush();
        assertEquals("false", output.toString());
    }

    @Test
    public void testWriteNull() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(output);
        generator.setOutput(writer);

        generator.writeNull();
        generator.flush();
        assertEquals("null", output.toString());
    }

    @Test
    public void testWriteObject() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(output);
        generator.setOutput(writer);

        generator.writeObject(new Object());
        generator.flush();
        assertEquals("{}", output.toString());
    }

    @Test
    public void testWriteTree() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(output);
        generator.setOutput(writer);

        TreeNode treeNode = new Object();
        generator.writeTree(treeNode);
        generator.flush();
        assertEquals("{}", output.toString());
    }

    @Test
    public void testFlush() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(output);
        generator.setOutput(writer);

        generator.writeNumber(123);
        generator.flush();
        assertEquals("123", output.toString());

        generator.writeString("Test string");
        generator.flush();
        assertEquals("123", output.toString());

        generator.writeString("Test string");
        generator.writeNumber(456);
        generator.flush();
        assertEquals("Test string\n123", output.toString());
    }

    @Test
    public void testClose() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        genertor.close();
        assertFalse(generator.isClosed());

        generator = new GeneratorBase(0, null);
        generator.writeString("Test string");
        generator.close();
        assertTrue(generator.isClosed());
    }

    @Test(expected = IOException.class)
    public void testWriteAfterClose() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.writeString("Test string");
        generator.close();
        generator.writeString("Test string after close");
    }

    @Test
    public void test_releaseBuffers() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null) {
            @Override
            protected void _releaseBuffers() throws IOException {
                // no implementation here
            }
        };

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(output);
        generator.setOutput(writer);

        generator.writeString("Test string");
        generator.flush();
        generator._releaseBuffers();
        generator.writeString("Test string after release");
        generator.flush();
        assertEquals("Test string\nTest string after release", output.toString());
    }

    @Test
    public void test_verifyValueWrite() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null) {
            @Override
            protected void _verifyValueWrite(String typeMsg) throws IOException {
                // no implementation here
            }
        };

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(output);
        generator.setOutput(writer);

        try {
            generator.writeNumber(123);
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void test_constructDefaultPrettyPrinter() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        PrettyPrinter prettyPrinter = generator._constructDefaultPrettyPrinter();
        assertNotNull(prettyPrinter);
        assertTrue(prettyPrinter instanceof DefaultPrettyPrinter);
    }

    @Test(expected = IllegalStateException.class)
    public void testNoObjectCodecDefined() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.writeObject(new Object());
    }

    @Test
    public void testWriteBigDecimal() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(output);
        generator.setOutput(writer);

        generator.writeNumber(new BigDecimal("123.45"));
        generator.flush();
        assertEquals("123.45", output.toString());
    }

    
}