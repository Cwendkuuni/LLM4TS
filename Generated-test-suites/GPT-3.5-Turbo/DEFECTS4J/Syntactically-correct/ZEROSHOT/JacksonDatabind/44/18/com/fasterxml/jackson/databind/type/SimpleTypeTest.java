
import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        assertEquals(String.class, type.getRawClass());
    }

    @Test
    public void testConstruct() {
        SimpleType type = SimpleType.construct(Integer.class);
        assertEquals(Integer.class, type.getRawClass());
    }

    @Test
    public void test_narrow() {
        SimpleType baseType = new SimpleType(Integer.class);
        SimpleType newType = baseType._narrow(Long.class);
        assertEquals(Long.class, newType.getRawClass());
    }

    @Test
    public void testWithContentType() {
        SimpleType type = new SimpleType(String.class);
        try {
            type.withContentType(Integer.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType type = new SimpleType(String.class);
        try {
            type.withContentTypeHandler("handler");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType type = new SimpleType(String.class);
        SimpleType staticType = type.withStaticTyping();
        assertTrue(staticType.isStatic());
    }

    @Test
    public void testRefine() {
        SimpleType type = new SimpleType(String.class);
        JavaType refinedType = type.refine(String.class, TypeBindings.emptyBindings(), null, null);
        assertNull(refinedType);
    }

    @Test
    public void testIsContainerType() {
        SimpleType type = new SimpleType(String.class);
        assertFalse(type.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType type = new SimpleType(String.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = type.getErasedSignature(sb);
        assertEquals("Ljava/lang/String;", result.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType type = new SimpleType(String.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = type.getGenericSignature(sb);
        assertEquals("Ljava/lang/String;", result.toString());
    }

    @Test
    public void testEquals() {
        SimpleType type1 = new SimpleType(String.class);
        SimpleType type2 = new SimpleType(String.class);
        assertTrue(type1.equals(type2));
    }
}
