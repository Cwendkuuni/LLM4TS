import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectIdValuePropertyTest {

    @Test
    public void testWithName() {
        ObjectIdValueProperty prop = new ObjectIdValueProperty(null, null);
        PropertyName newName = new PropertyName("newName");
        ObjectIdValueProperty newProp = prop.withName(newName);
        assertEquals(newName, newProp.getPropertyName());
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdValueProperty prop = new ObjectIdValueProperty(null, null);
        JsonDeserializer<?> deser = new JsonDeserializer() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        ObjectIdValueProperty newProp = prop.withValueDeserializer(deser);
        assertNotNull(newProp.getValueDeserializer());
    }

    @Test
    public void testDeserializeAndSet() {
        ObjectIdValueProperty prop = new ObjectIdValueProperty(null, null);
        try {
            prop.deserializeAndSet(null, null, null);
        } catch (Exception e) {
            fail("Exception thrown in deserializeAndSet");
        }
    }

    @Test
    public void testDeserializeSetAndReturn() {
        ObjectIdValueProperty prop = new ObjectIdValueProperty(null, null);
        try {
            prop.deserializeSetAndReturn(null, null, null);
        } catch (Exception e) {
            fail("Exception thrown in deserializeSetAndReturn");
        }
    }

    @Test
    public void testSet() {
        ObjectIdValueProperty prop = new ObjectIdValueProperty(null, null);
        try {
            prop.set(null, null);
        } catch (Exception e) {
            fail("Exception thrown in set");
        }
    }

    @Test
    public void testSetAndReturn() {
        ObjectIdValueProperty prop = new ObjectIdValueProperty(null, null);
        try {
            prop.setAndReturn(null, null);
        } catch (Exception e) {
            fail("Exception thrown in setAndReturn");
        }
    }
}