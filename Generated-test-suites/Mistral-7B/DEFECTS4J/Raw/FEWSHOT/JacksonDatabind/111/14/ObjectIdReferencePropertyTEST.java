package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonId;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public class ObjectIdReferencePropertyTest {

    @Test
    public void testObjectIdReferenceProperty() throws JsonProcessingException {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(Introspect.getExactMember("name", String.class, TestPojo.class), JsonType.STRING_TYPE);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(new SettableBeanProperty.Std(new JsonField("name", JsonType.STRING_TYPE), TestPojo.class), objectIdInfo);
        assertEquals(TestPojo.class, property.getType());
        assertNull(property.getMember());
        assertNull(property.getAnnotation(JsonId.class));
        assertNull(property.getAnnotations());
        assertEquals(TestPojo.class, property.getCreatorIndex());
        assertNull(property.getValueDeserializer());
        assertNull(property.getNullProvider());
        assertNull(property.getAccessModifier());
        assertNull(property.getName());
        assertNull(property.getBeanProperty());
        assertNull(property._forward);
        assertNull(property._objectIdInfo);
    }

    @Test
    public void testObjectIdReferencePropertyWithValueDeserializer() throws JsonProcessingException {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(Introspect.getExactMember("name", String.class, TestPojo.class), JsonType.STRING_TYPE);
        JsonDeserializer<Number> jsonDeserializer = new JsonDeserializer<Number>() {
            @Override
            public Number deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return 1;
            }
        };
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(new SettableBeanProperty.Std(new JsonField("name", JsonType.STRING_TYPE), TestPojo.class), jsonDeserializer, objectIdInfo);
        assertEquals(TestPojo.class, property.getType());
        assertNull(property.getMember());
        assertNull(property.getAnnotation(JsonId.class));
        assertNull(property.getAnnotations());
        assertEquals(TestPojo.class, property.getCreatorIndex());
        assertEquals(jsonDeserializer, property.getValueDeserializer());
        assertNull(property.getNullProvider());
        assertNull(property.getAccessModifier());
        assertNull(property.getName());
        assertNull(property.getBeanProperty());
        assertNull(property._forward);
        assertNull(property._objectIdInfo);
    }

    @Test
    public void testObjectIdReferencePropertyWithNullProvider() throws JsonProcessingException {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(Introspect.getExactMember("name", String.class, TestPojo.class), JsonType.STRING_TYPE);
        NullValueProvider nva = new NullValueProvider() {
            @Override
            public Object getNullProvider() {
                return null;
            }
        };
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(new SettableBeanProperty.Std(new JsonField("name", JsonType.STRING_TYPE), TestPojo.class), null, objectIdInfo, nva);
        assertEquals(TestPojo.class, property.getType());
        assertNull(property.getMember());
        assertNull(property.getAnnotation(JsonId.class));
        assertNull(property.getAnnotations());
        assertEquals(TestPojo.class, property.getCreatorIndex());
        assertNull(property.getValueDeserializer());
        assertEquals(nva, property.getNullProvider());
        assertNull(property.getAccessModifier());
        assertNull(property.getName());
        assertNull(property.getBeanProperty());
        assertNull(property._forward);
        assertNull(property._objectIdInfo);
    }

    @Test
    public void testObjectIdReferencePropertyWithName() throws JsonProcessingException {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(Introspect.getExactMember("name", String.class, TestPojo.class), JsonType.STRING_TYPE);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(new SettableBeanProperty.Std(new JsonField("name", JsonType.STRING_TYPE), TestPojo.class), null, objectIdInfo);
        ObjectIdReferenceProperty propertyWithName = property.withName(new JsonField("newName", JsonType.STRING_TYPE));
        assertEquals(TestPojo.class, propertyWithName.getType());
        assertEquals("newName", propertyWithName.getName().getShortName());
        assertNull(propertyWithName.getMember());
        assertNull(propertyWithName.getAnnotation(JsonId.class));
        assertNull(propertyWithName.getAnnotations());
        assertEquals(TestPojo.class, propertyWithName.getCreatorIndex());
        assertNull(propertyWithName.getValueDeserializer());
        assertNull(propertyWithName.getNullProvider());
        assertNull(propertyWithName.getAccessModifier());
        assertNull(propertyWithName.getBeanProperty());
        assertNull(propertyWithName._forward);
        assertNull(propertyWithName._objectIdInfo);
    }

    @Test
    public void testObjectIdReferencePropertyFixAccess() throws JsonProcessingException {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(Introspect.getExactMember("name", String.class, TestPojo.class), JsonType.STRING_TYPE);
        SettableBeanProperty forward = new SettableBeanProperty.Std(new JsonField("name", JsonType.STRING_TYPE), TestPojo.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        property.fixAccess(new DeserializationConfig(new AnnotationIntrospector(), new SerializationConfig(new AnnotationIntrospector())));
        assertNotNull(property._forward);
        assertSame(forward, property._forward);
    }

    @Test
    public void testObjectIdReferencePropertyGetAnnotation() throws JsonProcessingException {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(Introspect.getExactMember("name", String.class, TestPojo.class), JsonType.STRING_TYPE);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(new SettableBeanProperty.Std(new JsonField("name", JsonType.STRING_TYPE), TestPojo.class), null, objectIdInfo);
        JsonId jsonIdAnnotation = new JsonId();
        property.setAnnotation(jsonIdAnnotation);
        assertEquals(jsonIdAnnotation, property.getAnnotation(JsonId.class));
    }

    @Test
    public void testObjectIdReferencePropertyGetMember() throws JsonProcessingException {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(Introspect.getExactMember("name", String.class, TestPojo.class), JsonType.STRING_TYPE);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(new SettableBeanProperty.Std(new JsonField("name", JsonType.STRING_TYPE), TestPojo.class), null, objectIdInfo);
        AnnotatedMember annotatedMember = new SimpleBeanPropertyMember("name", JsonType.STRING_TYPE, TestPojo.class);
        property.setMember(annotatedMember);
        assertEquals(annotatedMember, property.getMember());
    }

    @Test
    public void testObjectIdReferencePropertyDeserializeAndSet() throws JsonProcessingException {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(Introspect.getExactMember("name", String.class, TestPojo.class), JsonType.STRING_TYPE);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(new SettableBeanProperty.Std(new JsonField("name", JsonType.STRING_TYPE), TestPojo.class), objectIdInfo);
        TestPojo pojo = new TestPojo();
        JsonParser p = new TestJsonParser();
        p.setValue(ObjectMapper.getDefaultInstance().valueToTree(pojo));
        Object deserializedValue = property.deserializeAndSet(p, new DeserializationContext(new AnnotationIntrospector(), new ObjectMapper()), pojo);
        assertEquals("expectedValue", deserializedValue);
        assertEquals("expectedValue", pojo.getName());
    }

    @Test
    public void testObjectIdReferencePropertyDeserializeSetAndReturn() throws JsonProcessingException {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(Introspect.getExactMember("name", String.class, TestPojo.class), JsonType.STRING_TYPE);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(new SettableBeanProperty.Std(new JsonField("name", JsonType.STRING_TYPE), TestPojo.class), objectIdInfo);
        TestPojo pojo = new TestPojo();
        JsonParser p = new TestJsonParser();
        Object deserializedValue = property.deserializeSetAndReturn(p, new DeserializationContext(new AnnotationIntrospector(), new ObjectMapper()), pojo);
        assertEquals("expectedValue", deserializedValue);
        assertEquals("expectedValue", pojo.getName());
    }

    @Test
    public void testObjectIdReferencePropertySet() throws JsonProcessingException {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(Introspect.getExactMember("name", String.class, TestPojo.class), JsonType.STRING_TYPE);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(new SettableBeanProperty.Std(new JsonField("name", JsonType.STRING_TYPE), TestPojo.class), objectIdInfo);
        TestPojo pojo = new TestPojo();
        Object value = "expectedValue";
        property.set(pojo, value);
        assertEquals("expectedValue", pojo.getName());
    }

    @Test
    public void testObjectIdReferencePropertySetAndReturn() throws JsonProcessingException {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(Introspect.getExactMember("name", String.class, TestPojo.class), JsonType.STRING_TYPE);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(new SettableBeanProperty.Std(new JsonField("name", JsonType.STRING_TYPE), TestPojo.class), objectIdInfo);
        TestPojo pojo = new TestPojo();
        Object value = "expectedValue";
        Object returnedValue = property.setAndReturn(pojo, value);
        assertEquals("expectedValue", pojo.getName());
        assertEquals("expectedValue", returnedValue);
    }

    @Test
    public void testPropertyReferring() throws JsonProcessingException {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(Introspect.getExactMember("name", String.class, TestPojo.class), JsonType.STRING_TYPE);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(new SettableBeanProperty.Std(new JsonField("name", JsonType.STRING_TYPE), TestPojo.class), objectIdInfo);
        TestPojo pojo1 = new TestPojo();
        TestPojo pojo2 = new TestPojo();
        UnresolvedForwardReference reference = new UnresolvedForwardReference();
        PropertyReferring referring = new PropertyReferring(property, reference, TestPojo.class, pojo1);
        Object deserializedValue = referring.deserializeSetAndReturn(new TestJsonParser(), new DeserializationContext(new AnnotationIntrospector(), new ObjectMapper()), pojo2);
        assertEquals(null, deserializedValue);
        referring.handleResolvedForwardReference(pojo1.getId(), "expectedValue");
        assertEquals("expectedValue", pojo1.getName());
    }

    static class TestPojo {
        private String name;
        private ObjectId id;

        public TestPojo() {
            this.name = "defaultValue";
        }

        public TestPojo(String id) {
            this.id = new ObjectId(id);
            this.name = "defaultValue";
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public ObjectId getId() {
            return id;
        }

        @Override
        public String toString() {
            return new ToStringSerializer(String.class).serialize(this, null);
        }
    }

    static class TestJsonParser extends JsonParser {
        @Override
        public ObjectId readValueAsId(Class<?> valueType) throws IOException {
            return null;
        }
    }
}