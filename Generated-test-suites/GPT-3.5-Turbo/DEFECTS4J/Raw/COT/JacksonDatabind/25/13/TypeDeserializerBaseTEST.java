
package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

    private TypeDeserializerBase deserializer;

    @Before
    public void setUp() {
        deserializer = new TypeDeserializerBase(null, null, null, false, null) {
            @Override
            public org.codehaus.jackson.annotate.JsonTypeInfo.As getTypeInclusion() {
                return null;
            }

            @Override
            public TypeDeserializer forProperty(org.codehaus.jackson.map.BeanProperty prop) {
                return null;
            }

        };
    }

    @Test
    public void testBaseTypeName() {
        String typeName = deserializer.baseTypeName();
        assertNotNull(typeName);
        assertEquals("java.lang.Object", typeName);
    }

    @Test
    public void testGetPropertyName() {
        String propertyName = deserializer.getPropertyName();
        assertNull(propertyName);
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeResolverBuilder<?> resolver = deserializer.getTypeIdResolver();
        assertNull(resolver);
    }

    @Test
    public void testGetDefaultImpl() {
        Class<?> defaultImpl = deserializer.getDefaultImpl();
        assertNull(defaultImpl);
    }
}
