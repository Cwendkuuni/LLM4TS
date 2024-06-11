import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.lang.reflect.Type;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class StdDelegatingSerializerTest {

    @Mock
    private Converter<Object, ?> converterMock;

    @Mock
    private JavaType delegateTypeMock;

    @Mock
    private JsonSerializer<Object> delegateSerializerMock;

    @Mock
    private Object valueMock;

    @Mock
    private JsonGenerator generatorMock;

    @Mock
    private SerializerProvider providerMock;

    @Mock
    private TypeSerializer typeSerializerMock;

    private StdDelegatingSerializer stdDelegatingSerializer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        stdDelegatingSerializer = new StdDelegatingSerializer(converterMock, delegateTypeMock, delegateSerializerMock);
    }

    @After
    public void tearDown() {
        stdDelegatingSerializer = null;
        reset(converterMock, delegateTypeMock, delegateSerializerMock, valueMock, generatorMock, providerMock, typeSerializerMock);
    }

    @Test
    public void serialize_NullValue_ShouldCallDefaultSerializeNull() throws IOException {
        // Arrange
        when(converterMock.convert(any())).thenReturn(null);
        // Act
        stdDelegatingSerializer.serialize(valueMock, generatorMock, providerMock);
        // Assert
        verify(providerMock, times(1)).defaultSerializeNull(generatorMock);
    }

    @Test
    public void serialize_WithValue_ShouldCallDelegateSerializerSerialize() throws IOException {
        // Arrange
        when(converterMock.convert(any())).thenReturn(valueMock);
        // Act
        stdDelegatingSerializer.serialize(valueMock, generatorMock, providerMock);
        // Assert
        verify(delegateSerializerMock, times(1)).serialize(valueMock, generatorMock, providerMock);
    }

    @Test
    public void serializeWithType_WithValue_ShouldCallDelegateSerializerSerializeWithType() throws IOException {
        // Arrange
        when(converterMock.convert(any())).thenReturn(valueMock);
        // Act
        stdDelegatingSerializer.serializeWithType(valueMock, generatorMock, providerMock, typeSerializerMock);
        // Assert
        verify(delegateSerializerMock, times(1)).serializeWithType(valueMock, generatorMock, providerMock, typeSerializerMock);
    }

    @Test
    public void isEmpty_WithDelegateValue_ShouldCallDelegateSerializerIsEmpty() {
        // Arrange
        when(converterMock.convert(any())).thenReturn(valueMock);
        when(delegateSerializerMock.isEmpty(any())).thenReturn(true);
        // Act
        stdDelegatingSerializer.isEmpty(providerMock, valueMock);
        // Assert
        verify(delegateSerializerMock, times(1)).isEmpty(valueMock);
    }

    @Test
    public void isEmpty_WithoutDelegateValue_ShouldReturnDelegateSerializerIsEmpty() {
        // Arrange
        when(converterMock.convert(any())).thenReturn(null);
        when(delegateSerializerMock.isEmpty(any())).thenReturn(false);
        // Act
        boolean result = stdDelegatingSerializer.isEmpty(providerMock, valueMock);
        // Assert
        verify(delegateSerializerMock, never()).isEmpty(any());
        assert result == false;
    }

    @Test
    public void getSchema_WithSchemaAwareDelegateSerializer_ShouldCallDelegateSerializerGetSchema() throws JsonMappingException {
        // Arrange
        when(delegateSerializerMock instanceof SchemaAware).thenReturn(true);
        when(((SchemaAware) delegateSerializerMock).getSchema(any(), any())).thenReturn(null);
        Type typeHint = Object.class;
        // Act
        stdDelegatingSerializer.getSchema(providerMock, typeHint);
        // Assert
        verify(((SchemaAware) delegateSerializerMock), times(1)).getSchema(providerMock, typeHint);
    }

    @Test
    public void getSchema_WithoutSchemaAwareDelegateSerializer_ShouldReturnSuperGetSchema() throws JsonMappingException {
        // Arrange
        when(delegateSerializerMock instanceof SchemaAware).thenReturn(false);
        when(stdDelegatingSerializer.getSchema(providerMock, Object.class)).thenReturn(null);
        Type typeHint = Object.class;
        // Act
        stdDelegatingSerializer.getSchema(providerMock, typeHint);
        // Assert
        verify(stdDelegatingSerializer, times(1)).getSchema(providerMock, typeHint);
    }

    @Test
    public void getSchema_WithSchemaAwareDelegateSerializer_ShouldCallDelegateSerializerGetSchemaWithOptional()
            throws JsonMappingException {
        // Arrange
        when(delegateSerializerMock instanceof SchemaAware).thenReturn(true);
        when(((SchemaAware) delegateSerializerMock).getSchema(any(), any(), any())).thenReturn(null);
        Type typeHint = Object.class;
        // Act
        stdDelegatingSerializer.getSchema(providerMock, typeHint, false);
        // Assert
        verify(((SchemaAware) delegateSerializerMock), times(1)).getSchema(providerMock, typeHint, false);
    }

    @Test
    public void getSchema_WithoutSchemaAwareDelegateSerializer_ShouldReturnSuperGetSchemaWithOptional()
            throws JsonMappingException {
        // Arrange
        when(delegateSerializerMock instanceof SchemaAware).thenReturn(false);
        when(stdDelegatingSerializer.getSchema(providerMock, Object.class, false)).thenReturn(null);
        Type typeHint = Object.class;
        // Act
        stdDelegatingSerializer.getSchema(providerMock, typeHint, false);
        // Assert
        verify(stdDelegatingSerializer, times(1)).getSchema(providerMock, typeHint, false);
    }

    @Test
    public void acceptJsonFormatVisitor_ShouldCallDelegateSerializerAcceptJsonFormatVisitor() throws JsonMappingException {
        // Arrange
        when(delegateSerializerMock instanceof JsonFormatVisitable).thenReturn(true);
        JavaType typeHint = mock(JavaType.class);
        // Act
        stdDelegatingSerializer.acceptJsonFormatVisitor(visitorMock, typeHint);
        // Assert
        verify(delegateSerializerMock, times(1)).acceptJsonFormatVisitor(visitorMock, typeHint);
    }

    @Test
    public void convertValue_ShouldCallConverterConvert() {
        // Arrange
        when(converterMock.convert(any())).thenReturn(valueMock);
        // Act
        Object result = stdDelegatingSerializer.convertValue(valueMock);
        // Assert
        verify(converterMock, times(1)).convert(valueMock);
        assert result == valueMock;
    }

}