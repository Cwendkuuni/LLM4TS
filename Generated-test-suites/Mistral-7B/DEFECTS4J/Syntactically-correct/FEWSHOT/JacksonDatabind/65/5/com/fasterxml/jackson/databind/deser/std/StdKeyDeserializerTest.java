package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.StaticSubType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;

public class StdKeyDeserializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testForType() throws Exception {
        Class<?> rawType = String.class;
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(rawType);
        assertEquals(StdKeyDeserializer.class, keyDeserializer.getClass());
        assertEquals(rawType, keyDeserializer.getKeyClass());
        assertEquals(TYPE_STRING, keyDeserializer._kind);

        rawType = UUID.class;
        keyDeserializer = StdKeyDeserializer.forType(rawType);
        assertEquals(StdKeyDeserializer.class, keyDeserializer.getClass());
        assertEquals(rawType, keyDeserializer.getKeyClass());
        assertEquals(TYPE_UUID, keyDeserializer._kind);

        rawType = Integer.class;
        keyDeserializer = StdKeyDeserializer.forType(rawType);
        assertEquals(StdKeyDeserializer.class, keyDeserializer.getClass());
        assertEquals(rawType, keyDeserializer.getKeyClass());
        assertEquals(TYPE_INT, keyDeserializer._kind);

        rawType = Date.class;
        keyDeserializer = StdKeyDeserializer.forType(rawType);
        assertEquals(StdKeyDeserializer.class, keyDeserializer.getClass());
        assertEquals(rawType, keyDeserializer.getKeyClass());
        assertEquals(TYPE_DATE, keyDeserializer._kind);

        rawType = Calendar.class;
        keyDeserializer = StdKeyDeserializer.forType(rawType);
        assertEquals(StdKeyDeserializer.class, keyDeserializer.getClass());
        assertEquals(rawType, keyDeserializer.getKeyClass());
        assertEquals(TYPE_CALENDAR, keyDeserializer._kind);

        rawType = Boolean.class;
        keyDeserializer = StdKeyDeserializer.forType(rawType);
        assertEquals(StdKeyDeserializer.class, keyDeserializer.getClass());
        assertEquals(rawType, keyDeserializer.getKeyClass());
        assertEquals(TYPE_BOOLEAN, keyDeserializer._kind);

        rawType = Byte.class;
        keyDeserializer = StdKeyDeserializer.forType(rawType);
        assertEquals(StdKeyDeserializer.class, keyDeserializer.getClass());
        assertEquals(rawType, keyDeserializer.getKeyClass());
        assertEquals(TYPE_BYTE, keyDeserializer._kind);

        rawType = Character.class;
        keyDeserializer = StdKeyDeserializer.forType(rawType);
        assertEquals(StdKeyDeserializer.class, keyDeserializer.getClass());
        assertEquals(rawType, keyDeserializer.getKeyClass());
        assertEquals(TYPE_CHAR, keyDeserializer._kind);

        rawType = Short.class;
        keyDeserializer = StdKeyDeserializer.forType(rawType);
        assertEquals(StdKeyDeserializer.class, keyDeserializer.getClass());
        assertEquals(rawType, keyDeserializer.getKeyClass());
        assertEquals(TYPE_SHORT, keyDeserializer._kind);

        rawType = Float.class;
        keyDeserializer = StdKeyDeserializer.forType(rawType);
        assertEquals(StdKeyDeserializer.class, keyDeserializer.getClass());
        assertEquals(rawType, keyDeserializer.getKeyClass());
        assertEquals(TYPE_FLOAT, keyDeserializer._kind);

        rawType = Double.class;
        keyDeserializer = StdKeyDeserializer.forType(rawType);
        assertEquals(StdKeyDeserializer.class, keyDeserializer.getClass());
        assertEquals(rawType, keyDeserializer.getKeyClass());
        assertEquals(TYPE_DOUBLE, keyDeserializer._kind);

        rawType = URI.class;
        keyDeserializer = StdKeyDeserializer.forType(rawType);
        assertEquals(StdKeyDeserializer.class, keyDeserializer.getClass());
        assertEquals(rawType, keyDeserializer.getKeyClass());
        assertEquals(TYPE_URI, keyDeserializer._kind);

        rawType = URL.class;
        keyDeserializer = StdKeyDeserializer.forType(rawType);
        assertEquals(StdKeyDeserializer.class, keyDeserializer.getClass());
        assertEquals(rawType, keyDeserializer.getKeyClass());
        assertEquals(TYPE_URL, keyDeserializer._kind);

        rawType = Locale.class;
        keyDeserializer = StdKeyDeserializer.forType(rawType);
        assertEquals(StdKeyDeserializer.class, keyDeserializer.getClass());
        assertEquals(rawType, keyDeserializer.getKeyClass());
        assertEquals(TYPE_LOCALE, keyDeserializer._kind);

