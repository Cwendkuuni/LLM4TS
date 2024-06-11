package com.example.junit; // replace with your testing package name

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URI;
import java.util.Currency;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonFactory;

@RunWith(org.mockito.junit.MockitoJUnitRunner.class)
public class FromStringDeserializerTest {

    

    @Test
    public void testDeserialize_StringToFile() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.getParser();

        parser.setValue("path/to/file.txt");
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        FromStringDeserializer<File> fileDeserializer = new FromStringDeserializer<File>(File.class);
        File result = fileDeserializer.deserialize(parser, ctxt);

        assertNotNull(result);
        assertEquals("path/to/file.txt", result.getAbsolutePath());
    }

    @Test
    public void testDeserialize_StringToURL() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.getParser();

        parser.setValue("http://example.com");
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        FromStringDeserializer<URL> urlDeserializer = new FromStringDeserializer<URL>(URL.class);
        URL result = urlDeserializer.deserialize(parser, ctxt);

        assertNotNull(result);
        assertEquals("http://example.com", result.toString());
    }

    @Test
    public void testDeserialize_StringToURI() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.getParser();

        parser.setValue("http://example.com");
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        FromStringDeserializer<URI> uriDeserializer = new FromStringDeserializer<URI>(URI.class);
        URI result = uriDeserializer.deserialize(parser, ctxt);

        assertNotNull(result);
        assertEquals("http", result.getScheme());
        assertEquals("example.com", result.getHost());
    }

    @Test
    public void testDeserialize_StringToClass() throws IOException, ClassNotFoundException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.getParser();

        parser.setValue("java.lang.String");
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        FromStringDeserializer<Class<?>> classDeserializer = new FromStringDeserializer<Class<Object>>(Class.class);
        Class<?> result = classDeserializer.deserialize(parser, ctxt);

        assertNotNull(result);
        assertEquals(String.class, result);
    }

    @Test
    public void testDeserialize_StringToJavaType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.getParser();

        parser.setValue("java.util.List<java.lang.Integer>");
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        FromStringDeserializer<JavaType> javaTypeDeserializer = new FromStringDeserializer<JavaType>(JavaType.class);
        JavaType result = javaTypeDeserializer.deserialize(parser, ctxt);

        assertNotNull(result);
        assertEquals(JavaType.constructType(List.class, Integer.class), result);
    }

    @Test
    public void testDeserialize_StringToCurrency() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.getParser();

        parser.setValue("EUR");
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        FromStringDeserializer<Currency> currencyDeserializer = new FromStringDeserializer<Currency>(Currency.class);
        Currency result = currencyDeserializer.deserialize(parser, ctxt);

        assertNotNull(result);
        assertEquals(Currency.getInstance("EUR"), result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserialize_InvalidStringToPattern() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.getParser();

        parser.setValue("InvalidPattern");
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        FromStringDeserializer<Pattern> patternDeserializer = new FromStringDeserializer<Pattern>(Pattern.class);
        patternDeserializer.deserialize(parser, ctxt);

        fail("Exception expected: JsonMappingException");
    }

    @Test
    public void testDeserialize_EmptyStringToLocale() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.getParser();

        parser.setValue("");
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        FromStringDeserializer<Locale> localeDeserializer = new FromStringDeserializer<Locale>(Locale.class);
        Locale result = localeDeserializer.deserialize(parser, ctxt);

        assertNotNull(result);
        assertEquals(Locale.ROOT, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserialize_EmptyStringToCharset() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.getParser();

        parser.setValue("");
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        FromStringDeserializer<Charset> charsetDeserializer = new FromStringDeserializer<Charset>(Charset.class);
        charsetDeserializer.deserialize(parser, ctxt);

        fail("Exception expected: JsonMappingException");
    }

    @Test
    public void testDeserialize_EmptyStringToTimeZone() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.getParser();

        parser.setValue("");
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        FromStringDeserializer<TimeZone> timeZoneDeserializer = new FromStringDeserializer<TimeZone>(TimeZone.class);
        TimeZone result = timeZoneDeserializer.deserialize(parser, ctxt);

        assertNotNull(result);
        assertEquals(TimeZone.getDefault(), result);
    }

    @Test(expected = InvalidFormatException.class)
    public void testDeserialize_InvalidStringToInetAddress() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.getParser();

        parser.setValue("InvalidIPAddress");
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        FromStringDeserializer<InetAddress> inetAddressDeserializer = new FromStringDeserializer<InetAddress>(InetAddress.class);
        inetAddressDeserializer.deserialize(parser, ctxt);

        fail("Exception expected: InvalidFormatException");
    }

    @Test
    public void testDeserialize_StringToInetSocketAddress() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.getParser();

        parser.setValue("[255.255.255.255]:80");
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        FromStringDeserializer<InetSocketAddress> inetSocketAddressDeserializer = new FromStringDeserializer<InetSocketAddress>(InetSocketAddress.class);
        InetSocketAddress result = inetSocketAddressDeserializer.deserialize(parser, ctxt);

        assertNotNull(result);
        assertEquals("255.255.255.255", result.getAddress().getHostAddress());
        assertEquals(80, result.getPort());
    }

    @Test
    public void testDeserialize_StringArrayToSingleValueType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.getParser();

        parser.setValue("[\"File:path/to/file.txt\"]");
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        FromStringDeserializer<File> fileDeserializer = new FromStringDeserializer<File>(File.class);
        File[] result = fileDeserializer.deserializeAll(parser, ctxt);

        assertNotNull(result);
        assertEquals(1, result.length);
        File singleResult = result[0];
        assertEquals("path/to/file.txt", singleResult.getAbsolutePath());
    }

    
}