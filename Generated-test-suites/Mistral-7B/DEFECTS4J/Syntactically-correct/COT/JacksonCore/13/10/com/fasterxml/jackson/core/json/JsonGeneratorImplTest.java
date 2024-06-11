package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;

/**
 * Test class for JsonGeneratorImpl.
 */
public class JsonGeneratorImplTest {

    private IOContext ioContext;
    private JsonGeneratorImpl jsonGenerator;

    @Before
    public void setUp() {
        ioContext = new IOContext();
        jsonGenerator = new JsonGeneratorImpl(ioContext, 0, null);
    }

    @After
    public void tearDown() throws IOException {
        jsonGenerator.close();
    }

    /*
     * Test method for {@link JsonGeneratorImpl#JsonGeneratorImpl(IOContext, int, ObjectCodec)}
     */
    @Test
    public void testJsonGeneratorImplConstruction() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, null);
        assertNotNull(jsonGenerator);
    }

    /*
     * Test method for {@link JsonGeneratorImpl#writeFieldName(String)}
     */
    @Test
    public void testWriteFieldName() throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("testField");
        jsonGenerator.writeEndObject();

        String jsonString = jsonGenerator.getOutputFactory().createString("");
        assertTrue(jsonString.contains("\"testField\""));
    }

    /*
     * Test method for {@link JsonGeneratorImpl#writeString(String)}
     */
    @Test
    public void testWriteString() throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("testField");
        jsonGenerator.writeString("testString");
        jsonGenerator.writeEndObject();

        String jsonString = jsonGenerator.getOutputFactory().createString("");
        assertTrue(jsonString.contains("\"testField\":\"testString\""));
    }

    /*
     * Test method for {@link JsonGeneratorImpl#writeStartObject()}
     */
    @Test
    public void testWriteStartObject() throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeEndObject();

        String jsonString = jsonGenerator.getOutputFactory().createString("");
        assertTrue(jsonString.contains("{}"));
    }

    /*
     * Test method for {@link JsonGeneratorImpl#writeEndObject()}
     */
    @Test
    public void testWriteEndObject() throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("testField", "testString");
        jsonGenerator.writeEndObject();

        String jsonString = jsonGenerator.getOutputFactory().createString("");
        assertTrue(jsonString.contains("{\"testField\":\"testString\"}"));
    }

    /*
     * Test method for {@link JsonGeneratorImpl#setCharacterEscapes(CharacterEscapes)}
     */
    @Test
    public void testSetCharacterEscapes() throws IOException {
        CharacterEscapes characterEscapes = new CharacterEscapes() {
            @Override
            public int encodeForUTF8(int c) {
                return c == '\\' ? '\\' * 2 : c;
            }
        };

        jsonGenerator.setCharacterEscapes(characterEscapes);
        jsonGenerator.writeString("testString\\");

        String jsonString = jsonGenerator.getOutputFactory().createString("");
        assertTrue(jsonString.contains("\\\\testString\\\\"));
    }

    /*
     * Test method for {@link JsonGeneratorImpl#getCharacterEscapes()}
     */
    @Test
    public void testGetCharacterEscapes() throws IOException {
        CharacterEscapes characterEscapes = jsonGenerator.getCharacterEscapes();
        assertEquals("com.fasterxml.jackson.core.io.StandardCharacterEscapes@1b6d651", characterEscapes.getClass().getName());
    }

    /*
     * Test method for {@link JsonGeneratorImpl#setRootValueSeparator(SerializableString)}
     */
    @Test
    public void testSetRootValueSeparator() throws IOException {
        SerializableString rootValueSeparator = new SerializableString("test_");
        jsonGenerator.setRootValueSeparator(rootValueSeparator);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("testField", "testString");
        jsonGenerator.writeEndObject();

        String jsonString = jsonGenerator.getOutputFactory().createString("");
        assertTrue(jsonString.contains("\"test_\":{\"testField\":\"testString\"}"));
    }
}