package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Test;

public class PropertyBuilderTest {

    @Test
    public void testFindSerializationType() {
        BeanDescription beanDesc = new BeanDescription();
        SerializationConfig config = new SerializationConfig();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        AnnotationIntrospector annotationIntrospector = Mockito.mock(AnnotationIntrospector.class);
        when(annotationIntrospector.refineSerializationType(config, am, null)).thenReturn(null);

        JavaType declaredType = TypeFactory.unknownType();

        // Test when no annotations and static typing is disabled
        assertNull(propertyBuilder.findSerializationType(am, false, declaredType));

        // Test when annotation is present
        JavaType secondaryType = TypeFactory.type(int[].class, Integer.TYPE);
        when(annotationIntrospector.refineSerializationType(config, am, declaredType)).thenReturn(secondaryType);
        assertEquals(secondaryType, propertyBuilder.findSerializationType(am, false, declaredType));

        // Test when annotation is present and static typing is enabled
        useStaticTyping(propertyBuilder);
        assertEquals(secondaryType.withStaticTyping(), propertyBuilder.findSerializationType(am, true, declaredType));

        // Test when annotation is not a super type
        Class<?> serClass = String.class;
        when(annotationIntrospector.refineSerializationType(config, am, declaredType)).thenReturn(serClass);
        assertFalse(serClass.isAssignableFrom(declaredType.getRawClass()));
        assertNull(propertyBuilder.findSerializationType(am, true, declaredType));
    }

    @Test
    public void testgetDefaultBean() {
        BeanDescription beanDesc = new BeanDescription();
        SerializationConfig config = new SerializationConfig();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        assertNull(propertyBuilder.getDefaultBean());

        Object defaultBean = new Object();
        propertyBuilder._defaultBean = defaultBean;

        assertEquals(defaultBean, propertyBuilder.getDefaultBean());

        propertyBuilder._defaultBean = null;
        assertNull(propertyBuilder.getDefaultBean());
    }

    @Test
    public void testgetPropertyDefaultValue() throws Exception {
        BeanDescription beanDesc = new BeanDescription();
        SerializationConfig config = new SerializationConfig();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        Object defaultBean = new Object();
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        when(am.getValue(defaultBean)).thenReturn("defaultValue");
        when(am.getName()).thenReturn("propertyName");

        String propName = "propertyName";
        JavaType type = TypeFactory.unknownType();

        assertEquals("defaultValue", propertyBuilder.getPropertyDefaultValue(propName, am, type));

        propertyBuilder._defaultBean = null;
        assertNull(propertyBuilder.getPropertyDefaultValue(propName, am, type));
    }

