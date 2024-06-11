package com.fasterxml.jackson.databind.type;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
public class SimpleTypeTest {
    
    @Test
    public void testConstructor() {
        // Test case to check the constructor
        SimpleType simpleType = new SimpleType(Object.class);
        assertEquals(simpleType.getClass(), SimpleType.class);
    }
    
    @Test
    public void testConstructorWithArgs() {
        // Test case to check the constructor with arguments
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = new JavaType[1];
        SimpleType simpleType = new SimpleType(Object.class, bindings, superClass, superInts);
        assertEquals(simpleType.getClass(), SimpleType.class);
        assertEquals(simpleType.getBindings(), bindings);
        assertEquals(simpleType.getSuperClass(), superClass);
        assertEquals(simpleType.getSuperInterfaces(), superInts);
    }
    
    @Test
    public void testCopyConstructor() {
        // Test case to check the copy constructor
        TypeBase base = new TypeBase();
        SimpleType simpleType = new SimpleType(base);
        assertEquals(simpleType.getClass(), SimpleType.class);
    }
    
    @Test
    public void testConstructorWithExtraHash() {
        // Test case to check the constructor with extraHash
        SimpleType simpleType = new SimpleType(Object.class, new TypeBindings(), new JavaType(), new JavaType[0], "valueHandler", "typeHandler", true);
        assertEquals(simpleType.getClass(), SimpleType.class);
    }
    
    @Test
    public void testConstructUnsafe() {
        // Test case to check the constructUnsafe method
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
        assertEquals(simpleType.getClass(), SimpleType.class);
    }
    
    @Test
    @Deprecated
    public void testConstruct() {
        // Test case to check the deprecated construct method
        SimpleType simpleType = SimpleType.construct(Object.class);
        assertEquals(simpleType.getClass(), SimpleType.class);
    }
    
    @Test
    public void testNarrow() {
        // Test case to check the _narrow method
        SimpleType simpleType = new SimpleType(Object.class);
        assertEquals(simpleType._narrow(Object.class), simpleType);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testWithContentType() {
        // Test case to check the withContentType method
        SimpleType simpleType = new SimpleType(Object.class);
        simpleType.withContentType(new JavaType());
    }
    
    @Test
    public void testWithTypeHandler() {
        // Test case to check the withTypeHandler method
        SimpleType simpleType = new SimpleType(Object.class);
        assertEquals(simpleType.withTypeHandler("typeHandler"), simpleType);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandler() {
        // Test case to check the withContentTypeHandler method
        SimpleType simpleType = new SimpleType(Object.class);
        simpleType.withContentTypeHandler("typeHandler");
    }
    
    @Test
    public void testWithValueHandler() {
        // Test case to check the withValueHandler method
        SimpleType simpleType = new SimpleType(Object.class);
        assertEquals(simpleType.withValueHandler("valueHandler"), simpleType);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandler() {
        // Test case to check the withContentValueHandler method
        SimpleType simpleType = new SimpleType(Object.class);
        simpleType.withContentValueHandler("valueHandler");
    }
    
    @Test
    public void testWithStaticTyping() {
        // Test case to check the withStaticTyping method
        SimpleType simpleType = new SimpleType(Object.class);
        assertEquals(simpleType.withStaticTyping(), simpleType);
    }
    
    @Test
    public void testRefine() {
        // Test case to check the refine method
        SimpleType simpleType = new SimpleType(Object.class);
        assertNull(simpleType.refine(Object.class, new TypeBindings(), new JavaType(), new JavaType[0]));
    }
    
    @Test
    public void testIsContainerType() {
        // Test case to check the isContainerType method
        SimpleType simpleType = new SimpleType(Object.class);
        assertFalse(simpleType.isContainerType());
    }
    
    @Test
    public void testGetErasedSignature() {
        // Test case to check the getErasedSignature method
        StringBuilder sb = new StringBuilder();
        SimpleType simpleType = new SimpleType(Object.class);
        assertEquals(simpleType.getErasedSignature(sb), sb);
    }
    
    @Test
    public void testGetGenericSignature() {
        // Test case to check the getGenericSignature method
        StringBuilder sb = new StringBuilder();
        SimpleType simpleType = new SimpleType(Object.class);
        assertEquals(simpleType.getGenericSignature(sb), sb);
    }
    
    @Test
    public void testToString() {
        // Test case to check the toString method
        SimpleType simpleType = new SimpleType(Object.class);
        assertEquals(simpleType.toString(), "[simple type, class java.lang.Object]");
    }
    
    @Test
    public void testEquals() {
        // Test case to check the equals method
        SimpleType simpleType1 = new SimpleType(Object.class);
        SimpleType simpleType2 = new SimpleType(Object.class);
        assertTrue(simpleType1.equals(simpleType2));
    }
}