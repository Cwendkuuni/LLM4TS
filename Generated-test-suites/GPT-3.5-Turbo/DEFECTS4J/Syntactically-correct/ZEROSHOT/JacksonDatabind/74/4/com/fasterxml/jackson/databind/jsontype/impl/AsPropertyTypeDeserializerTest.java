package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.util.JsonParserSequence;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class AsPropertyTypeDeserializerTest {

    @Test
    public void testConstructor1() {
        JavaType bt = Mockito.mock(JavaType.class);
        TypeIdResolver idRes = Mockito.mock(TypeIdResolver.class);
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = Mockito.mock(JavaType.class);
        As inclusion = As.PROPERTY;
        AsPropertyTypeDeserializer deserializer1 = new AsPropertyTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl, inclusion);
        Assert.assertEquals(bt, deserializer1._baseType);
        Assert.assertEquals(idRes, deserializer1._idResolver);
        Assert.assertEquals(typePropertyName, deserializer1._typePropertyName);
        Assert.assertEquals(typeIdVisible, deserializer1._typeIdVisible);
        Assert.assertEquals(defaultImpl, deserializer1._defaultImpl);
        Assert.assertEquals(inclusion, deserializer1._inclusion);
    }

    @Test
    public void testConstructor2() {
        JavaType bt = Mockito.mock(JavaType.class);
        TypeIdResolver idRes = Mockito.mock(TypeIdResolver.class);
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = Mockito.mock(JavaType.class);
        AsPropertyTypeDeserializer deserializer1 = new AsPropertyTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl);
        Assert.assertEquals(bt, deserializer1._baseType);
        Assert.assertEquals(idRes, deserializer1._idResolver);
        Assert.assertEquals(typePropertyName, deserializer1._typePropertyName);
        Assert.assertEquals(typeIdVisible, deserializer1._typeIdVisible);
        Assert.assertEquals(defaultImpl, deserializer1._defaultImpl);
        Assert.assertEquals(As.PROPERTY, deserializer1._inclusion);
    }

    @Test
    public void testForProperty() {
        JavaType bt = Mockito.mock(JavaType.class);
        TypeIdResolver idRes = Mockito.mock(TypeIdResolver.class);
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = Mockito.mock(JavaType.class);
        AsPropertyTypeDeserializer deserializer1 = new AsPropertyTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl);
        BeanProperty prop = Mockito.mock(BeanProperty.class);
        TypeDeserializer result = deserializer1.forProperty(prop);
        Assert.assertEquals(deserializer1, result);
    }

    @Test
    public void testGetTypeInclusion() {
        JavaType bt = Mockito.mock(JavaType.class);
        TypeIdResolver idRes = Mockito.mock(TypeIdResolver.class);
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = Mockito.mock(JavaType.class);
        AsPropertyTypeDeserializer deserializer1 = new AsPropertyTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl);
        As result = deserializer1.getTypeInclusion();
        Assert.assertEquals(As.PROPERTY, result);
    }

    @Test
    public void testDeserializeTypedFromObject1() throws IOException {
        JavaType bt = Mockito.mock(JavaType.class);
        TypeIdResolver idRes = Mockito.mock(TypeIdResolver.class);
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = Mockito.mock(JavaType.class);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl);
        JsonParser p = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object result = deserializer.deserializeTypedFromObject(p, ctxt);
        Assert.assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromObject2() throws IOException {
        JavaType bt = Mockito.mock(JavaType.class);
        TypeIdResolver idRes = Mockito.mock(TypeIdResolver.class);
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = Mockito.mock(JavaType.class);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl);
        JsonParser p = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Mockito.doReturn(true).when(p).canReadTypeId();
        Mockito.doReturn("id").when(p).getTypeId();
        Object result = deserializer.deserializeTypedFromObject(p, ctxt);
        Assert.assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromObject3() throws IOException {
        JavaType bt = Mockito.mock(JavaType.class);
        TypeIdResolver idRes = Mockito.mock(TypeIdResolver.class);
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = Mockito.mock(JavaType.class);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl);
        JsonParser p = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Mockito.doReturn(JsonToken.FIELD_NAME).when(p).getCurrentToken();
        Mockito.doReturn(JsonToken.START_OBJECT).when(p).nextToken();
        Object result = deserializer.deserializeTypedFromObject(p, ctxt);
        Assert.assertNull(result);
    }

    @Test
    public void testDeserializeTypedUsingDefaultImpl() throws IOException {
        JavaType bt = Mockito.mock(JavaType.class);
        TypeIdResolver idRes = Mockito.mock(TypeIdResolver.class);
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = Mockito.mock(JavaType.class);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl);
        JsonParser p = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        TokenBuffer tb = Mockito.mock(TokenBuffer.class);
        Object result = deserializer._deserializeTypedUsingDefaultImpl(p, ctxt, tb);
        Assert.assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromAny() throws IOException {
        JavaType bt = Mockito.mock(JavaType.class);
        TypeIdResolver idRes = Mockito.mock(TypeIdResolver.class);
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = Mockito.mock(JavaType.class);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl);
        JsonParser p = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Mockito.doReturn(JsonToken.START_ARRAY).when(p).getCurrentToken();
        Object result = deserializer.deserializeTypedFromAny(p, ctxt);
        Assert.assertNull(result);
    }

    @Test
    public void testDeserializeTypedForId() throws IOException {
        JavaType bt = Mockito.mock(JavaType.class);
        TypeIdResolver idRes = Mockito.mock(TypeIdResolver.class);
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = Mockito.mock(JavaType.class);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl);
        JsonParser p = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        TokenBuffer tb = Mockito.mock(TokenBuffer.class);
        Mockito.doReturn("id").when(p).getText();
        Object result = deserializer._deserializeTypedForId(p, ctxt, tb);
        Assert.assertNull(result);
    }
}