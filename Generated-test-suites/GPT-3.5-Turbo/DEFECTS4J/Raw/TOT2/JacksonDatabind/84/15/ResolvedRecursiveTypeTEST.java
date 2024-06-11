import com.fasterxml.jackson.databind.JavaType;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    private ResolvedRecursiveType resolvedRecursiveType;

    @Before
    public void setUp() {
        resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        JavaType ref = new SimpleType(Object.class);
        resolvedRecursiveType.setReference(ref);
    }

    @Test
    public void testSetReference() {
        JavaType ref = new SimpleType(String.class);
        resolvedRecursiveType.setReference(ref);
        assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder result = resolvedRecursiveType.getGenericSignature(sb);
        assertTrue(result.toString().contains("java.lang.Object"));
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder result = resolvedRecursiveType.getErasedSignature(sb);
        assertTrue(result.toString().contains("java.lang.Object"));
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new SimpleType(String.class);
        JavaType result = resolvedRecursiveType.withContentType(contentType);
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withContentTypeHandler(handler);
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithValueHandler() {
        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withValueHandler(handler);
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentValueHandler() {
        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withContentValueHandler(handler);
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithStaticTyping() {
        JavaType result = resolvedRecursiveType.withStaticTyping();
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    @Deprecated
    public void testNarrow() {
        Class<?> subclass = String.class;
        JavaType result = resolvedRecursiveType._narrow(subclass);
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testRefine() {
        JavaType result = resolvedRecursiveType.refine(String.class, null, null, null);
        assertNull(result);
    }

    @Test
    public void testIsContainerType() {
        assertFalse(resolvedRecursiveType.isContainerType());
    }

    @Test
    public void testToString() {
        assertTrue(resolvedRecursiveType.toString().contains("java.lang.Object"));
        assertFalse(resolvedRecursiveType.toString().contains("UNRESOLVED"));
    }

    @Test
    public void testEquals() {
        assertTrue(resolvedRecursiveType.equals(resolvedRecursiveType));
        assertFalse(resolvedRecursiveType.equals(null));
    }
}