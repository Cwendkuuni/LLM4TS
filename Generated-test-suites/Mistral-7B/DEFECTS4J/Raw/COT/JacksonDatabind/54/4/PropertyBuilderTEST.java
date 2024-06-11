package com.example.test;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.serializer.*;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;

public class PropertyBuilderTest {

    private SerializationConfig config;
    private BeanDescription beanDesc;
    private PropertyBuilder propertyBuilder;

    @Before
    public void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        config = objectMapper.getSerializationConfig();
        beanDesc = objectMapper.findBeanDescription(Bean.class);
        propertyBuilder = new PropertyBuilder(config, beanDesc);
    }

    @Test
    public void testGetClassAnnotations() {
        Annotations annotations = propertyBuilder.getClassAnnotations();
        assertNotNull(annotations);
        assertTrue(annotations.isAnnotationPresent(JsonInclude.class));
    }

    @Test
    public void testFindSerializationType() throws JsonMappingException {
        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        JavaType declaredType = Mockito.mock(JavaType.class);
        when(member.getType()).thenReturn(declaredType);

        JavaType serializationType = propertyBuilder.findSerializationType(member, false, declaredType);
        assertNull(serializationType);

        doReturn(ClassUtil.typeFromAnnotation(Mockito.mock(JsonSerialize.class), int.class)).when(member).getSerializerProvider().findSerializer(int.class);
        doReturn(TypeFactory.defaultInstanceType()).when(declaredType).getRawClass();
        doReturn(JsonSerialize.Typing.STATIC).when(member).getSerializationType();

        serializationType = propertyBuilder.findSerializationType(member, true, declaredType);
        assertEquals(int.class, serializationType.getRawClass());
        assertEquals(JsonSerialize.Typing.STATIC.getClass(), serializationType.getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindSerializationTypeWithInvalidType() throws JsonMappingException {
        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        JavaType declaredType = Mockito.mock(JavaType.class);
        when(member.getType()).thenReturn(declaredType);

        doReturn(TypeFactory.defaultInstanceType()).when(declaredType).getRawClass();
        doReturn(null).when(member).getSerializerProvider();

        propertyBuilder.findSerializationType(member, false, declaredType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindSerializationTypeWithInvalidSerializerProvider() throws JsonMappingException {
        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        JavaType declaredType = Mockito.mock(JavaType.class);
        when(member.getType()).thenReturn(declaredType);

        doReturn(null).when(member).getSerializerProvider();

        propertyBuilder.findSerializationType(member, false, declaredType);
    }

    @Test
    public void testGetPropertyDefaultValue() throws IOException {
        doReturn("defaultValue").when(propertyBuilder).getDefaultValue(String.class);

        assertEquals("defaultValue", propertyBuilder.getPropertyDefaultValue("test", Mockito.mock(AnnotatedMember.class), String.class));
    }

    @Test
    public void testGetPropertyDefaultValueWithNullValue() throws IOException {
        doReturn(null).when(propertyBuilder).getDefaultValue(String.class);

        assertNull(propertyBuilder.getPropertyDefaultValue("test", Mockito.mock(AnnotatedMember.class), String.class));
    }

    @Test
    public void testGetPropertyDefaultValueWithEmptyCollection() throws IOException {
        doReturn(new Object()[0]).when(propertyBuilder).getDefaultValue(Object[].class);

        assertArrayEquals(new Object[0], propertyBuilder.getPropertyDefaultValue("test", Mockito.mock(AnnotatedMember.class), Object[].class));
    }

    @Test
    public void testGetPropertyDefaultValueWithEmptyMap() throws IOException {
        doReturn(new HashMap<>()).when(propertyBuilder).getDefaultValue(Map.class);

        assertEquals(new HashMap<>(), propertyBuilder.getPropertyDefaultValue("test", Mockito.mock(AnnotatedMember.class), Map.class));
    }

    @Test
    public void testGetDefaultValue() throws IOException {
        doReturn(0).when(propertyBuilder).getDefaultValue(int.class);

        assertEquals(0, propertyBuilder.getDefaultValue(int.class).intValue());
    }

    @Test
    public void testGetDefaultValueWithNullType() throws IOException {
        doReturn(null).when(propertyBuilder).getDefaultValue(Object.class);

        assertNull(propertyBuilder.getDefaultValue(Object.class));
    }

    @Test
    public void testGetDefaultBean() {
        Object defaultBean = propertyBuilder.getDefaultBean();
        assertNull(defaultBean);

        Bean bean = new Bean();
        propertyBuilder.getDefaultBean() = bean;
        assertEquals(bean, propertyBuilder.getDefaultBean());
    }

    @Test
    public void testBuildWriter() throws IOException {
        doReturn(Mockito.mock(SerializerProvider.class)).when(propertyBuilder).getSerializerProvider();
        doReturn(Mockito.mock(BeanPropertyDefinition.class)).when(propertyBuilder)._beanDesc.findPropertyDefinition("test");
        doReturn(Mockito.mock(JavaType.class)).when(propertyBuilder).findSerializationType(Mockito.mock(AnnotatedMember.class), true, Mockito.mock(JavaType.class));

        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(Mockito.mock(SerializerProvider.class), Mockito.mock(BeanPropertyDefinition.class), Mockito.mock(JavaType.class), Mockito.mock(JsonSerializer.class), Mockito.mock(TypeSerializer.class), Mockito.mock(TypeSerializer.class), Mockito.mock(AnnotatedMember.class), false);
        assertNotNull(beanPropertyWriter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildWriterWithNullPropertyDefinition() throws IOException {
        doReturn(Mockito.mock(SerializerProvider.class)).when(propertyBuilder).getSerializerProvider();
        doReturn(null).when(propertyBuilder).findSerializationType(Mockito.mock(AnnotatedMember.class), true, Mockito.mock(JavaType.class));

        propertyBuilder.buildWriter(Mockito.mock(SerializerProvider.class), null, Mockito.mock(JavaType.class), Mockito.mock(JsonSerializer.class), Mockito.mock(TypeSerializer.class), Mockito.mock(TypeSerializer.class), Mockito.mock(AnnotatedMember.class), false);
    }

    static class Bean {
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        int test = 1;
    }

    static class BeanSerializerFactory {
        // Stub implementation
    }

}