package com.fasterxml.jackson.databind.deser.impl.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdReader;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

import org.junit.Test;

public class ObjectIdValuePropertyTest {

    @Test
    public void testConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public Class<?> getIdType() {
                return String.class;
            }

            @Override
            public AnnotatedMember getIdProperty() {
                return null;
            }

            @Override
            public ObjectIdGenerator<?> getGenerator() {
                return new ObjectIdGenerator() {};
            }

            @Override
            public ObjectIdResolver getResolver() {
                return new ObjectIdResolver() {};
            }
        };

        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        assertNotNull(objectIdValueProperty);
        assertEquals(objectIdReader.propertyName, objectIdValueProperty.getName());
        assertEquals(String.class, objectIdValueProperty.getBeanType());
        assertNull(objectIdValueProperty.getAnnotations());
        assertEquals(metadata, objectIdValueProperty.getMetadata());
        assertEquals(objectIdReader, objectIdValueProperty._objectIdReader);
    }

    @Test
    public void testWithName() {
        ObjectIdReader objectIdReader = new ObjectIdReader() {};
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyName("test"));
        assertEquals(new PropertyName("test"), objectIdValueProperty.getName());
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdReader objectIdReader = new ObjectIdReader() {};
        JsonDeserializer<Object> valueDeserializer = new StdSerializer<Object>(Object.class) {};
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, valueDeserializer, null);
        assertEquals(valueDeserializer, objectIdValueProperty.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() {
        ObjectIdReader objectIdReader = new ObjectIdReader() {};
        NullValueProvider nullValueProvider = new NullValueProvider() {};
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null, nullValueProvider);
        assertEquals(nullValueProvider, objectIdValueProperty.getNullProvider());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetNullProperty() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader() {};
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object instance = new Object();
        objectIdValueProperty.set(instance, null);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public Class<?> getIdType() {
                return String.class;
            }

            @Override
            public AnnotatedMember getIdProperty() {
                return null;
            }

            @Override
            public ObjectIdGenerator<?> getGenerator() {
                return new ObjectIdGenerator() {};
            }

            @Override
            public ObjectIdResolver getResolver() {
                return new ObjectIdResolver() {};
            }
        };

        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);

        Object id = new Object();
        ObjectIdReader idReader = new ObjectIdReader() {};
        ObjectIdGenerator<?> objectIdGenerator = new ObjectIdGenerator() {};
        ObjectIdResolver objectIdResolver = new ObjectIdResolver() {};
        ReadableObjectId roid = new ObjectIdReaderShallowBase() {};
        roid.bindItem(new Object());

        JsonParser p = new JsonFactory().createParser("{\"id\":\"123\"}");
        DeserializationContext ctxt = new TestContext();
        ctxt.findObjectId(id, idReader, objectIdGenerator, objectIdResolver);
        ctxt.setContextHandler(new ObjectIdContextHandler(roid));

        Object instance = new Object();
        objectIdValueProperty.deserializeAndSet(p, ctxt, instance);
        assertEquals("123", roid.getId());
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public Class<?> getIdType() {
                return String.class;
            }

            @Override
            public AnnotatedMember getIdProperty() {
                return null;
            }

            @Override
            public ObjectIdGenerator<?> getGenerator() {
                return new ObjectIdGenerator() {};
            }

            @Override
            public ObjectIdResolver getResolver() {
                return new ObjectIdResolver() {};
            }
        };

        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        Object instance = new Object();

        JsonParser p = new JsonFactory().createParser("{\"id\":null}");
        DeserializationContext ctxt = new TestContext();
        Object id = objectIdValueProperty.deserializeSetAndReturn(p, ctxt, instance);
        assertNull(id);

        p = new JsonFactory().createParser("{\"id\":\"123\"}");
        id = objectIdValueProperty.deserializeSetAndReturn(p, ctxt, instance);
        assertEquals("123", id.toString());
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader() {};
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        Object instance = new Object();
        Object id = new Object();
        ObjectIdReader idReader = new ObjectIdReader() {};
        ObjectIdGenerator<?> objectIdGenerator = new ObjectIdGenerator() {};
        ObjectIdResolver objectIdResolver = new ObjectIdResolver() {};
        ReadableObjectId roid = new ObjectIdReaderShallowBase() {};
        roid.bindItem(instance);

        SettableBeanProperty idProp = new StdSettableBeanProperty("id", String.class, new PropertyMetadata());
        Object idSet = idProp.setAndReturn(instance, id);
        assertEquals(id, idSet);
        assertEquals(id, roid.getId());
    }
}

class ObjectIdContextHandler implements ContextHandler {
    private final ReadableObjectId _id;

    public ObjectIdContextHandler(ReadableObjectId id) {
        _id = id;
    }

    @Override
    public void handleContextualization(DeserializationContext ctxt) throws JavaLangException, IOException {
        ctxt.putContextValue("_OBJECT_ID", _id);
    }
}

class ObjectIdGenerator implements ObjectIdGenerator<Object> {
}

class ObjectIdResolver implements ObjectIdResolver {
}

class ObjectIdReaderShallowBase extends ReadableObjectId {
    private static final long serialVersionUID = 1L;
    private String _id;

    @Override
    public ObjectIdType getIdType() {
        return ObjectIdType.STRING;
    }

    @Override
    public String getId() {
        return _id;
    }

    @Override
    public void bindItem(Object item) {
        _id = ClassUtil.classNameIdentifier(item.getClass());
    }

    @Override
    public void bindValue(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (p.hasToken(JsonToken.VALUE_STRING)) {
            _id = p.getValueAsString();
        }
    }
}

class TestContext extends DeserializationContext {
    @Override
    public ObjectMapper getContextualObjectMapper() {
        return new ObjectMapper();
    }

    @Override
    public ObjectReader getContextualObjectReader() {
        return new ObjectMapper().readerForUpdating(Object.class);
    }

    @Override
    public Object readValueAs(Class<?> valueType, Converter converter) throws IOException {
        return null;
    }
}