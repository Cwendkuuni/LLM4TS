package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

public class FromStringDeserializerTest {
    @Test
    public void testDeserialize() throws IOException {
        //File type
        assertEquals(new File("test"), new FromStringDeserializer.Std.Std(FromFileDeserializer.File.class, 1)._deserialize("test", null));
        
        //URL type
        assertEquals(new URL("http://www.example.com"), new FromStringDeserializer.Std.Std(FromFileDeserializer.URL.class, 2)._deserialize("http://www.example.com", null));
        
        //URI type
        assertEquals(URI.create("http://www.example.com"), new FromStringDeserializer.Std.Std(FromFileDeserializer.URI.class, 3)._deserialize("http://www.example.com", null));
        
        //Class type
        assertEquals(String.class, new FromStringDeserializer.Std.Std(FromFileDeserializer.Class.class, 4)._deserialize("java.lang.String", null));
        
        //JavaType type
        //Todo: how to test this?
        
        //Currency type
        assertEquals(Currency.getInstance("GBP"), new FromStringDeserializer.Std.Std(FromFileDeserializer.Currency.class, 6)._deserialize("GBP", null));
        
        //Pattern type
        assertEquals(Pattern.compile(".*"), new FromStringDeserializer.Std.Std(FromFileDeserializer.Pattern.class, 7)._deserialize(".*", null));
        
        //Locale type
        assertEquals(new Locale("en", "US"), new FromStringDeserializer.Std.Std(FromFileDeserializer.Locale.class, 8)._deserialize("en-US", null));
        
        //Charset type
        assertEquals(Charset.forName("UTF-8"), new FromStringDeserializer.Std.Std(FromFileDeserializer.Charset.class, 9)._deserialize("UTF-8", null));
        
        //TimeZone type
        assertEquals(TimeZone.getTimeZone("GMT"), new FromStringDeserializer.Std.Std(FromFileDeserializer.TimeZone.class, 10)._deserialize("GMT", null));
        
        //InetAddress type
        assertEquals(InetAddress.getByName("127.0.0.1"), new FromStringDeserializer.Std.Std(FromFileDeserializer.InetAddress.class, 11)._deserialize("127.0.0.1", null));
        
        //InetSocketAddress type
        assertEquals(new InetSocketAddress("localhost", 8080), new FromStringDeserializer.Std.Std(FromFileDeserializer.InetSocketAddress.class, 12)._deserialize("localhost:8080", null));
        
        //StringBuilder type
        assertEquals(new StringBuilder("test"), new FromStringDeserializer.Std.Std(FromFileDeserializer.StringBuilder.class, 13)._deserialize("test", null));
    }
    
    @Test
    public void testDeserializeFromEmptyString() throws IOException {
        //URI type
        assertEquals(URI.create(""), new FromStringDeserializer.Std.Std(FromFileDeserializer.URI.class, 3)._deserializeFromEmptyString());
        
        //Locale type
        assertEquals(Locale.ROOT, new FromStringDeserializer.Std.Std(FromFileDeserializer.Locale.class, 8)._deserializeFromEmptyString());
        
        //StringBuilder type
        assertEquals(new StringBuilder(), new FromStringDeserializer.Std.Std(FromFileDeserializer.StringBuilder.class, 13)._deserializeFromEmptyString());
    }
    
    @Test(expected = InvalidFormatException.class)
    public void testDeserializeWithInvalidFormat() throws IOException {
        //URI type
        new FromStringDeserializer.Std.Std(FromFileDeserializer.URI.class, 3)._deserialize("invalidURI", null);
    }
    
    @Test(expected = JsonParseException.class)
    public void testDeserializeWithJsonParseException() throws IOException {
        //URI type
        JsonParser jsonParser = new JsonParser(null);
        new FromStringDeserializer.Std.Std(FromFileDeserializer.URI.class, 3)._deserialize(jsonParser, null);
    }
    
    @Test(expected = JsonMappingException.class)
    public void testDeserializeFromUnexpectedToken() throws IOException {
        //URI type
        JsonParser jsonParser = new JsonParser(null);
        new FromStringDeserializer.Std.Std(FromFileDeserializer.URI.class, 3)._deserialize(jsonParser, null);
    }
    
    @Test
    public void testDeserializeFromArray() throws IOException {
       //implement the test method for _deserializeFromArray
    }
    
    @Test
    public void testDeserializeEmbedded() throws IOException {
        //implement the test method for _deserializeEmbedded
    }
    
    @Test
    public void testFirstHyphenOrUnderscore() throws IOException {
       //implement the test method for _firstHyphenOrUnderscore
    }
}