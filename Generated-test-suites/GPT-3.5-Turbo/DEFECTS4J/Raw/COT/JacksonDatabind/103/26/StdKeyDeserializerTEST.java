
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey() throws IOException {
        StdKeyDeserializer deserializer = new StdKeyDeserializer() {
            @Override
            public Object _parse(String key, DeserializationContext ctxt) throws Exception {
                return key;
            }
        };
        DeserializationContext context = new DeserializationContext(null, null);
        assertEquals("key", deserializer.deserializeKey("key", context));
    }
    
    @Test
    public void testKeyClass() throws IOException {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class) {
            @Override
            public Object _parse(String key, DeserializationContext ctxt) throws Exception {
                return Integer.parseInt(key);
            }
        };
        assertEquals(Integer.class, deserializer.getKeyClass());
    }
    
    @Test
    public void testParseInt() throws IOException {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class) {
            @Override
            public Object _parse(String key, DeserializationContext ctxt) throws Exception {
                return Integer.parseInt(key);
            }
        };
        DeserializationContext context = new DeserializationContext(null, null);
        assertEquals(42, deserializer._parseInt("42"));
    }
    
    @Test
    public void testParseLong() throws IOException {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class) {
            @Override
            public Object _parse(String key, DeserializationContext ctxt) throws Exception {
                return Long.parseLong(key);
            }
        };
        DeserializationContext context = new DeserializationContext(null, null);
        assertEquals(123456789L, deserializer._parseLong("123456789"));
    }
    
    @Test
    public void testParseDouble() throws IOException {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class) {
            @Override
            public Object _parse(String key, DeserializationContext ctxt) throws Exception {
                return Double.parseDouble(key);
            }
        };
        DeserializationContext context = new DeserializationContext(null, null);
        assertEquals(3.14, deserializer._parseDouble("3.14"), 0.00001);
    }
    
    @Test
    public void testWeirdKey() throws IOException {
        StdKeyDeserializer deserializer = new StdKeyDeserializer() {
            @Override
            protected Object _weirdKey(DeserializationContext ctxt, String key, Exception e) throws IOException {
                return "WeirdKey";
            }
        };
        DeserializationContext context = new DeserializationContext(null, null);
        assertEquals("WeirdKey", deserializer._weirdKey(context, "key", new Exception("error")));
    }
}
