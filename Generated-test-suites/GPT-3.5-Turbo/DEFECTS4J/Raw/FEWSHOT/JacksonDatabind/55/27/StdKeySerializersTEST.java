package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class StdKeySerializersTest {
    
    @Test
    public void testGetStdKeySerializer() {
        // Test for rawKeyType is null
        assertNull(StdKeySerializers.getStdKeySerializer(null, null, false));
        
        // Test for rawKeyType is Object.class
        assertTrue(StdKeySerializers.getStdKeySerializer(new SerializationConfig(), Object.class, true) instanceof StdKeySerializers.Dynamic);
        
        // Test for rawKeyType is String.class
        assertTrue(StdKeySerializers.getStdKeySerializer(new SerializationConfig(), String.class, true) instanceof StdKeySerializers.StringKeySerializer);
        
        // Test for rawKeyType is primitive
        assertTrue(StdKeySerializers.getStdKeySerializer(new SerializationConfig(), int.class, true) instanceof StdKeySerializers.Default);
        
        // Test for rawKeyType is Number
        assertTrue(StdKeySerializers.getStdKeySerializer(new SerializationConfig(), Double.class, true) instanceof StdKeySerializers.Default);
        
        // Test for rawKeyType is Class.class
        assertTrue(StdKeySerializers.getStdKeySerializer(new SerializationConfig(), Class.class, true) instanceof StdKeySerializers.Default);
        
        // Test for rawKeyType is Date
        assertTrue(StdKeySerializers.getStdKeySerializer(new SerializationConfig(), Date.class, true) instanceof StdKeySerializers.Default);
        
        // Test for rawKeyType is Calendar
        assertTrue(StdKeySerializers.getStdKeySerializer(new SerializationConfig(), Calendar.class, true) instanceof StdKeySerializers.Default);
        
        // Test for rawKeyType is java.util.UUID
        assertTrue(StdKeySerializers.getStdKeySerializer(new SerializationConfig(), java.util.UUID.class, true) instanceof StdKeySerializers.Default);
        
        // Test for useDefault is false
        assertNull(StdKeySerializers.getStdKeySerializer(new SerializationConfig(), int.class, false));
    }
    
    @Test
    public void testGetFallbackKeySerializer() {
        // Test for rawKeyType is null
        assertNotNull(StdKeySerializers.getFallbackKeySerializer(new SerializationConfig(), null));
        
        // Test for rawKeyType is Enum.class
        assertTrue(StdKeySerializers.getFallbackKeySerializer(new SerializationConfig(), Enum.class) instanceof StdKeySerializers.Dynamic);
        
        // Test for rawKeyType is not Enum
        assertTrue(StdKeySerializers.getFallbackKeySerializer(new SerializationConfig(), Boolean.class) instanceof StdKeySerializers.Default);
    }
    
    @Test
    public void testDefaultSerializeDateKey() throws IOException {
        StdKeySerializers.Default defaultKeySerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_DATE, Date.class);
        Date date = new Date();
        FakeJsonGenerator fakeJsonGenerator = new FakeJsonGenerator();
        FakeSerializerProvider fakeSerializerProvider = new FakeSerializerProvider();
        defaultKeySerializer.serialize(date, fakeJsonGenerator, fakeSerializerProvider);
        assertEquals(date, fakeSerializerProvider.actualDate);
    }
    
    @Test
    public void testDefaultSerializeCalendarKey() throws IOException {
        StdKeySerializers.Default defaultKeySerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CALENDAR, Calendar.class);
        Calendar calendar = Calendar.getInstance();
        FakeJsonGenerator fakeJsonGenerator = new FakeJsonGenerator();
        FakeSerializerProvider fakeSerializerProvider = new FakeSerializerProvider();
        defaultKeySerializer.serialize(calendar, fakeJsonGenerator, fakeSerializerProvider);
        assertEquals(calendar.getTimeInMillis(), fakeSerializerProvider.actualTimestamp);
    }
    
    @Test
    public void testDefaultSerializeClassKey() throws IOException {
        StdKeySerializers.Default defaultKeySerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CLASS, Class.class);
        Class<?> clazz = this.getClass();
        FakeJsonGenerator fakeJsonGenerator = new FakeJsonGenerator();
        FakeSerializerProvider fakeSerializerProvider = new FakeSerializerProvider();
        defaultKeySerializer.serialize(clazz, fakeJsonGenerator, fakeSerializerProvider);
        assertEquals(clazz.getName(), fakeSerializerProvider.actualFieldName);
    }
    
    @Test
    public void testDefaultSerializeEnumKey() throws IOException {
        StdKeySerializers.Default defaultKeySerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_ENUM, Enum.class);
        FakeJsonGenerator fakeJsonGenerator = new FakeJsonGenerator();
        FakeSerializerProvider fakeSerializerProvider = new FakeSerializerProvider();
        FakeEnum fakeEnum = FakeEnum.FAKE_VALUE;
        defaultKeySerializer.serialize(fakeEnum, fakeJsonGenerator, fakeSerializerProvider);
        assertEquals(fakeEnum.name(), fakeSerializerProvider.actualFieldName);
    }
    
    @Test
    public void testDefaultSerializeToStringKey() throws IOException {
        StdKeySerializers.Default defaultKeySerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_TO_STRING, String.class);
        String key = "key";
        FakeJsonGenerator fakeJsonGenerator = new FakeJsonGenerator();
        FakeSerializerProvider fakeSerializerProvider = new FakeSerializerProvider();
        defaultKeySerializer.serialize(key, fakeJsonGenerator, fakeSerializerProvider);
        assertEquals(key, fakeSerializerProvider.actualFieldName);
    }
    
    @Test
    public void testDynamicKeySerialize() throws IOException {
        StdKeySerializers.Dynamic dynamicKeySerializers = new StdKeySerializers.Dynamic();
        FakeJsonGenerator fakeJsonGenerator = new FakeJsonGenerator();
        FakeSerializerProvider fakeSerializerProvider = new FakeSerializerProvider();
        FakeClass fakeClass = new FakeClass();
        dynamicKeySerializers.serialize(fakeClass, fakeJsonGenerator, fakeSerializerProvider);
        assertEquals(fakeClass.toString(), fakeSerializerProvider.actualFieldName);
    }
    
    @Test
    public void testStringKeySerialize() throws IOException {
        String key = "key";
        StdKeySerializers.StringKeySerializer stringKeySerializer = new StdKeySerializers.StringKeySerializer();
        FakeJsonGenerator fakeJsonGenerator = new FakeJsonGenerator();
        FakeSerializerProvider fakeSerializerProvider = new FakeSerializerProvider();
        stringKeySerializer.serialize(key, fakeJsonGenerator, fakeSerializerProvider);
        assertEquals(key, fakeSerializerProvider.actualFieldName);
    }
    
    private enum FakeEnum {
        FAKE_VALUE
    }
    
    private static class FakeClass {
        
    }
    
    private static class FakeJsonGenerator extends JsonGenerator {
        
    }
    
    private static class FakeSerializerProvider extends SerializerProvider {
        
        private String actualFieldName;
        private Date actualDate;
        private long actualTimestamp;
        
        FakeSerializerProvider() {
            super();
        }
        
        @Override
        public void defaultSerializeDateKey(Date date, JsonGenerator jsonGenerator) throws IOException {
            actualDate = date;
        }
        
        @Override
        public void defaultSerializeDateKey(long timestamp, JsonGenerator jsonGenerator) throws IOException {
            actualTimestamp = timestamp;
        }
        
        @Override
        public void writeFieldName(String name) throws IOException {
            actualFieldName = name;
        }
    }
    
}