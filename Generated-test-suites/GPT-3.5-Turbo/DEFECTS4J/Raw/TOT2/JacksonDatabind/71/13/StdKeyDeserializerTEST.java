import static org.junit.Assert.*;
import org.junit.Test;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey_NullKey_ReturnsNull() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        Object result = keyDeserializer.deserializeKey(null, null);
        assertNull(result);
    }

    @Test
    public void testDeserializeKey_InvalidKey_ThrowsIOException() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DATE, Date.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        try {
            keyDeserializer.deserializeKey("InvalidKey", ctxt);
            fail("Expected IOException was not thrown");
        } catch (IOException e) {
            // Expected this exception
        }
    }

    @Test
    public void testForType_String_ReturnsStringKD() {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);
        assertTrue(keyDeserializer instanceof StdKeyDeserializer.StringKD);
    }

    @Test
    public void testForType_Int_ReturnsStdKeyDeserializer() {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Integer.class);
        assertTrue(keyDeserializer instanceof StdKeyDeserializer);
    }

    // Add more test cases for other methods and edge cases

}