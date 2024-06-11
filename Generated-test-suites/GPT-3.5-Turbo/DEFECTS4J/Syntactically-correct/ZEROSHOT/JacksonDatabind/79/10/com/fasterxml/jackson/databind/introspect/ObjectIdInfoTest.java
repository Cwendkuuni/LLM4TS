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

    private static final PropertyName PROPERTY_NAME = new PropertyName("testProperty");
    private static final Class<?> SCOPE = String.class;
    private static final Class<? extends ObjectIdGenerator<?>> GENERATOR_TYPE = ObjectIdGenerator.class;
    private static final Class<? extends ObjectIdResolver> RESOLVER_TYPE = SimpleObjectIdResolver.class;

    private ObjectIdInfo objectIdInfo;

    @Before
    public void setUp() {
        objectIdInfo = new ObjectIdInfo(PROPERTY_NAME, SCOPE, GENERATOR_TYPE, RESOLVER_TYPE);
    }

    @Test
    public void testGetPropertyName() {
        assertEquals(PROPERTY_NAME, objectIdInfo.getPropertyName());
    }

    @Test
    public void testGetScope() {
        assertEquals(SCOPE, objectIdInfo.getScope());
    }

    @Test
    public void testGetGeneratorType() {
        assertEquals(GENERATOR_TYPE, objectIdInfo.getGeneratorType());
    }

    @Test
    public void testGetResolverType() {
        assertEquals(RESOLVER_TYPE, objectIdInfo.getResolverType());
    }

    @Test
    public void testGetAlwaysAsId() {
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testWithAlwaysAsId() {
        ObjectIdInfo updatedInfo = objectIdInfo.withAlwaysAsId(true);
        assertTrue(updatedInfo.getAlwaysAsId());
    }

    @Test
    public void testToString() {
        String expectedString = "ObjectIdInfo: propName=testProperty, scope=java.lang.String, generatorType=com.fasterxml.jackson.annotation.ObjectIdGenerator, alwaysAsId=false";
        assertEquals(expectedString, objectIdInfo.toString());
    }
}