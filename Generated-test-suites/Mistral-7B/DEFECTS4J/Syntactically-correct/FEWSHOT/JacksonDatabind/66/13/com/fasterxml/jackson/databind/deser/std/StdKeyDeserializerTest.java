package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.MethodFunction;
import com.fasterxml.jackson.databind.introspect.NestedAnnotation;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.FinderCache;
import com.fasterxml.jackson.databind.util.Named;
import org.junit.Test;

public class StdKeyDeserializerTest {

    @Test
    public void testForType() {
        Class<?> raw = String.class;
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(raw);
        assertEquals(StdKeyDeserializer.StringKD.class, keyDeserializer.getClass());

        raw = UUID.class;
        keyDeserializer = StdKeyDeserializer.forType(raw);
        assertEquals(StdKeyDeserializer.StdKeyDeserializer.class, keyDeserializer.getClass());
        assertEquals(StdKeyDeserializer.TYPE_UUID, keyDeserializer._kind);

        raw = Integer.class;
        keyDeserializer = StdKeyDeserializer.forType(raw);
        assertEquals(StdKeyDeserializer.StdKeyDeserializer.class, keyDeserializer.getClass());
        assertEquals(StdKeyDeserializer.TYPE_INT, keyDeserializer._kind);

        raw = Date.class;
        keyDeserializer = StdKeyDeserializer.forType(raw);
        assertEquals(StdKeyDeserializer.StdKeyDeserializer.class, keyDeserializer.getClass());
        assertEquals(StdKeyDeserializer.TYPE_DATE, keyDeserializer._kind);

        raw = Calendar.class;
        keyDeserializer = StdKeyDeserializer.forType(raw);
        assertEquals(StdKeyDeserializer.StdKeyDeserializer.class, keyDeserializer.getClass());
        assertEquals(StdKeyDeserializer.TYPE_CALENDAR, keyDeserializer._kind);

        raw = Boolean.class;
        keyDeserializer = StdKeyDeserializer.forType(raw);
        assertEquals(StdKeyDeserializer.EnumKD.class, keyDeserializer.getClass());
        assertEquals(StdKeyDeserializer.TYPE_BOOLEAN, keyDeserializer._kind);

        raw = Byte.class;
        keyDeserializer = StdKeyDeserializer.forType(raw);
        assertEquals(StdKeyDeserializer.StdKeyDeserializer.class, keyDeserializer.getClass());
        assertEquals(StdKeyDeserializer.TYPE_BYTE, keyDeserializer._kind);

        raw = Character.class;
        keyDeserializer = StdKeyDeserializer.forType(raw);
        assertEquals(StdKeyDeserializer.StdKeyDeserializer.class, keyDeserializer.getClass());
        assertEquals(StdKeyDeserializer.TYPE_CHAR, keyDeserializer._kind);

        raw = Short.class;
        keyDeserializer = StdKeyDeserializer.forType(raw);
        assertEquals(StdKeyDeserializer.StdKeyDeserializer.class, keyDeserializer.getClass());
        assertEquals(StdKeyDeserializer.TYPE_SHORT, keyDeserializer._kind);

        raw = Float.class;
        keyDeserializer = StdKeyDeserializer.forType(raw);
        assertEquals(StdKeyDeserializer.StdKeyDeserializer.class, keyDeserializer.getClass());
        assertEquals(StdKeyDeserializer.TYPE_FLOAT, keyDeserializer._kind);

        raw = Double.class;
        keyDeserializer = StdKeyDeserializer.forType(raw);
        assertEquals(StdKeyDeserializer.StdKeyDeserializer.class, keyDeserializer.getClass());
        assertEquals(StdKeyDeserializer.TYPE_DOUBLE, keyDeserializer._kind);

        raw = URI.class;
        keyDeserializer = StdKeyDeserializer.forType(raw);
        assertEquals(StdKeyDeserializer.StdKeyDeserializer.class, keyDeserializer.getClass());
        assertEquals(StdKeyDeserializer.TYPE_URI, keyDeserializer._kind);

        raw = URL.class;
        keyDeserializer = StdKeyDeserializer.forType(raw);
        assertEquals(StdKeyDeserializer.StdKeyDeserializer.class, keyDeserializer.getClass());
        assertEquals(StdKeyDeserializer.TYPE_URL, keyDeserializer._kind);

        raw = Class.class;
        keyDeserializer = StdKeyDeserializer.forType(raw);
        assertEquals(StdKeyDeserializer.StdKeyDeserializer.class, keyDeserializer.getClass());
        assertEquals(StdKeyDeserializer.TYPE_CLASS, keyDeserializer._kind);

        raw = Locale.class;
        keyDeserializer = StdKeyDeserializer.forType(raw);
        assertEquals(StdKeyDeserializer.DelegateingKD.class, keyDeserializer.getClass());
        assertEquals(StdKeyDeserializer.TYPE_LOCALE, keyDeserializer._kind);

        raw = Currency.class;
        keyDeserializer = StdKeyDeserializer.forType(raw);
        assertEquals(StdKeyDeserializer.DelegateingKD.class, keyDeserializer.getClass());
        assertEquals(StdKeyDeserializer.TYPE_CURRENCY, keyDeserializer._kind);

        raw = new Object() {}.getClass();
        keyDeserializer = StdKeyDeserializer.forType(raw);
        assertEquals(StdKeyDeserializer.StringKD.class, keyDeserializer.getClass());
        assertEquals(StdKeyDeserializer.TYPE_STRING, keyDeserializer._kind);

        raw = null;
        keyDeserializer = StdKeyDeserializer.forType(raw);
        assertNull(keyDeserializer);
    }

