package com.fasterxml.jackson.databind.deser.impl;

import static com.fasterxml.jackson.databind.DeserializationTest.createContext;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonId;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ObjectIdValuePropertyTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        when(objectIdReader.propertyName).thenReturn(PropertyName.construct("id"));
        when(objectIdReader.getIdType()).thenReturn(Integer.class);
        when(objectIdReader.generator).thenReturn(new ObjectIdGenerator());
        when(objectIdReader.resolver).thenReturn(new ObjectIdResolver());
        when(objectIdReader.idProperty).thenReturn(new ObjectIdProperty(mock(PropertyName.class), null, null));
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        JsonParser p = mock(JsonParser.class);
        when(p.getCurrentName()).thenReturn("id");
        when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(p.getValueAsString()).thenReturn("123");
        DeserializationContext ctxt = createContext();
        Object instance = new Object();

        property.deserializeAndSet(p, ctxt, instance);

        Object id = ctxt.findValueToDeserialize(p, Object.class, PropertyName.construct("id"));
        assertEquals(123, id);
    }

    @Test
    public void testDeserializeAndSetNull() throws IOException {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        when(objectIdReader.propertyName).thenReturn(PropertyName.construct("id"));
        when(objectIdReader.getIdType()).thenReturn(Void.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        JsonParser p = mock(JsonParser.class);
        when(p.getCurrentName()).thenReturn("id");
        when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);

        Object instance = new Object();

        property.deserializeAndSet(p, createContext(), instance);

        assertNull(instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        when(objectIdReader.propertyName).thenReturn(PropertyName.construct("id"));
        when(objectIdReader.getIdType()).thenReturn(Integer.class);
        when(objectIdReader.generator).thenReturn(new ObjectIdGenerator());
        when(objectIdReader.resolver).thenReturn(new ObjectIdResolver());
        when(objectIdReader.idProperty).thenReturn(new ObjectIdProperty(mock(PropertyName.class), null, null));
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        JsonParser p = mock(JsonParser.class);
        when(p.getCurrentName()).thenReturn("id");
        when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(p.getValueAsString()).thenReturn("123");
        DeserializationContext ctxt = createContext();
        Object instance = new Object();

        Object result = property.deserializeSetAndReturn(p, ctxt, instance);

        Object id = ctxt.findValueToDeserialize(p, Object.class, PropertyName.construct("id"));
        assertEquals(123, id);
        assertEquals(instance, result);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        ObjectIdProperty idProperty = mock(ObjectIdProperty.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);

        Object instance = new Object();
        Object id = new Object();

        when(idProperty.setAndReturn(instance, id)).thenReturn(instance);

        Object result = property.setAndReturn(instance, id);

        assertEquals(instance, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturnNoIdProperty() throws IOException {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);

        Object instance = new Object();
        Object id = new Object();

        property.setAndReturn(instance, id);
    }

    @Test
    public void testOverrideValueDeserializer() throws IOException {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        when(objectIdReader.propertyName).thenReturn(PropertyName.construct("id"));
        when(objectIdReader.getIdType()).thenReturn(Integer.class);
        when(objectIdReader.generator).thenReturn(new ObjectIdGenerator());
        when(objectIdReader.resolver).thenReturn(new ObjectIdResolver());
        when(objectIdReader.idProperty).thenReturn(new ObjectIdProperty(mock(PropertyName.class), null, null));

        JsonDeserializer<Integer> deserializer = mock(JsonDeserializer.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(), deserializer);

        JsonParser p = mock(JsonParser.class);
        when(p.getCurrentName()).thenReturn("id");
        when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(p.getValueAsString()).thenReturn("123");
        DeserializationContext ctxt = createContext();
        Object instance = new Object();

        property.deserializeAndSet(p, ctxt, instance);

        Object id = ctxt.findValueToDeserialize(p, Object.class, PropertyName.construct("id"));
        assertEquals(123, id);
    }

    @Test
    public void testOverrideNullProvider() throws IOException {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        when(objectIdReader.propertyName).thenReturn(PropertyName.construct("id"));
        when(objectIdReader.getIdType()).thenReturn(Integer.class);
        when(objectIdReader.generator).thenReturn(new ObjectIdGenerator());
        when(objectIdReader.resolver).thenReturn(new ObjectIdResolver());
        when(objectIdReader.idProperty).thenReturn(new ObjectIdProperty(mock(PropertyName.class), null, null));

        NullValueProvider nullProvider = mock(NullValueProvider.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null, nullProvider);

        Object instance = new Object();
        Object id = new Object();

        when(nullProvider.findNullValue(property, null)).thenReturn(id);

        Object result = property.setAndReturn(instance, null);

        assertEquals(instance, result);
        assertEquals(id, result);
    }

    @Test
    public void testOverrideValueDeserializerAndNullProvider() throws IOException {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        when(objectIdReader.propertyName).thenReturn(PropertyName.construct("id"));
        when(objectIdReader.getIdType()).thenReturn(Integer.class);
        when(objectIdReader.generator).thenReturn(new ObjectIdGenerator());
        when(objectIdReader.resolver).thenReturn(new ObjectIdResolver());
        when(objectIdReader.idProperty).thenReturn(new ObjectIdProperty(mock(PropertyName.class), null, null));

        JsonDeserializer<Integer> deserializer = mock(JsonDeserializer.class);
        NullValueProvider nullProvider = mock(NullValueProvider.class);

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(), deserializer, nullProvider);

        JsonParser p = mock(JsonParser.class);
        when(p.getCurrentName()).thenReturn("id");
        when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
        DeserializationContext ctxt = createContext();
        Object instance = new Object();

        Object result = property.setAndReturn(instance, null);

        assertEquals(instance, result);
        assertNull(result);
    }

    static class ObjectIdProperty extends SettableBeanProperty {
        ObjectIdProperty(PropertyName name, Class<?> type, PropertyMetadata metadata, JsonDeserializer<?> deserializer) {
            super(name, type, metadata, deserializer);
        }
    }

    static class ObjectIdGenerator implements ObjectIdGenerator {
        @Override
        public ObjectId generateId(SerializerProvider serializers, BeanProperty property, JsonGenerator jgen, Object idValue) throws IOException {
            return new ObjectId(System.currentTimeMillis(), ClassUtil.classToInt(property.getMember().getAnnotations().getClass()));
        }
    }

    static class ObjectIdResolver implements ObjectIdResolver {
        @Override
        public ObjectId findId(SerializerProvider serializers, BeanProperty property, JsonParser p, Object value) throws IOException {
            return new ObjectId(System.currentTimeMillis(), ClassUtil.classToInt(property.getMember().getAnnotations().getClass()));
        }
    }

    static class ObjectId {
        private final long id;
        private final int typeId;

        ObjectId(long id, int typeId) {
            this.id = id;
            this.typeId = typeId;
        }

        public long getId() {
            return id;
        }

        public int getTypeId() {
            return typeId;
        }

        public void bindItem(Object item) {
            // empty
        }
    }

    static class ObjectIdReader {
        PropertyName propertyName;
        Class<?> idType;
        ObjectIdGenerator generator;
        ObjectIdResolver resolver;
        SettableBeanProperty idProperty;
    }
}