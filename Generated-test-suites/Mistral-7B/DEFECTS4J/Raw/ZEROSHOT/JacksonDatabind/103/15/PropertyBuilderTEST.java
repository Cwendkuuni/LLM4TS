package com.example.test;

import com.fasterxml.jackson.annotation.JsonInclude;
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
    private PropertyBuilder propertyBuilder;

    @Before
    public void setUp() {
        ObjectMapper mapper = new ObjectMapper();
        config = mapper.getSerializationConfig();
        beanDesc = mapper.getTypeFactory().constructType(TestBean.class);
        propertyBuilder = new PropertyBuilder(config, beanDesc);
    }

    @Test
    public void testClassAnnotations() {
        Annotations annotations = propertyBuilder.getClassAnnotations();
        assertNotNull(annotations);
        assertTrue(annotations.isAnnotationPresent(JsonInclude.class));
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        SerializerProvider prov = mapper.getSerializerProvider();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType declaredType = mapper.getTypeFactory().constructType(String.class);
        JsonSerializer<?> ser = mock(JsonSerializer.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = mapper.getTypeSerializer(String[].class);
        AnnotatedMember am = mock(AnnotatedMember.class);
        assertTrue(propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, false) instanceof BeanPropertyWriter);
    }

    @Test(expected = JsonMappingException.class)
    public void testBuildWriterInvalidType() throws JsonMappingException {
        SerializerProvider prov = mapper.getSerializerProvider();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType declaredType = mapper.getTypeFactory().constructType(Object.class);
        JsonSerializer<?> ser = mock(JsonSerializer.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = mapper.getTypeSerializer(String[].class);
        AnnotatedMember am = mock(AnnotatedMember.class);
        propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);
    }

    @Test
    public void testFindSerializationType() throws JsonMappingException {
        Annotated a = mock(Annotated.class);
        JavaType declaredType = mapper.getTypeFactory().constructType(String.class);
        JavaType secondary = mapper.getTypeFactory().constructType(Integer.class);
        when(a.getType()).thenReturn(declaredType);
        propertyBuilder.findSerializationType(a, false, declaredType);
        verify(a).getType();
        verifyNoMoreInteractions(a);

        when(a.getType()).thenReturn(secondary);
        propertyBuilder.findSerializationType(a, false, declaredType);
        verify(a).getType();
        verify(a).getType().isAssignableFrom(declaredType.getRawClass());
        verifyNoMoreInteractions(a);

        when(a.getType()).thenReturn(Mapper.ValueHandler.anyType());
        propertyBuilder.findSerializationType(a, false, declaredType);
        verify(a).getType();
        verifyNoMoreInteractions(a);

        when(a.getType()).thenThrow(new JsonMappingException("Test"));
        propertyBuilder.findSerializationType(a, false, declaredType);
        fail("Expected JsonMappingException");
    }

    @Test
    public void testFindSerializationTyping() throws JsonMappingException {
        Annotated a = mock(Annotated.class);
        when(a.getType()).thenReturn(Mapper.ValueHandler.anyType());
        JsonSerialize.Typing typing = propertyBuilder.findSerializationTyping(a);
        assertEquals(JsonSerialize.Typing.DEFAULT_TYPING, typing);

        when(a.getAnnotation(JsonSerialize.class)).thenReturn(new JsonSerialize());
        typing = propertyBuilder.findSerializationTyping(a);
        assertEquals(JsonSerialize.Typing.STATIC, typing);
    }

    @Test
    public void testGetClassAnnotations() {
        Annotations annotations = propertyBuilder.getClassAnnotations();
        assertNotNull(annotations);
        assertTrue(annotations.isAnnotationPresent(JsonInclude.class));
    }

    @Test
    public void testGetDefaultBean() {
        Object defaultBean = propertyBuilder.getDefaultBean();
        assertNull(defaultBean);

        propertyBuilder = new PropertyBuilder(config, beanDesc) {
            @Override
            protected Object getDefaultBean() {
                return new TestBean();
            }
        };
        defaultBean = propertyBuilder.getDefaultBean();
        assertNotNull(defaultBean);
        assertTrue(defaultBean instanceof TestBean);
    }

    @Test
    public void testGetPropertyDefaultValue() throws Exception {
        String name = "name";
        AnnotatedMember member = mock(AnnotatedMember.class);
        JavaType type = mapper.getTypeFactory().constructType(String.class);
        Object defaultBean = propertyBuilder.getDefaultBean();
        Object value = propertyBuilder.getPropertyDefaultValue(name, member, type);
        assertEquals(null, value);

        TestBean testBean = new TestBean();
        when(member.getValue(defaultBean)).thenReturn("defaultValue");
        value = propertyBuilder.getPropertyDefaultValue(name, member, type);
        assertEquals("defaultValue", value);
    }

    @Test(expected = DeprecationException.class)
    public void testGetPropertyDefaultValueDeprecated() throws Exception {
        String name = "name";
        AnnotatedMember member = mock(AnnotatedMember.class);
        JavaType type = mapper.getTypeFactory().constructType(String.class);
        propertyBuilder.getPropertyDefaultValue(name, member, type);
    }

    @Test
    public void testGetDefaultValue() throws Exception {
        JavaType type = mapper.getTypeFactory().constructType(String.class);
        Object value = propertyBuilder.getDefaultValue(type);
        assertNull(value);
    }

    @Test(expected = DeprecationException.class)
    public void testGetDefaultValueDeprecated() throws Exception {
        JavaType type = mapper.getTypeFactory().constructType(String.class);
        propertyBuilder.getDefaultValue(type);
    }

    @Test
    public void testThrowWrapped() throws Exception {
        Exception e = new Exception("TestException");
        String propName = "propName";
        Object defaultBean = new TestBean();
        Object thrown = propertyBuilder._throwWrapped(e, propName, defaultBean);
        fail("Expected IllegalArgumentException");
    }
}

class TestBean {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}