import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import org.junit.Assert;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        JavaType referencedType = new JavaType();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        
        resolvedRecursiveType.setReference(referencedType);
        
        Assert.assertEquals(referencedType, resolvedRecursiveType.getSelfReferencedType());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testSetReferenceMultipleTimes() {
        JavaType referencedType = new JavaType();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        
        resolvedRecursiveType.setReference(referencedType);
        resolvedRecursiveType.setReference(referencedType);
    }
    
    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        JavaType referencedType = new JavaType();
        referencedType.setGenericSignature(sb);
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        resolvedRecursiveType.setReference(referencedType);
        
        StringBuilder result = resolvedRecursiveType.getGenericSignature(new StringBuilder());
        
        Assert.assertEquals(sb, result);
    }
    
    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        JavaType referencedType = new JavaType();
        referencedType.setErasedSignature(sb);
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        resolvedRecursiveType.setReference(referencedType);
        
        StringBuilder result = resolvedRecursiveType.getErasedSignature(new StringBuilder());
        
        Assert.assertEquals(sb, result);
    }
    
    @Test
    public void testWithContentType() {
        JavaType contentType = new JavaType();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        
        JavaType result = resolvedRecursiveType.withContentType(contentType);
        
        Assert.assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithTypeHandler() {
        Object handler = new Object();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        
        JavaType result = resolvedRecursiveType.withTypeHandler(handler);
        
        Assert.assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithContentTypeHandler() {
        Object handler = new Object();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        
        JavaType result = resolvedRecursiveType.withContentTypeHandler(handler);
        
        Assert.assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithValueHandler() {
        Object handler = new Object();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        
        JavaType result = resolvedRecursiveType.withValueHandler(handler);
        
        Assert.assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithContentValueHandler() {
        Object handler = new Object();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        
        JavaType result = resolvedRecursiveType.withContentValueHandler(handler);
        
        Assert.assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        
        JavaType result = resolvedRecursiveType.withStaticTyping();
        
        Assert.assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testNarrow() {
        Class<?> subclass = Object.class;
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        
        JavaType result = resolvedRecursiveType.narrow(subclass);
        
        Assert.assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testRefine() {
        Class<?> rawType = Object.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = new JavaType[0];
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        
        JavaType result = resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces);
        
        Assert.assertNull(result);
    }
    
    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        
        boolean result = resolvedRecursiveType.isContainerType();
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testToStringUnresolvedType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        
        String result = resolvedRecursiveType.toString();
        
        Assert.assertEquals("[recursive type; UNRESOLVED]", result);
    }
    
    @Test
    public void testToStringResolvedType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType referencedType = new JavaType();
        referencedType.setRawClass(String.class);
        resolvedRecursiveType.setReference(referencedType);
        
        String result = resolvedRecursiveType.toString();
        
        Assert.assertEquals("[recursive type; java.lang.String]", result);
    }
}