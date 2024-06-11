
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SimpleTypeTest {

    @Test
    public void testConstruct() {
        SimpleType simpleType = SimpleType.construct(String.class);
        assertEquals(String.class, simpleType.getRawClass());
    }
    
    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(Long.class);
        assertEquals(Long.class, simpleType.getRawClass());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructForMap() {
        SimpleType.construct(Map.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructForCollection() {
        SimpleType.construct(Collection.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructForArray() {
        SimpleType.construct(int[].class);
    }
    
    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = SimpleType.construct(String.class);
        assertEquals("Ljava/lang/String;", simpleType.getErasedSignature().toString());
    }
    
    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = SimpleType.construct(List.class);
        assertEquals("Ljava/util/List<*>;", simpleType.getGenericSignature().toString());
    }
}

