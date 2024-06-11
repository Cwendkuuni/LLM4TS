import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.util.*;

public class SimpleAbstractTypeResolverTest {

    private SimpleAbstractTypeResolver resolver;
    private DeserializationConfig config;
    private TypeFactory typeFactory;

    @Before
    public void setUp() {
        resolver = new SimpleAbstractTypeResolver();
        config = new ObjectMapper().getDeserializationConfig();
        typeFactory = config.getTypeFactory();
    }

    @Test
    public void testAddMapping_sameType() {
        Class<List> type = List.class;

        try {
            resolver.addMapping(type, type);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class to itself", e.getMessage());
        }
    }

    @Test
    public void testAddMapping_notSubType() {
        Class<AbstractList> superType = AbstractList.class;
        Class<LinkedList> subType = LinkedList.class;

        try {
            resolver.addMapping(superType, subType);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class " + superType.getName() +
                    " to " + subType.getName() + ", as latter is not a subtype of former", e.getMessage());
        }
    }

    @Test
    public void testAddMapping_notAbstract() {
        Class<ArrayList> type = ArrayList.class;

        try {
            resolver.addMapping(type, LinkedList.class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class " + type.getName() +
                    " since it is not abstract", e.getMessage());
        }
    }

    @Test
    public void testFindTypeMapping_noMappingFound() {
        JavaType type = typeFactory.constructType(HashMap.class);

        assertNull(resolver.findTypeMapping(config, type));
    }

    @Test
    public void testFindTypeMapping_mappingFound() {
        Class<List> superType = List.class;
        Class<LinkedList> subType = LinkedList.class;
        JavaType type = typeFactory.constructType(superType);

        resolver.addMapping(superType, subType);

        JavaType resolvedType = resolver.findTypeMapping(config, type);

        assertNotNull(resolvedType);
        assertEquals(subType, resolvedType.getRawClass());
    }

    @Test
    public void testResolveAbstractType() {
        JavaType type = typeFactory.constructType(LinkedList.class);

        assertNull(resolver.resolveAbstractType(config, type));
    }
}