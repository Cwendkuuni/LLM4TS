import static org.junit.Assert.*;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref = new ResolvedRecursiveType(Integer.class, new TypeBindings());
        
        resolvedType.setReference(ref);
        
        assertEquals(ref, resolvedType.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignatureWhenNoReference() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(String.class, new TypeBindings());
        
        StringBuilder sb = new StringBuilder();
        StringBuilder result = resolvedType.getGenericSignature(sb);
        
        assertEquals("UNRESOLVED", result.toString());
    }

    @Test
    public void testGetGenericSignatureWhenReferenceAvailable() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref = new ResolvedRecursiveType(Integer.class, new TypeBindings());
        resolvedType.setReference(ref);
        
        StringBuilder sb = new StringBuilder();
        StringBuilder result = resolvedType.getGenericSignature(sb);
        
        assertEquals(Integer.class.getName(), result.toString());
    }

    // Add more test methods for the remaining public methods

}