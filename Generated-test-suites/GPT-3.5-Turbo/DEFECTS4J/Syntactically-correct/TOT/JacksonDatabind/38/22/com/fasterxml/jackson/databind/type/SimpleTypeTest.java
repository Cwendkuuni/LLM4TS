import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
    }

    @Test
    public void testConstruct() {
        SimpleType simpleType = SimpleType.construct(Integer.class);
        assertNotNull(simpleType);
        assertEquals(Integer.class, simpleType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        SimpleType.construct(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        SimpleType.construct(String[].class);
    }

    @Test
    public void testNarrow() {
        SimpleType simpleType = SimpleType.construct(Object.class);
        JavaType narrowedType = simpleType.narrow(String.class);
        assertNotNull(narrowedType);
        assertEquals(String.class, narrowedType.getRawClass());
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = SimpleType.construct(Object.class);
        try {
            simpleType.withContentType(String.class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = SimpleType.construct(Object.class);
        SimpleType withTypeHandler = simpleType.withTypeHandler(new Object());
        assertNotNull(withTypeHandler);
        assertNotSame(simpleType, withTypeHandler);
        assertSame(withTypeHandler.getTypeHandler(), new Object());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandler() {
        SimpleType simpleType = SimpleType.construct(Object.class);
        simpleType.withContentTypeHandler(new Object());
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType simpleType = SimpleType.construct(Object.class);
        try {
            simpleType.withContentValueHandler(new Object());
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentValueHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = SimpleType.construct(Object.class);
        SimpleType withStaticTyping = simpleType.withStaticTyping();
        assertNotNull(withStaticTyping);
        assertNotSame(simpleType, withStaticTyping);
        assertTrue(withStaticTyping.hasRawClass(Object.class));
        assertTrue(withStaticTyping.isStatic());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = SimpleType.construct(Object.class);
        JavaType refinedType = simpleType.refine(String.class, TypeBindings.emptyBindings(), null, null);
        assertNull(refinedType);
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = SimpleType.construct(Object.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = SimpleType.construct(Object.class);
        StringBuilder sb = simpleType.getErasedSignature(new StringBuilder());
        assertNotNull(sb);
        assertEquals("Ljava/lang/Object;", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = SimpleType.construct(Object.class);
        StringBuilder sb = simpleType.getGenericSignature(new StringBuilder());
        assertNotNull(sb);
        assertEquals("Ljava/lang/Object;", sb.toString());
    }

    @Test
    public void testToString() {
        SimpleType simpleType = SimpleType.construct(Object.class);
        String toString = simpleType.toString();
        assertEquals("[simple type, class java.lang.Object]", toString);
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = SimpleType.construct(Object.class);
        SimpleType simpleType2 = SimpleType.construct(Object.class);
        assertTrue(simpleType1.equals(simpleType2));
    }
}