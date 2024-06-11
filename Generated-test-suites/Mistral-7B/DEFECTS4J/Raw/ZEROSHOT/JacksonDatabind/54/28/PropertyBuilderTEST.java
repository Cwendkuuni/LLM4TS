package com.fasterxml.jackson.databind.ser.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.BeanPropertyWriter;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Test class for {@link PropertyBuilder}
 */
public class PropertyBuilderTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testPropertyBuilder() throws InstantiationException, IllegalAccessException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        SerializationConfig config = mapper.getSerializationConfig();
        BeanDescription beanDesc = mapper.constructType(PropertyBuilderTest.DummyClass.class).findPropertyDescription("dummyProperty");

        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        // Test getClassAnnotations()
        Annotations classAnnotations = propertyBuilder.getClassAnnotations();
        Assert.assertNotNull(classAnnotations);
        Assert.assertEquals(classAnnotations.getClass(), PropertyBuilderTest.DummyClass.AnnotatedDummyClass.class);

        // Test buildWriter()
        SerializerProvider prov = mapper.getSerializerProvider();
        JavaType declaredType = mapper.getTypeFactory().constructType(String.class);
        JsonSerializer<String> ser = mapper.findValueSerializer(String.class);
        TypeSerializer typeSer = mapper.getSerializerProvider().findValueSerializer(String[].class);
        TypeSerializer contentTypeSer = mapper.getSerializerProvider().findValueSerializer(Integer[].class);
        BeanPropertyDefinition propDef = beanDesc.findPropertyDefinition("dummyProperty");

        BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, null, false, null);
        Assert.assertNotNull(bpw);
        Assert.assertEquals(bpw.getPropertyName(), "dummyProperty");
    }

    static class DummyClass {
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        String dummyProperty;

        static class AnnotatedDummyClass {
            @JsonProperty("test")
            @JsonSerialize(using = PropertyBuilderTest.TestSerializer.class)
            int test;
        }
    }

    static class TestSerializer extends JsonSerializer<Object> {
        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            // Do nothing
        }
    }
}