
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Default;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Dynamic;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.StringKeySerializer;

public class StdKeySerializersTest {
    
    private SerializationConfig mockConfig = mock(SerializationConfig.class);
    
    @Test
    public void testGetStdKeySerializerWithNullValue() {
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(mockConfig, null, false);
        assertEquals(Dynamic.class, result.getClass());
    }
    
    @Test
    public void testGetStdKeySerializerWithStringType() {
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(mockConfig, String.class, false);
        assertEquals(StringKeySerializer.class, result.getClass());
    }
    
    @Test
    public void testGetStdKeySerializerWithPrimitiveType() {
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(mockConfig, int.class, false);
        assertEquals(Default.class, result.getClass());
    }
    
    @Test
    public void testGetStdKeySerializerWithNumberType() {
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(mockConfig, Integer.class, false);
        assertEquals(Default.class, result.getClass());
    }
    
    @Test
    public void testGetStdKeySerializerWithClassType() {
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(mockConfig, Class.class, false);
        assertEquals(Default.class, result.getClass());
    }
    
    @Test
    public void testGetStdKeySerializerWithDateType() {
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(mockConfig, Date.class, false);
        assertEquals(Default.class, result.getClass());
    }
    
    @Test
    public void testGetStdKeySerializerWithCalendarType() {
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(mockConfig, Calendar.class, false);
        assertEquals(Default.class, result.getClass());
    }
    
    @Test
    public void testGetStdKeySerializerWithUUIDType() {
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(mockConfig, java.util.UUID.class, false);
        assertEquals(Default.class, result.getClass());
    }
    
    @Test
    public void testGetStdKeySerializerWithUseDefaultTrue() {
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(mockConfig, Integer.class, true);
        assertEquals(Default.class, result.getClass());
    }
    
    @Test
    public void testGetStdKeySerializerWithUseDefaultFalse() {
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(mockConfig, Integer.class, false);
        assertNull(result);
    }
    
    @Test
    public void testGetFallbackKeySerializerWithEnumType() {
        JsonSerializer<Object> result = StdKeySerializers.getFallbackKeySerializer(mockConfig, Enum.class);
        assertEquals(Dynamic.class, result.getClass());
    }
    
    @Test
    public void testGetFallbackKeySerializerWithNonEnumType() {
        JsonSerializer<Object> result = StdKeySerializers.getFallbackKeySerializer(mockConfig, Integer.class);
        assertEquals(Default.class, result.getClass());
    }
    
    @Test
    public void testDefaultSerializeDateKey() throws Exception {
        JsonGenerator mockGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider mockProvider = Mockito.mock(SerializerProvider.class);
        Default defaultSerializer = new Default(Default.TYPE_DATE, Date.class);
        
        Date date = new Date();
        
        defaultSerializer.serialize(date, mockGenerator, mockProvider);
        
        verify(mockProvider).defaultSerializeDateKey(date, mockGenerator);
    }
    
    @Test
    public void testDefaultSerializeDateKeyWithCalendar() throws Exception {
        JsonGenerator mockGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider mockProvider = Mockito.mock(SerializerProvider.class);
        Default defaultSerializer = new Default(Default.TYPE_CALENDAR, Calendar.class);
        
        Calendar calendar = Calendar.getInstance();
        
        defaultSerializer.serialize(calendar, mockGenerator, mockProvider);
        
        verify(mockProvider).defaultSerializeDateKey(calendar.getTimeInMillis(), mockGenerator);
    }
    
    @Test
    public void testDefaultSerializeDateKeyWithClass() throws Exception {
        JsonGenerator mockGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider mockProvider = Mockito.mock(SerializerProvider.class);
        Default defaultSerializer = new Default(Default.TYPE_CLASS, Class.class);
        
        Class<?> cls = String.class;
        
        defaultSerializer.serialize(cls, mockGenerator, mockProvider);
        
        verify(mockGenerator).writeFieldName(cls.getName());
    }
    
