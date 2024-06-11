package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

    private PropertyBuilder propertyBuilder;
    private SerializerProvider serializerProvider;
    private BeanPropertyDefinition propDef;
    private JavaType declaredType;
    private JsonSerializer<?> ser;
    private TypeSerializer typeSer;
    private TypeSerializer contentTypeSer;
    private AnnotatedMember am;
    private boolean defaultUseStaticTyping;

    @Before
    public void setUp() {
        propertyBuilder = new PropertyBuilder(serializerConfig, beanDesc);
        serializerProvider = new SerializerProvider();
        propDef = new BeanPropertyDefinition();
        declaredType = new JavaType();
        ser = new JsonSerializer<>();
        typeSer = new TypeSerializer();
        contentTypeSer = new TypeSerializer();
        am = new AnnotatedMember();
        defaultUseStaticTyping = true;
    }

    @Test
    public void testBuildWriter() {
        try {
            BeanPropertyWriter writer = propertyBuilder.buildWriter(
                    serializerProvider, propDef, declaredType, ser,
                    typeSer, contentTypeSer, am, defaultUseStaticTyping
            );
            assertNotNull(writer);
        } catch (JsonMappingException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetClassAnnotations() {
        Annotations annotations = propertyBuilder.getClassAnnotations();
        assertNotNull(annotations);
    }

    @Test
    public void testFindSerializationType() {
        try {
            JavaType serializationType = propertyBuilder.findSerializationType(
                    am, defaultUseStaticTyping, declaredType
            );
            assertNull(serializationType);
        } catch (JsonMappingException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetPropertyDefaultValue() {
        Object defaultValue = propertyBuilder.getPropertyDefaultValue(
                "testProperty", am, declaredType
        );
        assertNull(defaultValue);
    }

    @Test
    public void testGetDefaultValue() {
        Object defaultValue = propertyBuilder.getDefaultValue(declaredType);
        assertNull(defaultValue);
    }

}