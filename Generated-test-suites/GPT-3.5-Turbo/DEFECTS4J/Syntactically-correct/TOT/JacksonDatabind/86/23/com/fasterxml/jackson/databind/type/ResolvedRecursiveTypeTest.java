import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class ResolvedRecursiveTypeTest {

    private ResolvedRecursiveType resolvedRecursiveType;

    @Before
    public void setup() {
        Class<?> clazz = Object.class;
        TypeBindings bindings = null;
        resolvedRecursiveType = new ResolvedRecursiveType(clazz, bindings);
    }

    @Test
    public void testSetReference() {
        JavaType ref = new JavaType();
        resolvedRecursiveType.setReference(ref);
        assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReference_reSetSelfReference() {
        JavaType ref1 = new JavaType();
        JavaType ref2 = new JavaType();
        resolvedRecursiveType.setReference(ref1);
        resolvedRecursiveType.setReference(ref2);
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        assertNotNull(resolvedRecursiveType.getGenericSignature(sb));
        assertTrue(sb.length() > 0);
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        assertNotNull(resolvedRecursiveType.getErasedSignature(sb));
        assertTrue(sb.length() > 0);
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new JavaType();
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
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = null;
        assertNull(resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces));
    }

    @Test
    public void testIsContainerType() {
        assertFalse(resolvedRecursiveType.isContainerType());
    }

    @Test
    public void testToString() {
        assertNotNull(resolvedRecursiveType.toString());
        assertTrue(resolvedRecursiveType.toString().startsWith("[recursive type;"));
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType sameType = new ResolvedRecursiveType(Object.class, null);
        sameType.setReference(new JavaType());
        assertTrue(resolvedRecursiveType.equals(sameType));

        ResolvedRecursiveType differentType = new ResolvedRecursiveType(Integer.class, null);
        differentType.setReference(new JavaType());
        assertFalse(resolvedRecursiveType.equals(differentType));

        assertFalse(resolvedRecursiveType.equals(null));
        assertFalse(resolvedRecursiveType.equals(new Object()));
    }
}