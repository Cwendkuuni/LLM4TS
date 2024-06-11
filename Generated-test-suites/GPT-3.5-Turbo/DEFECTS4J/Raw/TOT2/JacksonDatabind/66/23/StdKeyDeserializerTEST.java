import static org.junit.Assert.*;
import org.junit.Test;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey_NullKey_ReturnsNull() throws Exception {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        assertNull(stdKeyDeserializer.deserializeKey(null, null));
    }

    @Test
    public void testDeserializeKey_ValidKey_ReturnsDeserializedObject() throws Exception {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        assertEquals(Boolean.TRUE, stdKeyDeserializer.deserializeKey("true", null));
    }

    @Test
    public void testDeserializeKey_InvalidKey_ThrowsIOException() {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class);
        assertThrows(IOException.class, () -> stdKeyDeserializer.deserializeKey("invalidurl", null));
    }

    @Test
    public void testForType_WithValidType_ReturnsStdKeyDeserializer() {
        assertNotNull(StdKeyDeserializer.forType(String.class));
    }

    @Test
    public void testForType_WithInvalidType_ReturnsNull() {
        assertNull(StdKeyDeserializer.forType(ArrayList.class));
    }

    @Test
    public void testGetKeyClass_ReturnsKeyClass() {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        assertEquals(Integer.class, stdKeyDeserializer.getKeyClass());
    }

    @Test
    public void testStandardStringDeserializer_ReturnsString() throws Exception {
        StdKeyDeserializer.StringKD stringKD = new StdKeyDeserializer.StringKD(String.class);
        assertEquals("testString", stringKD.deserializeKey("testString", null));
    }
}