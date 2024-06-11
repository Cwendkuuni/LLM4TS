package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.BeforeClass;
import org.junit.Test;

public class SimpleAbstractTypeResolverTest {

    private static SimpleAbstractTypeResolver resolver;

    @BeforeClass
    public static void setup() {
        resolver = new SimpleAbstractTypeResolver();
    }

    @Test
    public void testAddMapping() {
        resolver.addMapping(AbstractType.class, SubType.class);
        JavaType type = TypeFactory.defaultInstance().constructType(AbstractType.class);
        DeserializationConfig config = new DeserializationConfig(TypeFactory.defaultInstance());
        JavaType result = resolver.findTypeMapping(config, type);
        assertNotNull(result);
        assertEquals(SubType.class, result.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_SameClass() {
        resolver.addMapping(AbstractType.class, AbstractType.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_NotSubType() {
        resolver.addMapping(AbstractType.class, NonSubType.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_NotAbstractType() {
        resolver.addMapping(ConcreteType.class, SubType.class);
    }

    @Test
    public void testFindTypeMapping() {
        resolver.addMapping(AbstractType2.class, SubType2.class);
        JavaType type = TypeFactory.defaultInstance().constructType(AbstractType2.class);
        DeserializationConfig config = new DeserializationConfig(TypeFactory.defaultInstance());
        JavaType result = resolver.findTypeMapping(config, type);
        assertNotNull(result);
        assertEquals(SubType2.class, result.getRawClass());
    }
    
    @Test
    public void testFindTypeMapping_NotFound() {
        JavaType type = TypeFactory.defaultInstance().constructType(AbstractType.class);
        DeserializationConfig config = new DeserializationConfig(TypeFactory.defaultInstance());
        JavaType result = resolver.findTypeMapping(config, type);
        assertNull(result);
    }
    
    @Test
    public void testResolveAbstractType() {
        JavaType type = TypeFactory.defaultInstance().constructType(AbstractType.class);
        DeserializationConfig config = new DeserializationConfig(TypeFactory.defaultInstance());
        JavaType result = resolver.resolveAbstractType(config, type);
        assertNull(result);
    }
}