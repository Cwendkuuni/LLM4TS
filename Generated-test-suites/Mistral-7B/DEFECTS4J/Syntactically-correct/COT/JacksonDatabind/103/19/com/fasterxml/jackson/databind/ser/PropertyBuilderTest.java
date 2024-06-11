package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory.PropertyBuilder;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

public class PropertyBuilderTest {

    private SerializationConfig _config;
    private BeanDescription _beanDesc;
    private PropertyBuilder _propertyBuilder;
    private AnnotationIntrospector _annotationIntrospector;

    @Before
    public void setUp() {
        _config = new SerializationConfig();
        _beanDesc = new BeanDescription(Object.class);
        _annotationIntrospector = new AnnotationIntrospector();
        _propertyBuilder = new PropertyBuilder(_config, _beanDesc);
    }

    @Test
    public void testConstructor() {
        assertNotNull(_propertyBuilder);
    }

    @Test
    public void testGetClassAnnotations() {
        assertNotNull(_propertyBuilder.getClassAnnotations());
    }

    @Test
    public void testBuildWriter_NoException() throws Exception {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("testProperty", Object.class, null);
        JavaType declaredType = TypeFactory.uncheckedInstance(String[].class);
        JsonSerializer<?> ser = new StdSerializer<>(String[].class);
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = null;
        AnnotatedMember am = null;
        boolean defaultUseStaticTyping = false;

        BeanPropertyWriter bpw = _propertyBuilder.buildWriter(_config.getDefaultSerializerProvider(), propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        assertNotNull(bpw);
    }

    @Test(expected = JsonMappingException.class)
    public void testBuildWriter_JsonMappingException() throws Exception {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("testProperty", Object.class, null);
        JavaType declaredType = TypeFactory.uncheckedInstance(String[].class);
        JsonSerializer<?> ser = new StdSerializer<>(String[].class);
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = null;
        AnnotatedMember am = null;
        boolean defaultUseStaticTyping = false;

        _propertyBuilder.buildWriter(_config.getDefaultSerializerProvider(), null, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
    }

    @Test
    public void testFindSerializationType_Annotated() throws Exception {
        Class<?> clazz = String[].class;
        Annotated a = new AnnotatedMember() {
            @Override
            public JavaType getType() {
                return TypeFactory.uncheckedInstance(clazz);
            }

            @Override
            public String getName() {
                return "testProperty";
            }
        };
        JavaType declaredType = null;

        JavaType secondary = _propertyBuilder.findSerializationType(a, true, declaredType);
        assertEquals(clazz, secondary.getRawClass());
    }

    @Test
    public void testFindSerializationType_NoAnnotated() throws Exception {
        Class<?> clazz = String[].class;
        JavaType declaredType = TypeFactory.uncheckedInstance(clazz);

        JavaType secondary = _propertyBuilder.findSerializationType(null, true, declaredType);
        assertNull(secondary);
    }

    @Test
    public void testGetDefaultBean() {
        Object defaultBean = _propertyBuilder.getDefaultBean();
        assertNull(defaultBean);
    }

    @Test
    public void testGetDefaultBean_Instantiated() {
        Object defaultBean = _propertyBuilder.getDefaultBean();
        if (defaultBean == null) {
            _propertyBuilder = new PropertyBuilder(_config, _beanDesc);
            defaultBean = _propertyBuilder.getDefaultBean();
        }
        assertNotNull(defaultBean);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPropertyDefaultValue_Null() {
        String name = "testProperty";
        AnnotatedMember member = new AnnotatedMember() {};
        JavaType type = TypeFactory.uncheckedInstance(Object.class);

        Object defaultValue = _propertyBuilder.getPropertyDefaultValue(name, member, type);
        assertNull(defaultValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPropertyDefaultValue_Exception() {
        String name = "testProperty";
        AnnotatedMember member = new AnnotatedMember() {};
        JavaType type = TypeFactory.uncheckedInstance(Object.class);

        Object defaultValue = _propertyBuilder.getPropertyDefaultValue(name, member, type);
        _propertyBuilder.getDefaultBean();
    }

    @Test
    public void testGetDefaultValue_Null() {
        JavaType type = TypeFactory.uncheckedInstance(Object.class);

        Object defaultValue = _propertyBuilder.getDefaultValue(type);
        assertNull(defaultValue);
    }

    @Test
    public void testGetDefaultValue_DefaultValue() {
        JavaType type = TypeFactory.uncheckedInstance(Integer.class);

        Object defaultValue = _propertyBuilder.getDefaultValue(type);
        assertEquals(0, defaultValue);
    }

    @Test
    public void testGetDefaultValue_CustomConverter() {
        JavaType type = TypeFactory.uncheckedInstance(Converter.class);
        Converter converter = new ConverterImpl();
        _propertyBuilder.setDefaultValueConverter(_config, converter);

        Object defaultValue = _propertyBuilder.getDefaultValue(type);
        assertEquals(converter, defaultValue);
    }

    static class ConverterImpl implements Converter {
        @Override
        public Object readValue(Class<?> clazz, Object value) {
            return null;
        }

        @Override
        public Object writeValue(Class<?> clazz, Object value) {
            return null;
        }
    }
}