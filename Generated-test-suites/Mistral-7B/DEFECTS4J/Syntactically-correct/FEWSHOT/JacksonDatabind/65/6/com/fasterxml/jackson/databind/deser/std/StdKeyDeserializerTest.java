package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;

/**
 * Test cases for StdKeyDeserializer class.
 */
public class StdKeyDeserializerTest {

    @Test
    public void testForType() {
        Class<?>[] types = {String.class, Object.class, UUID.class, Integer.class, Long.class, Date.class,
                Calendar.class, Boolean.class, Byte.class, Character.class, Short.class, Float.class,
                Double.class, URI.class, URL.class, Class.class, Locale.class, Currency.class};
        for (Class<?> type : types) {
            StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(type);
            if (keyDeserializer != null) {
                assertEquals(type, keyDeserializer.getKeyClass());
            } else {
                assertNull(keyDeserializer);
            }
        }
    }

    @Test
    public void testDeserializeKey() throws IOException, JsonProcessingException {
        // Test for common types
        StdKeyDeserializer stringKD = StringKD.forType(String.class);
        assertInstanceOf(StringKD.class, stringKD);
        Object key = stringKD.deserializeKey("test", new TestContext());
        assertEquals("test", key);

        StdKeyDeserializer objectKD = StringKD.forType(Object.class);
        assertInstanceOf(StringKD.class, objectKD);
        key = objectKD.deserializeKey("TestObject", new TestContext());
        assertInstanceOf(TestObject.class, key);

        // Test for UUID type
        StdKeyDeserializer uuidKD = StdKeyDeserializer.forType(UUID.class);
        assertInstanceOf(StdKeyDeserializer.class, uuidKD);
        key = uuidKD.deserializeKey("123e4567-e89b-12d3-a456-426614174000", new TestContext());
        UUID uuid = (UUID) key;
        assertEquals("123e4567-e89b-12d3-a456-426614174000", uuid.toString());

        // Test for Integer type
        StdKeyDeserializer intKD = StdKeyDeserializer.forType(Integer.class);
        assertInstanceOf(StdKeyDeserializer.class, intKD);
        key = intKD.deserializeKey("123", new TestContext());
        int number = (Integer) key;
        assertEquals(123, number.intValue());

        // Test for Date type
        StdKeyDeserializer dateKD = StdKeyDeserializer.forType(Date.class);
        assertInstanceOf(StdKeyDeserializer.class, dateKD);
        key = dateKD.deserializeKey("2023-03-15", new TestContext());
        Date date = (Date) key;
        assertEquals("Thu Mar 16 00:00:00 CET 2023", date.toString());

        // Test for custom classes with String constructor
        class TestCustomClass {
            private final String value;

            @JsonCreator
            public TestCustomClass(String value) {
                this.value = value;
            }

            @JsonValue
            public String toString() {
                return value;
            }
        }

        AnnotatedClass annotatedClass = new TestContext().mapper().getTypeFactory()
                .constructType(TestCustomClass.class);
        AnnotatedConstructor constructor = annotatedClass.findConstructor(ConstructorProperties.Std.STANDARD_CONSTRUCTOR);
        StdKeyDeserializer customClassKD = new StringCtorKeyDeserializer(constructor);
        key = customClassKD.deserializeKey("TestCustomClassValue", new TestContext());
        TestCustomClass testCustomClass = (TestCustomClass) key;
        assertEquals("TestCustomClassValue", testCustomClass.value);

        // Test for custom classes with static factory method
        class TestCustomClassFactory {
            public static TestCustomClass create(String value) {
                return new TestCustomClass(value);
            }
        }

        AnnotatedClass annotatedFactoryClass = new TestContext().mapper().getTypeFactory()
                .constructType(TestCustomClassFactory.class);
        AnnotatedMethod method = annotatedFactoryClass.findMethod("create", MethodProperties.Std.PUBLIC_STATIC);
        StdKeyDeserializer factoryClassKD = new StringFactoryKeyDeserializer(method);
        key = factoryClassKD.deserializeKey("TestCustomClassValue", new TestContext());
        TestCustomClass testCustomClass = (TestCustomClass) key;
        assertEquals("TestCustomClassValue", testCustomClass.value);

        // Test for Enum type with name resolution
        EnumSet<TestEnum> enumValues = EnumSet.allOf(TestEnum.class);
        StdKeyDeserializer enumKD = new EnumKD(new EnumResolver(TestEnum.class), null);
        key = enumKD.deserializeKey("TestEnumValue", new TestContext());
        TestEnum testEnum = (TestEnum) key;
        assertEquals(TestEnum.TestEnumValue, testEnum);

        // Test for Enum type with toString resolution
        EnumResolver toStringResolver = new EnumResolver(TestEnum.class);
        StdKeyDeserializer toStringEnumKD = new EnumKD(null, null);
        toStringEnumKD._byToStringResolver = toStringResolver;
        key = toStringEnumKD.deserializeKey("TEST_ENUM_VALUE", new TestContext());
        TestEnum testToStringEnum = (TestEnum) key;
        assertEquals(TestEnum.TestEnumValue, testToStringEnum);

        // Test for Locale type
        StdKeyDeserializer localeKD = StdKeyDeserializer.forType(Locale.class);
        assertInstanceOf(StdKeyDeserializer.class, localeKD);
        key = localeKD.deserializeKey("en_US", new TestContext());
        Locale locale = (Locale) key;
        assertEquals("en_US", locale.toString());

        // Test for Currency type
        StdKeyDeserializer currencyKD = StdKeyDeserializer.forType(Currency.class);
        assertInstanceOf(StdKeyDeserializer.class, currencyKD);
        key = currencyKD.deserializeKey("USD", new TestContext());
        Currency currency = (Currency) key;
        assertEquals("USD", currency.getCurrencyCode());

        // Test for Class type
        StdKeyDeserializer classKD = StdKeyDeserializer.forType(Class.class);
        assertInstanceOf(StdKeyDeserializer.class, classKD);
        Class<?> testClass = TestClass.class;
        key = classKD.deserializeKey(testClass.getName(), new TestContext());
        assertSame(testClass, key);

        // Test for URI and URL types
        StdKeyDeserializer uriKD = StdKeyDeserializer.forType(URI.class);
        assertInstanceOf(StdKeyDeserializer.class, uriKD);
        key = uriKD.deserializeKey("https://www.example.com", new TestContext());
        URI uri = (URI) key;
        assertEquals("https://www.example.com", uri.toString());

        StdKeyDeserializer urlKD = StdKeyDeserializer.forType(URL.class);
        assertInstanceOf(StdKeyDeserializer.class, urlKD);
        URL url = new URL("https://www.example.com");
        key = urlKD.deserializeKey(url.toString(), new TestContext());
        assertSame(url, key);
    }
}

class TestContext extends TestBaseContext {
    // Extend TestBaseContext for additional tests
}

class TestEnum {
    @JsonDeserialize
    public enum TestEnum {
        TestEnumValue,
        @JsonValue
        TEST_ENUM_VALUE;
    }
}

class TestObject {
    private final String value;

    public TestObject(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

class TestClass {
    // A custom class for testing
}