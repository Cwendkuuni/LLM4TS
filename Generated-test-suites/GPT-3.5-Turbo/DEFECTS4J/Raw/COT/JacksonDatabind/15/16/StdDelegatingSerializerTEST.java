
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class StdDelegatingSerializerTest {

    private StdDelegatingSerializer delegatingSerializer;

    @Mock
    private Converter<Object, ?> converter;

    @Mock
    private JavaType delegateType;

    @Mock
    private JsonSerializer<Object> delegateSerializer;

    @Mock
    private JsonGenerator generator;

    @Mock
    private SerializerProvider provider;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        delegatingSerializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);
    }

    @Test
    public void testSerialize_WhenDelegateValueIsNull_ShouldCallDefaultSerializeNull() throws IOException {
        // Arrange
        Object value = new Object();
        Object delegateValue = null;
        when(converter.convert(value)).thenReturn(delegateValue);

        // Act
        delegatingSerializer.serialize(value, generator, provider);

        // Assert
        verify(provider).defaultSerializeNull(generator);
    }

    @Test
    public void testSerialize_WhenDelegateValueIsNotNull_ShouldCallDelegateSerializer() throws IOException {
        // Arrange
        Object value = new Object();
        Object delegateValue = new Object();
        when(converter.convert(value)).thenReturn(delegateValue);

        // Act
        delegatingSerializer.serialize(value, generator, provider);

        // Assert
        verify(delegateSerializer).serialize(delegateValue, generator, provider);
    }

    @Test
    public void testSerializeWithType_ShouldCallDelegateSerializerSerializeWithType() throws IOException {
        // Arrange
        Object value = new Object();
        Object delegateValue = new Object();
        when(converter.convert(value)).thenReturn(delegateValue);

        // Act
        delegatingSerializer.serializeWithType(value, generator, provider, null);

        // Assert
        verify(delegateSerializer).serializeWithType(delegateValue, generator, provider, null);
    }

    @Test
    public void testIsEmpty_WhenDelegateValueIsEmpty_ShouldReturnTrue() {
        // Arrange
        Object value = new Object();
        Object delegateValue = new Object();
        when(converter.convert(value)).thenReturn(delegateValue);
        when(delegateSerializer.isEmpty(delegateValue)).thenReturn(true);

        // Act
        boolean result = delegatingSerializer.isEmpty(provider, value);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsEmpty_WhenDelegateValueIsNotEmpty_ShouldReturnFalse() {
        // Arrange
        Object value = new Object();
        Object delegateValue = new Object();
        when(converter.convert(value)).thenReturn(delegateValue);
        when(delegateSerializer.isEmpty(delegateValue)).thenReturn(false);

        // Act
        boolean result = delegatingSerializer.isEmpty(provider, value);

        // Assert
        assertFalse(result);
    }
}

