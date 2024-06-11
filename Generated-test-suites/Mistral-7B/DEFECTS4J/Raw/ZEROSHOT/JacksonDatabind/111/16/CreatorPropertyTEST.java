package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer.EmptyContextTypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.BeanProperty;
import com.fasterxml.jackson.databind.util.IntegerProperties;

public class CreatorPropertyTest {

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testCreatorProperty() throws Exception {
        // Test constructor
        PropertyName name = PropertyName.construct("testProperty");
        JavaType type = mock(JavaType.class);
        PropertyName wrapperName = PropertyName.construct("wrapperProperty");
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = Annotations.empty();
        AnnotatedParameter param = mock(AnnotatedParameter.class);
        int index = 0;
        Object injectableValueId = "testId";
        CreatorProperty creatorProperty = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, new IntegerProperties());

        assertSame(name, creatorProperty.getName());
        assertSame(type, creatorProperty.getType());
        assertSame(wrapperName, creatorProperty.getWrapperName());
        assertSame(typeDeser, creatorProperty.getTypeDeserializer());
        assertSame(contextAnnotations, creatorProperty.getContextAnnotations());
        assertSame(param, creatorProperty.getAnnotated());
        assertSame(index, creatorProperty.getCreatorIndex());
        assertSame(injectableValueId, creatorProperty.getInjectableValueId());
        assertNull(creatorProperty.getFallbackSetter());
        assertFalse(creatorProperty.isIgnorable());

        // Test copy constructor
        CreatorProperty copy = new CreatorProperty(creatorProperty, null);
        assertSame(creatorProperty, copy);

        // Test deserializer constructor
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);
        NullValueProvider nva = new EmptyContextTypeDeserializer();
        CreatorProperty deserCreatorProperty = new CreatorProperty(creatorProperty, deser, nva);
        assertSame(creatorProperty, deserCreatorProperty);

        // Test setter for fallbackSetter
        SettableBeanProperty fallbackSetter = mock(SettableBeanProperty.class);
        creatorProperty.setFallbackSetter(fallbackSetter);
        assertSame(fallbackSetter, creatorProperty.getFallbackSetter());

        // Test markAsIgnorable
        creatorProperty.markAsIgnorable();
        assertTrue(creatorProperty.isIgnorable());

        // Test findInjectableValue
        DeserializationContext context = mock(DeserializationContext.class);
        Object beanInstance = new Object();
        when(context.findInjectableValue(injectableValueId, creatorProperty, beanInstance)).thenReturn(new Object());
        Object result = creatorProperty.findInjectableValue(context, beanInstance);
        assertSame(new Object(), result);

        // Test inject
        Object newValue = new Object();
        creatorProperty.inject(context, beanInstance);
        creatorProperty.set(beanInstance, newValue);
        assertEquals(newValue, findPropertyValue(context, beanInstance, creatorProperty.getName()));

        // Test deserializeAndSet
        JsonParser p = mock(JsonParser.class);
        when(p.getValueAsToken()).thenReturn(mock(JsonToken.class));
        when(p.nextValue()).thenReturn(mock(JsonToken.class));
        when(p.getValueAsString()).thenReturn("testValue");

        when(context.findInjectableValue(injectableValueId, creatorProperty, beanInstance)).thenReturn(new Object());
        when(creatorProperty.getTypeDeserializer().deserialize(p, context, null)).thenReturn(new Object());
        creatorProperty.deserializeAndSet(p, context, beanInstance);
        assertEquals(new Object(), findPropertyValue(context, beanInstance, creatorProperty.getName()));

        // Test deserializeSetAndReturn
        when(creatorProperty.getTypeDeserializer().deserialize(p, context, null)).thenReturn(newObjectWithValue(newValue));
        assertEquals(newValue, creatorProperty.deserializeSetAndReturn(p, context, beanInstance));

        // Test set
        creatorProperty.set(beanInstance, newValue);
        assertEquals(newValue, findPropertyValue(context, beanInstance, creatorProperty.getName()));

        // Test setAndReturn
        assertEquals(newValue, creatorProperty.setAndReturn(beanInstance, newValue));

        // Test toString
        assertEquals("{creator property, name 'testProperty'; inject id 'testId'}", creatorProperty.toString());

        // Test _verifySetter
        creatorProperty._verifySetter();

        // Test _reportMissingSetter
        JsonParser p2 = mock(JsonParser.class);
        DeserializationContext context2 = mock(DeserializationContext.class);
        creatorProperty._reportMissingSetter(p2, context2);

        // Test getter for Annotation
        JsonProperty jsonProperty = mock(JsonProperty.class);
        when(jsonProperty.value()).thenReturn("test");
        Annotation annotation = mock(Annotation.class);
        when(param.getAnnotation(jsonProperty.getClass())).thenReturn(annotation);
        when(contextAnnotations.findAnnotation(jsonProperty.getClass())).thenReturn(annotation);
        assertEquals(annotation, creatorProperty.getAnnotation(jsonProperty.getClass()));

        // Test getter for AnnotatedMember
        assertEquals(param, creatorProperty.getMember());

        // Test getter for getCreatorIndex
        assertEquals(index, creatorProperty.getCreatorIndex());

        // Test deserialize
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"testProperty\":\"testValue\"}";
        assertEquals(newObjectWithValue(newValue), mapper.readValue(json, creatorProperty.getType()));

        // Test deserialize with TypeDeserializer
        TypeDeserializer typeDeser2 = mock(TypeDeserializer.class);
        when(typeDeser.deserialize(p, context, null)).thenReturn(new Object());
        assertEquals(new Object(), mapper.readValue(json, creatorProperty.getType(), context));

        // Test deserialize with JsonDeserializer
        JsonDeserializer<?> deser2 = mock(JsonDeserializer.class);
        when(deser.deserialize(p, context, null)).thenReturn(new Object());
        assertEquals(new Object(), mapper.readValue(json, creatorProperty.getType(), context, deser));

        // Test deserialize with TypeDeserializer and JsonDeserializer
        when(typeDeser.deserialize(p, context, null)).thenReturn(new Object());
        when(deser.deserialize(p, context, null)).thenReturn(new Object());
        assertEquals(new Object(), mapper.readValue(json, creatorProperty.getType(), context, deser, typeDeser));

        // Test fixAccess
        creatorProperty.fixAccess(context);

        // Test isIgnorable
        creatorProperty.markAsIgnorable();
        assertTrue(creatorProperty.isIgnorable());

        // Test deserializeAndSet with missing setter
        creatorProperty._reportMissingSetter(p, context);
    }

    private Object findPropertyValue(DeserializationContext context, Object beanInstance, PropertyName name)
            throws JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        BeanProperty beanProperty = new BeanProperty(context.getBeanDescription().findBeanProperty(name));
        return mapper.TreeAsTokens(mapper.findAndModifyValue(context, beanInstance, beanProperty))
                .get(0).asText();
    }

    private static Object newObjectWithValue(Object value) {
        return new Object() {
            @Override
            public Object getValue() {
                return value;
            }
        };
    }
}