package com.example.test; // replace with your test package

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.TypeFactory;

@RunWith(MockitoJUnitRunner.class)
public class FromStringDeserializerTest {
    @InjectMocks
    private FromStringDeserializer<Object> fromStringDeserializer;

    private ObjectMapper objectMapper;

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeValidStrings() throws IOException {
        objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(URI.class, new FromStringDeserializer<URI>(URI.class));
        module.addSerializer(URL.class, new FromStringDeserializer<URL>(URL.class));
        module.addSerializer(Locale.class, new FromStringDeserializer<Locale>(Locale.class));
        module.addSerializer(TimeZone.class, new FromStringDeserializer<TimeZone>(TimeZone.class));
        module.addSerializer(Currency.class, new FromStringDeserializer<Currency>(Currency.class));
        module.addSerializer(Charset.class, new FromStringDeserializer<Charset>(Charset.class));
        module.addSerializer(InetAddress.class, new FromStringDeserializer<InetAddress>(InetAddress.class));
        module.addSerializer(InetSocketAddress.class, new FromStringDeserializer<InetSocketAddress>(InetSocketAddress.class));
        objectMapper.registerModule(module);

        String jsonString = "{\"value\":\"file:/path/to/file.txt\"}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonString);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        Object value = fromStringDeserializer.deserialize(jsonParser, context);
        assertEquals(new File("/path/to/file.txt"), value);

        jsonString = "{\"value\":\"http://example.com\"}";
        jsonParser = objectMapper.getFactory().createParser(jsonString);
        value = fromStringDeserializer.deserialize(jsonParser, context);
        assertEquals(new URL("http://example.com"), value);

        jsonString = "{\"value\":\"http://example.com/path?query=param\"}";
        jsonParser = objectMapper.getFactory().createParser(jsonString);
        value = fromStringDeserializer.deserialize(jsonParser, context);
        assertEquals(new URI("http://example.com/path?query=param"), value);

        jsonString = "{\"value\":\"java.util.Locale.ENGLISH\"}";
        jsonParser = objectMapper.getFactory().createParser(jsonString);
        value = fromStringDeserializer.deserialize(jsonParser, context);
        assertEquals(Locale.ENGLISH, value);

        jsonString = "{\"value\":\"Europe/London\"}";
        jsonParser = objectMapper.getFactory().createParser(jsonString);
        value = fromStringDeserializer.deserialize(jsonParser, context);
        assertEquals(TimeZone.getTimeZone("Europe/London"), value);

        jsonString = "{\"value\":\"USD\"}";
        jsonParser = objectMapper.getFactory().createParser(jsonString);
        value = fromStringDeserializer.deserialize(jsonParser, context);
        assertEquals(Currency.getInstance("USD"), value);

        jsonString = "{\"value\":\"UTF-8\"}";
        jsonParser = objectMapper.getFactory().createParser(jsonString);
        value = fromStringDeserializer.deserialize(jsonParser, context);
        assertEquals(Charset.forName("UTF-8"), value);

        jsonString = "{\"value\":\"192.168.1.1:80\"}";
        jsonParser = objectMapper.getFactory().createParser(jsonString);
        value = fromStringDeserializer.deserialize(jsonParser, context);
        assertEquals(new InetSocketAddress("192.168.1.1", 80), value);

        jsonString = "{\"value\":\"[::1]:8080\"}";
        jsonParser = objectMapper.getFactory().createParser(jsonString);
        value = fromStringDeserializer.deserialize(jsonParser, context);
        assertEquals(new InetSocketAddress(InetAddress.getByName("::1"), 8080), value);

        jsonString = "{\"value\":\"java.util.Locale.CHINESE\"}";
        jsonParser = objectMapper.getFactory().createParser(jsonString);
        Mockito.when(context.findClass(Locale.CHINESE.toString()))
                .thenReturn(Locale.class);
        value = fromStringDeserializer.deserialize(jsonParser, context);
        assertEquals(Locale.CHINESE, value);

        jsonString = "{\"value\":\"java.util.Locale.ITALIAN\"_IT\"}";
        jsonParser = objectMapper.getFactory().createParser(jsonString);
        value = fromStringDeserializer.deserialize(jsonParser, context);
        assertEquals(new Locale("it", "IT"), value);

        jsonString = "{\"value\":\"java.net.URL\".toLowerCase()+\"!invalid\"]}";
        jsonParser = objectMapper.getFactory().createParser(jsonString);
        Mockito.when(context.findClass(JsonMappingException.class.getName()))
                .thenReturn(JsonMappingException.class);
        try {
            fromStringDeserializer.deserialize(jsonParser, context);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Success
        }

        jsonString = "{\"value\":\"java.util.TimeZone.UTC\"}";
        jsonParser = objectMapper.getFactory().createParser(jsonString);
        value = fromStringDeserializer.deserialize(jsonParser, context);
        assertEquals(TimeZone.getTimeZone("UTC"), value);

        jsonString = "{\"value\":\"java.net.URI\".toLowerCase()+\"!invalid\"}";
        jsonParser = objectMapper.getFactory().createParser(jsonString);
        Mockito.when(context.findClass(JsonMappingException.class.getName()))
                .thenReturn(JsonMappingException.class);
        try {
            fromStringDeserializer.deserialize(jsonParser, context);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Success
        }
    }

