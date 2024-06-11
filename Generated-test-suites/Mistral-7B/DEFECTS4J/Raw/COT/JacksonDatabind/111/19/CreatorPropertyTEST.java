package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Annotations;

import java.io.IOException;
import java.lang.annotation.Annotation;

public class CreatorPropertyTest {

    @SuppressWarnings("unchecked")
    private static class TestBean {

        private CreatorProperty _name;
        private CreatorProperty _age;

        @JsonCreator
        public TestBean(@JsonProperty("name") String name, @JsonProperty("age") int age) {
            this._name = new CreatorProperty(PropertyName.construct("name"), String.class, null, null, null, null, -1, null, null);
            this._name.setFallbackSetter(new SettableBeanProperty(PropertyName.construct("name"), String.class, null, null, null, null, null, null, null) {
                @Override
                public void set(Object instance, Object value) throws IOException {
                    ((TestBean) instance).name = (String) value;
                }

                @Override
                public Object setAndReturn(Object instance, Object value) throws IOException {
                    ((TestBean) instance).name = (String) value;
                    return value;
                }
            });

            this._age = new CreatorProperty(PropertyName.construct("age"), Integer.TYPE, null, null, null, null, -1, null, null);
            this._age.setFallbackSetter(new SettableBeanProperty(PropertyName.construct("age"), Integer.TYPE, null, null, null, null, null, null, null) {
                @Override
                public void set(Object instance, Object value) throws IOException {
                    ((TestBean) instance).age = (int) value;
                }

                @Override
                public Object setAndReturn(Object instance, Object value) throws IOException {
                    ((TestBean) instance).age = (int) value;
                    return value;
                }
            });
        }

