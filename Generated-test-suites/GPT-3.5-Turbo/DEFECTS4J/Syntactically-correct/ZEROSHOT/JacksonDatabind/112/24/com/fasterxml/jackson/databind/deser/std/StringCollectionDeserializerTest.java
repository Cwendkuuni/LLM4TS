
package com.fasterxml.jackson.databind.deser.std;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StringCollectionDeserializerTest {
    @Test
    public void deserialize_WithStartArrayToken_AddsAllStringValuesToResultCollection() throws JsonParseException, IOException {
        // Arrange
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        JsonParser jsonParser = new ObjectMapper().getFactory().createParser("[\"value1\", \"value2\", \"value3\"]");
        DeserializationContext context = new ObjectMapper().getDeserializationContext();
        Collection<String> result = new ArrayList<>();

        // Act
        deserializer.deserialize(jsonParser, context, result);

        // Assert
        Assert.assertEquals(3, result.size());
        Assert.assertTrue(result.contains("value1"));
        Assert.assertTrue(result.contains("value2"));
        Assert.assertTrue(result.contains("value3"));
    }

    @Test
    public void deserialize_WithOtherToken_ReturnsResultCollectionWithoutAddingValues() throws JsonParseException, IOException {
        // Arrange
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        JsonParser jsonParser = new ObjectMapper().getFactory().createParser("123");
        DeserializationContext context = new ObjectMapper().getDeserializationContext();
        Collection<String> result = new ArrayList<>();

        // Act
        deserializer.deserialize(jsonParser, context, result);

        // Assert
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void deserialize_WithNullValue_AddsNullValueToResultCollection() throws JsonParseException, IOException {
        // Arrange
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        JsonParser jsonParser = new ObjectMapper().getFactory().createParser("[null]");
        DeserializationContext context = new ObjectMapper().getDeserializationContext();
        Collection<String> result = new ArrayList<>();

        // Act
        deserializer.deserialize(jsonParser, context, result);

        // Assert
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(null, result.iterator().next());
    }

    @Test
    public void deserialize_WithCustomDeserializer_AddsAllStringValuesToResultCollection() throws JsonParseException, IOException {
        // Arrange
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        JsonParser jsonParser = new ObjectMapper().getFactory().createParser("[1, 2, 3]");
        DeserializationContext context = new ObjectMapper().getDeserializationContext();
        Collection<String> result = new ArrayList<>();

        // Act
        deserializer.deserializeUsingCustom(jsonParser, context, result, null);

        // Assert
        Assert.assertEquals(3, result.size());
        Assert.assertTrue(result.contains("1"));
        Assert.assertTrue(result.contains("2"));
        Assert.assertTrue(result.contains("3"));
    }
}
