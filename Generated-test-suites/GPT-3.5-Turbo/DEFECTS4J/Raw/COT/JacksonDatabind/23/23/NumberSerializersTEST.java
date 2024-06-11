import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);
        // Assert that allDeserializers contains the expected serializers
        // ...
    }

    @Test
    public void testShortSerializer() throws IOException {
        JsonSerializer<Short> serializer = new NumberSerializers.ShortSerializer();
        // Test the functionality of the ShortSerializer
        // ...
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        JsonSerializer<Object> serializer = new NumberSerializers.IntegerSerializer();
        // Test the functionality of the IntegerSerializer
        // ...
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        JsonSerializer<Number> serializer = new NumberSerializers.IntLikeSerializer();
        // Test the functionality of the IntLikeSerializer
        // ...
    }

    @Test
    public void testLongSerializer() throws IOException {
        JsonSerializer<Object> serializer = new NumberSerializers.LongSerializer();
        // Test the functionality of the LongSerializer
        // ...
    }

    @Test
    public void testFloatSerializer() throws IOException {
        JsonSerializer<Object> serializer = new NumberSerializers.FloatSerializer();
        // Test the functionality of the FloatSerializer
        // ...
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        JsonSerializer<Object> serializer = new NumberSerializers.DoubleSerializer();
        // Test the functionality of the DoubleSerializer
        // ...
    }

}