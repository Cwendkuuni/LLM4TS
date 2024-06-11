import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.std.StdDelegatingSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Test;
import java.io.IOException;
import java.lang.reflect.Type;

import static org.mockito.Mockito.*;

public class StdDelegatingSerializerTest {

    @Test
    public void testSerialize_WhenDelegateValueIsNull_ShouldCallDefaultSerializeNull() throws IOException {
        // Arrange
        Converter<Object, Object> converter = mock(Converter.class);
        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, null, delegateSerializer);
        JsonSerializer<Object> spySerializer = spy(serializer);

        Object value = new Object();
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        // Act
        when(converter.convert(value)).thenReturn(null);
        spySerializer.serialize(value, generator, provider);

        // Assert
        verify(provider, times(1)).defaultSerializeNull(generator);
    }

    @Test
    public void testSerialize_WhenDelegateValueIsValid_ShouldCallDelegateSerializer() throws IOException {
        // Arrange
        Converter<Object, Object> converter = mock(Converter.class);
        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, null, delegateSerializer);
        JsonSerializer<Object> spySerializer = spy(serializer);

        Object value = new Object();
        Object delegateValue = new Object();
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        // Act
        when(converter.convert(value)).thenReturn(delegateValue);
        spySerializer.serialize(value, generator, provider);

        // Assert
        verify(delegateSerializer, times(1)).serialize(delegateValue, generator, provider);
    }

    @Test
    public void testSerializeWithType_WhenDelegateValueIsValid_ShouldCallDelegateSerializer() throws IOException {
        // Arrange
        Converter<Object, Object> converter = mock(Converter.class);
        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, null, delegateSerializer);
        JsonSerializer<Object> spySerializer = spy(serializer);

        Object value = new Object();
        Object delegateValue = new Object();
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        TypeSerializer typeSerializer = mock(TypeSerializer.class);

        // Act
        when(converter.convert(value)).thenReturn(delegateValue);
        spySerializer.serializeWithType(value, generator, provider, typeSerializer);

        // Assert
        verify(delegateSerializer, times(1)).serializeWithType(delegateValue, generator, provider, typeSerializer);
    }

    @Test
    public void testIsEmpty_WhenDelegateValueIsEmpty_ShouldReturnTrue() throws IOException {
        // Arrange
        Converter<Object, Object> converter = mock(Converter.class);
        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, null, delegateSerializer);
        JsonSerializer<Object> spySerializer = spy(serializer);

        Object value = new Object();
        Object delegateValue = new Object();

        // Act
        when(converter.convert(value)).thenReturn(delegateValue);
        when(delegateSerializer.isEmpty(delegateValue)).thenReturn(true);
        boolean result = spySerializer.isEmpty(value);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsEmpty_WhenDelegateValueIsNotEmpty_ShouldReturnFalse() throws IOException {
        // Arrange
        Converter<Object, Object> converter = mock(Converter.class);
        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, null, delegateSerializer);
        JsonSerializer<Object> spySerializer = spy(serializer);

        Object value = new Object();
        Object delegateValue = new Object();

        // Act
        when(converter.convert(value)).thenReturn(delegateValue);
        when(delegateSerializer.isEmpty(delegateValue)).thenReturn(false);
        boolean result = spySerializer.isEmpty(value);

        // Assert
        assertFalse(result);
    }

}