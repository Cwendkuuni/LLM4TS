import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);
        
        assertFalse(resolver._mappings.isEmpty());
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        
        JavaType result = resolver.findTypeMapping(null, TypeFactory.defaultInstance().constructType(Collection.class));
        assertNotNull(result);
        assertEquals(LinkedList.class, result.getRawClass());
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        JavaType result = resolver.resolveAbstractType(null, TypeFactory.defaultInstance().constructType(Collection.class));
        
        assertNull(result);
    }
}