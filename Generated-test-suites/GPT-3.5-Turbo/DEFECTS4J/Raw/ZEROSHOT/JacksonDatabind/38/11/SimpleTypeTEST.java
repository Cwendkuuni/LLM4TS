import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
    }

    @Test
    public void testConstruct() {
        try {
            SimpleType simpleType = SimpleType.construct(Map.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        try {
            SimpleType simpleType = SimpleType.construct(Collection.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        try {
            SimpleType simpleType = SimpleType.construct(int[].class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testNarrow() {
        SimpleType simpleType = new SimpleType(String.class);
        assertEquals(simpleType, simpleType._narrow(String.class));
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(Integer.class);
        try {
            simpleType.withContentType(Integer.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = new SimpleType(Long.class);
        Object handler = new Object();
        SimpleType result = simpleType.withTypeHandler(handler);
        assertNotEquals(simpleType, result);
        assertEquals(handler, result.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = new SimpleType(Float.class);
        try {
            simpleType.withContentTypeHandler(new Object());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType simpleType = new SimpleType(Double.class);
        try {
            simpleType.withContentValueHandler(new Object());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(Character.class);
        assertEquals(simpleType, simpleType.withStaticTyping());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(Boolean.class);
        assertNull(simpleType.refine(Object.class, null, null, null));
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(Byte.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(Short.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = simpleType.getErasedSignature(sb);
        assertEquals(simpleType.toString(), result.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(Void.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = simpleType.getGenericSignature(sb);
        assertEquals(simpleType.toString(), result.toString());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType = new SimpleType(Integer.class);
        assertTrue(simpleType.equals(simpleType));
        assertFalse(simpleType.equals(null));
        assertFalse(simpleType.equals(new Object()));
        assertFalse(simpleType.equals(new SimpleType(Long.class)));

        SimpleType sameType = new SimpleType(Integer.class);
        assertTrue(simpleType.equals(sameType));

        SimpleType differentType = new SimpleType(Byte.class);
        assertFalse(simpleType.equals(differentType));
    }

}