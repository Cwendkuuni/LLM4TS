import org.junit.*;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType();
        type.setReference(ref);
        assertEquals(ref, type.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = type.getGenericSignature(sb);
        assertEquals(sb, result);
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = type.getErasedSignature(sb);
        assertEquals(sb, result);
    }

    @Test
    public void testWithContentType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        JavaType contentType = new JavaType();
        JavaType result = type.withContentType(contentType);
        assertEquals(type, result);
    }

    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        Object h = new Object();
        JavaType result = type.withTypeHandler(h);
        assertEquals(type, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        Object h = new Object();
        JavaType result = type.withContentTypeHandler(h);
        assertEquals(type, result);
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        Object h = new Object();
        JavaType result = type.withValueHandler(h);
        assertEquals(type, result);
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        Object h = new Object();
        JavaType result = type.withContentValueHandler(h);
        assertEquals(type, result);
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        JavaType result = type.withStaticTyping();
        assertEquals(type, result);
    }

    @Test
    public void test_narrow() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        Class<?> subclass = Object.class;
        JavaType result = type._narrow(subclass);
        assertEquals(type, result);
    }

    @Test
    public void testRefine() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        Class<?> rawType = Object.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType result = type.refine(rawType, bindings, superClass, superInterfaces);
        assertNull(result);
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        boolean result = type.isContainerType();
        assertFalse(result);
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        String result = type.toString();
        assertTrue(result.contains("[recursive type;"));
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        ResolvedRecursiveType other = new ResolvedRecursiveType(null, null);
        assertTrue(type.equals(type));
        assertFalse(type.equals(null));
        assertFalse(type.equals(other));
    }
}