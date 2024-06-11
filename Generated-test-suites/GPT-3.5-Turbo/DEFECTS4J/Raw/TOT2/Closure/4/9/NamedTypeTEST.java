import org.junit.Test;
import static org.junit.Assert.*;

public class NamedTypeTest {

    @Test
    public void testNamedTypeConstructor() {
        JSTypeRegistry registry = new JSTypeRegistry();
        NamedType namedType = new NamedType(registry, "exampleType", "exampleSource", 1, 2);
        assertNotNull(namedType);
    }

    @Test
    public void testDefineProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        NamedType namedType = new NamedType(registry, "exampleType", "exampleSource", 1, 2);
        assertTrue(namedType.defineProperty("property1", registry.getNativeType(JSTypeNative.STRING_TYPE), false, null));
    }

    @Test
    public void testGetReferencedType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        NamedType namedType = new NamedType(registry, "exampleType", "exampleSource", 1, 2);
        assertEquals(registry.getNativeType(JSTypeNative.UNKNOWN_TYPE), namedType.getReferencedType());
    }

    @Test
    public void testResolveInternal() {
        JSTypeRegistry registry = new JSTypeRegistry();
        NamedType namedType = new NamedType(registry, "exampleType", "exampleSource", 1, 2);
        namedType.resolveInternal(new ErrorReporter(), new StaticScope<JSType>());
        assertNotNull(namedType.getReferencedType());
        assertTrue(namedType.isResolved());
    }

    @Test
    public void testSetValidator() {
        JSTypeRegistry registry = new JSTypeRegistry();
        NamedType namedType = new NamedType(registry, "exampleType", "exampleSource", 1, 2);
        Predicate<JSType> validator = type -> type == registry.getNativeType(JSTypeNative.UNKNOWN_TYPE);
        assertTrue(namedType.setValidator(validator));
    }
}