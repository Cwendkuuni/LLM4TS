import static org.junit.Assert.*;
import org.junit.Test;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey_String() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer();
        DeserializationContext ctxt = new DeserializationContext();
        String key = "testString";
        Object result = keyDeserializer.deserializeKey(key, ctxt);
        assertEquals("testString", result);
    }

    @Test
    public void testDeserializeKey_Boolean() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer();
        DeserializationContext ctxt = new DeserializationContext();
        String key = "true";
        Object result = keyDeserializer.deserializeKey(key, ctxt);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testDeserializeKey_Enum() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer();
        DeserializationContext ctxt = new DeserializationContext();
        String key = "VALUE1";
        Object result = keyDeserializer.deserializeKey(key, ctxt);
        assertEquals(Enum.VALUE1, result);
    }

    @Test(expected = WeirdKeyException.class)
    public void testDeserializeKey_InvalidEnum() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer();
        DeserializationContext ctxt = new DeserializationContext();
        String key = "INVALID";
        keyDeserializer.deserializeKey(key, ctxt);
    }

    @Test
    public void testDeserializeKey_Integer() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer();
        DeserializationContext ctxt = new DeserializationContext();
        String key = "123";
        Object result = keyDeserializer.deserializeKey(key, ctxt);
        assertEquals(123, result);
    }

    @Test
    public void testDeserializeKey_InvalidInteger() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer();
        DeserializationContext ctxt = new DeserializationContext();
        String key = "abc";
        Object result = keyDeserializer.deserializeKey(key, ctxt);
        assertNull(result);
    }

    @Test
    public void testDeserializeKey_Null() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer();
        DeserializationContext ctxt = new DeserializationContext();
        String key = null;
        Object result = keyDeserializer.deserializeKey(key, ctxt);
        assertNull(result);
    }

}