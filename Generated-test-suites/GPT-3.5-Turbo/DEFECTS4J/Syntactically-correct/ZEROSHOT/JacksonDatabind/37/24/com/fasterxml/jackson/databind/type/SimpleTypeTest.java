
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType type = SimpleType.constructUnsafe(Integer.class);
        assertNotNull(type);
        assertEquals(Integer.class, type.getRawClass());
    }

    @Test
    public void testConstruct() {
        SimpleType type = SimpleType.construct(Long.class);
        assertNotNull(type);
        assertEquals(Long.class, type.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithTypeMap() {
        SimpleType.construct(HashMap.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithTypeCollection() {
        SimpleType.construct(ArrayList.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithTypeArray() {
        SimpleType.construct(String[].class);
    }

    @Test
    public void testWithContentType() {
        SimpleType type = SimpleType.constructUnsafe(Map.class);
        try {
            type.withContentType(Object.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        Object handler = new Object();
        SimpleType newType = type.withTypeHandler(handler);
        assertNotSame(type, newType);
        assertEquals(handler, newType.getTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandler() {
        SimpleType type = SimpleType.constructUnsafe(Set.class);
        type.withContentTypeHandler(new Object());
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType type = SimpleType.constructUnsafe(List.class);
        assertFalse(type.isStaticTyping());
        SimpleType newType = type.withStaticTyping();
        assertNotSame(type, newType);
        assertTrue(newType.isStaticTyping());
    }

    @Test
    public void testRefine() {
        SimpleType type = SimpleType.constructUnsafe(ArrayList.class);
        assertNull(type.refine(ArrayList.class, TypeBindings.emptyBindings(), null, null));
    }

    @Test
    public void testIsContainerType() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        assertFalse(type.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType type = SimpleType.constructUnsafe(Integer.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = type.getErasedSignature(sb);
        assertEquals(sb, result);
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = type.getGenericSignature(sb);
        assertEquals(sb, result);
    }

    @Test
    public void testToString() {
        SimpleType type = SimpleType.constructUnsafe(Boolean.class);
        String result = type.toString();
        assertEquals("[simple type, class java.lang.Boolean]", result);
    }

    @Test
    public void testEquals() {
        SimpleType type1 = SimpleType.constructUnsafe(Integer.class);
        SimpleType type2 = SimpleType.constructUnsafe(Integer.class);
        assertTrue(type1.equals(type2));
    }

}
