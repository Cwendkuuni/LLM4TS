import static org.junit.Assert.*;
import org.junit.Test;

public class NamedTypeTest {

    @Test
    public void testGetReferencedType() {
        NamedType namedType = new NamedType(null, "reference", "sourceName", 1, 1);
        assertNull(namedType.getReferencedType());
    }

    @Test
    public void testGetReferenceName() {
        NamedType namedType = new NamedType(null, "reference", "sourceName", 1, 1);
        assertEquals("reference", namedType.getReferenceName());
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
    public void testResolveInternal() {
        NamedType namedType = new NamedType(null, "reference", "sourceName", 1, 1);
        assertNull(namedType.resolveInternal(null, null));
    }

}