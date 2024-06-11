
package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class NullifyingDeserializerTest {

    private NullifyingDeserializer nullDeser = NullifyingDeserializer.instance;

    @Test
    public void testDeserialize_WhenJsonTokenIsField_Name_ShouldSkipChildrenAndReturnNull() throws IOException {
        // Arrange
        JsonParser jsonParser = null;
        DeserializationContext deserializationContext = null;

        // Act
        Object actual = nullDeser.deserialize(jsonParser, deserializationContext);

        // Assert
        assertNull(actual);
    }

    @Test
    public void testDeserializeWithType_WhenJsonTokenIsStartArray_ShouldReturnNull() throws IOException {
        // Arrange
        JsonParser jsonParser = null;
        DeserializationContext deserializationContext = null;
        TypeDeserializer typeDeserializer = null;

        // Act
        Object actual = nullDeser.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);

        // Assert
        assertNull(actual);
    }

    @Test
    public void testDeserializeWithType_WhenJsonTokenIsStartObject_ShouldReturnNull() throws IOException {
        // Arrange
        JsonParser jsonParser = null;
        DeserializationContext deserializationContext = null;
        TypeDeserializer typeDeserializer = null;

        // Act
        Object actual = nullDeser.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);

        // Assert
        assertNull(actual);
    }

    @Test
    public void testDeserializeWithType_WhenJsonTokenIsFieldName_ShouldReturnNull() throws IOException {
        // Arrange
        JsonParser jsonParser = null;
        DeserializationContext deserializationContext = null;
        TypeDeserializer typeDeserializer = null;

        // Act
        Object actual = nullDeser.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);

        // Assert
        assertNull(actual);
    }
}

