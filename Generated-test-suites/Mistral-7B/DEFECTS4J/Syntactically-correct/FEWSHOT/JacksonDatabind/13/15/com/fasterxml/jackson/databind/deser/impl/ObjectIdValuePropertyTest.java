package com.fasterxml.jackson.databind.deser.impl;

import static com.fasterxml.jackson.core.util.TestUtils.convertObjectId;
import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonId;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

import java.io.IOException;
import java.lang.annotation.Annotation;

public class ObjectIdValuePropertyTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testObjectIdValueProperty() throws IOException {
        ObjectIdReader objectIdReader = new SimpleObjectIdReader(new JsonIdentifier());
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        // Test constructor
        assertEquals("objectIdProperty", property.getName());
        assertEquals(String.class, property.getType());
        assertNull(property.getMember());
        assertNull(property.getAnnotation(JsonId.class));
        assertEquals(ObjectIdReader.class, property._objectIdReader.getClass());
        assertEquals(JsonDeserializer.None.class, property.getValueDeserializer().getClass());

        // Test withName
        ObjectIdValueProperty newProperty = property.withName("newName");
        assertEquals("newName", newProperty.getName());
        assertEquals("objectIdProperty", property.getName());

        // Test withValueDeserializer
        JsonDeserializer<Object> deserializer = new StdDeserializer<Object>() {};
        ObjectIdValueProperty newPropertyWithDeserializer = property.withValueDeserializer(deserializer);
        assertEquals(deserializer, newPropertyWithDeserializer.getValueDeserializer());
        assertEquals(deserializer.getClass(), newPropertyWithDeserializer.getValueDeserializer().getClass());

        // Test deserializeAndSet and deserializeSetAndReturn
        Object id = "123";
        Object idInstance = new Object() {};
        JsonParser parser = createParser(id);

        property.deserializeAndSet(parser, new DeserializationContext(), idInstance);
        assertEquals(id, idInstance);

        Object deserializedId = property.deserializeSetAndReturn(parser, new DeserializationContext(), idInstance);
        assertEquals(id, deserializedId);

        // Test set and setAndReturn
        property.set(idInstance, id);
        assertEquals(id, idInstance);

        Object deserializedIdFromSet = property.setAndReturn(idInstance, id);
        assertEquals(id, deserializedIdFromSet);

        // Test deserialization with ObjectIdReader
        JsonParser parserWithObjectIdReader = createParser(id);
        ObjectIdReader objectIdReader2 = new SimpleObjectIdReader(new JsonIdentifier());
        ObjectIdValueProperty propertyWithObjectIdReader = new ObjectIdValueProperty(objectIdReader2, new PropertyMetadata());

        objectIdReader2.generator = new ObjectIdGenerator() {};
        objectIdReader2.resolver = new ObjectIdResolver() {};

        Object idInstanceWithObjectIdReader = new Object() {};
        propertyWithObjectIdReader.deserializeAndSet(parserWithObjectIdReader, new DeserializationContext(), idInstanceWithObjectIdReader);
        ReadableObjectId roid = propertyWithObjectIdReader.getObjectIdReader().getObjectId(parserWithObjectIdReader, new DeserializationContext());
        assertEquals(id, roid.getIdValue());
        assertEquals(idInstanceWithObjectIdReader, roid.getBindingProperty());
    }

    @Test
    public void testAnnotation() throws IOException {
        ObjectIdReader objectIdReader = new SimpleObjectIdReader(new JsonIdentifier());
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        JsonId jsonIdAnnotation = new JsonId();
        property.setAnnotation(JsonId.class, jsonIdAnnotation);

        assertEquals(jsonIdAnnotation, property.getAnnotation(JsonId.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdReader objectIdReader = new SimpleObjectIdReader(new JsonIdentifier());
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        assertNull(property.getMember());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetWithNoIdProperty() throws IOException {
        ObjectIdReader objectIdReader = new SimpleObjectIdReader(new JsonIdentifier());
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        Object idInstance = new Object() {};
        property.set(idInstance, "id");
    }

    @Test
    public void testSetWithIdProperty() throws IOException {
        ObjectIdReader objectIdReader = new SimpleObjectIdReader(new JsonIdentifier());
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        SettableBeanProperty idProperty = new SimpleSettableBeanProperty("idProperty", String.class);
        ObjectIdReader idPropertyReader = new SimpleObjectIdReader(new JsonIdentifier());

        property._objectIdReader.idProperty = idProperty;
        idProperty._objectIdReader = idPropertyReader;

        Object idInstance = new Object() {};
        property.set(idInstance, "id");
        assertEquals("id", idInstance.getClass().getDeclaredField("idProperty").get(idInstance));
    }

    private JsonParser createParser(Object id) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser parser = objectMapper.getFactory().createParser(ClassUtil.writeValueAsString(id));
        parser.setCodec(objectMapper);
        return parser;
    }
}