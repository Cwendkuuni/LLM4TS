
import org.junit.Test;
import static org.junit.Assert.*;

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
        SimpleType.construct(Object[].class);
    }
    
    @Test
    public void testWithContentType() {
        SimpleType simpleType = SimpleType.constructUnsafe(List.class);
        try {
            simpleType.withContentType(Integer.class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }
    
    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        Object typeHandler = new Object();
        SimpleType modifiedType = simpleType.withTypeHandler(typeHandler);
        assertNotSame(simpleType, modifiedType);
        assertSame(typeHandler, modifiedType.getTypeHandler());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(Set.class);
        simpleType.withContentTypeHandler(new Object());
    }
    
    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertFalse(simpleType.isStaticTyping());
        SimpleType modifiedType = simpleType.withStaticTyping();
        assertTrue(modifiedType.isStaticTyping());
    }
    
    @Test
    public void testRefine() {
        SimpleType simpleType = SimpleType.constructUnsafe(ArrayList.class);
        JavaType refinedType = simpleType.refine(List.class, TypeBindings.emptyBindings(), null, null);
        assertNull(refinedType);
    }
    
    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        StringBuilder sb = simpleType.getErasedSignature(new StringBuilder());
        assertEquals("java.lang.String", sb.toString());
    }
    
    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = SimpleType.constructUnsafe(Map.class);
        StringBuilder sb = simpleType.getGenericSignature(new StringBuilder());
        assertEquals("java.util.Map<?, ?>;", sb.toString());
    }
    
    @Test
    public void testIsContainerType() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertFalse(simpleType.isContainerType());
    }
    
    @Test
    public void testToString() {
        SimpleType simpleType = SimpleType.constructUnsafe(Integer.class);
        assertEquals("[simple type, class java.lang.Integer]", simpleType.toString());
    }
    
    @Test
    public void testEquals() {
        SimpleType simpleType1 = SimpleType.constructUnsafe(String.class);
        SimpleType simpleType2 = SimpleType.constructUnsafe(String.class);
        SimpleType simpleType3 = SimpleType.constructUnsafe(Integer.class);
        
        assertTrue(simpleType1.equals(simpleType2));
        assertTrue(simpleType2.equals(simpleType1));
        
        assertFalse(simpleType1.equals(simpleType3));
        assertFalse(simpleType3.equals(simpleType1));
    }
}
