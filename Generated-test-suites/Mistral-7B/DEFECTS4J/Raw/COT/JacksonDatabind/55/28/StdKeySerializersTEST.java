package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializerProvider;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider.PropertySerializerMap;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Default;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Dynamic;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.StringKeySerializer;

import org.junit.Before;
import org.junit.Test;

public class StdKeySerializersTest {

    private SerializerProvider provider;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        provider = mock(SerializerProvider.class);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetStdKeySerializer() throws JsonMappingException, IOException {
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(),
                String.class, false);
        assertNotNull(serializer);
        assertNotNull(serializer.getClass().getName());
        serializer.serialize(new Object(), provider.getJsonGenerator(), provider);
    }

    @Test
    public void testGetStdKeySerializerForObjectClass() throws JsonMappingException, IOException {
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(),
                Object.class, false);
        assertNotNull(serializer);
        assertNotNull(serializer.getClass().getName());
        serializer.serialize(new Object(), provider.getJsonGenerator(), provider);
    }

    @Test
    public void testGetStdKeySerializerForDate() throws JsonMappingException, IOException {
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(),
                Date.class, false);
        assertNotNull(serializer);
        assertNotNull(serializer.getClass().getName());
        serializer.serialize(new Date(), provider.getJsonGenerator(), provider);
    }

    @Test
    public void testGetStdKeySerializerForCalendar() throws JsonMappingException, IOException {
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(),
                Calendar.class, false);
        assertNotNull(serializer);
        assertNotNull(serializer.getClass().getName());
        Calendar calendar = Calendar.getInstance();
        serializer.serialize(calendar, provider.getJsonGenerator(), provider);
    }

    @Test
    public void testGetStdKeySerializerForEnum() throws JsonMappingException, IOException {
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(),
                MyEnum.class, false);
        assertNotNull(serializer);
        assertNotNull(serializer.getClass().getName());
        serializer.serialize(MyEnum.VALUE1, provider.getJsonGenerator(), provider);
    }

    @Test(expected = NullPointerException.class)
    public void testGetStdKeySerializerForNullConfig() throws IOException {
        JsonSerializer<Object> serializer = StdKeySerializers
                .getStdKeySerializer(null, String.class, false);
    }

    @Test(expected = NullPointerException.class)
    public void testGetStdKeySerializerForNullRawKeyType() throws IOException {
        JsonSerializer<Object> serializer = StdKeySerializers
                .getStdKeySerializer(objectMapper.getSerializationConfig(), null, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetStdKeySerializerForNullUseDefault() throws IOException {
        JsonSerializer<Object> serializer = StdKeySerializers
                .getStdKeySerializer(objectMapper.getSerializationConfig(), String.class, null);
    }

    @Test
    public void testGetFallbackKeySerializerForString() throws JsonMappingException, IOException {
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(objectMapper.getSerializationConfig(),
                String.class);
        assertNotNull(serializer);
        assertNotNull(serializer.getClass().getName());
        serializer.serialize(new Object(), provider.getJsonGenerator(), provider);
    }

    @Test
    public void testGetFallbackKeySerializerForObject() throws JsonMappingException, IOException {
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(objectMapper.getSerializationConfig(),
                Object.class);
        assertNotNull(serializer);
        assertNotNull(serializer.getClass().getName());
        serializer.serialize(new Object(), provider.getJsonGenerator(), provider);
    }

    @Test
    public void testGetFallbackKeySerializerForEnumWithNoDefault() throws JsonMappingException, IOException {
        SerializationConfig config = objectMapper.getSerializationConfig();
        config.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        JsonSerializer<Object> serializer = StdKeySerializers
                .getFallbackKeySerializer(config, MyEnum.class);
        assertNotNull(serializer);
        assertNotNull(serializer.getClass().getName());
        serializer.serialize(MyEnum.VALUE1, provider.getJsonGenerator(), provider);
    }

    @Test
    public void testGetFallbackKeySerializerForEnumWithDefault() throws JsonMappingException, IOException {
        SerializationConfig config = objectMapper.getSerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers
                .getFallbackKeySerializer(config, MyEnum.class, true);
        assertNotNull(serializer);
        assertNotNull(serializer.getClass().getName());
        serializer.serialize(MyEnum.VALUE1, provider.getJsonGenerator(), provider);
    }

    @Test
    public void testGetFallbackKeySerializerForNullConfig() throws IOException {
        JsonSerializer<Object> serializer = StdKeySerializers
                .getFallbackKeySerializer(null, MyEnum.class);
    }

    @Test
    public void testGetFallbackKeySerializerForNullRawKeyType() throws IOException {
        JsonSerializer<Object> serializer = StdKeySerializers
                .getFallbackKeySerializer(objectMapper.getSerializationConfig(), null);
    }

    @Test
    public void testDefaultSerializerForString() throws JsonMappingException, IOException {
        JsonSerializer<Object> serializer = StdKeySerializers.getDefault();
        assertNotNull(serializer);
        assertNotNull(serializer.getClass().getName());
        serializer.serialize(new Object(), provider.getJsonGenerator(), provider);
    }

    @Test(expected = DeprecationException.class)
    public void testDefaultSerializer() throws JsonMappingException, IOException {
        JsonSerializer<Object> serializer = StdKeySerializers.getDefault();
    }

    @Test
    public void testDynamicSerializerForInt() throws JsonMappingException, IOException {
        int value = 5;
        PropertySerializerMap map = mock(PropertySerializerMap.class);
        when(map.findAndAddKeySerializer(Integer.TYPE, provider, null))
                .thenReturn(new PropertySerializerMap.SerializerAndMapResult(null, map));
        JsonSerializer<Object> serializer = new Dynamic()._findAndAddDynamic(map, Integer.TYPE, provider);
        serializer.serialize(value, provider.getJsonGenerator(), provider);
    }

    @Test
    public void testDynamicSerializerForString() throws JsonMappingException, IOException {
        String value = "test";
        PropertySerializerMap map = mock(PropertySerializerMap.class);
        when(map.findAndAddKeySerializer(String.class, provider, null))
                .thenReturn(new PropertySerializerMap.SerializerAndMapResult(new StringKeySerializer(), map));
        JsonSerializer<Object> serializer = new Dynamic()._findAndAddDynamic(map, String.class, provider);
        serializer.serialize(value, provider.getJsonGenerator(), provider);
    }

    @Test
    public void testDynamicSerializerForEnum() throws JsonMappingException, IOException {
        MyEnum value = MyEnum.VALUE1;
        PropertySerializerMap map = mock(PropertySerializerMap.class);
        when(map.findAndAddKeySerializer(MyEnum.class, provider, null))
                .thenReturn(new PropertySerializerMap.SerializerAndMapResult(new Default(Default.TYPE_ENUM, MyEnum.class), map));
        JsonSerializer<Object> serializer = new Dynamic()._findAndAddDynamic(map, MyEnum.class, provider);
        serializer.serialize(value, provider.getJsonGenerator(), provider);
    }
}

enum MyEnum {
    VALUE1, VALUE2
}