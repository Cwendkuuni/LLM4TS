package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.BeanPropertyWriter;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.ConverterUtils;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Method;

public class PropertyBuilderTest {

    private SerializationConfig config;
    private BeanDescription beanDesc;

    @Before
    public void setUp() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        config = mapper.getSerializationConfig();
        beanDesc = mapper.getIntrospector().findBeanDescription(PropertyBuilder.class, null);
    }

    @Test
    public void testPropertyBuilderBasic() throws IOException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        SerializerProvider prov = mapper.getSerializerProvider();
        BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, null, null, null, null, null, null, true);
        assertEquals(BeanPropertyWriter.class, bpw.getClass());
    }

    @Test
    public void testPropertyBuilderCustomSerializationType() throws IOException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        SerializerProvider prov = mapper.getSerializerProvider();
        Object customSerializer = ConverterUtils.createConverter(String.class, Integer.class);
        TypeSerializer typeSerializer = mapper.getTypeSerializer(String.class);
        BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, null, mapper.getTypeFactory().constructType(String[].class), null, typeSerializer, null, null, true);
        assertEquals(BeanPropertyWriter.class, bpw.getClass());
        assertEquals(customSerializer, bpw.getNullSerializer());
    }

    @Test
    public void testPropertyBuilderUnwrappingWriter() throws Exception {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        SerializerProvider prov = mapper.getSerializerProvider();
        NameTransformer unwrapper = new NameTransformer() {
            @Override
            public String transform(String name, TypeSerializer typeSerializer) {
                return name.toUpperCase();
            }
        };
        BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, null, null, null, null, null, null, false).unwrappingWriter(unwrapper);
        Method writeMethod = BeanPropertyWriter.class.getDeclaredMethod("writeValue", Object, JsonGenerator);
        writeMethod.setAccessible(true);

        Object propertyBuilderInstance = new PropertyBuilder(config, beanDesc);
        Object testObject = new TestClass();
        Object expectedResult = "TEST";

        writeMethod.invoke(bpw, testObject, mapper.getFactory().createGenerator(mapper.getOutputFactory()));
        String result = mapper.writeValueAsString(testObject);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testPropertyBuilderSerializationTypeWithContentTypeSerializer() throws IOException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        SerializerProvider prov = mapper.getSerializerProvider();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        propDef.setName("list");
        propDef.setBeanProperty(new TestClassListProperty());
        JavaType serializationType = mapper.getTypeFactory().constructType(ArrayList.class);
        JavaType contentType = mapper.getTypeFactory().constructType(String.class);
        TypeSerializer contentTypeSer = mapper.getTypeSerializer(String.class);
        BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, serializationType, null, null, contentTypeSer, null, true);

        assertEquals(serializationType, bpw.getSerializableType());
        assertEquals(contentTypeSer, bpw.getTypeSerializer());
    }

    @Test(expected = JsonMappingException.class)
    public void testPropertyBuilderBadPropertyDefinition() throws IOException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        SerializerProvider prov = mapper.getSerializerProvider();
        BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, null, null, null, null, null, null, true);
        bpw = bpw.nextWriter(null);
        bpw.writeValue(null, mapper.getFactory().createGenerator(mapper.getOutputFactory()));
    }

    @Test
    public void testPropertyBuilderNullValues() throws IOException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        SerializerProvider prov = mapper.getSerializerProvider();
        BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, null, null, null, null, null, null, true);

        Object testObject = null;
        bpw.writeValue(testObject, mapper.getFactory().createGenerator(mapper.getOutputFactory()));
        String result = mapper.writeValueAsString(testObject);

        assertTrue(result.isEmpty());
        assertFalse(config.isEnabled(SerializationFeature.WRITE_NULL_MAP_VALUES));
        result = mapper.writeValueAsString(testObject, JsonInclude.Include.NON_NULL);
        assertTrue(result.isEmpty());
        result = mapper.writeValueAsString(testObject, JsonInclude.Include.NON_EMPTY);
        assertTrue(result.isEmpty());
        result = mapper.writeValueAsString(testObject, JsonInclude.Include.ALWAYS);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testPropertyBuilderEmptyValues() throws IOException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        SerializerProvider prov = mapper.getSerializerProvider();
        BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, null, null, null, null, null, null, true);

        Object testObject = new TestClass();
        bpw.writeValue(testObject, mapper.getFactory().createGenerator(mapper.getOutputFactory()));
        String result = mapper.writeValueAsString(testObject);

        assertTrue(result.isEmpty());
        assertFalse(config.isEnabled(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS));
        result = mapper.writeValueAsString(testObject, JsonInclude.Include.NON_EMPTY);
        assertTrue(result.isEmpty());
        result = mapper.writeValueAsString(testObject, JsonInclude.Include.NON_NULL);
        assertFalse(result.isEmpty());
        result = mapper.writeValueAsString(testObject, JsonInclude.Include.ALWAYS);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testPropertyBuilderNonDefaultValues() throws IOException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        SerializerProvider prov = mapper.getSerializerProvider();
        BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, null, null, null, null, null, null, true);
        Object testObject = new TestClass();
        bpw.setValueToSuppress(BeanPropertyWriter.MARKER_FOR_EMPTY);

        Object expectedDefaultValue = "DEFAULT";
        Object expectedPropertyValue = "VALUE";

        assertEquals(expectedDefaultValue, bpw.getDefaultValue());
        assertEquals(expectedPropertyValue, bpw.getValueToSuppress());

        testObject.setTestProperty("VALUE");
        bpw.writeValue(testObject, mapper.getFactory().createGenerator(mapper.getOutputFactory()));
        String result = mapper.writeValueAsString(testObject);

        assertEquals(expectedPropertyValue, result);
    }

    @Test
    public void testPropertyBuilderNullSerializer() throws IOException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        SerializerProvider prov = mapper.getSerializerProvider();
        BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, null, null, null, null, null, null, true);

        bpw.assignNullSerializer(null);

        Object testObject = new TestClass();
        bpw.writeValue(testObject, mapper.getFactory().createGenerator(mapper.getOutputFactory()));
        String result = mapper.writeValueAsString(testObject);

        assertTrue(result.isEmpty());
    }

    static class TestClass {
        @JsonSerialize(using = TestClassSerializer.class)
        private String testProperty;

        public String getTestProperty() {
            return testProperty;
        }

        public void setTestProperty(String testProperty) {
            this.testProperty = testProperty;
        }

        public TestClass() {
        }
    }

    static class TestClassListProperty {
        @JsonSerialize(using = TestClassSerializer.class)
        private List<String> list;

        public List<String> getList() {
            return list;
        }

        public void setList(List<String> list) {
            this.list = list;
        }

        public TestClassListProperty() {
        }
    }

    static class TestClassSerializer extends JsonSerializer<TestClass> {
        @Override
        public void serialize(TestClass testClass, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeString(testClass.getTestProperty().toUpperCase());
        }
    }
}