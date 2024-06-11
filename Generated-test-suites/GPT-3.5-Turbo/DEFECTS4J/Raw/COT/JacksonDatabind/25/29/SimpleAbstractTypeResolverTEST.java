import static org.junit.Assert.*;
import java.lang.reflect.Modifier;
import java.util.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.*;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);
        HashMap<ClassKey, Class<?>> mappings = resolver.getMappings();
        assertTrue(mappings.containsKey(new ClassKey(Collection.class)));
        assertTrue(mappings.containsKey(new ClassKey(List.class)));
        assertEquals(LinkedList.class, mappings.get(new ClassKey(Collection.class)));
        assertEquals(LinkedList.class, mappings.get(new ClassKey(List.class)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithSameClass() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithNonSubtype() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, ArrayList.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithNonAbstractSuperclass() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(ArrayList.class, LinkedList.class);
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);
        DeserializationConfig config = new DeserializationConfig();
        JavaType type = TypeFactory.defaultInstance().constructType(Collection.class);
        JavaType mappedType = resolver.findTypeMapping(config, type);
        assertEquals(LinkedList.class, mappedType.getRawClass());

        type = TypeFactory.defaultInstance().constructType(List.class);
        mappedType = resolver.findTypeMapping(config, type);
        assertEquals(LinkedList.class, mappedType.getRawClass());
    }

    @Test
    public void testFindTypeMappingWithNoMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        DeserializationConfig config = new DeserializationConfig();
        JavaType type = TypeFactory.defaultInstance().constructType(Collection.class);
        JavaType mappedType = resolver.findTypeMapping(config, type);
        assertNull(mappedType);
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        DeserializationConfig config = new DeserializationConfig();
        JavaType type = TypeFactory.defaultInstance().constructType(Collection.class);
        JavaType resolvedType = resolver.resolveAbstractType(config, type);
        assertNull(resolvedType);
    }

}