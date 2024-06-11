package com.fasterxml.jackson.core.base;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertThrows;
import static org.testng.Assert.assertTrue;

public class GeneratorBaseTest {

    private JsonGenerator generator;
    private ObjectMapper mapper;

    @BeforeClass
    public void setup() throws IOException {
        JsonFactory factory = new JsonFactory();
        generator = factory.createGenerator(new ByteArrayOutputStream(1024), null);
        generator.enable(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS);
        mapper = factory.createObjectMapper();
    }

    @Test
    public void testWriteNull() throws JsonProcessingException {
        generator.writeNull();
        JsonNode rootNode = mapper.readTree(generator.asStream());
        assertEquals(rootNode.toString(), "null");
    }

    @Test
    public void testWriteBoolean() throws JsonProcessingException {
        generator.writeBoolean(true);
        JsonNode rootNode = mapper.readTree(generator.asStream());
        assertEquals(rootNode.asBoolean(), true);

        generator.writeBoolean(false);
        rootNode = mapper.readTree(generator.asStream());
        assertEquals(rootNode.asBoolean(), false);
    }

    @Test
    public void testWriteNumber() throws JsonProcessingException {
        generator.writeNumber(123);
        JsonNode rootNode = mapper.readTree(generator.asStream());
        assertEquals(rootNode.asInt(), 123);

        generator.writeNumber(Long.MAX_VALUE);
        rootNode = mapper.readTree(generator.asStream());
        assertEquals(rootNode.asLong(), Long.MAX_VALUE);

        generator.writeNumber(0.123);
        rootNode = mapper.readTree(generator.asStream());
        assertEquals(rootNode.asDouble(), 0.123);

        generator.writeNumber(new BigDecimal("123.456"));
        rootNode = mapper.readTree(generator.asStream());
        assertEquals(rootNode.get("@type"), "BigDecimal");
        assertEquals(rootNode.get("numberValue").asText(), "123.456");
    }

    @Test
    public void testWriteString() throws JsonProcessingException {
        generator.writeString("abc");
        JsonNode rootNode = mapper.readTree(generator.asStream());
        assertEquals(rootNode.asText(), "abc");
    }

    @Test
    public void testWriteRawValue() throws JsonProcessingException {
        generator.writeRawValue("abc");
        JsonNode rootNode = mapper.readTree(generator.asStream());
        assertEquals(rootNode.asText(), "abc");
    }

    @Test
    public void testWriteRawValueWithLength() throws JsonProcessingException {
        generator.writeRawValue("abc", 3);
        JsonNode rootNode = mapper.readTree(generator.asStream());
        assertEquals(rootNode.asText(), "abc");
    }

    @Test
    public void testWriteRawValueWithOffsetAndLength() throws JsonProcessingException {
        String value = "abc123";
        generator.writeRawValue(value, 1, value.length() - 3);
        JsonNode rootNode = mapper.readTree(generator.asStream());
        assertEquals(rootNode.asText(), "123");
    }

    @Test
    public void testWriteRawValueWithInvalidParameters() throws JsonProcessingException {
        assertThrows(IllegalArgumentException.class, () -> {
            generator.writeRawValue("abc", -1, 3);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            generator.writeRawValue("abc", 1, -3);
        });
    }

    @Test
    public void testWriteRawValueWithInvalidChars() throws JsonProcessingException {
        assertThrows(IllegalArgumentException.class, () -> {
            generator.writeRawValue("abc\u0000def");
        });
    }

    @Test
    public void testWriteBinary() throws JsonProcessingException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        generator = new GeneratorBase(JsonGenerator.Feature.AUTO_CLOSE_SOURCE.getMask(), null, generator.getOutputContext()) {
            @Override
            public int writeBinary(Base64Variant b64variant, InputStream data, int dataLength) throws IOException {
                byte[] bytes = new byte[dataLength];
                int length = data.read(bytes, 0, bytes.length);
                outputStream.write(bytes, 0, length);
                return length;
            }
        };
        generator.writeStartArray();
        generator.writeBinary(Base64Variant.BASE64, new ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=".getBytes()), "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=".length());
        generator.writeEndArray();

        JsonNode rootNode = mapper.readTree(new ByteArrayInputStream(outputStream.toByteArray()));
        assertEquals(rootNode.asText(), "\"VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=\"".replaceAll("\\\\", ""));
    }

    @Test
    public void testWriteObject() throws JsonProcessingException {
        generator.writeStartObject();
        generator.writeNumberField("field1", 123);
        generator.writeStringField("field2", "abc");
        generator.writeNullField("field3");
        generator.writeEndObject();

        JsonNode rootNode = mapper.readTree(generator.asStream());
        assertEquals(rootNode.get("field1").asInt(), 123);
        assertEquals(rootNode.get("field2").asText(), "abc");
        assertNull(rootNode.get("field3"));
    }

    @Test
    public void testWriteObjectWithInvalidValue() throws JsonProcessingException {
        generator.writeStartObject();
        generator.writeNumberField("field1", 123);
        generator.writeStringField("field2", null);
        generator.writeEndObject();

        assertThrows(IllegalArgumentException.class, () -> mapper.readTree(generator.asStream()));
    }

    @Test
    public void testWriteEmptyArray() throws JsonProcessingException {
        generator.writeStartArray();
        generator.writeEndArray();

        JsonNode rootNode = mapper.readTree(generator.asStream());
        assertTrue(rootNode.isNull());
    }