        String name;
        int age;

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }
    }

    @Test
    public void testCreatorPropertyBasic() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer();

        TestBean bean = new TestBean("TestName", 25);
        String json = writer.writeValueAsString(bean);

        TypeReference<TestBean> typeRef = new TypeReference<TestBean>() {};
        TestBean deserializedBean = mapper.readValue(json, typeRef);

        assertEquals(bean.getName(), deserializedBean.getName());
        assertEquals(bean.getAge(), deserializedBean.getAge());
    }

    @Test
    public void testCreatorPropertyFunctionality() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer();

        // Test name property deserialization
        CreatorProperty nameProp = new CreatorProperty(PropertyName.construct("name"), String.class, null, null, null, null, -1, null, null);
        AnnotatedParameter nameParam = AnnotatedParameter.std(String.class, 0, null);
        nameProp._annotated = nameParam;

        String json = writer.writeValueAsString(new String[]{"TestName"});
        TestBean deserializedBean = mapper.readerFor(TestBean.class).readValue(json);

        assertEquals("TestName", deserializedBean.getName().get(deserializedBean).asText());

        // Test age property deserialization
        CreatorProperty ageProp = new CreatorProperty(PropertyName.construct("age"), Integer.TYPE, null, null, null, null, -1, null, null);
        AnnotatedParameter ageParam = AnnotatedParameter.std(Integer.TYPE, 1, null);
        ageProp._annotated = ageParam;

        json = writer.writeValueAsString(new Integer[]{25});
        deserializedBean = mapper.readerFor(TestBean.class).readValue(json);

        assertEquals(25, deserializedBean.getAge().get(deserializedBean).asInt());
    }

    @Test(expected = InvalidDefinitionException.class)
    public void testMissingSetter() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer();

        // Test name property missing setter
        CreatorProperty nameProp = new CreatorProperty(PropertyName.construct("name"), String.class, null, null, null, null, -1, null, null);

        String json = writer.writeValueAsString(new String[]{"TestName"});
        TestBean deserializedBean = mapper.readerFor(TestBean.class).readValue(json);
        nameProp._verifySetter();

        // Test age property missing setter
        CreatorProperty ageProp = new CreatorProperty(PropertyName.construct("age"), Integer.TYPE, null, null, null, null, -1, null, null);

        json = writer.writeValueAsString(new Integer[]{25});
        deserializedBean = mapper.readerFor(TestBean.class).readValue(json);
        ageProp._verifySetter();

        // Test invalid type for name property
        CreatorProperty namePropInvalid = new CreatorProperty(PropertyName.construct("name"), new TypeReference<Number>() {}, null, null, null, null, -1, null, null);

        json = writer.writeValueAsString(new String[]{"TestName"});
        deserializedBean = mapper.readerFor(TestBean.class).readValue(json);
        namePropInvalid._verifySetter();

        // Test invalid type for age property
        CreatorProperty agePropInvalid = new CreatorProperty(PropertyName.construct("age"), String.class, null, null, null, null, -1, null, null);

        json = writer.writeValueAsString(new Integer[]{25});
        deserializedBean = mapper.readerFor(TestBean.class).readValue(json);
        agePropInvalid._verifySetter();
    }

    @Test
    public void testInjectValue() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer();

        TestBean bean = new TestBean("TestName", 25);
        bean._name.markAsIgnorable();
        String injectableValueId = "injectName";

        String json = writer.writeValueAsString(new Object[]{injectableValueId, "TestName"});
        TestBean deserializedBean = mapper.readerFor(TestBean.class).readValue(json);

        assertEquals("TestName", deserializedBean.getName().get(deserializedBean).asText());

        bean._age.markAsIgnorable();
        injectableValueId = "injectAge";

        json = writer.writeValueAsString(new Object[]{injectableValueId, 25});
        deserializedBean = mapper.readerFor(TestBean.class).readValue(json);

        assertEquals(25, deserializedBean.getAge().get(deserializedBean).asInt());

        // Test injection failure due to missing injectableValueId
        TestBean beanMissingId = new TestBean("TestNameMissingId", 25);
        beanMissingId._name.markAsIgnorable();
        injectableValueId = "injectNameMissingId";

        json = writer.writeValueAsString(new Object[]{injectableValueId});
        try {
            deserializedBean = mapper.readerFor(TestBean.class).readValue(json);
            fail("Expected JsonMappingException, but got TestBean instance");
        } catch (JsonMappingException e) {
            // Expected
        }

        // Test injection failure due to unavailable injectable value
        TestBean beanUnavailableId = new TestBean("TestNameUnavailableId", 25);
        beanUnavailableId._name.markAsIgnorable();
        injectableValueId = "injectNameUnavailableId";

        json = writer.writeValueAsString(new Object[]{"UnavailableId", "TestName"});
        try {
            deserializedBean = mapper.readerFor(TestBean.class).readValue(json);
            fail("Expected JsonMappingException, but got TestBean instance");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testInjectValueToIgnorableProperty() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer();

        TestBean bean = new TestBean("TestName", 25);
        bean._name.markAsIgnorable();
        String injectableValueId = "injectName";

        String json = writer.writeValueAsString(new Object[]{injectableValueId, "TestName"});
        TestBean deserializedBean = mapper.readerFor(TestBean.class).readValue(json);

        assertNull(deserializedBean.getName().get(deserializedBean));
    }

    @Test
    public void testInjectValueToNonIgnorableProperty() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer();

        TestBean bean = new TestBean("TestName", 25);
        String injectableValueId = "injectName";

        String json = writer.writeValueAsString(new Object[]{injectableValueId, "TestName"});
        TestBean deserializedBean = mapper.readerFor(TestBean.class).readValue(json);

        assertEquals("TestName", deserializedBean.getName().get(deserializedBean).asText());
    }

    @Test
    public void testInjectValueToIgnorablePropertyWithFallback() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer();

        TestBean bean = new TestBean("TestName", 25);
        bean._name.markAsIgnorable();
        bean._name.setFallbackSetter(new SettableBeanProperty(PropertyName.construct("nameFallback"), String.class, null, null, null, null, null, null, null) {
            @Override
            public void set(Object instance, Object value) throws IOException {
                ((TestBean) instance).nameFallback = (String) value;
            }

            @Override
            public Object setAndReturn(Object instance, Object value) throws IOException {
                ((TestBean) instance).nameFallback = (String) value;
                return value;
            }
        });

        String injectableValueId = "injectName";

        String json = writer.writeValueAsString(new Object[]{injectableValueId, "TestName"});
        TestBean deserializedBean = mapper.readerFor(TestBean.class).readValue(json);

        assertNull(deserializedBean.getName().get(deserializedBean));
        assertEquals("TestName", deserializedBean.nameFallback);
    }

    @Test
    public void testInjectValueToNonIgnorablePropertyWithFallback() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer();

        TestBean bean = new TestBean("TestName", 25);
        String injectableValueId = "injectName";

        String json = writer.writeValueAsString(new Object[]{injectableValueId, "TestName"});
        TestBean deserializedBean = mapper.readerFor(TestBean.class).readValue(json);

        assertEquals("TestName", deserializedBean.getName().get(deserializedBean).asText());
        assertNull(deserializedBean.nameFallback);
    }
}