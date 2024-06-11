import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.DeserializationConfig;
import java.util.LinkedList;

public class SimpleAbstractTypeResolverTest {
    
    private SimpleAbstractTypeResolver resolver;
    
    @Before
    public void setup() {
        resolver = new SimpleAbstractTypeResolver();
    }
    
    @Test
    public void testAddMapping() {
        resolver.addMapping(Collection.class, LinkedList.class);
        assertNotNull(resolver._mappings);
        assertTrue(resolver._mappings.containsKey(new ClassKey(Collection.class)));
        assertEquals(LinkedList.class, resolver._mappings.get(new ClassKey(Collection.class)));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingExceptionSelf() {
        resolver.addMapping(Collection.class, Collection.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingExceptionSubType() {
        resolver.addMapping(List.class, ArrayList.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingExceptionConcreteSuperType() {
        resolver.addMapping(LinkedList.class, LinkedList.class);
    }
    
    @Test
    public void testFindTypeMapping() {
        resolver.addMapping(Collection.class, LinkedList.class);
        DeserializationConfig config = null; // Not needed for this test
        JavaType type = resolver.findTypeMapping(config, TypeFactory.defaultInstance().constructType(Collection.class));
        assertNotNull(type);
        assertEquals(LinkedList.class, type.getRawClass());
    }
    
    @Test
    public void testFindTypeMappingNotFound() {
        DeserializationConfig config = null; // Not needed for this test
        JavaType type = resolver.findTypeMapping(config, TypeFactory.defaultInstance().constructType(ArrayList.class));
        assertNull(type);
    }
    
    @Test
    public void testResolveAbstractType() {
        DeserializationConfig config = null; // Not needed for this test
        JavaType type = resolver.resolveAbstractType(config, TypeFactory.defaultInstance().constructType(Collection.class));
        assertNull(type);
    }
}