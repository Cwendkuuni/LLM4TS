import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertEquals(String.class, simpleType.getRawClass());
    }

    @Test
    public void testConstruct() {
        try {
            SimpleType simpleType = SimpleType.construct(Map.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception, do nothing
        }

        try {
            SimpleType simpleType = SimpleType.construct(Collection.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception, do nothing
        }

        try {
            SimpleType simpleType = SimpleType.construct(String[].class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception, do nothing
        }

        SimpleType simpleType = SimpleType.construct(String.class);
        assertEquals(String.class, simpleType.getRawClass());
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = SimpleType.constructUnsafe(List.class);
        try {
            simpleType.withContentType(String.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception, do nothing
        }
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType1 = SimpleType.constructUnsafe(String.class);
        SimpleType simpleType2 = simpleType1.withTypeHandler(null);
        assertNull(simpleType2.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(List.class);
        try {
            simpleType.withContentTypeHandler(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception, do nothing
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType1 = SimpleType.constructUnsafe(String.class);
        SimpleType simpleType2 = simpleType1.withStaticTyping();
        assertTrue(simpleType2.isStatic());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType1 = SimpleType.constructUnsafe(String.class);
        JavaType refinedType = simpleType1.refine(String.class, null, null, null);
        assertNull(refinedType);
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
    public void testToString() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertEquals("[simple type, class java.lang.String]", simpleType.toString());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = SimpleType.constructUnsafe(String.class);
        SimpleType simpleType2 = SimpleType.constructUnsafe(String.class);
        assertTrue(simpleType1.equals(simpleType2));

        SimpleType simpleType3 = SimpleType.constructUnsafe(Integer.class);
        assertFalse(simpleType1.equals(simpleType3));
    }
}