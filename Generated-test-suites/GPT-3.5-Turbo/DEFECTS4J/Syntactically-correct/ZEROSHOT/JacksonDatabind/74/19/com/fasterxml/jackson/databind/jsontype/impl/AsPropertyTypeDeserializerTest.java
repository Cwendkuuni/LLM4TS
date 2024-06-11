package com.fasterxml.jackson.databind.jsontype.impl;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.Converter;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SuppressWarnings("resource")
public class AsPropertyTypeDeserializerTest {

    private JavaType javaType;
    private TypeIdResolver idResolver;
    private String typePropertyName;
    private boolean typeIdVisible;
    private JavaType defaultImpl;
    private As inclusion;    
    private AsPropertyTypeDeserializer asPropertyTypeDeserializer;

    private JsonParser jsonParser;
    private DeserializationContext deserializationContext;
    private JsonDeserializer<Object> jsonDeserializer;
    private DeserializationContext deserializationContextMock;

    @Before
    public void setup() throws IOException {
        javaType = mock(JavaType.class);
        idResolver = mock(TypeIdResolver.class);
        typePropertyName = "type";
        typeIdVisible = true;
        defaultImpl = mock(JavaType.class);
        inclusion = As.PROPERTY;

        asPropertyTypeDeserializer = new AsPropertyTypeDeserializer(javaType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        jsonParser = mock(JsonParser.class);
        deserializationContext = mock(DeserializationContext.class);
        jsonDeserializer = mock(JsonDeserializer.class);
        deserializationContextMock = mock(DeserializationContext.class);
    }
    
    @Test
    public void test_constructor() {
        AsPropertyTypeDeserializer as = new AsPropertyTypeDeserializer(javaType, idResolver, typePropertyName, typeIdVisible, defaultImpl, inclusion);
        Assert.assertEquals(javaType, as.javaType);
        Assert.assertEquals(idResolver, as.idResolver);
        Assert.assertEquals(typePropertyName, as.typePropertyName);
        Assert.assertEquals(typeIdVisible, as.typeIdVisible);
        Assert.assertEquals(defaultImpl, as.defaultImpl);
        Assert.assertEquals(inclusion, as._inclusion);
    }

    @Test
    public void test_forProperty() {
        BeanProperty beanProperty = mock(BeanProperty.class);

        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        AsPropertyTypeDeserializer as = new AsPropertyTypeDeserializer(javaType, idResolver, typePropertyName, typeIdVisible, defaultImpl, inclusion) {
            @Override TypeDeserializer forProperty(BeanProperty prop) {
                return typeDeserializer;
            }
        };

        TypeDeserializer returnedTypeDeserializer = as.forProperty(beanProperty);

        Assert.assertNotEquals(as, returnedTypeDeserializer);
        Assert.assertEquals(typeDeserializer, returnedTypeDeserializer);
    }

    @Test
    public void test_getTypeInclusion() {
        AsPropertyTypeDeserializer as = new AsPropertyTypeDeserializer(javaType, idResolver, typePropertyName, typeIdVisible, defaultImpl, inclusion);

        As returnedInclusion = as.getTypeInclusion();

        Assert.assertEquals(inclusion, returnedInclusion);
    }

    @Test
    public void test_deserializeTypedFromObject_whenCanReadTypeIdTrue() throws IOException {
        given(jsonParser.canReadTypeId()).willReturn(true);
        Object typeId = "typeId";
        given(jsonParser.getTypeId()).willReturn(typeId);

        Object result = asPropertyTypeDeserializer.deserializeTypedFromObject(jsonParser, deserializationContext);

        verify(asPropertyTypeDeserializer).deserializeTypedFromObject(eq(jsonParser), eq(deserializationContext));
        Assert.assertNull(result);
    }

    @Test
    public void test_deserializeTypedFromObject_whenCurrentTokenIsNotStartObject() throws IOException {
        given(jsonParser.getCurrentToken()).willReturn(JsonToken.VALUE_TRUE, JsonToken.VALUE_STRING);
        given(jsonParser.nextToken()).willReturn(JsonToken.VALUE_STRING);
        TokenBuffer tb = mock(TokenBuffer.class);
        given(tb.asParser(jsonParser)).willReturn(jsonParser);
        given(jsonParser.nextFieldName()).willReturn(null);
        given(asPropertyTypeDeserializer._deserializeTypedUsingDefaultImpl(eq(jsonParser), eq(deserializationContext), any())).willReturn(null);

        Object result = asPropertyTypeDeserializer.deserializeTypedFromObject(jsonParser, deserializationContext);

        verify(asPropertyTypeDeserializer).deserializeTypedFromObject(eq(jsonParser), eq(deserializationContext));
        Assert.assertNull(result);
    }

    @Test
    public void test_deserializeTypedFromObject_whenCurrentTokenIsStartObject() throws IOException {
        given(jsonParser.getCurrentToken()).willReturn(JsonToken.START_OBJECT);
        given(jsonParser.nextToken()).willReturn(JsonToken.FIELD_NAME, JsonToken.VALUE_TRUE, JsonToken.FIELD_NAME, JsonToken.VALUE_STRING, JsonToken.END_OBJECT);
        given(jsonParser.getCurrentName()).willReturn("type");
        given(jsonParser.nextValue()).willReturn(JsonToken.VALUE_TRUE);
        Object result = new Object();
        given(asPropertyTypeDeserializer._deserializeTypedForId(eq(jsonParser), eq(deserializationContext), any())).willReturn(result);

        Object returnedObject = asPropertyTypeDeserializer.deserializeTypedFromObject(jsonParser, deserializationContext);

        Assert.assertEquals(result, returnedObject);
        verify(asPropertyTypeDeserializer).deserializeTypedFromObject(eq(jsonParser), eq(deserializationContext));
    }

    @Test
    public void test_deserializeTypedForId_whenTypeIdVisibleTrue() throws IOException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        String typeId = "typeId";
        given(jsonParser.getText()).willReturn(typeId);
        JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
        given(asPropertyTypeDeserializer._findDeserializer(ctxt, typeId)).willReturn(deser);
        TokenBuffer tb = mock(TokenBuffer.class);
        given(asPropertyTypeDeserializer._typeIdVisible).willReturn(true);
        given(tb.asParser(jsonParser)).willReturn(jsonParser);
        given(jsonParser.currentName()).willReturn("name");
        given(jsonParser.getText()).willReturn("name");
        given(jsonParser.nextToken()).willReturn(JsonToken.START_OBJECT, JsonToken.END_OBJECT);

        Object returnedObject = asPropertyTypeDeserializer._deserializeTypedForId(jsonParser, ctxt, tb);

        Assert.assertNull(returnedObject);
        verify(asPropertyTypeDeserializer).deserializeTypedFromObject(eq(jsonParser), eq(ctxt));
    }

