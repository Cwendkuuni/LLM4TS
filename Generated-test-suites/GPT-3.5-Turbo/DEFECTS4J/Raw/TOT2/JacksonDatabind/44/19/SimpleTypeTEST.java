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
    public void testConstructWithMap() {
        try {
            SimpleType.construct(Map.class);
            fail("Should throw IllegalArgumentException for Map class");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testConstructWithArray() {
        try {
            SimpleType.construct(String[].class);
            fail("Should throw IllegalArgumentException for Array class");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = SimpleType.constructUnsafe(List.class);
        try {
            simpleType.withContentType(String.class);
            fail("Should throw IllegalArgumentException for withContentType()");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        try {
            simpleType.withContentTypeHandler(null);
            fail("Should throw IllegalArgumentException for withContentTypeHandler()");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        try {
            simpleType.withContentValueHandler(null);
            fail("Should throw IllegalArgumentException for withContentValueHandler()");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

}