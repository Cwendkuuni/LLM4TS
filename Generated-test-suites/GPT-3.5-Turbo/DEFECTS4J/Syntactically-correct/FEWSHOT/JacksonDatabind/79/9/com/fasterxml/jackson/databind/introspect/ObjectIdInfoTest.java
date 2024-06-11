package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ObjectIdInfoTest {
    @Test
    public void testConstructorWithResolver() {
        ObjectIdInfo info = new ObjectIdInfo(new PropertyName("name"), String.class, CustomObjectIdGenerator.class, CustomObjectIdResolver.class);
        assertEquals("name", info.getPropertyName().toString());
        assertEquals(String.class, info.getScope());
        assertEquals(CustomObjectIdGenerator.class, info.getGeneratorType());
        assertEquals(CustomObjectIdResolver.class, info.getResolverType());
        assertEquals(false, info.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithoutResolver() {
        ObjectIdInfo info = new ObjectIdInfo(new PropertyName("name"), Integer.class, DefaultObjectIdGenerator.class);
        assertEquals("name", info.getPropertyName().toString());
        assertEquals(Integer.class, info.getScope());
        assertEquals(DefaultObjectIdGenerator.class, info.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, info.getResolverType());
        assertEquals(false, info.getAlwaysAsId());
    }

    @Test
    public void testWithAlwaysAsId() {
        ObjectIdInfo info = new ObjectIdInfo(new PropertyName("name"), Long.class, SequentialObjectIdGenerator.class);
        assertEquals("name", info.getPropertyName().toString());
        assertEquals(Long.class, info.getScope());
        assertEquals(SequentialObjectIdGenerator.class, info.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, info.getResolverType());
        assertEquals(false, info.getAlwaysAsId());
        
        info = info.withAlwaysAsId(true);
        assertEquals(true, info.getAlwaysAsId());
    }
}