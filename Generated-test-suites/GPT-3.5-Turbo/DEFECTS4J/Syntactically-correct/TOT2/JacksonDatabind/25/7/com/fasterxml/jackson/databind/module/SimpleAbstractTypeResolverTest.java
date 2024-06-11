import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        
        // Test adding mapping from abstract superclass to concrete subclass
        resolver.addMapping(Collection.class, ArrayList.class);
        assertNotNull(resolver._mappings.get(new ClassKey(Collection.class)));
        
        // Test adding mapping from interface to implementing class
        resolver.addMapping(List.class, ArrayList.class);
        assertNotNull(resolver._mappings.get(new ClassKey(List.class)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingIllegalArguments() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        
        // Test adding mapping from class to itself (illegal)
        resolver.addMapping(ArrayList.class, ArrayList.class);
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(List.class, ArrayList.class);
        
        // Test finding type mapping for existing mapping
        JavaType result = resolver.findTypeMapping(null, TypeFactory.defaultInstance().constructType(List.class));
        assertNotNull(result);
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        // Test resolving abstract type (should return null)
        JavaType result = resolver.resolveAbstractType(null, TypeFactory.defaultInstance().constructType(Collection.class));
        assertNull(result);
    }
}