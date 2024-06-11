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
        SimpleType constructedType = SimpleType.constructUnsafe(Integer.class);
        assertEquals(Integer.class, constructedType.getRawClass());
    }

    @Test
    public void testConstruct() {
        SimpleType constructedType = SimpleType.construct(Boolean.class);
        assertEquals(Boolean.class, constructedType.getRawClass());
    }

    @Test
    public void testNarrow() {
        SimpleType narrowedType = simpleType._narrow(Object.class);
        assertEquals(Object.class, narrowedType.getRawClass());
    }

    @Test
    public void testWithContentType() {
        try {
            simpleType.withContentType(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType typeWithHandler = simpleType.withTypeHandler(new Object());
        assertNotNull(typeWithHandler);
        assertNotEquals(simpleType, typeWithHandler);
    }

    @Test
    public void testWithContentTypeHandler() {
        try {
            simpleType.withContentTypeHandler(new Object());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithValueHandler() {
        SimpleType typeWithValueHandler = simpleType.withValueHandler(new Object());
        assertNotNull(typeWithValueHandler);
        assertNotEquals(simpleType, typeWithValueHandler);
    }

    @Test
    public void testWithContentValueHandler() {
        try {
            simpleType.withContentValueHandler(new Object());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType typeWithStaticTyping = simpleType.withStaticTyping();
        assertNotNull(typeWithStaticTyping);
        assertNotEquals(simpleType, typeWithStaticTyping);
    }

    @Test
    public void testRefine() {
        JavaType refinedType = simpleType.refine(Object.class, TypeBindings.emptyBindings(), null, null);
        assertNull(refinedType);
    }

    @Test
    public void testIsContainerType() {
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder erasedSignature = simpleType.getErasedSignature(sb);
        assertNotNull(erasedSignature);
        assertFalse(sb.toString().isEmpty());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder genericSignature = simpleType.getGenericSignature(sb);
        assertNotNull(genericSignature);
        assertFalse(sb.toString().isEmpty());
    }

    @Test
    public void testToString() {
        String typeString = simpleType.toString();
        assertNotNull(typeString);
        assertFalse(typeString.isEmpty());
    }

    @Test
    public void testEquals() {
        SimpleType sameType = new SimpleType(String.class);
        assertEquals(simpleType, sameType);

        SimpleType differentType = new SimpleType(Integer.class);
        assertNotEquals(simpleType, differentType);
    }
}