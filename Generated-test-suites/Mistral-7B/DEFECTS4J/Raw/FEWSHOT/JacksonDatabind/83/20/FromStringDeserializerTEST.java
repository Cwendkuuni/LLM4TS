package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.deser.std.Std;

public class FromStringDeserializerTest {

    private static <T> T deserialize(FromStringDeserializer<T> deserializer, String input,
            DeserializationContext context) throws IOException {
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser(new StringReader(input));
        return deserializer.deserialize(parser, context);
    }

    @Test
    public void testStdDeserializeStringFile() throws IOException {
        String input = "path/to/file.txt";
        FromStringDeserializer<File> deserializer = FromStringDeserializer.findDeserializer(File.class);
        File file = deserialize(deserializer, input, new DeserializationContext(new AnnotationIntrospector()));
        assertEquals(new File(input), file);
    }

    @Test
    public void testStdDeserializeStringURL() throws IOException {
        String input = "http://example.com";
        FromStringDeserializer<URL> deserializer = FromStringDeserializer.findDeserializer(URL.class);
        URL url = deserialize(deserializer, input, new DeserializationContext(new AnnotationIntrospector()));
        assertEquals(new URL(input), url);
    }

    @Test
    public void testStdDeserializeStringURI() throws IOException {
        String input = "http://example.com/path?query=value#fragment";
        FromStringDeserializer<URI> deserializer = FromStringDeserializer.findDeserializer(URI.class);
        URI uri = deserialize(deserializer, input, new DeserializationContext(new AnnotationIntrospector()));
        assertEquals(new URI(input), uri);
    }

    @Test
    public void testStdDeserializeStringClass() throws IOException {
        String input = "java.lang.String";
        FromStringDeserializer<Class<?>> deserializer = FromStringDeserializer.findDeserializer(Class.class);
        Class<?> clazz = deserialize(deserializer, input, new DeserializationContext(new AnnotationIntrospector()));
        assertEquals(Class.forName(input), clazz);
    }

    @Test
    public void testStdDeserializeStringJavaType() throws IOException {
        String input = "java.util.List<java.lang.Integer>";
        FromStringDeserializer<JavaType> deserializer = FromStringDeserializer.findDeserializer(JavaType.class);
        JavaType javaType = deserializer.deserialize(new JsonFactory(), new ObjectMapper().getSerializationConfig(), input);
        assertEquals(new TypeFactory().constructType(java.util.List.class, java.lang.Integer.class), javaType);
    }

    @Test
    public void testStdDeserializeStringCurrency() throws IOException {
        String input = "USD";
        FromStringDeserializer<Currency> deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        Currency currency = deserialize(deserializer, input, new DeserializationContext(new AnnotationIntrospector()));
        assertEquals(Currency.getInstance(input), currency);
    }

    @Test
    public void testStdDeserializeStringPattern() throws IOException {
        String input = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";
        FromStringDeserializer<Pattern> deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        Pattern pattern = deserialize(deserializer, input, new DeserializationContext(new AnnotationIntrospector()));
        assertInstanceOf(Pattern.class, pattern);
    }

    @Test
    public void testStdDeserializeStringLocale() throws IOException {
        String input = "en_US";
        FromStringDeserializer<Locale> deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        Locale locale = deserialize(deserializer, input, new DeserializationContext(new AnnotationIntrospector()));
        assertEquals(Locale.forLanguageTag(input), locale);
    }

    @Test
    public void testStdDeserializeStringCharset() throws IOException {
        String input = "UTF-8";
        FromStringDeserializer<Charset> deserializer = FromStringDeserializer.findDeserializer(Charset.class);
        Charset charset = deserialize(deserializer, input, new DeserializationContext(new AnnotationIntrospector()));
        assertEquals(Charset.forName(input), charset);
    }

    @Test
    public void testStdDeserializeStringTimeZone() throws IOException {
        String input = "America/New_York";
        FromStringDeserializer<TimeZone> deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        TimeZone timeZone = deserialize(deserializer, input, new DeserializationContext(new AnnotationIntrospector()));
        assertEquals(TimeZone.getTimeZone(input), timeZone);
    }

    @Test
    public void testStdDeserializeStringInetAddress() throws IOException {
        String input = "192.168.1.1";
        FromStringDeserializer<InetAddress> deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        InetAddress inetAddress = deserialize(deserializer, input, new DeserializationContext(new AnnotationIntrospector()));
        assertEquals(InetAddress.getByName(input), inetAddress);
    }

    @Test
    public void testStdDeserializeStringInetSocketAddress() throws IOException {
        String input = "[255.255.255.255]:8080";
        FromStringDeserializer<InetSocketAddress> deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        InetSocketAddress inetSocketAddress = deserialize(deserializer, input, new DeserializationContext(new AnnotationIntrospector()));
        assertEquals(new InetSocketAddress(InetAddress.getByName("255.255.255.255"), 8080), inetSocketAddress);
    }

    @Test
    public void testStdDeserializeStringStringBuilder() throws IOException {
        String input = "abcdef";
        FromStringDeserializer<StringBuilder> deserializer = FromStringDeserializer.findDeserializer(StringBuilder.class);
        StringBuilder stringBuilder = deserialize(deserializer, input, new DeserializationContext(new AnnotationIntrospector()));
        assertEquals(new StringBuilder(input), stringBuilder);
    }

    @Test(expected = JsonMappingException.class)
    public void testStdDeserializeUnknownType() throws IOException {
        String input = "java.util.ArrayList<java.lang.Object>";
        FromStringDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(Object.class);
        deserialize(deserializer, input, new DeserializationContext(new AnnotationIntrospector()));
    }

    @Test(expected = JsonMappingException.class)
    public void testStdDeserializeEmptyString() throws IOException {
        String input = "";
        FromStringDeserializer<StringBuilder> deserializer = FromStringDeserializer.findDeserializer(StringBuilder.class);
        deserialize(deserializer, input, new DeserializationContext(new AnnotationIntrospector()));
    }

    @Test
    public void testStdDeserializeNull() throws IOException {
        FromStringDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(Object.class);
        Object result = deserialize(deserializer, null, new DeserializationContext(new AnnotationIntrospector()));
        assertNull(result);
    }

    @Test
    public void testStdDeserializeEmptyClass() throws IOException {
        String input = "com.example.MyEmptyClass";
        FromStringDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(Object.class);
        Object result = deserialize(deserializer, input, new DeserializationContext(new AnnotationIntrospector()));
        assertEquals(Object.class, result.getClass());
    }
}