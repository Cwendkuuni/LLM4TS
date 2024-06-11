
import org.junit.Assert;
import org.junit.Test;

public class NumberSerializersTest {
    
    @Test
    public void testIntegerSerializer() {
        NumberSerializers.IntegerSerializer serializer = new NumberSerializers.IntegerSerializer(Integer.class);
        
        Object value = 10;
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        
        try {
            serializer.serialize(value, gen, provider);
        } catch (IOException e) {
            Assert.fail("IOException thrown");
        }
        
        // Perform assertions here
        
    }
    
    @Test
    public void testShortSerializer() {
        NumberSerializers.ShortSerializer serializer = new NumberSerializers.ShortSerializer();
        
        Object value = (short) 10;
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        
        try {
            serializer.serialize(value, gen, provider);
        } catch (IOException e) {
            Assert.fail("IOException thrown");
        }
        
        // Perform assertions here
        
    }
    
    @Test
    public void testIntLikeSerializer() {
        NumberSerializers.IntLikeSerializer serializer = new NumberSerializers.IntLikeSerializer();
        
        Object value = 10;
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        
        try {
            serializer.serialize(value, gen, provider);
        } catch (IOException e) {
            Assert.fail("IOException thrown");
        }
        
        // Perform assertions here
        
    }
    
    @Test
    public void testLongSerializer() {
        NumberSerializers.LongSerializer serializer = new NumberSerializers.LongSerializer(Long.class);
        
        Object value = 10L;
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        
        try {
            serializer.serialize(value, gen, provider);
        } catch (IOException e) {
            Assert.fail("IOException thrown");
        }
        
        // Perform assertions here
        
    }
    
    @Test
    public void testFloatSerializer() {
        NumberSerializers.FloatSerializer serializer = new NumberSerializers.FloatSerializer();
        
        Object value = 10.0f;
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        
        try {
            serializer.serialize(value, gen, provider);
        } catch (IOException e) {
            Assert.fail("IOException thrown");
        }
        
        // Perform assertions here
        
    }
    
    @Test
    public void testDoubleSerializer() {
        NumberSerializers.DoubleSerializer serializer = new NumberSerializers.DoubleSerializer(Double.class);
        
        Object value = 10.0;
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        
        try {
            serializer.serialize(value, gen, provider);
        } catch (IOException e) {
            Assert.fail("IOException thrown");
        }
        
        // Perform assertions here
        
    }
    
}
