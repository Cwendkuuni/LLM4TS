import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

    @Test
    public void testDeserializeAndSet_WithNullValue_ShouldReturnNull() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        assertNull(property.deserializeSetAndReturn(null, null, null));
    }
    
    @Test
    public void testDeserializeAndSet_WithNonNullValue_ShouldReturnInstance() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        Object instance = new Object();
        assertNotNull(property.deserializeSetAndReturn(null, null, instance));    
    }
    
    @Test
    public void testDeserializeAndSet_WithNullToken_ShouldReturnNull() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        JsonParser p = new JsonParser();
        p.setToken(JsonToken.VALUE_NULL);
        assertNull(property.deserializeSetAndReturn(p, null, null));
    }
    
    @Test
    public void testDeserializeAndSet_WithNonNullToken_ShouldReturnInstance() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        Object instance = new Object();
        JsonParser p = new JsonParser();
        p.setToken(JsonToken.VALUE_STRING);
        assertNotNull(property.deserializeSetAndReturn(p, null, instance));
    }
    
    @Test(expected = IOException.class)
    public void testDeserializeAndSet_WithThrowingDeserializer_ShouldThrowIOException() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, new ThrowingDeserializer());
        property.deserializeSetAndReturn(null, null, null);
    }
    
    @Test
    public void testDeserializeAndSet_WithObjectIdReader_ShouldReturnInstance() {
        ObjectIdReader reader = new ObjectIdReader(null, null, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(reader, null);
        Object instance = new Object();
        JsonParser p = new JsonParser();
        p.setToken(JsonToken.VALUE_STRING);
        assertNotNull(property.deserializeSetAndReturn(p, null, instance));
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturn_WithoutIdProperty_ShouldThrowUnsupportedOperationException() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        property.setAndReturn(null, null);
    }
    
    @Test
    public void testSetAndReturn_WithIdProperty_ShouldReturnInstance() throws IOException {
        SettableBeanProperty idProperty = new SettableBeanProperty(null, null, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        assertNotNull(property.setAndReturn(idProperty, null));
    }
    
    @Test
    public void testWithNullProvider_ShouldReturnNewProperty() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        Object nva = new NullValueProvider();
        assertEquals(property.withNullProvider(nva), property);
    }

    @Test
    public void testWithNullProvider_ShouldReturnDifferentProperty() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        NullValueProvider nva = new NullValueProvider();
        assertNotEquals(property.withNullProvider(nva), property);
    }
    
    @Test
    public void testWithNullProvider_ShouldBeSameSerialVersionUID() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        NullValueProvider nva = new NullValueProvider();
        assertEquals(property.withNullProvider(nva).serialVersionUID, property.serialVersionUID);
    }
    
    @Test
    public void testWithNullProvider_ShouldKeepObjectIdReader() {
        ObjectIdReader reader = new ObjectIdReader(null, null, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(reader, null);
        NullValueProvider nva = new NullValueProvider();
        assertEquals(property.withNullProvider(nva)._objectIdReader, property._objectIdReader);
    }
    
    @Test
    public void testWithNullProvider_ShouldKeepDeserializer() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, new ThrowingDeserializer());
        NullValueProvider nva = new NullValueProvider();
        assertEquals(property.withNullProvider(nva)._valueDeserializer, property._valueDeserializer);
    }
    
    @Test
    public void testWithNullProvider_ShouldKeepNewName() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        PropertyName newName = new PropertyName();
        assertEquals(property.withNullProvider(newName).getFullName(), newName);
    }

    @Test
    public void testWithNullProvider_ShouldKeepObjectIdReaderWhenChangingName() {
        ObjectIdReader reader = new ObjectIdReader(null, null, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(reader, null);
        PropertyName newName = new PropertyName();
        assertEquals(property.withNullProvider(newName)._objectIdReader, property._objectIdReader);
    }

}