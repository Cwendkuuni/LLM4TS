import static org.junit.Assert.*;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        resolvedRecursiveType.setReference(JavaType.forType(String.class));
        assertEquals(String.class, resolvedRecursiveType.getSelfReferencedType().getRawClass());
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        StringBuilder sb = new StringBuilder();
        assertEquals("[recursive type; UNRESOLVED", resolvedRecursiveType.getGenericSignature(sb).toString());
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        StringBuilder sb = new StringBuilder();
        assertEquals("[recursive type; UNRESOLVED", resolvedRecursiveType.getErasedSignature(sb).toString());
    }

    // Add more test cases for the remaining public methods
}