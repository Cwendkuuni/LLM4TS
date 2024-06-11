import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructor1() {
        SimpleType st = new SimpleType(Object.class);
        assertNotNull(st);
        assertEquals(Object.class, st.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), st.getBindings());
        assertNull(st.getSuperClass());
        assertNull(st.getInterfaces());
        assertNull(st.getValueHandler());
        assertNull(st.getTypeHandler());
        assertFalse(st.isStatic());
    }

    @Test
    public void testConstructor2() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        SimpleType st = new SimpleType(Object.class, bindings, superClass, superInts);
        assertNotNull(st);
        assertEquals(Object.class, st.getRawClass());
        assertEquals(bindings, st.getBindings());
        assertEquals(superClass, st.getSuperClass());
        assertArrayEquals(superInts, st.getInterfaces());
        assertNull(st.getValueHandler());
        assertNull(st.getTypeHandler());
        assertFalse(st.isStatic());
    }

    @Test
    public void testConstructor3() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        SimpleType st = new SimpleType(Object.class, bindings, superClass, superInts, valueHandler, typeHandler, asStatic);
        assertNotNull(st);
        assertEquals(Object.class, st.getRawClass());
        assertEquals(bindings, st.getBindings());
        assertEquals(superClass, st.getSuperClass());
        assertArrayEquals(superInts, st.getInterfaces());
        assertEquals(valueHandler, st.getValueHandler());
        assertEquals(typeHandler, st.getTypeHandler());
        assertFalse(st.isStatic());
    }

    @Test
    public void testConstructor4() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        int extraHash = 0;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        SimpleType st = new SimpleType(Object.class, bindings, superClass, superInts, extraHash, valueHandler, typeHandler, asStatic);
        assertNotNull(st);
        assertEquals(Object.class, st.getRawClass());
        assertEquals(bindings, st.getBindings());
        assertEquals(superClass, st.getSuperClass());
        assertArrayEquals(superInts, st.getInterfaces());
        assertEquals(extraHash, st.getExtraHash());
        assertEquals(valueHandler, st.getValueHandler());
        assertEquals(typeHandler, st.getTypeHandler());
        assertFalse(st.isStatic());
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType st = SimpleType.constructUnsafe(Object.class);
        assertNotNull(st);
        assertEquals(Object.class, st.getRawClass());
        assertEquals(null, st.getBindings());
        assertEquals(null, st.getSuperClass());
        assertEquals(null, st.getInterfaces());
        assertEquals(null, st.getValueHandler());
        assertEquals(null, st.getTypeHandler());
        assertFalse(st.isStatic());
    }

    @Test
    public void testConstruct() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            SimpleType.construct(Map.class);
        });

        String expectedMessage = "Can not construct SimpleType for a Map (class: java.util.Map)";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void test_narrow() {
        SimpleType st = new SimpleType(Object.class).narrow(Object.class);
        assertNotNull(st);
        assertEquals(Object.class, st.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), st.getBindings());
        assertNull(st.getSuperClass());
        assertNull(st.getInterfaces());
        assertNull(st.getValueHandler());
        assertNull(st.getTypeHandler());
        assertFalse(st.isStatic());
    }

    @Test
    public void testWithContentType() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new SimpleType(Object.class).withContentType(new SimpleType(Object.class));
        });

        String expectedMessage = "Simple types have no content types; can not call withContentType()";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testWithSuperType() {
        SimpleType st = new SimpleType(Object.class).withSuperType(new SimpleType(Object.class));
        assertNotNull(st);
        assertEquals(Object.class, st.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), st.getBindings());
        assertNotNull(st.getSuperClass());
        assertNotNull(st.getInterfaces());
        assertNull(st.getValueHandler());
        assertNull(st.getTypeHandler());
        assertFalse(st.isStatic());
    }

    @Test
    public void testWithGenericSuperType() {
        SimpleType st = new SimpleType(Object.class).withGenericType(new SimpleType(Object.class));
        assertNotNull(st);
        assertEquals(Object.class, st.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), st.getBindings());
        assertNull(st.getSuperClass());
        assertNull(st.getInterfaces());
        assertNull(st.getValueHandler());
        assertNull(st.getTypeHandler());
        assertFalse(st.isStatic());
    }

    @Test
    public void testWithSuperInterface() {
        SimpleType st = new SimpleType(Object.class).withSuperInterface(new SimpleType(Object.class));
        assertNotNull(st);
        assertEquals(Object.class, st.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), st.getBindings());
        assertNull(st.getSuperClass());
        assertNotNull(st.getInterfaces());
        assertNull(st.getValueHandler());
        assertNull(st.getTypeHandler());
        assertFalse(st.isStatic());
    }

    @Test
    public void testWithGenericSuperInterface() {
        SimpleType st = new SimpleType(Object.class).withGenericSuperInterface(new SimpleType(Object.class));
        assertNotNull(st);
        assertEquals(Object.class, st.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), st.getBindings());
        assertNull(st.getSuperClass());
        assertNull(st.getInterfaces());
        assertNull(st.getValueHandler());
        assertNull(st.getTypeHandler());
        assertFalse(st.isStatic());
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType st = new SimpleType(Object.class).withStaticTyping();
        assertNotNull(st);
        assertEquals(Object.class, st.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), st.getBindings());
        assertNull(st.getSuperClass());
        assertNull(st.getInterfaces());
        assertNull(st.getValueHandler());
        assertNull(st.getTypeHandler());
        assertTrue(st.isStatic());
    }

    @Test
    public void testRefine() {
        SimpleType st = new SimpleType(Object.class).refine(Object.class, TypeBindings.emptyBindings(), null, null);
        assertNull(st);
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType st = new SimpleType(Object.class);
        String expected = "java.lang.Object";
        String actual = st.buildCanonicalName();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsContainerType() {
        SimpleType st = new SimpleType(Object.class);
        boolean actual = st.isContainerType();
        assertFalse(actual);
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType st = new SimpleType(Object.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder actual = st.getErasedSignature(sb);
        assertTrue(actual.toString().contains("Ljava/lang/Object"));
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType st = new SimpleType(Object.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder actual = st.getGenericSignature(sb);
        assertTrue(actual.toString().contains("Ljava/lang/Object"));
    }

    @Test
    public void testToString() {
        SimpleType st = new SimpleType(Object.class);
        String expected = "[simple type, class java.lang.Object]";
        String actual = st.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        SimpleType st1 = new SimpleType(Object.class);
        SimpleType st2 = new SimpleType(Object.class);
        assertTrue(st1.equals(st2));
    }

}