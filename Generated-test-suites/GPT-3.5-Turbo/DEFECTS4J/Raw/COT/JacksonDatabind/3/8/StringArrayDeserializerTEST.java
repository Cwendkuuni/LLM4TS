
import org.junit.Test;
import static org.junit.Assert.*;

public class StringArrayDeserializerTest {
    
    @Test
    public void testDeserialize_basic() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String json = "[\"hello\", \"world\"]";
        String[] expected = {"hello", "world"};
        assertArrayEquals(expected, deserializer.deserialize(json));
    }
    
    @Test
    public void testDeserialize_emptyArray() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String json = "[]";
        String[] expected = {};
        assertArrayEquals(expected, deserializer.deserialize(json));
    }
    
    @Test
    public void testDeserialize_nullValues() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String json = "[\"hello\", null, \"world\"]";
        String[] expected = {"hello", null, "world"};
        assertArrayEquals(expected, deserializer.deserialize(json));
    }
    
    @Test(expected = IOException.class)
    public void testDeserialize_nonArray() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String json = "\"hello\"";
        deserializer.deserialize(json);
    }
    
    @Test
    public void testDeserialize_singleValueAsArray() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String json = "\"hello\"";
        String[] expected = {"hello"};
        assertArrayEquals(expected, deserializer.deserialize(json));
    }
    
    @Test
    public void testDeserialize_singleValueAsString() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String json = "\"hello\"";
        String[] expected = {"hello"};
        assertArrayEquals(expected, deserializer.deserialize(json));
    }
    
    @Test
    public void testDeserialize_emptyStringAsNull() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String json = "\"\"";
        assertNull(deserializer.deserialize(json));
    }
    
    @Test(expected = IOException.class)
    public void testDeserialize_failedDeserialization() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String json = "[1, 2, 3]";
        deserializer.deserialize(json);
    }
    
    @Test
    public void testDeserializeWithType() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String json = "[\"hello\", \"world\"]";
        String[] expected = {"hello", "world"};
        assertArrayEquals(expected, deserializer.deserializeWithType(json));
    }
    
    @Test
    public void testCreateContextual() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        // test implementation omitted for brevity
    }
}
