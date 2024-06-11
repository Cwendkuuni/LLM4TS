import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.std.StdDelegatingSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StdDelegatingSerializerTest {

    @Mock
    private JsonGenerator mockGenerator;

    @Mock
    private SerializerProvider mockProvider;

    @Mock
    private Converter<Object, String> mockConverter;

    @Mock
    private JavaType mockType;

    @Mock
    private JsonSerializer<Object> mockDelegateSerializer;

    private StdDelegatingSerializer delegatingSerializer;

    public void setUp() {
        MockitoAnnotations.initMocks(this);
        delegatingSerializer = new StdDelegatingSerializer(mockConverter, mockType, mockDelegateSerializer);
    }

    @Test
    public void testSerialize_WhenDelegateValueIsNull_ShouldCallDefaultSerializeNull() throws IOException {
        // Arrange
        Object value = new Object();
        when(mockConverter.convert(value)).thenReturn(null);

        // Act
        delegatingSerializer.serialize(value, mockGenerator, mockProvider);

        // Assert
        verify(mockProvider).defaultSerializeNull(mockGenerator);
    }

    @Test
    public void testSerialize_WhenDelegateValueIsNotNull_ShouldCallDelegateSerializer() throws IOException {
        // Arrange
        Object value = new Object();
        Object delegateValue = new Object();
        when(mockConverter.convert(value)).thenReturn(delegateValue);

        // Act
        delegatingSerializer.serialize(value, mockGenerator, mockProvider);

        // Assert
        verify(mockDelegateSerializer).serialize(delegateValue, mockGenerator, mockProvider);
    }

    @Test
    public void testSerializeWithType_ShouldCallDelegateSerializer() throws IOException {
        // Arrange
        Object value = new Object();
        Object delegateValue = new Object();
        when(mockConverter.convert(value)).thenReturn(delegateValue);

        // Act
        delegatingSerializer.serializeWithType(value, mockGenerator, mockProvider, null);

        // Assert
        verify(mockDelegateSerializer).serializeWithType(delegateValue, mockGenerator, mockProvider, null);
    }

    @Test
    public void testIsEmpty_ShouldCallDelegateSerializer() {
        // Arrange
        Object value = new Object();
        Object delegateValue = new Object();
        when(mockConverter.convert(value)).thenReturn(delegateValue);
        when(mockDelegateSerializer.isEmpty(delegateValue)).thenReturn(true);

        // Act
        boolean result = delegatingSerializer.isEmpty(mockProvider, value);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testGetSchema_WhenDelegateSerializerIsSchemaAware_ShouldCallGetSchema() throws JsonMappingException {
        // Arrange
        when(mockDelegateSerializer instanceof SchemaAware).thenReturn(true);
        when(((SchemaAware) mockDelegateSerializer).getSchema(mockProvider, mockType)).thenReturn(null);

        // Act
        JsonNode result = delegatingSerializer.getSchema(mockProvider, mockType);

        // Assert
        Assert.assertNull(result);
    }

    @Test
    public void testGetSchema_WhenDelegateSerializerIsNotSchemaAware_ShouldCallSuperGetSchema() throws JsonMappingException {
        // Arrange
        when(mockDelegateSerializer instanceof SchemaAware).thenReturn(false);

        // Act
        JsonNode result = delegatingSerializer.getSchema(mockProvider, mockType);

        // Assert
        Assert.assertNull(result);
    }

    @Test
    public void testAcceptJsonFormatVisitor_ShouldCallDelegateSerializer() throws JsonMappingException {
        // Arrange
        when(mockDelegateSerializer).thenReturn(true);

        // Act
        delegatingSerializer.acceptJsonFormatVisitor(mockVisitor, mockType);

        // Assert
        verify(mockDelegateSerializer).acceptJsonFormatVisitor(mockVisitor, mockType);
    }

}