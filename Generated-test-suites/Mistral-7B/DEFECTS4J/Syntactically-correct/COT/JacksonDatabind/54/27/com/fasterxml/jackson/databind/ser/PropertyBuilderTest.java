package com.fasterxml.jackson.databind.ser;

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

    @Before
    public void setup() {
        ObjectMapper mapper = new ObjectMapper();
        config = mapper.getSerializationConfig();
        beanDesc = mapper.getTypeFactory().constructType(String.class);
    }

    @Test
    public void testPropertyBuilderConstructor() {
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
    public void testBuildWriter_NullSerializer() {
        SerializerProvider prov = new StandardSerializerProvider();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        try {
            propertyBuilder.buildWriter(prov, null, null, null, null, null, null, false);
            fail("IllegalArgumentException expected");
        } catch (JsonMappingException e) {
            assertEquals("Null serializer specified", e.getMessage());
        }
    }

    @Test
    public void testBuildWriter() {
        SerializerProvider prov = new StandardSerializerProvider();
        JsonSerializer<String> ser = new StandardSerializer<>(String.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        BeanPropertyWriter writer = propertyBuilder.buildWriter(prov, null, beanDesc.getRawClass(), ser, null, null, null, false);
        assertNotNull(writer);
    }

    @Test
    public void testFindSerializationType() {
        Annotated a = mock(Annotated.class);
        when(a.getType()).thenReturn(beanDesc.getType());
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        JavaType type = propertyBuilder.findSerializationType(a, false, beanDesc.getType());
        assertEquals(beanDesc.getType(), type);

        when(a.getType()).thenReturn(new TypeFactory().constructType(Map.class));
        type = propertyBuilder.findSerializationType(a, false, beanDesc.getType());
        assertNull(type);

        when(a.getType()).thenReturn(new TypeFactory().constructType(Map.class, beanDesc.getType()));
        JavaType serializationType = propertyBuilder.findSerializationType(a, true, beanDesc.getType());
        assertEquals(Map.class, serializationType.getRawClass());
    }

    @Test
    public void testGetDefaultBean() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Object defaultBean = propertyBuilder.getDefaultBean();
        assertNull(defaultBean);
    }

    @Test
    public void testGetPropertyDefaultValue() {
        AnnotatedMember member = mock(AnnotatedMember.class);
        when(member.getName()).thenReturn("test");
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Object defaultBean = new Object();
        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue("test", member, beanDesc.getType());
        assertNull(propertyDefaultValue);

        when(member.getValue(defaultBean)).thenReturn("defaultValue");
        propertyDefaultValue = propertyBuilder.getPropertyDefaultValue("test", member, beanDesc.getType());
        assertEquals("defaultValue", propertyDefaultValue);
    }

    @Test
    public void testGetDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Object defaultValue = propertyBuilder.getDefaultValue(beanDesc.getType());
        assertNull(defaultValue);

        JavaType stringType = new TypeFactory().constructType(String[].class);
        defaultValue = propertyBuilder.getDefaultValue(stringType);
        assertEquals(JsonInclude.Include.NON_EMPTY, defaultValue);
    }
}