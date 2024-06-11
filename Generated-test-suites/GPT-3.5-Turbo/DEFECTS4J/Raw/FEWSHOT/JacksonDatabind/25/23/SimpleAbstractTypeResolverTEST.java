import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SimpleAbstractTypeResolverTest {
    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingToItself() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, Collection.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingInvalidSubtype() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, Map.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingNonAbstractSuperType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(String.class, ArrayList.class);
    }
    
    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);
        
        assertNull(resolver.findTypeMapping(null, null));
        
        JavaType type1 = new JavaType(LinkedList.class);
        assertEquals(type1, resolver.findTypeMapping(null, type1));
        
        JavaType type2 = new JavaType(ArrayList.class);
        assertNull(resolver.findTypeMapping(null, type2));
        
        JavaType type3 = new JavaType(Collection.class);
        assertEquals(type1, resolver.findTypeMapping(null, type3));
    }
    
    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);
        
        assertNull(resolver.resolveAbstractType(null, null));
    }
}