    @Test
    public void test_deserializeTypedForId_whenTypeIdVisibleFalse() throws IOException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        String typeId = "typeId";
        given(jsonParser.getText()).willReturn(typeId);
        JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
        given(asPropertyTypeDeserializer._findDeserializer(ctxt, typeId)).willReturn(deser);
        TokenBuffer tb = mock(TokenBuffer.class);
        given(tb.asParser(jsonParser)).willReturn(jsonParser);
        TokenBuffer tb2 = mock(TokenBuffer.class);
        given(tb2.asParser(jsonParser)).willReturn(jsonParser);
        given(jsonParser.currentName()).willReturn("name");
        given(jsonParser.getText()).willReturn("name");
        given(jsonParser.nextToken()).willReturn(JsonToken.START_OBJECT, JsonToken.END_OBJECT);

        Object returnedObject = asPropertyTypeDeserializer._deserializeTypedForId(jsonParser, ctxt, tb);

        Assert.assertNull(returnedObject);
        verify(asPropertyTypeDeserializer).deserializeTypedFromObject(eq(jsonParser), eq(ctxt));
    }

    @Test
    public void test_deserializeTypedUsingDefaultImpl_whenDefaultImplDeserializerNotNull() throws IOException {
        given(asPropertyTypeDeserializer._findDefaultImplDeserializer(eq(deserializationContext))).willReturn(jsonDeserializer);
        given(jsonParser.getCurrentToken()).willReturn(JsonToken.START_OBJECT);
        given(jsonParser.nextToken()).willReturn(JsonToken.FIELD_NAME, JsonToken.VALUE_TRUE, JsonToken.FIELD_NAME, JsonToken.VALUE_STRING, JsonToken.END_OBJECT);
        given(jsonParser.getCurrentName()).willReturn("name");
        given(jsonParser.nextValue()).willReturn(JsonToken.VALUE_TRUE);

        Object result = mock(Object.class);
        given(jsonDeserializer.deserialize(jsonParser, deserializationContext)).willReturn(result);

        Object returnedObject = asPropertyTypeDeserializer._deserializeTypedUsingDefaultImpl(jsonParser, deserializationContext, null);

        Assert.assertEquals(result, returnedObject);
    }

    @Test
    public void test_deserializeTypedUsingDefaultImpl_whenDefaultImplDeserializerIsNull() throws IOException {
        given(asPropertyTypeDeserializer._findDefaultImplDeserializer(eq(deserializationContext))).willReturn(null);
        given(jsonParser.getCurrentToken()).willReturn(JsonToken.VALUE_NULL);
        given(jsonParser.readValueAs(any(Class.class))).willReturn(null);

        Object returnedObject = asPropertyTypeDeserializer._deserializeTypedUsingDefaultImpl(jsonParser, deserializationContext, null);

        Assert.assertNull(returnedObject);
    }

    @Test
    public void test_deserializeTypedUsingDefaultImpl_whenCurrentTokenIsStartArray() throws IOException {
        given(jsonParser.getCurrentToken()).willReturn(JsonToken.START_ARRAY);

        Object returnedObject = asPropertyTypeDeserializer._deserializeTypedUsingDefaultImpl(jsonParser, deserializationContext, null);

        Assert.assertNull(returnedObject);
    }

    @Test
    public void test_deserializeTypedFromAny_whenCurrentTokenIsStartArray() throws IOException {
        given(jsonParser.getCurrentToken()).willReturn(JsonToken.START_ARRAY);

        Object returnedObject = asPropertyTypeDeserializer.deserializeTypedFromAny(jsonParser, deserializationContext);

        Assert.assertNull(returnedObject);
    }

    @Test
    public void test_deserializeTypedFromAny_whenCurrentTokenIsNotStartArray() throws IOException {
        Object result = new Object();
        given(asPropertyTypeDeserializer.deserializeTypedFromObject(jsonParser, deserializationContext)).willReturn(result);

        Object returnedObject = asPropertyTypeDeserializer.deserializeTypedFromAny(jsonParser, deserializationContext);

        Assert.assertEquals(result, returnedObject);
    }
}