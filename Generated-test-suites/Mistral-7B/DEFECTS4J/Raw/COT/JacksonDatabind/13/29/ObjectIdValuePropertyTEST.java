package com.example.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.BeanUtil;

import org.junit.Test;

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
            public JsonDeserializer<ObjectIdValueProperty> idDeserializer() {
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
        };

        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty obj = new ObjectIdValueProperty(objectIdReader, metadata);
        assertNotNull(obj);
    }

    @Test
    public void testObjectIdValuePropertyCopyConstructor() {
        ObjectIdValueProperty obj = new ObjectIdValueProperty(null, null);
        ObjectIdValueProperty copy = new ObjectIdValueProperty(obj, null);
        assertEquals(obj, copy);
    }

    @Test
    public void testObjectIdValuePropertyDeprecatedConstructor() {
        ObjectIdValueProperty obj = new ObjectIdValueProperty(null, null);
        ObjectIdValueProperty copy = new ObjectIdValueProperty(obj, (PropertyName) null);
        assertEquals(obj, copy);
    }

    @Test
    public void testObjectIdValuePropertyDeprecatedConstructor2() {
        ObjectIdValueProperty obj = new ObjectIdValueProperty(null, null);
        ObjectIdValueProperty copy = new ObjectIdValueProperty(obj, "name");
        assertEquals(obj, copy);
    }

    @Test
    public void testObjectIdValuePropertyWithName() {
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
            public JsonDeserializer<ObjectIdValueProperty> idDeserializer() {
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
        };
        PropertyMetadata metadata = new PropertyMetadata();

        ObjectIdValueProperty obj = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectIdValueProperty newObj = obj.withName(new PropertyName("newName"));
        assertNotSame(obj, newObj);
        assertEquals(obj.getName(), obj.getName());
        assertEquals(newObj.getName(), "newName");
    }

    @Test
    public void testObjectIdValuePropertyWithValueDeserializer() {
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
            public JsonDeserializer<ObjectIdValueProperty> idDeserializer() {
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
        };

        PropertyMetadata metadata = new PropertyMetadata();
        JsonDeserializer<Object> jsonDeserializer = new JsonDeserializer<Object>() {};

        ObjectIdValueProperty obj = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectIdValueProperty newObj = obj.withValueDeserializer(jsonDeserializer);
        assertNotSame(obj, newObj);
        assertEquals(obj.getValueDeserializer(), obj.getValueDeserializer());
        assertEquals(newObj.getValueDeserializer(), jsonDeserializer);
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
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
            public JsonDeserializer<ObjectIdValueProperty> idDeserializer() {
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
        };

        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty obj = new ObjectIdValueProperty(objectIdReader, metadata);

        JsonParser jsonParser = new JsonFactory().createParser("{\"id\":1}");
        Object id = obj.deserializeSetAndReturn(jsonParser, null, null);
        assertEquals(Long.valueOf(1), id);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetThrowsException() throws Exception {
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
            public JsonDeserializer<ObjectIdValueProperty> idDeserializer() {
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
        };

        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty obj = new ObjectIdValueProperty(objectIdReader, metadata);
        obj.set(null, null);
    }

    @Test
    public void testGetAnnotation() {
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
            public JsonDeserializer<ObjectIdValueProperty> idDeserializer() {
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
        };

        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty obj = new ObjectIdValueProperty(objectIdReader, metadata);
        assertNull(obj.getAnnotation(Annotation.class));
    }

    @Test
    public void testGetMember() {
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
            public JsonDeserializer<ObjectIdValueProperty> idDeserializer() {
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
        };

        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty obj = new ObjectIdValueProperty(objectIdReader, metadata);
        assertNull(obj.getMember());
    }
}