import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(erasedType, bindings);
        JavaType refType = new SimpleType(String.class);

        type1.setReference(refType);
        JavaType expected = refType;
        JavaType actual = type1.getSelfReferencedType();

        assertEquals(expected, actual);

        try {
            type1.setReference(refType);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetSelfReferencedType() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(erasedType, bindings);
        JavaType refType = new SimpleType(String.class);

        type1.setReference(refType);
        JavaType expected = refType;
        JavaType actual = type1.getSelfReferencedType();

        assertEquals(expected, actual);

        type1.setReference(null);
        expected = null;
        actual = type1.getSelfReferencedType();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetGenericSignature() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(erasedType, bindings);
        JavaType refType = new SimpleType(String[].class);

        StringBuilder sb = new StringBuilder();
        type1.getGenericSignature(sb);

        String expected = "<T[]>";
        String actual = sb.toString();

        assertEquals(expected, actual);

        type1.setReference(refType);
        type1.getGenericSignature(sb);

        expected = "<T[]>";
        actual = sb.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetErasedSignature() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(erasedType, bindings);
        JavaType refType = new SimpleType(String[].class);

        StringBuilder sb = new StringBuilder();
        type1.getErasedSignature(sb);

        String expected = "Ljava/lang/Object;[Ljava/lang/Object;";
        String actual = sb.toString();

        assertEquals(expected, actual);

        type1.setReference(refType);
        type1.getErasedSignature(sb);

        expected = "Ljava/lang/Object;[Ljava/lang/Object;";
        actual = sb.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(erasedType, bindings);
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(erasedType, bindings);
        JavaType refType = new SimpleType(String.class);

        type1.setReference(refType);
        type2.setReference(refType);

        assertTrue(type1.equals(type2));

        type1.setReference(null);
        type2.setReference(null);

        assertTrue(type1.equals(type2));

        type1.setReference(refType);
        type2.setReference(new SimpleType(Integer.class));

        assertFalse(type1.equals(type2));
    }
}