package com.example; // replace with your test package

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.TypeFactory;

import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.deser.std.Std;

@RunWith(MockitoJUnitRunner.class)
public class FromStringDeserializerTest {

    @Test
    public void testDeserializeStringToFile() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(File.class, FromStringDeserializer.findDeserializer(File.class));
        mapper.registerModule(module);
        String json = "\"C:\\test.txt\"";
        File file = mapper.readValue(json, File.class);
        assertEquals("C:\\test.txt", file.getAbsolutePath());
    }

    @Test
    public void testDeserializeStringToURL() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(URL.class, FromStringDeserializer.findDeserializer(URL.class));
        mapper.registerModule(module);
        String json = "\"https://www.google.com\"";
        URL url = mapper.readValue(json, URL.class);
        assertEquals("https://www.google.com", url.toString());
    }

    @Test
    public void testDeserializeStringToURI() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(URI.class, FromStringDeserializer.findDeserializer(URI.class));
        mapper.registerModule(module);
        String json = "\"https://www.google.com\"";
        URI uri = mapper.readValue(json, URI.class);
        assertEquals("https://www.google.com", uri.toString());
    }

    @Test
    public void testDeserializeStringToClass() throws IOException, ClassNotFoundException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(String.class, FromStringDeserializer.findDeserializer(String.class));
        mapper.registerModule(module);
        String json = "\"java.util.Locale\"";
        Class<?> clazz = mapper.readValue(json, Class.class);
        assertEquals(Locale.class, clazz);
    }

    @Test
    public void testDeserializeStringToJavaType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(String.class, FromStringDeserializer.findDeserializer(String.class));
        mapper.registerModule(module);
        String json = "\"com.example.CustomClass\"";
        JavaType javaType = mapper.getTypeFactory().constructType(json);
        assertEquals(javaType.getClass(), JavaType.class);
        assertEquals("com.example.CustomClass", json.substring(1, json.length() - 1));
    }

    @Test
    public void testDeserializeEmptyStringToCurrency() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Currency.class, FromStringDeserializer.findDeserializer(Currency.class));
        mapper.registerModule(module);
        assertNull(mapper.readValue("", Currency.class));
    }

    @Test
    public void testDeserializeStringToPattern() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Pattern.class, FromStringDeserializer.findDeserializer(Pattern.class));
        mapper.registerModule(module);
        String json = "\"[0-9]+\"";
        Pattern pattern = mapper.readValue(json, Pattern.class);
        assertEquals("[0-9]+", pattern.pattern());
    }

    @Test
    public void testDeserializeStringToLocale() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Locale.class, FromStringDeserializer.findDeserializer(Locale.class));
        mapper.registerModule(module);
        String json = "\"en_US\"";
        Locale locale = mapper.readValue(json, Locale.class);
        assertEquals("en_US", locale.toString());
    }

    @Test
    public void testDeserializeStringToCharset() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Charset.class, FromStringDeserializer.findDeserializer(Charset.class));
        mapper.registerModule(module);
        String json = "\"UTF-8\"";
        Charset charset = mapper.readValue(json, Charset.class);
        assertEquals(StandardCharsets.UTF_8, charset);
    }

    @Test
    public void testDeserializeStringToTimeZone() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(TimeZone.class, FromStringDeserializer.findDeserializer(TimeZone.class));
        mapper.registerModule(module);
        String json = "\"UTC\"";
        TimeZone timeZone = mapper.readValue(json, TimeZone.class);
        assertEquals("UTC", timeZone.getID());
    }

    @Test
    public void testDeserializeStringToInetAddress() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(InetAddress.class, FromStringDeserializer.findDeserializer(InetAddress.class));
        mapper.registerModule(module);
        String json = "\"192.168.0.1\"";
        InetAddress inetAddress = mapper.readValue(json, InetAddress.class);
        assertEquals("192.168.0.1", inetAddress.getHostAddress());
    }

    @Test
    public void testDeserializeStringToInetSocketAddress() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(InetSocketAddress.class, FromStringDeserializer.findDeserializer(InetSocketAddress.class));
        mapper.registerModule(module);
        String json = "\"[192.168.0.1]:80\"";
        InetSocketAddress inetSocketAddress = mapper.readValue(json, InetSocketAddress.class);
        assertEquals("192.168.0.1", inetSocketAddress.getAddress().getHostAddress());
        assertEquals(80, inetSocketAddress.getPort());
    }

    @Test
    public void testDeserializeEmptyStringToStringBuilder() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(StringBuilder.class, FromStringDeserializer.findDeserializer(StringBuilder.class));
        mapper.registerModule(module);
        assertNull(mapper.readValue("", StringBuilder.class));
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(String.class, FromStringDeserializer.findDeserializer(String.class));
        mapper.registerModule(module);
        String json = "\"invalid\"";
        mapper.readValue(json, String.class);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidURL() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(URL.class, FromStringDeserializer.findDeserializer(URL.class));
        mapper.registerModule(module);
        String json = "\"invalid:/\"";
        mapper.readValue(json, URL.class);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidURI() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(URI.class, FromStringDeserializer.findDeserializer(URI.class));
        mapper.registerModule(module);
        String json = "\"invalid:/\"";
        mapper.readValue(json, URI.class);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidClass() throws IOException, ClassNotFoundException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(String.class, FromStringDeserializer.findDeserializer(String.class));
        mapper.registerModule(module);
        String json = "\"InvalidClass\"";
        mapper.readValue(json, String.class);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidJavaType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(String.class, FromStringDeserializer.findDeserializer(String.class));
        mapper.registerModule(module);
        String json = "\"java.util.CustomList\"";
        mapper.readValue(json, JavaType.class);
    }

    @Test(expected = MalformedURLException.class)
    public void testDeserializeMalformedURL() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(URL.class, FromStringDeserializer.findDeserializer(URL.class));
        mapper.registerModule(module);
        String json = "\"invalid:/\"";
        mapper.readValue(json, URL.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeInvalidLocale() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Locale.class, FromStringDeserializer.findDeserializer(Locale.class));
        mapper.registerModule(module);
        String json = "\"en_US_US\"";
        Locale locale = mapper.readValue(json, Locale.class);
        assertNull(locale);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeInvalidCharset() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Charset.class, FromStringDeserializer.findDeserializer(Charset.class));
        mapper.registerModule(module);
        String json = "\"UTF-8_Invalid\"";
        Charset charset = mapper.readValue(json, Charset.class);
        assertNull(charset);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeInvalidTimeZone() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(TimeZone.class, FromStringDeserializer.findDeserializer(TimeZone.class));
        mapper.registerModule(module);
        String json = "\"invalid\"";
        TimeZone timeZone = mapper.readValue(json, TimeZone.class);
        assertNull(timeZone);
    }

    @Test(expected = InvalidFormatException.class)
    public void testDeserializeInvalidInetAddress() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(InetAddress.class, FromStringDeserializer.findDeserializer(InetAddress.class));
        mapper.registerModule(module);
        String json = "\"invalid\"";
        mapper.readValue(json, InetAddress.class);
    }

    @Test(expected = InvalidFormatException.class)
    public void testDeserializeInvalidInetSocketAddress() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(InetSocketAddress.class, FromStringDeserializer.findDeserializer(InetSocketAddress.class));
        mapper.registerModule(module);
        String json = "\"[invalid]:80\"";
        mapper.readValue(json, InetSocketAddress.class);
    }

    @Test
    public void testDeserializeArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(String[].class, new ArrayDeserializer(String[].class));
        mapper.registerModule(module);
        String json = "[\"String1\", \"String2\"]";
        String[] strings = mapper.readValue(json, String[].class);
        assertEquals("String1", strings[0]);
        assertEquals("String2", strings[1]);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeEmptyArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(String[].class, new ArrayDeserializer(String[].class));
        mapper.registerModule(module);
        String json = "[]";
        mapper.readValue(json, String[].class);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(String[].class, new ArrayDeserializer(String[].class));
        mapper.registerModule(module);
        String json = "[\"invalid\", \"String2\"]";
        mapper.readValue(json, String[].class);
    }

    private static class ArrayDeserializer extends StdDeserializer<Object> {
        private final JavaType _type;

        public ArrayDeserializer(JavaType valueType) {
            this(null, valueType);
        }

        protected ArrayDeserializer(DeserializationConfig config, JavaType valueType) {
            super(config, valueType);
            _type = valueType;
        }

        @Override
        public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            ObjectReader reader = ctxt.readerForArrayOf(_type.getComponentType());
            return reader.readValue(jp);
        }
    }
}