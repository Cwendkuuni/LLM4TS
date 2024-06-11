package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.test.BaseMapTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

public class FromStringDeserializerTest extends BaseMapTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testDeserializeFile() throws JsonProcessingException {
        String json = "\"C:\\test\\file.txt\"";
        File expected = new File("C:\\test\\file.txt");
        FromStringDeserializer<File> deserializer = new Std.StdFile();
        File actual = deserializer.deserialize(mapper.getFactory().createParser(json), null);
        assertEquals(expected.getAbsolutePath(), actual.getAbsolutePath());
    }

    @Test
    public void testDeserializeURL() throws JsonProcessingException {
        String json = "\"http://test.com\"";
        URL expected = new URL("http://test.com");
        FromStringDeserializer<URL> deserializer = new Std.StdURL();
        URL actual = deserializer.deserialize(mapper.getFactory().createParser(json), null);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testDeserializeURI() throws JsonProcessingException {
        String json = "\"http://test.com/path?param=value#fragment\"";
        URI expected = new URI("http", "test.com", "/path", null, null, null, null, null, null);
        FromStringDeserializer<URI> deserializer = new Std.StdURI();
        URI actual = deserializer.deserialize(mapper.getFactory().createParser(json), null);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testDeserializeClass() throws JsonProcessingException {
        String json = "\"java.lang.String\"";
        Class<?> expected = String.class;
        FromStringDeserializer<Class<?>> deserializer = new Std.StdClass();
        Class<?> actual = deserializer.deserialize(mapper.getFactory().createParser(json), null);
        assertEquals(expected.getName(), actual.getName());
    }

    @Test
    public void testDeserializeJavaType() throws JsonProcessingException {
        String json = "\"com.example.MyClass\"";
        JavaType expected = mapper.getTypeFactory().constructType(MyClass.class);
        FromStringDeserializer<JavaType> deserializer = new Std.StdJAVA_TYPE();
        JavaType actual = deserializer.deserialize(mapper.getFactory().createParser(json), null);
        assertEquals(expected.getClass(), actual.getClass());
        assertEquals(expected.getTypeId(), actual.getTypeId());
    }

    @Test
    public void testDeserializeCurrency() throws JsonProcessingException {
        String json = "\":USD\"";
        Currency expected = Currency.getInstance("USD");
        FromStringDeserializer<Currency> deserializer = new Std.StdCurrency();
        Currency actual = deserializer.deserialize(mapper.getFactory().createParser(json), null);
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializePattern() throws JsonProcessingException {
        String json = "\"/([a-z]+)\\.js$/g\"";
        Pattern expected = Pattern.compile("/([a-z]+)\\.js$/g");
        FromStringDeserializer<Pattern> deserializer = new Std.StdPATTERN();
        Pattern actual = deserializer.deserialize(mapper.getFactory().createParser(json), null);
        assertEquals(expected.pattern(), actual.pattern());
    }

    @Test
    public void testDeserializeLocale() throws JsonProcessingException {
        String json = "\"en_US\"";
        Locale expected = Locale.forLanguageTag("en_US");
        FromStringDeserializer<Locale> deserializer = new Std.StdLOCALE();
        Locale actual = deserializer.deserialize(mapper.getFactory().createParser(json), null);
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeCharset() throws JsonProcessingException {
        String json = "\"UTF-8\"";
        Charset expected = Charset.forName("UTF-8");
        FromStringDeserializer<Charset> deserializer = new Std.StdCHARSET();
        Charset actual = deserializer.deserialize(mapper.getFactory().createParser(json), null);
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeTimeZone() throws JsonProcessingException {
        String json = "\"UTC\"";
        TimeZone expected = TimeZone.getTimeZone("UTC");
        FromStringDeserializer<TimeZone> deserializer = new Std.StdTIME_ZONE();
        TimeZone actual = deserializer.deserialize(mapper.getFactory().createParser(json), null);
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeInetAddress() throws JsonProcessingException {
        String json = "\"192.168.1.1\"";
        InetAddress expected = InetAddress.getByName("192.168.1.1");
        FromStringDeserializer<InetAddress> deserializer = new Std.StdINET_ADDRESS();
        InetAddress actual = deserializer.deserialize(mapper.getFactory().createParser(json), null);
        assertEquals(expected.getHostAddress(), actual.getHostAddress());
    }

    @Test
    public void testDeserializeInetSocketAddress() throws JsonProcessingException {
        String json = "[\"192.168.1.1\", 80]";
        InetSocketAddress expected = new InetSocketAddress("192.168.1.1", 80);
        FromStringDeserializer<InetSocketAddress> deserializer = new Std.StdINET_SOCKET_ADDRESS();
        InetSocketAddress actual = deserializer.deserialize(mapper.getFactory().createParser(json), null);
        assertEquals(expected.getAddress().getHostAddress(), actual.getAddress().getHostAddress());
        assertEquals(expected.getPort(), actual.getPort());
    }

    @Test
    public void testDeserializeStringBuilder() throws JsonProcessingException {
        String json = "\"Hello World\"";
        StringBuilder expected = new StringBuilder("Hello World");
        FromStringDeserializer<StringBuilder> deserializer = new Std.StdSTRING_BUILDER();
        StringBuilder actual = deserializer.deserialize(mapper.getFactory().createParser(json), null);
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeEmptyString() throws JsonProcessingException {
        String json = "\"\"";
        FromStringDeserializer<File> fileDeserializer = new Std.StdFile();
        File actualFile = fileDeserializer.deserialize(mapper.getFactory().createParser(json), null);
        assertNull(actualFile);

        FromStringDeserializer<URL> urlDeserializer = new Std.StdURL();
        URL actualURL = urlDeserializer.deserialize(mapper.getFactory().createParser(json), null);
        assertNull(actualURL);

        FromStringDeserializer<URI> uriDeserializer = new Std.StdURI();
        URI actualURI = uriDeserializer.deserialize(mapper.getFactory().createParser(json), null);
        assertNull(actualURI);

        FromStringDeserializer<Class<?>> clazzDeserializer = new Std.StdClass();
        Class<?> actualCLazz = clazzDeserializer.deserialize(mapper.getFactory().createParser(json), null);
        assertNull(actualCLazz);

        FromStringDeserializer<JavaType> javaTypeDeserializer = new Std.StdJAVA_TYPE();
        JavaType actualJavaType = javaTypeDeserializer.deserialize(mapper.getFactory().createParser(json), null);
        assertNull(actualJavaType);

        FromStringDeserializer<Currency> currencyDeserializer = new Std.StdCURRENCY();
        Currency actualCurrency = currencyDeserializer.deserialize(mapper.getFactory().createParser(json), null);
        assertNull(actualCurrency);

        FromStringDeserializer<Pattern> patternDeserializer = new Std.StdPATTERN();
        Pattern actualPattern = patternDeserializer.deserialize(mapper.getFactory().createParser(json), null);
        assertNull(actualPattern);

        FromStringDeserializer<Locale> localeDeserializer = new Std.StdLOCALE();
        Locale actualLocale = localeDeserializer.deserialize(mapper.getFactory().createParser(json), null);
        assertNull(actualLocale);

        FromStringDeserializer<Charset> charsetDeserializer = new Std.StdCHARSET();
        Charset actualCharset = charsetDeserializer.deserialize(mapper.getFactory().createParser(json), null);
        assertNull(actualCharset);

        FromStringDeserializer<TimeZone> timeZoneDeserializer = new Std.StdTIME_ZONE();
        TimeZone actualTimeZone = timeZoneDeserializer.deserialize(mapper.getFactory().createParser(json), null);
        assertNull(actualTimeZone);

        FromStringDeserializer<InetAddress> inetAddressDeserializer = new Std.StdINET_ADDRESS();
        InetAddress actualInetAddress = inetAddressDeserializer.deserialize(mapper.getFactory().createParser(json), null);
        assertNull(actualInetAddress);

        FromStringDeserializer<InetSocketAddress> inetSocketAddressDeserializer = new Std.StdINET_SOCKET_ADDRESS();
        InetSocketAddress actualInetSocketAddress = inetSocketAddressDeserializer.deserialize(mapper.getFactory().createParser(json), null);
        assertNull(actualInetSocketAddress);

        FromStringDeserializer<StringBuilder> stringBuilderDeserializer = new Std.StdSTRING_BUILDER();
        StringBuilder actualStringBuilder = stringBuilderDeserializer.deserialize(mapper.getFactory().createParser(json), null);
        assertNull(actualStringBuilder);
    }

    @Test
    public void testDeserializeEmptyArray() throws JsonProcessingException {
        String json = "[ ]";
        FromStringDeserializer<String> stringDeserializer = new Std();
        Object actual = stringDeserializer.deserialize(mapper.getFactory().createParser(json), null);
        assertNull(actual);
    }

    @Test
    public void testDeserializeEmbeddedObject() throws JsonProcessingException {
        String json = "{\"className\": \"java.lang.String\", \"value\": \"Hello World\"}";
        String expected = "Hello World";
        FromStringDeserializer<String> deserializer = new Std();
        String actual = deserializer.deserialize(mapper.getFactory().createParser(json), null).getValue();
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeInvalidString() throws JsonProcessingException {
        String json = "\"invalid\"";
        FromStringDeserializer<File> fileDeserializer = new Std.StdFile();
        try {
            fileDeserializer.deserialize(mapper.getFactory().createParser(json), null);
            fail();
        } catch (JsonMappingException e) {
            assertInstanceOf(InvalidFormatException.class, e.getCause());
        }

        FromStringDeserializer<URL> urlDeserializer = new Std.StdURL();
        try {
            urlDeserializer.deserialize(mapper.getFactory().createParser(json), null);
            fail();
        } catch (JsonMappingException e) {
            assertInstanceOf(InvalidFormatException.class, e.getCause());
        }

        FromStringDeserializer<URI> uriDeserializer = new Std.StdURI();
        try {
            uriDeserializer.deserialize(mapper.getFactory().createParser(json), null);
            fail();
        } catch (JsonMappingException e) {
            assertInstanceOf(InvalidFormatException.class, e.getCause());
        }

        FromStringDeserializer<Class<?>> clazzDeserializer = new Std.StdClass();
        try {
            clazzDeserializer.deserialize(mapper.getFactory().createParser(json), null);
            fail();
        } catch (JsonMappingException e) {
            assertInstanceOf(InvalidFormatException.class, e.getCause());
        }

        FromStringDeserializer<JavaType> javaTypeDeserializer = new Std.StdJAVA_TYPE();
        try {
            javaTypeDeserializer.deserialize(mapper.getFactory().createParser(json), null);
            fail();
        } catch (JsonMappingException e) {
            assertInstanceOf(InvalidFormatException.class, e.getCause());
        }

        FromStringDeserializer<Currency> currencyDeserializer = new Std.StdCURRENCY();
        try {
            currencyDeserializer.deserialize(mapper.getFactory().createParser(json), null);
            fail();
        } catch (JsonMappingException e) {
            assertInstanceOf(InvalidFormatException.class, e.getCause());
        }

        FromStringDeserializer<Pattern> patternDeserializer = new Std.StdPATTERN();
        try {
            patternDeserializer.deserialize(mapper.getFactory().createParser(json), null);
            fail();
        } catch (JsonMappingException e) {
            assertInstanceOf(InvalidFormatException.class, e.getCause());
        }

        FromStringDeserializer<Locale> localeDeserializer = new Std.StdLOCALE();
        try {
            localeDeserializer.deserialize(mapper.getFactory().createParser(json), null);
            fail();
        } catch (JsonMappingException e) {
            assertInstanceOf(InvalidFormatException.class, e.getCause());
        }

        FromStringDeserializer<Charset> charsetDeserializer = new Std.StdCHARSET();
        try {
            charsetDeserializer.deserialize(mapper.getFactory().createParser(json), null);
            fail();
        } catch (JsonMappingException e) {
            assertInstanceOf(InvalidFormatException.class, e.getCause());
        }

        FromStringDeserializer<TimeZone> timeZoneDeserializer = new Std.StdTIME_ZONE();
        try {
            timeZoneDeserializer.deserialize(mapper.getFactory().createParser(json), null);
            fail();
        } catch (JsonMappingException e) {
            assertInstanceOf(InvalidFormatException.class, e.getCause());
        }

        FromStringDeserializer<InetAddress> inetAddressDeserializer = new Std.StdINET_ADDRESS();
        try {
            inetAddressDeserializer.deserialize(mapper.getFactory().createParser(json), null);
            fail();
        } catch (JsonMappingException e) {
            assertInstanceOf(InvalidFormatException.class, e.getCause());
        }

        FromStringDeserializer<InetSocketAddress> inetSocketAddressDeserializer = new Std.StdINET_SOCKET_ADDRESS();
        try {
            inetSocketAddressDeserializer.deserialize(mapper.getFactory().createParser(json), null);
            fail();
        } catch (JsonMappingException e) {
            assertInstanceOf(InvalidFormatException.class, e.getCause());
        }

        FromStringDeserializer<StringBuilder> stringBuilderDeserializer = new Std.StdSTRING_BUILDER();
        try {
            stringBuilderDeserializer.deserialize(mapper.getFactory().createParser(json), null);
            fail();
        } catch (JsonMappingException e) {
            assertInstanceOf(InvalidFormatException.class, e.getCause());
        }
    }
}