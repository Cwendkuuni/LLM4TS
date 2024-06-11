import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleTypeTest {
    
    // Test constructor
    @Test
    public void testConstructor() {
        SimpleType simpleType = new SimpleType(String.class);
        assertEquals("java.lang.String", simpleType.getGenericSignature(new StringBuilder()).toString());
    }
    
    // Test constructor with bindings, superClass, superInterfaces, valueHandler, and typeHandler
    @Test
    public void testConstructorWithBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        Object valueHandler = null;
        Object typeHandler = null;
        
        SimpleType simpleType = new SimpleType(String.class, bindings, superClass, superInterfaces, valueHandler, typeHandler, false);
        
        assertEquals("java.lang.String", simpleType.getGenericSignature(new StringBuilder()).toString());
    }
    
    // Test copy constructor with base type
    @Test
    public void testCopyConstructor() {
        TypeBase baseType = new TypeBase(String.class);
        SimpleType simpleType = new SimpleType(baseType);
        
        assertEquals("java.lang.String", simpleType.getGenericSignature(new StringBuilder()).toString());
    }
    
    // Test constructor with extraHash
    @Test
    public void testConstructorWithExtraHash() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        Object valueHandler = null;
        Object typeHandler = null;
        
        SimpleType simpleType = new SimpleType(String.class, bindings, superClass, superInterfaces, 10, valueHandler, typeHandler, false);
        
        assertEquals("java.lang.String", simpleType.getGenericSignature(new StringBuilder()).toString());
    }
    
    // Test constructUnsafe method
    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        
        assertEquals("java.lang.String", simpleType.getGenericSignature(new StringBuilder()).toString());
    }
    
    // Test construct method
    @Test(expected = IllegalArgumentException.class)
    public void testConstructMap() {
        SimpleType.construct(Map.class);
    }
    
    // Test construct method
    @Test(expected = IllegalArgumentException.class)
    public void testConstructCollection() {
        SimpleType.construct(Collection.class);
    }
    
    // Test construct method
    @Test(expected = IllegalArgumentException.class)
    public void testConstructArray() {
        SimpleType.construct(String[].class);
    }
    
    // Test _narrow method with same subclass
    @Test
    public void test_narrowSameSubclass() {
        SimpleType simpleType = new SimpleType(String.class);
        JavaType narrowedType = simpleType._narrow(String.class);
        
        assertSame(simpleType, narrowedType);
    }
    
    // Test _narrow method with different subclass
    @Test
    public void test_narrowDifferentSubclass() {
        SimpleType simpleType = new SimpleType(Object.class);
        JavaType narrowedType = simpleType._narrow(String.class);
        
        assertEquals("java.lang.String", narrowedType.getGenericSignature(new StringBuilder()).toString());
    }
    
    // Test withContentType method (should throw exception)
    @Test(expected = IllegalArgumentException.class)
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(String.class);
        simpleType.withContentType(new SimpleType(Integer.class));
    }
    
    // Test withTypeHandler method
    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType newType = simpleType.withTypeHandler("handler");
        
        assertEquals("java.lang.String", newType.getGenericSignature(new StringBuilder()).toString());
    }
    
    // Test withContentTypeHandler method (should throw exception)
    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        simpleType.withContentTypeHandler("handler");
    }
    
    // Test withValueHandler method
    @Test
    public void testWithValueHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType newType = simpleType.withValueHandler("handler");
        
        assertEquals("java.lang.String", newType.getGenericSignature(new StringBuilder()).toString());
    }
    
    // Test withContentValueHandler method (should throw exception)
    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        simpleType.withContentValueHandler("handler");
    }
    
    // Test withStaticTyping method
    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType newType = simpleType.withStaticTyping();
        
        assertEquals("java.lang.String", newType.getGenericSignature(new StringBuilder()).toString());
    }
    
    // Test refine method
    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(String.class);
        JavaType refinedType = simpleType.refine(String.class, TypeBindings.emptyBindings(), null, null);
        
        assertNull(refinedType);
    }
    
    // Test isContainerType method
    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(String.class);
        assertFalse(simpleType.isContainerType());
    }
    
    // Test getErasedSignature method
    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(String.class);
        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);
        
        assertEquals("Ljava/lang/String;", sb.toString());
    }
    
    // Test getGenericSignature method
    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(String.class);
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        
        assertEquals("Ljava/lang/String;", sb.toString());
    }
    
    // Test toString method
    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(String.class);
        String str = simpleType.toString();
        
        assertEquals("[simple type, class java.lang.String]", str);
    }
    
    // Test equals method with same object
    @Test
    public void testEqualsSameObject() {
        SimpleType simpleType = new SimpleType(String.class);
        
        assertTrue(simpleType.equals(simpleType));
    }
    
    // Test equals method with null object
    @Test
    public void testEqualsNullObject() {
        SimpleType simpleType = new SimpleType(String.class);
        
        assertFalse(simpleType.equals(null));
    }
    
    // Test equals method with different class type
    @Test
    public void testEqualsDifferentClass() {
        SimpleType simpleType = new SimpleType(String.class);
        String str = "java.lang.String";
        
        assertFalse(simpleType.equals(str));
    }
    
    // Test equals method with different simple types with different classs
    @Test
    public void testEqualsDifferentClasss() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(Integer.class);
        
        assertFalse(simpleType1.equals(simpleType2));
    }
    
    // Test equals method with different simple types with same classs and different bindings
    @Test
    public void testEqualsDifferentBindings() {
        TypeBindings bindings1 = TypeBindings.emptyBindings();
        TypeBindings bindings2 = new TypeBindings();
        
        SimpleType simpleType1 = new SimpleType(String.class, bindings1, null, null, null, null, false);
        SimpleType simpleType2 = new SimpleType(String.class, bindings2, null, null, null, null, false);
        
        assertTrue(simpleType1.equals(simpleType2));
    }
    
}