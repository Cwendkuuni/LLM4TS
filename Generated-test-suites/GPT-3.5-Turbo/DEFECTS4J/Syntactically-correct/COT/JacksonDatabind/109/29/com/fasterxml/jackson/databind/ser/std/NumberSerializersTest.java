
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;

import org.junit.Test;
import org.junit.Assert;
import java.io.IOException;

public class NumberSerializersTest {
    @Test
    public void testIntegerSerializer() throws IOException {
        NumberSerializers.IntegerSerializer serializer = new NumberSerializers.IntegerSerializer(Integer.class);
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        int value = 100;
        
        try {
            serializer.serialize(value, gen, provider);
        } catch (Exception e) {
            Assert.fail("Exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testShortSerializer() throws IOException {
        NumberSerializers.ShortSerializer serializer = new NumberSerializers.ShortSerializer();
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        short value = 10;
        
        try {
            serializer.serialize(value, gen, provider);
        } catch (Exception e) {
            Assert.fail("Exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testLongSerializer() throws IOException {
        NumberSerializers.LongSerializer serializer = new NumberSerializers.LongSerializer(Long.class);
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        long value = 1000L;
        
        try {
            serializer.serialize(value, gen, provider);
        } catch (Exception e) {
            Assert.fail("Exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testFloatSerializer() throws IOException {
        NumberSerializers.FloatSerializer serializer = new NumberSerializers.FloatSerializer();
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        float value = 10.5f;
        
        try {
            serializer.serialize(value, gen, provider);
        } catch (Exception e) {
            Assert.fail("Exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testDoubleSerializer() throws IOException {
        NumberSerializers.DoubleSerializer serializer = new NumberSerializers.DoubleSerializer(Double.class);
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        double value = 10.5;
        
        try {
            serializer.serialize(value, gen, provider);
        } catch (Exception e) {
            Assert.fail("Exception: " + e.getMessage());
        }
    }
}
