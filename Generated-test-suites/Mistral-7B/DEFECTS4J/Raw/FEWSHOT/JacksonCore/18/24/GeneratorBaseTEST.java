package com.fasterxml.jackson.core.base;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.json.DupDetector;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;

public class GeneratorBaseTest {

    private final static int DERIVED_FEATURES_MASK =
            JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask()
            | JsonGenerator.Feature.ESCAPE_NON_ASCII.getMask()
            | JsonGenerator.Feature.STRICT_DUPLICATE_DETECTION.getMask()
            ;

    @Test
    public void testDerivedFeaturesMask() {
        assertEquals(DERIVED_FEATURES_MASK, GeneratorBase.DERIVED_FEATURES_MASK);
    }

    @Test
    public void testVersion() throws IOException {
        GeneratorBase gen = new GeneratorBase(JsonGenerator.Feature.AUTO_CLOSE_TARGET.getMask(), new ObjectMapper.DefaultObjectMapper());
        assertEquals(VersionUtil.versionFor(GeneratorBase.class), gen.version());
    }

    @Test
    public void testIsEnabled() throws IOException {
        GeneratorBase gen = new GeneratorBase(JsonGenerator.Feature.AUTO_CLOSE_TARGET.getMask(), new ObjectMapper.DefaultObjectMapper());
        assertTrue(gen.isEnabled(JsonGenerator.Feature.AUTO_CLOSE_TARGET));
        assertFalse(gen.isEnabled(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS));

        gen.enable(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(gen.isEnabled(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS));

        gen.disable(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(gen.isEnabled(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void testGetFeatureMask() throws IOException {
        GeneratorBase gen = new GeneratorBase(JsonGenerator.Feature.AUTO_CLOSE_TARGET.getMask(), new ObjectMapper.DefaultObjectMapper());
        assertEquals(JsonGenerator.Feature.AUTO_CLOSE_TARGET.getMask(), gen.getFeatureMask());
    }

    @Test
    public void testEnable() throws IOException {
        GeneratorBase gen = new GeneratorBase(JsonGenerator.Feature.AUTO_CLOSE_TARGET.getMask(), new ObjectMapper.DefaultObjectMapper());

        gen.enable(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(gen.isEnabled(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS));

        gen.enable(JsonGenerator.Feature.ESCAPE_NON_ASCII);
        assertTrue(gen.isEnabled(JsonGenerator.Feature.ESCAPE_NON_ASCII));

        gen.enable(JsonGenerator.Feature.STRICT_DUPLICATE_DETECTION);
        assertTrue(gen.isEnabled(JsonGenerator.Feature.STRICT_DUPLICATE_DETECTION));

        gen.enable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        assertFalse(gen.isEnabled(JsonGenerator.Feature.AUTO_CLOSE_TARGET));
    }

    @Test
    public void testDisable() throws IOException {
        GeneratorBase gen = new GeneratorBase(JsonGenerator.Feature.AUTO_CLOSE_TARGET.getMask() | JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), new ObjectMapper.DefaultObjectMapper());

        gen.disable(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(gen.isEnabled(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS));

        gen.disable(JsonGenerator.Feature.ESCAPE_NON_ASCII);
        assertFalse(gen.isEnabled(JsonGenerator.Feature.ESCAPE_NON_ASCII));

        gen.disable(JsonGenerator.Feature.STRICT_DUPLICATE_DETECTION);
        assertFalse(gen.isEnabled(JsonGenerator.Feature.STRICT_DUPLICATE_DETECTION));

        gen.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        assertFalse(gen.isEnabled(JsonGenerator.Feature.AUTO_CLOSE_TARGET));
    }

    @Test
    public void testSetFeatureMask() throws IOException {
        GeneratorBase gen = new GeneratorBase(JsonGenerator.Feature.AUTO_CLOSE_TARGET.getMask(), new ObjectMapper.DefaultObjectMapper());

        int newMask = JsonGenerator.Feature.AUTO_CLOSE_TARGET.getMask() | JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask();
        gen.setFeatureMask(newMask);
        assertTrue(gen.isEnabled(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS));

        int oldMask = gen.getFeatureMask();
        int changed = newMask ^ oldMask;
        gen.setFeatureMask(oldMask);
        assertEquals(oldMask, gen.getFeatureMask());
        assertEquals(changed, JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
    }

    @Test
    public void testOverrideStdFeatures() throws IOException {
        GeneratorBase gen = new GeneratorBase(JsonGenerator.Feature.AUTO_CLOSE_TARGET.getMask(), new ObjectMapper.DefaultObjectMapper());

        int newState = JsonGenerator.Feature.AUTO_CLOSE_TARGET.getMask() | JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | JsonGenerator.Feature.ESCAPE_NON_ASCII.getMask();
        gen.overrideStdFeatures(JsonGenerator.Feature.AUTO_CLOSE_TARGET.getMask() | JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
        assertTrue(gen.isEnabled(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS));

        int oldState = gen.getFeatureMask();
        int changed = newState ^ oldState;
        gen.overrideStdFeatures(oldState, changed);
        assertEquals(oldState, gen.getFeatureMask());
        assertEquals(changed, JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
    }

    @Test
    public void testUseDefaultPrettyPrinter() throws IOException {
        GeneratorBase gen = new GeneratorBase(JsonGenerator.Feature.AUTO_CLOSE_TARGET.getMask(), new ObjectMapper.DefaultObjectMapper());

        assertNull(gen.getPrettyPrinter());
        gen.useDefaultPrettyPrinter();
        assertSame(new DefaultPrettyPrinter(), gen.getPrettyPrinter());
    }

    @Test
    public void testSetCodec() throws IOException {
        GeneratorBase gen = new GeneratorBase(JsonGenerator.Feature.AUTO_CLOSE_TARGET.getMask(), new ObjectMapper.DefaultObjectMapper());

        ObjectCodec oldCodec = gen.getCodec();
        ObjectCodec newCodec = new ObjectMapper.DefaultObjectMapper();
        gen.setCodec(newCodec);
        assertSame(newCodec, gen.getCodec());
        assertNotSame(oldCodec, newCodec);
    }

    @Test
    public void testGetOutputContext() throws IOException {
        GeneratorBase gen = new GeneratorBase(JsonGenerator.Feature.AUTO_CLOSE_TARGET.getMask(), new ObjectMapper.DefaultObjectMapper());

        JsonWriteContext outputContext = gen.getOutputContext();
        assertSame(outputContext, gen.getOutputContext());
    }

    @Test
    public void testWriteObject() throws IOException {
        GeneratorBase gen = new GeneratorBase(JsonGenerator.Feature.AUTO_CLOSE_TARGET.getMask(), new ObjectMapper.DefaultObjectMapper());

        String s = "test";
        gen.writeObject(s);
        assertEquals(s, gen.getCurrentValue().asText());
    }

    @Test
    public void testWriteTree() throws IOException {
        GeneratorBase gen = new GeneratorBase(JsonGenerator.Feature.AUTO_CLOSE_TARGET.getMask(), new ObjectMapper.DefaultObjectMapper());

        TreeNode tree = new ObjectNode(new ObjectMapper().getValueFactory().objectNodeBuilder().set("test", "test"));
        gen.writeTree(tree);
        assertEquals(tree.toString(), gen.getCurrentValue().asText());
    }

    @Test
    public void testWriteNumber() throws IOException {
        GeneratorBase gen = new GeneratorBase(JsonGenerator.Feature.AUTO_CLOSE_TARGET.getMask(), new ObjectMapper.DefaultObjectMapper());

        int number = 1;
        gen.writeNumber(number);
        assertEquals(Integer.toString(number), gen.getCurrentValue().asText());

        double d = 1.2;
        gen.writeNumber(d);
        assertEquals(Double.toString(d), gen.getCurrentValue().asText());

        BigDecimal dec = new BigDecimal("1.23");
        gen.writeNumber(dec);
        assertEquals(_asString(dec), gen.getCurrentValue().asText());

        gen.enable(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS);
        gen.writeNumber(number);
        assertEquals(Integer.toString(number), gen.getCurrentValue().asText());
        gen.writeNumber(d);
        assertEquals(Double.toString(d), gen.getCurrentValue().asText());
        gen.writeNumber(dec);
        assertEquals(_asString(dec), gen.getCurrentValue().asText());

        gen.disable(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS);
        gen.writeNumber(number);
        assertEquals(Integer.toString(number), gen.getCurrentValue().asText());
        gen.writeNumber(d);
        assertEquals(Double.toString(d), gen.getCurrentValue().asText());
        gen.writeNumber(dec);
        assertEquals(_asString(dec), gen.getCurrentValue().asText());
    }

    @Test
    public void testWriteBoolean() throws IOException {
        GeneratorBase gen = new GeneratorBase(JsonGenerator.Feature.AUTO_CLOSE_TARGET.getMask(), new ObjectMapper.DefaultObjectMapper());

        gen.writeBoolean(true);
        assertEquals("true", gen.getCurrentValue().asText());

        gen.writeBoolean(false);
        assertEquals("false", gen.getCurrentValue().asText());
    }

    @Test
    public void testWriteNull() throws IOException {
        GeneratorBase gen = new GeneratorBase(JsonGenerator.Feature.AUTO_CLOSE_TARGET.getMask(), new ObjectMapper.DefaultObjectMapper());

        gen.writeNull();
        assertNull(gen.getCurrentValue());
    }

    @Test
    public void testWriteString() throws IOException {
        GeneratorBase gen = new GeneratorBase(JsonGenerator.Feature.AUTO_CLOSE_TARGET.getMask(), new ObjectMapper.DefaultObjectMapper());

        String s = "test";
        gen.writeString(s);
        assertEquals(s, gen.getCurrentValue().asText());
    }

    @Test
    public void testWriteRawValue() throws IOException {
        GeneratorBase gen = new GeneratorBase(JsonGenerator.Feature.AUTO_CLOSE_TARGET.getMask(), new ObjectMapper.DefaultObjectMapper());

        String s = "test";
        gen.writeRawValue(s);
        assertEquals(s, gen.getCurrentValue().asText());

        char[] chars = s.toCharArray();
        gen.writeRawValue(chars, 0, s.length());
        assertEquals(s, gen.getCurrentValue().asText());

        StringBuilder sb = new StringBuilder();
        sb.append("test");
        gen.writeRawValue(sb.toString());
        assertEquals(s, gen.getCurrentValue().asText());

        gen.enable(JsonGenerator.Feature.ESCAPE_NON_ASCII);
        sb.setCharAt(0, '\u00E9');
        gen.writeRawValue(sb.toString());
        assertEquals("\\u00E9est", gen.getCurrentValue().asText());

        gen.disable(JsonGenerator.Feature.ESCAPE_NON_ASCII);
        gen.writeRawValue(sb.toString());
        assertEquals("test", gen.getCurrentValue().asText());
    }

    @Test
    public void testWriteBinary() throws IOException {
        GeneratorBase gen = new GeneratorBase(JsonGenerator.Feature.AUTO_CLOSE_TARGET.getMask(), new ObjectMapper.DefaultObjectMapper());

        byte[] data = "test".getBytes();
        byte[] result = gen.writeBinary(null, new ByteArrayInputStream(data), data.length);
        assertArrayEquals(data, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testWriteBinaryUnsupported() throws IOException {
        GeneratorBase gen = new GeneratorBase(JsonGenerator.Feature.AUTO_CLOSE_TARGET.getMask(), new ObjectMapper.DefaultObjectMapper());

        gen.writeBinary(null, new ByteArrayInputStream("test".getBytes()), "test".length());
    }

    @Test
    public void testFlush() throws IOException {
        GeneratorBase gen = new GeneratorBase(JsonGenerator.Feature.AUTO_CLOSE_TARGET.getMask(), new ObjectMapper.DefaultObjectMapper());

        gen.writeString("test");
        gen.flush();
        assertEquals("test", gen.getCurrentValue().asText());
        assertNull(gen.getCurrentValue());
    }

    @Test
    public void testClose() throws IOException {
        GeneratorBase gen = new GeneratorBase(JsonGenerator.Feature.AUTO_CLOSE_TARGET.getMask(), new ObjectMapper.DefaultObjectMapper());

        gen.writeString("test");
        gen.close();
        assertNull(gen.getCurrentValue());
    }

    @Test
    public void testIsClosed() throws IOException {
        GeneratorBase gen = new GeneratorBase(JsonGenerator.Feature.AUTO_CLOSE_TARGET.getMask(), new ObjectMapper.DefaultObjectMapper());

        assertFalse(gen.isClosed());
        gen.close();
        assertTrue(gen.isClosed());
    }

    @Test
    public void testReleaseBuffers() throws IOException {
        GeneratorBase gen = new GeneratorBase(JsonGenerator.Feature.AUTO_CLOSE_TARGET.getMask(), new ObjectMapper.DefaultObjectMapper()) {
            @Override
            protected void _releaseBuffers() throws IOException {
                // Do nothing
            }
        };

        gen.writeString("test");
        gen.close();

        try {
            gen.writeString("test2");
        } catch (IOException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testVerifyValueWrite() throws IOException {
        GeneratorBase gen = new GeneratorBase(JsonGenerator.Feature.AUTO_CLOSE_TARGET.getMask(), new ObjectMapper.DefaultObjectMapper()) {
            @Override
            protected void _verifyValueWrite(String typeMsg) throws IOException {
                throw new AssertionError("Should not reach here");
            }
        };

        try {
            gen.writeString("test");
        } catch (IOException e) {
            assertTrue(e.getMessage().startsWith("Should not reach here"));
        }
    }

    @Test
    public void testConstructDefaultPrettyPrinter() throws IOException {
        GeneratorBase gen = new GeneratorBase(JsonGenerator.Feature.AUTO_CLOSE_TARGET.getMask(), new ObjectMapper.DefaultObjectMapper());

        PrettyPrinter pp = gen.getDefaultPrettyPrinter();
        assertSame(new DefaultPrettyPrinter(), pp);
    }

    private String _asString(BigDecimal value) throws IOException {
        return value.toString();
    }

    private void assertArrayEquals(byte[] expected, byte[] actual) {
        if (!Arrays.equals(expected, actual)) {
            fail("Expected " + new String(expected) + " but was " + new String(actual));
        }
    }
}