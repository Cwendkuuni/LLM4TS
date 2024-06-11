
import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import org.junit.Before;
import org.junit.Test;

public class NumberSerializerTest {
    
    private NumberSerializer serializer;
    
    @Before
    public void setUp() {
        serializer = new NumberSerializer(Number.class);
    }
    
    @Test
    public void testSerialize_WhenValueIsBigDecimal_ThenWriteNumberAsBigDecimal() throws IOException {
        // Arrange
        BigDecimal value = BigDecimal.valueOf(10.5);
        CustomJsonGenerator generator = new CustomJsonGenerator();
        
        // Act
        serializer.serialize(value, generator, null);
        
        // Assert
        assertEquals(value, generator.getWrittenNumber());
    }
    
    @Test
    public void testSerialize_WhenValueIsBigInteger_ThenWriteNumberAsBigInteger() throws IOException {
        // Arrange
        BigInteger value = BigInteger.valueOf(100);
        CustomJsonGenerator generator = new CustomJsonGenerator();
        
        // Act
        serializer.serialize(value, generator, null);
        
        // Assert
        assertEquals(value, generator.getWrittenNumber());
    }
    
    @Test
    public void testSerialize_WhenValueIsInteger_ThenWriteNumberAsInteger() throws IOException {
        // Arrange
        Integer value = 5;
        CustomJsonGenerator generator = new CustomJsonGenerator();
        
        // Act
        serializer.serialize(value, generator, null);
        
        // Assert
        assertEquals(value, generator.getWrittenNumber());
    }
    
    @Test
    public void testSerialize_WhenValueIsLong_ThenWriteNumberAsLong() throws IOException {
        // Arrange
        Long value = 1000L;
        CustomJsonGenerator generator = new CustomJsonGenerator();
        
        // Act
        serializer.serialize(value, generator, null);
        
        // Assert
        assertEquals(value, generator.getWrittenNumber());
    }
    
    @Test
    public void testSerialize_WhenValueIsDouble_ThenWriteNumberAsDouble() throws IOException {
        // Arrange
        Double value = 10.5;
        CustomJsonGenerator generator = new CustomJsonGenerator();
        
        // Act
        serializer.serialize(value, generator, null);
        
        // Assert
        assertEquals(value, generator.getWrittenNumber());
    }
    
    @Test
    public void testSerialize_WhenValueIsFloat_ThenWriteNumberAsFloat() throws IOException {
        // Arrange
        Float value = 3.14f;
        CustomJsonGenerator generator = new CustomJsonGenerator();
        
        // Act
        serializer.serialize(value, generator, null);
        
        // Assert
        assertEquals(value, generator.getWrittenNumber());
    }
    
    @Test
    public void testSerialize_WhenValueIsByte_ThenWriteNumberAsByte() throws IOException {
        // Arrange
        Byte value = 127;
        CustomJsonGenerator generator = new CustomJsonGenerator();
        
        // Act
        serializer.serialize(value, generator, null);
        
        // Assert
        assertEquals(value, generator.getWrittenNumber());
    }
    
    @Test
    public void testSerialize_WhenValueIsShort_ThenWriteNumberAsShort() throws IOException {
        // Arrange
        Short value = 100;
        CustomJsonGenerator generator = new CustomJsonGenerator();
        
        // Act
        serializer.serialize(value, generator, null);
        
        // Assert
        assertEquals(value, generator.getWrittenNumber());
    }
    
    @Test
    public void testSerialize_WhenValueIsNotSpecialType_ThenWriteNumberAsString() throws IOException {
        // Arrange
        CustomNumber value = new CustomNumber(123);
        CustomJsonGenerator generator = new CustomJsonGenerator();
        
        // Act
        serializer.serialize(value, generator, null);
        
        // Assert
        assertEquals(value.toString(), generator.getWrittenNumber());
    }
    
    @Test
    public void testGetSchema_WhenTypeIsBigInteger_ThenReturnSchemaNodeWithIntegerType() {
        // Arrange
        SerializerProvider provider = null;
        JavaType typeHint = null;
        
        // Act
        JsonNode schema = serializer.getSchema(provider, typeHint);
        
        // Assert
        assertEquals("integer", schema.get("type").asText());
    }
    
