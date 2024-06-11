package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.StdKeyDeserializerTestBase;

/**
 * Test cases for {@link StdKeyDeserializer} class.
 */
@JacksonStdImpl
public class StdKeyDeserializerTest extends StdKeyDeserializerTestBase {

    @Test
    public void testBooleanDeserializer() {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Boolean.class);
        assertEquals(Boolean.TRUE, serializer.deserializeKey("true", context));
        assertEquals(Boolean.FALSE, serializer.deserializeKey("false", context));
    }

    @Test
    public void testByteDeserializer() {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Byte.class);
        assertEquals((Byte) Byte.parseByte("5"), serializer.deserializeKey("5", context));
        try {
            serializer.deserializeKey("256", context);
            fail();
        } catch (JsonMappingException expected) {
            // expected
        }
    }

    @Test
    public void testShortDeserializer() {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Short.class);
        assertEquals((Short) Short.parseShort("127"), serializer.deserializeKey("127", context));
        try {
            serializer.deserializeKey("32768", context);
            fail();
        } catch (JsonMappingException expected) {
            // expected
        }
    }

    @Test
    public void testCharDeserializer() {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Character.class);
        assertEquals(Character.valueOf('A'), serializer.deserializeKey("A", context));
        assertEquals(Character.valueOf('\\u00E9'), serializer.deserializeKey("\\u00E9", context));

        try {
            serializer.deserializeKey("", context);
            fail();
        } catch (JsonMappingException expected) {
            // expected
        }
    }

    @Test
    public void testIntDeserializer() {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Integer.class);
        assertEquals(Integer.parseInt("123"), serializer.deserializeKey("123", context));

        try {
            serializer.deserializeKey("2147483648", context);
            fail();
        } catch (JsonMappingException expected) {
            // expected
        }
    }

    @Test
    public void testLongDeserializer() {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Long.class);
        assertEquals(Long.parseLong("12345678901234567890"), serializer.deserializeKey("12345678901234567890", context));

        try {
            serializer.deserializeKey("9223372036854775808", context);
            fail();
        } catch (JsonMappingException expected) {
            // expected
        }
    }

    @Test
    public void testFloatDeserializer() {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Float.class);
        assertEquals(Float.parseFloat("123.45"), serializer.deserializeKey("123.45", context));

        try {
            serializer.deserializeKey("NaN", context);
            fail();
        } catch (JsonMappingException expected) {
            // expected
        }
    }

    @Test
    public void testDoubleDeserializer() {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Double.class);
        assertEquals(Double.parseDouble("123.45"), serializer.deserializeKey("123.45", context));

        try {
            serializer.deserializeKey("Inf", context);
            fail();
        } catch (JsonMappingException expected) {
            // expected
        }
    }

    @Test
    public void testLocaleDeserializer() {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Locale.class);
        Locale expected = new Locale("fr", "FR");
        assertEquals(expected, serializer.deserializeKey("fr_FR", context));

        try {
            serializer.deserializeKey("en_GB_London", context);
            fail();
        } catch (JsonMappingException expected) {
            // expected
        }
    }

    @Test
    public void testDateDeserializer() {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Date.class);
        Date expected = new Date(1234567890123L);
        assertEquals(expected, serializer.deserializeKey("2015-01-26T12:34:56.789Z", context));

        try {
            serializer.deserializeKey("2015-13-31T12:34:56.789Z", context);
            fail();
        } catch (JsonMappingException expected) {
            // expected
        }
    }

    @Test
    public void testCalendarDeserializer() {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Calendar.class);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2015);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 26);
        assertEquals(calendar, serializer.deserializeKey("2015-01-26", context));

        try {
            serializer.deserializeKey("2015-13-31", context);
            fail();
        } catch (JsonMappingException expected) {
            // expected
        }
    }

    @Test
    public void testUUIDDeserializer() {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(UUID.class);
        UUID expected = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        assertEquals(expected, serializer.deserializeKey("123e4567-e89b-12d3-a456-426614174000", context));

        try {
            serializer.deserializeKey("123e4567-e89b-12d3-a456-426614174001", context);
            fail();
        } catch (JsonMappingException expected) {
            // expected
        }
    }

    @Test
    public void testURIDeserializer() {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URI.class);
        URI expected = new URI("http://example.com/a/b/c/d?p=q#f");
        assertEquals(expected, serializer.deserializeKey("http://example.com/a/b/c/d?p=q#f", context));

        try {
            serializer.deserializeKey("http://example.com:80/a/b/c/d?p=q#f:8080", context);
            fail();
        } catch (JsonMappingException expected) {
            // expected
        }
    }

    @Test
    public void testURLDeserializer() {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URL.class);
        URL expected = new URL("http://example.com/a/b/c/d?p=q#f");
        assertEquals(expected, serializer.deserializeKey("http://example.com/a/b/c/d?p=q#f", context));

        try {
            serializer.deserializeKey("http://example.com:80/a/b/c/d?p=q#f:8080", context);
            fail();
        } catch (JsonMappingException expected) {
            // expected
        }
    }

    @Test
    public void testEnumDeserializer() throws IOException {
        MyEnum enum1 = MyEnum.ONE;
        MyEnum enum2 = MyEnum.TWO;

        StdKeyDeserializer serializer = StdKeyDeserializer.forType(MyEnum.class);
        assertEquals(enum1, serializer.deserializeKey("ONE", context));
        assertEquals(enum2, serializer.deserializeKey("TWO", context));

        try {
            serializer.deserializeKey("THREE", context);
            fail();
        } catch (JsonMappingException expected) {
            // expected
        }
    }

    @Test
    public void testClassDeserializer() throws IOException {
        Class<?> clazz1 = String[].class;
        Class<?> clazz2 = List.class;

        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Class.class);
        assertEquals(clazz1, serializer.deserializeKey("class [Ljava.lang.String;", context));
        assertEquals(clazz2, serializer.deserializeKey("class java.util.List", context));

        try {
            serializer.deserializeKey("class java.util.ArrayList", context);
            fail();
        } catch (JsonMappingException expected) {
            // expected
        }
    }

    // Add other test methods here as needed for other methods in StdKeyDeserializer class
}