import org.junit.Test;
import static org.junit.Assert.*;

public class NamedTypeTest {

    @Test
    public void testNamedTypeConstructor() {
        NamedType namedType = new NamedType(new JSTypeRegistry(), "TestType", "TestSource", 1, 1);
        assertNotNull(namedType);
        assertEquals("TestType", namedType.getReferenceName());
        assertEquals("TestSource", namedType.toString());
        assertTrue(namedType.hasReferenceName());
        assertTrue(namedType.isNamedType());
        assertTrue(namedType.isNominalType());
    }

    @Test
    public void testDefineProperty() {
        NamedType namedType = new NamedType(new JSTypeRegistry(), "TestType", "TestSource", 1, 1);
        namedType.defineProperty("prop1", JSTypeNative.STRING_TYPE, false, null);
        assertTrue(namedType.getOwnPropertyNames().contains("prop1"));
    }

    @Test
    public void testResolve() {
        NamedType namedType = new NamedType(new JSTypeRegistry(), "TestType", "TestSource", 1, 1);
        StaticScope<JSType> enclosingScope = new StaticScope<>();
        namedType.resolveInternal(new ErrorReporter(), enclosingScope);
        assertNotNull(namedType.getReferencedType());
        assertNotNull(namedType.getSymScope());
    }

    @Test
    public void testValidator() {
        NamedType namedType = new NamedType(new JSTypeRegistry(), "TestType", "TestSource", 1, 1);
        namedType.setValidator(new Predicate<JSType>() {
            @Override
            public boolean apply(JSType type) {
                return true;
            }
        });
        assertTrue(namedType.getValidators().size() == 1);
    }
}