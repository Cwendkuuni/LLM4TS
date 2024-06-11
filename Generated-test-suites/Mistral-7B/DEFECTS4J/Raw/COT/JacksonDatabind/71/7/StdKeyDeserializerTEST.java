package com.example.test; // replace with your test package name

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer.None;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;

import java.io.IOException;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.EnumSet;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.UUID;

/**
 * StdKeyDeserializer test file.
 */
public class StdKeyDeserializerTest
{
    

    @org.junit.Test
    public void testForType() throws Exception
    {
        Class<?> raw = String.class;
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(raw);
        assertEquals(StdKeyDeserializer.TYPE_STRING, keyDeserializer.getKind());
        assertEquals(String.class, keyDeserializer.getKeyClass());

        raw = UUID.class;
        keyDeserializer = StdKeyDeserializer.forType(raw);
        assertEquals(StdKeyDeserializer.TYPE_UUID, keyDeserializer.getKind());
        assertEquals(UUID.class, keyDeserializer.getKeyClass());

        raw = Integer.class;
        keyDeserializer = StdKeyDeserializer.forType(raw);
        assertEquals(StdKeyDeserializer.TYPE_INT, keyDeserializer.getKind());
        assertEquals(Integer.class, keyDeserializer.getKeyClass());

        raw = Date.class;
        keyDeserializer = StdKeyDeserializer.forType(raw);
        assertEquals(StdKeyDeserializer.TYPE_DATE, keyDeserializer.getKind());
        assertEquals(Date.class, keyDeserializer.getKeyClass());
    }

    @org.junit.Test
    public void testDeserializeKey() throws Exception
    {
        ObjectMapper objectMapper = new ObjectMapper();
        DeserializationContext context = objectMapper.getFactory().createContext();

        String key = "true";
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
        Object result = keyDeserializer.deserializeKey(key, context);
        assertTrue((Boolean) result);

        key = "false";
        result = keyDeserializer.deserializeKey(key, context);
        assertFalse((Boolean) result);

        key = "1";
        keyDeserializer = StdKeyDeserializer.forType(Byte.class);
        result = keyDeserializer.deserializeKey(key, context);
        assertEquals((Byte) Byte.parseByte("1"), result);

        key = "100";
        keyDeserializer = StdKeyDeserializer.forType(Short.class);
        result = keyDeserializer.deserializeKey(key, context);
        assertEquals((Short) Short.parseShort("100"), result);

        key = "hello";
        keyDeserializer = StdKeyDeserializer.forType(Character.class);
        result = keyDeserializer.deserializeKey(key, context);
        assertEquals(key.charAt(0), ((Character) result).charValue());

        key = "10";
        keyDeserializer = StdKeyDeserializer.forType(Integer.class);
        result = keyDeserializer.deserializeKey(key, context);
        assertEquals(Integer.parseInt(key), result);

        key = "100L";
        keyDeserializer = StdKeyDeserializer.forType(Long.class);
        result = keyDeserializer.deserializeKey(key, context);
        assertEquals(Long.parseLong(key), result);

        key = "3.14";
        keyDeserializer = StdKeyDeserializer.forType(Float.class);
        result = keyDeserializer.deserializeKey(key, context);
        assertEquals(Float.parseFloat(key), result);

        key = "3.1415926536";
        keyDeserializer = StdKeyDeserializer.forType(Double.class);
        result = keyDeserializer.deserializeKey(key, context);
        assertEquals(Double.parseDouble(key), result);

        key = "en_US";
        keyDeserializer = StdKeyDeserializer.forType(Locale.class);
        result = keyDeserializer.deserializeKey(key, context);
        assertEquals(Locale.forLanguageTag("en_US"), result);

        key = "2023-03-01";
        Calendar calendar = new GregorianCalendar(2023, 2, 1);
        keyDeserializer = StdKeyDeserializer.forType(Calendar.class);
        result = keyDeserializer.deserializeKey(key, context);
        assertEquals(calendar.getTime(), result);

        key = "uuid:123e4567-e89b-12d3-a456-426614174000";
        UUID uuid = UUID.fromString(key);
        keyDeserializer = StdKeyDeserializer.forType(UUID.class);
        result = keyDeserializer.deserializeKey(key, context);
        assertEquals(uuid, result);

        key = "http://example.com";
        URI uri = new URI(key);
        keyDeserializer = StdKeyDeserializer.forType(URI.class);
        result = keyDeserializer.deserializeKey(key, context);
        assertEquals(uri, result);

        key = "http://example.com";
        URL url = new URL(key);
        keyDeserializer = StdKeyDeserializer.forType(URL.class);
        result = keyDeserializer.deserializeKey(key, context);
        assertEquals(url, result);

        key = "java.lang.String";
        Class<?> clazz = Class.forName(key);
        keyDeserializer = StdKeyDeserializer.forType(clazz);
        result = keyDeserializer.deserializeKey(key, context);
        assertEquals(clazz, result);

        // Test unknown key
        keyDeserializer = StdKeyDeserializer.forType(Integer.class);
        assertNull(keyDeserializer.deserializeKey("unknown", context));

        // Test enum key with enum class having a no-args constructor
        enum TestEnum {
            @JsonAutoDetect
            VALUE1,
            @JsonAutoDetect
            VALUE2,
            @JsonAutoDetect
            VALUE3
        }
        keyDeserializer = StdKeyDeserializer.forType(TestEnum.class);
        TestEnum testEnum = TestEnum.VALUE1;
        key = testEnum.name();
        result = keyDeserializer.deserializeKey(key, context);
        assertSame(testEnum, result);

        // Test enum key with enum class having an enum method
        @JsonSubTypes({
            @Type(name = "TestEnum1", value = TestEnum1.class),
            @Type(name = "TestEnum2", value = TestEnum2.class)
        })
        enum TestEnumWithEnumMethod {
            @JsonAutoDetect
            TEST_ENUM1,
            @JsonAutoDetect
            TEST_ENUM2;

            TestEnumWithEnumMethod() {
                // no-op
            }

            @JsonDeserialize(using = None.class)
            TestEnum getValue() {
                return this;
            }
        }
        keyDeserializer = StdKeyDeserializer.forType(TestEnumWithEnumMethod.class);
        TestEnumWithEnumMethod testEnumWithEnumMethod = TestEnumWithEnumMethod.TEST_ENUM1;
        key = testEnumWithEnumMethod.name();
        result = keyDeserializer.deserializeKey(key, context);
        assertSame(testEnumWithEnumMethod, result);
    }

    
}

// Replace TestEnum1 and TestEnum2 with your enum classes if needed