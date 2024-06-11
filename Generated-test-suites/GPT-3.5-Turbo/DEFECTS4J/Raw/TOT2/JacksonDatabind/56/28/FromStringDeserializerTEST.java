import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.DeserializationContext;

public class FromStringDeserializerTest {

    @Test
    public void testTypes() {
        Class<?>[] expectedTypes = {
            File.class,
            URL.class,
            URI.class,
            Class.class,
            JavaType.class,
            Currency.class,
            Pattern.class,
            Locale.class,
            Charset.class,
            TimeZone.class,
            InetAddress.class,
            InetSocketAddress.class
        };
        
        assertArrayEquals(expectedTypes, FromStringDeserializer.types());
    }

    @Test
    public void testFindDeserializer() {
        Std deserializer = FromStringDeserializer.findDeserializer(URL.class);
        assertNotNull(deserializer);
        assertEquals(Std.STD_URL, deserializer._kind);
    }

    @Test
    public void testDeserialize() throws IOException {
        FromStringDeserializer.Std urlDeserializer = new FromStringDeserializer.Std(URL.class, Std.STD_URL);
        DeserializationContext ctxt = new DeserializationContext(null);
        
        URL url = new URL("https://www.example.com");
        assertEquals(url, urlDeserializer.deserialize(new JsonParser(), ctxt));
    }

    @Test
    public void testDeserializeInvalidUrl() {
        FromStringDeserializer.Std urlDeserializer = new FromStringDeserializer.Std(URL.class, Std.STD_URL);
        DeserializationContext ctxt = new DeserializationContext(null);
        
        try {
            urlDeserializer.deserialize(new JsonParser(), ctxt);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testDesrializeWithEmptyString() throws IOException {
        FromStringDeserializer.Std uriDeserializer = new FromStringDeserializer.Std(URI.class, Std.STD_URI);
        DeserializationContext ctxt = new DeserializationContext(null);
        
        assertEquals(URI.create(""), uriDeserializer.deserialize(new JsonParser(), ctxt));
    }

    @Test
    public void testDeserializeEmbedded() {
        FromStringDeserializer.Std classDeserializer = new FromStringDeserializer.Std(Class.class, Std.STD_CLASS);
        DeserializationContext ctxt = new DeserializationContext(null);
        
        try {
            classDeserializer.deserializeEmbedded(new Object(), ctxt);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testDeserializeFromEmptyString() throws IOException {
        FromStringDeserializer.Std localeDeserializer = new FromStringDeserializer.Std(Locale.class, Std.STD_LOCALE);
        DeserializationContext ctxt = new DeserializationContext(null);
        
        assertEquals(Locale.ROOT, localeDeserializer._deserializeFromEmptyString());
    }

}