import org.junit.Test;
import java.util.UUID;
import java.util.Date;
import java.util.Calendar;
import java.net.URI;
import java.net.URL;
import java.util.Locale;
import java.util.Currency;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {
    
    @Test
    public void testDeserializeKey_string() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer.StringKD(String.class);
        String key = "test";
        Object result = deserializer.deserializeKey(key, null);
        assertEquals(key, result);
    }
    
    @Test
    public void testDeserializeKey_object() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer.StringKD(Object.class);
        String key = "test";
        Object result = deserializer.deserializeKey(key, null);
        assertEquals(key, result);
    }
    
    @Test
    public void testDeserializeKey_uuid() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(EnumResolver.class);
        String key = "123e4567-e89b-12d3-a456-556642440000";
        Object result = deserializer.deserializeKey(key, null);
        UUID expected = UUID.fromString(key);
        assertEquals(expected, result);
    }
    
    @Test
    public void testDeserializeKey_integer() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(EnumResolver.class);
        String key = "123";
        Object result = deserializer.deserializeKey(key, null);
        Integer expected = Integer.parseInt(key);
        assertEquals(expected, result);
    }
    
    @Test
    public void testDeserializeKey_long() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(EnumResolver.class);
        String key = "1234567890";
        Object result = deserializer.deserializeKey(key, null);
        Long expected = Long.parseLong(key);
        assertEquals(expected, result);
    }
    
    @Test
    public void testDeserializeKey_date() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(EnumResolver.class);
        String key = "2018-01-01";
        Object result = deserializer.deserializeKey(key, null);
        Date expected = new SimpleDateFormat("yyyy-MM-dd").parse(key);
        assertEquals(expected, result);
    }
    
    @Test
    public void testDeserializeKey_calendar() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(EnumResolver.class);
        String key = "2018-01-01";
        Object result = deserializer.deserializeKey(key, null);
        Calendar expected = Calendar.getInstance();
        expected.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(key));
        assertEquals(expected, result);
    }
    
    @Test
    public void testDeserializeKey_uri() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(EnumResolver.class);
        String key = "https://www.google.com";
        Object result = deserializer.deserializeKey(key, null);
        URI expected = new URI(key);
        assertEquals(expected, result);
    }
    
    @Test
    public void testDeserializeKey_url() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(EnumResolver.class);
        String key = "https://www.google.com";
        Object result = deserializer.deserializeKey(key, null);
        URL expected = new URL(key);
        assertEquals(expected, result);
    }
    
    @Test
    public void testDeserializeKey_class() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(EnumResolver.class);
        String key = "java.lang.String";
        Object result = deserializer.deserializeKey(key, null);
        Class<?> expected = Class.forName(key);
        assertEquals(expected, result);
    }
    
    @Test
    public void testDeserializeKey_locale() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(EnumResolver.class);
        String key = "en_US";
        Object result = deserializer.deserializeKey(key, null);
        Locale expected = new Locale(key.split("_")[0], key.split("_")[1]);
        assertEquals(expected, result);
    }
    
    @Test
    public void testDeserializeKey_currency() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(EnumResolver.class);
        String key = "USD";
        Object result = deserializer.deserializeKey(key, null);
        Currency expected = Currency.getInstance(key);
        assertEquals(expected, result);
    }
    
}