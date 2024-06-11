package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.DeserializationContext;

import org.junit.Before;
import org.junit.Test;

public class FromStringDeserializerTest {
    
    private JsonParser jsonParser;
    private DeserializationContext deserializationContext;
    
    @Before
    public void setUp() throws IOException {
        jsonParser = mock(JsonParser.class);
        deserializationContext = mock(DeserializationContext.class);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
    }
    
    @Test
    public void testStdFile() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        when(jsonParser.getValueAsString()).thenReturn("/path/to/file.txt");
        
        File result = (File) deserializer.deserialize(jsonParser, deserializationContext);
        
        assertEquals(new File("/path/to/file.txt"), result);
    }
    
    @Test
    public void testStdURL() throws IOException, MalformedURLException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        when(jsonParser.getValueAsString()).thenReturn("http://www.example.com");
        
        URL result = (URL) deserializer.deserialize(jsonParser, deserializationContext);
        
        assertEquals(new URL("http://www.example.com"), result);
    }
    
    @Test
    public void testStdURI() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        when(jsonParser.getValueAsString()).thenReturn("http://www.example.com");
        
        URI result = (URI) deserializer.deserialize(jsonParser, deserializationContext);
        
        assertEquals(URI.create("http://www.example.com"), result);
    }
    
    @Test
    public void testStdClass() throws IOException, ClassNotFoundException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        when(jsonParser.getValueAsString()).thenReturn("java.lang.String");
        when(deserializationContext.findClass("java.lang.String")).thenReturn((Class<?>) String.class);
        
        Class<?> result = (Class<?>) deserializer.deserialize(jsonParser, deserializationContext);
        
        assertEquals(String.class, result);
    }
    
    @Test
    public void testStdCurrency() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        when(jsonParser.getValueAsString()).thenReturn("USD");
        
        Currency result = (Currency) deserializer.deserialize(jsonParser, deserializationContext);
        
        assertEquals(Currency.getInstance("USD"), result);
    }
    
    @Test
    public void testStdPattern() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        when(jsonParser.getValueAsString()).thenReturn("[a-z]+");
        
        Pattern result = (Pattern) deserializer.deserialize(jsonParser, deserializationContext);
        
        assertEquals(Pattern.compile("[a-z]+"), result);
    }
    
    @Test
    public void testStdLocale() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        when(jsonParser.getValueAsString()).thenReturn("en");
        
        Locale result = (Locale) deserializer.deserialize(jsonParser, deserializationContext);
        
        assertEquals(new Locale("en"), result);
    }
    
    @Test
    public void testStdCharset() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        when(jsonParser.getValueAsString()).thenReturn("UTF-8");
        
        Charset result = (Charset) deserializer.deserialize(jsonParser, deserializationContext);
        
        assertEquals(Charset.forName("UTF-8"), result);
    }
    
    @Test
    public void testStdTimeZone() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        when(jsonParser.getValueAsString()).thenReturn("America/Los_Angeles");
        
        TimeZone result = (TimeZone) deserializer.deserialize(jsonParser, deserializationContext);
        
        assertEquals(TimeZone.getTimeZone("America/Los_Angeles"), result);
    }
    
    @Test
    public void testStdInetAddress() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        when(jsonParser.getValueAsString()).thenReturn("127.0.0.1");
        
        InetAddress result = (InetAddress) deserializer.deserialize(jsonParser, deserializationContext);
        
        assertEquals(InetAddress.getByName("127.0.0.1"), result);
    }
    
    @Test
    public void testStdInetSocketAddress() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        when(jsonParser.getValueAsString()).thenReturn("localhost:8080");
        
        InetSocketAddress result = (InetSocketAddress) deserializer.deserialize(jsonParser, deserializationContext);
        
        assertEquals(new InetSocketAddress("localhost", 8080), result);
    }
    
    @Test
    public void testStdInvalidDataType() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InputStream.class, 13);
        
        Exception exception = null;
        try {
            deserializer.deserialize(jsonParser, deserializationContext);
        } catch (IllegalArgumentException e) {
            exception = e;
        }
        
        assertEquals(IllegalArgumentException.class, exception.getClass());
    }
    
    @Test
    public void testStdDeserializeEmptyStringForURI() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        when(jsonParser.getValueAsString()).thenReturn("");
        
        URI result = (URI) deserializer.deserialize(jsonParser, deserializationContext);
        
        assertEquals(URI.create(""), result);
    }
    
    @Test
    public void testStdDeserializeEmptyStringForLocale() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        when(jsonParser.getValueAsString()).thenReturn("");
        
        Locale result = (Locale) deserializer.deserialize(jsonParser, deserializationContext);
        
        assertEquals(Locale.ROOT, result);
    }
    
    @Test
    public void testStdDeserializeEmptyStringForOtherTypes() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(String.class, FromStringDeserializer.Std.STD_CLASS);
        when(jsonParser.getValueAsString()).thenReturn("");
        
        String result = (String) deserializer.deserialize(jsonParser, deserializationContext);
        
        assertNull(result);
    }

}