package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.annotation.JsonId;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.StandardDeserializationProblemHandler;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ObjectIdValuePropertyTest {

    private ObjectMapper mapper;
    private ObjectIdReader objectIdReader;
    private ObjectIdValueProperty property;

    @Before
    public void setup() throws Exception {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.setAnnotationIntrospector(new NopAnnotationIntrospector());

        ObjectReader reader = mapper.readerForClass(MyPojo.class);
        objectIdReader = (ObjectIdReader) reader.findProperty("id");
        property = (ObjectIdValueProperty) objectIdReader.getMember();
    }

    @Test
    public void testDeserializeAndSetNull() throws IOException {
        MyPojo pojo = mapper.readValue(new ByteArrayInputStream(new byte[0]), MyPojo.class);
        assertNull(property.getValue(pojo));
    }

    @Test
    public void testDeserializeAndSetValue() throws IOException {
        MyPojo pojo = new MyPojo();
        String idValue = "someIdValue";

        JsonParser parser = mapper.getFactory().createParser(new ByteArrayInputStream(idValue.getBytes()));
        DeserializationContext ctxt = mapper.getDeserializationContext();
        pojo = property.deserializeAndSet(parser, ctxt, pojo);

        ObjectNode idNode = (ObjectNode) parser.readValueAsTree();
        assertEquals(idValue, property.getValue(pojo).toString());
        assertEquals(idNode.toString(), idValue);
    }

    @Test
    public void testDeserializeAndSetValueWithEmptyJsonNode() throws IOException {
        MyPojo pojo = property.deserializeAndSet(null, null, new MyPojo());
        assertNull(property.getValue(pojo));
    }

    @Test
    public void testDeserializeAndSetValueWithInvalidJsonNode() {
        String jsonString = "{ \"invalid\": \"json\" }";
        MyPojo pojo = mapper.readValue(new ByteArrayInputStream(jsonString.getBytes()), MyPojo.class);
        try {
            property.deserializeAndSet(mapper.getFactory().createParser(new ByteArrayInputStream(jsonString.getBytes())), null, pojo);
            fail();
        } catch (JsonProcessingException e) {
            // expected
        }
        assertNull(property.getValue(pojo));
    }

    @Test
    public void testSetValue() throws IOException {
        MyPojo pojo = new MyPojo();
        String idValue = "someIdValue";

        property.setValue(pojo, idValue);
        assertEquals(idValue, property.getValue(pojo).toString());
    }

    @Test
    public void testSetValueWithNull() {
        MyPojo pojo = new MyPojo();
        property.setValue(pojo, null);
        assertNull(property.getValue(pojo));
    }

    @Test
    public void testSetValueWithInvalidType() {
        MyPojo pojo = new MyPojo();
        int idValue = 42;

        try {
            property.setValue(pojo, idValue);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
        assertNull(property.getValue(pojo));
    }

    @Test
    public void testGetAnnotation() {
        JsonId idAnnotation = property.getAnnotation(JsonId.class);
        assertNotNull(idAnnotation);
    }

    @Test
    public void testGetMember() {
        AnnotatedMember member = property.getMember();
        assertNotNull(member);
    }

    @Test
    public void testWithName() {
        BeanPropertyDefinition newPropertyDefinition = new BeanPropertyDefinition("newName", String.class, null, null, null, null, null, null, null, null, null, null);
        ObjectIdValueProperty newProperty = property.withName(new PropertyName("newName"));
        assertEquals(newPropertyDefinition, newProperty.getMember().getBeanPropertyDefinition());
    }

    @Test
    public void testWithValueDeserializer() {
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class) {};
        ObjectIdValueProperty newProperty = property.withValueDeserializer(stringSerializer);
        assertEquals(stringSerializer, newProperty.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nva = new NullValueProvider() {};
        ObjectIdValueProperty newProperty = property.withNullProvider(nva);
        assertEquals(nva, newProperty.getNullProvider());
    }

    @Test
    public void testFindObjectId() throws IOException {
        ObjectNode pojoNode = mapper.createObjectNode();
        pojoNode.put("id", "idValue");

        MyPojo pojo = mapper.readerForValue(pojoNode).readValueAs(MyPojo.class);
        ReadableObjectId id = objectIdReader.findObjectId("idValue", objectIdReader.generator, objectIdReader.resolver);
        assertEquals("idValue", id.getIdValue());
        assertEquals(pojo, id.getObject());
    }

    @Test
    public void testFindObjectIdWithNullId() {
        ReadableObjectId id = objectIdReader.findObjectId(null, objectIdReader.generator, objectIdReader.resolver);
        assertNull(id);
    }

    @Test
    public void testFindObjectIdWithInvalidId() {
        ReadableObjectId id = objectIdReader.findObjectId("invalidId", objectIdReader.generator, objectIdReader.resolver);
        assertNull(id);
    }

    @Test
    public void testFindObjectIdWithNotFoundId() {
        ReadableObjectId id = objectIdReader.findObjectId("notFoundId", objectIdReader.generator, objectIdReader.resolver);
        assertNull(id);
    }

    @Test
    public void testBindItem() throws IOException {
        ObjectNode pojoNode = mapper.createObjectNode();
        pojoNode.put("id", "idValue");

        MyPojo pojo = mapper.readerForValue(pojoNode).readValueAs(MyPojo.class);
        ReadableObjectId id = objectIdReader.findObjectId("idValue", objectIdReader.generator, objectIdReader.resolver);
        id.bindItem(pojo);

        assertEquals("MyPojo{id='idValue'}", pojo.toString());
    }

    @Test
    public void testBindItemWithNullId() {
        ReadableObjectId id = objectIdReader.findObjectId(null, objectIdReader.generator, objectIdReader.resolver);
        MyPojo pojo = new MyPojo();
        id.bindItem(pojo);
        assertNull(property.getValue(pojo));
    }

    @Test
    public void testBindItemWithInvalidId() {
        ObjectNode pojoNode = mapper.createObjectNode();
        pojoNode.put("invalid", "json");

        ReadableObjectId id = objectIdReader.findObjectId("invalid", objectIdReader.generator, objectIdReader.resolver);
        MyPojo pojo = new MyPojo();
        id.bindItem(pojo);
        assertNull(property.getValue(pojo));
    }

    @Test
    public void testBindItemWithNotFoundId() {
        ObjectNode pojoNode = mapper.createObjectNode();
        pojoNode.put("notFoundId", "json");

        ReadableObjectId id = objectIdReader.findObjectId("notFoundId", objectIdReader.generator, objectIdReader.resolver);
        MyPojo pojo = new MyPojo();
        id.bindItem(pojo);
        assertNull(property.getValue(pojo));
    }

    @Test
    public void testSetAndReturn() throws IOException {
        MyPojo pojo = new MyPojo();
        String idValue = "someIdValue";

        Object value = property.setAndReturn(pojo, idValue);
        assertEquals(idValue, property.getValue(pojo).toString());
        assertEquals(idValue, value.toString());
    }

    @Test
    public void testSetAndReturnWithNull() {
        MyPojo pojo = new MyPojo();
        property.setAndReturn(pojo, null);
        assertNull(property.getValue(pojo));
    }

    @Test
    public void testSetAndReturnWithInvalidType() {
        MyPojo pojo = new MyPojo();
        int idValue = 42;

        try {
            property.setAndReturn(pojo, idValue);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
        assertNull(property.getValue(pojo));
    }

    @Test
    public void testSetAndReturnWithBindingError() throws IOException {
        ObjectMapper mapperWithProblemHandler = new ObjectMapper();
        mapperWithProblemHandler.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapperWithProblemHandler.setAnnotationIntrospector(new NopAnnotationIntrospector());
        mapperWithProblemHandler.setDefaultPropertyValueInsetter(new StdSerializer<Object>(Object.class) {});
        mapperWithProblemHandler.setDefaultTyping(new StandardDeserializationProblemHandler());

        ObjectNode pojoNode = mapperWithProblemHandler.createObjectNode();

        try {
            property.setAndReturn(mapperWithProblemHandler.readerForValue(pojoNode).readValueAs(MyPojo.class), "invalidJson");
            fail();
        } catch (JsonProcessingException e) {
            // expected
        }
        assertNull(property.getValue(mapperWithProblemHandler.readerForValue(pojoNode).readValueAs(MyPojo.class)));
    }
}

class MyPojo {
    private String id;

    @JsonId
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}