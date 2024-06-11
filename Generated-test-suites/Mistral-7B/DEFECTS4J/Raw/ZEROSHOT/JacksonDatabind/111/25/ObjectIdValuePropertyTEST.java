package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdReader;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.junit.Test;

/**
 * Test class for {@link ObjectIdValueProperty}
 */
public class ObjectIdValuePropertyTest {

    @Test
    public void testObjectIdValueProperty() throws Exception {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public String propertyName() {
                return "id";
            }

            @Override
            public Class<?> idType() {
                return Long.class;
            }

            @Override
            public JsonDeserializer<?> idDeserializer() {
                return new StdDeserializer<Long>(Long.class, null) {
                    @Override
                    public Long deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                        return p.valueAsLong();
                    }
                };
            }

            @Override
            public ObjectIdGenerator generator() {
                return null;
            }

            @Override
            public ObjectIdResolver resolver() {
                return null;
            }

            @Override
            public SettableBeanProperty idProperty() {
                return null;
            }
        };

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        assertNotNull(objectIdValueProperty);
        assertEquals("id", objectIdValueProperty.getName());
        assertEquals(Long.class, objectIdValueProperty.getType());
        assertNull(objectIdValueProperty.getAnnotation(JsonProperty.class));
        assertNull(objectIdValueProperty.getMember());

        ObjectIdValueProperty clone = objectIdValueProperty.withName(new PropertyName("newName"));
        assertEquals("newName", clone.getName());

        JsonDeserializer<Object> valueDeserializer = new StdDeserializer<Object>(Object.class, null);
        ObjectIdValueProperty settableBeanPropertyWithValueDeserializer = objectIdValueProperty.withValueDeserializer(valueDeserializer);
        assertEquals(valueDeserializer, settableBeanPropertyWithValueDeserializer.getValueDeserializer());

        NullValueProvider nullValueProvider = new NullValueProvider() {
            @Override
            public JsonProvider getProvider() {
                return null;
            }

            @Override
            public Object getNullValue() {
                return null;
            }
        };
        ObjectIdValueProperty settableBeanPropertyWithNullProvider = settableBeanPropertyWithValueDeserializer.withNullProvider(nullValueProvider);
        assertEquals(nullValueProvider, settableBeanPropertyWithNullProvider.getNullProvider());

        JsonParser jsonParser = createMockParser();
        Object instance = new Object();
        DeserializationContext deserializationContext = createMockContext();

        objectIdValueProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
        assertNull(instance.getId());

        jsonParser.nextToken(); // empty ObjectId
        objectIdValueProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
        assertNull(instance.getId());

        jsonParser.nextValue("123");
        Object id = objectIdValueProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);
        assertEquals(Long.valueOf(123), id);
        assertEquals(Long.valueOf(123), instance.getId());

        jsonParser.nextValue("null");
        id = objectIdValueProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);
        assertNull(id);
        assertNull(instance.getId());

        objectIdValueProperty.set(instance, null);
        assertNull(instance.getId());

        objectIdValueProperty.set(instance, new Long(456));
        assertEquals(Long.valueOf(456), instance.getId());
    }

    private JsonParser createMockParser() {
        return mock(JsonParser.class);
    }

    private DeserializationContext createMockContext() {
        return mock(DeserializationContext.class);
    }
}