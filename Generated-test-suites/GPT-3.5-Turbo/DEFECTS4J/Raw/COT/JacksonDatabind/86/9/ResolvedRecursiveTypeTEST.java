import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType resolved = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType(Object.class);
        resolved.setReference(ref);
        assertEquals(ref, resolved.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceMultipleTimes() {
        ResolvedRecursiveType resolved = new ResolvedRecursiveType(null, null);
        JavaType ref1 = new JavaType(Object.class);
        JavaType ref2 = new JavaType(String.class);
        resolved.setReference(ref1);
        resolved.setReference(ref2);
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType resolved = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType(Object.class);
        resolved.setReference(ref);
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = ref.getGenericSignature(sb);
        StringBuilder result = resolved.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType resolved = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType(Object.class);
        resolved.setReference(ref);
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = ref.getErasedSignature(sb);
        StringBuilder result = resolved.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testWithContentType() {
        ResolvedRecursiveType resolved = new ResolvedRecursiveType(null, null);
        JavaType contentType = new JavaType(String.class);
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
    public void testNarrowType() {
        ResolvedRecursiveType resolved = new ResolvedRecursiveType(null, null);
        Class<?> subclass = String.class;
        assertEquals(resolved, resolved._narrow(subclass));
    }

    @Test
    public void testRefine() {
        ResolvedRecursiveType resolved = new ResolvedRecursiveType(null, null);
        Class<?> rawType = Object.class;
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
        ResolvedRecursiveType resolved = new ResolvedRecursiveType(null, null);
        StringBuilder sb = new StringBuilder(40).append("[recursive type; UNRESOLVED");
        assertEquals(sb.toString(), resolved.toString());
        
        ResolvedRecursiveType resolved2 = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType(Object.class);
        resolved2.setReference(ref);
        StringBuilder sb2 = new StringBuilder(40).append("[recursive type; java.lang.Object");
        assertEquals(sb2.toString(), resolved2.toString());
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType resolved = new ResolvedRecursiveType(null, null);
        ResolvedRecursiveType resolved2 = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType(Object.class);
        resolved.setReference(ref);
        resolved2.setReference(ref);
        assertTrue(resolved.equals(resolved2));
        
        ResolvedRecursiveType resolved3 = new ResolvedRecursiveType(null, null);
        resolved3.setReference(new JavaType(String.class));
        assertFalse(resolved.equals(resolved3));
        
        assertTrue(resolved.equals(resolved));
        assertFalse(resolved.equals(null));
        assertFalse(resolved.equals(new Object()));
    }
}