import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructor() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertNotNull(simpleType);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertNotNull(simpleType);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testConstruct() {
        try {
            SimpleType simpleType = SimpleType.construct(Map.class);
            fail("Expected IllegalArgumentException for constructing Map type");
        } catch (IllegalArgumentException e) {
            // expected exception
        }

        try {
            SimpleType simpleType = SimpleType.construct(Collection.class);
            fail("Expected IllegalArgumentException for constructing Collection type");
        } catch (IllegalArgumentException e) {
            // expected exception
        }

        try {
            SimpleType simpleType = SimpleType.construct(Object[].class);
            fail("Expected IllegalArgumentException for constructing array type");
        } catch (IllegalArgumentException e) {
            // expected exception
        }

        SimpleType simpleType = SimpleType.construct(String.class);
        assertNotNull(simpleType);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testWithContentType() {
        try {
            SimpleType simpleType = new SimpleType(String.class);
            simpleType.withContentType(String.class);
            fail("Expected IllegalArgumentException for calling withContentType() on SimpleType");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }

    @Test
    public void testWithTypeHandler() {
        Object typeHandler = new Object();
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType updatedType = simpleType.withTypeHandler(typeHandler);
        assertNotNull(updatedType);
        assertSame(updatedType.getTypeHandler(), typeHandler);
    }

    @Test
    public void testWithContentTypeHandler() {
        try {
            SimpleType simpleType = new SimpleType(String.class);
            simpleType.withContentTypeHandler(new Object());
            fail("Expected IllegalArgumentException for calling withContenTypeHandler() on SimpleType");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(String.class);
        assertFalse(simpleType.isStaticTyping());
        SimpleType updatedType = simpleType.withStaticTyping();
        assertNotNull(updatedType);
        assertTrue(updatedType.isStaticTyping());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(String.class);
        assertNull(simpleType.refine(Object.class, null, null, null));
    }

    @Test
    public void testEquals() {
        SimpleType type1 = new SimpleType(String.class);
        SimpleType type2 = new SimpleType(String.class);
        assertTrue(type1.equals(type2));

        SimpleType type3 = new SimpleType(Integer.class);
        assertFalse(type1.equals(type3));

        SimpleType type4 = new SimpleType(String.class);
        type4.withTypeHandler(new Object());
        assertFalse(type1.equals(type4));

        SimpleType type5 = new SimpleType(String.class);
        type5.withContentTypeHandler(new Object());
        assertFalse(type1.equals(type5));
    }

    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(String.class);
        assertEquals("[simple type, class java.lang.String]", simpleType.toString());
    }

}