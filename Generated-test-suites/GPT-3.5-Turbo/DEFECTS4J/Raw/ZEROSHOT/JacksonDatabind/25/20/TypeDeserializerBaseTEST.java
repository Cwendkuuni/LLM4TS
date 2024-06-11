package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

public class TypeDeserializerBaseTest {

    @Mock
    private DeserializationContext ctxt;
    @Mock
    private JsonDeserializer<Object> deser;
    private TypeDeserializerBase deserializer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        deserializer = new TypeDeserializerBase(null, null, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }

            @Override
            public Object deserializeTypedFromAny(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return null;
            }

            @Override
            public Object deserializeTypedFromArray(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return null;
            }

            @Override
            public Object deserializeTypedFromObject(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return null;
            }

            @Override
            public Object deserializeTypedFromScalar(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return null;
            }

            @Override
            public Object deserializeTypedFromAny(JsonParser jp, DeserializationContext ctxt, JsonDeserializer<?> deser) throws IOException {
                return null;
            }

            @Override
            public Object deserializeTypedFromArray(JsonParser jp, DeserializationContext ctxt, JsonDeserializer<?> deser) throws IOException {
                return null;
            }

            @Override
            public Object deserializeTypedFromObject(JsonParser jp, DeserializationContext ctxt, JsonDeserializer<?> deser) throws IOException {
                return null;
            }

            @Override
            public Object deserializeTypedFromScalar(JsonParser jp, DeserializationContext ctxt, JsonDeserializer<?> deser) throws IOException {
                return null;
            }
        };
        deserializer._deserializers.put("testId", deser);
    }

    @Test
    public void testBaseTypeName() {
        Assert.assertNull(deserializer.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        Assert.assertNull(deserializer.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        Assert.assertNull(deserializer.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() {
        Assert.assertNull(deserializer.getDefaultImpl());
    }

    @Test
    public void testToString() {
        Assert.assertNotNull(deserializer.toString());
    }

    @Test
    public void test_findDeserializer() throws IOException {
        Assert.assertEquals(deser, deserializer._findDeserializer(ctxt, "testId"));
    }

    @Test
    public void test_findDefaultImplDeserializer() throws IOException {
        Assert.assertNull(deserializer._findDefaultImplDeserializer(ctxt));
    }

    @Test
    public void test_deserializeWithNativeTypeId() throws IOException {
        Assert.assertNull(deserializer._deserializeWithNativeTypeId(null, ctxt));
    }

    @Test
    public void test_handleUnknownTypeId() throws IOException {
        Assert.assertNull(deserializer._handleUnknownTypeId(ctxt, "testId", null, null));
    }
}