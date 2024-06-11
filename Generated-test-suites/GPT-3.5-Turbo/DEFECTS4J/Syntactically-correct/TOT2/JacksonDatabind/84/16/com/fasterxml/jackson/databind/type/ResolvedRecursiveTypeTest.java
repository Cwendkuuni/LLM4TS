import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        JavaType ref = new JavaType();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Class.class, new TypeBindings());
        resolvedRecursiveType.setReference(ref);
        assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        JavaType ref = new JavaType();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Class.class, new TypeBindings());
        resolvedRecursiveType.setReference(ref);

        assertEquals(ref.getGenericSignature(sb).toString(), resolvedRecursiveType.getGenericSignature(sb).toString());
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Class.class, new TypeBindings());
        assertEquals("[recursive type; UNRESOLVED", resolvedRecursiveType.toString());
    }

    @Test
    public void testEquals() {
        JavaType ref = new JavaType();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Class.class, new TypeBindings());
        resolvedRecursiveType.setReference(ref);

        ResolvedRecursiveType other = new ResolvedRecursiveType(Class.class, new TypeBindings());
        other.setReference(new JavaType());
        
        assertFalse(resolvedRecursiveType.equals(other));
    }

    //Additional edge cases and error scenarios can be added here for further testing.

}