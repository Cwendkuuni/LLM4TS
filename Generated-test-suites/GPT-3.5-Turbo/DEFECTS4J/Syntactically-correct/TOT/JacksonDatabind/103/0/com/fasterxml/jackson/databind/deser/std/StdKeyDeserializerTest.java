import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    private StdKeyDeserializer keyDeserializer;

    @Before
    public void setUp() {
        keyDeserializer = new StdKeyDeserializer(TYPE_STRING, String.class);
    }

    @Test
    public void testDeserializeKey_ValidString_ShouldReturnString() throws Exception {
        String key = "test";
        DeserializationContext ctxt = new DeserializationContext();

        Object result = keyDeserializer.deserializeKey(key, ctxt);

        assertEquals("test", result);
    }

    @Test
    public void testDeserializeKey_NullString_ShouldReturnNull() throws Exception {
        String key = null;
        DeserializationContext ctxt = new DeserializationContext();

        Object result = keyDeserializer.deserializeKey(key, ctxt);

        assertNull(result);
    }

    @Test
    public void testDeserializeKey_EmptyString_ShouldReturnEmptyString() throws Exception {
        String key = "";
        DeserializationContext ctxt = new DeserializationContext();

        Object result = keyDeserializer.deserializeKey(key, ctxt);

        assertEquals("", result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeKey_InvalidString_ShouldThrowIOException() throws Exception {
        String key = "invalid";
        DeserializationContext ctxt = new DeserializationContext();

        keyDeserializer.deserializeKey(key, ctxt);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeKey_NullKeyClass_ShouldThrowIllegalArgumentException() throws Exception {
        String key = "test";
        DeserializationContext ctxt = new DeserializationContext();
        keyDeserializer = new StdKeyDeserializer(TYPE_STRING, null);

        keyDeserializer.deserializeKey(key, ctxt);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeKey_NullDeserializationContext_ShouldThrowIllegalArgumentException() throws Exception {
        String key = "test";
        DeserializationContext ctxt = null;

        keyDeserializer.deserializeKey(key, ctxt);
    }

}