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
    public void testConstruct_MapClass_ExceptionThrown() {
        try {
            SimpleType.construct(Map.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Map (class: java.util.Map)", e.getMessage());
        }
    }

    @Test
    public void testConstruct_CollectionClass_ExceptionThrown() {
        try {
            SimpleType.construct(Collection.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Collection (class: java.util.Collection)", e.getMessage());
        }
    }

    @Test
    public void testConstruct_ArrayClass_ExceptionThrown() {
        try {
            SimpleType.construct(String[].class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for an array (class: [Ljava.lang.String;)", e.getMessage());
        }
    }

    @Test
    public void testWithContentType_ExceptionThrown() {
        try {
            SimpleType simpleType = SimpleType.constructUnsafe(String.class);
            simpleType.withContentType(SimpleType.constructUnsafe(Integer.class));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }

    @Test
    public void testWithContentTypeHandler_ExceptionThrown() {
        try {
            SimpleType simpleType = SimpleType.constructUnsafe(String.class);
            simpleType.withContentTypeHandler(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithContentValueHandler_ExceptionThrown() {
        try {
            SimpleType simpleType = SimpleType.constructUnsafe(String.class);
            simpleType.withContentValueHandler(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
        }
    }

    @Test
    public void testConstruct_CopyConstructor() {
        SimpleType simpleType1 = SimpleType.constructUnsafe(String.class);
        SimpleType simpleType2 = new SimpleType(simpleType1);
        assertEquals(simpleType1.getRawClass(), simpleType2.getRawClass());
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertFalse(simpleType.isStaticTyping());
        SimpleType staticSimpleType = simpleType.withStaticTyping();
        assertTrue(staticSimpleType.isStaticTyping());
    }

    // Add more test cases for other methods...

}