import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;
public class SimpleAbstractTypeResolverTest {
    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        // Test with mappings
        resolver.addMapping(List.class, ArrayList.class);
        resolver.addMapping(Map.class, HashMap.class);
        // Test with no mappings
        JavaType type1 = new JavaType(List.class);
        JavaType type2 = new JavaType(Map.class);
        assertNull(resolver.findTypeMapping(type1));
        assertNull(resolver.findTypeMapping(type2));
        // Test with valid mappings
        JavaType type3 = new JavaType(ArrayList.class);
        JavaType type4 = new JavaType(HashMap.class);
        assertEquals(type3, resolver.findTypeMapping(type1));
        assertEquals(type4, resolver.findTypeMapping(type2));
    }
    
    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        // Test with no mappings
        JavaType type1 = new JavaType(List.class);
        JavaType type2 = new JavaType(Map.class);
        assertNull(resolver.resolveAbstractType(type1));
        assertNull(resolver.resolveAbstractType(type2));
        // Test with valid mappings
        resolver.addMapping(List.class, ArrayList.class);
        resolver.addMapping(Map.class, HashMap.class);
        JavaType type3 = new JavaType(ArrayList.class);
        JavaType type4 = new JavaType(HashMap.class);
        assertEquals(type3, resolver.resolveAbstractType(type1));
        assertEquals(type4, resolver.resolveAbstractType(type2));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingSameClass() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(List.class, List.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingInvalidSubtype() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(List.class, String.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingNotAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(HashMap.class, LinkedHashMap.class);
    }
}