package com.fasterxml.jackson.core.json; // import statements go here

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonGeneratorImplTest {

    private JsonGeneratorImpl jsonGenerator;
    private IOContext ioContext;
    private ObjectCodec objectCodec;

    @Before
    public void setup() {
        ioContext = new IOContext();
        objectCodec = new ObjectMapper().getFactory().createObjectCodec();
        jsonGenerator = new JsonGeneratorImpl(ioContext, 0, objectCodec);
    }

    @Test
    public void testWriteStringField() throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("name", "John Doe");
        jsonGenerator.writeEndObject();

        String json = jsonGenerator.getFactory().createParser(new StringReader(jsonGenerator.toString())).readValueAsString();
        assertEquals("{\"name\":\"John Doe\"}", json);
    }

    @Test
    public void testWriteStringFieldWithCharacterEscapes() throws IOException {
        CharacterEscapes characterEscapes = new StandardCharacterEscapes();
        jsonGenerator = new JsonGeneratorImpl(ioContext, 0, objectCodec);
        jsonGenerator.setCharacterEscapes(characterEscapes);

        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("name", "John O'Doe");
        jsonGenerator.writeEndObject();

        String json = jsonGenerator.getFactory().createParser(new StringReader(jsonGenerator.toString())).readValueAsString();
        assertEquals("{\"name\":\"John O\\'Doe\"}", json);
    }

    @Test(expected = IOException.class)
    public void testWriteStringFieldWithNullValue() throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("name", null);
        jsonGenerator.writeEndObject();
    }

    // Add other test cases for other methods here

    
}