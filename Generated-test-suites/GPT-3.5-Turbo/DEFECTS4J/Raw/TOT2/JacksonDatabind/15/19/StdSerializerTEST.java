package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class StdSerializerTest {

    private StdSerializer<String> stdSerializer;

    @Before
    public void setUp() {
        stdSerializer = new StringSerializer();
    }

    @Test
    public void testHandledType() {
        assertEquals(String.class, stdSerializer.handledType());
    }

    @Test
    public void testSerialize() throws IOException, JsonGenerationException {
        JsonGenerator jsonGenerator = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializationConfig().getProvider();
        stdSerializer.serialize("Test", jsonGenerator, provider);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        JsonNode schema = stdSerializer.getSchema(new ObjectMapper().getSerializationConfig().getProvider(), String.class);
        assertNotNull(schema);
        assertEquals("string", schema.get("type").asText());
    }

    @Test
    public void testWrapAndThrow() {
        try {
            throw new IOException("Test Exception");
        } catch (IOException e) {
            stdSerializer.wrapAndThrow(null, e, null, "field");
        } catch (Exception ex) {
            assertTrue(ex instanceof IOException);
        }
    }

    @Test(expected = JsonParseException.class)
    public void testWrapAndThrowWithJsonParseException() throws IOException {
        throw new JsonParseException("Test Exception", JsonLocation.NA);
    }

}