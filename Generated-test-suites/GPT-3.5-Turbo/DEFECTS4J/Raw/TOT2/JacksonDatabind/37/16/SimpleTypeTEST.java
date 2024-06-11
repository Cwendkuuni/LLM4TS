import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    private SimpleType simpleType;

    @Before
    public void setUp() {
        simpleType = new SimpleType(String.class);
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType constructedType = SimpleType.constructUnsafe(String.class);
        assertNotNull(constructedType);
        assertEquals(String.class, constructedType.getRawClass());
    }

    @Test
    public void testConstruct() {
        try {
            SimpleType.construct(Map.class);
            fail("Exception not thrown for constructing SimpleType for Map");
        } catch (IllegalArgumentException e) {
            // Expected exception, test passed
        }

        try {
            SimpleType.construct(Collection.class);
            fail("Exception not thrown for constructing SimpleType for Collection");
        } catch (IllegalArgumentException e) {
            // Expected exception, test passed
        }

        try {
            SimpleType.construct(String[].class);
            fail("Exception not thrown for constructing SimpleType for array");
        } catch (IllegalArgumentException e) {
            // Expected exception, test passed
        }

        SimpleType constructedType = SimpleType.construct(String.class);
        assertNotNull(constructedType);
        assertEquals(String.class, constructedType.getRawClass());
    }

    @Test
    public void testNarrow() {
        SimpleType subclassType = (SimpleType) simpleType._narrow(Object.class);
        assertEquals(Object.class, subclassType.getRawClass());
    }

    @Test
    public void testRefine() {
        JavaType refinedType = simpleType.refine(String.class, TypeBindings.emptyBindings(), null, null);
        assertNull(refinedType);
    }

    @Test
    public void testIsContainerType() {
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder signature = simpleType.getErasedSignature(sb);
        assertNotNull(signature.toString());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder signature = simpleType.getGenericSignature(sb);
        assertNotNull(signature.toString());
    }

    @Test
    public void testEquals() {
        SimpleType equalType = new SimpleType(String.class);
        SimpleType notEqualType = new SimpleType(Object.class);

        assertTrue(simpleType.equals(equalType));
        assertFalse(simpleType.equals(notEqualType));
    }
}