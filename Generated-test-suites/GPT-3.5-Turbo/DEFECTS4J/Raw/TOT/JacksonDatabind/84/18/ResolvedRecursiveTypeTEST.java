import com.fasterxml.jackson.databind.JavaType;
import org.junit.Assert;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        JavaType ref = null;
        rrt.setReference(ref);
        Assert.assertEquals(ref, rrt.getSelfReferencedType());
        
        JavaType ref2 = new JavaTypeTestImpl(JavaTypeTestImpl.class, null);
        rrt.setReference(ref2);
        Assert.assertEquals(ref2, rrt.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReference_MultipleTimes() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        JavaType ref = new JavaTypeTestImpl(JavaTypeTestImpl.class, null);
        rrt.setReference(ref);
        
        JavaType ref2 = new JavaTypeTestImpl(JavaTypeTestImpl.class, null);
        rrt.setReference(ref2);
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        JavaType ref = new JavaTypeTestImpl(JavaTypeTestImpl.class, null);
        rrt.setReference(ref);
        
        StringBuilder sb = new StringBuilder();
        rrt.getGenericSignature(sb);
        
        Assert.assertEquals(ref.getGenericSignature(sb), sb.toString());
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        JavaType ref = new JavaTypeTestImpl(JavaTypeTestImpl.class, null);
        rrt.setReference(ref);
        
        StringBuilder sb = new StringBuilder();
        rrt.getErasedSignature(sb);
        
        Assert.assertEquals(ref.getErasedSignature(sb), sb.toString());
    }

    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        Object handler = new Object();
        JavaType result = rrt.withTypeHandler(handler);
        
        Assert.assertEquals(rrt, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        Object handler = new Object();
        JavaType result = rrt.withContentTypeHandler(handler);
        
        Assert.assertEquals(rrt, result);
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        Object handler = new Object();
        JavaType result = rrt.withValueHandler(handler);
        
        Assert.assertEquals(rrt, result);
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        Object handler = new Object();
        JavaType result = rrt.withContentValueHandler(handler);
        
        Assert.assertEquals(rrt, result);
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        JavaType result = rrt.withStaticTyping();
        
        Assert.assertEquals(rrt, result);
    }

    @Test
    public void testNarrow() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        Class<?> subclass = ResolvedRecursiveType.class;
        JavaType result = rrt._narrow(subclass);
        
        Assert.assertEquals(rrt, result);
    }

    @Test
    public void testRefine() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        Class<?> rawType = ResolvedRecursiveType.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType result = rrt.refine(rawType, bindings, superClass, superInterfaces);
        
        Assert.assertNull(result);
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        boolean result = rrt.isContainerType();
        
        Assert.assertFalse(result);
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        JavaType ref = new JavaTypeTestImpl(JavaTypeTestImpl.class, null);
        rrt.setReference(ref);
        
        Assert.assertEquals("[recursive type; " + ref.getRawClass().getName() + "]", rrt.toString());
    }

    @Test
    public void testEquals_SameObject() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        
        Assert.assertTrue(rrt.equals(rrt));
    }

    @Test
    public void testEquals_Null() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        
        Assert.assertFalse(rrt.equals(null));
    }

    @Test
    public void testEquals_DifferentClass() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        Object obj = new Object();
        
        Assert.assertFalse(rrt.equals(obj));
    }

    @Test
    public void testEquals_DifferentReferencedType() {
        ResolvedRecursiveType rrt1 = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        JavaType ref1 = new JavaTypeTestImpl(JavaTypeTestImpl.class, null);
        rrt1.setReference(ref1);
        
        ResolvedRecursiveType rrt2 = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        JavaType ref2 = new JavaTypeTestImpl2(JavaTypeTestImpl2.class, null);
        rrt2.setReference(ref2);
        
        Assert.assertFalse(rrt1.equals(rrt2));
    }

    @Test
    public void testEquals_SameReferencedType() {
        ResolvedRecursiveType rrt1 = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        JavaType ref1 = new JavaTypeTestImpl(JavaTypeTestImpl.class, null);
        rrt1.setReference(ref1);
        
        ResolvedRecursiveType rrt2 = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        JavaType ref2 = new JavaTypeTestImpl(JavaTypeTestImpl.class, null);
        rrt2.setReference(ref2);
        
        Assert.assertTrue(rrt1.equals(rrt2));
    }

    private static class JavaTypeTestImpl extends JavaType {
        public JavaTypeTestImpl(Class<?> cls, TypeBindings bindings) {
            super(cls, bindings);
        }
    }
    
    private static class JavaTypeTestImpl2 extends JavaType {
        public JavaTypeTestImpl2(Class<?> cls, TypeBindings bindings) {
            super(cls, bindings);
        }
    }
}