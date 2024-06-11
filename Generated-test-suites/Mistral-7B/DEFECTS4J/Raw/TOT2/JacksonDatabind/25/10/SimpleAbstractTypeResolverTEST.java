import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import com.fasterxml.jackson.databind.type.ClassKey;
import java.util.*;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<List> superType = List.class;
        Class<LinkedList> subType = LinkedList.class;
        resolver.addMapping(superType, subType);
        assertNotNull(resolver._mappings.get(new ClassKey(superType)));
        assertEquals(subType, resolver._mappings.get(new ClassKey(superType)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingSameClass() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<List> superType = List.class;
        Class<LinkedList> subType = LinkedList.class;
        resolver.addMapping(superType, subType);
        resolver.addMapping(superType, superType); // The second call should throw an exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingNonAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<List> superType = ArrayList.class; // ArrayList is not abstract
        Class<LinkedList> subType = LinkedList.class;
        resolver.addMapping(superType, subType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingNotSubclass() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<List> superType = List.class;
        Class<ArrayList> subType = ArrayList.class; // ArrayList is not a subclass of List
        resolver.addMapping(superType, subType);
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        DeserializationConfig config = new DeserializationConfig(new ObjectMapper.StandardConfig());
        JavaType superType = TypeFactory.defaultInstance().constructType(List.class);
        JavaType subType = TypeFactory.defaultInstance().constructType(LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);
        JavaType mappedType = resolver.findTypeMapping(config, superType);
        assertNotNull(mappedType);
        assertEquals(subType, mappedType);
    }

    @Test
    public void testFindTypeMappingNoMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        DeserializationConfig config = new DeserializationConfig(new ObjectMapper.StandardConfig());
        JavaType superType = TypeFactory.defaultInstance().constructType(AbstractMap.class);
        JavaType mappedType = resolver.findTypeMapping(config, superType);
        assertNull(mappedType);
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        DeserializationConfig config = new DeserializationConfig(new ObjectMapper.StandardConfig());
        JavaType type = TypeFactory.defaultInstance().constructType(AbstractList.class);
        JavaType mappedType = resolver.resolveAbstractType(config, type);
        assertNull(mappedType);
    }
}

//