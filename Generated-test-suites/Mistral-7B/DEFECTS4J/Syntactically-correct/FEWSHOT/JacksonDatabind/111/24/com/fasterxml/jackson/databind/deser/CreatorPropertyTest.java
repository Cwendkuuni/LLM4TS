package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

public class CreatorPropertyTest extends TestBase {

    @Test
    public void testCreatorPropertyConstructor() throws Exception {
        PropertyName name = PropertyName._("name");
        JavaType type = TypeFactory.defaultInstance().constructType(String.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedParameter param = mock(AnnotatedParameter.class);
        int index = 0;
        Object injectableValueId = "injectId";
        CreatorProperty property = new CreatorProperty(name, type, null, null, contextAnnotations, param, index, injectableValueId, null);
        assertSame(name, property.getName());
        assertSame(type, property.getType());
        assertSame(contextAnnotations, property.getContextAnnotations());
        assertEquals(param, property._annotated);
        assertEquals(index, property._creatorIndex);
        assertNull(property._injectableValueId);
        assertNull(property._fallbackSetter);
        assertFalse(property.isIgnorable());
    }

    @Test
    public void testCreatorPropertyWithInjectableValueId() throws Exception {
        PropertyName name = PropertyName._("name");
        JavaType type = TypeFactory.defaultInstance().constructType(String.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedParameter param = mock(AnnotatedParameter.class);
        int index = 0;
        Object injectableValueId = "injectId";
        CreatorProperty property = new CreatorProperty(name, type, null, null, contextAnnotations, param, index, injectableValueId, null);
        assertTrue(property.hasInjectableValueId());
        assertEquals(injectableValueId, property.getInjectableValueId());
    }

    @Test
    public void testCreatorPropertyWithFallbackSetter() throws Exception {
        PropertyName name = PropertyName._("name");
        JavaType type = TypeFactory.defaultInstance().constructType(String.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedParameter param = mock(AnnotatedParameter.class);
        int index = 0;
        SettableBeanProperty fallbackSetter = mock(SettableBeanProperty.class);
        Object injectableValueId = "injectId";
        CreatorProperty property = new CreatorProperty(name, type, null, null, contextAnnotations, param, index, null, fallbackSetter);
        assertEquals(fallbackSetter, property._fallbackSetter);
    }

    @Test
    public void testCreatorPropertyWithValueDeserializer() throws Exception {
        PropertyName name = PropertyName._("name");
        JavaType type = TypeFactory.defaultInstance().constructType(String.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedParameter param = mock(AnnotatedParameter.class);
        int index = 0;
        Object injectableValueId = "injectId";
        SettableBeanProperty fallbackSetter = mock(SettableBeanProperty.class);
        JsonDeserializer<?> deserializer = mock(JsonDeserializer.class);
        NullValueProvider nva = mock(NullValueProvider.class);
        CreatorProperty property = new CreatorProperty(name, type, null, deserializer, contextAnnotations, param, index, injectableValueId, fallbackSetter, nva);
        assertEquals(deserializer, property._valueDeserializer);
        assertEquals(nva, property._nullProvider);
    }

    @Test
    public void testCreatorPropertyWithTypeDeserializer() throws Exception {
        PropertyName name = PropertyName._("name");
        JavaType type = TypeFactory.defaultInstance().constructType(String.class);
        JavaType wrapperType = TypeFactory.defaultInstance().constructType(String[].class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedParameter param = mock(AnnotatedParameter.class);
        int index = 0;
        Object injectableValueId = "injectId";
        SettableBeanProperty fallbackSetter = mock(SettableBeanProperty.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        CreatorProperty property = new CreatorProperty(name, type, wrapperType, typeDeser, contextAnnotations, param, index, injectableValueId, fallbackSetter, null);
        assertEquals(typeDeser, property._typeDeserializer);
    }

    @Test
    public void testCreatorPropertyWithContextAnnotations() throws Exception {
        PropertyName name = PropertyName._("name");
        JavaType type = TypeFactory.defaultInstance().constructType(String.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedParameter param = mock(AnnotatedParameter.class);
        int index = 0;
        Object injectableValueId = "injectId";
        SettableBeanProperty fallbackSetter = mock(SettableBeanProperty.class);
        CreatorProperty property = new CreatorProperty(name, type, null, null, contextAnnotations, param, index, injectableValueId, fallbackSetter, null);
        assertEquals(contextAnnotations, property.getContextAnnotations());
    }

    @Test
    public void testCreatorPropertyWithIgnorableFlag() throws Exception {
        PropertyName name = PropertyName._("name");
        JavaType type = TypeFactory.defaultInstance().constructType(String.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedParameter param = mock(AnnotatedParameter.class);
        int index = 0;
        Object injectableValueId = "injectId";
        SettableBeanProperty fallbackSetter = mock(SettableBeanProperty.class);
        CreatorProperty property = new CreatorProperty(name, type, null, null, contextAnnotations, param, index, injectableValueId, fallbackSetter, null);
        assertFalse(property.isIgnorable());
        property.markAsIgnorable();
        assertTrue(property.isIgnorable());
    }

    @Test(expected = InvalidDefinitionException.class)
    public void testCreatorPropertyMissingSetter() throws Exception {
        PropertyName name = PropertyName._("name");
        JavaType type = TypeFactory.defaultInstance().constructType(String.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedParameter param = mock(AnnotatedParameter.class);
        int index = 0;
        Object injectableValueId = "injectId";
        SettableBeanProperty fallbackSetter = null;
        CreatorProperty property = new CreatorProperty(name, type, null, null, contextAnnotations, param, index, injectableValueId, fallbackSetter, null);
    }

    @Test
    public void testCreatorPropertyFindInjectableValue() throws Exception {
        PropertyName name = PropertyName._("name");
        JavaType type = TypeFactory.defaultInstance().constructType(String.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedParameter param = mock(AnnotatedParameter.class);
        int index = 0;
        Object injectableValueId = "injectId";
        DeserializationContext context = mock(DeserializationContext.class);
        Object beanInstance = mock(Object.class);
        CreatorProperty property = new CreatorProperty(name, type, null, null, contextAnnotations, param, index, injectableValueId, null);
        when(context.findInjectableValue(injectableValueId, property, beanInstance)).thenReturn("value");
        assertEquals("value", property.findInjectableValue(context, beanInstance));
    }

    @Test
    public void testCreatorPropertyInject() throws Exception {
        PropertyName name = PropertyName._("name");
        JavaType type = TypeFactory.defaultInstance().constructType(String.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedParameter param = mock(AnnotatedParameter.class);
        int index = 0;
        Object injectableValueId = "injectId";
        DeserializationContext context = mock(DeserializationContext.class);
        Object beanInstance = mock(Object.class);
        CreatorProperty property = new CreatorProperty(name, type, null, null, contextAnnotations, param, index, injectableValueId, null);
        when(context.findInjectableValue(injectableValueId, property, beanInstance)).thenReturn("value");
        property.inject(context, beanInstance);
        assertEquals("value", property.get(beanInstance));
    }

    @Test
    public void testCreatorPropertyDeserializeAndSet() throws Exception {
        PropertyName name = PropertyName._("name");
        JavaType type = TypeFactory.defaultInstance().constructType(String.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedParameter param = mock(AnnotatedParameter.class);
        int index = 0;
        Object injectableValueId = "injectId";
        SettableBeanProperty fallbackSetter = mock(SettableBeanProperty.class);
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        CreatorProperty property = new CreatorProperty(name, type, null, null, contextAnnotations, param, index, injectableValueId, fallbackSetter, null);
        when(fallbackSetter.getName()).thenReturn(name);
        when(fallbackSetter.getType()).thenReturn(type);
        when(fallbackSetter.deserialize(p, ctxt)).thenReturn("value");
        property.deserializeAndSet(p, ctxt, instance);
        assertEquals("value", property.get(instance));
    }

    @Test
    public void testCreatorPropertyDeserializeSetAndReturn() throws Exception {
        PropertyName name = PropertyName._("name");
        JavaType type = TypeFactory.defaultInstance().constructType(String.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedParameter param = mock(AnnotatedParameter.class);
        int index = 0;
        Object injectableValueId = "injectId";
        SettableBeanProperty fallbackSetter = mock(SettableBeanProperty.class);
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        CreatorProperty property = new CreatorProperty(name, type, null, null, contextAnnotations, param, index, injectableValueId, fallbackSetter, null);
        when(fallbackSetter.getName()).thenReturn(name);
        when(fallbackSetter.getType()).thenReturn(type);
        when(fallbackSetter.setAndReturn(instance, "value")).thenReturn("value");
        assertEquals("value", property.deserializeSetAndReturn(p, ctxt, instance));
    }

    @Test
    public void testCreatorPropertySet() throws Exception {
        PropertyName name = PropertyName._("name");
        JavaType type = TypeFactory.defaultInstance().constructType(String.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedParameter param = mock(AnnotatedParameter.class);
        int index = 0;
        Object injectableValueId = "injectId";
        SettableBeanProperty fallbackSetter = mock(SettableBeanProperty.class);
        Object instance = mock(Object.class);
        CreatorProperty property = new CreatorProperty(name, type, null, null, contextAnnotations, param, index, injectableValueId, fallbackSetter, null);
        property.set(instance, "value");
        assertEquals("value", property.get(instance));
    }

    @Test
    public void testCreatorPropertySetAndReturn() throws Exception {
        PropertyName name = PropertyName._("name");
        JavaType type = TypeFactory.defaultInstance().constructType(String.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedParameter param = mock(AnnotatedParameter.class);
        int index = 0;
        Object injectableValueId = "injectId";
        SettableBeanProperty fallbackSetter = mock(SettableBeanProperty.class);
        Object instance = mock(Object.class);
        CreatorProperty property = new CreatorProperty(name, type, null, null, contextAnnotations, param, index, injectableValueId, fallbackSetter, null);
        when(fallbackSetter.setAndReturn(instance, "value")).thenReturn("value");
        assertEquals("value", property.setAndReturn(instance, "value"));
    }

    @JsonCreator
    static class TestBean {
        @JsonProperty("name")
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @Test
    public void testCreatorPropertyWithJsonCreator() throws Exception {
        PropertyName name = PropertyName._("name");
        JavaType type = TypeFactory.defaultInstance().constructType(String.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedParameter param = mock(AnnotatedParameter.class);
        int index = 0;
        Object injectableValueId = "injectId";
        SettableBeanProperty fallbackSetter = mock(SettableBeanProperty.class);
        Object beanInstance = new TestBean();
        CreatorProperty property = new CreatorProperty(name, type, null, null, contextAnnotations, param, index, injectableValueId, fallbackSetter, null);
        when(fallbackSetter.getName()).thenReturn(name);
        when(fallbackSetter.getType()).thenReturn(type);
        when(fallbackSetter.deserialize(p, ctxt)).thenReturn("value");
        property.deserializeAndSet(p, ctxt, beanInstance);
        assertEquals("value", ((TestBean) beanInstance).getName());
    }
}