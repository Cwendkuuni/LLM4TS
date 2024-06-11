import static org.junit.Assert.*;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {
    
    @Test
    public void testSetReference() {
        JavaType ref = new JavaType();
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        
        rrt.setReference(ref);
        
        assertEquals(ref, rrt.getSelfReferencedType());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        JavaType ref1 = new JavaType();
        JavaType ref2 = new JavaType();
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        
        rrt.setReference(ref1);
        rrt.setReference(ref2);
    }
    
    @Test
    public void testGetGenericSignature() {
        JavaType ref = new JavaType();
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        rrt.setReference(ref);
        
        StringBuilder sb = new StringBuilder();
        StringBuilder result = rrt.getGenericSignature(sb);
        
        assertEquals(sb, result);
    }
    
    @Test
    public void testGetErasedSignature() {
        JavaType ref = new JavaType();
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        rrt.setReference(ref);
        
        StringBuilder sb = new StringBuilder();
        StringBuilder result = rrt.getErasedSignature(sb);
        
        assertEquals(sb, result);
    }
    
    @Test
    public void testWithContentType() {
        JavaType ref = new JavaType();
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        rrt.setReference(ref);
        
        JavaType contentType = new JavaType();
        JavaType result = rrt.withContentType(contentType);
        
        assertEquals(rrt, result);
    }
    
    @Test
    public void testWithTypeHandler() {
        JavaType ref = new JavaType();
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        rrt.setReference(ref);
        
        Object handler = new Object();
        JavaType result = rrt.withTypeHandler(handler);
        
        assertEquals(rrt, result);
    }
    
    @Test
    public void testWithContentTypeHandler() {
        JavaType ref = new JavaType();
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        rrt.setReference(ref);
        
        Object handler = new Object();
        JavaType result = rrt.withContentTypeHandler(handler);
        
        assertEquals(rrt, result);
    }
    
    @Test
    public void testWithValueHandler() {
        JavaType ref = new JavaType();
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        rrt.setReference(ref);
        
        Object handler = new Object();
        JavaType result = rrt.withValueHandler(handler);
        
        assertEquals(rrt, result);
    }
    
    @Test
    public void testWithContentValueHandler() {
        JavaType ref = new JavaType();
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        rrt.setReference(ref);
        
        Object handler = new Object();
        JavaType result = rrt.withContentValueHandler(handler);
        
        assertEquals(rrt, result);
    }
    
    @Test
    public void testWithStaticTyping() {
        JavaType ref = new JavaType();
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        rrt.setReference(ref);
        
        JavaType result = rrt.withStaticTyping();
        
        assertEquals(rrt, result);
    }
    
    @Test
    public void test_narrow() {
        JavaType ref = new JavaType();
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        rrt.setReference(ref);
        
        Class<?> subclass = ResolvedRecursiveType.class;
        JavaType result = rrt._narrow(subclass);
        
        assertEquals(rrt, result);
    }
    
    @Test
    public void testRefine() {
        JavaType ref = new JavaType();
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        rrt.setReference(ref);
        
        Class<?> rawType = Object.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = new JavaType[0];
        JavaType result = rrt.refine(rawType, bindings, superClass, superInterfaces);
        
        assertNull(result);
    }
    
    @Test
    public void testIsContainerType() {
        JavaType ref = new JavaType();
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        rrt.setReference(ref);
        
        assertFalse(rrt.isContainerType());
    }
    
    @Test
    public void testToString() {
        JavaType ref = new JavaType();
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        rrt.setReference(ref);
        
        assertTrue(rrt.toString().contains(ResolvedRecursiveType.class.getName()));
    }
    
    @Test
    public void testEquals() {
        JavaType ref = new JavaType();
        ResolvedRecursiveType rrt1 = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        rrt1.setReference(ref);
        
        ResolvedRecursiveType rrt2 = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        rrt2.setReference(ref);
        
        assertEquals(rrt1, rrt2);
        
        ResolvedRecursiveType rrt3 = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        rrt3.setReference(new JavaType());
        
        assertNotEquals(rrt1, rrt3);
        assertNotEquals(rrt1, new Object());
    }
}