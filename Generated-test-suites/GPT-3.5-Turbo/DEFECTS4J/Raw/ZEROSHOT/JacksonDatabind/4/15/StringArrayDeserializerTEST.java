import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

public class StringArrayDeserializerTest {

    private StringArrayDeserializer stringArrayDeserializer;

    @Before
    public void setUp() {
        stringArrayDeserializer = new StringArrayDeserializer();
    }

    @Test
    public void testDeserialize() throws Exception {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        String[] result = stringArrayDeserializer.deserialize(jp, ctxt);
        assertNull(result);
    }

    @Test
    public void testDeserializeCustom() throws Exception {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        String[] result = stringArrayDeserializer._deserializeCustom(jp, ctxt);
        assertNull(result);
    }

    @Test
    public void testDeserializeWithType() throws Exception {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        String[] result = (String[]) stringArrayDeserializer.deserializeWithType(jp, ctxt, null);
        assertNull(result);
    }

    @Test
    public void testHandleNonArray() throws Exception {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        String[] result = stringArrayDeserializer.handleNonArray(jp, ctxt);
        assertNull(result);
    }

    @Test
    public void testCreateContextual() throws Exception {
        DeserializationContext ctxt = null;
        JsonDeserializer<?> deser = null;
        assertNull(stringArrayDeserializer.createContextual(ctxt, deser));
    }

}