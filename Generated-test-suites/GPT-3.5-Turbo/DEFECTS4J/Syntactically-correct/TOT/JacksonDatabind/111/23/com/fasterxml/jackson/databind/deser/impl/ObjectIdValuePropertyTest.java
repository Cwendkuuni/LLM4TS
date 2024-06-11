
package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.NullValueProvider;

public class ObjectIdValuePropertyTest {

    @Test
    public void testWithNullValueDeserializer() {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null);
        ObjectIdValueProperty objectIdProperty = new ObjectIdValueProperty(objectIdReader, metadata);

        SettableBeanProperty result = objectIdProperty.withValueDeserializer(null);

        Assert.assertEquals(objectIdProperty, result);
    }

    @Test
    public void testWithDifferentValueDeserializer() {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null);
        ObjectIdValueProperty objectIdProperty = new ObjectIdValueProperty(objectIdReader, metadata);

        JsonDeserializer<?> dummyDeserializer = new JsonDeserializer<>() {};

        SettableBeanProperty result = objectIdProperty.withValueDeserializer(dummyDeserializer);

        Assert.assertNotEquals(objectIdProperty, result);
        Assert.assertEquals(dummyDeserializer, result.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null);
        ObjectIdValueProperty objectIdProperty = new ObjectIdValueProperty(objectIdReader, metadata);

        SettableBeanProperty result = objectIdProperty.withNullProvider(null);

        Assert.assertEquals(objectIdProperty, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetWithNoIdProperty() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null);
        ObjectIdValueProperty objectIdProperty = new ObjectIdValueProperty(objectIdReader, metadata);

        objectIdProperty.setAndReturn(this, null);
    }

    @Test
    public void testSetAndReturnWithIdProperty() throws IOException {
        PropertyName newName = new PropertyName("id");
        ObjectIdValueProperty src = new ObjectIdValueProperty(null, newName);

        Object result = src.setAndReturn(this, "123");

        Assert.assertEquals(this, result);
        // Verify if the new name is set correctly
        Assert.assertEquals(newName, src.getFullName());
    }

    @Test
    public void testDeserializeSetAndReturnWithValueNull() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null);
        ObjectIdValueProperty objectIdProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser("null");
        DeserializationContext deserializationContext = new ObjectMapper().createDeserializationContext();

        Object result = objectIdProperty.deserializeSetAndReturn(jsonParser, deserializationContext, null);

        Assert.assertNull(result);
    }

    @Test
    public void testDeserializeSetAndReturnWithDeserializedValue() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null);
        ObjectIdValueProperty objectIdProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser("\"123\"");
        DeserializationContext deserializationContext = new ObjectMapper().createDeserializationContext();

        Object result = objectIdProperty.deserializeSetAndReturn(jsonParser, deserializationContext, null);

        Assert.assertNull(result); // The result is null since idProperty is null
    }
}
