import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        // Test adding all serializers
        NumberSerializers numberSerializers = new NumberSerializers();
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        numberSerializers.addAll(allDeserializers);

        assertNotNull(allDeserializers.get(Integer.class.getName()));
        assertNotNull(allDeserializers.get(Long.class.getName()));
        assertNotNull(allDeserializers.get(Byte.class.getName()));
        assertNotNull(allDeserializers.get(Short.class.getName()));
        assertNotNull(allDeserializers.get(Float.class.getName()));
        assertNotNull(allDeserializers.get(Double.class.getName()));
    }

    @Test
    public void testShortSerializerSerialize() throws IOException {
        // Test ShortSerializer serialize method
        ShortSerializer shortSerializer = new ShortSerializer();
        JsonGenerator gen = null; // mock JsonGenerator
        SerializerProvider provider = null; // mock SerializerProvider
        short value = 10;
        shortSerializer.serialize(value, gen, provider);
        // Assert expected output from serialize method
    }

    // Continue with similar test cases for IntegerSerializer, IntLikeSerializer, LongSerializer, FloatSerializer, and DoubleSerializer

}