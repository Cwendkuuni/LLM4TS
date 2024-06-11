import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

/**
 * Test cases for NumberSerializer class.
 */
@RunWith(MockitoJUnitRunner.class)
public class NumberSerializerTest {

    @Mock
    private SerializerProvider serializerProvider;

    private NumberSerializer numberSerializer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        numberSerializer = new NumberSerializer(Number.class);
    }

    @Test
    public void testCreateContextualWithStringShape() throws JsonMappingException {
        BeanProperty beanProperty = mock(BeanProperty.class);
        when(beanProperty.getType()).thenReturn(mock(JavaType.class));
        when(beanProperty.getType().getRawClass()).thenReturn(Number.class);
        SerializerProvider prov = mock(SerializerProvider.class);
        
        JsonFormat.Value format = JsonFormat.Value.forShape(JsonFormat.Shape.STRING);
        when(numberSerializer.findFormatOverrides(prov, beanProperty, Number.class)).thenReturn(format);

        JsonSerializer<?> serializer = numberSerializer.createContextual(serializerProvider, beanProperty);
        
        assertEquals(ToStringSerializer.class, serializer.getClass());
    }

    @Test
    public void testSerializeBigDecimal() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        BigDecimal bigDecimal = new BigDecimal("1.23");
        
        numberSerializer.serialize(bigDecimal, jsonGenerator, serializerProvider);
        
        // Verify that writeNumber method was called with correct BigDecimal value
        verify(jsonGenerator).writeNumber(bigDecimal);
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        BigInteger bigInteger = new BigInteger("1234567890");
        
        numberSerializer.serialize(bigInteger, jsonGenerator, serializerProvider);
        
        // Verify that writeNumber method was called with correct BigInteger value
        verify(jsonGenerator).writeNumber(bigInteger);
    }

    @Test
    public void testSerializeLong() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        Long longNumber = 123L;
        
        numberSerializer.serialize(longNumber, jsonGenerator, serializerProvider);
        
        // Verify that writeNumber method was called with correct Long value
        verify(jsonGenerator).writeNumber(longNumber);
    }

    @Test
    public void testSerializeDouble() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        Double doubleNumber = 1.23;
        
        numberSerializer.serialize(doubleNumber, jsonGenerator, serializerProvider);
        
        // Verify that writeNumber method was called with correct Double value
        verify(jsonGenerator).writeNumber(doubleNumber);
    }

    @Test
    public void testSerializeFloat() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        Float floatNumber = 1.23f;
        
        numberSerializer.serialize(floatNumber, jsonGenerator, serializerProvider);
        
        // Verify that writeNumber method was called with correct Float value
        verify(jsonGenerator).writeNumber(floatNumber);
    }

    @Test
    public void testSerializeInteger() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        Integer integerNumber = 123;
        
        numberSerializer.serialize(integerNumber, jsonGenerator, serializerProvider);
        
        // Verify that writeNumber method was called with correct Integer value
        verify(jsonGenerator).writeNumber(integerNumber);
    }

    @Test
    public void testSerializeByte() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        Byte byteNumber = 1;
        
        numberSerializer.serialize(byteNumber, jsonGenerator, serializerProvider);
        
        // Verify that writeNumber method was called with correct Byte value
        verify(jsonGenerator).writeNumber(byteNumber.intValue());
    }

    @Test
    public void testSerializeShort() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        Short shortNumber = 12;
        
        numberSerializer.serialize(shortNumber, jsonGenerator, serializerProvider);
        
        // Verify that writeNumber method was called with correct Short value
        verify(jsonGenerator).writeNumber(shortNumber.intValue());
    }

    @Test
    public void testSerializeFallback() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        Number number = 1;
        
        numberSerializer.serialize(number, jsonGenerator, serializerProvider);
        
        // Verify that writeNumber method was called with correct String representation of the Number
        verify(jsonGenerator).writeNumber(number.toString());
    }

    @Test
    public void testGetSchema() {
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        Type typeHint = Number.class;
        
        JsonNode schema = numberSerializer.getSchema(serializerProvider, typeHint);
        
        // Verify that createSchemaNode method was called with correct arguments
        assertEquals("number", schema.get("type").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitorWithBigIntegerType() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        when(numberSerializer.handledType()).thenReturn((Class<?>) BigInteger.class);

        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        // Verify that visitIntFormat method was called with correct arguments
        verify(visitor).visitIntFormat(typeHint, JsonParser.NumberType.BIG_INTEGER);
    }

    @Test
    public void testAcceptJsonFormatVisitorWithBigDecimalType() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        when(numberSerializer.handledType()).thenReturn((Class<?>) BigDecimal.class);

        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        // Verify that visitFloatFormat method was called with correct arguments
        verify(visitor).visitFloatFormat(typeHint, JsonParser.NumberType.BIG_DECIMAL);
    }

    @Test
    public void testAcceptJsonFormatVisitorWithOtherType() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        when(numberSerializer.handledType()).thenReturn((Class<?>) Number.class);

        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        // Verify that expectNumberFormat method was called
        verify(visitor).expectNumberFormat(typeHint);
    }
}