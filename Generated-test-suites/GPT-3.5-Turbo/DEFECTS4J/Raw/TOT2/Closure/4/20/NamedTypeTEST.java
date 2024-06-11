import static org.junit.Assert.*;
import org.junit.Test;

public class NamedTypeTest {
    
    @Test
    public void testConstruction() {
        NamedType namedType = new NamedType(registry, "testReference", "testSource", 1, 1);
        assertNotNull(namedType);
        assertEquals("testReference", namedType.getReferenceName());
    }
    
    @Test
    public void testDefineProperty() {
        NamedType namedType = new NamedType(registry, "testReference", "testSource", 1, 1);
        boolean result = namedType.defineProperty("testProperty", JSType.UNKNOWN, false, new Node());
        assertTrue(result);
        assertEquals(JSType.UNKNOWN, namedType.getPropertyType("testProperty"));
    }
    
    @Test
    public void testResolveInternal() {
        NamedType namedType = new NamedType(registry, "testReference", "testSource", 1, 1);
        JSType resolvedType = namedType.resolveInternal(errorReporter, enclosingScope);
        assertNotNull(resolvedType);
        assertTrue(namedType.isResolved());
    }
    
    @Test
    public void testGetReferencedType() {
        NamedType namedType = new NamedType(registry, "testReference", "testSource", 1, 1);
        JSType referencedType = namedType.getReferencedType();
        assertEquals(JSType.UNKNOWN, referencedType);
    }
    
    @Test
    public void testSetValidator() {
        NamedType namedType = new NamedType(registry, "testReference", "testSource", 1, 1);
        namedType.setValidator(type -> type != null);
        assertTrue(namedType.isValidatorSet());
    }
    
    @Test
    public void testValidatorFunctionality() {
        NamedType namedType = new NamedType(registry, "testReference", "testSource", 1, 1);
        namedType.setValidator(type -> type != null);
        assertTrue(namedType.getValidator().test(JSType.UNKNOWN));
        assertFalse(namedType.getValidator().test(null));
    }
    
    @Test
    public void testHashCode() {
        NamedType namedType1 = new NamedType(registry, "testReference", "testSource", 1, 1);
        NamedType namedType2 = new NamedType(registry, "testReference", "testSource", 1, 1);
        assertEquals(namedType1.hashCode(), namedType2.hashCode());
    }
}