import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class ResolvedRecursiveTypeTest {
    
    @Test
    public void testSetReference() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType nestedType = new JavaType(String.class);
        
        type.setReference(nestedType);
        
        assertEquals(nestedType, type.getSelfReferencedType());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType nestedType1 = new JavaType(String.class);
        JavaType nestedType2 = new JavaType(Integer.class);
        
        type.setReference(nestedType1);
        type.setReference(nestedType2);
    }
    
    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType nestedType = new JavaType(String.class);
        
        type.setReference(nestedType);
        
        StringBuilder sb = new StringBuilder();
        assertEquals(nestedType.getGenericSignature(sb).toString(), type.getGenericSignature(sb).toString());
    }
    
    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType nestedType = new JavaType(String.class);
        
        type.setReference(nestedType);
        
        StringBuilder sb = new StringBuilder();
        assertEquals(nestedType.getErasedSignature(sb).toString(), type.getErasedSignature(sb).toString());
    }
    
    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        Object handler = new Object();
        
        JavaType newType = type.withTypeHandler(handler);
        
        assertEquals(type, newType);
    }
    
    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        Object handler = new Object();
        
        JavaType newType = type.withContentTypeHandler(handler);
        
        assertEquals(type, newType);
    }
    
    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        Object handler = new Object();
        
        JavaType newType = type.withValueHandler(handler);
        
        assertEquals(type, newType);
    }
    
    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        Object handler = new Object();
        
        JavaType newType = type.withContentValueHandler(handler);
        
        assertEquals(type, newType);
    }
    
    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        
        JavaType newType = type.withStaticTyping();
        
        assertEquals(type, newType);
    }
    
    @Test
    public void testNarrow() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        Class<?> subclass = String.class;
        
        JavaType newType = type._narrow(subclass);
        
        assertEquals(type, newType);
    }
    
    @Test
    public void testRefine() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        Class<?> rawType = String.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        
        JavaType newType = type.refine(rawType, bindings, superClass, superInterfaces);
        
        assertNull(newType);
    }
    
    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        
        assertFalse(type.isContainerType());
    }
    
    @Test
    public void testToString() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType nestedType = new JavaType(String.class);
        
        type.setReference(nestedType);
        
        assertNotNull(type.toString());
    }
    
    @Test
    public void testEquals() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(String.class, new TypeBindings());
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(String.class, new TypeBindings());
        ResolvedRecursiveType type3 = new ResolvedRecursiveType(Integer.class, new TypeBindings());
        
        JavaType nestedType = new JavaType(String.class);
        type1.setReference(nestedType);
        type2.setReference(nestedType);
        
        assertTrue(type1.equals(type2));
        assertFalse(type1.equals(type3));
    }
}