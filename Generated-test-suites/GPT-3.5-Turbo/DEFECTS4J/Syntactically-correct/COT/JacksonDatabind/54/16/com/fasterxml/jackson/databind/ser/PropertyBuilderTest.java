
package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanDescription;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.impl.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ArrayBuilders;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.NameTransformer;
import com.fasterxml.jackson.databind.util.SerializerProvider;
import com.fasterxml.jackson.databind.util.TypeFactory;

public class PropertyBuilderTest {

    private PropertyBuilder propertyBuilder;

    @Before
    public void setUp() {
        SerializationConfig config = null;
        BeanDescription beanDesc = null;
        propertyBuilder = new PropertyBuilder(config, beanDesc);
    }

    @Test
    public void testGetClassAnnotations() {
        Annotations annotations = propertyBuilder.getClassAnnotations();
        assertNotNull(annotations);
    }

    @Test
    public void testBuildWriter() throws Exception {
        SerializerProvider prov = null;
        BeanPropertyDefinition propDef = null;
        JavaType declaredType = null;
        JsonSerializer<?> ser = null;
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = null;
        AnnotatedMember am = null;
        boolean defaultUseStaticTyping = false;

        BeanPropertyWriter writer = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer,
                contentTypeSer, am, defaultUseStaticTyping);
        assertNotNull(writer);
    }

    @Test
    public void testFindSerializationType() throws Exception {
        Annotated a = null;
        boolean useStaticTyping = false;
        JavaType declaredType = null;

        JavaType type = propertyBuilder.findSerializationType(a, useStaticTyping, declaredType);
        assertNull(type);
    }

    @Test
    public void testGetPropertyDefaultValue() {
        String name = "";
        AnnotatedMember member = null;
        JavaType type = null;

        Object defaultValue = propertyBuilder.getPropertyDefaultValue(name, member, type);
        assertNull(defaultValue);
    }

    @Test
    public void testGetDefaultValue() {
        JavaType type = null;

        Object defaultValue = propertyBuilder.getDefaultValue(type);
        assertNull(defaultValue);
    }

    @Test
    public void testThrowWrapped() {
        Exception e = new Exception();
        String propName = "";
        Object defaultBean = null;

        try {
            propertyBuilder._throwWrapped(e, propName, defaultBean);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertEquals("Failed to get property '' of default null instance", ex.getMessage());
        }
    }
}
