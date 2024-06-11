import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    private ResolvedRecursiveType resolvedRecursiveType;

    @Before
    public void setUp() {
        resolvedRecursiveType = new ResolvedRecursiveType(null, null);
    }

    @Test
    public void testSetReference() {
        assertNull(resolvedRecursiveType.getSelfReferencedType());
        JavaType javaType = new JavaType(null, null, false, false);
        resolvedRecursiveType.setReference(javaType);
        assertEquals(javaType, resolvedRecursiveType.getSelfReferencedType());
        try {
            resolvedRecursiveType.setReference(javaType);
        } catch (IllegalStateException e) {
            assertEquals("Trying to re-set self reference; old value = " + javaType + ", new = " + javaType, e.getMessage());
        }
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        assertNull(resolvedRecursiveType.getGenericSignature(sb));
        JavaType javaType = new JavaType(null, null, false, false);
        resolvedRecursiveType.setReference(javaType);
        assertEquals(javaType.getGenericSignature(sb), resolvedRecursiveType.getGenericSignature(sb));
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        assertNull(resolvedRecursiveType.getErasedSignature(sb));
        JavaType javaType = new JavaType(null, null, false, false);
        resolvedRecursiveType.setReference(javaType);
        assertEquals(javaType.getErasedSignature(sb), resolvedRecursiveType.getErasedSignature(sb));
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new JavaType(null, null, false, false);
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentType(contentType));
    }

    @Test
    public void testWithTypeHandler() {
        Object handler = new Object();
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withTypeHandler(handler));
    }

    @Test
    public void testWithContentTypeHandler() {
        Object handler = new Object();
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentTypeHandler(handler));
    }

    @Test
    public void testWithValueHandler() {
        Object handler = new Object();
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withValueHandler(handler));
    }

    @Test
    public void testWithContentValueHandler() {
        Object handler = new Object();
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentValueHandler(handler));
    }

    @Test
    public void testWithStaticTyping() {
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withStaticTyping());
    }

    @Test
    public void testNarrow() {
        Class<?> subclass = Object.class;
        assertEquals(resolvedRecursiveType, resolvedRecursiveType._narrow(subclass));
    }

    @Test
    public void testRefine() {
        Class<?> rawType = Object.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        assertNull(resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces));
    }

    @Test
    public void testIsContainerType() {
        assertFalse(resolvedRecursiveType.isContainerType());
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType unresolvedType = new ResolvedRecursiveType(null, null);
        assertEquals("[recursive type; UNRESOLVED]", unresolvedType.toString());
        JavaType javaType = new JavaType(null, null, false, false);
        resolvedRecursiveType.setReference(javaType);
        assertEquals("[recursive type; " + javaType.getRawClass().getName() + "]", resolvedRecursiveType.toString());
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType recursiveType1 = new ResolvedRecursiveType(null, null);
        ResolvedRecursiveType recursiveType2 = new ResolvedRecursiveType(null, null);
        JavaType javaType = new JavaType(null, null, false, false);
        recursiveType1.setReference(javaType);
        recursiveType2.setReference(javaType);
        assertTrue(recursiveType1.equals(recursiveType2));
        assertFalse(recursiveType1.equals(null));
        assertFalse(recursiveType1.equals(new Object()));
        assertFalse(recursiveType1.equals(new ResolvedRecursiveType(null, null)));
    }
}