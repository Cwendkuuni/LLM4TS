import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
        assertEquals(Object.class, simpleType.getRawClass());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStaticTyping());
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testConstruct() {
        try {
            SimpleType simpleType = SimpleType.construct(Map.class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Map (class: java.util.Map)", e.getMessage());
        }

        try {
            SimpleType simpleType = SimpleType.construct(Collection.class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Collection (class: java.util.Collection)", e.getMessage());
        }

        try {
            SimpleType simpleType = SimpleType.construct(int[].class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for an array (class: [I)", e.getMessage());
        }

        SimpleType simpleType = SimpleType.construct(Object.class);
        assertEquals(Object.class, simpleType.getRawClass());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStaticTyping());
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(Object.class);
        try {
            simpleType.withContentType(SimpleType.constructUnsafe(Object.class));
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = new SimpleType(Object.class);
        try {
            simpleType.withContentTypeHandler(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType simpleType = new SimpleType(Object.class);
        try {
            simpleType.withContentValueHandler(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertFalse(simpleType.isStaticTyping());
        SimpleType newSimpleType = simpleType.withStaticTyping();
        assertFalse(simpleType.isStaticTyping());
        assertTrue(newSimpleType.isStaticTyping());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertEquals(null, simpleType.refine(Object.class, TypeBindings.emptyBindings(), null, null));
    }

    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertEquals("[simple type, class java.lang.Object]", simpleType.toString());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(Object.class);
        SimpleType simpleType2 = new SimpleType(Object.class);
        SimpleType simpleType3 = new SimpleType(String.class);
        assertTrue(simpleType1.equals(simpleType2));
        assertFalse(simpleType1.equals(simpleType3));
    }
}