package com.fasterxml.jackson.databind.deser.std;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.core.io.NumberInput;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StdKeyDeserializerTest {

    private StdKeyDeserializer stringKD;
    private StdKeyDeserializer forType;
    private StdKeyDeserializer enumKD;
    private StdKeyDeserializer stringCtorKeyDeserializer;
    private StdKeyDeserializer stringFactoryKeyDeserializer;

    @Mock
    private ObjectMapper mockObjectMapper;

    @Mock
    private DeserializationContext mockDeserializationContext;

    @Before
    public void setUp() throws Exception {
        AnnotatedMethod annotatedMethod = new AnnotatedMethod(getClass().getMethod("annotationTestMethod"));
        EnumResolver enumResolver = new EnumResolver(getClass());
        Constructor<?> constructor = getClass().getConstructor(String.class);
        Method factoryMethod = getClass().getMethod("factoryMethod", String.class);

        stringKD = new StdKeyDeserializer(1, getClass());
        forType = StdKeyDeserializer.forType(getClass());
        enumKD = new EnumKD(enumResolver, annotatedMethod);
        stringCtorKeyDeserializer = new StringCtorKeyDeserializer(constructor);
        stringFactoryKeyDeserializer = new StringFactoryKeyDeserializer(factoryMethod);
    }

    @Test
    public void deserializeKey() throws IOException {
        String key = "testKey";
        Object expected = key;
        Object actual = stringKD.deserializeKey(key, mockDeserializationContext);
        assertEquals(expected, actual);
    }

    @Test
    public void getKeyClass() {
        Class<?> expected = getClass();
        Class<?> actual = stringKD.getKeyClass();
        assertEquals(expected, actual);
    }

    @Test
    public void _parse() throws Exception {
        String key = "testKey";
        Object expected = key;
        Object actual = stringKD._parse(key, mockDeserializationContext);
        assertEquals(expected, actual);
    }

    @Test
    public void _parseInt() throws Exception {
        String key = "1";
        int expected = 1;
        int actual = stringKD._parseInt(key);
        assertEquals(expected, actual);
    }

    @Test
    public void _parseLong() throws Exception {
        String key = "1";
        long expected = 1L;
        long actual = stringKD._parseLong(key);
        assertEquals(expected, actual);
    }

    @Test
    public void _parseDouble() throws Exception {
        String key = "1.0";
        double expected = 1.0;
        double actual = stringKD._parseDouble(key);
        assertEquals(expected, actual, 0.0);
    }

    @Test
    public void deserializeKey_withNullKey() throws IOException {
        String key = null;
        assertNull(stringKD.deserializeKey(key, mockDeserializationContext));
    }

    @Test
    public void deserializeKey_withException() throws IOException {
        String key = "exceptionKey";

        when(mockDeserializationContext.weirdKeyException(Mockito.<Class<?>>any(), Mockito.eq(key), Mockito.anyString()))
                .thenReturn(new JsonMappingException(""));

        assertNull(stringKD.deserializeKey(key, mockDeserializationContext));
    }

    @Test
    public void deserializeKey_withEnumNotFound() throws IOException {
        StdKeyDeserializer mockStdKeyDeserializer = mock(StdKeyDeserializer.class);
        String key = "enumNotFoundKey";

        when(mockStdKeyDeserializer._kind).thenReturn(-1);
        when(mockDeserializationContext.isEnabled(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL)).thenReturn(false);
        when(mockDeserializationContext.weirdKeyException(Mockito.<Class<?>>any(), Mockito.eq(key), Mockito.anyString()))
                .thenReturn(new JsonMappingException(""));

        assertNull(mockStdKeyDeserializer.deserializeKey(key, mockDeserializationContext));
    }

    @Test
    public void deserializeKey_withEnumNotFoundAsNull() throws IOException {
        StdKeyDeserializer mockStdKeyDeserializer = mock(StdKeyDeserializer.class);
        String key = "enumNotFoundKey";

        when(mockStdKeyDeserializer._kind).thenReturn(-1);
        when(mockDeserializationContext.isEnabled(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL)).thenReturn(true);

        assertNull(mockStdKeyDeserializer.deserializeKey(key, mockDeserializationContext));
    }

    public Enum<?> annotationTestMethod() {
        return null;
    }

    public static Enum<?> factoryMethod(String key) {
        return null;
    }
}