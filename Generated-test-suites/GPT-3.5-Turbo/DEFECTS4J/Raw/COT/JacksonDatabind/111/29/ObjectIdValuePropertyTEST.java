import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

    @Test
    public void testDeserializeAndSet_NullValue() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        assertNull(property.deserializeAndSet(null, null, null));
    }

    @Test
    public void testDeserializeSetAndReturn_NullValue() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        assertNull(property.deserializeSetAndReturn(null, null, null));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturn_NoIdProperty() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        property.setAndReturn(null, null);
    }

}