
import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectIdReferencePropertyTest {

    @Test
    public void testConstructor1() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        assertEquals(forward, property._forward);
        assertEquals(objectIdInfo, property._objectIdInfo);
    }

    @Test
    public void testConstructor2() {
        ObjectIdReferenceProperty src = new ObjectIdReferenceProperty();
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        NullValueProvider nva = new NullValueProvider();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(src, deser, nva);

        assertEquals(src._forward, property._forward);
        assertEquals(src._objectIdInfo, property._objectIdInfo);
        assertEquals(deser, property._valueDeserializer);
        assertEquals(nva, property._nullProvider);
    }

    @Test
    public void testConstructor3() {
        ObjectIdReferenceProperty src = new ObjectIdReferenceProperty();
        PropertyName newName = new PropertyName();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(src, newName);

        assertEquals(src._forward, property._forward);
        assertEquals(src._objectIdInfo, property._objectIdInfo);
        assertEquals(newName, property._name);
    }

    @Test
    public void testWithName() {
        ObjectIdReferenceProperty src = new ObjectIdReferenceProperty();
        PropertyName newName = new PropertyName();
        ObjectIdReferenceProperty property = (ObjectIdReferenceProperty) src.withName(newName);

        assertEquals(src._forward, property._forward);
        assertEquals(src._objectIdInfo, property._objectIdInfo);
        assertEquals(newName, property._name);
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdReferenceProperty src = new ObjectIdReferenceProperty();
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        ObjectIdReferenceProperty property = (ObjectIdReferenceProperty) src.withValueDeserializer(deser);

        assertEquals(src._forward, property._forward);
        assertEquals(src._objectIdInfo, property._objectIdInfo);
        assertEquals(deser, property._valueDeserializer);
    }

    @Test
    public void testWithNullProvider() {
        ObjectIdReferenceProperty src = new ObjectIdReferenceProperty();
        NullValueProvider nva = new NullValueProvider();
        ObjectIdReferenceProperty property = (ObjectIdReferenceProperty) src.withNullProvider(nva);

        assertEquals(src._forward, property._forward);
        assertEquals(src._objectIdInfo, property._objectIdInfo);
        assertEquals(nva, property._nullProvider);
    }

    @Test
    public void testFixAccess() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty();
        DeserializationConfig config = new DeserializationConfig();
        property.fixAccess(config);
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty();
        assertNull(property.getAnnotation(Annotation.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty();
        assertNull(property.getMember());
    }

    @Test
    public void testGetCreatorIndex() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty();
        assertEquals(-1, property.getCreatorIndex());
    }

    @Test
    public void testDeserializeAndSet() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty();
        JsonParser parser = new JsonParser();
        DeserializationContext context = new DeserializationContext();
        Object instance = new Object();
        try {
            property.deserializeAndSet(parser, context, instance);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testDeserializeSetAndReturn() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty();
        JsonParser parser = new JsonParser();
        DeserializationContext context = new DeserializationContext();
        Object instance = new Object();
        try {
            property.deserializeSetAndReturn(parser, context, instance);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testSet() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty();
        Object instance = new Object();
        Object value = new Object();
        try {
            property.set(instance, value);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testSetAndReturn() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty();
        Object instance = new Object();
        Object value = new Object();
        try {
            property.setAndReturn(instance, value);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testPropertyReferring() {
        ObjectIdReferenceProperty parent = new ObjectIdReferenceProperty();
        UnresolvedForwardReference ref = new UnresolvedForwardReference();
        Class<?> type = Object.class;
        Object ob = new Object();
        ObjectIdReferenceProperty.PropertyReferring referring = new ObjectIdReferenceProperty.PropertyReferring(parent, ref, type, ob);

        assertEquals(parent, referring._parent);
        assertEquals(ob, referring._pojo);
    }

    @Test
    public void testHandleResolvedForwardReference() {
        ObjectIdReferenceProperty.PropertyReferring referring = new ObjectIdReferenceProperty.PropertyReferring();
        Object id = new Object();
        Object value = new Object();
        try {
            referring.handleResolvedForwardReference(id, value);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
}

