import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey() throws IOException {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        DeserializationContext context = new DeserializationContext(null, null, null, null);
        Object result = deserializer.deserializeKey("10", context);
        assertEquals(Integer.valueOf(10), result);
    }
    
    @Test
    public void test_deserializeKey_null() throws IOException {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        DeserializationContext context = new DeserializationContext(null, null, null, null);
        Object result = deserializer.deserializeKey(null, context);
        assertNull(result);
    }
    
    @Test(expected = IOException.class)
    public void test_deserializeKey_invalidRepresentation() throws IOException {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        DeserializationContext context = new DeserializationContext(null, null, null, null);
        deserializer.deserializeKey("abc", context);
    }
    
    @Test
    public void test_getKeyClass() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        Class<?> keyClass = deserializer.getKeyClass();
        assertEquals(Integer.class, keyClass);
    }

    @Test
    public void test_parse() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        DeserializationContext context = new DeserializationContext(null, null, null, null);
        Object result = deserializer._parse("10", context);
        assertEquals(Integer.valueOf(10), result);
    }

    @Test
    public void test_parse_int_overflow() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        DeserializationContext context = new DeserializationContext(null, null, null, null);
        try {
            deserializer._parse("10000000000", context);
            fail("Should throw an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("For input string: \"10000000000\"", e.getMessage());
        }
    }
    
    @Test
    public void test_parseLong() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);
        DeserializationContext context = new DeserializationContext(null, null, null, null);
        Object result = deserializer._parseLong("12345678910");
        assertEquals(Long.valueOf(12345678910L), result);
    }
    
    @Test
    public void test_parseLong_overflow() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);
        DeserializationContext context = new DeserializationContext(null, null, null, null);
        try {
            deserializer._parseLong("100000000000000000000");
            fail("Should throw an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("For input string: \"100000000000000000000\"", e.getMessage());
        }
    }

    @Test
    public void test_parseDouble() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class);
        DeserializationContext context = new DeserializationContext(null, null, null, null);
        Object result = deserializer._parseDouble("3.14");
        assertEquals(3.14, result);
    }
  

}