
import org.junit.Assert;
import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType ref = new JavaType();
        
        type.setReference(ref);
        
        Assert.assertEquals(ref, type.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwiceThrowsException() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType ref1 = new JavaType();
        JavaType ref2 = new JavaType();
        
        type.setReference(ref1);
        type.setReference(ref2);
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType ref = new JavaType();
        
        type.setReference(ref);
        
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = ref.getGenericSignature(sb);
        
        Assert.assertEquals(expected.toString(), type.getGenericSignature(new StringBuilder()).toString());
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType ref = new JavaType();
        
        type.setReference(ref);
        
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = ref.getErasedSignature(sb);
        
        Assert.assertEquals(expected.toString(), type.getErasedSignature(new StringBuilder()).toString());
    }

    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
        
        JavaType result = type.withTypeHandler("handler");
        
        Assert.assertEquals(type, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
        
        JavaType result = type.withContentTypeHandler("handler");
        
        Assert.assertEquals(type, result);
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
        
        JavaType result = type.withValueHandler("handler");
        
        Assert.assertEquals(type, result);
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
        
        JavaType result = type.withContentValueHandler("handler");
        
        Assert.assertEquals(type, result);
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
        
        JavaType result = type.withStaticTyping();
        
        Assert.assertEquals(type, result);
    }

    @Test
    public void testNarrow() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
        
        JavaType result = type._narrow(Integer.class);
        
        Assert.assertEquals(type, result);
    }

    @Test
    public void testRefine() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
        
        JavaType result = type.refine(Object.class, new TypeBindings(), null, null);
        
        Assert.assertNull(result);
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
        
        boolean result = type.isContainerType();
        
        Assert.assertFalse(result);
    }

    @Test
    public void testToStringWithUnresolvedReference() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
        
        String expected = "[recursive type; UNRESOLVED]";
        String result = type.toString();
        
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testToStringWithResolvedReference() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType ref = new JavaType();
        type.setReference(ref);
        
        String expected = "[recursive type; " + ref.getRawClass().getName() + "]";
        String result = type.toString();
        
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testEqualsWithDifferentClasses() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(Object.class, new TypeBindings());
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(String.class, new TypeBindings());
        
        boolean result = type1.equals(type2);
        
        Assert.assertFalse(result);
    }

    @Test
    public void testEqualsWithNullReference() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(Object.class, new TypeBindings());
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(Object.class, new TypeBindings());
        
        boolean result = type1.equals(type2);
        
        Assert.assertFalse(result);
    }

    @Test
    public void testEqualsWhenEqual() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(Object.class, new TypeBindings());
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType ref1 = new JavaType();
        JavaType ref2 = new JavaType();
        type1.setReference(ref1);
        type2.setReference(ref2);
        
        boolean result = type1.equals(type2);
        
        Assert.assertTrue(result);
    }

    @Test
    public void testEqualsWhenNotEqual() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(Object.class, new TypeBindings());
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType ref1 = new JavaType();
        JavaType ref2 = new JavaType();
        type1.setReference(ref1);
        type2.setReference(ref2);
        
        boolean result = type1.equals(type2);
        
        Assert.assertFalse(result);
    }
}
