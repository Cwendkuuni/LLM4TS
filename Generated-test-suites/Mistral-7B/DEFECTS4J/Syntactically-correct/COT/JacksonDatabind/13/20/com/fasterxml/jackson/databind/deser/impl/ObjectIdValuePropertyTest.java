package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdReader;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import org.junit.Test;

/**
 * Test class for ObjectIdValueProperty
 */
public class ObjectIdValuePropertyTest {

    @Test
    public void testObjectIdValuePropertyConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return new PropertyName("id");
            }

            @Override
            public Class<?> idType() {
                return Long.class;
            }

            @Override
            public JsonDeserializer<ObjectId> getIdDeserializer() {
                return new StdSerializer<ObjectId>(ObjectId.class) {};
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return new ObjectIdGenerator() {};
            }

            @Override
            public ObjectIdResolver idResolver() {
                return new ObjectIdResolver() {};
            }

            @Override
            public SettableBeanProperty idProperty() {
                return new StdSettableBeanProperty("id", Long.class, null, null);
            }
        };

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        assertNotNull(property);
    }

    @Test
    public void testObjectIdValuePropertyCopyConstructor() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return new PropertyName("id");
            }

            @Override
            public Class<?> idType() {
                return Long.class;
            }

            @Override
            public JsonDeserializer<ObjectId> getIdDeserializer() {
                return new StdSerializer<ObjectId>(ObjectId.class) {};
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return new ObjectIdGenerator() {};
            }

            @Override
            public ObjectIdResolver idResolver() {
                return new ObjectIdResolver() {};
            }

            @Override
            public SettableBeanProperty idProperty() {
                return new StdSettableBeanProperty("id", Long.class, null, null);
            }
        };

        ObjectIdValueProperty property1 = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        ObjectIdValueProperty property2 = new ObjectIdValueProperty(property1, new JsonDeserializer<Object>() {});
        assertEquals(property1, property2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testObjectIdValuePropertyDeprecatedConstructors() {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return new PropertyName("id");
            }

            @Override
            public Class<?> idType() {
                return Long.class;
            }

            @Override
            public JsonDeserializer<ObjectId> getIdDeserializer() {
                return new StdSerializer<ObjectId>(ObjectId.class) {};
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return new ObjectIdGenerator() {};
            }

            @Override
            public ObjectIdResolver idResolver() {
                return new ObjectIdResolver() {};
            }

            @Override
            public SettableBeanProperty idProperty() {
                return new StdSettableBeanProperty("id", Long.class, null, null);
            }
        };

        ObjectIdValueProperty property1 = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        ObjectIdValueProperty property2 = new ObjectIdValueProperty(property1, "newName");
        fail("UnsupportedOperationException expected");
    }

    @Test
    public void testObjectIdValuePropertyOverrideMethods() {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return new PropertyName("id");
            }

            @Override
            public Class<?> idType() {
                return Long.class;
            }

            @Override
            public JsonDeserializer<ObjectId> getIdDeserializer() {
                return new StdSerializer<ObjectId>(ObjectId.class) {};
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return new ObjectIdGenerator() {};
            }

            @Override
            public ObjectIdResolver idResolver() {
                return new ObjectIdResolver() {};
            }

            @Override
            public SettableBeanProperty idProperty() {
                return new StdSettableBeanProperty("id", Long.class, null, null);
            }
        };

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        assertEquals("id", property.getName());
        assertEquals(Long.class, property.getType());
        assertNull(property.getAnnotation(JsonProperty.class));
        assertNull(property.getMember());

        try {
            property.deserializeAndSet(null, null, null);
            fail("IOException expected");
        } catch (IOException e) {
            // expected
        }

        try {
            property.deserializeSetAndReturn(null, null, null);
            fail("IOException expected");
        } catch (IOException e) {
            // expected
        }

        try {
            property.set(null, null);
            fail("IOException expected");
        } catch (IOException e) {
            // expected
        }

        Object id = new ObjectId();
        try {
            property.setAndReturn(null, id);
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }
}