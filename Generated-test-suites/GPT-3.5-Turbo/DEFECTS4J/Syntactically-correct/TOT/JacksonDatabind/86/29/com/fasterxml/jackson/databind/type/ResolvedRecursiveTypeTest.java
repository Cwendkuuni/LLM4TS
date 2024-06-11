import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        JavaType javaType = new JavaTypeMock();
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(Class.class, null);
        
        recursiveType.setReference(javaType);
        
        Assert.assertEquals(javaType, recursiveType.getSelfReferencedType());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testSetReferenceMultipleTimes() {
        JavaType javaType = new JavaTypeMock();
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(Class.class, null);
        recursiveType.setReference(javaType);
        
        // Try to set reference again, it should throw IllegalStateException
        recursiveType.setReference(javaType);
    }
    
    @Test
    public void testGetGenericSignature() {
        JavaType javaTypeMock = new JavaTypeMock();
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(Class.class, null);
        recursiveType.setReference(javaTypeMock);
        
        StringBuilder sb = new StringBuilder();
        recursiveType.getGenericSignature(sb);
        
        Assert.assertEquals(javaTypeMock.getGenericSignature(sb).toString(), sb.toString());
    }
    
    @Test
    public void testGetErasedSignature() {
        JavaType javaTypeMock = new JavaTypeMock();
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(Class.class, null);
        recursiveType.setReference(javaTypeMock);
        
        StringBuilder sb = new StringBuilder();
        recursiveType.getErasedSignature(sb);
        
        Assert.assertEquals(javaTypeMock.getErasedSignature(sb).toString(), sb.toString());
    }
    
    @Test
    public void testWithContentType() {
        JavaType javaTypeMock = new JavaTypeMock();
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(Class.class, null);
        recursiveType.setReference(javaTypeMock);
        
        JavaType result = recursiveType.withContentType(javaTypeMock);
        
        Assert.assertEquals(recursiveType, result);
    }
    
    @Test
    public void testWithTypeHandler() {
        JavaType javaTypeMock = new JavaTypeMock();
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(Class.class, null);
        recursiveType.setReference(javaTypeMock);
        
        JavaType result = recursiveType.withTypeHandler(null);
        
        Assert.assertEquals(recursiveType, result);
    }
    
    @Test
    public void testWithContentTypeHandler() {
        JavaType javaTypeMock = new JavaTypeMock();
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(Class.class, null);
        recursiveType.setReference(javaTypeMock);
        
        JavaType result = recursiveType.withContentTypeHandler(null);
        
        Assert.assertEquals(recursiveType, result);
    }
    
    @Test
    public void testWithValueHandler() {
        JavaType javaTypeMock = new JavaTypeMock();
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(Class.class, null);
        recursiveType.setReference(javaTypeMock);
        
        JavaType result = recursiveType.withValueHandler(null);
        
        Assert.assertEquals(recursiveType, result);
    }
    
    @Test
    public void testWithContentValueHandler() {
        JavaType javaTypeMock = new JavaTypeMock();
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(Class.class, null);
        recursiveType.setReference(javaTypeMock);
        
        JavaType result = recursiveType.withContentValueHandler(null);
        
        Assert.assertEquals(recursiveType, result);
    }
    
    @Test
    public void testWithStaticTyping() {
        JavaType javaTypeMock = new JavaTypeMock();
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(Class.class, null);
        recursiveType.setReference(javaTypeMock);
        
        JavaType result = recursiveType.withStaticTyping();
        
        Assert.assertEquals(recursiveType, result);
    }
    
    
    @Test
    public void testNarrow() {
        JavaType javaTypeMock = new JavaTypeMock();
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(Class.class, null);
        recursiveType.setReference(javaTypeMock);
        
        JavaType result = recursiveType._narrow(Class.class);
        
        Assert.assertEquals(recursiveType, result);
    }
    
    @Test
    public void testRefine() {
        JavaType javaTypeMock = new JavaTypeMock();
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(Class.class, null);
        recursiveType.setReference(javaTypeMock);
        
        JavaType result = recursiveType.refine(Class.class, null, null, null);
        
        Assert.assertNull(result);
    }
    
    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(Class.class, null);
        
        boolean isContainer = recursiveType.isContainerType();
        
        Assert.assertFalse(isContainer);
    }
    
    @Test
    public void testToString() {
        JavaType javaTypeMock = new JavaTypeMock();
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(Class.class, null);
        recursiveType.setReference(javaTypeMock);
        
        String toString = recursiveType.toString();
        
        Assert.assertTrue(toString.contains(javaTypeMock.getRawClass().getName()));
    }
    
    @Test
    public void testEqualsSame() {
        JavaType javaTypeMock = new JavaTypeMock();
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(Class.class, null);
        recursiveType.setReference(javaTypeMock);
        
        boolean equals = recursiveType.equals(recursiveType);
        
        Assert.assertTrue(equals);
    }
    
    @Test
    public void testEqualsNull() {
        JavaType javaTypeMock = new JavaTypeMock();
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(Class.class, null);
        recursiveType.setReference(javaTypeMock);
        
        boolean equals = recursiveType.equals(null);
        
        Assert.assertFalse(equals);
    }
    
    @Test
    public void testEqualsDifferentClass() {
        JavaType javaTypeMock = new JavaTypeMock();
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(Class.class, null);
        recursiveType.setReference(javaTypeMock);
        
        boolean equals = recursiveType.equals(new Object());
        
        Assert.assertFalse(equals);
    }
    
    @Test
    public void testEqualsSameReferenceType() {
        JavaType javaTypeMock = new JavaTypeMock();
        ResolvedRecursiveType recursiveType1 = new ResolvedRecursiveType(Class.class, null);
        recursiveType1.setReference(javaTypeMock);
        
        ResolvedRecursiveType recursiveType2 = new ResolvedRecursiveType(Class.class, null);
        recursiveType2.setReference(javaTypeMock);
        
        boolean equals = recursiveType1.equals(recursiveType2);
        
        Assert.assertTrue(equals);
    }
    
    @Test
    public void testEqualsDifferentReferenceType() {
        JavaType javaTypeMock1 = new JavaTypeMock();
        JavaType javaTypeMock2 = new JavaTypeMock();
        ResolvedRecursiveType recursiveType1 = new ResolvedRecursiveType(Class.class, null);
        recursiveType1.setReference(javaTypeMock1);
        
        ResolvedRecursiveType recursiveType2 = new ResolvedRecursiveType(Class.class, null);
        recursiveType2.setReference(javaTypeMock2);
        
        boolean equals = recursiveType1.equals(recursiveType2);
        
        Assert.assertFalse(equals);
    }

    private static class JavaTypeMock extends JavaType {
        // Mocking JavaType for testing purposes
    }
}