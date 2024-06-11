import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.JsonSerializer;
import java.util.Map;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.IntegerSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.ShortSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.IntLikeSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.LongSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.FloatSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.DoubleSerializer;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> deserializers = new HashMap<String, JsonSerializer<?>>();
        NumberSerializers.addAll(deserializers);
        
        assertNotNull(deserializers);
        assertEquals(8, deserializers.size());
        assertTrue(deserializers.containsKey(Integer.class.getName()));
        assertTrue(deserializers.containsKey(Integer.TYPE.getName()));
        assertTrue(deserializers.containsKey(Long.class.getName()));
        assertTrue(deserializers.containsKey(Long.TYPE.getName()));
        assertTrue(deserializers.containsKey(Byte.class.getName()));
        assertTrue(deserializers.containsKey(Byte.TYPE.getName()));
        assertTrue(deserializers.containsKey(Short.class.getName()));
        assertTrue(deserializers.containsKey(Short.TYPE.getName()));
    }
    
    @Test
    public void testShortSerializer() {
        ShortSerializer serializer = new ShortSerializer();
        
        assertNotNull(serializer);
        assertEquals(Short.class, serializer.handledType());
    }
    
    @Test
    public void testShortSerializerSerialize() throws Exception {
        ShortSerializer serializer = new ShortSerializer();
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        SerializerProvider provider = new DefaultSerializerProvider();
        serializer.serialize((short) 10, generator, provider);
        generator.close();
        
        assertEquals("10", writer.toString());
    }
    
    @Test
    public void testIntegerSerializer() {
        IntegerSerializer serializer = new IntegerSerializer(Integer.class);
        
        assertNotNull(serializer);
        assertEquals(Integer.class, serializer.handledType());
    }
    
    @Test
    public void testIntegerSerializerSerialize() throws Exception {
        IntegerSerializer serializer = new IntegerSerializer(Integer.class);
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        SerializerProvider provider = new DefaultSerializerProvider();
        serializer.serialize(10, generator, provider);
        generator.close();
        
        assertEquals("10", writer.toString());
    }
    
    @Test
    public void testIntLikeSerializer() {
        IntLikeSerializer serializer = IntLikeSerializer.instance;
        
        assertNotNull(serializer);
        assertEquals(Number.class, serializer.handledType());
    }
    
    @Test
    public void testIntLikeSerializerSerialize() throws Exception {
        IntLikeSerializer serializer = IntLikeSerializer.instance;
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        SerializerProvider provider = new DefaultSerializerProvider();
        serializer.serialize(10, generator, provider);
        generator.close();
        
        assertEquals("10", writer.toString());
    }
    
    @Test
    public void testLongSerializer() {
        LongSerializer serializer = new LongSerializer(Long.class);
        
        assertNotNull(serializer);
        assertEquals(Long.class, serializer.handledType());
    }
    
    @Test
    public void testLongSerializerSerialize() throws Exception {
        LongSerializer serializer = new LongSerializer(Long.class);
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        SerializerProvider provider = new DefaultSerializerProvider();
        serializer.serialize(10L, generator, provider);
        generator.close();
        
        assertEquals("10", writer.toString());
    }
    
    @Test
    public void testFloatSerializer() {
        FloatSerializer serializer = FloatSerializer.instance;
        
        assertNotNull(serializer);
        assertEquals(Float.class, serializer.handledType());
    }
    
    @Test
    public void testFloatSerializerSerialize() throws Exception {
        FloatSerializer serializer = FloatSerializer.instance;
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        SerializerProvider provider = new DefaultSerializerProvider();
        serializer.serialize(10.5f, generator, provider);
        generator.close();
        
        assertEquals("10.5", writer.toString());
    }
    
    @Test
    public void testDoubleSerializer() {
        DoubleSerializer serializer = new DoubleSerializer(Double.class);
        
        assertNotNull(serializer);
        assertEquals(Double.class, serializer.handledType());
    }
    
    @Test
    public void testDoubleSerializerSerialize() throws Exception {
        DoubleSerializer serializer = new DoubleSerializer(Double.class);
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        SerializerProvider provider = new DefaultSerializerProvider();
        serializer.serialize(10.5d, generator, provider);
        generator.close();
        
        assertEquals("10.5", writer.toString());
    }
}