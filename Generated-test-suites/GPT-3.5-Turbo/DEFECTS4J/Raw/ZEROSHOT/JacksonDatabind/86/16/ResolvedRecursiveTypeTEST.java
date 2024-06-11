import com.fasterxml.jackson.databind.JavaType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {
    
    private ResolvedRecursiveType resolvedRecursiveType;
    
    @Before
    public void setUp() {
        resolvedRecursiveType = new ResolvedRecursiveType(null, null);
    }
    
    @Test
    public void testSetReference() {
        JavaType ref = new JavaType();
        resolvedRecursiveType.setReference(ref);
        Assert.assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testSetReferenceWithExistingValue() {
        JavaType ref1 = new JavaType();
        JavaType ref2 = new JavaType();
        resolvedRecursiveType.setReference(ref1);
        resolvedRecursiveType.setReference(ref2);
    }
    
    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        JavaType ref = new JavaType();
        resolvedRecursiveType.setReference(ref);
        resolvedRecursiveType.getGenericSignature(sb);
        Assert.assertEquals(ref.getGenericSignature(sb).toString(), sb.toString());
    }
    
    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        JavaType ref = new JavaType();
        resolvedRecursiveType.setReference(ref);
        resolvedRecursiveType.getErasedSignature(sb);
        Assert.assertEquals(ref.getErasedSignature(sb).toString(), sb.toString());
    }
    
    @Test
    public void testWithTypeHandler() {
        Object handler = new Object();
        Assert.assertEquals(resolvedRecursiveType, resolvedRecursiveType.withTypeHandler(handler));
    }
    
    @Test
    public void testWithContentTypeHandler() {
        Object handler = new Object();
        Assert.assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentTypeHandler(handler));
    }
    
    @Test
    public void testWithValueHandler() {
        Object handler = new Object();
        Assert.assertEquals(resolvedRecursiveType, resolvedRecursiveType.withValueHandler(handler));
    }
    
    @Test
    public void testWithContentValueHandler() {
        Object handler = new Object();
        Assert.assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentValueHandler(handler));
    }
    
    @Test
    public void testWithStaticTyping() {
        Assert.assertEquals(resolvedRecursiveType, resolvedRecursiveType.withStaticTyping());
    }
    
    @Test
    public void testRefine() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        Assert.assertNull(resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces));
    }
    
    @Test
    public void testIsContainerType() {
        Assert.assertFalse(resolvedRecursiveType.isContainerType());
    }
    
    @Test
    public void testToStringUnresolved() {
        Assert.assertEquals("[recursive type; UNRESOLVED]", resolvedRecursiveType.toString());
    }
    
    @Test
    public void testToStringResolved() {
        JavaType ref = new JavaType();
        resolvedRecursiveType.setReference(ref);
        Assert.assertEquals("[recursive type; " + ref.getRawClass().getName() + "]", resolvedRecursiveType.toString());
    }
    
    @Test
    public void testEqualsSameObject() {
        Assert.assertTrue(resolvedRecursiveType.equals(resolvedRecursiveType));
    }
    
    @Test
    public void testEqualsNullObject() {
        Assert.assertFalse(resolvedRecursiveType.equals(null));
    }
    
    @Test
    public void testEqualsDifferentClass() {
        Assert.assertFalse(resolvedRecursiveType.equals(new Object()));
    }
    
    @Test
    public void testEqualsDifferentReferencedType() {
        ResolvedRecursiveType other = new ResolvedRecursiveType(null, null);
        Assert.assertFalse(resolvedRecursiveType.equals(other));
    }
    
    @Test
    public void testEqualsSameReferencedType() {
        JavaType ref = new JavaType();
        resolvedRecursiveType.setReference(ref);
        
        ResolvedRecursiveType other = new ResolvedRecursiveType(null, null);
        other.setReference(ref);
        
        Assert.assertTrue(resolvedRecursiveType.equals(other));
    }
}