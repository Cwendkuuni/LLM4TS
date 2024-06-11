import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.SerializationConfig;

public class StdKeySerializersTest {
    
    @Mock
    private SerializerProvider mockProvider;
    
    @Mock
    private JsonGenerator mockGenerator;
    
    @Mock
    private SerializationConfig mockConfig;
    
    private StdKeySerializers.Default defaultSerializer;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        defaultSerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_DATE, Date.class);
    }
    
    @Test
    public void testGetStdKeySerializer_WithNullRawKeyTypeAndUseDefault_ReturnsDynamicSerializer() {
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mockConfig, null, true);
        assertEquals(StdKeySerializers.Dynamic.class, serializer.getClass());
    }
    
    @Test
    public void testGetStdKeySerializer_WithStringRawKeyTypeAndUseDefault_ReturnsDefaultStringSerializer() {
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mockConfig, String.class, true);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, serializer);
    }
    
    @Test
    public void testGetStdKeySerializer_WithPrimitiveRawKeyTypeAndUseDefault_ReturnsDefaultKeySerializer() {
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mockConfig, int.class, true);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
    }
    
    @Test
    public void testGetStdKeySerializer_WithClassRawKeyTypeAndUseDefault_ReturnsDefaultClassSerializer() {
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mockConfig, Class.class, true);
        assertEquals(StdKeySerializers.Default.class, serializer.getClass());
        assertEquals(StdKeySerializers.Default.TYPE_CLASS, ((StdKeySerializers.Default) serializer)._typeId);
    }
    
    @Test
    public void testGetStdKeySerializer_WithDateRawKeyTypeAndUseDefault_ReturnsDefaultDateSerializer() {
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mockConfig, Date.class, true);
        assertEquals(StdKeySerializers.Default.class, serializer.getClass());
        assertEquals(StdKeySerializers.Default.TYPE_DATE, ((StdKeySerializers.Default) serializer)._typeId);
    }
    
    @Test
    public void testGetStdKeySerializer_WithCalendarRawKeyTypeAndUseDefault_ReturnsDefaultCalendarSerializer() {
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mockConfig, Calendar.class, true);
        assertEquals(StdKeySerializers.Default.class, serializer.getClass());
        assertEquals(StdKeySerializers.Default.TYPE_CALENDAR, ((StdKeySerializers.Default) serializer)._typeId);
    }
    
    @Test
    public void testGetStdKeySerializer_WithUUIDRawKeyTypeAndUseDefault_ReturnsDefaultUUIDSerializer() {
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mockConfig, java.util.UUID.class, true);
        assertEquals(StdKeySerializers.Default.class, serializer.getClass());
        assertEquals(StdKeySerializers.Default.TYPE_TO_STRING, ((StdKeySerializers.Default) serializer)._typeId);
    }
    
    @Test
    public void testGetStdKeySerializer_WithUnknownRawKeyTypeAndUseDefault_ReturnsNull() {
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mockConfig, Object.class, true);
        assertNull(serializer);
    }
    
    @Test
    public void testGetStdKeySerializer_WithNullRawKeyTypeAndNoUseDefault_ReturnsNull() {
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mockConfig, null, false);
        assertNull(serializer);
    }
    
    @Test
    public void testGetFallbackKeySerializer_WithEnumRawKeyType_ReturnsDynamicSerializer() {
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(mockConfig, Enum.class);
        assertEquals(StdKeySerializers.Dynamic.class, serializer.getClass());
    }
    
    @Test
    public void testGetFallbackKeySerializer_WithEnumClassRawKeyType_ReturnsDefaultEnumSerializer() {
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(mockConfig, EnumClass.class);
        assertEquals(StdKeySerializers.Default.class, serializer.getClass());
        assertEquals(StdKeySerializers.Default.TYPE_ENUM, ((StdKeySerializers.Default) serializer)._typeId);
    }
    
    @Test
    public void testGetFallbackKeySerializer_WithNonEnumRawKeyType_ReturnsDefaultKeySerializer() {
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(mockConfig, Object.class);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
    }
    
    @Test
    public void testSerialize_WithTypeDate_SerializesDateKey() throws IOException {
        Date value = new Date();
        defaultSerializer.serialize(value, mockGenerator, mockProvider);
        
        verify(mockProvider, times(1)).defaultSerializeDateKey(eq(value), eq(mockGenerator));
        verifyNoMoreInteractions(mockProvider);
        verify(mockGenerator, times(0)).writeFieldName(anyString());
        verify(mockGenerator, times(0)).writeFieldName(any(CharSequence.class));
    }
    
    @Test
    public void testSerialize_WithTypeCalendar_SerializesCalendarKey() throws IOException {
        Calendar value = Calendar.getInstance();
        defaultSerializer.serialize(value, mockGenerator, mockProvider);
        
        verify(mockProvider, times(1)).defaultSerializeDateKey(eq(value.getTimeInMillis()), eq(mockGenerator));
        verifyNoMoreInteractions(mockProvider);
        verify(mockGenerator, times(0)).writeFieldName(anyString());
        verify(mockGenerator, times(0)).writeFieldName(any(CharSequence.class));
    }
    
    @Test
    public void testSerialize_WithTypeClass_SerializesClassNameKey() throws IOException {
        Class<?> value = String.class;
        defaultSerializer.serialize(value, mockGenerator, mockProvider);
        
        verify(mockGenerator, times(1)).writeFieldName(eq(value.getName()));
        verifyNoMoreInteractions(mockProvider);
        verify(mockProvider, times(0)).defaultSerializeDateKey(any(Date.class), eq(mockGenerator));
        verify(mockProvider, times(0)).defaultSerializeDateKey(anyLong(), eq(mockGenerator));
    }
    
    @Test
    public void testSerialize_WithTypeEnum_SerializesEnumNameKey() throws IOException {
        EnumClass value = EnumClass.VALUE1;
        
        when(mockProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(false);
        defaultSerializer.serialize(value, mockGenerator, mockProvider);
        
        verify(mockGenerator, times(1)).writeFieldName(eq(value.name()));
        verifyNoMoreInteractions(mockProvider);
        verify(mockProvider, times(0)).defaultSerializeDateKey(any(Date.class), eq(mockGenerator));
        verify(mockProvider, times(0)).defaultSerializeDateKey(anyLong(), eq(mockGenerator));
    }
    
    @Test
    public void testSerialize_WithTypeEnumUseToString_SerializesEnumToStringKey() throws IOException {
        EnumClass value = EnumClass.VALUE1;
        
        when(mockProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(true);
        defaultSerializer.serialize(value, mockGenerator, mockProvider);
        
        verify(mockGenerator, times(1)).writeFieldName(eq(value.toString()));
        verifyNoMoreInteractions(mockProvider);
        verify(mockProvider, times(0)).defaultSerializeDateKey(any(Date.class), eq(mockGenerator));
        verify(mockProvider, times(0)).defaultSerializeDateKey(anyLong(), eq(mockGenerator));
    }
    
    @Test
    public void testSerialize_WithTypeToString_SerializesToStringKey() throws IOException {
        Object value = new Object();
        defaultSerializer.serialize(value, mockGenerator, mockProvider);
        
        verify(mockGenerator, times(1)).writeFieldName(eq(value.toString()));
        verifyNoMoreInteractions(mockProvider);
        verify(mockProvider, times(0)).defaultSerializeDateKey(any(Date.class), eq(mockGenerator));
        verify(mockProvider, times(0)).defaultSerializeDateKey(anyLong(), eq(mockGenerator));
    }

    enum EnumClass {
        VALUE1;
    }
}