package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.*;
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
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class FromStringDeserializerTest {

    @Test
    public void testDeserializeFile() throws IOException {
        FromStringDeserializer.Std fileDeserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        
        File expected = new File("test.txt");
        File result = (File) fileDeserializer.deserialize(new JsonFactory().createParser("\"test.txt\""), null);

        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeURL() throws IOException {
        FromStringDeserializer.Std urlDeserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        
        URL expected = new URL("http://www.example.com");
        URL result = (URL) urlDeserializer.deserialize(new JsonFactory().createParser("\"http://www.example.com\""), null);

        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeURI() throws IOException {
        FromStringDeserializer.Std uriDeserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        
        URI expected = new URI("http://www.example.com");
        URI result = (URI) uriDeserializer.deserialize(new JsonFactory().createParser("\"http://www.example.com\""), null);

        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeClass() throws IOException {
        FromStringDeserializer.Std classDeserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        
        Class<?> expected = String.class;
        Class<?> result = (Class<?>) classDeserializer.deserialize(new JsonFactory().createParser("\"java.lang.String\""), null);

        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        FromStringDeserializer.Std javaTypeDeserializer = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);
        
        JavaType expected = new JavaType(String.class);
        JavaType result = (JavaType) javaTypeDeserializer.deserialize(new JsonFactory().createParser("\"java.lang.String\""), null);

        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        FromStringDeserializer.Std currencyDeserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        
        Currency expected = Currency.getInstance("USD");
        Currency result = (Currency) currencyDeserializer.deserialize(new JsonFactory().createParser("\"USD\""), null);

        assertEquals(expected, result);
    }

    @Test
    public void testDeserializePattern() throws IOException {
        FromStringDeserializer.Std patternDeserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        
        Pattern expected = Pattern.compile("[a-z]+");
        Pattern result = (Pattern) patternDeserializer.deserialize(new JsonFactory().createParser("\"[a-z]+\""), null);

        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        FromStringDeserializer.Std localeDeserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        
        Locale expected = new Locale("en", "US");
        Locale result = (Locale) localeDeserializer.deserialize(new JsonFactory().createParser("\"en_US\""), null);

        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        FromStringDeserializer.Std charsetDeserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        
        Charset expected = Charset.forName("UTF-8");
        Charset result = (Charset) charsetDeserializer.deserialize(new JsonFactory().createParser("\"UTF-8\""), null);

        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        FromStringDeserializer.Std timeZoneDeserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        
        TimeZone expected = TimeZone.getTimeZone("GMT");
        TimeZone result = (TimeZone) timeZoneDeserializer.deserialize(new JsonFactory().createParser("\"GMT\""), null);

        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        FromStringDeserializer.Std inetAddressDeserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        
        InetAddress expected = InetAddress.getByName("localhost");
        InetAddress result = (InetAddress) inetAddressDeserializer.deserialize(new JsonFactory().createParser("\"localhost\""), null);

        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeInetSocketAddressIPv4() throws IOException {
        FromStringDeserializer.Std inetSocketAddressDeserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        
        InetSocketAddress expected = new InetSocketAddress("192.0.2.1", 8080);
        InetSocketAddress result = (InetSocketAddress) inetSocketAddressDeserializer.deserialize(new JsonFactory().createParser("\"192.0.2.1:8080\""), null);

        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeInetSocketAddressIPv6() throws IOException {
        FromStringDeserializer.Std inetSocketAddressDeserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        
        InetSocketAddress expected = new InetSocketAddress("[2001:0db8:85a3:0000:0000:8a2e:0370:7334]", 8080);
        InetSocketAddress result = (InetSocketAddress) inetSocketAddressDeserializer.deserialize(new JsonFactory().createParser("\"[2001:0db8:85a3:0000:0000:8a2e:0370:7334]:8080\""), null);

        assertEquals(expected, result);
    }

}