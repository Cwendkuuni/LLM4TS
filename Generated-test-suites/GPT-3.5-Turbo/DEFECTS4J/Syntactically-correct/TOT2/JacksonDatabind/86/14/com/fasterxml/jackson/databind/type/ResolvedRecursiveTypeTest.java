import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        resolvedRecursiveType.setReference(new JavaType());
        assertEquals("Should set reference correctly", new JavaType(), resolvedRecursiveType.getSelfReferencedType());
        // Edge case: try to set reference again
        try {
            resolvedRecursiveType.setReference(new JavaType());
            fail("Should throw IllegalStateException when trying to re-set self reference");
        } catch (IllegalStateException e) {
            // expected exception
        }
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        StringBuilder sb = new StringBuilder();
        StringBuilder result = resolvedRecursiveType.getGenericSignature(sb);
        assertEquals("Should return the referenced type's generic signature", new JavaType().getGenericSignature(sb), result);
    }

    // Add test cases for other public methods...

}