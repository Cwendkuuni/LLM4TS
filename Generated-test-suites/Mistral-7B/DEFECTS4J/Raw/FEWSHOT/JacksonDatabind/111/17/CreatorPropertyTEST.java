package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

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

import org.junit.Before;
import org.junit.Test;

public class CreatorPropertyTest {

    private Object beanInstance;
    private AnnotatedParameter param;
    private CreatorProperty property;
    private DeserializationContext context;

    @Before
    public void setUp() throws Exception {
        beanInstance = new Object() {};
        param = new AnnotatedParameter(null, null, null, null, null, null, 0, null);
        context = new DeserializationContext(new TestAnnotationIntrospector(), null, null);
        property = new CreatorProperty(
                new PropertyName("testProperty"),
                JavaType.of(String.class),
                null,
                null,
                null,
                param,
                0,
                null,
                PropertyMetadata.EMPTY
        );
    }

    @Test
    public void testFindInjectableValue() throws Exception {
        assertNull(property.findInjectableValue(context, beanInstance));

        String injectValueId = "testInjectValueId";
        property.setInjectableValueId(injectValueId);
        Object expectedInjectValue = new Object() {};
        when(context.findInjectableValue(injectValueId, property, beanInstance)).thenReturn(expectedInjectValue);

        Object injectedValue = property.findInjectableValue(context, beanInstance);
        assertEquals(expectedInjectValue, injectedValue);
    }

    @Test
    public void testInject() throws Exception {
        String injectValue = "testInjectValue";
        property.setInjectableValueId("testInjectValueId");

        when(context.findInjectableValue("testInjectValueId", property, beanInstance)).thenReturn(injectValue);

        property.inject(context, beanInstance);

        assertSame(injectValue, property.getName().getValue().get(beanInstance));
    }

    @Test
    public void testIsIgnorable() {
        assertFalse(property.isIgnorable());

        property.markAsIgnorable();

        assertTrue(property.isIgnorable());
    }

    @Test
    public void testGetAnnotation() {
        Annotation annotation = new TestAnnotation();
        when(param.getAnnotation(TestAnnotation.class)).thenReturn(annotation);

        assertSame(annotation, property.getAnnotation(TestAnnotation.class));
    }

    @Test(expected = InvalidDefinitionException.class)
    public void testMissingSetter() throws IOException {
        property.setFallbackSetter(null);

        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        property.deserializeAndSet(p, ctxt, beanInstance);
    }
}

class TestAnnotation extends Annotation {
}