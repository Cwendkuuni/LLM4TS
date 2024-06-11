import static org.junit.Assert.*;

import org.junit.Test;

public class NamedTypeTest {

    @Test
    public void testNamedTypeConstructor() {
        NamedType namedType = new NamedType(registry, "reference", "sourceName", 1, 1);
        assertNotNull(namedType);
    }

    @Test
    public void testNamedTypeDefineProperty() {
        NamedType namedType = new NamedType(registry, "reference", "sourceName", 1, 1);
        boolean result = namedType.defineProperty("propertyName", JSType.UNKNOWN, false, null);
        assertTrue(result);
    }

    @Test
    public void testNamedTypeResolve() {
        NamedType namedType = new NamedType(registry, "reference", "sourceName", 1, 1);
        JSType resolvedType = namedType.resolveInternal(errorReporter, enclosingScope);
        assertNotNull(resolvedType);
    }

    @Test
    public void testNamedTypeProperties() {
        NamedType namedType = new NamedType(registry, "reference", "sourceName", 1, 1);
        namedType.defineProperty("propertyName", JSType.UNKNOWN, false, null);
        JSType propertyType = namedType.getPropertyType("propertyName");
        assertNotNull(propertyType);
    }

    @Test
    public void testNamedTypeReferenceName() {
        NamedType namedType = new NamedType(registry, "reference", "sourceName", 1, 1);
        String referenceName = namedType.getReferenceName();
        assertEquals("reference", referenceName);
    }

    @Test
    public void testNamedTypeNamedType() {
        NamedType namedType = new NamedType(registry, "reference", "sourceName", 1, 1);
        assertTrue(namedType.isNamedType());
    }

    @Test
    public void testNamedTypeNominalType() {
        NamedType namedType = new NamedType(registry, "reference", "sourceName", 1, 1);
        assertTrue(namedType.isNominalType());
    }

    @Test
    public void testNamedTypeValidator() {
        NamedType namedType = new NamedType(registry, "reference", "sourceName", 1, 1);
        namedType.setValidator((type) -> true);
        assertNotNull(namedType.getValidator());
    }
}