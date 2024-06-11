import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

public class FromStringDeserializerTest {

    @Test
    public void testTypes() throws IOException {
        assertArrayEquals(new Class<?>[] {
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
        }, FromStringDeserializer.types());
    }

    @Test
    public void testFindDeserializer() throws IOException {
        Class<?> clazz = String.class;
        Std deserializer = FromStringDeserializer.findDeserializer(clazz);
        assertNull(deserializer);

        clazz = File.class;
        deserializer = FromStringDeserializer.findDeserializer(clazz);
        assertEquals(Std.STD_FILE, deserializer.kind);

        clazz = URL.class;
        deserializer = FromStringDeserializer.findDeserializer(clazz);
        assertEquals(Std.STD_URL, deserializer.kind);

        clazz = URI.class;
        deserializer = FromStringDeserializer.findDeserializer(clazz);
        assertEquals(Std.STD_URI, deserializer.kind);

        clazz = Class.class;
        deserializer = FromStringDeserializer.findDeserializer(clazz);
        assertEquals(Std.STD_CLASS, deserializer.kind);

        clazz = JavaType.class;
        deserializer = FromStringDeserializer.findDeserializer(clazz);
        assertEquals(Std.STD_JAVA_TYPE, deserializer.kind);

        clazz = Currency.class;
        deserializer = FromStringDeserializer.findDeserializer(clazz);
        assertEquals(Std.STD_CURRENCY, deserializer.kind);

        clazz = Pattern.class;
        deserializer = FromStringDeserializer.findDeserializer(clazz);
        assertEquals(Std.STD_PATTERN, deserializer.kind);

        clazz = Locale.class;
        deserializer = FromStringDeserializer.findDeserializer(clazz);
        assertEquals(Std.STD_LOCALE, deserializer.kind);

        clazz = Charset.class;
        deserializer = FromStringDeserializer.findDeserializer(clazz);
        assertEquals(Std.STD_CHARSET, deserializer.kind);

        clazz = TimeZone.class;
        deserializer = FromStringDeserializer.findDeserializer(clazz);
        assertEquals(Std.STD_TIME_ZONE, deserializer.kind);

        clazz = InetAddress.class;
        deserializer = FromStringDeserializer.findDeserializer(clazz);
        assertEquals(Std.STD_INET_ADDRESS, deserializer.kind);

        clazz = InetSocketAddress.class;
        deserializer = FromStringDeserializer.findDeserializer(clazz);
        assertEquals(Std.STD_INET_SOCKET_ADDRESS, deserializer.kind);
    }

    @Test
    public void testDeserialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.getParser();
        DeserializationContext ctxt = mapper.getDeserializationContext();

        parser.setValue("file.txt", JsonToken.VALUE_STRING);
        FromStringDeserializer<File> fileDeserializer = new FileFromStringDeserializer();
        File file = fileDeserializer.deserialize(parser, ctxt);
        assertNotNull(file);
        assertEquals("file.txt", file.getName());

        parser.setValue("http://example.com", JsonToken.VALUE_STRING);
        FromStringDeserializer<URL> urlDeserializer = new URLOrStringDeserializer();
        URL url = urlDeserializer.deserialize(parser, ctxt);
        assertNotNull(url);
        assertEquals("http://example.com", url.toString());

        parser.setValue("URI[scheme=http,authority=example.com]", JsonToken.VALUE_STRING);
        FromStringDeserializer<URI> uriDeserializer = new URIOrStringDeserializer();
        URI uri = uriDeserializer.deserialize(parser, ctxt);
        assertNotNull(uri);
        assertEquals("http", uri.getScheme());
        assertEquals("example.com", uri.getAuthority());

        parser.setValue("java.lang.String", JsonToken.VALUE_STRING);
        FromStringDeserializer<String> stringDeserializer = new StandardDeserializer();
        String string = stringDeserializer.deserialize(parser, ctxt);
        assertNotNull(string);
        assertEquals("java.lang.String", string.getClass().getName());

        parser.setValue("java.util.Currency[symbol=USD]", JsonToken.VALUE_STRING);
        FromStringDeserializer<Currency> currencyDeserializer = new CurrencyDeserializer();
        Currency currency = currencyDeserializer.deserialize(parser, ctxt);
        assertNotNull(currency);
        assertEquals("USD", currency.getSymbol());

        parser.setValue("java.util.regex.Pattern[pattern=\\w+]", JsonToken.VALUE_STRING);
        FromStringDeserializer<Pattern> patternDeserializer = new PatternDeserializer();
        Pattern pattern = patternDeserializer.deserialize(parser, ctxt);
        assertNotNull(pattern);
        assertEquals("\\w+", pattern.pattern());

