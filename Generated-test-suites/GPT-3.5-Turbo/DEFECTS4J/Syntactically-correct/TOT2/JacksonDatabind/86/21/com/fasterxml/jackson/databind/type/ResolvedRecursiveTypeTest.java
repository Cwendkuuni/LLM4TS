import static org.junit.Assert.*;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        JavaType ref = new ResolvedType(String.class, null);
        type.setReference(ref);
        assertEquals(ref, type.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        StringBuilder sb = new StringBuilder();
        type.setReference(new ResolvedType(Integer.class, null));
        assertEquals("Ljava/lang/Integer;", type.getGenericSignature(sb).toString());
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(String.class, null);
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(Integer.class, null);
        assertFalse(type1.equals(type2));
    }

    @Test
    public void testRefine() {
        JavaType superClass = new ResolvedType(String.class, null);
        ResolvedRecursiveType type = new ResolvedRecursiveType(Integer.class, null);
        assertNull(type.refine(String.class, null, superClass, null));
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        assertEquals("[recursive type; java.lang.String", type.toString());
    }

}