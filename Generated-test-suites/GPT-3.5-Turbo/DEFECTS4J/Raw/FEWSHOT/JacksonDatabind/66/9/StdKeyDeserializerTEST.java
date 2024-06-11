import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class StdKeyDeserializerTest {
    
    @Test
    public void testDeserializeKey() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(1, String.class);

        assertNull(deserializer.deserializeKey(null, new DeserializationContext(null, null)));
        assertEquals("key", deserializer.deserializeKey("key", new DeserializationContext(null, null)));
    }
    
    @Test
    public void testForType() {
        assertNull(StdKeyDeserializer.forType(null));
        assertEquals(StdKeyDeserializer.class, StdKeyDeserializer.forType(String.class).getClass());
    }
    
    @Test
    public void testGetKeyClass() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(1, String.class);

        assertEquals(String.class, deserializer.getKeyClass());
    }
    
    @Test
    public void test_parse() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(1, String.class) {
            @Override
            protected Object _parse(String key, DeserializationContext ctxt) throws Exception {
                return "parsed " + key;
            }
        };

        assertEquals("parsed key", deserializer._parse("key", new DeserializationContext(null, null)));
    }
    
    @Test
    public void test_parseInt() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(1, String.class) {
            @Override
            protected int _parseInt(String key) throws IllegalArgumentException {
                return Integer.parseInt(key);
            }
        };

        assertEquals(Integer.MAX_VALUE, deserializer._parseInt(String.valueOf(Integer.MAX_VALUE)));
    }
    
    @Test
    public void test_parseLong() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(1, String.class) {
            @Override
            protected long _parseLong(String key) throws IllegalArgumentException {
                return Long.parseLong(key);
            }
        };

        assertEquals(Long.MAX_VALUE, deserializer._parseLong(String.valueOf(Long.MAX_VALUE)));
    }
    
    @Test
    public void test_parseDouble() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(1, String.class) {
            @Override
            protected double _parseDouble(String key) throws IllegalArgumentException {
                return Double.parseDouble(key);
            }
        };

        assertEquals(Double.MAX_VALUE, deserializer._parseDouble(String.valueOf(Double.MAX_VALUE)), 0.1);
    }

}