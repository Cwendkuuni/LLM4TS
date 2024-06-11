import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
    }

    @Test
    public void testConstructForMap() {
        try {
            SimpleType.construct(Map.class);
            fail("Should throw IllegalArgumentException for Map class");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testConstructForCollection() {
        try {
            SimpleType.construct(Collection.class);
            fail("Should throw IllegalArgumentException for Collection class");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testConstructForArray() {
        try {
            SimpleType.construct(String[].class);
            fail("Should throw IllegalArgumentException for Array class");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(String.class);
        try {
            simpleType.withContentType(String.class);
            fail("Should throw IllegalArgumentException for withContentType()");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        try {
            simpleType.withContentValueHandler("handler");
            fail("Should throw IllegalArgumentException for withContentValueHandler()");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType simpleType = new SimpleType(String.class);
        String expectedCanonicalName = "java.lang.String";
        assertEquals(expectedCanonicalName, simpleType.buildCanonicalName());
    }

}