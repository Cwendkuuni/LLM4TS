package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.BeanPropertyWriter;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.BeanProperty;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter.MarkerForEmpty;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PropertyBuilderTest {

    @Test
    public void testDefaultValue() throws JsonMappingException, JsonProcessingException {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(new Object() {});
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        Object defaultBean = propertyBuilder.getDefaultBean();
        assertNull(defaultBean);

        JavaType type = TypeFactory.uncheckedType(String[].class);
        Object defaultValue = propertyBuilder.getDefaultValue(type);
        assertEquals(new Object[0], defaultValue);

        Object defaultBeanWithValue = new Object() {
            String value = "default";
        };
        propertyBuilder._defaultBean = defaultBeanWithValue;
        Object defaultValueWithInstance = propertyBuilder.getPropertyDefaultValue("property", null, type);
        assertEquals("default", defaultValueWithInstance);
    }

    @Test
    public void testFindSerializationType() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        AnnotationIntrospector annotationIntrospector = config.getDefaultAnnotationIntrospector();

        JavaType declaredType = TypeFactory.uncheckedType(Object.class);
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        JavaType serializationType = propertyBuilder._findSerializationType(am, false, declaredType);
        assertNull(serializationType);

        JsonSerialize.Typing typing = Mockito.mock(JsonSerialize.Typing.class);
        when(am.getAnnotations()).thenReturn(new JsonInclude(JsonInclude.Include.NON_DEFAULT));
        when(annotationIntrospector.findSerializationType(am, declaredType)).thenReturn(typing);
        when(typing.getValue()).thenReturn(JsonSerialize.Typing.STATIC);

        serializationType = propertyBuilder._findSerializationType(am, true, declaredType);
        assertEquals(declaredType, serializationType);

        when(typing.getValue()).thenReturn(JsonSerialize.Typing.DYNAMIC);
        serializationType = propertyBuilder._findSerializationType(am, true, declaredType);
        assertNull(serializationType);

        JavaType secondaryType = TypeFactory.uncheckedType(String.class);
        when(annotationIntrospector.refineSerializationType(config, am, declaredType)).thenReturn(secondaryType);
        serializationType = propertyBuilder._findSerializationType(am, false, declaredType);
        assertEquals(secondaryType, serializationType);

        when(annotationIntrospector.refineSerializationType(config, am, declaredType)).thenReturn(TypeFactory.uncheckedType(Integer.class));
        when(typing.getValue()).thenReturn(JsonSerialize.Typing.STATIC);
        serializationType = propertyBuilder._findSerializationType(am, true, declaredType);
        assertEquals(TypeFactory.uncheckedType(Integer.class).withStaticTyping(), serializationType);
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(new Object() {});
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        Object valueToSuppress = null;
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        JavaType declaredType = TypeFactory.uncheckedType(String.class);
        JavaType serializationType = TypeFactory.uncheckedType(String.class);
        JsonSerializer<?> ser = Mockito.mock(JsonSerializer.class);
        TypeSerializer typeSer = Mockito.mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = Mockito.mock(TypeSerializer.class);
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        when(am.getGenericType()).thenReturn(TypeFactory.uncheckedType(String[].class));

        BeanPropertyWriter bpw = propertyBuilder.buildWriter(Mockito.mock(SerializerProvider.class), propDef, declaredType, ser, typeSer, contentTypeSer, am, false);
        assertNull(bpw.getNullSerializer());
        assertNull(bpw.getUnwrappingNameTransformer());

        ValueInclusion inclusion = JsonInclude.Value.includeAnnotations(propDef.getAnnotations());
        when(config.getDefaultInclude(declaredType.getRawClass(), declaredType.getRawClass(), inclusion)).thenReturn(JsonInclude.Value.builder().build());
        when(propDef.getAnnotation(JsonInclude.class)).thenReturn(new JsonInclude(JsonInclude.Include.ALWAYS));

        bpw = propertyBuilder.buildWriter(Mockito.mock(SerializerProvider.class), propDef, declaredType, ser, typeSer, contentTypeSer, am, false);
        assertNull(bpw.getValueToSuppress());
        assertFalse(bpw.suppressNulls());
        assertEquals(declaredType, bpw.getSerializationType());
        assertNull(bpw.getUnwrappingNameTransformer());
        assertNull(bpw.getNullSerializer());

        when(propDef.getAnnotation(JsonInclude.class)).thenReturn(new JsonInclude(JsonInclude.Include.NON_DEFAULT));
        when(propDef.getAnnotation(JsonSerialize.class)).thenReturn(new JsonSerializerAnnotatedMethod());

        AnnotatedMember accessor = Mockito.mock(AnnotatedMember.class);
        when(accessor.getRawType()).thenReturn(String[].class);

        bpw = propertyBuilder.buildWriter(Mockito.mock(SerializerProvider.class), propDef, declaredType, ser, typeSer, contentTypeSer, accessor, false);
        assertNull(bpw.getValueToSuppress());
        assertFalse(bpw.suppressNulls());
        assertEquals(declaredType, bpw.getSerializationType());
        assertNull(bpw.getUnwrappingNameTransformer());
        assertNull(bpw.getNullSerializer());

        when(propDef.getAnnotation(JsonInclude.class)).thenReturn(new JsonInclude(JsonInclude.Include.NON_DEFAULT));
        when(propDef.findInclusion()).thenReturn(new JsonInclude(JsonInclude.Include.NON_ABSENT));

        bpw = propertyBuilder.buildWriter(Mockito.mock(SerializerProvider.class), propDef, declaredType, ser, typeSer, contentTypeSer, accessor, false);
        assertNull(bpw.getValueToSuppress());
        assertTrue(bpw.suppressNulls());
        assertEquals(declaredType, bpw.getSerializationType());
        assertNull(bpw.getUnwrappingNameTransformer());
        assertNull(bpw.getNullSerializer());

        when(propDef.getAnnotation(JsonInclude.class)).thenReturn(new JsonInclude(JsonInclude.Include.NON_EMPTY));
        when(propDef.getAnnotation(JsonSerialize.class)).thenReturn(new JsonSerializerAnnotatedMethod());

        bpw = propertyBuilder.buildWriter(Mockito.mock(SerializerProvider.class), propDef, declaredType, ser, typeSer, contentTypeSer, accessor, false);
        assertNull(bpw.getValueToSuppress());
        assertTrue(bpw.suppressNulls());
        assertEquals(declaredType, bpw.getSerializationType());
        assertNull(bpw.getUnwrappingNameTransformer());
        assertNull(bpw.getNullSerializer());

        when(propDef.getAnnotation(JsonInclude.class)).thenReturn(new JsonInclude(JsonInclude.Include.CUSTOM));
        ValueFilter valueFilter = Mockito.mock(ValueFilter.class);
        when(propDef.findValueFilters()).thenReturn(new ValueFilter[] { valueFilter });

        bpw = propertyBuilder.buildWriter(Mockito.mock(SerializerProvider.class), propDef, declaredType, ser, typeSer, contentTypeSer, accessor, false);
        assertNull(bpw.getValueToSuppress());
        assertTrue(bpw.suppressNulls());
        assertEquals(declaredType, bpw.getSerializationType());
        assertEquals(valueFilter, bpw.getNullSerializer());
        assertNull(bpw.getUnwrappingNameTransformer());
    }

    @Test
    public void testUseRealPropertyDefaults() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(new Object() {
            String value = "default";
        });
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        Object defaultBean = propertyBuilder.getDefaultBean();
        assertNotNull(defaultBean);
        assertEquals("default", defaultBean.getValue());

        PropertyBuilder propertyBuilderNoDefault = new PropertyBuilder(config, new BeanDescription(new Object()));
        defaultBean = propertyBuilderNoDefault.getDefaultBean();
        assertNull(defaultBean);
    }
}

class JsonSerializerAnnotatedMethod {}