
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NumberSerializerTest {

    @Mock
    private JsonGenerator mockJsonGenerator;

    @Mock
    private SerializerProvider mockSerializerProvider;

    @InjectMocks
    private NumberSerializer numberSerializer;

    @Before
    public void setUp() throws Exception {
        numberSerializer = Mockito.spy(new NumberSerializer(Number.class));
    }
    
    @Test
    public void testSerialize_withBigDecimal_shouldSerializeNumber() throws IOException {
        BigDecimal value = new BigDecimal("123.45");
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        verify(mockJsonGenerator).writeNumber(value);
    }
    
    @Test
    public void testSerialize_withBigInteger_shouldSerializeNumber() throws IOException {
        BigInteger value = new BigInteger("12345");
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        verify(mockJsonGenerator).writeNumber(value);
    }
    
    @Test
    public void testSerialize_withInteger_shouldSerializeNumber() throws IOException {
        Integer value = 123;
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        verify(mockJsonGenerator).writeNumber(value);
    }
    
    @Test
    public void testSerialize_withLong_shouldSerializeNumber() throws IOException {
        Long value = 123L;
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        verify(mockJsonGenerator).writeNumber(value);
    }
    
    @Test
    public void testSerialize_withDouble_shouldSerializeNumber() throws IOException {
        Double value = 12.34;
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        verify(mockJsonGenerator).writeNumber(value);
    }
    
    @Test
    public void testSerialize_withFloat_shouldSerializeNumber() throws IOException {
        Float value = 12.34f;
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        verify(mockJsonGenerator).writeNumber(value);
    }
    
    @Test
    public void testSerialize_withByte_shouldSerializeNumber() throws IOException {
        Byte value = 1;
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        verify(mockJsonGenerator).writeNumber(value.intValue());
    }
    
    @Test
    public void testSerialize_withShort_shouldSerializeNumber() throws IOException {
        Short value = 12;
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        verify(mockJsonGenerator).writeNumber(value.intValue());
    }
    
    @Test
    public void testSerialize_withUnsupportedNumber_shouldSerializeNumberAsString() throws IOException {
        Number value = new TestNumber();
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        verify(mockJsonGenerator).writeNumber(value.toString());
    }
    
    @Test
    public void testGetSchema_withIntType_shouldCreateIntegerSchemaNode() {
        JsonNode schema = numberSerializer.getSchema(mockSerializerProvider, BigInteger.class);
        assertEquals("integer", schema.asText());
        assertTrue(schema.isNumber());
    }
    
    @Test
    public void testGetSchema_withNonIntType_shouldCreateNumberSchemaNode() {
        JsonNode schema = numberSerializer.getSchema(mockSerializerProvider, Double.class);
        assertEquals("number", schema.asText());
        assertTrue(schema.isNumber());
    }
    
    @Test
    public void testAcceptJsonFormatVisitor_withIntType_shouldVisitIntFormat() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        
        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        if (numberSerializer._isInt) {
            verify(visitor).visitIntFormat(eq(typeHint), eq(JsonParser.NumberType.BIG_INTEGER));
        } else {
            verify(visitor, never()).visitFloatFormat(any(), any(), any());
            verify(visitor).expectNumberFormat(eq(typeHint));
        }
    }
}

class TestNumber extends Number {
    
    // Implement abstract methods of Number class
    
    @Override
    public int intValue() {
        return 0;
    }

    @Override
    public long longValue() {
        return 0;
    }

    @Override
    public float floatValue() {
        return 0;
    }

    @Override
    public double doubleValue() {
        return 0;
    }
}
