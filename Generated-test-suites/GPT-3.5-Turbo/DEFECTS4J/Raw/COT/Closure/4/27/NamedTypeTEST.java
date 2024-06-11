import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;

public class NamedTypeTest {
    private JSTypeRegistry registry;
    private NamedType namedType;

    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
        namedType = new NamedType(registry, "TestType", "TestFile", 1, 1);
    }

    @Test
    public void testGetReferencedType() {
        JSType referencedType = namedType.getReferencedType();
        assertEquals(registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE), referencedType);
    }

    @Test
    public void testGetReferenceName() {
        String referenceName = namedType.getReferenceName();
        assertEquals("TestType", referenceName);
    }

    @Test
    public void testToStringHelper() {
        String toString = namedType.toStringHelper(false);
        assertEquals("TestType", toString);
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
    public void testResolveInternal() {
        ErrorReporter errorReporter = null;
        StaticScope<JSType> enclosing = null;
        JSType resolvedType = namedType.resolveInternal(errorReporter, enclosing);
        assertEquals(registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE), resolvedType);
    }

    @Test
    public void testGetTypedefType() {
        ErrorReporter errorReporter = null;
        StaticScope<JSType> enclosing = null;
        staticSlot = new StaticSlot<>();
        staticSlot.setType(registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE));
        JSType typedefType = namedType.getTypedefType(errorReporter, staticSlot, "TestType");
        assertEquals(registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE), typedefType);
    }

    @Test
    public void testSetValidator() {
        Predicate<JSType> validator = new Predicate<JSType>() {
            @Override
            public boolean apply(JSType type) {
                return type.isNumberType();
            }
        };
        assertTrue(namedType.setValidator(validator));
    }
}