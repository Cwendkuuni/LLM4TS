package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.TestBase;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertInstanceOf;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertThrows;

public class StdKeyDeserializerTest extends TestBase {

    private static final Class<?> KNOWN_KEY_TYPES[] = {
            String.class, Object.class,
            UUID.class, Integer.class, Long.class, Date.class, Calendar.class,
            Boolean.class, Byte.class, Character.class, Short.class, Float.class, Double.class,
            URI.class, URL.class, Class.class, Locale.class, Currency.class
    };

    @Test
    public void testForType() {
        for (Class<?> keyType : KNOWN_KEY_TYPES) {
            KeyDeserializer deser = StdKeyDeserializer.forType(keyType);
            assertEquals(deser.getClass(), StdKeyDeserializer.class);
            assertEquals(deser.getKeyClass(), keyType);
        }
        assertNull(StdKeyDeserializer.forType(String[].class));
    }

    @Test
    public void testBooleanKeyDeserializer() throws IOException {
        Object deserializedKey = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class).deserializeKey("true", context);
        assertEquals(deserializedKey, Boolean.TRUE);

        deserializedKey = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class).deserializeKey("false", context);
        assertEquals(deserializedKey, Boolean.FALSE);

        deserializedKey = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class).deserializeKey("false5", context);
        assertInstanceOf(IllegalArgumentException.class, deserializedKey);
    }

    @Test
    public void testByteKeyDeserializer() throws IOException {
        Object deserializedKey = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, Byte.class).deserializeKey("127", context);
        assertEquals(deserializedKey, Byte.valueOf((byte) 127));

        deserializedKey = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, Byte.class).deserializeKey("128", context);
        assertInstanceOf(IllegalArgumentException.class, deserializedKey);
    }

    @Test
    public void testShortKeyDeserializer() throws IOException {
        Object deserializedKey = new StdKeyDeserializer(StdKeyDeserializer.TYPE_SHORT, Short.class).deserializeKey("32767", context);
        assertEquals(deserializedKey, Short.valueOf((short) 32767));

        deserializedKey = new StdKeyDeserializer(StdKeyDeserializer.TYPE_SHORT, Short.class).deserializeKey("32768", context);
        assertInstanceOf(IllegalArgumentException.class, deserializedKey);
    }

    @Test
    public void testCharKeyDeserializer() throws IOException {
        Object deserializedKey = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CHAR, Character.class).deserializeKey("'A'", context);
        assertEquals(deserializedKey, Character.valueOf('A'));

        deserializedKey = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CHAR, Character.class).deserializeKey("'B'", context);
        assertEquals(deserializedKey, Character.valueOf('B'));

        deserializedKey = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CHAR, Character.class).deserializeKey("'C'", context);
        assertEquals(deserializedKey, Character.valueOf('C'));

        deserializedKey = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CHAR, Character.class).deserializeKey("'D'", context);
        assertInstanceOf(IllegalArgumentException.class, deserializedKey);
    }

    @Test
    public void testIntKeyDeserializer() throws IOException {
        Object deserializedKey = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class).deserializeKey("42", context);
        assertEquals(deserializedKey, Integer.valueOf(42));
    }

    @Test
    public void testLongKeyDeserializer() throws IOException {
        Object deserializedKey = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class).deserializeKey("123456789012345", context);
        assertEquals(deserializedKey, Long.valueOf(123456789012345L));
    }

    @Test
    public void testFloatKeyDeserializer() throws IOException {
        Object deserializedKey = new StdKeyDeserializer(StdKeyDeserializer.TYPE_FLOAT, Float.class).deserializeKey("3.14159265358979323846", context);
        assertEquals(deserializedKey, Float.valueOf((float) 3.14159265358979323846f));
    }

    @Test
    public void testDoubleKeyDeserializer() throws IOException {
        Object deserializedKey = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class).deserializeKey("3.14159265358979323846", context);
        assertEquals(deserializedKey, Double.valueOf(3.14159265358979323846));
    }

    @Test
    public void testLocaleKeyDeserializer() throws IOException {
        Object deserializedKey = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class)
                .deserializeKey("en_US", context);
        assertEquals(deserializedKey, Locale.US);
    }

    @Test
    public void testCalendarKeyDeserializer() throws IOException {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        Object deserializedKey = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CALENDAR, Calendar.class)
                .deserializeKey("Mon Jul 19 15:19:21 EDT 2021", context);
        assertEquals(deserializedKey, calendar);
    }

    @Test
    public void testUriKeyDeserializer() throws IOException {
        Object deserializedKey = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URI, URI.class).deserializeKey("https://example.com/path?query=value#fragment", context);
        assertEquals(deserializedKey, new URI("https", "example.com", "/path", null, "query=value", null, null));
    }

    @Test
    public void testUrlKeyDeserializer() throws IOException {
        Object deserializedKey = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class).deserializeKey("https://example.com/path?query=value#fragment", context);
        assertEquals(deserializedKey, new URL("https", "example.com", "/path", null, null, null, null));
    }

    @Test
    public void testEnumKeyDeserializer() throws IOException {
        EnumSet<MyEnum> enumSet = EnumSet.allOf(MyEnum.class);
        Object deserializedKey = new StdKeyDeserializer(StdKeyDeserializer.TYPE_ENUM, MyEnum.class)
                .deserializeKey("FIRST", context);
        assertEquals(deserializedKey, enumSet.stream().filter(e -> e.getName().equals("FIRST")).findFirst().orElse(null));
    }

    @Test
    public void testClassKeyDeserializer() throws IOException {
        Object deserializedKey = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CLASS, String[].class)
                .deserializeKey("java.lang.String[2]", context);
        assertEquals(deserializedKey, new Object[2].getClass());
    }

    @Test
    public void testNullKeyDeserializer() throws IOException {
        Object deserializedKey = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class)
                .deserializeKey(null, context);
        assertNull(deserializedKey);
    }

    @Test
    public void testUnknownKeyDeserializer() throws IOException {
        assertThrows(IOException.class, () -> new StdKeyDeserializer(123, String.class)
                .deserializeKey("test", context));
    }

    @Test
    public void testInvalidKeyDeserializer() throws IOException {
        assertThrows(IllegalArgumentException.class, () -> new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class)
                .deserializeKey("foo", context));
    }

    @Test
    public void testInvalidKeyWithToStringDeserializer() throws IOException {
        Class<?> invalidClass = new Object() {}.getClass();
        Object deserializedKey = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CLASS, InvalidClass.class)
                .deserializeKey("InvalidClass", context);
        assertInstanceOf(IllegalArgumentException.class, deserializedKey);

        class InvalidClass {
            public String toString() {
                return "InvalidClass";
            }
        }
    }

    @Test
    public void testDefaultKeyDeserializer() throws IOException {
        Object deserializedKey = new StdKeyDeserializer(-1, String.class).deserializeKey("test", context);
        assertEquals(deserializedKey, "test");
    }

    @Test
    public void testStringKeyDeserializer() throws IOException {
        Object deserializedKey = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class)
                .deserializeKey("test", context);
        assertEquals(deserializedKey, "test");
    }

    @Test
    public void testStringListKeyDeserializerWithToStringSerializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new SimpleModule()
                .setSerializerFactory(new BeanSerializerFactory(mapper.getTypeFactory()) {
                    @Override
                    public JsonSerializer<?> findSerializer(Class<?> type) {
                        if (type.isAssignableFrom(List.class)) {
                            return new ToStringSerializer(type);
                        }
                        return super.findSerializer(type);
                    }
                })
                .addSerializer(String[].class, new ToStringSerializer(String[].class)));

        Object deserializedKey = mapper.readValue("{\"key\":[\"foo\",\"bar\"]}", Map.class).get("key");
        assertEquals(deserializedKey, Arrays.asList("foo", "bar"));
    }
}