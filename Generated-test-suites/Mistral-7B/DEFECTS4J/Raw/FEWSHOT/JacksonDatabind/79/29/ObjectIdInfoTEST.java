package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.*;

import org.junit.Test;

public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfoConstruction() {
        PropertyName propName = new PropertyName("id");
        Class<String> scope = String.class;
        Class<ObjectIdGenerators.PropertyNameHashCodeGenerator> gen = ObjectIdGenerators.PropertyNameHashCodeGenerator.class;

        ObjectIdInfo objIdInfo1 = new ObjectIdInfo(propName, scope, gen, false, null);
        ObjectIdInfo objIdInfo2 = new ObjectIdInfo(propName, scope, gen, false, SimpleObjectIdResolver.class);
        ObjectIdInfo objIdInfo3 = new ObjectIdInfo(propName, scope, gen, true, SimpleObjectIdResolver.class);

        assertEquals(propName, objIdInfo1.getPropertyName());
        assertEquals(scope, objIdInfo1.getScope());
        assertEquals(gen, objIdInfo1.getGeneratorType());
        assertNull(objIdInfo1.getResolverType());
        assertFalse(objIdInfo1.getAlwaysAsId());

        assertEquals(propName, objIdInfo2.getPropertyName());
        assertEquals(scope, objIdInfo2.getScope());
        assertEquals(gen, objIdInfo2.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objIdInfo2.getResolverType());
        assertFalse(objIdInfo2.getAlwaysAsId());

        assertEquals(propName, objIdInfo3.getPropertyName());
        assertEquals(scope, objIdInfo3.getScope());
        assertEquals(gen, objIdInfo3.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objIdInfo3.getResolverType());
        assertTrue(objIdInfo3.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoWithAlwaysAsId() {
        PropertyName propName = new PropertyName("id");
        Class<String> scope = String.class;
        Class<ObjectIdGenerators.PropertyNameHashCodeGenerator> gen = ObjectIdGenerators.PropertyNameHashCodeGenerator.class;

        ObjectIdInfo objIdInfo = new ObjectIdInfo(propName, scope, gen, false, SimpleObjectIdResolver.class);
        ObjectIdInfo objIdInfoWithAlwaysAsId = objIdInfo.withAlwaysAsId(true);

        assertNotSame(objIdInfo, objIdInfoWithAlwaysAsId);
        assertTrue(objIdInfoWithAlwaysAsId.getAlwaysAsId());
    }

    @Test
    public void testToString() {
        PropertyName propName = new PropertyName("id");
        Class<String> scope = String.class;
        Class<ObjectIdGenerators.PropertyNameHashCodeGenerator> gen = ObjectIdGenerators.PropertyNameHashCodeGenerator.class;

        ObjectIdInfo objIdInfo = new ObjectIdInfo(propName, scope, gen, false, SimpleObjectIdResolver.class);
        String expectedToString = "ObjectIdInfo: propName=id, scope=class java.lang.String, generatorType=class com.fasterxml.jackson.databind.introspect.ObjectIdGenerators$PropertyNameHashCodeGenerator, alwaysAsId=false";

        assertEquals(expectedToString, objIdInfo.toString());
    }
}