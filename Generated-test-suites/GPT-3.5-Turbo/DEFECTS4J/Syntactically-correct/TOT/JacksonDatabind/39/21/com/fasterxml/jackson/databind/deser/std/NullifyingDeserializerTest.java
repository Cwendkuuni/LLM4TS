import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;

public class NullifyingDeserializerTest {
    
    @Test
    public void testDeserializeWithEmptyJson() throws Exception {
        JsonParser p = new JsonFactory().createParser("{}");
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        Object result = deserializer.deserialize(p, ctxt);
        
        assertNull(result);
    }
    
    @Test
    public void testDeserializeWithArray() throws Exception {
        JsonParser p = new JsonFactory().createParser("[]");
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        Object result = deserializer.deserialize(p, ctxt);
        
        assertNull(result);
    }
    
    @Test
    public void testDeserializeWithNestedObject() throws Exception {
        JsonParser p = new JsonFactory().createParser("{\"nested\":{\"key\":\"value\"}}");
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        Object result = deserializer.deserialize(p, ctxt);
        
        assertNull(result);
    }
    
    @Test
    public void testDeserializeWithField() throws Exception {
        JsonParser p = new JsonFactory().createParser("{\"field\":\"value\"}");
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        Object result = deserializer.deserialize(p, ctxt);
        
        assertNull(result);
    }
    
    @Test
    public void testDeserializeWithTypeAndArray() throws Exception {
        JsonParser p = new JsonFactory().createParser("[]");
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        TypeDeserializer typeDeserializer = null;
        
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        Object result = deserializer.deserializeWithType(p, ctxt, typeDeserializer);
        
        assertNull(result);
    }
    
    @Test
    public void testDeserializeWithTypeAndObject() throws Exception {
        JsonParser p = new JsonFactory().createParser("{}");
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        TypeDeserializer typeDeserializer = null;
        
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        Object result = deserializer.deserializeWithType(p, ctxt, typeDeserializer);
        
        assertNull(result);
    }
    
    @Test
    public void testDeserializeWithTypeAndField() throws Exception {
        JsonParser p = new JsonFactory().createParser("{\"field\":\"value\"}");
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        TypeDeserializer typeDeserializer = null;
        
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        Object result = deserializer.deserializeWithType(p, ctxt, typeDeserializer);
        
        assertNull(result);
    }
}