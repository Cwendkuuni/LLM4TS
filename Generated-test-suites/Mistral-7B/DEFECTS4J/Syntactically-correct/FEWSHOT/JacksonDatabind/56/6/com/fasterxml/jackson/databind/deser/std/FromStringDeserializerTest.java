package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

public class FromStringDeserializerTest {

    // Test Std class
    @SuppressWarnings("unchecked")
    @Test
    public void testStdDeserializer() {
        for (Class<?> type : FromStringDeserializer.types()) {
            StdSerializer<?> serializer = (StdSerializer<?>) FromStringDeserializer.findDeserializer(type);
            assertNotNull(serializer);
        }
    }

    // Test File deserializer
    @Test
    public void testFileDeserializer() throws IOException {
        FromStringDeserializer.Std fileDeserializer = FromStringDeserializer.findDeserializer(File.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("\"/path/to/file.txt\"");
        File file = fileDeserializer.deserialize(mapper.getFactory().createParser(node), new DeserializationContext(mapper.getConfig()));
        assertEquals("/path/to/file.txt", file.getAbsolutePath());
    }

    // Test URL deserializer
    @Test
    public void testURLDeserializer() throws IOException {
        FromStringDeserializer.Std urlDeserializer = FromStringDeserializer.findDeserializer(URL.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("\"http://example.com\"");
        URL url = urlDeserializer.deserialize(mapper.getFactory().createParser(node), new DeserializationContext(mapper.getConfig()));
        assertEquals("http://example.com", url.toString());
    }

    // Test URI deserializer
    @Test
    public void testURIDeserializer() throws IOException {
        FromStringDeserializer.Std uriDeserializer = FromStringDeserializer.findDeserializer(URI.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("\"http://example.com/path?query=value#fragment\"");
        URI uri = uriDeserializer.deserialize(mapper.getFactory().createParser(node), new DeserializationContext(mapper.getConfig()));
        assertEquals("http://example.com/path?query=value#fragment", uri.toString());
    }

    // Test Class deserializer
    @Test
    public void testClassDeserializer() throws IOException, ClassNotFoundException {
        FromStringDeserializer.Std classDeserializer = FromStringDeserializer.findDeserializer(String.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("\"java.lang.String\"");
        Class<?> clazz = classDeserializer.deserialize(mapper.getFactory().createParser(node), new DeserializationContext(mapper.getConfig()));
        assertEquals(String.class, clazz);
    }

    // Test JavaType deserializer
    @Test
    public void testJavaTypeDeserializer() throws IOException {
        FromStringDeserializer.Std javaTypeDeserializer = FromStringDeserializer.findDeserializer(JavaType.class);

        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType("java.util.List<java.lang.String>");
        JsonNode node = mapper.valueToTree(javaType);

        JsonParser parser = mapper.getFactory().createParser(node);
        DeserializationContext ctxt = new DeserializationContext(mapper.getConfig());
        JavaType deserializedJavaType = (JavaType) javaTypeDeserializer.deserialize(parser, ctxt);
        assertEquals(javaType, deserializedJavaType);
    }

    // Test Currency deserializer
    @Test(expected = InvalidFormatException.class)
    public void testCurrencyDeserializerInvalid() throws IOException {
        FromStringDeserializer.Std currencyDeserializer = FromStringDeserializer.findDeserializer(Currency.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("\"ABC\"");
        currencyDeserializer.deserialize(mapper.getFactory().createParser(node), new DeserializationContext(mapper.getConfig()));
    }

    @Test
    public void testCurrencyDeserializerValid() throws IOException {
        FromStringDeserializer.Std currencyDeserializer = FromStringDeserializer.findDeserializer(Currency.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("\"USD\"");
        Currency currency = currencyDeserializer.deserialize(mapper.getFactory().createParser(node), new DeserializationContext(mapper.getConfig()));
        assertEquals(Currency.getInstance("USD"), currency);
    }

    // Test Pattern deserializer
    @Test(expected = PatternSyntaxException.class)
    public void testPatternDeserializerInvalid() throws IOException {
        FromStringDeserializer.Std patternDeserializer = FromStringDeserializer.findDeserializer(Pattern.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("\"[a-z]\\*-[0-9]+\""); // Invalid regex
        patternDeserializer.deserialize(mapper.getFactory().createParser(node), new DeserializationContext(mapper.getConfig()));
    }

    @Test
    public void testPatternDeserializerValid() throws IOException {
        FromStringDeserializer.Std patternDeserializer = FromStringDeserializer.findDeserializer(Pattern.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("\"\\w+\\@\\w+\\.\\w+\"");
        Pattern pattern = patternDeserializer.deserialize(mapper.getFactory().createParser(node), new DeserializationContext(mapper.getConfig()));
        assertTrue(pattern.matcher("test@example.com").matches());
    }

    // Test Locale deserializer
    @Test
    public void testLocaleDeserializerSingleArgument() throws IOException {
        FromStringDeserializer.Std localeDeserializer = FromStringDeserializer.findDeserializer(Locale.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("\"en\"");
        Locale locale = localeDeserializer.deserialize(mapper.getFactory().createParser(node), new DeserializationContext(mapper.getConfig()));
        assertEquals(Locale.ENGLISH, locale);
    }

    @Test
    public void testLocaleDeserializerTwoArguments() throws IOException {
        FromStringDeserializer.Std localeDeserializer = FromStringDeserializer.findDeserializer(Locale.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("\"en_US\"");
        Locale locale = localeDeserializer.deserialize(mapper.getFactory().createParser(node), new DeserializationContext(mapper.getConfig()));
        assertEquals(Locale.US, locale);
    }

    // Test Charset deserializer
    @Test
    public void testCharsetDeserializer() throws IOException {
        FromStringDeserializer.Std charsetDeserializer = FromStringDeserializer.findDeserializer(Charset.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("\"UTF-8\"");
        Charset charset = charsetDeserializer.deserialize(mapper.getFactory().createParser(node), new DeserializationContext(mapper.getConfig()));
        assertEquals(Charset.forName("UTF-8"), charset);
    }

    // Test TimeZone deserializer
    @Test
    public void testTimeZoneDeserializer() throws IOException {
        FromStringDeserializer.Std timeZoneDeserializer = FromStringDeserializer.findDeserializer(TimeZone.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("\"UTC\"");
        TimeZone timeZone = timeZoneDeserializer.deserialize(mapper.getFactory().createParser(node), new DeserializationContext(mapper.getConfig()));
        assertEquals(TimeZone.getTimeZone("UTC"), timeZone);
    }

    // Test InetAddress deserializer
    @Test
    public void testInetAddressDeserializerIPv4() throws IOException {
        FromStringDeserializer.Std inetAddressDeserializer = FromStringDeserializer.findDeserializer(InetAddress.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("\"192.168.0.1\"");
        InetAddress inetAddress = inetAddressDeserializer.deserialize(mapper.getFactory().createParser(node), new DeserializationContext(mapper.getConfig()));
        assertEquals(InetAddress.getByName("192.168.0.1"), inetAddress);
    }

    @Test
    public void testInetAddressDeserializerIPv6() throws IOException {
        FromStringDeserializer.Std inetAddressDeserializer = FromStringDeserializer.findDeserializer(InetAddress.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("[\"2001:0db8:85a3:0000:0000:8a2e:0370:7334\"]");
        InetAddress inetAddress = inetAddressDeserializer.deserialize(mapper.getFactory().createParser(node), new DeserializationContext(mapper.getConfig()));
        assertEquals(InetAddress.getByName("[2001:0db8:85a3:0000:0000:8a2e:0370:7334]"), inetAddress);
    }

    // Test InetSocketAddress deserializer
    @Test
    public void testInetSocketAddressDeserializerIPv4() throws IOException {
        FromStringDeserializer.Std inetSocketAddressDeserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("\"localhost:8080\"");
        InetSocketAddress inetSocketAddress = inetSocketAddressDeserializer.deserialize(mapper.getFactory().createParser(node), new DeserializationContext(mapper.getConfig()));
        assertEquals(new InetSocketAddress("localhost", 8080), inetSocketAddress);
    }

    @Test
    public void testInetSocketAddressDeserializerIPv6() throws IOException {
        FromStringDeserializer.Std inetSocketAddressDeserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("[\"[2001:0db8:85a3:0000:0000:8a2e:0370:7334]:8080\"]");
        InetSocketAddress inetSocketAddress = inetSocketAddressDeserializer.deserialize(mapper.getFactory().createParser(node), new DeserializationContext(mapper.getConfig()));
        assertEquals(new InetSocketAddress(InetAddress.getByName("[2001:0db8:85a3:0000:0000:8a2e:0370:7334]"), 8080), inetSocketAddress);
    }
}