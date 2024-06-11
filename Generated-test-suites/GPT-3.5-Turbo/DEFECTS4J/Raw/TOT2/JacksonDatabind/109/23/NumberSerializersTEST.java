import static org.junit.Assert.*;
import org.junit.Test;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        // Test adding serializers for Integer
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);

        assertNotNull(allDeserializers.get(Integer.class.getName()));
        assertNotNull(allDeserializers.get(Integer.TYPE.getName()));
    }

    @Test
    public void testShortSerializer() {
        ShortSerializer shortSerializer = new ShortSerializer();

        // Test serialize method for ShortSerializer
        // Typical use case
        StringWriter writer = new StringWriter();
        JsonGenerator gen = new JsonFactory().createGenerator(writer);
        shortSerializer.serialize((short) 10, gen, new DefaultSerializerProvider());
        gen.close();
        assertEquals("10", writer.toString());

        // Edge case
        writer = new StringWriter();
        gen = new JsonFactory().createGenerator(writer);
        shortSerializer.serialize((short) -10, gen, new DefaultSerializerProvider());
        gen.close();
        assertEquals("-10", writer.toString());
    }

    // Add test methods for IntegerSerializer, IntLikeSerializer, LongSerializer, FloatSerializer, DoubleSerializer

}