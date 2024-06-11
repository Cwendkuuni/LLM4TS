import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import java.io.IOException;

public class NullifyingDeserializerTest {
    
    private NullifyingDeserializer nullifyingDeserializer;
    
    @Before
    public void setUp() {
        nullifyingDeserializer = new NullifyingDeserializer();
    }
    
    @Test
    public void testDeserialize() throws IOException {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object result = nullifyingDeserializer.deserialize(p, ctxt);
        assertNull(result);
    }
    
    @Test
    public void testDeserializeWithType() throws IOException {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        TypeDeserializer typeDeserializer = null;
        Object result = nullifyingDeserializer.deserializeWithType(p, ctxt, typeDeserializer);
        assertNull(result);
    }
}