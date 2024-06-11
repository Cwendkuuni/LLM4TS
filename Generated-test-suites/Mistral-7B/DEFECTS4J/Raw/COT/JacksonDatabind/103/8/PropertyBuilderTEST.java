package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.BeanPropertyWriter;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class PropertyBuilderTest {

    private SerializationConfig config;
    private BeanDescription beanDesc;
    private AnnotationIntrospector annotationIntrospector;

    @Before
    public void setup() {
        config = Mockito.mock(SerializationConfig.class);
        beanDesc = Mockito.mock(BeanDescription.class);
        annotationIntrospector = Mockito.mock(AnnotationIntrospector.class);

        // Initialize mocks with stubs for methods that won't be called in tests
        Mockito.when(beanDesc.getClassAnnotations()).thenReturn(new Annotations());
        Mockito.when(beanDesc.findPropertyInclusion(JsonInclude.Value.empty())).thenReturn(JsonInclude.Value.empty());
        Mockito.when(config.getDefaultPropertyInclusion(beanDesc.getBeanClass(), JsonInclude.Value.empty())).thenReturn(JsonInclude.Value.empty());
        Mockito.when(config.getDefaultPropertyInclusion()).thenReturn(JsonInclude.Value.empty());
    }

    @Test
    public void testPropertyBuilderConstructor() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        assertNotNull(propertyBuilder);
    }

    @Test
    public void testBuildWriter_NoSerializationType() {
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        JavaType declaredType = Mockito.mock(JavaType.class);
        JsonSerializer<?> ser = Mockito.mock(JsonSerializer.class);
        TypeSerializer typeSer = Mockito.mock(TypeSerializer.class);
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        boolean defaultUseStaticTyping = true;

        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        BeanPropertyWriter bpw = propertyBuilder.buildWriter(Mockito.mock(SerializerProvider.class), propDef, declaredType, ser, typeSer, null, am, defaultUseStaticTyping);
        assertNotNull(bpw);
    }

    @Test
    public void testBuildWriter_WithSerializationType() {
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        JavaType declaredType = Mockito.mock(JavaType.class);
        JavaType serializationType = Mockito.mock(JavaType.class);
        JsonSerializer<?> ser = Mockito.mock(JsonSerializer.class);
        TypeSerializer typeSer = Mockito.mock(TypeSerializer.class);
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        boolean defaultUseStaticTyping = true;

        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        BeanPropertyWriter bpw = propertyBuilder.buildWriter(Mockito.mock(SerializerProvider.class), propDef, declaredType, ser, typeSer, serializationType, am, defaultUseStaticTyping);
        assertNotNull(bpw);
    }

    @Test
    public void testBuildWriter_WithContentTypeSerializer() {
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        JavaType declaredType = Mockito.mock(JavaType.class);
        JsonSerializer<?> ser = Mockito.mock(JsonSerializer.class);
        TypeSerializer typeSer = Mockito.mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = Mockito.mock(TypeSerializer.class);
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        boolean defaultUseStaticTyping = true;

        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        BeanPropertyWriter bpw = propertyBuilder.buildWriter(Mockito.mock(SerializerProvider.class), propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        assertNotNull(bpw);
    }

    @Test
    public void testFindSerializationType_NoSerializationAnnotation() {
        Annotated a = Mockito.mock(Annotated.class);
        JavaType declaredType = Mockito.mock(JavaType.class);

        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        JavaType result = propertyBuilder.findSerializationType(a, false, declaredType);
        assertNull(result);
    }

    @Test
    public void testFindSerializationType_WithSerializationAnnotation() {
        Annotated a = Mockito.mock(Annotated.class);
        JavaType declaredType = Mockito.mock(JavaType.class);
        JsonSerialize jsonSerialize = Mockito.mock(JsonSerialize.class);

        Mockito.when(a.getAnnotations()).thenReturn(new Annotations() {{
            add(jsonSerialize);
        }});

        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        JavaType result = propertyBuilder.findSerializationType(a, false, declaredType);
        assertEquals(declaredType, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindSerializationType_InvalidSerializationType() {
        Annotated a = Mockito.mock(Annotated.class);
        JavaType declaredType = Mockito.mock(JavaType.class);

        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        propertyBuilder.findSerializationType(a, false, declaredType);
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testGetClassAnnotations() {
        Annotations classAnnotations = PropertyBuilder.getClassAnnotations();
        assertNotNull(classAnnotations);
    }

    // Add more test cases as needed for other methods
    // ...

    @Test(expected = IllegalArgumentException.class)
    public void testGetPropertyDefaultValue_NullPropertyName() {
        String propName = null;
        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        JavaType type = Mockito.mock(JavaType.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        try {
            propertyBuilder.getPropertyDefaultValue(propName, member, type);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Property name cannot be null", e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPropertyDefaultValue_NullDefaultBean() {
        String propName = "propName";
        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        JavaType type = Mockito.mock(JavaType.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        PropertyBuilder builder = Mockito.spy(propertyBuilder);
        Mockito.doReturn(null).when(builder).getDefaultBean();

        try {
            builder.getPropertyDefaultValue(propName, member, type);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to get property 'propName' of default bean instance", e.getMessage());
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testGetPropertyDefaultValue_UnsupportedAnnotation() {
        String propName = "propName";
        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        JavaType type = Mockito.mock(JavaType.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        Mockito.when(member.getValue(Mockito.anyObject())).thenReturn("defaultValue");
        Mockito.doReturn(new JsonInclude(JsonInclude.Include.NON_DEFAULT)).when(propertyBuilder)._useRealPropertyDefaults;

        try {
            propertyBuilder.getPropertyDefaultValue(propName, member, type);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertEquals("Returned default value is not null, but include type is NON_DEFAULT, which requires null default", e.getMessage());
        }
    }

    @Test
    public void testGetPropertyDefaultValue_DefaultValueIsArray() {
        String propName = "propName";
        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        JavaType type = Mockito.mock(JavaType.class);
        Object defaultBean = new Object[]{"defaultValue1", "defaultValue2"};
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        Mockito.when(member.getValue(Mockito.anyObject())).thenCallRealMethod();
        Mockito.when(member.getValue(defaultBean)).thenReturn(new Object[]{"value1", "value2"});

        Object result = propertyBuilder.getPropertyDefaultValue(propName, member, type);
        assertArrayEquals(new Object[]{"defaultValue1", "defaultValue2"}, result);
    }
}