import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ser.SerializerProvider;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import java.io.IOException;

public class StdDelegatingSerializerTest {

    @Test
    public void testSerialize() throws IOException {
        // Arrange
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(new Converter<Object, Object>() {
            public Object convert(Object value) {
                return value.toString();
            }
        });
        JsonSerializer<Object> delegateSerializer = Mockito.mock(JsonSerializer.class);
        serializer._delegateSerializer = delegateSerializer;
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        Object value = "test";

        // Act
        serializer.serialize(value, gen, provider);

        // Assert
        Mockito.verify(delegateSerializer).serialize(value.toString(), gen, provider);
    }

    @Test
    public void testSerializeWithType() throws IOException {
        // Arrange
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(new Converter<Object, Object>() {
            public Object convert(Object value) {
                return value.toString();
            }
        });
        JsonSerializer<Object> delegateSerializer = Mockito.mock(JsonSerializer.class);
        serializer._delegateSerializer = delegateSerializer;
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        TypeSerializer typeSer = Mockito.mock(TypeSerializer.class);
        Object value = "test";

        // Act
        serializer.serializeWithType(value, gen, provider, typeSer);

        // Assert
        Mockito.verify(delegateSerializer).serializeWithType(value.toString(), gen, provider, typeSer);
    }

    // Add more test cases for other methods as needed...

}