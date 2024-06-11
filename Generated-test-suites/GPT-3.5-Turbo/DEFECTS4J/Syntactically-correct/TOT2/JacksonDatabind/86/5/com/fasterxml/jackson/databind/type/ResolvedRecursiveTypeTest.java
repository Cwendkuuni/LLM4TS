import static org.junit.Assert.*;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(String.class, null);
        JavaType reference = new SimpleType(Object.class);
        resolvedType.setReference(reference);
        assertEquals(reference, resolvedType.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(String.class, null);
        StringBuilder sb = new StringBuilder();
        assertEquals(sb, resolvedType.getGenericSignature(sb));
    }

    @Test
    public void testToStringUnresolved() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(String.class, null);
        assertEquals("[recursive type; UNRESOLVED", resolvedType.toString());
    }

    @Test
    public void testEqualsSame() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(String.class, null);
        assertTrue(resolvedType.equals(resolvedType));
    }

    @Test
    public void testEqualsDifferentClass() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(String.class, null);
        assertFalse(resolvedType.equals("string"));
    }

}