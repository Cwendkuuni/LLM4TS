
package com.fasterxml.jackson.databind.deser.std;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FromStringDeserializerTest {

    private FromStringDeserializer<TestObject> deserializer;

    @Mock
    private DeserializationContext mockDeserializationContext;

    @Mock
    private JsonParser mockJsonParser;

    @Mock
    private JsonMappingException mockJsonMappingException;

    private static class TestObject {}

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        deserializer = new FromStringDeserializer<TestObject>(TestObject.class) {
            @Override
            protected TestObject _deserialize(String value, DeserializationContext ctxt) throws JsonMappingException {
                return new TestObject();
            }
        };
    }

    @Test
    public void testDeserialize_File() throws Exception {
        File file = deserializer._deserialize("test.txt", mockDeserializationContext);
        assertNotNull(file);
        assertEquals(File.class, file.getClass());
        assertEquals("test.txt", file.getName());
    }

    @Test
    public void testDeserialize_URL() throws Exception {
        URL url = deserializer._deserialize("http://www.example.com", mockDeserializationContext);
        assertNotNull(url);
        assertEquals(URL.class, url.getClass());
        assertEquals("http://www.example.com", url.toString());
    }

    @Test
    public void testDeserialize_URI() throws Exception {
        URI uri = deserializer._deserialize("http://www.example.com", mockDeserializationContext);
        assertNotNull(uri);
        assertEquals(URI.class, uri.getClass());
        assertEquals("http://www.example.com", uri.toString());
    }

    @Test
    public void testDeserialize_Locale() throws Exception {
        Locale locale = deserializer._deserialize("en_US", mockDeserializationContext);
        assertNotNull(locale);
        assertEquals(Locale.class, locale.getClass());
        assertEquals(Locale.US, locale);
    }

    @Test
    public void testDeserialize_Charset() throws Exception {
        Charset charset = deserializer._deserialize("UTF-8", mockDeserializationContext);
        assertNotNull(charset);
        assertEquals(Charset.class, charset.getClass());
        assertEquals(Charset.forName("UTF-8"), charset);
    }

    @Test
    public void testDeserialize_TimeZone() throws Exception {
        TimeZone timeZone = deserializer._deserialize("Europe/London", mockDeserializationContext);
        assertNotNull(timeZone);
        assertEquals(TimeZone.class, timeZone.getClass());
        assertEquals(TimeZone.getTimeZone("Europe/London"), timeZone);
    }

    @Test
    public void testDeserialize_InetAddress() throws Exception {
        InetAddress inetAddress = deserializer._deserialize("127.0.0.1", mockDeserializationContext);
        assertNotNull(inetAddress);
        assertEquals(InetAddress.class, inetAddress.getClass());
        assertEquals(InetAddress.getByName("127.0.0.1"), inetAddress);
    }

    @Test
    public void testDeserialize_InetSocketAddress() throws Exception {
        InetSocketAddress inetSocketAddress = deserializer._deserialize("127.0.0.1:8080", mockDeserializationContext);
        assertNotNull(inetSocketAddress);
        assertEquals(InetSocketAddress.class, inetSocketAddress.getClass());
        assertEquals(new InetSocketAddress("127.0.0.1", 8080), inetSocketAddress);
    }

    @Test
    public void testDeserialize_StringBuilder() throws Exception {
        StringBuilder stringBuilder = deserializer._deserialize("test", mockDeserializationContext);
        assertNotNull(stringBuilder);
        assertEquals(StringBuilder.class, stringBuilder.getClass());
        assertEquals("test", stringBuilder.toString());
    }
}
