import com.fasterxml.jackson.databind.DeserializationContext;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    private StdKeyDeserializer stringKeyDeserializer;
    private StdKeyDeserializer objectKeyDeserializer;

    @Before
    public void setUp() {
        stringKeyDeserializer = StdKeyDeserializer.StringKD.forType(String.class);
        objectKeyDeserializer = StdKeyDeserializer.StringKD.forType(Object.class);
    }

    @Test
    public void testDeserializeKey_StringKey() throws Exception {
        DeserializationContext ctxt = null;
        String key = "test";

        Object result = stringKeyDeserializer.deserializeKey(key, ctxt);

        assertEquals(key, result);
    }

    @Test
    public void testDeserializeKey_ObjectKey() throws Exception {
        DeserializationContext ctxt = null;
        String key = "test";

        Object result = objectKeyDeserializer.deserializeKey(key, ctxt);

        assertEquals(key, result);
    }
}