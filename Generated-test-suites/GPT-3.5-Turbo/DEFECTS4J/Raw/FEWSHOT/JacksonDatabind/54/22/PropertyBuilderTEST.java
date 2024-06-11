package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.PropertyBuilderTest.MockBean;
import com.fasterxml.jackson.databind.util.Annotations;

public class PropertyBuilderTest {

    private PropertyBuilder propertyBuilder;

    @Before
    public void setup() {
        AnnotationIntrospector annotationIntrospector = new AnnotationIntrospectorPair(null, null);
        SerializationConfig config = new SerializationConfig(null, annotationIntrospector, null);
        BeanDescription beanDescription = new BeanDescription(null, null, null);
        propertyBuilder = new PropertyBuilder(config, beanDescription);
    }

    @Test
    public void testBuildWriterNonDefaultInclusion() throws Exception {
        SerializerProvider serializerProvider = new SerializerProvider();
        JavaType declaredType = new JavaType(MockBean.class);
        JsonSerializer<?> serializer = new JsonSerializer();
        TypeSerializer typeSerializer = new TypeSerializer();
        TypeSerializer contentTypeSerializer = new TypeSerializer();
        AnnotatedMember annotatedMember = new AnnotatedMember();
        boolean defaultUseStaticTyping = false;

        JsonInclude.Value defaultInclusion = JsonInclude.Value.construct(JsonInclude.Include.NON_DEFAULT, JsonInclude.Include.NON_DEFAULT);
        propertyBuilder._defaultInclusion = defaultInclusion;

        BeanPropertyWriter result = propertyBuilder.buildWriter(serializerProvider, null, declaredType, serializer, typeSerializer, contentTypeSerializer, annotatedMember, defaultUseStaticTyping);

        assertEquals(JsonInclude.Include.NON_DEFAULT, result.propertyInclusion);
        assertNull(result.valueToSuppress);
        assertTrue(result.suppressNulls);
    }

    @Test
    public void testGetPropertyDefaultValue() throws Exception {
        String name = "testProp";
        AnnotatedMember member = new AnnotatedMember();
        JavaType type = new JavaType(MockBean.class);

        Object result = propertyBuilder.getPropertyDefaultValue(name, member, type);

        assertNull(result);
    }

    @Test
    public void testGetDefaultValueForPrimitives() throws Exception {
        JavaType type = new JavaType(int.class);

        Object result = propertyBuilder.getDefaultValue(type);

        assertEquals(0, result);
    }

    @Test
    public void testGetDefaultValueForStrings() throws Exception {
        JavaType type = new JavaType(String.class);

        Object result = propertyBuilder.getDefaultValue(type);

        assertEquals("", result);
    }

    @Test
    public void testGetDefaultValueForContainers() throws Exception {
        JavaType type = new JavaType(MockBean.class);

        Object result = propertyBuilder.getDefaultValue(type);

        assertEquals(JsonInclude.Include.NON_EMPTY, result);
    }
}