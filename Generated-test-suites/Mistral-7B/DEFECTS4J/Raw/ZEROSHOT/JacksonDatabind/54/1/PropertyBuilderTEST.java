package com.fasterxml.jackson.databind.ser.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.BeanPropertyWriter;
import com.fasterxml.jackson.databind.util.BeanPropertyWriter.MarkerForEmpty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.AnnotatedMember;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.mockito.Mockito.*;

public class PropertyBuilderTest {

    private SerializationConfig _config;
    private BeanDescription _beanDesc;
    private AnnotationIntrospector _annotationIntrospector;

    @BeforeEach
    public void setup() {
        _config = mock(SerializationConfig.class);
        _beanDesc = mock(BeanDescription.class);
        _annotationIntrospector = mock(AnnotationIntrospector.class);
    }

    @Test
    public void testBuildWriter() throws Exception {
        Class<?> testClass = TestClass.class;
        Object testInstance = new TestClass();

        // Set up mocks to return expected values and behaviors
        when(_config.getDefaultPropertyInclusion(testClass)).thenReturn(JsonInclude.Include.NON_NULL);
        when(_beanDesc.findPropertyInclusion(_config, TestProperty.class)).thenReturn(JsonInclude.Include.ALWAYS);
        when(_beanDesc.getClassAnnotations()).thenReturn(mock(Annotations.class));
        when(_annotationIntrospector.findNullSerializer(any()))
                .thenReturn(mock(JsonSerializer.class));
        when(_annotationIntrospector.findUnwrappingNameTransformer(any()))
                .thenReturn(mock(NameTransformer.class));

        PropertyBuilder propertyBuilder = new PropertyBuilder(_config, _beanDesc);

        SerializerProvider prov = mock(SerializerProvider.class);
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType declaredType = mock(JavaType.class);
        JsonSerializer<?> ser = mock(JsonSerializer.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = mock(TypeSerializer.class);
        AnnotatedMember am = mock(AnnotatedMember.class);

        Object valueToSuppress = null;
        boolean suppressNulls = false;

        // Test case for JsonInclude.Include.NON_NULL
        when(propDef.getName()).thenReturn("property1");
        when(declaredType.getRawClass()).thenReturn(String.class);
        BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);
        Assertions.assertNull(valueToSuppress);
        Assertions.assertFalse(suppressNulls);

        // Test case for JsonInclude.Include.NON_EMPTY
        when(propDef.getName()).thenReturn("property2");
        when(declaredType.getRawClass()).thenReturn(Arrays.asList("test1", "test2").getClass());
        valueToSuppress = MarkerForEmpty.EMPTY_ARRAY;
        suppressNulls = true;
        bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);
        Assertions.assertEquals(MarkerForEmpty.EMPTY_ARRAY, valueToSuppress);
        Assertions.assertTrue(suppressNulls);

        // Test case for JsonInclude.Include.NON_DEFAULT
        when(propDef.getName()).thenReturn("property3");
        when(declaredType.getRawClass()).thenReturn(TestClass.class);
        Object defaultBean = new TestClass();
        Object propertyDefaultValue = "defaultValue";
        when(am.getValue(defaultBean)).thenReturn(propertyDefaultValue);
        valueToSuppress = propertyDefaultValue;
        suppressNulls = false;
        bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);
        Assertions.assertEquals(propertyDefaultValue, valueToSuppress);
        Assertions.assertFalse(suppressNulls);
    }
}

class TestClass {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String property1;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> property2;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String property3 = "defaultValue";
}

class TestProperty {
    // Empty annotation used for testing purpose
}