package com.fasterxml.jackson.databind.ser.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.BeanPropertyWriter;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class PropertyBuilderTest {

    @Test
    public void testPropertyBuilder() throws Exception {
        SerializationConfig config = new SerializationConfig();
        ObjectNode node = ObjectMapper.defaultInstance().createObjectNode();
        BeanDescription beanDesc = new BeanDescription(NodeTestClass.class, NodeTestClass.class, null, null, null, null, null, null, null, null, new AnnotationIntrospector());
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        // Test getClassAnnotations()
        Annotations classAnnotations = propertyBuilder.getClassAnnotations();
        Assert.assertNotNull(classAnnotations);
        Assert.assertEquals(classAnnotations.size(), 1);
        Assert.assertEquals(classAnnotations.getClass(), NodeTestClass.class.getAnnotation(JsonSerialize.class).annotationType());

        // Test buildWriter()
        Method getValue = NodeTestClass.class.getMethod("getValue");
        AnnotatedMember am = new AnnotatedElement(){
            @Override
            public AnnotatedElement getMember() {
                return getValue;
            }

            @Override
            public Class<?> getType() {
                return getValue.getReturnType();
            }
        };
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(getPropertyName(getValue), null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, false, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        SerializerProvider prov = new SerializerProvider();
        JsonSerializer<?> ser = new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, JsonGenerator jg, SerializerProvider prov) throws IOException {
            }
        };
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = new TestTypeSerializer();
        JavaType declaredType = ObjectTypeFactory.defaultInstance().constructType(getValue.getReturnType());
        List<String> expectedPropertyNames = Arrays.asList("name", "list", "map", "nested");
        for (String propertyName : expectedPropertyNames) {
            Object propertyValue = getValue.invoke(new NodeTestClass());
            BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);
            Assert.assertEquals(bpw.getName(), propertyName);
            Assert.assertNull(bpw.getSerializer());
            Assert.assertEquals(bpw.getValue(), propertyValue);
        }

        // Test findSerializationType()
        Annotated ann = new AnnotatedElement(){
            @Override
            public AnnotatedElement getMember() {
                return null;
            }

            @Override
            public Class<?> getType() {
                return NodeTestClass.class;
            }
        };
        JavaType expectedType = ObjectTypeFactory.defaultInstance().constructType(String[].class);
        JavaType actualType = propertyBuilder.findSerializationType(ann, false, declaredType);
        Assert.assertNull(actualType);
        actualType = propertyBuilder.findSerializationType(ann, true, declaredType);
        Assert.assertEquals(actualType, expectedType);

        // Test getPropertyDefaultValue()
        String propertyName = "name";
        Object defaultBean = new NodeTestClass();
        Object defaultValue = propertyBuilder.getPropertyDefaultValue(propertyName, am, declaredType);
        Assert.assertNull(defaultValue);
        defaultBean = new NodeTestClass("defaultValue");
        defaultValue = propertyBuilder.getPropertyDefaultValue(propertyName, am, declaredType);
        Assert.assertEquals(defaultValue, "defaultValue");

        // Test getDefaultValue()
        JavaType intType = ObjectTypeFactory.defaultInstance().constructType(int.class);
        JavaType intWrapperType = ObjectTypeFactory.defaultInstance().constructType(Integer.class);
        JavaType stringType = ObjectTypeFactory.defaultInstance().constructType(String.class);
        JavaType mapType = new TypeFactory().constructMapType(Map.class, String.class, Integer.class);
        JavaType listType = ObjectTypeFactory.defaultInstance().constructType(List.class, String.class);
        Object intDefault = propertyBuilder.getDefaultValue(intType);
        Assert.assertEquals(intDefault, 0);
        Object intWrapperDefault = propertyBuilder.getDefaultValue(intWrapperType);
        Assert.assertEquals(intWrapperDefault, 0);
        Object stringDefault = propertyBuilder.getDefaultValue(stringType);
        Assert.assertEquals(stringDefault, "");
        Object mapDefault = propertyBuilder.getDefaultValue(mapType);
        Assert.assertEquals(mapDefault, JsonInclude.Include.NON_EMPTY);
        Object listDefault = propertyBuilder.getDefaultValue(listType);
        Assert.assertEquals(listDefault, JsonInclude.Include.NON_EMPTY);
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    static class NodeTestClass {
        private String name;
        private String[] list;
        private Map<String, Integer> map;
        private Nested nested;

        public String getValue() {
            return name;
        }
    }

    static class Nested {

    }

    static class TestTypeSerializer extends TypeSerializer {
        @Override
        public void serialize(SerializerProvider prov, TypeSerializer contentTypeSer, Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeValue(value);
        }

        @Override
        public Object deserialize(DeserializationContext ctxt, BeanProperty property, JsonParser p, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            return p.getValueAs(String[].class);
        }
    }

    private static String getPropertyName(Method method) {
        return method.getName().substring(3);
    }
}

class ObjectTypeFactory {
    public static ObjectTypeFactory defaultInstance() {
        return new ObjectTypeFactory() {
            @Override
            public JavaType constructType(Class<?> type) {
                return null;
            }
        };
    }

    public abstract JavaType constructType(Class<?> type);
}