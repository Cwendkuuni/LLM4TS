package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.Locale;
import java.util.UUID;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;

/**
 * Test file for StdKeyDeserializer class
 */
@JacksonStdImpl
public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKeyBoolean() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mock(DeserializationContext.class);
        when(context.getParser()).thenReturn(mapper.getFactory().createParser("true"));

        Object result = deserializer.deserializeKey("true", context);
        assertEquals(Boolean.TRUE, result);

        when(context.getParser()).thenReturn(mapper.getFactory().createParser("false"));
        result = deserializer.deserializeKey("false", context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testDeserializeKeyByte() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mock(DeserializationContext.class);
        when(context.getParser()).thenReturn(mapper.getFactory().createParser("127"));

        Object result = deserializer.deserializeKey("127", context);
        assertEquals((byte) 127, result);
    }

    @Test
    public void testDeserializeKeyShort() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Short.class);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mock(DeserializationContext.class);
        when(context.getParser()).thenReturn(mapper.getFactory().createParser("32767"));

        Object result = deserializer.deserializeKey("32767", context);
        assertEquals((short) 32767, result);
    }

    @Test
    public void testDeserializeKeyChar() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mock(DeserializationContext.class);
        when(context.getParser()).thenReturn(mapper.getFactory().createParser("A"));

        Object result = deserializer.deserializeKey("A", context);
        assertEquals('A', result);
    }

    @Test
    public void testDeserializeKeyInt() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mock(DeserializationContext.class);
        when(context.getParser()).thenReturn(mapper.getFactory().createParser("42"));

        Object result = deserializer.deserializeKey("42", context);
        assertEquals(42, result);
    }

    @Test
    public void testDeserializeKeyLong() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mock(DeserializationContext.class);
        when(context.getParser()).thenReturn(mapper.getFactory().createParser("9223372036854775807"));

        Object result = deserializer.deserializeKey("9223372036854775807", context);
        assertEquals(9223372036854775807L, result);
    }

    @Test
    public void testDeserializeKeyFloat() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Float.class);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mock(DeserializationContext.class);
        when(context.getParser()).thenReturn(mapper.getFactory().createParser("3.14"));

        Object result = deserializer.deserializeKey("3.14", context);
        assertEquals(3.14f, result);
    }

    @Test
    public void testDeserializeKeyDouble() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Double.class);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mock(DeserializationContext.class);
        when(context.getParser()).thenReturn(mapper.getFactory().createParser("3.14159265358979323846"));

        Object result = deserializer.deserializeKey("3.14159265358979323846", context);
        assertEquals(3.14159265358979323846, result, 0.0000001);
    }

    @Test
    public void testDeserializeKeyLocale() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Locale.class);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mock(DeserializationContext.class);
        Locale locale = Locale.forLanguageTag("en-US");
        when(context.getParser()).thenReturn(mapper.getFactory().createParser("en-US"));

        Object result = deserializer.deserializeKey("en-US", context);
        assertEquals(locale, result);
    }

    @Test
    public void testDeserializeKeyCurrency() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Currency.class);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mock(DeserializationContext.class);
        Currency currency = Currency.getInstance("USD");
        when(context.getParser()).thenReturn(mapper.getFactory().createParser("USD"));

        Object result = deserializer.deserializeKey("USD", context);
        assertEquals(currency, result);
    }

    @Test
    public void testDeserializeKeyDate() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Date.class);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mock(DeserializationContext.class);
        Date date = new Date();
        when(context.parseDate("2022-12-31")).thenReturn(date);

        Object result = deserializer.deserializeKey("2022-12-31", context);
        assertEquals(date, result);
    }

    @Test
    public void testDeserializeKeyCalendar() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Calendar.class);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mock(DeserializationContext.class);
        Calendar calendar = Calendar.getInstance();
        when(context.constructCalendar(mapper.getFactory().parseDate("2022-12-31"))).thenReturn(calendar);

        Object result = deserializer.deserializeKey("2022-12-31", context);
        assertEquals(calendar, result);
    }

    @Test
    public void testDeserializeKeyURI() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URI.class);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mock(DeserializationContext.class);
        URI uri = new URI("https://www.example.com");
        when(context.createUri(mapper.getFactory().createParser("https://www.example.com"))).thenReturn(uri);

        Object result = deserializer.deserializeKey("https://www.example.com", context);
        assertEquals(uri, result);
    }

    @Test
    public void testDeserializeKeyURL() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URL.class);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mock(DeserializationContext.class);
        URL url = new URL("https://www.example.com");
        when(context.findValueAsURL(mapper.getFactory().createParser("https://www.example.com"))).thenReturn(url);

        Object result = deserializer.deserializeKey("https://www.example.com", context);
        assertEquals(url, result);
    }

    @Test
    public void testDeserializeKeyClass() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mock(DeserializationContext.class);
        String className = String.class.getName();
        when(context.findClass(mapper.getFactory().createParser(className))).thenReturn(String.class);

        Object result = deserializer.deserializeKey(className, context);
        assertEquals(String.class, result);
    }

    @Test
    public void testDeserializeKeyUnknownType() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(UUID.class);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mock(DeserializationContext.class);
        when(context.getParser()).thenReturn(mapper.getFactory().createParser("some-uuid"));

        assertThrows(JsonMappingException.class, () -> deserializer.deserializeKey("some-uuid", context));
    }
}