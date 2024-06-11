import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.util.Currency;
import java.util.Locale;

public class FromStringDeserializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testDeserializeFile() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(File.class);
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"test.txt\"");
        assertInstanceOf(File.class, deserializer.deserialize(jsonParser, context()));
    }

    @Test
    public void testDeserializeUrl() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URL.class);
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"http://www.example.com\"");
        assertInstanceOf(URL.class, deserializer.deserialize(jsonParser, context()));
    }

    @Test
    public void testDeserializeUri() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URI.class);
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"http://www.example.com/path/to/file\"");
        assertInstanceOf(URI.class, deserializer.deserialize(jsonParser, context()));
    }

    @Test
    public void testDeserializeClass() throws IOException, ClassNotFoundException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class);
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"java.lang.String\"");
        assertEquals(String.class, deserializer.deserialize(jsonParser, context()).getClass());
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(int[].class);
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"class [I\"");
        assertArrayEquals(new int[]{1, 2, 3}, deserializer.deserialize(jsonParser, context()));
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"USD\"");
        assertEquals(Currency.getInstance("USD"), deserializer.deserialize(jsonParser, context()));
    }

    @Test
    public void testDeserializePattern() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"\\w+@\\w+\\.\\w+\"");
        assertInstanceOf(Pattern.class, deserializer.deserialize(jsonParser, context()));
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"en_US\"");
        assertEquals(Locale.US, deserializer.deserialize(jsonParser, context()));
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Charset.class);
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"UTF-8\"");
        assertEquals(Charset.forName("UTF-8"), deserializer.deserialize(jsonParser, context()));
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"America/Los_Angeles\"");
        assertEquals(TimeZone.getTimeZone("America/Los_Angeles"), deserializer.deserialize(jsonParser, context()));
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"192.168.1.1\"");
        assertInstanceOf(InetAddress.class, deserializer.deserialize(jsonParser, context()));
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"[192.168.1.1]:80\"");
        assertInstanceOf(InetSocketAddress.class, deserializer.deserialize(jsonParser, context()));
        
        jsonParser = objectMapper.getFactory().createParser("\"[::1]:8080\"");
        assertInstanceOf(InetSocketAddress.class, deserializer.deserialize(jsonParser, context()));
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidFile() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(File.class);
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"test\"");
        deserializer.deserialize(jsonParser, context());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidUrl() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URL.class);
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"http\"");
        deserializer.deserialize(jsonParser, context());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidUri() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URI.class);
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"http\"");
        deserializer.deserialize(jsonParser, context());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidClass() throws IOException, ClassNotFoundException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class);
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"java.lang.Object\"");
        deserializer.deserialize(jsonParser, context());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidJavaType() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(int[].class);
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"[I\"");
        deserializer.deserialize(jsonParser, context());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidCurrency() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"EUR\"");
        deserializer.deserialize(jsonParser, context());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidPattern() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"invalid\"");
        deserializer.deserialize(jsonParser, context());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidLocale() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"en_US_Invalid\"");
        deserializer.deserialize(jsonParser, context());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidCharset() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Charset.class);
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"UTF-8_Invalid\"");
        deserializer.deserialize(jsonParser, context());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidTimeZone() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"InvalidTimeZone\"");
        deserializer.deserialize(jsonParser, context());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidInetAddress() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"invalid_address\"");
        deserializer.deserialize(jsonParser, context());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidInetSocketAddress() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"[invalid_address]:80\"");
        deserializer.deserialize(jsonParser, context());
    }

    @Test
    public void testDeserializeEmbeddedObject() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class);
        String json = "{\"value\": \"test\"}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        Object embeddedObject = jsonParser.readValueAsTree().get("value");
        assertInstanceOf(String.class, deserializer.deserializeEmbedded(embeddedObject, context()));
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidEmbeddedObject() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class);
        String json = "{\"value\": 123}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        Object embeddedObject = jsonParser.readValueAsTree().get("value");
        deserializer.deserializeEmbedded(embeddedObject, context());
    }

    private DeserializationContext context() {
        return objectMapper.getDeserializationContext();
    }

    private void assertArrayEquals(int[] expected, Object actual) {
        if (expected.length != ((Object)actual).length) {
            throw new AssertionError("Arrays have different lengths");
        }
        for (int i = 0; i < expected.length; i++) {
            if (expected[i] != ((Object)actual)[i]) {
                throw new AssertionError("Unexpected array element: expected [" + expected[i] + "], actual [" + ((Object)actual)[i] + "]");
            }
        }
    }

    private void assertInstanceOf(Class<?> expectedType, Object actual) {
        if (!expectedType.isInstance(actual)) {
            throw new AssertionError("Expected instance of " + expectedType.getName() + ", but was instance of " + actual.getClass().getName());
        }
    }
}