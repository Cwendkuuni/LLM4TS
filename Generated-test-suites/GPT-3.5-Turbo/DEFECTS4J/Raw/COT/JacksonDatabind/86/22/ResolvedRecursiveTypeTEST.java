
import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType();
        
        resolvedRecursiveType.setReference(ref);
        
        assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testSetReferenceMultipleTimes() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType ref1 = new JavaType();
        JavaType ref2 = new JavaType();
        
        resolvedRecursiveType.setReference(ref1);
        resolvedRecursiveType.setReference(ref2);
    }
    
    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        StringBuilder sb = new StringBuilder();
        JavaType ref = new JavaType();
        
        resolvedRecursiveType.setReference(ref);
        StringBuilder genericSignature = resolvedRecursiveType.getGenericSignature(sb);
        
        assertEquals(ref.getGenericSignature(sb), genericSignature);
    }
    
    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        StringBuilder sb = new StringBuilder();
        JavaType ref = new JavaType();
        
        resolvedRecursiveType.setReference(ref);
        StringBuilder erasedSignature = resolvedRecursiveType.getErasedSignature(sb);
        
        assertEquals(ref.getErasedSignature(sb), erasedSignature);
    }
    
    @Test
    public void testWithContentType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType contentType = new JavaType();
        
        JavaType result = resolvedRecursiveType.withContentType(contentType);
        
        assertSame(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Object h = new Object();
        
        JavaType result = resolvedRecursiveType.withTypeHandler(h);
        
        assertSame(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Object h = new Object();
        
        JavaType result = resolvedRecursiveType.withContentTypeHandler(h);
        
        assertSame(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Object h = new Object();
        
        JavaType result = resolvedRecursiveType.withValueHandler(h);
        
        assertSame(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Object h = new Object();
        
        JavaType result = resolvedRecursiveType.withContentValueHandler(h);
        
        assertSame(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        
        JavaType result = resolvedRecursiveType.withStaticTyping();
        
        assertSame(resolvedRecursiveType, result);
    }
    
    @Test
    public void testNarrow() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Class<?> subclass = new Class();
        
        JavaType result = resolvedRecursiveType._narrow(subclass);
        
        assertSame(resolvedRecursiveType, result);
    }
    
    @Test
    public void testRefine() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Class<?> rawType = new Class();
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = new JavaType[1];
        
        JavaType result = resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces);
        
        assertNull(result);
    }
    
    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        
        boolean isContainer = resolvedRecursiveType.isContainerType();
        
        assertFalse(isContainer);
    }
    
    @Test
    public void testToString() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType();
        
        resolvedRecursiveType.setReference(ref);
        String expected = "[recursive type; " + ref.getRawClass().getName() + "]";
        
        assertEquals(expected, resolvedRecursiveType.toString());
    }
    
    @Test
    public void testEqualsSameObject() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        
        assertTrue(resolvedRecursiveType.equals(resolvedRecursiveType));
    }
    
    @Test
    public void testEqualsNullObject() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        
        assertFalse(resolvedRecursiveType.equals(null));
    }
    
    @Test
    public void testEqualsDifferentClass() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Object obj = new Object();
        
        assertFalse(resolvedRecursiveType.equals(obj));
    }
    
    @Test
    public void testEqualsSameReference() {
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(null, null);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType();
        
        resolvedRecursiveType1.setReference(ref);
        resolvedRecursiveType2.setReference(ref);
        
        assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType2));
    }
    
    @Test
    public void testEqualsDifferentReference() {
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(null, null);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(null, null);
        JavaType ref1 = new JavaType();
        JavaType ref2 = new JavaType();
        
        resolvedRecursiveType1.setReference(ref1);
        resolvedRecursiveType2.setReference(ref2);
        
        assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType2));
    }
}
