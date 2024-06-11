import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
        assertEquals(Object.class, simpleType.getClass());
    }

    @Test
    public void testConstruct() {
        try {
            SimpleType.construct(Map.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Map (class: java.util.Map)", e.getMessage());
        }

        try {
            SimpleType.construct(Collection.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Collection (class: java.util.Collection)", e.getMessage());
        }

        try {
            SimpleType.construct(Object[].class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for an array (class: [Ljava.lang.Object;)", e.getMessage());
        }

        SimpleType simpleType = SimpleType.construct(Object.class);
        assertEquals(Object.class, simpleType.getClass());
    }

    @Test
    public void testNarrow() {
        SimpleType simpleType = new SimpleType(Object.class);
        JavaType narrowedType = simpleType._narrow(Object.class);
        assertEquals(Object.class, narrowedType.getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(Object.class);
        simpleType.withContentType(null);
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = new SimpleType(Object.class);
        Object typeHandler = new Object();
        SimpleType newSimpleType = simpleType.withTypeHandler(typeHandler);
        assertEquals(Object.class, newSimpleType.getClass());
        assertEquals(typeHandler, newSimpleType._typeHandler);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandler() {
        SimpleType simpleType = new SimpleType(Object.class);
        simpleType.withContentTypeHandler(null);
    }

    @Test
    public void testWithValueHandler() {
        SimpleType simpleType = new SimpleType(Object.class);
        Object valueHandler = new Object();
        SimpleType newSimpleType = simpleType.withValueHandler(valueHandler);
        assertEquals(Object.class, newSimpleType.getClass());
        assertEquals(valueHandler, newSimpleType._valueHandler);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandler() {
        SimpleType simpleType = new SimpleType(Object.class);
        simpleType.withContentValueHandler(null);
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(Object.class);
        SimpleType newSimpleType = simpleType.withStaticTyping();
        assertEquals(Object.class, newSimpleType.getClass());
        assertTrue(newSimpleType._asStatic);
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertNull(simpleType.refine(Object.class, null, null, null));
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(Object.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder newSb = simpleType.getErasedSignature(sb);
        assertEquals(sb, newSb);
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(Object.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder newSb = simpleType.getGenericSignature(sb);
        assertEquals(sb, newSb);
    }

    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertEquals("[simple type, class java.lang.Object]", simpleType.toString());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertTrue(simpleType.equals(simpleType));
        assertFalse(simpleType.equals(null));
        assertFalse(simpleType.equals(new SimpleTypeTest()));
        assertFalse(simpleType.equals(new SimpleType(String.class)));
        assertFalse(simpleType.equals(new SimpleType(Object.class, TypeBindings.emptyBindings(), null, null, null, null, false)));
        assertTrue(simpleType.equals(new SimpleType(Object.class, TypeBindings.emptyBindings(), null, null, null, null, false)));
    }
}