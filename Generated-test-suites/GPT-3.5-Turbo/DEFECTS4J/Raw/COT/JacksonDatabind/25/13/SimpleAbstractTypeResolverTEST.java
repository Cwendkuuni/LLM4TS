
import org.junit.Assert;
import org.junit.Test;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);
        
        // Verify mappings
        Assert.assertEquals(LinkedList.class, resolver._mappings.get(new ClassKey(Collection.class)));
        Assert.assertEquals(LinkedList.class, resolver._mappings.get(new ClassKey(List.class)));

        // Verify non-existent mappings
        Assert.assertNull(resolver._mappings.get(new ClassKey(Set.class)));
        Assert.assertNull(resolver._mappings.get(new ClassKey(Map.class)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithSameClass() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithNonSubType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(List.class, Set.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithNonAbstractSuperType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(LinkedList.class, List.class);
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);
        
        // Test finding type mapping
        DeserializationConfig config = new DeserializationConfig();
        JavaType type = new JavaType(Collection.class, null);
        JavaType resolvedType = resolver.findTypeMapping(config, type);
        Assert.assertEquals(LinkedList.class, resolvedType.getRawClass());
        
        // Test non-existent type mapping
        JavaType nonexistentType = new JavaType(Set.class, null);
        JavaType nonexistentResolvedType = resolver.findTypeMapping(config, nonexistentType);
        Assert.assertNull(nonexistentResolvedType);
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);
        
        // Test resolving abstract type
        DeserializationConfig config = new DeserializationConfig();
        JavaType type = new JavaType(Collection.class, true, null, null);
        JavaType resolvedType = resolver.resolveAbstractType(config, type);
        Assert.assertNull(resolvedType);
    }
}
