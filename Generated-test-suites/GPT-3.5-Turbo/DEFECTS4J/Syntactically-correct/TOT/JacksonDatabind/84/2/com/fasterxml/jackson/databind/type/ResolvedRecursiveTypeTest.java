import com.fasterxml.jackson.databind.JavaType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResolvedRecursiveTypeTest {

    @Test
    void testSetReference() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        JavaType javaType = new JavaType();
        
        resolvedRecursiveType.setReference(javaType);
        
        assertEquals(javaType, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test
    void testGetGenericSignature() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        StringBuilder sb = new StringBuilder(50);
        JavaType javaType = new JavaType();
        
        resolvedRecursiveType.setReference(javaType);
        
        StringBuilder result = resolvedRecursiveType.getGenericSignature(sb);
        
        assertEquals(sb, result);
    }

    @Test
    void testGetErasedSignature() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        StringBuilder sb = new StringBuilder(50);
        JavaType javaType = new JavaType();
        
        resolvedRecursiveType.setReference(javaType);
        
        StringBuilder result = resolvedRecursiveType.getErasedSignature(sb);
        
        assertEquals(sb, result);
    }

    @Test
    void testWithContentType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        JavaType contentType = new JavaType();
        
        JavaType result = resolvedRecursiveType.withContentType(contentType);
        
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    void testWithTypeHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        Object handler = new Object();
        
        JavaType result = resolvedRecursiveType.withTypeHandler(handler);
        
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    void testWithContentTypeHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        Object handler = new Object();
        
        JavaType result = resolvedRecursiveType.withContentTypeHandler(handler);
        
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    void testWithValueHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        Object handler = new Object();
        
        JavaType result = resolvedRecursiveType.withValueHandler(handler);
        
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    void testWithContentValueHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        Object handler = new Object();
        
        JavaType result = resolvedRecursiveType.withContentValueHandler(handler);
        
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    void testWithStaticTyping() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        
        JavaType result = resolvedRecursiveType.withStaticTyping();
        
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    @Deprecated
    void test_narrow() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        Class<?> subclass = ResolvedRecursiveType.class;
        
        JavaType result = resolvedRecursiveType._narrow(subclass);
        
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    void testRefine() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        Class<?> rawType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        
        JavaType result = resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces);
        
        assertNull(result);
    }

    @Test
    void testIsContainerType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        
        assertFalse(resolvedRecursiveType.isContainerType());
    }

    @Test
    void testToString() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        
        String expected = "[recursive type; UNRESOLVED]";
        String result = resolvedRecursiveType.toString();
        
        assertEquals(expected, result);
    }

    @Test
    void testEquals() {
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        JavaType javaType = new JavaType();
        
        resolvedRecursiveType1.setReference(javaType);
        resolvedRecursiveType2.setReference(javaType);
        
        assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType2));
    }
}