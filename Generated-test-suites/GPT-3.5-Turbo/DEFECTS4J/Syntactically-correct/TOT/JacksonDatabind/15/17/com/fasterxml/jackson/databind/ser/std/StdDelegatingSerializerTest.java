import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import com.fasterxml.jackson.databind.util.Converter;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;

import static org.mockito.Mockito.*;

public class StdDelegatingSerializerTest {

    private StdDelegatingSerializer delegatingSerializer;
    private Converter<Object, ?> converter;
    private JavaType delegateType;
    private JsonSerializer<?> delegateSerializer;

    @Before
    public void setup() {
        converter = mock(Converter.class);
        delegateType = TypeFactory.defaultInstance().constructType(String.class);
        delegateSerializer = mock(JsonSerializer.class);
        delegatingSerializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);
    }

    @Test
    public void givenNullValue_shouldSerializeNull() throws IOException {
        // Arrange
        Object value = null;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        
        // Act
        delegatingSerializer.serialize(value, gen, provider);

        // Assert
        verify(provider, times(1)).defaultSerializeNull(gen);
    }

    @Test
    public void givenValue_shouldUseDelegateSerializer() throws IOException {
        // Arrange
        Object value = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        // Act
        delegatingSerializer.serialize(value, gen, provider);

        // Assert
        verify(delegateSerializer, times(1)).serialize(value, gen, provider);
    }

    @Test
    public void givenDelegateSerializer_implementingResolvableSerializer_shouldResolve()
            throws IOException, JsonMappingException {
        // Arrange
        Object value = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        when(delegateSerializer instanceof ResolvableSerializer).thenReturn(true);

        // Act
        delegatingSerializer.serialize(value, gen, provider);

        // Assert
        verify((ResolvableSerializer) delegateSerializer, times(1)).resolve(provider);
    }

    @Test
    public void givenDelegateSerializer_notImplementingResolvableSerializer_shouldNotResolve()
            throws IOException, JsonMappingException {
        // Arrange
        Object value = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        when(delegateSerializer instanceof ResolvableSerializer).thenReturn(false);

        // Act
        delegatingSerializer.serialize(value, gen, provider);

        // Assert
        verify(delegateSerializer, never()).resolve(provider);
    }

    @Test
    @Deprecated
    public void givenValue_shouldCheckIfDelegateSerializerIsEmpty() {
        // Arrange
        Object value = new Object();

        // Act
        delegatingSerializer.isEmpty(value);

        // Assert
        verify(delegateSerializer, times(1)).isEmpty(value);
    }

    @Test
    public void givenValueAndSerializerProvider_shouldCheckIfDelegateSerializerIsEmpty() {
        // Arrange
        Object value = new Object();
        SerializerProvider prov = mock(SerializerProvider.class);

        // Act
        delegatingSerializer.isEmpty(prov, value);

        // Assert
        verify(delegateSerializer, times(1)).isEmpty(prov, value);
    }

    @Test
    public void givenSchemaAwareDelegateSerializer_shouldGetSchema() throws JsonMappingException {
        // Arrange
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);
        SchemaAware schemaAwareDelegateSerializer = mock(SchemaAware.class);
        when(delegateSerializer instanceof SchemaAware).thenReturn(true);
        when(delegateSerializer).thenReturn(schemaAwareDelegateSerializer);

        // Act
        delegatingSerializer.getSchema(provider, typeHint);

        // Assert
        verify(schemaAwareDelegateSerializer, times(1)).getSchema(provider, typeHint);
    }

    @Test
    public void givenNonSchemaAwareDelegateSerializer_shouldGetSchemaFromSuper() throws JsonMappingException {
        // Arrange
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);
        when(delegateSerializer instanceof SchemaAware).thenReturn(false);

        // Act
        delegatingSerializer.getSchema(provider, typeHint);

        // Assert
        verify(delegateSerializer, times(1)).getSchema(provider, typeHint);
    }

    @Test
    public void givenSchemaAwareDelegateSerializer_shouldGetSchemaWithIsOptional() throws JsonMappingException {
        // Arrange
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);
        boolean isOptional = true;
        SchemaAware schemaAwareDelegateSerializer = mock(SchemaAware.class);
        when(delegateSerializer instanceof SchemaAware).thenReturn(true);
        when(delegateSerializer).thenReturn(schemaAwareDelegateSerializer);

        // Act
        delegatingSerializer.getSchema(provider, typeHint, isOptional);

        // Assert
        verify(schemaAwareDelegateSerializer, times(1)).getSchema(provider, typeHint, isOptional);
    }

    @Test
    public void givenNonSchemaAwareDelegateSerializer_shouldGetSchemaWithIsOptionalFromSuper() throws JsonMappingException {
        // Arrange
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);
        boolean isOptional = true;
        when(delegateSerializer instanceof SchemaAware).thenReturn(false);

        // Act
        delegatingSerializer.getSchema(provider, typeHint, isOptional);

        // Assert
        verify(delegateSerializer, times(1)).getSchema(provider, typeHint);
    }

    @Test
    public void givenJsonFormatVisitorWithJavaType_shouldAcceptJsonFormatVisitor() throws JsonMappingException {
        // Arrange
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        // Act
        delegatingSerializer.acceptJsonFormatVisitor(visitor, typeHint);

        // Assert
        verify(delegateSerializer, times(1)).acceptJsonFormatVisitor(visitor, typeHint);
    }

    @Test
    public void givenValue_shouldConvertValueUsingConverter() {
        // Arrange
        Object value = new Object();

        // Act
        delegatingSerializer.convertValue(value);

        // Assert
        verify(converter, times(1)).convert(value);
    }
}