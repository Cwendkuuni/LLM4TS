import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {
    
    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertEquals(String.class, simpleType.getRawClass());
        assertFalse(simpleType.isContainerType());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        SimpleType.construct(Map.class);;
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
    public void testWithContentType() {
        SimpleType simpleType = SimpleType.constructUnsafe(List.class);
        assertThrows(IllegalArgumentException.class, () -> simpleType.withContentType(String.class));
    }
    
    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(Integer.class);
        assertThrows(IllegalArgumentException.class, () -> simpleType.withContentTypeHandler(null));
    }
    
    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = SimpleType.constructUnsafe(Integer.class);
        assertTrue(simpleType.withStaticTyping().isStatic());
    }
    
    @Test
    public void testRefine() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertNull(simpleType.refine(Object.class, TypeBindings.emptyBindings(), null, null));
    }
    
    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = SimpleType.constructUnsafe(Integer.class);
        assertEquals("Ljava/lang/Integer;", simpleType.getErasedSignature(new StringBuilder()).toString());
    }
    
    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = SimpleType.constructUnsafe(Long.class);
        assertEquals("Ljava/lang/Long;", simpleType.getGenericSignature(new StringBuilder()).toString());
    }
    
    @Test
    public void testToString() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertEquals("[simple type, class java.lang.String]", simpleType.toString());
    }
    
    @Test
    public void testEquals() {
        SimpleType simpleType1 = SimpleType.constructUnsafe(Integer.class);
        SimpleType simpleType2 = SimpleType.constructUnsafe(Integer.class);
        assertEquals(simpleType1, simpleType2);
        assertEquals(simpleType1.hashCode(), simpleType2.hashCode());
    }
}