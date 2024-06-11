package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;
import org.junit.Test;

public class TypeDeserializerBaseTest {

    @Test
    public void testBaseTypeName() {
        TypeDeserializerBase base = new TypeDeserializerBase() {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        assertEquals("java.lang.Object", base.baseTypeName());
    }
    
    @Test
    public void testFindDeserializer() {
        TypeDeserializerBase base = new TypeDeserializerBase() {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        // Test for null type id
        DeserializationContext ctxt = null;
        String typeId = null;
        try {
            base._findDeserializer(ctxt, typeId);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // Expected IOException
        }

        // Test for unknown type id
        ctxt = null;
        typeId = "unknown_id";
        try {
            base._findDeserializer(ctxt, typeId);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // Expected IOException
        }
    }

    @Test
    public void testDeserializeWithNativeTypeId() {
        TypeDeserializerBase base = new TypeDeserializerBase() {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        // Test for null type id
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        try {
            base._deserializeWithNativeTypeId(jp, ctxt);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // Expected IOException
        }
    }
}