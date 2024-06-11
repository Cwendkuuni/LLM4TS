import static org.junit.Assert.*;
import org.junit.Test;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey_NullKey_ReturnsNull() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(1, String.class);
        assertNull(keyDeserializer.deserializeKey(null, null));
    }

    @Test
    public void testDeserializeKey_ValidKey_ReturnsKey() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(1, String.class);
        assertEquals("test", keyDeserializer.deserializeKey("test", null));
    }

    @Test
    public void testDeserializeKey_InvalidKey_ThrowsException() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(1, String.class);
        assertThrows(IOException.class, () -> {
            keyDeserializer.deserializeKey("", null);
        });
    }

    @Test
    public void testParse_ParseInt_ValidInt_ReturnsInt() throws Exception {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(1, String.class);
        assertEquals(10, keyDeserializer._parseInt("10"));
    }

    @Test
    public void testParse_ParseLong_ValidLong_ReturnsLong() throws Exception {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(1, String.class);
        assertEquals(100L, keyDeserializer._parseLong("100"));
    }

    @Test
    public void testParse_ParseDouble_ValidDouble_ReturnsDouble() throws Exception {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(1, String.class);
        assertEquals(10.5, keyDeserializer._parseDouble("10.5"), 0.001);
    }
}