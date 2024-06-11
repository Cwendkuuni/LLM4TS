package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;

import org.junit.Test;

public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfo() {
        PropertyName propName = new PropertyName("id");
        Class<Integer> scope = Integer.class;
        Class<CustomIdGenerator> generator = CustomIdGenerator.class;
        Class<SimpleObjectIdResolver> resolver = SimpleObjectIdResolver.class;
        ObjectIdInfo objIdInfo = new ObjectIdInfo(propName, scope, generator, false, resolver);

        assertEquals(propName, objIdInfo.getPropertyName());
        assertEquals(scope, objIdInfo.getScope());
        assertEquals(generator, objIdInfo.getGeneratorType());
        assertEquals(resolver, objIdInfo.getResolverType());
        assertFalse(objIdInfo.getAlwaysAsId());

        ObjectIdInfo objIdInfoWithAlwaysId = objIdInfo.withAlwaysAsId(true);
        assertTrue(objIdInfoWithAlwaysId.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoWithNullPropertyName() {
        assertNull(new ObjectIdInfo(null, null, null, false, null));
    }

    @Test
    public void testObjectIdInfoWithNullScope() {
        assertNull(new ObjectIdInfo(new PropertyName("id"), null, null, false, null));
    }

    @Test
    public void testObjectIdInfoWithNullGeneratorType() {
        assertNull(new ObjectIdInfo(new PropertyName("id"), null, null, false, null));
    }

    @Test
    public void testObjectIdInfoWithNullResolverType() {
        Class<CustomObjectIdResolver> resolver = CustomObjectIdResolver.class;
        ObjectIdInfo objIdInfo = new ObjectIdInfo(new PropertyName("id"), null, null, false, null);
        ObjectIdInfo objIdInfoWithResolver = objIdInfo.withResolverType(resolver);
        assertEquals(resolver, objIdInfoWithResolver.getResolverType());
    }

    @Test
    public void testObjectIdInfoWithDefaultResolverType() {
        ObjectIdInfo objIdInfo = new ObjectIdInfo(new PropertyName("id"), null, null, false, null);
        assertEquals(SimpleObjectIdResolver.class, objIdInfo.getResolverType());
    }

    @Test
    public void testObjectIdInfoToString() {
        PropertyName propName = new PropertyName("id");
        Class<Integer> scope = Integer.class;
        Class<CustomIdGenerator> generator = CustomIdGenerator.class;
        ObjectIdInfo objIdInfo = new ObjectIdInfo(propName, scope, generator, false, null);
        String expected = "ObjectIdInfo: propName=id, scope=class int, generatorType=class com.example.CustomIdGenerator, alwaysAsId=false";
        assertEquals(expected, objIdInfo.toString());
    }
}

class CustomIdGenerator implements ObjectIdGenerator<Object> {
    // Add implementation here, if needed
}

class CustomObjectIdResolver implements ObjectIdResolver {
    // Add implementation here, if needed
}