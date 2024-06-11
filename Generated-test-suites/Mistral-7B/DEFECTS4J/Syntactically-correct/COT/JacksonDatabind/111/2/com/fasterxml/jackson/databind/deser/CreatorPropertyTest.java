package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.StandardDeserializationContext;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class CreatorPropertyTest {

    @SuppressWarnings("unchecked")
    private static <T> T newInstance(Class<T> clazz) throws InstantiationException, IllegalAccessException,
            InvocationTargetException {
        Constructor<T> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        return constructor.newInstance();
    }

    @JsonDeserialize(using = CreatorProperty.class)
    static class TestBean {
        @JsonProperty("name")
        private String name;

        @JsonCreator
        public TestBean(@JsonProperty("name") String name) {
            this.name = name;
        }

        @JsonProperty("getId")
        private long id;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }
    }

    @JsonDeserialize(using = CreatorProperty.class)
    static class TestBeanWithInjectedId {
        @JsonProperty("name")
        private String name;

        @JsonProperty("id")
        private Long id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @JsonProperty("id")
        public Long getInjectedId() {
            return id;
        }

        @JsonCreator
        public TestBeanWithInjectedId(@JsonProperty("name") String name, @JsonProperty("injectedId") Long id) {
            this.name = name;
            this.id = id;
        }

        public void setInjectedId(Long id) {
            this.id = id;
        }
    }

    @JsonDeserialize(using = CreatorProperty.class)
    static class TestBeanWithFallbackSetter {
        @JsonProperty("name")
        private String name;

        @JsonProperty("value")
        private int value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public void setValueFromFallback(int value) {
            this.value = value;
        }

        @JsonCreator
        public TestBeanWithFallbackSetter(@JsonProperty("name") String name) {
            this.name = name;
        }

        public void setValueFromCreator(@JsonProperty("value") int value) {
            this.value = value;
        }
    }

    @Test
    public void testCreatorPropertyBasic() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\":\"John\"}";
        TestBean bean = mapper.readValue(json, TestBean.class);
        assertEquals("John", bean.getName());
    }

    @Test
    public void testCreatorPropertyInjectValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\":\"John\", \"injectedId\":1}";
        TestBeanWithInjectedId bean = mapper.readValue(json, TestBeanWithInjectedId.class);
        assertEquals("John", bean.getName());
        assertEquals(1, bean.getId());
    }

    @Test
    public void testCreatorPropertyFallbackSetter() throws InstantiationException,
            IllegalAccessException, InvocationTargetException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\":\"John\", \"value\":42}";
        TestBeanWithFallbackSetter bean = mapper.readValue(json, TestBeanWithFallbackSetter.class);
        assertEquals("John", bean.getName());
        assertEquals(42, bean.getValue());

        TestBeanWithFallbackSetter beanWithSetter = newInstance(TestBeanWithFallbackSetter.class);
        beanWithSetter.setValueFromFallback(10);
        assertEquals(10, beanWithSetter.getValue());
        beanWithSetter.setValueFromCreator(20);
        assertEquals(20, beanWithSetter.getValue());
    }

    @Test(expected = JsonMappingException.class)
    public void testCreatorPropertyMissingSetter() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\":\"John\", \"value\":42}";
        TestBeanWithFallbackSetter bean = mapper.readValue(json, TestBeanWithFallbackSetter.class);
    }

    @Test(expected = JsonMappingException.class)
    public void testCreatorPropertyNullValueDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_empty_BEANS, false);
        StandardDeserializationContext ctxt = new StandardDeserializationContext();
        PropertyName name = PropertyName.construct("name");
        JavaType type = TypeFactory.defaultInstance().constructType(TestBean.class);

        CreatorProperty creatorProperty = new CreatorProperty(name, type, null, null, null, null, 0, null, null);
        JsonDeserializer<?> deserializer = null;
        creatorProperty.setValueDeserializer(deserializer);
        creatorProperty.deserializeAndSet(null, ctxt, null);
    }
}