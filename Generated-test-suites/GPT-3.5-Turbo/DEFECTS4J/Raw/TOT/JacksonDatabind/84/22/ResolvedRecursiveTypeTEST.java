import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {
    
    @Test
    public void testSetReference_validInput() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Class.class, new TypeBindings());
        JavaType referencedType = new JavaType();
        resolvedRecursiveType.setReference(referencedType);
        
        assertEquals(referencedType, resolvedRecursiveType.getSelfReferencedType());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testSetReference_repeatedSet() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Class.class, new TypeBindings());
        JavaType referencedType = new JavaType();
        resolvedRecursiveType.setReference(referencedType);
        
        // Trying to set reference again should throw an exception
        resolvedRecursiveType.setReference(referencedType);
    }
    
    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Class.class, new TypeBindings());
        JavaType referencedType = new JavaType();
        resolvedRecursiveType.setReference(referencedType);
        
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = referencedType.getGenericSignature(sb);
        
        assertEquals(expected, resolvedRecursiveType.getGenericSignature(sb));
    }
    
    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Class.class, new TypeBindings());
        JavaType referencedType = new JavaType();
        resolvedRecursiveType.setReference(referencedType);
        
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = referencedType.getErasedSignature(sb);
        
        assertEquals(expected, resolvedRecursiveType.getErasedSignature(sb));
    }
    
    @Test
    public void testWithContentType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Class.class, new TypeBindings());
        JavaType contentType = new JavaType();
        
        JavaType result = resolvedRecursiveType.withContentType(contentType);
        
        assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Class.class, new TypeBindings());
        Object handler = new Object();
        
        JavaType result = resolvedRecursiveType.withTypeHandler(handler);
        
        assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Class.class, new TypeBindings());
        Object handler = new Object();
        
        JavaType result = resolvedRecursiveType.withContentTypeHandler(handler);
        
        assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Class.class, new TypeBindings());
        Object handler = new Object();
        
        JavaType result = resolvedRecursiveType.withValueHandler(handler);
        
        assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Class.class, new TypeBindings());
        Object handler = new Object();
        
        JavaType result = resolvedRecursiveType.withContentValueHandler(handler);
        
        assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Class.class, new TypeBindings());
        
        JavaType result = resolvedRecursiveType.withStaticTyping();
        
        assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testNarrow() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Class.class, new TypeBindings());
        Class<?> subclass = Class.class;
        
        JavaType result = resolvedRecursiveType._narrow(subclass);
        
        assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testRefine() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Class.class, new TypeBindings());
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = new JavaType[]{};
        
        JavaType result = resolvedRecursiveType.refine(Class.class, new TypeBindings(), superClass, superInterfaces);
        
        assertNull(result);
    }
    
    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Class.class, new TypeBindings());
        
        assertFalse(resolvedRecursiveType.isContainerType());
    }
    
    @Test
    public void testToString_unresolvedReference() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Class.class, new TypeBindings());
        
        assertEquals("[recursive type; UNRESOLVED]", resolvedRecursiveType.toString());
    }
    
    @Test
    public void testToString_resolvedReference() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Class.class, new TypeBindings());
        JavaType referencedType = new JavaType();
        resolvedRecursiveType.setReference(referencedType);
        
        assertEquals("[recursive type; " + referencedType.getRawClass().getName() + "]", resolvedRecursiveType.toString());
    }
    
    @Test
    public void testEquals_sameObject() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Class.class, new TypeBindings());
        
        assertTrue(resolvedRecursiveType.equals(resolvedRecursiveType));
    }
    
    @Test
    public void testEquals_differentClass() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Class.class, new TypeBindings());
        Object obj = new Object();
        
        assertFalse(resolvedRecursiveType.equals(obj));
    }
    
    @Test
    public void testEquals_differentReferencedType() {
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(Class.class, new TypeBindings());
        JavaType referencedType1 = new JavaType();
        resolvedRecursiveType1.setReference(referencedType1);
        
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(Class.class, new TypeBindings());
        JavaType referencedType2 = new JavaType();
        resolvedRecursiveType2.setReference(referencedType2);
        
        assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType2));
    }
    
    @Test
    public void testEquals_sameReferencedType() {
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(Class.class, new TypeBindings());
        JavaType referencedType = new JavaType();
        resolvedRecursiveType1.setReference(referencedType);
        
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(Class.class, new TypeBindings());
        resolvedRecursiveType2.setReference(referencedType);
        
        assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType2));
    }
}