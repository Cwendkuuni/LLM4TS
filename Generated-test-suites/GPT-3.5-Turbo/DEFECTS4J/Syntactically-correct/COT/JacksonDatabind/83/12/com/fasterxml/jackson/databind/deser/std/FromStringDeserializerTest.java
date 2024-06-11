
package com.fasterxml.jackson.databind.deser.std;

import org.junit.Assert;
import org.junit.Test;

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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;

public class FromStringDeserializerTest {

    @Test
    public void testDeserializeForFile() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, 1);
        JsonParser jsonParser = null;
        DeserializationContext deserializationContext = null;
        
        File result = (File) deserializer.deserialize(jsonParser, deserializationContext);
    
        Assert.assertNull(result);
    }

    @Test
    public void testDeserializeForURL() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, 2);
        JsonParser jsonParser = null;
        DeserializationContext deserializationContext = null;
        
        URL result = (URL) deserializer.deserialize(jsonParser, deserializationContext);
    
        Assert.assertNull(result);
    }

    @Test
    public void testDeserializeForURI() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, 3);
        JsonParser jsonParser = null;
        DeserializationContext deserializationContext = null;
        
        URI result = (URI) deserializer.deserialize(jsonParser, deserializationContext);
    
        Assert.assertNull(result);
    }

    @Test
    public void testDeserializeForClass() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, 4);
        JsonParser jsonParser = null;
        DeserializationContext deserializationContext = null;
        
        Class<?> result = (Class<?>) deserializer.deserialize(jsonParser, deserializationContext);
    
        Assert.assertNull(result);
    }

    @Test
    public void testDeserializeForJavaType() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(JavaType.class, 5);
        JsonParser jsonParser = null;
        DeserializationContext deserializationContext = null;
        
        JavaType result = (JavaType) deserializer.deserialize(jsonParser, deserializationContext);
    
        Assert.assertNull(result);
    }

    @Test
    public void testDeserializeForCurrency() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, 6);
        JsonParser jsonParser = null;
        DeserializationContext deserializationContext = null;
        
        Currency result = (Currency) deserializer.deserialize(jsonParser, deserializationContext);
    
        Assert.assertNull(result);
    }

    @Test
    public void testDeserializeForPattern() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, 7);
        JsonParser jsonParser = null;
        DeserializationContext deserializationContext = null;
        
        Pattern result = (Pattern) deserializer.deserialize(jsonParser, deserializationContext);
    
        Assert.assertNull(result);
    }

    @Test
    public void testDeserializeForLocale() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, 8);
        JsonParser jsonParser = null;
        DeserializationContext deserializationContext = null;
        
        Locale result = (Locale) deserializer.deserialize(jsonParser, deserializationContext);
    
        Assert.assertNull(result);
    }

    @Test
    public void testDeserializeForCharset() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Charset.class, 9);
        JsonParser jsonParser = null;
        DeserializationContext deserializationContext = null;
        
        Charset result = (Charset) deserializer.deserialize(jsonParser, deserializationContext);
    
        Assert.assertNull(result);
    }

    @Test
    public void testDeserializeForTimeZone() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, 10);
        JsonParser jsonParser = null;
        DeserializationContext deserializationContext = null;
        
        TimeZone result = (TimeZone) deserializer.deserialize(jsonParser, deserializationContext);
    
        Assert.assertNull(result);
    }

    @Test
    public void testDeserializeForInetAddress() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, 11);
        JsonParser jsonParser = null;
        DeserializationContext deserializationContext = null;
        
        InetAddress result = (InetAddress) deserializer.deserialize(jsonParser, deserializationContext);
    
        Assert.assertNull(result);
    }

    @Test
    public void testDeserializeForInetSocketAddress() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, 12);
        JsonParser jsonParser = null;
        DeserializationContext deserializationContext = null;
        
        InetSocketAddress result = (InetSocketAddress) deserializer.deserialize(jsonParser, deserializationContext);
    
        Assert.assertNull(result);
    }

    @Test
    public void testDeserializeForStringBuilder() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(StringBuilder.class, 13);
        JsonParser jsonParser = null;
        DeserializationContext deserializationContext = null;
        
        StringBuilder result = (StringBuilder) deserializer.deserialize(jsonParser, deserializationContext);
    
        Assert.assertNull(result);
    }

    @Test
    public void testDeserializeFromEmptyString() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, 4);
        JsonParser jsonParser = null;
        DeserializationContext deserializationContext = null;
        
        Class<?> result = (Class<?>) deserializer.deserialize(jsonParser, deserializationContext);
    
        Assert.assertNull(result);
    }

}
