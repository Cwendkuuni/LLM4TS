package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Locale;
import java.util.UUID;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;

public class StdKeyDeserializerTest {

    @Test
    public void testBooleanDeserializer() throws IOException, IllegalAccessException, InstantiationException {
        KeyDeserializer serializer = StdKeyDeserializer.forType(Boolean.class);
        Object result = serializer.deserializeKey("true", new TestContext());
        assertEquals(Boolean.TRUE, result);

        result = serializer.deserializeKey("false", new TestContext());
        assertEquals(Boolean.FALSE, result);

        result = serializer.deserializeKey("not a boolean", new TestContext());
        assertNull(result);
    }

    @Test
    public void testByteDeserializer() throws IOException, IllegalAccessException, InstantiationException {
        KeyDeserializer serializer = StdKeyDeserializer.forType(Byte.class);
        Object result = serializer.deserializeKey("127", new TestContext());
        assertEquals((Byte) Byte.parseByte("127"), result);

        result = serializer.deserializeKey("-128", new TestContext());
        assertEquals((Byte) Byte.parseByte("-128"), result);

        result = serializer.deserializeKey("256", new TestContext());
        assertThrows(JsonProcessingException.class, () -> serializer.deserializeKey("256", new TestContext()));
    }

    @Test
    public void testShortDeserializer() throws IOException, IllegalAccessException, InstantiationException {
        KeyDeserializer serializer = StdKeyDeserializer.forType(Short.class);
        Object result = serializer.deserializeKey("32767", new TestContext());
        assertEquals((Short) Short.parseShort("32767"), result);

        result = serializer.deserializeKey("-32768", new TestContext());
        assertEquals((Short) Short.parseShort("-32768"), result);

        result = serializer.deserializeKey("65536", new TestContext());
        assertThrows(JsonProcessingException.class, () -> serializer.deserializeKey("65536", new TestContext()));
    }

