import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructor() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertNotNull(simpleType);
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
        assertNotNull(simpleType);
    }

    @Test
    public void testConstruct() {
        SimpleType simpleType = SimpleType.construct(Object.class);
        assertNotNull(simpleType);
    }

    @Test
    public void testNarrow() {
        SimpleType simpleType = new SimpleType(Object.class);
        SimpleType narrowedType = simpleType._narrow(String.class);
        assertNotEquals(simpleType, narrowedType);
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(Object.class);
        try {
            simpleType.withContentType(null);
            fail("Expected exception");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = new SimpleType(Object.class);
        SimpleType updatedType = simpleType.withTypeHandler(new Object());
        assertNotNull(updatedType);
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = new SimpleType(Object.class);
        try {
            simpleType.withContentTypeHandler(null);
            fail("Expected exception");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(Object.class);
        SimpleType staticType = simpleType.withStaticTyping();
        assertTrue(staticType.isAsStatic());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(Object.class);
        JavaType refinedType = simpleType.refine(String.class, null, null, null);
        assertNull(refinedType);
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        SimpleType simpleType = new SimpleType(Object.class);
        simpleType.getErasedSignature(sb);
        assertTrue(sb.length() > 0);
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        SimpleType simpleType = new SimpleType(Object.class);
        simpleType.getGenericSignature(sb);
        assertTrue(sb.length() > 0);
    }
}