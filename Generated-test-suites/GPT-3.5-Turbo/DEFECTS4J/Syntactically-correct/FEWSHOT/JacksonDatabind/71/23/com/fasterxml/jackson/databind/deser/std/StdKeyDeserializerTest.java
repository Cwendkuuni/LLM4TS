import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class StdKeyDeserializerTest {
    @Test
    public void testDeserializeKey() throws Exception {
        assertNull(StdKeyDeserializer.forType(String.class).deserializeKey(null, null));
        assertEquals("foo", StdKeyDeserializer.forType(String.class).deserializeKey("foo", null));
        
        assertNull(StdKeyDeserializer.forType(Object.class).deserializeKey(null, null));
        assertEquals("foo", StdKeyDeserializer.forType(Object.class).deserializeKey("foo", null));
        
        assertNull(StdKeyDeserializer.forType(UUID.class).deserializeKey(null, null));
        assertEquals(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d"), 
                     StdKeyDeserializer.forType(UUID.class).deserializeKey("38400000-8cf0-11bd-b23e-10b96e4ef00d", null));
                     
        assertNull(StdKeyDeserializer.forType(Integer.class).deserializeKey(null, null));
        assertEquals(100, StdKeyDeserializer.forType(Integer.class).deserializeKey("100", null));
                     
        assertNull(StdKeyDeserializer.forType(Long.class).deserializeKey(null, null));
        assertEquals(1000L, StdKeyDeserializer.forType(Long.class).deserializeKey("1000", null));
                     
        assertNull(StdKeyDeserializer.forType(Date.class).deserializeKey(null, null));
        // Implement the expected output
    }
}