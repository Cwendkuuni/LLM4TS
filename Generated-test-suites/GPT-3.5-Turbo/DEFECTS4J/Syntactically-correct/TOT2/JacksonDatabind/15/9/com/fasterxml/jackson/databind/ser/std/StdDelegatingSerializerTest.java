import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdDelegatingSerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

public class StdDelegatingSerializerTest {

    private StdDelegatingSerializer serializer;

    @Before
    public void setUp() {
        serializer = new StdDelegatingSerializer(new Converter<Object, Object>() {
            @Override
            public Object convert(Object value) {
                return value;
            }
        });
    }

    @Test
    public void testSerializeNull() throws IOException {
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        serializer.serialize(null, generator, provider);

        verify(provider).defaultSerializeNull(generator);
    }

    @Test
    public void testSerializeNonNull() throws IOException {
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);

        StdDelegatingSerializer customSerializer = new StdDelegatingSerializer(new Converter<Object, Object>() {
            @Override
            public Object convert(Object value) {
                return value;
            }
        }, TypeFactory.defaultInstance().constructType(Integer.class), delegateSerializer);

        when(delegateSerializer.isEmpty(any())).thenReturn(false);
        
        customSerializer.serialize(42, generator, provider);

        verify(delegateSerializer).serialize(42, generator, provider);
    }

    @Test
    public void testSerializeWithType() throws IOException {
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);

        StdDelegatingSerializer customSerializer = new StdDelegatingSerializer(new Converter<Object, Object>() {
            @Override
            public Object convert(Object value) {
                return value;
            }
        }, TypeFactory.defaultInstance().constructType(String.class), delegateSerializer);

        when(delegateSerializer.isEmpty(any())).thenReturn(false);
        
        customSerializer.serializeWithType("test", generator, provider, null);

        verify(delegateSerializer).serializeWithType("test", generator, provider, null);
    }

    // Additional test cases for edge cases and error scenarios can be added here

}