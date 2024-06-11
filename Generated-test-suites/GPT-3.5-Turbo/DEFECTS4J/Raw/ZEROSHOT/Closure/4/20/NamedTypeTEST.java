import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamedTypeTest {

    private NamedType namedType;

    @Before
    public void setUp() {
        namedType = new NamedType(new JSTypeRegistry(), "reference", "sourceName", 1, 1);
    }

    @Test
    public void testGetReferencedType() {
        assertEquals(namedType.getReferencedType(), namedType.getType());
    }

    @Test
    public void testGetReferencedType_null() {
        namedType.setReferencedType(null);
        assertNull(namedType.getReferencedType());
    }

    @Test
    public void testGetReferencedTypeInternal() {
        assertEquals(namedType.getReferencedTypeInternal(), namedType.getReferencedType());
    }

    @Test
    public void testGetReferenceName() {
        assertEquals(namedType.getReferenceName(), "reference");
    }

    @Test
    public void testToStringHelper() {
        assertEquals(namedType.toStringHelper(false), namedType.getReferenceName());
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
    public void testResolveInternal_Resolved() {
        namedType.setReferencedType(new JSType());
        assertEquals(namedType.resolveInternal(new ErrorReporter(), new StaticScope<JSType>()), namedType);
    }

    @Test
    public void testResolveInternal_Unresolved() {
        namedType.setReferencedType(null);
        assertEquals(namedType.resolveInternal(new ErrorReporter(), new StaticScope<JSType>()), namedType);
    }

    @Test
    public void testHashCode() {
        assertEquals(namedType.hashCode(), namedType.getReferenceName().hashCode());
    }

}