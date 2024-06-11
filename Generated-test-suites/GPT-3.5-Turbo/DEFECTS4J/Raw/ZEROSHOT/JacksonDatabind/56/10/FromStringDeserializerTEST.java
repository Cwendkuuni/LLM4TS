import static org.junit.Assert.*;

import java.io.File;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;

public class FromStringDeserializerTest {
    
    private FromStringDeserializer<Object> deserializer;
    private JsonParser jsonParser;
    private DeserializationContext deserializationContext;

    @Before
    public void setup() throws Exception {
        deserializer = new FromStringDeserializer.Std(Object.class, 0);
        jsonParser = new JsonFactory().createParser("");
        deserializationContext = new DeserializationContext(null, null, null);
    }
    
    @Test
    public void testDeserialize_file() throws Exception {
        File result = (File) deserializer.deserialize(jsonParser, deserializationContext);
        assertNull(result);
    }
    
    @Test
    public void testDeserialize_url() throws Exception {
        URL result = (URL) deserializer.deserialize(jsonParser, deserializationContext);
        assertNull(result);
    }
    
    @Test
    public void testDeserialize_uri() throws Exception {
        URI result = (URI) deserializer.deserialize(jsonParser, deserializationContext);
        assertNull(result);
    }
    
    @Test
    public void testDeserialize_class() throws Exception {
        Class<?> result = (Class<?>) deserializer.deserialize(jsonParser, deserializationContext);
        assertNull(result);
    }
    
    @Test
    public void testDeserialize_javaType() throws Exception {
        JavaType result = (JavaType) deserializer.deserialize(jsonParser, deserializationContext);
        assertNull(result);
    }
    
    @Test
    public void testDeserialize_currency() throws Exception {
        Currency result = (Currency) deserializer.deserialize(jsonParser, deserializationContext);
        assertNull(result);
    }
    
    @Test
    public void testDeserialize_pattern() throws Exception {
        Pattern result = (Pattern) deserializer.deserialize(jsonParser, deserializationContext);
        assertNull(result);
    }
    
    @Test
    public void testDeserialize_locale() throws Exception {
        Locale result = (Locale) deserializer.deserialize(jsonParser, deserializationContext);
        assertNull(result);
    }
    
    @Test
    public void testDeserialize_charset() throws Exception {
        Charset result = (Charset) deserializer.deserialize(jsonParser, deserializationContext);
        assertNull(result);
    }
    
    @Test
    public void testDeserialize_timeZone() throws Exception {
        TimeZone result = (TimeZone) deserializer.deserialize(jsonParser, deserializationContext);
        assertNull(result);
    }
    
    @Test
    public void testDeserialize_inetAddress() throws Exception {
        InetAddress result = (InetAddress) deserializer.deserialize(jsonParser, deserializationContext);
        assertNull(result);
    }
    
    @Test
    public void testDeserialize_inetSocketAddress() throws Exception {
        InetSocketAddress result = (InetSocketAddress) deserializer.deserialize(jsonParser, deserializationContext);
        assertNull(result);
    }
    
    @Test(expected=JsonMappingException.class)
    public void testDeserialize_invalidValue() throws Exception {
        deserializer.deserialize(jsonParser, deserializationContext);
    }

}