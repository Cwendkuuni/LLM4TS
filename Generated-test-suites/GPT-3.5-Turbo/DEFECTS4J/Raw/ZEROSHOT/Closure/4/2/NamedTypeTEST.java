import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NamedTypeTest {
    // Class under test
    private NamedType namedType;

    @Before
    public void setUp() {
        namedType = new NamedType(new JSTypeRegistry(), "reference", "sourceName", 1, 1);
    }

    @Test
    public void testConstructor() {
        assertNotNull(namedType);
    }

    @Test
    public void testGetReferencedType() {
        assertNull(namedType.getReferencedType());
    }

    @Test
    public void testGetReferenceName() {
        assertEquals("reference", namedType.getReferenceName());
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
        assertEquals("reference".hashCode(), namedType.hashCode());
    }

    @Test
    public void testResolveInternal() {
        namedType.resolveInternal(null, null);
        assertNull(namedType.getReferencedType());
        assertNull(namedType.getResolvedTypeInternal());
    }
}