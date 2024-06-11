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
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(String.class);
        assertThrows(IllegalArgumentException.class, () -> {
            simpleType.withContentType(null);
        });
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        assertThrows(UnsupportedOperationException.class, () -> {
            simpleType.withTypeHandler(null);
        });
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(String.class);
        assertNull(simpleType.refine(String.class, TypeBindings.emptyBindings(), null, null));
    }
}