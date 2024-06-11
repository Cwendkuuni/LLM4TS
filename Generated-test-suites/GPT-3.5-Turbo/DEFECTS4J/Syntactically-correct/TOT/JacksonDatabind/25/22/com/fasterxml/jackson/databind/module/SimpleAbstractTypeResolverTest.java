import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping_typicalUseCase() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        
        // Test that the mapping was added correctly
        Class<?> subType = resolver._mappings.get(new ClassKey(Collection.class));
        assertEquals(LinkedList.class, subType);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_sameClass() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, Collection.class);
        
        // The mapping should not be allowed since it's the same class
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_notSubType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(List.class, Set.class);
        
        // The mapping should not be allowed since Set is not a subtype of List
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_notAbstractSuperType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(ArrayList.class, LinkedList.class);
        
        // The mapping should not be allowed since ArrayList is not an abstract class
    }
    
    @Test
    public void testFindTypeMapping_typicalUseCase() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        
        // Test that the correct subtype is returned for the given superType
        JavaType type = new JavaTypeForTesting(Collection.class);
        JavaType mappedType = resolver.findTypeMapping(config, type);
        assertEquals(LinkedList.class, mappedType.getRawClass());
    }
    
    @Test
    public void testFindTypeMapping_noMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        
        // Test that null is returned when there is no mapping for the superType
        JavaType type = new JavaTypeForTesting(Collection.class);
        JavaType mappedType = resolver.findTypeMapping(config, type);
        assertNull(mappedType);
    }
    
    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        
        // Test that the method always returns null
        JavaType type = new JavaTypeForTesting(Collection.class);
        JavaType resolvedType = resolver.resolveAbstractType(config, type);
        assertNull(resolvedType);
    }
}

class JavaTypeForTesting extends JavaType {

    public JavaTypeForTesting(Class<?> rawClass) {
        super(rawClass);
    }
}