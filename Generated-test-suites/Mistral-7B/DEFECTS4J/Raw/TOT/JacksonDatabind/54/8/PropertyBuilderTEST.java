package com.fasterxml.jackson.databind.ser.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.BeanPropertyWriter;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter.MarkerForEmpty;
import com.fasterxml.jackson.databind.ser.impl.BeanSerializerFactory;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.BeanUtil;

public class PropertyBuilderTest {

    public static class TestBean {
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonSerialize(using = CustomNullSerializer.class)
        private int intValue;

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        private String stringValue;

        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        private String defaultStringValue = "default";

        private String nullableStringValue;

        private TestInner inner;

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        @JsonSerialize(using = CustomCollectionSerializer.class)
        private int[] intArray;

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        @JsonSerialize(using = CustomCollectionSerializer.class)
        private List<Integer> intList;

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        @JsonSerialize(using = CustomMapSerializer.class)
        private Map<String, Integer> intMap;

        public TestBean() {
            this.intValue = 42;
            this.stringValue = "test";
            this.nullableStringValue = null;
            this.inner = new TestInner();
            this.intArray = new int[] { 1, 2, 3 };
            this.intList = Arrays.asList(4, 5, 6);
            this.intMap = new HashMap<>();
            this.intMap.put("one", 1);
            this.intMap.put("two", 2);
        }
    }

    public static class TestInner {
        private String stringValue;

        public TestInner() {
            this.stringValue = "innerTest";
        }
    }

