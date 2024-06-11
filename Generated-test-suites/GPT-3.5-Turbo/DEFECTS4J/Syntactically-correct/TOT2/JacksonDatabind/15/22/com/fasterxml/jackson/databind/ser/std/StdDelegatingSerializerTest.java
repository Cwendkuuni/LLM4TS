import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StdDelegatingSerializerTest {
    
    @Test
    public void testSerialize_withNonNullValue() throws IOException {
        Converter<String, Integer> converter = mock(Converter.class);
        JsonSerializer<Integer> delegateSerializer = mock(JsonSerializer.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, TypeFactory.defaultInstance().constructType(Integer.class), delegateSerializer);

        String value = "123";
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        when(converter.convert(value)).thenReturn(123);
        
        serializer.serialize(value, generator, provider);
        
        verify(delegateSerializer).serialize(123, generator, provider);
    }
    
    @Test
    public void testSerialize_withNullValue() throws IOException {
        Converter<String, Integer> converter = mock(Converter.class);
        JsonSerializer<Integer> delegateSerializer = mock(JsonSerializer.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, TypeFactory.defaultInstance().constructType(Integer.class), delegateSerializer);

        String value = null;
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        when(converter.convert(value)).thenReturn(null);
        
        serializer.serialize(value, generator, provider);
        
        verify(provider).defaultSerializeNull(generator);
    }
    
    // Add more test cases for other methods as needed to cover different scenarios
    
}