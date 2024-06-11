import org.junit.*;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        assertNotNull(type);
        assertEquals(String.class, type.getRawClass());
    }
    
    @Test
    public void testConstruct() {
        SimpleType type = SimpleType.construct(Integer.class);
        assertNotNull(type);
        assertEquals(Integer.class, type.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructExceptionMap() {
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructExceptionCollection() {
        SimpleType.construct(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructExceptionArray() {
        SimpleType.construct(String[].class);
    }

    @Test
    public void testNarrow() {
        SimpleType type = SimpleType.construct(String.class);
        JavaType narrowedType = type._narrow(Object.class);
        assertNotNull(narrowedType);
        assertEquals(Object.class, narrowedType.getRawClass());
    }
    
    @Test
    public void testWithContentType() {
        SimpleType type = SimpleType.construct(String.class);
        try {
            type.withContentType(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }
    
    @Test
    public void testWithContentTypeHandler() {
        SimpleType type = SimpleType.construct(String.class);
        try {
            type.withContentTypeHandler(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
        }
    }
    
    @Test
    public void testWithValueHandler() {
        SimpleType type = SimpleType.construct(String.class);
        JavaType newValueType = type.withValueHandler("valueHandler");
        assertNotNull(newValueType);
        assertEquals("valueHandler", newValueType.getValueHandler());
    }
    
    @Test
    public void testWithContentValueHandler() {
        SimpleType type = SimpleType.construct(String.class);
        try {
            type.withContentValueHandler(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType type = SimpleType.construct(String.class);
        JavaType newType = type.withStaticTyping();
        assertNotNull(newType);
        assertTrue(newType.isStaticTyping());
    }

    @Test
    public void testRefine() {
        SimpleType type = SimpleType.construct(String.class);
        JavaType refinedType = type.refine(Object.class, null, null, null);
        assertNull(refinedType);
    }

    @Test
    public void testIsContainerType() {
        SimpleType type = SimpleType.construct(String.class);
        assertFalse(type.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType type = SimpleType.construct(String.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder signature = type.getErasedSignature(sb);
        assertNotNull(signature);
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType type = SimpleType.construct(String.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder signature = type.getGenericSignature(sb);
        assertNotNull(signature);
    }

    @Test
    public void testToString() {
        SimpleType type = SimpleType.construct(String.class);
        String str = type.toString();
        assertNotNull(str);
        assertTrue(str.contains("SimpleType"));
        assertTrue(str.contains("class"));
        assertTrue(str.contains("java.lang.String"));
    }

    @Test
    public void testEquals() {
        SimpleType type1 = SimpleType.construct(String.class);
        SimpleType type2 = SimpleType.construct(String.class);
        assertTrue(type1.equals(type2));
    }
}