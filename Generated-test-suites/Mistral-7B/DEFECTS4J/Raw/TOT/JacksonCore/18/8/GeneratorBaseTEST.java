package com.fasterxml.jackson.core.base;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

public class GeneratorBaseTest extends JsonTest {

    public void testWriteNull() throws IOException {
        JsonGenerator gen = createGenerator();
        gen.writeNull();
        String actual = gen.getOutputContent();
        String expected = "null";
        assertEquals(expected, actual);
    }

    public void testWriteBoolean() throws IOException {
        JsonGenerator gen = createGenerator();
        gen.writeBoolean(true);
        String actual = gen.getOutputContent();
        String expected = "true";
        assertEquals(expected, actual);

        gen.writeBoolean(false);
        actual = gen.getOutputContent();
        expected = "false";
        assertEquals(expected, actual);
    }

    public void testWriteNumber() throws IOException {
        JsonGenerator gen = createGenerator();
        gen.enable(Feature.WRITE_NUMBERS_AS_STRINGS);

        gen.writeNumber(123);
        String actual = gen.getOutputContent();
        String expected = "123";
        assertEquals(expected, actual);

        gen.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        gen.writeNumber(123.45);
        actual = gen.getOutputContent();
        expected = "123.45";
        assertEquals(expected, actual);

        gen.writeNumber(123L);
        actual = gen.getOutputContent();
        expected = "123";
        assertEquals(expected, actual);

        gen.writeNumber(123.0);
        actual = gen.getOutputContent();
        expected = "123.0";
        assertEquals(expected, actual);

        gen.writeNumber(new BigDecimal("123.45"));
        actual = gen.getOutputContent();
        expected = "123.45";
        assertEquals(expected, actual);
    }

    public void testWriteString() throws IOException {
        JsonGenerator gen = createGenerator();
        String text = "Hello World!";

        gen.writeString(text);
        String actual = gen.getOutputContent();
        assertEquals(text, actual);

        gen.writeRaw(text);
        actual = gen.getOutputContent();
        assertEquals(text, actual);
    }

    public void testWriteRawValue() throws IOException {
        JsonGenerator gen = createGenerator();
        String text = "Hello World!";

        gen.writeRawValue(text);
        String actual = gen.getOutputContent();
        assertEquals(text, actual);

        gen.writeRawValue(text, 0, text.length());
        actual = gen.getOutputContent();
        assertEquals(text, actual);
    }

    public void testWriteRawValueWithInvalidUTF8() throws IOException {
        JsonGenerator gen = createGenerator();

        String invalidUTF8 = "\uD800\uDC00"; // invalid UTF-8 sequence (surrogate pair)

        try {
            gen.writeRawValue(invalidUTF8);
            fail("Expected JsonGenerationException");
        } catch (JsonGenerationException expected) {
            // expected
        }

        try {
            gen.writeRawValue(new String(new byte[] { (byte) 0xD8, (byte) 0x00, (byte) 0xDC, (byte) 0x00 }).toString());
            fail("Expected JsonGenerationException");
        } catch (JsonGenerationException expected) {
            // expected
        }

        try {
            gen.writeRawValue(new byte[] { (byte) 0xD8, (byte) 0x00, (byte) 0xDC, (byte) 0x00 });
            fail("Expected JsonGenerationException");
        } catch (JsonGenerationException expected) {
            // expected
        }
    }

    public void testWriteObject() throws IOException {
        JsonGenerator gen = createGenerator();
        ObjectMapper mapper = new ObjectMapper();
        Object obj = new Object();

        gen.writeObject(obj);
        String actual = gen.getOutputContent();
        String expected = mapper.writeValueAsString(obj);
        assertEquals(expected, actual);

        gen = createGenerator(Feature.WRITE_NULL_JSON.enabledIn(_features));
        gen.writeObject(null);
        actual = gen.getOutputContent();
        expected = "null";
        assertEquals(expected, actual);
    }

    public void testWriteTree() throws IOException {
        JsonGenerator gen = createGenerator();
        ObjectMapper mapper = new ObjectMapper();
        Object tree = mapper.readValue(getClass().getResourceAsStream("/testTree.json"), TreeNode.class);

        gen.writeTree(tree);
        String actual = gen.getOutputContent();
        String expected = mapper.writeValueAsString(tree);
        assertEquals(expected, actual);
    }

    private JsonGenerator createGenerator() {
        return createGenerator(Feature.WRITE_NUMBERS_AS_STRINGS.enabledIn(_features));
    }

    private JsonGenerator createGenerator(int features) {
        return new GeneratorBase(features, createObjectCodec()) {
            @Override
            protected void _releaseBuffers() {
                // no-op
            }

            @Override
            protected void _verifyValueWrite(String typeMsg) throws IOException {
                // no-op
            }

            @Override
            protected PrettyPrinter _constructDefaultPrettyPrinter() {
                return new DefaultPrettyPrinter();
            }

            @Override
            public void close() throws IOException {
                super.close();
            }
        };
    }

    private ObjectCodec createObjectCodec() {
        return new ObjectMapper().getFactory().getEncoder();
    }
}