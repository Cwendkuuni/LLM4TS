import static org.junit.Assert.*;

import org.junit.Test;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
        String key = "test";
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        Object result = deserializer.deserializeKey(key, ctxt);
        assertEquals(key, result);
    }

    @Test
    public void testGetKeyClass() {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
        Class<?> keyClass = deserializer.getKeyClass();
        assertEquals(String.class, keyClass);
    }

    @Test
    public void testParse() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        String key = "123";
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        Object result = deserializer._parse(key, ctxt);
        assertEquals(123, result);
    }

    @Test
    public void testParseInt() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        String key = "123";
        int result = deserializer._parseInt(key);
        assertEquals(123, result);
    }

    @Test
    public void testParseLong() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);
        String key = "123";
        long result = deserializer._parseLong(key);
        assertEquals(123, result);
    }

    @Test
    public void testParseDouble() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class);
        String key = "123.45";
        double result = deserializer._parseDouble(key);
        assertEquals(123.45, result, 0.001);
    }

    @Test
    public void testWeirdKey() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
        String key = "test";
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        Exception e = new IllegalArgumentException("Invalid key");
        Object result = deserializer._weirdKey(ctxt, key, e);
        assertNotNull(result);
    }

}