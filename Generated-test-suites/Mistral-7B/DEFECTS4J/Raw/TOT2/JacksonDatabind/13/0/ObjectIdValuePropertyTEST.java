import org.junit.Test;
import static org.mockito.Mockito.*;
import com.fasterxml.jackson.databind.*;

public class ObjectIdValuePropertyTest {

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object instance = new Object();

        when(objectIdReader.propertyName).thenReturn(new PropertyName("id"));
        when(objectIdReader.getIdType()).thenReturn(String.class);
        when(objectIdReader.getDeserializer()).thenReturn(mock(JsonDeserializer.class));

        objectIdValueProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        verify(objectIdValueProperty, times(1)).deserializeSetAndReturn(jsonParser, deserializationContext, instance);
    }
}