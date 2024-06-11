package com.fasterxml.jackson.core.json; // or the package where the test class is located

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.base.DefaultSerializableString;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JsonGeneratorImplTest {

    private JsonFactory factory;
    private ObjectMapper mapper;
    private JsonGenerator generator;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws IOException {
        factory = new JsonFactory();
        mapper = new ObjectMapper(factory);
        outputStream = new ByteArrayOutputStream();
    }

    @Test
    public void testDefaultJsonGenerator() throws IOException {
        generator = factory.createGenerator(new OutputStreamWriter(outputStream));
        assertNotNull(generator);
        assertEquals(Version.UNKNOWN_VERSION, generator.version());

        generator.writeStartObject();
        generator.writeStringField("name", "John Doe");
        generator.writeNumberField("age", 30);
        generator.writeEndObject();

        generator.close();

        String jsonString = outputStream.toString();
        assertEquals("{\"name\":\"John Doe\",\"age\":30}", jsonString);
    }

    @Test
    public void testCustomCharacterEscapes() throws IOException {
        CharacterEscapes customEscapes = new CharacterEscapes() {
            @Override
            public String getEscapeSequence(char c) {
                return String.format("\\u%04x", (int) c);
            }
        };

        generator = factory.createGenerator(new OutputStreamWriter(outputStream), null, customEscapes);

        generator.writeStartObject();
        generator.writeStringField("name", "John Doе");
        generator.writeEndObject();

        generator.close();

        String jsonString = outputStream.toString();
        assertEquals("{\"name\":\"John Do\\u00F4e\"}", jsonString);
    }

    @Test
    public void testQuoteFieldNames() throws IOException {
        generator = factory.createGenerator(new OutputStreamWriter(outputStream), null, null, true);

        generator.writeStartObject();
        generator.writeStringField("name", "John Doe");
        generator.writeEndObject();

        generator.close();

        String jsonString = outputStream.toString();
        assertEquals("{\"name\":\"John Doe\"}", jsonString);
    }

    @Test(expected = IOException.class)
    public void testWriteStringWithNullValue() throws IOException {
        generator = factory.createGenerator(new OutputStreamWriter(outputStream));

        generator.writeStartObject();
        generator.writeStringField("name", null);
        generator.writeEndObject();

        generator.close();
    }

    @Test
    public void testSetHighestNonEscapedChar() throws IOException {
        generator = factory.createGenerator(new OutputStreamWriter(outputStream), null, null, null);

        generator.setHighestNonEscapedChar(127);
        generator.writeStartObject();
        generator.writeStringField("name", "\u007FTest");
        generator.writeEndObject();

        generator.close();

        String jsonString = outputStream.toString();
        assertEquals("{\"name\":\"\\u007FTest\"}", jsonString);

        generator = factory.createGenerator(new OutputStreamWriter(outputStream), null, null, null);

        generator.setHighestNonEscapedChar(0);
        generator.writeStartObject();
        generator.writeStringField("name", "\u007FTest");
        generator.writeEndObject();

        generator.close();

        jsonString = outputStream.toString();
        String escapedJsonString = jsonString.replaceAll("\\\\u[0-9A-Fa-f]{4}", "%[0-9A-Fa-f]{2}");
        assertEquals("{\"name\":\"%7FTest\"}", escapedJsonString);
    }

    @Test
    public void testRootValueSeparator() throws IOException {
        generator = factory.createGenerator(new OutputStreamWriter(outputStream), null, null, null);
        generator.setRootValueSeparator(new DefaultSerializableString(":"));

        generator.writeStartObject();
        generator.writeStringField("name", "John Doe");
        generator.writeEndObject();

        generator.close();

        String jsonString = outputStream.toString();
        assertEquals(":{name:\"John Doe\"}", jsonString);
    }

    @Test
    public void testSetCharacterEscapesNull() throws IOException {
        generator = factory.createGenerator(new OutputStreamWriter(outputStream));

        generator.setCharacterEscapes(null);
        generator.writeStartObject();
        generator.writeStringField("name", "John Doe");
        generator.writeEndObject();

        generator.close();

        String jsonString = outputStream.toString();
        assertEquals("{\"name\":\"John Doe\"}", jsonString);
    }

    @Test
    public void testWriteStartObjectWithNullValue() throws IOException {
        generator = factory.createGenerator(new OutputStreamWriter(outputStream));

        generator.writeStartObject(null);
        generator.writeEndObject();

        generator.close();

        String jsonString = outputStream.toString();
        assertEquals("null", jsonString);
    }

    @Test
    public void testWriteEndObjectWithNullValue() throws IOException {
        generator = factory.createGenerator(new OutputStreamWriter(outputStream));

        generator.writeStartObject();
        generator.writeEndObject(null);

        generator.close();

        String jsonString = outputStream.toString();
        assertEquals("null", jsonString);
    }

    @Test
    public void testWriteNullWithNullValue() throws IOException {
        generator = factory.createGenerator(new OutputStreamWriter(outputStream));

        generator.writeNull();

        generator.close();

        String jsonString = outputStream.toString();
        assertEquals("null", jsonString);
    }
}