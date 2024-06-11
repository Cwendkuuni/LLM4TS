import org.junit.Test;
import static org.junit.Assert.*;

public class NamedTypeTest {

    @Test
    public void testNamedTypeConstructor() {
        NamedType namedType = new NamedType(registry, "reference", "sourceName", 1, 2);

        assertNotNull(namedType);
        assertEquals("reference", namedType.getReferenceName());
    }

    @Test
    public void testDefineProperty() {
        NamedType namedType = new NamedType(registry, "reference", "sourceName", 1, 2);
        boolean result = namedType.defineProperty("propertyName", new JSType(), false, new Node());

        assertTrue(result);
        assertNotNull(namedType.getProperty("propertyName"));
    }

    @Test
    public void testResolveInternal() {
        NamedType namedType = new NamedType(registry, "reference", "sourceName", 1, 2);
        JSType resolvedType = namedType.resolveInternal(new ErrorReporter(), new StaticScope<JSType>());

        assertNotNull(resolvedType);
        assertTrue(namedType.isResolved());
    }

    @Test
    public void testResolveProperty() {
        NamedType namedType = new NamedType(registry, "reference", "sourceName", 1, 2);
        namedType.defineProperty("propertyName", new JSType(), false, new Node());
        JSType propertyType = namedType.getPropertyType("propertyName");

        assertNotNull(propertyType);
    }

}