package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.DeserializerFactory;
import com.fasterxml.jackson.databind.JsonDeserializer.LookupCache;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer.Std;

public class FromStringDeserializerTest {

    @SuppressWarnings("unchecked")
    private static <T> ObjectReader createObjectMapper() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readerFor(Object.class);
    }

    @SuppressWarnings("unchecked")
    private static <T> DeserializerFactory<T> createDeserializerFactory(Class<T> valueType) {
        return (DeserializerFactory<T>) FromStringDeserializer.findDeserializer(valueType);
    }

    @Test
    public void testDeserializeFile() throws IOException {
        ObjectReader objectMapper = createObjectMapper();
        DeserializerFactory<File> fileDeserializerFactory = createDeserializerFactory(File.class);

        String jsonString = "\"/path/to/file.txt\"";
        File expected = new File("/path/to/file.txt");

        Std<File> std = (Std<File>) fileDeserializerFactory.createDeserializer(createContext());
        File actual = std.deserialize(createTestParser(jsonString), createContext());
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeUrl() throws IOException {
        ObjectReader objectMapper = createObjectMapper();
        DeserializerFactory<URL> urlDeserializerFactory = createDeserializerFactory(URL.class);

        String jsonString = "\"http://example.com\"";
        URL expected = new URL("http://example.com");

        Std<URL> std = (Std<URL>) urlDeserializerFactory.createDeserializer(createContext());
        URL actual = std.deserialize(createTestParser(jsonString), createContext());
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeUri() throws IOException {
        ObjectReader objectMapper = createObjectMapper();
        DeserializerFactory<URI> uriDeserializerFactory = createDeserializerFactory(URI.class);

        String jsonString = "\"http://example.com/path/to/file.txt\"";
        URI expected = new URI("http://example.com/path/to/file.txt");

        Std<URI> std = (Std<URI>) uriDeserializerFactory.createDeserializer(createContext());
        URI actual = std.deserialize(createTestParser(jsonString), createContext());
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeClass() throws IOException {
        ObjectReader objectMapper = createObjectMapper();
        DeserializerFactory<Class<?>> classDeserializerFactory = createDeserializerFactory(Class.class);

        String jsonString = "String.class";
        Class<?> expected = String.class;

        Std<Class<?>> std = (Std<Class<?>>) classDeserializerFactory.createDeserializer(createContext());
        Class<?> actual = std.deserialize(createTestParser(jsonString), createContext());
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        ObjectReader objectMapper = createObjectMapper();
        DeserializerFactory<JavaType> javaTypeDeserializerFactory = createDeserializerFactory(JavaType.class);

        String jsonString = "com.example.ExampleClass.class";
        JavaType expected = new SimpleTypeInstance(JavaType.constructUnboundType(Class.forName("com.example.ExampleClass")));

        Std<JavaType> std = (Std<JavaType>) javaTypeDeserializerFactory.createDeserializer(createContext());
        JavaType actual = std.deserialize(createTestParser(jsonString), createContext());
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        ObjectReader objectMapper = createObjectMapper();
        DeserializerFactory<Currency> currencyDeserializerFactory = createDeserializerFactory(Currency.class);

        String jsonString = "\"USD\"";
        Currency expected = Currency.getInstance("USD");

        Std<Currency> std = (Std<Currency>) currencyDeserializerFactory.createDeserializer(createContext());
        Currency actual = std.deserialize(createTestParser(jsonString), createContext());
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializePattern() throws IOException {
        ObjectReader objectMapper = createObjectMapper();
        DeserializerFactory<Pattern> patternDeserializerFactory = createDeserializerFactory(Pattern.class);

        String jsonString = "/(?i)example/";
        Pattern expected = Pattern.compile(jsonString);

        Std<Pattern> std = (Std<Pattern>) patternDeserializerFactory.createDeserializer(createContext());
        Pattern actual = std.deserialize(createTestParser(jsonString), createContext());
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        ObjectReader objectMapper = createObjectMapper();
        DeserializerFactory<Locale> localeDeserializerFactory = createDeserializerFactory(Locale.class);

        String jsonString = "\"en_US\"";
        Locale expected = new Locale("en", "US");

        Std<Locale> std = (Std<Locale>) localeDeserializerFactory.createDeserializer(createContext());
        Locale actual = std.deserialize(createTestParser(jsonString), createContext());
        assertEquals(expected, actual);

        jsonString = "\"en_US_POSIX\"";
        expected = new Locale("en", "US", "");
        actual = std.deserialize(createTestParser(jsonString), createContext());
        assertEquals(expected, actual);

        jsonString = "\"en_US_POSIX.UTF-8\"";
        expected = new Locale("en", "US", "UTF-8");
        actual = std.deserialize(createTestParser(jsonString), createContext());
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        ObjectReader objectMapper = createObjectMapper();
        DeserializerFactory<Charset> charsetDeserializerFactory = createDeserializerFactory(Charset.class);

        String jsonString = "\"UTF-8\"";
        Charset expected = Charset.forName("UTF-8");

        Std<Charset> std = (Std<Charset>) charsetDeserializerFactory.createDeserializer(createContext());
        Charset actual = std.deserialize(createTestParser(jsonString), createContext());
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        ObjectReader objectMapper = createObjectMapper();
        DeserializerFactory<TimeZone> timeZoneDeserializerFactory = createDeserializerFactory(TimeZone.class);

        String jsonString = "\"America/New_York\"";
        TimeZone expected = TimeZone.getTimeZone("America/New_York");

        Std<TimeZone> std = (Std<TimeZone>) timeZoneDeserializerFactory.createDeserializer(createContext());
        TimeZone actual = std.deserialize(createTestParser(jsonString), createContext());
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        ObjectReader objectMapper = createObjectMapper();
        DeserializerFactory<InetAddress> inetAddressDeserializerFactory = createDeserializerFactory(InetAddress.class);

        String jsonString = "\"127.0.0.1\"";
        InetAddress expected = InetAddress.getByName("127.0.0.1");

        Std<InetAddress> std = (Std<InetAddress>) inetAddressDeserializerFactory.createDeserializer(createContext());
        InetAddress actual = std.deserialize(createTestParser(jsonString), createContext());
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        ObjectReader objectMapper = createObjectMapper();
        DeserializerFactory<InetSocketAddress> inetSocketAddressDeserializerFactory = createDeserializerFactory(InetSocketAddress.class);

        String jsonString = "[\"127.0.0.1\", 80]";
        InetSocketAddress expected = new InetSocketAddress("127.0.0.1", 80);

        Std<InetSocketAddress> std = (Std<InetSocketAddress>) inetSocketAddressDeserializerFactory.createDeserializer(createContext());
        InetSocketAddress actual = std.deserialize(createTestParser(jsonString), createContext());
        assertEquals(expected, actual);

        jsonString = "[\"::1\", 80]";
        expected = new InetSocketAddress(InetAddress.getByName("::1"), 80);
        actual = std.deserialize(createTestParser(jsonString), createContext());
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeEmptyString() throws IOException {
        ObjectReader objectMapper = createObjectMapper();
        DeserializerFactory<Object> objectDeserializerFactory = createDeserializerFactory(Object.class);

        Std<Object> std = (Std<Object>) objectDeserializerFactory.createDeserializer(createContext());
        Object actual = std.deserialize(createTestParser(""), createContext());
        assertNull(actual);
    }

    @Test
    public void testDeserializeNull() throws IOException {
        ObjectReader objectMapper = createObjectMapper();
        DeserializerFactory<Object> objectDeserializerFactory = createDeserializerFactory(Object.class);

        Std<Object> std = (Std<Object>) objectDeserializerFactory.createDeserializer(createContext());
        Object actual = std.deserialize(createTestParser(null), createContext());
        assertNull(actual);
    }

    @Test
    public void testDeserializeEmbeddedObject() throws IOException {
        ObjectReader objectMapper = createObjectMapper();
        ObjectReader jsonObjectMapper = objectMapper.readerFor(Object.class);

        String jsonString = "{\"_class\":\"java.net.URL\", \"value\":\"http://example.com\"}";
        URL expected = new URL("http://example.com");

        Std<URL> std = (Std<URL>) createDeserializerFactory(URL.class).createDeserializer(createContext());
        ObjectReader embeddedObjectMapper = jsonObjectMapper.readerFor(Object.class);
        Object actual = std.deserializeEmbedded(embeddedObjectMapper.readValue(jsonString, Object.class), createContext());
        assertEquals(expected, actual);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidEmbeddedObject() throws IOException {
        ObjectReader objectMapper = createObjectMapper();
        ObjectReader jsonObjectMapper = objectMapper.readerFor(Object.class);

        String jsonString = "{\"_class\":\"java.net.URL\", \"value\":\"invalid\"}";
        Std<URL> std = (Std<URL>) createDeserializerFactory(URL.class).createDeserializer(createContext());
        Object actual = std.deserializeEmbedded(jsonObjectMapper.readValue(jsonString, Object.class), createContext());
        fail();
    }

    @Test
    public void testFindDeserializer() throws IOException {
        Class<?>[] types = FromStringDeserializer.types();
        for (Class<?> type : types) {
            Std<Object> std = (Std<Object>) createDeserializerFactory(Object.class).createDeserializer(createContext());
            Std<?> foundStd = (Std<?>) FromStringDeserializer.findDeserializer(type);
            assertInstanceOf(std.getClass(), foundStd.getClass());
        }
    }

    private static DeserializationContext createContext() {
        return new TestDeserializationContext();
    }

    private static JsonParser createTestParser(String text) throws IOException {
        ObjectReader objectMapper = createObjectMapper();
        return objectMapper.getFactory().createParser(new StringReader(text));
    }

    private static class TestDeserializationContext extends DeserializationContext {
        @Override
        public LookupCache _findValueDeserializer(DeserializerFactory deserializers, Class<?> vc, boolean valueIfNull) {
            return (LookupCache) deserializers.findValueDeserializer(vc, valueIfNull);
        }

        @Override
        public Object instantiateValue(Class<?> valueType) throws InstantiationException, IllegalAccessException {
            return valueType.newInstance();
        }

        @Override
        public Object instantiateValue(Class<?> valueType, Object[] args) throws InstantiationException, IllegalAccessException, InvocationTargetException {
            return valueType.getConstructor(args.getClass()).newInstance(args);
        }

        @Override
        public Object instantiateValue(Class<?> valueType, Object[] args, Locale local) throws InstantiationException, IllegalAccessException, InvocationTargetException {
            return valueType.getConstructor(args.getClass(), local).newInstance(args);
        }

        @Override
        public Object instantiateValue(Class<?> valueType, Object[] args, Locale local, TimeZone timezone) throws InstantiationException, IllegalAccessException, InvocationTargetException {
            return valueType.getConstructor(args.getClass(), local, timezone).newInstance(args);
        }

        @Override
        public Object instantiateValue(Class<?> valueType, Object[] args, Locale local, TimeZone timezone, ClassLoader loader) throws InstantiationException, IllegalAccessException, InvocationTargetException {
            return valueType.getConstructor(args.getClass(), local, timezone, loader).newInstance(args);
        }
    }
}