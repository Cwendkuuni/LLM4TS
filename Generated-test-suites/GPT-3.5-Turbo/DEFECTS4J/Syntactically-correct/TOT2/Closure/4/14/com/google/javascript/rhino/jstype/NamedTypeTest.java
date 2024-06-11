import static org.junit.Assert.*;
import org.junit.Test;

public class NamedTypeTest {

    @Test
    public void testNamedTypeInitialization() {
        NamedType namedType = new NamedType(new JSTypeRegistry(), "example", "source", 1, 1);
        assertNotNull(namedType);
        assertEquals("example", namedType.getReferenceName());
        assertNotNull(namedType.getReferencedType());
        assertFalse(namedType.isResolved());
    }

    @Test
    public void testResolveInternal() {
        NamedType namedType = new NamedType(new JSTypeRegistry(), "example", "source", 1, 1);
        namedType.resolveInternal(new ErrorReporter(), new StaticScope<>());
        assertTrue(namedType.isResolved());
        assertNotNull(namedType.getReferencedType());
    }

    @Test
    public void testDefineProperty() {
        NamedType namedType = new NamedType(new JSTypeRegistry(), "example", "source", 1, 1);
        assertFalse(namedType.defineProperty("property1", JSType.UNKNOWN, false, new Node()));
        namedType.resolveInternal(new ErrorReporter(), new StaticScope<>());
        assertTrue(namedType.defineProperty("property1", JSType.STRING, true, new Node()));
        assertTrue(namedType.defineProperty("property2", JSType.NUMBER, false, new Node()));
    }

    @Test
    public void testSetValidator() {
        NamedType namedType = new NamedType(new JSTypeRegistry(), "example", "source", 1, 1);
        assertFalse(namedType.setValidator(type -> type.isEnumType()));
        namedType.resolveInternal(new ErrorReporter(), new StaticScope<>());
        assertTrue(namedType.setValidator(type -> type.isFunctionType()));
    }
}