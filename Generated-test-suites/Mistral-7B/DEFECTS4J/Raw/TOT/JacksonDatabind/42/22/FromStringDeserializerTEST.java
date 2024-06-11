package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.deser.std.Std;

import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

public class FromStringDeserializerTest {

    @org.junit.Test
    public void testDeserializeFile() throws IOException {
        FromStringDeserializer<File> serializer = new FromStringDeserializer<File>(File.class);
        String json = "\"C:/test.txt\"";
        File file = serializer.deserialize(new JsonFactory(), JsonMappingContext.builder().build(), json);
        assertNotNull(file);
        assertEquals("C:/test.txt", file.getAbsolutePath());
    }

    @org.junit.Test
    public void testDeserializeUrl() throws IOException {
        FromStringDeserializer<URL> serializer = new FromStringDeserializer<URL>(URL.class);
        String json = "\"https://www.example.com\"";
        URL url = serializer.deserialize(new JsonFactory(), JsonMappingContext.builder().build(), json);
        assertNotNull(url);
        assertEquals("https://www.example.com", url.toString());
    }

    @org.junit.Test
    public void testDeserializeUri() throws IOException {
        FromStringDeserializer<URI> serializer = new FromStringDeserializer<URI>(URI.class);
        String json = "\"http://www.example.com/path?query=value\"";
        URI uri = serializer.deserialize(new JsonFactory(), JsonMappingContext.builder().build(), json);
        assertNotNull(uri);
        assertEquals("http://www.example.com/path?query=value", uri.toString());
    }

    @org.junit.Test
    public void testDeserializeClass() throws IOException {
        FromStringDeserializer<String> stringSerializer = new FromStringDeserializer<String>(String.class);
        FromStringDeserializer<Integer> integerSerializer = new FromStringDeserializer<Integer>(Integer.class);

        String jsonString = "\"java.lang.String\"";
        String string = stringSerializer.deserialize(new JsonFactory(), JsonMappingContext.builder().build(), jsonString);
        assertEquals("java.lang.String", string.getClass().getName());

        String jsonInt = "42";
        Integer integer = integerSerializer.deserialize(new JsonFactory(), JsonMappingContext.builder().build(), jsonInt);
        assertEquals(42, integer.intValue());
    }

