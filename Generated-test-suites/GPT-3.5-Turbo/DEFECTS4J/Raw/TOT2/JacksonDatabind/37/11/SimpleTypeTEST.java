import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertNotNull(simpleType);
    }

    @Test
    public void testConstruct() {
        SimpleType simpleType = SimpleType.construct(String.class);
        assertNotNull(simpleType);
    }

    @Test
    public void testNarrow() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType narrowedType = (SimpleType) simpleType._narrow(Object.class);
        assertEquals(Object.class, narrowedType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(String.class);
        simpleType.withContentType(String.class);
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType staticTypedType = (SimpleType) simpleType.withStaticTyping();
        assertTrue(staticTypedType.isStaticTyped());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(String.class);
        JavaType refinedType = simpleType.refine(String.class, new TypeBindings(), null, null);
        assertNull(refinedType);
    }
}