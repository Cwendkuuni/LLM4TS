package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertySerializerMap;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.Converter;

public class StdKeySerializersTest {

    private ObjectMapper mapper;

    @Before
    public void setUp() {
        this.mapper = new ObjectMapper();
    }

    @After
    public void tearDown() throws Exception {
        this.mapper = null;
    }

    // Test getStdKeySerializer method

    @Test
    public void testGetStdKeySerializerWithNullRawKeyType() throws Exception {
        SerializationConfig config = new SerializationConfig(new Properties());
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, null, true);
        assertSame(StdKeySerializers.DEFAULT_KEY_SERIALIZER, result);
    }

    @Test
    public void testGetStdKeySerializerWithStringType() throws Exception {
        SerializationConfig config = new SerializationConfig(new Properties());
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, String.class, true);
        assertSame(StdKeySerializers.DEFAULT_STRING_SERIALIZER, result);
    }

    @Test
    public void testGetStdKeySerializerWithNumberType() throws Exception {
        SerializationConfig config = new SerializationConfig(new Properties());
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, int.class, true);
        assertSame(StdKeySerializers.DEFAULT_KEY_SERIALIZER, result);
    }

    @Test
    public void testGetStdKeySerializerWithClassType() throws Exception {
        SerializationConfig config = new SerializationConfig(new Properties());
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, Class.class, true);
        assertSame(StdKeySerializers.DEFAULT, result);
    }

    @Test
    public void testGetStdKeySerializerWithDateType() throws Exception {
        SerializationConfig config = new SerializationConfig(new Properties());
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, Date.class, true);
        assertSame(StdKeySerializers.Default.TYPE_DATE, result.getClass().getField("_typeId").getInt(result));
    }

    @Test
    public void testGetStdKeySerializerWithCalendarType() throws Exception {
        SerializationConfig config = new SerializationConfig(new Properties());
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, Calendar.class, true);
        assertSame(StdKeySerializers.Default.TYPE_CALENDAR, result.getClass().getField("_typeId").getInt(result));
    }

    @Test
    public void testGetStdKeySerializerWithUUIDType() throws Exception {
        SerializationConfig config = new SerializationConfig(new Properties());
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, java.util.UUID.class, true);
        assertSame(StdKeySerializers.Default.TYPE_TO_STRING, result.getClass().getField("_typeId").getInt(result));
    }

    @Test
    public void testGetStdKeySerializerWithUnknownType() throws Exception {
        SerializationConfig config = new SerializationConfig(new Properties());
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, MyClass.class, true);
        assertNull(result);
    }

    @Test
    public void testGetFallbackKeySerializerWithNullRawKeyType() throws Exception {
        SerializationConfig config = new SerializationConfig(new Properties());
        JsonSerializer<Object> result = StdKeySerializers.getFallbackKeySerializer(config, null);
        assertSame(StdKeySerializers.DEFAULT_KEY_SERIALIZER, result);
    }

    @Test
    public void testGetFallbackKeySerializerWithEnumType() throws Exception {
        SerializationConfig config = new SerializationConfig(new Properties());
        JsonSerializer<Object> result = StdKeySerializers.getFallbackKeySerializer(config, Enum.class);
        assertSame(StdKeySerializers.Default.TYPE_ENUM, result.getClass().getField("_typeId").getInt(result));
    }

    @Test
    public void testGetFallbackKeySerializerWithKnownType() throws Exception {
        SerializationConfig config = new SerializationConfig(new Properties());
        JsonSerializer<Object> result = StdKeySerializers.getFallbackKeySerializer(config, String.class);
        assertSame(StdKeySerializers.DEFAULT_STRING_SERIALIZER, result);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetFallbackKeySerializerWithNullConfig() throws Exception {
        JsonSerializer<Object> result = StdKeySerializers.getFallbackKeySerializer(null, String.class);
    }

    // Test getFallbackKeySerializer method deprecated since 2.7
    @Test
    public void testGetDefault() throws Exception {
        JsonSerializer<Object> result = StdKeySerializers.getDefault();
        assertSame(StdKeySerializers.DEFAULT_KEY_SERIALIZER, result);
    }

    // Test Default class

    @Test
    public void testDefaultSerializeDateKey() throws Exception {
        SerializationConfig config = new SerializationConfig(new Properties());
        SerializerProvider provider = new SerializerProvider.Base();
        JsonGenerator gen = new JsonFactory(config).createGenerator(new ByteArrayOutputStream(), new JsonSerializer.DefaultConfig());
        provider.setGenerator(JsonGenerator.class, gen);

        Date date = new Date();
        StdKeySerializers.Default defaultSerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_DATE, Date.class);
        defaultSerializer.serialize(date, provider.getSerializerProvider(), provider);

        String result = gen.getOutputContentAsString();
        String expected = new ObjectMapper().writeValueAsString(new Date(date.getTime()));
        assertSame(expected, result);
    }

    @Test
    public void testDefaultSerializeCalendarKey() throws Exception {
        SerializationConfig config = new SerializationConfig(new Properties());
        SerializerProvider provider = new SerializerProvider.Base();
        JsonGenerator gen = new JsonFactory(config).createGenerator(new ByteArrayOutputStream(), new JsonSerializer.DefaultConfig());
        provider.setGenerator(JsonGenerator.class, gen);

        Calendar calendar = Calendar.getInstance();
        StdKeySerializers.Default defaultSerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CALENDAR, Calendar.class);
        defaultSerializer.serialize(calendar, provider.getSerializerProvider(), provider);

        String result = gen.getOutputContentAsString();
        String expected = new ObjectMapper().writeValueAsString(calendar.getTimeInMillis());
        assertSame(expected, result);
    }

    @Test
    public void testDefaultSerializeClassKey() throws Exception {
        SerializationConfig config = new SerializationConfig(new Properties());
        SerializerProvider provider = new SerializerProvider.Base();
        JsonGenerator gen = new JsonFactory(config).createGenerator(new ByteArrayOutputStream(), new JsonSerializer.DefaultConfig());
        provider.setGenerator(JsonGenerator.class, gen);

        Class<?> clazz = Object.class;
        StdKeySerializers.Default defaultSerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CLASS, clazz);
        defaultSerializer.serialize(clazz, provider.getSerializerProvider(), provider);

        String result = gen.getOutputContentAsString();
        String expected = new ObjectMapper().writeValueAsString(clazz.getName());
        assertSame(expected, result);
    }

    @Test
    public void testDefaultSerializeEnumKey() throws Exception {
        EnumSet<MyEnum> enumSet = EnumSet.allOf(MyEnum.class);
        SerializationConfig config = new SerializationConfig(new Properties());
        SerializerProvider provider = new SerializerProvider.Base();
        JsonGenerator gen = new JsonFactory(config).createGenerator(new ByteArrayOutputStream(), new JsonSerializer.DefaultConfig());
        provider.setGenerator(JsonGenerator.class, gen);

        StdKeySerializers.Default defaultSerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_ENUM, MyEnum.class);
        defaultSerializer.serialize(enumSet, provider.getSerializerProvider(), provider);

        String result = gen.getOutputContentAsString();
        String expected = new ObjectMapper().writeValueAsString(Arrays.toString(MyEnum.values()));
        assertSame(expected, result);
    }

    @Test
    public void testDefaultSerializeToStringKey() throws Exception {
        SerializationConfig config = new SerializationConfig(new Properties());
        SerializerProvider provider = new SerializerProvider.Base();
        JsonGenerator gen = new JsonFactory(config).createGenerator(new ByteArrayOutputStream(), new JsonSerializer.DefaultConfig());
        provider.setGenerator(JsonGenerator.class, gen);

        String stringValue = "test";
        StdKeySerializers.Default defaultSerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_TO_STRING, String.class);
        defaultSerializer.serialize(stringValue, provider.getSerializerProvider(), provider);

        String result = gen.getOutputContentAsString();
        assertSame(stringValue, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDynamicSerializeWithNullType() throws Exception {
        SerializationConfig config = new SerializationConfig(new Properties());
        SerializerProvider provider = new SerializerProvider.Base();
        JsonGenerator gen = new JsonFactory(config).createGenerator(new ByteArrayOutputStream(), new JsonSerializer.DefaultConfig());
        provider.setGenerator(JsonGenerator.class, gen);

        Object obj = new Object();
        StdKeySerializers.Dynamic dynamicSerializer = new StdKeySerializers.Dynamic();
        dynamicSerializer.serialize(obj, provider.getSerializerProvider(), provider);
    }

    @Test(expected = JsonProcessingException.class)
    public void testDynamicSerializeWithUnknownType() throws Exception {
        SerializationConfig config = new SerializationConfig(new Properties());
        SerializerProvider provider = new SerializerProvider.Base();
        JsonGenerator gen = new JsonFactory(config).createGenerator(new ByteArrayOutputStream(), new JsonSerializer.DefaultConfig());
        provider.setGenerator(JsonGenerator.class, gen);

        MyClass myClass = new MyClass();
        StdKeySerializers.Dynamic dynamicSerializer = new StdKeySerializers.Dynamic();
        dynamicSerializer.serialize(myClass, provider.getSerializerProvider(), provider);
    }

    @Test
    public void testDynamicSerializeWithKnownType() throws Exception {
        SerializationConfig config = new SerializationConfig(new Properties());
        SerializerProvider provider = new SerializerProvider.Base();
        JsonGenerator gen = new JsonFactory(config).createGenerator(new ByteArrayOutputStream(), new JsonSerializer.DefaultConfig());
        provider.setGenerator(JsonGenerator.class, gen);

        MyClass myClass = new MyClass();
        PropertySerializerMap dynamicSerializerMap = new PropertySerializerMap();
        TypeSerializer typeSerializer = mapper.getTypeSerializer();
        converter = mapper.findValueSerializer(MyClass.class);
        dynamicSerializerMap.addSerializer(MyClass.class, converter, typeSerializer);
        StdKeySerializers.Dynamic dynamicSerializer = new StdKeySerializers.Dynamic();
        dynamicSerializer._dynamicSerializers = dynamicSerializerMap;
        dynamicSerializer.serialize(myClass, provider.getSerializerProvider(), provider);

        String result = gen.getOutputContentAsString();
        String expected = new ObjectMapper().writeValueAsString(myClass);
        assertSame(expected, result);
    }

    @Test
    public void testStringKeySerializerSerialize() throws Exception {
        SerializationConfig config = new SerializationConfig(new Properties());
        SerializerProvider provider = new SerializerProvider.Base();
        JsonGenerator gen = new JsonFactory(config).createGenerator(new ByteArrayOutputStream(), new JsonSerializer.DefaultConfig());
        provider.setGenerator(JsonGenerator.class, gen);

        String stringValue = "test";
        StdKeySerializers.StringKeySerializer stringKeySerializer = new StdKeySerializers.StringKeySerializer();
        stringKeySerializer.serialize(stringValue, provider.getSerializerProvider(), provider);

        String result = gen.getOutputContentAsString();
        assertSame(stringValue, result);
    }
}