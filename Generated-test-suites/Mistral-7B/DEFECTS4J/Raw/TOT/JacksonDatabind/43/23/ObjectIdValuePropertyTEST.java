package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectIdReader;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.test.BaseMapTest;
import com.fasterxml.jackson.databind.util.BeanUtil;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class ObjectIdValuePropertyTest extends BaseMapTest {

    private ObjectIdReader objectIdReader;
    private ObjectIdValueProperty objectIdValueProperty;
    private JsonDeserializer<Object> objectDeserializer;
    private SettableBeanProperty idProperty;

    @Before
    public void setup() throws Exception {
        objectIdReader = mock(ObjectIdReader.class);
        when(objectIdReader.propertyName).thenReturn(PropertyName.construct("id"));
        when(objectIdReader.getIdType()).thenReturn(Integer.class);
        when(objectIdReader.getDeserializer()).thenReturn(objectDeserializer);
        when(objectIdReader.generator).thenReturn(new ObjectIdGenerator());
        when(objectIdReader.resolver).thenReturn(new ObjectIdResolver());

        objectDeserializer = mock(JsonDeserializer.class);
        when(objectDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenAnswer(i -> 1);

        idProperty = mock(SettableBeanProperty.class);

        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(true));
    }

    @Test
    public void testDeserializeAndSet() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectParser parser = mapper.parser();

        Object instance = new Object();

        parser.setValue("1", objectIdValueProperty);

        objectIdValueProperty.deserializeAndSet(parser.getParsingContext(), mapper.getDeserializationContext(), instance);

        verify(objectIdReader).getIdType();
        verify(objectDeserializer).deserialize(parser.getParsingContext(), mapper.getDeserializationContext());

        Object id = verify(objectDeserializer).deserialize(parser.getParsingContext(), mapper.getDeserializationContext());
        Assert.assertEquals(1, id);
        Assert.assertNotNull(parser.getCurrentName());
        Assert.assertEquals("id", parser.getCurrentName().asString());

        ObjectIdGenerator objectIdGenerator = new ObjectIdGenerator();
        ObjectIdResolver objectIdResolver = new ObjectIdResolver();
        ObjectId idObject = objectIdGenerator.generateId(parser.getText(), ObjectIdValueProperty.class, objectIdResolver);
        ObjectNode rootNode = mapper.readValue(parser, ObjectNode.class);

        ReadableObjectId readableObjectId = mapper.getDeserializationContext().findObjectId(idObject, objectIdGenerator, objectIdResolver);
        readableObjectId.bindItem(instance);

        verify(idProperty).setAndReturn(instance, id);
    }

    @Test
    public void testDeserializeSetAndReturn() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectParser parser = mapper.parser();

        Object instance = new Object();

        Object id = objectIdValueProperty.deserializeSetAndReturn(parser.getParsingContext(), mapper.getDeserializationContext(), instance);

        Assert.assertEquals(1, id);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws IOException {
        Object instance = new Object();

        objectIdValueProperty.set(instance, "2");
    }

    @Test
    public void testGetAnnotation() {
        Assert.assertNull(objectIdValueProperty.getAnnotation(JsonDeserializer.class));
    }

    @Test
    public void testGetMember() {
        Assert.assertNull(objectIdValueProperty.getMember());
    }
}