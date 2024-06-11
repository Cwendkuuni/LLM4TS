package com.fasterxml.jackson.core.base;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GeneratorBaseTest {

    private ObjectMapper objectMapper;
    private JsonGenerator generator;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        generator = new GeneratorBase(GeneratorBase.Feature.WRITE_DUP_KEYS.getMask() | GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), new ObjectMapper().getFactory()) {
            @Override
            public void writeRawValue(SerializableString text) throws IOException {
                super.writeRawValue(text.getValue());
            }

            @Override
            public void writeRawValue(String text) throws IOException {
                super.writeRawValue(text);
            }

            @Override
            public void writeRawValue(char[] text, int offset, int len) throws IOException {
                super.writeRawValue(text, offset, len);
            }

            @Override
            public void writeStartArray() throws IOException {
                super.writeStartArray();
            }

            @Override
            public void writeEndArray() throws IOException {
                super.writeEndArray();
            }

            @Override
            public void writeStartObject() throws IOException {
                super.writeStartObject();
            }

            @Override
            public void writeEndObject() throws IOException {
                super.writeEndObject();
            }

            @Override
            public void _verifyValueWrite(String typeMsg) throws IOException {
                // Do nothing as we are only testing generator behavior
            }

            @Override
            public void _releaseBuffers() {
                super._releaseBuffers();
            }

            @Override
            public void flush() throws IOException {
                super.writeRaw("").close();
            }

            @Override
            public PrettyPrinter _constructDefaultPrettyPrinter() {
                return new DefaultPrettyPrinter();
            }

            @Override
            public int writeBinary(Base64Variant b64variant, InputStream data, int dataLength) throws IOException {
                return super.writeRawValue(data.readAllBytes());
            }

            @Override
            public void writeObject(Object value) throws IOException {
                super.writeRawValue(serializeObject(value));
            }

            @Override
            public void writeTree(TreeNode rootNode) throws IOException {
                super.writeObject(rootNode);
            }
        };
        generator.setCodec(objectMapper.getFactory());
    }

    @Test
    public void testIsEnabled() {
        GeneratorBase generator = new GeneratorBase(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), new ObjectMapper().getFactory());
        assertTrue(generator.isEnabled(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(generator.isEnabled(GeneratorBase.Feature.WRITE_DUP_KEYS));
        generator.enable(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(generator.isEnabled(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS));
        generator.disable(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(generator.isEnabled(GeneratorFeature.WRITE_NUMBERS_AS_STRINGS));

        generator = new GeneratorBase(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | GeneratorBase.Feature.WRITE_DUP_KEYS.getMask(), new ObjectMapper().getFactory());
        assertTrue(generator.isEnabled(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(generator.isEnabled(GeneratorBase.Feature.WRITE_DUP_KEYS));
    }

    @Test
    public void testGetFeatureMask() {
        GeneratorBase generator = new GeneratorBase(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), new ObjectMapper().getFactory());
        assertEquals(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), generator.getFeatureMask());

        generator = new GeneratorBase(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | GeneratorBase.Feature.WRITE_DUP_KEYS.getMask(), new ObjectMapper().getFactory());
        assertEquals(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | GeneratorBase.Feature.WRITE_DUP_KEYS.getMask(), generator.getFeatureMask());
    }

    @Test(expected = JsonGenerationException.class)
    public void testSetFeatureMask() throws IOException {
        GeneratorBase generator = new GeneratorBase(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), new ObjectMapper().getFactory());
        generator.setFeatureMask(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | GeneratorBase.Feature.WRITE_DUP_KEYS.getMask());
    }

    @Test
    public void testWriteNull() throws IOException {
        generator.writeNull();
        assertEquals("[null]", serializeToString(generator));
    }

    @Test
    public void testWriteBoolean() throws IOException {
        generator.writeBoolean(true);
        assertEquals("[true]", serializeToString(generator));
        generator.writeBoolean(false);
        assertEquals("[false]", serializeToString(generator));
    }

    @Test
    public void testWriteNumber() throws IOException {
        generator.writeNumber(123);
        assertEquals("[123]", serializeToString(generator));
        generator.writeNumber(1.23f);
        assertEquals("[1.23]", serializeToString(generator));
        generator.writeNumber(1.23d);
        assertEquals("[1.23]", serializeToString(generator));
        generator.writeNumber(new BigDecimal("123.45"));
        assertEquals("[123.45]", serializeToString(generator));
    }

    @Test
    public void testWriteString() throws IOException {
        generator.writeString("hello");
        assertEquals("[\"hello\"]", serializeToString(generator));
        generator.writeString("世界");
        assertEquals("[\"世界\"]", serializeToString(generator));
    }

    @Test
    public void testWriteRawValue() throws IOException {
        String rawData = "{\"key\": \"value\"}";
        generator.writeRawValue(rawData);
        assertEquals(rawData, serializeToString(generator));
    }

    @Test
    public void testWriteRawValueCharArray() throws IOException {
        String rawData = "{\"key\": \"value\"}";
        generator.writeRawValue(rawData.toCharArray(), 0, rawData.length());
        assertEquals(rawData, serializeToString(generator));
    }

    @Test
    public void testWriteRawValueSerializableString() throws IOException {
        SerializableString serializableString = new SerializableString("hello");
        generator.writeRawValue(serializableString);
        assertEquals("[\"hello\"]", serializeToString(generator));
    }

    @Test
    public void testWriteArray() throws IOException {
        generator.writeStartArray();
        generator.writeNumber(1);
        generator.writeNumber(2);
        generator.writeString("three");
        generator.writeEndArray();
        assertEquals("[1, 2, \"three\"]", serializeToString(generator));
    }

    @Test
    public void testWriteObject() throws IOException {
        MyClass myClass = new MyClass();
        generator.writeObject(myClass);
        assertEquals("[{\"field1\": 1, \"field2\": \"test\"}]", serializeToString(generator));
    }

    @Test
    public void testWriteTree() throws IOException {
        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("key1", 1);
        rootNode.put("key2", "value2");
        generator.writeTree(rootNode);
        assertEquals("[{ \"key1\": 1, \"key2\": \"value2\" }]", serializeToString(generator));
    }

    @Test
    public void testWriteStartArrayAndWriteEndArray() throws IOException {
        generator.writeStartArray();
        generator.writeString("test");
        generator.writeEndArray();
        assertEquals("[\"test\"]", serializeToString(generator));
    }

    @Test
    public void testWriteStartObjectAndWriteEndObject() throws IOException {
        generator.writeStartObject();
        generator.writeFieldName("field1");
        generator.writeNumber(1);
        generator.writeFieldName("field2");
        generator.writeString("test");
        generator.writeEndObject();
        assertEquals("{\"field1\": 1, \"field2\": \"test\"}", serializeToString(generator));
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteValueAfterClose() throws IOException {
        generator.close();
        generator.writeString("test");
    }

    @Test(expected = JsonGenerationException.class)
    public void testWriteValueAfterFlush() throws IOException {
        generator.flush();
        generator.writeString("test");
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteValueWithDuplicateKey() throws IOException {
        generator.writeStartObject();
        generator.writeFieldName("key");
        generator.writeNumber(1);
        generator.writeFieldName("key"); //duplicate key
        generator.writeString("test");
        generator.writeEndObject();
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteValueWithOutstandingWriter() throws IOException {
        ByteArrayOutputStream firstOutput = new ByteArrayOutputStream();
        ByteArrayOutputStream secondOutput = new ByteArrayOutputStream();
        generator = new GeneratorBase(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), new ObjectMapper().getFactory()) {
            @Override
            public void writeStartArray() throws IOException {
                super.writeStartArray();
                firstOutput.writeTo(this);
            }

            @Override
            public void writeEndArray() throws IOException {
                super.writeEndArray();
                secondOutput.writeTo(this);
            }
        };
        generator.writeStartArray();
        generator.writeNumber(1);
        generator.writeNumber(2);
        generator.writeString("three");
        generator.writeEndArray();
        assertEquals("[1, 2, \"three\"]", serializeToString(generator));
        assertEquals("[]", serializeToString(generator));
    }

    @Test
    public void testWriteValueWithEscapedCharacters() throws IOException {
        generator.writeString("hello\\world");
        assertEquals("[\"hello\\world\"]", serializeToString(generator));
    }

    @Test
    public void testWriteValueWithSurrogateCharacters() throws IOException {
        generator.writeString("heλλo");
        assertEquals("[\"he\\u03B1llo\"]", serializeToString(generator));
    }

    @Test
    public void testWriteValueWithBigDecimal() throws IOException {
        generator.writeNumber(new BigDecimal("1234567890123456789012345678901234567890"));
        assertEquals("[1234567890123456789012345678901234567890]", serializeToString(generator));
    }

    @Test(expected = JsonGenerationException.class)
    public void testWriteValueWithInvalidNumber() throws IOException {
        generator.writeNumber("test");
    }

    @Test(expected = JsonGenerationException.class)
    public void testWriteValueWithInvalidBoolean() throws IOException {
        generator.writeBoolean("test");
    }

    @Test(expected = JsonGenerationException.class)
    public void testWriteValueWithInvalidNull() throws IOException {
        generator.writeNull("test");
    }

    @Test(expected = JsonGenerationException.class)
    public void testWriteValueWithInvalidString() throws IOException {
        generator.writeString("test");
        generator.writeNumber(1);
    }

    @Test(expected = JsonGenerationException.class)
    public void testWriteValueWithInvalidObject() throws IOException {
        generator.writeObject(new Object());
    }

    @Test
    public void testWriteValueWithUnicodeCharacters() throws IOException {
        generator.writeString("heλλo");
        assertEquals("[\"he\\u03B1llo\"]", serializeToString(generator));
    }

    @Test
    public void testWriteValueWithSurrogatePair() throws IOException {
        generator.writeString("\uD835\uDD33\uD835\uDD67");
        assertEquals("[\"𝄞𝄞\"]", serializeToString(generator));
    }

    @Test
    public void testWriteValueWithInvalidRawValue() throws IOException {
        generator.writeRawValue("test");
    }

    @Test
    public void testWriteValueWithEmptyRawValue() throws IOException {
        generator.writeRawValue("");
        assertEquals("[]", serializeToString(generator));
    }

    @Test
    public void testWriteValueWithRawValueString() throws IOException {
        generator.writeRawValue("{\"key\": \"value\"}");
        assertEquals("{\"key\": \"value\"}", serializeToString(generator));
    }

    @Test
    public void testWriteValueWithRawValueCharArray() throws IOException {
        String rawData = "{\"key\": \"value\"}";
        generator.writeRawValue(rawData.toCharArray(), 0, rawData.length());
        assertEquals("{\"key\": \"value\"}", serializeToString(generator));
    }

    @Test
    public void testWriteValueWithRawValueSerializableString() throws IOException {
        SerializableString serializableString = new SerializableString("{\"key\": \"value\"}");
        generator.writeRawValue(serializableString);
        assertEquals("{\"key\": \"value\"}", serializeToString(generator));
    }

    @Test
    public void testWriteValueWithRawValueByteArray() throws IOException {
        byte[] rawData = "{\"key\": \"value\"}".getBytes();
        generator.writeRawValue(rawData);
        assertEquals("{\"key\": \"value\"}", serializeToString(generator));
    }

    @Test
    public void testWriteValueWithBinaryData() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("{\"key\": \"value\"}".getBytes());
        generator.writeBinary(Base64Variant.STANDARD, inputStream, inputStream.available());
        assertEquals("{\"key\": \"value\"}", serializeToString(generator));
    }

    private String serializeToString(JsonGenerator generator) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonSerializer<Object> serializer = objectMapper.findValueSerializer(Object.class, generator);
        serializer.serializeValues(generator, null);
        return new String(outputStream.toByteArray());
    }

    private class MyClass {
        int field1 = 1;
        String field2 = "test";
    }
}