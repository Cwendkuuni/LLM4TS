
package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;
import org.junit.Before;
import org.junit.Test;

public class ObjectIdInfoTest {
    private ObjectIdInfo objectIdInfo;

    @Before
    public void setUp() {
        objectIdInfo = new ObjectIdInfo(new PropertyName("name"), String.class, ObjectIdGenerator.class, ObjectIdResolver.class);
    }

    @Test
    public void testGetPropertyName() {
        assertEquals(new PropertyName("name"), objectIdInfo.getPropertyName());
    }

    @Test
    public void testGetScope() {
        assertEquals(String.class, objectIdInfo.getScope());
    }

    @Test
    public void testGetGeneratorType() {
        assertEquals(ObjectIdGenerator.class, objectIdInfo.getGeneratorType());
    }

    @Test
    public void testGetResolverType() {
        assertEquals(ObjectIdResolver.class, objectIdInfo.getResolverType());
    }

    @Test
    public void testGetAlwaysAsId() {
        assertTrue(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testWithAlwaysAsId() {
        ObjectIdInfo newObjectIdInfo = objectIdInfo.withAlwaysAsId(false);
        assertEquals(objectIdInfo.getPropertyName(), newObjectIdInfo.getPropertyName());
        assertEquals(objectIdInfo.getScope(), newObjectIdInfo.getScope());
        assertEquals(objectIdInfo.getGeneratorType(), newObjectIdInfo.getGeneratorType());
        assertEquals(objectIdInfo.getResolverType(), newObjectIdInfo.getResolverType());
        assertEquals(false, newObjectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testToString() {
        String expected = "ObjectIdInfo: propName=name, scope=java.lang.String, generatorType=com.fasterxml.jackson.annotation.ObjectIdGenerator, alwaysAsId=true";
        assertEquals(expected, objectIdInfo.toString());
    }
}
