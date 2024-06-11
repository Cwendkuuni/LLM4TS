import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertNotNull(simpleType);
    }

    @Test
    public void testConstructException() {
        try {
            SimpleType.construct(Map.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testNarrow() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType narrowedType = (SimpleType) simpleType._narrow(Object.class);
        assertNotEquals(simpleType, narrowedType);
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(String.class);
        JavaType refinedType = simpleType.refine(String.class, TypeBindings.emptyBindings(), null, null);
        assertNull(refinedType);
    }

    @Test
    public void testConstructor() {
        TypeBase base = new TypeBase() {
            // For testing purposes
        };
        SimpleType simpleType = new SimpleType(base);
        assertNotNull(simpleType);
    }

    @Test
    public void testBuildCanonialName() {
        SimpleType simpleType = new SimpleType(String.class);
        String canonicalName = simpleType.buildCanonicalName();
        assertEquals("java.lang.String", canonicalName);
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(String.class);
        assertEquals(simpleType1, simpleType2);
    }

}