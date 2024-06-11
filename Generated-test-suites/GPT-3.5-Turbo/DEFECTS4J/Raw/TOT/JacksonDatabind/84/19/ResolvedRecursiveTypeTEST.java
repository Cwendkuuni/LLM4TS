import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        // Typical use case
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(Class.class, new TypeBindings());
        JavaType ref = new JavaType();
        rrt.setReference(ref);
        assertEquals(ref, rrt.getSelfReferencedType());
        
        // Error scenario - Trying to re-set self reference
        try {
            rrt.setReference(ref);
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException ex) {
            assertEquals("Trying to re-set self reference; old value = " + ref + ", new = " + ref, ex.getMessage());
        }
    }
    
    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(Class.class, new TypeBindings());
        assertEquals(rrt.getGenericSignature(new StringBuilder()), rrt.getSelfReferencedType().getGenericSignature(new StringBuilder()));
    }
    
    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(Class.class, new TypeBindings());
        assertEquals(rrt.getErasedSignature(new StringBuilder()), rrt.getSelfReferencedType().getErasedSignature(new StringBuilder()));
    }
    
    @Test
    public void testWithContentType() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(Class.class, new TypeBindings());
        JavaType contentType = new JavaType();
        assertEquals(rrt.withContentType(contentType), rrt);
    }
    
    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(Class.class, new TypeBindings());
        Object handler = new Object();
        assertEquals(rrt.withTypeHandler(handler), rrt);
    }
    
    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(Class.class, new TypeBindings());
        Object handler = new Object();
        assertEquals(rrt.withContentTypeHandler(handler), rrt);
    }
    
    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(Class.class, new TypeBindings());
        Object handler = new Object();
        assertEquals(rrt.withValueHandler(handler), rrt);
    }
    
    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(Class.class, new TypeBindings());
        Object handler = new Object();
        assertEquals(rrt.withContentValueHandler(handler), rrt);
    }
    
    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(Class.class, new TypeBindings());
        assertEquals(rrt.withStaticTyping(), rrt);
    }
    
    @Test
    public void testNarrow() {
        // Typical use case
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(Class.class, new TypeBindings());
        // We are not actually narrowing the type here, just returning the same instance
        assertEquals(rrt._narrow(ClassSub.class), rrt);
    }
    
    @Test
    public void testRefine() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(Class.class, new TypeBindings());
        assertNull(rrt.refine(Class.class, new TypeBindings(), new JavaType(), new JavaType[0]));
    }
    
    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(Class.class, new TypeBindings());
        assertFalse(rrt.isContainerType());
    }
    
    @Test
    public void testToString() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(Class.class, new TypeBindings());
        StringBuilder sb = new StringBuilder(40).append("[recursive type; UNRESOLVED");
        assertEquals(sb.toString(), rrt.toString());
        
        rrt.setReference(new JavaType());
        sb = new StringBuilder(40).append("[recursive type; " + rrt.getSelfReferencedType().getRawClass().getName());
        assertEquals(sb.toString(), rrt.toString());
    }
    
    @Test
    public void testEquals() {
        ResolvedRecursiveType rrt1 = new ResolvedRecursiveType(Class.class, new TypeBindings());
        ResolvedRecursiveType rrt2 = new ResolvedRecursiveType(Class.class, new TypeBindings());
        
        assertFalse(rrt1.equals(null));
        assertFalse(rrt1.equals(new Object()));
        
        assertFalse(rrt1.equals(rrt2));
        
        rrt1.setReference(new JavaType());
        assertFalse(rrt1.equals(rrt2));
        
        rrt2.setReference(new JavaType());
        assertTrue(rrt1.equals(rrt2));
    }
    
    // Additional test cases can be added here for edge cases and other scenarios
    
}

class ClassSub extends Class {}