import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleTypeTest {

    private SimpleType simpleType;

    @Before
    public void setUp() {
        simpleType = new SimpleType(Object.class);
    }

    @Test
    public void testConstructor() {
        assertEquals(Object.class, simpleType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getInterfaces());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStatic());
    }

    @Test
    public void testCopyConstructor() {
        SimpleType copy = new SimpleType(simpleType);
        assertEquals(Object.class, copy.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), copy.getBindings());
        assertNull(copy.getSuperClass());
        assertNull(copy.getInterfaces());
        assertNull(copy.getValueHandler());
        assertNull(copy.getTypeHandler());
        assertFalse(copy.isStatic());
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType unsafe = SimpleType.constructUnsafe(Object.class);
        assertEquals(Object.class, unsafe.getRawClass());
        assertNull(unsafe.getBindings());
        assertNull(unsafe.getSuperClass());
        assertNull(unsafe.getInterfaces());
        assertNull(unsafe.getValueHandler());
        assertNull(unsafe.getTypeHandler());
        assertFalse(unsafe.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        SimpleType constructWithMap = SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        SimpleType constructWithCollection = SimpleType.construct(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        SimpleType constructWithArray = SimpleType.construct(Object[].class);
    }

    @Test
    public void testNarrow() {
        SimpleType narrowed = simpleType.narrow(String.class);
        assertEquals(String.class, narrowed.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), narrowed.getBindings());
        assertEquals(simpleType, narrowed.getSuperClass());
        assertNull(narrowed.getInterfaces());
        assertNull(narrowed.getValueHandler());
        assertNull(narrowed.getTypeHandler());
        assertFalse(narrowed.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowError() {
        SimpleType narrowed = simpleType.narrow(Collection.class);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testWithContentType() {
        simpleType.withContentType(Object.class);
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType withTypeHandler = simpleType.withTypeHandler(new TypeHandler());
        assertEquals(Object.class, withTypeHandler.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), withTypeHandler.getBindings());
        assertNull(withTypeHandler.getSuperClass());
        assertNull(withTypeHandler.getInterfaces());
        assertNull(withTypeHandler.getValueHandler());
        assertNotNull(withTypeHandler.getTypeHandler());
        assertFalse(withTypeHandler.isStatic());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testWithContentTypeHandler() {
        simpleType.withContentTypeHandler(new TypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        SimpleType withValueHandler = simpleType.withValueHandler(new ValueHandler());
        assertEquals(Object.class, withValueHandler.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), withValueHandler.getBindings());
        assertNull(withValueHandler.getSuperClass());
        assertNull(withValueHandler.getInterfaces());
        assertNotNull(withValueHandler.getValueHandler());
        assertNull(withValueHandler.getTypeHandler());
        assertFalse(withValueHandler.isStatic());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testWithContentValueHandler() {
        simpleType.withContentValueHandler(new ValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType withStaticTyping = simpleType.withStaticTyping();
        assertEquals(Object.class, withStaticTyping.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), withStaticTyping.getBindings());
        assertNull(withStaticTyping.getSuperClass());
        assertNull(withStaticTyping.getInterfaces());
        assertNull(withStaticTyping.getValueHandler());
        assertNull(withStaticTyping.getTypeHandler());
        assertTrue(withStaticTyping.isStatic());
    }

    @Test
    public void testRefine() {
        assertNull(simpleType.refine(Object.class, TypeBindings.emptyBindings(), null, null));
    }

    @Test
    public void testGetErasedSignature() {
        assertEquals("Ljava/lang/Object;", simpleType.getErasedSignature(new StringBuilder()).toString());
    }

    @Test
    public void testGetGenericSignature() {
        assertEquals("Ljava/lang/Object;", simpleType.getGenericSignature(new StringBuilder()).toString());
    }

    @Test
    public void testIsContainerType() {
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testToString() {
        assertEquals("[simple type, class java.lang.Object]", simpleType.toString());
    }

    @Test
    public void testEquals_sameObject() {
        assertTrue(simpleType.equals(simpleType));
    }

    @Test
    public void testEquals_differentObject() {
        SimpleType other = new SimpleType(String.class);
        assertFalse(simpleType.equals(other));
    }

    @Test
    public void testEquals_sameClassSameBindings() {
        SimpleType same = new SimpleType(Object.class);
        assertTrue(simpleType.equals(same));
    }

    @Test
    public void testEquals_sameClassDifferentBindings() {
        TypeBindings bindings = new TypeBindings();
        SimpleType differentBindings = new SimpleType(Object.class, bindings);
        assertFalse(simpleType.equals(differentBindings));
    }

    @Test
    public void testEquals_differentClass() {
        SimpleType differentClass = new SimpleType(String.class);
        assertFalse(simpleType.equals(differentClass));
    }

    private static class TypeHandler {}

    private static class ValueHandler {}

}