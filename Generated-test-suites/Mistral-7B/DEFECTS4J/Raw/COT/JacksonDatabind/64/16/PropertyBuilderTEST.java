package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import com.fasterxml.jackson.databind.ser.PropertyBuilder;

import java.lang.reflect.AnnotatedMember;
import java.lang.reflect.Modifier;

import org.junit.Before;
import org.junit.Test;

public class PropertyBuilderTest {

    private SerializationConfig config;
    private BeanDescription beanDesc;
    private PropertyBuilder propertyBuilder;

    @Before
    public void setup() {
        ObjectMapper mapper = new ObjectMapper();
        this.config = mapper.getSerializationConfig();
        this.beanDesc = new BeanDescription(null, null, null, null, null, null, null, null, null, null, null);
        this.propertyBuilder = new PropertyBuilder(config, beanDesc);
    }

    @Test
    public void testGetClassAnnotations() {
        Annotations expected = new StandardAnnotations();
        Annotations actual = propertyBuilder.getClassAnnotations();
        assertEquals("Unexpected class annotations", expected, actual);
    }

    @Test
    public void testBuildWriter_SerializationTypeNull() throws JsonMappingException {
        SerializerProvider prov = new SerializerProvider();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("testProperty", null, null, null, null, null, null, null, null, null, null, null);
        JavaType declaredType = TypeFactory.uncheckedType(String.class);
        JsonSerializer<?> ser = null;
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = null;
        AnnotatedMember am = new StubAnnotatedMember();
        boolean defaultUseStaticTyping = true;

        BeanPropertyWriter actualWriter = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        assertNull("Unexpected writer instance", actualWriter);
    }

    @Test
    public void testBuildWriter_SerializationTypeNotNull() throws JsonMappingException {
        SerializerProvider prov = new SerializerProvider();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("testProperty", null, null, null, null, null, null, null, null, null, null, null);
        JavaType declaredType = TypeFactory.uncheckedType(String.class);
        JsonSerializer<?> ser = new StubJsonSerializer();
        TypeSerializer typeSer = new StubTypeSerializer();
        TypeSerializer contentTypeSer = new StubTypeSerializer();
        AnnotatedMember am = new StubAnnotatedMember();
        boolean defaultUseStaticTyping = true;

        JavaType serializationType = TypeFactory.uncheckedType(Integer.class);
        BeanPropertyWriter actualWriter = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);

