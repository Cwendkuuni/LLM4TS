
package com.fasterxml.jackson.databind.module;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.util.Collection;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SimpleAbstractTypeResolverTest {

    private SimpleAbstractTypeResolver resolver;

    @Before
    public void setUp() {
        resolver = new SimpleAbstractTypeResolver();
    }

    @Test
    public void addMapping_SuperTypeAndSubType_MappingAdded() {
        Class<Collection> superType = Collection.class;
        Class<LinkedList> subType = LinkedList.class;

        SimpleAbstractTypeResolver result = resolver.addMapping(superType, subType);

        assertEquals(subType, resolver._mappings.get(TypeFactory.defaultInstance().constructType(superType)));
        assertEquals(resolver, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addMapping_SuperTypeSameAsSubType_ThrowsIllegalArgumentException() {
        Class<Collection> superType = Collection.class;
        Class<Collection> subType = Collection.class;

        resolver.addMapping(superType, subType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addMapping_SubTypeNotSubtypeOfSuperType_ThrowsIllegalArgumentException() {
        Class<Collection> superType = Collection.class;
        Class<LinkedList> subType = LinkedList.class;

        resolver.addMapping(subType, superType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addMapping_SuperTypeNotAbstract_ThrowsIllegalArgumentException() {
        Class<LinkedList> superType = LinkedList.class;
        Class<LinkedList> subType = LinkedList.class;

        resolver.addMapping(superType, subType);
    }

    @Test
    public void findTypeMapping_ValidMapping_ReturnsMappedType() {
        DeserializationConfig config = null;
        JavaType type = TypeFactory.defaultInstance().constructType(Collection.class);
        Class<LinkedList> subType = LinkedList.class;

        resolver.addMapping(Collection.class, LinkedList.class);

        JavaType result = resolver.findTypeMapping(config, type);

        assertEquals(subType, result.getRawClass());
    }

    @Test
    public void findTypeMapping_NoMapping_ReturnsNull() {
        DeserializationConfig config = null;
        JavaType type = TypeFactory.defaultInstance().constructType(Collection.class);

        JavaType result = resolver.findTypeMapping(config, type);

        assertNull(result);
    }

    @Test
    public void resolveAbstractType_ReturnsNull() {
        DeserializationConfig config = null;
        JavaType type = TypeFactory.defaultInstance().constructType(Collection.class);

        JavaType result = resolver.resolveAbstractType(config, type);

        assertNull(result);
    }
}
