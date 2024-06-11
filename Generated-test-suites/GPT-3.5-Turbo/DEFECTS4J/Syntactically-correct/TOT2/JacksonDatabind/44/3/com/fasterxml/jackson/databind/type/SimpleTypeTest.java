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
        SimpleType simpleType = SimpleType.construct(Integer.class);
        assertNotNull(simpleType);
        assertEquals(Integer.class, simpleType.getRawClass());
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = SimpleType.construct(String.class);
        try {
            simpleType.withContentType(String.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = SimpleType.construct(String.class);
        try {
            simpleType.withContentTypeHandler(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType simpleType = SimpleType.construct(String.class);
        try {
            simpleType.withContentValueHandler(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = SimpleType.construct(Integer.class);
        JavaType javaType = simpleType.refine(String.class, TypeBindings.emptyBindings(), null, null);
        assertNull(javaType);
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = SimpleType.construct(Integer.class);
        assertFalse(simpleType.isContainerType());
    }

}