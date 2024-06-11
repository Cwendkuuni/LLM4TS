import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationContext;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Date;
import java.util.Calendar;
import java.util.UUID;
import java.util.Locale;
import java.util.Currency;

public class StdKeyDeserializerTest {
    
    @Test
    public void testDeserializeKey_NullKey_ReturnsNull() throws IOException {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        
        Object result = deserializer.deserializeKey(null, ctxt);
        
        assertNull(result);
    }
    
    @Test
    public void testDeserializeKey_BooleanKey_ReturnsBoolean() throws IOException {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        
        Object result = deserializer.deserializeKey("true", ctxt);
        
        assertTrue((Boolean) result);
    }
    
    @Test
    public void testDeserializeKey_InvalidBooleanKey_ThrowsException() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        
        assertThrows(IOException.class, () -> {
            deserializer.deserializeKey("invalid", ctxt);
        });
    }
    
    @Test
    public void testDeserializeKey_IntegerKey_ReturnsInteger() throws IOException {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        
        Object result = deserializer.deserializeKey("42", ctxt);
        
        assertEquals(42, result);
    }
    
    @Test
    public void testDeserializeKey_NullKeyForEnum_ReturnsNull() throws IOException {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_UUID, UUID.class);
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        
        Object result = deserializer.deserializeKey(null, ctxt);
        
        assertNull(result);
    }
    
    @Test
    public void testDeserializeKey_InvalidEnumKey_ThrowsException() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_UUID, UUID.class);
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        
        assertThrows(IOException.class, () -> {
            deserializer.deserializeKey("invalid", ctxt);
        });
    }
    
    @Test
    public void testDeserializeKey_DateKey_ReturnsDate() throws IOException {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DATE, Date.class);
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        
        Object result = deserializer.deserializeKey("2022-10-15", ctxt);
        
        assertNotNull(result);
    }
    
    @Test
    public void testDeserializeKey_InvalidDateKey_ReturnsNull() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DATE, Date.class);
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        
        Object result = deserializer.deserializeKey("invalid", ctxt);
        
        assertNull(result);
    }

}