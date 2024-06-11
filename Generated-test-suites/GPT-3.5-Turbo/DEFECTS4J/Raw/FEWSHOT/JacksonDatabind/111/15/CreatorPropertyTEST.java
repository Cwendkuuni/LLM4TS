package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class CreatorPropertyTest {

    @Test(expected = InvalidDefinitionException.class)
    public void testFindInjectableValue_NoInjectableValueId() throws Exception {
        CreatorProperty creatorProperty = createCreatorProperty();
        creatorProperty.findInjectableValue(null, null);
    }

    @Test
    public void testFindInjectableValue_WithInjectableValueId() throws Exception {
        CreatorProperty creatorProperty = createCreatorProperty();
        Object value = creatorProperty.findInjectableValue(null, null);
        assertNotNull(value);
    }

    @Test
    public void testInject() throws Exception {
        CreatorProperty creatorProperty = createCreatorProperty();
        creatorProperty.inject(null, null);
    }

    @Test
    public void testGetAnnotation() throws Exception {
        CreatorProperty creatorProperty = createCreatorProperty();
        assertNotNull(creatorProperty.getAnnotation(Override.class));
    }

    @Test
    public void testGetMember() {
        CreatorProperty creatorProperty = createCreatorProperty();
        assertNotNull(creatorProperty.getMember());
    }

    @Test
    public void testGetCreatorIndex() {
        CreatorProperty creatorProperty = createCreatorProperty();
        assertEquals(0, creatorProperty.getCreatorIndex());
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        CreatorProperty creatorProperty = createCreatorProperty();
        creatorProperty.deserializeAndSet(null, null, null);
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        CreatorProperty creatorProperty = createCreatorProperty();
        creatorProperty.deserializeSetAndReturn(null, null, null);
    }

    @Test
    public void testSet() throws Exception {
        CreatorProperty creatorProperty = createCreatorProperty();
        creatorProperty.set(null, null);
    }

    @Test
    public void testSetAndReturn() throws Exception {
        CreatorProperty creatorProperty = createCreatorProperty();
        creatorProperty.setAndReturn(null, null);
    }

    @Test
    public void testGetInjectableValueId() {
        CreatorProperty creatorProperty = createCreatorProperty();
        assertNull(creatorProperty.getInjectableValueId());
    }

    @Test
    public void testToString() {
        CreatorProperty creatorProperty = createCreatorProperty();
        assertNotNull(creatorProperty.toString());
    }

    private CreatorProperty createCreatorProperty() {
        PropertyName name = new PropertyName("test");
        JavaType type = JavaType.construct(String.class);
        PropertyName wrapperName = new PropertyName("wrapper");
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedParameter param = null;
        int index = 0;
        Object injectableValueId = null;
        PropertyMetadata metadata = null;

        return new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, metadata);
    }
}