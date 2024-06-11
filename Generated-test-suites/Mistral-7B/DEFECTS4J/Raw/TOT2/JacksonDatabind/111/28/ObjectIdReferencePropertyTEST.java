import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReferenceProperty;

public class ObjectIdReferencePropertyTest {

    @Test
    public void testObjectIdReferencePropertyConstructor() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null, null);
        SettableBeanProperty forward = new SettableBeanProperty(null, null, null);
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
        assertNotNull(objectIdReferenceProperty);
        assertEquals(forward, objectIdReferenceProperty._forward);
        assertEquals(objectIdInfo, objectIdReferenceProperty._objectIdInfo);
    }

    @Test(expected = NullPointerException.class)
    public void testObjectIdReferencePropertyConstructorWithNullForward() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null, null);
        SettableBeanProperty forward = null;
        new ObjectIdReferenceProperty(forward, objectIdInfo);
    }

    @Test
    public void testObjectIdReferencePropertyConstructorWithNullObjectIdInfo() {
        SettableBeanProperty forward = new SettableBeanProperty(null, null, null);
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, null);
        assertNotNull(objectIdReferenceProperty);
        assertNull(objectIdReferenceProperty._objectIdInfo);
    }

    @Test
    public void testSettableBeanPropertyWithName() {
        SettableBeanProperty forward = new SettableBeanProperty(null, null, null);
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, new PropertyName("name"));
        assertEquals("name", objectIdReferenceProperty.getName());
    }

    @Test
    public void testSettableBeanPropertyWithValueDeserializer() {
        SettableBeanProperty forward = new SettableBeanProperty(null, null, null);
        JsonDeserializer<String> deserializer = new StandardDeserializer<>();
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, deserializer, null);
        assertEquals(deserializer, objectIdReferenceProperty.getValueDeserializer());
    }

    @Test
    public void testSettableBeanPropertyWithNullProvider() {
        SettableBeanProperty forward = new SettableBeanProperty(null, null, null);
        NullValueProvider nva = new NullValueProvider();
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, null, nva);
        assertEquals(nva, objectIdReferenceProperty.getNullProvider());
    }
}