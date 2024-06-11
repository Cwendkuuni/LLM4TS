package com.fasterxml.jackson.databind.ser.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.TypeSerializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializerBuilder;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.test.BaseMapTest;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.ConverterUtil;

public class PropertyBuilderTest extends BaseMapTest {

    public void testPropertyBuilder() throws JsonProcessingException, IOException {
        // Test with default inclusion and no annotations
        SerializationConfig config = new SerializationConfig(new AnnotationIntrospectorStandard());
        BeanDescription beanDesc = createBeanDescription(config, DummyClass.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        Annotations classAnnotations = propertyBuilder.getClassAnnotations();
        assertNull(classAnnotations);

        SerializerProvider prov = mock(SerializerProvider.class);
        BeanPropertyDefinition propDef = propertyBuilder.buildWriter(prov, null, null, null, null, null, null, false).getBeanPropertyDefinition();
        assertEquals("defaultProperty", propDef.getName());

        // Test with JsonInclude.Include.NON_DEFAULT annotation
        DummyClassWithAnnotation annotatedClass = new DummyClassWithAnnotation();
        BeanDescription annotatedBeanDesc = createBeanDescription(config, annotatedClass.getClass());
        PropertyBuilder annotatedPropertyBuilder = new PropertyBuilder(config, annotatedBeanDesc);
        Object defaultBean = annotatedPropertyBuilder.getDefaultBean();
        assertEquals(annotatedClass, defaultBean);

        Object propertyDefaultValue = annotatedPropertyBuilder.getPropertyDefaultValue("defaultProperty", null, annotatedClass.getClass());
        assertNull(propertyDefaultValue);

        // Test with JsonInclude.Include.NON_EMPTY annotation
        DummyClassWithEmptyList emptyListClass = new DummyClassWithEmptyList();
        BeanDescription emptyListBeanDesc = createBeanDescription(config, emptyListClass.getClass());
        PropertyBuilder emptyListPropertyBuilder = new PropertyBuilder(config, emptyListBeanDesc);
        Object emptyListDefaultValue = emptyListPropertyBuilder.getDefaultValue(emptyListBeanDesc.findPropertyType("emptyList"));
        assertEquals(JsonInclude.Include.NON_EMPTY, emptyListDefaultValue);

        // Test with JsonInclude.Include.NON_NULL annotation
        DummyClassWithNullList nullListClass = new DummyClassWithNullList();
        BeanDescription nullListBeanDesc = createBeanDescription(config, nullListClass.getClass());
        PropertyBuilder nullListPropertyBuilder = new PropertyBuilder(config, nullListBeanDesc);
        Object nullListDefaultValue = nullListPropertyBuilder.getDefaultValue(nullListBeanDesc.findPropertyType("nullList"));
        assertNull(nullListDefaultValue);

        // Test with JsonInclude.Include.ALWAYS annotation
        DummyClassWithAlwaysInclude alwaysIncludeClass = new DummyClassWithAlwaysInclude();
        BeanDescription alwaysIncludeBeanDesc = createBeanDescription(config, alwaysIncludeClass.getClass());
        PropertyBuilder alwaysIncludePropertyBuilder = new PropertyBuilder(config, alwaysIncludeBeanDesc);
        Object alwaysIncludeDefaultValue = alwaysIncludePropertyBuilder.getDefaultValue(alwaysIncludeBeanDesc.findPropertyType("alwaysInclude"));
        assertNull(alwaysIncludeDefaultValue);

        // Test with JsonSerialize annotation and default value
        DummyClassWithJsonSerialize serializeClass = new DummyClassWithJsonSerialize();
        BeanDescription serializeBeanDesc = createBeanDescription(config, serializeClass.getClass());
        PropertyBuilder serializePropertyBuilder = new PropertyBuilder(config, serializeBeanDesc);
        Object serializeDefaultBean = serializePropertyBuilder.getDefaultBean();
        assertEquals(serializeClass, serializeDefaultBean);

        Object serializePropertyDefaultValue = serializePropertyBuilder.getPropertyDefaultValue("defaultProperty", null, serializeClass.getClass());
        assertEquals("defaultValue", serializePropertyDefaultValue);

        // Test with JsonSerialize annotation and custom serializer
        DummyClassWithCustomSerializer customSerializerClass = new DummyClassWithCustomSerializer();
        BeanDescription customSerializerBeanDesc = createBeanDescription(config, customSerializerClass.getClass());
        PropertyBuilder customSerializerPropertyBuilder = new PropertyBuilder(config, customSerializerBeanDesc);
        Object customSerializerDefaultBean = customSerializerPropertyBuilder.getDefaultBean();
        assertEquals(customSerializerClass, customSerializerDefaultBean);

        Object customSerializerPropertyDefaultValue = customSerializerPropertyBuilder.getPropertyDefaultValue("defaultProperty", null, customSerializerClass.getClass());
        assertEquals("customDefaultValue", customSerializerPropertyDefaultValue);

        // Test with JsonProperty annotation and custom name
        DummyClassWithJsonProperty customNameClass = new DummyClassWithJsonProperty();
        BeanDescription customNameBeanDesc = createBeanDescription(config, customNameClass.getClass());
        PropertyBuilder customNamePropertyBuilder = new PropertyBuilder(config, customNameBeanDesc);
        Object customNamePropertyDefaultValue = customNamePropertyBuilder.getPropertyDefaultValue("customName", null, customNameClass.getClass());
        assertNull(customNamePropertyDefaultValue);

        // Test with TypeSerializer annotation and custom serializer
        DummyClassWithTypeSerializer typeSerializerClass = new DummyClassWithTypeSerializer();
        BeanDescription typeSerializerBeanDesc = createBeanDescription(config, typeSerializerClass.getClass());
        PropertyBuilder typeSerializerPropertyBuilder = new PropertyBuilder(config, typeSerializerBeanDesc);
        Object typeSerializerDefaultBean = typeSerializerPropertyBuilder.getDefaultBean();
        assertEquals(typeSerializerClass, typeSerializerDefaultBean);

        TypeSerializer typeSerializer = mock(TypeSerializer.class);
        when(typeSerializer.writeValueAsBytes(prov, "test", null)).thenReturn(new byte[0]);
        JavaType typeSerializerType = TypeFactory.defaultInstance().constructType(String.class, TypeSerializerBuilder.class);
        when(typeSerializerBuilder.buildTypeSerializer(typeSerializerType, null, null)).thenReturn(typeSerializer);

        Object typeSerializerPropertyDefaultValue = typeSerializerPropertyBuilder.getPropertyDefaultValue("typeSerializerProperty", null, typeSerializerClass.getClass());
        assertEquals(typeSerializer, typeSerializerPropertyDefaultValue);

        // Test with JsonInclude.Include.NON_EMPTY and JsonInclude.Include.NON_NULL annotations
        DummyClassWithBothIncludeAnnotations includeAnnotationsClass = new DummyClassWithBothIncludeAnnotations();
        BeanDescription includeAnnotationsBeanDesc = createBeanDescription(config, includeAnnotationsClass.getClass());
        PropertyBuilder includeAnnotationsPropertyBuilder = new PropertyBuilder(config, includeAnnotationsBeanDesc);
        Object includeAnnotationsPropertyDefaultValue = includeAnnotationsPropertyBuilder.getPropertyDefaultValue("emptyList", null, includeAnnotationsClass.getClass());
        assertEquals(JsonInclude.Include.NON_EMPTY, includeAnnotationsPropertyDefaultValue);

        Object includeAnnotationsPropertyDefaultValue2 = includeAnnotationsPropertyBuilder.getPropertyDefaultValue("nullList", null, includeAnnotationsClass.getClass());
        assertNull(includeAnnotationsPropertyDefaultValue2);
    }

    private static <T> JavaType findPropertyType(BeanDescription beanDesc, String propertyName) {
        for (BeanPropertyDefinition propDef : beanDesc.findProperties()) {
            if (propDef.getName().equals(propertyName)) {
                return propDef.getType();
            }
        }
        throw new RuntimeException("Property not found: " + propertyName);
    }

    private static BeanDescription createBeanDescription(SerializationConfig config, Class<?> clazz) throws JsonMappingException {
        BeanFactory beanFactory = BeanDescription.StandardFactory.instance;
        return beanFactory.createBeanDescription(clazz, config);
    }

    private static class DummyClass {
        private String defaultProperty;
    }

    private static class DummyClassWithAnnotation {
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        private String annotatedProperty;
    }

    private static class DummyClassWithEmptyList {
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        private List emptyList;
    }

    private static class DummyClassWithNullList {
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private List nullList;
    }

    private static class DummyClassWithAlwaysInclude {
        @JsonInclude(JsonInclude.Include.ALWAYS)
        private String alwaysInclude;
    }

    private static class DummyClassWithJsonSerialize {
        @JsonSerialize(using = DummySerializer.class)
        private String jsonSerialize;

        public static class DummySerializer extends StdSerializer<String> {
            public DummySerializer() {
                this(null);
            }

            public DummySerializer(Class<String> t) {
                super(t);
            }

            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider serializers) throws IOException {
                jgen.writeString("defaultValue");
            }
        }
    }

    private static class DummyClassWithCustomSerializer {
        @JsonSerialize(using = DummyCustomSerializer.class)
        private String customSerializer;

        public static class DummyCustomSerializer extends StdSerializer<String> {
            public DummyCustomSerializer() {
                this(null);
            }

            public DummyCustomSerializer(Class<String> t) {
                super(t);
            }

            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider serializers) throws IOException {
                jgen.writeString("customDefaultValue");
            }
        }
    }

    private static class DummyClassWithJsonProperty {
        @JsonProperty("customName")
        private String jsonProperty;
    }

    private static class DummyClassWithTypeSerializer {
        @JsonSerialize(using = TypeSerializer.class)
        private TypeSerializer typeSerializerProperty;

        public static class TypeSerializer extends StdSerializer<TypeSerializerBuilder> {
            public TypeSerializer() {
                this(null);
            }

            public TypeSerializer(Class<TypeSerializerBuilder> t) {
                super(t);
            }

            @Override
            public void serialize(TypeSerializerBuilder value, JsonGenerator jgen, SerializerProvider serializers) throws IOException {
                jgen.writeObject(value);
            }
        }
    }

    private static class DummyClassWithBothIncludeAnnotations {
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private List<String> bothInclude;
    }
}