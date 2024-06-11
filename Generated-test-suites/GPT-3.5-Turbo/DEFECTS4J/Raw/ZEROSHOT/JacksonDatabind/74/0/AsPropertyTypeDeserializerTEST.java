package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonParser;

public class AsPropertyTypeDeserializerTest {

    private AsPropertyTypeDeserializer deserializer;

    @Before
    public void setup() {
        TypeIdResolver idRes = null;
        JavaType bt = TypeFactory.defaultInstance().constructType(String.class);
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = TypeFactory.defaultInstance().constructType(Object.class);
        deserializer = new AsPropertyTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl);
    }

    @Test
    public void testConstructor1() {
        assertNotNull(deserializer);
        assertEquals(As.PROPERTY, deserializer.getTypeInclusion());
    }

    @Test
    public void testConstructor2() {
        TypeIdResolver idRes = null;
        JavaType bt = TypeFactory.defaultInstance().constructType(String.class);
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = TypeFactory.defaultInstance().constructType(Object.class);
        AsPropertyTypeDeserializer des = new AsPropertyTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl, As.WRAPPER_ARRAY);
        assertNotNull(des);
        assertEquals(As.WRAPPER_ARRAY, des.getTypeInclusion());
    }

    @Test
    public void testCopyConstructor() {
        BeanProperty bp = null;
        AsPropertyTypeDeserializer des = new AsPropertyTypeDeserializer(deserializer, bp);
        assertNotNull(des);
        assertEquals(deserializer.getTypeInclusion(), des.getTypeInclusion());
    }

    @Test
    public void testForProperty() {
        BeanProperty bp = null;
        TypeDeserializer td = deserializer.forProperty(bp);
        assertNotNull(td);
        assertEquals(deserializer.getTypeInclusion(), td.getTypeInclusion());
    }

    @Test
    public void testDeserializeTypedFromObject1() throws IOException {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object obj = deserializer.deserializeTypedFromObject(p, ctxt);
        assertNull(obj);
    }

    @Test
    public void testDeserializeTypedFromObject2() throws IOException {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        p.nextToken();
        Object obj = deserializer.deserializeTypedFromObject(p, ctxt);
        assertNotNull(obj);
    }

    @Test
    public void testDeserializeTypedFromObject3() throws IOException {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        p.nextToken();
        p.nextToken();
        p.nextToken();
        Object obj = deserializer.deserializeTypedFromObject(p, ctxt);
        assertNotNull(obj);
    }

    @Test
    public void testDeserializeTypedFromObject4() throws IOException {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        p.nextToken();
        p.nextToken();
        p.nextToken();
        p.nextToken();
        Object obj = deserializer.deserializeTypedFromObject(p, ctxt);
        assertNotNull(obj);
    }

    @Test
    public void testDeserializeTypedFromObject5() throws IOException {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        p.nextToken();
        p.nextToken();
        p.nextToken();
        p.nextToken();
        p.nextToken();
        p.nextToken();
        Object obj = deserializer.deserializeTypedFromObject(p, ctxt);
        assertNotNull(obj);
    }

    @Test
    public void testDeserializeTypedForId() throws IOException {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        TokenBuffer tb = null;
        Object obj = deserializer._deserializeTypedForId(p, ctxt, tb);
        assertNull(obj);
    }
    
    @Test
    public void testDeserializeTypedForIdWithTypeId() throws IOException {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        TokenBuffer tb = null;
        p.setText("testType");
        Object obj = deserializer._deserializeTypedForId(p, ctxt, tb);
        assertNull(obj);
    }

    @Test
    public void testDeserializeTypedUsingDefaultImpl() throws IOException {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        TokenBuffer tb = null;
        Object obj = deserializer._deserializeTypedUsingDefaultImpl(p, ctxt, tb);
        assertNull(obj);
    }

    @Test
    public void testDeserializeTypedUsingDefaultImplWithDefaultImpl() throws IOException {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        TokenBuffer tb = null;
        p.nextToken();
        Object obj = deserializer._deserializeTypedUsingDefaultImpl(p, ctxt, tb);
        assertNotNull(obj);
    }

    @Test
    public void testDeserializeTypedFromAny() throws IOException {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object obj = deserializer.deserializeTypedFromAny(p, ctxt);
        assertNull(obj);
    }

    @Test
    public void testDeserializeTypedFromArray() throws IOException {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object obj = deserializer.deserializeTypedFromArray(p, ctxt);
        assertNull(obj);
    }

    @Test
    public void testDeserializeTypedFromScalar() throws IOException {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object obj = deserializer.deserializeTypedFromScalar(p, ctxt);
        assertNull(obj);
    }
}