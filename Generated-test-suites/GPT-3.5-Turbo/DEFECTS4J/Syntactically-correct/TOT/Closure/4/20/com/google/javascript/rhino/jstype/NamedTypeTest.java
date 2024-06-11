import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamedTypeTest {

    private NamedType namedType;

    @Before
    public void setUp() {
        namedType = new NamedType(new JSTypeRegistry(), "testType", "source", 1, 1);
    }

    @Test
    public void testGetReferencedType() {
        JSType referencedType = namedType.getReferencedType();
        assertNotNull(referencedType);
        assertTrue(referencedType.isUnknownType());
    }

    @Test
    public void testGetReferenceName() {
        String referenceName = namedType.getReferenceName();
        assertEquals("testType", referenceName);
    }

    @Test
    public void testToStringHelper() {
        String stringHelper = namedType.toStringHelper(false);
        assertEquals("testType", stringHelper);
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
        int hashCode = namedType.hashCode();
        assertEquals("testType".hashCode(), hashCode);
    }

    @Test
    public void testResolveViaRegistry() {
        boolean resolved = namedType.resolveViaRegistry(new ErrorReporter(), new StaticScope<JSType>());
        assertFalse(resolved);
    }

    @Test
    public void testResolveInternal() {
        JSType resolvedType = namedType.resolveInternal(new ErrorReporter(), new StaticScope<JSType>());
        assertNotNull(resolvedType);
        assertTrue(resolvedType.isUnknownType());
    }

    @Test
    public void testSetValidator() {
        Predicate<JSType> validator = type -> type.isNoObjectType();
        boolean setValidator = namedType.setValidator(validator);
        assertTrue(setValidator);
    }

}