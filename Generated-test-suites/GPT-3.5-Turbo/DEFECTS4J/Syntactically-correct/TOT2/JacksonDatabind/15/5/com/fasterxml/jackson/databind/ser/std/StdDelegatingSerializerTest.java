import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StdDelegatingSerializerTest {

    @Mock
    private Converter<Object, ?> mockConverter;

    @Mock
    private JavaType mockDelegateType;

    @Mock
    private JsonSerializer<Object> mockDelegateSerializer;

    @Test
    public void testSerialize_NullValue() throws IOException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(mockConverter, mockDelegateType, mockDelegateSerializer);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        serializer.serialize(null, gen, provider);

        verify(provider, times(1)).defaultSerializeNull(gen);
        verifyNoMoreInteractions(gen, provider);
    }

    @Test
    public void testSerialize_WithValue() throws IOException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(mockConverter, mockDelegateType, mockDelegateSerializer);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Object value = new Object();

        serializer.serialize(value, gen, provider);

        verify(mockConverter, times(1)).convert(value);
        verify(mockDelegateSerializer, times(1)).serialize(any(), eq(gen), eq(provider));
        verifyNoMoreInteractions(gen, provider, mockConverter, mockDelegateSerializer);
    }

    @Test
    public void testSerializeWithType() throws IOException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(mockConverter, mockDelegateType, mockDelegateSerializer);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);
        Object value = new Object();

        serializer.serializeWithType(value, gen, provider, typeSer);

        verify(mockConverter, times(1)).convert(value);
        verify(mockDelegateSerializer, times(1)).serializeWithType(any(), eq(gen), eq(provider), eq(typeSer));
        verifyNoMoreInteractions(gen, provider, mockConverter, mockDelegateSerializer, typeSer);
    }

    @Test
    public void testIsEmpty() {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(mockConverter, mockDelegateType, mockDelegateSerializer);
        Object value = new Object();

        serializer.isEmpty(value);

        verify(mockConverter, times(1)).convert(value);
        verify(mockDelegateSerializer, times(1)).isEmpty(any());
        verifyNoMoreInteractions(mockConverter, mockDelegateSerializer);
    }

    @Test
    public void testIsEmptyWithProvider() {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(mockConverter, mockDelegateType, mockDelegateSerializer);
        Object value = new Object();
        SerializerProvider provider = mock(SerializerProvider.class);

        serializer.isEmpty(provider, value);

        verify(mockConverter, times(1)).convert(value);
        verify(mockDelegateSerializer, times(1)).isEmpty(eq(provider), any());
        verifyNoMoreInteractions(mockConverter, mockDelegateSerializer);
    }

    // Additional test cases for edge cases and exception handling scenarios can be added here

}