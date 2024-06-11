import static org.junit.Assert.*;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(String.class, null);
        resolvedType.setReference(JavaType.forClass(Integer.class));
        assertEquals(JavaType.forClass(Integer.class), resolvedType.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(String.class, null);
        StringBuilder sb = new StringBuilder();
        assertEquals(new StringBuilder("L" + JavaType.forClass(String.class).getRawClass().getName() + ";"), resolvedType.getGenericSignature(sb));
    }

    @Test
    public void testInvalidSetReference() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(String.class, null);
        resolvedType.setReference(JavaType.forClass(Integer.class));
        try {
            resolvedType.setReference(JavaType.forClass(Double.class));
            fail("Exception not thrown for re-setting self reference");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType resolvedType1 = new ResolvedRecursiveType(String.class, null);
        ResolvedRecursiveType resolvedType2 = new ResolvedRecursiveType(String.class, null);
        resolvedType1.setReference(JavaType.forClass(Integer.class));
        resolvedType2.setReference(JavaType.forClass(Integer.class));
        assertTrue(resolvedType1.equals(resolvedType2));
    }

    // Add more test cases for other methods as needed...

}