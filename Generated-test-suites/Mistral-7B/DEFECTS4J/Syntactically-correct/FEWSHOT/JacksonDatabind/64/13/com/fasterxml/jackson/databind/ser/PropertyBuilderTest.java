package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.BeanPropertyWriter;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MappingWriter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.BeanPropertyWriter.Markers;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

    private ObjectMapper objectMapper;
    private SerializationConfig config;
    private PropertyBuilder propertyBuilder;
    private BeanDescription beanDescription;

    @Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        config = objectMapper.getSerializationConfig();
        beanDescription = objectMapper.findTypeDescriptor(Person.class);
        propertyBuilder = new PropertyBuilder(config, beanDescription);
    }

    @After
    public void tearDown() throws Exception {
        objectMapper = null;
        config = null;
        propertyBuilder = null;
        beanDescription = null;
    }

    @Test
    public void testBuildWriterWithEmptyType() throws JsonMappingException {
        JavaType javaType = null;

        BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, javaType, null, null, null, null, false);
        assertNotNull(writer);
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        SerializerProvider prov = null;
        BeanPropertyDefinition propDef = null;
        JavaType declaredType = null;
        JsonSerializer<?> ser = null;
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = null;
        AnnotatedMember am = null;
        boolean defaultUseStaticTyping = false;

        JavaType serializationType = propertyBuilder.findSerializationType(am, defaultUseStaticTyping, declaredType);

        BeanPropertyWriter writer = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        assertNotNull(writer);
    }

    @Test
    public void testFindSerializationType() throws JsonMappingException {
        AnnotatedMember am = null;
        boolean useStaticTyping = false;
        JavaType declaredType = null;

        JavaType serializationType = propertyBuilder.findSerializationType(am, useStaticTyping, declaredType);
        assertNull(serializationType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindSerializationTypeWithInvalidType() throws JsonMappingException {
        AnnotatedMember am = null;
        boolean useStaticTyping = false;
        JavaType declaredType = null;

        JavaType serializationType = propertyBuilder.findSerializationType(am, useStaticTyping, declaredType);
        fail("Should have thrown IllegalArgumentException");
    }

    @Test
    public void testGetDefaultBean() throws Exception {
        Object defaultBean = propertyBuilder.getDefaultBean();
        assertNull(defaultBean);
    }

    @Test
    public void testGetPropertyDefaultValue() throws Exception {
        String name = "name";
        AnnotatedMember member = null;
        JavaType type = null;

        Object defaultValue = propertyBuilder.getPropertyDefaultValue(name, member, type);
        if (type.isContainerType() || type.isReferenceType()) {
            assertEquals(JsonInclude.Include.NON_EMPTY, defaultValue);
        } else {
            assertNull(defaultValue);
        }
    }

    @Test
    public void testGetDefaultValue() throws Exception {
        JavaType type = null;

        Object defaultValue = propertyBuilder.getDefaultValue(type);
        if (type.isContainerType() || type.isReferenceType()) {
            assertEquals(JsonInclude.Include.NON_EMPTY, defaultValue);
        } else {
            assertNull(defaultValue);
        }
    }

    @Test
    public void testGetClassAnnotations() throws Exception {
        Annotations classAnnotations = propertyBuilder.getClassAnnotations();
        assertTrue(classAnnotations.size() > 0);
    }

    @Test
    public void test_useRealPropertyDefaults() throws Exception {
        // Add a property with JsonInclude.Include.NON_DEFAULT annotation
        JavaType propertyType = JavaTypeFactory.constructSimpleType(Person.class, "property1", String.class);
        propertyBuilder._beanDesc.findPropertyDefinition("property1").withAnnotation(new JsonInclude(JsonInclude.Include.NON_DEFAULT));

        // Test _useRealPropertyDefaults is set to true
        Object defaultBean = propertyBuilder.getDefaultBean();
        assertNull(defaultBean);
        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue("property1", null, propertyType);
        assertEquals("", propertyDefaultValue);

        // Test _useRealPropertyDefaults is set to false
        propertyBuilder = new PropertyBuilder(config, beanDescription);
        Object defaultBean2 = propertyBuilder.getDefaultBean();
        assertNotNull(defaultBean2);
        Object propertyDefaultValue2 = propertyBuilder.getPropertyDefaultValue("property1", null, propertyType);
        assertNull(propertyDefaultValue2);
    }

    static class Person {
        private String name;

        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        public String getProperty1() {
            return "defaultValue";
        }

        @JsonSerialize(using = ToStringSerializer.class)
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

    static class ToStringSerializer extends JsonSerializer<Object> {
        @Override
        public void serialize(Object value, MappingWriter writer, SerializerProvider provider) throws IOException {
            writer.writeValueAsString(value.toString());
        }
    }
}