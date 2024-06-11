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

public class CreatorPropertyTest {

    @Test
    public void testCreatorProperty() {
        PropertyName name = PropertyName.construct("testProperty");
        JavaType type = BasicTypeFactory.constructUntypedPropertyType();
        PropertyName wrapperName = null;
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedParameter param = null;
        int index = 0;
        Object injectableValueId = null;
        CreatorProperty property = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, null);

        assertEquals(name, property.getName());
        assertEquals(type, property.getType());
        assertNull(property.getWrapperName());
        assertNull(property.getTypeDeserializer());
        assertNull(property.getContextAnnotations());
        assertNull(property.getAnnotated());
        assertEquals(0, property.getCreatorIndex());
        assertNull(property.getInjectableValueId());
        assertNull(property.getFallbackSetter());

        property = new CreatorProperty(property, new PropertyName("newName"));
        assertEquals("newName", property.getName().asString());
        assertEquals(name, property.getOriginalName());
        assertEquals(type, property.getType());
        assertEquals(wrapperName, property.getWrapperName());
        assertEquals(typeDeser, property.getTypeDeserializer());
        assertEquals(contextAnnotations, property.getContextAnnotations());
        assertEquals(param, property.getAnnotated());
        assertEquals(index, property.getCreatorIndex());
        assertNull(property.getInjectableValueId());
        assertNull(property.getFallbackSetter());

        property = new CreatorProperty(property, new JsonDeserializer<Object>() {}, null);
        assertEquals(property.getDeserializers().get(0), property.getValueDeserializer());
        assertEquals(property.getValueDeserializer(), property.getDeserializers().get(0));
        assertNull(property.getNullProvider());

        property.setFallbackSetter(new CreatorProperty(new PropertyName("testFallbackSetter"), type, wrapperName, typeDeser, contextAnnotations, param, index, null, null) {
            @Override
            public void set(Object instance, Object value) throws IOException {
                // Empty implementation
            }
        });
        assertNotNull(property.getFallbackSetter());

        property.markAsIgnorable();
        assertTrue(property.isIgnorable());
    }

    @Test
    public void testFindInjectableValue() throws IOException {
        PropertyName name = PropertyName.construct("testProperty");
        JavaType type = BasicTypeFactory.constructUntypedPropertyType();
        PropertyName wrapperName = null;
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedParameter param = Mockito.mock(AnnotatedParameter.class);
        int index = 0;
        Object injectableValueId = "injectableValueId";
        CreatorProperty property = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, null);

        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        Object beanInstance = Mockito.mock(Object.class);

        Mockito.when(context.findInjectableValue(injectableValueId, property, beanInstance)).thenReturn(new Object() {});

        Object value = property.findInjectableValue(context, beanInstance);
        assertNotNull(value);

        Mockito.verify(context).findInjectableValue(injectableValueId, property, beanInstance);
    }

    @Test
    public void testInject() throws IOException {
        PropertyName name = PropertyName.construct("testProperty");
        JavaType type = BasicTypeFactory.constructUntypedPropertyType();
        PropertyName wrapperName = null;
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedParameter param = Mockito.mock(AnnotatedParameter.class);
        int index = 0;
        Object injectableValueId = "injectableValueId";
        CreatorProperty property = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, null);

        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        Object beanInstance = Mockito.mock(Object.class);
        Object injectedValue = new Object() {
        };

        Mockito.when(context.findInjectableValue(injectableValueId, property, beanInstance)).thenReturn(injectedValue);

        property.inject(context, beanInstance);

        assertEquals(injectedValue, property.get(beanInstance));

        Mockito.verify(beanInstance, Mockito.times(1)).setField(property.getName().getShortName(), injectedValue);
    }
}