import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.ClassKey;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.HashMap;
import java.util.Map;

public class SimpleAbstractTypeResolverTest {

    private SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

    @Test
    public void testAddMapping() {
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);
        Map<ClassKey, Class<?>> mappings = resolver.getMappings();
        assertTrue(mappings.containsKey(new ClassKey(Collection.class)));
        assertTrue(mappings.containsKey(new ClassKey(List.class)));
    }

    @Test
    public void testFindTypeMapping() throws Exception {
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);

        DeserializationConfig config = new DeserializationConfig();
        ObjectMapper mapper = new ObjectMapper(config);

        JavaType javaType = mapper.getTypeFactory().constructType(List.class);
        JavaType resolvedType = resolver.findTypeMapping(config, javaType);
        assertTrue(resolvedType instanceof ClassType);
        assertEquals(LinkedList.class, resolvedType.getRawClass());

        javaType = mapper.getTypeFactory().constructType(Collection.class);
        resolvedType = resolver.findTypeMapping(config, javaType);
        assertTrue(resolvedType instanceof ClassType);
        assertEquals(LinkedList.class, resolvedType.getRawClass());

        javaType = mapper.getTypeFactory().constructType(ArrayList.class);
        resolvedType = resolver.findTypeMapping(config, javaType);
        assertNull(resolvedType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindTypeMappingWithInvalidArguments() throws Exception {
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);

        DeserializationConfig config = new DeserializationConfig();
        ObjectMapper mapper = new ObjectMapper(config);

        JavaType javaType = mapper.getTypeFactory().constructType(LinkedList.class);
        resolver.findTypeMapping(config, javaType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithInvalidArguments() {
        resolver.addMapping(LinkedList.class, Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithSelfReference() {
        resolver.addMapping(Collection.class, Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithAbstractClassAsSuperType() {
        resolver.addMapping(ArrayList.class, Object.class);
    }

    @Test
    public void testResolveAbstractType() {
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);

        DeserializationConfig config = new DeserializationConfig();
        ObjectMapper mapper = new ObjectMapper(config);

        JavaType javaType = mapper.getTypeFactory().constructType(Collection.class);
        JavaType resolvedType = resolver.resolveAbstractType(config, javaType);
        assertNull(resolvedType);
    }

    @Test
    public void testGetMappings() {
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);

        Map<ClassKey, Class<?>> mappings = resolver.getMappings();
        assertEquals(2, mappings.size());
    }
}