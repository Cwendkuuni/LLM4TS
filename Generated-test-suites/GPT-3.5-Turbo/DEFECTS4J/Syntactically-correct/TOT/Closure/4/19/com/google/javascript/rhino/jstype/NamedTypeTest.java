import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamedTypeTest {

    private NamedType namedType;

    @Before
    public void setUp() {
        namedType = new NamedType(new JSTypeRegistry(), "SomeType", "source", 1, 1);
    }

    @Test
    public void testGetReferencedType() {
        JSType referencedType = namedType.getReferencedType();
        assertNotNull(referencedType);
        assertTrue(referencedType.isUnknownType());
    }

    @Test
    public void testDefineProperty() {
        String propertyName = "property";
        JSType type = new JSType(new JSTypeRegistry(), "string");
        boolean inferred = false;
        Node propertyNode = new Node();

        boolean result = namedType.defineProperty(propertyName, type, inferred, propertyNode);

        assertTrue(result);
    }

    @Test
    public void testResolveInternal() {
        StaticScope<JSType> enclosingScope = new StaticScope<>();
        enclosingScope.addSlot("SomeType", new JSType(new JSTypeRegistry(), "string"));
        ErrorReporter reporter = new ErrorReporter();

        JSType resolvedType = namedType.resolveInternal(reporter, enclosingScope);

        assertNotNull(resolvedType);
        assertFalse(resolvedType.isUnknownType());
        assertFalse(resolvedType.isNoType());
    }

    @Test
    public void testToStringHelper() {
        String result = namedType.toStringHelper(false);

        assertNotNull(result);
        assertEquals("SomeType", result);
    }

    @Test
    public void testHasReferenceName() {
        boolean result = namedType.hasReferenceName();

        assertTrue(result);
    }

    @Test
    public void testIsNamedType() {
        boolean result = namedType.isNamedType();

        assertTrue(result);
    }

    @Test
    public void testIsNominalType() {
        boolean result = namedType.isNominalType();

        assertTrue(result);
    }
}