
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertEquals(String.class, simpleType.getRawClass());
    }

    @Test
    public void testConstruct_map() {
        try {
            SimpleType.construct(Map.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }

    @Test
    public void testConstruct_collection() {
        try {
            SimpleType.construct(Collection.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }

    @Test
    public void testConstruct_array() {
        try {
            SimpleType.construct(String[].class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }

    public void testWithContentType() {
        SimpleType simpleType = SimpleType.constructUnsafe(List.class);
        try {
            simpleType.withContentType(SimpleType.constructUnsafe(Integer.class));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        try {
            simpleType.withContentTypeHandler("handler");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        try {
            simpleType.withContentValueHandler("handler");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        SimpleType withStaticTyping = simpleType.withStaticTyping();
        assertTrue(withStaticTyping.isStatic());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        JavaType refinedType = simpleType.refine(Object.class, TypeBindings.emptyBindings(), null, null);
        assertNull(refinedType);
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);
        assertEquals("Lcom/fasterxml/jackson/databind/type/SimpleType;", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        assertEquals("Lcom/fasterxml/jackson/databind/type/SimpleType;L;", sb.toString());
    }

    @Test
    public void testToString() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        String expected = "[simple type, class java.lang.String]";
        assertEquals(expected, simpleType.toString());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = SimpleType.constructUnsafe(String.class);
        SimpleType simpleType2 = SimpleType.constructUnsafe(String.class);
        assertTrue(simpleType1.equals(simpleType2));
    }
}
