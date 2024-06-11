package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.BaseTest;
import com.fasterxml.jackson.core.IOContext;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.base.SerializerBase;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.VersionUtil;

/**
 * Test cases for {@link JsonGeneratorImpl} class.
 */
public class JsonGeneratorImplTest extends BaseTest {

    private JsonGeneratorImpl jsonGenerator;
    private IOContext ioContext;
    private ObjectCodec objectCodec;

    @Before
    public void setup() throws Exception {
        ioContext = new IOContext();
        objectCodec = new ObjectMapper().getFactory().getCodec();
        jsonGenerator = new JsonGeneratorImpl(ioContext, 0, objectCodec);
    }

    @Test
    public void testJsonGeneratorImplConstructor() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, objectCodec);
        assertNull(jsonGenerator.getCharacterEscapes());
        assertFalse(jsonGenerator._cfgUnqNames);
        assertSame(sOutputEscapes, jsonGenerator._outputEscapes);
        assertSame(DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, jsonGenerator._rootValueSeparator);
        assertEquals(VersionUtil.versionFor(JsonGeneratorImpl.class), jsonGenerator.version());
    }

    @Test
    public void testSetCharacterEscapes() throws Exception {
        CharacterEscapes characterEscapes = new CharacterEscapes();
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, objectCodec);

        jsonGenerator.setCharacterEscapes(characterEscapes);
        assertSame(characterEscapes, jsonGenerator.getCharacterEscapes());
    }

    @Test
    public void testGetCharacterEscapes() throws Exception {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, objectCodec);
        assertNull(jsonGenerator.getCharacterEscapes());
    }

    @Test
    public void testSetRootValueSeparator() throws Exception {
        SerializableString rootValueSeparator = new SerializableString("test");
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, objectCodec);

        jsonGenerator.setRootValueSeparator(rootValueSeparator);
        assertSame(rootValueSeparator, jsonGenerator._rootValueSeparator);
    }

    @Test
    public void testGetRootValueSeparator() throws Exception {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, objectCodec);
        assertSame(DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, jsonGenerator._rootValueSeparator);
    }

    @Test(expected = IOException.class)
    public void testWriteStringFieldNullName() throws Exception {
        jsonGenerator.writeStringField(null, "value");
    }

    @Test(expected = IOException.class)
    public void testWriteStringFieldNullValue() throws Exception {
        jsonGenerator.writeStringField("fieldName", null);
    }

    @Test
    public void testWriteStringField() throws Exception {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("fieldName", "value");
        jsonGenerator.writeEndObject();
        assertEquals("{\"fieldName\":\"value\"}", jsonGenerator.getOutput().toString());
    }

    @Test
    public void testEnableQuoteFieldNames() throws Exception {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, objectCodec);
        jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
        assertFalse(jsonGenerator._cfgUnqNames);

        jsonGenerator = new JsonGeneratorImpl(ioContext, Feature.QUOTE_FIELD_NAMES.mask(), objectCodec);
        assertTrue(jsonGenerator._cfgUnqNames);
    }

    @Test
    public void testSetHighestNonEscapedChar() throws Exception {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, objectCodec);
        jsonGenerator.setHighestNonEscapedChar(127);
        assertSame(sOutputEscapes, jsonGenerator._outputEscapes);

        jsonGenerator = new JsonGeneratorImpl(ioContext, 0, objectCodec);
        jsonGenerator.setHighestNonEscapedChar(128);
        assertNotSame(sOutputEscapes, jsonGenerator._outputEscapes);
    }

    @Test
    public void testGetHighestNonEscapedChar() throws Exception {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, objectCodec);
        assertEquals(127, jsonGenerator.getHighestEscapedChar());

        jsonGenerator = new JsonGeneratorImpl(ioContext, Feature.ESCAPE_NON_ASCII.mask(), objectCodec);
        assertEquals(0, jsonGenerator.getHighestEscapedChar());
    }

    @Test
    public void testWriteFieldNameNull() throws Exception {
        jsonGenerator.writeFieldName(null);
    }

    @Test(expected = IOException.class)
    public void testWriteStartArrayNull() throws Exception {
        jsonGenerator.writeStartArray(null);
    }

    @Test
    public void testWriteStartObjectNull() throws Exception {
        jsonGenerator.writeStartObject(null);
    }

    @Test
    public void testWriteEndArray() throws Exception {
        jsonGenerator.flush();
        jsonGenerator.writeStartArray();
        jsonGenerator.writeEndArray();
        assertEquals("[]", jsonGenerator.getOutput().toString());
    }

    @Test
    public void testWriteEndObject() throws Exception {
        jsonGenerator.flush();
        jsonGenerator.writeStartObject();
        jsonGenerator.writeEndObject();
        assertEquals("{}", jsonGenerator.getOutput().toString());
    }

    @Test
    public void testWriteNull() throws Exception {
        jsonGenerator.writeNull();
        assertEquals("null", jsonGenerator.getOutput().toString());
    }

    @Test
    public void testWriteBoolean() throws Exception {
        jsonGenerator.writeBoolean(true);
        assertEquals("true", jsonGenerator.getOutput().toString());

        jsonGenerator.writeBoolean(false);
        assertEquals("false", jsonGenerator.getOutput().toString());
    }

    @Test
    public void testWriteNumberInt() throws Exception {
        jsonGenerator.writeNumber(42);
        assertEquals("42", jsonGenerator.getOutput().toString());
    }

    @Test
    public void testWriteNumberLong() throws Exception {
        jsonGenerator.writeNumber(42L);
        assertEquals("42", jsonGenerator.getOutput().toString());

        jsonGenerator.writeNumber(Long.MAX_VALUE);
        assertEquals("9223372036854775807", jsonGenerator.getOutput().toString());
    }

    @Test
    public void testWriteNumberFloat() throws Exception {
        jsonGenerator.writeNumber(3.14F);
        assertEquals("3.14", jsonGenerator.getOutput().toString());
    }

    @Test
    public void testWriteNumberDouble() throws Exception {
        jsonGenerator.writeNumber(3.14);
        assertEquals("3.14", jsonGenerator.getOutput().toString());

        jsonGenerator.writeNumber(Double.POSITIVE_INFINITY);
        assertEquals("null", jsonGenerator.getOutput().toString());

        jsonGenerator.writeNumber(Double.NEGATIVE_INFINITY);
        assertEquals("null", jsonGenerator.getOutput().toString());

        jsonGenerator.writeNumber(Double.NaN);
        assertEquals("null", jsonGenerator.getOutput().toString());
    }

    @Test
    public void testWriteString() throws Exception {
        jsonGenerator.writeString("test");
        assertEquals("\"test\"", jsonGenerator.getOutput().toString());
    }

    @Test
    public void testWriteStringWithEscapes() throws Exception {
        CharacterEscapes characterEscapes = new CharacterEscapes();
        jsonGenerator.setCharacterEscapes(characterEscapes);

        jsonGenerator.writeString("test\\\"test\\/test");
        assertEquals("\"test\\\\test\\\\/test\"", jsonGenerator.getOutput().toString());
    }
}