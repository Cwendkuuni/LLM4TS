import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;
import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {
    
    @Test
    public void testSetReference() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(String.class, null);
        JavaType refType = new JavaType(String.class);
        
        resolvedType.setReference(refType);
        
        assertEquals(refType, resolvedType.getSelfReferencedType());
    }
    
    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(String.class, null);
        JavaType refType = new JavaType(String.class);
        resolvedType.setReference(refType);
        
        StringBuilder sb = new StringBuilder();
        StringBuilder result = resolvedType.getGenericSignature(sb);
        
        assertEquals(sb, result);
    }
    
    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Integer.class, null);
        JavaType refType = new JavaType(Integer.class);
        resolvedType.setReference(refType);
        
        StringBuilder sb = new StringBuilder();
        StringBuilder result = resolvedType.getErasedSignature(sb);
        
        assertEquals(sb, result);
    }
    
    // Add more test cases for other public methods if needed
    
    // Edge cases and exception handling scenarios testing
    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Double.class, null);
        JavaType refType1 = new JavaType(Double.class);
        JavaType refType2 = new JavaType(Float.class);
        
        resolvedType.setReference(refType1);
        resolvedType.setReference(refType2);
    }
    
    // Add more edge cases and exception handling scenarios test as needed
    
}