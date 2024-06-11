package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

public class FromStringDeserializerTest {
    
    @Test
    public void testDeserializeFile() throws Exception {
        FromStringDeserializer.Std fileDeserializer = (FromStringDeserializer.Std) FromStringDeserializer.Std.findDeserializer(File.class);
        
        File file = (File) fileDeserializer.deserialize(null, null);
        assertNull(file);
        
        file = (File) fileDeserializer.deserialize(null, createDeserializationContext("\"test.txt\""));
        assertEquals("test.txt", file.getName());
    }
    
    @Test
    public void testDeserializeURL() throws Exception {
        FromStringDeserializer.Std urlDeserializer = (FromStringDeserializer.Std) FromStringDeserializer.Std.findDeserializer(URL.class);
        
        URL url = (URL) urlDeserializer.deserialize(null, null);
        assertNull(url);
        
        url = (URL) urlDeserializer.deserialize(null, createDeserializationContext("\"http://www.example.com\""));
        assertEquals("http://www.example.com", url.toString());
    }
    
    @Test
    public void testDeserializeURI() throws Exception {
        FromStringDeserializer.Std uriDeserializer = (FromStringDeserializer.Std) FromStringDeserializer.Std.findDeserializer(URI.class);
        
        URI uri = (URI) uriDeserializer.deserialize(null, null);
        assertNull(uri);
        
        uri = (URI) uriDeserializer.deserialize(null, createDeserializationContext("\"http://www.example.com\""));
        assertEquals("http://www.example.com", uri.toString());
    }
    
    @Test
    public void testDeserializeClass() throws Exception {
        FromStringDeserializer.Std classDeserializer = (FromStringDeserializer.Std) FromStringDeserializer.Std.findDeserializer(Class.class);
        
        Class<?> clazz = (Class<?>) classDeserializer.deserialize(null, null);
        assertNull(clazz);
        
        clazz = (Class<?>) classDeserializer.deserialize(null, createDeserializationContext("\"java.lang.String\""));
        assertEquals(String.class, clazz);
    }
    
    @Test
    public void testDeserializeCurrency() throws Exception {
        FromStringDeserializer.Std currencyDeserializer = (FromStringDeserializer.Std) FromStringDeserializer.Std.findDeserializer(Currency.class);
        
        Currency currency = (Currency) currencyDeserializer.deserialize(null, null);
        assertNull(currency);
        
        currency = (Currency) currencyDeserializer.deserialize(null, createDeserializationContext("\"USD\""));
        assertEquals("USD", currency.getCurrencyCode());
    }
    
    @Test(expected = InvalidFormatException.class)
    public void testDeserializeCurrencyWithInvalidValue() throws Exception {
        FromStringDeserializer.Std currencyDeserializer = (FromStringDeserializer.Std) FromStringDeserializer.Std.findDeserializer(Currency.class);
        
        currencyDeserializer.deserialize(null, createDeserializationContext("\"XYZ\""));
    }
    
    // complete the test cases for the remaining types
    // ...
    
    private DeserializationContext createDeserializationContext(String input) throws Exception {
        JsonParser parser = new DummyJsonParser(input);
        return new DummyDeserializationContext(parser);
    }
}

class DummyJsonParser extends JsonParser {
    
    private String value;
    
    public DummyJsonParser(String value) {
        this.value = value;
    }

    @Override
    public String getValueAsString() {
        return value;
    }
    
    // implement the remaining methods
    // ...
}

class DummyDeserializationContext extends DeserializationContext {
    
    private JsonParser parser;
    
    public DummyDeserializationContext(JsonParser parser) {
        this.parser = parser;
    }

    @Override
    public JsonParser getParser() {
        return parser;
    }
    
    // implement the remaining methods
    // ...
}