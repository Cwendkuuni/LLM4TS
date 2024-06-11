import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        // Test adding all deserializers
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);
        assertNotNull(allDeserializers);
        assertEquals(8, allDeserializers.size());
    }

    @Test
    public void testShortSerializer() {
        // Test ShortSerializer for serialization
        ShortSerializer serializer = new ShortSerializer();
        Short value = 10;
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        SerializerProvider provider = new DefaultSerializerProvider.Impl();
        try {
            serializer.serialize(value, generator, provider);
        } catch (IOException e) {
            fail("Serialization error: " + e.getMessage());
        }
        assertEquals("10", writer.toString());
    }

    // Add test cases for other serializers like IntegerSerializer, IntLikeSerializer, LongSerializer, FloatSerializer, and DoubleSerializer
    // Make sure to include typical use cases, edge cases, and error scenarios

}