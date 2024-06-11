package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.node.*;

public class FromStringDeserializerTest {

    @Test
    public void testDeserializeFile() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FileDeserializer fileDeserializer = (FileDeserializer) FromStringDeserializer.findDeserializer(File.class);
        assertTrue(fileDeserializer instanceof FileDeserializer);
        ObjectNode node = mapper.readValue("\"/path/to/file.txt\"", ObjectNode.class);
        File file = fileDeserializer.deserialize(mapper.getFactory().createParser(node), null);
        assertEquals("/path/to/file.txt", file.getAbsolutePath());
    }

    @Test
    public void testDeserializeUrl() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        URLEndPointDeserializer urlDeserializer = (URLEndPointDeserializer) FromStringDeserializer.findDeserializer(URL.class);
        assertTrue(urlDeserializer instanceof URLEndPointDeserializer);
        ObjectNode node = mapper.readValue("\"http://localhost:8080/api/data\"", ObjectNode.class);
        URL url = urlDeserializer.deserialize(mapper.getFactory().createParser(node), null);
        assertEquals("http://localhost:8080/api/data", url.toString());
    }

    @Test
    public void testDeserializeUri() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        URIStringDeserializer uriDeserializer = (URIStringDeserializer) FromStringDeserializer.findDeserializer(URI.class);
        assertTrue(uriDeserializer instanceof URIStringDeserializer);
        ObjectNode node = mapper.readValue("\"http://localhost:8080/api/data?param1=value1&param2=value2\"", ObjectNode.class);
        URI uri = uriDeserializer.deserialize(mapper.getFactory().createParser(node), null);
        assertEquals("http://localhost:8080/api/data?param1=value1&param2=value2", uri.toString());
    }

    @Test
    public void testDeserializeClass() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ClassDeserializer classDeserializer = (ClassDeserializer) FromStringDeserializer.findDeserializer(String.class);
        assertTrue(classDeserializer instanceof ClassDeserializer);
        ObjectNode node = mapper.readValue("\"java.lang.String\"", ObjectNode.class);
        Class<?> clazz = classDeserializer.deserialize(mapper.getFactory().createParser(node), null);
        assertEquals(String.class, clazz);
    }

    @Test
    public void testDeserializeJavaType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaTypeDeserializer javaTypeDeserializer = (JavaTypeDeserializer) FromStringDeserializer.findDeserializer(Object.class);
        assertTrue(javaTypeDeserializer instanceof JavaTypeDeserializer);
        ObjectNode node = mapper.readValue("\"com.example.MyClass\"", ObjectNode.class);
        JavaType javaType = javaTypeDeserializer.deserialize(mapper.getFactory().createParser(node), null);
        assertEquals(Class.forName("com.example.MyClass"), javaType.getRawClass());
    }

    @Test
    public void testDeserializeCurrency() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        CurrencyDeserializer currencyDeserializer = (CurrencyDeserializer) FromStringDeserializer.findDeserializer(Currency.class);
        assertTrue(currencyDeserializer instanceof CurrencyDeserializer);
        ObjectNode node = mapper.readValue("\"USD\"", ObjectNode.class);
        Currency currency = currencyDeserializer.deserialize(mapper.getFactory().createParser(node), null);
        assertEquals(Currency.getInstance("USD"), currency);
    }

    @Test
    public void testDeserializePattern() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        PatternDeserializer patternDeserializer = (PatternDeserializer) FromStringDeserializer.findDeserializer(Pattern.class);
        assertTrue(patternDeserializer instanceof PatternDeserializer);
        ObjectNode node = mapper.readValue("\"/[a-zA-Z]+\\.txt\"", ObjectNode.class);
        Pattern pattern = patternDeserializer.deserialize(mapper.getFactory().createParser(node), null);
        assertTrue(pattern.matcher("example.txt").matches());
    }

    @Test
    public void testDeserializeLocale() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        LocaleDeserializer localeDeserializer = (LocaleDeserializer) FromStringDeserializer.findDeserializer(Locale.class);
        assertTrue(localeDeserializer instanceof LocaleDeserializer);
        ObjectNode node = mapper.readValue("\"en_US\"", ObjectNode.class);
        Locale locale = localeDeserializer.deserialize(mapper.getFactory().createParser(node), null);
        assertEquals("en_US", locale.toString());
    }

    @Test
    public void testDeserializeCharset() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        CharsetDeserializer charsetDeserializer = (CharsetDeserializer) FromStringDeserializer.findDeserializer(Charset.class);
        assertTrue(charsetDeserializer instanceof CharsetDeserializer);
        ObjectNode node = mapper.readValue("\"UTF-8\"", ObjectNode.class);
        Charset charset = charsetDeserializer.deserialize(mapper.getFactory().createParser(node), null);
        assertEquals(Charset.forName("UTF-8"), charset);
    }

    @Test
    public void testDeserializeTimeZone() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TimeZoneDeserializer timeZoneDeserializer = (TimeZoneDeserializer) FromStringDeserializer.findDeserializer(TimeZone.class);
        assertTrue(timeZoneDeserializer instanceof TimeZoneDeserializer);
        ObjectNode node = mapper.readValue("\"UTC\"", ObjectNode.class);
        TimeZone timeZone = timeZoneDeserializer.deserialize(mapper.getFactory().createParser(node), null);
        assertEquals("UTC", timeZone.getID());
    }

    @Test
    public void testDeserializeInetAddress() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        InetAddressDeserializer inetAddressDeserializer = (InetAddressDeserializer) FromStringDeserializer.findDeserializer(InetAddress.class);
        assertTrue(inetAddressDeserializer instanceof InetAddressDeserializer);
        ObjectNode node = mapper.readValue("\"127.0.0.1\"", ObjectNode.class);
        InetAddress inetAddress = inetAddressDeserializer.deserialize(mapper.getFactory().createParser(node), null);
        assertTrue(inetAddress.isLoopbackAddress());
    }

    @Test
    public void testDeserializeInetSocketAddress() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        InetSocketAddressDeserializer inetSocketAddressDeserializer = (InetSocketAddressDeserializer) FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        assertTrue(inetSocketAddressDeserializer instanceof InetSocketAddressDeserializer);
        ObjectNode node = mapper.readValue("\"[::1]:8080\"", ObjectNode.class);
        InetSocketAddress inetSocketAddress = inetSocketAddressDeserializer.deserialize(mapper.getFactory().createParser(node), null);
        assertEquals("[::1]", inetSocketAddress.getAddress().getHostAddress());
        assertEquals(8080, inetSocketAddress.getPort());
    }

    @Test
    public void testDeserializeStringBuilder() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StringBuilderDeserializer stringBuilderDeserializer = (StringBuilderDeserializer) FromStringDeserializer.findDeserializer(StringBuilder.class);
        assertTrue(stringBuilderDeserializer instanceof StringBuilderDeserializer);
        ObjectNode node = mapper.readValue("\"Hello World\"", ObjectNode.class);
        StringBuilder stringBuilder = (StringBuilder) stringBuilderDeserializer.deserialize(mapper.getFactory().createParser(node), null);
        assertEquals("Hello World", stringBuilder.toString());
    }
}