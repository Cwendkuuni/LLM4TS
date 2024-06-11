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
        SimpleType type = SimpleType.construct(String.class);
        assertNotNull(type);
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
    public void testNarrow() {
        SimpleType base = SimpleType.construct(Object.class);
        SimpleType narrowed = (SimpleType) base.narrow(String.class);
        assertNotNull(narrowed);
        assertEquals(String.class, narrowed.getRawClass());
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType type = SimpleType.construct(String.class);
        Object handler = new SampleHandler();
        SimpleType modified = type.withContentTypeHandler(handler);
        assertNotNull(modified);
        assertEquals(handler, modified.getContentType().getTypeHandler());
    }

    @Test
    public void testRefine() {
        SimpleType type = SimpleType.construct(Object.class);
        JavaType refined = type.refine(String.class, TypeBindings.emptyBindings(), null, null);
        assertNull(refined);
    }

    private static class SampleHandler {
        // just for testing
    }
}