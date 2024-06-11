import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleAbstractTypeResolverTest {

    SimpleAbstractTypeResolver resolver;

    @Test
    public void testAddMapping() {
        resolver = new SimpleAbstractTypeResolver();
        Class<Integer> superType = Number.class;
        Class<Integer> subType = Integer.class;
        resolver.addMapping(superType, subType);
        Class<?> result = resolver._mappings.get(new com.fasterxml.jackson.databind.type.ClassKey(superType));
        assertEquals(subType, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingSameClass() {
        resolver = new SimpleAbstractTypeResolver();
        Class<Integer> superType = Integer.class;
        Class<Integer> subType = Integer.class;
        resolver.addMapping(superType, subType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingNotSubType() {
        resolver = new SimpleAbstractTypeResolver();
        Class<Object> superType = Number.class;
        Class<Integer> subType = Integer.class;
        resolver.addMapping(superType, subType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingNotAbstract() {
        resolver = new SimpleAbstractTypeResolver();
        Class<Integer> superType = Integer.class;
        Class<Integer> subType = Integer.class;
        resolver.addMapping(superType, subType);
    }

    @Test
    public void testFindTypeMapping() {
        resolver = new SimpleAbstractTypeResolver();
        Class<Number> superType = Number.class;
        Class<Integer> subType = Integer.class;
        resolver.addMapping(superType, subType);
        DeserializationConfig config = new DeserializationConfig();
        JavaType type = new JavaType(superType);
        JavaType result = resolver.findTypeMapping(config, type);
        assertEquals(subType, result.getRawClass());
    }

    @Test
    public void testFindTypeMappingNull() {
        resolver = new SimpleAbstractTypeResolver();
        DeserializationConfig config = new DeserializationConfig();
        JavaType type = new JavaType(Number.class);
        JavaType result = resolver.findTypeMapping(config, type);
        assertNull(result);
    }

    @Test
    public void testResolveAbstractTypeNull() {
        resolver = new SimpleAbstractTypeResolver();
        DeserializationConfig config = new DeserializationConfig();
        JavaType type = new JavaType(Number.class);
        JavaType result = resolver.resolveAbstractType(config, type);
        assertNull(result);
    }

    // Additional test cases for other methods in the SimpleAbstractTypeResolver class...

}