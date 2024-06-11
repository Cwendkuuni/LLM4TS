import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.Locale;
import java.util.Set;

public class StdKeyDeserializerTest {

    @JacksonStdImpl
    static class TestKeyDeserializer extends StdKeyDeserializer {
        public TestKeyDeserializer(int kind, Class<?> cls) {
            super(kind, cls);
        }
    }

    @org.junit.Test
    public void testDeserializeKey() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
        Object key = deserializer.deserializeKey("test", null);
        assertEquals("test", key);
    }

    @org.junit.Test(expected = IOException.class)
    public void testDeserializeKeyNull() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
        deserializer.deserializeKey(null, null);
    }

    @org.junit.Test(expected = IOException.class)
    public void testDeserializeKeyInvalidType() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(int.class);
        deserializer.deserializeKey("test", null);
    }

    @org.junit.Test
    public void testParseBoolean() throws Exception {
        StdKeyDeserializer deserializer = new TestKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        assertTrue(deserializer._parse("true", null));
        assertFalse(deserializer._parse("false", null));
        assertNull(deserializer._parse("invalid", null));
    }

    @org.junit.Test(expected = IOException.class)
    public void testParseByteOutOfRange() throws Exception {
        StdKeyDeserializer deserializer = new TestKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, Byte.class);
        deserializer._parse("-128", null);
        deserializer._parse("256", null);
    }

    @org.junit.Test
    public void testParseByte() throws Exception {
        StdKeyDeserializer deserializer = new TestKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, Byte.class);
        assertEquals((byte) 1, deserializer._parse("1", null));
        assertEquals((byte) 127, deserializer._parse("127", null));
        assertNull(deserializer._parse("invalid", null));
    }

    @org.junit.Test
    public void testParseShort() throws Exception {
        StdKeyDeserializer deserializer = new TestKeyDeserializer(StdKeyDeserializer.TYPE_SHORT, Short.class);
        assertEquals((short) 1, deserializer._parse("1", null));
        assertEquals((short) Short.MAX_VALUE, deserializer._parse("32767", null));
        assertNull(deserializer._parse("invalid", null));
    }

    @org.junit.Test(expected = IOException.class)
    public void testParseShortOutOfRange() throws Exception {
        StdKeyDeserializer deserializer = new TestKeyDeserializer(StdKeyDeserializer.TYPE_SHORT, Short.class);
        deserializer._parse("-32768", null);
        deserializer._parse("32768", null);
    }

    @org.junit.Test
    public void testParseChar() throws Exception {
        StdKeyDeserializer deserializer = new TestKeyDeserializer(StdKeyDeserializer.TYPE_CHAR, Character.class);
        assertEquals((char) 'a', deserializer._parse("a", null));
        assertEquals((char) '\u00FF', deserializer._parse("\u00FF", null));
        assertNull(deserializer._parse("invalid", null));
    }

    @org.junit.Test
    public void testParseInt() throws Exception {
        StdKeyDeserializer deserializer = new TestKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        assertEquals(1, deserializer._parseInt("1"));
        assertEquals(Integer.MAX_VALUE, deserializer._parseInt("2147483647"));
        assertNull(deserializer._parse("invalid", null));
    }

    @org.junit.Test(expected = IOException.class)
    public void testParseLongOutOfRange() throws Exception {
        StdKeyDeserializer deserializer = new TestKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);
        deserializer._parseLong("-9223372036854775808", null);
        deserializer._parseLong("9223372036854775807", null);
    }

    @org.junit.Test
    public void testParseLong() throws Exception {
        StdKeyDeserializer deserializer = new TestKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);
        assertEquals(1L, deserializer._parseLong("1"));
        assertEquals(Long.MAX_VALUE, deserializer._parseLong("9223372036854775807"));
        assertNull(deserializer._parse("invalid", null));
    }

    @org.junit.Test
    public void testParseFloat() throws Exception {
        StdKeyDeserializer deserializer = new TestKeyDeserializer(StdKeyDeserializer.TYPE_FLOAT, Float.class);
        assertEquals(1.0f, deserializer._parseFloat("1.0"), 0.01f);
        assertEquals(Float.POSITIVE_INFINITY, deserializer._parseFloat("1.79769313486231E38"));
        assertNull(deserializer._parse("invalid", null));
    }

    @org.junit.Test
    public void testParseDouble() throws Exception {
        StdKeyDeserializer deserializer = new TestKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class);
        assertEquals(1.0, deserializer._parseDouble("1.0"), 0.001);
        assertEquals(Double.POSITIVE_INFINITY, deserializer._parseDouble("1.79769313486231E308"));
        assertNull(deserializer._parse("invalid", null));
    }

    @org.junit.Test
    public void testParseLocale() throws Exception {
        StdKeyDeserializer deserializer = new TestKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class);
        Locale locale = deserializer._parse("en_US", null);
        assertEquals("en_US", locale.toString());

        try {
            deserializer._parse("invalid", null);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testParseDate() throws Exception {
        StdKeyDeserializer deserializer = new TestKeyDeserializer(StdKeyDeserializer.TYPE_DATE, java.util.Date.class);
        java.util.Date date = deserializer._parse("2023-03-21", null);
        assertEquals("Tue Mar 21 00:00:00 CET 2023", date.toString());

        try {
            deserializer._parse("invalid", null);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testParseCalendar() throws Exception {
        StdKeyDeserializer deserializer = new TestKeyDeserializer(StdKeyDeserializer.TYPE_CALENDAR, java.util.Calendar.class);
        java.util.Calendar calendar = deserializer._parse("2023-03-21", java.util.TimeZone.getTimeZone("CET"));
        assertEquals(2023, calendar.get(Calendar.YEAR));
        assertEquals(3, calendar.get(Calendar.MONTH));
        assertEquals(21, calendar.get(Calendar.DAY_OF_MONTH));

        try {
            deserializer._parse("invalid", null);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testParseUUID() throws Exception {
        StdKeyDeserializer deserializer = new TestKeyDeserializer(StdKeyDeserializer.TYPE_UUID, UUID.class);
        UUID uuid = deserializer._parse("00000000-0000-0000-0000-000000000000", null);
        assertEquals("00000000-0000-0000-0000-000000000000", uuid.toString());

        try {
            deserializer._parse("invalid", null);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testParseURI() throws Exception {
        StdKeyDeserializer deserializer = new TestKeyDeserializer(StdKeyDeserializer.TYPE_URI, URI.class);
        URI uri = deserializer._parse("http://test.com", null);
        assertEquals("http", uri.getScheme());
        assertEquals("/test.com", uri.getPath());

        try {
            deserializer._parse("invalid", null);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testParseURL() throws Exception {
        StdKeyDeserializer deserializer = new TestKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class);
        URL url = deserializer._parse("http://test.com", null);
        assertEquals("http", url.getProtocol());
        assertEquals("/test.com", url.getFile());

        try {
            deserializer._parse("invalid", null);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testParseClass() throws Exception {
        StdKeyDeserializer deserializer = new TestKeyDeserializer(StdKeyDeserializer.TYPE_CLASS, String[].class);
        Class<?> clazz = deserializer.deserializeKey("java.lang.String[]", null);
        assertEquals(String[].class, clazz);

        try {
            deserializer.deserializeKey(null, null);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testParseEnumByName() throws Exception {
        EnumResolver resolver = new EnumResolver(LocaleEnum.class);
        StdKeyDeserializer deserializer = new TestKeyDeserializer(StdKeyDeserializer.TYPE_ENUM, LocaleEnum.class);
        assertEquals(LocaleEnum.CHINESE, deserializer._parse("CHINESE", null));

        try {
            deserializer._parse("invalid", null);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }

        EnumResolver resolverToString = new EnumResolver(LocaleEnum.class, true);
        StdKeyDeserializer deserializerToString = new TestKeyDeserializer(StdKeyDeserializer.TYPE_ENUM, LocaleEnum.class);
        assertEquals(LocaleEnum.CHINESE, deserializerToString._parse("zh_CN", null));
    }

    @org.junit.Test
    public void testParseEnumByToString() throws Exception {
        EnumResolver resolverToString = new EnumResolver(LocaleEnum.class, true);
        StdKeyDeserializer deserializerToString = new TestKeyDeserializer(StdKeyDeserializer.TYPE_ENUM, LocaleEnum.class);
        assertEquals(LocaleEnum.CHINESE, deserializerToString._parse("zh_CN", null));

        try {
            deserializerToString._parse("invalid", null);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }

        EnumResolver resolver = new EnumResolver(LocaleEnum.class);
        StdKeyDeserializer deserializer = new TestKeyDeserializer(StdKeyDeserializer.TYPE_ENUM, LocaleEnum.class);
        try {
            deserializer.deserializeKey("invalid", null);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @org.junit.Test(expected = IOException.class)
    public void testParseByteArrayInvalid() throws Exception {
        StdKeyDeserializer deserializer = new TestKeyDeserializer(StdKeyDeserializer.TYPE_BYTE_ARRAY, byte[].class);
        deserializer._parse("invalid", null);
    }

    @org.junit.Test
    public void testParseByteArray() throws Exception {
        StdKeyDeserializer deserializer = new TestKeyDeserializer(StdKeyDeserializer.TYPE_BYTE_ARRAY, byte[].class);
        byte[] byteArray = deserializer._parse("Rm9udCAoMCI=".getBytes(), null);
        assertEquals("Jackson Test".getBytes(), byteArray);
    }
}