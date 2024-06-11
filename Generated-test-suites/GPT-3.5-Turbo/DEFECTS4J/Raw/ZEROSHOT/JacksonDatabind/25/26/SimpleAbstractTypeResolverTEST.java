package com.fasterxml.jackson.databind.module;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleAbstractTypeResolverTest {

    private SimpleAbstractTypeResolver resolver;

    @Before
    public void setup() {
        resolver = new SimpleAbstractTypeResolver();
    }

    @Test
    public void testAddMapping() {
        Class<?> superType = Collection.class;
        Class<?> subType = LinkedList.class;

        SimpleAbstractTypeResolver result = resolver.addMapping(superType, subType);

        assertNotNull(result);
        assertEquals(subType, resolver._mappings.get(new ClassKey(superType)));
    }

    @Test
    public void testAddMappingThrowsExceptionForSameSuperAndSubType() {
        Class<?> type = Collection.class;

        try {
            resolver.addMapping(type, type);
            fail("IllegalArgumentException should be thrown when adding mapping from class to itself");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class to itself", e.getMessage());
        }
    }

    @Test
    public void testAddMappingThrowsExceptionForNonSubType() {
        Class<?> superType = Collection.class;
        Class<?> subType = HashMap.class;

        try {
            resolver.addMapping(superType, subType);
            fail("IllegalArgumentException should be thrown when adding mapping to a non-subtype");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class " + superType.getName() + " to " + subType.getName() + ", as latter is not a subtype of former", e.getMessage());
        }
    }

    @Test
    public void testAddMappingThrowsExceptionForNonAbstractSuperType() {
        Class<?> superType = ArrayList.class;
        Class<?> subType = LinkedList.class;

        try {
            resolver.addMapping(superType, subType);
            fail("IllegalArgumentException should be thrown when adding mapping from a non-abstract super type");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class " + superType.getName() + " since it is not abstract", e.getMessage());
        }
    }

    @Test
    public void testFindTypeMappingReturnsNullForUnsupportedType() {
        DeserializationConfig config = null;
        JavaType type = null;

        JavaType result = resolver.findTypeMapping(config, type);

        assertNull(result);
    }

    @Test
    public void testFindTypeMappingReturnsMappedType() {
        DeserializationConfig config = null;
        JavaType type = null;

        resolver.addMapping(Collection.class, LinkedList.class);

        JavaType result = resolver.findTypeMapping(config, type);

        assertNotNull(result);
        assertEquals(LinkedList.class, result.getRawClass());
    }

    @Test
    public void testResolveAbstractTypeReturnsNullForAnyType() {
        DeserializationConfig config = null;
        JavaType type = null;

        JavaType result = resolver.resolveAbstractType(config, type);

        assertNull(result);
    }
}