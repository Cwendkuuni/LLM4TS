import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    private ResolvedRecursiveType resolvedRecursiveType;

    @Before
    public void setUp() {
        resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
    }

    @After
    public void tearDown() {
        resolvedRecursiveType = null;
    }

    @Test
    public void testSetReference() {
        JavaType javaType = new JavaType(String.class);
        resolvedRecursiveType.setReference(javaType);
        assertEquals(javaType, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test
    public void testSetReference_multipleTimes() {
        JavaType javaType1 = new JavaType(String.class);
        JavaType javaType2 = new JavaType(Integer.class);
        resolvedRecursiveType.setReference(javaType1);
        try {
            resolvedRecursiveType.setReference(javaType2);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertEquals("Trying to re-set self reference; old value = "+javaType1+", new = "+javaType2, e.getMessage());
        }
    }

    @Test
    public void testGetGenericSignature() {
        JavaType javaType = new JavaType(String.class);
        resolvedRecursiveType.setReference(javaType);
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        javaType.getGenericSignature(expected);

        StringBuilder result = resolvedRecursiveType.getGenericSignature(sb);
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType javaType = new JavaType(String.class);
        resolvedRecursiveType.setReference(javaType);
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        javaType.getErasedSignature(expected);

        StringBuilder result = resolvedRecursiveType.getErasedSignature(sb);
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new JavaType(String.class);
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
    public void testRefine() {
        Class<?> rawType = String.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType(Object.class);
        JavaType[] superInterfaces = new JavaType[]{};

        JavaType result = resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces);
        assertNull(result);
    }

    @Test
    public void testIsContainerType() {
        boolean isContainer = resolvedRecursiveType.isContainerType();
        assertFalse(isContainer);
    }

    @Test
    public void testToString_unresolvedType() {
        String expected = "[recursive type; UNRESOLVED]";
        String result = resolvedRecursiveType.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testToString_resolvedType() {
        JavaType javaType = new JavaType(String.class);
        resolvedRecursiveType.setReference(javaType);
        String expected = "[recursive type; java.lang.String]";
        String result = resolvedRecursiveType.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testEquals_sameObject() {
        assertTrue(resolvedRecursiveType.equals(resolvedRecursiveType));
    }
    
    @Test
    public void testEquals_differentClass() {
        Object object = new Object();
        assertFalse(resolvedRecursiveType.equals(object));
    }
    
    @Test
    public void testEquals_differentRefType() {
        ResolvedRecursiveType differentRRType = new ResolvedRecursiveType(Integer.class, new TypeBindings());
        assertFalse(resolvedRecursiveType.equals(differentRRType));
    }
    
    @Test
    public void testEquals_sameRefType() {
        ResolvedRecursiveType sameRRType = new ResolvedRecursiveType(String.class, new TypeBindings());
        resolvedRecursiveType.setReference(new JavaType(String.class));
        sameRRType.setReference(new JavaType(String.class));
        assertTrue(resolvedRecursiveType.equals(sameRRType));
    }

}