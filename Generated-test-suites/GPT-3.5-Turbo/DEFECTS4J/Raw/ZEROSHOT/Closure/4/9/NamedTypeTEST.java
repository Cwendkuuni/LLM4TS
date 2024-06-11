import org.junit.Test;
import static org.junit.Assert.*;

public class NamedTypeTest {

    @Test
    public void testGetReferencedType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        NamedType namedType = new NamedType(registry, "reference", "testSource", 1, 1);
        assertNull(namedType.getReferencedType());
    }

    @Test
    public void testGetReferenceName() {
        JSTypeRegistry registry = new JSTypeRegistry();
        NamedType namedType = new NamedType(registry, "referenceName", "testSource", 1, 1);
        assertEquals("referenceName", namedType.getReferenceName());
    }

    @Test
    public void testHasReferenceName() {
        JSTypeRegistry registry = new JSTypeRegistry();
        NamedType namedType = new NamedType(registry, "reference", "testSource", 1, 1);
        assertTrue(namedType.hasReferenceName());
    }

    @Test
    public void testIsNamedType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        NamedType namedType = new NamedType(registry, "reference", "testSource", 1, 1);
        assertTrue(namedType.isNamedType());
    }

    @Test
    public void testIsNominalType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        NamedType namedType = new NamedType(registry, "reference", "testSource", 1, 1);
        assertTrue(namedType.isNominalType());
    }
}