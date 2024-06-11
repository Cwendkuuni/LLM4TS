package com.example.test; // replace with your test package

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Arrays;
import java.util.Date;

import javax.xml.bind.DatatypeConverter;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer.NullValueProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBase;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector.FindAnnotatedMembersHandler;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector.FindValueDeserializerHandler;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.SerializerInstanceManager;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.type.Types;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

public class StdKeyDeserializerTest {

    private static final Constructor<?> STRING_CTOR_CONSTRUCTOR;
    private static final Method STRING_FACTORY_METHOD;

    static {
        try {
            STRING_CTOR_CONSTRUCTOR = StdKeyDeserializer.StringKD.class.getDeclaredConstructor(Class.class);
            STRING_CTOR_CONSTRUCTOR.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Missing constructor StdKeyDeserializer.StringKD(Class<?>)", e);
        }

        try {
            STRING_FACTORY_METHOD = StdKeyDeserializer.class.getDeclaredMethod("forType", Class.class);
            STRING_FACTORY_METHOD.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Missing method StdKeyDeserializer.forType(Class<?>)", e);
        }
    }

    private DeserializationContext context;
    private ObjectMapper objectMapper;

    @Before
    public void setup() throws Exception {
        context = mock(DeserializationContext.class);
        AnnotationIntrospector annotationIntrospector = new NopAnnotationIntrospector();
        objectMapper = new ObjectMapper();
        objectMapper.setAnnotationIntrospector(annotationIntrospector);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testStdKeyDeserializer_forBoolean() throws IOException, JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
        Object key = keyDeserializer.deserializeKey("true", context);
        assertEquals(Boolean.TRUE, key);

        key = keyDeserializer.deserializeKey("false", context);
        assertEquals(Boolean.FALSE, key);
    }

