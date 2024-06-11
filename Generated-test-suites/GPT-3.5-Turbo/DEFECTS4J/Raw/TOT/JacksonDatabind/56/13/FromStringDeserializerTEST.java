
package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class FromStringDeserializerTest {

    @Test
    public void testFileDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"value\":\"/path/to/file.txt\"}";
        FileWrapper wrapper = mapper.readValue(json, FileWrapper.class);
        assertEquals("/path/to/file.txt", wrapper.getValue().getPath());
    }
     
    @Test
    public void testURLDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"value\":\"http://www.example.com\"}";
        URLWrapper wrapper = mapper.readValue(json, URLWrapper.class);
        assertEquals("http://www.example.com", wrapper.getValue().toString());
    }

    @Test
    public void testURIDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"value\":\"http://www.example.com\"}";
        URIWrapper wrapper = mapper.readValue(json, URIWrapper.class);
        assertEquals("http://www.example.com", wrapper.getValue().toString());
    }

    @Test
    public void testClassDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"value\":\"java.lang.String\"}";
        ClassWrapper wrapper = mapper.readValue(json, ClassWrapper.class);
        assertEquals(String.class, wrapper.getValue());
    }
    
    @Test
    public void testJavaTypeDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"value\":\"java.util.Map\"}";
        JavaTypeWrapper wrapper = mapper.readValue(json, JavaTypeWrapper.class);
        assertEquals(JavaType.class, wrapper.getValue().getClass());
    }
    
    @Test
    public void testCurrencyDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"value\":\"USD\"}";
        CurrencyWrapper wrapper = mapper.readValue(json, CurrencyWrapper.class);
        assertEquals(Currency.getInstance("USD"), wrapper.getValue());
    }
    
    @Test
    public void testPatternDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"value\":\"[A-Za-z]+\"}";
        PatternWrapper wrapper = mapper.readValue(json, PatternWrapper.class);
        assertEquals(Pattern.compile("[A-Za-z]+"), wrapper.getValue());
    }

    @Test
    public void testLocaleDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"value\":\"en_US\"}";
        LocaleWrapper wrapper = mapper.readValue(json, LocaleWrapper.class);
        assertEquals(new Locale("en", "US"), wrapper.getValue());
    }
    
    @Test
    public void testCharsetDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"value\":\"UTF-8\"}";
        CharsetWrapper wrapper = mapper.readValue(json, CharsetWrapper.class);
        assertEquals(Charset.forName("UTF-8"), wrapper.getValue());
    }
    
    @Test
    public void testTimeZoneDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"value\":\"America/New_York\"}";
        TimeZoneWrapper wrapper = mapper.readValue(json, TimeZoneWrapper.class);
        assertEquals(TimeZone.getTimeZone("America/New_York"), wrapper.getValue());
    }
    
    @Test
    public void testInetAddressDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"value\":\"127.0.0.1\"}";
        InetAddressWrapper wrapper = mapper.readValue(json, InetAddressWrapper.class);
        assertEquals(InetAddress.getByName("127.0.0.1"), wrapper.getValue());
    }
    
    @Test
    public void testInetSocketAddressDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"value\":\"http://www.example.com:8080\"}";
        InetSocketAddressWrapper wrapper = mapper.readValue(json, InetSocketAddressWrapper.class);
        assertEquals(new InetSocketAddress("www.example.com", 8080), wrapper.getValue());
    }
    
}

class FileWrapper {
    private File value;
    
    public File getValue() {
        return value;
    }

    public void setValue(File value) {
        this.value = value;
    }
}

class URLWrapper {
    private URL value;
    
    public URL getValue() {
        return value;
    }

    public void setValue(URL value) {
        this.value = value;
    }
}

class URIWrapper {
    private URI value;
    
    public URI getValue() {
        return value;
    }

    public void setValue(URI value) {
        this.value = value;
    }
}

class ClassWrapper {
    private Class<?> value;
    
    public Class<?> getValue() {
        return value;
    }

    public void setValue(Class<?> value) {
        this.value = value;
    }
}

class JavaTypeWrapper {
    private JavaType value;
    
    public JavaType getValue() {
        return value;
    }

    public void setValue(JavaType value) {
        this.value = value;
    }
}

class CurrencyWrapper {
    private Currency value;
    
    public Currency getValue() {
        return value;
    }

    public void setValue(Currency value) {
        this.value = value;
    }
}

class PatternWrapper {
    private Pattern value;
    
    public Pattern getValue() {
        return value;
    }

    public void setValue(Pattern value) {
        this.value = value;
    }
}

class LocaleWrapper {
    private Locale value;
    
    public Locale getValue() {
        return value;
    }

    public void setValue(Locale value) {
        this.value = value;
    }
}

class CharsetWrapper {
    private Charset value;
    
    public Charset getValue() {
        return value;
    }

    public void setValue(Charset value) {
        this.value = value;
    }
}

class TimeZoneWrapper {
    private TimeZone value;
    
    public TimeZone getValue() {
        return value;
    }

    public void setValue(TimeZone value) {
        this.value = value;
    }
}

class InetAddressWrapper {
    private InetAddress value;
    
    public InetAddress getValue() {
        return value;
    }

    public void setValue(InetAddress value) {
        this.value = value;
    }
}

class InetSocketAddressWrapper {
    private InetSocketAddress value;
    
    public InetSocketAddress getValue() {
        return value;
    }

    public void setValue(InetSocketAddress value) {
        this.value = value;
    }
}

