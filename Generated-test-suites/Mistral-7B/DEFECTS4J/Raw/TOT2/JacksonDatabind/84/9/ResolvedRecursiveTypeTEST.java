import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testConstructor() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = TypeBindings.empty();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        assertEquals(erasedType, resolvedRecursiveType.getRawClass());
        assertEquals(bindings, resolvedRecursiveType.getTypeBindings());
        assertNull(resolvedRecursiveType.getSelfReferencedType());
    }
}