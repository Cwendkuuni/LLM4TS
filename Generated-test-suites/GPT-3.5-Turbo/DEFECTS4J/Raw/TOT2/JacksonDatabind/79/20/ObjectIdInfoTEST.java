package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ObjectIdInfoTest {

    @Test
    public void testConstructorWithResolver() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("id"), String.class,
                CustomObjectIdGenerator.class, CustomObjectIdResolver.class);

        assertNotNull(objectIdInfo);
        assertEquals("id", objectIdInfo.getPropertyName().getSimpleName());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(CustomObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertEquals(CustomObjectIdResolver.class, objectIdInfo.getResolverType());
        assertTrue(!objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithoutResolver() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("id"), Integer.class,
                CustomObjectIdGenerator.class);

        assertNotNull(objectIdInfo);
        assertEquals("id", objectIdInfo.getPropertyName().getSimpleName());
        assertEquals(Integer.class, objectIdInfo.getScope());
        assertEquals(CustomObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertTrue(!objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testWithAlwaysAsId() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("id"), Long.class,
                CustomObjectIdGenerator.class, true);

        objectIdInfo = objectIdInfo.withAlwaysAsId(false);

        assertNotNull(objectIdInfo);
        assertEquals("id", objectIdInfo.getPropertyName().getSimpleName());
        assertEquals(Long.class, objectIdInfo.getScope());
        assertEquals(CustomObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertTrue(!objectIdInfo.getAlwaysAsId());
    }

    // Add more test cases here for other methods as needed

}