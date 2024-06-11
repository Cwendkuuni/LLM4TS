package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

public class CreatorPropertyTest {

    private static ObjectMapper mapper;

    @BeforeClass
    public static void setup() throws IOException {
        mapper = new ObjectMapper();
    }

    /**
     * Test for CreatorProperty constructor with valid parameters.
     */
    @Test
    public void testCreatorPropertyConstructorValidParameters() throws IOException {
        JavaType type = mapper.getTypeFactory().constructType(CreatorProperty.class);
        PropertyName name = PropertyName.construct("propertyName");
        JavaType propertyType = mapper.getTypeFactory().constructType(String.class);
        PropertyName wrapperName = null;
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedParameter param = mock(AnnotatedParameter.class);
        int index = 0;
        Object injectableValueId = "injectId";

        CreatorProperty creatorProperty = new CreatorProperty(name, propertyType, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, null);

        assertEquals(name, creatorProperty.getName());
        assertEquals(propertyType, creatorProperty.getType());
        assertNull(creatorProperty.getWrapperName());
        assertNull(creatorProperty.getTypeDeserializer());
        assertNull(creatorProperty.getMetadata());
        assertEquals(param, creatorProperty.getAnnotated());
        assertEquals(index, creatorProperty.getCreatorIndex());
        assertEquals(injectableValueId, creatorProperty.getInjectableValueId());
        assertNull(creatorProperty.getFallbackSetter());
        assertFalse(creatorProperty.isIgnorable());
    }

    /**
     * Test for CreatorProperty constructor with null parameters.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreatorPropertyConstructorNullParameters() {
        PropertyName name = null;
        JavaType propertyType = null;
        PropertyName wrapperName = null;
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedParameter param = null;
        int index = 0;
        Object injectableValueId = null;

        new CreatorProperty(name, propertyType, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, null);
    }

    /**
     * Test for CreatorProperty constructor with invalid injectableValueId.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreatorPropertyConstructorInvalidInjectableValueId() {
        PropertyName name = PropertyName.construct("propertyName");
        JavaType propertyType = mapper.getTypeFactory().constructType(String.class);
        PropertyName wrapperName = null;
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedParameter param = mock(AnnotatedParameter.class);
        int index = 0;
        Object injectableValueId = null;

        new CreatorProperty(name, propertyType, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, null);
    }

    /**
     * Test for CreatorProperty setFallbackSetter method.
     */
    @Test
    public void testCreatorPropertySetFallbackSetter() throws IOException {
        PropertyName name = PropertyName.construct("propertyName");
        JavaType propertyType = mapper.getTypeFactory().constructType(String.class);
        PropertyName wrapperName = null;
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedParameter param = mock(AnnotatedParameter.class);
        int index = 0;
        Object injectableValueId = "injectId";
        CreatorProperty creatorProperty = new CreatorProperty(name, propertyType, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, null);

        SettableBeanProperty fallbackSetter = mock(SettableBeanProperty.class);

        creatorProperty.setFallbackSetter(fallbackSetter);

        assertEquals(fallbackSetter, creatorProperty.getFallbackSetter());
    }

    /**
     * Test for CreatorProperty isIgnorable method.
     */
    @Test
    public void testCreatorPropertyIsIgnorable() {
        PropertyName name = PropertyName.construct("propertyName");
        JavaType propertyType = mapper.getTypeFactory().constructType(String.class);
        PropertyName wrapperName = null;
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedParameter param = mock(AnnotatedParameter.class);
        int index = 0;
        Object injectableValueId = "injectId";
        CreatorProperty creatorProperty = new CreatorProperty(name, propertyType, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, null);

        creatorProperty.markAsIgnorable();

        assertTrue(creatorProperty.isIgnorable());
    }

    /**
     * Test for CreatorProperty findInjectableValue method.
     */
    @Test
    public void testCreatorPropertyFindInjectableValue() throws IOException {
        ObjectNode rootObject = mapper.createObjectNode();
        ObjectNode injectValueNode = mapper.createObjectNode();
        injectValueNode.put("value", "injectValue");
        rootObject.set("injectValueId", injectValueNode);

        DeserializationContext context = mapper.getDeserializationContext();
        Object beanInstance = new Object();

        PropertyName name = PropertyName.construct("propertyName");
        JavaType propertyType = mapper.getTypeFactory().constructType(String.class);
        PropertyName wrapperName = null;
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedParameter param = mock(AnnotatedParameter.class);
        int index = 0;
        Object injectableValueId = "injectId";

        CreatorProperty creatorProperty = new CreatorProperty(name, propertyType, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, null);

        Object result = creatorProperty.findInjectableValue(context, beanInstance);

        assertEquals("injectValue", result);
    }

    /**
     * Test for CreatorProperty inject method.
     */
    @Test
    public void testCreatorPropertyInject() throws IOException {
        ObjectNode rootObject = mapper.createObjectNode();
        ObjectNode injectValueNode = mapper.createObjectNode();
        injectValueNode.put("value", "injectValue");
        rootObject.set("injectValueId", injectValueNode);

        Object beanInstance = new Object();

        PropertyName name = PropertyName.construct("propertyName");
        JavaType propertyType = mapper.getTypeFactory().constructType(String.class);
        PropertyName wrapperName = null;
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedParameter param = mock(AnnotatedParameter.class);
        int index = 0;
        Object injectableValueId = "injectId";

        CreatorProperty creatorProperty = new CreatorProperty(name, propertyType, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, null);

        creatorProperty.inject(context, beanInstance);

        assertEquals("injectValue", creatorProperty.get(beanInstance));
    }

    /**
     * Test for CreatorProperty deserializeAndSet method.
     */
    @Test
    public void testCreatorPropertyDeserializeAndSet() throws IOException {
        String jsonString = "{\"propertyName\": \"value\"}";
        Object beanInstance = new Object();

        PropertyName name = PropertyName.construct("propertyName");
        JavaType propertyType = mapper.getTypeFactory().constructType(String.class);
        PropertyName wrapperName = null;
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedParameter param = mock(AnnotatedParameter.class);
        int index = 0;
        Object injectableValueId = null;

        CreatorProperty creatorProperty = new CreatorProperty(name, propertyType, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, null);

        JsonParser parser = mapper.getFactory().createParser(jsonString);
        DeserializationContext context = mapper.getDeserializationContext();

        creatorProperty.deserializeAndSet(parser, context, beanInstance);

        assertEquals("value", creatorProperty.get(beanInstance));
    }

    /**
     * Test for CreatorProperty deserializeSetAndReturn method.
     */
    @Test
    public void testCreatorPropertyDeserializeSetAndReturn() throws IOException {
        String jsonString = "{\"propertyName\": \"value\"}";
        Object beanInstance = new Object();

        PropertyName name = PropertyName.construct("propertyName");
        JavaType propertyType = mapper.getTypeFactory().constructType(String.class);
        PropertyName wrapperName = null;
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedParameter param = mock(AnnotatedParameter.class);
        int index = 0;
        Object injectableValueId = null;

        CreatorProperty creatorProperty = new CreatorProperty(name, propertyType, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, null);

        JsonParser parser = mapper.getFactory().createParser(jsonString);
        DeserializationContext context = mapper.getDeserializationContext();

        Object result = creatorProperty.deserializeSetAndReturn(parser, context, beanInstance);

        assertEquals("value", result);
        assertEquals("value", creatorProperty.get(beanInstance));
    }
}