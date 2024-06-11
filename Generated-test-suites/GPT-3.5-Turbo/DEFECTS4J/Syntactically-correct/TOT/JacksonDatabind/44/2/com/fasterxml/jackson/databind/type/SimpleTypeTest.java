
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructSafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertEquals(String.class, simpleType.getRawClass());
        assertFalse(simpleType.hasContentType());
        assertFalse(simpleType.hasGenericTypes());
        assertNull(simpleType.getGenericSignature());
    }

    @Test
    public void testConstructSafeWithArray() {
        try {
            SimpleType.constructUnsafe(String[].class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for an array (class: [Ljava.lang.String;)", e.getMessage());
        }
    }

    @Test
    public void testConstructSafeWithCollection() {
        try {
            SimpleType.constructUnsafe(List.class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Collection (class: java.util.List)", e.getMessage());
        }
    }

    @Test
    public void testConstructSafeWithMap() {
        try {
            SimpleType.constructUnsafe(Map.class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Map (class: java.util.Map)", e.getMessage());
        }
    }

    @Test
    public void testConstructSafeWithSubClass() {
        SimpleType simpleType = SimpleType.constructUnsafe(SubClass.class);
        assertEquals(SubClass.class, simpleType.getRawClass());
        assertFalse(simpleType.hasContentType());
        assertFalse(simpleType.hasGenericTypes());
        assertNull(simpleType.getGenericSignature());
    }

    @Test
    public void testConstructSafeWithInterface() {
        SimpleType simpleType = SimpleType.constructUnsafe(Interface.class);
        assertEquals(Interface.class, simpleType.getRawClass());
        assertFalse(simpleType.hasContentType());
        assertFalse(simpleType.hasGenericTypes());
        assertNull(simpleType.getGenericSignature());
    }

    @Test
    public void testConstructSafeWithGenericSuperClass() {
        SimpleType simpleType = SimpleType.constructUnsafe(GenericSubClass.class);
        assertEquals(GenericSubClass.class, simpleType.getRawClass());
        assertFalse(simpleType.hasContentType());
        assertTrue(simpleType.hasGenericTypes());
        assertEquals("com.fasterxml.jackson.databind.type.SimpleTypeTest.GenericType<java.lang.String>", simpleType.getGenericSignature());
    }

    @Test
    public void testConstructSafeWithGenericSuperInterface() {
        SimpleType simpleType = SimpleType.constructUnsafe(GenericInterface.class);
        assertEquals(GenericInterface.class, simpleType.getRawClass());
        assertFalse(simpleType.hasContentType());
        assertTrue(simpleType.hasGenericTypes());
        assertEquals("com.fasterxml.jackson.databind.type.SimpleTypeTest.GenericType<java.lang.Integer>", simpleType.getGenericSignature());
    }

    // Helper classes
    private static class SubClass extends BaseClass {}

    private interface Interface {}

    private static class BaseClass {}

    private static class GenericSubClass extends GenericType<String> {}

    private static interface GenericInterface extends GenericType<Integer> {}

    private static class GenericType<T> {}
}
