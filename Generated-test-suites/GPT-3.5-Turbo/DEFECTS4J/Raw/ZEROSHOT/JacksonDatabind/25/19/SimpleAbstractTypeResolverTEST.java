import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        assertEquals(0, resolver._mappings.size());

        resolver.addMapping(Map.class, HashMap.class);
        assertEquals(1, resolver._mappings.size());

        resolver.addMapping(List.class, ArrayList.class);
        assertEquals(2, resolver._mappings.size());

        resolver.addMapping(Set.class, HashSet.class);
        assertEquals(3, resolver._mappings.size());
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(List.class, ArrayList.class);

        DeserializationConfig config = new DeserializationConfig();
        JavaType type = new JavaType(ArrayList.class);

        JavaType result = resolver.findTypeMapping(config, type);

        assertEquals(ArrayList.class, result.getRawClass());
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        DeserializationConfig config = new DeserializationConfig();
        JavaType type = new JavaType(List.class);

        JavaType result = resolver.resolveAbstractType(config, type);

        assertNull(result);
    }

}