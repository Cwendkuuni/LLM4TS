package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

public class FromStringDeserializerTest {

    @Test
    public void testDeserializeFile() throws IOException {
        FromStringDeserializer<File> fileDeserializer = FromStringDeserializer.findDeserializer(File.class);
        File expected = new File("test.txt");
        JsonParser parser = createParser("\"/test.txt\"");
        File result = fileDeserializer.deserialize(parser, createContext());
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeURL() throws IOException {
        FromStringDeserializer<URL> urlDeserializer = FromStringDeserializer.findDeserializer(URL.class);
        URL expected = new URL("http://test.com");
        JsonParser parser = createParser("\"http://test.com\"");
        URL result = urlDeserializer.deserialize(parser, createContext());
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeURI() throws IOException {
        FromStringDeserializer<URI> uriDeserializer = FromStringDeserializer.findDeserializer(URI.class);
        URI expected = new URI("http://test.com");
        JsonParser parser = createParser("\"http://test.com\"");
        URI result = uriDeserializer.deserialize(parser, createContext());
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeClass() throws IOException {
        FromStringDeserializer<String> stringDeserializer = FromStringDeserializer.findDeserializer(String.class);
        String expected = "TestClass";
        JsonParser parser = createParser("\"TestClass\"");
        String result = stringDeserializer.deserialize(parser, createContext());
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        JavaType javaType = createContext().getTypeFactory().constructType(int[].class);
        FromStringDeserializer<JavaType> javaTypeDeserializer = FromStringDeserializer.findDeserializer(JavaType.class);
        JsonParser parser = createParser("\"int[10]\"");
        JavaType result = javaTypeDeserializer.deserialize(parser, createContext());
        assertEquals(javaType, result);
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        FromStringDeserializer<Currency> currencyDeserializer = FromStringDeserializer.findDeserializer(Currency.class);
        Currency expected = Currency.getInstance("USD");
        JsonParser parser = createParser("\"USD\"");
        Currency result = currencyDeserializer.deserialize(parser, createContext());
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializePattern() throws IOException {
        FromStringDeserializer<Pattern> patternDeserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        Pattern expected = Pattern.compile("[a-z]+");
        JsonParser parser = createParser("\"[a-z]+\"");
        Pattern result = patternDeserializer.deserialize(parser, createContext());
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        FromStringDeserializer<Locale> localeDeserializer = FromStringDeserializer.findDeserializer(Locale.class);
        Locale expected = new Locale("en", "US");
        JsonParser parser = createParser("\"en_US\"");
        Locale result = localeDeserializer.deserialize(parser, createContext());
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        FromStringDeserializer<Charset> charsetDeserializer = FromStringDeserializer.findDeserializer(Charset.class);
        Charset expected = Charset.forName("UTF-8");
        JsonParser parser = createParser("\"UTF-8\"");
        Charset result = charsetDeserializer.deserialize(parser, createContext());
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        FromStringDeserializer<TimeZone> timeZoneDeserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        TimeZone expected = TimeZone.getTimeZone("UTC");
        JsonParser parser = createParser("\"UTC\"");
        TimeZone result = timeZoneDeserializer.deserialize(parser, createContext());
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        FromStringDeserializer<InetAddress> inetAddressDeserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        InetAddress expected = InetAddress.getByName("127.0.0.1");
        JsonParser parser = createParser("\"127.0.0.1\"");
        InetAddress result = inetAddressDeserializer.deserialize(parser, createContext());
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        FromStringDeserializer<InetSocketAddress> inetSocketAddressDeserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        InetSocketAddress expected = new InetSocketAddress("localhost", 8080);
        JsonParser parser = createParser("\"localhost:8080\"");
        InetSocketAddress result = inetSocketAddressDeserializer.deserialize(parser, createContext());
        assertEquals(expected, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeUnknownType() throws IOException {
        FromStringDeserializer<Object> objectDeserializer = FromStringDeserializer.findDeserializer(Integer.class);
        JsonParser parser = createParser("\"Test\"");
        objectDeserializer.deserialize(parser, createContext());
    }

    @Test
    public void testDeserializeNullFromEmptyString() throws IOException {
        FromStringDeserializer<String> stringDeserializer = FromStringDeserializer.findDeserializer(String.class);
        JsonParser parser = createParser("\"\"");
        String result = stringDeserializer.deserialize(parser, createContext());
        assertNull(result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidFromEmptyString() throws IOException {
        FromStringDeserializer<URI> uriDeserializer = FromStringDeserializer.findDeserializer(URI.class);
        JsonParser parser = createParser("\"\"");
        uriDeserializer.deserialize(parser, createContext());
    }

    @Test
    public void testDeserializeEmbeddedObject() throws IOException {
        FromStringDeserializer<URL> urlDeserializer = FromStringDeserializer.findDeserializer(URL.class);
        Object embeddedObject = new URL("http://test.com");
        JsonParser parser = createParser("{\"url\": \"http://test.com\"}");
        URL result = urlDeserializer.deserializeEmbedded(embeddedObject, createContext());
        assertEquals(embeddedObject, result);
    }

    @Test
    public void testDeserializeInvalidEmbeddedObject() throws IOException {
        FromStringDeserializer<Integer> intDeserializer = FromStringDeserializer.findDeserializer(Integer.class);
        Object embeddedObject = new Object();
        JsonParser parser = createParser("{\"invalid\": \"Test\"}");
        assertInstanceOf(JsonMappingException.class, intDeserializer.deserializeEmbedded(embeddedObject, createContext()));
    }

    private JsonParser createParser(String content) throws IOException {
        return new JsonFactory().createParser(new ByteArrayInputStream(content.getBytes()));
    }

    private DeserializationContext createContext() {
        return new TestContext();
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public DeserializationFeature[] config() {
            return new DeserializationFeature[0];
        }

        @Override
        public boolean isEnabled(DeserializationFeature feature) {
            return true;
        }

        @Override
        public void setBeanProperty(Object Bean, String propertyName, Object value) throws IOException {
            // empty implementation
        }

        @Override
        public void failWithMessage(String msg) throws JsonMappingException {
            throw new JsonMappingException(msg);
        }

        @Override
        public void failWithMessage(String msg, Throwable t) throws JsonMappingException {
            throw new JsonMappingException(msg, t);
        }

        @Override
        public Object instantiateValue(Class<?> beanType, JavaType valueType) throws InstantiationException, IllegalAccessException {
            return valueType.getValueInstance();
        }

        @Override
        public Object findCreator(Class<?> c, MapperConfig<?> config) {
            return null;
        }

        @Override
        public Object findValueInMap(Map<String, Object> m, String key) {
            return m.get(key);
        }

        @Override
        public void reportError(JsonParser p, JsonMappingException e) throws IOException {
            throw e;
        }

        @Override
        public void reportWarning(JsonParser p, JsonMappingWarning w) {
            // empty implementation
        }
    }
}