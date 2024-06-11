package com.example.test;

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
import java.net.URL;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer.NullValueProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.StandardDeserializationProblemHandler;

public class StdKeyDeserializerTest {

    

    @Test
    public void testDeserializeKey_Boolean() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mock(DeserializationContext.class);
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);

        assertEquals(Boolean.TRUE, keyDeserializer.deserializeKey("true", context));
        assertEquals(Boolean.FALSE, keyDeserializer.deserializeKey("false", context));
    }

    @Test
    public void testDeserializeKey_Byte() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mock(DeserializationContext.class);
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Byte.class);

        assertEquals((byte) 1, keyDeserializer.deserializeKey("1", context));
        assertEquals((byte) 127, keyDeserializer.deserializeKey("127", context));
        assertThrows(DeserializationFeature.ReadInvalidNumberException.class, () -> keyDeserializer.deserializeKey("256", context));
    }

    @Test
    public void testDeserializeKey_Short() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mock(DeserializationContext.class);
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Short.class);

        assertEquals((short) 1, keyDeserializer.deserializeKey("1", context));
        assertEquals((short) 32767, keyDeserializer.deserializeKey("32767", context));
        assertThrows(DeserializationFeature.ReadInvalidNumberException.class, () -> keyDeserializer.deserializeKey("32768", context));
    }

    @Test
    public void testDeserializeKey_Char() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mock(DeserializationContext.class);
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Character.class);

        assertEquals('a', keyDeserializer.deserializeKey("a", context));
        assertEquals('\u00FF', keyDeserializer.deserializeKey("\u00FF", context));
        assertThrows(IllegalArgumentException.class, () -> keyDeserializer.deserializeKey("ab", context));
    }

    @Test
    public void testDeserializeKey_Int() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mock(DeserializationContext.class);
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Integer.class);

        assertEquals(1, keyDeserializer.deserializeKey("1", context));
        assertEquals(Integer.MAX_VALUE, keyDeserializer.deserializeKey(String.valueOf(Integer.MAX_VALUE), context));
        assertThrows(DeserializationFeature.ReadInvalidNumberException.class, () -> keyDeserializer.deserializeKey("-1", context));
    }

    @Test
    public void testDeserializeKey_Long() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mock(DeserializationContext.class);
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Long.class);

        assertEquals(1L, keyDeserializer.deserializeKey("1", context));
        assertEquals(Long.MAX_VALUE, keyDeserializer.deserializeKey(String.valueOf(Long.MAX_VALUE), context));
        assertThrows(DeserializationFeature.ReadInvalidNumberException.class, () -> keyDeserializer.deserializeKey("-1", context));
    }

    @Test
    public void testDeserializeKey_Float() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mock(DeserializationContext.class);
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Float.class);

        assertEquals((float) 1.0, keyDeserializer.deserializeKey("1.0", context), 0.001f);
        assertThrows(DeserializationFeature.ReadInvalidNumberException.class, () -> keyDeserializer.deserializeKey("abc", context));
    }

    @Test
    public void testDeserializeKey_Double() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mock(DeserializationContext.class);
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Double.class);

        assertEquals(1.0, keyDeserializer.deserializeKey("1.0", context), 0.001);
        assertThrows(DeserializationFeature.ReadInvalidNumberException.class, () -> keyDeserializer.deserializeKey("abc", context));
    }

    @Test
    public void testDeserializeKey_Locale() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mock(DeserializationContext.class);
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Locale.class);

        assertEquals(Locale.US, keyDeserializer.deserializeKey("en_US", context));
        assertNull(keyDeserializer.deserializeKey("invalidLocale", context));
    }

    @Test
    public void testDeserializeKey_Date() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mock(DeserializationContext.class);
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Date.class);

        assertEquals(new Date(), keyDeserializer.deserializeKey("2022-12-31", context));
        assertNull(keyDeserializer.deserializeKey("invalidDate", context));
    }

    @Test
    public void testDeserializeKey_Calendar() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mock(DeserializationContext.class);
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Calendar.class);

        assertEquals(new GregorianCalendar(), keyDeserializer.deserializeKey("2022-12-31", context));
        assertNull(keyDeserializer.deserializeKey("invalidDate", context));
    }

    @Test
    public void testDeserializeKey_UUID() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mock(DeserializationContext.class);
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(UUID.class);

        assertEquals(UUID.fromString("9b1deb4d-3b14-4a9a-a973-7a2849e03a17"), keyDeserializer.deserializeKey("9b1deb4d-3b14-4a9a-a973-7a2849e03a17", context));
        assertThrows(MalformedURLException.class, () -> keyDeserializer.deserializeKey("http://example.com", context));
    }

    @Test
    public void testDeserializeKey_URI() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mock(DeserializationContext.class);
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URI.class);

        assertEquals(new URI("http://example.com"), keyDeserializer.deserializeKey("http://example.com", context));
        assertThrows(MalformedURLException.class, () -> keyDeserializer.deserializeKey("invalidURI", context));
    }

    @Test
    public void testDeserializeKey_URL() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mock(DeserializationContext.class);
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);

        assertEquals(new URL("http://example.com"), keyDeserializer.deserializeKey("http://example.com", context));
        assertThrows(MalformedURLException.class, () -> keyDeserializer.deserializeKey("invalidURL", context));
    }

    @Test
    public void testDeserializeKey_Class() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mock(DeserializationContext.class);
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);

        assertEquals(String.class, keyDeserializer.deserializeKey("String.class", context));
        assertNull(keyDeserializer.deserializeKey("invalidClass", context));
    }

    @Test
    public void testDeserializeKey_Enum() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mock(DeserializationContext.class);
        AnnotationIntrospector introspector = mock(AnnotationIntrospector.class);
        EnumResolver enumResolver = mock(EnumResolver.class);
        when(introspector.findAnnotation(Any.class, Enum.class)).thenReturn(null);
        when(introspector.findAnnotation(JsonDeserialize.class, Enum.class)).thenReturn(null);
        when(introspector.findAnnotation(JsonValue.class, Enum.class)).thenReturn(null);
        when(introspector.findAnnotation(JsonValue.class, Object.class)).thenReturn(null);
        when(context.getAnnotationIntrospector()).thenReturn(introspector);
        when(enumResolver.getEnumIds()).thenReturn(new String[]{"ONE", "TWO"});

        StdKeyDeserializer enumKeyDeserializer = new StdKeyDeserializer(enumResolver, null);

        assertEquals(EnumValueOne.ONE, enumKeyDeserializer.deserializeKey("ONE", context));
        assertNull(enumKeyDeserializer.deserializeKey("INVALID", context));
    }

    @Test
    public void testDeserializeKey_String() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mock(DeserializationContext.class);
        StdKeyDeserializer stringKeyDeserializer = StdKeyDeserializer.forType(StringKD.class);

        assertEquals("test", stringKeyDeserializer.deserializeKey("test", context));
    }

    @Test
    public void testDeserializeKey_Object() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mock(DeserializationContext.class);
        StdKeyDeserializer objectKeyDeserializer = StdKeyDeserializer.forType(StringKD.class);

        assertEquals("test", objectKeyDeserializer.deserializeKey("test", context));
    }

    @Test
    public void testDeserializeKey_NullValueProvider() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mock(DeserializationContext.class);
        StdKeyDeserializer nullValueProviderKeyDeserializer = new StdKeyDeserializer(-1, Object.class, null);
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);

        when(context.getNullValueProvider()).thenReturn(nullValueProvider);
        when(nullValueProvider.getNullProvider()).thenReturn(null);

        assertEquals(null, nullValueProviderKeyDeserializer.deserializeKey(null, context));
    }

    @Test
    public void testDeserializeKey_TypeDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        StdKeyDeserializer typeKeyDeserializer = new StdKeyDeserializer(-1, Object.class, typeDeserializer);

        when(context.getTypeDeserializer()).thenReturn(typeDeserializer);

        assertEquals("test", typeKeyDeserializer.deserializeKey("test", context));
    }

    @Test
    public void testDeserializeKey_TypeSerializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mock(DeserializationContext.class);
        TypeSerializer typeSerializer = mock(TypeSerializer.class);
        StdKeyDeserializer typeKeyDeserializer = new StdKeyDeserializer(-1, Object.class, null, typeSerializer);

        when(context.getSerializers()).thenReturn(new StandardSerializers());

        assertEquals("test", typeKeyDeserializer.deserializeKey("test", context));
    }

    @Test
    public void testDeserializeKey_EnumToStringResolver() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mock(DeserializationContext.class);
        EnumResolver enumResolver = mock(EnumResolver.class);
        AnnotatedMethod factoryMethod = mock(AnnotatedMethod.class);
        StdKeyDeserializer enumToStringKeyDeserializer = new StdKeyDeserializer(enumResolver, factoryMethod);

        when(context.isEnabled(DeserializationFeature.READ_ENUMS_USING_TO_STRING)).thenReturn(true);
        when(context.getAnnotationIntrospector()).thenReturn(mock(AnnotationIntrospector.class));

        assertEquals(EnumValueOne.ONE, enumToStringKeyDeserializer.deserializeKey("ONE", context));
    }

    @Test
    public void testDeserializeKey_StringConstructor() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mock(DeserializationContext.class);
        Constructor<?> constructor = mock(Constructor.class);
        StdKeyDeserializer stringConstructorKeyDeserializer = new StdKeyDeserializer(constructor);

        assertEquals(new String("test"), stringConstructorKeyDeserializer.deserializeKey("test", context));
    }

    @Test
    public void testDeserializeKey_StringFactoryMethod() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mock(DeserializationContext.class);
        Method factoryMethod = mock(Method.class);
        StdKeyDeserializer stringFactoryMethodKeyDeserializer = new StdKeyDeserializer(factoryMethod);

        assertEquals(new String("test"), stringFactoryMethodKeyDeserializer.deserializeKey("test", context));
    }

    
}