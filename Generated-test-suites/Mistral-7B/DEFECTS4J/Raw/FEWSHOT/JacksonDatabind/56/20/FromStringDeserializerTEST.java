package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Locale;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FromStringDeserializerTest {

    @Test
    public void testDeserializeFile() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(File.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        String fileString = "/path/to/file.txt";
        File file = (File) deserializer.deserialize(mapper.getFactory().createParser(fileString), ctxt);
        assertEquals(fileString, file.getAbsolutePath());
    }

    @Test
    public void testDeserializeURL() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URL.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        String urlString = "http://example.com";
        URL url = (URL) deserializer.deserialize(mapper.getFactory().createParser(urlString), ctxt);
        assertEquals(urlString, url.toString());
    }

    @Test
    public void testDeserializeURI() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URI.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        String uriString = "http://example.com";
        URI uri = (URI) deserializer.deserialize(mapper.getFactory().createParser(uriString), ctxt);
        assertEquals(uriString, uri.toString());
    }

    @Test
    public void testDeserializeClass() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String[].class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        String className = "[Ljava.lang.String;";
        Class<?> clazz = (Class<?>) deserializer.deserialize(mapper.getFactory().createParser(className), ctxt);
        assertEquals(Class.forName("java.lang.String[]"), clazz);
    }

    @Test
    public void testDeserializeJavaType() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Integer.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        String javaTypeString = "int";
        JavaType javaType = (JavaType) deserializer.deserialize(mapper.getFactory().createParser(javaTypeString), ctxt);
        assertEquals(mapper.getTypeFactory().constructType(int.class), javaType);
    }

    @Test
    public void testDeserializeCurrency() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        String currencyString = "USD";
        Currency currency = (Currency) deserializer.deserialize(mapper.getFactory().createParser(currencyString), ctxt);
        assertEquals(Currency.getInstance("USD"), currency);
    }

    @Test
    public void testDeserializePattern() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        String patternString = "\\d{3}-\\d{2}-\\d{3}";
        Pattern pattern = (Pattern) deserializer.deserialize(mapper.getFactory().createParser(patternString), ctxt);
        assertNotNull(pattern.pattern());
    }

    @Test
    public void testDeserializeLocale() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        String localeString = "en_US";
        Locale locale = (Locale) deserializer.deserialize(mapper.getFactory().createParser(localeString), ctxt);
        assertEquals(new Locale("en", "US"), locale);

        String localeStringWithLanguageAndCountry = "en_US_POSIX";
        locale = (Locale) deserializer.deserialize(mapper.getFactory().createParser(localeStringWithLanguageAndCountry), ctxt);
        assertEquals(new Locale("en", "US", ""), locale);

        String localeStringWithLanguageAndCountryAndVariants = "en_US_POSIX.apple.doubleByte";
        locale = (Locale) deserializer.deserialize(mapper.getFactory().createParser(localeStringWithLanguageAndCountryAndVariants), ctxt);
        assertEquals(new Locale("en", "US", "doubleByte"), locale);
    }

    @Test
    public void testDeserializeCharset() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Charset.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        String charsetString = "UTF-8";
        Charset charset = (Charset) deserializer.deserialize(mapper.getFactory().createParser(charsetString), ctxt);
        assertEquals(Charset.forName("UTF-8"), charset);
    }

    @Test
    public void testDeserializeTimeZone() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        String timeZoneString = "America/New_York";
        TimeZone timeZone = (TimeZone) deserializer.deserialize(mapper.getFactory().createParser(timeZoneString), ctxt);
        assertEquals(TimeZone.getTimeZone(timeZoneString), timeZone);
    }

    @Test
    public void testDeserializeInetAddress() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        String inetAddressString = "192.168.1.1";
        InetAddress inetAddress = (InetAddress) deserializer.deserialize(mapper.getFactory().createParser(inetAddressString), ctxt);
        assertEquals(InetAddress.getByName(inetAddressString), inetAddress);

        String inetSocketAddressString = "[::1]:8080";
        InetSocketAddress inetSocketAddress = (InetSocketAddress) deserializer.deserialize(mapper.getFactory().createParser(inetSocketAddressString), ctxt);
        assertEquals(new InetSocketAddress("::1", 8080), inetSocketAddress);
    }

    @Test
    public void testDeserializeInetSocketAddressWithIPv4() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        String inetSocketAddressString = "192.168.1.1:8080";
        InetSocketAddress inetSocketAddress = (InetSocketAddress) deserializer.deserialize(mapper.getFactory().createParser(inetSocketAddressString), ctxt);
        assertEquals(new InetSocketAddress("192.168.1.1", 8080), inetSocketAddress);
    }

    @Test
    public void testDeserializeInetSocketAddressWithIPv6() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        String inetSocketAddressString = "[2001:db8:1:1::1]:8080";
        InetSocketAddress inetSocketAddress = (InetSocketAddress) deserializer.deserialize(mapper.getFactory().createParser(inetSocketAddressString), ctxt);
        assertEquals(new InetSocketAddress("2001:db8:1:1::1", 8080), inetSocketAddress);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeUnknownClass() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class); // incorrect class
        DeserializationContext ctxt = mapper.getDeserializationContext();

        String unknownClassString = "com.example.MyClass";
        deserializer.deserialize(mapper.getFactory().createParser(unknownClassString), ctxt);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeEmptyString() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URI.class);

        JsonParser parser = mapper.getFactory().createParser("");
        deserializer.deserialize(parser, mapper.getDeserializationContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeNull() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URI.class);

        JsonParser parser = mapper.getFactory().createParser(null);
        deserializer.deserialize(parser, mapper.getDeserializationContext());
    }
}