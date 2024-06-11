import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SimpleTypeTest {

    private SimpleType simpleType;

    @Before
    public void setUp() {
        simpleType = new SimpleType(Object.class);
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType constructed = SimpleType.constructUnsafe(String.class);
        assertEquals(String.class, constructed.getRawClass());
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
        SimpleType.construct(Array.class);
    }

    @Test
    public void testWithContentType() {
        try {
            simpleType.withContentType(String.class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testWithContentTypeHandler() {
        try {
            simpleType.withContentTypeHandler(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testWithContentValueHandler() {
        try {
            simpleType.withContentValueHandler(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType staticType = simpleType.withStaticTyping();
        assertTrue(staticType.isStaticTyping());
    }

    @Test
    public void testRefine() {
        JavaType refined = simpleType.refine(Object.class, TypeBindings.emptyBindings(), null, null);
        assertNull(refined);
    }

    @Test
    public void testIsContainerType() {
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        assertEquals("Ljava/lang/Object;", simpleType.getErasedSignature(new StringBuilder()).toString());
    }

    @Test
    public void testGetGenericSignature() {
        assertEquals("Ljava/lang/Object;;", simpleType.getGenericSignature(new StringBuilder()).toString());
    }

    @Test
    public void testEquals() {
        SimpleType otherType = new SimpleType(Object.class);
        assertTrue(simpleType.equals(otherType));
    }

    @Test
    public void testToString() {
        assertEquals("[simple type, class java.lang.Object]", simpleType.toString());
    }
}