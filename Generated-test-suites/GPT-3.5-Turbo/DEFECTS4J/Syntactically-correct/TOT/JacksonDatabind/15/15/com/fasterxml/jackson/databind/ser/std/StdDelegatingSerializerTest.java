import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import com.fasterxml.jackson.databind.util.Converter;

import java.io.IOException;
import java.lang.reflect.Type;

public class StdDelegatingSerializerTest {
    private StdDelegatingSerializer serializer;

    @Before
    public void setup() {
        Converter<Object, Object> converter = mock(Converter.class);
        JavaType delegateType = mock(JavaType.class);
        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);

        serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);
    }

    @Test
    public void testSerialize_NormalValue_Success() throws IOException {
        // Arrange
        Object value = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        
        // Act
        serializer.serialize(value, gen, provider);
        
        // Assert
        verify(gen, times(1)).writeObject(value);
        verify(provider, times(1)).defaultSerializeNull(gen);
        verifyNoMoreInteractions(gen);
        verifyNoMoreInteractions(provider);
    }
    
    @Test
    public void testSerialize_NullValue_Success() throws IOException {
        // Arrange
        Object value = null;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        
        // Act
        serializer.serialize(value, gen, provider);
        
        // Assert
        verify(provider, times(1)).defaultSerializeNull(gen);
        verifyNoMoreInteractions(gen);
        verifyNoMoreInteractions(provider);
    }

    @Test
    public void testSerializeWithType_NormalValue_Success() throws IOException {
        // Arrange
        Object value = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);
        
        // Act
        serializer.serializeWithType(value, gen, provider, typeSer);
        
        // Assert
        verify(gen, times(1)).writeObject(value);
        verifyNoMoreInteractions(gen);
        verifyNoMoreInteractions(provider);
        verifyNoMoreInteractions(typeSer);
    }

    @Test
    public void testIsEmpty_NullValue_Success() {
        // Arrange
        Object value = null;
        
        // Act
        boolean result = serializer.isEmpty(value);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testIsEmpty_DelegateSerializerIsEmpty_False() {
        // Arrange
        Object value = new Object();
        when(serializer.getConverter()).thenReturn(mock(Converter.class));
        when(serializer.getDelegatee()).thenReturn(mock(JsonSerializer.class));
        when(serializer.getDelegatee().isEmpty(value)).thenReturn(false);
        
        // Act
        boolean result = serializer.isEmpty(value);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testIsEmpty_DelegateSerializerIsEmpty_True() {
        // Arrange
        Object value = new Object();
        when(serializer.getConverter()).thenReturn(mock(Converter.class));
        when(serializer.getDelegatee()).thenReturn(mock(JsonSerializer.class));
        when(serializer.getDelegatee().isEmpty(value)).thenReturn(true);
        
        // Act
        boolean result = serializer.isEmpty(value);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testIsEmpty_ProviderDelegateSerializerIsEmpty_False() {
        // Arrange
        Object value = new Object();
        SerializerProvider prov = mock(SerializerProvider.class);
        when(serializer.getConverter()).thenReturn(mock(Converter.class));
        when(serializer.getDelegatee()).thenReturn(mock(JsonSerializer.class));
        when(serializer.getDelegatee().isEmpty(prov, value)).thenReturn(false);
        
        // Act
        boolean result = serializer.isEmpty(prov, value);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testIsEmpty_ProviderDelegateSerializerIsEmpty_True() {
        // Arrange
        Object value = new Object();
        SerializerProvider prov = mock(SerializerProvider.class);
        when(serializer.getConverter()).thenReturn(mock(Converter.class));
        when(serializer.getDelegatee()).thenReturn(mock(JsonSerializer.class));
        when(serializer.getDelegatee().isEmpty(prov, value)).thenReturn(true);
        
        // Act
        boolean result = serializer.isEmpty(prov, value);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testGetSchema_SchemaAwareDelegateSerializerExists_SchemaFromDelegateSerializer() throws JsonMappingException {
        // Arrange
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);
        when(serializer.getDelegatee()).thenReturn(mock(SchemaAware.class));
        when(serializer.getDelegatee().getSchema(provider, typeHint))
                .thenReturn(mock(JsonNode.class));
        
        // Act
        JsonNode result = serializer.getSchema(provider, typeHint);
        
        // Assert
        assertNotNull(result);
    }
    
    @Test
    public void testGetSchema_SchemaAwareDelegateSerializerDoesNotExist_SchemaFromSuperclass() throws JsonMappingException {
        // Arrange
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);
        
        // Act
        JsonNode result = serializer.getSchema(provider, typeHint);
        
        // Assert
        assertNotNull(result);
    }
    
    @Test
    public void testGetSchemaWithOptional_SchemaAwareDelegateSerializerExists_SchemaFromDelegateSerializer() throws JsonMappingException {
        // Arrange
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);
        boolean isOptional = true;
        when(serializer.getDelegatee()).thenReturn(mock(SchemaAware.class));
        when(serializer.getDelegatee().getSchema(provider, typeHint, isOptional))
                .thenReturn(mock(JsonNode.class));
        
        // Act
        JsonNode result = serializer.getSchema(provider, typeHint, isOptional);
        
        // Assert
        assertNotNull(result);
    }
    
    @Test
    public void testGetSchemaWithOptional_SchemaAwareDelegateSerializerDoesNotExist_SchemaFromSuperclass() throws JsonMappingException {
        // Arrange
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);
        boolean isOptional = true;
        
        // Act
        JsonNode result = serializer.getSchema(provider, typeHint, isOptional);
        
        // Assert
        assertNotNull(result);
    }
    
    @Test
    public void testAcceptJsonFormatVisitor_ValidVisitor_VisitorAcceptedByDelegateSerializer() throws JsonMappingException {
        // Arrange
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        when(serializer.getDelegatee()).thenReturn(mock(JsonSerializer.class));
        
        // Act
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        // Assert
        verify(serializer.getDelegatee(), times(1)).acceptJsonFormatVisitor(visitor, typeHint);
        verifyNoMoreInteractions(visitor);
    }
    
    @Test
    public void testConvertValue_ValidValue_ConvertedValueReturned() {
        // Arrange
        Object value = new Object();
        Converter<Object, Object> converter = mock(Converter.class);
        when(serializer.getConverter()).thenReturn(converter);
        when(converter.convert(value)).thenReturn(new Object());
        
        // Act
        Object result = serializer.convertValue(value);
        
        // Assert
        assertNotNull(result);
    }
}