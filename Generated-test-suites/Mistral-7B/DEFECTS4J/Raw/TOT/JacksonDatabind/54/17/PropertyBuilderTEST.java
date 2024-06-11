import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.BeanPropertyWriterImpl;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBase;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.Converter.StdConverter;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PropertyBuilderTest {

    @Test
    public void testBuilderWithDefaultInclusion() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        SerializationConfig config = objectMapper.getSerializationConfig();
        BeanDescription beanDesc = new BeanDescription(MyClass.class, null);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        // Test with default inclusion
        PropertyWriter writer = propertyBuilder.buildWriter(new DefaultSerializerProvider(), null, null, null, null, null, null, false);
        Assert.assertNotNull(writer);
    }

    @Test
    public void testBuilderWithCustomInclusion() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        SerializationConfig config = objectMapper.getSerializationConfig();
        config.setDefaultPropertyInclusion(JsonInclude.Include.NON_DEFAULT);
        BeanDescription beanDesc = new BeanDescription(MyClass.class, null);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        // Test with custom inclusion
        AnnotatedMember am = new AnnotatedMember(MyClass.class, "customProperty");
        PropertyWriter writer = propertyBuilder.buildWriter(new DefaultSerializerProvider(), null, javaTypeOf(MyClass.class), null, null, null, am, false);
        Object valueToSuppress = propertyBuilder.getPropertyDefaultValue("customProperty", am, javaTypeOf(MyClass.class));
        Assert.assertNotNull(valueToSuppress);
    }

    @Test
    public void testBuilderWithNullValues() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        SerializationConfig config = objectMapper.getSerializationConfig();
        config.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        BeanDescription beanDesc = new BeanDescription(MyClass.class, null);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        // Test with null values
        AnnotatedMethod method = new AnnotatedMethod(MyClass.class, "getNullProperty", null);
        PropertyWriter writer = propertyBuilder.buildWriter(new DefaultSerializerProvider(), new BeanPropertyDefinition(method), null, null, null, null, null, false);
        Assert.assertFalse(writer.isWriteNullValue());
    }

    @Test
    public void testBuilderWithEmptyCollection() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        SerializationConfig config = objectMapper.getSerializationConfig();
        config.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        BeanDescription beanDesc = new BeanDescription(MyClass.class, null);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        // Test with empty collection
        AnnotatedMethod method = new AnnotatedMethod(MyClass.class, "getEmptyListProperty", null);
        PropertyWriter writer = propertyBuilder.buildWriter(new DefaultSerializerProvider(), new BeanPropertyDefinition(method), javaTypeOf(List.class), null, null, null, null, false);
        Assert.assertFalse(writer.isWriteNullValue());
        Assert.assertNotNull(writer.getValueSerializer());
    }

    @Test
    public void testBuilderWithUnwrapping() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        SerializationConfig config = objectMapper.getSerializationConfig();
        BeanDescription beanDesc = new BeanDescription(MyClass.class, null);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        // Test with unwrapping
        AnnotatedMember am = new AnnotatedMember(MyClass.class, "wrappedProperty");
        NameTransformer unwrapper = new NameTransformer() {
            @Override
            public String transform(String name, JavaType secondaryType) {
                return "unwrapped_" + name;
            }
        };
        PropertyWriter writer = propertyBuilder.buildWriter(new DefaultSerializerProvider(), null, null, null, null, null, am, false).unwrappingWriter(unwrapper);
        Assert.assertNotNull(writer);
    }

    @Test
    public void testBuilderWithCustomNullSerializer() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        SerializationConfig config = objectMapper.getSerializationConfig();
        BeanDescription beanDesc = new BeanDescription(MyClass.class, null);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        // Test with custom null serializer
        AnnotatedMember am = new AnnotatedMember(MyClass.class, "nullProperty");
        Object serializerDef = new SerializerProvider.SerializerProvider.SerializerProviderAnnotatedClass(MyNullSerializer.class, null);
        PropertyWriter writer = propertyBuilder.buildWriter(new DefaultSerializerProvider(), null, null, null, null, null, am, false).assignNullSerializer(new DefaultSerializerProvider().serializerInstance(am, serializerDef));
        Assert.assertNotNull(writer);
    }

    @Test
    public void testSerialization() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        MyClass obj = new MyClass();
        obj.setStringProperty("test");
        obj.setIntProperty(1);
        obj.setListProperty(Arrays.asList(1, 2, 3));
        String json = objectMapper.writeValueAsString(obj);
        MyClass deserializedObj = objectMapper.readValue(json, MyClass.class);

        Assert.assertEquals(obj.getStringProperty(), deserializedObj.getStringProperty());
        Assert.assertEquals(obj.getIntProperty(), deserializedObj.getIntProperty());
        Assert.assertEquals(obj.getListProperty(), deserializedObj.getListProperty());
    }

    @Test
    public void testSerializationWithEmpties() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        MyClass obj = new MyClass();
        String json = objectMapper.writeValueAsString(obj);
        MyClass deserializedObj = objectMapper.readValue(json, MyClass.class);

        // Test empty string property
        Assert.assertNull(obj.getStringProperty());
        Assert.assertNull(deserializedObj.getStringProperty());

        // Test empty list property
        Assert.assertNotNull(obj.getListProperty());
        Assert.assertNotNull(deserializedObj.getListProperty());
        Assert.assertTrue(deserializedObj.getListProperty().isEmpty());
    }

    @Test
    public void testSerializationWithNulls() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        MyClass obj = new MyClass();
        obj.setStringProperty(null);
        obj.setIntProperty(null);
        obj.setListProperty(null);
        String json = objectMapper.writeValueAsString(obj);
        MyClass deserializedObj = objectMapper.readValue(json, MyClass.class);

        // Test null string property
        Assert.assertNull(obj.getStringProperty());
        Assert.assertNull(deserializedObj.getStringProperty());

        // Test null int property
        Assert.assertNull(obj.getIntProperty());
        Assert.assertNull(deserializedObj.getIntProperty());

        // Test null list property
        Assert.assertNull(obj.getListProperty());
        Assert.assertNull(deserializedObj.getListProperty());
    }

    @Test
    public void testSerializationWithCustomNullSerializer() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        MyClass obj = new MyClass();
        obj.setNullProperty(null);
        String json = objectMapper.writeValueAsString(obj);
        MyClass deserializedObj = objectMapper.readValue(json, MyClass.class);

        // Test null property with custom null serializer
        Assert.assertNull(obj.getNullProperty());
        Assert.assertNull(deserializedObj.getNullProperty());
    }

    private static JavaType javaTypeOf(Class<?> clazz) {
        return TypeFactory.defaultInstance().constructType(clazz);
    }

    static class MyClass {
        private String stringProperty;
        private Integer intProperty;
        private List<Integer> listProperty;
        private Object nullProperty;

        public String getStringProperty() {
            return stringProperty;
        }

        public void setStringProperty(String stringProperty) {
            this.stringProperty = stringProperty;
        }

        public Integer getIntProperty() {
            return intProperty;
        }

        public void setIntProperty(Integer intProperty) {
            this.intProperty = intProperty;
        }

        public List<Integer> getListProperty() {
            return listProperty;
        }

        public void setListProperty(List<Integer> listProperty) {
            this.listProperty = listProperty;
        }

        public Object getNullProperty() {
            return nullProperty;
        }

        public void setNullProperty(Object nullProperty) {
            this.nullProperty = nullProperty;
        }
    }

    static class MyNullSerializer extends StdSerializer<Object> {
        public MyNullSerializer() {
            this(null);
        }

        public MyNullSerializer(Class<Object> t) {
            this(null);
        }

        public MyNullSerializer(Class<Object> t, Converter<?> valueConverter) {
            super(Object.class, null);
        }

        @Override
        public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
            jgen.writeNull();
        }
    }
}