    @Test
    public void testWriteArrayWithOneElement() throws JsonProcessingException {
        generator.writeStartArray();
        generator.writeString("abc");
        generator.writeEndArray();

        JsonNode rootNode = mapper.readTree(generator.asStream());
        assertEquals(rootNode.size(), 1);
        assertEquals(rootNode.get(0).asText(), "abc");
    }

    @Test
    public void testWriteArrayWithMultipleElements() throws JsonProcessingException {
        generator.writeStartArray();
        generator.writeString("abc");
        generator.writeString("def");
        generator.writeEndArray();

        JsonNode rootNode = mapper.readTree(generator.asStream());
        assertEquals(rootNode.size(), 2);
        assertEquals(rootNode.get(0).asText(), "abc");
        assertEquals(rootNode.get(1).asText(), "def");
    }

    @Test
    public void testCloseGenerator() throws JsonProcessingException {
        generator.writeStartArray();
        generator.writeString("abc");
        generator.writeEndArray();
        generator.close();

        assertThrows(IOException.class, () -> mapper.readTree(generator.asStream()));
    }

    @Test
    public void testWriteValueAfterClose() throws JsonProcessingException {
        generator.writeStartArray();
        generator.writeString("abc");
        generator.close();
        generator.writeString("def");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        generator = new GeneratorBase(JsonGenerator.Feature.AUTO_CLOSE_SOURCE.getMask(), null, generator.getOutputContext()) {
            @Override
            public void flush() throws IOException {
                outputStream.writeTo(this.outStream);
            }
        };
        generator.writeEndArray();
        generator.close();

        assertThrows(IOException.class, () -> mapper.readTree(generator.asStream()));
        assertEquals(outputStream.toString(), "]");
    }

    @Test
    public void testWriteValueWithFeatureDisabled() throws JsonProcessingException {
        generator = new GeneratorBase(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask() ^ JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null, generator.getOutputContext());
        generator.writeNumber(123);
        JsonNode rootNode = mapper.readTree(generator.asStream());
        assertEquals(rootNode.asInt(), 123);
    }

    @Test
    public void testWriteValueWithFeatureEnabled() throws JsonProcessingException {
        generator = new GeneratorBase(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null, generator.getOutputContext());
        generator.writeNumber(123);
        JsonNode rootNode = mapper.readTree(generator.asStream());
        assertEquals(rootNode.asText(), "123");
    }

    @Test
    public void testWriteValueWithFeaturesDisabledAndEnabled() throws JsonProcessingException {
        generator = new GeneratorBase(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask() ^ JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null, generator.getOutputContext()) {
            {
                enable(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS);
            }
        };
        generator.writeNumber(123);
        JsonNode rootNode = mapper.readTree(generator.asStream());
        assertEquals(rootNode.asText(), "123");
    }

    @Test
    public void testWriteValueWithFeaturesEnabledAndDisabled() throws JsonProcessingException {
        generator = new GeneratorBase(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null, generator.getOutputContext()) {
            {
                disable(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS);
            }
        };
        generator.writeNumber(123);
        JsonNode rootNode = mapper.readTree(generator.asStream());
        assertEquals(rootNode.asInt(), 123);
    }

    @Test
    public void testWriteValueWithFeaturesSetAndCleared() throws JsonProcessingException {
        generator = new GeneratorBase(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null, generator.getOutputContext()) {
            {
                setFeatureMask(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask() ^ JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
            }
        };
        generator.writeNumber(123);
        JsonNode rootNode = mapper.readTree(generator.asStream());
        assertEquals(rootNode.asInt(), 123);
    }

    @Test
    public void testWriteValueWithFeatureSetAndOverridden() throws JsonProcessingException {
        generator = new GeneratorBase(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null, generator.getOutputContext()) {
            @Override
            public void writeNumber(int i) throws IOException {
                writeString(String.valueOf(i));
            }
        };
        generator.writeNumber(123);
        JsonNode rootNode = mapper.readTree(generator.asStream());
        assertEquals(rootNode.asText(), "123");
    }

    @Test
    public void testWriteValueWithFeatureClearedAndOverridden() throws JsonProcessingException {
        generator = new GeneratorBase(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask() ^ JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null, generator.getOutputContext()) {
            @Override
            public void writeNumber(int i) throws IOException {
                writeNumber(i);
            }
        };
        generator.writeNumber(123);
        JsonNode rootNode = mapper.readTree(generator.asStream());
        assertEquals(rootNode.asInt(), 123);
    }

    @Test
    public void testWriteValueWithFeatureSetAndOverriddenAndSetAgain() throws JsonProcessingException {
        generator = new GeneratorBase(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null, generator.getOutputContext()) {
            @Override
            public void writeNumber(int i) throws IOException {
                writeString(String.valueOf(i));
            }

            {
                setFeatureMask(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
            }
        };
        generator.writeNumber(123);
        JsonNode rootNode = mapper.readTree(generator.asStream());
        assertEquals(rootNode.asText(), "123");
    }

    @Test
    public void testWriteValueWithFeatureClearedAndOverriddenAndSetAgain() throws JsonProcessingException {
        generator = new GeneratorBase(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask() ^ JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null, generator.getOutputContext()) {
            @Override
            public void writeNumber(int i) throws IOException {
                writeNumber(i);
            }

            {
                setFeatureMask(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
            }
        };
        generator.writeNumber(123);
        JsonNode rootNode = mapper.readTree(generator.asStream());
        assertEquals(rootNode.asInt(), 123);
    }
}