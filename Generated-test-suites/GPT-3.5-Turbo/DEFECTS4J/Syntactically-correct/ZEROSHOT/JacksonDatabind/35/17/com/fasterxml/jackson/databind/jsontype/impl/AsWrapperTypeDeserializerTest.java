package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class AsWrapperTypeDeserializerTest {

    private AsWrapperTypeDeserializer deserializer;
    private JavaType baseType;
    private TypeIdResolver idResolver;
    private String typePropertyName;
    private boolean typeIdVisible;
    private Class<?> defaultImpl;
    private BeanProperty property;
    private JsonParser parser;
    private DeserializationContext context;
    
    @Before
    public void setUp() {
        baseType = new JavaType();
        idResolver = new TypeIdResolver();
        typePropertyName = "type";
        typeIdVisible = true;
        defaultImpl = Object.class;
        property = new BeanProperty();
        parser = new JsonFactory().createParser("");
        context = new DeserializationContext();
        deserializer = new AsWrapperTypeDeserializer(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
    }
    
    @Test
    public void testForProperty() {
        TypeDeserializer result = deserializer.forProperty(property);
        assertEquals(deserializer, result);
        assertNotEquals(deserializer, new AsWrapperTypeDeserializer(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl));
    }

    @Test
    public void testGetTypeInclusion() {
        assertEquals(As.WRAPPER_OBJECT, deserializer.getTypeInclusion());
    }

    @Test
    public void testDeserializeTypedFromObject() throws Exception {
        Object result = deserializer.deserializeTypedFromObject(parser, context);
        assertNotNull(result);
    }

    @Test
    public void testDeserializeTypedFromArray() throws Exception {
        Object result = deserializer.deserializeTypedFromArray(parser, context);
        assertNotNull(result);
    }

    @Test
    public void testDeserializeTypedFromScalar() throws Exception {
        Object result = deserializer.deserializeTypedFromScalar(parser, context);
        assertNotNull(result);
    }

    @Test
    public void testDeserializeTypedFromAny() throws Exception {
        Object result = deserializer.deserializeTypedFromAny(parser, context);
        assertNotNull(result);
    }

    @Test
    public void testDeserialize() throws Exception {
        TokenBuffer tb = new TokenBuffer(null, false);
        tb.writeStartObject();
        tb.writeFieldName(typePropertyName);
        tb.writeString("typeId");
        tb.writeEndObject();
        parser = tb.asParser(parser);
        
        Object result = deserializer.deserialize(parser, context);
        assertNotNull(result);
    }

}