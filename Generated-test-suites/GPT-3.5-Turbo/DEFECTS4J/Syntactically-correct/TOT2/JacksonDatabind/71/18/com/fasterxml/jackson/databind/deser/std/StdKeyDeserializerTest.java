import static org.junit.Assert.*;

import org.junit.Test;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey_NullKey() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        assertNull(deserializer.deserializeKey(null, null));
    }

    @Test
    public void testDeserializeKey_EmptyStringKey() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        assertEquals("", deserializer.deserializeKey("", null));
    }

    @Test
    public void testDeserializeKey_NonEmptyStringKey() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        assertEquals("test", deserializer.deserializeKey("test", null));
    }

    @Test
    public void testDeserializeKey_Integer() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        assertEquals(Integer.valueOf(123), deserializer.deserializeKey("123", null));
    }

    @Test
    public void testDeserializeKey_Long() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);
        assertEquals(Long.valueOf(1234567890), deserializer.deserializeKey("1234567890", null));
    }

    @Test
    public void testDeserializeKey_Float() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_FLOAT, Float.class);
        assertEquals(Float.valueOf(3.14f), deserializer.deserializeKey("3.14", null));
    }

    @Test
    public void testDeserializeKey_Double() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class);
        assertEquals(Double.valueOf(3.14159), deserializer.deserializeKey("3.14159", null));
    }

    // Add more test cases for other types

}