
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import org.junit.Test;

import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        // Test setting reference to self
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        JavaType referenceType = new JavaType(this.getClass());
        resolvedType.setReference(referenceType);
        assertEquals(referenceType, resolvedType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceMultipleTimes() {
        // Test setting reference multiple times
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        JavaType referenceType = new JavaType(this.getClass());
        resolvedType.setReference(referenceType);
        resolvedType.setReference(referenceType);
    }

    @Test
    public void testGetGenericSignature() {
        // Test getting generic signature
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        JavaType referenceType = new JavaType(this.getClass());
        resolvedType.setReference(referenceType);
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        referenceType.getGenericSignature(expected);
        assertEquals(expected.toString(), resolvedType.getGenericSignature(sb).toString());
    }

    @Test
    public void testGetErasedSignature() {
        // Test getting erased signature
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        JavaType referenceType = new JavaType(this.getClass());
        resolvedType.setReference(referenceType);
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        referenceType.getErasedSignature(expected);
        assertEquals(expected.toString(), resolvedType.getErasedSignature(sb).toString());
    }

    // Rest of the test cases...

}
