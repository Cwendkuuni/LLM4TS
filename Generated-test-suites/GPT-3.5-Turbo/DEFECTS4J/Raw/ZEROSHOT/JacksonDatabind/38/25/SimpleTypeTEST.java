import org.junit.*;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructor1() {
        SimpleType simpleType = new SimpleType(Integer.class);
        assertNotNull(simpleType);
        assertEquals(Integer.class, simpleType.getRawClass());
        assertFalse(simpleType.isContainerType());
        assertNull(simpleType.getContentType());
        assertNull(simpleType.getKeyType());
        assertNull(simpleType.getValueType());
        assertEquals("java.lang.Integer", simpleType.toString());
    }

    @Test
    public void testConstructor2() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        SimpleType simpleType = new SimpleType(Integer.class, bindings, superClass, superInterfaces);
        assertNotNull(simpleType);
        assertEquals(Integer.class, simpleType.getRawClass());
        assertFalse(simpleType.isContainerType());
        assertNull(simpleType.getContentType());
        assertNull(simpleType.getKeyType());
        assertNull(simpleType.getValueType());
        assertEquals("java.lang.Integer", simpleType.toString());
    }

    @Test
    public void testConstructor3() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        SimpleType simpleType = new SimpleType(Integer.class, bindings, superClass, superInterfaces, valueHandler, typeHandler, asStatic);
        assertNotNull(simpleType);
        assertEquals(Integer.class, simpleType.getRawClass());
        assertFalse(simpleType.isContainerType());
        assertNull(simpleType.getContentType());
        assertNull(simpleType.getKeyType());
        assertNull(simpleType.getValueType());
        assertEquals("java.lang.Integer", simpleType.toString());
    }

    @Test
    public void testConstructor4() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        int extraHash = 0;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        SimpleType simpleType = new SimpleType(Integer.class, bindings, superClass, superInterfaces, extraHash, valueHandler, typeHandler, asStatic);
        assertNotNull(simpleType);
        assertEquals(Integer.class, simpleType.getRawClass());
        assertFalse(simpleType.isContainerType());
        assertNull(simpleType.getContentType());
        assertNull(simpleType.getKeyType());
        assertNull(simpleType.getValueType());
        assertEquals("java.lang.Integer", simpleType.toString());
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(Integer.class);
        assertNotNull(simpleType);
        assertEquals(Integer.class, simpleType.getRawClass());
        assertFalse(simpleType.isContainerType());
        assertNull(simpleType.getContentType());
        assertNull(simpleType.getKeyType());
        assertNull(simpleType.getValueType());
        assertEquals("java.lang.Integer", simpleType.toString());
    }

    @Test
    public void testConstruct() {
        try {
            SimpleType.construct(Map.class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
        }
        try {
            SimpleType.construct(Collection.class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
        }
        try {
            SimpleType.construct(Integer[].class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
        }
        SimpleType simpleType = SimpleType.construct(Integer.class);
        assertNotNull(simpleType);
        assertEquals(Integer.class, simpleType.getRawClass());
        assertFalse(simpleType.isContainerType());
        assertNull(simpleType.getContentType());
        assertNull(simpleType.getKeyType());
        assertNull(simpleType.getValueType());
        assertEquals("java.lang.Integer", simpleType.toString());
    }

    @Test
    public void testNarrow() {
        SimpleType simpleType = new SimpleType(Integer.class);
        SimpleType narrowed = (SimpleType) simpleType._narrow(Double.class);
        assertNotSame(simpleType, narrowed);
        assertEquals(Double.class, narrowed.getRawClass());
        assertFalse(narrowed.isContainerType());
        assertNull(narrowed.getContentType());
        assertNull(narrowed.getKeyType());
        assertNull(narrowed.getValueType());
        assertEquals("java.lang.Double", narrowed.toString());
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(List.class);
        try {
            simpleType.withContentType(Integer.class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = new SimpleType(Integer.class);
        Object typeHandler = new Object();
        SimpleType result = simpleType.withTypeHandler(typeHandler);
        assertNotSame(simpleType, result);
        assertEquals(Integer.class, result.getRawClass());
        assertFalse(result.isContainerType());
        assertNull(result.getContentType());
        assertNull(result.getKeyType());
        assertNull(result.getValueType());
        assertEquals("java.lang.Integer", result.toString());
        assertSame(typeHandler, result.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = new SimpleType(List.class);
        try {
            simpleType.withContentTypeHandler(new Object());
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(Integer.class);
        SimpleType result = simpleType.withStaticTyping();
        assertNotSame(simpleType, result);
        assertEquals(Integer.class, result.getRawClass());
        assertFalse(result.isContainerType());
        assertNull(result.getContentType());
        assertNull(result.getKeyType());
        assertNull(result.getValueType());
        assertEquals("java.lang.Integer", result.toString());
        assertTrue(result.isStatic());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(Integer.class);
        assertNull(simpleType.refine(Integer.class, TypeBindings.emptyBindings(), null, null));
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        SimpleType simpleType = new SimpleType(Integer.class);
        StringBuilder result = simpleType.getErasedSignature(sb);
        assertNotNull(result);
        assertSame(sb, result);
        assertEquals("Ljava/lang/Integer;", result.toString());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        SimpleType simpleType = new SimpleType(Integer.class);
        StringBuilder result = simpleType.getGenericSignature(sb);
        assertNotNull(result);
        assertSame(sb, result);
        assertEquals("Ljava/lang/Integer;", result.toString());
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(Integer.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetContentType() {
        SimpleType simpleType = new SimpleType(Integer.class);
        assertNull(simpleType.getContentType());
    }

    @Test
    public void testGetKeyAndValueType() {
        SimpleType simpleType = new SimpleType(Integer.class);
        assertNull(simpleType.getKeyType());
        assertNull(simpleType.getValueType());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType = new SimpleType(Integer.class);
        assertFalse(simpleType.equals(null));
        assertFalse(simpleType.equals(new Object()));
        assertTrue(simpleType.equals(simpleType));
        assertTrue(simpleType.equals(new SimpleType(Integer.class)));
    }
}