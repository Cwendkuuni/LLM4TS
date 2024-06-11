package com.fasterxml.jackson.databind.deser.impl;

import static com.fasterxml.jackson.core.util.TestUtilities.createParser;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdReader;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.lang.annotation.Annotation;

public class ObjectIdValuePropertyTest {

    private static final JsonDeserializer<ObjectIdValueProperty> VALUE_DESERIALIZER = new StdSerializer<ObjectIdValueProperty>(ObjectIdValueProperty.class) {
        @Override
        public ObjectIdValueProperty deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            ObjectIdValueProperty result = new ObjectIdValueProperty(null, ctxt.findValueDeserializer(p, ObjectIdReader.class));
            result.setName("testProperty");
            return result;
        }
    };

    @Test
    public void testObjectIdValueProperty() {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public String idTypeName() {
                return String.class.getName();
            }

            @Override
            public JsonDeserializer<?> getIdDeserializer() {
                return VALUE_DESERIALIZER;
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                throw new UnsupportedOperationException();
            }

            @Override
            public ObjectIdResolver idResolver() {
                throw new UnsupportedOperationException();
            }

            @Override
            public PropertyName propertyName() {
                return new PropertyName("testProperty");
            }
        };

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        assertEquals("testProperty", property.getName());
        assertNull(property.getAnnotation(Annotation.class));
        assertEquals(ObjectIdReader.class, property.getMember().getType());
        assertEquals(null, property.getMember());

        ObjectIdValueProperty copied = new ObjectIdValueProperty(property, null);
        assertEquals(property, copied);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public String idTypeName() {
                return String.class.getName();
            }

            @Override
            public JsonDeserializer<?> getIdDeserializer() {
                return VALUE_DESERIALIZER;
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                throw new UnsupportedOperationException();
            }

            @Override
            public ObjectIdResolver idResolver() {
                throw new UnsupportedOperationException();
            }

            @Override
            public PropertyName propertyName() {
                return new PropertyName("testProperty");
            }
        };

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        JsonParser parser = createParser("\"123\"");

        Object result = property.deserializeAndSet(parser, new TestContext(), new Object());
        assertEquals("123", result);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public String idTypeName() {
                return String.class.getName();
            }

            @Override
            public JsonDeserializer<?> getIdDeserializer() {
                return VALUE_DESERIALIZER;
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                throw new UnsupportedOperationException();
            }

            @Override
            public ObjectIdResolver idResolver() {
                throw new UnsupportedOperationException();
            }

            @Override
            public PropertyName propertyName() {
                return new PropertyName("testProperty");
            }
        };

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        JsonParser parser = createParser("\"123\"");

        Object result = property.deserializeSetAndReturn(parser, new TestContext(), new Object());
        assertEquals("123", result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public String idTypeName() {
                return String.class.getName();
            }

            @Override
            public JsonDeserializer<?> getIdDeserializer() {
                return VALUE_DESERIALIZER;
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                throw new UnsupportedOperationException();
            }

            @Override
            public ObjectIdResolver idResolver() {
                throw new UnsupportedOperationException();
            }

            @Override
            public PropertyName propertyName() {
                return new PropertyName("testProperty");
            }
        };

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        Object id = new Object();
        property.set(new Object(), id);
    }
}

class TestContext extends DeserializationContext {
}