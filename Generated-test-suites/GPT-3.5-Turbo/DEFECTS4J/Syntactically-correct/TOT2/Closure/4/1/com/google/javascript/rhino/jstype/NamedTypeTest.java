import static org.junit.Assert.*;
import org.junit.Test;

public class NamedTypeTest {

    @Test
    public void testNamedTypeConstructor() {
        NamedType namedType = new NamedType(registry, "TestType", "SourceFile", 1, 1);
        
        assertNotNull(namedType);
        assertEquals("TestType", namedType.getReferenceName());
        assertEquals("SourceFile", namedType.getSourceName());
        assertEquals(1, namedType.getLineno());
        assertEquals(1, namedType.getCharno());
    }

    @Test
    public void testReferenceName() {
        NamedType namedType = new NamedType(registry, "TestType", "SourceFile", 1, 1);
        
        assertEquals("TestType", namedType.getReferenceName());
    }

    @Test
    public void testToStringHelper() {
        NamedType namedType = new NamedType(registry, "TestType", "SourceFile", 1, 1);
        
        String expected = "TestType";
        assertEquals(expected, namedType.toStringHelper(true));
    }

    @Test
    public void testHasReferenceName() {
        NamedType namedType = new NamedType(registry, "TestType", "SourceFile", 1, 1);
        
        assertTrue(namedType.hasReferenceName());
    }

    @Test
    public void testIsNominalType() {
        NamedType namedType = new NamedType(registry, "TestType", "SourceFile", 1, 1);
        
        assertTrue(namedType.isNominalType());
    }

    @Test
    public void testResolveInternal() {
        NamedType namedType = new NamedType(registry, "TestType", "SourceFile", 1, 1);
        
        ErrorReporter errorReporter = new ErrorReporter();
        StaticScope<JSType> enclosing = new StaticScope<>();
        JSType resolvedType = namedType.resolveInternal(errorReporter, enclosing);
        
        assertNotNull(resolvedType);
    }
}