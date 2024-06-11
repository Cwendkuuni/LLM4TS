import org.junit.Test;

import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(1, Boolean.class);
        Object result = deserializer.deserializeKey("true", null);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGetKeyClass() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(1, Boolean.class);
        Class<?> result = deserializer.getKeyClass();
        assertEquals(Boolean.class, result);
    }

    @Test
    public void test_parse() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(1, Boolean.class);
        Object result = deserializer._parse("true", null);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void test_parseInt() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(1, Boolean.class);
        int result = deserializer._parseInt("123");
        assertEquals(123, result);
    }

    @Test
    public void test_parseLong() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(1, Boolean.class);
        long result = deserializer._parseLong("123");
        assertEquals(123L, result);
    }

    @Test
    public void test_parseDouble() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(1, Boolean.class);
        double result = deserializer._parseDouble("123.45");
        assertEquals(123.45, result, 0.0);
    }

}