    @Test
    public void testDefaultSerializeDateKeyWithEnumUsingToString() throws Exception {
        JsonGenerator mockGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider mockProvider = Mockito.mock(SerializerProvider.class);
        Default defaultSerializer = new Default(Default.TYPE_ENUM, Enum.class);
        
        Enum<?> enumValue = Mockito.mock(Enum.class);
        when(enumValue.toString()).thenReturn("EnumValue");
        when(mockProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(true);
        
        defaultSerializer.serialize(enumValue, mockGenerator, mockProvider);
        
        verify(mockGenerator).writeFieldName("EnumValue");
    }
    
    @Test
    public void testDefaultSerializeDateKeyWithEnumUsingName() throws Exception {
        JsonGenerator mockGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider mockProvider = Mockito.mock(SerializerProvider.class);
        Default defaultSerializer = new Default(Default.TYPE_ENUM, Enum.class);
        
        Enum<?> enumValue = Mockito.mock(Enum.class);
        when(enumValue.name()).thenReturn("ENUM_VALUE");
        when(mockProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(false);
        
        defaultSerializer.serialize(enumValue, mockGenerator, mockProvider);
        
        verify(mockGenerator).writeFieldName("ENUM_VALUE");
    }
    
    @Test
    public void testDefaultSerializeDateKeyWithToString() throws Exception {
        JsonGenerator mockGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider mockProvider = Mockito.mock(SerializerProvider.class);
        Default defaultSerializer = new Default(Default.TYPE_TO_STRING, Object.class);
        
        Object object = 12345;
        
        defaultSerializer.serialize(object, mockGenerator, mockProvider);
        
        verify(mockGenerator).writeFieldName("12345");
    }
    
    @Test
    public void testStringKeySerializer() throws Exception {
        JsonGenerator mockGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider mockProvider = Mockito.mock(SerializerProvider.class);
        StringKeySerializer stringKeySerializer = new StringKeySerializer();
        
        String key = "keyValue";
        
        stringKeySerializer.serialize(key, mockGenerator, mockProvider);
        
        verify(mockGenerator).writeFieldName(key);
    }
    
    @Test
    public void testDynamicSerialize() throws Exception {
        JsonGenerator mockGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider mockProvider = Mockito.mock(SerializerProvider.class);
        Dynamic dynamicSerializer = new Dynamic();
        dynamicSerializer._dynamicSerializers = Mockito.mock(PropertySerializerMap.class);
        
        Object value = new Object();
        Class<?> valueClass = value.getClass();
        JsonSerializer<Object> mockSerializer = Mockito.mock(JsonSerializer.class);
        when(dynamicSerializer._dynamicSerializers.serializerFor(valueClass)).thenReturn(mockSerializer);
        
        dynamicSerializer.serialize(value, mockGenerator, mockProvider);
        
        verify(mockSerializer).serialize(value, mockGenerator, mockProvider);
    }
    
    @Test
    public void testDynamicSerializeWithSerializerNotFound() throws Exception {
        JsonGenerator mockGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider mockProvider = Mockito.mock(SerializerProvider.class);
        Dynamic dynamicSerializer = new Dynamic();
        dynamicSerializer._dynamicSerializers = Mockito.mock(PropertySerializerMap.class);
        
        Object value = new Object();
        Class<?> valueClass = value.getClass();
        when(dynamicSerializer._dynamicSerializers.serializerFor(valueClass)).thenReturn(null);
        
        dynamicSerializer.serialize(value, mockGenerator, mockProvider);
        
        verify(mockGenerator, never()).writeFieldName(anyString());
    }
    
    @Test
    public void testDynamicFindAndAddDynamic() throws Exception {
        Dynamic dynamicSerializer = new Dynamic();
        dynamicSerializer._dynamicSerializers = Mockito.mock(PropertySerializerMap.class);
        
        PropertySerializerMap.SerializerAndMapResult mockResult =
                Mockito.mock(PropertySerializerMap.SerializerAndMapResult.class);
        when(mockResult.map).thenReturn(dynamicSerializer._dynamicSerializers);
        when(mockResult.serializer).thenReturn(Mockito.mock(JsonSerializer.class));
        SerializerProvider mockProvider = Mockito.mock(SerializerProvider.class);
        when(dynamicSerializer._dynamicSerializers.findAndAddKeySerializer(any(Class.class), any(SerializerProvider.class), any())).thenReturn(mockResult);
        
        dynamicSerializer._findAndAddDynamic(dynamicSerializer._dynamicSerializers, Object.class, mockProvider);
        
        verify(dynamicSerializer._dynamicSerializers).findAndAddKeySerializer(Object.class, mockProvider, null);
        verify(mockProvider, never()).defaultSerializeDateKey(anyLong(), any(JsonGenerator.class));
        verify(mockProvider, never()).defaultSerializeDateKey(any(Date.class), any(JsonGenerator.class));
    }
    
    @SuppressWarnings("deprecation")
    @Test
    public void testGetDefault() {
        JsonSerializer<Object> result = StdKeySerializers.getDefault();
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, result);
    }
}
