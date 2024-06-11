import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertNotNull(simpleType);
    }

    @Test
    public void testConstructInvalidMap() {
        try {
            SimpleType.construct(Map.class);
            fail("Should throw IllegalArgumentException for constructing SimpleType with Map");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testConstructInvalidCollection() {
        try {
            SimpleType.construct(Collection.class);
            fail("Should throw IllegalArgumentException for constructing SimpleType with Collection");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testConstructInvalidArray() {
        try {
            SimpleType.construct(String[].class);
            fail("Should throw IllegalArgumentException for constructing SimpleType with Array");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testNarrow() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        SimpleType narrowedType = (SimpleType) simpleType._narrow(String.class);
        assertNotNull(narrowedType);
        assertNotEquals(simpleType, narrowedType);
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        try {
            simpleType.withContentType(String.class);
            fail("Should throw IllegalArgumentException for calling withContentType on SimpleType");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        try {
            simpleType.withContentTypeHandler(String.class);
            fail("Should throw IllegalArgumentException for calling withContentTypeHandler on SimpleType");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        try {
            simpleType.withContentValueHandler(String.class);
            fail("Should throw IllegalArgumentException for calling withContentValueHandler on SimpleType");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        SimpleType staticTyped = simpleType.withStaticTyping();
        assertNotEquals(simpleType, staticTyped);
        assertTrue(staticTyped.isStaticTyping());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        JavaType refinedType = simpleType.refine(Integer.class, TypeBindings.emptyBindings(), null, null);
        assertNull(refinedType);
    }

}