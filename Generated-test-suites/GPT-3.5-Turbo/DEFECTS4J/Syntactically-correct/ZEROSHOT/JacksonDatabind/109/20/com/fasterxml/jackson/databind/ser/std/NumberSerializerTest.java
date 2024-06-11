import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializerTest {

    @Test
    public void testSerializeBigDecimal() {
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        BigDecimal number = new BigDecimal("123.456");
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        
        try {
            serializer.serialize(number, generator, provider);
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
        
        Mockito.verify(generator).writeNumber(number);
    }
    
    @Test
    public void testSerializeBigInteger() {
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        BigInteger number = new BigInteger("1234567890");
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        
        try {
            serializer.serialize(number, generator, provider);
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
        
        Mockito.verify(generator).writeNumber(number);
    }
    
    @Test
    public void testSerializeLong() {
        NumberSerializer serializer = new NumberSerializer(Long.class);
        Long number = 123L;
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        
        try {
            serializer.serialize(number, generator, provider);
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
        
        Mockito.verify(generator).writeNumber(number);
    }
    
    @Test
    public void testSerializeDouble() {
        NumberSerializer serializer = new NumberSerializer(Double.class);
        Double number = 123.456;
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        
        try {
            serializer.serialize(number, generator, provider);
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
        
        Mockito.verify(generator).writeNumber(number);
    }
    
    @Test
    public void testSerializeFloat() {
        NumberSerializer serializer = new NumberSerializer(Float.class);
        Float number = 123.456f;
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        
        try {
            serializer.serialize(number, generator, provider);
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
        
        Mockito.verify(generator).writeNumber(number);
    }
    
    @Test
    public void testSerializeInteger() {
        NumberSerializer serializer = new NumberSerializer(Integer.class);
        Integer number = 123;
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        
        try {
            serializer.serialize(number, generator, provider);
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
        
        Mockito.verify(generator).writeNumber(number);
    }
    
    @Test
    public void testSerializeByte() {
        NumberSerializer serializer = new NumberSerializer(Byte.class);
        Byte number = 123;
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        
        try {
            serializer.serialize(number, generator, provider);
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
        
        Mockito.verify(generator).writeNumber(number);
    }
    
    @Test
    public void testSerializeShort() {
        NumberSerializer serializer = new NumberSerializer(Short.class);
        Short number = 123;
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        
        try {
            serializer.serialize(number, generator, provider);
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
        
        Mockito.verify(generator).writeNumber(number);
    }
    
    @Test
    public void testSerializeOtherType() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        Number number = new Byte("123");
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        
        try {
            serializer.serialize(number, generator, provider);
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
        
        Mockito.verify(generator).writeNumber(number.toString());
    }
    
}