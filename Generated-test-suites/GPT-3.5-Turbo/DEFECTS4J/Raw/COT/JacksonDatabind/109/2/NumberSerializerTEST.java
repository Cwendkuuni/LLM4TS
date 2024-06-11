
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializerTest {
    
    @Test
    public void testSerializeBigDecimal() {
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        BigDecimal value = new BigDecimal("123.45");
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        
        try {
            serializer.serialize(value, generator, provider);
            verify(generator).writeNumber(value);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }
    
    @Test
    public void testSerializeBigInteger() {
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        BigInteger value = new BigInteger("12345");
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        
        try {
            serializer.serialize(value, generator, provider);
            verify(generator).writeNumber(value);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }
    
    @Test
    public void testSerializeLong() {
        NumberSerializer serializer = new NumberSerializer(Long.class);
        Long value = 12345L;
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        
        try {
            serializer.serialize(value, generator, provider);
            verify(generator).writeNumber(value.longValue());
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }
    
    @Test
    public void testSerializeDouble() {
        NumberSerializer serializer = new NumberSerializer(Double.class);
        Double value = 123.45;
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        
        try {
            serializer.serialize(value, generator, provider);
            verify(generator).writeNumber(value.doubleValue());
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }
    
    @Test
    public void testSerializeFloat() {
        NumberSerializer serializer = new NumberSerializer(Float.class);
        Float value = 123.45f;
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        
        try {
            serializer.serialize(value, generator, provider);
            verify(generator).writeNumber(value.floatValue());
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }
    
    @Test
    public void testSerializeInteger() {
        NumberSerializer serializer = new NumberSerializer(Integer.class);
        Integer value = 12345;
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        
        try {
            serializer.serialize(value, generator, provider);
            verify(generator).writeNumber(value.intValue());
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }
    
    @Test
    public void testSerializeByte() {
        NumberSerializer serializer = new NumberSerializer(Byte.class);
        Byte value = 123;
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        
        try {
            serializer.serialize(value, generator, provider);
            verify(generator).writeNumber(value.intValue());
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }
    
    @Test
    public void testSerializeShort() {
        NumberSerializer serializer = new NumberSerializer(Short.class);
        Short value = 12345;
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        
        try {
            serializer.serialize(value, generator, provider);
            verify(generator).writeNumber(value.intValue());
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }
    
    @Test
    public void testSerializeOtherNumber() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        Number value = new AtomicInteger(12345);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        
        try {
            
            serializer.serialize(value, generator, provider);
            verify(generator).writeNumber(value.toString());
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }
}
