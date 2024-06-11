import static org.junit.Assert.*;
import org.junit.Test;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey_NullKey() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        assertNull(deserializer.deserializeKey(null, null));
    }

    @Test
    public void testDeserializeKey_ValidStringKey() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        assertEquals("testKey", deserializer.deserializeKey("testKey", null));
    }

    @Test(expected = IOException.class)
    public void testDeserializeKey_DeserializationException() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        deserializer.deserializeKey("invalidKey", null);
    }

    @Test
    public void testGetType_String() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        assertEquals(String.class, deserializer.getType());
    }

    @Test
    public void testGetType_Integer() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        assertEquals(Integer.class, deserializer.getType());
    }

    @Test
    public void testParse_IntKey() throws IOException {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        assertEquals(10, deserializer._parse("10", null));
    }

    @Test(expected = IOException.class)
    public void testParse_IntKey_Invalid() throws IOException {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        deserializer._parse("invalid", null);
    }

    @Test
    public void testParseDouble_FloatKey() throws IOException {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_FLOAT, Float.class);
        assertEquals(10.5f, deserializer._parseDouble("10.5"), 0.001);
    }

    @Test
    public void testParseLong_LongKey() throws IOException {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);
        assertEquals(1000000000L, deserializer._parseLong("1000000000"));
    }

}