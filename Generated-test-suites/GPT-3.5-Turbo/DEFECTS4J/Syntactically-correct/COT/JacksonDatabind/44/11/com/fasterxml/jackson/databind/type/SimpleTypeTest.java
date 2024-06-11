import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstruct() {
        SimpleType type = SimpleType.construct(String.class);
        assertEquals(String.class, type.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        SimpleType.construct(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        SimpleType.construct(String[].class);
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        assertEquals(String.class, type.getRawClass());
    }

    @Test
    public void testNarrow() {
        SimpleType baseType = SimpleType.construct(Object.class);
        SimpleType narrowedType = baseType.narrow(String.class);
        assertEquals(String.class, narrowedType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithInvalidSubclass() {
        SimpleType baseType = SimpleType.construct(Object.class);
        baseType.narrow(Integer.class);
    }

    @Test
    public void testWithContentType() {
        SimpleType type = SimpleType.construct(String.class);
        try {
            type.withContentType(String.class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType type = SimpleType.construct(String.class);
        Object typeHandler = new Object();
        SimpleType newType = type.withTypeHandler(typeHandler);
        assertEquals(typeHandler, newType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType type = SimpleType.construct(String.class);
        try {
            type.withContentTypeHandler(new Object());
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithValueHandler() {
        SimpleType type = SimpleType.construct(String.class);
        Object valueHandler = new Object();
        SimpleType newType = type.withValueHandler(valueHandler);
        assertEquals(valueHandler, newType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType type = SimpleType.construct(String.class);
        try {
            type.withContentValueHandler(new Object());
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType type = SimpleType.construct(String.class);
        assertFalse(type.isStaticTyping());
        SimpleType newType = type.withStaticTyping();
        assertTrue(newType.isStaticTyping());
    }

    @Test
    public void testRefine() {
        SimpleType type = SimpleType.construct(String.class);
        assertNull(type.refine(String.class, null, null, null));
    }

    @Test
    public void testIsContainerType() {
        SimpleType type = SimpleType.construct(String.class);
        assertFalse(type.isContainerType());
    }

    // Add additional test cases for edge cases and exception handling scenarios here

}

