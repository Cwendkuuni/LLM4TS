
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {
    
    @Test
    public void testConstructUnsafe() {
        // Test constructUnsafe method
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
    }
    
    @Test
    public void testConstruct() {
        // Test construct method
        SimpleType simpleType = SimpleType.construct(Long.class);
        assertNotNull(simpleType);
        assertEquals(Long.class, simpleType.getRawClass());
    }
    
    @Test
    public void testWithContentType() {
        // Test withContentType method
        SimpleType simpleType = SimpleType.constructUnsafe(List.class);
        assertThrows(IllegalArgumentException.class, () -> simpleType.withContentType(String.class));
    }
    
    @Test
    public void testWithContentTypeHandler() {
        // Test withContentTypeHandler method
        SimpleType simpleType = SimpleType.constructUnsafe(Set.class);
        assertThrows(IllegalArgumentException.class, () -> simpleType.withContentTypeHandler(null));
    }
    
    @Test
    public void testWithValueHandler() {
        // Test withValueHandler method
        SimpleType simpleType = SimpleType.constructUnsafe(Date.class);
        assertThrows(IllegalArgumentException.class, () -> simpleType.withValueHandler(null));
    }
    
    @Test
    public void testWithContentValueHandler() {
        // Test withContentValueHandler method
        SimpleType simpleType = SimpleType.construct(Set.class);
        assertThrows(IllegalArgumentException.class, () -> simpleType.withContentValueHandler(null));
    }
    
    @Test
    public void testWithStaticTyping() {
        // Test withStaticTyping method
        SimpleType simpleType = SimpleType.constructUnsafe(Integer.class);
        assertTrue(simpleType.withStaticTyping().isStatic());
    }
    
    @Test
    public void testRefine() {
        // Test refine method
        SimpleType simpleType = SimpleType.constructUnsafe(Collection.class);
        assertNull(simpleType.refine(List.class, null, null, null));
    }
    
    @Test
    public void testIsContainerType() {
        SimpleType simpleType = SimpleType.constructUnsafe(StringBuilder.class);
        assertFalse(simpleType.isContainerType());
    }
    
    @Test
    public void testGetErasedSignature() {
        // Test getErasedSignature method
        SimpleType simpleType = SimpleType.constructUnsafe(Integer.class);
        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);
        assertEquals("I", sb.toString());
    }
    
    @Test
    public void testGetGenericSignature() {
        // Test getGenericSignature method
        SimpleType simpleType = SimpleType.constructUnsafe(ArrayList.class);
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        assertEquals("Ljava/util/ArrayList<*>;", sb.toString());
    }
    
    @Test
    public void testToString() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertEquals("[simple type, class java.lang.String]", simpleType.toString());
    }
    
    @Test
    public void testEquals() {
        SimpleType simpleType1 = SimpleType.constructUnsafe(HashSet.class);
        SimpleType simpleType2 = SimpleType.constructUnsafe(HashSet.class);
        SimpleType simpleType3 = SimpleType.constructUnsafe(TreeSet.class);
        assertTrue(simpleType1.equals(simpleType2));
        assertFalse(simpleType1.equals(simpleType3));
    }
}
