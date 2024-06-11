import org.junit.Test;
import static org.junit.Assert.*;

public class NamedTypeTest {

    @Test
    public void testGetReferencedType() {
        NamedType namedType = new NamedType(null, "reference", "sourceName", 1, 1);
        namedType.setReferencedType(new JSType());
        assertEquals(new JSType(), namedType.getReferencedType());
    }

    @Test
    public void testResolveInternal() {
        NamedType namedType = new NamedType(null, "reference", "sourceName", 1, 1);
        namedType.resolveInternal(null, null);
        assertEquals(null, namedType.getReferencedType());
    }

    @Test
    public void testDefineProperty() {
        NamedType namedType = new NamedType(null, "reference", "sourceName", 1, 1);
        assertFalse(namedType.defineProperty("propertyName", new JSType(), true, null));
    }

    @Test
    public void testGetReferenceName() {
        NamedType namedType = new NamedType(null, "reference", "sourceName", 1, 1);
        assertEquals("reference", namedType.getReferenceName());
    }

    @Test
    public void testToStringHelper() {
        NamedType namedType = new NamedType(null, "reference", "sourceName", 1, 1);
        assertEquals("reference", namedType.toStringHelper(false));
    }

    @Test
    public void testHasReferenceName() {
        NamedType namedType = new NamedType(null, "reference", "sourceName", 1, 1);
        assertTrue(namedType.hasReferenceName());
    }

    @Test
    public void testIsNamedType() {
        NamedType namedType = new NamedType(null, "reference", "sourceName", 1, 1);
        assertTrue(namedType.isNamedType());
    }

    @Test
    public void testIsNominalType() {
        NamedType namedType = new NamedType(null, "reference", "sourceName", 1, 1);
        assertTrue(namedType.isNominalType());
    }

    @Test
    public void testHashCode() {
        NamedType namedType = new NamedType(null, "reference", "sourceName", 1, 1);
        assertEquals("reference".hashCode(), namedType.hashCode());
    }
}