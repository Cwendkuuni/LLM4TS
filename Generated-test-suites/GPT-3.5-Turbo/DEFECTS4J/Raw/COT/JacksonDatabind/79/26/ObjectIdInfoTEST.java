
package com.fasterxml.jackson.databind.introspect;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ObjectIdInfoTest {

    private ObjectIdInfo objectIdInfo;

    @Before
    public void setUp() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = MyObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = MyObjectIdResolver.class;
        objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);
    }

    @Test
    public void testWithAlwaysAsId() {
        assertFalse(objectIdInfo.getAlwaysAsId());
        ObjectIdInfo newObjectIdInfo = objectIdInfo.withAlwaysAsId(true);
        assertTrue(newObjectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testGetPropertyName() {
        PropertyName propertyName = objectIdInfo.getPropertyName();
        assertNotNull(propertyName);
        assertEquals("testProperty", propertyName.getSimpleName());
    }

    @Test
    public void testGetScope() {
        Class<?> scope = objectIdInfo.getScope();
        assertNotNull(scope);
        assertEquals(String.class, scope);
    }

    @Test
    public void testGetGeneratorType() {
        Class<? extends ObjectIdGenerator<?>> generatorType = objectIdInfo.getGeneratorType();
        assertNotNull(generatorType);
        assertEquals(MyObjectIdGenerator.class, generatorType);
    }

    @Test
    public void testGetResolverType() {
        Class<? extends ObjectIdResolver> resolverType = objectIdInfo.getResolverType();
        assertNotNull(resolverType);
        assertEquals(MyObjectIdResolver.class, resolverType);
    }

    @Test
    public void testGetAlwaysAsId() {
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

}

class MyObjectIdGenerator implements ObjectIdGenerator<Object> {
    // implementation
}

class MyObjectIdResolver implements ObjectIdResolver {
    // implementation
}
