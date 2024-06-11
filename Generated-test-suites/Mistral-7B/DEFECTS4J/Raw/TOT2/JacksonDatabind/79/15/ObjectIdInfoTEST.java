
package com.example;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;

public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfoConstructor() {
        PropertyName propertyName = new PropertyName("testPropName");
        Class<String> scope = String.class;
        Class<CustomObjectIdGenerator> generator = CustomObjectIdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false);
        assertNotNull(objectIdInfo);
        assertEquals("testPropName", objectIdInfo.getPropertyName().getName());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(CustomObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test(expected = NullPointerException.class)
    public void testObjectIdInfoConstructorWithNullArguments() {
        new ObjectIdInfo(null, null, null, false);
    }

    @Test
    public void testGetters() {
        PropertyName propertyName = new PropertyName("testPropName");
        Class<String> scope = String.class;
        Class<CustomObjectIdGenerator> generator = CustomObjectIdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false);
        assertEquals("testPropName", objectIdInfo.getPropertyName().getName());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(CustomObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertFalse(objectIdInfo.getAlwaysAsId());
        assertEquals("ObjectIdInfo: propName=testPropName, scope=class java.lang.String, generatorType=class com.custom.CustomObjectIdGenerator, alwaysAsId=false", objectIdInfo.toString());
    }

    @Test
    public void testObjectIdInfoWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("testPropName");
        Class<String> scope = String.class;
        Class<CustomObjectIdGenerator> generator = CustomObjectIdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, true);
        assertTrue(objectIdInfo.getAlwaysAsId());
    }
}
