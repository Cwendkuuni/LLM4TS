package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.Locale;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.StdKeyDeserializer;
import com.fasterxml.jackson.databind.util.TestBase;

/**
 * Test class for {@link StdKeyDeserializer}.
 */
@JacksonStdImpl
public class StdKeyDeserializerTest extends TestBase {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @SuppressWarnings("unchecked")
    private static <T> T newInstance(Class<T> cls, Object... initargs) throws InstantiationException, IllegalAccessException,
            InvocationTargetException {
        return cls.getDeclaredConstructor(initargs.getClass()).newInstance(initargs);
    }

    @SuppressWarnings("unchecked")
    private static <T> T newInstance(Class<T> cls, JsonDeserializer<?> deserializer) throws InstantiationException,
            IllegalAccessException {
        return (T) newInstance(cls, deserializer);
    }

    @Test
    public void testBoolean() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<Boolean> deserializer = mapper.getFactory().createBooleanDeserializer();
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
        assertEquals(Boolean.TRUE, keyDeserializer.deserializeKey("true", context()));
        assertEquals(Boolean.FALSE, keyDeserializer.deserializeKey("false", context()));
        assertNull(keyDeserializer.deserializeKey("not_a_boolean", context()));
    }

    @Test
    public void testByte() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Byte.class);
        assertEquals((Byte) Byte.valueOf((byte) 1), keyDeserializer.deserializeKey("1", context()));
        assertEquals((Byte) Byte.valueOf((byte) 127), keyDeserializer.deserializeKey("127", context()));
        assertNull(keyDeserializer.deserializeKey("256", context()));
    }

    @Test
    public void testShort() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Short.class);
        assertEquals((Short) Short.valueOf((short) 1), keyDeserializer.deserializeKey("1", context()));
        assertEquals((Short) Short.valueOf((short) 32767), keyDeserializer.deserializeKey("32767", context()));
        assertNull(keyDeserializer.deserializeKey("65536", context()));
    }

    @Test
    public void testChar() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Character.class);
        assertEquals((Character) Character.valueOf('A'), keyDeserializer.deserializeKey("A", context()));
        assertNull(keyDeserializer.deserializeKey("not_a_char", context()));
    }

    @Test
    public void testInt() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Integer.class);
        assertEquals(1, keyDeserializer.deserializeKey("1", context()).hashCode());
        assertEquals(123456, keyDeserializer.deserializeKey("123456", context()).hashCode());
        assertNull(keyDeserializer.deserializeKey("not_an_int", context()));
    }

    @Test
    public void testLong() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Long.class);
        assertEquals(1L, keyDeserializer.deserializeKey("1", context()).hashCode());
        assertEquals(1234567890123456L, keyDeserializer.deserializeKey("1234567890123456", context()).hashCode());
        assertNull(keyDeserializer.deserializeKey("not_an_long", context()));
    }

    @Test
    public void testFloat() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Float.class);
        assertEquals(1.0f, keyDeserializer.deserializeKey("1.0", context()).floatValue(), 0.0f);
        assertEquals(1.234567890f, keyDeserializer.deserializeKey("1.234567890", context()).floatValue(), 0.0f);
        assertNull(keyDeserializer.deserializeKey("not_a_float", context()));
    }

    @Test
    public void testDouble() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Double.class);
        assertEquals(1.0, keyDeserializer.deserializeKey("1.0", context()).doubleValue(), 0.0);
        assertEquals(1.234567890123456, keyDeserializer.deserializeKey("1.234567890123456", context()).doubleValue(), 0.0);
        assertNull(keyDeserializer.deserializeKey("not_a_double", context()));
    }

    @Test
    public void testLocale() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<Locale> localeDeserializer = mapper.getFactory().createLocaleDeserializer();
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Locale.class, localeDeserializer);
        assertEquals(Locale.US, keyDeserializer.deserializeKey("en_US", context()));
        assertEquals(Locale.JAPAN, keyDeserializer.deserializeKey("ja_JP", context()));
        assertNull(keyDeserializer.deserializeKey("not_a_locale", context()));
    }

    @Test
    public void testDate() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Date.class);
        assertEquals(new Date(), keyDeserializer.deserializeKey("", context()));
        assertEquals(new Date(1234567890123L), keyDeserializer.deserializeKey("1234567890123", context()));
        assertNull(keyDeserializer.deserializeKey("not_a_date", context()));
    }

    @Test
    public void testCalendar() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Calendar.class);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        assertEquals(calendar, keyDeserializer.deserializeKey("", context()));
        calendar.setTimeInMillis(1234567890123L);
        assertEquals(calendar, keyDeserializer.deserializeKey("1234567890123", context()));
        assertNull(keyDeserializer.deserializeKey("not_a_calendar", context()));
    }

    @Test
    public void testUUID() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(UUID.class);
        assertEquals(UUID.randomUUID(), keyDeserializer.deserializeKey("", context()));
        UUID uuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        assertEquals(uuid, keyDeserializer.deserializeKey("123e4567-e89b-12d3-a456-426614174000", context()));
        assertNull(keyDeserializer.deserializeKey("not_a_uuid", context()));
    }

    @Test
    public void testURI() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URI.class);
        assertEquals(new URI(""), keyDeserialiser.deserializeKey("", context()));
        assertEquals(new URI("https://google.com"), keyDeserializer.deserializeKey("https://google.com", context()));
        assertNull(keyDeserializer.deserializeKey("not_a_uri", context()));
    }

    @Test
    public void testURL() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);
        assertEquals(new URL(""), keyDeserializer.deserializeKey("", context()));
        assertEquals(new URL("https://google.com"), keyDeserializer.deserializeKey("https://google.com", context()));
        assertNull(keyDeserializer.deserializeKey("not_a_url", context()));
    }

    @Test
    public void testEnum() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        EnumSet<Color> colors = EnumSet.allOf(Color.class);
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Color.class);
        EnumResolver enumResolver = mock(EnumResolver.class);
        when(enumResolver.findEnum(eq("RED"))).thenReturn(colors.stream().filter(c -> c == Color.RED).findFirst().orElse(null));
        when(enumResolver.getEnumIds()).thenReturn(EnumSet.of("RED", "GREEN", "BLUE"));
        keyDeserializer._byNameResolver = enumResolver;
        assertEquals(Color.RED, keyDeserializer.deserializeKey("RED", context()));
        assertEquals(Color.GREEN, keyDeserializer.deserializeKey("GREEN", context()));
        assertNull(keyDeserializer.deserializeKey("NOT_COLOR", context()));
    }

    @Test
    public void testClass() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);
        assertEquals(String.class, keyDeserializer.deserializeKey("java.lang.String", context()));
        assertNull(keyDeserializer.deserializeKey("not_a_class", context()));
    }

    @Test
    public void testDelegatingKD() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JacksonAnnotationIntrospector introspector = new JacksonAnnotationIntrospector();
        JsonDeserializer<MyClass> deserializer = mapper.getFactory().createValueDeserializer(MyClass.class, introspector);
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer.DelegatingKD(MyClass.class, deserializer);
        assertEquals(MyClass.class, keyDeserializer.getKeyClass());
        assertEquals(new MyClass(), keyDeserializer.deserializeKey("", context()));
    }

    @Test
    public void testEnumFactory() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        EnumResolver enumResolver = mock(EnumResolver.class);
        when(enumResolver.getEnumClass()).thenReturn(MyEnum.class);
        when(enumResolver.findEnum(anyString())).thenAnswer(i -> i.getArguments()[0]);
        JsonDeserializer<MyEnum> deserializer = new StdKeyDeserializer.EnumFactoryKeyDeserializer<>(MyEnum.class, enumResolver);
        assertEquals(MyEnum.class, deserializer.getKeyClass());
        MyEnum expectedEnum = MyEnum.GREEN;
        when(enumResolver.findEnum(eq("GREEN"))).thenReturn(expectedEnum);
        assertEquals(expectedEnum, deserializer.deserializeKey("GREEN", context()));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void testStringCtor() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Constructor<MyClass> constructor = MyClass.class.getDeclaredConstructor(String.class);
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer.StringCtorKeyDeserializer(constructor);
        assertEquals(MyClass.class, keyDeserializer.getKeyClass());
        assertEquals(new MyClass("test"), keyDeserializer.deserializeKey("test", context()));
    }

    @Test
    public void testStringFactory() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Method method = MyClass.class.getDeclaredMethod("create", String.class);
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer.StringFactoryKeyDeserializer(method);
        assertEquals(MyClass.class, keyDeserializer.getKeyClass());
        MyClass expected = new MyClass();
        when(expected.create("test")).thenReturn(expected);
        assertEquals(expected, keyDeserializer.deserializeKey("test", context()));
    }

    static class Color {
        @JsonProperty
        public final String name;

        Color(String name) {
            this.name = name;
        }

        public static enum Enum {
            RED,
            GREEN,
            BLUE
        }
    }

    static class MyClass {
        @JsonProperty
        public final String name;

        MyClass(String name) {
            this.name = name;
        }

        public static MyClass create(String name) {
            return new MyClass(name);
        }
    }

    static class MyEnum {
        @JsonProperty
        public final String name;

        MyEnum(String name) {
            this.name = name;
        }

        public static class Enum {
            @JsonProperty
            public final String name;

            Enum(String name) {
                this.name = name;
            }

            public static final Enum RED = new Enum("RED");
            public static final Enum GREEN = new Enum("GREEN");
            public static final Enum BLUE = new Enum("BLUE");
        }
    }
}