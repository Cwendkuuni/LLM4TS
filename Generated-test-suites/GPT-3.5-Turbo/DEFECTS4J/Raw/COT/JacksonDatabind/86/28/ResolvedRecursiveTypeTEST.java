import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    private ResolvedRecursiveType resolvedRecursiveType;

    @Before
    public void setUp() {
        resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
    }

    @Test
    public void testSetReference() {
        JavaType reference = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        resolvedRecursiveType.setReference(reference);

        assertEquals(reference, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceMultipleTimes() {
        JavaType reference1 = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        JavaType reference2 = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());

        resolvedRecursiveType.setReference(reference1);
        resolvedRecursiveType.setReference(reference2);
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        resolvedRecursiveType.setReference(new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings()));
        resolvedRecursiveType.getGenericSignature(sb);

        assertEquals(expected.toString(), sb.toString());
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        resolvedRecursiveType.setReference(new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings()));
        resolvedRecursiveType.getErasedSignature(sb);

        assertEquals(expected.toString(), sb.toString());
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
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
        Class<?> subclass = ResolvedRecursiveType.class;
        assertEquals(resolvedRecursiveType, resolvedRecursiveType._narrow(subclass));
    }

    @Test
    public void testRefine() {
        Class<?> rawType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
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
        String expected = "[recursive type; UNRESOLVED]";
        assertEquals(expected, resolvedRecursiveType.toString());
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType other = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        resolvedRecursiveType.setReference(other);

        assertTrue(resolvedRecursiveType.equals(other));
    }
}