        rawType = Currency.class;
        keyDeserializer = StdKeyDeserializer.forType(rawType);
        assertEquals(StdKeyDeserializer.class, keyDeserializer.getClass());
        assertEquals(rawType, keyDeserializer.getKeyClass());
        assertEquals(TYPE_CURRENCY, keyDeserializer._kind);

        rawType = Enum.class; // Should not match any known type
        keyDeserializer = StdKeyDeserializer.forType(rawType);
        assertNull(keyDeserializer);
    }

    @Test
    public void testDeserializeKey() throws Exception {
        Class<?> rawType = Boolean.class;
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(rawType);

        String key = "true";
        Object deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertEquals(Boolean.TRUE, deserializedKey);

        key = "false";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertEquals(Boolean.FALSE, deserializedKey);

        key = "123";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertEquals(Integer.valueOf(123), deserializedKey);

        key = "456";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertEquals(Integer.valueOf(456), deserializedKey);

        key = "12.3";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertEquals(Float.valueOf(12.3f), deserializedKey);

        key = "45.6";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertEquals(Float.valueOf(45.6f), deserializedKey);

        key = "1234567890";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertEquals(Long.valueOf(1234567890L), deserializedKey);

        key = "1234567890123";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertEquals(Long.valueOf(1234567890123L), deserializedKey);

        key = "2022-12-31";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertEquals(Date.class, deserializedKey.getClass());
        assertEquals("Fri Dec 31 00:00:00 CET 2022", deserializedKey.toString());

        key = "2022-12-31T23:59:59Z";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertEquals(Date.class, deserializedKey.getClass());
        assertEquals("Fri Dec 31 23:59:59 CET 2022", deserializedKey.toString());

        key = "2022-12-31T23:59:59.123Z";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertEquals(Date.class, deserializedKey.getClass());
        assertEquals("Fri Dec 31 23:59:59.123 CET 2022", deserializedKey.toString());

        key = "en_US";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertEquals(Locale.US, deserializedKey);

        key = "EUR";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertEquals(Currency.EUR, deserializedKey);

        key = "2022-12-31";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertEquals(Date.class, deserializedKey.getClass());

        key = "2022-12-31T23:59:59Z";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertEquals(Date.class, deserializedKey.getClass());

        key = "2022-12-31T23:59:59.123Z";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertEquals(Date.class, deserializedKey.getClass());

        key = "de.Munchen";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertEquals(Locale.class, deserializedKey.getClass());

        key = "EUR";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertEquals(Currency.class, deserializedKey.getClass());

        key = "12345678-1234-1234-1234-123456789012";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertEquals(UUID.class, deserializedKey.getClass());
        assertEquals("12345678-1234-1234-1234-123456789012", deserializedKey.toString());

        key = "http://example.com/api/v1";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertEquals(URL.class, deserializedKey.getClass());
        assertEquals("http://example.com/api/v1", deserializedKey.toString());

        key = "http://example.com/api/v1";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertEquals(URI.class, deserializedKey.getClass());
        assertEquals(new URI("http://example.com/api/v1"), deserializedKey);

        key = "com.example.MyClass";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertEquals(Class.class, deserializedKey.getClass());
        assertEquals(Class.forName(key), deserializedKey);

        key = "com.example.MyClass";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertEquals(Class.class, deserializedKey.getClass());
        assertEquals(Class.forName(key), deserializedKey);

        key = "MyClass";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertEquals(Class.class, deserializedKey.getClass());
        assertEquals(Class.forName("com.example.MyClass"), deserializedKey);

        key = "MyClassWithSubTypes";
        StdKeyDeserializer subKeyDeserializer = StdKeyDeserializer.forType(MyClassWithSubTypes.class);
        deserializedKey = subKeyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertEquals(MyClassWithSubTypes.class, deserializedKey.getClass());

        key = "subType1";
        deserializedKey = subKeyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertEquals(SubType1.class, deserializedKey.getClass());

        key = "subType2";
        deserializedKey = subKeyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertEquals(SubType2.class, deserializedKey.getClass());

        key = "invalid";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertInstanceOf(JsonMappingException.class, deserializedKey);

        key = "null";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertNull(deserializedKey);

        key = "not_a_valid_representation";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertInstanceOf(RuntimeException.class, deserializedKey);
    }

    static class MyClassWithSubTypes {
        @JsonSubTypes({
            @StaticSubType(value = SubType1.class, name = "subType1"),
            @StaticSubType(value = SubType2.class, name = "subType2")
        })
        public static class SubType1 {}
        public static class SubType2 {}
    }
}