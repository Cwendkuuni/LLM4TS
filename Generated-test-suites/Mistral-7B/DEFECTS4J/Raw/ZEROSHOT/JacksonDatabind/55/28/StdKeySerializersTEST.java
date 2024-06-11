package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapperFactory;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class StdKeySerializersTest {

    private ObjectMapper mapper;

    @BeforeMethod
    public void setup() {
        mapper = JsonMapperFactory.getMapper();
    }

    @Test
    public void testDefaultKeySerializer() throws IOException {
        JsonSerializer<Object> serializer = StdKeySerializers.getDefaultKeySerializer(null, null, true);
        Object value = new Object();
        String expected = "value"; // replace with actual value's toString()
        JsonGenerator generator = mapper.getFactory().createGenerator();
        serializer.serialize(value, generator, mapper.getSerializerProvider());
        Assert.assertEquals(generator.getOutputShape().getName(), expected);
    }

    @Test
    public void testGetStringSerializer() throws IOException {
        JsonSerializer<Object> serializer = StdKeySerializers.getDefaultStringSerializer();
        Object value = "stringValue";
        String expected = "stringValue";
        JsonGenerator generator = mapper.getFactory().createGenerator();
        serializer.serialize(value, generator, mapper.getSerializerProvider());
        Assert.assertEquals(generator.getOutputShape().getName(), expected);
    }

    @Test
    public void testGetStdKeySerializerForDate() throws IOException {
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(),
                Date.class, true);
        Object value = new Date();
        String expected = "\"{" + value.getClass().getName() + ":\"" + value + "\"" + '}';
        JsonGenerator generator = mapper.getFactory().createGenerator();
        serializer.serialize(value, generator, mapper.getSerializerProvider());
        Assert.assertEquals(generator.getOutputShape().getName(), expected);
    }

    @Test
    public void testGetStdKeySerializerForCalendar() throws IOException {
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(),
                Calendar.class, true);
        Object value = new Calendar(2022, 1, 1);
        long timeInMillis = ((Calendar) value).getTimeInMillis();
        String expected = "\"{" + value.getClass().getName() + ":\"" + timeInMillis + "\"" + '}';
        JsonGenerator generator = mapper.getFactory().createGenerator();
        serializer.serialize(value, generator, mapper.getSerializerProvider());
        Assert.assertEquals(generator.getOutputShape().getName(), expected);
    }

    @Test
    public void testGetStdKeySerializerForEnum() throws IOException {
        enum MyEnum {
            VALUE1, VALUE2, VALUE3
        }

        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(),
                MyEnum.class, true);
        Object value = MyEnum.VALUE1;
        String expected = "\"" + MyEnum.VALUE1.name() + '}';
        JsonGenerator generator = mapper.getFactory().createGenerator();
        serializer.serialize(value, generator, mapper.getSerializerProvider());
        Assert.assertEquals(generator.getOutputShape().getName(), expected);
    }

    @Test
    public void testGetStdKeySerializerForClass() throws IOException {
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(),
                String.class.getClass(), true);
        Object value = String.class;
        String expected = "\"" + value.getClass().getName() + '}';
        JsonGenerator generator = mapper.getFactory().createGenerator();
        serializer.serialize(value, generator, mapper.getSerializerProvider());
        Assert.assertEquals(generator.getOutputShape().getName(), expected);
    }

    @Test
    public void testGetStdKeySerializerForDynamic() throws IOException {
        JsonSerializer<Object> serializer = StdKeySerializers.getDefaultKeySerializer(mapper.getSerializationConfig(),
                Integer.class, true);
        Object value = 123;
        JsonGenerator generator = mapper.getFactory().createGenerator();
        serializer.serialize(value, generator, mapper.getSerializerProvider());
        String actual = generator.getOutputShape().getName();
        JsonSerializer<Object> serializerForInteger = mapper.getSerializerProvider().findValueSerializer(Integer.class);
        serializerForInteger.serialize(value, generator, mapper.getSerializerProvider());
        String expected = serializerForInteger.getSerializerProperties().getSerializationConfig().getFactory()
                .getValueSerializerBuilderInstance().buildValueSerializer(Integer.class).getSerializerByName()
                .getName();
        Assert.assertEquals(actual, expected);
    }
}