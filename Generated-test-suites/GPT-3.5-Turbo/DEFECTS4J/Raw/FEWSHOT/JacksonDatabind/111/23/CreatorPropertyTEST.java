package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.fasterxml.jackson.core.JsonParser;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;

public class CreatorPropertyTest {
  
    @Test
    public void testConstructor() {
        PropertyName name = mock(PropertyName.class);
        JavaType type = mock(JavaType.class);
        PropertyName wrapperName = mock(PropertyName.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedParameter param = mock(AnnotatedParameter.class);
        int index = 1;
        Object injectableValueId = mock(Object.class);
        PropertyMetadata metadata = mock(PropertyMetadata.class);
    
        CreatorProperty creatorProperty = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, metadata);
    
        assertEquals(name, creatorProperty.getFullName());
        assertEquals(type, creatorProperty.getType());
        assertEquals(wrapperName, creatorProperty.getWrapperName());
        assertEquals(typeDeser, creatorProperty.getTypeDeserializer());
        assertEquals(contextAnnotations, creatorProperty.getContextAnnotations());
        assertEquals(param, creatorProperty.getMember());
        assertEquals(index, creatorProperty.getCreatorIndex());
        assertEquals(injectableValueId, creatorProperty.getInjectableValueId());
        assertEquals(metadata, creatorProperty.getMetadata());
        assertNull(creatorProperty.getValueDeserializer());
        assertNotNull(creatorProperty.getNullValueProvider());
        assertNull(creatorProperty.getMetadata().getDescription());
        assertNull(creatorProperty.getMetadata().getDefaultValue());
        assertNull(creatorProperty.getMetadata().getInclusion());
    }
    
    @Test
    public void testDeserializeAndSet() throws IOException {
        CreatorProperty creatorProperty = new CreatorProperty(
            mock(PropertyName.class),
            mock(JavaType.class),
            mock(PropertyName.class),
            mock(TypeDeserializer.class),
            mock(Annotations.class),
            mock(AnnotatedParameter.class),
            1,
            mock(Object.class),
            mock(PropertyMetadata.class)
        );
    
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
    
        creatorProperty.deserializeAndSet(p, ctxt, instance);
    
        verify(creatorProperty).set(instance, creatorProperty.deserialize(p, ctxt));
    }
    
    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        CreatorProperty creatorProperty = new CreatorProperty(
            mock(PropertyName.class),
            mock(JavaType.class),
            mock(PropertyName.class),
            mock(TypeDeserializer.class),
            mock(Annotations.class),
            mock(AnnotatedParameter.class),
            1,
            mock(Object.class),
            mock(PropertyMetadata.class)
        );
    
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
    
        creatorProperty.deserializeSetAndReturn(p, ctxt, instance);
    
        verify(creatorProperty).setAndReturn(instance, creatorProperty.deserialize(p, ctxt));
    }
    
    @Test
    public void testSet() throws IOException {
        CreatorProperty creatorProperty = new CreatorProperty(
            mock(PropertyName.class),
            mock(JavaType.class),
            mock(PropertyName.class),
            mock(TypeDeserializer.class),
            mock(Annotations.class),
            mock(AnnotatedParameter.class),
            1,
            mock(Object.class),
            mock(PropertyMetadata.class)
        );
    
        Object instance = mock(Object.class);
        Object value = mock(Object.class);
    
        creatorProperty.set(instance, value);
    
        verify(creatorProperty).set(instance, value);
    }
    
    @Test
    public void testSetAndReturn() throws IOException {
        CreatorProperty creatorProperty = new CreatorProperty(
            mock(PropertyName.class),
            mock(JavaType.class),
            mock(PropertyName.class),
            mock(TypeDeserializer.class),
            mock(Annotations.class),
            mock(AnnotatedParameter.class),
            1,
            mock(Object.class),
            mock(PropertyMetadata.class)
        );
    
        Object instance = mock(Object.class);
        Object value = mock(Object.class);
    
        creatorProperty.setAndReturn(instance, value);
    
        verify(creatorProperty).setAndReturn(instance, value);
    }
    
    @Test
    public void testFindInjectableValue() throws IOException {
        CreatorProperty creatorProperty = spy(
            new CreatorProperty(
                mock(PropertyName.class),
                mock(JavaType.class),
                mock(PropertyName.class),
                mock(TypeDeserializer.class),
                mock(Annotations.class),
                mock(AnnotatedParameter.class),
                1,
                mock(Object.class),
                mock(PropertyMetadata.class)
            )
        );
    
        DeserializationContext context = mock(DeserializationContext.class);
        Object beanInstance = mock(Object.class);
    
        creatorProperty.findInjectableValue(context, beanInstance);
    
        verify(context).findInjectableValue(creatorProperty.getInjectableValueId(), creatorProperty, beanInstance);
    }
    
    @Test
    public void testInject() throws IOException {
        CreatorProperty creatorProperty = spy(
            new CreatorProperty(
                mock(PropertyName.class),
                mock(JavaType.class),
                mock(PropertyName.class),
                mock(TypeDeserializer.class),
                mock(Annotations.class),
                mock(AnnotatedParameter.class),
                1,
                mock(Object.class),
                mock(PropertyMetadata.class)
            )
        );
    
        DeserializationContext context = mock(DeserializationContext.class);
        Object beanInstance = mock(Object.class);
    
        creatorProperty.inject(context, beanInstance);
    
        verify(creatorProperty).set(beanInstance, creatorProperty.findInjectableValue(context, beanInstance));
    }
    
    @Test
    public void testGetAnnotation() {
        CreatorProperty creatorProperty = new CreatorProperty(
            mock(PropertyName.class),
            mock(JavaType.class),
            mock(PropertyName.class),
            mock(TypeDeserializer.class),
            mock(Annotations.class),
            mock(AnnotatedParameter.class),
            1,
            mock(Object.class),
            mock(PropertyMetadata.class)
        );
    
        Class<Annotation> acls = Annotation.class;
    
        assertNull(creatorProperty.getAnnotation(acls));
        verify(creatorProperty.getMember(), times(1)).getAnnotation(acls);
    }
    
    @Test
    public void testToString() {
        CreatorProperty creatorProperty = new CreatorProperty(
            mock(PropertyName.class),
            mock(JavaType.class),
            mock(PropertyName.class),
            mock(TypeDeserializer.class),
            mock(Annotations.class),
            mock(AnnotatedParameter.class),
            1,
            mock(Object.class),
            mock(PropertyMetadata.class)
        );
    
        assertNotNull(creatorProperty.toString());
    }
}