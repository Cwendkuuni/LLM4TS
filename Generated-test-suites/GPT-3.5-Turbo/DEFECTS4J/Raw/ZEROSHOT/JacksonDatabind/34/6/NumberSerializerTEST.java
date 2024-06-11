import org.junit.Test;

public class NumberSerializerTest {

    @Test
    public void testSerializeBigDecimal() {
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        
        BigDecimal value = new BigDecimal("123.456");
        
        serializer.serialize(value, generator, provider);
        
        Mockito.verify(generator).writeNumber(value);
    }
    
    @Test
    public void testSerializeBigInteger() {
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        
        BigInteger value = BigInteger.valueOf(123456789);
        
        serializer.serialize(value, generator, provider);
        
        Mockito.verify(generator).writeNumber(value);
    }
    
    @Test
    public void testSerializeInteger() {
        NumberSerializer serializer = new NumberSerializer(Integer.class);
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        
        Integer value = 123;
        
        serializer.serialize(value, generator, provider);
        
        Mockito.verify(generator).writeNumber(value.intValue());
    }
    
    @Test
    public void testSerializeLong() {
        NumberSerializer serializer = new NumberSerializer(Long.class);
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        
        Long value = 123L;
        
        serializer.serialize(value, generator, provider);
        
        Mockito.verify(generator).writeNumber(value.longValue());
    }
    
    @Test
    public void testSerializeDouble() {
        NumberSerializer serializer = new NumberSerializer(Double.class);
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        
        Double value = 123.456;
        
        serializer.serialize(value, generator, provider);
        
        Mockito.verify(generator).writeNumber(value.doubleValue());
    }
    
    @Test
    public void testSerializeFloat() {
        NumberSerializer serializer = new NumberSerializer(Float.class);
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        
        Float value = 123.456f;
        
        serializer.serialize(value, generator, provider);
        
        Mockito.verify(generator).writeNumber(value.floatValue());
    }
    
    @Test
    public void testSerializeByte() {
        NumberSerializer serializer = new NumberSerializer(Byte.class);
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        
        Byte value = 123;
        
        serializer.serialize(value, generator, provider);
        
        Mockito.verify(generator).writeNumber(value.intValue());
    }
    
    @Test
    public void testSerializeShort() {
        NumberSerializer serializer = new NumberSerializer(Short.class);
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        
        Short value = 123;
        
        serializer.serialize(value, generator, provider);
        
        Mockito.verify(generator).writeNumber(value.intValue());
    }
    
    @Test
    public void testSerializeOther() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        
        Number value = 123.456;
        
        serializer.serialize(value, generator, provider);
        
        Mockito.verify(generator).writeNumber(value.toString());
    }
    
    @Test
    public void testGetSchema() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        Type typeHint = Mockito.mock(Type.class);
        
        serializer.getSchema(provider, typeHint);
        
        // Add assertions here
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = Mockito.mock(JavaType.class);
        
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        // Add assertions here
    }
}