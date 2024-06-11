import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructor() {
        SimpleType simpleType = new SimpleType(String.class);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getInterfaces());
        assertEquals(0, simpleType.getExtraHash());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStatic());
    }

    @Test
    public void testCopyConstructor() {
        SimpleType baseType = new SimpleType(String.class);
        SimpleType simpleType = new SimpleType(baseType);
        assertNotNull(simpleType);
        assertEquals(baseType.getRawClass(), simpleType.getRawClass());
        assertEquals(baseType.getBindings(), simpleType.getBindings());
        assertEquals(baseType.getSuperClass(), simpleType.getSuperClass());
        assertEquals(baseType.getInterfaces(), simpleType.getInterfaces());
        assertEquals(baseType.getExtraHash(), simpleType.getExtraHash());
        assertEquals(baseType.getValueHandler(), simpleType.getValueHandler());
        assertEquals(baseType.getTypeHandler(), simpleType.getTypeHandler());
        assertEquals(baseType.isStatic(), simpleType.isStatic());
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getBindings());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getInterfaces());
        assertEquals(0, simpleType.getExtraHash());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStatic());
    }

    @Test
    public void testConstruct() {
        try {
            SimpleType.construct(Map.class);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Map (class: java.util.Map)", e.getMessage());
        }

        try {
            SimpleType.construct(Collection.class);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Collection (class: java.util.Collection)", e.getMessage());
        }

        try {
            SimpleType.construct(String[].class);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for an array (class: [Ljava.lang.String;)", e.getMessage());
        }

        SimpleType simpleType = SimpleType.construct(String.class);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
        assertNotNull(simpleType.getSuperClass());
        assertNull(simpleType.getInterfaces());
        assertEquals(0, simpleType.getExtraHash());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStatic());
    }

    @Test
    public void testNarrow() {
        SimpleType baseType = new SimpleType(String.class);
        SimpleType simpleType = (SimpleType) baseType._narrow(Object.class);
        assertNotNull(simpleType);
        assertEquals(Object.class, simpleType.getRawClass());
        assertEquals(baseType.getBindings(), simpleType.getBindings());
        assertEquals(baseType.getSuperClass(), simpleType.getSuperClass());
        assertEquals(baseType.getInterfaces(), simpleType.getInterfaces());
        assertEquals(baseType.getExtraHash(), simpleType.getExtraHash());
        assertEquals(baseType.getValueHandler(), simpleType.getValueHandler());
        assertEquals(baseType.getTypeHandler(), simpleType.getTypeHandler());
        assertEquals(baseType.isStatic(), simpleType.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(String.class);
        simpleType.withContentType(null);
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType baseType = new SimpleType(String.class);
        Object handler = new Object();
        SimpleType simpleType = baseType.withTypeHandler(handler);
        assertNotNull(simpleType);
        assertEquals(baseType.getRawClass(), simpleType.getRawClass());
        assertEquals(baseType.getBindings(), simpleType.getBindings());
        assertEquals(baseType.getSuperClass(), simpleType.getSuperClass());
        assertEquals(baseType.getInterfaces(), simpleType.getInterfaces());
        assertEquals(baseType.getExtraHash(), simpleType.getExtraHash());
        assertEquals(baseType.getValueHandler(), simpleType.getValueHandler());
        assertEquals(handler, simpleType.getTypeHandler());
        assertEquals(baseType.isStatic(), simpleType.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        simpleType.withContentTypeHandler(null);
    }

    @Test
    public void testWithValueHandler() {
        SimpleType baseType = new SimpleType(String.class);
        Object handler = new Object();
        SimpleType simpleType = baseType.withValueHandler(handler);
        assertNotNull(simpleType);
        assertEquals(baseType.getRawClass(), simpleType.getRawClass());
        assertEquals(baseType.getBindings(), simpleType.getBindings());
        assertEquals(baseType.getSuperClass(), simpleType.getSuperClass());
        assertEquals(baseType.getInterfaces(), simpleType.getInterfaces());
        assertEquals(baseType.getExtraHash(), simpleType.getExtraHash());
        assertEquals(handler, simpleType.getValueHandler());
        assertEquals(baseType.getTypeHandler(), simpleType.getTypeHandler());
        assertEquals(baseType.isStatic(), simpleType.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        simpleType.withContentValueHandler(null);
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType baseType = new SimpleType(String.class);
        SimpleType simpleType = baseType.withStaticTyping();
        assertNotNull(simpleType);
        assertEquals(baseType.getRawClass(), simpleType.getRawClass());
        assertEquals(baseType.getBindings(), simpleType.getBindings());
        assertEquals(baseType.getSuperClass(), simpleType.getSuperClass());
        assertEquals(baseType.getInterfaces(), simpleType.getInterfaces());
        assertEquals(baseType.getExtraHash(), simpleType.getExtraHash());
        assertEquals(baseType.getValueHandler(), simpleType.getValueHandler());
        assertEquals(baseType.getTypeHandler(), simpleType.getTypeHandler());
        assertTrue(simpleType.isStatic());
    }

    @Test
    public void testRefine() {
        SimpleType baseType = new SimpleType(String.class);
        SimpleType refinedType = (SimpleType) baseType.refine(String.class, TypeBindings.emptyBindings(),
                null, null);
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
        assertEquals("Ljava/lang/String;", simpleType.getErasedSignature(new StringBuilder()).toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(String.class);
        assertEquals("Ljava/lang/String;", simpleType.getGenericSignature(new StringBuilder()).toString());
    }

    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(String.class);
        assertEquals("[simple type, class java.lang.String]", simpleType.toString());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(String.class);
        assertEquals(simpleType1, simpleType1);
        assertEquals(simpleType1, simpleType2);

        SimpleType simpleType3 = new SimpleType(Integer.class);
        assertNotEquals(simpleType1, null);
        assertNotEquals(simpleType1, new Object());
        assertNotEquals(simpleType1, simpleType3);
    }
}