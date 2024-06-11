import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class SimpleAbstractTypeResolverTest {
    
    private SimpleAbstractTypeResolver resolver;
    
    @Before
    public void setup() {
        resolver = new SimpleAbstractTypeResolver();
    }
    
    @Test
    public void testAddMapping() {
        Class<?> superType = Collection.class;
        Class<?> subType = LinkedList.class;
        
        resolver.addMapping(superType, subType);
        
        assertEquals(subType, resolver._mappings.get(new ClassKey(superType)));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithSameClass() {
        Class<?> superType = Collection.class;
        
        resolver.addMapping(superType, superType);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithSubTypeNotSubclass() {
        Class<?> superType = Collection.class;
        Class<?> subType = ArrayList.class;
        
        resolver.addMapping(superType, subType);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithNonAbstractSuperType() {
        Class<?> superType = LinkedList.class;
        Class<?> subType = ArrayList.class;
        
        resolver.addMapping(superType, subType);
    }
    
    @Test
    public void testFindTypeMapping() {
        DeserializationConfig config = null;
        
        // Test with a valid mapping
        Class<?> src = Collection.class;
        Class<?> dst = LinkedList.class;
        JavaType type = TypeFactory.defaultInstance().constructType(src);
        
        resolver.addMapping(src, dst);
        
        JavaType result = resolver.findTypeMapping(config, type);
        
        assertNotNull(result);
        assertEquals(dst, result.getRawClass());
        
        // Test with no mapping
        src = ArrayList.class;
        type = TypeFactory.defaultInstance().constructType(src);
        
        result = resolver.findTypeMapping(config, type);
        
        assertNull(result);
    }
    
    @Test
    public void testResolveAbstractType() {
        DeserializationConfig config = null;
        JavaType type = TypeFactory.defaultInstance().constructType(Collection.class);
        
        JavaType result = resolver.resolveAbstractType(config, type);
        
        assertNull(result);
    }
}