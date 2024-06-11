import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class ResolvedRecursiveTypeTest {

    private ResolvedRecursiveType resolvedRecursiveType;

    @Before
    public void setUp() {
        resolvedRecursiveType = new ResolvedRecursiveType(String.class, TypeBindings.emptyBindings());
    }

    @Test
    public void testSetReference() {
        JavaType ref = JavaType.construct(String.class);
        resolvedRecursiveType.setReference(ref);
        assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder result = resolvedRecursiveType.getGenericSignature(sb);
        assertEquals(resolvedRecursiveType.getSelfReferencedType().getGenericSignature(sb), result);
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder result = resolvedRecursiveType.getErasedSignature(sb);
        assertEquals(resolvedRecursiveType.getSelfReferencedType().getErasedSignature(sb), result);
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = JavaType.construct(String.class);
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
    public void testWithStaticTyping() {
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withStaticTyping());
    }

    @Test
    public void testNarrow() {
        Class<?> subclass = String.class;
        assertEquals(resolvedRecursiveType, resolvedRecursiveType._narrow(subclass));
    }

    @Test
    public void testRefine() {
        Class<?> rawType = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = JavaType.construct(String.class);
        JavaType[] superInterfaces = new JavaType[0];
        assertEquals(null, resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces));
    }

    @Test
    public void testIsContainerType() {
        assertFalse(resolvedRecursiveType.isContainerType());
    }

    @Test
    public void testToString() {
        String result = resolvedRecursiveType.toString();
        assertTrue(result.startsWith("[recursive type;"));
        if (resolvedRecursiveType.getSelfReferencedType() == null) {
            assertTrue(result.contains("UNRESOLVED"));
        } else {
            assertTrue(result.contains(resolvedRecursiveType.getSelfReferencedType().getRawClass().getName()));
        }
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType sameInstance = resolvedRecursiveType;
        ResolvedRecursiveType differentInstance = new ResolvedRecursiveType(String.class, TypeBindings.emptyBindings());

        assertTrue(resolvedRecursiveType.equals(sameInstance));
        assertFalse(resolvedRecursiveType.equals(differentInstance));
    }
}