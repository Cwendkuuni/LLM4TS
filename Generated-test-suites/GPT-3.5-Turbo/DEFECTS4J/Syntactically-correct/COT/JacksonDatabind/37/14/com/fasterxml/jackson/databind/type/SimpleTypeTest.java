import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        assertNotNull(type);
        assertEquals(String.class, type.getRawClass());
    }

    @Test
    public void testConstruct() {
        SimpleType type = SimpleType.construct(Integer.class);
        assertNotNull(type);
        assertEquals(Integer.class, type.getRawClass());
    }

    @Test
    public void testWithContentType() {
        SimpleType originalType = SimpleType.construct(ArrayList.class);
        try {
            originalType.withContentType(Integer.class);
            fail("Expected IllegalArgumentException but test passed");
        } catch (IllegalArgumentException e) {
            // Test passed
        }
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType originalType = SimpleType.construct(Map.class);
        try {
            originalType.withTypeHandler(null);
            fail("Expected IllegalArgumentException but test passed");
        } catch (IllegalArgumentException e) {
            // Test passed
        }
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType originalType = SimpleType.construct(HashSet.class);
        try {
            originalType.withContentTypeHandler(null);
            fail("Expected IllegalArgumentException but test passed");
        } catch (IllegalArgumentException e) {
            // Test passed
        }
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType originalType = SimpleType.construct(TreeSet.class);
        try {
            originalType.withContentValueHandler(null);
            fail("Expected IllegalArgumentException but test passed");
        } catch (IllegalArgumentException e) {
            // Test passed
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType originalType = SimpleType.construct(Double.class);
        SimpleType newType = originalType.withStaticTyping();
        assertNotEquals(originalType, newType);
        assertTrue(newType.isStatic());
    }

    @Test
    public void testRefine() {
        SimpleType originalType = SimpleType.constructUnsafe(LinkedList.class);
        JavaType newType = originalType.refine(LinkedList.class, TypeBindings.emptyBindings(), null, null);
        assertNull(newType);
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType type = SimpleType.constructUnsafe(Long.class);
        StringBuilder sb = new StringBuilder();
        type.getErasedSignature(sb);
        assertEquals("Ljava/lang/Long;", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType type = SimpleType.constructUnsafe(HashMap.class);
        StringBuilder sb = new StringBuilder();
        type.getGenericSignature(sb);
        assertEquals("Ljava/util/HashMap<Ljava/lang/Object;Ljava/lang/Object;>;", sb.toString());
    }

    @Test
    public void testIsContainerType() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        assertFalse(type.isContainerType());
    }

    @Test
    public void testToString() {
        SimpleType type = SimpleType.constructUnsafe(Integer.class);
        assertEquals("[simple type, class java.lang.Integer]", type.toString());
    }

    @Test
    public void testEquals() {
        SimpleType type1 = SimpleType.constructUnsafe(Short.class);
        SimpleType type2 = SimpleType.constructUnsafe(Short.class);
        assertEquals(type1, type2);
    }
}