    @org.junit.Test
    public void testDeserializeJavaType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        FromStringDeserializer<JavaType> serializer = FromStringDeserializer.findDeserializer(JavaType.class);
        String json = "com.example.MyClass";
        JavaType javaType = serializer.deserialize(new JsonFactory(), JsonMappingContext.builder().build(), json);
        assertEquals(JavaType.constructType(ClassUtil.loadClass(json)), javaType);
    }

    @org.junit.Test
    public void testDeserializeCurrency() throws IOException {
        FromStringDeserializer<Currency> serializer = new FromStringDeserializer<Currency>(Currency.class);
        String json = "\"USD\"";
        Currency currency = serializer.deserialize(new JsonFactory(), JsonMappingContext.builder().build(), json);
        assertEquals(Currency.getInstance("USD"), currency);
    }

    @org.junit.Test
    public void testDeserializePattern() throws IOException {
        FromStringDeserializer<Pattern> serializer = new FromStringDeserializer<Pattern>(Pattern.class);
        String json = "/[a-z]+/i";
        Pattern pattern = serializer.deserialize(new JsonFactory(), JsonMappingContext.builder().build(), json);
        assertNotNull(pattern);
        assertEquals("(?i)[a-z]+", pattern.pattern());
    }

    @org.junit.Test
    public void testDeserializeLocale() throws IOException {
        FromStringDeserializer<Locale> serializer = new FromStringDeserializer<Locale>(Locale.class);

        String jsonSingleLocale = "\"en_US\"";
        Locale localeSingle = serializer.deserialize(new JsonFactory(), JsonMappingContext.builder().build(), jsonSingleLocale);
        assertEquals("en_US", localeSingle.toString());

        String jsonTwoLocales = "\"en_US_POSIX\",\"fr_FR\"";
        Locale[] locales = (Locale[]) serializer.deserialize(new JsonFactory(), JsonMappingContext.builder().build(), jsonTwoLocales);
        assertArrayEquals(new Locale[]{"en_US_POSIX", "fr_FR"}, locales);
    }

    @org.junit.Test
    public void testDeserializeCharset() throws IOException {
        FromStringDeserializer<Charset> serializer = new FromStringDeserializer<Charset>(Charset.class);
        String json = "\"UTF-8\"";
        Charset charset = serializer.deserialize(new JsonFactory(), JsonMappingContext.builder().build(), json);
        assertEquals(Charset.forName("UTF-8"), charset);
    }

    @org.junit.Test
    public void testDeserializeTimeZone() throws IOException {
        FromStringDeserializer<TimeZone> serializer = new FromStringDeserializer<TimeZone>(TimeZone.class);
        String json = "\"America/Los_Angeles\"";
        TimeZone timeZone = serializer.deserialize(new JsonFactory(), JsonMappingContext.builder().build(), json);
        assertEquals(TimeZone.getTimeZone("America/Los_Angeles"), timeZone);
    }

    @org.junit.Test
    public void testDeserializeInetAddress() throws IOException {
        FromStringDeserializer<InetAddress> serializer = new FromStringDeserializer<InetAddress>(InetAddress.class);

        String jsonIPv4 = "\"192.168.1.1\"";
        InetAddress ipv4 = serializer.deserialize(new JsonFactory(), JsonMappingContext.builder().build(), jsonIPv4);
        assertEquals(InetAddress.getByName("192.168.1.1"), ipv4);

        String jsonIPv6 = "[2001:0db8:85a3:0000:0000:8a2e:0370:7334]";
        InetAddress ipv6 = serializer.deserialize(new JsonFactory(), JsonMappingContext.builder().build(), jsonIPv6);
        assertEquals(InetAddress.getByAddress(new byte[]{0x20, 0x0db8, 0x85, 0xa3, 0, 0x8a, 0x2e, 0x03, 0x70, 0x73, 0x34}), ipv6);
    }

    @org.junit.Test
    public void testDeserializeInetSocketAddress() throws IOException {
        FromStringDeserializer<InetSocketAddress> serializer = new FromStringDeserializer<InetSocketAddress>(InetSocketAddress.class);

        String jsonIpv4AddressAndPort = "\"192.168.1.1:80\"";
        InetSocketAddress ipv4AddressAndPort = serializer.deserialize(new JsonFactory(), JsonMappingContext.builder().build(), jsonIpv4AddressAndPort);
        assertEquals(new InetSocketAddress(InetAddress.getByName("192.168.1.1"), 80), ipv4AddressAndPort);

        String jsonIpv6AddressAndPort = "[2001:0db8:85a3:0000:0000:8a2e:0370:7334]:80";
        InetSocketAddress ipv6AddressAndPort = serializer.deserialize(new JsonFactory(), JsonMappingContext.builder().build(), jsonIpv6AddressAndPort);
        assertEquals(new InetSocketAddress(InetAddress.getByAddress(new byte[]{0x20, 0x0db8, 0x85, 0xa3, 0, 0x8a, 0x2e, 0x03, 0x70, 0x73, 0x34}), 80), ipv6AddressAndPort);
    }

    @org.junit.Test
    public void testDeserializeEmptyString() throws IOException {
        FromStringDeserializer<URI> serializer = new FromStringDeserializer<URI>(URI.class);
        URI uri = serializer.deserialize(new JsonFactory(), JsonMappingContext.builder().build(), "\"\"");
        assertEquals(URI.create(""), uri);
    }

    @org.junit.Test
    public void testDeserializeNull() throws IOException {
        FromStringDeserializer<Locale> serializer = new FromStringDeserializer<Locale>(Locale.class);

        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser("null");
        JsonMappingContext jsonContext = JsonMappingContext.builder().build();

        try {
            Locale locale = serializer.deserialize(jsonFactory, jsonContext, jsonParser.getValueAsString());
            fail("Expected an exception");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testDeserializeInvalidString() throws IOException {
        FromStringDeserializer<Locale> serializer = new FromStringDeserializer<Locale>(Locale.class);

        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser("\"invalid\"");
        JsonMappingContext jsonContext = JsonMappingContext.builder().build();

        try {
            Locale locale = serializer.deserialize(jsonFactory, jsonContext, jsonParser.getValueAsString());
            fail("Expected an exception");
        } catch (JsonMappingException e) {
            // Expected
        }
    }
}