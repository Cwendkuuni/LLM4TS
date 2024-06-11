import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class NumberSerializersTest {

    private Map<String, JsonSerializer<?>> serializers;

    @Before
    public void setup() {
        serializers = new HashMap<>();
        NumberSerializers.addAll(serializers);
    }

    @Test
    public void testAddAll() {
        Assert.assertTrue(serializers.containsKey(Integer.class.getName()));
        Assert.assertTrue(serializers.containsKey(Integer.TYPE.getName()));
        Assert.assertTrue(serializers.containsKey(Long.class.getName()));
        Assert.assertTrue(serializers.containsKey(Long.TYPE.getName()));
        Assert.assertTrue(serializers.containsKey(Byte.class.getName()));
        Assert.assertTrue(serializers.containsKey(Byte.TYPE.getName()));
        Assert.assertTrue(serializers.containsKey(Short.class.getName()));
        Assert.assertTrue(serializers.containsKey(Short.TYPE.getName()));
        Assert.assertTrue(serializers.containsKey(Float.class.getName()));
        Assert.assertTrue(serializers.containsKey(Float.TYPE.getName()));
        Assert.assertTrue(serializers.containsKey(Double.class.getName()));
        Assert.assertTrue(serializers.containsKey(Double.TYPE.getName()));
    }

    @Test
    public void testShortSerializerSerialize() throws IOException {
        NumberSerializers.ShortSerializer shortSerializer = NumberSerializers.ShortSerializer.instance;
        MockJsonGenerator generator = new MockJsonGenerator();
        SerializerProvider provider = null;
        
        shortSerializer.serialize((short) 42, generator, provider);
        
        Assert.assertEquals("42", generator.getResult());
    }

    @Test
    public void testIntegerSerializerSerialize() throws IOException {
        NumberSerializers.IntegerSerializer integerSerializer = NumberSerializers.IntegerSerializer.instance;
        MockJsonGenerator generator = new MockJsonGenerator();
        SerializerProvider provider = null;
        
        integerSerializer.serialize(42, generator, provider);
        
        Assert.assertEquals("42", generator.getResult());
    }
    
    @Test
    public void testIntLikeSerializerSerialize() throws IOException {
        NumberSerializers.IntLikeSerializer intLikeSerializer = NumberSerializers.IntLikeSerializer.instance;
        MockJsonGenerator generator = new MockJsonGenerator();
        SerializerProvider provider = null;
        
        intLikeSerializer.serialize(42, generator, provider);
        
        Assert.assertEquals("42", generator.getResult());
    }
    
    @Test
    public void testLongSerializerSerialize() throws IOException {
        NumberSerializers.LongSerializer longSerializer = NumberSerializers.LongSerializer.instance;
        MockJsonGenerator generator = new MockJsonGenerator();
        SerializerProvider provider = null;
        
        longSerializer.serialize(42L, generator, provider);
        
        Assert.assertEquals("42", generator.getResult());
    }
    
    @Test
    public void testFloatSerializerSerialize() throws IOException {
        NumberSerializers.FloatSerializer floatSerializer = NumberSerializers.FloatSerializer.instance;
        MockJsonGenerator generator = new MockJsonGenerator();
        SerializerProvider provider = null;
        
        floatSerializer.serialize(3.14f, generator, provider);
        
        Assert.assertEquals("3.14", generator.getResult());
    }
    
    @Test
    public void testDoubleSerializerSerialize() throws IOException {
        NumberSerializers.DoubleSerializer doubleSerializer = NumberSerializers.DoubleSerializer.instance;
        MockJsonGenerator generator = new MockJsonGenerator();
        SerializerProvider provider = null;
        
        doubleSerializer.serialize(3.14, generator, provider);
        
        Assert.assertEquals("3.14", generator.getResult());
    }

    private static class MockJsonGenerator extends JsonGenerator {
        private StringBuilder result = new StringBuilder();

        public String getResult() {
            return result.toString();
        }

        @Override
        public void writeNumber(int v) throws IOException {
            result.append(v);
        }

        @Override
        public void writeNumber(long v) throws IOException {
            result.append(v);
        }

        @Override
        public void writeNumber(double v) throws IOException {
            result.append(v);
        }

        @Override
        public void writeNumber(float v) throws IOException {
            result.append(v);
        }
        
        // other abstract method implementations...
    }

}