    @Test
    public void testCharDeserializer() throws IOException, IllegalAccessException, InstantiationException {
        KeyDeserializer serializer = StdKeyDeserializer.forType(Character.class);
        Object result = serializer.deserializeKey("A", new TestContext());
        assertEquals(Character.valueOf('A'), result);

        result = serializer.deserializeKey("z", new TestContext());
        assertEquals(Character.valueOf('z'), result);

        result = serializer.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    @Test
    public void testIntDeserializer() throws IOException {
        KeyDeserializer serializer = StdKeyDeserializer.forType(Integer.class);
        Object result = serializer.deserializeKey("12345", new TestContext());
        assertEquals(Integer.valueOf(12345), result);

        result = serializer.deserializeKey("-12345", new TestContext());
        assertEquals(Integer.valueOf(-12345), result);
    }

    @Test
    public void testLongDeserializer() throws IOException {
        KeyDeserializer serializer = StdKeyDeserializer.forType(Long.class);
        Object result = serializer.deserializeKey("12345678901234567890", new TestContext());
        assertEquals(Long.parseLong("12345678901234567890"), result);

        result = serializer.deserializeKey("-12345678901234567890", new TestContext());
        assertEquals(Long.parseLong("-12345678901234567890"), result);
    }

    @Test
    public void testFloatDeserializer() throws IOException {
        KeyDeserializer serializer = StdKeyDeserializer.forType(Float.class);
        Object result = serializer.deserializeKey("3.14", new TestContext());
        assertEquals(Float.valueOf(3.14f), result);

        result = serializer.deserializeKey("-3.14", new TestContext());
        assertEquals(Float.valueOf(-3.14f), result);
    }

    @Test
    public void testDoubleDeserializer() throws IOException {
        KeyDeserializer serializer = StdKeyDeserializer.forType(Double.class);
        Object result = serializer.deserializeKey("3.1415926535897932384626433832795", new TestContext());
        assertEquals(Double.parseDouble("3.1415926535897932384626433832795"), result);

        result = serializer.deserializeKey("-3.1415926535897932384626433832795", new TestContext());
        assertEquals(Double.parseDouble("-3.1415926535897932384626433832795"), result);
    }

    @Test
    public void testLocaleDeserializer() throws IOException {
        KeyDeserializer serializer = StdKeyDeserializer.forType(Locale.class);
        Object result = serializer.deserializeKey("en_US", new TestContext());
        assertEquals(Locale.US, result);

        result = serializer.deserializeKey("fr_FR", new TestContext());
        assertEquals(Locale.FRANCE, result);

        result = serializer.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    @Test
    public void testDateDeserializer() throws IOException {
        KeyDeserializer serializer = StdKeyDeserializer.forType(Date.class);
        Object result = serializer.deserializeKey("2022-03-20", new TestContext());
        assertEquals(new Date(2022, 2, 20), result);

        result = serializer.deserializeKey("2022-03-29", new TestContext());
        assertEquals(new Date(2022, 3, 29), result);

        result = serializer.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    @Test
    public void testCalendarDeserializer() throws IOException {
        KeyDeserializer serializer = StdKeyDeserializer.forType(Calendar.class);
        Object result = serializer.deserializeKey("2022-03-20", new TestContext());
        assertEquals(java.util.GregorianCalendar.getInstance(2022, 2, 20), result);

        result = serializer.deserializeKey("2022-03-29", new TestContext());
        assertEquals(java.util.GregorianCalendar.getInstance(2022, 3, 29), result);

        result = serializer.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    @Test
    public void testURIDeserializer() throws IOException {
        KeyDeserializer serializer = StdKeyDeserializer.forType(URI.class);
        Object result = serializer.deserializeKey("http://example.com", new TestContext());
        assertEquals(new URI("http://example.com"), result);

        result = serializer.deserializeKey("https://example.org", new TestContext());
        assertEquals(new URI("https://example.org"), result);

        result = serializer.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    @Test
    public void testURLDeserializer() throws IOException {
        KeyDeserializer serializer = StdKeyDeserializer.forType(URL.class);
        Object result = serializer.deserializeKey("http://example.com", new TestContext());
        assertEquals(new URL("http://example.com"), result);

        result = serializer.deserializeKey("https://example.org", new TestContext());
        assertEquals(new URL("https://example.org"), result);

        result = serializer.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    @Test
    public void testClassDeserializer() throws IOException {
        KeyDeserializer serializer = StdKeyDeserializer.forType(String[].class);
        Object result = serializer.deserializeKey("[Ljava/lang/String;@789abcd", new TestContext());
        assertEquals(new String[]{"test"}, result);

        serializer = StdKeyDeserializer.forType(Locale[].class);
        result = serializer.deserializeKey("[Ljava/util/Locale;@789abcd", new TestContext());
        assertEquals(new Locale[]{}, result);

        result = serializer.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    @Test
    public void testEnumDeserializer() throws IOException {
        KeyDeserializer serializer = StdKeyDeserializer.forType(Locale.LanguageRange.High.class);
        Object result = serializer.deserializeKey("HIGH", new TestContext());
        assertEquals(Locale.LanguageRange.High.HIGH, result);

        result = serializer.deserializeKey("MID", new TestContext());
        assertEquals(Locale.LanguageRange.High.MID, result);

        result = serializer.deserializeKey("LOW", new TestContext());
        assertEquals(Locale.LanguageRange.High.LOW, result);

        result = serializer.deserializeKey("INVALID", new TestContext());
        assertNull(result);
    }

    @Test
    public void testDelegateDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Class<?> enumClass = Enum.class;
        AnnotatedClass annotatedEnumClass = mapper.getTypeFactory().constructType(enumClass);
        AnnotatedMethod stringEnumValueMethod = annotatedEnumClass.findMethod("valueOf", String.class);

        KeyDeserializer serializer = new StdKeyDeserializer.DelegatingKD(enumClass, stringEnumValueMethod);
        Object result = serializer.deserializeKey("HIGH", new TestContext());
        assertEquals(Locale.LanguageRange.High.HIGH, result);

        result = serializer.deserializeKey("MID", new TestContext());
        assertEquals(Locale.LanguageRange.High.MID, result);

        result = serializer.deserializeKey("LOW", new TestContext());
        assertEquals(Locale.LanguageRange.High.LOW, result);

        result = serializer.deserializeKey("INVALID", new TestContext());
        assertNull(result);
    }

    @Test
    public void testToStringDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        EnumResolver enumResolver = mapper.getFactory().getEnumResolver();
        AnnotatedClass annotatedEnumClass = mapper.getTypeFactory().constructType(Locale.LanguageRange.class);

        KeyDeserializer serializer = new StdKeyDeserializer.EnumKD(enumResolver, null);
        Object result = serializer.deserializeKey("High", new TestContext());
        assertEquals(Locale.LanguageRange.High.HIGH, result);

        result = serializer.deserializeKey("Mid", new TestContext());
        assertEquals(Locale.LanguageRange.High.MID, result);

        result = serializer.deserializeKey("Low", new TestContext());
        assertEquals(Locale.LanguageRange.High.LOW, result);

        result = serializer.deserializeKey("INVALID", new TestContext());
        assertNull(result);
    }

    @Test
    public void testCustomConstructorDeserializer() throws IOException, InstantiationException, IllegalAccessException {
        class TestClass {
            private int value;

            public TestClass(int value) {
                this.value = value;
            }

            public int getValue() {
                return value;
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        AnnotatedClass annotatedClass = mapper.getTypeFactory().constructType(TestClass.class);
        AnnotatedConstructor constructor = annotatedClass.getConstructors()[0];

        KeyDeserializer serializer = new StdKeyDeserializer.StringCtorKeyDeserializer(constructor);
        Object result = serializer.deserializeKey("123", new TestContext());
        assertEquals(new TestClass(123), result);

        result = serializer.deserializeKey("456", new TestContext());
        assertEquals(new TestClass(456), result);

        result = serializer.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    @Test
    public void testCustomStaticFactoryMethodDeserializer() throws IOException {
        class TestClass {
            private int value;

            public TestClass(int value) {
                this.value = value;
            }

            public static TestClass createInstance(String value) {
                return new TestClass(Integer.parseInt(value));
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        AnnotatedClass annotatedClass = mapper.getTypeFactory().constructType(TestClass.class);
        AnnotatedMethod factoryMethod = annotatedClass.findMethod("createInstance", String.class);

        KeyDeserializer serializer = new StdKeyDeserializer.StringFactoryKeyDeserializer(factoryMethod);
        Object result = serializer.deserializeKey("123", new TestContext());
        assertEquals(new TestClass(123), result);

        result = serializer.deserializeKey("456", new TestContext());
        assertEquals(new TestClass(456), result);

        result = serializer.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    static class TestContext extends DeserializationContext {
        private boolean handledWeirdKey;

        @Override
        public Object handleUnknownProperty(DeserializationConfig config, BeanDescription beanDesc, String name, String propertyName, JsonProperty property) throws JsonMappingException {
            handledWeirdKey = true;
            throw new JsonMappingException("Test Exception");
        }

        @Override
        public Object handleWeirdKey(Class<?> keyType, String key, String msg, Object... args) throws JsonMappingException {
            if (!handledWeirdKey) {
                throw new JsonMappingException(String.format(msg, args));
            }
            return null;
        }
    }
}