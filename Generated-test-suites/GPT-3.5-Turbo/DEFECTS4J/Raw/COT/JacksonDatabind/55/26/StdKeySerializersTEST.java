import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeySerializersTest {
    
    // Test getStdKeySerializer() method with different input types
    @Test
    public void testGetStdKeySerializer() {
        SerializationConfig config = null;
        
        // Test null input type
        assertNull(StdKeySerializers.getStdKeySerializer(config, null, false));
        
        // Test Object class input type
        assertTrue(StdKeySerializers.getStdKeySerializer(config, Object.class, false) instanceof StdKeySerializers.Dynamic);
        
        // Test String class input type
        assertTrue(StdKeySerializers.getStdKeySerializer(config, String.class, false) instanceof StdKeySerializers.StringKeySerializer);
        
        // Test primitive type input
        assertTrue(StdKeySerializers.getStdKeySerializer(config, int.class, false) instanceof StdKeySerializers.Default);
        
        // Test Number class input type
        assertTrue(StdKeySerializers.getStdKeySerializer(config, Number.class, false) instanceof StdKeySerializers.Default);
        
        // Test Class class input type
        assertTrue(StdKeySerializers.getStdKeySerializer(config, Class.class, false) instanceof StdKeySerializers.Default);
        
        // Test Date class input type
        assertTrue(StdKeySerializers.getStdKeySerializer(config, Date.class, false) instanceof StdKeySerializers.Default);
        
        // Test Calendar class input type
        assertTrue(StdKeySerializers.getStdKeySerializer(config, Calendar.class, false) instanceof StdKeySerializers.Default);
        
        // Test UUID class input type
        assertTrue(StdKeySerializers.getStdKeySerializer(config, java.util.UUID.class, false) instanceof StdKeySerializers.Default);
        
        // Test useDefault = true
        assertNotNull(StdKeySerializers.getStdKeySerializer(config, String.class, true));
    }
    
    // Test getFallbackKeySerializer() method with different input types
    @Test
    public void testGetFallbackKeySerializer() {
        SerializationConfig config = null;
        
        // Test null input type
        assertTrue(StdKeySerializers.getFallbackKeySerializer(config, null) instanceof StdKeySerializers.Default);
        
        // Test Enum class input type
        assertTrue(StdKeySerializers.getFallbackKeySerializer(config, Enum.class) instanceof StdKeySerializers.Dynamic);
        
        // Test Enum sub-class input type
        assertTrue(StdKeySerializers.getFallbackKeySerializer(config, MyEnum.class) instanceof StdKeySerializers.Default);
    }
    
    // Test getDefault() method
    @Test
    public void testGetDefault() {
        assertTrue(StdKeySerializers.getDefault() instanceof StdKeySerializers.Default);
    }
    
    // Test Default class serialize() method with different input values
    @Test
    public void testDefaultSerialize() throws IOException {
        StdKeySerializers.Default serializer1 = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_DATE, Date.class);
        StdKeySerializers.Default serializer2 = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CALENDAR, Calendar.class);
        StdKeySerializers.Default serializer3 = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CLASS, Class.class);
        StdKeySerializers.Default serializer4 = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_ENUM, MyEnum.class);
        StdKeySerializers.Default serializer5 = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_TO_STRING, String.class);
        
        JsonGenerator generator = null;
        SerializerProvider provider = null;
        
        // Test TYPE_DATE
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(outputStream, JsonEncoding.UTF8);
        serializer1.serialize(new Date(), jsonGenerator, provider);
        jsonGenerator.close();
        String result = new String(outputStream.toByteArray(), "UTF-8");
        assertEquals("\"" + new Date().toInstant().toString() + "\"", result);
        
        // Test TYPE_CALENDAR
        outputStream = new ByteArrayOutputStream();
        jsonGenerator = new JsonFactory().createGenerator(outputStream, JsonEncoding.UTF8);
        Calendar calendar = Calendar.getInstance();
        serializer2.serialize(calendar, jsonGenerator, provider);
        jsonGenerator.close();
        result = new String(outputStream.toByteArray(), "UTF-8");
        assertEquals("\"" + calendar.getTimeInMillis() + "\"", result);
        
        // Test TYPE_CLASS
        outputStream = new ByteArrayOutputStream();
        jsonGenerator = new JsonFactory().createGenerator(outputStream, JsonEncoding.UTF8);
        Class<?> cls = String.class;
        serializer3.serialize(cls, jsonGenerator, provider);
        jsonGenerator.close();
        result = new String(outputStream.toByteArray(), "UTF-8");
        assertEquals("\"java.lang.String\"", result);
        
        // Test TYPE_ENUM
        outputStream = new ByteArrayOutputStream();
        jsonGenerator = new JsonFactory().createGenerator(outputStream, JsonEncoding.UTF8);
        MyEnum myEnum = MyEnum.VALUE;
        serializer4.serialize(myEnum, jsonGenerator, provider);
        jsonGenerator.close();
        result = new String(outputStream.toByteArray(), "UTF-8");
        assertEquals("\"VALUE\"", result);
        
        // Test TYPE_TO_STRING
        outputStream = new ByteArrayOutputStream();
        jsonGenerator = new JsonFactory().createGenerator(outputStream, JsonEncoding.UTF8);
        String str = "test";
        serializer5.serialize(str, jsonGenerator, provider);
        jsonGenerator.close();
        result = new String(outputStream.toByteArray(), "UTF-8");
        assertEquals("\"test\"", result);
    }
    
    // Test Dynamic class serialize() method
    @Test
    public void testDynamicSerialize() throws IOException {
        StdKeySerializers.Dynamic serializer = new StdKeySerializers.Dynamic();
        serializer._dynamicSerializers = PropertySerializerMap.emptyForProperties();
        JsonGenerator generator = null;
        SerializerProvider provider = null;
        
        // Test with String value
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(outputStream, JsonEncoding.UTF8);
        String value = "test";
        serializer.serialize(value, jsonGenerator, provider);
        jsonGenerator.close();
        String result = new String(outputStream.toByteArray(), "UTF-8");
        assertEquals("\"test\"", result);
        
        // Test with Enum value
        outputStream = new ByteArrayOutputStream();
        jsonGenerator = new JsonFactory().createGenerator(outputStream, JsonEncoding.UTF8);
        MyEnum myEnum = MyEnum.VALUE;
        serializer.serialize(myEnum, jsonGenerator, provider);
        jsonGenerator.close();
        result = new String(outputStream.toByteArray(), "UTF-8");
        assertEquals("\"VALUE\"", result);
    }
    
    // Test StringKeySerializer class serialize() method
    @Test
    public void testStringKeySerialize() throws IOException {
        StdKeySerializers.StringKeySerializer serializer = new StdKeySerializers.StringKeySerializer();
        JsonGenerator generator = null;
        SerializerProvider provider = null;
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(outputStream, JsonEncoding.UTF8);
        String value = "test";
        serializer.serialize(value, jsonGenerator, provider);
        jsonGenerator.close();
        String result = new String(outputStream.toByteArray(), "UTF-8");
        assertEquals("\"test\"", result);
    }
    
    // Enum for testing
    enum MyEnum {
        VALUE
    }
}