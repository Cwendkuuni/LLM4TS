package com.example.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Type;
import java.util.Arrays;

public class PropertyBuilderTest {

    private PropertyBuilder propertyBuilder;
    private SerializationConfig config;
    private BeanDescription beanDesc;
    private AnnotationIntrospector annotationIntrospector;

    @Before
    public void setup() {
        config = new SerializationConfig();
        beanDesc = new BeanDescription(new ClassInfo(MyTestClass.class, null));
        propertyBuilder = new PropertyBuilder(config, beanDesc);
        annotationIntrospector = new StandardAnnotationIntrospector();
    }

    @Test
    public void testConstructor() {
        assertNotNull(propertyBuilder);
    }

    @Test
    public void testGetClassAnnotations() {
        Annotations annotations = propertyBuilder.getClassAnnotations();
        assertNotNull(annotations);
        assertTrue(annotations instanceof Annotations);
    }

    @Test
    public void testBuildWriter_SimpleType() throws JsonMappingException {
        // Given
        SerializerProvider prov = new StandardSerializerProvider();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(new PropertyName("test"), new TypeIdentifier(String.class), true);
        JavaType declaredType = TypeFactory.uncheckedType(String.class);
        JsonSerializer<String> ser = new StringSerializer();
        TypeSerializer typeSer = null;
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        boolean defaultUseStaticTyping = true;

        // When
        BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, null, am, defaultUseStaticTyping);

        // Then
        assertNotNull(bpw);
    }

    @Test(expected = JsonMappingException.class)
    public void testBuildWriter_TypeSerializationException() throws JsonMappingException {
        // Given
        SerializerProvider prov = new StandardSerializerProvider();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(new PropertyName("test"), new TypeIdentifier(String[].class), true);
        JavaType declaredType = TypeFactory.uncheckedType(String[].class);
        JsonSerializer<String[]> ser = Mockito.mock(JsonSerializer.class);
        TypeSerializer typeSer = TypeSerializers.CollectionTypeSerializer.instance;
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        boolean defaultUseStaticTyping = true;

        // When & Then
        propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, null, am, defaultUseStaticTyping);
    }

    @Test
    public void testFindSerializationType_NoAnnotation() {
        Annotated a = Mockito.mock(Annotated.class);
        JavaType declaredType = TypeFactory.uncheckedType(MyTestClass.class);

        // When
        JavaType actual = propertyBuilder.findSerializationType(a, false, declaredType);

        // Then
        assertNull(actual);
    }

    @Test
    public void testFindSerializationType_Annotated() {
        Annotated a = Mockito.mock(Annotated.class);
        JavaType declaredType = TypeFactory.uncheckedType(MyTestClass.class);
        JsonInclude myAnnotation = new JsonInclude();
        myAnnotation.setInclude(JsonInclude.Include.ALWAYS);
        a.setValue(myAnnotation);

        // When
        JavaType actual = propertyBuilder.findSerializationType(a, false, declaredType);

        // Then
        assertEquals(declaredType, actual);
    }

    @Test
    public void testFindSerializationType_TypeOverride() {
        Annotated a = Mockito.mock(Annotated.class);
        JavaType declaredType = TypeFactory.uncheckedType(MyTestClass.class);
        JsonInclude myAnnotation = new JsonInclude();
        myAnnotation.setInclude(JsonInclude.Include.ALWAYS);
        myAnnotation.setSerializationIncludes(Arrays.asList(new PropertyAccessorAnnotation("test", JsonInclude.Include.NON_EMPTY)));
        a.setValue(myAnnotation);
        JavaType typeOverride = TypeFactory.uncheckedType(String.class);

        // When
        JavaType actual = propertyBuilder.findSerializationType(a, false, declaredType);

        // Then
        assertEquals(typeOverride, actual);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindSerializationType_IllegalAnnotation() {
        Annotated a = Mockito.mock(Annotated.class);
        JavaType declaredType = TypeFactory.uncheckedType(MyTestClass.class);
        JsonInclude myAnnotation = new JsonInclude();
        myAnnotation.setInclude(JsonInclude.Include.ALWAYS);
        myAnnotation.setSerializationIncludes(Arrays.asList(new PropertyAccessorAnnotation("test", JsonInclude.Include.NON_NULL)));
        a.setValue(myAnnotation);

        // When & Then
        propertyBuilder.findSerializationType(a, false, declaredType);
    }

    @Test
    public void testGetDefaultBean() {
        // Given
        Object defaultBean = propertyBuilder.getDefaultBean();

        // Then
        assertNull(defaultBean);
    }

    @Test
    public void testGetDefaultBean_DefaultInstance() {
        Object defaultBean = propertyBuilder.getDefaultBean();

        // Given
        propertyBuilder = new PropertyBuilder(config, beanDesc);
        MyTestClass defaultInstance = new MyTestClass();
        propertyBuilder._defaultBean = defaultInstance;

        // When
        Object result = propertyBuilder.getDefaultBean();

        // Then
        assertEquals(defaultInstance, result);
    }

    @Test
    public void testGetPropertyDefaultValue_SimpleType() {
        // Given
        String defaultValue = "default";
        MyTestClass defaultBean = new MyTestClass();
        MyTestClass.Property property = new MyTestClass.Property();
        property.setValue(defaultValue);
        ReflectionTestUtils.setFieldValue(defaultBean, "property", property);
        String propertyName = "property";
        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        JavaType type = TypeFactory.uncheckedType(String.class);

        // When
        Object result = propertyBuilder.getPropertyDefaultValue(propertyName, member, type);

        // Then
        assertEquals(defaultValue, result);
    }

    @Test
    public void testGetPropertyDefaultValue_NullDefault() {
        // Given
        MyTestClass defaultBean = new MyTestClass();
        String propertyName = "property";
        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        JavaType type = TypeFactory.uncheckedType(String.class);

        // When
        Object result = propertyBuilder.getPropertyDefaultValue(propertyName, member, type);

        // Then
        assertNull(result);
    }

    @Test
    public void testGetPropertyDefaultValue_EmptyArray() {
        // Given
        MyTestClass defaultBean = new MyTestClass();
        MyTestClass.Property property = new MyTestClass.Property();
        property.setValue(new String[0]);
        ReflectionTestUtils.setFieldValue(defaultBean, "property", property);
        String propertyName = "property";
        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        JavaType type = TypeFactory.uncheckedType(String[].class);

        // When
        Object result = propertyBuilder.getPropertyDefaultValue(propertyName, member, type);

        // Then
        assertEquals(BeanPropertyWriter.MARKER_FOR_EMPTY, result);
    }

    @Test
    public void testGetPropertyDefaultValue_EmptyValue() {
        // Given
        MyTestClass defaultBean = new MyTestClass();
        MyTestClass.Property property = new MyTestClass.Property();
        property.setValue("");
        ReflectionTestUtils.setFieldValue(defaultBean, "property", property);
        String propertyName = "property";
        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        JavaType type = TypeFactory.uncheckedType(String.class);

        // When
        Object result = propertyBuilder.getPropertyDefaultValue(propertyName, member, type);

        // Then
        assertEquals("", result);
    }

    @Test
    public void testGetPropertyDefaultValue_NullAnnotatedMember() {
        // Given
        String propertyName = "property";
        JavaType type = TypeFactory.uncheckedType(String.class);

        // When & Then
        assertNull(propertyBuilder.getPropertyDefaultValue(propertyName, null, type));
    }

    public static class MyTestClass {
        public static class Property {
            private String value;

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }

        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        public Property property;
    }
}