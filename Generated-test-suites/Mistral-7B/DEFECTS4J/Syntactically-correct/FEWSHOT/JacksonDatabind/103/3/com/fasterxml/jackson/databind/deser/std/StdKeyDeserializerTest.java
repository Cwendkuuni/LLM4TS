package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.io.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.*;

import org.junit.Test;

public class StdKeyDeserializerTest {

    // Test for StringKD
    @Test
    public void testStringKeyDeserializer() throws Exception {
        StringKD stringKD = StdKeyDeserializer.StringKD.forType(String.class);
        Object key = stringKD.deserializeKey("test", new TestContext());
        assertEquals("test", key);
    }

    @Test
    public void testObjectKeyDeserializer() throws Exception {
        StringKD stringKD = StdKeyDeserializer.StringKD.forType(Object.class);
        Object key = stringKD.deserializeKey("test", new TestContext());
        assertEquals("test", key.toString());
    }

    // Test for Boolean type
    @Test
    public void testBooleanKeyDeserializer() throws Exception {
        StdKeyDeserializer booleanKD = StdKeyDeserializer.forType(Boolean.class);
        Object key = booleanKD.deserializeKey("true", new TestContext());
        assertTrue((Boolean) key);

        key = booleanKD.deserializeKey("false", new TestContext());
        assertFalse((Boolean) key);
    }

    // Test for Byte type
    @Test
    public void testByteKeyDeserializer() throws Exception {
        StdKeyDeserializer byteKD = StdKeyDeserializer.forType(Byte.class);
        Object key = byteKD.deserializeKey("127", new TestContext());
        assertEquals((Byte) 127, key);

        try {
            key = byteKD.deserializeKey("-128", new TestContext());
            fail();
        } catch (IOException e) {
            // Expected
        }
    }

    // Test for Short type
    @Test
    public void testShortKeyDeserializer() throws Exception {
        StdKeyDeserializer shortKD = StdKeyDeserializer.forType(Short.class);
        Object key = shortKD.deserializeKey("32767", new TestContext());
        assertEquals((Short) 32767, key);

        try {
            key = shortKD.deserializeKey("-32768", new TestContext());
            fail();
        } catch (IOException e) {
            // Expected
        }
    }

    // Test for Char type
    @Test
    public void testCharKeyDeserializer() throws Exception {
        StdKeyDeserializer charKD = StdKeyDeserializer.forType(Character.class);
        Object key = charKD.deserializeKey("A", new TestContext());
        assertEquals((Character) 'A', key);
    }

    // Test for Int type
    @Test
    public void testIntKeyDeserializer() throws Exception {
        StdKeyDeserializer intKD = StdKeyDeserializer.forType(Integer.class);
        Object key = intKD.deserializeKey("42", new TestContext());
        assertEquals((Integer) 42, key);
    }

    // Test for Long type
    @Test
    public void testLongKeyDeserializer() throws Exception {
        StdKeyDeserializer longKD = StdKeyDeserializer.forType(Long.class);
        Object key = longKD.deserializeKey("9223372036854775807", new TestContext());
        assertEquals((Long) 9223372036854775807L, key);
    }

    // Test for Float type
    @Test
    public void testFloatKeyDeserializer() throws Exception {
        StdKeyDeserializer floatKD = StdKeyDeserializer.forType(Float.class);
        Object key = floatKD.deserializeKey("3.14159265", new TestContext());
        assertEquals((Float) 3.14159265F, key);
    }

    // Test for Double type
    @Test
    public void testDoubleKeyDeserializer() throws Exception {
        StdKeyDeserializer doubleKD = StdKeyDeserializer.forType(Double.class);
        Object key = doubleKD.deserializeKey("6.28318530718", new TestContext());
        assertEquals((Double) 6.28318530718, key);
    }

    // Test for Locale type
    @Test
    public void testLocaleKeyDeserializer() throws Exception {
        StdKeyDeserializer localeKD = StdKeyDeserializer.forType(Locale.class);
        Object key = localeKD.deserializeKey("en_US", new TestContext());
        assertEquals(Locale.US, key);
    }

    // Test for Date type
    @Test
    public void testDateKeyDeserializer() throws Exception {
        StdKeyDeserializer dateKD = StdKeyDeserializer.forType(Date.class);
        Object key = dateKD.deserializeKey("2023-03-23", new TestContext());
        assertEquals(new Date(1679462400000L), key);
    }

    // Test for Calendar type
    @Test
    public void testCalendarKeyDeserializer() throws Exception {
        StdKeyDeserializer calendarKD = StdKeyDeserializer.forType(Calendar.class);
        Object key = calendarKD.deserializeKey("2023-03-23", new TestContext());
        GregorianCalendar gc = new GregorianCalendar(2023, 2, 23);
        assertEquals(gc, key);
    }

