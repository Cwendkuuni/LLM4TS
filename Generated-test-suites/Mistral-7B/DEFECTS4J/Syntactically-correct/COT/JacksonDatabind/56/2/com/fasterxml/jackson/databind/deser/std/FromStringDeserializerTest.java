package com.example.junit4; // Replace 'com.example.junit4' with your test package

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapperFactory;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class FromStringDeserializerTest {

    @Test
    public void testTypes() {
        Class<?>[] types = FromStringDeserializer.types();
        assertEquals(12, types.length);
        for (Class<?> type : types) {
            assertTrue(type.isPrimitive() || type.isAssignableFrom(File.class) ||
                    type.isAssignableFrom(URL.class) || type.isAssignableFrom(URI.class) ||
                    type.isAssignableFrom(Class.class) || type.isAssignableFrom(JavaType.class) ||
                    type.isAssignableFrom(Currency.class) || type.isAssignableFrom(Pattern.class) ||
                    type.isAssignableFrom(Locale.class) || type.isAssignableFrom(Charset.class) ||
                    type.isAssignableFrom(TimeZone.class) || type.isAssignableFrom(InetAddress.class) ||
                    type.isAssignableFrom(InetSocketAddress.class));
        }
    }

    @Test
    public void testDeserialize() throws IOException {
        ObjectMapper mapper = JsonMapperFactory.getJackson2ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();

        JsonParser p = factory.createParser("\"file.txt\"");
        FromStringDeserializer<File> fileDeserializer = FromStringDeserializer.findDeserializer(File.class);
        File file = fileDeserializer.deserialize(p, ctxt);
        assertEquals(File.class, file.getClass());
        assertNotNull(file);

        p = factory.createParser("\"http://example.com\"");
        FromStringDeserializer<URL> urlDeserializer = FromStringDeserializer.findDeserializer(URL.class);
        URL url = urlDeserializer.deserialize(p, ctxt);
        assertEquals(URL.class, url.getClass());
        assertNotNull(url);

        p = factory.createParser("\"URI(scheme:http, authority:example.com, path:/path)\"");
        FromStringDeserializer<URI> uriDeserializer = FromStringDeserializer.findDeserializer(URI.class);
        URI uri = uriDeserializer.deserialize(p, ctxt);
        assertEquals(URI.class, uri.getClass());
        assertNotNull(uri);

        p = factory.createParser("\"java.util.Currency:USD\"");
        FromStringDeserializer<Currency> currencyDeserializer = FromStringDeserializer.findDeserializer(Currency.class);
        Currency currency = currencyDeserializer.deserialize(p, ctxt);
        assertEquals(Currency.class, currency.getClass());
        assertNotNull(currency);

        // Test deserialization with empty string
        p = factory.createParser("\"\"");
        FromStringDeserializer<String> stringDeserializer = FromStringDeserializer.findDeserializer(String.class);
        String result = (String) stringDeserializer.deserialize(p, ctxt);
        assertNull(result);
    }

    @Test
    public void testDeserializeWithArrayWrapper() throws IOException {
        ObjectMapper mapper = JsonMapperFactory.getJackson2ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();

        JsonParser p = factory.createParser("[\"file.txt\"]");
        FromStringDeserializer<File> fileDeserializer = FromStringDeserializer.findDeserializer(File.class);
        File[] files = (File[]) fileDeserializer.deserialize(p, ctxt);
        assertEquals(1, files.length);
        assertEquals(File.class, files[0].getClass());
        assertNotNull(files[0]);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidTextualRepresentation() throws IOException {
        ObjectMapper mapper = JsonMapperFactory.getJackson2ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();

        JsonParser p = factory.createParser("\"invalidTextualRepresentation\"");
        FromStringDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(Object.class);
        deserializer.deserialize(p, ctxt);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeUnsupportedType() throws IOException {
        ObjectMapper mapper = JsonMapperFactory.getJackson2ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();

        JsonParser p = factory.createParser("\"java.util.ArrayList:[1, 2, 3]\"");
        FromStringDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(Object.class);
        deserializer.deserialize(p, ctxt);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidFormatException() throws IOException {
        ObjectMapper mapper = JsonMapperFactory.getJackson2ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();

        JsonParser p = factory.createParser("\"java.util.TimeZone:InvalidTimeZone\"");
        FromStringDeserializer<TimeZone> timeZoneDeserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        timeZoneDeserializer.deserialize(p, ctxt);
    }

    @Test(expected = InvalidFormatException.class)
    public void testDeserializeInvalidInetSocketAddress() throws IOException {
        ObjectMapper mapper = JsonMapperFactory.getJackson2ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();

        JsonParser p = factory.createParser("\"[256.0.0.1:80]\"");
        FromStringDeserializer<InetSocketAddress> inetSocketAddressDeserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        inetSocketAddressDeserializer.deserialize(p, ctxt);
    }
}