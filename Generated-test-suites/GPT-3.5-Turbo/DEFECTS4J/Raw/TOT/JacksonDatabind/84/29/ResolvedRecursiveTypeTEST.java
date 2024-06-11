import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    private ResolvedRecursiveType resolvedRecursiveType;

    @Before
    public void setUp() {
        resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
    }

    @Test
    public void testSetReference() {
        JavaType ref = new JavaType();
        resolvedRecursiveType.setReference(ref);
        assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwiceShouldThrowException() {
        JavaType ref1 = new JavaType();
        resolvedRecursiveType.setReference(ref1);

        JavaType ref2 = new JavaType();
        resolvedRecursiveType.setReference(ref2);
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        assertEquals(expected.toString(), resolvedRecursiveType.getGenericSignature(sb).toString());
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        assertEquals(expected.toString(), resolvedRecursiveType.getErasedSignature(sb).toString());
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
        Class<?> subclass = String.class;
        assertEquals(resolvedRecursiveType, resolvedRecursiveType._narrow(subclass));
    }

    @Test
    public void testRefine() {
        Class<?> rawType = String.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = new JavaType[0];
        assertNull(resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces));
    }

    @Test
    public void testIsContainerType() {
        assertFalse(resolvedRecursiveType.isContainerType());
    }

    @Test
    public void testToString() {
        StringBuilder sb = new StringBuilder(40);
        sb.append("[recursive type; ");
        sb.append("UNRESOLVED");
        sb.append("]");
        assertEquals(sb.toString(), resolvedRecursiveType.toString());
    }

    @Test
    public void testEqualsWithNull() {
        assertFalse(resolvedRecursiveType.equals(null));
    }

    @Test
    public void testEqualsWithDifferentClass() {
        assertFalse(resolvedRecursiveType.equals(new Object()));
    }

    @Test
    public void testEqualsWithDifferentSelfReferencedType() {
        JavaType ref1 = new JavaType();
        resolvedRecursiveType.setReference(ref1);

        ResolvedRecursiveType other = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref2 = new JavaType();
        other.setReference(ref2);

        assertFalse(resolvedRecursiveType.equals(other));
    }

    @Test
    public void testEqualsWithSameInstance() {
        assertTrue(resolvedRecursiveType.equals(resolvedRecursiveType));
    }

    @Test
    public void testEqualsWithSameSelfReferencedType() {
        JavaType ref1 = new JavaType();
        resolvedRecursiveType.setReference(ref1);

        ResolvedRecursiveType other = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref2 = new JavaType();
        other.setReference(ref2);

        assertTrue(resolvedRecursiveType.equals(other));
    }
}