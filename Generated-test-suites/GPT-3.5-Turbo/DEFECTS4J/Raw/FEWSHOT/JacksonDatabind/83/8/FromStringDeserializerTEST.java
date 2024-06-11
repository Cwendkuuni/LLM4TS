
package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FromStringDeserializerTest {

    private static final String EMPTY_STRING = "";

    private FromStringDeserializer deserializer;
    private JsonParser mockJsonParser;
    private DeserializationContext mockDeserializationContext;

    @Before
    public void setup() {
        deserializer = Mockito.spy(FromStringDeserializer.Std.class);
        mockJsonParser = mock(JsonParser.class);
        mockDeserializationContext = mock(DeserializationContext.class);
    }

    @Test
    public void testDeserializeFromFile() throws IOException {
        File file = new File("testFile.txt");
        when(mockJsonParser.getValueAsString()).thenReturn(file.getPath());
        Object result = deserializer.deserialize(mockJsonParser, mockDeserializationContext);
        assertTrue(result instanceof File);
        assertEquals(file, result);
    }

    @Test
    public void testDeserializeFromURL() throws IOException {
        URL url = new URL("http://www.example.com");
        when(mockJsonParser.getValueAsString()).thenReturn(url.toString());
        Object result = deserializer.deserialize(mockJsonParser, mockDeserializationContext);
        assertTrue(result instanceof URL);
        assertEquals(url, result);
    }

    @Test
    public void testDeserializeFromURI() throws IOException {
        URI uri = URI.create("http://www.example.com");
        when(mockJsonParser.getValueAsString()).thenReturn(uri.toString());
        Object result = deserializer.deserialize(mockJsonParser, mockDeserializationContext);
        assertTrue(result instanceof URI);
        assertEquals(uri, result);
    }

    @Test
    public void testDeserializeFromClass() throws IOException, ClassNotFoundException {
        Class<?> clazz = getClass();
        when(mockJsonParser.getValueAsString()).thenReturn(clazz.getName());
        JavaType javaType = mock(JavaType.class);
        when(mockDeserializationContext.findClass(clazz.getName())).thenReturn(clazz);
        when(mockDeserializationContext.handleInstantiationProblem(clazz, clazz.getName(), null))
                .thenReturn(clazz);
        when(mockDeserializationContext.getTypeFactory()).thenReturn(mock(JavaType.class));
        when(mockDeserializationContext.getTypeFactory().constructFromCanonical(clazz.getName()))
                .thenReturn(javaType);

        Object result = deserializer.deserialize(mockJsonParser, mockDeserializationContext);
        assertTrue(result instanceof Class);
        assertEquals(clazz, result);

        Object result2 = deserializer.deserialize(mockJsonParser, mockDeserializationContext);
        assertTrue(result2 instanceof JavaType);
        assertEquals(javaType, result2);
    }

    @Test
    public void testDeserializeFromCurrency() throws IOException {
        Currency currency = Currency.getInstance("USD");
        when(mockJsonParser.getValueAsString()).thenReturn(currency.toString());

        Object result = deserializer.deserialize(mockJsonParser, mockDeserializationContext);
        assertTrue(result instanceof Currency);
        assertEquals(currency, result);
    }

    @Test
    public void testDeserializeFromPattern() throws IOException {
        String regex = "\\d+";
        Pattern pattern = Pattern.compile(regex);
        when(mockJsonParser.getValueAsString()).thenReturn(regex);

        Object result = deserializer.deserialize(mockJsonParser, mockDeserializationContext);
        assertTrue(result instanceof Pattern);
        assertEquals(pattern.pattern(), ((Pattern) result).pattern());
    }

    @Test
    public void testDeserializeFromLocale() throws IOException {
        Locale locale = new Locale("en", "US");
        when(mockJsonParser.getValueAsString()).thenReturn(locale.toString());

        Object result = deserializer.deserialize(mockJsonParser, mockDeserializationContext);
        assertTrue(result instanceof Locale);
        assertEquals(locale, result);
    }

    @Test
    public void testDeserializeFromCharset() throws IOException {
        Charset charset = Charset.forName("UTF-8");
        when(mockJsonParser.getValueAsString()).thenReturn(charset.toString());

        Object result = deserializer.deserialize(mockJsonParser, mockDeserializationContext);
        assertTrue(result instanceof Charset);
        assertEquals(charset, result);
    }

    @Test
    public void testDeserializeFromTimeZone() throws IOException {
        TimeZone timeZone = TimeZone.getTimeZone("America/New_York");
        when(mockJsonParser.getValueAsString()).thenReturn(timeZone.toString());

        Object result = deserializer.deserialize(mockJsonParser, mockDeserializationContext);
        assertTrue(result instanceof TimeZone);
        assertEquals(timeZone, result);
    }

    @Test
    public void testDeserializeFromInetAddress() throws IOException {
        InetAddress inetAddress = InetAddress.getByName("localhost");
        when(mockJsonParser.getValueAsString()).thenReturn(inetAddress.toString());

        Object result = deserializer.deserialize(mockJsonParser, mockDeserializationContext);
        assertTrue(result instanceof InetAddress);
        assertEquals(inetAddress, result);
    }

    @Test
    public void testDeserializeFromInetSocketAddress() throws IOException {
        InetSocketAddress inetSocketAddress = new InetSocketAddress("localhost", 8080);
        when(mockJsonParser.getValueAsString()).thenReturn(inetSocketAddress.toString());

        Object result = deserializer.deserialize(mockJsonParser, mockDeserializationContext);
        assertTrue(result instanceof InetSocketAddress);
        assertEquals(inetSocketAddress, result);
    }

    @Test
    public void testDeserializeFromStringBuilder() throws IOException {
        StringBuilder stringBuilder = new StringBuilder("Test");
        when(mockJsonParser.getValueAsString()).thenReturn(stringBuilder.toString());

        Object result = deserializer.deserialize(mockJsonParser, mockDeserializationContext);
        assertTrue(result instanceof StringBuilder);
        assertEquals(stringBuilder.toString(), result.toString());
    }

    @Test
    public void testDeserializeWithEmptyString() throws IOException {
        when(mockJsonParser.getValueAsString()).thenReturn(EMPTY_STRING);

        Object result = deserializer.deserialize(mockJsonParser, mockDeserializationContext);
        assertNull(result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithInvalidValue() throws IOException {
        when(mockJsonParser.getValueAsString()).thenReturn("Invalid");

        deserializer.deserialize(mockJsonParser, mockDeserializationContext);
    }
}
