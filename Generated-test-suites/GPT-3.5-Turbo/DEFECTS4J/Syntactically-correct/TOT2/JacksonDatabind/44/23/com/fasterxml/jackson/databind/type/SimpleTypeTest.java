import org.junit.Test;
import static org.junit.Assert.*;

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
            SimpleType.construct(Map.class);
            fail("Should throw IllegalArgumentException for Map class");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            SimpleType.construct(Collection.class);
            fail("Should throw IllegalArgumentException for Collection class");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            SimpleType.construct(String[].class);
            fail("Should throw IllegalArgumentException for array class");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testNarrow() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        SimpleType narrowedType = simpleType._narrow(String.class);

        assertEquals(String.class, narrowedType.getRawClass());
    }

    @Test
    public void testContentType() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);

        try {
            simpleType.withContentType(String.class);
            fail("Should throw IllegalArgumentException for withContentType() method");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testValueHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        Object handler = new Object();

        SimpleType handledType = simpleType.withValueHandler(handler);

        assertEquals(handler, handledType.getValueHandler());
    }

    @Test
    public void testStaticTyping() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);

        SimpleType staticType = simpleType.withStaticTyping();

        assertTrue(staticType.isStaticTyping());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);

        JavaType refinedType = simpleType.refine(Integer.class, TypeBindings.emptyBindings(),
                null, null);

        assertNull(refinedType);
    }

}