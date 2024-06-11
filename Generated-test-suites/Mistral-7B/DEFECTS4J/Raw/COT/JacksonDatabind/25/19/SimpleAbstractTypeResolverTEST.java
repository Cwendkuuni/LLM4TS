import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import com.fasterxml.jackson.databind.type.ClassKey;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<?> abstractType = Collection.class;
        Class<?> concreteType = LinkedList.class;
        resolver.addMapping(abstractType, concreteType);
        assertTrue(resolver._mappings.containsKey(new ClassKey(abstractType)));
        assertEquals(concreteType, resolver._mappings.get(new ClassKey(abstractType)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithSameClasses() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<?> abstractType = Collection.class;
        resolver.addMapping(abstractType, abstractType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithNotSubType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<?> abstractType = Collection.class;
        Class<?> concreteType = ArrayList.class;
        resolver.addMapping(abstractType, concreteType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithNonAbstractSuperType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<?> abstractType = Collection.class;
        Class<?> concreteType = ArrayList.class;
        resolver.addMapping(concreteType, abstractType);
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        JavaType javaType = JavaType.construct(Collection.class, String.class);
        JavaType result = resolver.findTypeMapping(null, javaType);
        assertNotNull(result);
        assertEquals(LinkedList.class, result.getRawClass());
    }

    @Test
    public void testFindTypeMappingWhenNoMappingFound() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        JavaType javaType = JavaType.construct(Collection.class, String.class);
        JavaType result = resolver.findTypeMapping(null, javaType);
        assertNull(result);
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        JavaType javaType = JavaType.construct(Collection.class, String.class);
        JavaType result = resolver.resolveAbstractType(null, javaType);
        assertNull(result);
    }
}