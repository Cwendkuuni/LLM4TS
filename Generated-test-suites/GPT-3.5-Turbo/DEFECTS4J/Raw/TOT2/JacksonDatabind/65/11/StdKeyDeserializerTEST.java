import static org.junit.Assert.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey() throws IOException {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        
        DeserializationContext ctxt = null;
        String key = "123";
        Object result = deserializer.deserializeKey(key, ctxt);
        
        assertEquals(Integer.valueOf(123), result);
    }
    
    @Test
    public void testDeserializeKeyNull() throws IOException {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        
        DeserializationContext ctxt = null;
        String key = null;
        Object result = deserializer.deserializeKey(key, ctxt);
        
        assertNull(result);
    }
    
    @Test
    public void testDeserializeKeyInvalidBoolean() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        
        DeserializationContext ctxt = null;
        String key = "invalid";
        
        assertThrows(IOException.class, () -> deserializer.deserializeKey(key, ctxt));
    }
    
    @Test
    public void testDeserializeKeyMalformedURL() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class);
        
        DeserializationContext ctxt = null;
        String key = "invalid url";
        
        assertThrows(MalformedURLException.class, () -> deserializer.deserializeKey(key, ctxt));
    }
    
    @Test
    public void testDeserializeKeyUUID() throws IOException {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_UUID, UUID.class);
        
        DeserializationContext ctxt = null;
        String key = "123e4567-e89b-12d3-a456-426614174000";
        Object result = deserializer.deserializeKey(key, ctxt);
        
        assertEquals(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), result);
    }
    
    @Test
    public void testDeserializeKeyLocaleInvalid() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class);
        
        DeserializationContext ctxt = null;
        String key = "invalid";
        
        assertThrows(IOException.class, () -> deserializer.deserializeKey(key, ctxt));
    }
    
}