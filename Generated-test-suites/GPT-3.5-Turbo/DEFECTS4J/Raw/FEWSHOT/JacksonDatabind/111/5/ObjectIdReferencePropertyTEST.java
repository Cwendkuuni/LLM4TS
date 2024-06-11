import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectIdReferencePropertyTest {
    @Test
    public void testConstructor1() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);
        assertNull(property.getValueDeserializer());
        assertNull(property.getNullProvider());
    }

    @Test
    public void testConstructor2() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null, null);
        assertNull(property.getValueDeserializer());
        assertNull(property.getNullProvider());
    }

    @Test
    public void testConstructor3() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null, null);
        assertNull(property.getValueDeserializer());
        assertNull(property.getNullProvider());
    }

    @Test
    public void testWithName() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);
        ObjectIdReferenceProperty newProperty = property.withName(null);
        assertNull(newProperty.getFullName());
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);
        ObjectIdReferenceProperty newProperty = property.withValueDeserializer(null);
        assertNull(newProperty.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);
        ObjectIdReferenceProperty newProperty = property.withNullProvider(null);
        assertNull(newProperty.getNullProvider());
    }

    @Test
    public void testFixAccess() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);
        property.fixAccess(null);
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);
        assertNull(property.getAnnotation(null));
    }

    @Test
    public void testGetMember() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);
        assertNull(property.getMember());
    }

    @Test
    public void testGetCreatorIndex() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);
        assertEquals(0, property.getCreatorIndex());
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);
        property.deserializeAndSet(null, null, null);
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);
        assertNull(property.deserializeSetAndReturn(null, null, null));
    }

    @Test
    public void testSet() throws Exception {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);
        property.set(null, null);
    }

    @Test
    public void testSetAndReturn() throws Exception {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);
        assertNull(property.setAndReturn(null, null));
    }

    @Test
    public void testPropertyReferring() {
        ObjectIdReferenceProperty.PropertyReferring referring = new ObjectIdReferenceProperty.PropertyReferring(null, null, null, null);
        referring.handleResolvedForwardReference(null, null);
    }
}