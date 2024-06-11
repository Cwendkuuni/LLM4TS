package com.fasterxml.jackson.databind.ser; // make sure to import all necessary packages

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;

/**
 * Test class for PropertyBuilder
 */
public class PropertyBuilderTest {

    private SerializationConfig config;
    private BeanDescription beanDesc;
    private AnnotationIntrospector annotationIntrospector;

    @Before
    public void setUp() {
        ObjectMapper mapper = new ObjectMapper();
        config = mapper.getSerializationConfig();
        beanDesc = mapper.findBeanDescription(Object[].class);
        annotationIntrospector = config.getAnnotationIntrospector();
    }

    @Test
    public void testPropertyBuilderConstructor() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Assert.assertNotNull(propertyBuilder);
        Assert.assertEquals(config, propertyBuilder._config);
        Assert.assertEquals(beanDesc, propertyBuilder._beanDesc);
        Assert.assertEquals(annotationIntrospector, propertyBuilder._annotationIntrospector);
    }

    @Test
    public void testGetClassAnnotations() {
        Class<?>[] classAnnotations = propertyBuilder.getClassAnnotations();
        Assert.assertNotNull(classAnnotations);
        Assert.assertTrue(classAnnotations.length > 0);
    }

    @Test
    public void testBuildWriterMethod() throws JsonMappingException {
        BeanPropertyDefinition propDef = propertyBuilder.findProperty("testProperty");
        Object ser = new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                gen.writeString("test");
            }
        };
        TypeSerializer typeSer = TypeSerializer.staticSerializer(String.class);
        TypeSerializer contentTypeSer = TypeSerializer.instance;
        AnnotatedMember am = null;
        JavaType declaredType = Object.class;

        BeanPropertyWriter writer = propertyBuilder.buildWriter(null, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);
        Assert.assertNotNull(writer);
    }

    @Test(expected = JsonMappingException.class)
    public void testBuildWriterMethodWithException() throws JsonMappingException {
        BeanPropertyDefinition propDef = propertyBuilder.findProperty("testProperty");
        TypeSerializer typeSer = null;
        try {
            propertyBuilder.buildWriter(null, propDef, null, null, typeSer, null, null, false);
        } catch (JsonMappingException e) {
            throw new AssertionError("Unexpected exception", e);
        }
    }

    @Test
    public void testFindSerializationTypeMethod() throws JsonMappingException {
        Annotated a = mock(Annotated.class);
        when(a.getRawType()).thenReturn(String[].class);
        JavaType declaredType = JavaType.construct((Class<?>) String[].class);

        JavaType expectedType = JavaType.construct((Class<?>) String.class);
        JavaType actualType = propertyBuilder.findSerializationType(a, false, declaredType);
        Assert.assertEquals(expectedType, actualType);

        when(a.getRawType()).thenReturn(Object.class);
        actualType = propertyBuilder.findSerializationType(a, false, declaredType);
        Assert.assertNull(actualType);
    }

    @Test
    public void testGetDefaultBeanMethod() {
        Object defaultBean = propertyBuilder.getDefaultBean();
        Assert.assertNull(defaultBean);
    }

    @Test
    public void testGetPropertyDefaultValueMethod() {
        String propName = "testProperty";
        AnnotatedMember member = mock(AnnotatedMember.class);
        when(member.getName()).thenReturn(propName);
        JavaType type = JavaType.construct((Class<?>) Integer.class);

        Object defaultBean = new Object() {
            int testProperty = 5;
        };
        propertyBuilder._defaultBean = defaultBean;

        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue(propName, member, type);
        Assert.assertEquals(5, propertyDefaultValue);

        propertyBuilder._defaultBean = null;
        propertyDefaultValue = propertyBuilder.getPropertyDefaultValue(propName, member, type);
        Assert.assertEquals(BeanUtil.getDefaultValue(type), propertyDefaultValue);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetPropertyDefaultValueMethodWithException() {
        String propName = "testProperty";
        AnnotatedMember member = mock(AnnotatedMember.class);
        when(member.getName()).thenReturn(propName);
        JavaType type = JavaType.construct((Class<?>) Integer.class);

        try {
            propertyBuilder.getPropertyDefaultValue(propName, member, type);
        } catch (IllegalArgumentException e) {
            throw new AssertionError("Unexpected exception", e);
        }
    }

    @Test
    public void testGetDefaultValueMethod() {
        JavaType type = JavaType.construct((Class<?>) Integer.class);
        Object defaultValue = propertyBuilder.getDefaultValue(type);
        Assert.assertEquals(0, defaultValue);
    }
}