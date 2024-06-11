import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Type;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdDelegatingSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

@RunWith(MockitoJUnitRunner.class)
public class StdDelegatingSerializerTest {

    @Mock
    private JsonSerializer<Object> mockSerializer;
    
    @Mock
    private Converter<Object, ?> mockConverter;
    
    @Mock
    private JavaType mockJavaType;
    
    private StdDelegatingSerializer serializer;
    
    @Test
    public void testSerialize() throws IOException {
        serializer = new StdDelegatingSerializer(mockConverter, mockJavaType, mockSerializer);
        
        // Mocking value
        Object value = new Object();
        
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        
        serializer.serialize(value, mockGenerator, mockProvider);
        
        verify(mockConverter, times(1)).convert(value);
        verify(mockSerializer, times(1)).serialize(any(), eq(mockGenerator), eq(mockProvider));
    }
    
    @Test
    public void testResolve() throws JsonMappingException {
        serializer = new StdDelegatingSerializer(mockConverter, mockJavaType, mockSerializer);
        
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        
        serializer.resolve(mockProvider);
        
        if (mockSerializer instanceof ResolvableSerializer) {
            verify((ResolvableSerializer) mockSerializer, times(1)).resolve(mockProvider);
        } else {
            assertTrue(true);
        }
    }
    
    @Test
    public void testCreateContextual() throws JsonMappingException {
        serializer = new StdDelegatingSerializer(mockConverter, mockJavaType, mockSerializer);
        
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        BeanProperty mockProperty = mock(BeanProperty.class);
        
        serializer.createContextual(mockProvider, mockProperty);
        
        verify(mockProvider, times(1)).findValueSerializer(mockJavaType);
        verify(mockProvider, times(1)).handleSecondaryContextualization(any(), eq(mockProperty));
    }
    
    @Test
    public void testGetSchema() throws JsonMappingException {
        serializer = new StdDelegatingSerializer(mockConverter, mockJavaType, mockSerializer);
        
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        Type typeHint = new Type() {};
        
        JsonNode schema = serializer.getSchema(mockProvider, typeHint);
        
        assertNotNull(schema);
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        serializer = new StdDelegatingSerializer(mockConverter, mockJavaType, mockSerializer);
        
        JsonFormatVisitorWrapper mockVisitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        
        serializer.acceptJsonFormatVisitor(mockVisitor, typeHint);
        
        verify(mockSerializer, times(1)).acceptJsonFormatVisitor(mockVisitor, typeHint);
    }
    
    @Test
    public void testConvertValue() {
        Object value = new Object();
        
        serializer = new StdDelegatingSerializer(mockConverter, mockJavaType, mockSerializer);
        
        when(mockConverter.convert(value)).thenReturn(value);
        
        Object result = serializer.convertValue(value);
        
        assertEquals(value, result);
    }
    
}