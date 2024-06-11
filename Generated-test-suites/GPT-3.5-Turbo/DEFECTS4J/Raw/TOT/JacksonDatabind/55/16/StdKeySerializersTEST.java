import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.SerializationConfig;


public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializer() {
        SerializationConfig config = new SerializationConfig();
        
        // Test for rawKeyType as null
        Class<?> rawKeyType = null;
        boolean useDefault = true;
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertNull(result);
        
        // Test for rawKeyType as Object.class
        rawKeyType = Object.class;
        result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertNotNull(result);
        
        // Test for rawKeyType as String.class
        rawKeyType = String.class;
        result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertNotNull(result);
        
        // Test for rawKeyType as primitive type
        rawKeyType = int.class;
        result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertNotNull(result);
        
        // Test for rawKeyType as Number class
        rawKeyType = Integer.class;
        result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertNotNull(result);
        
        // Test for rawKeyType as Class.class
        rawKeyType = Class.class;
        result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertNotNull(result);
        
        // Test for rawKeyType as Date class
        rawKeyType = Date.class;
        result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertNotNull(result);
        
        // Test for rawKeyType as Calendar class
        rawKeyType = Calendar.class;
        result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertNotNull(result);
        
        // Test for rawKeyType as UUID class
        rawKeyType = java.util.UUID.class;
        result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertNotNull(result);
    }

    @Test
    public void testGetFallbackKeySerializer() {
        SerializationConfig config = new SerializationConfig();
        
        // Test for rawKeyType as null
        Class<?> rawKeyType = null;
        JsonSerializer<Object> result = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertNotNull(result);
        
        // Test for rawKeyType as Enum.class
        rawKeyType = Enum.class;
        result = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertNotNull(result);
        
        // Test for rawKeyType as Enum subclass
        rawKeyType = MyEnum.class;
        result = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertNotNull(result);
        
        // Test for rawKeyType as non-Enum class
        rawKeyType = String.class;
        result = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertNotNull(result);
    }
    
    @Test
    public void testDefaultKeySerializer() throws IOException {
        TestJsonGenerator jsonGenerator = new TestJsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        
        // Test for TYPE_DATE
        StdKeySerializers.Default defaultKeySerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_DATE, Date.class);
        defaultKeySerializer.serialize(new Date(), jsonGenerator, provider);
        assertEquals("Expected output for TYPE_DATE", "2021-01-01", jsonGenerator.getOutput());
        jsonGenerator.reset();
        
        // Test for TYPE_CALENDAR
        defaultKeySerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CALENDAR, Calendar.class);
        Calendar calendar = Calendar.getInstance();
        defaultKeySerializer.serialize(calendar, jsonGenerator, provider);
        assertEquals("Expected output for TYPE_CALENDAR", calendar.getTime().toString(), jsonGenerator.getOutput());
        jsonGenerator.reset();
        
        // Test for TYPE_CLASS
        defaultKeySerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CLASS, Class.class);
        defaultKeySerializer.serialize(String.class, jsonGenerator, provider);
        assertEquals("Expected output for TYPE_CLASS", "java.lang.String", jsonGenerator.getOutput());
        jsonGenerator.reset();
        
        // Test for TYPE_ENUM
        MyEnum myEnum = MyEnum.ONE;
        defaultKeySerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_ENUM, myEnum.getClass());
        defaultKeySerializer.serialize(myEnum, jsonGenerator, provider);
        assertEquals("Expected output for TYPE_ENUM", "ONE", jsonGenerator.getOutput());
        jsonGenerator.reset();
        
        // Test for TYPE_TO_STRING
        defaultKeySerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_TO_STRING, String.class);
        defaultKeySerializer.serialize("test", jsonGenerator, provider);
        assertEquals("Expected output for TYPE_TO_STRING", "test", jsonGenerator.getOutput());
        jsonGenerator.reset();   
    }
    
    @Test
    public void testDynamicKeySerializer() throws IOException {
        TestJsonGenerator jsonGenerator = new TestJsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        
        StdKeySerializers.Dynamic dynamicKeySerializer = new StdKeySerializers.Dynamic();
        
        // Test for String key
        dynamicKeySerializer.serialize("key", jsonGenerator, provider);
        assertEquals("Expected output for String key", "key", jsonGenerator.getOutput());
        jsonGenerator.reset();
        
        // Test for Integer key
        dynamicKeySerializer.serialize(123, jsonGenerator, provider);
        assertEquals("Expected output for Integer key", "123", jsonGenerator.getOutput());
        jsonGenerator.reset();
        
    }
    
    @Test
    public void testStringKeySerializer() throws IOException {
        TestJsonGenerator jsonGenerator = new TestJsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        
        StdKeySerializers.StringKeySerializer stringKeySerializer = new StdKeySerializers.StringKeySerializer();
        
        // Test for String key
        stringKeySerializer.serialize("key", jsonGenerator, provider);
        assertEquals("Expected output for String key", "key", jsonGenerator.getOutput());
        jsonGenerator.reset();
    }
    
    @Test
    public void testGetDefault() {
        JsonSerializer<Object> result = StdKeySerializers.getDefault();
        assertNotNull(result);
    }
    
    // Custom enum class
    enum MyEnum {
        ONE, TWO, THREE;
    }

}

class TestJsonGenerator extends JsonGenerator {

    private StringBuilder output;

    public TestJsonGenerator() {
        output = new StringBuilder();
    }

    public String getOutput() {
        return output.toString();
    }

    public void reset() {
        output = new StringBuilder();
    }

    // Other overridden methods

}