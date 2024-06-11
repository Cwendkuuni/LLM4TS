package com.fasterxml.jackson.core.base;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.json.*;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

public class GeneratorBaseTest {

    private static class TestGenerator extends GeneratorBase {
        public TestGenerator(int features, ObjectCodec codec) { super(features, codec); }
    }

    @Test
    public void testIsEnabled() throws IOException {
        int features = Feature.AUTO_CLOSE_SOURCE.getMask();
        ObjectCodec codec = null;
        TestGenerator gen = new TestGenerator(features, codec);

        assertTrue(gen.isEnabled(Feature.AUTO_CLOSE_SOURCE));
        assertFalse(gen.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        int newFeatures = features | Feature.WRITE_NUMBERS_AS_STRINGS.getMask();
        gen.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(gen.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(gen.isEnabled(Feature.AUTO_CLOSE_SOURCE));

        gen.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(gen.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(gen.isEnabled(Feature.AUTO_CLOSE_SOURCE));

        gen.setFeatureMask(newFeatures);
        assertTrue(gen.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(gen.isEnabled(Feature.AUTO_CLOSE_SOURCE));
    }

    @Test
    public void testUseDefaultPrettyPrinter() throws IOException {
        ObjectCodec codec = new ObjectMapper().getFactory().createObjectCodec();
        TestGenerator gen = new TestGenerator(0, codec);
        DefaultPrettyPrinter defaultPrinter = gen.useDefaultPrettyPrinter();
        assertEquals(defaultPrinter.getClass(), DefaultPrettyPrinter.class);
    }

    @Test
    public void testSetCodec() throws IOException {
        ObjectCodec codec = new ObjectMapper().getFactory().createObjectCodec();
        TestGenerator gen1 = new TestGenerator(0, codec);
        ObjectCodec codec2 = new ObjectMapper().getFactory().createObjectCodec();
        gen1.setCodec(codec2);
        assertEquals(codec2, gen1.getCodec());
    }

    @Test
    public void testWriteFieldName() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        gen.writeStartObject();
        gen.writeFieldName("foo");
        gen.writeEndObject();
        gen.flush();

        ByteArrayOutputStream bos = (ByteArrayOutputStream) gen.asOutput();
        assertEquals("{\"foo\":null}", bos.toString());
    }

    @Test
    public void testWriteString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        gen.writeString("foo");
        gen.flush();

        ByteArrayOutputStream bos = (ByteArrayOutputStream) gen.asOutput();
        assertEquals("\"foo\"", bos.toString());
    }

    @Test
    public void testWriteRawValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        gen.writeRawValue("foo");
        gen.flush();

        ByteArrayOutputStream bos = (ByteArrayOutputStream) gen.asOutput();
        assertEquals("\"foo\"", bos.toString());
    }

    @Test
    public void testWriteRawValueWithOffset() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        String s = "foo";
        gen.writeRawValue(s, 0, s.length());
        gen.flush();

        ByteArrayOutputStream bos = (ByteArrayOutputStream) gen.asOutput();
        assertEquals("\"foo\"", bos.toString());
    }

    @Test
    public void testWriteRawValueWithNull() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        gen.writeRawValue(null, 0, 0);
        gen.flush();

