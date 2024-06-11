package com.fasterxml.jackson.databind.deser.impl;

import static com.fasterxml.jackson.core.util.TestUtilities.constructTestObjectIdReader;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Field;

import com.fasterxml.jackson.annotation.JsonId;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ObjectIdValuePropertyTest {

    private static final ObjectIdReader objectIdReader = constructTestObjectIdReader();

    @Test
    public void testDeserializeAndSet() throws Exception {
        Object id = "123";
        Object instance = new Object();

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        property.deserializeAndSet(mock(JsonParser.class), mock(DeserializationContext.class), instance);

        assertEquals(id, property.getId(instance));
    }

    @Test
    public void testDeserializeSetAndReturnNullId() throws Exception {
        Object instance = new Object();

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        Object result = property.deserializeSetAndReturn(mock(JsonParser.class), mock(DeserializationContext.class), instance);

        assertNull(result);
    }

    @Test
    public void testSetAndReturnNullId() throws Exception {
        Object instance = new Object();

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        Object result = property.setAndReturn(instance, null);

        assertNull(result);
    }

    @Test
    public void testSetAndReturn() throws Exception {
        Object id = "123";
        Object instance = new Object();

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        Object result = property.setAndReturn(instance, id);

        assertEquals(id, property.getId(instance));
    }

    @Test
    public void testSetAndReturnWithIdProperty() throws Exception {
        Object id = "123";
        Object instance = new Object();

        ObjectIdReader objectIdReaderWithIdProperty = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return new PropertyName("idProperty");
            }

            @Override
            public Class<?> idType() {
                return String.class;
            }

            @Override
            public JsonDeserializer<ObjectId> idDeserializer() {
                return null;
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return null;
            }

            @Override
            public ObjectIdResolver idResolver() {
                return null;
            }

            @Override
            public SettableBeanProperty idProperty() {
                return new ObjectIdValueProperty(objectIdReader, null) {
                    @Override
                    public ObjectIdValueProperty withName(PropertyName newName) {
                        throw new UnsupportedOperationException();
                    }

                    @Override
                    public ObjectIdValueProperty withValueDeserializer(JsonDeserializer<?> deser) {
                        throw new UnsupportedOperationException();
                    }

                    @Override
                    public Object setAndReturn(Object instance, Object value) throws IOException {
                        Field idField = ClassUtil.findSimpleValueField(instance.getClass(), "id");
                        idField.set(instance, value);
                        return value;
                    }
                };
            }
        };

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReaderWithIdProperty, null);
        Object result = property.setAndReturn(instance, id);

        assertEquals(id, property.getId(instance));
        assertEquals(id, instance.getClass().getField("id").get(instance));
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        assertNull(property.getAnnotation(JsonId.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        assertNull(property.getMember());
    }

    @Test
    public void testDeserialize() throws Exception {
        ObjectParser parser = new ObjectMapper().treeToValue(new ObjectMapper().readTree("{\"id\":123}"), Object.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);

        Object result = property.deserialize(new JsonParser(parser.getRawValueAsTree()), mock(DeserializationContext.class));

        assertEquals("123", result);
    }

    @Test
    public void testDeserializeWithIdProperty() throws Exception {
        ObjectParser parser = new ObjectMapper().treeToValue(new ObjectMapper().readTree("{\"idProperty\":123}"), Object.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);

        Object result = property.deserialize(new JsonParser(parser.getRawValueAsTree()), mock(DeserializationContext.class));

        assertEquals("123", result);
    }
}