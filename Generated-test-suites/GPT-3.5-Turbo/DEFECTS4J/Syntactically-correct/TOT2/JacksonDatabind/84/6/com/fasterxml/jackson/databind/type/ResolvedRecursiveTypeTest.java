import com.fasterxml.jackson.databind.JavaType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class ResolvedRecursiveTypeTest {

    private ResolvedRecursiveType resolvedRecursiveType;

    @Before
    public void setUp() {
        resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
    }

    @Test
    public void testSetReference() {
        JavaType ref = new ResolvedRecursiveType(String.class, new TypeBindings());
        resolvedRecursiveType.setReference(ref);
        assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        resolvedRecursiveType.setReference(new ResolvedRecursiveType(String.class, new TypeBindings()));
        resolvedRecursiveType.getGenericSignature(sb);
        expected.append("Lcom/fasterxml/jackson/databind/type/ResolvedRecursiveType;");
        assertEquals(expected.toString(), sb.toString());
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType other = new ResolvedRecursiveType(String.class, new TypeBindings());
        resolvedRecursiveType.setReference(new ResolvedRecursiveType(String.class, new TypeBindings()));
        assertTrue(resolvedRecursiveType.equals(resolvedRecursiveType));
        assertFalse(resolvedRecursiveType.equals(other));
    }

    // Additional test cases for other methods can be added here

}