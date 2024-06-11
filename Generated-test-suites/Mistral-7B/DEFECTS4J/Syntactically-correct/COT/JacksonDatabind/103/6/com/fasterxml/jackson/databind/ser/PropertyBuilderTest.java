package com.fasterxml.jackson.databind.ser; // package import

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

    private PropertyBuilder propertyBuilder;
    private SerializationConfig serializationConfig;
    private BeanDescription beanDescription;

    @Before
    public void setUp() {
        serializationConfig = new SerializationConfig();
        beanDescription = new BeanDescription("TestBean");
        propertyBuilder = new PropertyBuilder(serializationConfig, beanDescription);
    }

    @Test
    public void testPropertyBuilderConstructor() {
        assertNotNull(propertyBuilder);
    }

    @Test
    public void testGetClassAnnotations() {
        Annotations annotations = propertyBuilder.getClassAnnotations();
        assertNotNull(annotations);
    }

    @Test
    public void testBuildWriterWithNullSer() {
        SerializerProvider prov = new SerializerProvider();
        assertNull(propertyBuilder.buildWriter(prov, null, null, null, null, null, null, false));
    }

    @Test
    public void testBuildWriterWithNullDeclaredType() {
        SerializerProvider prov = new SerializerProvider();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        assertNull(propertyBuilder.buildWriter(prov, propDef, null, null, null, null, null, false));
    }

    @Test(expected = JsonMappingException.class)
    public void testBuildWriterWithNullPropDef() {
        SerializerProvider prov = new SerializerProvider();
        assertNull(propertyBuilder.buildWriter(prov, null, null, null, null, null, null, false));
        fail("Expected JsonMappingException");
    }

    @Test
    public void testBuildWriterWithNullDeclaredTypeAndSer() {
        SerializerProvider prov = new SerializerProvider();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        assertNull(propertyBuilder.buildWriter(prov, propDef, null, null, null, null, null, false));
    }

    @Test
    public void testFindSerializationTypeWithAnnotation() {
        AnnotatedMember am = new AnnotatedMember(new Class<?>[0], "test");
        JavaType declaredType = TypeFactory.uncheckedType(int[].class);
        JavaType serializationType = propertyBuilder.findSerializationType(am, false, declaredType);
        assertEquals(int[].class, serializationType.getRawClass());
    }

    @Test
    public void testFindSerializationTypeWithoutAnnotation() {
        AnnotatedMember am = new AnnotatedMember(new Class<?>[0], "test");
        JavaType declaredType = TypeFactory.uncheckedType(int[].class);
        JavaType serializationType = propertyBuilder.findSerializationType(am, false, declaredType);
        assertNull(serializationType);
    }

    @Test
    public void testGetDefaultBean() {
        Object defaultBean = propertyBuilder.getDefaultBean();
        assertNull(defaultBean);
    }

    @Test
    public void testGetPropertyDefaultValue() {
        // This method is deprecated since 2.9, so we cannot test it here
    }

    @Test
    public void testGetDefaultValue() {
        Object defaultValue = propertyBuilder.getDefaultValue(TypeFactory.uncheckedType(String.class));
        assertEquals("", defaultValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThrowWrappedExceptionWithNullPropName() {
        Exception e = new RuntimeException();
        propertyBuilder._throwWrapped(e, null, new Object());
        fail("Expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThrowWrappedExceptionWithNullDefaultBean() {
        Exception e = new RuntimeException();
        propertyBuilder._throwWrapped(e, "test", null);
        fail("Expected IllegalArgumentException");
    }
}