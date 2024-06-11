package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.node.TextNode;

public class FromStringDeserializerTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeFile() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructParametricType(FromStringDeserializer.class, File.class);
        FromStringDeserializer<File> deserializer = (FromStringDeserializer<File>) mapper.findValueDeserializer(File.class);

        String fileString = "/test/file.txt";
        File expectedFile = new File(fileString);
        File deserializedFile = deserializer.deserialize(mapper.readTree(new TextNode(fileString)), null);

        assertEquals(expectedFile, deserializedFile);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeUrl() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructParametricType(FromStringDeserializer.class, URL.class);
        FromStringDeserializer<URL> deserializer = (FromStringDeserializer<URL>) mapper.findValueDeserializer(URL.class);

        String urlString = "http://example.com";
        URL expectedUrl = new URL(urlString);
        URL deserializedUrl = deserializer.deserialize(mapper.readTree(new TextNode(urlString)), null);

        assertEquals(expectedUrl, deserializedUrl);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeUri() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructParametricType(FromStringDeserializer.class, URI.class);
        FromStringDeserializer<URI> deserializer = (FromStringDeserializer<URI>) mapper.findValueDeserializer(URI.class);

        String uriString = "http://example.com";
        URI expectedUri = new URI(uriString);
        URI deserializedUri = deserializer.deserialize(mapper.readTree(new TextNode(uriString)), null);

        assertEquals(expectedUri, deserializedUri);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeClass() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructParametricType(FromStringDeserializer.class, String.class.getClass());
        FromStringDeserializer<Class<String>> deserializer = (FromStringDeserializer<Class<String>>) mapper.findValueDeserializer(String.class);

        String className = "java.lang.String";
        Class<String> expectedClass = String.class;
        Class<String> deserializedClass = deserializer.deserialize(mapper.readTree(new TextNode(className)), null);

        assertEquals(expectedClass, deserializedClass);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeJavaType() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructParametricType(FromStringDeserializer.class, JavaType.class);
        FromStringDeserializer<JavaType> deserializer = (FromStringDeserializer<JavaType>) mapper.findValueDeserializer(JavaType.class);

        String typeString = "java.util.List<java.lang.String>";
        JavaType expectedType = mapper.getTypeFactory().constructType(typeString);
        JavaType deserializedType = deserializer.deserialize(mapper.readTree(new TextNode(typeString)), null);

        assertEquals(expectedType, deserializedType);
    }

    @Test
    public void testDeserializeCurrency() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Currency> deserializer = new FromStringDeserializer<>(Currency.class);

        String currencyString = "EUR";
        Currency expectedCurrency = Currency.getInstance(currencyString);
        Currency deserializedCurrency = deserializer.deserialize(mapper.readTree(new TextNode(currencyString)), null);

        assertEquals(expectedCurrency, deserializedCurrency);
    }

    @Test
    public void testDeserializePattern() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Pattern> deserializer = new FromStringDeserializer<>(Pattern.class);

        String patternString = "\\d+";
        Pattern expectedPattern = Pattern.compile(patternString);
        Pattern deserializedPattern = deserializer.deserialize(mapper.readTree(new TextNode(patternString)), null);

        assertEquals(expectedPattern, deserializedPattern);
    }

    @Test
    public void testDeserializeLocale() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Locale> deserializer = new FromStringDeserializer<>(Locale.class);

        String localeString = "en_US";
        Locale expectedLocale = Locale.forLanguageTag(localeString);
        Locale deserializedLocale = deserializer.deserialize(mapper.readTree(new TextNode(localeString)), null);

        assertEquals(expectedLocale, deserializedLocale);
    }

    @Test
    public void testDeserializeCharset() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Charset> deserializer = new FromStringDeserializer<>(Charset.class);

        String charsetString = "UTF-8";
        Charset expectedCharset = Charset.forName(charsetString);
        Charset deserializedCharset = deserializer.deserialize(mapper.readTree(new TextNode(charsetString)), null);

        assertEquals(expectedCharset, deserializedCharset);
    }

    @Test
    public void testDeserializeTimeZone() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<TimeZone> deserializer = new FromStringDeserializer<>(TimeZone.class);

        String timeZoneString = "America/Los_Angeles";
        TimeZone expectedTimeZone = TimeZone.getTimeZone(timeZoneString);
        TimeZone deserializedTimeZone = deserializer.deserialize(mapper.readTree(new TextNode(timeZoneString)), null);

        assertEquals(expectedTimeZone, deserializedTimeZone);
    }

    @Test
    public void testDeserializeInetAddress() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<InetAddress> deserializer = new FromStringDeserializer<>(InetAddress.class);

        String inetAddressString = "192.168.1.1";
        InetAddress expectedInetAddress = InetAddress.getByName(inetAddressString);
        InetAddress deserializedInetAddress = deserializer.deserialize(mapper.readTree(new TextNode(inetAddressString)), null);

        assertEquals(expectedInetAddress, deserializedInetAddress);
    }

    @Test
    public void testDeserializeInetSocketAddress() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<InetSocketAddress> deserializer = new FromStringDeserializer<>(InetSocketAddress.class);

        String inetSocketAddressString = "[255.255.255.255]:1234";
        InetSocketAddress expectedInetSocketAddress = new InetSocketAddress(InetAddress.getByName("255.255.255.255"), 1234);
        InetSocketAddress deserializedInetSocketAddress = deserializer.deserialize(mapper.readTree(new TextNode(inetSocketAddressString)), null);

        assertEquals(expectedInetSocketAddress, deserializedInetSocketAddress);
    }

    @Test
    public void testDeserializeEmptyString() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<String> deserializer = new FromStringDeserializer<>(String.class);

        String emptyString = "";
        String deserializedEmptyString = deserializer.deserializeFromEmptyString();

        assertNull(deserializedEmptyString);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeUnknownType() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Object> deserializer = new FromStringDeserializer<>(Object.class);

        String unknownTypeString = "java.util.List<java.lang.Integer>";
        deserializer.deserialize(mapper.readTree(new TextNode(unknownTypeString)), null);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidString() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Integer> deserializer = new FromStringDeserializer<>(Integer.class);

        String invalidString = "foo";
        deserializer.deserialize(mapper.readTree(new TextNode(invalidString)), null);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeNull() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<String> deserializer = new FromStringDeserializer<>(String.class);

        Object nullValue = null;
        deserializer.deserialize(mapper.readTree(new TextNode(null)), null);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeUnexpectedToken() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<String> deserializer = new FromStringDeserializer<>(String.class);

        JsonParser jsonParser = mapper.getFactory().createParser(new ByteArrayInputStream("[]".getBytes()));
        jsonParser.nextToken(); // ARRAY_BEGIN
        deserializer.deserialize(jsonParser, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeUnexpectedEmbeddedObject() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<String> deserializer = new FromStringDeserializer<>(String.class);

        Object embeddedObject = new Object();
        deserializer._deserializeEmbedded(embeddedObject, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeUnexpectedEmptyString() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<String> deserializer = new FromStringDeserializer<>(String.class);

        Object emptyString = "";
        deserializer._deserializeFromEmptyString();
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeUnexpectedNull() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<String> deserializer = new FromStringDeserializer<>(String.class);

        Object nullValue = null;
        deserializer._deserializeFromEmptyString();
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeUnexpectedToken_2() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<String> deserializer = new FromStringDeserializer<>(String.class);

        JsonParser jsonParser = mapper.getFactory().createParser(new ByteArrayInputStream("true".getBytes()));
        jsonParser.nextToken(); // BOOLEAN
        deserializer.deserialize(jsonParser, null);
    }
}