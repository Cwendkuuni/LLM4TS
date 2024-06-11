import com.fasterxml.jackson.databind.DeserializationContext;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class StdKeyDeserializerTest {

    private StdKeyDeserializer deserializer;

    @Before
    public void setUp(){
        deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
    }

    @Test
    public void testDeserializeKey_ValidKey() throws IOException {
        DeserializationContext context = null;
        Object deserializedKey = deserializer.deserializeKey("123", context);
        assertNotNull(deserializedKey);
        assertEquals(Integer.class, deserializedKey.getClass());
        assertEquals(123, deserializedKey);
    }

    @Test(expected = IOException.class)
    public void testDeserializeKey_NullKey() throws IOException {
        DeserializationContext context = null;
        deserializer.deserializeKey(null, context);
    }

    @Test(expected = IOException.class)
    public void testDeserializeKey_InvalidKey() throws IOException {
        DeserializationContext context = null;
        deserializer.deserializeKey("abc", context);
    }

    @Test(expected = IOException.class)
    public void testDeserializeKey_OutOfRangeKey() throws IOException {
        DeserializationContext context = null;
        deserializer.deserializeKey("10000000000", context);
    }

    @Test(expected = IOException.class)
    public void testDeserializeKey_NegativeKey() throws IOException {
        DeserializationContext context = null;
        deserializer.deserializeKey("-100", context);
    }

    @Test
    public void testGetKeyClass(){
        assertEquals(Integer.class, deserializer.getKeyClass());
    }

}