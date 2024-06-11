package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;

public class CreatorPropertyTest {

    private CreatorProperty property;
    private AnnotatedParameter annotatedParameter;
    private Object injectableValueId;

    @Before
    public void setup() {
        annotatedParameter = mock(AnnotatedParameter.class);
        injectableValueId = new Object() {};
        property = new CreatorProperty(
                new PropertyName("testProperty"),
                new JavaType(String.class),
                null,
                null,
                Annotations.empty(),
                annotatedParameter,
                0,
                injectableValueId,
                PropertyMetadata.STD_ORDER
        );
    }

    @Test
    public void testGetAnnotation() {
        Annotation annotation = mock(Annotation.class);
        when(annotatedParameter.getAnnotation(Annotation.class)).thenReturn(annotation);
        assertEquals(annotation, property.getAnnotation(Annotation.class));
    }

    @Test(expected = NullPointerException.class)
    public void testGetAnnotationNullParameter() {
        property.getAnnotation(null);
    }

    @Test
    public void testGetMember() {
        assertEquals(annotatedParameter, property.getMember());
    }

    @Test
    public void testGetCreatorIndex() {
        assertEquals(0, property.getCreatorIndex());
    }

    @Test
    public void testFindInjectableValue() throws IOException {
        DeserializationContext context = mock(DeserializationContext.class);
        Object beanInstance = new Object() {};
        when(context.findInjectableValue(eq(injectableValueId), any(), eq(beanInstance))).thenReturn("injectedValue");
        assertEquals("injectedValue", property.findInjectableValue(context, beanInstance));
    }

    @Test(expected = JsonMappingException.class)
    public void testFindInjectableValueNullInjectableValueId() {
        DeserializationContext context = mock(DeserializationContext.class);
        Object beanInstance = new Object() {};
        property.findInjectableValue(context, beanInstance);
    }

    @Test
    public void testInject() throws IOException {
        DeserializationContext context = mock(DeserializationContext.class);
        Object beanInstance = new Object() {};
        String valueToInject = "valueToInject";
        when(context.findInjectableValue(eq(injectableValueId), any(), eq(beanInstance))).thenReturn(valueToInject);
        property.inject(context, beanInstance);
        verify(beanInstance).set(any(), eq(valueToInject));
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object instance = new Object() {};
        String valueToDeserialize = "valueToDeserialize";
        when(parser.getValueAsString()).thenReturn(valueToDeserialize);
        property.deserializeAndSet(parser, context, instance);
        verify(instance).set(any(), eq(valueToDeserialize));
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object instance = new Object() {};
        String valueToDeserialize = "valueToDeserialize";
        Object returnValue = new Object() {};
        when(parser.getValueAsString()).thenReturn(valueToDeserialize);
        when(instance.setAndReturn(any(), any())).thenReturn(returnValue);
        assertEquals(returnValue, property.deserializeSetAndReturn(parser, context, instance));
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object() {};
        String valueToSet = "valueToSet";
        property.set(instance, valueToSet);
        verify(instance).set(any(), eq(valueToSet));
    }

    @Test
    public void testSetNull() throws IOException {
        Object instance = new Object() {};
        property.set(instance, null);
        verify(instance).set(any(), null);
    }

    @Test
    public void testSetFallbackSetter() {
        SettableBeanProperty fallbackSetter = mock(SettableBeanProperty.class);
        property.setFallbackSetter(fallbackSetter);
        assertEquals(fallbackSetter, property._fallbackSetter);
    }

    @Test(expected = IOException.class)
    public void testVerifySetterNullFallbackSetter() throws IOException {
        property._verifySetter();
    }

    @Test(expected = InvalidDefinitionException.class)
    public void testReportMissingSetterNullParser() throws IOException {
        property._reportMissingSetter(null, null);
    }

    @Test(expected = InvalidDefinitionException.class)
    public void testReportMissingSetterNotNullParser() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        property._reportMissingSetter(parser, null);
    }
}