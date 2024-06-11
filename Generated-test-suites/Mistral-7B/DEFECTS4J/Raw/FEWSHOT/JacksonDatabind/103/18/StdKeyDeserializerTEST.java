package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.EnumSet;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFactory;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.databind.util.TestBase;

/**
 * Test cases for StdKeyDeserializer.
 */
public class StdKeyDeserializerTest extends TestBase {
    @JacksonStdImpl
    static class TestEnum {
        public static final TestEnum VALUE_1 = new TestEnum(1);
        public static final TestEnum VALUE_2 = new TestEnum(2);

        private final int _value;

        TestEnum(int value) {
            _value = value;
        }

        @JsonValue
        public int value() {
            return _value;
        }

        @JsonCreator
        public static TestEnum fromValue(int value) {
            return new TestEnum(value);
        }

        @JsonFactory
        public static TestEnum fromString(String value) {
            return fromValue(Integer.parseInt(value));
        }
    }

    @JacksonStdImpl
    static class TestClass {
        private String _key;

        @JsonCreator
        public TestClass(@JsonProperty("key") String key) {
            _key = key;
        }

        @JsonProperty
        public String key() {
            return _key;
        }
    }

    @Test
    public void testSimpleTypes() throws Exception {
        StdKeyDeserializer<String> stringKD = StdKeyDeserializer.forType(String.class);
        assertEquals("key", stringKD.deserializeKey("key", null));

        StdKeyDeserializer<Integer> intKD = StdKeyDeserializer.forType(Integer.class);
        assertEquals((Integer) 1, intKD.deserializeKey("1", null));
        assertEquals((Integer) 0, intKD.deserializeKey("0", null));
        assertEquals((Integer) null, intKD.deserializeKey(null, null));

        StdKeyDeserializer<Boolean> booleanKD = StdKeyDeserializer.forType(Boolean.class);
        assertEquals(Boolean.TRUE, booleanKD.deserializeKey("true", null));
        assertEquals(Boolean.FALSE, booleanKD.deserializeKey("false", null));
        assertEquals(Boolean.FALSE, booleanKD.deserializeKey("foo", null));

        StdKeyDeserializer<Byte> byteKD = StdKeyDeserializer.forType(Byte.class);
        assertEquals((Byte) (byte) 1, byteKD.deserializeKey("1", null));
        assertEquals((Byte) (byte) 0, byteKD.deserializeKey("0", null));
        assertEquals((Byte) null, byteKD.deserializeKey(null, null));

        StdKeyDeserializer<Short> shortKD = StdKeyDeserializer.forType(Short.class);
        assertEquals((Short) (short) 1, shortKD.deserializeKey("1", null));
        assertEquals((Short) (short) 0, shortKD.deserializeKey("0", null));
        assertEquals((Short) null, shortKD.deserializeKey(null, null));

        StdKeyDeserializer<Character> charKD = StdKeyDeserializer.forType(Character.class);
        assertEquals((Character) 'a', charKD.deserializeKey("a", null));
        assertEquals((Character) null, charKD.deserializeKey(null, null));

        StdKeyDeserializer<Float> floatKD = StdKeyDeserializer.forType(Float.class);
        assertEquals((Float) 1.0f, floatKD.deserializeKey("1.0", null));
        assertEquals((Float) 0.0f, floatKD.deserializeKey("0", null));
        assertEquals((Float) null, floatKD.deserializeKey(null, null));

        StdKeyDeserializer<Double> doubleKD = StdKeyDeserializer.forType(Double.class);
        assertEquals((Double) 1.0, doubleKD.deserializeKey("1.0", null));
        assertEquals((Double) 0.0, doubleKD.deserializeKey("0", null));
        assertEquals((Double) null, doubleKD.deserializeKey(null, null));

        StdKeyDeserializer<Locale> localeKD = StdKeyDeserializer.forType(Locale.class);
        assertEquals(Locale.ENGLISH, localeKD.deserializeKey("en_US", null));
        assertEquals(Locale.US, localeKD.deserializeKey("us", null));
        assertEquals(null, localeKD.deserializeKey(null, null));

        StdKeyDeserializer<Date> dateKD = StdKeyDeserializer.forType(Date.class);
        assertEquals(new Date(0), dateKD.deserializeKey("1970-01-01", null));
        assertEquals(null, dateKD.deserializeKey(null, null));

        StdKeyDeserializer<Calendar> calendarKD = StdKeyDeserializer.forType(Calendar.class);
        assertEquals(Calendar.getInstance(), calendarKD.deserializeKey("2023-03-17", null));
        assertEquals(null, calendarKD.deserializeKey(null, null));

        StdKeyDeserializer<URI> uriKD = StdKeyDeserializer.forType(URI.class);
        assertEquals(new URI("http://example.com"), uriKD.deserializeKey("http://example.com", null));
        assertEquals(null, uriKD.deserializeKey(null, null));

        StdKeyDeserializer<URL> urlKD = StdKeyDeserializer.forType(URL.class);
        assertEquals(new URL("http://example.com"), urlKD.deserializeKey("http://example.com", null));
        assertEquals(null, urlKD.deserializeKey(null, null));
    }

