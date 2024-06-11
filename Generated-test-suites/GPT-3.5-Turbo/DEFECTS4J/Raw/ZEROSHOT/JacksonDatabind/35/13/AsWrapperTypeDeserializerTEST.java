
package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.BeanDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeDeserializer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AsWrapperTypeDeserializerTest {

    @Test
    public void testDeserializeTypedFromObject() throws Exception {
        // create mock objects
        JavaType javaType = mock(JavaType.class);
        TypeIdResolver typeIdResolver = mock(TypeIdResolver.class);
        BeanDeserializer beanDeserializer = mock(BeanDeserializer.class);
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);

        // create test object
        AsWrapperTypeDeserializer asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(javaType, typeIdResolver, "typePropertyName", true, Object.class);

        // mock behavior
        when(jsonParser.canReadTypeId()).thenReturn(true);
        when(jsonParser.getTypeId()).thenReturn("1");
        when(asWrapperTypeDeserializer._findDeserializer(deserializationContext, "1")).thenReturn(beanDeserializer);
        when(beanDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn("test");

        // perform test
        Object result = asWrapperTypeDeserializer.deserializeTypedFromObject(jsonParser, deserializationContext);

        // assert result
        assertEquals("test", result);
    }

    @Test
    public void testDeserializeTypedFromArray() throws Exception {
        // create mock objects
        JavaType javaType = mock(JavaType.class);
        TypeIdResolver typeIdResolver = mock(TypeIdResolver.class);
        BeanDeserializer beanDeserializer = mock(BeanDeserializer.class);
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);

        // create test object
        AsWrapperTypeDeserializer asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(javaType, typeIdResolver, "typePropertyName", true, Object.class);

        // mock behavior
        when(jsonParser.canReadTypeId()).thenReturn(false);
        when(asWrapperTypeDeserializer._findDeserializer(deserializationContext, null)).thenReturn(beanDeserializer);
        when(beanDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn("test");

        // perform test
        Object result = asWrapperTypeDeserializer.deserializeTypedFromArray(jsonParser, deserializationContext);

        // assert result
        assertEquals("test", result);
    }

    @Test
    public void testDeserializeTypedFromScalar() throws Exception {
        // create mock objects
        JavaType javaType = mock(JavaType.class);
        TypeIdResolver typeIdResolver = mock(TypeIdResolver.class);
        BeanDeserializer beanDeserializer = mock(BeanDeserializer.class);
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);

        // create test object
        AsWrapperTypeDeserializer asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(javaType, typeIdResolver, "typePropertyName", true, Object.class);

        // mock behavior
        when(jsonParser.canReadTypeId()).thenReturn(false);
        when(asWrapperTypeDeserializer._findDeserializer(deserializationContext, null)).thenReturn(beanDeserializer);
        when(beanDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn("test");

        // perform test
        Object result = asWrapperTypeDeserializer.deserializeTypedFromScalar(jsonParser, deserializationContext);

        // assert result
        assertEquals("test", result);
    }

    @Test
    public void testDeserializeTypedFromAny() throws Exception {
        // create mock objects
        JavaType javaType = mock(JavaType.class);
        TypeIdResolver typeIdResolver = mock(TypeIdResolver.class);
        BeanDeserializer beanDeserializer = mock(BeanDeserializer.class);
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);

        // create test object
        AsWrapperTypeDeserializer asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(javaType, typeIdResolver, "typePropertyName", true, Object.class);

        // mock behavior
        when(jsonParser.canReadTypeId()).thenReturn(false);
        when(asWrapperTypeDeserializer._findDeserializer(deserializationContext, null)).thenReturn(beanDeserializer);
        when(beanDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn("test");

        // perform test
        Object result = asWrapperTypeDeserializer.deserializeTypedFromAny(jsonParser, deserializationContext);

        // assert result
        assertEquals("test", result);
    }
}
