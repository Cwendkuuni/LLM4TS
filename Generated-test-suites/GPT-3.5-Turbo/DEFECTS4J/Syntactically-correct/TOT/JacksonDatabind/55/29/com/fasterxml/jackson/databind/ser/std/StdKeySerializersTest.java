import org.junit.Assert;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializer_WithNullRawKeyType_ReturnsDynamicSerializer() {
        // Arrange
        SerializationConfig configMock = mock(SerializationConfig.class);
        
        // Act
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(configMock, null, false);
        
        // Assert
        Assert.assertTrue(result instanceof StdKeySerializers.Dynamic);
    }
    
    @Test
    public void testGetStdKeySerializer_WithStringRawKeyType_ReturnsDefaultStringSerializer() {
        // Arrange
        SerializationConfig configMock = mock(SerializationConfig.class);
        
        // Act
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(configMock, String.class, false);
        
        // Assert
        Assert.assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, result);
    }
    
    @Test
    public void testGetStdKeySerializer_WithPrimitiveNumberRawKeyType_ReturnsDefaultKeySerializer() {
        // Arrange
        SerializationConfig configMock = mock(SerializationConfig.class);
        
        // Act
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(configMock, int.class, false);
        
        // Assert
        Assert.assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, result);
    }
    
    @Test
    public void testGetStdKeySerializer_WithClassRawKeyType_ReturnsDefaultClassKeySerializer() {
        // Arrange
        SerializationConfig configMock = mock(SerializationConfig.class);
        
        // Act
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(configMock, Class.class, false);
        
        // Assert
        Assert.assertTrue(result instanceof StdKeySerializers.Default);
        StdKeySerializers.Default defaultSerializer = (StdKeySerializers.Default) result;
        Assert.assertEquals(StdKeySerializers.Default.TYPE_CLASS, defaultSerializer._typeId);
    }
    
    @Test
    public void testGetStdKeySerializer_WithDateRawKeyType_ReturnsDefaultDateKeySerializer() {
        // Arrange
        SerializationConfig configMock = mock(SerializationConfig.class);
        
        // Act
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(configMock, Date.class, false);
        
        // Assert
        Assert.assertTrue(result instanceof StdKeySerializers.Default);
        StdKeySerializers.Default defaultSerializer = (StdKeySerializers.Default) result;
        Assert.assertEquals(StdKeySerializers.Default.TYPE_DATE, defaultSerializer._typeId);
    }
    
    @Test
    public void testGetStdKeySerializer_WithCalendarRawKeyType_ReturnsDefaultCalendarKeySerializer() {
        // Arrange
        SerializationConfig configMock = mock(SerializationConfig.class);
        
        // Act
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(configMock, Calendar.class, false);
        
        // Assert
        Assert.assertTrue(result instanceof StdKeySerializers.Default);
        StdKeySerializers.Default defaultSerializer = (StdKeySerializers.Default) result;
        Assert.assertEquals(StdKeySerializers.Default.TYPE_CALENDAR, defaultSerializer._typeId);
    }
    
    @Test
    public void testGetStdKeySerializer_WithUUIDRawKeyTypeAndUseDefault_ReturnsDefaultToStringKeySerializer() {
        // Arrange
        SerializationConfig configMock = mock(SerializationConfig.class);
        
        // Act
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(configMock, java.util.UUID.class, true);
        
        // Assert
        Assert.assertTrue(result instanceof StdKeySerializers.Default);
        StdKeySerializers.Default defaultSerializer = (StdKeySerializers.Default) result;
        Assert.assertEquals(StdKeySerializers.Default.TYPE_TO_STRING, defaultSerializer._typeId);
        Assert.assertEquals(java.util.UUID.class, defaultSerializer.handledType());
    }
    
    @Test
    public void testGetStdKeySerializer_NotUseDefault_ReturnsNull() {
        // Arrange
        SerializationConfig configMock = mock(SerializationConfig.class);
        
        // Act
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(configMock, Object.class, false);
        
        // Assert
        Assert.assertNull(result);
    }
    
    @Test
    public void testGetFallbackKeySerializer_WithNullRawKeyType_ReturnsDefaultKeySerializer() {
        // Arrange
        SerializationConfig configMock = mock(SerializationConfig.class);
        
        // Act
        JsonSerializer<Object> result = StdKeySerializers.getFallbackKeySerializer(configMock, null);
        
        // Assert
        Assert.assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, result);
    }
    
    @Test
    public void testGetFallbackKeySerializer_WithEnumRawKeyType_ReturnsDynamicSerializer() {
        // Arrange
        SerializationConfig configMock = mock(SerializationConfig.class);
        
        // Act
        JsonSerializer<Object> result = StdKeySerializers.getFallbackKeySerializer(configMock, Enum.class);
        
        // Assert
        Assert.assertTrue(result instanceof StdKeySerializers.Dynamic);
    }
    
    @Test
    public void testGetFallbackKeySerializer_WithSpecificEnumRawKeyType_ReturnsDefaultEnumKeySerializer() {
        // Arrange
        SerializationConfig configMock = mock(SerializationConfig.class);
        
        // Act
        JsonSerializer<Object> result = StdKeySerializers.getFallbackKeySerializer(configMock, SomeEnum.class);
        
        // Assert
        Assert.assertTrue(result instanceof StdKeySerializers.Default);
        StdKeySerializers.Default defaultSerializer = (StdKeySerializers.Default) result;
        Assert.assertEquals(StdKeySerializers.Default.TYPE_ENUM, defaultSerializer._typeId);
        Assert.assertEquals(SomeEnum.class, defaultSerializer.handledType());
    }
    
    @Test
    public void testGetFallbackKeySerializer_WithNonEnumRawKeyType_ReturnsDefaultKeySerializer() {
        // Arrange
        SerializationConfig configMock = mock(SerializationConfig.class);
        
        // Act
        JsonSerializer<Object> result = StdKeySerializers.getFallbackKeySerializer(configMock, Object.class);
        
        // Assert
        Assert.assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, result);
    }
    
    @Test
    public void testGetDefault_ReturnsDefaultKeySerializer() {
        // Act
        JsonSerializer<Object> result = StdKeySerializers.getDefault();
        
        // Assert
        Assert.assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, result);
    }
    
    private enum SomeEnum {
        VALUE1, VALUE2, VALUE3;
    }
}