import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;

public class ResolvedRecursiveTypeTest {

    private ResolvedRecursiveType resolvedRecursiveType;

    @Before
    public void setup() {
        resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
    }

    @Test
    public void testSetReference() {
        JavaType ref = new JavaType(String.class);
        resolvedRecursiveType.setReference(ref);
        assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        assertEquals(ref.getGenericSignature(sb), resolvedRecursiveType.getGenericSignature(sb));
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        assertEquals(ref.getErasedSignature(sb), resolvedRecursiveType.getErasedSignature(sb));
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new JavaType(String.class);
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentType(contentType));
    }

    @Test
    public void testWithTypeHandler() {
        Object h = new Object();
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withTypeHandler(h));
    }

    @Test
    public void testWithContentTypeHandler() {
        Object h = new Object();
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentTypeHandler(h));
    }

    @Test
    public void testWithValueHandler() {
        Object h = new Object();
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withValueHandler(h));
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = new Object();
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentValueHandler(h));
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
        JavaType superClass = new JavaType(Object.class);
        JavaType[] superInterfaces = new JavaType[0];
        assertNull(resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces));
    }

    @Test
    public void testIsContainerType() {
        assertFalse(resolvedRecursiveType.isContainerType());
    }

    @Test
    public void testToString() {
        StringBuilder expected = new StringBuilder(40)
                .append("[recursive type; UNRESOLVED");
        assertEquals(expected.toString(), resolvedRecursiveType.toString());
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType other = new ResolvedRecursiveType(String.class, new TypeBindings());
        assertFalse(resolvedRecursiveType.equals(null));
        assertFalse(resolvedRecursiveType.equals(new Object()));
        assertFalse(resolvedRecursiveType.equals(other));
        assertTrue(resolvedRecursiveType.equals(resolvedRecursiveType));
    }

}