import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    private SimpleType simpleType;

    @Before
    public void setup() {
        simpleType = new SimpleType(Object.class);
    }

    @Test
    public void testConstructor() {
        assertNotNull(simpleType);
        assertEquals(Object.class, simpleType.getRawClass());
        assertEquals(0, simpleType.getBindings().size());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStaticTyping());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithMap() {
        SimpleType.constructUnsafe(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithCollection() {
        SimpleType.constructUnsafe(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithArray() {
        SimpleType.constructUnsafe(String[].class);
    }

    @Test
    public void testConstructWithMap() {
        try {
            SimpleType.construct(Map.class);
            fail("IllegalArgumentException not thrown for Map class");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Map (class: java.util.Map)", e.getMessage());
        }
    }

    @Test
    public void testConstructWithCollection() {
        try {
            SimpleType.construct(Collection.class);
        fail("IllegalArgumentException not thrown for Collection class");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Collection (class: java.util.Collection)", e.getMessage());
        }
    }

    @Test
    public void testConstructWithArray() {
        try {
            SimpleType.construct(String[].class);
            fail("IllegalArgumentException not thrown for array");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for an array (class: [Ljava.lang.String;)", e.getMessage());
        }
    }

    @Test
    public void testToString() {
        assertEquals("[simple type, class java.lang.Object]", simpleType.toString());
    }

    @Test
    public void testEquals() {
        SimpleType sameSimpleType = new SimpleType(Object.class);
        SimpleType differentSimpleType = new SimpleType(Integer.class);

        assertTrue(simpleType.equals(sameSimpleType));
        assertFalse(simpleType.equals(differentSimpleType));
    }

}