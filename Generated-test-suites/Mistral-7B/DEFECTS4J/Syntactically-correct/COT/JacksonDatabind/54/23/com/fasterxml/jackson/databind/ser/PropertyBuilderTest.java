package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for {@link PropertyBuilder}.
 */
public class PropertyBuilderTest {

    private SerializationConfig config;
    private BeanDescription beanDesc;

    @Before
    public void setup() {
        ObjectMapper mapper = new ObjectMapper();
        config = mapper.getSerializationConfig();
        beanDesc = mapper.getTypeFactory().constructType(Bean.class);
    }

    @Test
    public void testGetClassAnnotations() {
        Annotations annotations = new PropertyBuilder(config, beanDesc).getClassAnnotations();
        assertNotNull(annotations);
        // Additional assertions can be added here if needed
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        SerializerProvider prov = mapper.getSerializerProvider();
        BeanPropertyDefinition propDef = beanDesc.findPropertyDefinition("name");
        // Test with null parameters
        assertNull(propertyBuilder.buildWriter(prov, null, null, null, null, null, null, false));
        // Test with valid parameters
        BeanSerializerFactory beanSerializerFactory = new BeanSerializerFactory(mapper.getAnnotationIntrospector(), config);
        BeanSerializer serializer = beanSerializerFactory.createBeanSerializer(beanDesc);
        JsonSerializer<?> ser = serializer.getSerializerProvider().getSerializerInstance(String.class);
        BeanPropertyWriter writer = propertyBuilder.buildWriter(prov, propDef, JavaType.getType(String[].class), ser, null, null, null, false);
        assertNotNull(writer);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildWriter_contentTypeIsNull() throws JsonMappingException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        SerializerProvider prov = mapper.getSerializerProvider();
        BeanPropertyDefinition propDef = beanDesc.findPropertyDefinition("name");
        BeanPropertyWriter writer = propertyBuilder.buildWriter(prov, propDef, JavaType.getType(Bean.class), null, null, null, null, false);
    }

    @Test
    public void testFindSerializationType() throws JsonMappingException {
        AnnotatedMember am = mock(AnnotatedMember.class);
        JavaType declaredType = JavaType.getType(Bean.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        JavaType secondary = propertyBuilder.findSerializationType(am, false, declaredType);
        // Additional assertions can be added here if needed
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindSerializationType_annotationIsNotASuperType() throws JsonMappingException {
        AnnotatedMember am = mock(AnnotatedMember.class);
        JavaType declaredType = JavaType.getType(Bean.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        JavaType annotationType = JavaType.getType(String.class);
        propertyBuilder.findSerializationType(am, false, declaredType);
        propertyBuilder.findSerializationType(am, true, declaredType);
        JavaType secondary = propertyBuilder.findSerializationType(am, false, declaredType);
        assertTrue(annotationType.getRawClass().isAssignableFrom(secondary.getRawClass()));
    }

    @Test
    public void testGetPropertyDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Object defaultBean = propertyBuilder.getDefaultBean();
        assertNull(defaultBean);
        Object name = "name";
        Object value = propertyBuilder.getPropertyDefaultValue((String) name, null, JavaType.getType(Bean.class));
        assertNull(value);
        // Additional assertions can be added here if needed
    }

    @Test
    public void testGetDefaultBean() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Object defaultBean = propertyBuilder.getDefaultBean();
        assertNull(defaultBean);
        Object bean = new Bean();
        propertyBuilder = new PropertyBuilder(config, beanDesc);
        propertyBuilder._defaultBean = bean;
        Object defaultBean1 = propertyBuilder.getDefaultBean();
        assertEquals(bean, defaultBean1);
        // Additional assertions can be added here if needed
    }

    @Test
    public void testGetDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Object defaultValue = propertyBuilder.getDefaultValue(JavaType.getType(Boolean.class));
        assertNull(defaultValue);
        defaultValue = propertyBuilder.getDefaultValue(JavaType.getType(String[].class));
        assertSame(JsonInclude.Include.NON_EMPTY, defaultValue);
        // Additional assertions can be added here if needed
    }

    @Test
    public void test_throwWrapped() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Exception e = new RuntimeException();
        String propName = "propName";
        Object defaultBean = new Object();
        Object thrown = propertyBuilder._throwWrapped(e, propName, defaultBean);
        assertSame(IllegalArgumentException.class, thrown.getClass());
        // Additional assertions can be added here if needed
    }
}