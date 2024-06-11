import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {
    
    @Test
    public void testConstructUnsafe() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        assertEquals(String.class, type.getRawClass());
        assertFalse(type.isContainerType());
    }
    
    @Test
    public void testConstruct() {
        SimpleType type = SimpleType.construct(Integer.class);
        assertEquals(Integer.class, type.getRawClass());
        assertFalse(type.isContainerType());
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
        SimpleType.construct(int[].class);
    }
    
    @Test
    public void testWithContentType() {
        SimpleType type = SimpleType.constructUnsafe(List.class);
        try {
            type.withContentType(SimpleType.constructUnsafe(String.class));
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }
    
    @Test
    public void testWithTypeHandler() {
        SimpleType type = SimpleType.constructUnsafe(Integer.class);
        Object handler = new Object();
        SimpleType newType = type.withTypeHandler(handler);
        assertEquals(handler, newType.getTypeHandler());
        assertEquals(type.getValueHandler(), newType.getValueHandler());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandler() {
        SimpleType type = SimpleType.constructUnsafe(List.class);
        type.withContentTypeHandler(new Object());
    }
    
    @Test
    public void testWithStaticTyping() {
        SimpleType type = SimpleType.constructUnsafe(Double.class);
        SimpleType newType = type.withStaticTyping();
        assertTrue(newType.isStatic());
    }
    
    @Test
    public void testRefine() {
        SimpleType type = SimpleType.constructUnsafe(Number.class);
        JavaType refined = type.refine(Integer.class, TypeBindings.emptyBindings(), null, null);
        assertNull(refined);
    }
    
    @Test
    public void testGetErasedSignature() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = type.getErasedSignature(sb);
        assertEquals("Lclass:java.lang.String;", result.toString());
    }
    
    @Test
    public void testGetGenericSignature() {
        SimpleType type = SimpleType.constructUnsafe(List.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = type.getGenericSignature(sb);
        assertEquals("Lclass:java.util.List<>;", result.toString());
    }
    
    @Test
    public void testIsContainerType() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        assertFalse(type.isContainerType());
    }
    
    @Test
    public void testToString() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        assertEquals("[simple type, class class:java.lang.String;]", type.toString());
    }
    
    @Test
    public void testEquals() {
        SimpleType type1 = SimpleType.constructUnsafe(String.class);
        SimpleType type2 = SimpleType.constructUnsafe(String.class);
        assertTrue(type1.equals(type2));
    }
}