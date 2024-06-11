
import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleTypeTest {
    
    @Test
    public void testConstructUnsafe() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        assertEquals(String.class, type.getRawClass());
        assertFalse(type.isContainerType());
        assertEquals("java.lang.String", type.getErasedSignature(new StringBuilder()).toString());
        assertEquals("[simple type, class java.lang.String]", type.toString());
        assertFalse(type.hasContentType());
        try {
            type.getContentType();
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call getContentType()", e.getMessage());
        }
    }
    
    @Test
    public void testConstruct() {
        SimpleType type = SimpleType.construct(Integer.class);
        assertEquals(Integer.class, type.getRawClass());
        assertFalse(type.isContainerType());
        assertEquals("java.lang.Integer", type.getErasedSignature(new StringBuilder()).toString());
        assertEquals("[simple type, class java.lang.Integer]", type.toString());
        assertFalse(type.hasContentType());
        try {
            type.getContentType();
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call getContentType()", e.getMessage());
        }
    }
    
    @Test
    public void testWithStaticTyping() {
        SimpleType type1 = SimpleType.constructUnsafe(String.class);
        assertFalse(type1.isStaticTyping());
        
        SimpleType type2 = type1.withStaticTyping();
        assertTrue(type2.isStaticTyping());
        assertEquals(type1.getRawClass(), type2.getRawClass());
        assertEquals(type1.getTypeHandler(), type2.getTypeHandler());
        assertEquals(type1.getValueHandler(), type2.getValueHandler());
        assertEquals(type1.getContentValueHandler(), type2.getContentValueHandler());
    }
    
    @Test
    public void testWithContentType() {
        SimpleType type = SimpleType.constructUnsafe(List.class);
        assertFalse(type.hasContentType());
        
        try {
            type.withContentType(SimpleType.constructUnsafe(String.class));
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
        
        assertFalse(type.hasContentType());
        try {
            type.getContentType();
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call getContentType()", e.getMessage());
        }
    }
    
    @Test
    public void testWithContentValueHandler() {
        SimpleType type = SimpleType.constructUnsafe(Set.class);
        assertFalse(type.hasContentType());
        
        try {
            type.withContentValueHandler(new Object());
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
        }
    }
    
    @Test
    public void testRefine() {
        SimpleType type1 = SimpleType.constructUnsafe(Map.class);
        assertNull(type1.refine(null, null, null, null));
    }
    
    @Test
    public void testEquals() {
        SimpleType type1 = SimpleType.constructUnsafe(String.class);
        SimpleType type2 = SimpleType.constructUnsafe(String.class);
        assertTrue(type1.equals(type2));
        assertTrue(type2.equals(type1));
        
        SimpleType type3 = SimpleType.constructUnsafe(Integer.class);
        assertFalse(type1.equals(type3));
        assertFalse(type3.equals(type1));
    }
}
