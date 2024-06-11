import static org.junit.Assert.*;
import org.junit.Test;

public class NamedTypeTest {
    
    @Test
    public void testNamedTypeConstructor() {
        NamedType namedType = new NamedType(registry, "example", "source", 1, 1);
        assertNotNull(namedType);
    }

    @Test
    public void testNamedTypeResolveInternal() {
        NamedType namedType = new NamedType(registry, "example", "source", 1, 1);
        namedType.resolveInternal(errorReporter, enclosingScope);
        assertTrue(namedType.isResolved());
    }

    @Test
    public void testNamedTypeGetReferencedType() {
        NamedType namedType = new NamedType(registry, "example", "source", 1, 1);
        JSType referencedType = namedType.getReferencedType();
        assertNotNull(referencedType);
    }

    @Test
    public void testNamedTypeLookupViaProperties() {
        NamedType namedType = new NamedType(registry, "example", "source", 1, 1);
        JSType result = namedType.lookupViaProperties(errorReporter, enclosingScope);
        assertNotNull(result);
    }

    @Test
    public void testNamedTypeSetValidator() {
        NamedType namedType = new NamedType(registry, "example", "source", 1, 1);
        namedType.setValidator(type -> type != null);
        assertNotNull(namedType.getValidator());
    }
}