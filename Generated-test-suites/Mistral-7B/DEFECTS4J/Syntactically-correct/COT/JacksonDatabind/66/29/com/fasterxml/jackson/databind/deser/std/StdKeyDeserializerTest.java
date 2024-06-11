package com.example; // replace with your test package name

import com.fasterxml.jackson.core.DeserializationContext;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.EnumResolver;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StdKeyDeserializerTest {

    private ObjectMapper objectMapper;
    private StdKeyDeserializer stringKD;
    private StdKeyDeserializer booleanKD;
    private StdKeyDeserializer byteKD;
    private StdKeyDeserializer shortKD;
    private StdKeyDeserializer charKD;
    private StdKeyDeserializer intKD;
    private StdKeyDeserializer longKD;
    private StdKeyDeserializer floatKD;
    private StdKeyDeserializer doubleKD;
    private StdKeyDeserializer localeKD;
    private StdKeyDeserializer dateKD;
    private StdKeyDeserializer calendarKD;
    private StdKeyDeserializer uuidKD;
    private StdKeyDeserializer uriKD;
    private StdKeyDeserializer urlKD;
    private StdKeyDeserializer classKD;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        stringKD = StdKeyDeserializer.forType(String.class);
        booleanKD = StdKeyDeserializer.forType(Boolean.class);
        byteKD = StdKeyDeserializer.forType(Byte.class);
        shortKD = StdKeyDeserializer.forType(Short.class);
        charKD = StdKeyDeserializer.forType(Character.class);
        intKD = StdKeyDeserializer.forType(Integer.class);
        longKD = StdKeyDeserializer.forType(Long.class);
        floatKD = StdKeyDeserializer.forType(Float.class);
        doubleKD = StdKeyDeserializer.forType(Double.class);
        localeKD = StdKeyDeserializer.forType(Locale.class);
        dateKD = StdKeyDeserializer.forType(Date.class);
        calendarKD = StdKeyDeserializer.forType(Calendar.class);
        uuidKD = StdKeyDeserializer.forType(UUID.class);
        uriKD = StdKeyDeserializer.forType(URI.class);
        urlKD = StdKeyDeserializer.forType(URL.class);
        classKD = StdKeyDeserializer.forType(Class.class);
    }

    @Test
    public void testDeserializeKey_String() throws IOException {
        String key = "key";
        String expectedKey = "key";
        Object actualKey = stringKD.deserializeKey(key, mockContext());
        assertEquals(expectedKey, actualKey);
    }

    @Test
    public void testDeserializeKey_Boolean() throws IOException {
        String key = "true";
        Boolean expectedKey = Boolean.TRUE;
        Object actualKey = booleanKD.deserializeKey(key, mockContext());
        assertEquals(expectedKey, actualKey);

        key = "false";
        expectedKey = Boolean.FALSE;
        actualKey = booleanKD.deserializeKey(key, mockContext());
        assertEquals(expectedKey, actualKey);
    }

    @Test
    public void testDeserializeKey_Byte() throws IOException {
        String key = "10";
        Byte expectedKey = Byte.valueOf((byte) 10);
        Object actualKey = byteKD.deserializeKey(key, mockContext());
        assertEquals(expectedKey, actualKey);

        key = "-127";
        expectedKey = Byte.MIN_VALUE;
        actualKey = byteKD.deserializeKey(key, mockContext());
        assertEquals(expectedKey, actualKey);

        key = "128";
        assertNull(byteKD.deserializeKey(key, mockContext()));
    }

    @Test
    public void testDeserializeKey_Short() throws IOException {
        String key = "100";
        Short expectedKey = Short.valueOf((short) 100);
        Object actualKey = shortKD.deserializeKey(key, mockContext());
        assertEquals(expectedKey, actualKey);

        key = "-32768";
        expectedKey = Short.MIN_VALUE;
        actualKey = shortKD.deserializeKey(key, mockContext());
        assertEquals(expectedKey, actualKey);

        key = "32768";
        assertNull(shortKD.deserializeKey(key, mockContext()));
    }

    @Test
    public void testDeserializeKey_Char() throws IOException {
        String key = "A";
        Character expectedKey = Character.valueOf('A');
        Object actualKey = charKD.deserializeKey(key, mockContext());
        assertEquals(expectedKey, actualKey);

        key = "\u0061";
        expectedKey = Character.valueOf('a');
        actualKey = charKD.deserializeKey(key, mockContext());
        assertEquals(expectedKey, actualKey);

        key = "abc";
        assertNull(charKD.deserializeKey(key, mockContext()));
    }

    @Test
    public void testDeserializeKey_Int() throws IOException {
        String key = "1000";
        Integer expectedKey = Integer.valueOf(1000);
        Object actualKey = intKD.deserializeKey(key, mockContext());
        assertEquals(expectedKey, actualKey);
    }

    @Test
    public void testDeserializeKey_Long() throws IOException {
        String key = "1000000";
        Long expectedKey = Long.valueOf(1000000L);
        Object actualKey = longKD.deserializeKey(key, mockContext());
        assertEquals(expectedKey, actualKey);
    }

    @Test
    public void testDeserializeKey_Float() throws IOException {
        String key = "3.14";
        Float expectedKey = Float.valueOf(3.14f);
        Object actualKey = floatKD.deserializeKey(key, mockContext());
        assertEquals(expectedKey, actualKey);
    }

    @Test
    public void testDeserializeKey_Double() throws IOException {
        String key = "3.14159265358979323846";
        Double expectedKey = Double.valueOf(3.14159265358979323846);
        Object actualKey = doubleKD.deserializeKey(key, mockContext());
        assertEquals(expectedKey, actualKey);
    }

    @Test
    public void testDeserializeKey_Locale() throws IOException {
        String key = "en_US";
        Locale expectedKey = Locale.forLanguageTag("en_US");
        Object actualKey = localeKD.deserializeKey(key, mockContext());
        assertEquals(expectedKey, actualKey);
    }

    @Test
    public void testDeserializeKey_Date() throws IOException, ParseException {
        String key = "2023-02-28";
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date expectedKey = dateFormat.parse(key);
        Object actualKey = dateKD.deserializeKey(key, mockContext());
        assertEquals(expectedKey, actualKey);
    }

    @Test
    public void testDeserializeKey_Calendar() throws IOException {
        String key = "2023-02-28";
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date expectedDate = dateFormat.parse(key);
        Calendar expectedCalendar = Calendar.getInstance();
        expectedCalendar.setTime(expectedDate);
        Object actualCalendar = calendarKD.deserializeKey(key, mockContext());
        assertEquals(expectedCalendar.getClass(), actualCalendar.getClass());
        assertEquals(expectedCalendar, actualCalendar);
    }

    @Test
    public void testDeserializeKey_UUID() throws IOException {
        String key = "123e4567-e89b-12d3-a456-426614174000";
        UUID expectedKey = UUID.fromString(key);
        Object actualKey = uuidKD.deserializeKey(key, mockContext());
        assertEquals(expectedKey, actualKey);
    }

    @Test
    public void testDeserializeKey_URI() throws IOException {
        String key = "http://example.com/path?query=param";
        URI expectedKey = new URI(key);
        Object actualKey = uriKD.deserializeKey(key, mockContext());
        assertEquals(expectedKey, actualKey);
    }

    @Test
    public void testDeserializeKey_URL() throws IOException {
        String key = "http://example.com/path?query=param";
        URL expectedKey = new URL(key);
        Object actualKey = urlKD.deserializeKey(key, mockContext());
        assertEquals(expectedKey, actualKey);
    }

    @Test
    public void testDeserializeKey_Class() throws IOException {
        String key = "java.lang.String";
        Class<?> expectedKey = String.class;
        Object actualKey = classKD.deserializeKey(key, mockContext());
        assertEquals(expectedKey, actualKey.getClass());
        assertEquals(expectedKey, actualKey);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeKey_InvalidBoolean() throws IOException {
        String key = "invalid";
        booleanKD.deserializeKey(key, mockContext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeKey_InvalidByte() throws IOException {
        String key = "invalid";
        byteKD.deserializeKey(key, mockContext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeKey_InvalidShort() throws IOException {
        String key = "invalid";
        shortKD.deserializeKey(key, mockContext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeKey_InvalidChar() throws IOException {
        String key = new String(new char[121]);
        charKD.deserializeKey(key, mockContext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeKey_InvalidInt() throws IOException {
        String key = "invalid";
        intKD.deserializeKey(key, mockContext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeKey_InvalidLong() throws IOException {
        String key = "invalid";
        longKD.deserializeKey(key, mockContext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeKey_InvalidFloat() throws IOException {
        String key = "invalid";
        floatKD.deserializeKey(key, mockContext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeKey_InvalidDouble() throws IOException {
        String key = "invalid";
        doubleKD.deserializeKey(key, mockContext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeKey_InvalidLocale() throws IOException {
        String key = "invalid";
        localeKD.deserializeKey(key, mockContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeKey_InvalidDate() throws IOException {
        String key = "invalid";
        dateKD.deserializeKey(key, mockContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeKey_InvalidCalendar() throws IOException {
        String key = "invalid";
        calendarKD.deserializeKey(key, mockContext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeKey_InvalidURI() throws IOException {
        String key = "invalid";
        uriKD.deserializeKey(key, mockContext());
    }

    @Test(expected = MalformedURLException.class)
    public void testDeserializeKey_InvalidURL() throws IOException {
        String key = "invalid";
        urlKD.deserializeKey(key, mockContext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeKey_InvalidClass() throws IOException {
        String key = "invalid";
        classKD.deserializeKey(key, mockContext());
    }

    private DeserializationContext mockContext() {
        BeanDescription beanDesc = mock(BeanDescription.class);
        JavaType type = mock(JavaType.class);
        ObjectMapper mapper = mock(ObjectMapper.class);
        AnnotationIntrospector introspector = mock(AnnotationIntrospector.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        Converter<String, ?> stringConverter = mock(Converter.class);
        EnumResolver enumResolver = mock(EnumResolver.class);

        when(beanDesc.getType()).thenReturn(type);
        when(beanDesc.getBeanClass()).thenReturn(type.getRawClass());
        when(mapper.getAnnotationIntrospector()).thenReturn(introspector);
        when(introspector.findEnumValues(type)).thenReturn(Arrays.asList(enumValues(type)).toArray());
        when(mapper.getTypeFactory()).thenReturn(mapper::getTypeFactory);
        when(mapper.getFactory()).thenReturn(mapper);
        when(mapper.findConverters(beanDesc, type, null)).thenReturn(Collections.singletonList(stringConverter));
        when(introspector.findValueDeserializer(beanDesc, type)).thenReturn(typeDeserializer);
        when(typeDeserializer.findBeanPropertyName()).thenReturn("");
        return mock(DeserializationContext.class);
    }

    private Enum<?>[] enumValues(JavaType type) {
        Class<?> enumClass = type.getRawClass().getComponentType();
        Enum<?>[] values = enumClass.getEnumConstants();
        return values;
    }
}