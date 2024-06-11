import org.junit.Test;
import static org.junit.Assert.*;

public class NamedTypeTest {

    @Test
    public void testNamedTypeConstructor() {
        NamedType namedType = new NamedType(registry, "example", "ExampleSource", 1, 2);
        assertNotNull(namedType);
    }

    @Test
    public void testDefineProperty() {
        NamedType namedType = new NamedType(registry, "example", "ExampleSource", 1, 2);
        assertTrue(namedType.defineProperty("property", JSType.UNKNOWN, false, null));
    }

    @Test
    public void testGetReferencedType() {
        NamedType namedType = new NamedType(registry, "example", "ExampleSource", 1, 2);
        assertNotNull(namedType.getReferencedType());
    }

    @Test
    public void testResolveInternal() {
        NamedType namedType = new NamedType(registry, "example", "ExampleSource", 1, 2);
        namedType.resolveInternal(errorReporter, enclosingScope);
        assertTrue(namedType.isResolved());
    }

    @Test
    public void testSetValidator() {
        NamedType namedType = new NamedType(registry, "example", "ExampleSource", 1, 2);
        namedType.setValidator((type) -> type.isObjectType());
        assertNotNull(namedType.getValidator());
    }

    @Test
    public void testHashCode() {
        NamedType namedType = new NamedType(registry, "example", "ExampleSource", 1, 2);
        assertEquals("example".hashCode(), namedType.hashCode());
    }

    @Test
    public void testToStringHelper() {
        NamedType namedType = new NamedType(registry, "example", "ExampleSource", 1, 2);
        assertEquals("example", namedType.toStringHelper(false));
    }

}