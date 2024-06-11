
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import com.fasterxml.jackson.databind.serializer.DeserializationTestBase;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBaseHelper;

import java.io.IOException;
import java.lang.reflect.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class StdKeyDeserializerTest extends DeserializationTestBase {

    // Base case tests for deserializeKey method

    @Test
    public void testBasicDeserializeKey() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);
        Object result = keyDeserializer.deserializeKey("test", context());
        assertEquals("test", result);
    }

    @Test
    public void testDeserializeKeyForBoolean() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
        Object result = keyDeserializer.deserializeKey("true", context());
        assertTrue((Boolean) result);

        result = keyDeserializer.deserializeKey("false", context());
        assertFalse((Boolean) result);
    }

    @Test
    public void testDeserializeKeyForByte() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Byte.class);

        Object result = keyDeserializer.deserializeKey("1", context());
        assertEquals((Byte) 1, result);

        result = keyDeserializer.deserializeKey("100", context());
        assertEquals((Byte) 100, result);

        result = keyDeserializer.deserializeKey("-127", context());
        assertEquals((Byte) -127, result);

        result = keyDeserializer.deserializeKey("128", context());
        assertNull(result);
    }

    @Test
    public void testDeserializeKeyForShort() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Short.class);

        Object result = keyDeserializer.deserializeKey("1", context());
        assertEquals((Short) 1, result);

        result = keyDeserializer.deserializeKey("32767", context());
        assertEquals((Short) 32767, result);

        result = keyDeserializer.deserializeKey("-32768", context());
        assertEquals((Short) -32768, result);

        result = keyDeserializer.deserializeKey("32768", context());
        assertNull(result);
    }

    @Test
    public void testDeserializeKeyForCharacter() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Character.class);

        Object result = keyDeserializer.deserializeKey("A", context());
        assertEquals((Character) 'A', result);

        result = keyDeserializer.deserializeKey("\u00E9", context());
        assertEquals((Character) '\u00E9', result);
    }

    @Test
    public void testDeserializeKeyForInt() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Integer.class);

        Object result = keyDeserializer.deserializeKey("1", context());
        assertEquals((Integer) 1, result);

        result = keyDeserializer.deserializeKey("-1234567890", context());
        assertEquals((Integer) -1234567890, result);

        result = keyDeserializer.deserializeKey("0x1f", context());
        assertEquals((Integer) 0x1f, result);

        result = keyDeserializer.deserializeKey("0x1f000000", context());
        assertEquals((Integer) 0x1f000000, result);

        result = keyDeserializer.deserializeKey("0xFFFF_FFFE", context());
        assertEquals((Integer) 0xFFFF_FFFE, result);

        result = keyDeserializer.deserializeKey("0x7FFFFFFF", context());
        assertNull(result);
    }

    @Test
    public void testDeserializeKeyForLong() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Long.class);

        Object result = keyDeserializer.deserializeKey("1", context());
        assertEquals((Long) 1, result);

        result = keyDeserializer.deserializeKey("1234567890123456789", context());
        assertEquals((Long) 1234567890123456789L, result);

        result = keyDeserializer.deserializeKey("0x1f", context());
        assertEquals((Long) 0x1f, result);

        result = keyDeserializer.deserializeKey("0x1f00000000000000", context());
        assertEquals((Long) 0x1f00000000000000L, result);

        result = keyDeserializer.deserializeKey("0xFFFF_FFFE_FFFF_FFFF", context());
        assertEquals((Long) 0xFFFF_FFFE_FFFF_FFFFL, result);

        result = keyDeserializer.deserializeKey("0x7FFFFFFFL", context());
        assertNull(result);
    }

    @Test
    public void testDeserializeKeyForFloat() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Float.class);

        Object result = keyDeserializer.deserializeKey("1.0", context());
        assertEquals((Float) 1.0f, result);

        result = keyDeserializer.deserializeKey("1.0E2", context());
        assertEquals((Float) 100.0f, result);

        result = keyDeserializer.deserializeKey("1.123e-5", context());
        assertEquals((Float) 0.00001123f, result, 0.000001f);

        result = keyDeserializer.deserializeKey("NaN", context());
        assertNotNull(result);
        assertEquals(Float.NaN, result);

        result = keyDeserializer.deserializeKey("Inf", context());
        assertNotNull(result);
        assertEquals(Float.POSITIVE_INFINITY, result);

        result = keyDeserializer.deserializeKey("-Inf", context());
        assertNotNull(result);
        assertEquals(Float.NEGATIVE_INFINITY, result);
    }

    @Test
    public void testDeserializeKeyForDouble() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Double.class);

        Object result = keyDeserializer.deserializeKey("1.0", context());
        assertEquals((Double) 1.0, result);

        result = keyDeserializer.deserializeKey("1.0E2", context());
        assertEquals((Double) 100.0, result);

        result = keyDeserializer.deserializeKey("1.123e-5", context());
        assertEquals((Double) 0.00001123, result, 0.0000001);

        result = keyDeserializer.deserializeKey("NaN", context());
        assertNotNull(result);
        assertEquals(Double.NaN, result);

        result = keyDeserializer.deserializeKey("Inf", context());
        assertNotNull(result);
        assertEquals(Double.POSITIVE_INFINITY, result);

        result = keyDeserializer.deserializeKey("-Inf", context());
        assertNotNull(result);
        assertEquals(Double.NEGATIVE_INFINITY, result);
    }

    @Test
    public void testDeserializeKeyForLocale() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Locale.class);

        Object result = keyDeserializer.deserializeKey("en_US", context());
        assertEquals(Locale.US, result);

        result = keyDeserializer.deserializeKey("en_GB", context());
        assertEquals(Locale.ENGLISH, result);

        result = keyDeserializer.deserializeKey("es_ES", context());
        assertEquals(Locale.SPAIN, result);

        result = keyDeserializer.deserializeKey("fr_FR", context());
        assertEquals(Locale.FRANCE, result);
    }

    @Test
    public void testDeserializeKeyForDate() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Date.class);

        Object result = keyDeserializer.deserializeKey("2023-03-01", context());
        assertEquals(new Date(2023 - 1900, 2 - 1, 1), result);

        result = keyDeserializer.deserializeKey("2023-03-01T12:30:45.123Z", context());
        assertEquals(new Date(2023 - 1900, 2 - 1, 1, 12, 30, 45, 123), result);
    }

    @Test
    public void testDeserializeKeyForCalendar() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Calendar.class);

        Object result = keyDeserializer.deserializeKey("2023-03-01", context());
        GregorianCalendar calendar = new GregorianCalendar(2023, 2 - 1, 1);
        assertEquals(calendar, result);
    }

    @Test
    public void testDeserializeKeyForURI() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URI.class);

        Object result = keyDeserializer.deserializeKey("http://example.com/test", context());
        URI expectedUri = new URI("http", "//example.com/test");
        assertEquals(expectedUri, result);
    }

    @Test
    public void testDeserializeKeyForURL() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);

        Object result = keyDeserializer.deserializeKey("http://example.com/test", context());
        URL expectedUrl = new URL("http", "//example.com/test");
        assertEquals(expectedUrl, result);
    }

    @Test
    public void testDeserializeKeyForEnum() throws Exception {
        Enum enumType = MyEnum.class;
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(enumType);

        Object result = keyDeserializer.deserializeKey("VALUE_1", context());
        assertEquals(MyEnum.VALUE_1, result);

        result = keyDeserializer.deserializeKey("VALUE_2", context());
        assertEquals(MyEnum.VALUE_2, result);
    }

    @Test
    public void testDeserializeKeyForClass() throws Exception {
        Object result = keyDeserializer.deserializeKey("java.lang.String", context());
        assertEquals(String.class, result);
    }

    @Test
    public void testDeserializeKeyForByteArray() throws Exception {
        Object result = keyDeserializer.deserializeKey("dGVzdA==", context());
        byte[] expected = {68, 101, 118, 101, 114, 121, 65, 112, 112, 114, 105, 103, 68};
        assertArrayEquals(expected, result);
    }

    // Edge cases tests

    @Test
    public void testDeserializeKeyNull() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);

        Object result = keyDeserializer.deserializeKey(null, context());
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeKeyInvalidType() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Object.class);
        keyDeserializer.deserializeKey("test", context());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeKeyInvalidKey() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Short.class);

        try {
            keyDeserializer.deserializeKey("test", context());
        } catch (IOException e) {
            throw new AssertionError(e);
        }

        Annotation[] annotations = keyDeserializer.getClass().getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().isAssignableFrom(DeserializeWithAnnotation.class)) {
                DeserializeWithAnnotation deserializeAnnotation = (DeserializeWithAnnotation) annotation;
                assertNull(deserializeAnnotation.value());
                break;
            }
        }
    }
}
