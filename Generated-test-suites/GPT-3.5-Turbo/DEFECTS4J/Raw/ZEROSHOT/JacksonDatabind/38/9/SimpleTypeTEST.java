import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructor1() {
        SimpleType simpleType = new SimpleType(String.class);

        // Assert statements
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
    }
    
    @Test
    public void testConstructor2() {
        // Create TypeBindings
        TypeBindings bindings = new TypeBindings();

        SimpleType superClass = new SimpleType(Object.class);
        JavaType[] superInterfaces = new JavaType[]{};

        SimpleType simpleType = new SimpleType(String.class, bindings, superClass, superInterfaces);

        // Assert statements
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(bindings, simpleType.getBindings());
        assertEquals(superClass, simpleType.getSuperClass());
        assertArrayEquals(superInterfaces, simpleType.getInterfaces());
    }

    @Test
    public void testCopyConstructor() {
        SimpleType base = new SimpleType(String.class);
        SimpleType simpleType = new SimpleType(base);

        // Assert statements
        assertNotNull(simpleType);
        assertEquals(base.getRawClass(), simpleType.getRawClass());
    }
    
    @Test
    public void testConstructor3() {
        // Create TypeBindings
        TypeBindings bindings = new TypeBindings();

        SimpleType superClass = new SimpleType(Object.class);
        JavaType[] superInterfaces = new JavaType[]{};

        Object valueHandler = new Object();
        Object typeHandler = new Object();
        boolean asStatic = true;

        SimpleType simpleType = new SimpleType(String.class, bindings, superClass, superInterfaces, valueHandler, typeHandler, asStatic);

        // Assert statements
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(bindings, simpleType.getBindings());
        assertEquals(superClass, simpleType.getSuperClass());
        assertArrayEquals(superInterfaces, simpleType.getInterfaces());
        assertEquals(valueHandler, simpleType.getValueHandler());
        assertEquals(typeHandler, simpleType.getTypeHandler());
        assertEquals(asStatic, simpleType.isStatic());
    }
    
    @Test
    public void testConstructor4() {
        // Create TypeBindings
        TypeBindings bindings = new TypeBindings();
        SimpleType superClass = new SimpleType(Object.class);
        JavaType[] superInterfaces = new JavaType[]{};

        int extraHash = 0;
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        boolean asStatic = true;

        SimpleType simpleType = new SimpleType(String.class, bindings, superClass, superInterfaces, extraHash, valueHandler, typeHandler, asStatic);

        // Assert statements
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(bindings, simpleType.getBindings());
        assertEquals(superClass, simpleType.getSuperClass());
        assertArrayEquals(superInterfaces, simpleType.getInterfaces());
        assertEquals(extraHash, simpleType.getExtraHash());
        assertEquals(valueHandler, simpleType.getValueHandler());
        assertEquals(typeHandler, simpleType.getTypeHandler());
        assertEquals(asStatic, simpleType.isStatic());
    }
    
    /*
    public static SimpleType constructUnsafe(Class<?> raw) {
    }
    */
    
    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);

        // Assert statements
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
    }
    
    /*
    public static SimpleType construct(Class<?> cls) {
    }
    */

    @Deprecated
    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMapClass() {
        SimpleType.construct(Map.class);
    }

    @Deprecated
    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollectionClass() {
        SimpleType.construct(Collection.class);
    }
    
    @Deprecated
    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArrayClass() {
        SimpleType.construct(String[].class);
    }

    /*
    @Override
    @Deprecated
    protected JavaType _narrow(Class<?> subclass) {
    }
    */
    
    @Test
    public void testNarrow() {
        SimpleType simpleType = new SimpleType(String.class);
        JavaType javaType = simpleType._narrow(Object.class);

        // Assert statements
        assertNotNull(javaType);
        assertTrue(javaType instanceof SimpleType);
        assertEquals(Object.class, javaType.getRawClass());
    }
    
    /*
    @Override
    public JavaType withContentType(JavaType contentType) {
    }
    */
    
    @Test(expected = IllegalArgumentException.class)
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(String.class);
        simpleType.withContentType(String.class);
    }
    
    /*
    @Override
    public SimpleType withTypeHandler(Object h) {
    }
    */
    
    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType newType = simpleType.withTypeHandler(new Object());

        // Assert statements
        assertNotNull(newType);
        assertEquals(String.class, newType.getRawClass());
    }
    
    /*
    @Override
    public JavaType withContentTypeHandler(Object h) {
    }
    */
    
    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        simpleType.withContentTypeHandler(null);
    }

    /*
    @Override
    public SimpleType withValueHandler(Object h) {
    }
    */

    @Test
    public void testWithValueHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType newType = simpleType.withValueHandler(new Object());

        // Assert statements
        assertNotNull(newType);
        assertEquals(String.class, newType.getRawClass());
    }
    
    /*
    @Override
    public SimpleType withContentValueHandler(Object h) {
    }
    */
    
    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        simpleType.withContentValueHandler(null);
    }
    
    /*
    @Override
    public SimpleType withStaticTyping() {
    }
    */
    
    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType newType = simpleType.withStaticTyping();

        // Assert statements
        assertNotNull(newType);
        assertEquals(String.class, newType.getRawClass());
    }
    
    /*
    @Override
    public JavaType refine(Class<?> rawType, TypeBindings bindings,
            JavaType superClass, JavaType[] superInterfaces) {
    }
    */
    
    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(String.class);
        JavaType refinedType = simpleType.refine(String.class, null, null, null);

        // Assert statements
        assertNull(refinedType);
    }
    
    /*
    @Override
    protected String buildCanonicalName() {
    }
    */
    
    @Test
    public void testBuildCanonicalName() {
        SimpleType simpleType = new SimpleType(String.class);
        String canonicalName = simpleType.buildCanonicalName();

        // Assert statements
        assertNotNull(canonicalName);
        assertEquals(String.class.getName(), canonicalName);
    }
    
    /*
    @Override
    public boolean isContainerType() {
    }
    */
    
    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(String.class);
        boolean isContainerType = simpleType.isContainerType();

        // Assert statements
        assertFalse(isContainerType);
    }
    
    /*
    @Override
    public StringBuilder getErasedSignature(StringBuilder sb) {
    }
    */
    
    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(String.class);
        StringBuilder signature = new StringBuilder();
        StringBuilder result = simpleType.getErasedSignature(signature);

        // Assert statements
        assertNotNull(result);
        assertEquals(signature, result);
    }
    
    /*
    @Override
    public StringBuilder getGenericSignature(StringBuilder sb) {
    }
    */
    
    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(String.class);
        StringBuilder signature = new StringBuilder();
        StringBuilder result = simpleType.getGenericSignature(signature);

        // Assert statements
        assertNotNull(result);
        assertEquals(signature, result);
    }
    
    /*
    @Override
    public String toString() {
    }
    */
    
    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(String.class);
        String result = simpleType.toString();

        // Assert statements
        assertNotNull(result);
        assertEquals("[simple type, class " + String.class.getCanonicalName() + "]", result);
    }
    
    /*
    @Override
    public boolean equals(Object o) {
    }
    */
    
    @Test
    public void testEqualsWithEqualObjects() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(String.class);

        // Assert statements
        assertTrue(simpleType1.equals(simpleType2));
    }
    
    @Test
    public void testEqualsWithNotEqualObjects() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(Object.class);

        // Assert statements
        assertFalse(simpleType1.equals(simpleType2));
    }
}