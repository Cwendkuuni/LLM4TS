import org.junit.Test;
import static org.junit.Assert.*;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserialize() {
        // Test case 1
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] expected1 = {"test1", "test2"};
        String[] result1 = deserializer.deserialize(new JsonParser(), new DeserializationContext());
        assertArrayEquals(expected1, result1);
        
        // Test case 2
        deserializer._elementDeserializer = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return jp.getText().toUpperCase();
            }
        };
        String[] expected2 = {"TEST1", "TEST2"};
        String[] result2 = deserializer.deserialize(new JsonParser(), new DeserializationContext());
        assertArrayEquals(expected2, result2);
    }

    @Test
    public void testDeserializeWithType() {
        // Test case 1
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        Object result1 = deserializer.deserializeWithType(new JsonParser(), new DeserializationContext(), new TypeDeserializer());
        assertNull(result1);
        
        // Test case 2
        deserializer._elementDeserializer = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return jp.getText().toUpperCase();
            }
        };
        Object result2 = deserializer.deserializeWithType(new JsonParser(), new DeserializationContext(), new TypeDeserializer());
        assertNull(result2);
    }
}