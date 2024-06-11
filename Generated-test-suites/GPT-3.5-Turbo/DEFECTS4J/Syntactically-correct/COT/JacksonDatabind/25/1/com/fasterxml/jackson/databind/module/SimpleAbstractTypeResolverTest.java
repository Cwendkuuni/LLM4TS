// Generated test cases for SimpleAbstractTypeResolver class
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.HashMap;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import com.fasterxml.jackson.databind.type.ClassKey;

public class SimpleAbstractTypeResolverTest {
    private SimpleAbstractTypeResolver resolver;

    @Before
    public void setup() {
        resolver = new SimpleAbstractTypeResolver();
    }

    @Test
    public void testAddMapping() {
        resolver.addMapping(Collection.class, ArrayList.class);
        ClassKey key = new ClassKey(Collection.class);
        Class<?> value = resolver._mappings.get(key);
        Assert.assertEquals(ArrayList.class, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithSameType() {
        resolver.addMapping(Collection.class, Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithNoSubType() {
        resolver.addMapping(List.class, HashMap.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithNonAbstractSuperType() {
        resolver.addMapping(ArrayList.class, LinkedList.class);
    }

    @Test
    public void testFindTypeMapping() {
        resolver.addMapping(Collection.class, ArrayList.class);
        DeserializationConfig config = null;
        JavaType type = TypeFactory.defaultInstance().constructType(ArrayList.class);
        JavaType result = resolver.findTypeMapping(config, type);
        Assert.assertEquals(type, result);
    }

    @Test
    public void testFindTypeMappingWithNotMappedType() {
        DeserializationConfig config = null;
        JavaType type = TypeFactory.defaultInstance().constructType(ArrayList.class);
        JavaType result = resolver.findTypeMapping(config, type);
        Assert.assertNull(result);
    }

    @Test
    public void testResolveAbstractType() {
        DeserializationConfig config = null;
        JavaType type = TypeFactory.defaultInstance().constructType(ArrayList.class);
        JavaType result = resolver.resolveAbstractType(config, type);
        Assert.assertNull(result);
    }
}