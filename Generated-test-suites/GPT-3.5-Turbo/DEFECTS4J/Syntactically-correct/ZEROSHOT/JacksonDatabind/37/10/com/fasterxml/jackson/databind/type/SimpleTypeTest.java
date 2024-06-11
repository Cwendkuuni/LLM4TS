import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType st = SimpleType.constructUnsafe(String.class);
        assertEquals(String.class, st.getRawClass());
        assertFalse(st.isContainerType());
    }

    @Test
    public void testConstruct() {
        try {
            SimpleType.construct(Map.class);
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Map (class: java.util.Map)", e.getMessage());
        }

        try {
            SimpleType.construct(Collection.class);
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Collection (class: java.util.Collection)", e.getMessage());
        }

        try {
            SimpleType.construct(int[].class);
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for an array (class: [I)", e.getMessage());
        }
    }

    @Test
    public void testWithContentType() {
        SimpleType st = SimpleType.constructUnsafe(List.class);
        try {
            st.withContentType(String.class);
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType st = SimpleType.constructUnsafe(String.class);
        Object handler = new Object();
        SimpleType newSt = st.withTypeHandler(handler);

        assertEquals(handler, newSt.getTypeHandler());
        assertNotSame(st, newSt);
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType st = SimpleType.constructUnsafe(List.class);
        try {
            st.withContentTypeHandler(new Object());
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType st = SimpleType.constructUnsafe(List.class);
        try {
            st.withContentValueHandler(new Object());
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType st = SimpleType.constructUnsafe(String.class);
        SimpleType newSt = st.withStaticTyping();

        assertTrue(newSt.isStatic());
        assertNotSame(st, newSt);
    }

    @Test
    public void testRefine() {
        SimpleType st = SimpleType.constructUnsafe(String.class);
        JavaType newType = st.refine(Object.class, null, null, null);

        assertNull(newType);
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType st = SimpleType.constructUnsafe(int.class);
        StringBuilder sb = st.getErasedSignature(new StringBuilder());

        assertEquals("I", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType st = SimpleType.constructUnsafe(String.class);
        StringBuilder sb = st.getGenericSignature(new StringBuilder());

        assertEquals("Ljava/lang/String;", sb.toString());
    }

    @Test
    public void testToString() {
        SimpleType st = SimpleType.constructUnsafe(boolean.class);
        String str = st.toString();

        assertEquals("[simple type, class boolean]", str);
    }

    @Test
    public void testEquals() {
        SimpleType st1 = SimpleType.constructUnsafe(boolean.class);
        SimpleType st2 = SimpleType.constructUnsafe(boolean.class);
        SimpleType st3 = SimpleType.constructUnsafe(String.class);

        assertTrue(st1.equals(st2));
        assertFalse(st1.equals(st3));
    }
}