import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        JavaType ref = new JavaType();
        type.setReference(ref);
        assertEquals(ref, type.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceMultipleTimes() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        JavaType ref1 = new JavaType();
        JavaType ref2 = new JavaType();
        type.setReference(ref1);
        type.setReference(ref2);
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        JavaType ref = new JavaType();
        type.setReference(ref);
        ref.getGenericSignature(expected);
        type.getGenericSignature(sb);
        assertEquals(expected.toString(), sb.toString());
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        JavaType ref = new JavaType();
        type.setReference(ref);
        ref.getErasedSignature(expected);
        type.getErasedSignature(sb);
        assertEquals(expected.toString(), sb.toString());
    }

    @Test
    public void testWithContentType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        JavaType contentType = new JavaType();
        assertSame(type, type.withContentType(contentType));
    }

    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        Object handler = new Object();
        assertSame(type, type.withTypeHandler(handler));
    }

    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        Object handler = new Object();
        assertSame(type, type.withContentTypeHandler(handler));
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        Object handler = new Object();
        assertSame(type, type.withValueHandler(handler));
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        Object handler = new Object();
        assertSame(type, type.withContentValueHandler(handler));
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        assertSame(type, type.withStaticTyping());
    }

    @Test
    public void testNarrow() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        Class<?> subclass = ResolvedRecursiveType.class;
        assertSame(type, type._narrow(subclass));
    }

    @Test
    public void testRefine() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        Class<?> rawType = ResolvedRecursiveType.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        assertNull(type.refine(rawType, bindings, superClass, superInterfaces));
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        assertFalse(type.isContainerType());
    }

    @Test
    public void testToStringUnresolved() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        assertEquals("[recursive type; UNRESOLVED]", type.toString());
    }

    @Test
    public void testToStringResolved() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        JavaType ref = new JavaType();
        type.setReference(ref);
        assertEquals("[recursive type; " + ResolvedRecursiveType.class.getName() + "]", type.toString());
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        JavaType ref = new JavaType();
        type1.setReference(ref);
        type2.setReference(ref);
        assertTrue(type1.equals(type2));
    }
}