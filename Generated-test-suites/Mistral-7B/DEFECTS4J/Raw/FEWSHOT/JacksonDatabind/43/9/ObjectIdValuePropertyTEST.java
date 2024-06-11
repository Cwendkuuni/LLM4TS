package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class ObjectIdValuePropertyTest {

    @SuppressWarnings("unchecked")
    private static final ObjectIdReader dummyObjectIdReader = new ObjectIdReader() {
        @Override
        public PropertyName propertyName() {
            return PropertyName.construct("id");
        }

        @Override
        public ObjectIdType idType() {
            return ObjectIdType.STRING;
        }

        @Override
        public ObjectIdGenerator idGenerator() {
            return ObjectIdGenerators.IntSequenceGenerator.instance;
        }

        @Override
        public ObjectIdResolver idResolver() {
            return null;
        }

        @Override
        public JsonDeserializer<ObjectIdNode> getIdDeserializer() {
            return new StdSerializer<ObjectIdNode>(ObjectIdNode.class) {};
        }

        @Override
        public SettableBeanProperty idProperty() {
            return null;
        }
    };

    @Test
    public void testObjectIdValuePropertyCreation() {
        ObjectIdValueProperty prop = new ObjectIdValueProperty(dummyObjectIdReader, null);
        assertEquals("id", prop.getName());
        assertEquals(ObjectIdType.STRING, prop.getIdType());
        assertNull(prop.getIdProperty());
        assertEquals(null, prop.getValueDeserializer());
    }

    @Test
    public void testObjectIdValuePropertyWithIdProperty() {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public ObjectIdType idType() {
                return ObjectIdType.INT;
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return ObjectIdGenerators.IntSequenceGenerator.instance;
            }

            @Override
            public ObjectIdResolver idResolver() {
                return null;
            }

            @Override
            public JsonDeserializer<ObjectIdNode> getIdDeserializer() {
                return new StdSerializer<ObjectIdNode>(ObjectIdNode.class) {};
            }

            @Override
            public SettableBeanProperty idProperty() {
                return new StdSettableBeanProperty(PropertyName.construct("id"), ObjectIdType.INT, null, null);
            }
        };

        ObjectIdValueProperty prop = new ObjectIdValueProperty(objectIdReader, null);
        assertEquals("id", prop.getName());
        assertEquals(ObjectIdType.INT, prop.getIdType());
        assertNotNull(prop.getIdProperty());
        assertNull(prop.getValueDeserializer());
    }

    @Test
    public void testObjectIdValuePropertyWithDeserializer() {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public ObjectIdType idType() {
                return ObjectIdType.STRING;
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return ObjectIdGenerators.IntSequenceGenerator.instance;
            }

            @Override
            public ObjectIdResolver idResolver() {
                return null;
            }

            @Override
            public JsonDeserializer<ObjectIdNode> getIdDeserializer() {
                return new StdSerializer<ObjectIdNode>(ObjectIdNode.class) {};
            }

            @Override
            public SettableBeanProperty idProperty() {
                return null;
            }

            @Override
            public JsonDeserializer<Object> getValueDeserializer() {
                return new StdSerializer<Object>(String.class) {};
            }
        };

        ObjectIdValueProperty prop = new ObjectIdValueProperty(objectIdReader, null);
        assertEquals("id", prop.getName());
        assertEquals(ObjectIdType.STRING, prop.getIdType());
        assertNull(prop.getIdProperty());
        assertNotNull(prop.getValueDeserializer());
    }

    @Test
    public void testObjectIdValuePropertyWithName() {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public ObjectIdType idType() {
                return ObjectIdType.STRING;
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return ObjectIdGenerators.IntSequenceGenerator.instance;
            }

            @Override
            public ObjectIdResolver idResolver() {
                return null;
            }

            @Override
            public JsonDeserializer<ObjectIdNode> getIdDeserializer() {
                return new StdSerializer<ObjectIdNode>(ObjectIdNode.class) {};
            }

            @Override
            public SettableBeanProperty idProperty() {
                return null;
            }

            @Override
            public JsonDeserializer<Object> getValueDeserializer() {
                return new StdSerializer<Object>(String.class) {};
            }
        };

        ObjectIdValueProperty prop = new ObjectIdValueProperty(objectIdReader, null);
        ObjectIdValueProperty newProp = prop.withName(PropertyName.construct("newId"));
        assertEquals("newId", newProp.getName());
        assertEquals("id", prop.getName());
    }

    @Test
    public void testObjectIdValuePropertyWithValueDeserializer() {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public ObjectIdType idType() {
                return ObjectIdType.STRING;
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return ObjectIdGenerators.IntSequenceGenerator.instance;
            }

            @Override
            public ObjectIdResolver idResolver() {
                return null;
            }

            @Override
            public JsonDeserializer<ObjectIdNode> getIdDeserializer() {
                return new StdSerializer<ObjectIdNode>(ObjectIdNode.class) {};
            }

            @Override
            public SettableBeanProperty idProperty() {
                return null;
            }

            @Override
            public JsonDeserializer<Object> getValueDeserializer() {
                return new StdSerializer<Object>(String.class) {};
            }
        };

        ObjectIdValueProperty prop = new ObjectIdValueProperty(objectIdReader, null);
        ObjectIdValueProperty newProp = prop.withValueDeserializer(new StdSerializer<Object>(Integer.class) {});
        assertSame(prop.getValueDeserializer(), newProp.getValueDeserializer());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public ObjectIdType idType() {
                return ObjectIdType.STRING;
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return ObjectIdGenerators.IntSequenceGenerator.instance;
            }

            @Override
            public ObjectIdResolver idResolver() {
                return null;
            }

            @Override
            public JsonDeserializer<ObjectIdNode> getIdDeserializer() {
                return new StdSerializer<ObjectIdNode>(ObjectIdNode.class) {};
            }

            @Override
            public SettableBeanProperty idProperty() {
                return new StdSettableBeanProperty(PropertyName.construct("id"), ObjectIdType.STRING, null, null);
            }

            @Override
            public JsonDeserializer<Object> getValueDeserializer() {
                return new StdSerializer<Object>(String.class) {};
            }
        };

        ObjectIdValueProperty prop = new ObjectIdValueProperty(objectIdReader, null);

        // Test deserialization and setting with null id
        Object testInstance = new Object();
        prop.deserializeAndSet(null, null, testInstance);
        assertNull(testInstance);

        // Test deserialization and setting with valid id
        JsonParser mockParser = new JsonTestParser();
        ObjectIdNode idNode = new ObjectIdNode(1L);
        mockParser.setValueAsId(idNode);
        Object testInstance2 = new Object();
        Object id = prop.deserializeAndSet(mockParser, null, testInstance2);
        assertEquals(1L, id.longValue());
        assertNotNull(testInstance2);
    }
}