    // Test for UUID type
    @Test
    public void testUUIDKeyDeserializer() throws Exception {
        StdKeyDeserializer uuidKD = StdKeyDeserializer.forType(UUID.class);
        Object key = uuidKD.deserializeKey("00000000-0000-0000-0000-000000000000", new TestContext());
        assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), key);
    }

    // Test for URI type
    @Test
    public void testURIKeyDeserializer() throws Exception {
        StdKeyDeserializer uriKD = StdKeyDeserializer.forType(URI.class);
        Object key = uriKD.deserializeKey("http://example.com", new TestContext());
        URI uri = new URI("http://example.com");
        assertEquals(uri, key);
    }

    // Test for URL type
    @Test
    public void testURLKeyDeserializer() throws Exception {
        StdKeyDeserializer urlKD = StdKeyDeserializer.forType(URL.class);
        Object key = urlKD.deserializeKey("http://example.com", new TestContext());
        URL url = new URL("http://example.com");
        assertEquals(url, key);
    }

    // Test for Class type
    @Test
    public void testClassKeyDeserializer() throws Exception {
        StdKeyDeserializer classKD = StdKeyDeserializer.forType(String.class);
        Class<?> keyClass = Class.forName("java.lang.String");
        Object key = classKD.deserializeKey(keyClass.getName(), new TestContext());
        assertEquals(keyClass, key);
    }

    // Test for Byte Array type
    @Test
    public void testByteArrayKeyDeserializer() throws Exception {
        StdKeyDeserializer byteArrayKD = StdKeyDeserializer.forType(byte[].class);
        Object key = byteArrayKD.deserializeKey("RgNoXKlNTl9GgoQrJw+E2A==", new TestContext());
        byte[] expectedByteArray = { 82, 103, 111, 111, 108, 83, 110, 100, 111, 110, 116, 103, 101, 114, 121, 112, 108, 101 };
        byte[] keyByteArray = (byte[]) key;
        assertArrayEquals(expectedByteArray, keyByteArray);
    }

    // Test for Enum type
    @Test
    public void testEnumKeyDeserializer() throws Exception {
        Enum enumType = Enum.class;
        StdKeyDeserializer enumKD = new StdKeyDeserializer(1, enumType);

        // Test with valid enum value
        Object key = enumKD.deserializeKey("EAGER", new TestContext());
        assertEquals(EnumValue1.EAGER, key);

        // Test with unknown enum value
        try {
            key = enumKD.deserializeKey("LAZY", new TestContext());
            fail();
        } catch (IOException e) {
            // Expected
        }
    }

    // Test for Custom class key deserializer
    @Test
    public void testCustomClassKeyDeserializer() throws Exception {
        CustomClass customClass = new CustomClass();
        Constructor<CustomClass> constructor = CustomClass.class.getDeclaredConstructor(String.class);
        constructor.setAccessible(true);

        StdKeyDeserializer customClassKD = new StdKeyDeserializer(1, CustomClass.class, null) {
            @Override
            protected Object _parse(String key, DeserializationContext ctxt) throws Exception {
                return constructor.newInstance(key);
            }
        };

        Object key = customClassKD.deserializeKey("CustomValue", new TestContext());
        CustomClass customClassInstance = (CustomClass) key;
        assertEquals("CustomValue", customClassInstance.getValue());
    }

    // Test for Delegating key deserializer
    @Test
    public void testDelegatingKeyDeserializer() throws Exception {
        DeserializationContext context = new TestContext();
        JsonDeserializer<Integer> integerDeserializer = new SimpleIntDeserializer();
        StdKeyDeserializer delegatingKD = new StdKeyDeserializer(-1, String.class) {
            @Override
            public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
                return integerDeserializer.deserialize(ctxt.getParser(), ctxt);
            }
        };

        Object key = delegatingKD.deserializeKey("42", context);
        assertEquals(42, key);
    }

    // Test for Enum with factory method key deserializer
    @Test
    public void testEnumWithFactoryMethodKeyDeserializer() throws Exception {
        EnumWithFactoryMethod enumType = EnumWithFactoryMethod.class;
        ObjectFactory<EnumWithFactoryMethod> factory = new ObjectFactory<EnumWithFactoryMethod>() {
            @Override
            public EnumWithFactoryMethod createInstance(DeserializationContext ctxt) throws IOException {
                return EnumWithFactoryMethod.VALUE2;
            }
        };
        Method factoryMethod = EnumWithFactoryMethod.class.getMethod("createInstance");

        StdKeyDeserializer enumWithFactoryMethodKD = new StdKeyDeserializer(1, enumType.getComponentType()) {
            @Override
            protected Object _parse(String key, DeserializationContext ctxt) throws Exception {
                return factoryMethod.invoke(null, key);
            }
        };

        Object key = enumWithFactoryMethodKD.deserializeKey("VALUE2", new TestContext());
        assertEquals(EnumWithFactoryMethod.VALUE2, key);
    }
}

class TestContext extends DeserializationContext {
    @Override
    public ObjectMapper getConfig() {
        return new ObjectMapper();
    }
}