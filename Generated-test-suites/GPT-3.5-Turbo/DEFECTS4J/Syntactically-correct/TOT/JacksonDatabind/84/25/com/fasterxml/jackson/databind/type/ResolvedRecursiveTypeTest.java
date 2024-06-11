import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    private ResolvedRecursiveType resolvedRecursiveType;

    @Before
    public void setUp() {
        resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveTypeTest.class, null);
    }

    @Test
    public void testSetReference() {
        JavaType javaType = new JavaType();
        resolvedRecursiveType.setReference(javaType);
        assertEquals(javaType, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceMultipleTimes() {
        JavaType javaType = new JavaType();
        resolvedRecursiveType.setReference(javaType);
        resolvedRecursiveType.setReference(javaType);
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        JavaType javaType = new JavaType();
        javaType.setGenericSignature("generic");
        resolvedRecursiveType.setReference(javaType);
        resolvedRecursiveType.getGenericSignature(sb);
        assertEquals("generic", sb.toString());
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        JavaType javaType = new JavaType();
        javaType.setErasedSignature("erased");
        resolvedRecursiveType.setReference(javaType);
        resolvedRecursiveType.getErasedSignature(sb);
        assertEquals("erased", sb.toString());
    }

    @Test
    public void testWithContentType() {
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
        Class<?> subclass = ResolvedRecursiveTypeTest.class;
        JavaType result = resolvedRecursiveType._narrow(subclass);
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testRefine() {
        Class<?> rawType = ResolvedRecursiveTypeTest.class;
        TypeBindings bindings = null;
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = null;
        JavaType result = resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces);
        assertNull(result);
    }

    @Test
    public void testIsContainerType() {
        assertFalse(resolvedRecursiveType.isContainerType());
    }

    @Test
    public void testToStringWithoutReference() {
        String expected = "[recursive type; UNRESOLVED]";
        String result = resolvedRecursiveType.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testToStringWithReference() {
        JavaType javaType = new JavaType();
        javaType.setRawClass(ResolvedRecursiveTypeTest.class);
        resolvedRecursiveType.setReference(javaType);

        String expected = "[recursive type; " + ResolvedRecursiveTypeTest.class.getName() + "]";
        String result = resolvedRecursiveType.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testEqualsWithSelf() {
        assertTrue(resolvedRecursiveType.equals(resolvedRecursiveType));
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
    public void testEqualsWithUnresolvedReference() {
        assertFalse(resolvedRecursiveType.equals(new ResolvedRecursiveType(ResolvedRecursiveTypeTest.class, null)));
    }

    @Test
    public void testEqualsWithDifferentReference() {
        JavaType javaType = new JavaType();
        resolvedRecursiveType.setReference(javaType);
        assertFalse(resolvedRecursiveType.equals(new ResolvedRecursiveType(ResolvedRecursiveTypeTest.class, null)));
    }

    @Test
    public void testEqualsWithSameReference() {
        JavaType javaType = new JavaType();
        resolvedRecursiveType.setReference(javaType);
        assertTrue(resolvedRecursiveType.equals(new ResolvedRecursiveType(ResolvedRecursiveTypeTest.class, null)));
    }
}