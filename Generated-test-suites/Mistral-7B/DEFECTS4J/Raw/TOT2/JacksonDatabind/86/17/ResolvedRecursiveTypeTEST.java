import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class ResolvedRecursiveTypeTest {
    @Test
    public void testResolvedRecursiveTypeConstructor() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = TypeBindings.empty();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        assertNotNull(resolvedRecursiveType);
    }
}