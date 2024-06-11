package com.fasterxml.jackson.databind.introspect;

import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfoConstructor() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("test"), String.class, MyObjectIdGenerator.class, MyObjectIdResolver.class);
        assertNotNull(objectIdInfo);
        assertEquals("test", objectIdInfo.getPropertyName().getSimpleName());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(MyObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertEquals(MyObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoToString() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("test"), String.class, MyObjectIdGenerator.class, MyObjectIdResolver.class);
        String expected = "ObjectIdInfo: propName=test, scope=java.lang.String, generatorType=com.example.MyObjectIdGenerator, alwaysAsId=false";
        assertEquals(expected, objectIdInfo.toString());
    }

    @Test
    public void testWithAlwaysAsId() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("test"), String.class, MyObjectIdGenerator.class, MyObjectIdResolver.class);
        assertFalse(objectIdInfo.getAlwaysAsId());

        ObjectIdInfo updatedInfo = objectIdInfo.withAlwaysAsId(true);
        assertTrue(updatedInfo.getAlwaysAsId());
    }

    // Add more test cases for other methods as needed

}