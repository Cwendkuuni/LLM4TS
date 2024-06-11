import static org.junit.Assert.*;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(null,null);
        JavaType ref1 = new JavaType();
        rrt.setReference(ref1);
        assertEquals(ref1, rrt.getSelfReferencedType());

        JavaType ref2 = new JavaType();
        try {
            rrt.setReference(ref2);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertEquals("Trying to re-set self reference; old value = "+ref1+", new = "+ref2, e.getMessage());
        }
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(null,null);
        JavaType ref = new JavaType();
        rrt.setReference(ref);

        StringBuilder sb = new StringBuilder();
        rrt.getGenericSignature(sb);
        // Add assertions here
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(null,null);
        JavaType ref = new JavaType();
        rrt.setReference(ref);

        StringBuilder sb = new StringBuilder();
        rrt.getErasedSignature(sb);
        // Add assertions here
    }

    @Test
    public void testWithContentType() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(null,null);
        JavaType contentType = new JavaType();
        JavaType result = rrt.withContentType(contentType);
        assertEquals(rrt, result);
    }

    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(null,null);
        Object handler = new Object();
        JavaType result = rrt.withTypeHandler(handler);
        assertEquals(rrt, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(null,null);
        Object handler = new Object();
        JavaType result = rrt.withContentTypeHandler(handler);
        assertEquals(rrt, result);
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(null,null);
        Object handler = new Object();
        JavaType result = rrt.withValueHandler(handler);
        assertEquals(rrt, result);
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(null,null);
        Object handler = new Object();
        JavaType result = rrt.withContentValueHandler(handler);
        assertEquals(rrt, result);
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(null,null);
        JavaType result = rrt.withStaticTyping();
        assertEquals(rrt, result);
    }

    @Test
    public void test_narrow() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(null,null);
        Class<?> subclass = Object.class;
        JavaType result = rrt._narrow(subclass);
        assertEquals(rrt, result);
    }

    @Test
    public void testRefine() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(null,null);
        Class<?> rawType = Object.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType result = rrt.refine(rawType, bindings, superClass, superInterfaces);
        assertNull(result);
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(null, null);
        assertFalse(rrt.isContainerType());
    }

    @Test
    public void testToString1() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(null,null);
        String expected = "[recursive type; UNRESOLVED]";
        assertEquals(expected, rrt.toString());
    }

    @Test
    public void testToString2() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(null,null);
        JavaType ref = new JavaType();
        rrt.setReference(ref);
        String expected = "[recursive type; null]";
        assertEquals(expected, rrt.toString());
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType rrt1 = new ResolvedRecursiveType(null,null);
        ResolvedRecursiveType rrt2 = new ResolvedRecursiveType(null,null);
        JavaType ref = new JavaType();
        rrt1.setReference(ref);
        rrt2.setReference(ref);
        assertTrue(rrt1.equals(rrt2));

        rrt2.setReference(new JavaType());
        assertFalse(rrt1.equals(rrt2));
    }
}