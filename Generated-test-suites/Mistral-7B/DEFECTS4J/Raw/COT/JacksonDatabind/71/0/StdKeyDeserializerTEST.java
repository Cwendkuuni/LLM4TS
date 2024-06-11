package com.example.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer.None;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.StdKeyDeserializer.StringKD;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;

public class StdKeyDeserializerTest {

    private DeserializationContext context;
    private ObjectMapper objectMapper;

    @Before
    public void setup() throws Exception {
        context = mock(DeserializationContext.class);
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testDeserializeKey_Boolean() throws JsonMappingException, IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Boolean.class);
        Object result = serializer.deserializeKey("true", context);
        assertEquals(Boolean.TRUE, result);

        result = serializer.deserializeKey("false", context);
        assertEquals(Boolean.FALSE, result);

        result = serializer.deserializeKey("invalid", context);
        assertNull(result);
    }

    @Test
    public void testDeserializeKey_Byte() throws JsonMappingException, IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Byte.class);
        Object result = serializer.deserializeKey("10", context);
        assertEquals((byte) 10, result);

        result = serializer.deserializeKey("-5", context);
        assertEquals((byte) -5, result);

        result = serializer.deserializeKey("256", context);
        assertNull(result);
    }

    @Test
    public void testDeserializeKey_Short() throws JsonMappingException, IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Short.class);
        Object result = serializer.deserializeKey("100", context);
        assertEquals((short) 100, result);

        result = serializer.deserializeKey("-32768", context);
        assertEquals((short) -32768, result);

        result = serializer.deserializeKey("32768", context);
        assertNull(result);
    }

    @Test
    public void testDeserializeKey_Char() throws JsonMappingException, IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Character.class);
        Object result = serializer.deserializeKey("a", context);
        assertEquals('a', result);

        result = serializer.deserializeKey("\\u0061", context);
        assertEquals('a', result);

        result = serializer.deserializeKey(" invalid", context);
        assertNull(result);
    }

    @Test
    public void testDeserializeKey_Int() throws JsonMappingException, IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Integer.class);
        Object result = serializer.deserializeKey("100", context);
        assertEquals(100, result);

        result = serializer.deserializeKey("-32768", context);
        assertEquals(-32768, result);

        result = serializer.deserializeKey("2147483648", context);
        assertNull(result);
    }

    @Test
    public void testDeserializeKey_Long() throws JsonMappingException, IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Long.class);
        Object result = serializer.deserializeKey("100000000000", context);
        assertEquals(100000000000L, result);

        result = serializer.deserializeKey("-9223372036854775808", context);
        assertEquals(-9223372036854775808L, result);

        result = serializer.deserializeKey("9223372036854775807", context);
        assertNull(result);
    }

    @Test
    public void testDeserializeKey_Float() throws JsonMappingException, IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Float.class);
        Object result = serializer.deserializeKey("1.23", context);
        assertEquals(1.23f, result);

        result = serializer.deserializeKey("invalid", context);
        assertNull(result);
    }

    @Test
    public void testDeserializeKey_Double() throws JsonMappingException, IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Double.class);
        Object result = serializer.deserializeKey("1.23", context);
        assertEquals(1.23, result);

        result = serializer.deserializeKey("invalid", context);
        assertNull(result);
    }

    @Test
    public void testDeserializeKey_Locale() throws JsonMappingException, IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Locale.class);
        Object result = serializer.deserializeKey("en_US", context);
        assertEquals(Locale.US, result);

        result = serializer.deserializeKey("invalid", context);
        assertNull(result);
    }

    @Test
    public void testDeserializeKey_Date() throws JsonMappingException, IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Date.class);
        Object result = serializer.deserializeKey("2022-01-01", context);
        assertEquals(new Date(0), result);

        result = serializer.deserializeKey("invalid", context);
        assertNull(result);
    }

    @Test
    public void testDeserializeKey_Calendar() throws JsonMappingException, IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Calendar.class);
        Object result = serializer.deserializeKey("2022-01-01", context);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(0));
        assertEquals(calendar, result);

        result = serializer.deserializeKey("invalid", context);
        assertNull(result);
    }

    @Test
    public void testDeserializeKey_Enum() throws JsonMappingException, IOException {
        Class<?> enumClass = MyEnum.class;
        Constructor<MyEnum> constructor = MyEnum.class.getDeclaredConstructor(String.class);
        constructor.setAccessible(true);

        EnumResolver enumResolver = mock(EnumResolver.class);
        when(enumResolver.getEnumClass()).thenReturn(enumClass);
        when(enumResolver.findEnum(anyString())).thenAnswer(i -> {
            String name = i.getArgument(0);
            try {
                return constructor.newInstance(name);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        StdKeyDeserializer serializer = new StdKeyDeserializer.EnumKD(enumResolver, null);
        Object result = serializer.deserializeKey("MyEnum.VALUE1", context);
        assertEquals(MyEnum.VALUE1, result);

        result = serializer.deserializeKey("invalid", context);
        assertNull(result);
    }

    @Test
    public void testDeserializeKey_URI() throws JsonMappingException, IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URI.class);
        Object result = serializer.deserializeKey("http://example.com", context);
        assertEquals(new URI("http://example.com"), result);

        result = serializer.deserializeKey("invalid", context);
        assertNull(result);
    }

    @Test
    public void testDeserializeKey_URL() throws JsonMappingException, IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URL.class);
        Object result = serializer.deserializeKey("http://example.com", context);
        assertEquals(new URL("http://example.com"), result);

        result = serializer.deserializeKey("invalid", context);
        assertNull(result);
    }

    @Test
    public void testDeserializeKey_String() throws JsonMappingException, IOException {
        StdKeyDeserializer serializer = new StdKeyDeserializer.StringKD(String.class);
        Object result = serializer.deserializeKey("test", context);
        assertEquals("test", result);

        result = serializer.deserializeKey("invalid", context);
        assertNull(result);
    }

    @Test
    public void testDeserializeKey_Object() throws JsonMappingException, IOException {
        StdKeyDeserializer serializer = new StdKeyDeserializer.StringKD(Object.class);
        Object result = serializer.deserializeKey("test", context);
        assertEquals("test", result.toString());

        result = serializer.deserializeKey("invalid", context);
        assertNull(result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeKey_InvalidClass() throws JsonMappingException, IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Integer.class);
        Object result = serializer.deserializeKey("MyCustomClass", context);
    }

    @Test
    public void testDeserializeKey_CustomDeserializer() throws JsonMappingException, IOException {
        class CustomClass {
            private String name;

            public CustomClass(String name) {
                this.name = name;
            }

            public String getName() {
                return name;
            }
        }

        class CustomKeyDeserializer extends StdKeyDeserializer {
            public CustomKeyDeserializer() {
                super(-1, CustomClass.class);
            }

            @Override
            public Object _parse(String key, DeserializationContext ctxt) throws Exception {
                return new CustomClass(key);
            }
        }

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectMapper objectMapperWithCustomDeserializer = new ObjectMapper();
        objectMapperWithCustomDeserializer.registerKeyDeserializer(CustomClass.class, new CustomKeyDeserializer());

        Object result = objectMapperWithCustomDeserializer.readValue("{\"key\":\"Test\"}", CustomClass.class);
        assertEquals("Test", result.getName());

        result = objectMapper.readValue("{\"key\":\"Test\"}", CustomClass.class);
        assertNull(result);
    }
}

class MyEnum {
    @JacksonStdImpl
    public enum MyEnum {
        VALUE1, VALUE2, VALUE3;
    }
}