    @Test
    public void testEnumTypes() throws Exception {
        StdKeyDeserializer<TestEnum> testEnumKD = StdKeyDeserializer.forType(TestEnum.class);
        assertEquals(TestEnum.VALUE_1, testEnumKD.deserializeKey("1", null));
        assertEquals(TestEnum.VALUE_2, testEnumKD.deserializeKey("2", null));
        assertEquals(null, testEnumKD.deserializeKey(null, null));

        EnumResolver enumResolver = EnumResolver.constructFromEnumClass(TestEnum.class);
        StdKeyDeserializer<TestEnum, EnumResolver> enumKD = new EnumKD(enumResolver, null);
        assertEquals(TestEnum.VALUE_1, enumKD.deserializeKey("1", null));
        assertEquals(TestEnum.VALUE_2, enumKD.deserializeKey("2", null));
        assertEquals(null, enumKD.deserializeKey(null, null));
    }

    @Test
    public void testClassTypes() throws Exception {
        StdKeyDeserializer<TestClass> testClassKD = StdKeyDeserializer.forType(TestClass.class);
        TestClass testClass1 = new TestClass("key1");
        TestClass testClass2 = new TestClass("key2");
        assertEquals(testClass1, testClassKD.deserializeKey("key1", null));
        assertEquals(testClass2, testClassKD.deserializeKey("key2", null));
        assertEquals(null, testClassKD.deserializeKey(null, null));

        AnnotatedClass testClassAnnotatedClass = mapper().getFactory().findClassAnnotations(TestClass.class);
        AnnotatedMethod keyFactoryMethod = testClassAnnotatedClass.findMethod("key");
        Method keyFactory = keyFactoryMethod.getAnnotatedMember();
        StdKeyDeserializer<TestClass, Method> stringFactoryKeyDeserializer = new StringFactoryKeyDeserializer(keyFactory);
        assertEquals(testClass1, stringFactoryKeyDeserializer.deserializeKey("key1", null));
        assertEquals(testClass2, stringFactoryKeyDeserializer.deserializeKey("key2", null));
        assertEquals(null, stringFactoryKeyDeserializer.deserializeKey(null, null));

        Constructor<TestClass> testClassConstructor = TestClass.class.getDeclaredConstructor(String.class);
        testClassConstructor.setAccessible(true);
        StdKeyDeserializer<TestClass, Constructor<?>> constructorKeyDeserializer = new StringCtorKeyDeserializer(testClassConstructor);
        assertEquals(testClass1, constructorKeyDeserializer.deserializeKey("key1", null));
        assertEquals(testClass2, constructorKeyDeserializer.deserializeKey("key2", null));
        assertEquals(null, constructorKeyDeserializer.deserializeKey(null, null));
    }

    @Test
    public void testDelegateDeserializer() throws Exception {
        StdKeyDeserializer<String> stringKD = StdKeyDeserializer.forType(String.class);
        JsonDeserializer<Object> delegateDeserializer = mapper().findValueDeserializer(Object.class, null);
        StdKeyDeserializer<Object> delegatingKD = new DelegatingKD(Object.class, delegateDeserializer);
        assertEquals("key", delegatingKD.deserializeKey("key", null));
    }

    @Test
    public void testUnknownEnumValue() throws Exception {
        EnumSet<TestEnum> enumValues = EnumSet.allOf(TestEnum.class);
        EnumResolver enumResolver = EnumResolver.constructFromEnumClass(TestEnum.class);
        StdKeyDeserializer<TestEnum, EnumResolver> enumKD = new EnumKD(enumResolver, null);
        TestEnum unknownEnumValue = new TestEnum(3);
        // This will not throw any exception as it's enabled by default
        assertEquals(unknownEnumValue, enumKD.deserializeKey("3", null));

        // Disable this behavior and test
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL);
        StdKeyDeserializer<TestEnum, EnumResolver> disabledEnumKD = new EnumKD(enumResolver, null);
        JsonProcessingException e = assertThrows(JsonProcessingException.class, () -> disabledEnumKD.deserializeKey("3", null));
        assertEquals("not one of values excepted for Enum class: [VALUE_1, VALUE_2]", e.getMessage());
    }
}