    @Test
    public void testDeserializeEmptyStrings() throws IOException {
        String jsonString = "{\"value\":\"\"}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonString);
        Object value = fromStringDeserializer.deserialize(jsonParser, context);
        assertEquals(null, value);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidStrings() throws IOException {
        String jsonString = "{\"value\":\"invalid\"}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonString);
        fromStringDeserializer.deserialize(jsonParser, context);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeNullStrings() throws IOException {
        String jsonString = "{\"value\":null}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonString);
        fromStringDeserializer.deserialize(jsonParser, context);
    }

    @Test
    public void testDeserializeInvalidTypes() throws IOException {
        String jsonString = "{\"value\":3}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonString);
        Mockito.when(context.isEnabled(Mockito.eq(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)))
                .thenReturn(true);
        Mockito.when(context.findValueSerializer(URI.class)).thenReturn(null);
        try {
            Object value = fromStringDeserializer.deserialize(jsonParser, context);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Success
        }

        jsonString = "{\"value\":3}";
        jsonParser = objectMapper.getFactory().createParser(jsonString);
        Mockito.when(context.isEnabled(Mockito.eq(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)))
                .thenReturn(false);
        try {
            Object value = fromStringDeserializer.deserialize(jsonParser, context);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Success
        }

        jsonString = "{\"value\":[3]}";
        jsonParser = objectMapper.getFactory().createParser(jsonString);
        Mockito.when(context.isEnabled(Mockito.eq(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)))
                .thenReturn(true);
        Mockito.when(context.findValueSerializer(URI.class)).thenReturn(null);
        try {
            Object value = fromStringDeserializer.deserialize(jsonParser, context);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Success
        }

        jsonString = "{\"value\":[3]}";
        jsonParser = objectMapper.getFactory().createParser(jsonString);
        Mockito.when(context.isEnabled(Mockito.eq(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)))
                .thenReturn(false);
        try {
            Object value = fromStringDeserializer.deserialize(jsonParser, context);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Success
        }

        jsonString = "{\"value\":\"invalidType\"}";
        jsonParser = objectMapper.getFactory().createParser(jsonString);
        try {
            Object value = fromStringDeserializer.deserialize(jsonParser, context);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Success
        }
    }

    @Test
    public void testDeserializeArray() throws IOException {
        String jsonString = "{\"value\":[\"file:/path/to/file.txt\",\"http://example.com\"]}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonString);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        Mockito.when(context.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS))
                .thenReturn(true);
        Object value = fromStringDeserializer.deserialize(jsonParser, context);
        assertEquals(new Object[] {new File("/path/to/file.txt"), new URL("http://example.com")}, value);

        jsonString = "{\"value\":[\"file:/path/to/file.txt\",\"http://example.com\"]}";
        jsonParser = objectMapper.getFactory().createParser(jsonString);
        Mockito.when(context.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS))
                .thenReturn(false);
        try {
            Object value = fromStringDeserializer.deserialize(jsonParser, context);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Success
        }
    }
}