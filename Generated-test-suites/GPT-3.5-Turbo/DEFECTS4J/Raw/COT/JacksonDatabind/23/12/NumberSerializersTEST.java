import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);
        
        // Check if all integer serializers have been added
        assertTrue(allDeserializers.containsKey(Integer.class.getName()));
        assertTrue(allDeserializers.containsKey(Integer.TYPE.getName()));
        
        // Check if all long serializers have been added
        assertTrue(allDeserializers.containsKey(Long.class.getName()));
        assertTrue(allDeserializers.containsKey(Long.TYPE.getName()));
        
        // Check if all byte serializers have been added
        assertTrue(allDeserializers.containsKey(Byte.class.getName()));
        assertTrue(allDeserializers.containsKey(Byte.TYPE.getName()));
        
        // Check if all short serializers have been added
        assertTrue(allDeserializers.containsKey(Short.class.getName()));
        assertTrue(allDeserializers.containsKey(Short.TYPE.getName()));
        
        // Check if all float serializers have been added
        assertTrue(allDeserializers.containsKey(Float.class.getName()));
        assertTrue(allDeserializers.containsKey(Float.TYPE.getName()));
        
        // Check if all double serializers have been added
        assertTrue(allDeserializers.containsKey(Double.class.getName()));
        assertTrue(allDeserializers.containsKey(Double.TYPE.getName()));
    }

    @Test
    public void testShortSerializer() throws IOException {
        NumberSerializers.ShortSerializer serializer = new NumberSerializers.ShortSerializer();
        Short value = 12345;
        JsonGenerator gen = null; // Dummy generator
        
        serializer.serialize(value, gen, null);
        
        // Assert that serialization succeeded
        // (no exceptions thrown)
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        NumberSerializers.IntegerSerializer serializer = new NumberSerializers.IntegerSerializer();
        Integer value = 12345;
        JsonGenerator gen = null; // Dummy generator
        
        serializer.serialize(value, gen, null);
        
        // Assert that serialization succeeded
        // (no exceptions thrown)
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        NumberSerializers.IntLikeSerializer serializer = new NumberSerializers.IntLikeSerializer();
        Number value = 12345;
        JsonGenerator gen = null; // Dummy generator
        
        serializer.serialize(value, gen, null);
        
        // Assert that serialization succeeded
        // (no exceptions thrown)
    }

    @Test
    public void testLongSerializer() throws IOException {
        NumberSerializers.LongSerializer serializer = new NumberSerializers.LongSerializer();
        Long value = 12345L;
        JsonGenerator gen = null; // Dummy generator
        
        serializer.serialize(value, gen, null);
        
        // Assert that serialization succeeded
        // (no exceptions thrown)
    }

    @Test
    public void testFloatSerializer() throws IOException {
        NumberSerializers.FloatSerializer serializer = new NumberSerializers.FloatSerializer();
        Float value = 123.45f;
        JsonGenerator gen = null; // Dummy generator
        
        serializer.serialize(value, gen, null);
        
        // Assert that serialization succeeded
        // (no exceptions thrown)
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        NumberSerializers.DoubleSerializer serializer = new NumberSerializers.DoubleSerializer();
        Double value = 123.45;
        JsonGenerator gen = null; // Dummy generator
        
        serializer.serialize(value, gen, null);
        
        // Assert that serialization succeeded
        // (no exceptions thrown)
    }
}