import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.util.Locale;

public class FromStringDeserializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testDeserialize_File() throws IOException {
        String json = "\"/path/to/file.txt\"";
        File expected = new File("/path/to/file.txt");
        FromStringDeserializer<File> fileDeserializer = new FromStringDeserializer<File>(File.class);
        File actual = fileDeserializer.deserialize(objectMapper.getFactory().createParser(json), objectMapper.getDeserializationContext());
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDeserialize_URL() throws IOException {
        String json = "\"http://example.com\"";
        URL expected = new URL("http://example.com");
        FromStringDeserializer<URL> urlDeserializer = new FromStringDeserializer<URL>(URL.class);
        URL actual = urlDeserializer.deserialize(objectMapper.getFactory().createParser(json), objectMapper.getDeserializationContext());
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDeserialize_URI() throws IOException {
        String json = "\"urn:example:com\"";
        URI expected = new URI("urn:example:com");
        FromStringDeserializer<URI> uriDeserializer = new FromStringDeserializer<URI>(URI.class);
        URI actual = uriDeserializer.deserialize(objectMapper.getFactory().createParser(json), objectMapper.getDeserializationContext());
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDeserialize_Class() throws IOException {
        String json = "\"java.lang.String\"";
        Class<?> expected = String.class;
        FromStringDeserializer<Class<?>> classDeserializer = new FromStringDeserializer<Class<?>>(Class.class);
        Class<?> actual = classDeserializer.deserialize(objectMapper.getFactory().createParser(json), objectMapper.getDeserializationContext());
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDeserialize_JavaType() throws IOException {
        String json = "\"com.example.MyClass\"";
        JavaType expected = objectMapper.getTypeFactory().constructType(MyClass.class);
        FromStringDeserializer<JavaType> javaTypeDeserializer = new FromStringDeserializer<JavaType>(JavaType.class);
        JavaType actual = javaTypeDeserializer.deserialize(objectMapper.getFactory().createParser(json), objectMapper.getDeserializationContext());
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDeserialize_Currency() throws IOException {
        String json = "\"EUR\"";
        Currency expected = Currency.getInstance("EUR");
        FromStringDeserializer<Currency> currencyDeserializer = new FromStringDeserializer<Currency>(Currency.class);
        Currency actual = currencyDeserializer.deserialize(objectMapper.getFactory().createParser(json), objectMapper.getDeserializationContext());
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDeserialize_Pattern() throws IOException {
        String json = "\"/[\\w\\d]+\\.txt$\"";
        Pattern expected = Pattern.compile("/[\\w\\d]+\\.txt$");
        FromStringDeserializer<Pattern> patternDeserializer = new FromStringDeserializer<Pattern>(Pattern.class);
        Pattern actual = patternDeserializer.deserialize(objectMapper.getFactory().createParser(json), objectMapper.getDeserializationContext());
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDeserialize_Locale() throws IOException {
        String json = "\"en_US\"";
        Locale expected = new Locale("en", "US");
        FromStringDeserializer<Locale> localeDeserializer = new FromStringDeserializer<Locale>(Locale.class);
        Locale actual = localeDeserializer.deserialize(objectMapper.getFactory().createParser(json), objectMapper.getDeserializationContext());
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDeserialize_Charset() throws IOException {
        String json = "\"UTF-8\"";
        Charset expected = Charset.forName("UTF-8");
        FromStringDeserializer<Charset> charsetDeserializer = new FromStringDeserializer<Charset>(Charset.class);
        Charset actual = charsetDeserializer.deserialize(objectMapper.getFactory().createParser(json), objectMapper.getDeserializationContext());
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDeserialize_TimeZone() throws IOException {
        String json = "\"America/Los_Angeles\"";
        TimeZone expected = TimeZone.getTimeZone("America/Los_Angeles");
        FromStringDeserializer<TimeZone> timeZoneDeserializer = new FromStringDeserializer<TimeZone>(TimeZone.class);
        TimeZone actual = timeZoneDeserializer.deserialize(objectMapper.getFactory().createParser(json), objectMapper.getDeserializationContext());
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDeserialize_InetAddress() throws IOException {
        String json = "\"192.168.1.1\"";
        InetAddress expected = InetAddress.getByName("192.168.1.1");
        FromStringDeserializer<InetAddress> inetAddressDeserializer = new FromStringDeserializer<InetAddress>(InetAddress.class);
        InetAddress actual = inetAddressDeserializer.deserialize(objectMapper.getFactory().createParser(json), objectMapper.getDeserializationContext());
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDeserialize_InetSocketAddress() throws IOException {
        String json = "\"[255.255.255.255]:80\"";
        InetSocketAddress expected = new InetSocketAddress("255.255.255.255", 80);
        FromStringDeserializer<InetSocketAddress> inetSocketAddressDeserializer = new FromStringDeserializer<InetSocketAddress>(InetSocketAddress.class);
        InetSocketAddress actual = inetSocketAddressDeserializer.deserialize(objectMapper.getFactory().createParser(json), objectMapper.getDeserializationContext());
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDeserialize_StringBuilder() throws IOException {
        String json = "\"My StringBuilder\"";
        StringBuilder expected = new StringBuilder("My StringBuilder");
        FromStringDeserializer<StringBuilder> stringBuilderDeserializer = new FromStringDeserializer<StringBuilder>(StringBuilder.class);
        StringBuilder actual = stringBuilderDeserializer.deserialize(objectMapper.getFactory().createParser(json), objectMapper.getDeserializationContext());
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserialize_EmptyString() throws IOException {
        String json = "\"\"";
        FromStringDeserializer<String> stringDeserializer = new FromStringDeserializer<String>(String.class);
        stringDeserializer.deserialize(objectMapper.getFactory().createParser(json), objectMapper.getDeserializationContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserialize_MalformedURL() throws IOException {
        String json = "\"malformed:url\"";
        FromStringDeserializer<URL> urlDeserializer = new FromStringDeserializer<URL>(URL.class);
        urlDeserializer.deserialize(objectMapper.getFactory().createParser(json), objectMapper.getDeserializationContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserialize_UnknownType() throws IOException {
        String json = "\"MyUnknownType\"";
        FromStringDeserializer<Object> objectDeserializer = new FromStringDeserializer<Object>(Object.class);
        objectDeserializer.deserialize(objectMapper.getFactory().createParser(json), objectMapper.getDeserializationContext());
    }
}