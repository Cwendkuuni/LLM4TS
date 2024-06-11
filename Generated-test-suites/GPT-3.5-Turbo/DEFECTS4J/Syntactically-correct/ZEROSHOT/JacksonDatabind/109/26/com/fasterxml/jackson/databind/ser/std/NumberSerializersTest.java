import org.junit.Test;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ser.std.*;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class NumberSerializersTest {
    
    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        
        NumberSerializers.addAll(allDeserializers);
        
        assertNotNull(allDeserializers.get(Integer.class.getName()));
        assertNotNull(allDeserializers.get(Integer.TYPE.getName()));
        assertNotNull(allDeserializers.get(Long.class.getName()));
        assertNotNull(allDeserializers.get(Long.TYPE.getName()));
        assertNotNull(allDeserializers.get(Byte.class.getName()));
        assertNotNull(allDeserializers.get(Byte.TYPE.getName()));
        assertNotNull(allDeserializers.get(Short.class.getName()));
        assertNotNull(allDeserializers.get(Short.TYPE.getName()));
        assertNotNull(allDeserializers.get(Double.class.getName()));
        assertNotNull(allDeserializers.get(Double.TYPE.getName()));
        assertNotNull(allDeserializers.get(Float.class.getName()));
        assertNotNull(allDeserializers.get(Float.TYPE.getName()));
    }
    
    @Test
    public void testShortSerializer() {
        ShortSerializer serializer = new ShortSerializer();
        
        assertNotNull(serializer);
        assertEquals(Short.class, serializer.handledType());
        assertEquals(JsonParser.NumberType.INT, serializer._numberType);
        assertEquals("number", serializer._schemaType);
        
        // TODO: Implement further tests for ShortSerializer
    }
    
    @Test
    public void testIntegerSerializer() {
        IntegerSerializer serializer = new IntegerSerializer(Integer.class);
        
        assertNotNull(serializer);
        assertEquals(Integer.class, serializer.handledType());
        assertEquals(JsonParser.NumberType.INT, serializer._numberType);
        assertEquals("integer", serializer._schemaType);
        
        // TODO: Implement further tests for IntegerSerializer
    }
    
    @Test
    public void testIntLikeSerializer() {
        IntLikeSerializer serializer = IntLikeSerializer.instance;
        
        assertNotNull(serializer);
        assertEquals(Number.class, serializer.handledType());
        assertEquals(JsonParser.NumberType.INT, serializer._numberType);
        assertEquals("integer", serializer._schemaType);
        
        // TODO: Implement further tests for IntLikeSerializer
    }
    
    @Test
    public void testLongSerializer() {
        LongSerializer serializer = new LongSerializer(Long.class);
        
        assertNotNull(serializer);
        assertEquals(Long.class, serializer.handledType());
        assertEquals(JsonParser.NumberType.LONG, serializer._numberType);
        assertEquals("number", serializer._schemaType);
        
        // TODO: Implement further tests for LongSerializer
    }
    
    @Test
    public void testFloatSerializer() {
        FloatSerializer serializer = FloatSerializer.instance;
        
        assertNotNull(serializer);
        assertEquals(Float.class, serializer.handledType());
        assertEquals(JsonParser.NumberType.FLOAT, serializer._numberType);
        assertEquals("number", serializer._schemaType);
        
        // TODO: Implement further tests for FloatSerializer
    }
    
    @Test
    public void testDoubleSerializer() {
        DoubleSerializer serializer = new DoubleSerializer(Double.class);
        
        assertNotNull(serializer);
        assertEquals(Double.class, serializer.handledType());
        assertEquals(JsonParser.NumberType.DOUBLE, serializer._numberType);
        assertEquals("number", serializer._schemaType);
        
        // TODO: Implement further tests for DoubleSerializer
    }
}