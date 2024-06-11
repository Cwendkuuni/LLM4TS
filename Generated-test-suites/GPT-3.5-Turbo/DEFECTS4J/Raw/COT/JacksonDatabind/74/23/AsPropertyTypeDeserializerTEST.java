
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class AsPropertyTypeDeserializerTest {
    
    private AsPropertyTypeDeserializer deserializer;
    
    @Before
    public void setUp() {
        deserializer = new AsPropertyTypeDeserializer(null, null, null, true, null);
    }
    
    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        deserializer.deserializeTypedFromObject(p, ctxt);
        // Add assertion here
    }
    
    @Test
    public void testDeserializeTypedForId() throws IOException {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        TokenBuffer tb = null;
        deserializer._deserializeTypedForId(p, ctxt, tb);
        // Add assertion here
    }
    
    @Test
    public void testDeserializeTypedUsingDefaultImpl() throws IOException {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        TokenBuffer tb = null;
        deserializer._deserializeTypedUsingDefaultImpl(p, ctxt, tb);
        // Add assertion here
    }
    
    @Test
    public void testDeserializeTypedFromAny() throws IOException {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        deserializer.deserializeTypedFromAny(p, ctxt);
        // Add assertion here
    }
}
