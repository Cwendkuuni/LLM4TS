import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        // Typical use case
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        JavaType ref = new JavaType();
        type.setReference(ref);
        assertEquals(ref, type.getSelfReferencedType());
        
        // Error scenario: Trying to re-set self reference
        try {
            type.setReference(new JavaType());
            fail("Expected IllegalStateException");
        } catch (IllegalStateException ex) {
            assertEquals("Trying to re-set self reference; old value = " + ref + ", new = " + new JavaType(), ex.getMessage());
        }
    }
   
    @Test
    public void testGetGenericSignature() {
        // Typical use case
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        JavaType ref = new JavaType();
        type.setReference(ref);
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        ref.getGenericSignature(expected);
        
        assertEquals(expected.toString(), type.getGenericSignature(sb).toString());
    }
    
    @Test
    public void testGetErasedSignature() {
        // Typical use case
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        JavaType ref = new JavaType();
        type.setReference(ref);
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        ref.getErasedSignature(expected);
        
        assertEquals(expected.toString(), type.getErasedSignature(sb).toString());
    }
    
    @Test
    public void testWithTypeHandler() {
        // Typical use case
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        JavaType ref = new JavaType();
        type.setReference(ref);
        Object handler = new Object();
        
        assertEquals(type, type.withTypeHandler(handler));
    }
    
    @Test
    public void testWithContentTypeHandler() {
        // Typical use case
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        JavaType ref = new JavaType();
        type.setReference(ref);
        Object handler = new Object();
        
        assertEquals(type, type.withContentTypeHandler(handler));
    }
    
    @Test
    public void testWithValueHandler() {
        // Typical use case
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        JavaType ref = new JavaType();
        type.setReference(ref);
        Object handler = new Object();
        
        assertEquals(type, type.withValueHandler(handler));
    }
    
    @Test
    public void testWithContentValueHandler() {
        // Typical use case
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        JavaType ref = new JavaType();
        type.setReference(ref);
        Object handler = new Object();
        
        assertEquals(type, type.withContentValueHandler(handler));
    }
    
    @Test
    public void testWithStaticTyping() {
        // Typical use case
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        JavaType ref = new JavaType();
        type.setReference(ref);
        
        assertEquals(type, type.withStaticTyping());
    }
    
    @Test
    public void test_refine() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        JavaType ref = new JavaType();
        type.setReference(ref);
        Class<?> rawType = String.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = new JavaType[0];
        
        assertNull(type.refine(rawType, bindings, superClass, superInterfaces));
    }
    
    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        assertFalse(type.isContainerType());
    }
    
    @Test
    public void testToString() {
        // Typical use case
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        JavaType ref = new JavaType();
        type.setReference(ref);
        String className = String.class.getName();
        
        assertEquals("[recursive type; " + className + "]", type.toString());
        
        // Edge case: unresolved reference
        ResolvedRecursiveType unresolved = new ResolvedRecursiveType(String.class, null);
        assertEquals("[recursive type; UNRESOLVED]", unresolved.toString());
    }
    
    @Test
    public void testEquals() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        JavaType ref1 = new JavaType();
        JavaType ref2 = new JavaType();
        type.setReference(ref1);
        
        // Edge case: object is null
        assertFalse(type.equals(null));
        
        // Edge case: object is of different class
        assertFalse(type.equals(new Object()));
        
        // Edge case: object is of same class but different reference
        assertFalse(type.equals(new ResolvedRecursiveType(String.class, null)));
        
        // Typical use case
        assertTrue(type.equals(type));
        
        // Typical use case: equal references
        assertTrue(type.equals(new ResolvedRecursiveType(String.class, null)));
        
        // Typical use case: unequal references
        assertFalse(type.equals(new ResolvedRecursiveType(String.class, null)));
    }
}