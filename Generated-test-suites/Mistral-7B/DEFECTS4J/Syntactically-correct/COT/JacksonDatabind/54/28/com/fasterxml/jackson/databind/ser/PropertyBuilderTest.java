package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

    private SerializationConfig config;
    private BeanDescription beanDesc;

    @Before
    public void setUp() {
        ObjectMapper mapper = new ObjectMapper();
        this.config = mapper.getSerializationConfig();
        this.beanDesc = mapper.getTypeFactory().constructModel("TestClass");
    }

    @Test
    public void testConstructor() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        assertNotNull(propertyBuilder);
    }

    @Test
    public void testGetClassAnnotations() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Annotations annotations = propertyBuilder.getClassAnnotations();
        assertNotNull(annotations);
    }

    @Test
    public void testBuildWriterWithNullValues() throws JsonMappingException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Object valueToSuppress = null;
        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);
        assertNotNull(beanPropertyWriter);
        assertEquals(valueToSuppress, beanPropertyWriter.getValueToSuppress());
        assertFalse(beanPropertyWriter.suppressNulls());
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildWriterWithNullContentTypeSer() throws JsonMappingException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        TypeSerializer contentTypeSer = null;
        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);
    }

    @Test
    public void testFindSerializationTypeWithAnnotation() throws JsonMappingException {
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        when(am.getGenericType()).thenReturn(TypeFactory.type(Integer.class, String.class));
        JavaType declaredType = TypeFactory.type(Integer.class);
        JavaType serializationType = propertyBuilder.findSerializationType(am, false, declaredType);
        assertEquals(TypeFactory.type(String.class), serializationType);
    }

    @Test
    public void testFindSerializationTypeWithoutAnnotation() throws JsonMappingException {
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        when(am.getGenericType()).thenReturn(TypeFactory.type(Integer.class));
        JavaType declaredType = TypeFactory.type(Integer.class);
        JavaType serializationType = propertyBuilder.findSerializationType(am, false, declaredType);
        assertNull(serializationType);
    }

    @Test
    public void testFindSerializationTypeWithSuperTypeAnnotation() throws JsonMappingException {
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        when(am.getGenericType()).thenReturn(TypeFactory.type(Integer[].class, String.class));
        JavaType declaredType = TypeFactory.type(Integer[].class);
        JavaType serializationType = propertyBuilder.findSerializationType(am, false, declaredType);
        assertEquals(TypeFactory.type(Integer[].class), serializationType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindSerializationTypeWithUnrelatedTypeAnnotation() throws JsonMappingException {
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        when(am.getGenericType()).thenReturn(TypeFactory.type(String.class, Integer.class));
        JavaType declaredType = TypeFactory.type(Integer.class);
        propertyBuilder.findSerializationType(am, false, declaredType);
    }

    @Test
    public void testGetPropertyDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Object defaultBean = propertyBuilder.getDefaultBean();
        String name = "testProperty";
        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        when(member.getName()).thenReturn(name);
        when(member.getValue(defaultBean)).thenReturn("defaultValue");
        JavaType type = TypeFactory.type(String.class);
        Object defaultValue = propertyBuilder.getPropertyDefaultValue(name, member, type);
        assertEquals("defaultValue", defaultValue);
    }

    @Test
    public void testGetDefaultValueForPrimitiveType() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        JavaType type = TypeFactory.type(char.class);
        Object defaultValue = propertyBuilder.getDefaultValue(type);
        assertEquals((char) 0, defaultValue);
    }

    @Test
    public void testGetDefaultValueForStringType() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        JavaType type = TypeFactory.type(String.class);
        Object defaultValue = propertyBuilder.getDefaultValue(type);
        assertEquals("", defaultValue);
    }

    @Test
    public void testGetDefaultValueForContainerType() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        JavaType type = TypeFactory.typeCollection(JavaType.constructCollectionType(List.class, Integer.class));
        Object defaultValue = propertyBuilder.getDefaultValue(type);
        assertEquals(JsonInclude.Include.NON_EMPTY, defaultValue);
    }

    @Test
    public void testGetDefaultValueForReferenceType() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        JavaType type = TypeFactory.type(TestClass.class);
        Object defaultValue = propertyBuilder.getDefaultValue(type);
        assertEquals(JsonInclude.Include.NON_EMPTY, defaultValue);
    }

    @Test
    public void testThrowWrapped() {
        Exception e = new RuntimeException("Test exception");
        String propName = "testProperty";
        Object defaultBean = new Object();
        Object result = propertyBuilder._throwWrapped(e, propName, defaultBean);
        assertEquals(IllegalArgumentException.class, result.getClass());
    }
}

class TestClass {

    @JsonSerialize(using = ToStringSerializer.class)
    private int testProperty;

}