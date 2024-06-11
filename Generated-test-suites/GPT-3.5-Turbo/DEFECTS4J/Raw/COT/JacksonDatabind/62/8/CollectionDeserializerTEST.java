
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;

public class CollectionDeserializerTest {

    @Test
    public void testDeserialize() throws IOException {
        JavaType collectionType = Mockito.mock(JavaType.class);
        JsonDeserializer<Object> valueDeser = Mockito.mock(JsonDeserializer.class);
        JsonParser p = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Collection<Object> result = new ArrayList<>();
        CollectionDeserializer deserializer = new CollectionDeserializer(collectionType, valueDeser, null, null);

        deserializer.deserialize(p, ctxt, result);

        // Assert that the valueDeserializer is called
        Mockito.verify(valueDeser, Mockito.times(1)).deserialize(p, ctxt);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        JavaType collectionType = Mockito.mock(JavaType.class);
        JsonDeserializer<Object> valueDeser = Mockito.mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeser = Mockito.mock(TypeDeserializer.class);
        JsonParser p = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Collection<Object> result = new ArrayList<>();
        CollectionDeserializer deserializer = new CollectionDeserializer(collectionType, valueDeser, valueTypeDeser, null);

        deserializer.deserializeWithType(p, ctxt, valueTypeDeser);

        // Assert that the valueDeserializer is called with type deserializer
        Mockito.verify(valueDeser, Mockito.times(1)).deserializeWithType(p, ctxt, valueTypeDeser);
    }

    @Test
    public void testDeserializeThrowsException() throws IOException {
        JavaType collectionType = Mockito.mock(JavaType.class);
        JsonDeserializer<Object> valueDeser = Mockito.mock(JsonDeserializer.class);
        JsonParser p = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Collection<Object> result = new ArrayList<>();
        CollectionDeserializer deserializer = new CollectionDeserializer(collectionType, valueDeser, null, null);

        Mockito.when(valueDeser.deserialize(p, ctxt)).thenThrow(new IOException("testException"));

        try {
            deserializer.deserialize(p, ctxt, result);
        } catch (IOException e) {
            Assert.assertEquals("testException", e.getMessage());
        }
    }

}
