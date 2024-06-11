import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleTypeTest {
    
    @Test
    public void testConstructorWithClass() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertEquals(Object.class, simpleType.getRawClass());
    }
    
    @Test
    public void testConstructorWithClassAndBindings() {
        TypeBindings bindings = new TypeBindings();
        SimpleType simpleType = new SimpleType(Object.class, bindings, null, null);
        assertEquals(bindings, simpleType.getBindings());
    }
    
    @Test
    public void testConstructorWithBaseType() {
        TypeBase base = new TypeBase();
        SimpleType simpleType = new SimpleType(base);
        assertEquals(base, simpleType.getBaseType());
    }
    
    @Test
    public void testConstructorWithAllParameters() {
        Class<?> cls = Object.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = new JavaType[2];
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        boolean asStatic = true;
        
        SimpleType simpleType = new SimpleType(cls, bindings, superClass, superInts, valueHandler, typeHandler, asStatic);
        
        assertEquals(cls, simpleType.getRawClass());
        assertEquals(bindings, simpleType.getBindings());
        assertEquals(superClass, simpleType.getSuperClass());
        assertEquals(superInts, simpleType.getInterfaces());
        assertEquals(valueHandler, simpleType.getValueHandler());
        assertEquals(typeHandler, simpleType.getTypeHandler());
        assertEquals(asStatic, simpleType.isStatic());
    }
    
    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
        assertEquals(Object.class, simpleType.getRawClass());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMapClass() {
        SimpleType.construct(Map.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollectionClass() {
        SimpleType.construct(Collection.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArrayClass() {
        SimpleType.construct(Array.class);
    }
    
    @Test
    public void testNarrow() {
        SimpleType simpleType = new SimpleType(Object.class);
        SimpleType narrowedType = simpleType._narrow(String.class);
        assertEquals(String.class, narrowedType.getRawClass());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithInvalidSubclass() {
        SimpleType simpleType = new SimpleType(Object.class);
        simpleType._narrow(Collection.class);
    }
    
    @Test
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(Object.class);
        try {
            simpleType.withContentType(new JavaType());
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }
    
    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = new SimpleType(Object.class);
        Object handler = new Object();
        SimpleType updatedType = simpleType.withTypeHandler(handler);
        
        assertEquals(handler, updatedType.getTypeHandler());
    }
    
    @Test
    public void testWithTypeHandlerSameHandler() {
        SimpleType simpleType = new SimpleType(Object.class);
        Object handler = new Object();
        simpleType.withTypeHandler(handler);
        SimpleType updatedType = simpleType.withTypeHandler(handler);
        
        assertEquals(simpleType, updatedType);
    }
    
    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = new SimpleType(Object.class);
        try {
            simpleType.withContentTypeHandler(new Object());
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
        }
    }
    
    @Test
    public void testWithValueHandler() {
        SimpleType simpleType = new SimpleType(Object.class);
        Object handler = new Object();
        SimpleType updatedType = simpleType.withValueHandler(handler);
        
        assertEquals(handler, updatedType.getValueHandler());
    }
    
    @Test
    public void testWithValueHandlerSameHandler() {
        SimpleType simpleType = new SimpleType(Object.class);
        Object handler = new Object();
        simpleType.withValueHandler(handler);
        SimpleType updatedType = simpleType.withValueHandler(handler);
        
        assertEquals(simpleType, updatedType);
    }
    
    @Test
    public void testWithContentValueHandler() {
        SimpleType simpleType = new SimpleType(Object.class);
        try {
            simpleType.withContentValueHandler(new Object());
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
        }
    }
    
    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(Object.class);
        SimpleType updatedType = simpleType.withStaticTyping();
        
        assertTrue(updatedType.isStatic());
    }
    
    @Test
    public void testWithStaticTypingSameValue() {
        SimpleType simpleType = new SimpleType(Object.class);
        simpleType.withStaticTyping();
        SimpleType updatedType = simpleType.withStaticTyping();
        
        assertEquals(simpleType, updatedType);
    }
    
    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(Object.class);
        JavaType refinedType = simpleType.refine(Object.class, new TypeBindings(), null, new JavaType[2]);
        
        assertNull(refinedType);
    }
    
    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertFalse(simpleType.isContainerType());
    }
    
    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(Object.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = simpleType.getErasedSignature(sb);
        
        assertEquals(sb, result);
    }
    
    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(Object.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = simpleType.getGenericSignature(sb);
        
        assertEquals(sb, result);
    }
    
    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertEquals("[simple type, class java.lang.Object]", simpleType.toString());
    }
    
    @Test
    public void testEquals() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertTrue(simpleType.equals(simpleType));
    }
    
    @Test
    public void testEqualsWithNull() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertFalse(simpleType.equals(null));
    }
    
    @Test
    public void testEqualsWithDifferentClass() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertFalse(simpleType.equals(new SimpleType(String.class)));
    }
    
    @Test
    public void testEqualsWithDifferentTypeBindings() {
        SimpleType simpleType1 = new SimpleType(Object.class);
        SimpleType simpleType2 = new SimpleType(Object.class, new TypeBindings(), null, null);
        assertFalse(simpleType1.equals(simpleType2));
    }
    
    @Test
    public void testEqualsWithSameTypeBindings() {
        TypeBindings bindings = new TypeBindings();
        SimpleType simpleType1 = new SimpleType(Object.class, bindings, null, null);
        SimpleType simpleType2 = new SimpleType(Object.class, bindings, null, null);
        assertTrue(simpleType1.equals(simpleType2));
    }
}