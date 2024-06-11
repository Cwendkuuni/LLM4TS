package com.example.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StdKeyDeserializerTest {

    @Mock
    private DeserializationContext context;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private AnnotatedClass annotatedClass;
    @Mock
    private AnnotatedMethod annotatedMethod;
    @Mock
    private EnumResolver enumResolver;
    @Mock
    private JsonDeserializer<Object> jsonDeserializer;

    @InjectMocks
    private StdKeyDeserializer stdKeyDeserializer;

    @Before
    public void setUp() {
        when(context.getParser()).thenReturn(mock(JsonParser.class));
        when(context.getParser().getCurrentName()).thenReturn("testKey");
        when(context.isEnabled(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL)).thenReturn(true);
        when(context.isEnabled(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE)).thenReturn(false);
        when(context.getAnnotationIntrospector()).thenReturn(mock(com.fasterxml.jackson.databind.AnnotationIntrospector.class));
        when(context.findClass(anyString())).thenAnswer(invocation -> invocation.getArgument(0));
        when(context.getConfig()).thenReturn(mock(com.fasterxml.jackson.databind.JsonConfiguration.class));
    }

    @Test
    public void testForType() throws Exception {
        assertEquals(StdKeyDeserializer.class, stdKeyDeserializer.getClass());
        assertEquals(String.class, stdKeyDeserializer.forType(String.class).getKeyClass());
        assertEquals(Integer.class, stdKeyDeserializer.forType(Integer.class).getKeyClass());
        assertEquals(Date.class, stdKeyDeserializer.forType(Date.class).getKeyClass());
        assertEquals(null, stdKeyDeserializer.forType(Float.class));
    }

    @Test
    public void testDeserializeKey() throws Exception {
        when(annotatedClass.getRawClass()).thenReturn(String.class);
        when(context.findClass(anyString())).thenAnswer(invocation -> invocation.getArgument(0));

        assertEquals("testKey", stdKeyDeserializer.deserializeKey("testKey", context));

        when(annotatedClass.getRawClass()).thenReturn(Boolean.class);
        when(context.handleWeirdKey(any(Class.class), anyString(), anyString())).thenReturn(Boolean.TRUE);
        assertEquals(Boolean.TRUE, stdKeyDeserializer.deserializeKey("true", context));
        assertEquals(Boolean.FALSE, stdKeyDeserializer.deserializeKey("false", context));

        when(annotatedClass.getRawClass()).thenReturn(Byte.class);
        byte[] byteArray = {(byte) 0x7F};
        when(context.getConfig().getBase64Variant()).thenReturn(new com.fasterxml.jackson.core.JsonFactory.ValueDecodingBase64Variant());
        when(context.getConfig().getBase64Variant().decode(anyString())).thenReturn(byteArray);
        assertEquals(Byte.valueOf((byte) 0x7F), stdKeyDeserializer.deserializeKey("MTIzNDU2Nzg5QGdtYWlsLmNvbS5hdXRo", context));

        when(annotatedClass.getRawClass()).thenReturn(Short.class);
        short value = 0x7FFF;
        assertEquals(Short.valueOf(value), stdKeyDeserializer.deserializeKey("32767", context));
        assertEquals(stdKeyDeserializer.deserializeKey("32768", context), context.handleWeirdKey(Short.class, "32768", "value cannot be represented as 16-bit value"));

        when(annotatedClass.getRawClass()).thenReturn(Character.class);
        assertEquals(Character.valueOf('A'), stdKeyDeserializer.deserializeKey("A", context));
        assertEquals(stdKeyDeserializer.deserializeKey("z", context), context.handleWeirdKey(Character.class, "z", "can only convert 1-character Strings"));

        when(annotatedClass.getRawClass()).thenReturn(Integer.class);
        assertEquals(Integer.parseInt("123"), stdKeyDeserializer.deserializeKey("123", context));

        when(annotatedClass.getRawClass()).thenReturn(Long.class);
        long valueLong = Long.parseLong("9223372036854775807");
        assertEquals(Long.valueOf(valueLong), stdKeyDeserializer.deserializeKey("9223372036854775807", context));

        when(annotatedClass.getRawClass()).thenReturn(Float.class);
        assertEquals(Float.valueOf(1.0f), stdKeyDeserializer.deserializeKey("1.0", context));

        when(annotatedClass.getRawClass()).thenReturn(Double.class);
        assertEquals(Double.valueOf(1.0), stdKeyDeserializer.deserializeKey("1.0", context));

        when(annotatedClass.getRawClass()).thenReturn(URI.class);
        URI uri = new URI("http://example.com");
        when(context.createObjectInstance(URI.class)).thenReturn(uri);
        assertEquals(uri, stdKeyDeserializer.deserializeKey("http://example.com", context));

        when(annotatedClass.getRawClass()).thenReturn(URL.class);
        URL url = new URL("http://example.com");
        when(context.createObjectInstance(URL.class)).thenReturn(url);
        assertEquals(url, stdKeyDeserializer.deserializeKey("http://example.com", context));

        when(annotatedClass.getRawClass()).thenReturn(Class.class);
        assertEquals(String.class, stdKeyDeserializer.deserializeKey("java.lang.String", context));

        when(annotatedClass.getRawClass()).thenReturn(Locale.class);
        when(context.getAnnotationIntrospector().findSerializableValue("value")).thenReturn(annotatedMethod);
        when(annotatedMethod.getDeclaringClass()).thenReturn(Locale.class);
        when(annotatedMethod.getName()).thenReturn("value");
        when(enumResolver.findDeserializer(any(Class.class))).thenReturn(jsonDeserializer);
        assertEquals(jsonDeserializer, stdKeyDeserializer.deserializeKey("en_US", context));

        when(annotatedClass.getRawClass()).thenReturn(Currency.class);
        when(context.getAnnotationIntrospector().findSerializableValue("value")).thenReturn(annotatedMethod);
        when(annotatedMethod.getDeclaringClass()).thenReturn(Currency.class);
        when(annotatedMethod.getName()).thenReturn("value");
        assertEquals(jsonDeserializer, stdKeyDeserializer.deserializeKey("USD", context));

        when(annotatedClass.getRawClass()).thenReturn(byte[].class);
        assertEquals(new byte[]{}, stdKeyDeserializer.deserializeKey("", context));

        when(annotatedClass.getRawClass()).thenReturn(Enum.class);
        assertEquals(MyEnum.VALUE_1, stdKeyDeserializer.deserializeKey("VALUE_1", context));
        assertEquals(MyEnum.VALUE_2, stdKeyDeserializer.deserializeKey("VALUE_2", context));
        assertEquals(MyEnum.VALUE_3, stdKeyDeserializer.deserializeKey("VALUE_3", context));
        assertEquals(context.handleWeirdKey(MyEnum.class, "INVALID_VALUE", "not one of values excepted for Enum class: [VALUE_1, VALUE_2, VALUE_3]"), stdKeyDeserializer.deserializeKey("INVALID_VALUE", context));

        when(annotatedClass.getRawClass()).thenReturn(MyEnum[].class);
        MyEnum[] values = MyEnum.values();
        assertEquals(values, stdKeyDeserializer.deserializeKey(Arrays.toString(values), context));

        when(annotatedClass.getRawClass()).thenReturn(MyEnumSet.class);
        MyEnumSet enumSet = EnumSet.allOf(MyEnum.class);
        assertEquals(enumSet, stdKeyDeserializer.deserializeKey(enumSet.toString(), context));

        when(annotatedClass.getRawClass()).thenReturn(MyEnumMap.class);
        MyEnumMap enumMap = new MyEnumMap();
        when(context.handleWeirdKey(MyEnumMap.class, "", "not a valid representation")).thenReturn(enumMap);
        assertEquals(enumMap, stdKeyDeserializer.deserializeKey("", context));

        when(annotatedClass.getRawClass()).thenReturn(MyEnumMultimap.class);
        MyEnumMultimap multimap = new MyEnumMultimap();
        when(context.handleWeirdKey(MyEnumMultimap.class, "", "not a valid representation")).thenReturn(multimap);
        assertEquals(multimap, stdKeyDeserializer.deserializeKey("", context));
    }

    @Test
    public void testGetKeyClass() throws Exception {
        assertEquals(String.class, stdKeyDeserializer.forType(String.class).getKeyClass());
        assertEquals(Integer.class, stdKeyDeserializer.forType(Integer.class).getKeyClass());
        assertEquals(Date.class, stdKeyDeserializer.forType(Date.class).getKeyClass());
        assertEquals(null, stdKeyDeserializer.forType(Float.class).getKeyClass());
    }

    static class MyEnum {
        public static final MyEnum VALUE_1 = new MyEnum("VALUE_1");
        public static final MyEnum VALUE_2 = new MyEnum("VALUE_2");
        public static final MyEnum VALUE_3 = new MyEnum("VALUE_3");

        private final String value;

        private MyEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    static class MyEnumMap extends HashMap<MyEnum, Object> {
    }

    static class MyEnumMultimap extends ArrayList<Map.Entry<MyEnum, Object>> {
    }
}