import org.junit.Test;
import static org.junit.Assert.*;

public class NamedTypeTest {

    @Test
    public void testNamedTypeConstructor() {
        JSTypeRegistry registry = new JSTypeRegistry();
        NamedType namedType = new NamedType(registry, "testReference", "testSource", 1, 1);

        assertEquals("testReference", namedType.getReferenceName());
        assertEquals("testSource", namedType.getSourceName());
        assertEquals(1, namedType.getLineno());
        assertEquals(1, namedType.getCharno());
    }

    @Test
    public void testSetValidator() {
        JSTypeRegistry registry = new JSTypeRegistry();
        NamedType namedType = new NamedType(registry, "testReference", "testSource", 1, 1);

        namedType.setValidator(type -> type.isUnknownType());
        assertTrue(namedType.setValidator(type -> type.isUnknownType()));
    }

    @Test
    public void testResolveType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        NamedType namedType = new NamedType(registry, "testReference", "testSource", 1, 1);

        namedType.resolveInternal(new ErrorReporter(), new StaticScope<>());
        assertNotNull(namedType.getReferencedType());
    }

    @Test
    public void testDefineProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        NamedType namedType = new NamedType(registry, "testReference", "testSource", 1, 1);

        assertFalse(namedType.defineProperty("testProperty", registry.getNativeType(JSTypeNative.UNDEFINED_TYPE), false, null));
    }

    @Test
    public void testHashCode() {
        JSTypeRegistry registry = new JSTypeRegistry();
        NamedType namedType1 = new NamedType(registry, "testReference", "testSource", 1, 1);
        NamedType namedType2 = new NamedType(registry, "testReference", "testSource", 1, 1);

        assertEquals(namedType1.hashCode(), namedType2.hashCode());
    }
}