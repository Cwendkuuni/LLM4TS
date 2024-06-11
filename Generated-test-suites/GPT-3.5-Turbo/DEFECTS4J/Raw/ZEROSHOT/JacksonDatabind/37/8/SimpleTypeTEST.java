import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        assertEquals(String.class, type.getRawClass());
    }

    @Test
    public void testConstruct() {
        try {
            SimpleType type = SimpleType.construct(Map.class);
            fail("Exception should have been thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Map (class: java.util.Map)", e.getMessage());
        }

        try {
            SimpleType type = SimpleType.construct(Collection.class);
            fail("Exception should have been thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Collection (class: java.util.Collection)", e.getMessage());
        }

        try {
            SimpleType type = SimpleType.construct(int[].class);
            fail("Exception should have been thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for an array (class: [I)", e.getMessage());
        }
    }

    @Test
    public void test_narrow() {
        SimpleType type = SimpleType.constructUnsafe(Number.class);
        SimpleType subtype = (SimpleType) type.narrow(Integer.class);
        assertEquals(Integer.class, subtype.getRawClass());
    }

    @Test
    public void testWithContentType() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        try {
            type.withContentType(SimpleType.constructUnsafe(Integer.class));
            fail("Exception should have been thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        Object handler = new Object();
        SimpleType newType = type.withTypeHandler(handler);
        assertEquals(handler, newType.getTypeHandler());
        assertNotSame(newType, type);
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        try {
            type.withContentTypeHandler(new Object());
            fail("Exception should have been thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithValueHandler() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        Object handler = new Object();
        SimpleType newType = type.withValueHandler(handler);
        assertEquals(handler, newType.getValueHandler());
        assertNotSame(newType, type);
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        try {
            type.withContentValueHandler(new Object());
            fail("Exception should have been thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        SimpleType newType = type.withStaticTyping();
        assertTrue(newType.isStatic());
        assertNotSame(newType, type);
    }

    @Test
    public void testRefine() {
        SimpleType type = SimpleType.constructUnsafe(Number.class);
        JavaType refinedType = type.refine(Integer.class, TypeBindings.emptyBindings(), null, null);
        assertNull(refinedType);
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder signature = type.getErasedSignature(sb);
        assertEquals("Ljava/lang/String;", signature.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder signature = type.getGenericSignature(sb);
        assertEquals("Ljava/lang/String;", signature.toString());
    }

    @Test
    public void testIsContainerType() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        assertFalse(type.isContainerType());
    }

    @Test
    public void testToString() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        assertEquals("[simple type, class java.lang.String]", type.toString());
    }

    @Test
    public void testEquals() {
        SimpleType type1 = SimpleType.constructUnsafe(String.class);
        SimpleType type2 = SimpleType.constructUnsafe(String.class);
        SimpleType type3 = SimpleType.constructUnsafe(Integer.class);
        assertTrue(type1.equals(type2));
        assertFalse(type1.equals(type3));
    }
}