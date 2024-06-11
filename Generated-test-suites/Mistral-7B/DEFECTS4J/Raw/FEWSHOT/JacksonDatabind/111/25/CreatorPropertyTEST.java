package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.PropertyName;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

public class CreatorPropertyTest extends TestBase {

    private static final Object INJECTABLE_VALUE_ID = new Object() {
        // Marker interface for injectable values
    };

    @SuppressWarnings("unchecked")
    @Test
    public void testCreation() throws IOException, JsonProcessingException {
        PropertyName name = PropertyName.construct("testName");
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(String.class);
        JavaType wrapperType = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(StringWrapper.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedParameter param = mock(AnnotatedParameter.class);
        when(param.getType()).thenReturn(type);

        Object injectableValueId = INJECTABLE_VALUE_ID;
        CreatorProperty property = new CreatorProperty(name, type, wrapperType, typeDeser, contextAnnotations, param, 0, injectableValueId, PropertyMetadata.empty());

        assertEquals("testName", property.getName());
        assertEquals(type, property.getType());
        assertNull(property._fallbackSetter);
        assertEquals(0, property._creatorIndex);
        assertNull(property._injectableValueId);
    }

    @Test
    public void testValueDeserializer() {
        PropertyName name = PropertyName.construct("testName");
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(String.class);
        PropertyName wrapperName = PropertyName.construct("wrapperName");
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedParameter param = mock(AnnotatedParameter.class);
        int index = 0;
        Object injectableValueId = INJECTABLE_VALUE_ID;

        CreatorProperty property = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, PropertyMetadata.empty());

        JsonDeserializer<?> deserializer = mock(JsonDeserializer.class);
        property = property.withValueDeserializer(deserializer);

        assertEquals(deserializer, property._valueDeserializer);
    }

    @Test
    public void testNullProvider() {
        PropertyName name = PropertyName.construct("testName");
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(String.class);
        PropertyName wrapperName = PropertyName.construct("wrapperName");
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedParameter param = mock(AnnotatedParameter.class);
        int index = 0;
        Object injectableValueId = INJECTABLE_VALUE_ID;

        CreatorProperty property = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, PropertyMetadata.empty());

        NullValueProvider nva = mock(NullValueProvider.class);
        property = property.withNullProvider(nva);

        assertEquals(nva, property._nullProvider);
    }

    @Test
    public void testSetFallbackSetter() {
        PropertyName name = PropertyName.construct("testName");
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(String.class);
        PropertyName wrapperName = PropertyName.construct("wrapperName");
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedParameter param = mock(AnnotatedParameter.class);
        int index = 0;
        Object injectableValueId = INJECTABLE_VALUE_ID;

        CreatorProperty property = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, PropertyMetadata.empty());

        SettableBeanProperty fallbackSetter = mock(SettableBeanProperty.class);
        property.setFallbackSetter(fallbackSetter);

        assertSame(fallbackSetter, property._fallbackSetter);
    }

    @Test
    public void testIsIgnorable() {
        PropertyName name = PropertyName.construct("testName");
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(String.class);
        PropertyName wrapperName = PropertyName.construct("wrapperName");
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedParameter param = mock(AnnotatedParameter.class);
        int index = 0;
        Object injectableValueId = INJECTABLE_VALUE_ID;

        CreatorProperty property = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, PropertyMetadata.empty());

        assertFalse(property.isIgnorable());

        property.markAsIgnorable();

        assertTrue(property.isIgnorable());
    }

    @Test(expected = InvalidDefinitionException.class)
    public void testMissingSetter() throws IOException {
        PropertyName name = PropertyName.construct("testName");
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(String.class);
        PropertyName wrapperName = PropertyName.construct("wrapperName");
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedParameter param = mock(AnnotatedParameter.class);
        int index = 0;
        Object injectableValueId = INJECTABLE_VALUE_ID;

        CreatorProperty property = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, PropertyMetadata.empty());

        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        property._verifySetter();
    }

    @Test(expected = JsonMappingException.class)
    public void testMissingInjectableValue() throws IOException {
        PropertyName name = PropertyName.construct("testName");
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(String.class);
        PropertyName wrapperName = PropertyName.construct("wrapperName");
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedParameter param = mock(AnnotatedParameter.class);
        int index = 0;
        Object injectableValueId = null;

        CreatorProperty property = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, PropertyMetadata.empty());

        DeserializationContext context = mock(DeserializationContext.class);
        Object beanInstance = new Object();

        property.inject(context, beanInstance);
    }

    @Test
    public void testInjectValue() throws IOException {
        PropertyName name = PropertyName.construct("testName");
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(String.class);
        PropertyName wrapperName = PropertyName.construct("wrapperName");
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedParameter param = mock(AnnotatedParameter.class);
        int index = 0;
        Object injectableValueId = INJECTABLE_VALUE_ID;

        CreatorProperty property = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, PropertyMetadata.empty());

        DeserializationContext context = mock(DeserializationContext.class);
        Object beanInstance = new Object();
        String injectedValue = "injected value";

        when(context.findInjectableValue(injectableValueId, property, beanInstance)).thenReturn(injectedValue);

        property.inject(context, beanInstance);

        assertEquals(injectedValue, property.get(beanInstance));
    }
}