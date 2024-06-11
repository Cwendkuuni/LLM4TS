
package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import static org.mockito.Mockito.when;

public class SetterlessPropertyTest {

    private SetterlessProperty setterlessProperty;

    @Before
    public void setup() {
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        JavaType type = Mockito.mock(JavaType.class);
        TypeDeserializer typeDeserializer = Mockito.mock(TypeDeserializer.class);
        Annotations annotations = Mockito.mock(Annotations.class);
        AnnotatedMethod annotatedMethod = Mockito.mock(AnnotatedMethod.class);
        Method method = Mockito.mock(Method.class);

        setterlessProperty = new SetterlessProperty(propDef, type, typeDeserializer, annotations, annotatedMethod);
        setterlessProperty._annotated = annotatedMethod;
        setterlessProperty._getter = method;
    }

    @Test
    public void testWithName() {
        PropertyName newName = Mockito.mock(PropertyName.class);
        SettableBeanProperty result = setterlessProperty.withName(newName);
        Assert.assertNotNull(result);
        Assert.assertEquals(setterlessProperty._annotated, result._annotated);
        Assert.assertEquals(setterlessProperty._getter, result._getter);
        Assert.assertEquals(newName, result.getName());
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> deser = Mockito.mock(JsonDeserializer.class);
        NullValueProvider nva = Mockito.mock(NullValueProvider.class);
        SettableBeanProperty result = setterlessProperty.withValueDeserializer(deser);
        Assert.assertNotNull(result);
        Assert.assertEquals(setterlessProperty._annotated, result._annotated);
        Assert.assertEquals(setterlessProperty._getter, result._getter);
        Assert.assertEquals(deser, result._valueDeserializer);
        Assert.assertEquals(setterlessProperty._nullProvider, result._nullProvider);
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nva = Mockito.mock(NullValueProvider.class);
        SettableBeanProperty result = setterlessProperty.withNullProvider(nva);
        Assert.assertNotNull(result);
        Assert.assertEquals(setterlessProperty._annotated, result._annotated);
        Assert.assertEquals(setterlessProperty._getter, result._getter);
        Assert.assertEquals(setterlessProperty._valueDeserializer, result._valueDeserializer);
        Assert.assertEquals(nva, result._nullProvider);
    }

    @Test
    public void testFixAccess() {
        DeserializationConfig config = Mockito.mock(DeserializationConfig.class);
        setterlessProperty._annotated = Mockito.mock(AnnotatedMethod.class);
        setterlessProperty.fixAccess(config);
        Mockito.verify(setterlessProperty._annotated).fixAccess(Mockito.anyBoolean());
    }

    @Test
    public void testGetAnnotation() {
        Class<Annotation> acls = Annotation.class;
        Annotation result = Mockito.mock(Annotation.class);
        when(setterlessProperty._annotated.getAnnotation(acls)).thenReturn(result);
        Annotation actualResult = setterlessProperty.getAnnotation(acls);
        Assert.assertEquals(result, actualResult);
    }

    @Test
    public void testGetMember() {
        AnnotatedMember result = setterlessProperty.getMember();
        Assert.assertEquals(setterlessProperty._annotated, result);
    }

    @Test
    public void testDeserializeAndSet_ValueNull() throws IOException {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
        Object instance = Mockito.mock(Object.class);
        setterlessProperty._valueDeserializer = Mockito.mock(JsonDeserializer.class);
        setterlessProperty._valueTypeDeserializer = null;

        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);

        setterlessProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        Mockito.verify(instance, Mockito.times(0)).invoke(setterlessProperty._getter, Mockito.any());
        Mockito.verify(setterlessProperty._valueDeserializer, Mockito.times(0)).deserialize(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    public void testDeserializeAndSet_TypeDeserializerNotNull() throws IOException {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
        Object instance = Mockito.mock(Object.class);
        setterlessProperty._valueDeserializer = Mockito.mock(JsonDeserializer.class);
        setterlessProperty._valueTypeDeserializer = Mockito.mock(TypeDeserializer.class);

        try {
            setterlessProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
        } catch (Exception e) {
            Assert.assertEquals(
                    "Problem deserializing 'setterless' property (\"null\"): no way to handle typed deser with setterless yet",
                    e.getMessage());
        }

        Mockito.verify(instance, Mockito.times(0)).invoke(setterlessProperty._getter, Mockito.any());
        Mockito.verify(setterlessProperty._valueDeserializer, Mockito.times(0)).deserialize(Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.verify(setterlessProperty._valueDeserializer, Mockito.times(1))
                .deserializeWithType(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    public void testDeserializeAndSet_ToModifyNotNull() throws IOException {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
        Object instance = Mockito.mock(Object.class);
        setterlessProperty._getter = Mockito.mock(Method.class);
        setterlessProperty._valueDeserializer = Mockito.mock(JsonDeserializer.class);
        Object toModify = Mockito.mock(Object.class);

        when(setterlessProperty._getter.invoke(instance, null)).thenReturn(toModify);

        setterlessProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        Mockito.verify(toModify, Mockito.times(1)).invoke(setterlessProperty._getter, Mockito.any());
        Mockito.verify(setterlessProperty._valueDeserializer, Mockito.times(1)).deserialize(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    public void testDeserializeAndSet_ToModifyNull() throws IOException {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
        Object instance = Mockito.mock(Object.class);
        setterlessProperty._getter = Mockito.mock(Method.class);
        setterlessProperty._valueDeserializer = Mockito.mock(JsonDeserializer.class);

        when(setterlessProperty._getter.invoke(instance, null)).thenReturn(null);

        try {
            setterlessProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
        } catch (Exception e) {
            Assert.assertEquals(
                    "Problem deserializing 'setterless' property 'null': get method returned null",
                    e.getMessage());
        }

        Mockito.verify(setterlessProperty._getter, Mockito.times(1)).invoke(instance, null);
        Mockito.verify(setterlessProperty._valueDeserializer, Mockito.times(0)).deserialize(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
        Object instance = Mockito.mock(Object.class);

        setterlessProperty.deserializeAndSet = Mockito.mock(DeserializerSet.class);

        setterlessProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);

        Mockito.verify(setterlessProperty.deserializeAndSet)
                .deserializeAndSet(jsonParser, deserializationContext, instance);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws IOException {
        Object instance = Mockito.mock(Object.class);
        Object value = Mockito.mock(Object.class);

        setterlessProperty.set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = Mockito.mock(Object.class);
        Object value = Mockito.mock(Object.class);

        Object result = setterlessProperty.setAndReturn(instance, value);

        Assert.assertEquals(instance, result);
        Mockito.verify(setterlessProperty).set(instance, value);
    }

}
