package com.fasterxml.jackson.databind.deser.impl;

import static com.fasterxml.jackson.core.util.TestUtils.convertObjectId;
import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonId;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ObjectIdValuePropertyTest {

    private static final ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null, null);
    private static final ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

    @Test
    public void testDeserializeAndSet() throws IOException, JsonProcessingException {
        ObjectParser parser = new ObjectMapper().getFactory().createParser("{\"id\": 123}");
        Object value = property.deserializeAndSet(parser, new TestContext(), new Object());
        assertEquals(123, value);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException, JsonProcessingException {
        ObjectParser parser = new ObjectMapper().getFactory().createParser("{\"id\": 123}");
        Object value = property.deserializeSetAndReturn(parser, new TestContext(), new Object());
        assertEquals(123, value);
    }

    @Test
    public void testSetAndReturn() throws IOException, JsonProcessingException {
        Object instance = new Object();
        Object id = 123;
        Object value = property.setAndReturn(instance, id);
        assertEquals(id, value);
        assertEquals(id, getIdFieldValue(instance));
    }

    @Test
    public void testGetAnnotation() {
        assertEquals(null, property.getAnnotation(JsonId.class));
    }

    @Test
    public void testGetMember() {
        assertNull(property.getMember());
    }

    @Test
    public void testDeserialize() throws IOException {
        ObjectParser parser = new ObjectMapper().getFactory().createParser("{\"id\": 123}");
        Object value = property._valueDeserializer.deserialize(parser, new TestContext());
        assertEquals(123, value);
    }

    @Test
    public void testBindItem() throws IOException {
        Object instance = new Object();
        Object id = 123;
        ObjectNode node = ObjectMapper.getDefaultInstance().createObjectNode();
        node.put("id", id);

        property.bindItem(instance, node);
        assertEquals(id, getIdFieldValue(instance));
    }

    private Object getIdFieldValue(Object instance) throws IllegalAccessException, NoSuchFieldException {
        Field idField = getIdField(instance.getClass());
        idField.setAccessible(true);
        return idField.get(instance);
    }

    private Field getIdField(Class<?> clazz) {
        Field idField = null;
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(JsonId.class)) {
                idField = field;
                break;
            }
        }
        if (idField == null) {
            Class<?> superclass = clazz.getSuperclass();
            if (superclass != null) {
                idField = getIdField(superclass);
            }
        }
        if (idField == null) {
            throw new AssertionError("No JsonId field found in " + clazz);
        }
        return idField;
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public ObjectMapper getContextualObjectMapper() {
            return new ObjectMapper();
        }

        @Override
        public ObjectIdReader getObjectIdReader() {
            return objectIdReader;
        }

        @Override
        public ObjectReader getRootValueFactory() {
            return new SimpleObjectReader();
        }

        @Override
        public JsonDeserializer<ObjectIdValueProperty> getDeserializerFactory() {
            return new ObjectIdValueProperty.Deserializer();
        }

        @Override
        public JsonDeserializer<?> findValueDeserializer(Class<?> deserializerType, PropertyName name, JsonDeserializer<?> existingDeserializer) {
            return new SimpleStringDeserializer();
        }

        @Override
        public ObjectFactory getObjectFactory() {
            return new SimpleObjectFactory();
        }

        @Override
        public TypeDeserializer getTypeDeserializer() {
            return null;
        }

        @Override
        public AnnotationIntrospector getAnnotationIntrospector() {
            return new StandardAnnotationIntrospector();
        }

        @Override
        public BeanDescription findBeanDescription(JavaType type) {
            return null;
        }

        @Override
        public BeanDescription findBeanDescription(String name) {
            return null;
        }

        @Override
        public void findAndBindValueDeserializers(JavaType type, AnnotatedMember member, List<DeserializerProvider.Finder> finders) {
        }

        @Override
        public void findAndRegisterSerializers(JavaType type, List<SerializerProvider.Finder> finders) {
        }

        @Override
        public void findAndRegisterValueDeserializers(JavaType type, List<DeserializerProvider.Finder> finders) {
        }

        @Override
        public void findAndRegisterBeanDeserializers(JavaType type, List<BeanDeserializerProvider.Finder> finders) {
        }

        @Override
        public void findAndRegisterKeyDeserializers(Class<?> keyType, List<KeyDeserializerProvider.Finder> finders) {
        }

        @Override
        public void findAndRegisterPropertyIdDeserializers(JavaType type, PropertyName idName, List<PropertyIdDeserializerProvider.Finder> finders) {
        }

        @Override
        public void findAndRegisterValueSerializers(JavaType type, List<SerializerProvider.Finder> finders) {
        }

        @Override
        public void findAndRegisterBeanSerializers(JavaType type, List<BeanSerializerProvider.Finder> finders) {
        }

        @Override
        public void findAndRegisterKeySerializers(Class<?> keyType, List<KeySerializerProvider.Finder> finders) {
        }

        @Override
        public void findAndRegisterPropertyValueSerializers(JavaType type, PropertyName name, List<PropertySerializerProvider.Finder> finders) {
        }

        @Override
        public void findAndRegisterPropertyValueTypeSerializers(JavaType type, PropertyName name, List<PropertySerializerProvider.Finder> finders) {
        }

        @Override
        public void findAndRegisterPropertyIdProviders(JavaType type, PropertyName idName, List<PropertyIdProvider.Finder> finders) {
        }
    }

    private static class SimpleObjectReader extends SimpleModule {
        @Override
        public void setupModule(SerializationConfig config) {
            // No-op
        }

        @Override
        public JsonDeserializer<?> findDeserializer(Class<?> paramType) {
            return new SimpleStringDeserializer();
        }
    }

    private static class SimpleObjectFactory extends SimpleModule {
        @Override
        public void setupModule(SerializationConfig config) {
            // No-op
        }

        @Override
        public SimpleBeanProperty buildPropertyDefinition(String name, AnnotatedMember member) {
            return null;
        }
    }

    private static class SimpleDeserializer extends JsonDeserializer<Object> {
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return null;
        }
    }

    private static class SimpleStringDeserializer extends JsonDeserializer<String> {
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return convertObjectId(p.getValueAsString());
        }
    }
}