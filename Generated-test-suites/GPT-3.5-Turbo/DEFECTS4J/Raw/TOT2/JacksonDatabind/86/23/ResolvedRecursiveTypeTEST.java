import static org.junit.Assert.*;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, null);
        JavaType refType = new SimpleType(String.class);
        recursiveType.setReference(refType);
        assertEquals(refType, recursiveType.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, null);
        JavaType refType = new SimpleType(String.class);
        recursiveType.setReference(refType);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = recursiveType.getGenericSignature(sb);
        assertEquals(refType.getGenericSignature(sb), result);
    }

    @Test
    public void testToStringWithResolved() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, null);
        JavaType refType = new SimpleType(String.class);
        recursiveType.setReference(refType);
        String expected = "[recursive type; java.lang.String";
        assertEquals(expected, recursiveType.toString());
    }

    @Test
    public void testEqualsWithSameObject() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, null);
        JavaType refType = new SimpleType(String.class);
        recursiveType.setReference(refType);
        assertTrue(recursiveType.equals(recursiveType));
    }
}