
import static org.junit.Assert.*;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;

public class NumberSerializersTest {

    private Map<String, JsonSerializer<?>> allDeserializers;

    @Before
    public void setUp() {
        allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);
    }

    @Test
    public void testAddAll() {
        assertNotNull(allDeserializers);
        assertTrue(allDeserializers.size() > 0);
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        JsonSerializer<?> serializer = allDeserializers.get(Integer.class.getName());
        assertNotNull(serializer);
        assertTrue(serializer instanceof NumberSerializers.IntegerSerializer);

        SerializerProvider provider = new SerializerProvider();
        JsonNode schema = serializer.getSchema(provider, null);
        assertNotNull(schema);
        assertTrue(schema.isObject());
        assertEquals("integer", schema.get("type").asText());
    }

    @Test
    public void testShortSerializer() throws IOException {
        JsonSerializer<?> serializer = allDeserializers.get(Short.class.getName());
        assertNotNull(serializer);
        assertTrue(serializer instanceof NumberSerializers.ShortSerializer);

        SerializerProvider provider = new SerializerProvider();
        JsonNode schema = serializer.getSchema(provider, null);
        assertNotNull(schema);
        assertTrue(schema.isObject());
        assertEquals("number", schema.get("type").asText());
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        JsonSerializer<?> serializer = allDeserializers.get(Number.class.getName());
        assertNotNull(serializer);
        assertTrue(serializer instanceof NumberSerializers.IntLikeSerializer);

        SerializerProvider provider = new SerializerProvider();
        JsonNode schema = serializer.getSchema(provider, null);
        assertNotNull(schema);
        assertTrue(schema.isObject());
        assertEquals("integer", schema.get("type").asText());
    }

    @Test
    public void testLongSerializer() throws IOException {
        JsonSerializer<?> serializer = allDeserializers.get(Long.class.getName());
        assertNotNull(serializer);
        assertTrue(serializer instanceof NumberSerializers.LongSerializer);

        SerializerProvider provider = new SerializerProvider();
        JsonNode schema = serializer.getSchema(provider, null);
        assertNotNull(schema);
        assertTrue(schema.isObject());
        assertEquals("number", schema.get("type").asText());
    }

    @Test
    public void testFloatSerializer() throws IOException {
        JsonSerializer<?> serializer = allDeserializers.get(Float.class.getName());
        assertNotNull(serializer);
        assertTrue(serializer instanceof NumberSerializers.FloatSerializer);

        SerializerProvider provider = new SerializerProvider();
        JsonNode schema = serializer.getSchema(provider, null);
        assertNotNull(schema);
        assertTrue(schema.isObject());
        assertEquals("number", schema.get("type").asText());
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        JsonSerializer<?> serializer = allDeserializers.get(Double.class.getName());
        assertNotNull(serializer);
        assertTrue(serializer instanceof NumberSerializers.DoubleSerializer);

        SerializerProvider provider = new SerializerProvider();
        JsonNode schema = serializer.getSchema(provider, null);
        assertNotNull(schema);
        assertTrue(schema.isObject());
        assertEquals("number", schema.get("type").asText());
    }
}
