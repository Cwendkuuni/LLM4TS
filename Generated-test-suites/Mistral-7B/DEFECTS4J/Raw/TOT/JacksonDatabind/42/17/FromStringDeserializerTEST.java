import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FromStringDeserializerTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testFileDeserialization() throws IOException {
        String json = "\"/path/to/file.txt\"";
        FromStringDeserializer<File> deserializer = new Std.StdFile();
        File file = mapper.readValue(json, deserializer.getType());
        assertEquals("/path/to/file.txt", file.getAbsolutePath());
    }

    @Test
    public void testURLDeserialization() throws IOException {
        String json = "\"https://example.com\"";
        FromStringDeserializer<URL> deserializer = new Std.StdURL();
        URL url = mapper.readValue(json, deserializer.getType());
        assertEquals("https://example.com", url.toString());
    }

    @Test
    public void testURIDeserialization() throws IOException {
        String json = "\"https://example.com/path?param=value#fragment\"";
        FromStringDeserializer<URI> deserializer = new Std.StdURI();
        URI uri = mapper.readValue(json, deserializer.getType());
        assertEquals("https", uri.getScheme());
        assertEquals("/path", uri.getPath());
        assertEquals("param=value", uri.getQuery());
        assertEquals("fragment", uri.getFragment());
    }

    @Test
    public void testClassDeserialization() throws IOException, ClassNotFoundException {
        String json = "\"java.lang.String\"";
        FromStringDeserializer<Class<?>> deserializer = new Std.StdClass();
        Class<?> clazz = mapper.readValue(json, deserializer.getType());
        assertEquals(String.class, clazz);
    }

    @Test
    public void testCurrencyDeserialization() throws IOException {
        String json = "\"USD\"";
        FromStringDeserializer<Currency> deserializer = new Std.StdCurrency();
        Currency currency = mapper.readValue(json, deserializer.getType());
        assertEquals(Currency.getInstance("USD"), currency);
    }

    @Test
    public void testLocaleDeserialization() throws IOException {
        String json = "\"en_US\"";
        FromStringDeserializer<Locale> deserializer = new Std.StdLocale();
        Locale locale = mapper.readValue(json, deserializer.getType());
        assertEquals(Locale.US, locale);

        json = "\"de_DE\"_\"Berlin\"";
        locale = mapper.readValue(json, deserializer.getType());
        assertEquals(Locale.GERMANY, locale.getCountry());
        assertEquals(Locale.GERMANY, locale);
    }

    @Test
    public void testCharsetDeserialization() throws IOException {
        String json = "\"UTF-8\"";
        FromStringDeserializer<Charset> deserializer = new Std.StdCharset();
        Charset charset = mapper.readValue(json, deserializer.getType());
        assertEquals(Charset.forName("UTF-8"), charset);
    }

    @Test
    public void testTimeZoneDeserialization() throws IOException {
        String json = "\"America/New_York\"";
        FromStringDeserializer<TimeZone> deserializer = new Std.StdTimeZone();
        TimeZone timeZone = mapper.readValue(json, deserializer.getType());
        assertEquals("America/New_York", timeZone.getID());
    }

    @Test
    public void testInvalidDeserialization() throws IOException {
        String json = "\"invalid\"";
        FromStringDeserializer<Integer> deserializer = new FromStringDeserializer<Integer>(Integer.class);

        try {
            mapper.readValue(json, deserializer.getType());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Cannot deserialize value of type [simple type, class java.lang.Integer] from String \"invalid\": not a valid JSON Number (number value must be an integer or a floating-point number (excluding Hex and Binary literals)", e.getMessage());
        }
    }

    @Test
    public void testEmptyStringDeserialization() throws IOException {
        String json = "\"\"";
        FromStringDeserializer<Locale> deserializer = new FromStringDeserializer<>(Locale.class);
        Locale locale = mapper.readValue(json, deserializer.getType());
        assertNotNull(locale);
    }

    @Test
    public void testEmbeddedObjectDeserialization() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String json = "{\"value\":{\"className\":\"java.net.URL\",\"value\":\"https://example.com\"}}";
        FromStringDeserializer<URL> deserializer = new Std.StdURL();
        URL url = objectMapper.readValue(json, deserializer.getType());
        assertEquals("https://example.com", url.toString());
    }

    @Test
    public void testEmptyArrayDeserialization() throws IOException {
        String json = "[\"\",\"\"]";
        FromStringDeserializer<Object[]> deserializer = new FromStringDeserializer<Object[]>(Object[].class);
        Object[] objects = mapper.readValue(json, deserializer.getType());
        assertNotNull(objects);
        assertEquals(2, objects.length);
        assertEquals(null, objects[0]);
        assertEquals(null, objects[1]);
    }

}