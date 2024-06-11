import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleTypeTest {
    
    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
        assertEquals(Object.class, simpleType.getRawClass());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructForMap() {
        SimpleType.construct(Map.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructForCollection() {
        SimpleType.construct(Collection.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructForArray() {
        SimpleType.construct(Object[].class);
    }
    
    @Test
    public void testConstructorWithClass() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertEquals(Object.class, simpleType.getRawClass());
        assertNull(simpleType.getContentType());
        assertFalse(simpleType.isContainerType());
    }
    
    @Test
    public void testConstructorWithClassAndBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        SimpleType simpleType = new SimpleType(Object.class, bindings, null, null);
        assertEquals(Object.class, simpleType.getRawClass());
        assertEquals(bindings, simpleType.getBindings());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertNull(simpleType.getContentType());
        assertFalse(simpleType.isContainerType());
    }
    
    @Test
    public void testConstructorWithTypeBase() {
        SimpleType base = new SimpleType(Object.class);
        SimpleType simpleType = new SimpleType(base);
        assertEquals(Object.class, simpleType.getRawClass());
        assertNull(simpleType.getContentType());
        assertFalse(simpleType.isContainerType());
    }
    
    @Test
    public void testConstructorWithAllParams() {
        SimpleType superClass = new SimpleType(Object.class);
        SimpleType[] superInterfaces = new SimpleType[0];
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        SimpleType simpleType = new SimpleType(Object.class, null, superClass, superInterfaces, valueHandler, typeHandler, false);
        assertEquals(Object.class, simpleType.getRawClass());
        assertNull(simpleType.getContentType());
        assertEquals(superClass, simpleType.getSuperClass());
        assertArrayEquals(superInterfaces, simpleType.getSuperInterfaces());
        assertEquals(valueHandler, simpleType.getValueHandler());
        assertEquals(typeHandler, simpleType.getTypeHandler());
        assertFalse(simpleType.isContainerType());
    }
    
    @Test
    public void testConstructorWithExtraHash() {
        SimpleType superClass = new SimpleType(Object.class);
        SimpleType[] superInterfaces = new SimpleType[0];
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        SimpleType simpleType = new SimpleType(Object.class, null, superClass, superInterfaces, 1, valueHandler, typeHandler, false);
        assertEquals(Object.class, simpleType.getRawClass());
        assertNull(simpleType.getContentType());
        assertEquals(superClass, simpleType.getSuperClass());
        assertArrayEquals(superInterfaces, simpleType.getSuperInterfaces());
        assertEquals(valueHandler, simpleType.getValueHandler());
        assertEquals(typeHandler, simpleType.getTypeHandler());
        assertFalse(simpleType.isContainerType());
        assertEquals(1, simpleType.getExtraHash());
    }
    
    @Test
    public void testConstructorsWithBindingAndExtraHash() {
        SimpleType superClass = new SimpleType(Object.class);
        SimpleType[] superInterfaces = new SimpleType[0];
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        TypeBindings bindings = TypeBindings.emptyBindings();
        SimpleType simpleType = new SimpleType(Object.class, bindings, superClass, superInterfaces, 1, valueHandler, typeHandler, false);
        assertEquals(Object.class, simpleType.getRawClass());
        assertEquals(bindings, simpleType.getBindings());
        assertEquals(superClass, simpleType.getSuperClass());
        assertArrayEquals(superInterfaces, simpleType.getSuperInterfaces());
        assertEquals(valueHandler, simpleType.getValueHandler());
        assertEquals(typeHandler, simpleType.getTypeHandler());
        assertFalse(simpleType.isContainerType());
        assertEquals(1, simpleType.getExtraHash());
    }
    
    @Test
    public void testNarrow() {
        SimpleType base = new SimpleType(Object.class);
        SimpleType subClass = new SimpleType(String.class);
        JavaType result = base.narrow(subClass.getRawClass());
        assertEquals(String.class, result.getRawClass());
    }
    
    @Test
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(Object.class);
        JavaType contentType = new SimpleType(String.class);
        SimpleType result = simpleType.withContentType(contentType);
        assertEquals(Object.class, result.getRawClass());
        assertNull(result.getContentType());
    }
    
    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = new SimpleType(Object.class);
        Object typeHandler = new Object();
        SimpleType result = simpleType.withTypeHandler(typeHandler);
        assertEquals(Object.class, result.getRawClass());
        assertEquals(typeHandler, result.getTypeHandler());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandler() {
        SimpleType simpleType = new SimpleType(Object.class);
        Object contentTypeHandler = new Object();
        simpleType.withContentTypeHandler(contentTypeHandler);
    }
    
    @Test
    public void testWithValueHandler() {
        SimpleType simpleType = new SimpleType(Object.class);
        Object valueHandler = new Object();
        SimpleType result = simpleType.withValueHandler(valueHandler);
        assertEquals(Object.class, result.getRawClass());
        assertEquals(valueHandler, result.getValueHandler());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandler() {
        SimpleType simpleType = new SimpleType(Object.class);
        Object contentValueHandler = new Object();
        simpleType.withContentValueHandler(contentValueHandler);
    }
    
    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(Object.class);
        SimpleType result = simpleType.withStaticTyping();
        assertEquals(Object.class, result.getRawClass());
        assertTrue(result.isStaticTyping());
    }
    
    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(Object.class);
        SimpleType superClass = new SimpleType(String.class);
        JavaType[] superInterfaces = new SimpleType[0];
        JavaType result = simpleType.refine(Object.class, TypeBindings.emptyBindings(), superClass, superInterfaces);
        assertNull(result);
    }
    
    @Test
    public void testBuildCanonicalName() {
        SimpleType simpleType = new SimpleType(Object.class);
        String canonicalName = "java.lang.Object";
        assertEquals(canonicalName, simpleType.buildCanonicalName());
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
        sb.append("Ljava/lang/Object;");
        StringBuilder result = simpleType.getErasedSignature(new StringBuilder());
        assertEquals(sb.toString(), result.toString());
    }
    
    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(Object.class);
        StringBuilder sb = new StringBuilder();
        sb.append("Ljava/lang/Object;;");
        StringBuilder result = simpleType.getGenericSignature(new StringBuilder());
        assertEquals(sb.toString(), result.toString());
    }
    
    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(Object.class);
        String expected = "[simple type, class java.lang.Object]";
        assertEquals(expected, simpleType.toString());
    }
    
    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(Object.class);
        SimpleType simpleType2 = new SimpleType(Object.class);
        assertTrue(simpleType1.equals(simpleType2));
    }
}