        ByteArrayOutputStream bos = (ByteArrayOutputStream) gen.asOutput();
        assertEquals("null", bos.toString());
    }

    @Test
    public void testWriteNull() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        gen.writeNull();
        gen.flush();

        ByteArrayOutputStream bos = (ByteArrayOutputStream) gen.asOutput();
        assertEquals("null", bos.toString());
    }

    @Test
    public void testWriteBoolean() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        gen.writeBoolean(true);
        gen.flush();

        ByteArrayOutputStream bos = (ByteArrayOutputStream) gen.asOutput();
        assertEquals("true", bos.toString());
    }

    @Test
    public void testWriteNumber() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        gen.writeNumber(123);
        gen.flush();

        ByteArrayOutputStream bos = (ByteArrayOutputStream) gen.asOutput();
        assertEquals("123", bos.toString());
    }

    @Test
    public void testWriteNumberWithQuotes() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        gen.writeNumber("123");
        gen.flush();

        ByteArrayOutputStream bos = (ByteArrayOutputStream) gen.asOutput();
        assertEquals("\"123\"", bos.toString());
    }

    @Test
    public void testWriteNumberWithScale() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        BigDecimal dec = new BigDecimal("123.456");
        gen.writeNumber(dec);
        gen.flush();

        ByteArrayOutputStream bos = (ByteArrayOutputStream) gen.asOutput();
        assertEquals("123.456", bos.toString());
    }

    @Test
    public void testWriteObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<String> serializer = mapper.findValueSerializer(String.class);
        JsonSerializer<Integer> serializerInt = mapper.findValueSerializer(Integer.class);
        TypeSerializer<String> typeSerializer = mapper.getTypeSerializer();
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        String s = "foo";
        Integer i = 123;

        gen.writeStartObject();
        gen.writeFieldName("foo");
        typeSerializer.serializeValue(gen, null, s);
        gen.writeFieldName("bar");
        gen.writeNumber(i.doubleValue());
        gen.writeEndObject();
        gen.flush();

        ByteArrayOutputStream bos = (ByteArrayOutputStream) gen.asOutput();
        assertEquals("{\"foo\":\"foo\",\"bar\":123}", bos.toString());
    }

    @Test
    public void testWriteTree() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<String> serializer = mapper.findValueSerializer(String.class);
        JsonSerializer<Integer> serializerInt = mapper.findValueSerializer(Integer.class);
        TypeSerializer<String> typeSerializer = mapper.getTypeSerializer();
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        String s = "foo";
        Integer i = 123;

        gen.writeTree(new TreeNode() {
            @Override
            public String getValueAsString() {
                return s;
            }

            @Override
            public JsonSerializer<?> getSerializer() {
                return serializer;
            }

            @Override
            public void serializeWithType(JsonGenerator g, SerializerProvider provider, TypeSerializer typeSerializer) throws IOException {
                typeSerializer.serializeValue(g, null, s);
            }

            @Override
            public TreeNode getParent() {
                return null;
            }

            @Override
            public TreeNode[] getChildren() {
                return new TreeNode[0];
            }

            @Override
            public int getSerializationOrder() {
                return -1;
            }

            @Override
            public void serializeChildren(JsonGenerator g, SerializerProvider provider) throws IOException {
                // noop
            }
        });

        gen.writeFieldName("bar");
        gen.writeNumber(i.doubleValue());
        gen.writeEndObject();
        gen.flush();

        ByteArrayOutputStream bos = (ByteArrayOutputStream) gen.asOutput();
        assertEquals("{\"foo\":\"foo\",\"bar\":123}", bos.toString());
    }

    @Test
    public void testWriteBinary() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);

        _verifyWriteBinaryException(gen, Base64Variant.STANDARD);
        _verifyWriteBinaryException(gen, Base64Variant.UTF8);
        _verifyWriteBinaryException(gen, Base64Variant.BASE64_URL_SAFE);
    }

    private void _verifyWriteBinaryException(JsonGenerator gen, Base64Variant b64variant) throws IOException {
        InputStream data = new ByteArrayInputStream("test".getBytes());
        int dataLength = "test".length();

        try {
            gen.writeBinary(b64variant, data, dataLength);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testFlush() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);

        gen.writeStartObject();
        gen.writeString("foo");
        gen.writeEndObject();
        gen.flush();

        ByteArrayOutputStream bos = (ByteArrayOutputStream) gen.asOutput();
        assertEquals("{\"foo\":null}", bos.toString());
    }

    @Test
    public void testClose() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);

        gen.writeStartObject();
        gen.writeString("foo");
        gen.writeEndObject();
        gen.close();

        ByteArrayOutputStream bos = (ByteArrayOutputStream) gen.asOutput();
        assertEquals("{\"foo\":null}", bos.toString());
        assertFalse(gen.isOpen());
    }

    @Test
    public void testIsClosed() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);

        assertFalse(gen.isClosed());

        gen.writeStartObject();
        gen.writeString("foo");
        gen.writeEndObject();
        gen.close();

        assertFalse(gen.isOpen());
        assertTrue(gen.isClosed());
    }

    @Test
    public void test_verifyValueWrite() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);

        try {
            gen._verifyValueWrite("write null value");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            gen._verifyValueWrite("write raw string");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            gen._verifyValueWrite("write raw bytes");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            gen._verifyValueWrite("write number");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            gen._verifyValueWrite("write boolean");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            gen._verifyValueWrite("write POJO");
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }

        try {
            gen._verifyValueWrite("write tree");
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void test_releaseBuffers() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);

        try {
            gen._releaseBuffers();
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }
}