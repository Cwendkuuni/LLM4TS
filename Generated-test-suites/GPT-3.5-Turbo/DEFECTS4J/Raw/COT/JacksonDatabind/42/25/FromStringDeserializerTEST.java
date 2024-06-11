
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class FromStringDeserializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testDeserialize_File() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, 1);
        String fileName = "testfile.txt";
        File file = (File) deserializer._deserialize(fileName, DeserializationContext.builder().build());
        assertEquals(fileName, file.getName());
    }

    @Test
    public void testDeserialize_URL() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, 2);
        String urlStr = "http://www.example.com";
        URL url = (URL) deserializer._deserialize(urlStr, DeserializationContext.builder().build());
        assertEquals(urlStr, url.toString());
    }

    @Test
    public void testDeserialize_URI() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, 3);
        String uriStr = "http://www.example.com";
        URI uri = (URI) deserializer._deserialize(uriStr, DeserializationContext.builder().build());
        assertEquals(uriStr, uri.toString());
    }

    @Test
    public void testDeserialize_Class() throws IOException, ClassNotFoundException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, 4);
        String className = "java.lang.String";
        Class<?> clazz = (Class<?>) deserializer._deserialize(className, DeserializationContext.builder().build());
        assertEquals(className, clazz.getName());
    }

    @Test
    public void testDeserialize_JavaType() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(JavaType.class, 5);
        String javaTypeStr = "java.util.Set";
        JavaType javaType = (JavaType) deserializer._deserialize(javaTypeStr, DeserializationContext.builder().build());
        assertEquals(javaTypeStr, javaType.getRawClass().getName());
    }

    @Test
    public void testDeserialize_Currency() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, 6);
        String currencyCode = "USD";
        Currency currency = (Currency) deserializer._deserialize(currencyCode, DeserializationContext.builder().build());
        assertEquals(currencyCode, currency.getCurrencyCode());
    }

    @Test
    public void testDeserialize_Pattern() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, 7);
        String regex = "\\d+";
        Pattern pattern = (Pattern) deserializer._deserialize(regex, DeserializationContext.builder().build());
        assertTrue(pattern.matcher("123").matches());
    }

    @Test
    public void testDeserialize_Locale() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, 8);
        String localeStr = "en_US";
        Locale locale = (Locale) deserializer._deserialize(localeStr, DeserializationContext.builder().build());
        assertEquals(localeStr, locale.toString());
    }

    @Test
    public void testDeserialize_Charset() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Charset.class, 9);
        String charsetName = "UTF-8";
        Charset charset = (Charset) deserializer._deserialize(charsetName, DeserializationContext.builder().build());
        assertEquals(charsetName, charset.name());
    }

    @Test
    public void testDeserialize_TimeZone() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, 10);
        String timeZoneId = "UTC";
        TimeZone timeZone = (TimeZone) deserializer._deserialize(timeZoneId, DeserializationContext.builder().build());
        assertEquals(timeZoneId, timeZone.getID());
    }

    @Test
    public void testDeserialize_InetAddress() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, 11);
        String ipAddress = "127.0.0.1";
        InetAddress address = (InetAddress) deserializer._deserialize(ipAddress, DeserializationContext.builder().build());
        assertEquals(ipAddress, address.getHostAddress());
    }

    @Test
    public void testDeserialize_InetSocketAddress() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, 12);
        String socketAddressStr = "localhost:8080";
        InetSocketAddress socketAddress = (InetSocketAddress) deserializer._deserialize(socketAddressStr, DeserializationContext.builder().build());
        assertEquals("localhost", socketAddress.getHostString());
        assertEquals(8080, socketAddress.getPort());
    }

    @Test
    public void testDeserialize_EmbeddedObject_Null() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Object.class, 0);

        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.putNull("obj");
        String json = node.toString();

        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jp = jsonFactory.createParser(json);

        Object obj = deserializer.deserialize(jp, DeserializationContext.builder().build());
        assertNull(obj);
    }
    
    @Test(expected = JsonMappingException.class)
    public void testDeserialize_EmbeddedObject_UnsupportedType() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Object.class, 0);

        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("obj", 123);
        String json = node.toString();

        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jp = jsonFactory.createParser(json);

        deserializer.deserialize(jp, DeserializationContext.builder().build());
    }

    @Test
    public void testDeserialize_EmbeddedObject_WithType() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(String.class, 0);

        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("obj", "test");
        String json = node.toString();

        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jp = jsonFactory.createParser(json);

        String obj = (String) deserializer.deserialize(jp, DeserializationContext.builder().build());
        assertEquals("test", obj);
    }
}