        parser.setValue("en_US", JsonToken.VALUE_STRING);
        FromStringDeserializer<Locale> localeDeserializer = new LocaleDeserializer();
        Locale locale = localeDeserializer.deserialize(parser, ctxt);
        assertNotNull(locale);
        assertEquals("en_US", locale.toString());

        parser.setValue("US-ASCII", JsonToken.VALUE_STRING);
        FromStringDeserializer<Charset> charsetDeserializer = new CharsetDeserializer();
        Charset charset = charsetDeserializer.deserialize(parser, ctxt);
        assertNotNull(charset);
        assertEquals("US-ASCII", charset.name());

        parser.setValue("America/Los_Angeles", JsonToken.VALUE_STRING);
        FromStringDeserializer<TimeZone> timeZoneDeserializer = new TimeZoneDeserializer();
        TimeZone timeZone = timeZoneDeserializer.deserialize(parser, ctxt);
        assertNotNull(timeZone);
        assertEquals("America/Los_Angeles", timeZone.getID());

        parser.setValue("192.168.0.1", JsonToken.VALUE_STRING);
        FromStringDeserializer<InetAddress> inetAddressDeserializer = new InetAddressDeserializer();
        InetAddress inetAddress = inetAddressDeserializer.deserialize(parser, ctxt);
        assertNotNull(inetAddress);

        parser.setValue("[192.168.0.1, 80]", JsonToken.VALUE_STRING);
        FromStringDeserializer<InetSocketAddress> inetSocketAddressDeserializer = new InetSocketAddressDeserializer();
        InetSocketAddress inetSocketAddress = inetSocketAddressDeserializer.deserialize(parser, ctxt);
        assertNotNull(inetSocketAddress);
        assertEquals("192.168.0.1", inetSocketAddress.getAddress().getHostAddress());
        assertEquals(80, inetSocketAddress.getPort());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeEmptyString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.getParser();
        DeserializationContext ctxt = mapper.getDeserializationContext();

        parser.setValue("", JsonToken.VALUE_STRING);
        FromStringDeserializer<String> stringDeserializer = new StandardDeserializer();
        String string = stringDeserializer.deserialize(parser, ctxt);
        fail("Expected JsonMappingException");
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidData() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.getParser();
        DeserializationContext ctxt = mapper.getDeserializationContext();

        parser.setValue("[1, 2]", JsonToken.VALUE_STRING);
        FromStringDeserializer<String> stringDeserializer = new StandardDeserializer();
        String string = stringDeserializer.deserialize(parser, ctxt);
        fail("Expected JsonMappingException");
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidDataForClass() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.getParser();
        DeserializationContext ctxt = mapper.getDeserializationContext();

        parser.setValue("[1, 2]", JsonToken.VALUE_STRING);
        FromStringDeserializer<File> fileDeserializer = new FileFromStringDeserializer();
        fileDeserializer.deserialize(parser, ctxt);
        fail("Expected JsonMappingException");
    }

    @Test
    public void testDeserializeEmbeddedObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.getParser();
        DeserializationContext ctxt = mapper.getDeserializationContext();

        parser.setValue("{\"value\": \"java.util.Locale[lang=en,country=US]\", \"anotherValue\": 1}", JsonToken.START_OBJECT);
        FromStringDeserializer<Locale> localeDeserializer = new LocaleDeserializer();
        Locale locale = localeDeserializer.deserializeEmbedded(parser, ctxt);
        assertNotNull(locale);
        assertEquals("en_US", locale.toString());

        parser.setValue("{\"value\": \"java.util.Currency[symbol=USD]\", \"anotherValue\": 1}", JsonToken.START_OBJECT);
        FromStringDeserializer<Currency> currencyDeserializer = new CurrencyDeserializer();
        Currency currency = currencyDeserializer.deserializeEmbedded(parser, ctxt);
        assertNotNull(currency);
        assertEquals("USD", currency.getSymbol());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeEmbeddedInvalidData() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.getParser();
        DeserializationContext ctxt = mapper.getDeserializationContext();

        parser.setValue("{\"value\": \"java.util.Locale[lang=en_US,country=US]\", \"anotherValue\": 1}", JsonToken.START_OBJECT);
        FromStringDeserializer<Locale> localeDeserializer = new LocaleDeserializer();
        localeDeserializer.deserializeEmbedded(parser, ctxt);
        fail("Expected JsonMappingException");
    }
}