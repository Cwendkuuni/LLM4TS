package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;

import java.io.File;
import java.net.*;
import java.net.URI;
import java.util.*;

public class FromStringDeserializerTest {

    @Test
    public void testDeserialize_File() throws Exception {
        FromStringDeserializer<File> fileDeserializer = FromStringDeserializer.findDeserializer(File.class);
        ObjectMapper objectMapper = new ObjectMapper();

        String filePath = "/path/to/test/file.txt";
        String jsonString = "\"/" + filePath + "\"";

        File result = fileDeserializer.deserialize(objectMapper.getFactory().createParser(jsonString), objectMapper.getDeserializationContext());

        assertEquals(new File(filePath), result);
    }

    @Test
    public void testDeserialize_URL() throws Exception {
        FromStringDeserializer<URL> urlDeserializer = FromStringDeserializer.findDeserializer(URL.class);
        ObjectMapper objectMapper = new ObjectMapper();

        String urlString = "http://example.com";

        URL result = urlDeserializer.deserialize(objectMapper.getFactory().createParser(jsonString), objectMapper.getDeserializationContext());

        assertEquals(new URL("http://example.com"), result);
    }

    @Test
    public void testDeserialize_URI() throws Exception {
        FromStringDeserializer<URI> uriDeserializer = FromStringDeserializer.findDeserializer(URI.class);
        ObjectMapper objectMapper = new ObjectMapper();

        String uriString = "http://example.com/path/to/resource";

        URI result = uriDeserializer.deserialize(objectMapper.getFactory().createParser(jsonString), objectMapper.getDeserializationContext());

        assertEquals(new URI("http://example.com/path/to/resource"), result);
    }

    @Test
    public void testDeserialize_Class() throws Exception {
        FromStringDeserializer<Class<?>> classDeserializer = FromStringDeserializer.findDeserializer(String.class);
        ObjectMapper objectMapper = new ObjectMapper();

        String className = "java.lang.String";

        Class<?> result = classDeserializer.deserialize(objectMapper.getFactory().createParser(jsonString), objectMapper.getDeserializationContext());

        assertEquals(Class.forName(className), result.getClass());
        assertEquals(className, result.getName());
    }

    @Test
    public void testDeserialize_JavaType() throws Exception {
        FromStringDeserializer<JavaType> javaTypeDeserializer = FromStringDeserializer.findDeserializer(JavaType.class);
        ObjectMapper objectMapper = new ObjectMapper();

        String jsonString = "com.example.MyClass";

        JavaType result = javaTypeDeserializer.deserialize(objectMapper.getFactory().createParser(jsonString), objectMapper.getDeserializationContext());

        assertEquals(ClassUtil.constructType(MyClass.class), result);
    }

    @Test
    public void testDeserialize_Currency() throws Exception {
        FromStringDeserializer<Currency> currencyDeserializer = FromStringDeserializer.findDeserializer(Currency.class);
        ObjectMapper objectMapper = new ObjectMapper();

        String currencyCode = "USD";

        Currency result = currencyDeserializer.deserialize(objectMapper.getFactory().createParser(jsonString), objectMapper.getDeserializationContext());

        assertEquals(Currency.getInstance(currencyCode), result);
    }

    @Test
    public void testDeserialize_Pattern() throws Exception {
        FromStringDeserializer<Pattern> patternDeserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        ObjectMapper objectMapper = new ObjectMapper();

        String patternString = "\\d+";

        Pattern result = patternDeserializer.deserialize(objectMapper.getFactory().createParser(jsonString), objectMapper.getDeserializationContext());

        assertEquals(Pattern.compile(patternString), result);
    }

    @Test
    public void testDeserialize_Locale() throws Exception {
        FromStringDeserializer<Locale> localeDeserializer = FromStringDeserializer.findDeserializer(Locale.class);
        ObjectMapper objectMapper = new ObjectMapper();

        String localeString = "en_US";

        Locale result = localeDeserializer.deserialize(objectMapper.getFactory().createParser(jsonString), objectMapper.getDeserializationContext());

        assertEquals(Locale.forLanguageTag(localeString), result);

        localeString = "en_US_POSIX";

        result = localeDeserializer.deserialize(objectMapper.getFactory().createParser(jsonString), objectMapper.getDeserializationContext());

        assertEquals(new Locale("en", "US"), result);
    }

    @Test
    public void testDeserialize_Charset() throws Exception {
        FromStringDeserializer<Charset> charsetDeserializer = FromStringDeserializer.findDeserializer(Charset.class);
        ObjectMapper objectMapper = new ObjectMapper();

        String charsetName = "UTF-8";

        Charset result = charsetDeserializer.deserialize(objectMapper.getFactory().createParser(jsonString), objectMapper.getDeserializationContext());

        assertEquals(Charset.forName(charsetName), result);
    }

    @Test
    public void testDeserialize_TimeZone() throws Exception {
        FromStringDeserializer<TimeZone> timeZoneDeserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        ObjectMapper objectMapper = new ObjectMapper();

        String timeZoneID = "America/Los_Angeles";

        TimeZone result = timeZoneDeserializer.deserialize(objectMapper.getFactory().createParser(jsonString), objectMapper.getDeserializationContext());

        assertEquals(TimeZone.getTimeZone(timeZoneID), result);
    }

    @Test
    public void testDeserialize_InetAddress() throws Exception {
        FromStringDeserializer<InetAddress> inetAddressDeserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        ObjectMapper objectMapper = new ObjectMapper();

        String ipv4Address = "192.168.1.1";

        InetAddress result = inetAddressDeserializer.deserialize(objectMapper.getFactory().createParser(jsonString), objectMapper.getDeserializationContext());

        assertEquals(InetAddress.getByName(ipv4Address), result);
    }

    @Test
    public void testDeserialize_InetSocketAddress() throws Exception {
        FromStringDeserializer<InetSocketAddress> inetSocketAddressDeserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        ObjectMapper objectMapper = new ObjectMapper();

        String ipv6Address = "[2001:db8:85a3:0:0:8a2e:370:7334]";

        InetSocketAddress result = (InetSocketAddress) inetSocketAddressDeserializer.deserialize(objectMapper.getFactory().createParser(jsonString), objectMapper.getDeserializationContext());

        assertEquals(new InetSocketAddress(ipv6Address), result);

        ipv6Address = "[2001:db8:85a3:0:0:8a2e:370:7334]:80";

        result = (InetSocketAddress) inetSocketAddressDeserializer.deserialize(objectMapper.getFactory().createParser(jsonString), objectMapper.getDeserializationContext());

        assertEquals(new InetSocketAddress(ipv6Address), result);
    }

    @Test
    public void testDeserialize_Null() throws Exception {
        FromStringDeserializer<Object> objectDeserializer = FromStringDeserializer.findDeserializer(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();

        assertNull(objectDeserializer.deserialize(objectMapper.getFactory().createParser(JsonToken.VALUE_NULL.name()), objectMapper.getDeserializationContext()));
    }

    @Test
    public void testDeserialize_EmptyString() throws Exception {
        FromStringDeserializer<Object> objectDeserializer = FromStringDeserializer.findDeserializer(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();

        assertNull(objectDeserializer.deserialize(objectMapper.getFactory().createParser(JsonToken.VALUE_NULL.name()), objectMapper.getDeserializationContext()));
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserialize_InvalidType() throws Exception {
        FromStringDeserializer<Object> objectDeserializer = FromStringDeserializer.findDeserializer(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();

        String jsonString = "[]";

        objectDeserializer.deserialize(objectMapper.getFactory().createParser(jsonString), objectMapper.getDeserializationContext());
    }
}