import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(ResolvedRecursiveTypeTest.class, TypeBindings.emptyBindings());
        JavaType ref = new JavaType(ResolvedRecursiveTypeTest.class);
        
        // Test setting reference to a non-null value
        rrt.setReference(ref);
        Assert.assertEquals(ref, rrt.getSelfReferencedType());
        
        // Test setting reference to a null value
        rrt.setReference(null);
        Assert.assertNull(rrt.getSelfReferencedType());
        
        // Test setting reference twice, should throw IllegalStateException
        try {
            rrt.setReference(ref);
            rrt.setReference(ref);
            Assert.fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            Assert.assertEquals("Trying to re-set self reference; old value = " + ref + ", new = " + ref, e.getMessage());
        }
    }
    
    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(ResolvedRecursiveTypeTest.class, TypeBindings.emptyBindings());
        JavaType ref = new JavaType(ResolvedRecursiveTypeTest.class);
        rrt.setReference(ref);
        
        // Test get generic signature with a non-null referenced type
        StringBuilder sb = new StringBuilder();
        rrt.getGenericSignature(sb);
        String expectedSignature = ref.getGenericSignature(new StringBuilder()).toString();
        Assert.assertEquals(expectedSignature, sb.toString());
        
        // Test get generic signature with a null referenced type
        rrt.setReference(null);
        sb = new StringBuilder();
        rrt.getGenericSignature(sb);
        Assert.assertEquals("", sb.toString());
    }
    
    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(ResolvedRecursiveTypeTest.class, TypeBindings.emptyBindings());
        JavaType ref = new JavaType(ResolvedRecursiveTypeTest.class);
        rrt.setReference(ref);
        
        // Test get erased signature with a non-null referenced type
        StringBuilder sb = new StringBuilder();
        rrt.getErasedSignature(sb);
        String expectedSignature = ref.getErasedSignature(new StringBuilder()).toString();
        Assert.assertEquals(expectedSignature, sb.toString());
        
        // Test get erased signature with a null referenced type
        rrt.setReference(null);
        sb = new StringBuilder();
        rrt.getErasedSignature(sb);
        Assert.assertEquals("", sb.toString());
    }
    
    @Test
    public void testWithContentType() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(ResolvedRecursiveTypeTest.class, TypeBindings.emptyBindings());
        JavaType ref = new JavaType(ResolvedRecursiveTypeTest.class);
        rrt.setReference(ref);
       
        // Test with content type, should return the same instance
        JavaType contentType = new JavaType(Object.class);
        Assert.assertEquals(rrt, rrt.withContentType(contentType));
    }
    
    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(ResolvedRecursiveTypeTest.class, TypeBindings.emptyBindings());
        JavaType ref = new JavaType(ResolvedRecursiveTypeTest.class);
        rrt.setReference(ref);
       
        // Test with type handler, should return the same instance
        Object handler = new Object();
        Assert.assertEquals(rrt, rrt.withTypeHandler(handler));
    }
    
    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(ResolvedRecursiveTypeTest.class, TypeBindings.emptyBindings());
        JavaType ref = new JavaType(ResolvedRecursiveTypeTest.class);
        rrt.setReference(ref);

        // Test with content type handler, should return the same instance
        Object handler = new Object();
        Assert.assertEquals(rrt, rrt.withContentTypeHandler(handler));
    }
    
    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(ResolvedRecursiveTypeTest.class, TypeBindings.emptyBindings());
        JavaType ref = new JavaType(ResolvedRecursiveTypeTest.class);
        rrt.setReference(ref);

        // Test with value handler, should return the same instance
        Object handler = new Object();
        Assert.assertEquals(rrt, rrt.withValueHandler(handler));
    }
    
    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(ResolvedRecursiveTypeTest.class, TypeBindings.emptyBindings());
        JavaType ref = new JavaType(ResolvedRecursiveTypeTest.class);
        rrt.setReference(ref);
        
        // Test with content value handler, should return the same instance
        Object handler = new Object();
        Assert.assertEquals(rrt, rrt.withContentValueHandler(handler));
    }
    
    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(ResolvedRecursiveTypeTest.class, TypeBindings.emptyBindings());
        JavaType ref = new JavaType(ResolvedRecursiveTypeTest.class);
        rrt.setReference(ref);
        
        // Test with static typing, should return the same instance
        Assert.assertEquals(rrt, rrt.withStaticTyping());
    }
    
    @Test
    public void test_narrow() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(ResolvedRecursiveTypeTest.class, TypeBindings.emptyBindings());
        JavaType ref = new JavaType(ResolvedRecursiveTypeTest.class);
        rrt.setReference(ref);
        
        // Test narrow, should return the same instance
        Class<?> subclass = ResolvedRecursiveTypeTest.class;
        Assert.assertEquals(rrt, rrt._narrow(subclass));
    }
    
    @Test
    public void testRefine() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(ResolvedRecursiveTypeTest.class, TypeBindings.emptyBindings());
        JavaType ref = new JavaType(ResolvedRecursiveTypeTest.class);
        rrt.setReference(ref);
        
        // Test refine, should return null
        Class<?> rawType = ResolvedRecursiveTypeTest.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = new JavaType(Object.class);
        JavaType[] superInterfaces = new JavaType[0];
        Assert.assertNull(rrt.refine(rawType, bindings, superClass, superInterfaces));
    }
    
    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(ResolvedRecursiveTypeTest.class, TypeBindings.emptyBindings());
        JavaType ref = new JavaType(ResolvedRecursiveTypeTest.class);
        rrt.setReference(ref);
        
        // Test is container type, should return false
        Assert.assertFalse(rrt.isContainerType());
    }
}