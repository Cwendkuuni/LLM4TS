package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.StdKeyDeserializer.StringKD;
import com.fasterxml.jackson.databind.util.StdKeyDeserializer.DelegatingKD;
import com.fasterxml.jackson.databind.util.StdKeyDeserializer.EnumKD;
import com.fasterxml.jackson.databind.util.StdKeyDeserializer.StringCtorKeyDeserializer;
import com.fasterxml.jackson.databind.util.StdKeyDeserializer.StringFactoryKeyDeserializer;

/**
 * Test cases for {@link StdKeyDeserializer} class.
 */
@JacksonStdImpl
public class StdKeyDeserializerTest {

    @Test
    public void testBooleanDeserialization() {
        StdKeyDeserializer booleanDeserializer = StdKeyDeserializer.forType(Boolean.class);
        assertEquals(Boolean.TRUE, booleanDeserializer.deserializeKey("true", new TestContext()));
        assertEquals(Boolean.FALSE, booleanDeserializer.deserializeKey("false", new TestContext()));
        try {
            booleanDeserializer.deserializeKey("falseish", new TestContext());
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testByteDeserialization() {
        StdKeyDeserializer byteDeserializer = StdKeyDeserializer.forType(Byte.class);
        assertEquals((byte) 1, byteDeserializer.deserializeKey("1", new TestContext()));
        assertEquals((byte) -127, byteDeserializer.deserializeKey("-127", new TestContext()));
        assertEquals((byte) 128, byteDeserializer.deserializeKey("128", new TestContext()));
        try {
            byteDeserializer.deserializeKey("256", new TestContext());
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testShortDeserialization() {
        StdKeyDeserializer shortDeserializer = StdKeyDeserializer.forType(Short.class);
        assertEquals((short) 1, shortDeserializer.deserializeKey("1", new TestContext()));
        assertEquals((short) -32767, shortDeserializer.deserializeKey("-32767", new TestContext()));
        assertEquals((short) 32767, shortDeserializer.deserializeKey("32767", new TestContext()));
        try {
            shortDeserializer.deserializeKey("32768", new TestContext());
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testCharDeserialization() {
        StdKeyDeserializer charDeserializer = StdKeyDeserializer.forType(Character.class);
        assertEquals('a', charDeserializer.deserializeKey("a", new TestContext()));
        assertEquals('_', charDeserializer.deserializeKey("_", new TestContext()));
        try {
            charDeserializer.deserializeKey("ab", new TestContext());
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testIntDeserialization() {
        StdKeyDeserializer intDeserializer = StdKeyDeserializer.forType(Integer.class);
        assertEquals(1, intDeserializer.deserializeKey("1", new TestContext()));
        assertEquals(-12345, intDeserializer.deserializeKey("-12345", new TestContext()));
        assertEquals(12345, intDeserializer.deserializeKey("12345", new TestContext()));
    }

    @Test
    public void testLongDeserialization() {
        StdKeyDeserializer longDeserializer = StdKeyDeserializer.forType(Long.class);
        assertEquals(1L, longDeserializer.deserializeKey("1", new TestContext()));
        assertEquals(-123456789012345L, longDeserializer.deserializeKey("-123456789012345", new TestContext()));
        assertEquals(123456789012345L, longDeserializer.deserializeKey("123456789012345", new TestContext()));
    }

    @Test
    public void testFloatDeserialization() {
        StdKeyDeserializer floatDeserializer = StdKeyDeserializer.forType(Float.class);
        assertEquals(1.0f, floatDeserializer.deserializeKey("1.0", new TestContext()), 0.0f);
        assertEquals(-1.23f, floatDeserializer.deserializeKey("-1.23", new TestContext()), 0.0f);
        assertEquals(1.2345f, floatDeserializer.deserializeKey("1.2345", new TestContext()), 0.0f);
        try {
            floatDeserializer.deserializeKey("1.2345e6", new TestContext());
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testDoubleDeserialization() {
        StdKeyDeserializer doubleDeserializer = StdKeyDeserializer.forType(Double.class);
        assertEquals(1.0, doubleDeserializer.deserializeKey("1.0", new TestContext()), 0.0);
        assertEquals(-1.23, doubleDeserializer.deserializeKey("-1.23", new TestContext()), 0.0);
        assertEquals(1.2345, doubleDeserializer.deserializeKey("1.2345", new TestContext()), 0.0);
        try {
            doubleDeserializer.deserializeKey("1.2345e6", new TestContext());
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testLocaleDeserialization() {
        StdKeyDeserializer localeDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class, null);
        assertEquals(Locale.US, localeDeserializer.deserializeKey("en_US", new TestContext()));
        assertEquals(Locale.CHINA, localeDeserializer.deserializeKey("zh_CN", new TestContext()));
        try {
            localeDeserializer.deserializeKey("invalid_locale", new TestContext());
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testCurrencyDeserialization() {
        StdKeyDeserializer currencyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CURRENCY, Currency.class, null);
        assertEquals(Currency.USD, currencyDeserializer.deserializeKey("USD", new TestContext()));
        assertEquals(Currency.EUR, currencyDeserializer.deserializeKey("EUR", new TestContext()));
        try {
            currencyDeserializer.deserializeKey("invalid_currency", new TestContext());
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testUUIDDeserialization() {
        StdKeyDeserializer uuidDeserializer = StdKeyDeserializer.forType(UUID.class);
        UUID expectedUuid = UUID.fromString("123e4567-123a-4567-a9ab-123456789ab0");
        assertEquals(expectedUuid, uuidDeserializer.deserializeKey("123e4567-123a-4567-a9ab-123456789ab0", new TestContext()));
    }

    @Test
    public void testURIDeserialization() {
        StdKeyDeserializer uriDeserializer = StdKeyDeserializer.forType(URI.class);
        URI expectedUri = new URI("http://example.com/test");
        assertEquals(expectedUri, uriDeserializer.deserializeKey("http://example.com/test", new TestContext()));
    }

    @Test
    public void testURLDeserialization() {
        StdKeyDeserializer urlDeserializer = StdKeyDeserializer.forType(URL.class);
        URL expectedUrl = new URL("http://example.com/test");
        assertEquals(expectedUrl, urlDeserializer.deserializeKey("http://example.com/test", new TestContext()));
    }

    @Test
    public void testClassDeserialization() {
        StdKeyDeserializer classDeserializer = StdKeyDeserializer.forType(String.class);
        Class<?> stringClass = String.class;
        assertEquals(stringClass, classDeserializer.deserializeKey(stringClass.getName(), new TestContext()));

        StdKeyDeserializer classDeserializer2 = StdKeyDeserializer.forType(Integer.class);
        Class<?> integerClass = Integer.class;
        assertEquals(integerClass, classDeserializer2.deserializeKey(integerClass.getName(), new TestContext()));
    }

    @Test
    public void testDateDeserialization() {
        StdKeyDeserializer dateDeserializer = StdKeyDeserializer.forType(Date.class);
        Date expectedDate = new Date(1234567890123L);
        assertEquals(expectedDate, dateDeserializer.deserializeKey("1234567890123", new TestContext()));
    }

    @Test
    public void testCalendarDeserialization() {
        StdKeyDeserializer calendarDeserializer = StdKeyDeserializer.forType(Calendar.class);
        Calendar expectedCalendar = Calendar.getInstance();
        expectedCalendar.setTime(new Date(1234567890123L));
        assertEquals(expectedCalendar, calendarDeserializer.deserializeKey("1234567890123", new TestContext()));
    }

    @Test
    public void testEnumDeserialization() {
        StdKeyDeserializer enumDeserializer = new EnumKD(EnumResolver.constructForClass(Locale.class), null);
        assertEquals(Locale.US, enumDeserializer.deserializeKey("ENGLISH", new TestContext()));
        assertEquals(Locale.CHINA, enumDeserializer.deserializeKey("SIMPLIFIED_CHINESE", new TestContext()));

        StdKeyDeserializer stringEnumDeserializer = new DelegatingKD(String.class, new StringKD(String.class));
        assertEquals("ENGLISH", stringEnumDeserializer.deserializeKey("ENGLISH", new TestContext()));
        assertEquals("SIMPLIFIED_CHINESE", stringEnumDeserializer.deserializeKey("SIMPLIFIED_CHINESE", new TestContext()));
    }

    @Test
    public void testCustomDeserialization() throws Exception {
        Class<?> clazz = Class.forName("com.custom.MyClass");
        Constructor<?> constructor = clazz.getDeclaredConstructor(String.class);
        constructor.setAccessible(true);

        StdKeyDeserializer customDeserializer = new StringCtorKeyDeserializer(constructor);
        MyClass expectedObject = new MyClass("custom_key");
        assertEquals(expectedObject, customDeserializer.deserializeKey("custom_key", new TestContext()));
    }

    @Test
    public void testStaticFactoryMethodDeserialization() throws Exception {
        Class<?> clazz = Class.forName("com.custom.MyClassFactory");
        Method factoryMethod = clazz.getDeclaredMethod("createInstance", String.class);
        factoryMethod.setAccessible(true);

        StdKeyDeserializer factoryDeserializer = new StringFactoryKeyDeserializer(factoryMethod);
        MyClass expectedObject = new MyClass("custom_key");
        assertEquals(expectedObject, factoryDeserializer.deserializeKey("custom_key", new TestContext()));
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public JsonParser _parser() {
            return null;
        }

        @Override
        public ObjectFactory _valueFactory() {
            return null;
        }

        @Override
        public AnnotationIntrospector _annotationIntrospector() {
            return null;
        }
    }
}