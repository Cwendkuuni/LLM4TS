package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;

public class ObjectIdInfoTest {

    private ObjectIdInfo objectIdInfo;

    @Before
    public void setUp() {
        objectIdInfo = new ObjectIdInfo(new PropertyName("test"), null, null, false);
    }

    @Test
    public void testConstructorWithPropertyNameAndScopeAndGeneratorAndResolver() {
        assertNotNull(objectIdInfo);
        assertEquals("test", objectIdInfo.getPropertyName().getSimpleName());
        assertNull(objectIdInfo.getScope());
        assertNull(objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithPropertyNameAndScopeAndGeneratorAndAlwaysAsIdAndResolver() {
        objectIdInfo = new ObjectIdInfo(new PropertyName("test2"), null, null, true, SimpleObjectIdResolver.class);
        assertNotNull(objectIdInfo);
        assertEquals("test2", objectIdInfo.getPropertyName().getSimpleName());
        assertNull(objectIdInfo.getScope());
        assertNull(objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertTrue(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testWithAlwaysAsId() {
        objectIdInfo = objectIdInfo.withAlwaysAsId(true);
        assertTrue(objectIdInfo.getAlwaysAsId());
        objectIdInfo = objectIdInfo.withAlwaysAsId(false);
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testToString() {
        String expected = "ObjectIdInfo: propName=test, scope=null, generatorType=null, alwaysAsId=false";
        assertEquals(expected, objectIdInfo.toString());
    }
}