package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.*;

import org.junit.Test;

public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfoConstructionWithAllArgs() {
        PropertyName propertyName = new PropertyName("propertyName");
        Class<String> scope = String.class;
        Class<MyCustomObjectIdGenerator> generator = MyCustomObjectIdGenerator.class;
        Class<SimpleObjectIdResolver> resolver = SimpleObjectIdResolver.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, resolver);
        assertEquals("propertyName", objectIdInfo.getPropertyName().getName());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(MyCustomObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoConstructionWithMinimalArgs() {
        PropertyName propertyName = new PropertyName("propertyName");
        Class<String> scope = String.class;
        Class<MyCustomObjectIdGenerator> generator = MyCustomObjectIdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);
        assertEquals("propertyName", objectIdInfo.getPropertyName().getName());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(MyCustomObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoConstructionWithMinimalArgsStringName() {
        PropertyName propertyName = new PropertyName("propertyName");
        Class<String> scope = String.class;
        Class<MyCustomObjectIdGenerator> generator = MyCustomObjectIdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName.getName(), scope, generator);
        assertEquals("propertyName", objectIdInfo.getPropertyName().getName());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(MyCustomObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("propertyName");
        Class<String> scope = String.class;
        Class<MyCustomObjectIdGenerator> generator = MyCustomObjectIdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, true, null);
        ObjectIdInfo newObjectIdInfo = objectIdInfo.withAlwaysAsId(false);
        assertNotEquals(objectIdInfo, newObjectIdInfo);
        assertFalse(newObjectIdInfo.getAlwaysAsId());
    }

    @Test(expected = NullPointerException.class)
    public void testObjectIdInfoNullResolver() {
        PropertyName propertyName = new PropertyName("propertyName");
        Class<String> scope = String.class;
        Class<MyCustomObjectIdGenerator> generator = MyCustomObjectIdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, null);
    }
}

class MyCustomObjectIdGenerator {
    // Empty custom ObjectIdGenerator for testing
}