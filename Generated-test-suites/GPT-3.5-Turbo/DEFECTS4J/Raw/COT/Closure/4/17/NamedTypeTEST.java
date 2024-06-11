import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NamedTypeTest {

    private NamedType namedType;

    @Before
    public void setUp() {
        namedType = new NamedType();
    }

    @Test
    public void testGetReferenceName() {
        String expected = "reference";
        namedType.setReferenceName(expected);
        String actual = namedType.getReferenceName();
        assertEquals(expected, actual);
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
    public void testHasReferenceName() {
        assertTrue(namedType.hasReferenceName());
    }

    @Test
    public void testHashCode() {
        int expected = "reference".hashCode();
        int actual = namedType.hashCode();
        assertEquals(expected, actual);
    }

    @Test
    public void testResolveInternal_WithResolvedType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        JSType type = registry.getType("SomeType");
        namedType.setReferencedType(type);
        namedType.resolveInternal(null, null);
        assertEquals(type, namedType.getResolvedType());
    }

    @Test
    public void testResolveInternal_WithUnresolvedType() {
        namedType.setReferencedType(null);
        namedType.resolveInternal(null, null);
        assertEquals(NamedType.UNKNOWN_TYPE, namedType.getResolvedType());
    }
}