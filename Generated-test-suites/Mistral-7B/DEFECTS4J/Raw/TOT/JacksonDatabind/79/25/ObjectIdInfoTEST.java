package com.example.junit4test;

import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdInfoTest {
    @Test
    public void testObjectIdInfoConstruction() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<Integer> scope = Integer.class;
        Class<MyCustomObjectIdGenerator> generator = MyCustomObjectIdGenerator.class;
        Class<SimpleObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, resolver);

        // Check constructor arguments
        assertEquals("testProperty", objectIdInfo.getPropertyName().getName());
        assertEquals(Integer.class, objectIdInfo.getScope());
        assertEquals(MyCustomObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<Integer> scope = Integer.class;
        Class<MyCustomObjectIdGenerator> generator = MyCustomObjectIdGenerator.class;
        Class<SimpleObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, resolver);

        ObjectIdInfo newObjectIdInfo = objectIdInfo.withAlwaysAsId(true);

        // Check constructor arguments
        assertEquals("testProperty", newObjectIdInfo.getPropertyName().getName());
        assertEquals(Integer.class, newObjectIdInfo.getScope());
        assertEquals(MyCustomObjectIdGenerator.class, newObjectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, newObjectIdInfo.getResolverType());
        assertTrue(newObjectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoDefaultConstruction() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null, false);

        // Check default constructor arguments
        assertNull(objectIdInfo.getPropertyName());
        assertNull(objectIdInfo.getScope());
        assertNull(objectIdInfo.getGeneratorType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoConstructionWithString() {
        String name = "testProperty";
        Class<Integer> scope = Integer.class;
        Class<MyCustomObjectIdGenerator> generator = MyCustomObjectIdGenerator.class;
        Class<SimpleObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, generator, false, resolver);

        // Check constructor arguments
        assertEquals(new PropertyName(name), objectIdInfo.getPropertyName());
        assertEquals(Integer.class, objectIdInfo.getScope());
        assertEquals(MyCustomObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }
}