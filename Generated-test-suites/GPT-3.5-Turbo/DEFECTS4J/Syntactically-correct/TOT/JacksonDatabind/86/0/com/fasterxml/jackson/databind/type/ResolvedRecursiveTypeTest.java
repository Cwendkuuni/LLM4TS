import static org.junit.Assert.*;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref = new JavaType();
        rrt.setReference(ref);
        assertEquals(ref, rrt.getSelfReferencedType());
    }

    @Test(expected=IllegalStateException.class)
    public void testSetReferenceMultipleTimes() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(Integer.class, new TypeBindings());
        JavaType ref1 = new JavaType();
        JavaType ref2 = new JavaType();
        rrt.setReference(ref1);
        rrt.setReference(ref2);
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(Double.class, new TypeBindings());
        StringBuilder sb = new StringBuilder();
        assertEquals(rrt.getSelfReferencedType().getGenericSignature(sb), rrt.getGenericSignature(sb).toString());
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(Float.class, new TypeBindings());
        StringBuilder sb = new StringBuilder();
        assertEquals(rrt.getSelfReferencedType().getErasedSignature(sb), rrt.getErasedSignature(sb).toString());
    }

    @Test
    public void testWithContentType() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(Boolean.class, new TypeBindings());
        JavaType contentType = new JavaType();
        assertEquals(rrt, rrt.withContentType(contentType));
    }

    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(Object.class, new TypeBindings());
        Object h = new Object();
        assertEquals(rrt, rrt.withTypeHandler(h));
    }

    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, new TypeBindings());
        Object h = new Object();
        assertEquals(rrt, rrt.withContentTypeHandler(h));
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(Long.class, new TypeBindings());
        Object h = new Object();
        assertEquals(rrt, rrt.withValueHandler(h));
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, new TypeBindings());
        Object h = new Object();
        assertEquals(rrt, rrt.withContentValueHandler(h));
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, new TypeBindings());
        assertEquals(rrt, rrt.withStaticTyping());
    }

    @Test
    public void testNarrow() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(Integer.class, new TypeBindings());
        Class<?> subclass = Object.class;
        assertEquals(rrt, rrt._narrow(subclass));
    }

    @Test
    public void testRefine() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(Number.class, new TypeBindings());
        Class<?> rawType = Object.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = new JavaType[] { new JavaType() };
        assertNull(rrt.refine(rawType, bindings, superClass, superInterfaces));
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(Boolean.class, new TypeBindings());
        assertFalse(rrt.isContainerType());
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType rrt1 = new ResolvedRecursiveType(String.class, new TypeBindings());
        assertEquals("[recursive type; java.lang.String]", rrt1.toString());

        ResolvedRecursiveType rrt2 = new ResolvedRecursiveType(null, new TypeBindings());
        assertEquals("[recursive type; UNRESOLVED]", rrt2.toString());
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType rrt1 = new ResolvedRecursiveType(String.class, new TypeBindings());
        ResolvedRecursiveType rrt2 = new ResolvedRecursiveType(String.class, new TypeBindings());
        assertTrue(rrt1.equals(rrt2));

        ResolvedRecursiveType rrt3 = new ResolvedRecursiveType(String.class, new TypeBindings());
        assertFalse(rrt1.equals(rrt3));

        assertFalse(rrt1.equals(null));
        assertFalse(rrt1.equals(new Object()));
    }
}