        assertNotNull("Unexpected null writer instance", actualWriter);
        assertEquals("Unexpected writer class", BeanPropertyWriter.class, actualWriter.getClass());
    }

    @Test(expected = JsonMappingException.class)
    public void testBuildWriter_JsonMappingException() throws JsonMappingException {
        SerializerProvider prov = new SerializerProvider();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("testProperty", null, null, null, null, null, null, null, null, null, null, null);
        JavaType declaredType = TypeFactory.uncheckedType(String.class);
        JsonSerializer<?> ser = new StubJsonSerializer();
        TypeSerializer typeSer = new StubTypeSerializer();
        TypeSerializer contentTypeSer = new StubTypeSerializer();
        AnnotatedMember am = new StubAnnotatedMember();
        boolean defaultUseStaticTyping = true;

        JavaType serializationType = null;
        BeanPropertyWriter actualWriter = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);

        fail("Expected JsonMappingException but no exception thrown");
    }

    @Test
    public void testFindSerializationType_AnnotatedIsNull() throws JsonMappingException {
        Annotated a = null;
        boolean useStaticTyping = false;
        JavaType declaredType = TypeFactory.uncheckedType(String.class);

        JavaType actualType = propertyBuilder.findSerializationType(a, useStaticTyping, declaredType);
        assertNull("Unexpected non-null type returned", actualType);
    }

    @Test
    public void testFindSerializationType_AnnotatedIsNotNull() throws JsonMappingException {
        Annotated a = new StubAnnotatedMember();
        boolean useStaticTyping = false;
        JavaType declaredType = TypeFactory.uncheckedType(String.class);

        JavaType actualType = propertyBuilder.findSerializationType(a, useStaticTyping, declaredType);
        assertNotNull("Unexpected null type returned", actualType);
        assertEquals("Unexpected type class", String.class, actualType.getRawClass());
    }

    @Test
    public void testgetDefaultBean_DefaultBeanIsNotNull() {
        Object defaultBean = new Object();
        propertyBuilder._defaultBean = defaultBean;
        Object actualBean = propertyBuilder.getDefaultBean();
        assertEquals("Unexpected bean instance", defaultBean, actualBean);
    }

    @Test
    public void testgetDefaultBean_DefaultBeanIsNull() {
        propertyBuilder._defaultBean = null;
        Object actualBean = propertyBuilder.getDefaultBean();
        assertNull("Unexpected non-null bean instance", actualBean);
    }

    @Test(expected = IllegalStateException.class)
    public void testgetPropertyDefaultValue_PropertyNameNull() {
        String name = null;
        AnnotatedMember member = new StubAnnotatedMember();
        JavaType type = TypeFactory.uncheckedType(String.class);

        propertyBuilder.getPropertyDefaultValue(name, member, type);
        fail("Expected IllegalStateException but no exception thrown");
    }

    @Test(expected = IllegalStateException.class)
    public void testgetPropertyDefaultValue_DefaultBeanIsNull() {
        String name = "testProperty";
        AnnotatedMember member = new StubAnnotatedMember();
        JavaType type = TypeFactory.uncheckedType(String.class);

        propertyBuilder._defaultBean = null;
        propertyBuilder.getPropertyDefaultValue(name, member, type);
        fail("Expected IllegalStateException but no exception thrown");
    }

    @Test
    public void testgetPropertyDefaultValue_DefaultBeanAndMemberNotNull() throws Exception {
        String name = "testProperty";
        AnnotatedMember member = new StubAnnotatedMember();
        JavaType type = TypeFactory.uncheckedType(String.class);
        Object defaultBean = new Object();

        Object actualDefaultValue = propertyBuilder.getPropertyDefaultValue(name, member, type);
        assertNull("Unexpected non-null default value", actualDefaultValue);

        propertyBuilder._defaultBean = defaultBean;
        actualDefaultValue = propertyBuilder.getPropertyDefaultValue(name, member, type);
        assertEquals("Unexpected default value", member.getValue(defaultBean), actualDefaultValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDefaultValue_TypeIsNull() {
        JavaType type = null;
        propertyBuilder.getDefaultValue(type);
        fail("Expected IllegalArgumentException but no exception thrown");
    }

    @Test
    public void testGetDefaultValue_PrimitiveType() {
        JavaType type = TypeFactory.uncheckedType(int.class);
        Object actualDefaultValue = propertyBuilder.getDefaultValue(type);
        assertEquals("Unexpected default value", 0, actualDefaultValue);
    }

    @Test
    public void testGetDefaultValue_WrapperType() {
        JavaType type = TypeFactory.uncheckedType(Integer.class);
        Object actualDefaultValue = propertyBuilder.getDefaultValue(type);
        assertNull("Unexpected non-null default value", actualDefaultValue);
    }

    @Test
    public void testGetDefaultValue_StringType() {
        JavaType type = TypeFactory.uncheckedType(String.class);
        Object actualDefaultValue = propertyBuilder.getDefaultValue(type);
        assertEquals("Unexpected default value", "", actualDefaultValue);
    }

    @Test
    public void testGetDefaultValue_CollectionType() {
        JavaType type = TypeFactory.uncheckedType(java.util.List.class);
        Object actualDefaultValue = propertyBuilder.getDefaultValue(type);
        assertEquals("Unexpected default value", JsonInclude.Include.NON_DEFAULT, actualDefaultValue);
    }

    @Test
    public void testGetDefaultValue_MapType() {
        JavaType type = TypeFactory.uncheckedType(java.util.Map.class);
        Object actualDefaultValue = propertyBuilder.getDefaultValue(type);
        assertEquals("Unexpected default value", JsonInclude.Include.NON_DEFAULT, actualDefaultValue);
    }

    @Test
    public void testGetDefaultValue_ReferenceType() {
        JavaType type = TypeFactory.uncheckedType(String.class);
        Object actualDefaultValue = propertyBuilder.getDefaultValue(type);
        assertEquals("Unexpected default value", JsonInclude.Include.NON_DEFAULT, actualDefaultValue);
    }

    private static class StubAnnotatedMember implements Annotated {
        @Override
        public Annotation[] findAnnotations() {
            return new Annotation[0];
        }

        @Override
        public Class<?> getDeclaringClass() {
            return PropertyBuilderTest.class;
        }

        @Override
        public Annotation[] getDeclaredAnnotationsByType(Class<? extends Annotation> annotationType) {
            return new Annotation[0];
        }

        @Override
        public boolean isAnnotationPresent(Class<? extends Annotation> annotationType) {
            return false;
        }

        @Override
        public String getName() {
            return "testProperty";
        }

        @Override
        public int getMemberVisibility() {
            return Modifier.PUBLIC;
        }

        @Override
        public boolean isSynthetic() {
            return false;
        }

        @Override
        public void setAccessible(boolean accessible) {
        }

        @Override
        public Object getValue(Object object) throws IllegalAccessException, InvocationTargetException {
            return null;
        }

        @Override
        public void setValue(Object object, Object value) throws IllegalAccessException, InvocationTargetException {
        }
    }

    private static class StubJsonSerializer extends JsonSerializer<Object> {
    }

    private static class StubTypeSerializer extends TypeSerializer {
    }
}