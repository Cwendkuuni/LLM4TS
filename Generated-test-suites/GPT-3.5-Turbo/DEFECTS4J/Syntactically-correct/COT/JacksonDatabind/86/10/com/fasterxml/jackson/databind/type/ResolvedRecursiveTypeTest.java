import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType resolved = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType(null, null, null, 0, null, null, null, false);
        resolved.setReference(ref);
        assertEquals(ref, resolved.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceMultipleTimes() {
        ResolvedRecursiveType resolved = new ResolvedRecursiveType(null, null);
        JavaType ref1 = new JavaType(null, null, null, 0, null, null, null, false);
        JavaType ref2 = new JavaType(null, null, null, 0, null, null, null, false);
        resolved.setReference(ref1);
        resolved.setReference(ref2);
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType resolved = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType(null, null, null, 0, null, null, null, false);
        resolved.setReference(ref);
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        ref.getGenericSignature(expected);
        assertEquals(expected.toString(), resolved.getGenericSignature(sb).toString());
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType resolved = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType(null, null, null, 0, null, null, null, false);
        resolved.setReference(ref);
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        ref.getErasedSignature(expected);
        assertEquals(expected.toString(), resolved.getErasedSignature(sb).toString());
    }

    @Test
    public void testWithContentType() {
        ResolvedRecursiveType resolved = new ResolvedRecursiveType(null, null);
        JavaType contentType = new JavaType(null, null, null, 0, null, null, null, false);
        assertEquals(resolved, resolved.withContentType(contentType));
    }

    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType resolved = new ResolvedRecursiveType(null, null);
        Object handler = new Object();
        assertEquals(resolved, resolved.withTypeHandler(handler));
    }

    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType resolved = new ResolvedRecursiveType(null, null);
        Object handler = new Object();
        assertEquals(resolved, resolved.withContentTypeHandler(handler));
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType resolved = new ResolvedRecursiveType(null, null);
        Object handler = new Object();
        assertEquals(resolved, resolved.withValueHandler(handler));
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType resolved = new ResolvedRecursiveType(null, null);
        Object handler = new Object();
        assertEquals(resolved, resolved.withContentValueHandler(handler));
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType resolved = new ResolvedRecursiveType(null, null);
        assertEquals(resolved, resolved.withStaticTyping());
    }

    @Test
    public void testNarrow() {
        ResolvedRecursiveType resolved = new ResolvedRecursiveType(null, null);
        Class<?> subclass = Object.class;
        assertEquals(resolved, resolved._narrow(subclass));
    }

    @Test
    public void testRefine() {
        ResolvedRecursiveType resolved = new ResolvedRecursiveType(null, null);
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        assertNull(resolved.refine(rawType, bindings, superClass, superInterfaces));
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType resolved = new ResolvedRecursiveType(null, null);
        assertFalse(resolved.isContainerType());
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType resolved1 = new ResolvedRecursiveType(null, null);
        assertEquals("[recursive type; UNRESOLVED]", resolved1.toString());
        JavaType ref = new JavaType(null, null, null, 0, null, null, null, false);
        ResolvedRecursiveType resolved2 = new ResolvedRecursiveType(null, null);
        resolved2.setReference(ref);
        assertEquals("[recursive type; " + ref.getRawClass().getName() + "]", resolved2.toString());
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType resolved1 = new ResolvedRecursiveType(null, null);
        ResolvedRecursiveType resolved2 = new ResolvedRecursiveType(null, null);
        assertTrue(resolved1.equals(resolved1));
        assertTrue(resolved1.equals(resolved2));
        assertFalse(resolved1.equals(null));
        assertFalse(resolved1.equals(new Object()));
        resolved2.setReference(new JavaType(null, null, null, 0, null, null, null, false));
        assertFalse(resolved1.equals(resolved2));
    }
}