    @Test
    public void testDeserializeKey() throws IOException, JsonProcessingException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);

        // Test for empty key
        String key = null;
        Object deserializedKey = keyDeserializer.deserializeKey(key, null);
        assertEquals(null, deserializedKey);

        // Test for boolean keys
        key = "true";
        deserializedKey = keyDeserializer.deserializeKey(key, null);
        assertEquals(Boolean.TRUE, deserializedKey);

        key = "false";
        deserializedKey = keyDeserializer.deserializeKey(key, null);
        assertEquals(Boolean.FALSE, deserializedKey);

        // Test for byte keys
        key = "127";
        deserializedKey = keyDeserializer.deserializeKey(key, null);
        assertEquals((byte) 127, deserializedKey);

        // Test for short keys
        key = "32767";
        deserializedKey = keyDeserializer.deserializeKey(key, null);
        assertEquals((short) 32767, deserializedKey);

        // Test for char keys
        key = "A";
        deserializedKey = keyDeserializer.deserializeKey(key, null);
        assertEquals('A', deserializedKey);

        // Test for int keys
        key = "12345";
        deserializedKey = keyDeserializer.deserializeKey(key, null);
        assertEquals(12345, deserializedKey);

        // Test for long keys
        key = "123456789012345";
        deserializedKey = keyDeserializer.deserializeKey(key, null);
        assertEquals(123456789012345L, deserializedKey);

        // Test for float keys
        key = "123.45";
        deserializedKey = keyDeserializer.deserializeKey(key, null);
        assertEquals(123.45f, deserializedKey);

        // Test for double keys
        key = "123.456";
        deserializedKey = keyDeserializer.deserializeKey(key, null);
        assertEquals(123.456, deserializedKey);

        // Test for locale keys
        AnnotationIntrospector introspector = new JacksonAnnotationIntrospector();
        AnnotatedClass<Locale> localeAnnotationClass = introspector.findAnnotatedClass(Locale.class, null);
        MethodFactory methodFactory = introspector.findMethod(localeAnnotationClass, null, "getDisplayName", new Class[]{}, new Named[]{"value"});
        MethodFunction methodFunction = methodFactory.getMethodFunction();
        Object localeValue = methodFunction.invoke(Locale.US, "en_US");
        keyDeserializer = StdKeyDeserializer.forType(Locale.class);
        deserializedKey = keyDeserializer.deserializeKey(localeValue.toString(), null);
        assertEquals(Locale.US, deserializedKey);

        // Test for URI keys
        key = "http://example.com";
        URI uri = new URI(key);
        deserializedKey = keyDeserializer.deserializeKey(uri.toString(), null);
        assertEquals(uri, deserializedKey);

        // Test for URL keys
        key = "http://example.com";
        URL url = new URL(key);
        deserializedKey = keyDeserializer.deserializeKey(url.toString(), null);
        assertEquals(url, deserializedKey);

        // Test for class keys
        keyDeserializer = StdKeyDeserializer.forType(String[].class);
        Class<?> clazz = Class.forName("java.lang.String[]");
        deserializedKey = keyDeserializer.deserializeKey(clazz.getName(), null);
        assertEquals(clazz, deserializedKey);

        // Test for enum keys
        EnumResolver enumResolver = FinderCache.weakCache.findEnumResolver(null);
        AnnotatedMethod enumMethod = introspector.findMethod(enumResolver.getEnumClass(Enum.class), null, "valueOf", new Class[]{String.class});
        Object enumValue = enumMethod.invoke(null, "FOO");
        keyDeserializer = StdKeyDeserializer.forType(enumResolver.getEnumClass(Enum.class));
        deserializedKey = keyDeserializer.deserializeKey(enumValue.toString(), null);
        assertEquals(enumValue, deserializedKey);

        // Test for invalid keys
        key = "invalid";
        deserializedKey = keyDeserializer.deserializeKey(key, null);
        assertNotNull(deserializedKey);
        assertEquals("not a valid representation", deserializedKey.getClass().getCanonicalName());

        // Test for null keys
        key = null;
        deserializedKey = keyDeserializer.deserializeKey(key, null);
        assertNull(deserializedKey);
    }
}