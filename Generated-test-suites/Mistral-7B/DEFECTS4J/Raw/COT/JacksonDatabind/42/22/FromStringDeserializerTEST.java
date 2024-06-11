package com.example.test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class FromStringDeserializerTest {

    @Test
    public void testDeserializeBasic() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<File> fileDeserializer = FromStringDeserializer.findDeserializer(File.class);
        String jsonString = "\"/path/to/file.txt\"";
        File expectedFile = new File("/path/to/file.txt");
        
        File deserializedFile = mapper.readValue(jsonString, File.class, fileDeserializer);
        assertEquals(expectedFile, deserializedFile);
    }

    @Test
    public void testDeserializeEmptyString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<URI> uriDeserializer = FromStringDeserializer.findDeserializer(URI.class);
        
        String jsonString = "\"\"";
        URI expectedURI = URI.create("");
        
        URI deserializedURI = mapper.readValue(jsonString, URI.class, uriDeserializer);
        assertEquals(expectedURI, deserializedURI);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidFormat() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<URL> urlDeserializer = FromStringDeserializer.findDeserializer(URL.class);
        
        String jsonString = "\"invalidUrl\"";
        mapper.readValue(jsonString, URL.class, urlDeserializer);
    }

    @Test
    public void testDeserializeEmbeddedObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Object obj = new Object() {{
            String value = "testValue";
        }};
        
        FromStringDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(Object.class);
        String jsonString = "{\"value\":{\"class\":\"java.lang.Object\",\"value\":\"testValue\"}}";
        
        Object deserializedObj = mapper.readValue(jsonString, Object.class, deserializer);
        assertEquals(obj, deserializedObj);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeUnknownType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(String.class);
        String jsonString = "123";
        
        mapper.readValue(jsonString, Object.class, deserializer);
    }

    @Test
    public void testDeserializeUrl() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<URL> urlDeserializer = FromStringDeserializer.findDeserializer(URL.class);
        String jsonString = "\"https://www.google.com\"";
        
        URL deserializedUrl = mapper.readValue(jsonString, URL.class, urlDeserializer);
        assertEquals("https://www.google.com", deserializedUrl.toString());
    }

    @Test
    public void testDeserializeUri() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<URI> uriDeserializer = FromStringDeserializer.findDeserializer(URI.class);
        String jsonString = "\"vnc://localhost:5901\"";
        
        URI deserializedUri = mapper.readValue(jsonString, URI.class, uriDeserializer);
        assertEquals("vnc://localhost:5901", deserializedUri.toString());
    }

    @Test
    public void testDeserializeClass() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Class<?>> classDeserializer = FromStringDeserializer.findDeserializer(Class.class);
        String jsonString = "java.lang.String";
        
        Class<?> deserializedClass = mapper.readValue(jsonString, Class.class, classDeserializer);
        assertEquals(String.class, deserializedClass);
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<JavaType> javaTypeDeserializer = FromStringDeserializer.findDeserializer(JavaType.class);
        String jsonString = "com.example.test.MyCustomType";
        
        JavaType deserializedJavaType = mapper.readValue(jsonString, JavaType.class, javaTypeDeserializer);
        assertEquals(MyCustomType.class, deserializedJavaType.getRawClass());
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Currency> currencyDeserializer = FromStringDeserializer.findDeserializer(Currency.class);
        String jsonString = "EUR";
        
        Currency deserializedCurrency = mapper.readValue(jsonString, Currency.class, currencyDeserializer);
        assertEquals(Currency.EUR, deserializedCurrency);
    }

    @Test
    public void testDeserializePattern() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Pattern> patternDeserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        String jsonString = "/[a-zA-Z0-9]+/";
        
        Pattern deserializedPattern = mapper.readValue(jsonString, Pattern.class, patternDeserializer);
        assertTrue(deserializedPattern.matcher("example").matches());
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Locale> localeDeserializer = FromStringDeserializer.findDeserializer(Locale.class);
        String jsonString = "en_US";
        
        Locale deserializedLocale = mapper.readValue(jsonString, Locale.class, localeDeserializer);
        assertEquals("en_US", deserializedLocale.toString());
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Charset> charsetDeserializer = FromStringDeserializer.findDeserializer(Charset.class);
        String jsonString = "UTF-8";
        
        Charset deserializedCharset = mapper.readValue(jsonString, Charset.class, charsetDeserializer);
        assertEquals("UTF-8", deserializedCharset.name());
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<TimeZone> timeZoneDeserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        String jsonString = "UTC";
        
        TimeZone deserializedTimeZone = mapper.readValue(jsonString, TimeZone.class, timeZoneDeserializer);
        assertEquals("UTC", deserializedTimeZone.getID());
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<InetAddress> inetAddressDeserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        String jsonString = "127.0.0.1";
        
        InetAddress deserializedInetAddress = mapper.readValue(jsonString, InetAddress.class, inetAddressDeserializer);
        assertEquals("127.0.0.1", deserializedInetAddress.getHostAddress());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidInetSocketAddress() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<InetSocketAddress> inetSocketAddressDeserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        String jsonString = "[127.0.0.1:a]";
        
        mapper.readValue(jsonString, InetSocketAddress.class, inetSocketAddressDeserializer);
    }

    @Test
    public void testDeserializeValidInetSocketAddress() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<InetSocketAddress> inetSocketAddressDeserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        String jsonString = "[127.0.0.1:8080]";
        
        InetSocketAddress deserializedInetSocketAddress = mapper.readValue(jsonString, InetSocketAddress.class, inetSocketAddressDeserializer);
        assertEquals("127.0.0.1", deserializedInetSocketAddress.getAddress().getHostAddress());
        assertEquals(8080, deserializedInetSocketAddress.getPort());
    }
}