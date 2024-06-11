import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import static org.mockito.Mockito.*;

@JacksonStdImpl
@SuppressWarnings("serial")
public class NumberSerializerTest {

    private NumberSerializer numberSerializer;
    private JsonGenerator mockJsonGenerator;
    private SerializerProvider mockSerializerProvider;
    
    @Before
    public void setUp() {
        numberSerializer = new NumberSerializer(Number.class);
        mockJsonGenerator = mock(JsonGenerator.class);
        mockSerializerProvider = mock(SerializerProvider.class);
    }

    @Test
    public void testSerialize_ShouldSerializeBigDecimalValue_WhenValueIsInstanceOfBigDecimal() throws IOException {
        // Arrange
        BigDecimal value = new BigDecimal(10);
        
        // Act
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        
        // Assert
        verify(mockJsonGenerator).writeNumber(value);
    }

    @Test
    public void testSerialize_ShouldSerializeBigIntegerValue_WhenValueIsInstanceOfBigInteger() throws IOException {
        // Arrange
        BigInteger value = new BigInteger("100");
        
        // Act
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        
        // Assert
        verify(mockJsonGenerator).writeNumber(value);
    }

    @Test
    public void testSerialize_ShouldSerializeIntegerValueAsInt_WhenValueIsInstanceOfInteger() throws IOException {
        // Arrange
        Integer value = 5;
        
        // Act
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        
        // Assert
        verify(mockJsonGenerator).writeNumber(value.intValue());
    }

    @Test
    public void testSerialize_ShouldSerializeLongValueAsLong_WhenValueIsInstanceOfLong() throws IOException {
        // Arrange
        Long value = 987654321L;
        
        // Act
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        
        // Assert
        verify(mockJsonGenerator).writeNumber(value.longValue());
    }

    @Test
    public void testSerialize_ShouldSerializeDoubleValueAsDouble_WhenValueIsInstanceOfDouble() throws IOException {
        // Arrange
        Double value = 3.14;
        
        // Act
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        
        // Assert
        verify(mockJsonGenerator).writeNumber(value.doubleValue());
    }

    @Test
    public void testSerialize_ShouldSerializeFloatValueAsFloat_WhenValueIsInstanceOfFloat() throws IOException {
        // Arrange
        Float value = 1.23f;
        
        // Act
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        
        // Assert
        verify(mockJsonGenerator).writeNumber(value.floatValue());
    }

    @Test
    public void testSerialize_ShouldSerializeByteValueAsInt_WhenValueIsInstanceOfByte() throws IOException {
        // Arrange
        Byte value = 127;
        
        // Act
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        
        // Assert
        verify(mockJsonGenerator).writeNumber(value.intValue());
    }

    @Test
    public void testSerialize_ShouldSerializeShortValueAsInt_WhenValueIsInstanceOfShort() throws IOException {
        // Arrange
        Short value = 32767;
        
        // Act
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        
        // Assert
        verify(mockJsonGenerator).writeNumber(value.intValue());
    }

    @Test
    public void testSerialize_ShouldSerializeNonNumericValueAsString_WhenValueIsNotInstanceOfAnyNumberClass() throws IOException {
        // Arrange
        String value = "test";
        
        // Act
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        
        // Assert
        verify(mockJsonGenerator).writeNumber(value);
    }

    @Test
    public void testGetSchema_ShouldCreateIntegerSchemaNode_WhenSerializerHandlesInteger() {
        // Arrange
        BigInteger value = new BigInteger("10");
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = value.getClass();
        
        // Act
        JsonNode schemaNode = numberSerializer.getSchema(provider, typeHint);
        
        // Assert
        assertEquals("integer", schemaNode.get("type").asText());
    }

    @Test
    public void testGetSchema_ShouldCreateNumberSchemaNode_WhenSerializerHandlesAnyNonIntegerNumber() {
        // Arrange
        BigDecimal value = new BigDecimal("1.234");
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = value.getClass();
        
        // Act
        JsonNode schemaNode = numberSerializer.getSchema(provider, typeHint);
        
        // Assert
        assertEquals("number", schemaNode.get("type").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor_ShouldVisitIntFormat_WhenSerializerHandlesInteger() throws JsonMappingException {
        // Arrange
        JavaType typeHint = mock(JavaType.class);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        
        // Act
        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        // Assert
        verify(visitor).visitIntFormat(eq(typeHint), eq(JsonParser.NumberType.BIG_INTEGER));
    }

    @Test
    public void testAcceptJsonFormatVisitor_ShouldVisitFloatFormat_WhenSerializerHandlesBigDecimal() throws JsonMappingException {
        // Arrange
        JavaType typeHint = mock(JavaType.class);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        
        // Act
        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        // Assert
        verify(visitor).visitFloatFormat(eq(typeHint), eq(JsonParser.NumberType.BIG_DECIMAL));
    }

    @Test
    public void testAcceptJsonFormatVisitor_ShouldExpectNumberFormat_WhenSerializerHandlesAnyOtherNumberType() throws JsonMappingException {
        // Arrange
        JavaType typeHint = mock(JavaType.class);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        
        // Act
        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        // Assert
        verify(visitor).expectNumberFormat(eq(typeHint));
    }
}