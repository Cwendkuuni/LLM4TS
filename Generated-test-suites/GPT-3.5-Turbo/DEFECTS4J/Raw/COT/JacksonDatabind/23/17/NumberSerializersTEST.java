
import com.fasterxml.jackson.databind.JsonSerializer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        // Test case to validate the addAll() method
        // Create a Map and invoke the addAll() method
        // Assert that all the serializers have been added to the Map correctly
        Map<String, JsonSerializer<?>> deserializers = new HashMap<>();
        NumberSerializers.addAll(deserializers);
        
        assertEquals(IntegerSerializer.class, deserializers.get(Integer.class.getName()).getClass());
        assertEquals(IntegerSerializer.class, deserializers.get(Integer.TYPE.getName()).getClass());
        assertEquals(LongSerializer.class, deserializers.get(Long.class.getName()).getClass());
        assertEquals(LongSerializer.class, deserializers.get(Long.TYPE.getName()).getClass());
        assertEquals(IntLikeSerializer.class, deserializers.get(Byte.class.getName()).getClass());
        assertEquals(IntLikeSerializer.class, deserializers.get(Byte.TYPE.getName()).getClass());
        assertEquals(ShortSerializer.class, deserializers.get(Short.class.getName()).getClass());
        assertEquals(ShortSerializer.class, deserializers.get(Short.TYPE.getName()).getClass());
        assertEquals(FloatSerializer.class, deserializers.get(Float.class.getName()).getClass());
        assertEquals(FloatSerializer.class, deserializers.get(Float.TYPE.getName()).getClass());
        assertEquals(DoubleSerializer.class, deserializers.get(Double.class.getName()).getClass());
        assertEquals(DoubleSerializer.class, deserializers.get(Double.TYPE.getName()).getClass());
    }
    
    @Test
    public void testShortSerializer() throws IOException {
        // Test case to validate the serialize() method of ShortSerializer class
        // Create a Short object and a mock JsonGenerator and SerializerProvider
        // Invoke the serialize() method and assert that the Short value is written to the JsonGenerator
        ShortSerializer serializer = new ShortSerializer();
        Short value = 10;
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        
        serializer.serialize(value, generator, provider);
        
        generator.flush();
        assertEquals("10", writer.toString());
    }
    
    // Additional test cases for other methods in the NumberSerializers class

}

