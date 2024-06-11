import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertNotNull(simpleType);
    }

    @Test
    public void testConstruct() {
        SimpleType simpleType = SimpleType.construct(Integer.class);
        assertNotNull(simpleType);
    }

    @Test
    public void testNarrow() {
        SimpleType baseSimpleType = new SimpleType(String.class);
        SimpleType narrowedSimpleType = (SimpleType) baseSimpleType._narrow(String.class);
        assertEquals(baseSimpleType, narrowedSimpleType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructMap() {
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructCollection() {
        SimpleType.construct(List.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructArray() {
        SimpleType.construct(String[].class);
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(String.class);
        assertThrows(IllegalArgumentException.class, () -> simpleType.withContentType(new SimpleType(Integer.class)));
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        Object typeHandler = new Object();
        SimpleType newSimpleType = simpleType.withTypeHandler(typeHandler);
        assertEquals(typeHandler, newSimpleType.getTypeHandler());
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType newSimpleType = simpleType.withStaticTyping();
        assertTrue(newSimpleType.isAsStatic());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(String.class);
        JavaType refinedType = simpleType.refine(String.class, new TypeBindings(), null, null);
        assertNull(refinedType);
    }

}