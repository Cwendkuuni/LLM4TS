import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);

        JavaType referencedType = new JavaType();
        resolvedRecursiveType.setReference(referencedType);

        assertEquals(referencedType, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceMultipleTimes() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);

        JavaType referencedType1 = new JavaType();
        resolvedRecursiveType.setReference(referencedType1);

        JavaType referencedType2 = new JavaType();
        resolvedRecursiveType.setReference(referencedType2);
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        StringBuilder sb = new StringBuilder();
        StringBuilder genericSignature = resolvedRecursiveType.getGenericSignature(sb);

        assertNotNull(genericSignature);
        assertEquals(sb, genericSignature);
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        StringBuilder sb = new StringBuilder();
        StringBuilder erasedSignature = resolvedRecursiveType.getErasedSignature(sb);

        assertNotNull(erasedSignature);
        assertEquals(sb, erasedSignature);
    }

    @Test
    public void testWithContentType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType contentType = new JavaType();
        JavaType result = resolvedRecursiveType.withContentType(contentType);

        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withTypeHandler(handler);

        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withContentTypeHandler(handler);

        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withValueHandler(handler);

        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withContentValueHandler(handler);

        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType result = resolvedRecursiveType.withStaticTyping();

        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testNarrow() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Class<?> subclass = new Class<?>();
        JavaType result = resolvedRecursiveType._narrow(subclass);

        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testRefine() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Class<?> rawType = new Class<?>();
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = new JavaType[1];
        JavaType result = resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces);

        assertNull(result);
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        boolean isContainer = resolvedRecursiveType.isContainerType();

        assertFalse(isContainer);
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        String str = resolvedRecursiveType.toString();

        assertNotNull(str);
        assertTrue(str.contains("[recursive type;"));
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(null, null);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(null, null);

        assertEquals(resolvedRecursiveType1, resolvedRecursiveType1);
        assertEquals(resolvedRecursiveType1, resolvedRecursiveType2);
        assertEquals(resolvedRecursiveType2, resolvedRecursiveType1);
    }
}