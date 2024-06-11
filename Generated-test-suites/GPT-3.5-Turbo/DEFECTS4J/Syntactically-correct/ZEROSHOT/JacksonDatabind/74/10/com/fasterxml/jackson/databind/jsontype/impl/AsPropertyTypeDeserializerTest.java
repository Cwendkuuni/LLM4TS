package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AsPropertyTypeDeserializerTest {

    @InjectMocks
    private AsPropertyTypeDeserializer asPropertyTypeDeserializer;

    @Mock
    private BeanProperty property;

    @Mock
    private TypeDeserializer typeDeserializer;

    @Mock
    private JsonParser jsonParser;

    @Mock
    private DeserializationContext deserializationContext;

    @Mock
    private TokenBuffer tokenBuffer;

    @Mock
    private JsonDeserializer<Object> jsonDeserializer;

    @Mock
    private JavaType javaType;

    @Mock
    private TypeIdResolver typeIdResolver;

    @Mock
    private JavaType defaultImpl;

    @org.junit.Test
    public void testForProperty() {
        TypeDeserializer result = asPropertyTypeDeserializer.forProperty(property);
        assertNotEquals(asPropertyTypeDeserializer, result);
    }

    @org.junit.Test
    public void testGetTypeInclusion() {
        As result = asPropertyTypeDeserializer.getTypeInclusion();
        assertNotNull(result);
    }

    @org.junit.Test
    public void testDeserializeTypedFromObject1() throws IOException {
        Object result = asPropertyTypeDeserializer.deserializeTypedFromObject(jsonParser, deserializationContext);
        assertNotNull(result);
    }

    @org.junit.Test
    public void testDeserializeTypedForId() throws IOException {
        Object result = asPropertyTypeDeserializer.deserializeTypedForId(jsonParser, deserializationContext, tokenBuffer);
        assertNotNull(result);
    }

    @org.junit.Test
    public void testDeserializeTypedUsingDefaultImpl() throws IOException {
        Object result = asPropertyTypeDeserializer.deserializeTypedUsingDefaultImpl(jsonParser, deserializationContext, tokenBuffer);
        assertNotNull(result);
    }

    @org.junit.Test
    public void testDeserializeTypedFromAny() throws IOException {
        Object result = asPropertyTypeDeserializer.deserializeTypedFromAny(jsonParser, deserializationContext);
        assertNotNull(result);
    }
}