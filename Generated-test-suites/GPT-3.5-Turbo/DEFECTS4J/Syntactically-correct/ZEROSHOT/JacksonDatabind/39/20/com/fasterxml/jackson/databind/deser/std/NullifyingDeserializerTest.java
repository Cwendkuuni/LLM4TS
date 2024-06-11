import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserialize() {
        JsonParser p = null; // initialize with appropriate value
        DeserializationContext ctxt = null; // initialize with appropriate value
        NullifyingDeserializer nullifyingDeserializer = new NullifyingDeserializer();
        Object result = nullifyingDeserializer.deserialize(p, ctxt);
        assertNull(result);
    }

    @Test
    public void testDeserializeWithType() {
        JsonParser p = null; // initialize with appropriate value
        DeserializationContext ctxt = null; // initialize with appropriate value
        TypeDeserializer typeDeserializer = null; // initialize with appropriate value
        NullifyingDeserializer nullifyingDeserializer = new NullifyingDeserializer();
        Object result = nullifyingDeserializer.deserializeWithType(p, ctxt, typeDeserializer);
        assertNull(result);
    }
}