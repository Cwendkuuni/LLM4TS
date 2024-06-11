import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

public class SimpleAbstractTypeResolverTest {

    SimpleAbstractTypeResolver resolver;

    @Before
    public void setup() {
        resolver = new SimpleAbstractTypeResolver();
    }

    @Test
    public void testAddMapping() {
        Class<?> superType = List.class;
        Class<?> subType = ArrayList.class;

        resolver.addMapping(superType, subType);
        assertEquals(subType, resolver._mappings.get(new ClassKey(superType)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_sameClass() {
        Class<?> superType = List.class;
        resolver.addMapping(superType, superType);
        fail("IllegalArgumentException expected");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_notSubType() {
        Class<?> superType = List.class;
        Class<?> subType = String.class;

        resolver.addMapping(superType, subType);
        fail("IllegalArgumentException expected");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_notAbstract() {
        Class<?> superType = ArrayList.class;
        Class<?> subType = LinkedList.class;

        resolver.addMapping(superType, subType);
        fail("IllegalArgumentException expected");
    }

    @Test
    public void testFindTypeMapping_positive() {
        Class<?> superType = List.class;
        Class<?> subType = ArrayList.class;
        resolver.addMapping(superType, subType);

        DeserializationConfig config = null; // test the null case
        JavaType type = new JavaType(superType, null, superType);

        JavaType result = resolver.findTypeMapping(config, type);

        assertEquals(subType, result.getRawClass());
    }

    @Test
    public void testFindTypeMapping_negative() {
        DeserializationConfig config = null; // test the null case
        JavaType type = new JavaType(List.class, null, List.class);

        JavaType result = resolver.findTypeMapping(config, type);

        assertNull(result);
    }

    @Test
    public void testResolveAbstractType() {
        DeserializationConfig config = null; // test the null case
        JavaType type = new JavaType(List.class, null, List.class);

        JavaType result = resolver.resolveAbstractType(config, type);

        assertNull(result);
    }

}