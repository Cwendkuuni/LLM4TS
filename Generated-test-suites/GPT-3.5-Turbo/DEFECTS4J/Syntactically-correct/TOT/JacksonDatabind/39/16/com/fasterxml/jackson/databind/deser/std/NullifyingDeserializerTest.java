import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class NullifyingDeserializerTest {

    private NullifyingDeserializer nullifyingDeserializer;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        nullifyingDeserializer = new NullifyingDeserializer();
        objectMapper = new ObjectMapper(new JsonFactory());
    }

    @Test
    void deserialize_ShouldSkipChildrenAndReturnNull() throws IOException {
        // Given
        String json = "{\"name\":\"John\",\"age\":30}";
        JsonParser parser = objectMapper.getFactory().createParser(json);

        // When
        Object result = nullifyingDeserializer.deserialize(parser, DeserializationContext.builder().build());

        // Then
        assertNull(result);
        assertTrue(parser.isClosed());
    }

    @Test
    void deserialize_ShouldSkipChildrenAndReturnNull_ForEmptyJson() throws IOException {
        // Given
        String json = "{}";
        JsonParser parser = objectMapper.getFactory().createParser(json);

        // When
        Object result = nullifyingDeserializer.deserialize(parser, DeserializationContext.builder().build());

        // Then
        assertNull(result);
        assertTrue(parser.isClosed());
    }

    @Test
    void deserializeWithType_ShouldDeserializeTypedFromAny_ForStartArray() throws IOException {
        // Given
        String json = "[1, 2, 3]";
        JsonParser parser = objectMapper.getFactory().createParser(json);
        TypeDeserializer typeDeserializer = null; // Mock type deserializer

        // When
        Object result = nullifyingDeserializer.deserializeWithType(parser, DeserializationContext.builder().build(), typeDeserializer);

        // Then
        assertEquals(new ObjectMapper().readTree(json), result);
        assertTrue(parser.isClosed());
    }

    @Test
    void deserializeWithType_ShouldDeserializeTypedFromAny_ForStartObject() throws IOException {
        // Given
        String json = "{\"name\":\"John\",\"age\":30}";
        JsonParser parser = objectMapper.getFactory().createParser(json);
        TypeDeserializer typeDeserializer = null; // Mock type deserializer

        // When
        Object result = nullifyingDeserializer.deserializeWithType(parser, DeserializationContext.builder().build(), typeDeserializer);

        // Then
        assertEquals(new ObjectMapper().readTree(json), result);
        assertTrue(parser.isClosed());
    }

    @Test
    void deserializeWithType_ShouldDeserializeTypedFromAny_ForFieldName() throws IOException {
        // Given
        String json = "{\"name\":\"John\",\"age\":30}";
        JsonParser parser = objectMapper.getFactory().createParser(json);
        TypeDeserializer typeDeserializer = null; // Mock type deserializer

        // When
        Object result = nullifyingDeserializer.deserializeWithType(parser, DeserializationContext.builder().build(), typeDeserializer);

        // Then
        assertEquals(new ObjectMapper().readTree(json), result);
        assertTrue(parser.isClosed());
    }

    @Test
    void deserializeWithType_ShouldReturnNull_ForOtherTokens() throws IOException {
        // Given
        String json = "30";
        JsonParser parser = objectMapper.getFactory().createParser(json);
        TypeDeserializer typeDeserializer = null; // Mock type deserializer

        // When
        Object result = nullifyingDeserializer.deserializeWithType(parser, DeserializationContext.builder().build(), typeDeserializer);

        // Then
        assertNull(result);
        assertTrue(parser.isClosed());
    }

    @Test
    void deserializeWithType_ShouldReturnNull_ForEmptyJson() throws IOException {
        // Given
        String json = "{}";
        JsonParser parser = objectMapper.getFactory().createParser(json);
        TypeDeserializer typeDeserializer = null; // Mock type deserializer

        // When
        Object result = nullifyingDeserializer.deserializeWithType(parser, DeserializationContext.builder().build(), typeDeserializer);

        // Then
        assertNull(result);
        assertTrue(parser.isClosed());
    }

}