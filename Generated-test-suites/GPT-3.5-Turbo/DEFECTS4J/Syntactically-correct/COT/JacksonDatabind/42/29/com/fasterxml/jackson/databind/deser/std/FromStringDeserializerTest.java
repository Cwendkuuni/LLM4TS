import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.testutil.NoCheckSubTypeValidator;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.nio.charset.Charset;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FromStringDeserializerTest {

    @BeforeClass
    public static void setup() {
        // Disable subtype checking to simplify test cases
        NoCheckSubTypeValidator.instance.failOnUnknown();
    }

    @Test
    public void testDeserialize_File() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        JsonParser parser = new JsonFactory().createParser("\"test.txt\"");
        DeserializationContext ctxt = new DeserializationContext(null, null, parser.getCodec(), null, null);
        File result = deserializer.deserialize(parser, ctxt);
        assertEquals("test.txt", result.getName());
    }

    @Test
    public void testDeserialize_URL() throws IOException, MalformedURLException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        JsonParser parser = new JsonFactory().createParser("\"http://www.example.com\"");
        DeserializationContext ctxt = new DeserializationContext(null, null, parser.getCodec(), null, null);
        URL result = deserializer.deserialize(parser, ctxt);
        assertEquals(new URL("http://www.example.com"), result);
    }

    @Test
    public void testDeserialize_URI() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        JsonParser parser = new JsonFactory().createParser("\"http://www.example.com\"");
        DeserializationContext ctxt = new DeserializationContext(null, null, parser.getCodec(), null, null);
        URI result = deserializer.deserialize(parser, ctxt);
        assertEquals(URI.create("http://www.example.com"), result);
    }

    @Test
    public void testDeserialize_Class() throws IOException, ClassNotFoundException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        JsonParser parser = new JsonFactory().createParser("\"java.lang.String\"");
        DeserializationContext ctxt = new DeserializationContext(null, null, parser.getCodec(), null, null);
        Class<?> result = deserializer.deserialize(parser, ctxt);
        assertEquals(String.class, result);
    }

    @Test
    public void testDeserialize_JavaType() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);
        JsonParser parser = new JsonFactory().createParser("\"java.lang.String\"");
        DeserializationContext ctxt = new DeserializationContext(null, null, parser.getCodec(), null, null);
        JavaType result = deserializer.deserialize(parser, ctxt);
        assertEquals(JavaType.constructFromCanonical("java.lang.String"), result);
    }

    @Test
    public void testDeserialize_Currency() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        JsonParser parser = new JsonFactory().createParser("\"USD\"");
        DeserializationContext ctxt = new DeserializationContext(null, null, parser.getCodec(), null, null);
        Currency result = deserializer.deserialize(parser, ctxt);
        assertEquals(Currency.getInstance("USD"), result);
    }

    @Test
    public void testDeserialize_Pattern() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        JsonParser parser = new JsonFactory().createParser("\"[0-9]+\"");
        DeserializationContext ctxt = new DeserializationContext(null, null, parser.getCodec(), null, null);
        Pattern result = deserializer.deserialize(parser, ctxt);
        assertEquals(Pattern.compile("[0-9]+"), result);
    }

    @Test
    public void testDeserialize_Locale_withoutVariant() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        JsonParser parser = new JsonFactory().createParser("\"en_US\"");
        DeserializationContext ctxt = new DeserializationContext(null, null, parser.getCodec(), null, null);
        Locale result = deserializer.deserialize(parser, ctxt);
        assertEquals(new Locale("en", "US"), result);
    }

    @Test
    public void testDeserialize_Locale_withVariant() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        JsonParser parser = new JsonFactory().createParser("\"en_US_var\"");
        DeserializationContext ctxt = new DeserializationContext(null, null, parser.getCodec(), null, null);
        Locale result = deserializer.deserialize(parser, ctxt);
        assertEquals(new Locale("en", "US", "var"), result);
    }

    @Test
    public void testDeserialize_Charset() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        JsonParser parser = new JsonFactory().createParser("\"UTF-8\"");
        DeserializationContext ctxt = new DeserializationContext(null, null, parser.getCodec(), null, null);
        Charset result = deserializer.deserialize(parser, ctxt);
        assertEquals(Charset.forName("UTF-8"), result);
    }

    @Test
    public void testDeserialize_TimeZone() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        JsonParser parser = new JsonFactory().createParser("\"UTC\"");
        DeserializationContext ctxt = new DeserializationContext(null, null, parser.getCodec(), null, null);
        TimeZone result = deserializer.deserialize(parser, ctxt);
        assertEquals(TimeZone.getTimeZone("UTC"), result);
    }

    @Test
    public void testDeserialize_InetAddress() throws IOException, UnknownHostException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        JsonParser parser = new JsonFactory().createParser("\"localhost\"");
        DeserializationContext ctxt = new DeserializationContext(null, null, parser.getCodec(), null, null);
        InetAddress result = deserializer.deserialize(parser, ctxt);
        assertEquals(InetAddress.getByName("localhost"), result);
    }

    @Test
    public void testDeserialize_InetSocketAddress_IPv4() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        JsonParser parser = new JsonFactory().createParser("\"127.0.0.1:8080\"");
        DeserializationContext ctxt = new DeserializationContext(null, null, parser.getCodec(), null, null);
        InetSocketAddress result = deserializer.deserialize(parser, ctxt);
        assertEquals(new InetSocketAddress("127.0.0.1", 8080), result);
    }

    @Test
    public void testDeserialize_InetSocketAddress_IPv6() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        JsonParser parser = new JsonFactory().createParser("\"[::1]:8080\"");
        DeserializationContext ctxt = new DeserializationContext(null, null, parser.getCodec(), null, null);
        InetSocketAddress result = deserializer.deserialize(parser, ctxt);
        assertEquals(new InetSocketAddress("::1", 8080), result);
    }

    @Test
    public void testDeserialize_EmbeddedObject_Null() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Object.class, 0);
        JsonParser parser = new JsonFactory().createParser("null");
        DeserializationContext ctxt = new DeserializationContext(null, null, parser.getCodec(), null, null);
        Object result = deserializer.deserialize(parser, ctxt);
        assertNull(result);
    }

    @Test
    public void testDeserialize_EmbeddedObject_SameTypeInstance() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Object.class, 0);
        String input = "\"test\"";
        JsonParser parser = new JsonFactory().createParser(input);
        DeserializationContext ctxt = new DeserializationContext(null, null, parser.getCodec(), null, null);
        Object result = deserializer.deserialize(parser, ctxt);
        assertEquals("test", result);
    }
}