    @Test
    public void testgetDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);

        assertNull(propertyBuilder.getDefaultValue(TypeFactory.unknownType()));

        // Test for primitive types and wrapper types
        assertEquals(0, propertyBuilder.getDefaultValue(TypeFactory.booleanType()));
        assertEquals(0, propertyBuilder.getDefaultValue(TypeFactory.intType()));
        assertEquals(0L, propertyBuilder.getDefaultValue(TypeFactory.longType()));
        assertEquals(0.0f, propertyBuilder.getDefaultValue(Float.TYPE));
        assertEquals(0.0d, propertyBuilder.getDefaultValue(Double.TYPE));
        assertEquals(' ', propertyBuilder.getDefaultValue(Character.TYPE));
        assertEquals("\u0000", propertyBuilder.getDefaultValue(String.class));

        // Test for arrays, maps, and reference types
        assertSame(JsonInclude.Include.NON_EMPTY, propertyBuilder.getDefaultValue(TypeFactory.arrayType(String.class)));
        assertSame(JsonInclude.Include.NON_EMPTY, propertyBuilder.getDefaultValue(Map.class));
        assertSame(JsonInclude.Include.NON_EMPTY, propertyBuilder.getDefaultValue(String.class));
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        BeanDescription beanDesc = new BeanDescription();
        SerializationConfig config = new SerializationConfig();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        JavaType declaredType = TypeFactory.unknownType();
        JsonSerializer<?> ser = Mockito.mock(JsonSerializer.class);
        TypeSerializer typeSer = Mockito.mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = Mockito.mock(TypeSerializer.class);
        Annotations classAnnotations = Mockito.mock(Annotations.class);

        Object valueToSuppress = "valueToSuppress";
        boolean suppressNulls = false;
        JsonInclude.Value _defaultInclusion = Mockito.mock(JsonInclude.Value.class);
        JsonInclude.Include inclusion = JsonInclude.Include.ALWAYS;

        when(am.getName()).thenReturn("propertyName");
        when(beanDesc.getClassAnnotations()).thenReturn(classAnnotations);
        when(_beanDesc.getType()).thenReturn(String.class);
        when(_config.getDefaultPropertyInclusion(beanDesc.getBeanClass())).thenReturn(_defaultInclusion);
        when(_defaultInclusion.getValueInclusion()).thenReturn(inclusion);

        when(_annotationIntrospector.findNullSerializer(am)).thenReturn(null);
        when(_annotationIntrospector.findUnwrappingNameTransformer(am)).thenReturn(null);

        BeanPropertyWriter bpw = propertyBuilder.buildWriter(Mockito.mock(SerializerProvider.class), propDef, declaredType, ser, typeSer, contentTypeSer, am, false);

        assertNotNull(bpw);

        // Test when inclusion is ALWAYS
        assertEquals(declaredType, bpw.getType());
        assertNull(bpw.getNullSerializer());
        assertNull(bpw.getUnwrappingNameTransformer());

        // Test when inclusion is NON_EMPTY and valueToSuppress is not null
        valueToSuppress = "emptyArray";
        suppressNulls = false;
        when(_defaultInclusion.getValueInclusion()).thenReturn(JsonInclude.Include.NON_EMPTY);
        when(propDef.getName()).thenReturn("propertyName");
        when(declaredType.isContainerType()).thenReturn(true);
        when(_config.isEnabled(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS)).thenReturn(false);

        bpw = propertyBuilder.buildWriter(Mockito.mock(SerializerProvider.class), propDef, declaredType, ser, typeSer, contentTypeSer, am, false);

        assertNotNull(bpw);
        assertEquals(BeanPropertyWriter.MARKER_FOR_EMPTY, bpw.getValueToSuppress());
        assertNull(bpw.getNullSerializer());
        assertNull(bpw.getUnwrappingNameTransformer());

        // Test when inclusion is NON_EMPTY and valueToSuppress is null
        valueToSuppress = null;
        suppressNulls = false;
        when(_defaultInclusion.getValueInclusion()).thenReturn(JsonInclude.Include.NON_EMPTY);
        when(propDef.getName()).thenReturn("propertyName");
        when(declaredType.isContainerType()).thenReturn(true);
        when(_config.isEnabled(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS)).thenReturn(false);

        bpw = propertyBuilder.buildWriter(Mockito.mock(SerializerProvider.class), propDef, declaredType, ser, typeSer, contentTypeSer, am, false);

        assertNotNull(bpw);
        assertNull(bpw.getValueToSuppress());
        assertNull(bpw.getNullSerializer());
        assertNull(bpw.getUnwrappingNameTransformer());

        // Test when inclusion is NON_NULL
        inclusion = JsonInclude.Include.NON_NULL;
        when(_defaultInclusion.getValueInclusion()).thenReturn(inclusion);

        bpw = propertyBuilder.buildWriter(Mockito.mock(SerializerProvider.class), propDef, declaredType, ser, typeSer, contentTypeSer, am, false);

        assertNotNull(bpw);
        assertNull(bpw.getValueToSuppress());
        assertNull(bpw.getNullSerializer());
        assertNull(bpw.getUnwrappingNameTransformer());

        // Test when inclusion is NON_DEFAULT and valueToSuppress is not null
        valueToSuppress = "defaultValue";
        suppressNulls = false;
        when(_defaultInclusion.getValueInclusion()).thenReturn(JsonInclude.Include.NON_DEFAULT);
        when(_beanDesc.getClassInfo().getAnnotated()).thenReturn(new MyClass());
        when(propDef.getName()).thenReturn("propertyName");
        when(am.getValue(defaultBean)).thenReturn("defaultValue");

        bpw = propertyBuilder.buildWriter(Mockito.mock(SerializerProvider.class), propDef, declaredType, ser, typeSer, contentTypeSer, am, false);

        assertNotNull(bpw);
        assertEquals("defaultValue", bpw.getValueToSuppress());
        assertNull(bpw.getNullSerializer());
        assertNull(bpw.getUnwrappingNameTransformer());

        // Test when inclusion is NON_DEFAULT and valueToSuppress is null
        valueToSuppress = null;
        suppressNulls = false;
        when(_defaultInclusion.getValueInclusion()).thenReturn(JsonInclude.Include.NON_DEFAULT);
        when(propDef.getName()).thenReturn("propertyName");
        when(am.getValue(defaultBean)).thenReturn(null);

        bpw = propertyBuilder.buildWriter(Mockito.mock(SerializerProvider.class), propDef, declaredType, ser, typeSer, contentTypeSer, am, false);

        assertNotNull(bpw);
        assertNull(bpw.getValueToSuppress());
        assertNull(bpw.getNullSerializer());
        assertNull(bpw.getUnwrappingNameTransformer());

        // Test when inclusion is NON_ABSENT and valueToSuppress is null
        inclusion = JsonInclude.Include.NON_ABSENT;
        when(_defaultInclusion.getValueInclusion()).thenReturn(inclusion);
        when(declaredType.isReferenceType()).thenReturn(true);

        bpw = propertyBuilder.buildWriter(Mockito.mock(SerializerProvider.class), propDef, declaredType, ser, typeSer, contentTypeSer, am, false);

        assertNotNull(bpw);
        assertNull(bpw.getValueToSuppress());
        assertNull(bpw.getNullSerializer());
        assertNull(bpw.getUnwrappingNameTransformer());

        // Test when inclusion is NON_ABSENT and valueToSuppress is not null
        valueToSuppress = BeanPropertyWriter.MARKER_FOR_EMPTY;
        suppressNulls = false;

        bpw = propertyBuilder.buildWriter(Mockito.mock(SerializerProvider.class), propDef, declaredType, ser, typeSer, contentTypeSer, am, false);

        assertNotNull(bpw);
        assertEquals(BeanPropertyWriter.MARKER_FOR_EMPTY, bpw.getValueToSuppress());
        assertNull(bpw.getNullSerializer());
        assertNull(bpw.getUnwrappingNameTransformer());

        // Test when inclusion is NON_EMPTY and suppressNulls is true
        inclusion = JsonInclude.Include.NON_EMPTY;
        suppressNulls = true;
        when(_defaultInclusion.getValueInclusion()).thenReturn(inclusion);
        when(_config.isEnabled(SerializationFeature.FAIL_ON_EMPTY_BEANS)).thenReturn(false);

        bpw = propertyBuilder.buildWriter(Mockito.mock(SerializerProvider.class), propDef, declaredType, ser, typeSer, contentTypeSer, am, false);

        assertNotNull(bpw);
        assertEquals(BeanPropertyWriter.MARKER_FOR_EMPTY, bpw.getValueToSuppress());
        assertNull(bpw.getNullSerializer());
        assertNull(bpw.getUnwrappingNameTransformer());

        // Test when inclusion is NON_EMPTY and suppressNulls is false and valueToSuppress is null
        suppressNulls = false;
        bpw = propertyBuilder.buildWriter(Mockito.mock(SerializerProvider.class), propDef, declaredType, ser, typeSer, contentTypeSer, am, false);

        assertNotNull(bpw);
        assertNull(bpw.getValueToSuppress());
        assertNull(bpw.getNullSerializer());
        assertNull(bpw.getUnwrappingNameTransformer());

        // Test when inclusion is NON_EMPTY and suppressNulls is false and valueToSuppress is not null
        valueToSuppress = "emptyValue";
        suppressNulls = false;
        when(propDef.getName()).thenReturn("propertyName");
        when(declaredType.isContainerType()).thenReturn(true);
        when(_config.isEnabled(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS)).thenReturn(false);

        bpw = propertyBuilder.buildWriter(Mockito.mock(SerializerProvider.class), propDef, declaredType, ser, typeSer, contentTypeSer, am, false);

        assertNotNull(bpw);
        assertEquals("emptyValue", bpw.getValueToSuppress());
        assertNull(bpw.getNullSerializer());
        assertNull(bpw.getUnwrappingNameTransformer());

        // Test when inclusion is NON_NULL and suppressNulls is true
        inclusion = JsonInclude.Include.NON_NULL;
        suppressNulls = true;
        when(_defaultInclusion.getValueInclusion()).thenReturn(inclusion);

        bpw = propertyBuilder.buildWriter(Mockito.mock(SerializerProvider.class), propDef, declaredType, ser, typeSer, contentTypeSer, am, false);

        assertNotNull(bpw);
        assertNull(bpw.getValueToSuppress());
        assertNull(bpw.getNullSerializer());
        assertNull(bpw.getUnwrappingNameTransformer());

        // Test when inclusion is NON_NULL and suppressNulls is false and valueToSuppress is null
        suppressNulls = false;
        bpw = propertyBuilder.buildWriter(Mockito.mock(SerializerProvider.class), propDef, declaredType, ser, typeSer, contentTypeSer, am, false);

        assertNotNull(bpw);
        assertNull(bpw.getValueToSuppress());
        assertNull(bpw.getNullSerializer());
        assertNull(bpw.getUnwrappingNameTransformer());

        // Test when inclusion is NON_NULL and suppressNulls is false and valueToSuppress is not null
        valueToSuppress = "nonNullValue";
        suppressNulls = false;
        when(propDef.getName()).thenReturn("propertyName");
        when(declaredType.isReferenceType()).thenReturn(true);

        bpw = propertyBuilder.buildWriter(Mockito.mock(SerializerProvider.class), propDef, declaredType, ser, typeSer, contentTypeSer, am, false);

        assertNotNull(bpw);
        assertEquals("nonNullValue", bpw.getValueToSuppress());
        assertNull(bpw.getNullSerializer());
        assertNull(bpw.getUnwrappingNameTransformer());

        // Test when inclusion is ALWAYS and suppressNulls is true
        inclusion = JsonInclude.Include.ALWAYS;
        suppressNulls = true;
        when(_defaultInclusion.getValueInclusion()).thenReturn(inclusion);

        bpw = propertyBuilder.buildWriter(Mockito.mock(SerializerProvider.class), propDef, declaredType, ser, typeSer, contentTypeSer, am, false);

        assertNotNull(bpw);
        assertNull(bpw.getValueToSuppress());
        assertNull(bpw.getNullSerializer());
        assertNull(bpw.getUnwrappingNameTransformer());
    }

    private void useStaticTyping(PropertyBuilder propertyBuilder) {
        propertyBuilder._defaultInclusion = JsonInclude.Value.standard();
        propertyBuilder._config.setAnnotationIntrospector(propertyBuilder._annotationIntrospector);
    }
}