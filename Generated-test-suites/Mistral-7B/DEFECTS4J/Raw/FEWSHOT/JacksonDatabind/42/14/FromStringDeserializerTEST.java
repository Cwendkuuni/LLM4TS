package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

public class FromStringDeserializerTest {

    @Test
    public void testFileDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<File> fileDeserializer = new Std.StdFile();
        File expected = new File("test.txt");
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected);
        File actual = mapper.findValue(jsonString, File.class);
        assertEquals(expected, actual);
    }

    @Test
    public void testUrlDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<URL> urlDeserializer = new Std.StdURL();
        URL expected = new URL("http://www.example.com");
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected);
        URL actual = mapper.findValue(jsonString, URL.class);
        assertEquals(expected, actual);
    }

    @Test
    public void testUriDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<URI> uriDeserializer = new Std.StdURI();
        URI expected = new URI("http://www.example.com");
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected);
        URI actual = mapper.findValue(jsonString, URI.class);
        assertEquals(expected, actual);
    }

    @Test
    public void testClassDeserialization() throws IOException, ClassNotFoundException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<String> stringDeserializer = new FromStringDeserializer<String>(String.class);
        Class<?> expected = String.class;
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected.getName());
        Class<?> actual = mapper.findValue(jsonString, Class.class);
        assertEquals(expected, actual);
    }

    @Test
    public void testJavaTypeDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<JavaType> javaTypeDeserializer = new FromStringDeserializer<JavaType>(JavaType.class);
        JavaType expected = mapper.getTypeFactory().constructType(String.class);
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected.getClass().getName());
        JavaType actual = mapper.findValue(jsonString, JavaType.class);
        assertEquals(expected, actual);
    }

    @Test
    public void testCurrencyDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Currency> currencyDeserializer = new Std.StdCurrency();
        Currency expected = Currency.getInstance("USD");
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected.getCurrencyCode());
        Currency actual = mapper.findValue(jsonString, Currency.class);
        assertEquals(expected, actual);
    }

    @Test
    public void testPatternDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Pattern> patternDeserializer = new Std.StdPattern();
        Pattern expected = Pattern.compile(".*\\.txt");
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected.pattern());
        Pattern actual = mapper.findValue(jsonString, Pattern.class);
        assertEquals(expected, actual);
    }

    @Test
    public void testLocaleDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Locale> localeDeserializer = new Std.StdLocale();
        Locale expected = new Locale("en", "US");
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected.toString());
        Locale actual = mapper.findValue(jsonString, Locale.class);
        assertEquals(expected, actual);
    }

    @Test
    public void testCharsetDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Charset> charsetDeserializer = new Std.StdCharset();
        Charset expected = Charset.forName("UTF-8");
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected.name());
        Charset actual = mapper.findValue(jsonString, Charset.class);
        assertEquals(expected, actual);
    }

    @Test
    public void testTimeZoneDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<TimeZone> timeZoneDeserializer = new Std.StdTimeZone();
        TimeZone expected = TimeZone.getTimeZone("UTC");
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected.getID());
        TimeZone actual = mapper.findValue(jsonString, TimeZone.class);
        assertEquals(expected, actual);
    }

    @Test
    public void testInetAddressDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<InetAddress> inetAddressDeserializer = new Std.StdInetAddress();
        InetAddress expected = InetAddress.getByName("127.0.0.1");
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected.getHostAddress());
        InetAddress actual = mapper.findValue(jsonString, InetAddress.class);
        assertEquals(expected, actual);
    }

    @Test
    public void testInetSocketAddressDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<InetSocketAddress> inetSocketAddressDeserializer = new Std.StdInetSocketAddress();
        InetSocketAddress expected = new InetSocketAddress("localhost", 8080);
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected.toString());
        InetSocketAddress actual = mapper.findValue(jsonString, InetSocketAddress.class);
        assertEquals(expected, actual);
    }

    @Test
    public void testEmptyStringDeserializationForClassDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<String> stringDeserializer = new FromStringDeserializer<String>(String.class);
        String expected = "";
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected);
        String actual = mapper.findValue(jsonString, String.class);
        assertEquals(expected, actual);
    }

    @Test(expected = JsonMappingException.class)
    public void testEmptyStringDeserializationForUriDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<URI> uriDeserializer = new Std.StdURI();
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString("");
        uriDeserializer.deserialize(mapper.getFactory().createParser(new ByteArrayInputStream(jsonString.getBytes())), null);
    }

    @Test
    public void testDeserializeFromEmptyStringForLocaleDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Locale> localeDeserializer = new Std.StdLocale();
        Locale expected = Locale.ROOT;
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected.toString());
        Locale actual = localeDeserializer._deserializeFromEmptyString();
        assertEquals(expected, actual);
    }
}