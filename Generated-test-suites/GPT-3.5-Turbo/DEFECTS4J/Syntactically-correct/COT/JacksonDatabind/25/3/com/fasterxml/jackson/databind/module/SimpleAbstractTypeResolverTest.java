import org.junit.Assert;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.lang.reflect.Modifier;
import java.util.*;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<?> superType = List.class;
        Class<? extends List> subType = LinkedList.class;

        resolver.addMapping(superType, subType);

        // Check if mapping was correctly added
        Map<ClassKey, Class<?>> mappings = resolver._mappings;
        Assert.assertEquals(subType, mappings.get(new ClassKey(superType)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingSameClass() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<?> superType = List.class;

        resolver.addMapping(superType, superType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingNotSubType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<?> superType = List.class;
        Class<? extends Set> subType = HashSet.class;

        resolver.addMapping(superType, subType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingNotAbstract() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<?> superType = ArrayList.class;
        Class<? extends List> subType = LinkedList.class;

        resolver.addMapping(superType, subType);
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<?> superType = List.class;
        Class<? extends List> subType = LinkedList.class;

        resolver.addMapping(superType, subType);

        DeserializationConfig config = null;
        JavaType type = TypeFactory.defaultInstance().constructType(List.class);
        JavaType result = resolver.findTypeMapping(config, type);

        // Check if the subType is returned as the mapping
        Assert.assertEquals(subType, result.getRawClass());
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        DeserializationConfig config = null;
        JavaType type = null;
        JavaType result = resolver.resolveAbstractType(config, type);

        // Check if null is returned, as no materialization is done
        Assert.assertNull(result);
    }
}