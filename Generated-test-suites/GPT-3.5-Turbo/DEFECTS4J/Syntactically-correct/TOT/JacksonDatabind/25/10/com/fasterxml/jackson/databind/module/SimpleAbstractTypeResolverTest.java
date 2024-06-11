import com.fasterxml.jackson.databind.AbstractTypeResolver;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);
        resolver.addMapping(ArrayList.class, LinkedList.class);
        Assert.assertEquals(3, resolver._mappings.size());
    }

    @Test
    public void testValidTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);
        DeserializationConfig config = new ObjectMapper().getDeserializationConfig();
        JavaType type = config.getTypeFactory().constructCollectionType(List.class, String.class);
        JavaType resolvedType = resolver.findTypeMapping(config, type);
        Assert.assertEquals(LinkedList.class, resolvedType.getRawClass());
    }

    @Test
    public void testInvalidTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);
        DeserializationConfig config = new ObjectMapper().getDeserializationConfig();
        JavaType type = config.getTypeFactory().constructArrayType(String.class);
        JavaType resolvedType = resolver.findTypeMapping(config, type);
        Assert.assertNull(resolvedType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSelfMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonSubTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, List.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonAbstractSuperTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(String.class, List.class);
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        DeserializationConfig config = new ObjectMapper().getDeserializationConfig();
        JavaType type = config.getTypeFactory().constructSimpleType(List.class, null);
        JavaType resolvedType = resolver.resolveAbstractType(config, type);
        Assert.assertNull(resolvedType);
    }

    @Test
    public void testResolveAbstractTypeWithConcreteType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        DeserializationConfig config = new ObjectMapper().getDeserializationConfig();
        JavaType type = config.getTypeFactory().constructSimpleType(String.class, null);
        JavaType resolvedType = resolver.resolveAbstractType(config, type);
        Assert.assertNull(resolvedType);
    }
}