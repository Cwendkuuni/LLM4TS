package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {
    
    @Test
    public void testBaseTypeName() {
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBaseTestImpl(null, null, null, false, null);
        String baseTypeName = typeDeserializer.baseTypeName();
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBaseTestImpl", baseTypeName);
    }
    
    private class TypeDeserializerBaseTestImpl extends TypeDeserializerBase {

        protected TypeDeserializerBaseTestImpl(JavaType baseType, TypeIdResolver idRes, String typePropertyName, boolean typeIdVisible, JavaType defaultImpl) {
            super(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return null;
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return null;
        }
    }
}