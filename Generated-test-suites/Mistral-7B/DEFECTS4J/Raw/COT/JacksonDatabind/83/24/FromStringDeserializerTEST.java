package com.example.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.deser.std.Std;

@RunWith(JUnit4.class)
public class FromStringDeserializerTest {

    @Test
    public void testDeserializeFile() throws IOException {
        FromStringDeserializer<File> fileDeserializer = FromStringDeserializer.findDeserializer(File.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser p = jsonFactory.createParser("\"path/to/file.txt\"");

        File result = fileDeserializer.deserialize(p, ctxt);
        assertEquals("path/to/file.txt", result.getAbsolutePath());
    }

    @Test
    public void testDeserializeUrl() throws IOException {
        FromStringDeserializer<URL> urlDeserializer = FromStringDeserializer.findDeserializer(URL.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser p = jsonFactory.createParser("\"http://www.example.com\"");

        URL result = urlDeserializer.deserialize(p, ctxt);
        assertEquals("http://www.example.com", result.toString());
    }

    @Test
    public void testDeserializeUri() throws IOException {
        FromStringDeserializer<URI> uriDeserializer = FromStringDeserializer.findDeserializer(URI.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser p = jsonFactory.createParser("\"http://www.example.com/path/to/resource\"");

        URI result = uriDeserializer.deserialize(p, ctxt);
        assertEquals("http", result.getScheme());
        assertEquals("/path/to/resource", result.getPath());
    }

    @Test
    public void testDeserializeClass() throws IOException {
        FromStringDeserializer<Class<?>> classDeserializer = FromStringDeserializer.findDeserializer(Class.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser p = jsonFactory.createParser("java.lang.String");

        Class<?> result = classDeserializer.deserialize(p, ctxt);
        assertEquals(String.class, result);
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        FromStringDeserializer<JavaType> javaTypeDeserializer = FromStringDeserializer.findDeserializer(JavaType.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser p = jsonFactory.createParser("com.example.Foo");

        JavaType result = javaTypeDeserializer.deserialize(p, ctxt);
        TypeFactory typeFactory = ctxt.getTypeFactory();
        assertEquals(typeFactory.constructType(Foo.class), result);
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        FromStringDeserializer<Currency> currencyDeserializer = FromStringDeserializer.findDeserializer(Currency.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser p = jsonFactory.createParser("USD");

        Currency result = currencyDeserializer.deserialize(p, ctxt);
        assertEquals(Currency.getInstance("USD"), result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidPattern() throws IOException {
        FromStringDeserializer<Pattern> patternDeserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser p = jsonFactory.createParser("invalidRegex");

        patternDeserializer.deserialize(p, ctxt);
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        FromStringDeserializer<Locale> localeDeserializer = FromStringDeserializer.findDeserializer(Locale.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser p = jsonFactory.createParser("en_US");

        Locale result = localeDeserializer.deserialize(p, ctxt);
        assertEquals(Locale.US, result);
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        FromStringDeserializer<Charset> charsetDeserializer = FromStringDeserializer.findDeserializer(Charset.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser p = jsonFactory.createParser("UTF-8");

        Charset result = charsetDeserializer.deserialize(p, ctxt);
        assertEquals(Charset.forName("UTF-8"), result);
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        FromStringDeserializer<TimeZone> timeZoneDeserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser p = jsonFactory.createParser("UTC");

        TimeZone result = timeZoneDeserializer.deserialize(p, ctxt);
        assertEquals(TimeZone.getTimeZone("UTC"), result);
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        FromStringDeserializer<InetAddress> inetAddressDeserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser p = jsonFactory.createParser("192.168.0.1");

        InetAddress result = inetAddressDeserializer.deserialize(p, ctxt);
        assertEquals(InetAddress.getByName("192.168.0.1"), result);
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        FromStringDeserializer<InetSocketAddress> inetSocketAddressDeserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser p = jsonFactory.createParser("[192.168.0.1]:80");

        InetSocketAddress result = (InetSocketAddress) inetSocketAddressDeserializer.deserialize(p, ctxt);
        assertEquals(new InetSocketAddress(InetAddress.getByName("192.168.0.1"), 80), result);
    }

    @Test
    public void testDeserializeStringBuilder() throws IOException {
        FromStringDeserializer<StringBuilder> stringBuilderDeserializer = FromStringDeserializer.findDeserializer(StringBuilder.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser p = jsonFactory.createParser("\"Hello, World!\"");

        StringBuilder result = stringBuilderDeserializer.deserialize(p, ctxt);
        assertEquals("Hello, World!", result.toString());
    }

    @Test
    public void testDeserializeEmptyString() throws IOException {
        FromStringDeserializer<String> stringDeserializer = FromStringDeserializer.findDeserializer(String.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser p = jsonFactory.createParser("\"\"");

        String result = stringDeserializer.deserialize(p, ctxt);
        assertNull(result);
    }

    @Test
    public void testDeserializeNull() throws IOException {
        FromStringDeserializer<String> stringDeserializer = FromStringDeserializer.findDeserializer(String.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser p = jsonFactory.createParser("null");

        String result = stringDeserializer.deserialize(p, ctxt);
        assertNull(result);
    }

    @Test
    public void testDeserializeEmptyArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser p = jsonFactory.createParser("[ ]");

        assertNull(FromStringDeserializer.findDeserializer(String.class).deserialize(p, ctxt));
    }

    @Test
    public void testDeserializeEmbeddedObject() throws IOException {
        FromStringDeserializer<Object> objectDeserializer = FromStringDeserializer.findDeserializer(Object.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser p = jsonFactory.createParser("{ \"value\": 123 }");

        Object result = objectDeserializer.deserialize(p, ctxt);
        assertEquals(123, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidEmbeddedObject() throws IOException {
        FromStringDeserializer<Object> objectDeserializer = FromStringDeserializer.findDeserializer(Object.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser p = jsonFactory.createParser("{ \"value\": 123 }");

        Object result = objectDeserializer._deserializeEmbedded(p.getValueAsObject(), ctxt);
    }
}