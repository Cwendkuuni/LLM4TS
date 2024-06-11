package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader.IdType;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ObjectIdValuePropertyTest {

    private ObjectMapper objectMapper;
    private ObjectIdReader objectIdReader;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectIdReader = Mockito.mock(ObjectIdReader.class);
    }

    @Test
    public void testNullDeserialization() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_INSTANCES);
        JsonDeserializer<Object> deserializer = Mockito.mock(JsonDeserializer.class);
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        Object instance = new Object();

        Mockito.when(parser.getValueAsToken()).thenReturn(JsonToken.VALUE_NULL);
        Mockito.when(context.findValueDeserializer(JsonDeserializer.class, Object.class)).thenReturn(deserializer);
        Mockito.when(context.findObjectId(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(Mockito.mock(ReadableObjectId.class));

        property.deserializeAndSet(parser, context, instance);
        assertNull(instance.getClass().getDeclaredField("id").get(instance));

        Mockito.verify(deserializer, Mockito.never()).deserialize(parser, context);
    }

    @Test
    public void testEmptyStringDeserialization() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_INSTANCES);
        JsonDeserializer<Object> deserializer = Mockito.mock(JsonDeserializer.class);
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        Object instance = new Object();

        Mockito.when(parser.getText()).thenReturn("");
        Mockito.when(parser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
        Mockito.when(context.findValueDeserializer(JsonDeserializer.class, Object.class)).thenReturn(deserializer);
        Mockito.when(context.findObjectId(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(Mockito.mock(ReadableObjectId.class));

        property.deserializeAndSet(parser, context, instance);
        assertNull(instance.getClass().getDeclaredField("id").get(instance));

        Mockito.verify(deserializer, Mockito.never()).deserialize(parser, context);
    }

    @Test
    public void testValidIdDeserialization() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_INSTANCES);
        JsonDeserializer<Object> deserializer = Mockito.mock(JsonDeserializer.class);
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        Object instance = new Object();

        String idValue = "validIdValue";
        ObjectIdReader.IdType idType = ObjectIdReader.IdType.INT;
        ReadableObjectId roid = Mockito.mock(ReadableObjectId.class);

        Mockito.when(parser.getText()).thenReturn(idValue);
        Mockito.when(parser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
        Mockito.when(context.findValueDeserializer(JsonDeserializer.class, Object.class)).thenReturn(deserializer);
        Mockito.when(context.findObjectId(idValue, idType, Mockito.any()))
                .thenReturn(roid);

        property.deserializeAndSet(parser, context, instance);
        assertEquals(idValue, instance.getClass().getDeclaredField("id").get(instance));

        Mockito.verify(deserializer, Mockito.never()).deserialize(parser, context);
    }

    @Test
    public void testValidIdDeserializationWithIdProperty() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_INSTANCES);
        JsonDeserializer<Object> deserializer = Mockito.mock(JsonDeserializer.class);
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        Object instance = new Object();
        ObjectIdReader.IdType idType = ObjectIdReader.IdType.INT;

        String idValue = "validIdValue";
        ObjectIdReader.IdType idTypeForIdProperty = ObjectIdReader.IdType.STRING;
        Object idProperty = "idPropertyValue";
        SettableBeanProperty idProp = Mockito.mock(SettableBeanProperty.class);

        Mockito.when(parser.getText()).thenReturn(idValue);
        Mockito.when(parser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
        Mockito.when(context.findValueDeserializer(JsonDeserializer.class, Object.class)).thenReturn(deserializer);
        Mockito.when(context.findObjectId(idValue, idType, Mockito.any()))
                .thenReturn(Mockito.mock(ReadableObjectId.class));
        Mockito.when(context.findBeanProperty(idProperty)).thenReturn(idProp);

        property.deserializeAndSet(parser, context, instance);
        assertEquals(idValue, instance.getClass().getDeclaredField("id").get(instance));
        assertEquals(idProperty, instance.getClass().getDeclaredField("idProperty").get(instance));
        assertEquals(idPropertyValue, instance.getClass().getDeclaredField("idProperty").get(instance).getValue());

        Mockito.verify(deserializer, Mockito.never()).deserialize(parser, context);
    }

    @Test
    public void testInvalidIdDeserialization() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_INSTANCES);
        JsonDeserializer<Object> deserializer = Mockito.mock(JsonDeserializer.class);
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        Object instance = new Object();

        String invalidIdValue = "invalidIdValue";
        ObjectIdReader.IdType idType = ObjectIdReader.IdType.INT;
        ReadableObjectId roid = Mockito.mock(ReadableObjectId.class);

        Mockito.when(parser.getText()).thenReturn(invalidIdValue);
        Mockito.when(parser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
        Mockito.when(context.findValueDeserializer(JsonDeserializer.class, Object.class)).thenReturn(deserializer);
        Mockito.when(context.findObjectId(invalidIdValue, idType, Mockito.any()))
                .thenThrow(new IOException("Invalid ObjectId"));

        try {
            property.deserializeAndSet(parser, context, instance);
            assertTrue(false);
        } catch (IOException e) {
            assertEquals("Invalid ObjectId", e.getMessage());
        }

        Mockito.verify(deserializer, Mockito.never()).deserialize(parser, context);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_INSTANCES);
        Object instance = new Object();

        ObjectIdReader.IdType idType = ObjectIdReader.IdType.INT;
        ReadableObjectId roid = Mockito.mock(ReadableObjectId.class);

        Mockito.when(roid.getIdValue()).thenReturn(42);

        Object result = property.deserializeAndSet(Mockito.mock(JsonParser.class), Mockito.mock(DeserializationContext.class), instance);
        assertEquals(instance, result);
        assertEquals(42, instance.getClass().getDeclaredField("id").get(instance));
    }

    @Test
    public void testSet() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_INSTANCES);
        Object instance = new Object();

        ObjectIdReader.IdType idType = ObjectIdReader.IdType.INT;
        ReadableObjectId roid = Mockito.mock(ReadableObjectId.class);
        Object idValue = 42;

        Mockito.when(roid.getIdValue()).thenReturn(idValue);

        property.set(instance, roid);
        assertEquals(instance, instance);
        assertEquals(idValue, instance.getClass().getDeclaredField("id").get(instance));
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_INSTANCES);
        Object instance = new Object();

        ObjectIdReader.IdType idType = ObjectIdReader.IdType.INT;
        ReadableObjectId roid = Mockito.mock(ReadableObjectId.class);
        Object idValue = 42;

        Mockito.when(roid.getIdValue()).thenReturn(idValue);

        Object result = property.setAndReturn(instance, roid);
        assertEquals(instance, result);
        assertEquals(idValue, instance.getClass().getDeclaredField("id").get(instance));
    }

    @Test
    public void testAnnotations() throws IOException {
        ObjectIdReader objectIdReaderAnnotation = Mockito.mock(ObjectIdReader.class);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectReader reader = objectMapper.readerForClass(ObjectIdValueProperty.class);

        ObjectIdValueProperty property = reader.forProperty("propertyName").getValue();

        AnnotatedClass ac = Mockito.mock(AnnotatedClass.class);
        Mockito.when(ac.getAnnotation(JsonDeserialize.class)).thenReturn(objectIdReaderAnnotation);

        assertEquals(objectIdReaderAnnotation, property._objectIdReader);
    }
}