import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testConstructor() {
        Class<Integer> integerType = Integer.TYPE;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(integerType, bindings);

        assertEquals(integerType, resolvedRecursiveType.getErasedType());
        assertNull(resolvedRecursiveType.getSelfReferencedType());
    }
}