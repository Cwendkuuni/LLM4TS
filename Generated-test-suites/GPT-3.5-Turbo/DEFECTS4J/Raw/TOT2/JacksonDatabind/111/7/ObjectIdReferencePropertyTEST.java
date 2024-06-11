import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ObjectIdReferencePropertyTest {

    private ObjectIdReferenceProperty objectIdReferenceProperty;

    @Before
    public void setUp() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
    }

    @Test
    public void testWithName() {
        PropertyName newName = new PropertyName("newName");
        SettableBeanProperty result = objectIdReferenceProperty.withName(newName);
        assertNotNull(result);
        assertEquals(newName, result.getName());
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        SettableBeanProperty result = objectIdReferenceProperty.withValueDeserializer(deser);
        assertNotNull(result);
        assertEquals(deser, result.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nva = new NullValueProvider();
        SettableBeanProperty result = objectIdReferenceProperty.withNullProvider(nva);
        assertNotNull(result);
        assertEquals(nva, result.getNullProvider());
    }

    // Additional test cases for edge cases and exception handling scenarios could be added here

}