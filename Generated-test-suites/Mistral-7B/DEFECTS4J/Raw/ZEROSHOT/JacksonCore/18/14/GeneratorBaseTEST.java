package com.fasterxml.jackson.core.base;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.WriterProvider;
import com.fasterxml.jackson.core.json.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializerProvider;

public class GeneratorBaseTest {

    @Test
    public void testGeneratorBaseFeatures() throws Exception {
        ObjectCodec codec = createObjectCodec();
        GeneratorBase gen = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), codec);

        assertTrue(gen.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(gen.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
        assertFalse(gen.isEnabled(Feature.ESCAPE_NON_ASCII));

        gen.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(gen.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        gen.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(gen.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        gen.disable(Feature.STRICT_DUPLICATE_DETECTION);
        assertFalse(gen.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        gen.enable(Feature.STRICT_DUPLICATE_DETECTION);
        assertTrue(gen.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        PrettyPrinter pp = gen.useDefaultPrettyPrinter();
        assertNotNull(pp);
        assertTrue(pp instanceof DefaultPrettyPrinter);

        gen.setCodec(null);
        assertNull(gen.getCodec());

        gen.writeNull();
        gen.close();
    }

    @Test
    public void testWriteString() throws Exception {
        ObjectCodec codec = createObjectCodec();
        GeneratorBase gen = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), codec);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        OutputStream out = bos;
        gen.writeString("Hello World");
        gen.flush();
        String expected = "\"Hello World\"";
        String actual = new String(bos.toByteArray());
        assertTrue(actual.equals(expected));

        gen.close();
    }

    @Test
    public void testWriteRawValueString() throws Exception {
        ObjectCodec codec = createObjectCodec();
        GeneratorBase gen = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), codec);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        OutputStream out = bos;
        gen.writeRawValue("Hello World");
        gen.flush();
        String expected = "Hello World";
        String actual = new String(bos.toByteArray());
        assertTrue(actual.equals(expected));

        gen.close();
    }

    @Test
    public void testWriteRawValueStringWithOffsetAndLength() throws Exception {
        ObjectCodec codec = createObjectCodec();
        GeneratorBase gen = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), codec);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        OutputStream out = bos;
        String text = "Hello World";
        gen.writeRawValue(text, 0, text.length());
        gen.flush();
        String expected = "Hello World";
        String actual = new String(bos.toByteArray());
        assertTrue(actual.equals(expected));

        gen.close();
    }

    @Test
    public void testWriteRawValueCharArray() throws Exception {
        ObjectCodec codec = createObjectCodec();
        GeneratorBase gen = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), codec);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        OutputStream out = bos;
        char[] text = new char[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
        gen.writeRawValue(text, 0, text.length);
        gen.flush();
        String expected = new String(text);
        String actual = new String(bos.toByteArray());
        assertTrue(actual.equals(expected));

        gen.close();
    }

    @Test
    public void testWriteRawValueSerializableString() throws Exception {
        ObjectCodec codec = createObjectCodec();
        GeneratorBase gen = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), codec);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        OutputStream out = bos;
        SerializableString ss = new SerializableString("Hello World");
        gen.writeRawValue(ss);
        gen.flush();
        String expected = "\"Hello World\"";
        String actual = new String(bos.toByteArray());
        assertTrue(actual.equals(expected));

        gen.close();
    }

    @Test
    public void testWriteObject() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer();
        Object code = new Object();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        OutputStream out = bos;
        GeneratorBase gen = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), mapper.getFactory());

        gen.writeObject(code);
        gen.flush();
        gen.close();

        String expected = writeObjectToJsonString(code);
        String actual = new String(bos.toByteArray());
        assertTrue(actual.equals(expected));
    }

    @Test
    public void testWriteTree() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer();
        Object tree = createTree();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        OutputStream out = bos;
        GeneratorBase gen = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), mapper.getFactory());

        gen.writeTree(tree);
        gen.flush();
        gen.close();

        String expected = writeTreeToJsonString(tree);
        String actual = new String(bos.toByteArray());
        assertTrue(actual.equals(expected));
    }

    private ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializerProvider(createSerializerProvider());
        return mapper;
    }

    private ObjectCodec createObjectCodec() {
        return createObjectMapper().getFactory().getEncoder().defaultCodec();
    }

    private SerializerProvider createSerializerProvider() {
        WriterProvider writerProvider = new WriterProvider() {
            @Override
            public JsonGenerator getProviderInstance(OutputStream out, Object toValue) throws IOException {
                return new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), null);
            }

            @Override
            public JsonGenerator getProviderInstance(Writer writer, Object toValue) throws IOException {
                return new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), null);
            }

            @Override
            public JsonGenerator getProviderInstance(String name, JsonSerializer<?> serializer, Object toValue) throws IOException {
                return new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), null);
            }
        };
        return new BaseJsonSerializerProvider(writerProvider);
    }

    private Object createTree() {
        return new Object() {
            int i;
            String s;
            Object o;
        };
    }

    private String writeObjectToJsonString(Object obj) throws JsonProcessingException {
        ObjectWriter writer = createObjectMapper().writer();
        return writer.writeValueAsString(obj);
    }

    private String writeTreeToJsonString(Object tree) throws JsonProcessingException {
        ObjectWriter writer = createObjectMapper().writer();
        return writer.writeValueAsString(tree);
    }
}