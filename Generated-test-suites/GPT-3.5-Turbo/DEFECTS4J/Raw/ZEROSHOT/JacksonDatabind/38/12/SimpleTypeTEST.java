import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType type1 = SimpleType.constructUnsafe(String.class);
        SimpleType type2 = SimpleType.constructUnsafe(Integer.class);

        assertEquals(String.class, type1.getRawClass());
        assertEquals(Integer.class, type2.getRawClass());
    }

    @Test
    public void testConstruct() {
        SimpleType type1 = SimpleType.construct(String.class);
        SimpleType type2 = SimpleType.construct(Integer.class);

        assertEquals(String.class, type1.getRawClass());
        assertEquals(Integer.class, type2.getRawClass());
    }

    @Test
    public void testWithContentType() {
        SimpleType type1 = SimpleType.constructUnsafe(String.class);
        SimpleType type2 = (SimpleType) type1.withContentType(SimpleType.construct(Integer.class));

        assertEquals(String.class, type1.getRawClass());
        assertEquals(Integer.class, type2.getContentType().getRawClass());
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType type1 = SimpleType.constructUnsafe(String.class);
        SimpleType type2 = (SimpleType) type1.withContentTypeHandler(new Object());

        assertEquals(String.class, type1.getRawClass());
        assertEquals(Object.class, type2.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        SimpleType type1 = SimpleType.constructUnsafe(String.class);
        SimpleType type2 = (SimpleType) type1.withValueHandler(new Object());

        assertEquals(String.class, type1.getRawClass());
        assertEquals(Object.class, type2.getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType type1 = SimpleType.constructUnsafe(String.class);
        SimpleType type2 = (SimpleType) type1.withStaticTyping();

        assertEquals(String.class, type1.getRawClass());
        assertTrue(type2.isStatic());
    }

    @Test
    public void testRefine() {
        SimpleType type1 = SimpleType.constructUnsafe(String.class);
        JavaType type2 = type1.refine(SimpleType.constructUnsafe(Object.class).getRawClass(), null, null, null);

        assertEquals(String.class, type1.getRawClass());
        assertEquals(Object.class, type2.getRawClass());
    }

    @Test
    public void testIsContainerType() {
        SimpleType type1 = SimpleType.constructUnsafe(String.class);

        assertFalse(type1.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType type1 = SimpleType.constructUnsafe(String.class);
        StringBuilder sb = new StringBuilder();

        type1.getErasedSignature(sb);

        assertEquals("Ljava/lang/String;", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType type1 = SimpleType.constructUnsafe(String.class);
        StringBuilder sb = new StringBuilder();

        type1.getGenericSignature(sb);

        assertEquals("Ljava/lang/String;;", sb.toString());
    }

    @Test
    public void testEquals() {
        SimpleType type1 = SimpleType.constructUnsafe(String.class);
        SimpleType type2 = SimpleType.constructUnsafe(String.class);
        SimpleType type3 = SimpleType.constructUnsafe(Integer.class);

        assertTrue(type1.equals(type2));
        assertFalse(type1.equals(type3));
    }

    @Test
    public void testToString() {
        SimpleType type1 = SimpleType.constructUnsafe(String.class);

        assertEquals("[simple type, class java.lang.String]", type1.toString());
    }
}