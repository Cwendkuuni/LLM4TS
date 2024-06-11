import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class StdDelegatingSerializerTest {

    private JsonSerializer delegateSerializer = mock(JsonSerializer.class);
    private Converter converter = mock(Converter.class);
    private StdDelegatingSerializer delegatingSerializer = new StdDelegatingSerializer(converter, JavaType.missingType(), delegateSerializer);
    private Object value = new Object();
    private JsonGenerator gen = mock(JsonGenerator.class);
    private SerializerProvider provider = mock(SerializerProvider.class);
    private TypeSerializer typeSer = mock(TypeSerializer.class);

    @Test
    void shouldSerializeNull() throws IOException {
        when(converter.convert(null)).thenReturn(null);

        delegatingSerializer.serialize(null, gen, provider);

        verify(provider).defaultSerializeNull(gen);
    }

    @Test
    void shouldSerializeNonNullValue() throws IOException {
        Object convertedValue = new Object();
        when(converter.convert(value)).thenReturn(convertedValue);

        delegatingSerializer.serialize(value, gen, provider);

        verify(delegateSerializer).serialize(convertedValue, gen, provider);
    }

    @Test
    void shouldSerializeWithType() throws IOException {
        Object convertedValue = new Object();
        when(converter.convert(value)).thenReturn(convertedValue);

        delegatingSerializer.serializeWithType(value, gen, provider, typeSer);

        verify(delegateSerializer).serializeWithType(convertedValue, gen, provider, typeSer);
    }

    @Test
    void shouldCheckIfEmpty() {
         Object convertedValue = new Object();
         when(converter.convert(value)).thenReturn(convertedValue);
         when(delegateSerializer.isEmpty(provider, convertedValue)).thenReturn(true);

         delegatingSerializer.isEmpty(provider, value);

         verify(delegateSerializer).isEmpty(provider, convertedValue);
    }
    
    // other test methods for the remaining methods in the class
}