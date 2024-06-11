import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class CollectionDeserializerTest {

    @Test
    public void testDeserialize() throws IOException {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(Collection.class,
                Mockito.mock(JsonDeserializer.class), Mockito.mock(TypeDeserializer.class),
                Mockito.mock(ValueInstantiator.class));
        collectionDeserializer.deserialize(jsonParser, deserializationContext);
        Mockito.verify(jsonParser, Mockito.times(1)).isExpectedStartArrayToken();
    }
    
    @Test
    public void testDeserializeWithType() throws IOException {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = Mockito.mock(TypeDeserializer.class);
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(Collection.class,
                Mockito.mock(JsonDeserializer.class), Mockito.mock(TypeDeserializer.class),
                Mockito.mock(ValueInstantiator.class));
        collectionDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        Mockito.verify(typeDeserializer, Mockito.times(1)).deserializeTypedFromArray(jsonParser, deserializationContext);
    }

    @Test
    public void testHandleNonArray() throws IOException {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(Collection.class,
                Mockito.mock(JsonDeserializer.class), Mockito.mock(TypeDeserializer.class),
                Mockito.mock(ValueInstantiator.class));
        collectionDeserializer.handleNonArray(jsonParser, deserializationContext, Mockito.mock(Collection.class));
        Mockito.verify(jsonParser, Mockito.times(1)).getCurrentToken();
    }
}