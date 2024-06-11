import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SimpleTypeTest {
    @Test
    public void testConstructor() {
        SimpleType simpleType = new SimpleType(String.class);
        assertEquals(String.class, simpleType.getRawClass());
    }
    
    @Test
    public void testCopyConstructor() {
        SimpleType baseType = new SimpleType(String.class);
        SimpleType simpleType = new SimpleType(baseType);
        assertEquals(baseType.getRawClass(), simpleType.getRawClass());
        assertEquals(baseType.getBindings(), simpleType.getBindings());
        assertEquals(baseType.getSuperClass(), simpleType.getSuperClass());
        assertEquals(baseType.getInterfaces(), simpleType.getInterfaces());
        assertEquals(baseType.getValueHandler(), simpleType.getValueHandler());
        assertEquals(baseType.getTypeHandler(), simpleType.getTypeHandler());
        assertEquals(baseType.isStatic(), simpleType.isStatic());
    }
    
    @Test
    public void testConstructorWithBindings() {
        TypeBindings bindings = new TypeBindings();
        SimpleType simpleType = new SimpleType(String.class, bindings, null, null);
        assertEquals(bindings, simpleType.getBindings());
    }
    
    @Test
    public void testConstructorWithSuperClassAndSuperInterfaces() {
        SimpleType superClass = new SimpleType(Object.class);
        SimpleType[] superInterfaces = { new SimpleType(List.class), new SimpleType(Set.class) };
        SimpleType simpleType = new SimpleType(String.class, null, superClass, superInterfaces);
        assertEquals(superClass, simpleType.getSuperClass());
        assertEquals(superInterfaces, simpleType.getInterfaces());
    }
    
    @Test
    public void testConstructorWithValueHandler() {
        Object valueHandler = new Object();
        SimpleType simpleType = new SimpleType(String.class, null, null, null, valueHandler, null, false);
        assertEquals(valueHandler, simpleType.getValueHandler());
    }
    
    @Test
    public void testConstructorWithTypeHandler() {
        Object typeHandler = new Object();
        SimpleType simpleType = new SimpleType(String.class, null, null, null, null, typeHandler, false);
        assertEquals(typeHandler, simpleType.getTypeHandler());
    }
    
    @Test
    public void testConstructorWithAsStatic() {
        SimpleType simpleType = new SimpleType(String.class, null, null, null, null, null, true);
        assertEquals(true, simpleType.isStatic());
    }
    
    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertEquals(String.class, simpleType.getRawClass());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        SimpleType.construct(Map.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        SimpleType.construct(Collection.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        SimpleType.construct(Array.class);
    }
    
    @Test
    public void testNarrow() {
        SimpleType baseType = new SimpleType(Object.class);
        SimpleType narrowedType = baseType._narrow(String.class);
        assertEquals(String.class, narrowedType.getRawClass());
        assertEquals(baseType.getBindings(), narrowedType.getBindings());
        assertEquals(baseType.getSuperClass(), narrowedType.getSuperClass());
        assertEquals(baseType.getInterfaces(), narrowedType.getInterfaces());
        assertEquals(baseType.getValueHandler(), narrowedType.getValueHandler());
        assertEquals(baseType.getTypeHandler(), narrowedType.getTypeHandler());
        assertEquals(baseType.isStatic(), narrowedType.isStatic());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithInvalidSubtype() {
        SimpleType baseType = new SimpleType(Object.class);
        baseType._narrow(Integer.class);
    }
    
    @Test
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType contentType = (SimpleType) simpleType.withContentType(new SimpleType(Object.class));
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(null, contentType);
    }
    
    @Test
    public void testWithContentValueHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType contentValueHandler = (SimpleType) simpleType.withContentValueHandler(new Object());
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(null, contentValueHandler);
    }
    
    @Test
    public void testWithGenericSignature() {
        SimpleType simpleType = new SimpleType(String.class);
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        assertEquals(sb.toString(), simpleType.toString());
    }
    
    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(String.class);
        assertEquals(false, simpleType.isContainerType());
    }
    
    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(String.class);
        assertEquals(true, simpleType1.equals(simpleType2));
    }
}