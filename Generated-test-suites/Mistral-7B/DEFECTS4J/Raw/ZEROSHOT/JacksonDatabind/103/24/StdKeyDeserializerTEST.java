package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.test.BaseMapTest;

/**
 * Test for StdKeyDeserializer class
 */
@JacksonStdImpl
public class StdKeyDeserializerTest extends BaseMapTest {

    @Test
    public void testBooleanDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Boolean.class);
        Object key = serializer.deserializeKey("true", mapper.getDeserializationContext());
        assertTrue((Boolean) key);
        key = serializer.deserializeKey("false", mapper.getDeserializationContext());
        assertFalse((Boolean) key);
    }

    @Test
    public void testByteDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Byte.class);
        Object key = serializer.deserializeKey("10", mapper.getDeserializationContext());
        assertEquals((Byte) 10, key);
        try {
            key = serializer.deserializeKey("-128", mapper.getDeserializationContext());
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testShortDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Short.class);
        Object key = serializer.deserializeKey("12345", mapper.getDeserializationContext());
        assertEquals((Short) 12345, key);
        try {
            key = serializer.deserializeKey("-32768", mapper.getDeserializationContext());
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testCharDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Character.class);
        Object key = serializer.deserializeKey("A", mapper.getDeserializationContext());
        assertEquals((Character) 'A', key);
        try {
            key = serializer.deserializeKey("\\u00E9", mapper.getDeserializationContext());
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testIntDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Integer.class);
        Object key = serializer.deserializeKey("123456", mapper.getDeserializationContext());
        assertEquals((Integer) 123456, key);
    }

    @Test
    public void testLongDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Long.class);
        Object key = serializer.deserializeKey("12345678901234567890", mapper.getDeserializationContext());
        assertEquals((Long) 12345678901234567890L, key);
    }

    @Test
    public void testFloatDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Float.class);
        Object key = serializer.deserializeKey("3.14", mapper.getDeserializationContext());
        assertEquals((Float) 3.14F, key);
    }

    @Test
    public void testDoubleDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Double.class);
        Object key = serializer.deserializeKey("3.1415926535897932384626433832795028841", mapper.getDeserializationContext());
        assertEquals((Double) 3.1415926535897932384626433832795028841D, key);
    }

    @Test
    public void testLocaleDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Locale.class);
        Object key = serializer.deserializeKey("en_US", mapper.getDeserializationContext());
        assertEquals(Locale.US, key);
    }

    @Test
    public void testDateDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Date.class);
        Object key = serializer.deserializeKey("2022-12-31T23:59:59.999Z", mapper.getDeserializationContext());
        assertEquals(new Date(1671542399999L), key);
    }

    @Test
    public void testCalendarDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Calendar.class);
        Object key = serializer.deserializeKey("2022-12-31", mapper.getDeserializationContext());
        Calendar calendar = (Calendar) key;
        assertEquals(2022, calendar.get(Calendar.YEAR));
        assertEquals(12, calendar.get(Calendar.MONTH));
        assertEquals(31, calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Test
    public void testUriDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URI.class);
        Object key = serializer.deserializeKey("https://www.example.com", mapper.getDeserializationContext());
        URI uri = (URI) key;
        assertEquals("https", uri.getScheme());
        assertEquals("/www.example.com", uri.getPath());
    }

    @Test
    public void testUrlDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URL.class);
        Object key = serializer.deserializeKey("https://www.example.com", mapper.getDeserializationContext());
        URL url = (URL) key;
        assertEquals("https", url.getProtocol());
        assertEquals("/www.example.com", url.getFile());
    }

    @Test
    public void testClassDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(String[].class);
        Object key = serializer.deserializeKey("[Ljava.lang.String;@13983742", mapper.getDeserializationContext());
        assertArrayEquals(new String[]{"a", "b", "c"}, key);
    }

    @Test
    public void testEnumDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        final Enum testEnum = Enum.valueOf(TestEnum.class, "ITEM_1");
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(TestEnum.class);
        Object key = serializer.deserializeKey("ITEM_1", mapper.getDeserializationContext());
        assertEquals(testEnum, key);
    }

    @Test
    public void testEnumDeserializerWhenUnknownValue() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        final Enum testEnum = Enum.valueOf(TestEnum.class, "ITEM_1");
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(TestEnum.class);
        Object key = serializer.deserializeKey("UNKNOWN_VALUE", mapper.getDeserializationContext());
        assertNull(key);
    }

    @Test(expected = IllegalStateException.class)
    public void testUnknownEnumTypeDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(UnknownEnumType.class);
        serializer.deserializeKey("UNKNOWN_VALUE", mapper.getDeserializationContext());
    }

    @Test
    public void testUrlDeserializerWithInvalidFormat() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URL.class);
        try {
            Object key = serializer.deserializeKey("http://example.com:80", mapper.getDeserializationContext());
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testUriDeserializerWithInvalidFormat() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URI.class);
        try {
            Object key = serializer.deserializeKey("http://example.com:80", mapper.getDeserializationContext());
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    enum TestEnum {
        ITEM_1, ITEM_2, ITEM_3;

        @JsonValue
        public String toString() {
            return name();
        }
    }

    enum UnknownEnumType {
        // no values or constructors defined
    }
}