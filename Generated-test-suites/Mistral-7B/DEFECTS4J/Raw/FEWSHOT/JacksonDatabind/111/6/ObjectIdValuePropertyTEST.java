package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonId;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public class ObjectIdValuePropertyTest {

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public Class<?> idType() {
                return Long.class;
            }

            @Override
            public JsonDeserializer<ObjectIdReader> idDeserializer() {
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
                return null;
            }
        };

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());

        JsonParser parser = new TestJsonParser();
        DeserializationContext ctxt = new TestDeserializationContext();
        Object instance = new Object();

        parser.nextToken(); // consume [START_OBJECT]

        parser.nextToken(); // consume "id": null
        property.deserializeAndSet(parser, ctxt, instance);
        assertEquals(null, instance.getClass().getDeclaredField("id").get(instance));

        parser.nextToken(); // consume "id": 1
        parser.nextToken(); // consume "name": "John Doe"
        Object id = property.deserializeSetAndReturn(parser, ctxt, instance);
        assertEquals(1, instance.getClass().getDeclaredField("id").get(instance));

        parser.nextToken(); // consume "name": "Jane Doe"
        assertNull(property.deserializeSetAndReturn(parser, ctxt, instance));
        assertNull(instance.getClass().getDeclaredField("id").get(instance));
    }

    @Test
    public void testSetAndReturn() throws Exception {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public Class<?> idType() {
                return Long.class;
            }

            @Override
            public JsonDeserializer<ObjectIdReader> idDeserializer() {
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
                return new SettableBeanProperty("id", LongTypeDeserializer.instance, PropertyMetadata.empty()) {
                    @JsonId
                    private static final long serialVersionUID = 1L;

                    @Override
                    public Object setAndReturn(Object instance, Object value) throws IOException {
                        Object fieldValue = instance.getClass().getDeclaredField("id").get(instance);
                        instance.getClass().getDeclaredField("id").set(instance, value);
                        return fieldValue;
                    }
                };
            }
        };

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());

        Object instance = new Object();
        long id = 1;

        assertEquals(null, property.setAndReturn(instance, null));
        assertNull(instance.getClass().getDeclaredField("id").get(instance));

        assertEquals(id, property.setAndReturn(instance, id));
        assertEquals(id, instance.getClass().getDeclaredField("id").get(instance));
    }

    @Test
    public void testSet() throws Exception {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public Class<?> idType() {
                return Long.class;
            }

            @Override
            public JsonDeserializer<ObjectIdReader> idDeserializer() {
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
                return new SettableBeanProperty("id", LongTypeDeserializer.instance, PropertyMetadata.empty()) {
                    @JsonId
                    private static final long serialVersionUID = 1L;
                };
            }
        };

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());

        Object instance = new Object();
        long id = 1;

        property.set(instance, id);
        assertEquals(id, instance.getClass().getDeclaredField("id").get(instance));
    }

    @Test
    public void testWithName() {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("oldId");
            }

            @Override
            public Class<?> idType() {
                return Long.class;
            }

            @Override
            public JsonDeserializer<ObjectIdReader> idDeserializer() {
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
                return null;
            }
        };

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());

        PropertyName newName = PropertyName.construct("newId");
        ObjectIdValueProperty newProperty = property.withName(newName);

        assertEquals(newName, newProperty.getName());
        assertEquals(property.getClass(), newProperty.getClass());
        assertEquals(property._objectIdReader, newProperty._objectIdReader);
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public Class<?> idType() {
                return Long.class;
            }

            @Override
            public JsonDeserializer<ObjectIdReader> idDeserializer() {
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
                return null;
            }
        };

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
        JsonDeserializer<String> deserializer = new StandardDeserializer<String>(String.class) {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getValueAsString();
            }
        };

        ObjectIdValueProperty newProperty = property.withValueDeserializer(deserializer);

        assertEquals(deserializer, newProperty._valueDeserializer);
        assertEquals(property.getClass(), newProperty.getClass());
        assertEquals(property._objectIdReader, newProperty._objectIdReader);
    }

    @Test
    public void testWithNullProvider() {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public Class<?> idType() {
                return Long.class;
            }

            @Override
            public JsonDeserializer<ObjectIdReader> idDeserializer() {
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
                return null;
            }
        };

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
        NullValueProvider nva = new NullValueProvider() {
            @Override
            public Object findValueForNullInput(DeserializationContext ctxt) {
                return null;
            }
        };

        ObjectIdValueProperty newProperty = property.withNullProvider(nva);

        assertEquals(nva, newProperty._nullProvider);
        assertEquals(property.getClass(), newProperty.getClass());
        assertEquals(property._objectIdReader, newProperty._objectIdReader);
    }
}

class TestJsonParser extends JsonParser {
    @Override
    public JsonToken nextToken() throws IOException {
        return JsonToken.START_OBJECT;
    }
}

class TestDeserializationContext extends DeserializationContext {
    @Override
    public ObjectIdReader findObjectId(Object id, ObjectIdGenerator generator, ObjectIdResolver resolver) {
        return null;
    }

    @Override
    public Object findValueForProperty(DeserializationConfig config, BeanProperty property, JsonParser p)
            throws IOException {
        return null;
    }

    @Override
    public Object instantiateValue(Class<?> valueClass, JsonParser p, DeserializationContext ctxt)
            throws IOException {
        return null;
    }

    @Override
    public void reportFindFails(DeserializationConfig config, BeanDescription beanDesc,
            String propertyName, ObjectIdReader idReader) {
    }

    @Override
    public void reportFindFails(DeserializationConfig config, BeanDescription beanDesc,
            String propertyName, JsonDeserializer<?> deserializer) {
    }

    @Override
    public void reportFindFails(DeserializationConfig config, String msg) {
    }
}

class ObjectIdReader {
    private static final long serialVersionUID = 1L;

    public PropertyName propertyName() {
        return null;
    }

    public Class<?> idType() {
        return null;
    }

    public JsonDeserializer<ObjectIdReader> idDeserializer() {
        return null;
    }

    public ObjectIdGenerator idGenerator() {
        return null;
    }

    public ObjectIdResolver idResolver() {
        return null;
    }

    public SettableBeanProperty idProperty() {
        return null;
    }
}

class Object {
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

class StandardDeserializer<T> extends JsonDeserializer<T> {
    private final Class<T> _valueClass;

    public StandardDeserializer(Class<T> valueClass) {
        this._valueClass = valueClass;
    }

    @Override
    public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return p.getValueAsString();
    }
}

class NullValueProvider {
    @Override
    public Object findValueForNullInput(DeserializationContext ctxt) {
        return null;
    }
}

class LongTypeDeserializer extends JsonDeserializer<Long> {
    private static final LongTypeDeserializer instance = new LongTypeDeserializer();

    private LongTypeDeserializer() {}

    @Override
    public Long deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return p.getValueAsLong();
    }
}