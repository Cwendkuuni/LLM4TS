import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;
import org.junit.Assert;
import org.junit.Test;
    
public class ResolvedRecursiveTypeTest {
    
    @Test
    public void testSetReference() {
        // Arrange
        JavaType ref = new JavaType();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Class.class, new TypeBindings());
        
        // Act
        resolvedRecursiveType.setReference(ref);
        
        // Assert
        Assert.assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
    }
    
    @Test
    public void testGetGenericSignature() {
        // Arrange
        StringBuilder sb = new StringBuilder();
        JavaType ref = new JavaType();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Class.class, new TypeBindings());
        resolvedRecursiveType.setReference(ref);
        
        // Act
        StringBuilder signature = resolvedRecursiveType.getGenericSignature(sb);
        
        // Assert
        Assert.assertEquals(ref.getGenericSignature(sb), signature);
    }
    
    @Test
    public void testGetErasedSignature() {
        // Arrange
        StringBuilder sb = new StringBuilder();
        JavaType ref = new JavaType();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Class.class, new TypeBindings());
        resolvedRecursiveType.setReference(ref);
        
        // Act
        StringBuilder signature = resolvedRecursiveType.getErasedSignature(sb);
        
        // Assert
        Assert.assertEquals(ref.getErasedSignature(sb), signature);
    }
    
    @Test
    public void testWithContentType() {
        // Arrange
        JavaType contentType = new JavaType();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Class.class, new TypeBindings());
        
        // Act
        JavaType result = resolvedRecursiveType.withContentType(contentType);
        
        // Assert
        Assert.assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithTypeHandler() {
        // Arrange
        Object h = new Object();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Class.class, new TypeBindings());
        
        // Act
        JavaType result = resolvedRecursiveType.withTypeHandler(h);
        
        // Assert
        Assert.assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithContentTypeHandler() {
        // Arrange
        Object h = new Object();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Class.class, new TypeBindings());
        
        // Act
        JavaType result = resolvedRecursiveType.withContentTypeHandler(h);
        
        // Assert
        Assert.assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithValueHandler() {
        // Arrange
        Object h = new Object();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Class.class, new TypeBindings());
        
        // Act
        JavaType result = resolvedRecursiveType.withValueHandler(h);
        
        // Assert
        Assert.assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithContentValueHandler() {
        // Arrange
        Object h = new Object();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Class.class, new TypeBindings());
        
        // Act
        JavaType result = resolvedRecursiveType.withContentValueHandler(h);
        
        // Assert
        Assert.assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testWithStaticTyping() {
        // Arrange
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Class.class, new TypeBindings());
        
        // Act
        JavaType result = resolvedRecursiveType.withStaticTyping();
        
        // Assert
        Assert.assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testNarrow() {
        // Arrange
        Class<?> subclass = Class.class;
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Class.class, new TypeBindings());
        
        // Act
        JavaType result = resolvedRecursiveType._narrow(subclass);
        
        // Assert
        Assert.assertEquals(resolvedRecursiveType, result);
    }
    
    @Test
    public void testRefine() {
        // Arrange
        Class<?> rawType = Class.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = {new JavaType()};
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Class.class, new TypeBindings());
        
        // Act
        JavaType result = resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces);
        
        // Assert
        Assert.assertEquals(null, result);
    }
    
    @Test
    public void testIsContainerType() {
        // Arrange
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Class.class, new TypeBindings());
        
        // Act
        boolean result = resolvedRecursiveType.isContainerType();
        
        // Assert
        Assert.assertEquals(false, result);
    }
    
    @Test
    public void testToString() {
        // Arrange
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Class.class, new TypeBindings());
        
        // Act
        String result = resolvedRecursiveType.toString();
        
        // Assert
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testEquals() {
        // Arrange
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(Class.class, new TypeBindings());
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(Class.class, new TypeBindings());
        
        // Act
        boolean result = resolvedRecursiveType1.equals(resolvedRecursiveType2);
        
        // Assert
        Assert.assertEquals(false, result);
    }
}