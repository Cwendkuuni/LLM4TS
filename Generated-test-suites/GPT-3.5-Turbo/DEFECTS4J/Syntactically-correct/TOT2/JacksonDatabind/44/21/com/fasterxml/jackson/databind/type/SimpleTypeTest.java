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
    public void testConstructIllegalArgumentExceptionForMap() {
        try {
            SimpleType.construct(Map.class);
            fail("Expected IllegalArgumentException for Map class");
        } catch (IllegalArgumentException e) {
            // Success
        }
    }

    @Test
    public void testConstructIllegalArgumentExceptionForCollection() {
        try {
            SimpleType.construct(Collection.class);
            fail("Expected IllegalArgumentException for Collection class");
        } catch (IllegalArgumentException e) {
            // Success
        }
    }

    @Test
    public void testConstructIllegalArgumentExceptionForArray() {
        try {
            SimpleType.construct(String[].class);
            fail("Expected IllegalArgumentException for Array class");
        } catch (IllegalArgumentException e) {
            // Success
        }
    }

    @Test
    public void testWithContentTypeThrowsException() {
        SimpleType simpleType = new SimpleType(String.class);
        try {
            simpleType.withContentType(String.class);
            fail("Expected IllegalArgumentException for calling withContentType()");
        } catch (IllegalArgumentException e) {
            // Success
        }
    }

    @Test
    public void testWithContentValueHandlerThrowsException() {
        SimpleType simpleType = new SimpleType(String.class);
        try {
            simpleType.withContentValueHandler(null);
            fail("Expected IllegalArgumentException for calling withContentValueHandler()");
        } catch (IllegalArgumentException e) {
            // Success
        }
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType simpleType = new SimpleType(String.class);
        assertEquals("java.lang.String", simpleType.buildCanonicalName());
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(String.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(String.class);
        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);
        assertEquals("L" + String.class.getName() + ";", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(String.class);
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        assertEquals("L" + String.class.getName() + "<>;", sb.toString());
    }

}