package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;
import com.fasterxml.jackson.databind.node.*;

import java.io.IOException;
import java.net.*;
import java.util.*;

public class FromStringDeserializerTest {

    @Test
    public void testDeserializeFile() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        FromStringDeserializer<File> fileDeserializer = FromStringDeserializer.findDeserializer(File.class);
        File inputFile = new File("src/test/resources/testFile.txt");
        String filePath = inputFile.getAbsolutePath();

        File deserializedFile = fileDeserializer.deserialize(objectMapper.readerForUpdating(new ObjectNode()), null);
        assertNotNull(deserializedFile);
        assertEquals(inputFile, deserializedFile);
    }

    @Test
    public void testDeserializeUrl() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        FromStringDeserializer<URL> urlDeserializer = FromStringDeserializer.findDeserializer(URL.class);
        String urlString = "http://www.example.com";

        URL deserializedUrl = urlDeserializer.deserialize(objectMapper.readerForUpdating(new ObjectNode()), null);
        assertNotNull(deserializedUrl);
        assertEquals(new URL(urlString), deserializedUrl);
    }

    @Test
    public void testDeserializeUri() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        FromStringDeserializer<URI> uriDeserializer = FromStringDeserializer.findDeserializer(URI.class);
        String uriString = "http://www.example.com/path/to/resource";

        URI deserializedUri = uriDeserializer.deserialize(objectMapper.readerForUpdating(new ObjectNode()), null);
        assertNotNull(deserializedUri);
        assertEquals(new URI(uriString), deserializedUri);
    }

    @Test
    public void testDeserializeClass() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        FromStringDeserializer<Class<?>> classDeserializer = FromStringDeserializer.findDeserializer(String.class);
        String className = String.class.getName();

        Class<?> deserializedClass = classDeserializer.deserialize(objectMapper.readerForUpdating(new ObjectNode()), null);
        assertNotNull(deserializedClass);
        assertEquals(Class.forName(className), deserializedClass);
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        FromStringDeserializer<JavaType> javaTypeDeserializer = FromStringDeserializer.findDeserializer(JavaType.class);
        String javaTypeString = "java.util.List<java.lang.String>";

        JavaType deserializedJavaType = javaTypeDeserializer.deserialize(objectMapper.readerForUpdating(new ObjectNode()), null);
        assertNotNull(deserializedJavaType);
        assertEquals(JavaType.constructType(Class.forName("java.util.List"), Class.forName("java.lang.String")), deserializedJavaType);
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        FromStringDeserializer<Currency> currencyDeserializer = FromStringDeserializer.findDeserializer(Currency.class);
        String currencyCode = "USD";

        Currency deserializedCurrency = currencyDeserializer.deserialize(objectMapper.readerForUpdating(new ObjectNode()), null);
        assertNotNull(deserializedCurrency);
        assertEquals(Currency.getInstance(currencyCode), deserializedCurrency);
    }

    @Test
    public void testDeserializePattern() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        FromStringDeserializer<Pattern> patternDeserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        String patternString = "\\d+";

        Pattern deserializedPattern = patternDeserializer.deserialize(objectMapper.readerForUpdating(new ObjectNode()), null);
        assertNotNull(deserializedPattern);
        assertEquals(Pattern.compile(patternString), deserializedPattern);
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        FromStringDeserializer<Locale> localeDeserializer = FromStringDeserializer.findDeserializer(Locale.class);
        String localeString = "en_US";

        Locale deserializedLocale = localeDeserializer.deserialize(objectMapper.readerForUpdating(new ObjectNode()), null);
        assertNotNull(deserializedLocale);
        assertEquals(new Locale("en", "US"), deserializedLocale);

        localeString = "en-US";
        deserializedLocale = localeDeserializer.deserialize(objectMapper.readerForUpdating(new ObjectNode()), null);
        assertNotNull(deserializedLocale);
        assertEquals(new Locale("en", "US"), deserializedLocale);

        localeString = "en_US_POSIX";
        deserializedLocale = localeDeserializer.deserialize(objectMapper.readerForUpdating(new ObjectNode()), null);
        assertNotNull(deserializedLocale);
        assertEquals(new Locale("en", "US", ""), deserializedLocale);

        localeString = "fr_FR";
        deserializedLocale = localeDeserializer.deserialize(objectMapper.readerForUpdating(new ObjectNode()), null);
        assertNotNull(deserializedLocale);
        assertEquals(new Locale("fr", "FR"), deserializedLocale);
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        FromStringDeserializer<Charset> charsetDeserializer = FromStringDeserializer.findDeserializer(Charset.class);
        String charsetName = "UTF-8";

        Charset deserializedCharset = charsetDeserializer.deserialize(objectMapper.readerForUpdating(new ObjectNode()), null);
        assertNotNull(deserializedCharset);
        assertEquals(Charset.forName(charsetName), deserializedCharset);
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        FromStringDeserializer<TimeZone> timeZoneDeserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        String timeZoneId = "America/New_York";

        TimeZone deserializedTimeZone = timeZoneDeserializer.deserialize(objectMapper.readerForUpdating(new ObjectNode()), null);
        assertNotNull(deserializedTimeZone);
        assertEquals(TimeZone.getTimeZone(timeZoneId), deserializedTimeZone);
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        FromStringDeserializer<InetAddress> inetAddressDeserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        String ipv4Address = "127.0.0.1";
        String ipv6Address = "[2001:db8:0:1234:0:567:8:1]";

        InetAddress deserializedInetAddress1 = inetAddressDeserializer.deserialize(objectMapper.readerForUpdating(new ObjectNode()), null);
        assertNotNull(deserializedInetAddress1);
        assertEquals(InetAddress.getByName(ipv4Address), deserializedInetAddress1);

        InetAddress deserializedInetAddress2 = inetAddressDeserializer.deserialize(objectMapper.readerForUpdating(new ObjectNode()), null);
        assertNotNull(deserializedInetAddress2);
        assertEquals(InetAddress.getByName(ipv6Address), deserializedInetAddress2);
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        FromStringDeserializer<InetSocketAddress> inetSocketAddressDeserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        String ipv4AddressWithPort = "127.0.0.1:8080";
        String ipv6AddressWithPort = "[2001:db8:0:1234:0:567:8:1]:8080";
        String ipv6AddressWithoutPort = "[2001:db8:0:1234:0:567:8:1]";

        InetSocketAddress deserializedInetSocketAddress1 = inetSocketAddressDeserializer.deserialize(objectMapper.readerForUpdating(new ObjectNode()), null);
        assertNotNull(deserializedInetSocketAddress1);
        assertEquals(new InetSocketAddress(InetAddress.getByName(ipv4AddressWithPort.substring(0, ipv4AddressWithPort.indexOf(":"))), 8080), deserializedInetSocketAddress1);

        InetSocketAddress deserializedInetSocketAddress2 = inetSocketAddressDeserializer.deserialize(objectMapper.readerForUpdating(new ObjectNode()), null);
        assertNotNull(deserializedInetSocketAddress2);
        assertEquals(new InetSocketAddress(InetAddress.getByName(ipv6AddressWithPort.substring(1, ipv6AddressWithPort.lastIndexOf(":"))), 8080), deserializedInetSocketAddress2);

        InetSocketAddress deserializedInetSocketAddress3 = inetSocketAddressDeserializer.deserialize(objectMapper.readerForUpdating(new ObjectNode()), null);
        assertNotNull(deserializedInetSocketAddress3);
        assertEquals(new InetSocketAddress(InetAddress.getByName(ipv6AddressWithoutPort), 0), deserializedInetSocketAddress3);
    }

    @Test
    public void testDeserializeStringBuilder() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        FromStringDeserializer<StringBuilder> stringBuilderDeserializer = FromStringDeserializer.findDeserializer(StringBuilder.class);
        String stringBuilderInitialValue = "Initial value";

        StringBuilder deserializedStringBuilder = stringBuilderDeserializer.deserialize(objectMapper.readerForUpdating(new ObjectNode()), null);
        assertNotNull(deserializedStringBuilder);
        assertEquals(new StringBuilder(stringBuilderInitialValue), deserializedStringBuilder);
    }
}