package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.type.ClassKey;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class PropertyBuilderTest {

    private ObjectMapper mapper;
    private BeanSerializerFactory factory;
    private BeanDescription beanDescription;
    private SerializationConfig config;

    @BeforeClass
    public void setup() throws Exception {
        mapper = new ObjectMapper();
        config = mapper.getSerializationConfig();
        factory = new PropertyBuilder.Std(config, null);
        beanDescription = mapper.findBeanDescription(TestClass.class);
    }

    @Test
    public void testBuildWriter_NoAnnotation() throws JsonMappingException {
        SerializerProvider prov = mapper.getSerializerProvider();
        BeanPropertyWriter writer = factory.buildWriter(prov, null, null, null, null, null, null, true);
        assertEquals(writer.getClass(), BeanPropertyWriter.class);

        // Test with null property definition
        BeanPropertyDefinition propDef = null;
        writer = factory.buildWriter(prov, propDef, null, null, null, null, null, true);
        assertNull(writer);
    }

    @Test
    public void testBuildWriter_DefaultInclude() throws JsonMappingException, IOException {
        SerializerProvider prov = mapper.getSerializerProvider();
        BeanPropertyWriter writer = factory.buildWriter(prov, null, null, null, null, null, null, true);
        assertNotNull(writer);

        ObjectNode tree = mapper.writeValueAsTree(new TestClass());
        assertEquals(tree.toString(), "{\"name\":\"John\",\"age\":30,\"address\":{\"street\":\"123 Main St\",\"city\":\"Anytown\"}}");

        writer = factory.buildWriter(prov, beanDescription.findPropertyDefinition("name"), null, null, null, null, null, true);
        assertEquals(writer.getClass(), BeanPropertyWriter.class);

        TreeNode node = writer.getSerializerProvider().getSerializer(TestClass.class).serializeValue(writer, new TestClass(), null);
        ObjectNode nodeAsObject = (ObjectNode) node;
        assertEquals(nodeAsObject.path("name").asText(), "John");
    }

    @Test
    public void testBuildWriter_IncludeExclude() throws JsonMappingException, IOException {
        SerializerProvider prov = mapper.getSerializerProvider();
        AnnotatedMember am = getTestClassMember("address.city");
        JsonInclude.Include include = JsonInclude.Include.NON_EMPTY;
        BeanPropertyWriter writer = factory.buildWriter(prov, null, null, null, null, null, am, true)
                .withInclusion(include);
        assertEquals(writer.getClass(), BeanPropertyWriter.class);

        ObjectNode tree = mapper.writeValueAsTree(new TestClass());
        ObjectNode address = tree.path("address");
        assertFalse(address.isMissingNode());
        assertTrue(address.hasNonNull("city"));
        assertEquals(address.get("city").asText(), "Anytown");

        ObjectNode treeWithEmptyAddress = mapper.writeValueAsTree(new TestClassWithEmptyAddress());
        ObjectNode addressWithEmpty = treeWithEmptyAddress.path("address");
        assertTrue(addressWithEmpty.isMissingNode());
    }

    @Test
    public void testBuildWriter_CustomSerializer() throws JsonMappingException, IOException {
        SerializerProvider prov = mapper.getSerializerProvider();

        JsonSerialize.Typing typing = JsonSerialize.Typing.STATIC;
        JavaType type = new ClassKey(TestClassWithCustomSerializer.class, null);
        BeanPropertyWriter writer = factory.buildWriter(prov, null, null, null, null, null, null, true)
                .withSerializerInstance(new StdSerializer<TestClassWithCustomSerializer>(type), null)
                .withSerializerProvider(prov)
                .withTyping(typing);
        assertEquals(writer.getClass(), BeanPropertyWriter.class);

        ObjectNode tree = mapper.writeValueAsTree(new TestClassWithCustomSerializer());
        assertEquals(tree.toString(), "{\"name\":\"John\",\"age\":30,\"custom\":\"Custom Value\"}");
    }

    @Test
    public void testFindSerializationType_NoAnnotations() throws JsonMappingException {
        String propertyName = "nonExistentProperty";
        JavaType declaredType = new ClassKey(TestClass.class, null);
        JavaType serializationType = factory.findSerializationType(null, false, declaredType);
        assertNull(serializationType);

        // Test with null accessor
        AnnotatedMember accessor = null;
        serializationType = factory.findSerializationType(accessor, false, declaredType);
        assertNull(serializationType);
    }

    @Test
    public void testFindSerializationType_OnlyClassAnnotation() throws JsonMappingException {
        AnnotatedMember accessor = getTestClassMember("name");
        JavaType serializationType = factory.findSerializationType(accessor, false, null);
        assertEquals(serializationType.getRawClass(), String.class);
    }

    @Test
    public void testFindSerializationType_BothClassAndMemberAnnotations() throws JsonMappingException {
        AnnotatedMember accessor = getTestClassMember("age");
        JavaType serializationType = factory.findSerializationType(accessor, false, null);
        assertEquals(serializationType.getRawClass(), Integer.class);
    }

    @Test
    public void testGetDefaultBean() throws JsonMappingException, IOException {
        Object defaultBean = factory.getDefaultBean();
        assertTrue(defaultBean instanceof TestClass);
    }

    @Test
    public void testGetPropertyDefaultValue_NoDefault() throws JsonMappingException, IOException {
        Object defaultBean = factory.getDefaultBean();
        Object propertyDefaultValue = factory.getPropertyDefaultValue("name", null, String.class);
        assertNull(propertyDefaultValue);
    }

    @Test
    public void testGetPropertyDefaultValue_Boolean() throws JsonMappingException, IOException {
        Object defaultBean = factory.getDefaultBean();
        Object propertyDefaultValue = factory.getPropertyDefaultValue("booleanProperty", null, Boolean.TYPE);
        assertEquals(propertyDefaultValue, Boolean.FALSE);
    }

    @Test
    public void testGetPropertyDefaultValue_Integer() throws JsonMappingException, IOException {
        Object defaultBean = factory.getDefaultBean();
        Object propertyDefaultValue = factory.getPropertyDefaultValue("integerProperty", null, Integer.TYPE);
        assertEquals(propertyDefaultValue, 0);
    }

    @Test
    public void testGetPropertyDefaultValue_String() throws JsonMappingException, IOException {
        Object defaultBean = factory.getDefaultBean();
        Object propertyDefaultValue = factory.getPropertyDefaultValue("stringProperty", null, String.class);
        assertEquals(propertyDefaultValue, "");
    }

    private AnnotatedMember getTestClassMember(String propertyName) throws JsonMappingException {
        Method method = beanDescription.findProperty(propertyName);
        if (method != null) {
            return new Annotated(method, beanDescription.getClassAnnotations());
        }
        BeanPropertyDefinition propDef = beanDescription.findPropertyDefinition(propertyName);
        return propDef != null ? new AnnotatedMember(propDef) : null;
    }

    private static class TestClass {
        @JsonProperty("name")
        private String name = "John";
        private int age = 30;

        private Address address = new Address();

        public static class Address {
            private String street = "123 Main St";
            private String city = "Anytown";
        }
    }

    private static class TestClassWithEmptyAddress {
        @JsonProperty("name")
        private String name = "John";
        private int age = 30;

        private Address address;
    }

    private static class TestClassWithCustomSerializer {
        @JsonProperty("name")
        private String name = "John";
        private int age = 30;

        @JsonSerialize(using = TestClassCustomSerializer.class)
        private String custom = "Default Value";
    }

    private static class TestClassCustomSerializer extends StdSerializer<TestClassWithCustomSerializer> {
        public TestClassCustomSerializer() {
            this(null);
        }

        public TestClassCustomSerializer(Class<TestClassWithCustomSerializer> t) {
            this(null);
        }

        public TestClassCustomSerializer(Class<TestClassWithCustomSerializer> t, JsonSerializer<TestClassWithCustomSerializer> s) {
            super(t, s);
        }

        @Override
        public void serialize(TestClassWithCustomSerializer src, JsonGenerator jg, SerializerProvider sp) throws IOException {
            jg.writeString("Custom Value");
        }
    }
}