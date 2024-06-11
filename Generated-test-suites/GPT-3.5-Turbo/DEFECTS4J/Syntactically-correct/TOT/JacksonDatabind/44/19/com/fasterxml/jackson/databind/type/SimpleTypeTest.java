import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleTypeTest {
    
    @Test
    public void testConstructUnsafe() {
        SimpleType type = SimpleType.constructUnsafe(Object.class);
        
        assertNotNull(type);
        assertEquals(Object.class, type.getRawClass());
    }
    
    @Test
    public void testWithContentType() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        JavaType contentType = SimpleType.constructUnsafe(Integer.class);
        
        try {
            type.withContentType(contentType);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }
    
    @Test
    public void testWithContentTypeHandler() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        
        try {
            type.withContentTypeHandler(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
        }
    }
    
    @Test
    public void testWithValueHandler() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        
        Object valueHandler = new Object();
        SimpleType newType = type.withValueHandler(valueHandler);
        
        assertNotSame(type, newType);
        assertSame(valueHandler, newType.getValueHandler());
    }
    
    @Test
    public void testWithContentValueHandler() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        
        try {
            type.withContentValueHandler(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
        }
    }
    
    @Test
    public void testWithStaticTyping() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        
        assertFalse(type.isStaticTyping());
        
        SimpleType newType = type.withStaticTyping();
        
        assertNotSame(type, newType);
        assertTrue(newType.isStaticTyping());
    }
    
    @Test
    public void testRefine() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        JavaType refinedType = type.refine(Object.class, null, null, null);
        
        assertNull(refinedType);
    }
    
    @Test
    public void testIsContainerType() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        
        assertFalse(type.isContainerType());
    }
    
    @Test
    public void testGetErasedSignature() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        StringBuilder sb = new StringBuilder();
        
        StringBuilder signature = type.getErasedSignature(sb);
        
        assertSame(sb, signature);
        assertEquals("Ljava/lang/String;", signature.toString());
    }
    
    @Test
    public void testGetGenericSignature() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        StringBuilder sb = new StringBuilder();
        
        StringBuilder signature = type.getGenericSignature(sb);
        
        assertSame(sb, signature);
        assertEquals("Ljava/lang/String;;", signature.toString());
    }
    
    @Test
    public void testToString() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        
        assertEquals("[simple type, class java.lang.String]", type.toString());
    }
    
    @Test
    public void testEqualAndHashCode() {
        SimpleType type1 = SimpleType.constructUnsafe(String.class);
        SimpleType type2 = SimpleType.constructUnsafe(String.class);
        SimpleType type3 = SimpleType.constructUnsafe(Integer.class);
        
        assertEquals(type1, type2);
        assertEquals(type1.hashCode(), type2.hashCode());
        assertNotEquals(type1, type3);
        assertNotEquals(type1.hashCode(), type3.hashCode());
    }
}