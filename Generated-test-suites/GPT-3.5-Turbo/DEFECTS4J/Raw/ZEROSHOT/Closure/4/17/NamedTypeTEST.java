import org.junit.Test;

import static org.junit.Assert.*;

public class NamedTypeTest {

    private NamedType namedType;

    @Test
    public void testGetReferencedType() {
        namedType = new NamedType(null, "reference", "sourceName", 0, 0);
        namedType.setReferencedType(new JSType());

        assertNotNull(namedType.getReferencedType());
    }

    @Test
    public void testGetReferenceName() {
        namedType = new NamedType(null, "reference", "sourceName", 0, 0);

        assertEquals("reference", namedType.getReferenceName());
    }

    @Test
    public void testHasReferenceName() {
        namedType = new NamedType(null, "reference", "sourceName", 0, 0);

        assertTrue(namedType.hasReferenceName());
    }

    @Test
    public void testIsNamedType() {
        namedType = new NamedType(null, "reference", "sourceName", 0, 0);

        assertTrue(namedType.isNamedType());
    }

    @Test
    public void testIsNominalType() {
        namedType = new NamedType(null, "reference", "sourceName", 0, 0);

        assertTrue(namedType.isNominalType());
    }

    @Test
    public void testHashCode() {
        namedType = new NamedType(null, "reference", "sourceName", 0, 0);

        assertEquals("reference".hashCode(), namedType.hashCode());
    }

    @Test
    public void testResolveInternal() {
        namedType = new NamedType(null, "reference", "sourceName", 0, 0);
        namedType.resolveInternal(null, null);

        assertTrue(namedType.isResolved());
    }

    @Test
    public void testSetValidator() {
        namedType = new NamedType(null, "reference", "sourceName", 0, 0);
        namedType.setValidator(null);

        assertNull(namedType.getValidator());
    }

    @Test
    public void testDefineProperty() {
        namedType = new NamedType(null, "reference", "sourceName", 0, 0);

        assertTrue(namedType.defineProperty("propertyName", new JSType(), false, null));
    }

    @Test
    public void testToStringHelper() {
        namedType = new NamedType(null, "reference", "sourceName", 0, 0);

        assertEquals("reference", namedType.toStringHelper(false));
    }

}