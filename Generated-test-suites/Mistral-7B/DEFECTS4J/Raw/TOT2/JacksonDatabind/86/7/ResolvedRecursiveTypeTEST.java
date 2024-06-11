import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testResolvedRecursiveTypeConstructor() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        assertNotNull(resolvedRecursiveType);
    }
}