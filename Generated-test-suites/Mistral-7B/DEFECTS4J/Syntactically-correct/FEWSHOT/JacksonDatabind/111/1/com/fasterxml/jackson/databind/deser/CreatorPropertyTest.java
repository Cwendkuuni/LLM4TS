package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;

public class CreatorPropertyTest {

    @Test
    public void testCreatorProperty() {
        PropertyName name = PropertyName.construct("testProperty");
        JavaType type = ObjectType.construct();
        PropertyName wrapperName = PropertyName.construct("wrapperName");
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = new Annotations();
        AnnotatedParameter param = Mockito.mock(AnnotatedParameter.class);
        int index = 0;
        Object injectableValueId = "injectedValueId";
        CreatorProperty property = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, null);

        assertEquals(name, property.getName());
        assertEquals(type, property.getType());
        assertEquals(wrapperName, property.getWrapperName());
        assertEquals(typeDeser, property.getTypeDeserializer());
        assertEquals(contextAnnotations, property.getContextAnnotations());
        assertEquals(param, property._annotated);
        assertEquals(index, property._creatorIndex);
        assertEquals(injectableValueId, property._injectableValueId);
        assertNull(property._fallbackSetter);
    }

    @Test(expected = InvalidDefinitionException.class)
    public void testReportMissingSetterNull() throws IOException {
        PropertyName name = PropertyName.construct("testProperty");
        JavaType type = ObjectType.construct();
        PropertyName wrapperName = PropertyName.construct("wrapperName");
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = new Annotations();
        AnnotatedParameter param = Mockito.mock(AnnotatedParameter.class);
        int index = 0;
        Object injectableValueId = "injectedValueId";
        CreatorProperty property = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, null);
        property._verifySetter();
    }

    @Test(expected = InvalidDefinitionException.class)
    public void testReportMissingSetterNotNull() throws IOException {
        PropertyName name = PropertyName.construct("testProperty");
        JavaType type = ObjectType.construct();
        PropertyName wrapperName = PropertyName.construct("wrapperName");
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = new Annotations();
        AnnotatedParameter param = Mockito.mock(AnnotatedParameter.class);
        int index = 0;
        Object injectableValueId = "injectedValueId";
        CreatorProperty property = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, null);
        JsonParser p = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        property._verifySetter();
        property._reportMissingSetter(p, ctxt);
    }

    @Test
    public void testSetFallbackSetter() {
        PropertyName name = PropertyName.construct("testProperty");
        JavaType type = ObjectType.construct();
        PropertyName wrapperName = PropertyName.construct("wrapperName");
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = new Annotations();
        AnnotatedParameter param = Mockito.mock(AnnotatedParameter.class);
        int index = 0;
        Object injectableValueId = "injectedValueId";
        CreatorProperty property = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, null);
        SettableBeanProperty fallbackSetter = Mockito.mock(SettableBeanProperty.class);
        property.setFallbackSetter(fallbackSetter);
        assertEquals(fallbackSetter, property._fallbackSetter);
    }

    @Test
    public void testIsIgnorable() {
        PropertyName name = PropertyName.construct("testProperty");
        JavaType type = ObjectType.construct();
        PropertyName wrapperName = PropertyName.construct("wrapperName");
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = new Annotations();
        AnnotatedParameter param = Mockito.mock(AnnotatedParameter.class);
        int index = 0;
        Object injectableValueId = "injectedValueId";
        CreatorProperty property = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, null);
        property.markAsIgnorable();
        assertTrue(property.isIgnorable());
    }

    @Test
    public void testFindInjectableValue() throws IOException {
        PropertyName name = PropertyName.construct("testProperty");
        JavaType type = ObjectType.construct();
        PropertyName wrapperName = PropertyName.construct("wrapperName");
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = new Annotations();
        AnnotatedParameter param = Mockito.mock(AnnotatedParameter.class);
        int index = 0;
        Object injectableValueId = "injectedValueId";
        CreatorProperty property = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, null);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object beanInstance = new Object();
        Object injectedValue = new Object();
        when(ctxt.findInjectableValue(injectableValueId, property, beanInstance)).thenReturn(injectedValue);
        Object result = property.findInjectableValue(ctxt, beanInstance);
        assertEquals(injectedValue, result);
    }

    @Test
    public void testInject() throws IOException {
        PropertyName name = PropertyName.construct("testProperty");
        JavaType type = ObjectType.construct();
        PropertyName wrapperName = PropertyName.construct("wrapperName");
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = new Annotations();
        AnnotatedParameter param = Mockito.mock(AnnotatedParameter.class);
        int index = 0;
        Object injectableValueId = "injectedValueId";
        CreatorProperty property = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, null);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object beanInstance = new Object();
        Object injectedValue = new Object();
        when(ctxt.findInjectableValue(injectableValueId, property, beanInstance)).thenReturn(injectedValue);
        property.inject(ctxt, beanInstance);
        assertEquals(injectedValue, property.get(beanInstance));
    }

    @Test(expected = JsonMappingException.class)
    public void testFindInjectableValueException() throws IOException {
        PropertyName name = PropertyName.construct("testProperty");
        JavaType type = ObjectType.construct();
        PropertyName wrapperName = PropertyName.construct("wrapperName");
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = new Annotations();
        AnnotatedParameter param = Mockito.mock(AnnotatedParameter.class);
        int index = 0;
        Object injectableValueId = "injectedValueId";
        CreatorProperty property = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, null);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object beanInstance = new Object();
        when(ctxt.findInjectableValue(injectableValueId, property, beanInstance)).thenThrow(JsonMappingException.class);
        property.inject(ctxt, beanInstance);
    }
}