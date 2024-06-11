import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    ResolvedRecursiveType resolvedRecursiveType;

    @Test
    public void testSetReference() {
        resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        assertNull(resolvedRecursiveType.getSelfReferencedType());

        JavaType ref = new JavaType();
        resolvedRecursiveType.setReference(ref);

        assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        assertNull(resolvedRecursiveType.getSelfReferencedType());

        StringBuilder sb = new StringBuilder();
        assertEquals(sb, resolvedRecursiveType.getGenericSignature(sb));
    }

    @Test
    public void testGetErasedSignature() {
        resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        assertNull(resolvedRecursiveType.getSelfReferencedType());

        StringBuilder sb = new StringBuilder();
        assertEquals(sb, resolvedRecursiveType.getErasedSignature(sb));
    }

    // More test cases for each public method...

}