package com.example.junit.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

import org.junit.Test;

public class CreatorPropertyTest {

    @Test
    public void testCreatorPropertyConstructor() throws Exception {
        CreatorProperty creatorProperty = new CreatorProperty(
                new PropertyName("testProperty"),
                new JavaType(Integer.class),
                null,
                null,
                Annotations.empty(),
                null,
                0,
                null,
                null);

        assertEquals("testProperty", creatorProperty.getName().getName());
        assertEquals(Integer.class, creatorProperty.getType());
        assertNull(creatorProperty._annotated);
        assertNull(creatorProperty._injectableValueId);
        assertNull(creatorProperty._fallbackSetter);
        assertEquals(0, creatorProperty._creatorIndex);
        assertFalse(creatorProperty._ignorable);
    }

    @Test
    public void testCreatorPropertyCopyConstructor() throws Exception {
        CreatorProperty originalCreatorProperty = new CreatorProperty(
                new PropertyName("testProperty"),
                new JavaType(Integer.class),
                null,
                null,
                Annotations.empty(),
                null,
                0,
                null,
                null);

        CreatorProperty copiedCreatorProperty = new CreatorProperty(originalCreatorProperty, new PropertyName("newTestProperty"));

        assertEquals("newTestProperty", copiedCreatorProperty.getName().getName());
        assertEquals(Integer.class, copiedCreatorProperty.getType());
        assertEquals(originalCreatorProperty._annotated, copiedCreatorProperty._annotated);
        assertEquals(originalCreatorProperty._injectableValueId, copiedCreatorProperty._injectableValueId);
        assertEquals(originalCreatorProperty._fallbackSetter, copiedCreatorProperty._fallbackSetter);
        assertEquals(originalCreatorProperty._creatorIndex, copiedCreatorProperty._creatorIndex);
        assertEquals(originalCreatorProperty._ignorable, copiedCreatorProperty._ignorable);
    }

    @Test
    public void testCreatorPropertyWithValueDeserializer() throws Exception {
        CreatorProperty creatorProperty = new CreatorProperty(
                new PropertyName("testProperty"),
                new JavaType(Integer.class),
                null,
                null,
                Annotations.empty(),
                null,
                0,
                null,
                null);

        JsonDeserializer<Integer> jsonDeserializer = new StandardDeserializer<Integer>(Integer.class);
        creatorProperty = new CreatorProperty(creatorProperty, jsonDeserializer, null);

        assertEquals(jsonDeserializer, creatorProperty._valueDeserializer);
        assertNull(creatorProperty._nullProvider);
    }

    @Test
    public void testCreatorPropertyWithNullProvider() throws Exception {
        CreatorProperty creatorProperty = new CreatorProperty(
                new PropertyName("testProperty"),
                new JavaType(Integer.class),
                null,
                null,
                Annotations.empty(),
                null,
                0,
                null,
                null);

        NullValueProvider nullValueProvider = new StandardNullValueProvider();
        creatorProperty = new CreatorProperty(creatorProperty, null, nullValueProvider);

        assertEquals(nullValueProvider, creatorProperty._nullProvider);
        assertNull(creatorProperty._valueDeserializer);
    }

    @Test
    public void testCreatorPropertyFixAccess() throws Exception {
        CreatorProperty creatorProperty = new CreatorProperty(
                new PropertyName("testProperty"),
                new JavaType(Integer.class),
                null,
                null,
                Annotations.empty(),
                null,
                0,
                null,
                null);

        DeserializationConfig config = new DeserializationConfig();
        creatorProperty.fixAccess(config);

        assertNotNull(creatorProperty._fallbackSetter);
    }

    @Test(expected = InvalidDefinitionException.class)
    public void testMissingSetter() throws Exception {
        CreatorProperty creatorProperty = new CreatorProperty(
                new PropertyName("testProperty"),
                new JavaType(Integer.class),
                null,
                null,
                Annotations.empty(),
                null,
                0,
                null,
                null);

        JsonParser parser = new TestJsonParser();
        DeserializationContext context = new TestDeserializationContext();
        Object instance = new Object();

        creatorProperty.deserializeAndSet(parser, context, instance);
    }

    @Test
    public void testFindInjectableValue() throws Exception {
        CreatorProperty creatorProperty = new CreatorProperty(
                new PropertyName("testProperty"),
                new JavaType(Integer.class),
                null,
                null,
                Annotations.empty(),
                null,
                0,
                null,
                null);

        creatorProperty._injectableValueId = "testValueId";

        Object beanInstance = new Object();
        DeserializationContext context = new TestDeserializationContext();

        Object injectedValue = creatorProperty.findInjectableValue(context, beanInstance);
        assertNotNull(injectedValue);
        assertEquals("testValueId", injectedValue.getClass().getName());
    }

    @Test
    public void testInject() throws Exception {
        CreatorProperty creatorProperty = new CreatorProperty(
                new PropertyName("testProperty"),
                new JavaType(Integer.class),
                null,
                null,
                Annotations.empty(),
                null,
                0,
                null,
                null);

        creatorProperty._injectableValueId = "testValueId";

        Object beanInstance = new Object();
        DeserializationContext context = new TestDeserializationContext();

        Object injectedValue = creatorProperty.findInjectableValue(context, beanInstance);
        creatorProperty.inject(context, beanInstance);

        assertEquals(injectedValue, creatorProperty.get(beanInstance));
    }
}