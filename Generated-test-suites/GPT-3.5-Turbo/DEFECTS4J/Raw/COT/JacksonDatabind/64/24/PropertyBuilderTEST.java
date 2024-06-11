
package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

    @Test
    public void getClassAnnotationsTest() {
        PropertyBuilder propertyBuilder = createPropertyBuilder();
        Annotations annotations = propertyBuilder.getClassAnnotations();
        assertNotNull(annotations);
    }

    @Test
    public void buildWriterTest() throws JsonMappingException {
        PropertyBuilder propertyBuilder = createPropertyBuilder();
        SerializerProvider provider = createSerializerProvider();
        BeanPropertyDefinition propDef = createBeanPropertyDefinition();
        JavaType declaredType = createJavaType();
        JsonSerializer ser = createJsonSerializer();
        TypeSerializer typeSer = createTypeSerializer();
        TypeSerializer contentTypeSer = createTypeSerializer();
        AnnotatedMember am = createAnnotatedMember();
        boolean defaultUseStaticTyping = true;
        BeanPropertyWriter writer = propertyBuilder.buildWriter(provider, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        assertNotNull(writer);
    }

    private PropertyBuilder createPropertyBuilder() {
        return new PropertyBuilder(createSerializationConfig(), createBeanDescription());
    }

    private SerializationConfig createSerializationConfig() {
        return new SerializationConfig(null, null, null);
    }

    private BeanDescription createBeanDescription() {
        return new BeanDescription(createMapperConfig(), null, null);
    }

    private MapperConfig createMapperConfig() {
        return new SerializationConfig(null, null, null);
    }

    private SerializerProvider createSerializerProvider() {
        return new SerializerProvider();
    }

    private BeanPropertyDefinition createBeanPropertyDefinition() {
        return new BeanPropertyDefinition(null, null, null, null, null);
    }

    private JavaType createJavaType() {
        return new JavaType(null, null, null, null, null, false, null, null);
    }

    private JsonSerializer createJsonSerializer() {
        return null;
    }

    private TypeSerializer createTypeSerializer() {
        return null;
    }

    private AnnotatedMember createAnnotatedMember() {
        return null;
    }

    private TypeSerializer createTypeSerializer() {
        return null;
    }
}

