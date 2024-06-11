package com.fasterxml.jackson.databind.deser.impl;

import static com.fasterxml.jackson.core.util.TestUtils.convertObjectIdToInt;
import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class ObjectIdReferencePropertyTest {

    static class TestBean {
        private int id;
        private String name;
        private ObjectIdReferenceProperty reference;

        @JsonIdentityInfo(generator = ObjectIdGenerators.IntArrayGenerator.class, property = "id")
        public int[] getIdValues() {
            return new int[] { 1 };
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public ObjectIdReferenceProperty getReference() {
            return reference;
        }

        public void setReference(ObjectIdReferenceProperty reference) {
            this.reference = reference;
        }
    }

    static class TestReferenceProperty extends ObjectIdReferenceProperty {
        public TestReferenceProperty(SettableBeanProperty forward, ObjectIdInfo objectIdInfo) {
            super(forward, objectIdInfo);
        }

        @Override
        public void deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {
            super.deserializeAndSet(p, ctxt, instance);
            assertEquals(1, ((TestBean) instance).getId());
        }
    }

    @Test
    public void testObjectIdReferenceProperty() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<TestBean> deser = new StdSerializer<TestBean>(TestBean.class) {
            @Override
            public TestBean deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                TestBean bean = new TestBean();
                bean.setId(convertObjectIdToInt(p.getValueAsToken()));
                return bean;
            }
        };
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(TestBean.class, "id");
        SettableBeanProperty forward = new TestableBeanProperty();
        ObjectIdReferenceProperty reference = new TestReferenceProperty(forward, objectIdInfo);

        TestBean bean = new TestBean();
        bean.setReference(reference);

        JsonParser p = mapper.getFactory().createParser("[1]");
        ObjectIdReader reader = mapper.getFactory().getObjectIdReader();
        JsonDeserializer<TestBean> deserializer = new ObjectIdReaderDeserializer(reader, deser);
        ObjectReader readerFactory = mapper.readerFor(TestBean.class);
        ObjectNode node = mapper.createObjectNode();
        node.put("name", "test");
        node.setMixInAnnotation(TestBean.class);
        JsonNode rootNode = mapper.readTree(new ByteArrayInputStream(new ObjectMapper().writeValueAsBytes(node)));
        ObjectReader rootReader = mapper.readerFor(ObjectNode.class);

        Object readValue = rootReader.readValue(JsonNode.class, deserializer);
        TestBean result = readerFactory.readValue(p, deserializer);
        assertEquals(result, readValue);
        assertEquals(bean, result);
    }

    @Test
    public void testForwardReference() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<TestBean> deser = new StdSerializer<TestBean>(TestBean.class) {
            @Override
            public TestBean deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                TestBean bean = new TestBean();
                bean.setId(convertObjectIdToInt(p.getValueAsToken()));
                return bean;
            }
        };
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(TestBean.class, "id");
        SettableBeanProperty forward = new TestableBeanProperty();
        ObjectIdReferenceProperty reference = new ObjectIdReferenceProperty(forward, objectIdInfo);

        TestBean bean1 = new TestBean();
        bean1.setId(1);

        TestBean bean2 = new TestBean();
        bean2.setId(2);
        bean2.setName("second");

        JsonParser p1 = mapper.getFactory().createParser("{\"name\": \"first\"}");
        ObjectIdReader reader = mapper.getFactory().getObjectIdReader();
        JsonDeserializer<TestBean> deserializer = new ObjectIdReaderDeserializer(reader, deser);
        ObjectReader readerFactory = mapper.readerFor(TestBean.class);
        ObjectNode node1 = mapper.createObjectNode();
        node1.put("name", "first");
        ObjectNode node2 = mapper.createObjectNode();
        node2.put("name", "second");
        node2.setMixInAnnotation(TestBean.class);
        JsonNode rootNode1 = mapper.readTree(new ByteArrayInputStream(new ObjectMapper().writeValueAsBytes(node1)));
        JsonNode rootNode2 = mapper.readTree(new ByteArrayInputStream(new ObjectMapper().writeValueAsBytes(node2)));
        ObjectReader rootReader = mapper.readerFor(ObjectNode.class);

        Object readValue1 = rootReader.readValue(rootNode1, deserializer);
        TestBean result1 = readerFactory.readValue(p1, deserializer);
        assertNull(result1.getReference());
        assertEquals(bean1, result1);

        ObjectIdReferenceProperty ref = result1.getReference();
        UnresolvedForwardReference reference = new UnresolvedForwardReference(ref, bean2);
        ref.handleResolvedForwardReference(convertObjectIdToInt(rootNode2.get("id")), reference.getValue());

        Object readValue2 = rootReader.readValue(rootNode2, deserializer);
        TestBean result2 = readerFactory.readValue(p1, deserializer);
        assertEquals(bean2, result2.getReference().getPojo());
        assertEquals(bean2, result2);
    }

    static class TestableBeanProperty extends SettableBeanProperty {
        @Override
        public void deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {
            String name = p.nextToken().asText();
            super.set(instance, name);
        }

        @Override
        public void set(Object instance, Object value) throws IOException {
            super.set(instance, value);
        }

        @Override
        public Object setAndReturn(Object instance, Object value) throws IOException {
            super.set(instance, value);
            return value;
        }
    }
}