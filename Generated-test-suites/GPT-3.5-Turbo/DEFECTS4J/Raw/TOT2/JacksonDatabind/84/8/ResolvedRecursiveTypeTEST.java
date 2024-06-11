import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

public class ResolvedRecursiveTypeTest {
    
    private ResolvedRecursiveType resolvedRecursiveType;
    private JavaType javaType;

    @Before
    public void setUp() {
        resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        javaType = new JavaType();
    }

    @Test
    public void testSetReference() {
        resolvedRecursiveType.setReference(javaType);
        assertEquals(javaType, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder result = resolvedRecursiveType.getGenericSignature(sb);
        assertEquals(javaType.getGenericSignature(sb), result);
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder result = resolvedRecursiveType.getErasedSignature(sb);
        assertEquals(javaType.getErasedSignature(sb), result);
    }

    @Test
    public void testWithTypeContentType() {
        JavaType contentType = new JavaType();
        JavaType result = resolvedRecursiveType.withContentType(contentType);
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithTypeHandler() {
        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withTypeHandler(handler);
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
        String expected = "[recursive type; UNRESOLVED";
        assertEquals(expected, resolvedRecursiveType.toString());
    }

    @Test
    public void testEquals() {
        assertFalse(resolvedRecursiveType.equals(null));
    }
}