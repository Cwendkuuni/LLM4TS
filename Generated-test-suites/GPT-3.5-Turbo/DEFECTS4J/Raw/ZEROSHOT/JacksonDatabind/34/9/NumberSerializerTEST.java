import static org.mockito.Mockito.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

public class NumberSerializerTest {

    private NumberSerializer numberSerializer;
    
    @Mock
    private JsonGenerator mockJsonGenerator;
    
    @Mock
    private SerializerProvider mockSerializerProvider;
    
    @Mock
    private JsonMappingException mockJsonMappingException;
    
    @Mock
    private JsonFormatVisitorWrapper mockJsonFormatVisitorWrapper;
    
    @Mock
    private JavaType mockJavaType;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        numberSerializer = new NumberSerializer(Number.class);
    }
    
    @Test
    public void testSerializeBigDecimal() throws IOException {
        BigDecimal value = mock(BigDecimal.class);
        
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        
        verify(mockJsonGenerator).writeNumber(value);
    }
    
    @Test
    public void testSerializeBigInteger() throws IOException {
        BigInteger value = mock(BigInteger.class);
        
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        
        verify(mockJsonGenerator).writeNumber(value);
    }
    
    @Test
    public void testSerializeInteger() throws IOException {
        Integer value = mock(Integer.class);
        
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        
        verify(mockJsonGenerator).writeNumber(value.intValue());
    }
    
    @Test
    public void testSerializeLong() throws IOException {
        Long value = mock(Long.class);
        
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        
        verify(mockJsonGenerator).writeNumber(value.longValue());
    }
    
    @Test
    public void testSerializeDouble() throws IOException {
        Double value = mock(Double.class);
        
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        
        verify(mockJsonGenerator).writeNumber(value.doubleValue());
    }
    
    @Test
    public void testSerializeFloat() throws IOException {
        Float value = mock(Float.class);
        
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        
        verify(mockJsonGenerator).writeNumber(value.floatValue());
    }
    
    @Test
    public void testSerializeByte() throws IOException {
        Byte value = mock(Byte.class);
        
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        
        verify(mockJsonGenerator).writeNumber(value.intValue());
    }
    
    @Test
    public void testSerializeShort() throws IOException {
        Short value = mock(Short.class);
        
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        
        verify(mockJsonGenerator).writeNumber(value.intValue());
    }
    
    @Test
    public void testSerializeFallback() throws IOException {
        Number value = mock(Number.class);
        
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        
        verify(mockJsonGenerator).writeNumber(value.toString());
    }
    
    @Test
    public void testGetSchemaWithInt() {
        JsonNode expectedSchemaNode = createSchemaNode("integer", true);
        
        JsonNode actualSchemaNode = numberSerializer.getSchema(mockSerializerProvider, BigInteger.class);
        
        assertEquals(expectedSchemaNode, actualSchemaNode);
    }
    
    @Test
    public void testGetSchemaWithFloat() {
        JsonNode expectedSchemaNode = createSchemaNode("number", true);
        
        JsonNode actualSchemaNode = numberSerializer.getSchema(mockSerializerProvider, BigDecimal.class);
        
        assertEquals(expectedSchemaNode, actualSchemaNode);
    }
    
    @Test
    public void testAcceptJsonFormatVisitorWithInt() throws JsonMappingException {
        numberSerializer.acceptJsonFormatVisitor(mockJsonFormatVisitorWrapper, mockJavaType);
        
        verify(mockJsonFormatVisitorWrapper).visitIntFormat(eq(mockJavaType), eq(JsonParser.NumberType.BIG_INTEGER));
    }
    
    @Test
    public void testAcceptJsonFormatVisitorWithFloat() throws JsonMappingException {
        numberSerializer.acceptJsonFormatVisitor(mockJsonFormatVisitorWrapper, mockJavaType);
        
        verify(mockJsonFormatVisitorWrapper).visitFloatFormat(eq(mockJavaType), eq(JsonParser.NumberType.BIG_INTEGER));
    }
    
    private JsonNode createSchemaNode(String type, boolean isNumber) {
        // Implementation of createSchemaNode method
    }
}