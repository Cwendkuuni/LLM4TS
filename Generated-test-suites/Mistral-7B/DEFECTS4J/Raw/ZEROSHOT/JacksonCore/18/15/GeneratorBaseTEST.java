package com.fasterxml.jackson.core.base;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonWriter;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class GeneratorBaseTest {

    @Test
    public void testConstants() {
        assertEquals("0xD800", Integer.toHexString(GeneratorBase.SURR1_FIRST));
        assertEquals("0xDBFF", Integer.toHexString(GeneratorBase.SURR1_LAST));
        assertEquals("0xDC00", Integer.toHexString(GeneratorBase.SURR2_FIRST));
        assertEquals("0xDFFF", Integer.toHexString(GeneratorBase.SURR2_LAST));
    }

    @Test
    public void testConstantsForValidationMessages() {
        assertEquals("write a binary value", GeneratorBase.WRITE_BINARY);
        assertEquals("write a boolean value", GeneratorBase.WRITE_BOOLEAN);
        assertEquals("write a null", GeneratorBase.WRITE_NULL);
        assertEquals("write a number", GeneratorBase.WRITE_NUMBER);
        assertEquals("write a raw (unencoded) value", GeneratorBase.WRITE_RAW);
        assertEquals("write a string", GeneratorBase.WRITE_STRING);
    }

    @Test
    public void testConfiguration() throws IOException {
        ObjectCodec objectCodec = new JsonFactory().getCodec();
        GeneratorBase base = new GeneratorBase(JsonGenerator.Feature.AUTO_CLOSE_Source.getMask() | JsonGenerator.Feature.QUOTE_FIELD_NAMES.getMask(), objectCodec);
        JsonWriter writer = new JsonWriter(new ByteArrayOutputStream());
        writer.setCodec(objectCodec);
        writer.setFeatureMask(JsonGenerator.Feature.AUTO_CLOSE_Source.getMask() | JsonGenerator.Feature.QUOTE_FIELD_NAMES.getMask());

        // Test enabling features
        writer.enable(JsonGenerator.Feature.AUTO_CLOSE_Source);
        assertTrue(writer.isEnabled(JsonGenerator.Feature.AUTO_CLOSE_Source));
        writer.enable(JsonGenerator.Feature.QUOTE_FIELD_NAMES);
        assertTrue(writer.isEnabled(JsonGenerator.Feature.QUOTE_FIELD_NAMES));

        // Test disabling features
        writer.disable(JsonGenerator.Feature.AUTO_CLOSE_Source);
        assertFalse(writer.isEnabled(JsonGenerator.Feature.AUTO_CLOSE_Source));
        writer.disable(JsonGenerator.Feature.QUOTE_FIELD_NAMES);
        assertFalse(writer.isEnabled(JsonGenerator.Feature.QUOTE_FIELD_NAMES));

        // Test setting feature mask
        writer.setFeatureMask(JsonGenerator.Feature.AUTO_CLOSE_Source.getMask());
        assertTrue(writer.isEnabled(JsonGenerator.Feature.AUTO_CLOSE_Source));
        writer.setFeatureMask(JsonGenerator.Feature.QUOTE_FIELD_NAMES.getMask());
        assertTrue(writer.isEnabled(JsonGenerator.Feature.QUOTE_FIELD_NAMES));

        // Test configuring with different context
        JsonWriteContext context = JsonWriteContext.createRootContext(DupDetector.rootDetector(writer));
        GeneratorBase base2 = new GeneratorBase(JsonGenerator.Feature.AUTO_CLOSE_Source.getMask() | JsonGenerator.Feature.QUOTE_FIELD_NAMES.getMask(), objectCodec, context);
        JsonWriter writer2 = new JsonWriter(new ByteArrayOutputStream());
        writer2.setCodec(objectCodec);
        writer2.setFeatureMask(JsonGenerator.Feature.AUTO_CLOSE_Source.getMask() | JsonGenerator.Feature.QUOTE_FIELD_NAMES.getMask());
        writer2.setOutputContext(context);

        // Test version
        assertEquals(new Version(1, 0, 0, null, 0, null), writer.version());
        assertEquals(new Version(1, 0, 0, null, 0, null), writer2.version());
    }

    @Test
    public void testWritingValues() throws IOException, JsonProcessingException {
        ObjectCodec objectCodec = new JsonFactory().getCodec();
        GeneratorBase base = new GeneratorBase(JsonGenerator.Feature.AUTO_CLOSE_Source.getMask() | JsonGenerator.Feature.QUOTE_FIELD_NAMES.getMask(), objectCodec);
        JsonWriter writer = new JsonWriter(new ByteArrayOutputStream());
        writer.setCodec(objectCodec);
        writer.setFeatureMask(JsonGenerator.Feature.AUTO_CLOSE_Source.getMask() | JsonGenerator.Feature.QUOTE_FIELD_NAMES.getMask());

        // Test null
        writer.writeNull();
        String json = writer.getEncoding().encode(writer.getOutputBuffer());
        assertEquals("null", json);

        // Test boolean
        writer.writeBoolean(false);
        json = writer.getEncoding().encode(writer.getOutputBuffer());
        assertEquals("false", json);
        writer.writeBoolean(true);
        json = writer.getEncoding().encode(writer.getOutputBuffer());
        assertEquals("true", json);

        // Test string
        writer.writeString("test");
        json = writer.getEncoding().encode(writer.getOutputBuffer());
        assertEquals("\"test\"", json);

        // Test number
        writer.writeNumber(42);
        json = writer.getEncoding().encode(writer.getOutputBuffer());
        assertEquals("42", json);
        writer.writeNumber(3.14);
        json = writer.getEncoding().encode(writer.getOutputBuffer());
        assertEquals("3.14", json);

        // Test BigDecimal
        BigDecimal dec = new BigDecimal("3.14159265358979323846264338327950288416653542");
        writer.writeNumber(dec);
        json = writer.getEncoding().encode(writer.getOutputBuffer());
        assertEquals("3.14159265358979323846264338327950288416653542", json);

        // Test object
        ObjectNode root = JsonNodeFactory.instance.objectNode();
        root.put("name", "test");
        writer.writeObject(root);
        json = writer.getEncoding().encode(writer.getOutputBuffer());
        assertEquals("{\"name\":\"test\"}", json);

        // Test array
        JsonNode array = JsonNodeFactory.instance.arrayNode();
        array.add(42);
        array.add(true);
        writer.writeValue(array);
        json = writer.getEncoding().encode(writer.getOutputBuffer());
        assertEquals("[42,true]", json);

        // Test binary
        writer.writeBinary(Base64Variant.STANDARD, new ByteArrayInputStream(new byte[]{}), 0);
        json = writer.getEncoding().encode(writer.getOutputBuffer());
        assertEquals("", json);

        // Test raw
        writer.writeRawValue("test");
        json = writer.getEncoding().encode(writer.getOutputBuffer());
        assertEquals("test", json);

        // Test raw with length
        writer.writeRawValue("test", 0, 3);
        json = writer.getEncoding().encode(writer.getOutputBuffer());
        assertEquals("tes", json);

        // Test raw with char array
        writer.writeRawValue(new char[]{'t', 'e', 's'});
        json = writer.getEncoding().encode(writer.getOutputBuffer());
        assertEquals("tes", json);

        // Test raw with string
        writer.writeRawValue("test");
        json = writer.getEncoding().encode(writer.getOutputBuffer());
        assertEquals("test", json);

        // Test raw with string and length
        writer.writeRawValue("test", 0, 4);
        json = writer.getEncoding().encode(writer.getOutputBuffer());
        assertEquals("test", json);

        // Test raw with string and offset
        writer.writeRawValue("test", 2, 3);
        json = writer.getEncoding().encode(writer.getOutputBuffer());
        assertEquals("st", json);

        // Test raw with char array and offset
        writer.writeRawValue(new char[]{'s', 't'}, 1, 2);
        json = writer.getEncoding().encode(writer.getOutputBuffer());
        assertEquals("st", json);

        // Test raw with char array and length
        writer.writeRawValue(new char[]{'s', 't'}, 0, 2);
        json = writer.getEncoding().encode(writer.getOutputBuffer());
        assertEquals("st", json);

        // Test raw with SerializableString
        SerializableString str = new SerializedString("test");
        writer.writeRawValue(str);
        json = writer.getEncoding().encode(writer.getOutputBuffer());
        assertEquals("test", json);

        // Test raw with SerializableString and length
        SerializableString str2 = new SerializedString("test", 4);
        writer.writeRawValue(str2);
        json = writer.getEncoding().encode(writer.getOutputBuffer());
        assertEquals("test", json);

        // Test raw with SerializableString and offset
        SerializableString str3 = new SerializedString("test", 2, 3);
        writer.writeRawValue(str3);
        json = writer.getEncoding().encode(writer.getOutputBuffer());
        assertEquals("st", json);

        // Test raw with SerializableString and length and offset
        SerializableString str4 = new SerializedString("test", 0, 3);
        writer.writeRawValue(str4);
        json = writer.getEncoding().encode(writer.getOutputBuffer());
        assertEquals("tes", json);

        // Test surrogates
        writer.writeString("\uD842\uDC00\uD842\uDC01");
        json = writer.getEncoding().encode(writer.getOutputBuffer());
        String hex = Integer.toHexString(0x10000 + 0xD842 * 0x10000 + 0xDC00 * 0x400 + 0xDC01);
        assertEquals(hex, json);

        // Test unsupported binary
        try {
            writer.writeBinary(Base64Variant.STANDARD, new ByteArrayInputStream(new byte[]{}), 0);
            fail();
        } catch (JsonGenerationException e) {
            // expected
        }

        // Test unsupported raw
        try {
            writer.writeRawValue("test", 0, 4);
            fail();
        } catch (JsonGenerationException e) {
            // expected
        }
    }

    @Test
    public void testClosing() throws IOException {
        ObjectCodec objectCodec = new JsonFactory().getCodec();
        GeneratorBase base = new GeneratorBase(JsonGenerator.Feature.AUTO_CLOSE_Source.getMask() | JsonGenerator.Feature.QUOTE_FIELD_NAMES.getMask(), objectCodec);
        JsonWriter writer = new JsonWriter(new ByteArrayOutputStream());
        writer.setCodec(objectCodec);
        writer.setFeatureMask(JsonGenerator.Feature.AUTO_CLOSE_Source.getMask() | JsonGenerator.Feature.QUOTE_FIELD_NAMES.getMask());
        writer.writeNull();
        writer.close();
        assertEquals("null", writer.getEncoding().encode(writer.getOutputBuffer()));
        assertTrue(writer.isClosed());

        try {
            writer.writeNull();
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testWritingValueAfterClosing() throws IOException {
        ObjectCodec objectCodec = new JsonFactory().getCodec();
        GeneratorBase base = new GeneratorBase(JsonGenerator.Feature.AUTO_CLOSE_Source.getMask() | JsonGenerator.Feature.QUOTE_FIELD_NAMES.getMask(), objectCodec);
        JsonWriter writer = new JsonWriter(new ByteArrayOutputStream());
        writer.setCodec(objectCodec);
        writer.setFeatureMask(JsonGenerator.Feature.AUTO_CLOSE_Source.getMask() | JsonGenerator.Feature.QUOTE_FIELD_NAMES.getMask());
        writer.writeNull();
        writer.close();
        try {
            writer.writeNull();
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testReleaseBuffers() throws IOException {
        ObjectCodec objectCodec = new JsonFactory().getCodec();
        GeneratorBase base = new GeneratorBase(JsonGenerator.Feature.AUTO_CLOSE_Source.getMask() | JsonGenerator.Feature.QUOTE_FIELD_NAMES.getMask(), objectCodec);
        JsonWriter writer = new JsonWriter(new ByteArrayOutputStream());
        writer.setCodec(objectCodec);
        writer.setFeatureMask(JsonGenerator.Feature.AUTO_CLOSE_Source.getMask() | JsonGenerator.Feature.QUOTE_FIELD_NAMES.getMask());
        writer.writeNull();
        writer._releaseBuffers();
        JsonParser parser = new JsonFactory().getParser(writer.getEncoding().getReader(writer.getOutputBuffer()));
        JsonNode root = parser.readValueAsTree();
        assertEquals(JsonNodeType.NULL, root.getNodeType());
    }
}