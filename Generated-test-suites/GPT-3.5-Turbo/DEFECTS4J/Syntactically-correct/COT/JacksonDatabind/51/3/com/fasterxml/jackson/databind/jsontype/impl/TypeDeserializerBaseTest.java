
package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TypeDeserializerBaseTest {

    @Test
    public void testBaseTypeName() {
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBaseStub();
        String expected = "java.lang.Object";
        assertEquals(expected, typeDeserializer.baseTypeName());
    }
    
    @Test
    public void testGetPropertyName() {
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBaseStub();
        String expected = "";
        assertEquals(expected, typeDeserializer.getPropertyName());
    }
    
    @Test
    public void testGetTypeIdResolver() {
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBaseStub();
        TypeIdResolver expected = null;
        assertEquals(expected, typeDeserializer.getTypeIdResolver());
    }
    
    @Test
    public void testGetDefaultImpl() {
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBaseStub();
        Class<?> expected = null;
        assertEquals(expected, typeDeserializer.getDefaultImpl());
    }

    private static class TypeDeserializerBaseStub extends TypeDeserializerBase {

        protected TypeDeserializerBaseStub() {
            super(null, null, "", false, null);
        }

        @Override
        public org.codehaus.jackson.annotate.JsonTypeInfo.As getTypeInclusion() {
            return null;
        }

        @Override
        public TypeDeserializer forProperty(org.codehaus.jackson.map.BeanProperty prop) {
            return null;
        }
    }
}