    @Test
    public void testStdKeyDeserializer_forByte() throws IOException, JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Byte.class);
        Object key = keyDeserializer.deserializeKey("0", context);
        assertEquals(Byte.valueOf((byte) 0), key);

        key = keyDeserializer.deserializeKey("127", context);
        assertEquals(Byte.valueOf((byte) 127), key);

        key = keyDeserializer.deserializeKey("128", context);
        assertNull(key);
    }

    @Test
    public void testStdKeyDeserializer_forShort() throws IOException, JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Short.class);
        Object key = keyDeserializer.deserializeKey("0", context);
        assertEquals(Short.valueOf((short) 0), key);

        key = keyDeserializer.deserializeKey("32767", context);
        assertEquals(Short.valueOf((short) 32767), key);

        key = keyDeserializer.deserializeKey("32768", context);
        assertNull(key);
    }

    @Test
    public void testStdKeyDeserializer_forChar() throws IOException, JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Character.class);
        Object key = keyDeserializer.deserializeKey("A", context);
        assertEquals(Character.valueOf('A'), key);
    }

    @Test
    public void testStdKeyDeserializer_forInt() throws IOException, JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Integer.class);
        Object key = keyDeserializer.deserializeKey("0", context);
        assertEquals(Integer.valueOf(0), key);
    }

    @Test
    public void testStdKeyDeserializer_forLong() throws IOException, JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Long.class);
        Object key = keyDeserializer.deserializeKey("0", context);
        assertEquals(Long.valueOf(0), key);
    }

    @Test
    public void testStdKeyDeserializer_forFloat() throws IOException, JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Float.class);
        Object key = keyDeserializer.deserializeKey("0.0", context);
        assertEquals(Float.valueOf(0.0F), key);
    }

    @Test
    public void testStdKeyDeserializer_forDouble() throws IOException, JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Double.class);
        Object key = keyDeserializer.deserializeKey("0.0", context);
        assertEquals(Double.valueOf(0.0), key);
    }

    @Test
    public void testStdKeyDeserializer_forLocale() throws IOException, JsonProcessingException {
        FromStringDeserializer deserializer = mock(FromStringDeserializer.class);
        when(deserializer.deserialize(eq("en_US"), eq(context))).thenReturn(Locale.ENGLISH);

        KeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class, deserializer);
        Object key = keyDeserializer.deserializeKey("en_US", context);
        assertEquals(Locale.ENGLISH, key);
    }

    @Test
    public void testStdKeyDeserializer_forURL() throws IOException, JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);
        Object key = keyDeserializer.deserializeKey("http://example.com", context);
        assertEquals(new URL("http://example.com"), key);
    }

    @Test
    public void testStdKeyDeserializer_forURI() throws IOException, JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URI.class);
        Object key = keyDeserializer.deserializeKey("http://example.com", context);
        assertEquals(new URI("http://example.com"), key);
    }

    @Test
    public void testStdKeyDeserializer_forEnum() throws IOException, JsonProcessingException {
        AnnotatedClass ac = mock(AnnotatedClass.class);
        AnnotatedMethod factory = mock(AnnotatedMethod.class);
        when(ac.getAnnotatedMembers(eq(PropertyName.class), eq(FindAnnotatedMembersHandler.class))).thenReturn(Arrays.asList(factory));
        when(factory.getParameterTypes()).thenReturn(new Class[0]);
        when(factory.getName()).thenReturn("fromString");

        KeyDeserializer keyDeserializer = new EnumKD(mock(EnumResolver.class), factory);
        Object key = keyDeserializer.deserializeKey("NORMAL", context);
        assertEquals(Enum.NORMAL, key);

        assertThrows(IllegalArgumentException.class, () -> keyDeserializer.deserializeKey("INVALID", context));
    }

    @Test
    public void testStdKeyDeserializer_forClass() throws IOException, JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);
        Object key = keyDeserializer.deserializeKey("java.util.HashMap", context);
        assertEquals(java.util.HashMap.class, key);
    }

    @Test
    public void testStdKeyDeserializer_forNullValueProvider() throws IOException, JsonProcessingException {
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);
        when(nullValueProvider.findValueDeserializer(eq(BeanPropertyDefinition.class),
                eq(FindValueDeserializerHandler.class), eq(SerializerInstanceManager.class)))
                .thenReturn(null);

        KeyDeserializer keyDeserializer = new BeanDeserializerBase.BeanPropertyKeyDeserializer(nullValueProvider);
        Object key = keyDeserializer.deserializeKey(null, context);
        assertNull(key);
    }

    @Test
    public void testStdKeyDeserializer_forStringFactory() throws IOException, JsonProcessingException {
        // This test requires a custom implementation of KeyDeserializer.StringFactoryKeyDeserializer
        // due to the private constructor and the lack of a public no-args factory method.
        // You can create a new test case for that, or remove this test if the class is not used.

        Class<?> clazz = StdKeyDeserializer.class;
        ObjectMapper mapper = new ObjectMapper();
        AnnotationIntrospector introspector = mapper.getAnnotationIntrospector();
        TypeFactory typeFactory = mapper.getTypeFactory();

        AnnotatedClass annotatedClass = introspector.findClassAnnotations(clazz);
        BeanPropertyDefinition propertyDefinition = introspector.findBeanPropertyDefinition(clazz, "stringFactoryKeyDeserializer");
        FindValueDeserializerHandler findValueDeserializerHandler = introspector.findValueDeserializerHandler();
        SerializerInstanceManager serializerInstanceManager = introspector.findSerializerInstanceManager();

        Method stringFactoryMethod = StdKeyDeserializer.class.getDeclaredMethod("stringFactoryKeyDeserializer");
        stringFactoryMethod.setAccessible(true);
        Object stringFactoryKeyDeserializer = stringFactoryMethod.invoke(null);

        Constructor<StringFactoryKeyDeserializer> constructor = StringFactoryKeyDeserializer.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        Object stringFactoryDeserializer = constructor.newInstance(stringFactoryKeyDeserializer);

        KeyDeserializer keyDeserializer = (KeyDeserializer) findValueDeserializerHandler.findValueDeserializer(
                propertyDefinition, annotatedClass, serializerInstanceManager);
        assertEquals(stringFactoryDeserializer, keyDeserializer);
    }

    @Test
    public void testStdKeyDeserializer_forStringCtor() throws IOException, JsonProcessingException {
        Constructor<StringCtorKeyDeserializer> constructor = (Constructor<StringCtorKeyDeserializer>) STRING_CTOR_CONSTRUCTOR.newInstance(String.class);
        KeyDeserializer keyDeserializer = constructor.newInstance(MockDeserializer.class);

        Object key = keyDeserializer.deserializeKey("String", context);
        assertEquals(String.class, key.getClass());
    }

    private static class MockDeserializer extends JsonDeserializer<Object> {
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            return null;
        }
    }
}