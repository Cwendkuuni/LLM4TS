package com.fasterxml.jackson.databind.deser.impl.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdReader;
import com.fasterxml.jackson.databind.node.ObjectIdValueProperty;

import org.junit.Test;

/**
 * Test class for ObjectIdValueProperty
 */
public class ObjectIdValuePropertyTest {

    @Test
    public void testConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName getPropertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public Class<?> getIdType() {
                return String.class;
            }

            @Override
            public ObjectIdGenerator getGenerator() {
                return null;
            }

            @Override
            public ObjectIdResolver getResolver() {
                return null;
            }
        };

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
        assertNotNull(objectIdValueProperty);
    }

    @Test
    public void testWithName() {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName getPropertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public Class<?> getIdType() {
                return String.class;
            }

            @Override
            public ObjectIdGenerator getGenerator() {
                return null;
            }

            @Override
            public ObjectIdResolver getResolver() {
                return null;
            }
        };

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
        ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withName(PropertyName.construct("newId"));
        assertNotSame(objectIdValueProperty, newObjectIdValueProperty);
        assertEquals("newId", newObjectIdValueProperty.getName().asString());
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName getPropertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public Class<?> getIdType() {
                return String.class;
            }

            @Override
            public ObjectIdGenerator getGenerator() {
                return null;
            }

            @Override
            public ObjectIdResolver getResolver() {
                return null;
            }
        };
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
        JsonDeserializer<String> jsonDeserializer = new JsonDeserializer<String>(String.class) {};
        ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withValueDeserializer(jsonDeserializer);
        assertNotSame(objectIdValueProperty, newObjectIdValueProperty);
        assertEquals(jsonDeserializer, newObjectIdValueProperty.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName getPropertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public Class<?> getIdType() {
                return String.class;
            }

            @Override
            public ObjectIdGenerator getGenerator() {
                return null;
            }

            @Override
            public ObjectIdResolver getResolver() {
                return null;
            }
        };
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
        NullValueProvider nullValueProvider = new StandardNullValueProvider();
        ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withNullProvider(nullValueProvider);
        assertNotSame(objectIdValueProperty, newObjectIdValueProperty);
        assertEquals(nullValueProvider, newObjectIdValueProperty.getNullProvider());
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName getPropertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public Class<?> getIdType() {
                return String.class;
            }

            @Override
            public ObjectIdGenerator getGenerator() {
                return null;
            }

            @Override
            public ObjectIdResolver getResolver() {
                return null;
            }
        };
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
        assertNull(objectIdValueProperty.getAnnotation(Annotation.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName getPropertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public Class<?> getIdType() {
                return String.class;
            }

            @Override
            public ObjectIdGenerator getGenerator() {
                return null;
            }

            @Override
            public ObjectIdResolver getResolver() {
                return null;
            }
        };
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
        assertNull(objectIdValueProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName getPropertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public Class<?> getIdType() {
                return String.class;
            }

            @Override
            public ObjectIdGenerator getGenerator() {
                return null;
            }

            @Override
            public ObjectIdResolver getResolver() {
                return null;
            }
        };

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
        ObjectNode root = objectMapper.createObjectNode();
        root.put("id", "123");

        try {
            objectMapper.readerForMap().readValue(root);
            fail("Expected exception");
        } catch (IOException e) {
            // expected
        }

        Object node = objectMapper.readerFor(Object.class).readValue(root);
        ObjectIdValueProperty testedObjectIdValueProperty = (ObjectIdValueProperty) objectMapper.findValue(node, "id");
        Object id = "456";
        JsonParser jsonParser = objectMapper.getFactory().createParser(new ByteArrayInputStream(id.getBytes()));

        testedObjectIdValueProperty.deserializeAndSet(jsonParser, null, new Object());

        String actualId = null;
        try {
            actualId = (String) ReflectionTestUtils.getFieldValue(testedObjectIdValueProperty, "boundValue");
        } catch (IllegalAccessException e) {
            fail("Unexpected exception");
        }

        assertEquals("456", actualId);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName getPropertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public Class<?> getIdType() {
                return String.class;
            }

            @Override
            public ObjectIdGenerator getGenerator() {
                return null;
            }

            @Override
            public ObjectIdResolver getResolver() {
                return null;
            }
        };

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
        ObjectNode root = objectMapper.createObjectNode();
        root.put("id", "123");

        JsonParser jsonParser = objectMapper.getFactory().createParser(root);
        Object id = objectIdValueProperty.deserializeSetAndReturn(jsonParser, null, new Object());

        assertEquals("123", id.toString());
    }

    @Test
    public void testSet() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName getPropertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public Class<?> getIdType() {
                return String.class;
            }

            @Override
            public ObjectIdGenerator getGenerator() {
                return null;
            }

            @Override
            public ObjectIdResolver getResolver() {
                return null;
            }
        };

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
        ObjectNode root = objectMapper.createObjectNode();
        Object id = "123";

        Object node = objectMapper.readerFor(Object.class).readValue(root);
        ObjectIdValueProperty testedObjectIdValueProperty = (ObjectIdValueProperty) objectMapper.findValue(node, "id");

        testedObjectIdValueProperty.set(new Object(), id);

        String actualId = null;
        try {
            actualId = (String) ReflectionTestUtils.getFieldValue(testedObjectIdValueProperty, "boundValue");
        } catch (IllegalAccessException e) {
            fail("Unexpected exception");
        }

        assertEquals("123", actualId);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName getPropertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public Class<?> getIdType() {
                return String.class;
            }

            @Override
            public ObjectIdGenerator getGenerator() {
                return null;
            }

            @Override
            public ObjectIdResolver getResolver() {
                return null;
            }
        };

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
        ObjectNode root = objectMapper.createObjectNode();
        Object id = "123";

        Object node = objectMapper.readerFor(Object.class).readValue(root);
        ObjectIdValueProperty testedObjectIdValueProperty = (ObjectIdValueProperty) objectMapper.findValue(node, "id");

        Object result = testedObjectIdValueProperty.setAndReturn(new Object(), id);

        assertEquals(id, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetShouldNotCallSet() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName getPropertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public Class<?> getIdType() {
                return String.class;
            }

            @Override
            public ObjectIdGenerator getGenerator() {
                return null;
            }

            @Override
            public ObjectIdResolver getResolver() {
                return null;
            }
        };

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
        Object node = new Object();

        Object result = objectIdValueProperty.setAndReturn(node, null);

        fail("Unexpected return value");
    }
}