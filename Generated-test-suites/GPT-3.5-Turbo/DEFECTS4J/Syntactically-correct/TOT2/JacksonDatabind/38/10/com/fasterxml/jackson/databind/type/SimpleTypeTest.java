import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructorEmptyBindings() {
        SimpleType simpleType = new SimpleType(Integer.class);
        assertNotNull(simpleType);
        assertEquals(Integer.class, simpleType.getRawClass());
    }

    @Test
    public void testConstructorWithBindings() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(Long.class);
        assertNotNull(simpleType);
        assertEquals(Long.class, simpleType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructMap() {
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructCollection() {
        SimpleType.construct(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructArray() {
        SimpleType.construct(String[].class);
    }

    @Test
    public void testNarrow() {
        SimpleType simpleType = new SimpleType(Integer.class);
        assertEquals(simpleType, simpleType._narrow(Integer.class));
    }

    // Additional test cases for content type handling and value handler setting can be added here

    @Test
    public void testStaticTyping() {
        SimpleType simpleType = new SimpleType(Integer.class);
        assertFalse(simpleType.isStaticTyping());
        SimpleType staticSimpleType = simpleType.withStaticTyping();
        assertTrue(staticSimpleType.isStaticTyping());
    }

    // More test cases for internal methods and standard methods can be added here

}