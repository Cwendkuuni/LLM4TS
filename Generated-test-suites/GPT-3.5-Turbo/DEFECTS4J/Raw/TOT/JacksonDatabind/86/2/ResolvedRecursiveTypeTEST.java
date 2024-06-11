import static org.junit.Assert.*;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        // Typical use case
        JavaType javaType = new JavaType();
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(Class.class, new TypeBindings());

        rrt.setReference(javaType);
        assertEquals(javaType, rrt.getSelfReferencedType());

        // Error scenario (calling setReference multiple times)
        try {
            rrt.setReference(new JavaType());
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertEquals("Trying to re-set self reference; old value = " + javaType + ", new = " + new JavaType(), e.getMessage());
        }
    }

    @Test
    public void testGetGenericSignature() {
        // Typical use case
        JavaType javaType = new JavaType();
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(Class.class, new TypeBindings());
        rrt.setReference(javaType);

        StringBuilder sb = new StringBuilder();
        StringBuilder resultSb = rrt.getGenericSignature(sb);
        assertEquals(resultSb, javaType.getGenericSignature(sb));
    }

    @Test
    public void testGetErasedSignature() {
        // Typical use case
        JavaType javaType = new JavaType();
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(Class.class, new TypeBindings());
        rrt.setReference(javaType);

        StringBuilder sb = new StringBuilder();
        StringBuilder resultSb = rrt.getErasedSignature(sb);
        assertEquals(resultSb, javaType.getErasedSignature(sb));
    }

    @Test
    public void testWithContentType() {
        // Typical use case
        JavaType javaType = new JavaType();
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(Class.class, new TypeBindings());
        rrt.setReference(javaType);

        assertEquals(rrt, rrt.withContentType(new JavaType()));
    }

    @Test
    public void testWithTypeHandler() {
        // Typical use case
        JavaType javaType = new JavaType();
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(Class.class, new TypeBindings());
        rrt.setReference(javaType);

        assertEquals(rrt, rrt.withTypeHandler(new Object()));
    }

    @Test
    public void testWithContentTypeHandler() {
        // Typical use case
        JavaType javaType = new JavaType();
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(Class.class, new TypeBindings());
        rrt.setReference(javaType);

        assertEquals(rrt, rrt.withContentTypeHandler(new Object()));
    }

    @Test
    public void testWithValueHandler() {
        // Typical use case
        JavaType javaType = new JavaType();
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(Class.class, new TypeBindings());
        rrt.setReference(javaType);

        assertEquals(rrt, rrt.withValueHandler(new Object()));
    }

    @Test
    public void testWithContentValueHandler() {
        // Typical use case
        JavaType javaType = new JavaType();
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(Class.class, new TypeBindings());
        rrt.setReference(javaType);

        assertEquals(rrt, rrt.withContentValueHandler(new Object()));
    }

    @Test
    public void testWithStaticTyping() {
        // Typical use case
        JavaType javaType = new JavaType();
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(Class.class, new TypeBindings());
        rrt.setReference(javaType);

        assertEquals(rrt, rrt.withStaticTyping());
    }

    @Test
    public void testNarrow() {
        // Typical use case
        JavaType javaType = new JavaType();
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(Class.class, new TypeBindings());
        rrt.setReference(javaType);

        assertEquals(rrt, rrt._narrow(Class.class));
    }

    @Test
    public void testRefine() {
        // Typical use case
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(Class.class, new TypeBindings());
        assertNull(rrt.refine(Class.class, new TypeBindings(), new JavaType(), new JavaType[]{}));
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(Class.class, new TypeBindings());
        assertFalse(rrt.isContainerType());
    }

    @Test
    public void testToString() {
        // Typical use case (resolved type)
        JavaType javaType = new JavaType();
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(Class.class, new TypeBindings());
        rrt.setReference(javaType);
        assertEquals("[recursive type; " + javaType.getRawClass().getName() + "]", rrt.toString());

        // Edges case (unresolved type)
        ResolvedRecursiveType unresolved = new ResolvedRecursiveType(Class.class, new TypeBindings());
        assertEquals("[recursive type; UNRESOLVED]", unresolved.toString());
    }

    @Test
    public void testEquals() {
        // Typical use case (equals)
        JavaType javaType = new JavaType();
        ResolvedRecursiveType rrt1 = new ResolvedRecursiveType(Class.class, new TypeBindings());
        ResolvedRecursiveType rrt2 = new ResolvedRecursiveType(Class.class, new TypeBindings());
        rrt1.setReference(javaType);
        rrt2.setReference(javaType);

        assertTrue(rrt1.equals(rrt2));

        // Typical use case (not equals)
        JavaType anotherJavaType = new JavaType();
        ResolvedRecursiveType rrt3 = new ResolvedRecursiveType(Class.class, new TypeBindings());
        rrt3.setReference(anotherJavaType);

        assertFalse(rrt1.equals(rrt3));

        // Edges case (null reference type)
        assertFalse(rrt1.equals(new ResolvedRecursiveType(Class.class, new TypeBindings())));
    }
}