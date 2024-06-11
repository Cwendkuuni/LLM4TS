import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class NumberSerializerTest {

    @Test
    public void serialize_shouldWriteBigDecimalValue() {
        // Arrange
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        BigDecimal value = new BigDecimal("10.5");
        JsonGenerator g = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        
        // Act
        numberSerializer.serialize(value, g, provider);
        
        // Assert
        verify(g).writeNumber(value);
    }
    
    @Test
    public void serialize_shouldWriteBigIntegerValue() {
        // Arrange
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        BigInteger value = new BigInteger("123");
        JsonGenerator g = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        
        // Act
        numberSerializer.serialize(value, g, provider);
        
        // Assert
        verify(g).writeNumber(value);
    }
    
    @Test
    public void serialize_shouldWriteIntegerValue() {
        // Arrange
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        Integer value = 123;
        JsonGenerator g = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        
        // Act
        numberSerializer.serialize(value, g, provider);
        
        // Assert
        verify(g).writeNumber(value.intValue());
    }
    
    @Test
    public void serialize_shouldWriteLongValue() {
        // Arrange
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        Long value = 123L;
        JsonGenerator g = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        
        // Act
        numberSerializer.serialize(value, g, provider);
        
        // Assert
        verify(g).writeNumber(value.longValue());
    }
    
    @Test
    public void serialize_shouldWriteDoubleValue() {
        // Arrange
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        Double value = 123.0;
        JsonGenerator g = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        
        // Act
        numberSerializer.serialize(value, g, provider);
        
        // Assert
        verify(g).writeNumber(value.doubleValue());
    }
    
    @Test
    public void serialize_shouldWriteFloatValue() {
        // Arrange
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        Float value = 123.0f;
        JsonGenerator g = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        
        // Act
        numberSerializer.serialize(value, g, provider);
        
        // Assert
        verify(g).writeNumber(value.floatValue());
    }
    
    @Test
    public void serialize_shouldWriteByteValue() {
        // Arrange
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        Byte value = 123;
        JsonGenerator g = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        
        // Act
        numberSerializer.serialize(value, g, provider);
        
        // Assert
        verify(g).writeNumber(value.intValue());
    }
    
    @Test
    public void serialize_shouldWriteShortValue() {
        // Arrange
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        Short value = 123;
        JsonGenerator g = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        
        // Act
        numberSerializer.serialize(value, g, provider);
        
        // Assert
        verify(g).writeNumber(value.intValue());
    }
    
    @Test
    public void serialize_shouldFallbackToWriteStringValue() {
        // Arrange
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        Number value = 123456789;
        JsonGenerator g = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        
        // Act
        numberSerializer.serialize(value, g, provider);
        
        // Assert
        verify(g).writeNumber(value.toString());
    }
    
    @Test
    public void getSchema_shouldReturnIntegerSchemaIfIsIntIsTrue() {
        // Arrange
        NumberSerializer numberSerializer = new NumberSerializer(BigInteger.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = BigInteger.class;
        
        // Act
        JsonNode schema = numberSerializer.getSchema(provider, typeHint);
        
        // Assert
        assertEquals("integer", schema.asText());
        assertEquals(true, schema.isIntegralNumber());
    }
    
    @Test
    public void getSchema_shouldReturnNumberSchemaIfIsIntIsFalse() {
        // Arrange
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = Number.class;
        
        // Act
        JsonNode schema = numberSerializer.getSchema(provider, typeHint);
        
        // Assert
        assertEquals("number", schema.asText());
        assertEquals(true, schema.isNumber());
    }
    
    @Test
    public void acceptJsonFormatVisitor_shouldVisitIntFormatIfIsIntIsTrue() {
        // Arrange
        NumberSerializer numberSerializer = new NumberSerializer(BigInteger.class);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        
        // Act
        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        // Assert
        verify(visitor).visitIntFormat(typeHint, JsonParser.NumberType.BIG_INTEGER);
    }
    
    @Test
    public void acceptJsonFormatVisitor_shouldVisitFloatFormatIfHandledTypeIsBigDecimal() {
        // Arrange
        NumberSerializer numberSerializer = new NumberSerializer(BigDecimal.class);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        
        // Act
        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        // Assert
        verify(visitor).visitFloatFormat(typeHint, JsonParser.NumberType.BIG_INTEGER);
    }
    
    @Test
    public void acceptJsonFormatVisitor_shouldFallBackToExpectNumberFormat() {
        // Arrange
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        
        // Act
        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        // Assert
        verify(visitor).expectNumberFormat(typeHint);
    }
}