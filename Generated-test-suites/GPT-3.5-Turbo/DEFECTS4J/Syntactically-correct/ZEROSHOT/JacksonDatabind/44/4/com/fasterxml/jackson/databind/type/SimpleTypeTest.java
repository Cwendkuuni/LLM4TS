import org.junit.*;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertEquals(String.class, simpleType.getRawClass());
        assertFalse(simpleType.isContainerType());
        assertEquals("String", simpleType.toCanonical());
    }

    @Test
    public void testConstruct() {
        try {
            SimpleType.construct(Map.class);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Map (class: java.util.Map)", e.getMessage());
        }
        try {
            SimpleType.construct(Collection.class);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Collection (class: java.util.Collection)", e.getMessage());
        }
        try {
            SimpleType.construct(String[].class);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for an array (class: [Ljava.lang.String;)", e.getMessage());
        }
    }

    @Test
    public void testNarrow() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertEquals(simpleType, simpleType._narrow(String.class));
        assertNotEquals(simpleType, simpleType._narrow(Integer.class));
        SimpleType subType = (SimpleType) simpleType._narrow(CharSequence.class);
        assertEquals(CharSequence.class, subType.getRawClass());
        assertEquals(simpleType.getBindings(), subType.getBindings());
        assertEquals(simpleType.getSuperClass(), subType.getSuperClass());
        assertEquals(simpleType.getSuperInterfaces(), subType.getSuperInterfaces());
        assertEquals(simpleType.getValueHandler(), subType.getValueHandler());
        assertEquals(simpleType.getTypeHandler(), subType.getTypeHandler());
        assertEquals(simpleType.isStatic(), subType.isStatic());
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        try {
            simpleType.withContentType(null);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        Object typeHandler = new Object();
        SimpleType newType = simpleType.withTypeHandler(typeHandler);
        assertEquals(simpleType.getRawClass(), newType.getRawClass());
        assertEquals(simpleType.getBindings(), newType.getBindings());
        assertEquals(simpleType.getSuperClass(), newType.getSuperClass());
        assertEquals(simpleType.getSuperInterfaces(), newType.getSuperInterfaces());
        assertEquals(simpleType.getValueHandler(), newType.getValueHandler());
        assertEquals(typeHandler, newType.getTypeHandler());
        assertEquals(simpleType.isStatic(), newType.isStatic());
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        try {
            simpleType.withContentTypeHandler(null);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        try {
            simpleType.withContentValueHandler(null);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        SimpleType newType = simpleType.withStaticTyping();
        assertEquals(simpleType.getRawClass(), newType.getRawClass());
        assertEquals(simpleType.getBindings(), newType.getBindings());
        assertEquals(simpleType.getSuperClass(), newType.getSuperClass());
        assertEquals(simpleType.getSuperInterfaces(), newType.getSuperInterfaces());
        assertEquals(simpleType.getValueHandler(), newType.getValueHandler());
        assertEquals(simpleType.getTypeHandler(), newType.getTypeHandler());
        assertTrue(newType.isStatic());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertNull(simpleType.refine(null, null, null, null));
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        StringBuilder sb = simpleType.getErasedSignature(new StringBuilder());
        assertEquals("Ljava/lang/String;", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        StringBuilder sb = simpleType.getGenericSignature(new StringBuilder());
        assertEquals("Ljava/lang/String;", sb.toString());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        SimpleType sameType = SimpleType.constructUnsafe(String.class);
        SimpleType differentType = SimpleType.constructUnsafe(Integer.class);
        assertTrue(simpleType.equals(simpleType));
        assertTrue(simpleType.equals(sameType));
        assertFalse(simpleType.equals(differentType));
        assertFalse(simpleType.equals(null));
        assertFalse(simpleType.equals("String"));
    }

    @Test
    public void testToString() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertEquals("[simple type, class java.lang.String]", simpleType.toString());
    }
}