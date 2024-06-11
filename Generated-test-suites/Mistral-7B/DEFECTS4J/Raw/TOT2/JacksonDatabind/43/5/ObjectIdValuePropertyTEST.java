import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {
    // ...

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        JsonDeserializer<?> idDeserializer = mock(JsonDeserializer.class);
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, idDeserializer);
        Object id = new Object();
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object instance = new Object();

        when(objectIdReader.propertyName).thenReturn(PropertyName.construct("testProperty"));
        when(objectIdReader.getIdType()).thenReturn(IdType.LONG);
        when(idDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(id);

        Object result = objectIdValueProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);

        verify(objectIdValueProperty)._objectIdReader.bindItem(instance);
        verify(objectIdReader.idProperty).setAndReturn(instance, id);
        assertEquals(instance, result);
    }
}