package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdReader;

import org.junit.Test;

public class ObjectIdValuePropertyTest {

    @Test
    public void testObjectIdValuePropertyConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("objectId");
            }

            @Override
            public Class<Object> idType() {
                return String.class;
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return new ObjectIdGenerator() {};
            }

            @Override
            public ObjectIdResolver idResolver() {
                return new ObjectIdResolver() {};
            }
        };

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata() {});
        assertNotNull(objectIdValueProperty);
        assertEquals("objectId", objectIdValueProperty.getName());
        assertNotNull(objectIdValueProperty._objectIdReader);
    }

    @Test
    public void testObjectIdValuePropertyCopyConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("objectId");
            }

            @Override
            public Class<Object> idType() {
                return String.class;
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return new ObjectIdGenerator() {};
            }

            @Override
            public ObjectIdResolver idResolver() {
                return new ObjectIdResolver() {};
            }
        };

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata() {});
        ObjectIdValueProperty copiedObjectIdValueProperty = new ObjectIdValueProperty(objectIdValueProperty, new JsonDeserializer<Object>() {}, new NullValueProvider() {});
        assertNotNull(copiedObjectIdValueProperty);
        assertEquals(objectIdValueProperty.getName(), copiedObjectIdValueProperty.getName());
        assertEquals(objectIdValueProperty._objectIdReader, copiedObjectIdValueProperty._objectIdReader);
    }

    @Test
    public void testObjectIdValuePropertyNameChangeConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("objectId");
            }

            @Override
            public Class<Object> idType() {
                return String.class;
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return new ObjectIdGenerator() {};
            }

            @Override
            public ObjectIdResolver idResolver() {
                return new ObjectIdResolver() {};
            }
        };

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata() {});
        ObjectIdValueProperty renamedObjectIdValueProperty = new ObjectIdValueProperty(objectIdValueProperty, new PropertyName("newName"));
        assertNotNull(renamedObjectIdValueProperty);
        assertEquals("newName", renamedObjectIdValueProperty.getName());
        assertEquals(objectIdValueProperty._objectIdReader, renamedObjectIdValueProperty._objectIdReader);
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("objectId");
            }

            @Override
            public Class<Object> idType() {
                return String.class;
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return new ObjectIdGenerator() {};
            }

            @Override
            public ObjectIdResolver idResolver() {
                return new ObjectIdResolver() {};
            }
        };

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata() {});
        assertNull(objectIdValueProperty.getAnnotation(Annotation.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("objectId");
            }

            @Override
            public Class<Object> idType() {
                return String.class;
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return new ObjectIdGenerator() {};
            }

            @Override
            public ObjectIdResolver idResolver() {
                return new ObjectIdResolver() {};
            }
        };

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata() {});
        assertNull(objectIdValueProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("objectId");
            }

            @Override
            public Class<Object> idType() {
                return String.class;
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return new ObjectIdGenerator() {};
            }

            @Override
            public ObjectIdResolver idResolver() {
                return new ObjectIdResolver() {};
            }
        };

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"objectId\": \"123\"}");
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata() {});
        Object testInstance = new Object();

        objectIdValueProperty.deserializeAndSet(mapper.parser(), mapper.getDeserializationContext(), testInstance);

        assertEquals("123", objectIdValueProperty.get(testInstance));
    }

    @Test
    public void testDeserializeSetAndReturnNull() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("objectId");
            }

            @Override
            public Class<Object> idType() {
                return String.class;
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return new ObjectIdGenerator() {};
            }

            @Override
            public ObjectIdResolver idResolver() {
                return new ObjectIdResolver() {};
            }
        };

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata() {});
        Object testInstance = new Object();

        Object result = objectIdValueProperty.deserializeSetAndReturn(mapper.parser(), mapper.getDeserializationContext(), testInstance);

        assertNull(result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetNullInstance() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("objectId");
            }

            @Override
            public Class<Object> idType() {
                return String.class;
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return new ObjectIdGenerator() {};
            }

            @Override
            public ObjectIdResolver idResolver() {
                return new ObjectIdResolver() {};
            }
        };

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata() {});
        objectIdValueProperty.set(null, "123");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetNullValue() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("objectId");
            }

            @Override
            public Class<Object> idType() {
                return String.class;
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return new ObjectIdGenerator() {};
            }

            @Override
            public ObjectIdResolver idResolver() {
                return new ObjectIdResolver() {};
            }
        };

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata() {});
        Object testInstance = new Object();

        objectIdValueProperty.set(testInstance, null);
    }
}