package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory.PropertyBuilder;
import org.testng.annotations.*;

import java.io.IOException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PropertyBuilderTest {

    private static final Object NO_DEFAULT_MARKER = Boolean.FALSE;

    private SerializationConfig _config;
    private ObjectNode _node;

    @BeforeClass
    public void setup() throws JsonMappingException {
        _config = new SerializationConfig();
        _config.setRootValueSerializer(new DefaultSerializerProvider().findValueSerializer(ObjectNode.class));
        _config.setDefaultSerializerProvider(new DefaultSerializerProvider());
        _node = new ObjectMapper(_config).createObjectNode();
    }

    @Test
    public void testBuilderWithDefaultValues() throws IOException {
        BeanDescription beanDesc = new BeanDescription(MyPojo.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(_config, beanDesc);

        MyPojo pojo = new MyPojo();
        Object defaultBean = propertyBuilder.getDefaultBean();

        BeanPropertyWriter writer = propertyBuilder.buildWriter(_config.getDefaultSerializerProvider(), null, MyPojo.class, null, null, null, null, false);
        writer.writeValue(_node, pojo);

        // Test default value serialization
        Object defaultValue = BeanUtil.getDefaultValue(MyPojo.class);
        Object valueToSuppress = propertyBuilder.getPropertyDefaultValue("name", null, MyPojo.class);
        assertEquals(valueToSuppress, defaultValue);
        assertEquals(_node.get("name").asText(), defaultValue.toString());
    }

    @Test
    public void testBuilderWithNonDefaultValues() throws IOException {
        BeanDescription beanDesc = new BeanDescription(MyPojo.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(_config, beanDesc);

        MyPojo pojo = new MyPojo("Test");
        Object defaultBean = propertyBuilder.getDefaultBean();

        BeanPropertyWriter writer = propertyBuilder.buildWriter(_config.getDefaultSerializerProvider(), null, MyPojo.class, null, null, null, null, false);
        writer.writeValue(_node, pojo);

        // Test non-default value serialization
        Object valueToSuppress = propertyBuilder.getPropertyDefaultValue("name", null, MyPojo.class);
        assertEquals(valueToSuppress, null);
        assertNull(_node.get("name"));
    }

    @Test
    public void testBuilderWithNonEmptyValue() throws IOException {
        BeanDescription beanDesc = new BeanDescription(MyPojoList.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(_config, beanDesc);

        MyPojoList pojoList = new MyPojoList();
        pojoList.add(new MyPojo());

        BeanPropertyWriter writer = propertyBuilder.buildWriter(_config.getDefaultSerializerProvider(), null, MyPojoList.class, null, null, null, null, false);
        writer.writeValue(_node, pojoList);

        // Test non-empty list serialization
        Object valueToSuppress = propertyBuilder.getPropertyDefaultValue("list", null, MyPojoList.class);
        assertEquals(valueToSuppress, BeanPropertyWriter.MARKER_FOR_EMPTY);
        assertNotNull(_node.get("list"));
    }

    @Test
    public void testBuilderWithStaticTyping() throws IOException {
        BeanDescription beanDesc = new BeanDescription(MyPojo.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(_config, beanDesc);
        propertyBuilder.setUseStaticTyping(true);

        MyPojo pojo = new MyPojo();
        BeanPropertyWriter writer = propertyBuilder.buildWriter(_config.getDefaultSerializerProvider(), null, MyPojo.class, null, null, null, null, false);
        writer.writeValue(_node, pojo);

        Object valueToSuppress = propertyBuilder.getPropertyDefaultValue("name", null, MyPojo.class);
        assertEquals(valueToSuppress, null);
        assertNull(_node.get("name"));
    }

    @Test
    public void testBuilderWithJsonInclude() throws IOException {
        BeanDescription beanDesc = new BeanDescription(MyPojoWithJsonInclude.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(_config, beanDesc);

        MyPojoWithJsonInclude pojo = new MyPojoWithJsonInclude();
        BeanPropertyWriter writer = propertyBuilder.buildWriter(_config.getDefaultSerializerProvider(), null, MyPojoWithJsonInclude.class, null, null, null, null, false);
        writer.writeValue(_node, pojo);

        // Test serialization with JsonInclude.Include.NON_EMPTY
        JsonNode expectedNode = _node.deepClone();
        PropertyFilterConfig filterConfig = new PropertyFilterConfig();
        filterConfig.setInclude(new JsonInclude.Value(JsonInclude.Include.NON_EMPTY));
        ObjectMapper mapper = new ObjectMapper(_config.withSerializerProvider(new DefaultSerializerProvider()));
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        ObjectMapper filteredMapper = new ObjectMapper(_config.withSerializationInclusion(JsonInclude.Include.NON_EMPTY)
                .withPropertyFilter(filterConfig));
        ObjectNode filteredNode = (ObjectNode) filteredMapper.readValue(_node.toString(), ObjectNode.class);
        assertEquals(expectedNode, filteredNode);
    }

    private static class MyPojo {
        private String name;

        public MyPojo() {
        }

        public MyPojo(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    private static class MyPojoList extends ArrayList<MyPojo> {
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private static class MyPojoWithJsonInclude {
        private String name;
        private Boolean flag;

        public MyPojoWithJsonInclude() {
            flag = null;
        }

        public MyPojoWithJsonInclude(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Boolean getFlag() {
            return flag;
        }

        public void setFlag(Boolean flag) {
            this.flag = flag;
        }
    }
}