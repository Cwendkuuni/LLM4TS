import org.junit.*;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructor() {
        SimpleType simpleType = new SimpleType(String.class);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
        assertNotNull(simpleType.getBindings());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStatic());
    }

    @Test
    public void testCopyConstructor() {
        TypeBase base = new TypeBase(String.class);
        SimpleType simpleType = new SimpleType(base);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getBindings());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStatic());
    }

    @Test
    public void testConstructorWithTypeBindings() {
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new SimpleType(Object.class);
        JavaType[] superInterfaces = new JavaType[]{new SimpleType(Comparable.class)};
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        SimpleType simpleType = new SimpleType(String.class, bindings, superClass, superInterfaces, valueHandler, typeHandler, true);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(bindings, simpleType.getBindings());
        assertEquals(superClass, simpleType.getSuperClass());
        assertArrayEquals(superInterfaces, simpleType.getSuperInterfaces());
        assertEquals(valueHandler, simpleType.getValueHandler());
        assertEquals(typeHandler, simpleType.getTypeHandler());
        assertTrue(simpleType.isStatic());
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getBindings());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithTypeMap() {
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithTypeCollection() {
        SimpleType.construct(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithTypeArray() {
        SimpleType.construct(Object[].class);
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(String.class);
        try {
            simpleType.withContentType(new SimpleType(Integer.class));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }

    @Test
    public void testWithTypeHandler() {
        Object typeHandler = new Object();

        SimpleType simpleType = new SimpleType(String.class);
        SimpleType newSimpleType = simpleType.withTypeHandler(typeHandler);

        assertNotNull(newSimpleType);
        assertEquals(String.class, newSimpleType.getRawClass());
        assertEquals(simpleType.getBindings(), newSimpleType.getBindings());
        assertEquals(simpleType.getSuperClass(), newSimpleType.getSuperClass());
        assertArrayEquals(simpleType.getSuperInterfaces(), newSimpleType.getSuperInterfaces());
        assertEquals(simpleType.getValueHandler(), newSimpleType.getValueHandler());
        assertEquals(typeHandler, newSimpleType.getTypeHandler());
        assertEquals(simpleType.isStatic(), newSimpleType.isStatic());
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        try {
            simpleType.withContentTypeHandler(new Object());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithValueHandler() {
        Object valueHandler = new Object();

        SimpleType simpleType = new SimpleType(String.class);
        SimpleType newSimpleType = simpleType.withValueHandler(valueHandler);

        assertNotNull(newSimpleType);
        assertEquals(String.class, newSimpleType.getRawClass());
        assertEquals(simpleType.getBindings(), newSimpleType.getBindings());
        assertEquals(simpleType.getSuperClass(), newSimpleType.getSuperClass());
        assertArrayEquals(simpleType.getSuperInterfaces(), newSimpleType.getSuperInterfaces());
        assertEquals(valueHandler, newSimpleType.getValueHandler());
        assertEquals(simpleType.getTypeHandler(), newSimpleType.getTypeHandler());
        assertEquals(simpleType.isStatic(), newSimpleType.isStatic());
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        try {
            simpleType.withContentValueHandler(new Object());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType newSimpleType = simpleType.withStaticTyping();

        assertNotNull(newSimpleType);
        assertEquals(String.class, newSimpleType.getRawClass());
        assertEquals(simpleType.getBindings(), newSimpleType.getBindings());
        assertEquals(simpleType.getSuperClass(), newSimpleType.getSuperClass());
        assertArrayEquals(simpleType.getSuperInterfaces(), newSimpleType.getSuperInterfaces());
        assertEquals(simpleType.getValueHandler(), newSimpleType.getValueHandler());
        assertEquals(simpleType.getTypeHandler(), newSimpleType.getTypeHandler());
        assertTrue(newSimpleType.isStatic());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(String.class);
        JavaType refinedType = simpleType.refine(String.class, new TypeBindings(), null, null);
        assertNull(refinedType);
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(String.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(String.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = simpleType.getErasedSignature(sb);

        assertNotNull(result);
        assertEquals(sb, result);
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(String.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = simpleType.getGenericSignature(sb);

        assertNotNull(result);
        assertEquals(sb, result);
    }

    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(String.class);
        String result = simpleType.toString();

        assertNotNull(result);
        assertTrue(result.startsWith("[simple type, class"));
        assertTrue(result.endsWith("]"));
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(String.class);
        SimpleType simpleType3 = new SimpleType(Integer.class);

        assertTrue(simpleType1.equals(simpleType2));
        assertFalse(simpleType1.equals(simpleType3));
        assertFalse(simpleType1.equals(null));
        assertFalse(simpleType1.equals(new Object()));
    }
}