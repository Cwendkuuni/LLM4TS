import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        assertNotNull(type);
        assertEquals(String.class, type.getRawClass());
    }

    @Test
    public void testConstruct_exception() {
        try {
            SimpleType.construct(Map.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testConstructArray_exception() {
        try {
            SimpleType.construct(String[].class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testConstruct() {
        SimpleType type = SimpleType.construct(String.class);
        assertNotNull(type);
        assertEquals(String.class, type.getRawClass());
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType type = SimpleType.construct(String.class);
        SimpleType staticType = (SimpleType) type.withStaticTyping();
        assertTrue(staticType.isStaticTyping());
    }

    @Test
    public void testWithContentType_exception() {
        SimpleType type = SimpleType.construct(String.class);
        try {
            type.withContentType(String.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testWithContentType_valueHandler() {
        SimpleType type = SimpleType.construct(String.class);
        Object valueHandler = new Object();
        SimpleType newType = (SimpleType) type.withValueHandler(valueHandler);
        assertEquals(valueHandler, newType.getValueHandler());
    }

    @Test
    public void testEquals() {
        SimpleType type1 = SimpleType.construct(String.class);
        SimpleType type2 = SimpleType.construct(String.class);
        assertTrue(type1.equals(type2));
    }
}