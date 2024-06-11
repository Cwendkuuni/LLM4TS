package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfoConstructorWithResolver() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), String.class, null, SimpleObjectIdResolver.class);
        assertNotNull(objectIdInfo);
        assertEquals("testProperty", objectIdInfo.getPropertyName().getSimpleName());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(null, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertEquals(false, objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoWithAlwaysAsId() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), String.class, null, false, SimpleObjectIdResolver.class);
        ObjectIdInfo updatedObjectIdInfo = objectIdInfo.withAlwaysAsId(true);
        assertNotNull(updatedObjectIdInfo);
        assertEquals(true, updatedObjectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testToString() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), String.class, null, SimpleObjectIdResolver.class);
        String expectedString = "ObjectIdInfo: propName=testProperty, scope=java.lang.String, generatorType=null, alwaysAsId=false";
        assertEquals(expectedString, objectIdInfo.toString());
    }
}