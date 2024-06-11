package com.fasterxml.jackson.databind.ser.test;

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

    private PropertyBuilder propertyBuilder;

    @Before
    public void setUp() {
        ObjectMapper mapper = new ObjectMapper();
        BeanDescription beanDesc = mapper.getTypeFactory().constructBeanDescription(DummyClass.class);
        SerializationConfig config = mapper.getSerializationConfig();
        propertyBuilder = new PropertyBuilder(config, beanDesc);
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        BeanPropertyDefinition propDef = propertyBuilder.getClassAnnotations().findProperty("name");
        SerializerProvider prov = mapper.getSerializerProvider();
        JavaType declaredType = propertyBuilder.getTypeFactory().constructType(String.class);
        JsonSerializer<String> ser = mapper.findValueSerializer(String.class, null);
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = mapper.getTypeSerializer();
        AnnotatedMember am = null;
        boolean defaultUseStaticTyping = false;

        BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        assertNotNull(bpw);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildWriterWhenContentTypeIsNull() throws JsonMappingException {
        BeanPropertyDefinition propDef = propertyBuilder.getClassAnnotations().findProperty("name");
        SerializerProvider prov = mapper.getSerializerProvider();
        JavaType declaredType = propertyBuilder.getTypeFactory().constructType(String.class);
        JsonSerializer<String> ser = mapper.findValueSerializer(String.class, null);
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = null;
        AnnotatedMember am = null;
        boolean defaultUseStaticTyping = false;

        propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
    }

    @Test
    public void testFindSerializationType() throws JsonMappingException {
        Annotated a = null;
        JavaType declaredType = propertyBuilder.getTypeFactory().constructType(DummyClass.class);

        JavaType serializationType = propertyBuilder.findSerializationType(a, false, declaredType);
        assertEquals(declaredType, serializationType);

        JsonInclude annotation = new JsonInclude();
        annotation.include(JsonInclude.Include.NON_DEFAULT);
        AnnotationInstance annotationInstance = propertyBuilder._annotationIntrospector.findAnnotationInstance(a, JsonSerialize.class);
        annotationInstance.setValue(new JsonSerialize(JsonSerialize.Typing.STATIC));
        a = propertyBuilder._beanDesc.findProperty("nonDefault");

        serializationType = propertyBuilder.findSerializationType(a, false, declaredType);
        assertNotNull(serializationType);
        assertEquals(DummyClassNonDefault.class, serializationType.getRawClass());

        annotationInstance.setValue(null);
        serializationType = propertyBuilder.findSerializationType(a, false, declaredType);
        assertNull(serializationType);

        annotationInstance.setValue(new JsonSerialize(JsonSerialize.Typing.DEFAULT_TYPING));
        serializationType = propertyBuilder.findSerializationType(a, false, declaredType);
        assertNull(serializationType);

        annotationInstance.setValue(new JsonSerialize(JsonSerialize.Typing.DYNATIC));
        serializationType = propertyBuilder.findSerializationType(a, false, declaredType);
        assertNull(serializationType);

        annotationInstance.setValue(new JsonInclude(JsonInclude.Include.NON_DEFAULT));
        serializationType = propertyBuilder.findSerializationType(a, true, declaredType);
        assertNotNull(serializationType);
        assertEquals(DummyClassNonDefault.class, serializationType.getRawClass());
    }

    @Test
    public void testGetPropertyDefaultValue() throws JsonMappingException {
        Object defaultBean = propertyBuilder.getDefaultBean();
        assertNull(defaultBean);

        defaultBean = propertyBuilder.getPropertyDefaultValue("name", null, propertyBuilder.getTypeFactory().constructType(String.class));
        assertNull(defaultBean);

        defaultBean = propertyBuilder.getPropertyDefaultValue("nonDefault", null, propertyBuilder.getTypeFactory().constructType(DummyClass.class));
        assertNotNull(defaultBean);
        assertEquals("default", defaultBean.toString());
    }

    @Test
    public void testGetDefaultValue() throws JsonMappingException {
        Object defaultValue = propertyBuilder.getDefaultValue(propertyBuilder.getTypeFactory().constructType(String.class));
        assertNull(defaultValue);

        defaultValue = propertyBuilder.getDefaultValue(propertyBuilder.getTypeFactory().constructType(DummyClass.class));
        assertNotNull(defaultValue);
        assertTrue(defaultValue instanceof JsonInclude.Include);
        assertEquals(JsonInclude.Include.NON_EMPTY, defaultValue);
    }

    @Test
    public void test_throwWrapped() throws JsonMappingException {
        Exception e = new Exception("Test Exception");
        String propName = "name";
        Object defaultBean = new Object();

        Object thrownObject = propertyBuilder._throwWrapped(e, propName, defaultBean);
        assertNull(thrownObject);

        throw new IllegalArgumentException("Failed to get property 'name' of default Object instance");
    }
}

class DummyClass {
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String name;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private DummyClassNonDefault nonDefault;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DummyClassNonDefault getNonDefault() {
        return nonDefault;
    }

    public void setNonDefault(DummyClassNonDefault nonDefault) {
        this.nonDefault = nonDefault;
    }
}

class DummyClassNonDefault {
    private String nonDefault;

    public String getNonDefault() {
        return nonDefault;
    }

    public void setNonDefault(String nonDefault) {
        this.nonDefault = nonDefault;
    }

    public String defaultValue() {
        return "default";
    }
}