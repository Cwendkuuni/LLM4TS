package com.example.test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class StdKeyDeserializerTest {

    private ObjectMapper objectMapper;
    private Properties properties;

    @Before
    public void setup() throws IOException {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        properties = new Properties();
    }

    @Test
    public void testForType() {
        Class<?> raw = String.class;
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(raw);
        assertEquals(StdKeyDeserializer.class, keyDeserializer.getClass());
        assertEquals(String.class, keyDeserializer.getKeyClass());

        raw = Integer.class;
        keyDeserializer = StdKeyDeserializer.forType(raw);
        assertEquals(StdKeyDeserializer.class, keyDeserializer.getClass());
        assertEquals(Integer.class, keyDeserializer.getKeyClass());

        raw = Double.class;
        keyDeserializer = StdKeyDeserializer.forType(raw);
        assertEquals(StdKeyDeserializer.class, keyDeserializer.getClass());
        assertEquals(Double.class, keyDeserializer.getKeyClass());
    }

    @Test
    public void testDeserializeKey() throws IOException {
        StdKeyDeserializer stringKD = StdKeyDeserializer.StringKD.forType(String.class);
        assertEquals("string", stringKD.deserializeKey("string", new DeserializationContext(objectMapper, properties)));

        StdKeyDeserializer booleanKD = StdKeyDeserializer.forType(Boolean.class);
        assertEquals(Boolean.TRUE, booleanKD.deserializeKey("true", new DeserializationContext(objectMapper, properties)));
        assertEquals(Boolean.FALSE, booleanKD.deserializeKey("false", new DeserializationContext(objectMapper, properties)));

        StdKeyDeserializer enumKD = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class, null);
        assertInstanceOf(Locale.class, enumKD.deserializeKey("en_US", new DeserializationContext(objectMapper, properties)));

        StdKeyDeserializer stringCtorKD = new StdKeyDeserializer(String[].class.getConstructor(String.class), null);
        String[] strings = {"one", "two"};
        assertEquals(strings, stringCtorKD.deserializeKey("[\"one\", \"two\"]", new DeserializationContext(objectMapper, properties)));
    }

    @Test
    public void testParse() throws IOException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        StdKeyDeserializer intKD = StdKeyDeserializer.forType(Integer.class);
        assertEquals(1, intKD._parse("1", new DeserializationContext(objectMapper, properties)).intValue());

        StdKeyDeserializer longKD = StdKeyDeserializer.forType(Long.class);
        assertEquals(1L, longKD._parse("1", new DeserializationContext(objectMapper, properties)).longValue());

        StdKeyDeserializer doubleKD = StdKeyDeserializer.forType(Double.class);
        assertEquals(1.0, doubleKD._parse("1.0", new DeserializationContext(objectMapper, properties)).doubleValue(), 0.0);

        StdKeyDeserializer uriKD = StdKeyDeserializer.forType(URI.class);
        URI uri = new URI("http://example.com");
        assertEquals(uri, uriKD._parse("http://example.com", new DeserializationContext(objectMapper, properties)));

        StdKeyDeserializer urlKD = StdKeyDeserializer.forType(URL.class);
        URL url = new URL("http://example.com");
        assertEquals(url, urlKD._parse("http://example.com", new DeserializationContext(objectMapper, properties)));
    }

    @Test
    public void testHandleWeirdKey() throws IOException {
        StdKeyDeserializer intKD = StdKeyDeserializer.forType(Integer.class);
        String message = intKD.deserializeKey("invalidKey", new DeserializationContext(objectMapper, properties)).toString();
        assertTrue(message.contains("not a valid representation"));

        String message2 = intKD.deserializeKey(null, new DeserializationContext(objectMapper, properties)).toString();
        assertTrue(message2.contains("is null"));

        StdKeyDeserializer enumKD = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class, null);
        String message3 = enumKD.deserializeKey("invalidLocale", new DeserializationContext(objectMapper, properties)).toString();
        assertTrue(message3.contains("not a valid representation"));
    }

    @Test
    public void testDeserializeKeyWithEnumResolver() throws IOException {
        StdKeyDeserializer enumKD = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class, null);
        EnumResolver enumResolver = new EnumResolver(Locale.class, null);
        Locale locale = new Locale("en", "US");

        assertEquals(locale, enumKD.deserializeKey("en_US", new DeserializationContext(objectMapper, properties, new EnumSet<>(EnumSet.allOf(Locale.LanguageRange.class)), enumResolver)));
        assertEquals(locale, enumKD.deserializeKey("en_US", new DeserializationContext(objectMapper, properties)));

        assertThrows(IllegalArgumentException.class, () -> enumKD.deserializeKey("invalidLocale", new DeserializationContext(objectMapper, properties, new EnumSet<>(EnumSet.allOf(Locale.LanguageRange.class)), enumResolver)));
        assertThrows(IllegalArgumentException.class, () -> enumKD.deserializeKey("invalidLocale", new DeserializationContext(objectMapper, properties)));
    }

    @Test
    public void testDeserializeKeyWithEnumResolverUnsupported() throws IOException {
        StdKeyDeserializer enumKD = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, MyCustomEnum.class, null);
        assertNull(enumKD.deserializeKey("MyCustomEnum_VALUE", new DeserializationContext(objectMapper, properties)));
    }

    enum MyCustomEnum {
        MyCustomEnum_VALUE
    }
}