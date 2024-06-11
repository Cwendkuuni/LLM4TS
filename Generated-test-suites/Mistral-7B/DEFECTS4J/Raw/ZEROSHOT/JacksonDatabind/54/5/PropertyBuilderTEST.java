package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.BeanSerializerFactory;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.BeanPropertyWriter;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;

public class PropertyBuilderTest {

    private SerializationConfig config;
    private BeanDescription beanDesc;
    private AnnotationIntrospector annotationIntrospector;
    private PropertyBuilder propertyBuilder;

    @Before
    public void setUp() {
        config = Mockito.mock(SerializationConfig.class);
        beanDesc = Mockito.mock(BeanDescription.class);
        annotationIntrospector = Mockito.mock(AnnotationIntrospector.class);

        propertyBuilder = new PropertyBuilder(config, beanDesc);
    }

    @Test
    public void testBuildWriter_NullSerializer() {
        SerializerProvider prov = Mockito.mock(SerializerProvider.class);
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        JavaType declaredType = Mockito.mock(JavaType.class);
        TypeSerializer typeSer = Mockito.mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = Mockito.mock(TypeSerializer.class);
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);

        Assert.assertNull(propertyBuilder.buildWriter(prov, propDef, declaredType, null, typeSer, contentTypeSer, am, false));
    }

    @Test
    public void testBuildWriter_DefaultValues() throws Exception {
        SerializerProvider prov = Mockito.mock(SerializerProvider.class);
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        JavaType declaredType = Mockito.mock(JavaType.class);
        JsonSerializer<?> ser = Mockito.mock(JsonSerializer.class);
        TypeSerializer typeSer = Mockito.mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = Mockito.mock(TypeSerializer.class);
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);

        when(beanDesc.getClassAnnotations()).thenReturn(new Annotations());
        when(beanDesc.getType()).thenReturn(Object.class);
        when(beanDesc.findPropertyInclusion(any())).thenReturn(JsonInclude.Value.builder().build());
        when(annotationIntrospector.findAnnotationValues(any())).thenReturn(new JsonSerialize.TypedAnnotation[0]);
        when(annotationIntrospector.findNullSerializer(am)).thenReturn(null);

        Object defaultBean = new Object() {
        };

        when(beanDesc.instantiateBean(anyBoolean())).thenReturn(defaultBean);

        JavaType actualType = Mockito.mock(JavaType.class);

        JsonInclude.Value inclusion = JsonInclude.Value.builder()
                .withInclude(JsonInclude.Include.NON_NULL)
                .build();

        when(propDef.findInclusion()).thenReturn(inclusion);

        Object result = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof BeanPropertyWriter);

        BeanPropertyWriter beanPropertyWriter = (BeanPropertyWriter) result;
        Assert.assertFalse(beanPropertyWriter.suppressNulls);
        Assert.assertNull(beanPropertyWriter.getValueToSuppress());
    }

    @Test
    public void testBuildWriter_NonDefaultValues() throws Exception {
        SerializerProvider prov = Mockito.mock(SerializerProvider.class);
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        JavaType declaredType = Mockito.mock(JavaType.class);
        JsonSerializer<?> ser = Mockito.mock(JsonSerializer.class);
        TypeSerializer typeSer = Mockito.mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = Mockito.mock(TypeSerializer.class);
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);

        when(beanDesc.getClassAnnotations()).thenReturn(new Annotations());
        when(beanDesc.getType()).thenReturn(Object.class);
        when(beanDesc.findPropertyInclusion(any())).thenReturn(JsonInclude.Value.builder()
                .withInclude(JsonInclude.Include.NON_DEFAULT)
                .build());
        when(annotationIntrospector.findAnnotationValues(any())).thenReturn(new JsonSerialize.TypedAnnotation[]{
                new JsonSerialize(){{
                    setUseEmptyTextValue(false);
                }}
        });
        when(annotationIntrospector.findNullSerializer(am)).thenReturn(null);

        Object defaultBean = new Object(){};

        when(beanDesc.instantiateBean(anyBoolean())).thenReturn(defaultBean);

        JavaType actualType = Mockito.mock(JavaType.class);
        when(actualType.getContentType()).thenReturn(Arrays.asList(int.class).getType());

        Object result = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof BeanPropertyWriter);

        BeanPropertyWriter beanPropertyWriter = (BeanPropertyWriter) result;
        Assert.assertTrue(beanPropertyWriter.suppressNulls);
        Assert.assertNull(beanPropertyWriter.getValueToSuppress());
    }

    @Test
    public void testFindSerializationType() throws Exception {
        Annotated a = Mockito.mock(Annotated.class);
        JavaType declaredType = Mockito.mock(JavaType.class);

        when(annotationIntrospector.refineSerializationType(any(SerializationConfig.class), a, declaredType)).thenReturn(declaredType);

        JavaType result = propertyBuilder.findSerializationType(a, false, declaredType);

        Assert.assertEquals(declaredType, result);

        when(annotationIntrospector.refineSerializationType(any(SerializationConfig.class), a, declaredType)).thenReturn(
                ClassUtil.typeFromClass(String[].class));

        result = propertyBuilder.findSerializationType(a, false, declaredType);

        Assert.assertEquals(ClassUtil.typeFromClass(String[].class), result);

        when(annotationIntrospector.refineSerializationType(any(SerializationConfig.class), a, declaredType)).thenThrow(new Exception());

        try {
            propertyBuilder.findSerializationType(a, false, declaredType);
            Assert.fail();
        } catch (JsonMappingException e) {
            // passed
        }
    }

    @Test
    public void testFindSerializationType_JsonSerializeAnnotation() throws Exception {
        Annotated a = Mockito.mock(Annotated.class);
        JavaType declaredType = Mockito.mock(JavaType.class);

        JsonSerialize jsonSerialize = new JsonSerialize(){};
        jsonSerialize.setContentAsType(int[].class);

        when(annotationIntrospector.findAnnotationValues(a)).thenReturn(new JsonSerialize.TypedAnnotation[]{jsonSerialize});

        JavaType result = propertyBuilder.findSerializationType(a, false, declaredType);

        Assert.assertEquals(int[].class, result.getRawClass());
    }

    @Test
    public void testFindSerializationType_JsonTypeNameAnnotation() throws Exception {
        Annotated a = Mockito.mock(Annotated.class);
        JavaType declaredType = Mockito.mock(JavaType.class);

        JsonTypeName jsonTypeName = new JsonTypeName(){};
        jsonTypeName.value("int");

        when(annotationIntrospector.findAnnotationValues(a)).thenReturn(new JsonTypeName.TypedAnnotation[]{jsonTypeName});

        JavaType result = propertyBuilder.findSerializationType(a, false, declaredType);

        Assert.assertEquals(int.class, result.getRawClass());
    }

    @Test
    public void testFindSerializationType_JsonSerializeAndJsonTypeNameAnnotations() throws Exception {
        Annotated a = Mockito.mock(Annotated.class);
        JavaType declaredType = Mockito.mock(JavaType.class);

        JsonSerialize jsonSerialize = new JsonSerialize(){};
        jsonSerialize.setContentAsType(int[].class);

        JsonTypeName jsonTypeName = new JsonTypeName(){};
        jsonTypeName.value("int");

        when(annotationIntrospector.findAnnotationValues(a)).thenReturn(new JsonSerialize.TypedAnnotation[]{jsonSerialize}, new JsonTypeName.TypedAnnotation[]{jsonTypeName});

        JavaType result = propertyBuilder.findSerializationType(a, false, declaredType);

        Assert.assertEquals(int[].class, result.getRawClass());
    }

    @Test
    public void testGetPropertyDefaultValue() throws Exception {
        String propName = "name";
        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        JavaType type = Mockito.mock(JavaType.class);

        when(annotationIntrospector.findAnnotationValues(member)).thenReturn(new JsonSerialize.TypedAnnotation[0]);

        Object defaultBean = new Object(){
            String name = "default";
        };

        when(beanDesc.instantiateBean(anyBoolean())).thenReturn(defaultBean);

        when(member.getValue(defaultBean)).thenReturn("value");

        Object result = propertyBuilder.getPropertyDefaultValue(propName, member, type);

        Assert.assertEquals("value", result);
    }

    @Test
    public void testGetDefaultValue() throws Exception {
        JavaType type = Mockito.mock(JavaType.class);

        Class<?> cls = String.class;

        when(type.getRawClass()).thenReturn(cls);

        Object result = propertyBuilder.getDefaultValue(type);

        if (cls.isPrimitive()) {
            Assert.assertEquals(Integer.TYPE, result.getClass());
            Assert.assertEquals(0, ((Number) result).intValue());
        } else if (cls.isAssignableFrom(String.class)) {
            Assert.assertEquals("", result);
        } else {
            Assert.assertNull(result);
        }
    }
}