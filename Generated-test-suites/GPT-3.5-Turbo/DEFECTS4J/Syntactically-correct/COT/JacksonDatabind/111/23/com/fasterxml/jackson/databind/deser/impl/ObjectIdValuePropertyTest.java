
import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectIdValuePropertyTest {

    @Test
    public void testDeserializeAndSet_NullValue() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        JsonParser parser = new JsonParser();
        DeserializationContext context = new DeserializationContext();
        Object instance = new Object();
    
        property.deserializeAndSet(parser, context, instance);
    
        assertNull(property.deserializeSetAndReturn(parser, context, instance));
    }

    @Test
    public void testDeserializeAndSet_NonNullValue() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        JsonParser parser = new JsonParser();
        DeserializationContext context = new DeserializationContext();
        Object instance = new Object();
        Object id = new Object();
        when(parser.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(property._valueDeserializer.deserialize(parser, context)).thenReturn(id);
        ReadableObjectId roid = new ReadableObjectId(null, null, null);
        when(context.findObjectId(id, null, null)).thenReturn(roid);
    
        Object result = property.deserializeSetAndReturn(parser, context, instance);
    
        assertNotNull(result);
        assertTrue(result instanceof Object);
        assertEquals(instance, roid.item);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet_NoIdProperty() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        Object instance = new Object();
        Object value = new Object();
    
        property.setAndReturn(instance, value);
    }

    @Test
    public void testSet_WithIdProperty() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        SettableBeanProperty idProperty = new SettableBeanProperty(null, null);
        ObjectIdValueProperty spyProperty = spy(property);
        doReturn(idProperty).when(spyProperty)._objectIdReader.idProperty;
        Object instance = new Object();
        Object value = new Object();
        when(idProperty.setAndReturn(instance, value)).thenReturn(instance);
    
        Object result = spyProperty.setAndReturn(instance, value);
    
        assertNotNull(result);
        assertTrue(result instanceof Object);
        assertEquals(instance, result);
    }
}

