import static org.junit.Assert.*;
import org.junit.Test;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey_NullKey_ReturnsNull() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        assertNull(keyDeserializer.deserializeKey(null, null));
    }

    @Test
    public void testDeserializeKey_InvalidKey_ThrowsException() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        assertThrows(IOException.class, () -> keyDeserializer.deserializeKey("invalid_key", null));
    }

    @Test
    public void testForType_StringClass_ReturnsStringKDInstance() {
        StdKeyDeserializer stringKeyDeserializer = StdKeyDeserializer.forType(String.class);
        assertTrue(stringKeyDeserializer instanceof StdKeyDeserializer.StringKD);
    }

    @Test
    public void testForType_UnknownClass_ReturnsNull() {
        assertNull(StdKeyDeserializer.forType(ArrayList.class));
    }

    @Test
    public void testConstructor_StringCtorKeyDeserializer_InitializedCorrectly() {
        Constructor<?> ctor = String.class.getConstructor(String.class);
        StdKeyDeserializer.StringCtorKeyDeserializer ctorKeyDeserializer = new StdKeyDeserializer.StringCtorKeyDeserializer(ctor);
        assertNotNull(ctorKeyDeserializer);
    }

    @Test
    public void test_parse_IntKey_ReturnsIntValue() throws Exception {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        assertEquals(42, keyDeserializer._parse("42", null));
    }

    @Test
    public void test_parseLong_LongKey_ReturnsLongValue() throws Exception {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);
        assertEquals(1234567890L, keyDeserializer._parseLong("1234567890"));
    }

    @Test
    public void test_parseDouble_DoubleKey_ReturnsDoubleValue() throws Exception {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class);
        assertEquals(3.14, keyDeserializer._parseDouble("3.14"), 0.01);
    }

}