    public static class CustomNullSerializer extends JsonSerializer<Object> {
        @Override
        public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            if (value == null) {
                jgen.writeNull();
            }
        }
    }

    public static class CustomCollectionSerializer extends JsonSerializer<Collection<?>> {
        @Override
        public void serialize(Collection<?> value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            jgen.writeStartArray();
            for (Object element : value) {
                jgen.writeObject(element);
            }
            jgen.writeEndArray();
        }
    }

    public static class CustomMapSerializer extends JsonSerializer<Map<?, ?>> {
        @Override
        public void serialize(Map<?, ?> value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            jgen.writeStartObject();
            for (Map.Entry<?, ?> entry : value.entrySet()) {
                jgen.writeStringField(entry.getKey().toString(), String.valueOf(entry.getValue()));
            }
            jgen.writeEndObject();
        }
    }

    @org.junit.Test
    public void testPropertyBuilder() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SerializationConfig config = mapper.getSerializationConfig();
        AnnotationIntrospector annotIntrospector = config.getDefaultAnnotationIntrospector();

        Class<TestBean> testBeanClass = TestBean.class;
        BeanDescription beanDesc = BeanDescription.findDescriptor(testBeanClass, config);
        BeanSerializerFactory serializerFactory = new BeanSerializerFactory(config, beanDesc);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        // Test intValue property
        BeanPropertyWriter intPropertyWriter = propertyBuilder.buildWriter(null, beanDesc.findPropertyDefinition("intValue"), int.class, null, null, null, null, false);
        intPropertyWriter.assignNullSerializer(serializerFactory.findNullSerializer(intPropertyWriter));

        TestBean testBean = new TestBean();
        int expectedIntValue = testBean.getIntValue();
        ObjectWriter writer = mapper.writerWithView(MyView.class);
        String json = writer.writeValueAsString(testBean);
        int actualIntValue = mapper.readValue(json, TestBean.class).getIntValue();
        assertEquals(expectedIntValue, actualIntValue);

        // Test stringValue property
        BeanPropertyWriter stringPropertyWriter = propertyBuilder.buildWriter(null, beanDesc.findPropertyDefinition("stringValue"), String.class, null, null, null, null, false);

        TestBean testBeanWithNullStringValue = new TestBean();
        testBeanWithNullStringValue.setNullableStringValue(null);
        ObjectWriter writerWithNullStringValue = mapper.writerWithView(MyView.class);
        String jsonWithNullStringValue = writerWithNullStringValue.writeValueAsString(testBeanWithNullStringValue);
        TestBean actualTestBeanWithNullStringValue = mapper.readValue(jsonWithNullStringValue, TestBean.class);
        assertNull(actualTestBeanWithNullStringValue.getStringValue());

        // Test defaultStringValue property
        BeanPropertyWriter defaultStringPropertyWriter = propertyBuilder.buildWriter(null, beanDesc.findPropertyDefinition("defaultStringValue"), String.class, null, null, null, null, false);
        String expectedDefaultStringValue = propertyBuilder.getDefaultBean().getStringDefaultValue("defaultStringValue");
        TestBean testBeanWithDefaultStringValue = new TestBean();
        ObjectWriter writerWithDefaultStringValue = mapper.writerWithView(MyView.class);
        String jsonWithDefaultStringValue = writerWithDefaultStringValue.writeValueAsString(testBeanWithDefaultStringValue);
        TestBean actualTestBeanWithDefaultStringValue = mapper.readValue(jsonWithDefaultStringValue, TestBean.class);
        assertEquals(expectedDefaultStringValue, actualTestBeanWithDefaultStringValue.getDefaultStringValue());

        // Test inner property
        BeanPropertyWriter innerPropertyWriter = propertyBuilder.buildWriter(null, beanDesc.findPropertyDefinition("inner"), TestInner.class, null, null, null, null, false);
        TestBean testBeanWithInner = new TestBean();
        ObjectWriter writerWithInner = mapper.writerWithView(MyView.class);
        String jsonWithInner = writerWithInner.writeValueAsString(testBeanWithInner);
        TestBean actualTestBeanWithInner = mapper.readValue(jsonWithInner, TestBean.class);
        assertNotNull(actualTestBeanWithInner.getInner());
        assertEquals(actualTestBeanWithInner.getInner().getStringValue(), "innerTest");

        // Test intArray property
        JavaType intArrayType = TypeFactory.uncheckedCollectionType(int[].class);
        BeanPropertyWriter intArrayPropertyWriter = propertyBuilder.buildWriter(null, beanDesc.findPropertyDefinition("intArray"), intArrayType, null, null, null, null, false);

        TestBean testBeanWithIntArray = new TestBean();
        int[] expectedIntArray = testBeanWithIntArray.getIntArray();
        ObjectWriter writerWithIntArray = mapper.writerWithView(MyView.class);
        String jsonWithIntArray = writerWithIntArray.writeValueAsString(testBeanWithIntArray);
        TestBean actualTestBeanWithIntArray = mapper.readValue(jsonWithIntArray, TestBean.class);
        assertArrayEquals(expectedIntArray, actualTestBeanWithIntArray.getIntArray());

        // Test intList property
        JavaType intListType = TypeFactory.collectionType(int.class);
        BeanPropertyWriter intListPropertyWriter = propertyBuilder.buildWriter(null, beanDesc.findPropertyDefinition("intList"), intListType, null, null, null, null, false);

        TestBean testBeanWithIntList = new TestBean();
        List<Integer> expectedIntList = Arrays.asList(testBeanWithIntList.getIntList());
        ObjectWriter writerWithIntList = mapper.writerWithView(MyView.class);
        String jsonWithIntList = writerWithIntList.writeValueAsString(testBeanWithIntList);
        TestBean actualTestBeanWithIntList = mapper.readValue(jsonWithIntList, TestBean.class);
        assertEquals(expectedIntList, actualTestBeanWithIntList.getIntList());

        // Test intMap property
        JavaType intMapType = MapType.construct(String.class, int.class);
        BeanPropertyWriter intMapPropertyWriter = propertyBuilder.buildWriter(null, beanDesc.findPropertyDefinition("intMap"), intMapType, null, null, null, null, false);

        TestBean testBeanWithIntMap = new TestBean();
        Map<String, Integer> expectedIntMap = testBeanWithIntMap.getIntMap();
        ObjectWriter writerWithIntMap = mapper.writerWithView(MyView.class);
        String jsonWithIntMap = writerWithIntMap.writeValueAsString(testBeanWithIntMap);
        TestBean actualTestBeanWithIntMap = mapper.readValue(jsonWithIntMap, TestBean.class);
        assertEquals(expectedIntMap, actualTestBeanWithIntMap.getIntMap());

        // Test empty properties
        TestBean emptyTestBean = new TestBean();
        emptyTestBean.setIntValue(null);
        emptyTestBean.setStringValue(null);
        emptyTestBean.setInner(null);
        intPropertyWriter = propertyBuilder.buildWriter(null, beanDesc.findPropertyDefinition("intValue"), int.class, null, null, null, null, false);
        intPropertyWriter.setValueSerialized(MarkerForEmpty.INSTANCE);
        stringPropertyWriter = propertyBuilder.buildWriter(null, beanDesc.findPropertyDefinition("stringValue"), String.class, null, null, null, null, false);
        stringPropertyWriter.setValueSerialized(MarkerForEmpty.INSTANCE);
        innerPropertyWriter = propertyBuilder.buildWriter(null, beanDesc.findPropertyDefinition("inner"), TestInner.class, null, null, null, null, false);
        innerPropertyWriter.setValueSerialized(MarkerForEmpty.INSTANCE);
        ObjectWriter writerWithEmptyProperties = mapper.writerWithView(MyView.class);
        String jsonWithEmptyProperties = writerWithEmptyProperties.writeValueAsString(emptyTestBean);
        TestBean actualTestBeanWithEmptyProperties = mapper.readValue(jsonWithEmptyProperties, TestBean.class);
        assertNull(actualTestBeanWithEmptyProperties.getIntValue());
        assertNull(actualTestBeanWithEmptyProperties.getStringValue());
        assertNull(actualTestBeanWithEmptyProperties.getInner());
    }

    static class MyView extends AnnotatedClass {
        @Override
        public JavaType findTypeParameters() {
            return null;
        }

        @Override
        public AnnotatedConstructor[] getConstructors() {
            return AnnotatedElement.uncheckedCast(new Object[0]);
        }

        @Override
        public AnnotatedMember[] getAllMembers() {
            return new AnnotatedMember[] {
                    AnnotatedElement.uncheckedCast(PropertyBuilderTest.TestBean.class.getField("intValue")),
                    AnnotatedElement.uncheckedCast(PropertyBuilderTest.TestBean.class.getField("stringValue")),
                    AnnotatedElement.uncheckedCast(PropertyBuilderTest.TestBean.class.getField("defaultStringValue")),
                    AnnotatedElement.uncheckedCast(PropertyBuilderTest.TestBean.class.getField("nullableStringValue")),
                    AnnotatedElement.uncheckedCast(PropertyBuilderTest.TestBean.class.getField("inner")),
                    AnnotatedElement.uncheckedCast(PropertyBuilderTest.TestBean.class.getField("intArray")),
                    AnnotatedElement.uncheckedCast(PropertyBuilderTest.TestBean.class.getField("intList")),
                    AnnotatedElement.uncheckedCast(PropertyBuilderTest.TestBean.class.getField("intMap"))};
        }

        @Override
        public AnnotatedType getAnnotatedSuperclass() {
            return null;
        }

        @Override
        public int getModifiers() {
            return Modifier.PUBLIC;
        }
    }
}