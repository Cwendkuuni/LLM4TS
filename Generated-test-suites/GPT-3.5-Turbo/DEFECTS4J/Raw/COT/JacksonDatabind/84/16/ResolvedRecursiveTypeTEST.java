import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        JavaType ref = new ResolvedRecursiveType(Object.class, null);
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        resolvedRecursiveType.setReference(ref);
        assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        JavaType ref1 = new ResolvedRecursiveType(Object.class, null);
        JavaType ref2 = new ResolvedRecursiveType(String.class, null);
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        resolvedRecursiveType.setReference(ref1);
        resolvedRecursiveType.setReference(ref2);
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        JavaType ref = new ResolvedRecursiveType(Object.class, null);
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        resolvedRecursiveType.setReference(ref);
        resolvedRecursiveType.getGenericSignature(sb);
        assertTrue(sb.toString().contains("Object"));
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        JavaType ref = new ResolvedRecursiveType(Object.class, null);
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        resolvedRecursiveType.setReference(ref);
        resolvedRecursiveType.getErasedSignature(sb);
        assertTrue(sb.toString().contains("Ljava/lang/Object;"));
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new ResolvedRecursiveType(Object.class, null);
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        JavaType result = resolvedRecursiveType.withContentType(contentType);
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithTypeHandler() {
        Object handler = new Object();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        JavaType result = resolvedRecursiveType.withTypeHandler(handler);
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object handler = new Object();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        JavaType result = resolvedRecursiveType.withContentTypeHandler(handler);
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithValueHandler() {
        Object handler = new Object();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        JavaType result = resolvedRecursiveType.withValueHandler(handler);
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentValueHandler() {
        Object handler = new Object();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        JavaType result = resolvedRecursiveType.withContentValueHandler(handler);
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        JavaType result = resolvedRecursiveType.withStaticTyping();
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void test_narrow() {
        Class<?> subclass = Object.class;
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        JavaType result = resolvedRecursiveType._narrow(subclass);
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testRefine() {
        Class<?> rawType = Object.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        JavaType result = resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces);
        assertNull(result);
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        assertFalse(resolvedRecursiveType.isContainerType());
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
        assertEquals("[recursive type; UNRESOLVED]", resolvedRecursiveType.toString());
    }

    @Test
    public void testEquals() {
        JavaType ref1 = new ResolvedRecursiveType(Object.class, null);
        JavaType ref2 = new ResolvedRecursiveType(String.class, null);
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(Object.class, null);
        resolvedRecursiveType1.setReference(ref1);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(Object.class, null);
        resolvedRecursiveType2.setReference(ref1);
        ResolvedRecursiveType resolvedRecursiveType3 = new ResolvedRecursiveType(Object.class, null);
        resolvedRecursiveType3.setReference(ref2);
        assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType2));
        assertFalse(resolvedRecursiveType2.equals(resolvedRecursiveType3));
    }

}