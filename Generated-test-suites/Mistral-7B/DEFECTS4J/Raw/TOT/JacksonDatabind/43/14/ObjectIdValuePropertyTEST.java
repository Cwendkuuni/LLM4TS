package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectIdReader;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.NestedPropertySignature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class ObjectIdValuePropertyTest {

    private ObjectMapper objectMapper;
    private ObjectIdReader objectIdReader;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        objectIdReader = new StubObjectIdReader();
    }

    @Test
    public void testNullId() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        JsonParser p = objectMapper.getFactory().createParser("[null]");
        Object instance = new Object();

        property.deserializeAndSet(p, ctxt, instance);

        Assert.assertNull(ctxt.getIdProvider().getId(instance));
    }

    @Test
    public void testValidId() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        JsonParser p = objectMapper.getFactory().createParser("{\"id\":123}");
        Object instance = new Object();

        property.deserializeAndSet(p, ctxt, instance);

        Object id = ctxt.getIdProvider().getId(instance);
        Assert.assertNotNull(id);
        Assert.assertEquals(123, id.longValue());
    }

    @Test
    public void testOverriddenId() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        ObjectIdReader overrideIdReader = new ObjectIdReaderForTesting();
        JsonDeserializer<Object> overridingDeserializer = new JsonDeserializer<Object>() {};
        SettableBeanProperty idProperty = new StubSettableBeanProperty();

        ObjectIdValueProperty overriddenProperty = new ObjectIdValueProperty(overrideIdReader, PropertyMetadata.empty());
        ObjectIdValueProperty propertyWithOverrider = new ObjectIdValueProperty(property, overridingDeserializer, idProperty);

        JsonParser p = objectMapper.getFactory().createParser("{\"id\":123}");
        Object instance = new Object();

        propertyWithOverrider.deserializeAndSet(p, ctxt, instance);

        Object id = ctxt.getIdProvider().getId(instance);
        Assert.assertNotNull(id);
        Assert.assertEquals(123, id.longValue());
        idProperty.set(instance, Long.valueOf(456));
        Assert.assertEquals(456, ctxt.getIdProvider().getId(instance).longValue());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetWithoutIdProperty() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
        Object instance = new Object();

        property.set(instance, new Object());
    }

    @Test
    public void testDeserializeWithIdProperty() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        ObjectIdReader idReader = new StubObjectIdReaderWithIdProperty();
        JsonDeserializer<Object> deserializer = new StubJsonDeserializer();
        SettableBeanProperty idProperty = new StubSettableBeanProperty();

        ObjectIdValueProperty propertyWithIdProperty = new ObjectIdValueProperty(property, idReader, deserializer, idProperty);

        JsonParser p = objectMapper.getFactory().createParser("{\"id\":123}");
        Object instance = new Object();

        propertyWithIdProperty.deserializeAndSet(p, ctxt, instance);

        Object id = ctxt.getIdProvider().getId(instance);
        Assert.assertNotNull(id);
        Assert.assertEquals(123, id.longValue());

        Object deserializedValue = propertyWithIdProperty.getValueDeserializer().deserialize(p, ctxt);
        Assert.assertNotNull(deserializedValue);
        Assert.assertEquals("test", deserializedValue.toString());
    }

    static class StubObjectIdReader implements ObjectIdReader {
        PropertyName propertyName = new PropertyName("id");
        Class<Long> idType = Long.class;

        ObjectIdGenerator generator = new StubObjectIdGenerator();
        ObjectIdResolver resolver = new StubObjectIdResolver();

        @Override
        public PropertyName getPropertyName() {
            return propertyName;
        }

        @Override
        public Class<?> getIdType() {
            return idType;
        }

        @Override
        public ObjectIdGenerator getObjectIdGenerator() {
            return generator;
        }

        @Override
        public ObjectIdResolver getObjectIdResolver() {
            return resolver;
        }
    }

    static class StubObjectIdGenerator implements ObjectIdGenerator {
        @Override
        public ObjectId generateId(DeserializationContext ctxt, JsonParser p, Object idFromText)
                throws IOException {
            return new ObjectIdImpl();
        }
    }

    static class StubObjectIdResolver implements ObjectIdResolver {
        @Override
        public ObjectId idFromValue(DeserializationContext ctxt, Object id) {
            return new ObjectIdImpl();
        }
    }

    static class StubJsonDeserializer extends JsonDeserializer<Object> {
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt)
                throws IOException {
            return "test";
        }
    }

    static class ObjectIdImpl implements ObjectId {
        @Override
        public void bindItem(Object idUsed) {}
    }

    static class StubSettableBeanProperty implements SettableBeanProperty {
        private Object id;

        @Override
        public Object getValue(Object bean) {
            return id;
        }

        @Override
        public void setValue(Object bean, Object value) {
            id = value;
        }

        @Override
        public PropertyName getName() {
            return new PropertyName("id");
        }

        @Override
        public void set(Object bean, Object value) throws IOException {
            id = value;
        }

        @Override
        public ObjectIdValueProperty withName(PropertyName newName) {
            return this;
        }

        @Override
        public ObjectIdValueProperty withValueDeserializer(JsonDeserializer<?> deser) {
            return this;
        }

        @Override
        public <A extends Annotation> A getAnnotation(Class<A> acls) {
            return null;
        }

        @Override public AnnotatedMember getMember() { return null; }

        @Override
        public BeanPropertyDefinition getBeanPropertyDefinition(BeanPropertyDefinition basePropertyDefinition, TypeDeserializer valueTypeDeserializer) {
            return null;
        }

        @Override
        public NestedPropertySignature getNestedPropertySignature(NestedPropertySignature basePropertySignature, JsonDeserializer<?> valueDeserializer) {
            return null;
        }

        @Override
        public JsonDeserializer<?> getChildPropertyDeserializer(JsonDeserializer<?> valueDeserializer) {
            return null;
        }

        @Override
        public String getNameAsString() {
            return "id";
        }

        @Override
        public JsonDeserializer<?> getValueDeserializer() {
            return null;
        }
    }
}