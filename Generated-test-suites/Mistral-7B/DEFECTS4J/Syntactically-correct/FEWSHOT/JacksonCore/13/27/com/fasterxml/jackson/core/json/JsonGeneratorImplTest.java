package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.BaseTest;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultSerializableString;
import com.fasterxml.jackson.core.util.VersionUtil;

public class JsonGeneratorImplTest extends BaseTest {

    private JsonGeneratorImpl jsonGenerator;

    @Before
    public void setUp() {
        IOContext context = new IOContext();
        JsonGenerator.Feature feature = JsonGenerator.Feature.QUOTE_FIELD_NAMES;
        ObjectCodec codec = new ObjectMapper().getFactory();
        jsonGenerator = new JsonGeneratorImpl(context, 0, codec);
    }

    @Test
    public void testVersion() {
        Version version = jsonGenerator.version();
        assertNotNull(version);
        assertEquals("2.12.5", version.toString());
    }

    @Test
    public void testWriteStringField() throws IOException {
        String fieldName = "testField";
        String value = "testValue";

        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField(fieldName, value);
        jsonGenerator.writeEndObject();

        String result = jsonGenerator.getOutputContentAsString();
        assertEquals("{\"testField\":\"testValue\"}", result);
    }

    @Test
    public void testSetCharacterEscapes() throws IOException {
        CharacterEscapes characterEscapes = new CharacterEscapes() {
            @Override
            public int[] getEscapeCodesForAscii() {
                return new int[] { 0x20, 0x7F };
            }
        };

        jsonGenerator.setCharacterEscapes(characterEscapes);
        jsonGenerator.writeString("test");
        String result = jsonGenerator.getOutputContentAsString();
        assertEquals("{\"test\":\"t\\020e\\07f\"}", result);
    }

    @Test
    public void testGetCharacterEscapes() throws IOException {
        jsonGenerator.writeString("test");
        CharacterEscapes characterEscapes = jsonGenerator.getCharacterEscapes();
        int[] escapeCodes = characterEscapes.getEscapeCodesForAscii();
        assertEquals(sOutputEscapes.length, escapeCodes.length);
        for (int i = 0; i < escapeCodes.length; i++) {
            assertEquals(sOutputEscapes[i], escapeCodes[i]);
        }
    }

    @Test
    public void testSetRootValueSeparator() throws IOException {
        SerializableString rootValueSeparator = new DefaultSerializableString("test");
        jsonGenerator.setRootValueSeparator(rootValueSeparator);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("testField");
        jsonGenerator.writeString("testValue");
        jsonGenerator.writeEndObject();
        String result = jsonGenerator.getOutputContentAsString();
        assertEquals("{\"test\":{\"testField\":\"testValue\"}}", result);
    }

    @Test
    public void testGetRootValueSeparator() {
        assertEquals(DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, jsonGenerator.getRootValueSeparator());
    }
}