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
        SimpleType unsafeType = SimpleType.constructUnsafe(Number.class);
        assertNotNull(unsafeType);
        assertEquals(Number.class, unsafeType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructSimpleTypeForMap() {
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructSimpleTypeForCollection() {
        SimpleType.construct(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructSimpleTypeForArray() {
        SimpleType.construct(Object[].class);
    }

    @Test
    public void testNarrow() {
        SimpleType narrowedType = simpleType.narrow(CharSequence.class);
        assertNotNull(narrowedType);
        assertEquals(CharSequence.class, narrowedType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithInvalidSubclass() {
        simpleType.narrow(Integer.class);
    }

    @Test
    public void testWithContentType() {
        try {
            simpleType.withContentType(SimpleType.constructUnsafe(Integer.class));
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }

    @Test
    public void testWithTypeHandler() {
        Object handler = new Object();
        SimpleType typeWithHandler = simpleType.withTypeHandler(handler);
        assertNotNull(typeWithHandler);
        assertEquals(handler, typeWithHandler.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        try {
            simpleType.withContentTypeHandler(new Object());
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithValueHandler() {
        Object handler = new Object();
        SimpleType typeWithValueHandler = simpleType.withValueHandler(handler);
        assertNotNull(typeWithValueHandler);
        assertEquals(handler, typeWithValueHandler.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        try {
            simpleType.withContentValueHandler(new Object());
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType staticType = simpleType.withStaticTyping();
        assertTrue(staticType.isStatic());
    }

    @Test
    public void testRefine() {
        SimpleType refinedType = simpleType.refine(CharSequence.class, null, null, null);
        assertNull(refinedType);
    }

    @Test
    public void testIsContainerType() {
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);
        assertEquals("Ljava/lang/String;", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        assertEquals("Ljava/lang/String;", sb.toString());
    }

    @Test
    public void testToString() {
        String expected = "[simple type, class java.lang.String]";
        String actual = simpleType.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        SimpleType equalType = new SimpleType(String.class);
        SimpleType differentType = new SimpleType(Integer.class);

        assertTrue(simpleType.equals(equalType));
        assertFalse(simpleType.equals(differentType));
    }
}