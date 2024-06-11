//import statements

import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        assertEquals(String.class, type.getRawClass());
        assertTrue(type.getBindings().isEmpty());
        assertNull(type.getSuperClass());
        assertNull(type.getInterfaces());
        assertNull(type.getValueHandler());
        assertNull(type.getTypeHandler());
        assertFalse(type.isAsStatic());
    }

    @Test
    public void testConstruct() {
        SimpleType type = SimpleType.construct(ArrayList.class);
        assertEquals(ArrayList.class, type.getRawClass());
        assertTrue(type.getBindings().isEmpty());
        assertNull(type.getSuperClass());
        assertNull(type.getInterfaces());
        assertNull(type.getValueHandler());
        assertNull(type.getTypeHandler());
        assertFalse(type.isAsStatic());
    }

    @Test
    public void testNarrow() {
        SimpleType baseType = SimpleType.constructUnsafe(Number.class);
        SimpleType type = (SimpleType) baseType.narrow(Integer.class);
        assertEquals(Integer.class, type.getRawClass());
        assertTrue(type.getBindings().isEmpty());
        assertEquals(baseType, type.getSuperClass());
        assertNull(type.getInterfaces());
        assertNull(type.getValueHandler());
        assertNull(type.getTypeHandler());
        assertFalse(type.isAsStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithTypeException_Map() {
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithTypeException_Collection() {
        SimpleType.construct(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithTypeException_Array() {
        SimpleType.construct(String[].class);
    }

    @Test
    public void testWithContentType() {
        SimpleType type = SimpleType.constructUnsafe(List.class);
        try {
            type.withContentType(String.class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }

    @Test
    public void testWithGenericTypeHandler() {
        SimpleType type = SimpleType.constructUnsafe(Integer.class);
        Object handler = new Object();
        SimpleType newType = type.withTypeHandler(handler);
        assertEquals(Integer.class, newType.getRawClass());
        assertEquals(type.getBindings(), newType.getBindings());
        assertEquals(type.getSuperClass(), newType.getSuperClass());
        assertEquals(type.getInterfaces(), newType.getInterfaces());
        assertEquals(type.getValueHandler(), newType.getValueHandler());
        assertSame(handler, newType.getTypeHandler());
        assertFalse(newType.isAsStatic());
    }

    @Test
    public void testWithGenericContentTypeHandler() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        try {
            type.withContentTypeHandler(new Object());
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithValueHandler() {
        SimpleType type = SimpleType.constructUnsafe(Character.class);
        Object handler = new Object();
        SimpleType newType = type.withValueHandler(handler);
        assertEquals(Character.class, newType.getRawClass());
        assertEquals(type.getBindings(), newType.getBindings());
        assertEquals(type.getSuperClass(), newType.getSuperClass());
        assertEquals(type.getInterfaces(), newType.getInterfaces());
        assertSame(handler, newType.getValueHandler());
        assertEquals(type.getTypeHandler(), newType.getTypeHandler());
        assertFalse(newType.isAsStatic());
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType type = SimpleType.constructUnsafe(Boolean.class);
        try {
            type.withContentValueHandler(new Object());
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType type = SimpleType.constructUnsafe(Long.class);
        SimpleType newType = type.withStaticTyping();
        assertEquals(Long.class, newType.getRawClass());
        assertEquals(type.getBindings(), newType.getBindings());
        assertEquals(type.getSuperClass(), newType.getSuperClass());
        assertEquals(type.getInterfaces(), newType.getInterfaces());
        assertEquals(type.getValueHandler(), newType.getValueHandler());
        assertEquals(type.getTypeHandler(), newType.getTypeHandler());
        assertTrue(newType.isAsStatic());
    }

    @Test
    public void testRefine() {
        SimpleType type = SimpleType.constructUnsafe(Byte.class);
        try {
            type.refine(Integer.class, null, null, null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            assertNull(e.getMessage());
        }
    }

    @Test
    public void testIsContainerType() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        assertFalse(type.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType type = SimpleType.constructUnsafe(Double.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = type.getErasedSignature(sb);
        assertSame(sb, result);
        assertEquals("Ljava/lang/Double;", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType type = SimpleType.constructUnsafe(Float.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = type.getGenericSignature(sb);
        assertSame(sb, result);
        assertEquals("Ljava/lang/Float;", sb.toString());
    }

    @Test
    public void testToString() {
        SimpleType type = SimpleType.constructUnsafe(Short.class);
        assertEquals("[simple type, class java.lang.Short]", type.toString());
    }

    @Test
    public void testEquals() {
        SimpleType type1 = SimpleType.constructUnsafe(Object.class);
        SimpleType type2 = SimpleType.constructUnsafe(Object.class);
        assertTrue(type1.equals(type2));

        type1 = SimpleType.constructUnsafe(String.class);
        type2 = SimpleType.constructUnsafe(Object.class);
        assertFalse(type1.equals(type2));

        type1 = SimpleType.constructUnsafe(Object.class);
        type2 = SimpleType.constructUnsafe(String.class);
        assertFalse(type1.equals(type2));

        type1 = SimpleType.constructUnsafe(String.class);
        type2 = SimpleType.constructUnsafe(String.class);
        assertTrue(type1.equals(type2));

        type1 = SimpleType.constructUnsafe(String.class);
        type2 = SimpleType.constructUnsafe(StringBuilder.class);
        assertFalse(type1.equals(type2));

        type1 = SimpleType.constructUnsafe(StringBuilder.class);
        type2 = SimpleType.constructUnsafe(String.class);
        assertFalse(type1.equals(type2));

        type1 = SimpleType.constructUnsafe(StringBuilder.class);
        type2 = SimpleType.constructUnsafe(StringBuilder.class);
        assertTrue(type1.equals(type2));

        type1 = SimpleType.constructUnsafe(Number.class);
        type2 = SimpleType.constructUnsafe(Number.class);
        assertTrue(type1.equals(type2));

        type1 = SimpleType.constructUnsafe(Integer.class);
        type2 = SimpleType.constructUnsafe(Number.class);
        assertFalse(type1.equals(type2));
    }
}