import static org.junit.Assert.*;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {
    
    @Test
    public void testSetReference() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref = new SimpleType(String.class);
        
        resolvedType.setReference(ref);
        
        assertEquals(ref, resolvedType.getSelfReferencedType());
    }
    
    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref = new SimpleType(String.class);
        resolvedType.setReference(ref);
        
        StringBuilder sb = new StringBuilder();
        StringBuilder result = resolvedType.getGenericSignature(sb);
        
        assertEquals(sb, result);
    }
    
    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref = new SimpleType(String.class);
        resolvedType.setReference(ref);
        
        StringBuilder sb = new StringBuilder();
        StringBuilder result = resolvedType.getErasedSignature(sb);
        
        assertEquals(sb, result);
    }
    
    // Add more test cases for the remaining public methods
    
}