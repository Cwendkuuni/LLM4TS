package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.annotation.JsonSerialize.Typing;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.BeanPropertyWriter;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.BeanPropertyWriter.MarkerForEmpty;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

    private SerializationConfig config;
    private BeanDescription beanDesc;
    private AnnotationIntrospector annotationIntrospector;

    @Before
    public void setUp() throws Exception {
        config = new SerializationConfig();
        ObjectMapper mapper = new ObjectMapper();
        BeanFactory beanFactory = mapper.getFactory();
        BeanDefinitionBuilder defBuilder = BeanDefinitionBuilder.rootBeanDefinition(DummyClass.class);
        AnnotatedClass ac = defBuilder.getAnnotatedClass();
        beanDesc = beanFactory.getBeanDescription(ac);
        annotationIntrospector = config.getDefaultAnnotationIntrospector();
    }

    @After
    public void tearDown() throws Exception {
        config = null;
        beanDesc = null;
        annotationIntrospector = null;
    }

    @Test
    public void testGetClassAnnotations() {
        assertEquals(getClass().getClass(), beanDesc.getClassAnnotations().get(0).getClass());
    }

    @Test
    public void testFindSerializationType() throws JsonMappingException {
        JavaType declaredType = TypeFactory.defaultInstance().constructType(DummyClass.class);
        JavaType expectedType = TypeFactory.defaultInstance().constructType(SubclassOfDummyClass.class);
        AnnotatedMember am = mock(AnnotatedMember.class);
        when(am.getGenericType()).thenReturn(TypeFactory.defaultInstance().constructType(String.class));
        when(am.isStatic()).thenReturn(false);

        JavaType result = new PropertyBuilder(config, beanDesc).findSerializationType(am, false, declaredType);
        assertNull(result);

        when(am.getAnnotation(JsonSerialize.class)).thenReturn(mock(JsonSerialize.class));
        when(am.getAnnotation(JsonSerialize.class).getType()).thenReturn(expectedType);
        result = new PropertyBuilder(config, beanDesc).findSerializationType(am, false, declaredType);
        assertEquals(expectedType, result);

        // Test static typing
        when(am.isStatic()).thenReturn(true);
        result = new PropertyBuilder(config, beanDesc).findSerializationType(am, true, declaredType);
        assertEquals(expectedType, result);
    }

    @Test
    public void testGetDefaultBean() {
        Object defaultBean = new PropertyBuilder(config, beanDesc).getDefaultBean();
        assertNull(defaultBean);

        // Test instantiation
        AnnotatedClass ac = mock(AnnotatedClass.class);
        when(ac.getAnnotated()).thenReturn(DummyClass.class);
        when(beanDesc.getClassInfo()).thenReturn(ac);
        defaultBean = new PropertyBuilder(config, beanDesc).getDefaultBean();
        assertNotNull(defaultBean);

        // Test null default bean
        AnnotatedClass acNull = mock(AnnotatedClass.class);
        when(acNull.getAnnotated()).thenReturn(null);
        when(beanDesc.getClassInfo()).thenReturn(acNull);
        defaultBean = new PropertyBuilder(config, beanDesc).getDefaultBean();
        assertNull(defaultBean);
    }

    @Test
    public void testGetPropertyDefaultValue() throws JsonMappingException {
        String name = "name";
        JavaType type = TypeFactory.defaultInstance().constructType(String.class);
        Object defaultBean = new PropertyBuilder(config, beanDesc).getDefaultBean();
        Object result = new PropertyBuilder(config, beanDesc).getPropertyDefaultValue(name, null, type);
        assertEquals("", result);

        // Test non-default bean
        AnnotatedMember member = mock(AnnotatedMember.class);
        when(member.getName()).thenReturn(name);
        when(member.getValue(defaultBean)).thenReturn("default");
        result = new PropertyBuilder(config, beanDesc).getPropertyDefaultValue(name, member, type);
        assertEquals("default", result);

        // Test null default value
        when(member.getValue(defaultBean)).thenReturn(null);
        result = new PropertyBuilder(config, beanDesc).getPropertyDefaultValue(name, member, type);
        assertNull(result);
    }

    @Test
    public void testGetDefaultValue() {
        JavaType type = TypeFactory.defaultInstance().constructType(Integer.class);
        Object result = new PropertyBuilder(config, beanDesc).getDefaultValue(type);
        assertEquals(0, result);

        type = TypeFactory.defaultInstance().constructType(String.class);
        result = new PropertyBuilder(config, beanDesc).getDefaultValue(type);
        assertEquals("", result);

        type = TypeFactory.defaultInstance().constructType(Map.class);
        result = new PropertyBuilder(config, beanDesc).getDefaultValue(type);
        assertEquals(JsonInclude.Include.NON_EMPTY, result);

        type = TypeFactory.defaultInstance().constructType(List.class);
        result = new PropertyBuilder(config, beanDesc).getDefaultValue(type);
        assertEquals(JsonInclude.Include.NON_EMPTY, result);

        type = TypeFactory.defaultInstance().constructType(DummyClass.class);
        result = new PropertyBuilder(config, beanDesc).getDefaultValue(type);
        assertEquals(JsonInclude.Include.NON_EMPTY, result);
    }

    static class DummyClass {
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        String name;
    }

    static class SubclassOfDummyClass extends DummyClass {
    }
}