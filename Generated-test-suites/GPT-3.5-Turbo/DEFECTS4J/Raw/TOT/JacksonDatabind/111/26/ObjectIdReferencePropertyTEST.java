
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdReferencePropertyTest {

    @Test
    public void testWithForwardIsNull() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);

        assertNull(property.withName(null));
        assertNull(property.withValueDeserializer(null));
        assertNull(property.withNullProvider(null));
        assertEquals(0, property.getCreatorIndex());
        assertNull(property.getAnnotation(null));
        assertNull(property.getMember());
        assertNull(property.deserializeSetAndReturn(null, null, null));
        assertNull(property.setAndReturn(null, null));
    }

    @Test
    public void testWithForwardIsNotNull() {
        ObjectIdReferenceProperty forward = new ObjectIdReferenceProperty(null, null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, null);

        assertEquals(property, property.withName(null));
        assertEquals(property, property.withValueDeserializer(null));
        assertEquals(property, property.withNullProvider(null));
        assertEquals(forward, property.getForward());
        property.fixAccess(null);
        assertNull(property.getAnnotation(null));
        assertNull(property.getMember());
        assertEquals(0, property.getCreatorIndex());

        try {
            property.deserializeSetAndReturn(null, null, null);
            fail("Expected an IOException to be thrown");
        } catch (IOException e) {
            assertEquals("Unresolved forward reference but no identity info", e.getMessage());
        }

        try {
            property.setAndReturn(null, null);
            fail("Expected an IOException to be thrown");
        } catch (IOException e) {
            assertEquals("Unresolved forward reference but no identity info", e.getMessage());
        }
    }

    @Test
    public void testPropertyReferring() throws IOException {
        ObjectIdReferenceProperty.ReflectionMethodInvoker.setAccessible(ObjectIdReferenceProperty.PropertyReferring.class);

        ObjectIdReferenceProperty forward = new ObjectIdReferenceProperty(null, null);
        ObjectIdReferenceProperty.PropertyReferring referring = new ObjectIdReferenceProperty.PropertyReferring(forward, null, null, null);

        referring.handleResolvedForwardReference(null, null);
        assertNull(referring.getBonaparte());
    }
}

