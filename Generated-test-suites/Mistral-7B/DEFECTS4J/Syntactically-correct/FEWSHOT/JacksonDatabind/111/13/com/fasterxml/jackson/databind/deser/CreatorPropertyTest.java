package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.JsonTestUtils;
import org.junit.Before;
import org.junit.Test;

public class CreatorPropertyTest {

    private Object beanInstance;
    private DeserializationContext context;
    private CreatorProperty property;
    private SettableBeanProperty fallbackSetter;
    private AnnotatedParameter annotatedParameter;

    @Before
    public void setup() {
        beanInstance = mock(Object.class);
        context = mock(DeserializationContext.class);
        annotatedParameter = mock(AnnotatedParameter.class);
        when(annotatedParameter.getName()).thenReturn("testProperty");
        fallbackSetter = mock(SettableBeanProperty.class);
        property = new CreatorProperty(
            new PropertyName("testProperty"),
            new JavaType(String.class),
            new PropertyName("wrapperName"),
            null,
            new Annotations(),
            annotatedParameter,
            0,
            null,
            new PropertyMetadata()
        );
    }

    @Test
    public void testFindInjectableValue() throws IOException {
        when(context.findInjectableValue(null, property, beanInstance)).thenReturn("injectedValue");

        Object result = property.findInjectableValue(context, beanInstance);

        assertEquals("injectedValue", result);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindInjectableValueWithNullInjectableValueId() throws IOException {
        property.setInjectableValueId(null);
        property.findInjectableValue(context, beanInstance);
    }

    @Test
    public void testInject() throws IOException {
        when(context.findInjectableValue(property.getInjectableValueId(), property, beanInstance)).thenReturn("injectedValue");

        property.inject(context, beanInstance);

        assertEquals("injectedValue", JsonTestUtils.asText(property.getName().getValue()));
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        String testString = "testValue";
        when(context.findInjectableValue(property.getInjectableValueId(), property, beanInstance)).thenReturn(null);
        when(property._fallbackSetter.getName()).thenReturn("fallbackSetter");

        JsonParser p = mock(JsonParser.class);
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        when(deserializer.deserialize(p, context)).thenReturn(testString);

        property.setFallbackSetter(fallbackSetter);
        property.deserializeAndSet(p, context, beanInstance);

        assertEquals(testString, JsonTestUtils.asText(property.getName().getValue()));
    }

    @Test
    public void testSet() throws IOException {
        String testString = "testValue";

        property.set(beanInstance, testString);

        assertEquals(testString, JsonTestUtils.asText(property.getName().getValue()));
    }

    @Test
    public void testGetInjectableValueId() {
        when(annotatedParameter.getId()).thenReturn("injectableValueId");

        String expectedInjectableValueId = "injectableValueId";
        Object result = property.getInjectableValueId();

        assertEquals(expectedInjectableValueId, result);
    }
}