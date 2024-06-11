import static org.junit.Assert.*;

import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class ResolvedRecursiveTypeTest {
    @Test
    public void testConstructor() {
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(Object.class, bindings);
        
        assertNotNull(recursiveType);
        assertEquals(Object.class, recursiveType.getRawClass());
        assertEquals(bindings, recursiveType.getTypeBindings());
        assertNull(recursiveType.getContentType());
        assertNull(recursiveType.getKeyType());
        assertNull(recursiveType.getValueHandler());
        assertNull(recursiveType.getContentValueHandler());
        assertNull(recursiveType.getTypeHandler());
        assertNull(recursiveType.getSuperClass());
        assertNull(recursiveType.getInterfaces());
        assertFalse(recursiveType.isContainerType());
    }
    
    @Test
    public void testSetReference() {
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(Object.class, bindings);
        
        JavaType ref = new JavaType(Object.class);
        recursiveType.setReference(ref);
        
        assertEquals(ref, recursiveType.getSelfReferencedType());
    }
    
    @Test
    public void testGetGenericSignature() {
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(Object.class, bindings);
        
        StringBuilder sb = new StringBuilder();
        assertEquals(recursiveType.getSelfReferencedType().getGenericSignature(sb), 
                     recursiveType.getGenericSignature(sb));
    }
    
    @Test
    public void testGetErasedSignature() {
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(Object.class, bindings);
        
        StringBuilder sb = new StringBuilder();
        assertEquals(recursiveType.getSelfReferencedType().getErasedSignature(sb), 
                     recursiveType.getErasedSignature(sb));
    }
    
    @Test
    public void testWithType() {
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(Object.class, bindings);
        
        Object handler = new Object();
        assertEquals(recursiveType, recursiveType.withTypeHandler(handler));
        assertEquals(recursiveType, recursiveType.withContentTypeHandler(handler));
        assertEquals(recursiveType, recursiveType.withValueHandler(handler));
        assertEquals(recursiveType, recursiveType.withContentValueHandler(handler));
        assertEquals(recursiveType, recursiveType.withStaticTyping());
    }
    
    @Test
    public void testNarrow() {
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(Object.class, bindings);
        
        assertEquals(recursiveType, recursiveType._narrow(Object.class));
    }
    
    @Test
    public void testRefine() {
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(Object.class, bindings);
        
        assertNull(recursiveType.refine(Object.class, null, null, null));
    }
    
    @Test
    public void testToString() {
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(Object.class, bindings);
        
        assertEquals("[recursive type; UNRESOLVED]", recursiveType.toString());
        
        JavaType ref = new JavaType(Object.class);
        recursiveType.setReference(ref);
        
        assertEquals("[recursive type; java.lang.Object]", recursiveType.toString());
    }
    
    @Test
    public void testEquals() {
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(Object.class, bindings);
        
        assertFalse(recursiveType.equals(null));
        assertFalse(recursiveType.equals(new Object()));
        assertFalse(recursiveType.equals(new ResolvedRecursiveType(Object.class, null)));
        
        JavaType ref = new JavaType(Object.class);
        recursiveType.setReference(ref);
        
        ResolvedRecursiveType other = new ResolvedRecursiveType(Object.class, bindings);
        other.setReference(ref);
        
        assertTrue(recursiveType.equals(other));
    }
}