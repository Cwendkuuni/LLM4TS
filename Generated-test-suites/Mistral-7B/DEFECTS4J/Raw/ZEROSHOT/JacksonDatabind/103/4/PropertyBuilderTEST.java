package com.fasterxml.jackson.databind.ser.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.lang.reflect.Method;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize.Typing;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDescription;
import com.fasterxml.jackson.databind.introspect.BeanPropertyWriter;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.ObjectBuilder;
import com.fasterxml.jackson.databind.util.StdConverter;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for PropertyBuilder.
 */
public class PropertyBuilderTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() throws Exception {
        SimpleModule module = new SimpleModule();
        module.addSerializer(String.class, new StringSerializer());
        ObjectMapper om = new ObjectMapper();
        om.registerModule(module);
        this.objectMapper = om;
    }

    @Test
    public void testBuilder() throws Exception {
        ObjectNode configNode = objectMapper.createObjectNode();
        configNode.put("serializationInclusion", JsonInclude.Value.findDefault());
        SerializationConfig config = objectMapper.getSerializationConfig();
        BeanDescription beanDesc = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        assertNotNull(propertyBuilder.getClassAnnotations());
        assertEquals(JsonInclude.Value.empty(), propertyBuilder.getClassAnnotations().getAnnotation(JsonInclude.class));
    }

    @Test
    public void testBuildWriter() throws Exception {
        class TestBean {
            private String stringProperty;
            private int intProperty;

            @JsonSerialize(using = StringSerializer.class, typing = JsonSerialize.Typing.STATIC)
            public String getStringProperty() {
                return stringProperty;
            }

            @JsonInclude(JsonInclude.Include.NON_DEFAULT)
            public int getIntProperty() {
                return intProperty;
            }

            public int getDefaultIntProperty() {
                return 42;
            }
        }

        class TestBeanBuilder implements ObjectBuilder<TestBean> {
            private String stringValue;
            private int intValue;

            public TestBeanBuilder() {
                this.stringValue = "test";
                this.intValue = 1;
            }

            @Override
            public TestBean build() {
                TestBean testBean = new TestBean();
                Method setter = TestBean.class.getDeclaredMethod("setStringProperty", String.class);
                setter.setAccessible(true);
                setter.invoke(testBean, stringValue);
                setter.setAccessible(false);
                Method getter = TestBean.class.getDeclaredMethod("getIntProperty");
                getter.setAccessible(true);
                intProperty = getter.getInt(testBean);
                getter.setAccessible(false);
                return testBean;
            }
        }

        ObjectNode configNode = objectMapper.createObjectNode();
        configNode.put("serializationInclusion", JsonInclude.Value.findDefault());
        SerializationConfig config = objectMapper.getSerializationConfig();
        BeanPropertyDescription beanPropertyDescription = new BeanPropertyDescription(
                TestBean.class, "stringProperty");
        ObjectBuilder<TestBean> objectBuilder = new TestBeanBuilder();
        JavaType declaredType = objectMapper.getTypeFactory().constructType(TestBean.class);
        JsonSerializer<String> serializer = new StringSerializer();
        TypeSerializer typeSerializer = null;
        AnnotatedMember am = BeanPropertyDescription.findGetter(beanPropertyDescription);
        boolean defaultUseStaticTyping = false;
        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(new TestSerializerProvider(),
                beanPropertyDescription, declaredType, serializer, typeSerializer, null, am,
                defaultUseStaticTyping);

        assertNotNull(beanPropertyWriter);
    }

    static class StringSerializer extends StdSerializer<String> {
        @Override
        public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException {
            jgen.writeString(value);
        }
    }

    static class TestSerializerProvider extends SerializerProvider {
        @Override
        public JsonSerializer<?> findValueSerializer(Class<?> type, JsonSerializer<?> serializer) {
            return serializer;
        }
    }
}

class TestSerializerProvider extends SerializerProvider {

    @Override
    public JsonSerializer<?> findValueSerializer(Class<?> type, JsonSerializer<?> serializer) {
        return serializer;
    }
}