    @Test
    public void testGetSchema_WhenTypeIsNotBigInteger_ThenReturnSchemaNodeWithNumberType() {
        // Arrange
        SerializerProvider provider = null;
        JavaType typeHint = null;
        
        // Act
        JsonNode schema = serializer.getSchema(provider, typeHint);
        
        // Assert
        assertEquals("number", schema.get("type").asText());
    }
    
    @Test
    public void testAcceptJsonFormatVisitor_WhenTypeIsBigInteger_ThenVisitIntFormatWithBigIntegerType() throws JsonMappingException {
        // Arrange
        JsonFormatVisitorWrapper visitor = new CustomJsonFormatVisitorWrapper();
        JavaType typeHint = null;
        
        // Act
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        // Assert
        assertEquals(JsonParser.NumberType.BIG_INTEGER, visitor.getVisitedNumberType());
    }
    
    @Test
    public void testAcceptJsonFormatVisitor_WhenTypeIsBigDecimal_ThenVisitFloatFormatWithBigDecimalType() throws JsonMappingException {
        // Arrange
        JsonFormatVisitorWrapper visitor = new CustomJsonFormatVisitorWrapper();
        JavaType typeHint = null;
        
        // Act
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        // Assert
        assertEquals(JsonParser.NumberType.BIG_INTEGER, visitor.getVisitedNumberType());
    }
    
    @Test
    public void testAcceptJsonFormatVisitor_WhenTypeIsNotSpecialType_ThenExpectNumberFormat() throws JsonMappingException {
        // Arrange
        JsonFormatVisitorWrapper visitor = new CustomJsonFormatVisitorWrapper();
        JavaType typeHint = null;
        
        // Act
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        // No assert, just validating that the method does not throw an exception
    }
}

class CustomJsonGenerator extends JsonGenerator {
    
    private Number writtenNumber;
    
    @Override
    public void writeNumber(int v) throws IOException {
        writtenNumber = v;
    }
    
    @Override
    public void writeNumber(long v) throws IOException {
        writtenNumber = v;
    }
    
    @Override
    public void writeNumber(double d) throws IOException {
        writtenNumber = d;
    }
    
    @Override
    public void writeNumber(float f) throws IOException {
        writtenNumber = f;
    }
    
    @Override
    public void writeNumber(BigInteger value) throws IOException {
        writtenNumber = value;
    }
    
    @Override
    public void writeNumber(BigDecimal value) throws IOException {
        writtenNumber = value;
    }
    
    @Override
    public void writeNumber(String encodedValue) throws IOException {
        writtenNumber = encodedValue;
    }
    
    public Number getWrittenNumber() {
        return writtenNumber;
    }
}

class CustomJsonFormatVisitorWrapper extends JsonFormatVisitorWrapper.Base {
    
    private JsonParser.NumberType visitedNumberType;
    
    @Override
    public JsonNumberFormatVisitor expectNumberFormat(JavaType type) throws JsonMappingException {
        return new CustomJsonNumberFormatVisitor();
    }
    
    public void setVisitedNumberType(JsonParser.NumberType numberType) {
        visitedNumberType = numberType;
    }
    
    public JsonParser.NumberType getVisitedNumberType() {
        return visitedNumberType;
    }
}

class CustomJsonNumberFormatVisitor extends JsonNumberFormatVisitor.Base<JsonParser.NumberType> {
    
    @Override
    public void visitIntFormat(JsonParser.NumberType v) throws JsonMappingException {
        setNumberType(v);
    }
    
    @Override
    public void visitFloatFormat(JsonParser.NumberType v) throws JsonMappingException {
        setNumberType(v);
    }
    
    private void setNumberType(JsonParser.NumberType numberType) {
        ((CustomJsonFormatVisitorWrapper) _visitor).setVisitedNumberType(numberType);
    }
}

class CustomNumber extends Number {
    
    private final int value;
    
    public CustomNumber(int value) {
        this.value = value;
    }
    
    @Override
    public int intValue() {
        return value;
    }
    
    @Override
    public long longValue() {
        return value;
    }
    
    @Override
    public float floatValue() {
        return value;
    }
    
    @Override
    public double doubleValue() {
        return value;
    }
}
