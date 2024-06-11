import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class StdKeyDeserializerTest {

    private StdKeyDeserializer keyDeserializer;

    @Before
    public void setUp() {
        keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
    }

    @Test
    public void testDeserializeKey() {
        String key = "test";
        DeserializationContext ctxt = new DeserializationContext(null, null);
        Object result = keyDeserializer.deserializeKey(key, ctxt);
        assertEquals(key, result);
    }

    @Test
    public void testGetKeyClass() {
        Class<?> keyClass = keyDeserializer.getKeyClass();
        assertEquals(String.class, keyClass);
    }

    @Test
    public void testParse() throws Exception {
        String key = "test";
        DeserializationContext ctxt = new DeserializationContext(null, null);
        Object result = keyDeserializer._parse(key, ctxt);
        assertEquals(key, result);
    }

    @Test
    public void testParseInt() throws IllegalArgumentException {
        String key = "123";
        int result = keyDeserializer._parseInt(key);
        assertEquals(123, result);
    }

    @Test
    public void testParseLong() throws IllegalArgumentException {
        String key = "123";
        long result = keyDeserializer._parseLong(key);
        assertEquals(123L, result);
    }

    @Test
    public void testParseDouble() throws IllegalArgumentException {
        String key = "123.45";
        double result = keyDeserializer._parseDouble(key);
        assertEquals(123.45, result, 0.0001);
    }
}