package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.DeserializerProvider;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;

public class TypeDeserializerBaseTest {
    
    @Test
    public void testBaseType() {
        JavaType baseType = Mockito.mock(JavaType.class);
        TypeDeserializerBase deserializerBase = new TypeDeserializerBaseTester(baseType);
        assertSame(baseType, deserializerBase.baseType());
    }
    
    private static class TypeDeserializerBaseTester extends TypeDeserializerBase {

        protected TypeDeserializerBaseTester(JavaType baseType) {
            super(baseType, null, null, false, baseType);
        }

        @Override
        public Object deserializeTypedFromAny(JsonParser p, DeserializationContext ctxt) throws IOException {
            return null;
        }

        @Override
        public Object deserializeTypedFromArray(JsonParser p, DeserializationContext ctxt) throws IOException {
            return null;
        }

        @Override
        public Object deserializeTypedFromObject(JsonParser p, DeserializationContext ctxt) throws IOException {
            return null;
        }

        @Override
        public Object deserializeTypedFromScalar(JsonParser p, DeserializationContext ctxt) throws IOException {
            return null;
        }

        @Override
        public Object deserializeTypedFromTree(JsonParser p, DeserializationContext ctxt) throws IOException {
            return null;
        }

        @Override
        public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializerOfChild) throws IOException {
            return null;
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return null;
        }

        @Override
        public Object deserializeWithType(JsonParser p, DeserializationContext ctxt) throws IOException {
            return null;
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return null;
        }
        
    }
    
}