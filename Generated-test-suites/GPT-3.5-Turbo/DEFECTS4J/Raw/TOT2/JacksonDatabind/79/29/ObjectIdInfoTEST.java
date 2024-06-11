package com.fasterxml.jackson.databind.introspect;

import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdInfoTest {
    
    @Test
    public void testObjectIdInfoConstructorWithResolver() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("id"), String.class, ObjectIdGenerator.class, SimpleObjectIdResolver.class);
        assertNotNull(objectIdInfo);
        assertEquals("id", objectIdInfo.getPropertyName().getSimpleName());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(ObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }
    
    @Test
    public void testObjectIdInfoConstructorWithoutResolver() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("name"), Long.class, ObjectIdGenerator.class);
        assertNotNull(objectIdInfo);
        assertEquals("name", objectIdInfo.getPropertyName().getSimpleName());
        assertEquals(Long.class, objectIdInfo.getScope());
        assertEquals(ObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }
    
    @Test
    public void testObjectIdInfoConstructorWithStringName() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo("objectId", Integer.class, ObjectIdGenerator.class);
        assertNotNull(objectIdInfo);
        assertEquals("objectId", objectIdInfo.getPropertyName().getSimpleName());
        assertEquals(Integer.class, objectIdInfo.getScope());
        assertEquals(ObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }
    
    @Test
    public void testObjectIdInfoWithAlwaysAsId() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("name"), Long.class, ObjectIdGenerator.class);
        ObjectIdInfo newObjectIdInfo = objectIdInfo.withAlwaysAsId(true);
        assertNotNull(newObjectIdInfo);
        assertEquals("name", newObjectIdInfo.getPropertyName().getSimpleName());
        assertEquals(Long.class, newObjectIdInfo.getScope());
        assertEquals(ObjectIdGenerator.class, newObjectIdInfo.getGeneratorType());
        assertNull(newObjectIdInfo.getResolverType());
        assertTrue(newObjectIdInfo.getAlwaysAsId());
    }
    
    @Test
    public void testObjectIdInfoToString() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("id"), String.class, ObjectIdGenerator.class);
        String expectedString = "ObjectIdInfo: propName=id, scope=java.lang.String, generatorType=com.fasterxml.jackson.databind.introspect.ObjectIdGenerator, alwaysAsId=false";
        assertEquals(expectedString, objectIdInfo.toString());
    }
}