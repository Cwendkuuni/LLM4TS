
package com.example; // Replace with your package name

import org.junit.Test;
import static org.junit.Assert.assertNull;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

public class NullifyingDeserializerTest {
    @Test
    public void testDeserialize() throws IOException {
        Object result = new NullifyingDeserializer().deserialize(jsonParser, null);
        assertNull(result);
    }

    @Test
    public void testDeserializeEmptyJsonObject() throws IOException {
        jsonParser = objectMapper.getFactory().createParser("{}");
        Object result = new NullifyingDeserializer().deserialize(jsonParser, null);
        assertNull(result);
    }

    @Test
    public void testDeserializeWithInvalidToken() throws IOException {
        JsonFactory jsonFactory = objectMapper.getFactory();
        JsonParser jsonParser = jsonFactory.createParser("InvalidJSON");
        Object result = new NullifyingDeserializer().deserializeWithType(jsonParser, null, null);
        assertNull(result);
    }

    @Test
    public void testDeserializeWithIOException() throws IOException {
        JsonFactory jsonFactory = objectMapper.getFactory();
        JsonParser jsonParser = jsonFactory.createParser("InvalidJSON");
        JsonProcessingException exception = Assertions.assertThrows(JsonProcessingException.class, () -> {
            new NullifyingDeserializer().deserialize(jsonParser, null);
        });
        Assertions.assertEquals("Unexpected character (POS: 0) : [\"InvalidJSON\"]", exception.getMessage());
    }

    @Test
    public void testDeserializeWithTypeDeserializerIOException() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory = objectMapper.getFactory();
        JsonParser jsonParser = jsonFactory.createParser("{\"type\":\"java.lang.String\"}");
        TypeDeserializer typeDeserializer = new BasicValueDeserializer();
        JsonProcessingException exception = Assertions.assertThrows(JsonProcessingException.class, () -> {
            new NullifyingDeserializer().deserializeWithType(jsonParser, null, typeDeserializer);
        });
        Assertions.assertEquals("Unexpected character (POS: 0) : [\"{\"type\":\"java.lang.String\"}\"]", exception.getMessage());
    }
}
