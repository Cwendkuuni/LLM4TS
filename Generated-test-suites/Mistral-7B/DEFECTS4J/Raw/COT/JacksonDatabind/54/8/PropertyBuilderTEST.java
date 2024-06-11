package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.BeanPropertyWriter;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TypeFactory;
import com.fasterxml.jackson.databind.util.WrappedException;

public class PropertyBuilderTest {

    private SerializationConfig config;
    private BeanDescription beanDesc;
    private PropertyBuilder pb;

    @Before
    public void setUp() {
        config = new SerializationConfig(new AnnotationIntrospector());
        ObjectMapper mapper = new ObjectMapper(config);
        JavaClass beanClass = mapper.getTypeFactory().constructType(TestBean.class);
        beanDesc = mapper.getClassInfo(beanClass);
        pb = new PropertyBuilder(config, beanDesc);
    }

    @Test
    public void testGetClassAnnotations() {
        assertNotNull(pb.getClassAnnotations());
    }

    @Test
    public void testBuildWriter_Default() throws JsonMappingException {
        ObjectSerializer ser = mock(ObjectSerializer.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = mock(TypeSerializer.class);
        JavaType declaredType = mock(JavaType.class);
        AnnotatedMember am = mock(AnnotatedMember.class);
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        BeanPropertyWriter bpw = pb.buildWriter(mock(SerializerProvider.class), propDef, declaredType, ser, typeSer, contentTypeSer, am, false);
        assertNotNull(bpw);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildWriter_NoContentType() throws JsonMappingException {
        ObjectSerializer ser = mock(ObjectSerializer.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType declaredType = mock(JavaType.class);
        AnnotatedMember am = mock(AnnotatedMember.class);
        BeanPropertyWriter bpw = pb.buildWriter(mock(SerializerProvider.class), propDef, declaredType, ser, typeSer, null, am, false);
    }

    @Test(expected = IllegalStateException.class)
    public void testFindSerializationType_IllegalAnnotation() throws JsonMappingException {
        Annotated a = mock(Annotated.class);
        JavaType declaredType = mock(JavaType.class);
        pb.findSerializationType(a, false, declaredType);
        verify(a).getClass();
        throw new AssertionError("Should not reach here");
    }

    @Test
    public void testFindSerializationType_ValidSerializationType() throws JsonMappingException {
        Annotated a = mock(Annotated.class);
        JavaType declaredType = mock(JavaType.class);
        JavaType secondary = mock(JavaType.class);
        when(declaredType.getRawClass()).thenReturn(String[].class);
        when(secondary.getRawClass()).thenReturn(List.class);
        doReturn(secondary).when(pb)._annotationIntrospector
                .refineSerializationType(config, a, declaredType);

        JavaType serializationType = pb.findSerializationType(a, false, declaredType);
        assertEquals(List.class, serializationType);
    }

    @Test
    public void testGetDefaultBean() {
        Object defaultBean = pb.getDefaultBean();
        assertNull(defaultBean);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPropertyDefaultValue_NullName() {
        String name = null;
        AnnotatedMember member = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        pb.getPropertyDefaultValue(name, member, type);
    }

    @Test
    public void testGetPropertyDefaultValue_DefaultValue() {
        String name = "name";
        AnnotatedMember member = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        Object defaultBean = new TestBean();
        when(member.getValue(defaultBean)).thenReturn(1);

        Object defaultValue = pb.getPropertyDefaultValue(name, member, type);
        assertEquals(1, defaultValue);
    }

    @Test
    public void testGetPropertyDefaultValue_NullDefaultBean() {
        String name = "name";
        AnnotatedMember member = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        pb.getPropertyDefaultValue(name, member, type);
        verifyNoMoreInteractions(member);
    }

    @Test
    public void testGetPropertyDefaultValue_ThrowException() throws Exception {
        String name = "name";
        AnnotatedMember member = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        Object defaultBean = new TestBean();
        when(member.getValue(defaultBean)).thenThrow(new Exception());

        Object defaultValue = pb.getPropertyDefaultValue(name, member, type);
        assertNull(defaultValue);
    }

    @Test
    public void testThrowWrapped() {
        Exception e = new IllegalArgumentException("Exception message");
        assertNotNull(pb._throwWrapped(e, "propertyName", null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThrowWrapped_NullPropertyName() {
        Exception e = new IllegalArgumentException("Exception message");
        pb._throwWrapped(e, null, null);
    }

    private static class TestBean {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}