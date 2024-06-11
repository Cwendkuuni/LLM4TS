package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.StringKeySerializer;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PropertyBuilderTest {

    private SerializationConfig config;
    private BeanDescription beanDesc;
    private PropertyBuilder propertyBuilder;

    @Before
    public void setUp() {
        config = new ObjectMapper().getSerializationConfig();
        beanDesc = new BeanDescription(config);
        propertyBuilder = new PropertyBuilder(config, beanDesc);
    }

    @Test
    public void testBuildWriter() {
        SerializerProvider prov = new SerializerProvider();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType declaredType = new JavaType();
        JsonSerializer<?> ser = new DateSerializer();
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = null;
        AnnotatedMember am = new AnnotatedField();
        boolean defaultUseStaticTyping = true;
        
        BeanPropertyWriter writer = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        
        assertNotNull(writer);
        assertEquals(propDef, writer.getPropertyDefinition());
        assertEquals(am, writer.getMember());
        assertEquals(declaredType, writer.getDeclaringType());
        assertEquals(ser, writer.getSerializer());
        assertEquals(typeSer, writer.getTypeSerializer());
        assertEquals(contentTypeSer, writer.getContentTypeSerializer());
        assertEquals(defaultUseStaticTyping, writer.getUseStaticTyping());
    }
}