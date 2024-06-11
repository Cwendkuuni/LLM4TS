import org.junit.Test;

import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    // Test deserializeKey(String key, DeserializationContext ctxt)
    @Test
    public void testDeserializeKey() throws Exception {
        // create an instance of StdKeyDeserializer
        StdKeyDeserializer deserializer = new StdKeyDeserializer(TYPE_STRING, String.class);

        // test with a valid key
        String validKey = "valid";
        Object result = deserializer.deserializeKey(validKey, null);
        assertEquals(validKey, result);

        // test with a null key
        String nullKey = null;
        result = deserializer.deserializeKey(nullKey, null);
        assertNull(result);
    }

    // Test getKeyClass()
    @Test
    public void testGetKeyClass() throws Exception {
        // create an instance of StdKeyDeserializer
        StdKeyDeserializer deserializer = new StdKeyDeserializer(TYPE_STRING, String.class);

        // test the key class
        Class<?> keyClass = deserializer.getKeyClass();
        assertEquals(String.class, keyClass);
    }
}