package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class FromStringDeserializerTest {

    private ObjectMapper mapper;

    @SuppressWarnings("unchecked")
    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFileDeserialization() throws IOException {
        JsonParser jp = mapper.getFactory().createParser("\"/path/to/file.txt\"");
        FromStringDeserializer<File> fileDeserializer = FromStringDeserializer.findDeserializer(File.class);
        File result = fileDeserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals(new File("/path/to/file.txt"), result);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testURLDeserialization() throws IOException {
        JsonParser jp = mapper.getFactory().createParser("\"http://example.com\"");
        FromStringDeserializer<URL> urlDeserializer = FromStringDeserializer.findDeserializer(URL.class);
        URL result = urlDeserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals("http://example.com", result.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testURIDeserialization() throws IOException {
        JsonParser jp = mapper.getFactory().createParser("\"urn:example:com:value\"");
        FromStringDeserializer<URI> uriDeserializer = FromStringDeserializer.findDeserializer(URI.class);
        URI result = uriDeserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals("urn:example:com:value", result.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testClassDeserialization() throws IOException {
        JsonParser jp = mapper.getFactory().createParser("\"java.lang.String\"");
        FromStringDeserializer<Class<?>> classDeserializer = FromStringDeserializer.findDeserializer(Class.class);
        Class<?> result = classDeserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals(String.class, result);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testJavaTypeDeserialization() throws IOException {
        JsonParser jp = mapper.getFactory().createParser("\"com.example.MyClass\"");
        FromStringDeserializer<JavaType> javaTypeDeserializer = FromStringDeserializer.findDeserializer(JavaType.class);
        JavaType result = javaTypeDeserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals(JavaType.constructType(MyClass.class), result);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testCurrencyDeserialization() throws IOException {
        JsonParser jp = mapper.getFactory().createParser("\"USD\"");
        FromStringDeserializer<Currency> currencyDeserializer = FromStringDeserializer.findDeserializer(Currency.class);
        Currency result = currencyDeserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals(Currency.getInstance("USD"), result);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testPatternDeserialization() throws IOException {
        JsonParser jp = mapper.getFactory().createParser("\"/d{3}/g\"");
        FromStringDeserializer<Pattern> patternDeserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        Pattern result = patternDeserializer.deserialize(jp, mapper.getDeserializationContext());
        assertNotNull(result);
        assertEquals("/d{3}/g", Pattern.compile(result.toString()).pattern());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testLocaleDeserialization() throws IOException {
        JsonParser jp = mapper.getFactory().createParser("\"en_US\"");
        FromStringDeserializer<Locale> localeDeserializer = FromStringDeserializer.findDeserializer(Locale.class);
        Locale result = localeDeserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals("en_US", result.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testCharsetDeserialization() throws IOException {
        JsonParser jp = mapper.getFactory().createParser("\"UTF-8\"");
        FromStringDeserializer<Charset> charsetDeserializer = FromStringDeserializer.findDeserializer(Charset.class);
        Charset result = charsetDeserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals(Charset.forName("UTF-8"), result);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testTimeZoneDeserialization() throws IOException {
        JsonParser jp = mapper.getFactory().createParser("\"America/Los_Angeles\"");
        FromStringDeserializer<TimeZone> timeZoneDeserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        TimeZone result = timeZoneDeserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals("America/Los_Angeles", result.getID());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testInetAddressDeserialization() throws IOException {
        JsonParser jp = mapper.getFactory().createParser("\"127.0.0.1\"");
        FromStringDeserializer<InetAddress> inetAddressDeserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        InetAddress result = inetAddressDeserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals("127.0.0.1", result.getHostAddress());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testInetSocketAddressDeserialization() throws IOException {
        JsonParser jp = mapper.getFactory().createParser("\"[::1]:8080\"");
        FromStringDeserializer<InetSocketAddress> inetSocketAddressDeserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        InetSocketAddress result = inetSocketAddressDeserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals("[::1]", result.getAddress().getHostAddress());
        assertEquals(8080, result.getPort());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testEmptyStringDeserialization() throws IOException {
        JsonParser jp = mapper.getFactory().createParser("\"\"");
        FromStringDeserializer<Object> objectDeserializer = FromStringDeserializer.findDeserializer(Object.class);
        Object result = objectDeserializer.deserialize(jp, mapper.getDeserializationContext());
        assertNull(result);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testEmptyStringDeserializationForURI() throws IOException {
        JsonParser jp = mapper.getFactory().createParser("\"\"");
        FromStringDeserializer<URI> uriDeserializer = FromStringDeserializer.findDeserializer(URI.class);
        URI result = uriDeserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals(URI.create(""), result);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testInvalidStringDeserialization() throws IOException {
        JsonParser jp = mapper.getFactory().createParser("\"invalid\"");
        FromStringDeserializer<Object> objectDeserializer = FromStringDeserializer.findDeserializer(Object.class);
        try {
            objectDeserializer.deserialize(jp, mapper.getDeserializationContext());
            fail();
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testEmptyArrayDeserialization() throws IOException {
        JsonParser jp = mapper.getFactory().createParser("[]");
        FromStringDeserializer<Object> objectDeserializer = FromStringDeserializer.findDeserializer(Object.class);
        Object result = objectDeserializer.deserialize(jp, mapper.getDeserializationContext());
        assertArrayEquals(new Object[0], result);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testEmptyArrayDeserializationWithSingleValue() throws IOException {
        JsonParser jp = mapper.getFactory().createParser("[\"example\"]");
        FromStringDeserializer<Object> objectDeserializer = FromStringDeserializer.findDeserializer(Object.class);
        Object result = objectDeserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals("example", result);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testEmptyArrayDeserializationWithMultipleValues() throws IOException {
        JsonParser jp = mapper.getFactory().createParser("[\"example1\", \"example2\"]");
        FromStringDeserializer<ArrayList<String>> listDeserializer = FromStringDeserializer.findDeserializer(ArrayList.class);
        TypeFactory typeFactory = mapper.getTypeFactory();
        JavaType javaType = typeFactory.constructParametricType(ArrayList.class, String.class);
        ArrayList<String> result = (ArrayList<String>) listDeserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals(2, result.size());
        assertEquals("example1", result.get(0));
        assertEquals("example2", result.get(1));
    }
}