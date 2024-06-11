import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class StdDelegatingSerializerTest {

    private StdDelegatingSerializer serializer;
    private SerializerProvider provider;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        serializer = new StdDelegatingSerializer(null);
        provider = mock(SerializerProvider.class);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testSerialize_NullValue_SerializeNullCalled() throws Exception {
        // Arrange
        Object value = null;
        JsonGenerator generator = mock(JsonGenerator.class);

        // Act
        serializer.serialize(value, generator, provider);

        // Assert
        verify(provider).defaultSerializeNull(generator);
    }

    @Test
    public void testSerialize_NonNullValue_ConvertAndDelegateSerializeCalled() throws Exception {
        // Arrange
        Object value = new Object();
        Object convertedValue = new Object();
        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);
        serializer = spy(serializer);
        when(serializer.convertValue(value)).thenReturn(convertedValue);
        when(serializer.getDelegatee()).thenReturn(delegateSerializer);
        JsonGenerator generator = mock(JsonGenerator.class);

        // Act
        serializer.serialize(value, generator, provider);

        // Assert
        verify(serializer).convertValue(value);
        verify(delegateSerializer).serialize(convertedValue, generator, provider);
    }

    @Test
    public void testSerializeWithType_NonNullValue_ConvertAndDelegateSerializeWithTypeCalled() throws Exception {
        // Arrange
        Object value = new Object();
        Object convertedValue = new Object();
        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);
        serializer = spy(serializer);
        when(serializer.convertValue(value)).thenReturn(convertedValue);
        when(serializer.getDelegatee()).thenReturn(delegateSerializer);
        JsonGenerator generator = mock(JsonGenerator.class);
        TypeSerializer typeSerializer = mock(TypeSerializer.class);

        // Act
        serializer.serializeWithType(value, generator, provider, typeSerializer);

        // Assert
        verify(serializer).convertValue(value);
        verify(delegateSerializer).serializeWithType(convertedValue, generator, provider, typeSerializer);
    }

    @Test
    public void testIsEmpty_NonNullValue_ConvertAndDelegateIsEmptyCalled() throws Exception {
        // Arrange
        Object value = new Object();
        Object convertedValue = new Object();
        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);
        serializer = spy(serializer);
        when(serializer.convertValue(value)).thenReturn(convertedValue);
        when(serializer.getDelegatee()).thenReturn(delegateSerializer);
        when(delegateSerializer.isEmpty(convertedValue)).thenReturn(false);

        // Act
        boolean isEmpty = serializer.isEmpty(provider, value);

        // Assert
        verify(serializer).convertValue(value);
        assertFalse(isEmpty);
    }

    @Test
    public void testGetSchema_DelegateSerializerIsSchemaAware_GetSchemaCalledOnDelegateSerializer() throws Exception {
        // Arrange
        Object value = new Object();
        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);
        SchemaAware schemaAwareSerializer = mock(SchemaAware.class);
        serializer = spy(serializer);
        when(serializer.convertValue(value)).thenReturn(value);
        when(serializer.getDelegatee()).thenReturn(delegateSerializer);
        when(delegateSerializer instanceof SchemaAware).thenReturn(true);
        when((SchemaAware) delegateSerializer).getSchema(provider, value.getClass()).thenReturn(null);

        // Act
        JsonNode jsonNode = serializer.getSchema(provider, value.getClass());

        // Assert
        verify(serializer).convertValue(value);
        verify((SchemaAware) delegateSerializer).getSchema(provider, value.getClass());
        assertNull(jsonNode);
    }

    @Test
    public void testGetSchema_DelegateSerializerIsNotSchemaAware_GetSchemaCalledOnSuperClass() throws Exception {
        // Arrange
        Object value = new Object();
        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);
        serializer = spy(serializer);
        when(serializer.convertValue(value)).thenReturn(value);
        when(serializer.getDelegatee()).thenReturn(delegateSerializer);
        when(delegateSerializer instanceof SchemaAware).thenReturn(false);
        when(serializer.getSchema(provider, value.getClass())).thenReturn(null);

        // Act
        JsonNode jsonNode = serializer.getSchema(provider, value.getClass());

        // Assert
        verify(serializer).convertValue(value);
        verify(serializer).getSchema(provider, value.getClass());
        assertNull(jsonNode);
    }

    @Test(expected = IllegalStateException.class)
    public void testWithDelegate_SubClassDoesNotOverrideWithDelegate_ThrowIllegalStateException() throws Exception {
        // Arrange
        Object value = new Object();
        JavaType delegateType = mock(JavaType.class);
        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);

        // Act
        StdDelegatingSerializer subClass = new StdDelegatingSerializer(null) {
            // Do not override withDelegate
        };
        subClass.withDelegate(null, delegateType, delegateSerializer);

        // Assert
        // Expected IllegalStateException
    }
}