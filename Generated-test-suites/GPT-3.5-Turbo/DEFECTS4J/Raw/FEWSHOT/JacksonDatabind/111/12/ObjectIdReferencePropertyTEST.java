import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectIdReferencePropertyTest {

    @Test
    public void testConstructorAndGetters() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        assertEquals(forward, property.getForward());

        JsonDeserializer<?> deserializer = new JsonDeserializer<?>();
        NullValueProvider nullValueProvider = new NullValueProvider();
        ObjectIdReferenceProperty property2 = new ObjectIdReferenceProperty(property, deserializer, nullValueProvider);
        assertEquals(deserializer, property2.getValueDeserializer());
        assertEquals(nullValueProvider, property2.getNullProvider());

        PropertyName newName = new PropertyName();
        ObjectIdReferenceProperty property3 = new ObjectIdReferenceProperty(property, newName);
        assertEquals(newName, property3.getName());
    }

    @Test
    public void testWithMethods() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        PropertyName newName = new PropertyName("newName");
        ObjectIdReferenceProperty property2 = property.withName(newName);
        assertEquals(newName, property2.getName());

        JsonDeserializer<?> deserializer = new JsonDeserializer<?>();
        ObjectIdReferenceProperty property3 = property.withValueDeserializer(deserializer);
        assertEquals(deserializer, property3.getValueDeserializer());

        NullValueProvider nullValueProvider = new NullValueProvider();
        ObjectIdReferenceProperty property4 = property.withNullProvider(nullValueProvider);
        assertEquals(nullValueProvider, property4.getNullProvider());
    }

    @Test
    public void testFixAccess() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        DeserializationConfig config = new DeserializationConfig();
        property.fixAccess(config);
        // assert something about config?
    }

    @Test
    public void testGetAnnotation() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        Class<A> acls = A.class;
        assertNull(property.getAnnotation(acls));
    }

    @Test
    public void testGetMember() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        assertNull(property.getMember());
    }

    @Test
    public void testGetCreatorIndex() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        assertEquals(0, property.getCreatorIndex());
    }

    @Test
    public void testDeserializeAndSet() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        try {
            property.deserializeAndSet(p, ctxt, instance);
        } catch (IOException e) {
            fail(e.getMessage());
        }
        // assert something about instance?
    }

    @Test
    public void testDeserializeSetAndReturn() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        try {
            Object result = property.deserializeSetAndReturn(p, ctxt, instance);
            assertNull(result);
        } catch (IOException e) {
            fail(e.getMessage());
        }
        // assert something about instance?
    }

    @Test
    public void testSetAndReturn() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        Object instance = new Object();
        try {
            Object result = property.setAndReturn(instance, null);
            assertNull(result);
        } catch (IOException e) {
            fail(e.getMessage());
        }
        // assert something about instance?
    }

    @Test
    public void testSet() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        Object instance = new Object();
        try {
            property.set(instance, null);
        } catch (IOException e) {
            fail(e.getMessage());
        }
        // assert something about instance?
    }

    @Test
    public void testPropertyReferring() {
        ObjectIdReferenceProperty parent = new ObjectIdReferenceProperty();
        UnresolvedForwardReference ref = new UnresolvedForwardReference();
        Class<?> type = Object.class;
        Object ob = new Object();
        ObjectIdReferenceProperty.PropertyReferring referring = new ObjectIdReferenceProperty.PropertyReferring(parent, ref, type, ob);
        assertEquals(parent, referring.getParent());
        assertEquals(ob, referring.getPojo());

        Object id = new Object();
        Object value = new Object();
        try {
            referring.handleResolvedForwardReference(id, value);
        } catch (IOException e) {
            fail(e.getMessage());
        }
        // assert something?
    }
}