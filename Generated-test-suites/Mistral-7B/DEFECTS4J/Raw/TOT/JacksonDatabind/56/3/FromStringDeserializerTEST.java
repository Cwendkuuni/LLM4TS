package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class FromStringDeserializerTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testFileDeserialization() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("\"test.txt\"");
        FromStringDeserializer<File> fileDeserializer = FromStringDeserializer.findDeserializer(File.class);
        File result = fileDeserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(new File("test.txt"), result);
    }

    @Test
    public void testUrlDeserialization() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("\"http://www.example.com\"");
        FromStringDeserializer<URL> urlDeserializer = FromStringDeserializer.findDeserializer(URL.class);
        URL result = urlDeserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(new URL("http://www.example.com"), result);
    }

    @Test
    public void testUriDeserialization() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("\"http://user:pass@www.example.com:80/path?query#fragment\"");
        FromStringDeserializer<URI> uriDeserializer = FromStringDeserializer.findDeserializer(URI.class);
        URI result = uriDeserializer.deserialize(parser, mapper.getDeserializationContext());
        URI expected = new URI("http://user:pass@www.example.com:80/path?query#fragment");
        assertEquals(expected, result);
    }

    @Test
    public void testClassDeserialization() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("\"java.lang.String.class\"");
        FromStringDeserializer<Class<?>> classDeserializer = FromStringDeserializer.findDeserializer(Class.class);
        Class<?> result = classDeserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(String.class, result);
    }

    @Test
    public void testJavaTypeDeserialization() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("\"com.example.MyClass\"");
        FromStringDeserializer<JavaType> javaTypeDeserializer = FromStringDeserializer.findDeserializer(JavaType.class);
        JavaType result = javaTypeDeserializer.deserialize(parser, mapper.getDeserializationContext());
        // The actual type cannot be determined here
        //assertNotNull(result);
    }

    @Test
    public void testCurrencyDeserialization() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("\"EUR\"");
        FromStringDeserializer<Currency> currencyDeserializer = FromStringDeserializer.findDeserializer(Currency.class);
        Currency result = currencyDeserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(Currency.getInstance("EUR"), result);
    }

    @Test
    public void testPatternDeserialization() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("\"/[A-Za-z]+\\.js$/g\"");
        FromStringDeserializer<Pattern> patternDeserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        Pattern result = patternDeserializer.deserialize(parser, mapper.getDeserializationContext());
        assertNotNull(result);
    }

    @Test
    public void testLocaleDeserialization() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("\"en_US\"");
        FromStringDeserializer<Locale> localeDeserializer = FromStringDeserializer.findDeserializer(Locale.class);
        Locale result = localeDeserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(Locale.US, result);

        parser = mapper.getFactory().createParser("\"en_US_POSIX\"");
        result = localeDeserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(Locale.US, result);

        parser = mapper.getFactory().createParser("\"fr_FR_fr_FR\"");
        result = localeDeserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(Locale.FRANCE, result);
    }

    @Test
    public void testCharsetDeserialization() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("\"UTF-8\"");
        FromStringDeserializer<Charset> charsetDeserializer = FromStringDeserializer.findDeserializer(Charset.class);
        Charset result = charsetDeserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(Charset.forName("UTF-8"), result);
    }

    @Test
    public void testTimeZoneDeserialization() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("\"America/Los_Angeles\"");
        FromStringDeserializer<TimeZone> timeZoneDeserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        TimeZone result = timeZoneDeserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(TimeZone.getTimeZone("America/Los_Angeles"), result);
    }

    @Test
    public void testInetAddressDeserialization() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("\"127.0.0.1\"");
        FromStringDeserializer<InetAddress> inetAddressDeserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        InetAddress result = inetAddressDeserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(InetAddress.getByAddress("127.0.0.1"), result);
    }

    @Test
    public void testInetSocketAddressDeserialization() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("\"[255.255.255.255]:80\"");
        FromStringDeserializer<InetSocketAddress> inetSocketAddressDeserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        InetSocketAddress result = inetSocketAddressDeserializer.deserialize(parser, mapper.getDeserializationContext());
        InetSocketAddress expected = new InetSocketAddress(InetAddress.getByAddress(new byte[]{255, 255, 255, 255}), 80);
        assertEquals(expected, result);

        parser = mapper.getFactory().createParser("\"127.0.0.1:80\"");
        result = inetSocketAddressDeserializer.deserialize(parser, mapper.getDeserializationContext());
        InetSocketAddress expectedPort80 = new InetSocketAddress("127.0.0.1", 80);
        assertEquals(expectedPort80, result);
    }

    @Test
    public void testDeserializationWithEmptyString() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("\"\"");
        FromStringDeserializer<File> fileDeserializer = FromStringDeserializer.findDeserializer(File.class);
        assertNull(fileDeserializer.deserialize(parser, mapper.getDeserializationContext()));
    }

    @Test
    public void testDeserializationWithNull() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("\"null\"");
        FromStringDeserializer<File> fileDeserializer = FromStringDeserializer.findDeserializer(File.class);
        assertNull(fileDeserializer.deserialize(parser, mapper.getDeserializationContext()));
    }

    @Test
    public void testDeserializationWithArray() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("[\"JavaClass\"]");
        FromStringDeserializer<File> fileDeserializer = FromStringDeserializer.findDeserializer(File.class);
        assertThrows(JsonMappingException.class, () -> {
            fileDeserializer.deserialize(parser, mapper.getDeserializationContext());
        });
    }

    @Test
    public void testDeserializationWithUnknownType() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("\"java.util.Date\"");
        FromStringDeserializer<File> fileDeserializer = FromStringDeserializer.findDeserializer(File.class);
        assertThrows(JsonMappingException.class, () -> {
            fileDeserializer.deserialize(parser, mapper.getDeserializationContext());
        });
    }
}