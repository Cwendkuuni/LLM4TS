import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testDeserializeKey_String() throws Exception {
        StdKeyDeserializer stringDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        
        String key = "test";
        Object deserializedKey = stringDeserializer.deserializeKey(key, ctxt);
        
        assertEquals("test", deserializedKey);
    }

    @Test
    public void testDeserializeKey_Integer() throws Exception {
        StdKeyDeserializer intDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        
        String key = "123";
        Object deserializedKey = intDeserializer.deserializeKey(key, ctxt);
        
        assertEquals(123, deserializedKey);
    }

    // Add more test cases for other methods here

}