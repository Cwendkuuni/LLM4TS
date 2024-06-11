import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamedTypeTest {

    NamedType namedType;

    @Before
    public void setUp() {
        namedType = new NamedType(null, null, null, 0, 0);
    }

    @Test
    public void testGetReferencedType() {
        assertNull(namedType.getReferencedType());
    }

    @Test
    public void testGetReferenceName() {
        assertNull(namedType.getReferenceName());
    }

    @Test
    public void testToStringHelper() {
        assertEquals("", namedType.toStringHelper(false));
    }

    @Test
    public void testHasReferenceName() {
        assertTrue(namedType.hasReferenceName());
    }

    @Test
    public void testIsNamedType() {
        assertTrue(namedType.isNamedType());
    }

    @Test
    public void testIsNominalType() {
        assertTrue(namedType.isNominalType());
    }

    @Test
    public void testHashCode() {
        assertEquals(0, namedType.hashCode());
    }

    @Test
    public void testResolveInternal() {
        assertNotNull(namedType.resolveInternal(null, null));
    }

    @Test
    public void testIsResolved() {
        assertFalse(namedType.isResolved());
    }

    @Test
    public void testGetPropertyTypes() {
        assertNotNull(namedType.getPropertyTypes());
    }

    @Test
    public void testDefineProperty() {
        assertTrue(namedType.defineProperty(null, null, false, null));
    }

    @Test
    public void testGetPropertyType() {
        assertNull(namedType.getPropertyType(null));
    }
}