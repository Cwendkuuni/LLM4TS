// JUnit test cases for the ResolvedRecursiveType class

import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Object.class, null);
        JavaType reference = new JavaType(Object.class, null);
        
        resolvedType.setReference(reference);
        
        assertEquals(reference, resolvedType.getSelfReferencedType());
    }
   
    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Object.class, null);
        JavaType reference = new JavaType(Object.class, null);
        resolvedType.setReference(reference);
        
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = reference.getGenericSignature(sb);
        
        assertEquals(expected, resolvedType.getGenericSignature(sb));
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Object.class, null);
        JavaType reference = new JavaType(Object.class, null);
        resolvedType.setReference(reference);
        
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = reference.getErasedSignature(sb);
        
        assertEquals(expected, resolvedType.getErasedSignature(sb));
    }

    @Test
    public void testWithContentType() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Object.class, null);
        JavaType contentType = new JavaType(Object.class, null);
        
        assertEquals(resolvedType, resolvedType.withContentType(contentType));
    }
    
    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Object.class, null);
        
        Object handler = new Object();
        
        assertEquals(resolvedType, resolvedType.withTypeHandler(handler));
    }

    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Object.class, null);
        
        Object handler = new Object();
        
        assertEquals(resolvedType, resolvedType.withContentTypeHandler(handler));
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Object.class, null);
        
        Object handler = new Object();
        
        assertEquals(resolvedType, resolvedType.withValueHandler(handler));
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Object.class, null);
        
        Object handler = new Object();
        
        assertEquals(resolvedType, resolvedType.withContentValueHandler(handler));
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Object.class, null);
        
        assertEquals(resolvedType, resolvedType.withStaticTyping());
    }

    @Test
    public void testRefine() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Object.class, null);
        Class<?> rawType = Object.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;

        assertNull(resolvedType.refine(rawType, bindings, superClass, superInterfaces));
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Object.class, null);
        
        assertFalse(resolvedType.isContainerType());
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Object.class, null);
        
        assertEquals("[recursive type; UNRESOLVED]", resolvedType.toString());
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType resolvedType1 = new ResolvedRecursiveType(Object.class, null);
        JavaType reference = new JavaType(Object.class, null);
        resolvedType1.setReference(reference);

        ResolvedRecursiveType resolvedType2 = new ResolvedRecursiveType(Object.class, null);
        resolvedType2.setReference(reference);

        assertTrue(resolvedType1.equals(resolvedType2));
    }
}