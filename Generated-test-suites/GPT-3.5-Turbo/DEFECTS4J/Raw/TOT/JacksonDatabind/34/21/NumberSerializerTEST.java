import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

public class NumberSerializerTest {
    
    private NumberSerializer numberSerializer = new NumberSerializer(Number.class);
    private JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
    private SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);
    
    @Test
    public void testSerialize_WithBigDecimal_ShouldWriteNumber() throws IOException {
        BigDecimal value = new BigDecimal(10.5);
        
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        
        Mockito.verify(jsonGenerator).writeNumber(value);
    }
    
    @Test
    public void testSerialize_WithBigInteger_ShouldWriteNumber() throws IOException {
        BigInteger value = new BigInteger("100000000000000000000000");
        
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        
        Mockito.verify(jsonGenerator).writeNumber(value);
    }
    
    @Test
    public void testSerialize_WithInteger_ShouldWriteNumber() throws IOException {
        Integer value = 100;
        
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        
        Mockito.verify(jsonGenerator).writeNumber(value);
    }
    
    @Test
    public void testSerialize_WithLong_ShouldWriteNumber() throws IOException {
        Long value = Long.MAX_VALUE;
        
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        
        Mockito.verify(jsonGenerator).writeNumber(value);
    }
    
    @Test
    public void testSerialize_WithDouble_ShouldWriteNumber() throws IOException {
        Double value = 10.5;
        
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        
        Mockito.verify(jsonGenerator).writeNumber(value);
    }
    
    @Test
    public void testSerialize_WithFloat_ShouldWriteNumber() throws IOException {
        Float value = 10.5f;
        
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        
        Mockito.verify(jsonGenerator).writeNumber(value);
    }
    
    @Test
    public void testSerialize_WithByte_ShouldWriteNumber() throws IOException {
        Byte value = 10;
        
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        
        Mockito.verify(jsonGenerator).writeNumber(value.intValue());
    }
    
    @Test
    public void testSerialize_WithShort_ShouldWriteNumber() throws IOException {
        Short value = 100;
        
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        
        Mockito.verify(jsonGenerator).writeNumber(value.intValue());
    }
    
    @Test
    public void testSerialize_WithOtherNumberType_ShouldWriteStringRepresentation() throws IOException {
        Number value = new ComplexNumber(2, 3);
        String expectedString = value.toString();
        
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        
        Mockito.verify(jsonGenerator).writeNumber(expectedString);
    }
    
    @Test
    public void testGetSchema_WhenIsInt_ShouldReturnIntegerSchemaNode() throws JsonMappingException {
        JsonNode schemaNode = numberSerializer.getSchema(serializerProvider, null);
        
        assertEquals("integer", schemaNode.asText());
    }
    
    @Test
    public void testGetSchema_WhenIsNotInt_ShouldReturnNumberSchemaNode() throws JsonMappingException {
        NumberSerializer nonIntNumberSerializer = new NumberSerializer(Float.class);
        
        JsonNode schemaNode = nonIntNumberSerializer.getSchema(serializerProvider, null);
        
        assertEquals("number", schemaNode.asText());
    }
    
    @Test
    public void testAcceptJsonFormatVisitor_WhenIsInt_ShouldVisitIntFormat() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = Mockito.mock(JavaType.class);
        
        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        Mockito.verify(visitor).expectIntFormat(typeHint, JsonParser.NumberType.BIG_INTEGER);
    }
    
    @Test
    public void testAcceptJsonFormatVisitor_WhenIsBigDecimal_ShouldVisitFloatFormat() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = Mockito.mock(JavaType.class);
        NumberSerializer bigDecimalSerializer = new NumberSerializer(BigDecimal.class);
        
        bigDecimalSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        Mockito.verify(visitor).expectFloatFormat(typeHint, JsonParser.NumberType.BIG_DECIMAL);
    }
    
    @Test
    public void testAcceptJsonFormatVisitor_WhenIsNotIntOrBigDecimal_ShouldExpectNumberFormat() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = Mockito.mock(JavaType.class);
        NumberSerializer nonIntNonBigDecimalSerializer = new NumberSerializer(Float.class);
        
        nonIntNonBigDecimalSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        Mockito.verify(visitor).expectNumberFormat(typeHint);
    }
}