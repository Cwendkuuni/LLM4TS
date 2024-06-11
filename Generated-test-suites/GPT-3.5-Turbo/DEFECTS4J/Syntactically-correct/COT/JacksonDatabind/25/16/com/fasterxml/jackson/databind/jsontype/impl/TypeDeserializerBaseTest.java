package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

    @Test
    public void testBaseTypeName() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase() {
            @Override
            public TypeIdResolver getTypeIdResolver() {
                return null;
            }

            @Override
            public Object deserializeTypedFromAny(JsonParser p, DeserializationContext ctxt) {
                return null;
            }

            @Override
            public Object deserializeTypedFromArray(JsonParser p, DeserializationContext ctxt) {
                return null;
            }

            @Override
            public Object deserializeTypedFromObject(JsonParser p, DeserializationContext ctxt) {
                return null;
            }

            @Override
            public Object deserializeTypedFromScalar(JsonParser p, DeserializationContext ctxt) {
                return null;
            }

            @Override
            public Object deserializeTypedFromAny(JsonParser p, DeserializationContext ctxt, JsonDeserializer<?> deser) {
                return null;
            }

            @Override
            public Object deserializeTypedFromArray(JsonParser p, DeserializationContext ctxt, JsonDeserializer<?> deser) {
                return null;
            }

            @Override
            public Object deserializeTypedFromObject(JsonParser p, DeserializationContext ctxt, JsonDeserializer<?> deser) {
                return null;
            }

            @Override
            public Object deserializeTypedFromScalar(JsonParser p, DeserializationContext ctxt, JsonDeserializer<?> deser) {
                return null;
            }

            @Override
            public Object deserializeTypedFromAny(JsonParser p, DeserializationContext ctxt, BeanProperty property) {
                return null;
            }

            @Override
            public Object deserializeTypedFromArray(JsonParser p, DeserializationContext ctxt, BeanProperty property) {
                return null;
            }

            @Override
            public Object deserializeTypedFromObject(JsonParser p, DeserializationContext ctxt, BeanProperty property) {
                return null;
            }

            @Override
            public Object deserializeTypedFromScalar(JsonParser p, DeserializationContext ctxt, BeanProperty property) {
                return null;
            }

            @Override
            public JavaType getDefaultImpl() {
                return null;
            }

            @Override
            public String getPropertyName() {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }

            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
        };
        String name = typeDeserializerBase.baseTypeName();
        assertEquals("java.lang.Object", name);
    }

    @Test
    public void testGetProperty(){
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase() {
            @Override
            public TypeIdResolver getTypeIdResolver() {
                return null;
            }

            @Override
            public Object deserializeTypedFromAny(JsonParser p, DeserializationContext ctxt) {
                return null;
            }

            @Override
            public Object deserializeTypedFromArray(JsonParser p, DeserializationContext ctxt) {
                return null;
            }

            @Override
            public Object deserializeTypedFromObject(JsonParser p, DeserializationContext ctxt) {
                return null;
            }

            @Override
            public Object deserializeTypedFromScalar(JsonParser p, DeserializationContext ctxt) {
                return null;
            }

            @Override
            public Object deserializeTypedFromAny(JsonParser p, DeserializationContext ctxt, JsonDeserializer<?> deser) {
                return null;
            }

            @Override
            public Object deserializeTypedFromArray(JsonParser p, DeserializationContext ctxt, JsonDeserializer<?> deser) {
                return null;
            }

            @Override
            public Object deserializeTypedFromObject(JsonParser p, DeserializationContext ctxt, JsonDeserializer<?> deser) {
                return null;
            }

            @Override
            public Object deserializeTypedFromScalar(JsonParser p, DeserializationContext ctxt, JsonDeserializer<?> deser) {
                return null;
            }

            @Override
            public Object deserializeTypedFromAny(JsonParser p, DeserializationContext ctxt, BeanProperty property) {
                return null;
            }

            @Override
            public Object deserializeTypedFromArray(JsonParser p, DeserializationContext ctxt, BeanProperty property) {
                return null;
            }

            @Override
            public Object deserializeTypedFromObject(JsonParser p, DeserializationContext ctxt, BeanProperty property) {
                return null;
            }

            @Override
            public Object deserializeTypedFromScalar(JsonParser p, DeserializationContext ctxt, BeanProperty property) {
                return null;
            }

            @Override
            public JavaType getDefaultImpl() {
                return null;
            }

            @Override
            public String getPropertyName() {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }

            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
        };
        String name = typeDeserializerBase.getPropertyName();
        assertNull(name);
    }
}