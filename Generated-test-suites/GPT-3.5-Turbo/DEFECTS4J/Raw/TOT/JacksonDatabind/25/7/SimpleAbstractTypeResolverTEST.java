import org.junit.Test;
import com.fasterxml.jackson.databind.AbstractTypeResolver;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class SimpleAbstractTypeResolverTest {

    private SimpleAbstractTypeResolver resolver;

    @Test
    public void testAddMapping() {
        resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, List.class);
        resolver.addMapping(List.class, ArrayList.class);

        assertNotNull(resolver._mappings);
        assertEquals(2, resolver._mappings.size());
        assertEquals(List.class, resolver._mappings.get(new ClassKey(Collection.class)));
        assertEquals(ArrayList.class, resolver._mappings.get(new ClassKey(List.class)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_sameType() {
        resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Stack.class, Stack.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_invalidMapping() {
        resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(AbstractTypeResolver.class, List.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_nonAbstractSuperType() {
        resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(ArrayList.class, List.class);
    }

    @Test
    public void testFindTypeMapping() {
        resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, List.class);
        resolver.addMapping(List.class, ArrayList.class);

        DeserializationConfig config = new ObjectMapper().getDeserializationConfig();

        JavaType collectionType = config.constructType(Collection.class);
        assertNull(resolver.findTypeMapping(config, collectionType));

        JavaType listType = config.constructType(List.class);
        JavaType resolvedType = resolver.findTypeMapping(config, listType);
        assertNotNull(resolvedType);
        assertEquals(ArrayList.class, resolvedType.getRawClass());
    }

    @Test
    public void testResolveAbstractType() {
        resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, List.class);
        resolver.addMapping(List.class, ArrayList.class);

        DeserializationConfig config = new ObjectMapper().getDeserializationConfig();

        JavaType collectionType = config.constructType(Collection.class);
        assertNull(resolver.resolveAbstractType(config, collectionType));

        JavaType listType = config.constructType(List.class);
        assertNull(resolver.resolveAbstractType(config, listType));
    }

}