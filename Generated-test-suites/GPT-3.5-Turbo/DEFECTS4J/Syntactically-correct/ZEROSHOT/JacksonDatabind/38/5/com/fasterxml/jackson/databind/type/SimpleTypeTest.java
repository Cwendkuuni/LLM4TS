import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleTypeTest {
    @Test
    public void testConstructUnsafe() {
        SimpleType type = SimpleType.constructUnsafe(Integer.class);
        
        assertEquals(Integer.class, type.getRawClass());
        assertFalse(type.isContainerType());
        assertNull(type.getContentType());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithMap() {
        SimpleType.constructUnsafe(Map.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithCollection() {
        SimpleType.constructUnsafe(Collection.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithArray() {
        SimpleType.constructUnsafe(int[].class);
    }
    
    @Test
    public void testConstruct() {
        SimpleType type = SimpleType.construct(Integer.class);
        
        assertEquals(Integer.class, type.getRawClass());
        assertFalse(type.isContainerType());
        assertNull(type.getContentType());
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
    public void testNarrow() {
        SimpleType baseType = SimpleType.constructUnsafe(Number.class);
        SimpleType subType = (SimpleType) baseType._narrow(Integer.class);
        
        assertEquals(Integer.class, subType.getRawClass());
        assertFalse(subType.isContainerType());
        assertNull(subType.getContentType());
    }
    
    @Test
    public void testWithContentType() {
        SimpleType type = SimpleType.constructUnsafe(Collection.class);
        
        try {
            type.withContentType(Integer.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }
    
    @Test
    public void testWithContentTypeHandler() {
        SimpleType type = SimpleType.constructUnsafe(Integer.class);
        
        try {
            type.withContentTypeHandler(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
        }
    }
    
    @Test
    public void testWithValueHandler() {
        SimpleType type = SimpleType.constructUnsafe(Integer.class);
        Object valueHandler = new Object();
        
        SimpleType newType = type.withValueHandler(valueHandler);
        
        assertEquals(Integer.class, newType.getRawClass());
        assertEquals(valueHandler, newType.getValueHandler());
    }
    
    @Test
    public void testWithContentValueHandler() {
        SimpleType type = SimpleType.constructUnsafe(Integer.class);
        
        try {
            type.withContentValueHandler(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
        }
    }
    
    @Test
    public void testWithStaticTyping() {
        SimpleType type = SimpleType.constructUnsafe(Integer.class);
        
        SimpleType newType = type.withStaticTyping();
        
        assertEquals(Integer.class, newType.getRawClass());
        assertTrue(newType.isStatic());
    }
    
    @Test
    public void testRefine() {
        SimpleType type = SimpleType.constructUnsafe(Number.class);
        
        JavaType newType = type.refine(Integer.class, TypeBindings.emptyBindings(), null, null);
        
        assertNull(newType);
    }
    
    @Test
    public void testIsContainerType() {
        SimpleType type = SimpleType.constructUnsafe(Integer.class);
        
        assertFalse(type.isContainerType());
    }
    
    @Test
    public void testGetErasedSignature() {
        SimpleType type = SimpleType.constructUnsafe(Integer.class);
        
        StringBuilder sb = type.getErasedSignature(new StringBuilder());
        
        assertEquals("L", sb.toString());
    }
    
    @Test
    public void testGetGenericSignature() {
        SimpleType type = SimpleType.constructUnsafe(List.class);
        
        StringBuilder sb = type.getGenericSignature(new StringBuilder());
        
        assertEquals("Ljava/util/List<*>;", sb.toString());
    }
    
    @Test
    public void testToString() {
        SimpleType type = SimpleType.constructUnsafe(Number.class);
        
        String str = type.toString();
        
        assertEquals("[simple type, class java.lang.Number]", str);
    }
    
    @Test
    public void testEquals() {
        SimpleType type1 = SimpleType.constructUnsafe(Number.class);
        SimpleType type2 = SimpleType.constructUnsafe(Number.class);
        
        assertTrue(type1.equals(type2));
    }
}