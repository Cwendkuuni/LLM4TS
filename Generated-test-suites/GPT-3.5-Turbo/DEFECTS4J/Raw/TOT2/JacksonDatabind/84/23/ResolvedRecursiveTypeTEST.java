import static org.junit.Assert.*;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        resolvedRecursiveType.setReference(JavaType.forType(String.class));
        assertEquals(JavaType.forType(String.class), resolvedRecursiveType.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Integer.class, new TypeBindings());
        StringBuilder sb = new StringBuilder();
        assertEquals(sb.append("int"), resolvedRecursiveType.getGenericSignature(sb).toString());
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(String.class, new TypeBindings());
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(String.class, new TypeBindings());
        assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType2));
    }
    
    // Add more test cases for other methods

}