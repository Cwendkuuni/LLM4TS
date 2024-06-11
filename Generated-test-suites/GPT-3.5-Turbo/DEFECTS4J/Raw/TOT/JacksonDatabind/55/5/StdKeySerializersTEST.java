import static org.junit.Assert.*;
import org.junit.Test;

public class StdKeySerializersTest {
    
    @Test
    public void testGetStdKeySerializer() {
        // Typical use case: rawKeyType = String.class
        SerializationConfig config = new SerializationConfig();
        Class<?> rawKeyType = String.class;
        boolean useDefault = true;
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, result);
        
        // Edge case: rawKeyType = null
        rawKeyType = null;
        result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertTrue(result instanceof StdKeySerializers.Dynamic);
        
        // Error scenario: rawKeyType = Integer.class (not supported)
        rawKeyType = Integer.class;
        useDefault = false;
        result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertNull(result);
    }
    
    @Test
    public void testGetFallbackKeySerializer() {
        // Typical use case: rawKeyType = Enum.class
        SerializationConfig config = new SerializationConfig();
        Class<?> rawKeyType = Enum.class;
        JsonSerializer<Object> result = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertTrue(result instanceof StdKeySerializers.Dynamic);
        
        // Error scenario: rawKeyType = Float.class (not supported)
        rawKeyType = Float.class;
        result = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, result);
    }
    
    @Test
    public void testGetDefault() {
        JsonSerializer<Object> result = StdKeySerializers.getDefault();
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, result);
    }
    
    @Test
    public void testDefaultSerialize() {
        // Typical use case: _typeId = TYPE_ENUM
        StdKeySerializers.Default defaultSerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_ENUM, Enum.class);
        JsonGeneratorMock generator = new JsonGeneratorMock();
        SerializerProviderMock provider = new SerializerProviderMock();
        defaultSerializer.serialize(EnumTest.VALUE1, generator, provider);
        assertEquals("VALUE1", generator.getCurrentFieldName());
        
        // Edge case: _typeId = TYPE_DATE
        defaultSerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_DATE, Date.class);
        generator = new JsonGeneratorMock();
        provider = new SerializerProviderMock();
        Date date = new Date();
        defaultSerializer.serialize(date, generator, provider);
        assertEquals(date.toString(), generator.getCurrentFieldName());
        
        // Error scenario: _typeId = TYPE_CLASS (not supported)
        defaultSerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CLASS, Class.class);
        generator = new JsonGeneratorMock();
        provider = new SerializerProviderMock();
        Class<?> clazz = String.class;
        defaultSerializer.serialize(clazz, generator, provider);
        assertEquals(clazz.getName(), generator.getCurrentFieldName());
    }
    
    @Test
    public void testDynamicSerialize() {
        StdKeySerializers.Dynamic dynamicSerializer = new StdKeySerializers.Dynamic();
        JsonGeneratorMock generator = new JsonGeneratorMock();
        SerializerProviderMock provider = new SerializerProviderMock();
        
        // Typical use case: value of type String
        String value = "test";
        dynamicSerializer.serialize(value, generator, provider);
        assertEquals(value, generator.getCurrentFieldName());
        
        // Typical use case: value of type Integer, dynamic serializer is used
        Integer integer = 5;
        generator = new JsonGeneratorMock();
        dynamicSerializer.serialize(integer, generator, provider);
        assertEquals(integer.toString(), generator.getCurrentFieldName());
    }
    
    @Test
    public void testStringKeySerializerSerialize() {
        StdKeySerializers.StringKeySerializer stringKeySerializer = new StdKeySerializers.StringKeySerializer();
        JsonGeneratorMock generator = new JsonGeneratorMock();
        SerializerProviderMock provider = new SerializerProviderMock();
        
        // Typical use case: value of type String
        String value = "test";
        stringKeySerializer.serialize(value, generator, provider);
        assertEquals(value, generator.getCurrentFieldName());
    }
    
    @Test
    public void testGetterMethods() {
        StdKeySerializers.Default defaultSerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_DATE, Date.class);
        assertEquals(Date.class, defaultSerializer.handledType());
        assertFalse(defaultSerializer.isEmpty(null));
        
        StdKeySerializers.Dynamic dynamicSerializer = new StdKeySerializers.Dynamic();
        assertEquals(String.class, dynamicSerializer.handledType());
        assertFalse(dynamicSerializer.isEmpty(null));
        
        StdKeySerializers.StringKeySerializer stringKeySerializer = new StdKeySerializers.StringKeySerializer();
        assertEquals(String.class, stringKeySerializer.handledType());
        assertFalse(stringKeySerializer.isEmpty(null));
    }
    
    private enum EnumTest {
        VALUE1, VALUE2
    }
    
    private class JsonGeneratorMock extends JsonGenerator {
        
        private String currentFieldName;
        
        public String getCurrentFieldName() {
            return currentFieldName;
        }
        
        @Override
        public void writeFieldName(String name) {
            currentFieldName = name;
        }
        
        // Other abstract method implementations...
    }
    
    private class SerializerProviderMock extends SerializerProvider {
        
        @Override
        public boolean isEnabled(SerializationFeature f) {
            return false;
        }
        
        // Other abstract method implementations...
    }
}