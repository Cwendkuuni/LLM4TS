package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdReader;
import com.fasterxml.jackson.databind.node.ObjectIdReader.IdReaderType;
import com.fasterxml.jackson.databind.node.ObjectIdReader.ObjectIdResolver;

public class ObjectIdValuePropertyTest {

    @Test
    public void testObjectIdValuePropertyCreation() {
        IdReaderType idReaderType = IdReaderType.CLASS;
        ObjectIdReader objectIdReader = new StandardObjectIdReader(idReaderType);
        PropertyMetadata metadata = new StandardPropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        assertNotNull(property);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetAnnotationThrowsException() {
        ObjectIdReader objectIdReader = new StandardObjectIdReader(IdReaderType.CLASS);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        property.getAnnotation(JsonProperty.class);
    }

    @Test
    public void testSettableBeanProperty() {
        IdReaderType idReaderType = IdReaderType.CLASS;
        ObjectIdReader objectIdReader = new StandardObjectIdReader(idReaderType);
        PropertyMetadata metadata = new StandardPropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        assertNotNull(property.getName());
        assertEquals(metadata, property.getMetadata());
        assertNull(property.getValueDeserializer());
    }

    @Test
    public void testSetValueDeserializer() {
        IdReaderType idReaderType = IdReaderType.CLASS;
        ObjectIdReader objectIdReader = new StandardObjectIdReader(idReaderType);
        PropertyMetadata metadata = new StandardPropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        JsonDeserializer<String> valueDeserializer = new StdDeserializer<String>(String.class) {};
        ObjectIdValueProperty propertyWithDeserializer = property.withValueDeserializer(valueDeserializer);
        assertEquals(valueDeserializer, propertyWithDeserializer.getValueDeserializer());
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        IdReaderType idReaderType = IdReaderType.CLASS;
        ObjectIdReader objectIdReader = new StandardObjectIdReader(idReaderType);
        PropertyMetadata metadata = new StandardPropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectIdResolver objectIdResolver = new StandardObjectIdResolver();
        JsonParser jsonParser = new TestJsonParser();
        Object object = new Object();

        JsonDeserializer<ObjectId> objectIdDeserializer = new StdDeserializer<ObjectId>(ObjectId.class) {};
        property._valueDeserializer = objectIdDeserializer;

        String json = "{\"id\":1}";
        jsonParser.setValueAsString("id", "1");
        property.deserializeAndSet(jsonParser, new JsonDeserializationContext(), object);

        ObjectId objectId = property.get(_objectIdReader, object);
        assertEquals(1, objectId.id);
    }

    @Test
    public void testSetAndReturn() throws Exception {
        IdReaderType idReaderType = IdReaderType.CLASS;
        ObjectIdReader objectIdReader = new StandardObjectIdReader(idReaderType);
        PropertyMetadata metadata = new StandardPropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectIdResolver objectIdResolver = new StandardObjectIdResolver();
        JsonParser jsonParser = new TestJsonParser();
        Object object = new Object();

        JsonDeserializer<ObjectId> objectIdDeserializer = new StdDeserializer<ObjectId>(ObjectId.class) {};
        property._valueDeserializer = objectIdDeserializer;

        String json = "{\"id\":1}";
        jsonParser.setValueAsString("id", "1");
        Object returnedObject = property.setAndReturn(object, jsonParser.getValueAsString("id"));
        ObjectId objectId = property.get(_objectIdReader, returnedObject);
        assertEquals(1, objectId.id);
    }
}

class TestJsonParser extends JsonParser {
    public void setValueAsString(String name, String value) {
        ObjectCodec objectMapper = new ObjectMapper();
        ObjectReader reader = objectMapper.getReader();
        ValueInput valueInput = new ValueInputSource(new StringReader(json));
        ObjectNode node = (ObjectNode) reader.readTree(valueInput);
        currentToken = node.nextToken();
        _valueString = value;
    }
}

class StandardObjectIdReader implements ObjectIdReader {
    private final IdReaderType _idReaderType;

    public StandardObjectIdReader(IdReaderType idReaderType) {
        _idReaderType = idReaderType;
    }

    @Override
    public IdReaderType getIdReaderType() {
        return _idReaderType;
    }
}

class StandardObjectIdResolver implements ObjectIdResolver {
    @Override
    public ReadableObjectId resolveId(ObjectId id, ObjectIdGenerator generator) {
        return new ReadableObjectId() {
            private final int id;

            public StandardObjectIdResolver(int id) {
                this.id = id;
            }

            @Override
            public int getId() {
                return id;
            }

            @Override
            public void bindItem(Object idObject) {
                // empty
            }
        };
    }
}

class ObjectId {
    public int id;
}

class StdDeserializer<T> extends JsonDeserializer<T> {
    public StdDeserializer(Class<T> tClass) {
        this(null, tClass);
    }

    public StdDeserializer(JsonDeserializer<T> deserializer, Class<T> tClass) {
        super(tClass);
        this._valueDeserializer = deserializer;
    }
}