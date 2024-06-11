package com.fasterxml.jackson.databind.jsontype.impl;

import static com.fasterxml.jackson.core.util.SettableBeanProperty.BeanPropertySetup.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class AsPropertyTypeDeserializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    private static class TestBean {
        private String type;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    private static class TestTypeDeserializer extends StdSerializer<TestBean> {
        private final JavaType _type;
        private final TypeIdResolver _idResolver;
        private final String _typePropertyName;
        private final JavaType _defaultImpl;
        private final As _inclusion;

        public TestTypeDeserializer(JavaType type, TypeIdResolver idResolver,
                String typePropertyName, JavaType defaultImpl, As inclusion) {
            this(null, type, idResolver, typePropertyName, defaultImpl, inclusion);
        }

        public TestTypeDeserializer(TestTypeDeserializer src, BeanProperty property) {
            this(src, src._type, src._idResolver, src._typePropertyName, src._defaultImpl, src._inclusion);
            _property = property;
        }

        public TestTypeDeserializer(JavaType type, TypeIdResolver idResolver,
                String typePropertyName, JavaType defaultImpl, As inclusion) {
            this(null, type, idResolver, typePropertyName, defaultImpl, inclusion);
        }

        public TestTypeDeserializer(TestTypeDeserializer src, JavaType type,
                TypeIdResolver idResolver, String typePropertyName, JavaType defaultImpl, As inclusion) {
            super(type);
            _type = type;
            _idResolver = idResolver;
            _typePropertyName = typePropertyName;
            _defaultImpl = defaultImpl;
            _inclusion = inclusion;
        }

        @Override
        public TestBean deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            TestBean bean = mapper.readValue(p, TestBean.class);
            Object id = p.readValueAs(String.class);
            bean.setType(id.toString());
            return bean;
        }

        @Override
        public TestBean deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
            TestBean bean = mapper.readerWithType(_type).readValue(p);
            Object id = p.readValueAs(String.class);
            bean.setType(id.toString());
            return bean;
        }

        @Override
        public TestBean deserializeTypedFromObject(JsonParser p, DeserializationContext ctxt) throws IOException {
            return super.deserializeTypedFromObject(p, ctxt);
        }

        @Override
        public TestBean deserializeTypedFromAny(JsonParser p, DeserializationContext ctxt) throws IOException {
            return super.deserializeTypedFromAny(p, ctxt);
        }

        @Override
        public TestBean deserializeTypedFromArray(JsonParser p, DeserializationContext ctxt) throws IOException {
            return super.deserializeTypedFromArray(p, ctxt);
        }

        @Override
        public TestBean deserializeTypedFromScalar(JsonParser p, DeserializationContext ctxt) throws IOException {
            return super.deserializeTypedFromScalar(p, ctxt);
        }

        @Override
        public TestBean deserializeWithNativeTypeId(JsonParser p, DeserializationContext ctxt, Object id) throws IOException {
            return mapper.readerForType(TestBean.class.getTypeName() + "+[type]").readValue(p);
        }

        @Override
        public As getTypeInclusion() {
            return _inclusion;
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return (prop == _property) ? this : new TestTypeDeserializer(this, prop);
        }
    }

    @BeforeClass
    public static void setup() throws Exception {
        SimpleModule module = new SimpleModule();
        module.setSerializerFactory(new BeanSerializerFactory(mapper.findValueType(TestBean.class)));
        mapper.registerModule(module);
    }

    @Test
    public void testDeserializeWithObject() throws IOException {
        JsonParser parser = mapper.createParser("{\"type\":\"TestType1\", \"foo\":\"bar\"}");
        ObjectNode node = mapper.readValue(parser, ObjectNode.class);
        String typeName = node.get("type").asText();
        TestBean bean = mapper.readValue(parser, TestBean.class);
        assertEquals(typeName, bean.getType());
        assertEquals("bar", bean.getFoo());
    }

    @Test
    public void testDeserializeWithArray() throws IOException {
        JsonParser parser = mapper.createParser("[[\"TestType1\", \"foo\", \"bar\"]]");
        TestBean bean = mapper.readValue(parser, TestBean.class);
        assertEquals("TestType1", bean.getType());
        assertEquals("foo", bean.getFoo());
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        JsonParser parser = mapper.createParser("{\"type\":\"TestBean+[type]\", \"foo\":\"bar\"}");
        TestBean bean = mapper.readValue(parser, TestBean.class);
        assertEquals("TestBean+[type]", bean.getType());
        assertEquals("bar", bean.getFoo());
    }

    @Test
    public void testDeserializeWithDefaultImpl() throws IOException {
        JsonParser parser = mapper.createParser("{\"foo\":\"bar\"}");
        TestBean bean = mapper.readValue(parser, TestBean.class);
        assertNull(bean.getType());
        assertEquals("bar", bean.getFoo());
    }

    @Test
    public void testDeserializeMissingProperty() throws IOException {
        JsonParser parser = mapper.createParser("{\"foo\":\"bar\"}");
        TestTypeDeserializer deserializer = new TestTypeDeserializer(null, null, null, null, null, null);
        TestBean bean = mapper.readerForType(TestBean.class).readValue(parser);
        assertNull(bean.getType());
        assertEquals("bar", bean.getFoo());
    }

    @Test(expected = IOException.class)
    public void testDeserializeMissingTypeProperty() throws IOException {
        TestTypeDeserializer deserializer = new TestTypeDeserializer(null, null, null, null, null, null);
        JsonParser parser = mapper.createParser("{\"foo\":\"bar\"}");
        deserializer.deserializeTypedFromObject(parser, null);
    }

    @Test(expected = IOException.class)
    public void testDeserializeMissingTypeId() throws IOException {
        JsonParser parser = mapper.createParser("{\"foo\":\"bar\"}");
        TestTypeDeserializer deserializer = new TestTypeDeserializer(null, null, "type", null, null, null);
        deserializer.deserializeTypedFromObject(parser, null);
    }

    @Test(expected = IOException.class)
    public void testDeserializeUnknownTypeId() throws IOException {
        JsonParser parser = mapper.createParser("{\"type\":\"UnknownType\", \"foo\":\"bar\"}");
        TestTypeDeserializer deserializer = new TestTypeDeserializer(null, null, "type", null, null, null);
        deserializer.deserializeTypedFromObject(parser, null);
    }

    @Test
    public void testDeserializeUsingDefaultImpl() throws IOException {
        JsonParser parser = mapper.createParser("{\"foo\":\"bar\"}");
        TestTypeDeserializer deserializer = new TestTypeDeserializer(null, null, null, null, null, null);
        TestBean bean = deserializer.deserializeUsingDefaultImpl(parser, null);
        assertNull(bean.getType());
        assertEquals("bar", bean.getFoo());
    }

    @Test
    public void testDeserializeWithCustomTypeIdResolver() throws IOException {
        JsonParser parser = mapper.createParser("{\"type\":\"TestType1\", \"foo\":\"bar\"}");
        ObjectNode node = mapper.readValue(parser, ObjectNode.class);
        String typeName = node.get("type").asText();
        TestBean bean = mapper.readValue(parser, TestBean.class);
        assertEquals(typeName, bean.getType());
        assertEquals("bar", bean.getFoo());

        TypeIdResolver idResolver = new TypeIdResolverBase() {
            @Override
            public Id typeIdResolver(DeserializationConfig config, BeanDescription beanDesc, TypeDeserializer deserializer) {
                return Id.CLASS;
            }

            @Override
            public JavaType resolveType(DeserializationConfig config, BeanDescription beanDesc, TypeDeserializer deserializer) throws IOException {
                return beanDesc.getBeanType();
            }
        };
        TestTypeDeserializer deserializer = new TestTypeDeserializer(null, null, null, null, null, null, idResolver);
        TestBean bean2 = deserializer.deserializeTypedFromObject(parser, null);
        assertEquals(TestBean.class.getName(), bean2.getType());
        assertEquals("bar", bean2.getFoo());
    }

    @Test
    public void testDeserializeWithComplexType() throws IOException {
        String json = "{\"type\":\"TestSubType\", \"foo\":{\"bar\":\"qux\"}}";
        TestBean bean = mapper.readValue(new ByteArrayInputStream(json.getBytes()), TestBean.class);
        assertEquals("TestSubType", bean.getType());
        ObjectNode node = (ObjectNode) BeanUtil.fieldAccess(bean, "foo");
        assertEquals("qux", node.get("bar").asText());
    }

    @Test
    public void testDeserializeWithList() throws IOException {
        String json = "[{\"type\":\"TestType1\", \"foo\":\"bar1\"},{\"type\":\"TestType2\", \"foo\":\"bar2\"}]";
        TypeReference<List<TestBean>> typeRef = new TypeReference<List<TestBean>>(){};
        List<TestBean> list = mapper.readValue(new ByteArrayInputStream(json.getBytes()), typeRef);
        assertEquals(2, list.size());
        TestBean bean1 = list.get(0);
        assertEquals("TestType1", bean1.getType());
        assertEquals("bar1", bean1.getFoo());
        TestBean bean2 = list.get(1);
        assertEquals("TestType2", bean2.getType());
        assertEquals("bar2", bean2.getFoo());
    }
}