package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

public class FromStringDeserializerTest {

    @Test
    public void testFileDeserializer() throws IOException {
        FromStringDeserializer<File> fileDeserializer = (FromStringDeserializer<File>) FromStringDeserializer.findDeserializer(File.class);
        DeserializationContext ctx = new TestContext();
        JsonParser parser = ctx.createParser("\"/path/to/file.txt\"");
        File result = fileDeserializer.deserialize(parser, ctx);
        assertEquals("/path/to/file.txt", result.getAbsolutePath());
    }

    @Test
    public void testUrlDeserializer() throws IOException {
        FromStringDeserializer<URL> urlDeserializer = (FromStringDeserializer<URL>) FromStringDeserializer.findDeserializer(URL.class);
        DeserializationContext ctx = new TestContext();
        JsonParser parser = ctx.createParser("\"http://example.com\"");
        URL result = urlDeserializer.deserialize(parser, ctx);
        assertEquals("http://example.com", result.toString());
    }

    @Test
    public void testUriDeserializer() throws IOException {
        FromStringDeserializer<URI> uriDeserializer = (FromStringDeserializer<URI>) FromStringDeserializer.findDeserializer(URI.class);
        DeserializationContext ctx = new TestContext();
        JsonParser parser = ctx.createParser("\"uri://example.com/path\"");
        URI result = uriDeserializer.deserialize(parser, ctx);
        assertEquals("uri://example.com/path", result.toString());
    }

    @Test
    public void testClassDeserializer() throws IOException {
        FromStringDeserializer<Class<?>> classDeserializer = (FromStringDeserializer<Class<?>>) FromStringDeserializer.findDeserializer(String.class);
        DeserializationContext ctx = new TestContext();
        JsonParser parser = ctx.createParser("\"java.lang.String\"");
        Class<?> result = classDeserializer.deserialize(parser, ctx);
        assertEquals(String.class, result);
    }

    @Test
    public void testJavaTypeDeserializer() throws IOException {
        FromStringDeserializer<JavaType> javaTypeDeserializer = (FromStringDeserializer<JavaType>) FromStringDeserializer.findDeserializer(JavaType.class);
        DeserializationContext ctx = new TestContext();
        JsonParser parser = ctx.createParser("\"com.example.MyClass\"");
        JavaType result = javaTypeDeserializer.deserialize(parser, ctx);
        assertEquals("com.example.MyClass", result.getName());
    }

    @Test
    public void testCurrencyDeserializer() throws IOException {
        FromStringDeserializer<Currency> currencyDeserializer = (FromStringDeserializer<Currency>) FromStringDeserializer.findDeserializer(Currency.class);
        DeserializationContext ctx = new TestContext();
        JsonParser parser = ctx.createParser("\"USD\"");
        Currency result = currencyDeserializer.deserialize(parser, ctx);
        assertEquals(Currency.getInstance("USD"), result);
    }

    @Test
    public void testPatternDeserializer() throws IOException {
        FromStringDeserializer<Pattern> patternDeserializer = (FromStringDeserializer<Pattern>) FromStringDeserializer.findDeserializer(Pattern.class);
        DeserializationContext ctx = new TestContext();
        JsonParser parser = ctx.createParser("\"/[a-zA-Z]+/g\"");
        Pattern result = patternDeserializer.deserialize(parser, ctx);
        assertEquals("/[a-zA-Z]+/g", result.pattern());
    }

    @Test
    public void testLocaleDeserializer() throws IOException {
        FromStringDeserializer<Locale> localeDeserializer = (FromStringDeserializer<Locale>) FromStringDeserializer.findDeserializer(Locale.class);
        DeserializationContext ctx = new TestContext();
        JsonParser parser = ctx.createParser("\"en_US\"");
        Locale result = localeDeserializer.deserialize(parser, ctx);
        assertEquals("en_US", result.toString());
    }

    @Test
    public void testCharsetDeserializer() throws IOException {
        FromStringDeserializer<Charset> charsetDeserializer = (FromStringDeserializer<Charset>) FromStringDeserializer.findDeserializer(Charset.class);
        DeserializationContext ctx = new TestContext();
        JsonParser parser = ctx.createParser("\"UTF-8\"");
        Charset result = charsetDeserializer.deserialize(parser, ctx);
        assertEquals("UTF-8", result.name());
    }

    @Test
    public void testTimeZoneDeserializer() throws IOException {
        FromStringDeserializer<TimeZone> timeZoneDeserializer = (FromStringDeserializer<TimeZone>) FromStringDeserializer.findDeserializer(TimeZone.class);
        DeserializationContext ctx = new TestContext();
        JsonParser parser = ctx.createParser("\"America/Los_Angeles\"");
        TimeZone result = timeZoneDeserializer.deserialize(parser, ctx);
        assertEquals("America/Los_Angeles", result.getID());
    }

    @Test
    public void testInetAddressDeserializer() throws IOException {
        FromStringDeserializer<InetAddress> inetAddressDeserializer = (FromStringDeserializer<InetAddress>) FromStringDeserializer.findDeserializer(InetAddress.class);
        DeserializationContext ctx = new TestContext();
        JsonParser parser = ctx.createParser("\"192.168.1.1\"");
        InetAddress result = inetAddressDeserializer.deserialize(parser, ctx);
        assertEquals("192.168.1.1", result.getHostAddress());
    }

    @Test
    public void testInetSocketAddressDeserializer() throws IOException {
        FromStringDeserializer<InetSocketAddress> inetSocketAddressDeserializer = (FromStringDeserializer<InetSocketAddress>) FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        DeserializationContext ctx = new TestContext();
        JsonParser parser = ctx.createParser("\"[255.255.255.255]:80\"");
        InetSocketAddress result = inetSocketAddressDeserializer.deserialize(parser, ctx);
        assertEquals("[255.255.255.255]:80", result.toString());
    }

    @Test
    public void testEmptyStringDeserializer() throws IOException {
        FromStringDeserializer<?> emptyStringDeserializer = (FromStringDeserializer<?>) FromStringDeserializer.findDeserializer(String.class);
        DeserializationContext ctx = new TestContext();
        JsonParser parser = ctx.createParser("\"\"");
        Object result = emptyStringDeserializer.deserialize(parser, ctx);
        assertNull(result);
    }

    @Test(expected = JsonMappingException.class)
    public void testUnknownDeserializer() throws IOException {
        FromStringDeserializer<?> unknownDeserializer = (FromStringDeserializer<?>) FromStringDeserializer.findDeserializer(Integer.class);
        DeserializationContext ctx = new TestContext();
        JsonParser parser = ctx.createParser("\"42\"");
        unknownDeserializer.deserialize(parser, ctx);
    }
}

class TestContext extends DeserializationContext {
    @Override
    public Object instantiateValue(Class<?> valueType) throws InstantiationException, IllegalAccessException {
        if (valueType == Object.class) {
            return new Object();
        }
        throw new InstantiationException();
    }
}