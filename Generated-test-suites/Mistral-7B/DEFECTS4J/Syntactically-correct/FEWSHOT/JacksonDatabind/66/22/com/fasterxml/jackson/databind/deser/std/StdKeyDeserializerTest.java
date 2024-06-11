package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.StaticSubType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.TestBase;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Test cases for {@link StdKeyDeserializer}.
 */
public class StdKeyDeserializerTest extends TestBase {

    @Test
    public void testForType() throws Exception {
        Class<?> raw = String.class;
        assertEquals(StdKeyDeserializer.class, StdKeyDeserializer.forType(raw));

        raw = UUID.class;
        assertEquals(StdKeyDeserializer.forType(UUID.class), new StdKeyDeserializer(StdKeyDeserializer.TYPE_UUID, UUID.class));

        raw = Integer.class;
        assertEquals(StdKeyDeserializer.forType(Integer.class), new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class));

        raw = Long.class;
        assertEquals(StdKeyDeserializer.forType(Long.class), new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class));

        raw = Date.class;
        assertEquals(StdKeyDeserializer.forType(Date.class), new StdKeyDeserializer(StdKeyDeserializer.TYPE_DATE, Date.class));

        raw = Calendar.class;
        assertEquals(StdKeyDeserializer.forType(Calendar.class), new StdKeyDeserializer(StdKeyDeserializer.TYPE_CALENDAR, Calendar.class));

        raw = Boolean.class;
        assertEquals(StdKeyDeserializer.forType(Boolean.class), new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class));

        raw = Byte.class;
        assertEquals(StdKeyDeserializer.forType(Byte.class), new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, Byte.class));

        raw = Character.class;
        assertEquals(StdKeyDeserializer.forType(Character.class), new StdKeyDeserializer(StdKeyDeserializer.TYPE_CHAR, Character.class));

        raw = Short.class;
        assertEquals(StdKeyDeserializer.forType(Short.class), new StdKeyDeserializer(StdKeyDeserializer.TYPE_SHORT, Short.class));

        raw = Float.class;
        assertEquals(StdKeyDeserializer.forType(Float.class), new StdKeyDeserializer(StdKeyDeserializer.TYPE_FLOAT, Float.class));

        raw = Double.class;
        assertEquals(StdKeyDeserializer.forType(Double.class), new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class));

        raw = URI.class;
        assertEquals(StdKeyDeserializer.forType(URI.class), new StdKeyDeserializer(StdKeyDeserializer.TYPE_URI, URI.class));

        raw = URL.class;
        assertEquals(StdKeyDeserializer.forType(URL.class), new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class));

        raw = Class.class;
        assertEquals(StdKeyDeserializer.forType(Class.class), new StdKeyDeserializer(StdKeyDeserializer.TYPE_CLASS, Class.class));

        raw = Locale.class;
        assertEquals(StdKeyDeserializer.forType(Locale.class), new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class, FromStringDeserializer.findDeserializer(Locale.class)));

        raw = Currency.class;
        assertEquals(StdKeyDeserializer.forType(Currency.class), new StdKeyDeserializer(StdKeyDeserializer.TYPE_CURRENCY, Currency.class, FromStringDeserializer.findDeserializer(Currency.class)));

        raw = CustomEnum.class;
        assertNull(StdKeyDeserializer.forType(CustomEnum.class));
    }

    @Test
    public void testDeserializeKey() throws IOException {
        StringKD strKD = StdKeyDeserializer.forType(String.class);
        assertEquals("foo", strKD.deserializeKey("foo", null));
        assertNull(strKD.deserializeKey(null, null));

        URIKeyDeserializer uriKD = StdKeyDeserializer.forType(URI.class);
        URI uri = uriKD.deserializeKey("http://example.com", null);
        assertEquals("http://example.com", uri.toASCIIString());
        assertNull(uriKD.deserializeKey(null, null));

        ClassKeyDeserializer classKD = StdKeyDeserializer.forType(String.class);
        assertEquals(String.class, classKD.deserializeKey("java.lang.String", null));
        assertNull(classKD.deserializeKey(null, null));

        EnumKD enumKD = StdKeyDeserializer.forType(CustomEnum.class);
        CustomEnum enumValue = (CustomEnum) enumKD.deserializeKey("RED", null);
        assertEquals(CustomEnum.RED, enumValue);

        BooleanKD booleanKD = StdKeyDeserializer.forType(Boolean.class);
        assertTrue(booleanKD.deserializeKey("true", null));
        assertFalse(booleanKD.deserializeKey("false", null));
        assertNull(booleanKD.deserializeKey(null, null));

        ByteKD byteKD = StdKeyDeserializer.forType(Byte.class);
        assertEquals((byte) 0, byteKD.deserializeKey("0", null));
        assertEquals((byte) 127, byteKD.deserializeKey("127", null));
        assertNull(byteKD.deserializeKey(null, null));

        ShortKD shortKD = StdKeyDeserializer.forType(Short.class);
        assertEquals((short) 0, shortKD.deserializeKey("0", null));
        assertEquals((short) 127, shortKD.deserializeKey("127", null));
        assertNull(shortKD.deserializeKey(null, null));

        CharKD charKD = StdKeyDeserializer.forType(Character.class);
        assertEquals((char) 'A', charKD.deserializeKey("A", null));
        assertEquals((char) 'Z', charKD.deserializeKey("Z", null));
        assertNull(charKD.deserializeKey(null, null));

        IntKD intKD = StdKeyDeserializer.forType(Integer.class);
        assertEquals(0, intKD.deserializeKey("0", null));
        assertEquals(123, intKD.deserializeKey("123", null));
        assertNull(intKD.deserializeKey(null, null));

        LongKD longKD = StdKeyDeserializer.forType(Long.class);
        assertEquals(0L, longKD.deserializeKey("0", null));
        assertEquals(1234567890123456789L, longKD.deserializeKey("1234567890123456789", null));
        assertNull(longKD.deserializeKey(null, null));

        FloatKD floatKD = StdKeyDeserializer.forType(Float.class);
        assertEquals(0.0f, floatKD.deserializeKey("0", null));
        assertEquals(123.456f, floatKD.deserializeKey("123.456", null));
        assertNull(floatKD.deserializeKey(null, null));

        DoubleKD doubleKD = StdKeyDeserializer.forType(Double.class);
        assertEquals(0.0, doubleKD.deserializeKey("0", null));
        assertEquals(123.456, doubleKD.deserializeKey("123.456", null));
        assertNull(doubleKD.deserializeKey(null, null));

        DateKD dateKD = StdKeyDeserializer.forType(Date.class);
        Date date = dateKD.deserializeKey("2023-03-01", null);
        assertEquals("Thu Mar 01 00:00:00 EST 2023", date.toString());
        assertNull(dateKD.deserializeKey(null, null));

        CalendarKD calendarKD = StdKeyDeserializer.forType(Calendar.class);
        Calendar calendar = calendarKD.deserializeKey("2023-03-01", null);
        assertEquals(2023, calendar.get(Calendar.YEAR));
        assertNull(calendarKD.deserializeKey(null, null));

        UUIDKD uuidKD = StdKeyDeserializer.forType(UUID.class);
        UUID uuid = uuidKD.deserializeKey("a1b2c3d4-e5f6-g7h8-i9j0-k1l2m3n4o5p6", null);
        assertEquals("a1b2c3d4-e5f6-g7h8-i9j0-k1l2m3n4o5p6", uuid.toString());
        assertNull(uuidKD.deserializeKey(null, null));

        URLKD urlKD = StdKeyDeserializer.forType(URL.class);
        URL url = urlKD.deserializeKey("http://example.com", null);
        assertEquals("http://example.com", url.toString());
        assertNull(urlKD.deserializeKey(null, null));

        ClassKeyDeserializer classKD2 = StdKeyDeserializer.forType(String.class);
        Class<?> cls = Class.forName("java.lang.String");
        assertEquals(cls, classKD2.deserializeKey(cls.getName(), null));
        assertNull(classKD2.deserializeKey(null, null));

        EnumResolver enumResolver = FromStringDeserializer.findDeserializer(CustomEnum.class);
        EnumKD enumKD2 = new StdKeyDeserializer(StdKeyDeserializer.TYPE_ENUM, CustomEnum.class, enumResolver);
        CustomEnum customEnum = (CustomEnum) enumKD2.deserializeKey("RED", null);
        assertEquals(CustomEnum.RED, customEnum);
        assertNull(enumKD2.deserializeKey(null, null));
    }

    @Test
    public void testGetKeyClass() throws Exception {
        Class<?> raw = String.class;
        assertEquals(String.class, StdKeyDeserializer.forType(raw).getKeyClass());

        raw = UUID.class;
        assertEquals(UUID.class, StdKeyDeserializer.forType(raw).getKeyClass());

        raw = Integer.class;
        assertEquals(Integer.class, StdKeyDeserializer.forType(raw).getKeyClass());

        raw = Long.class;
        assertEquals(Long.class, StdKeyDeserializer.forType(raw).getKeyClass());

        raw = Date.class;
        assertEquals(Date.class, StdKeyDeserializer.forType(raw).getKeyClass());

        raw = Calendar.class;
        assertEquals(Calendar.class, StdKeyDeserializer.forType(raw).getKeyClass());

        raw = Boolean.class;
        assertEquals(Boolean.class, StdKeyDeserializer.forType(raw).getKeyClass());

        raw = Byte.class;
        assertEquals(Byte.class, StdKeyDeserializer.forType(raw).getKeyClass());

        raw = Character.class;
        assertEquals(Character.class, StdKeyDeserializer.forType(raw).getKeyClass());

        raw = Short.class;
        assertEquals(Short.class, StdKeyDeserializer.forType(raw).getKeyClass());

        raw = Float.class;
        assertEquals(Float.class, StdKeyDeserializer.forType(raw).getKeyClass());

        raw = Double.class;
        assertEquals(Double.class, StdKeyDeserializer.forType(raw).getKeyClass());

        raw = URI.class;
        assertEquals(URI.class, StdKeyDeserializer.forType(raw).getKeyClass());

        raw = URL.class;
        assertEquals(URL.class, StdKeyDeserializer.forType(raw).getKeyClass());

        raw = Class.class;
        assertEquals(Class.class, StdKeyDeserializer.forType(raw).getKeyClass());

        raw = Locale.class;
        assertEquals(Locale.class, StdKeyDeserializer.forType(raw).getKeyClass());

        raw = Currency.class;
        assertEquals(Currency.class, StdKeyDeserializer.forType(raw).getKeyClass());

        raw = CustomEnum.class;
        assertNull(StdKeyDeserializer.forType(raw).getKeyClass());
    }

    private static class CustomEnum {
        @JsonSubTypes({
            @JsonSubTypes.StaticSubType(value = CustomEnum.RED.getClass(), name = "RED"),
            @JsonSubTypes.StaticSubType(value = CustomEnum.GREEN.getClass(), name = "GREEN")
        })
        public enum Color {
            RED,
            GREEN
        }

        public static final Color RED = Color.RED;
        public static final Color GREEN = Color.GREEN;
    }
}