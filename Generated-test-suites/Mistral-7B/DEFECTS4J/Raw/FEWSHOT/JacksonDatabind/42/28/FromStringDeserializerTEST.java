package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;

public class FromStringDeserializerTest {

    @Test
    public void testDeserializeFile() throws IOException {
        FromStringDeserializer<File> deserializer = (FromStringDeserializer<File>) FromStringDeserializer.findDeserializer(File.class);

        File result = deserializer.deserialize(createMockParser(), createContext());
        assertEquals(new File("test.txt"), result);
    }

    @Test
    public void testDeserializeUrl() throws IOException {
        FromStringDeserializer<URL> deserializer = (FromStringDeserializer<URL>) FromStringDeserializer.findDeserializer(URL.class);

        URL result = deserializer.deserialize(createMockParser(), createContext());
        assertEquals(new URL("http://example.com"), result);
    }

    @Test
    public void testDeserializeUri() throws IOException {
        FromStringDeserializer<URI> deserializer = (FromStringDeserializer<URI>) FromStringDeserializer.findDeserializer(URI.class);

        URI result = deserializer.deserialize(createMockParser(), createContext());
        assertEquals(new URI("http://example.com"), result);
    }

    @Test
    public void testDeserializeClass() throws IOException {
        FromStringDeserializer<Class<?>> deserializer = (FromStringDeserializer<Class<?>>) FromStringDeserializer.findDeserializer(String.class);

        Class<?> result = deserializer.deserialize(createMockParser(), createContext());
        assertEquals(String.class, result);
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        FromStringDeserializer<JavaType> deserializer = (FromStringDeserializer<JavaType>) FromStringDeserializer.findDeserializer(JavaType.class);

        JavaType result = deserializer.deserialize(createMockParser(), createContext());
        assertInstanceOf(JavaType.class, result);
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        FromStringDeserializer<Currency> deserializer = (FromStringDeserializer<Currency>) FromStringDeserializer.findDeserializer(Currency.class);

        Currency result = deserializer.deserialize(createMockParser(), createContext());
        assertEquals(Currency.USD, result);
    }

    @Test
    public void testDeserializePattern() throws IOException {
        FromStringDeserializer<Pattern> deserializer = (FromStringDeserializer<Pattern>) FromStringDeserializer.findDeserializer(Pattern.class);

        Pattern result = deserializer.deserialize(createMockParser(), createContext());
        assertEquals(Pattern.compile("[a-z]+"), result);
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        FromStringDeserializer<Locale> deserializer = (FromStringDeserializer<Locale>) FromStringDeserializer.findDeserializer(Locale.class);

        Locale result = deserializer.deserialize(createMockParser(), createContext());
        assertEquals(Locale.US, result);

        result = deserializer.deserialize(createMockParser(), createContext(), "en_US_POSIX");
        assertEquals(Locale.US, result);

        result = deserializer.deserialize(createMockParser(), createContext(), "en_US_POSIX.utf8");
        assertEquals(Locale.US, result);

        result = deserializer.deserialize(createMockParser(), createContext(), "en_US");
        assertEquals(Locale.US, result);

        result = deserializer.deserialize(createMockParser(), createContext(), "en_US_ja_JP");
        assertEquals(Locale.US, result);

        result = deserializer.deserialize(createMockParser(), createContext(), "ja_JP.UTF-8");
        assertEquals(Locale.JAPAN, result);
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        FromStringDeserializer<Charset> deserializer = (FromStringDeserializer<Charset>) FromStringDeserializer.findDeserializer(Charset.class);

        Charset result = deserializer.deserialize(createMockParser(), createContext());
        assertEquals(Charset.forName("UTF-8"), result);
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        FromStringDeserializer<TimeZone> deserializer = (FromStringDeserializer<TimeZone>) FromStringDeserializer.findDeserializer(TimeZone.class);

        TimeZone result = deserializer.deserialize(createMockParser(), createContext());
        assertEquals(TimeZone.getDefault(), result);

        result = deserializer.deserialize(createMockParser(), createContext(), "UTC");
        assertEquals(TimeZone.getTimeZone("UTC"), result);

        result = deserializer.deserialize(createMockParser(), createContext(), "GMT+02:00");
        assertEquals(TimeZone.getTimeZone("Europe/Paris"), result);
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        FromStringDeserializer<InetAddress> deserializer = (FromStringDeserializer<InetAddress>) FromStringDeserializer.findDeserializer(InetAddress.class);

        InetAddress result = deserializer.deserialize(createMockParser(), createContext());
        assertNull(result);

        result = deserializer.deserialize(createMockParser(), createContext(), "127.0.0.1");
        assertEquals(InetAddress.getByName("127.0.0.1"), result);

        result = deserializer.deserialize(createMockParser(), createContext(), "[::1]");
        assertEquals(InetAddress.getByName("[::1]"), result);

        result = deserializer.deserialize(createMockParser(), createContext(), "[2001:db8::1]");
        assertEquals(InetAddress.getByName("[2001:db8::1]"), result);
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        FromStringDeserializer<InetSocketAddress> deserializer = (FromStringDeserializer<InetSocketAddress>) FromStringDeserializer.findDeserializer(InetSocketAddress.class);

        InetSocketAddress result = deserializer.deserialize(createMockParser(), createContext());
        assertNull(result);

        result = deserializer.deserialize(createMockParser(), createContext(), "127.0.0.1:8080");
        assertEquals(new InetSocketAddress(InetAddress.getByName("127.0.0.1"), 8080), result);

        result = deserializer.deserialize(createMockParser(), createContext(), "[::1]:8080");
        assertEquals(new InetSocketAddress(InetAddress.getByName("[::1]"), 8080), result);

        result = deserializer.deserialize(createMockParser(), createContext(), "[2001:db8::1]:8080");
        assertEquals(new InetSocketAddress(InetAddress.getByName("[2001:db8::1]"), 8080), result);

        result = deserializer.deserialize(createMockParser(), createContext(), "[2001:db8::1]:8080:foo:bar");
        assertEquals(new InetSocketAddress(InetAddress.getByName("[2001:db8::1]"), 8080, "foo", "bar"), result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeUnknownType() throws IOException {
        FromStringDeserializer<Object> deserializer = (FromStringDeserializer<Object>) FromStringDeserializer.findDeserializer(Byte.class);

        deserializer.deserialize(createMockParser(), createContext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeMalformedString() throws IOException {
        FromStringDeserializer<Pattern> deserializer = (FromStringDeserializer<Pattern>) FromStringDeserializer.findDeserializer(Pattern.class);

        deserializer._deserialize("invalid", createContext());
    }

    private JsonParser createMockParser() {
        return mock(JsonParser.class);
    }

    private DeserializationContext createContext() {
        return new TestContext();
    }
}

class TestContext extends DeserializationContext {
    @Override
    public Object instantiateValue(Class<?> valueType) throws InstantiationException, IllegalAccessException, IOException {
        return valueType.newInstance();
    }

    @Override
    public Object findReference(String name) throws IOException {
        return null;
    }

    @Override
    public JavaType findValueType(Class<?> rawType, Locale locale) throws IOException {
        return null;
    }

    @Override
    public JavaType findValueType(Class<?> rawType) throws IOException {
        return null;
    }

    @Override
    public JavaType findValueType(String name, Locale locale) throws IOException {
        return null;
    }

    @Override
    public JavaType findValueType(String name, Class<?> valueType) throws IOException {
        return null;
    }
}