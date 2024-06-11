import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    @Test(expected = IllegalStateException.class)
    public void testSetReference() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Object.class, null);
        JavaType referencedType = new ResolvedRecursiveType(Integer.class, null);
        resolvedType.setReference(referencedType);
        resolvedType.setReference(referencedType);
    }

    @Test
    public void testGetSelfReferencedType() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Object.class, null);
        JavaType referencedType = new ResolvedRecursiveType(Integer.class, null);
        resolvedType.setReference(referencedType);
        assertEquals(referencedType, resolvedType.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Object.class, null);
        JavaType referencedType = new ResolvedRecursiveType(Integer.class, null);
        resolvedType.setReference(referencedType);
        StringBuilder sb = new StringBuilder();
        assertEquals(referencedType.getGenericSignature(sb), resolvedType.getGenericSignature(new StringBuilder()));
        assertEquals(sb.toString(), resolvedType.getGenericSignature(new StringBuilder()));
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Object.class, null);
        JavaType referencedType = new ResolvedRecursiveType(Integer.class, null);
        resolvedType.setReference(referencedType);
        StringBuilder sb = new StringBuilder();
        assertEquals(referencedType.getErasedSignature(sb), resolvedType.getErasedSignature(new StringBuilder()));
        assertEquals(sb.toString(), resolvedType.getErasedSignature(new StringBuilder()));
    }

    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Object.class, null);
        assertEquals(resolvedType, resolvedType.withTypeHandler(null));
    }

    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Object.class, null);
        assertEquals(resolvedType, resolvedType.withContentTypeHandler(null));
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Object.class, null);
        assertEquals(resolvedType, resolvedType.withValueHandler(null));
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Object.class, null);
        assertEquals(resolvedType, resolvedType.withContentValueHandler(null));
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Object.class, null);
        assertEquals(resolvedType, resolvedType.withStaticTyping());
    }

    @Test
    public void testNarrow() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Object.class, null);
        assertEquals(resolvedType, resolvedType._narrow(null));
    }

    @Test
    public void testRefine() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Object.class, null);
        assertNull(resolvedType.refine(null, null, null, null));
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Object.class, null);
        assertFalse(resolvedType.isContainerType());
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Object.class, null);
        String expected = "[recursive type; UNRESOLVED]";
        assertEquals(expected, resolvedType.toString());
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType resolvedType1 = new ResolvedRecursiveType(Object.class, null);
        ResolvedRecursiveType resolvedType2 = new ResolvedRecursiveType(Object.class, null);
        assertTrue(resolvedType1.equals(resolvedType2));
        assertFalse(resolvedType1.equals(null));
        assertFalse(resolvedType1.equals("string"));
        assertFalse(resolvedType1.equals(new Object()));
        resolvedType1.setReference(new ResolvedRecursiveType(Integer.class, null));
        assertFalse(resolvedType1.equals(resolvedType2));
    }
    
}