import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
    }

    @Test
    public void testConstructMap() {
        assertThrows(IllegalArgumentException.class, () -> SimpleType.construct(Map.class));
    }

    @Test
    public void testConstructCollection() {
        assertThrows(IllegalArgumentException.class, () -> SimpleType.construct(Collection.class));
    }

    @Test
    public void testConstructArray() {
        assertThrows(IllegalArgumentException.class, () -> SimpleType.construct(String[].class));
    }

    @Test
    public void testWithTypeHandler() {
        Object typeHandler = new Object();
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType updatedType = simpleType.withTypeHandler(typeHandler);
        assertNotNull(updatedType);
        assertEquals(typeHandler, updatedType.getTypeHandler());
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType updatedType = simpleType.withStaticTyping();
        assertNotNull(updatedType);
        assertTrue(updatedType.isStaticTyping());
    }

    @Test
    public void testWithInvalidContentType() {
        SimpleType simpleType = new SimpleType(String.class);
        assertThrows(IllegalArgumentException.class, () -> simpleType.withContentType(String.class));
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType simpleType = new SimpleType(String.class);
        String canonicalName = simpleType.buildCanonicalName();
        assertNotNull(canonicalName);
        assertEquals("java.lang.String", canonicalName);
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(String.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(String.class);
        String str = simpleType.toString();
        assertNotNull(str);
        assertTrue(str.contains("simple type"));
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(String.class);
        assertEquals(simpleType1, simpleType2);
    }
}