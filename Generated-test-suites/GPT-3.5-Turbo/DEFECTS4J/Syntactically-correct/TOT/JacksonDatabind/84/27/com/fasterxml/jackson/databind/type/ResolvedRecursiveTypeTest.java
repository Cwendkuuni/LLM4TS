import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class ResolvedRecursiveTypeTest {

    // Test case for constructor
    @Test
    public void testConstructor() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        
        assertNotNull(resolvedRecursiveType);
        assertEquals(erasedType, resolvedRecursiveType._class);
        assertEquals(bindings, resolvedRecursiveType._bindings);
    }
    
    // Test case for setReference method when referencedType is null
    @Test
    public void testSetReferenceWithNullReferencedType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType ref = null;
        
        resolvedRecursiveType.setReference(ref);
        
        assertNull(resolvedRecursiveType.getSelfReferencedType());
    }
    
    // Test case for setReference method when referencedType is not null
    @Test
    public void testSetReferenceWithNonNullReferencedType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType ref = new JavaType();
        
        resolvedRecursiveType.setReference(ref);
        
        assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
    }
    
    // Test case for getGenericSignature method
    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        StringBuilder sb = new StringBuilder();
        
        StringBuilder result = resolvedRecursiveType.getGenericSignature(sb);
        
        assertEquals(sb, result);
    }
    
    // Test case for getErasedSignature method
    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        StringBuilder sb = new StringBuilder();
        
        StringBuilder result = resolvedRecursiveType.getErasedSignature(sb);
        
        assertEquals(sb, result);
    }
    
    // Test case for withContentType method
    @Test
    public void testWithContentType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType contentType = new JavaType();
        
        JavaType result = resolvedRecursiveType.withContentType(contentType);
        
        assertEquals(resolvedRecursiveType, result);
    }
    
    // Test case for withTypeHandler method
    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        Object h = new Object();
        
        JavaType result = resolvedRecursiveType.withTypeHandler(h);
        
        assertEquals(resolvedRecursiveType, result);
    }
    
    // Test case for withContentTypeHandler method
    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        Object h = new Object();
        
        JavaType result = resolvedRecursiveType.withContentTypeHandler(h);
        
        assertEquals(resolvedRecursiveType, result);
    }
    
    // Test case for withValueHandler method
    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        Object h = new Object();
        
        JavaType result = resolvedRecursiveType.withValueHandler(h);
        
        assertEquals(resolvedRecursiveType, result);
    }
    
    // Test case for withContentValueHandler method
    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        Object h = new Object();
        
        JavaType result = resolvedRecursiveType.withContentValueHandler(h);
        
        assertEquals(resolvedRecursiveType, result);
    }
    
    // Test case for withStaticTyping method
    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        
        JavaType result = resolvedRecursiveType.withStaticTyping();
        
        assertEquals(resolvedRecursiveType, result);
    }
    
    // Test case for _narrow method
    @Test
    public void testNarrow() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        Class<?> subclass = Object.class;
        
        JavaType result = resolvedRecursiveType._narrow(subclass);
        
        assertEquals(resolvedRecursiveType, result);
    }
    
    // Test case for refine method
    @Test
    public void testRefine() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        Class<?> rawType = Object.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = new JavaType[2];
        
        JavaType result = resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces);
        
        assertNull(result);
    }
    
    // Test case for isContainerType method
    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        
        boolean result = resolvedRecursiveType.isContainerType();
        
        assertFalse(result);
    }
    
    // Test case for toString method when referencedType is null
    @Test
    public void testToStringWithNullReferencedType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        resolvedRecursiveType._referencedType = null;
        
        String result = resolvedRecursiveType.toString();
        
        assertEquals("[recursive type; UNRESOLVED", result);
    }
    
    // Test case for toString method when referencedType is not null
    @Test
    public void testToStringWithNonNullReferencedType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        resolvedRecursiveType._referencedType = new JavaType();
        resolvedRecursiveType._referencedType.setRawClass(String.class);
        
        String result = resolvedRecursiveType.toString();
        
        assertEquals("[recursive type; java.lang.String", result);
    }
    
    // Test case for equals method when objects are equal
    @Test
    public void testEqualsWhenEqual() {
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(Object.class, new TypeBindings());
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(Object.class, new TypeBindings());
        resolvedRecursiveType1._referencedType = new JavaType();
        resolvedRecursiveType2._referencedType = new JavaType();
        resolvedRecursiveType1._referencedType.setRawClass(String.class);
        resolvedRecursiveType2._referencedType.setRawClass(String.class);
        
        boolean result = resolvedRecursiveType1.equals(resolvedRecursiveType2);
        
        assertTrue(result);
    }
    
    // Test case for equals method when objects are not equal
    @Test
    public void testEqualsWhenNotEqual() {
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(Object.class, new TypeBindings());
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(Object.class, new TypeBindings());
        resolvedRecursiveType1._referencedType = new JavaType();
        resolvedRecursiveType2._referencedType = new JavaType();
        resolvedRecursiveType1._referencedType.setRawClass(String.class);
        resolvedRecursiveType2._referencedType.setRawClass(Integer.class);
        
        boolean result = resolvedRecursiveType1.equals(resolvedRecursiveType2);
        
        assertFalse(result);
    }
}