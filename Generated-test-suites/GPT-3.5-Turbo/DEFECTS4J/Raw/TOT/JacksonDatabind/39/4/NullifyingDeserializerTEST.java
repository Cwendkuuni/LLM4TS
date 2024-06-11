package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonFactoryBuilder;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import static org.junit.Assert.*;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserialize() throws Exception {
        // Arrange
        JsonFactory jsonFactory = new JsonFactory(new JsonFactoryBuilder());
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(System.out);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("Name", "John Doe");
        jsonGenerator.writeNumberField("Age", 30);
        jsonGenerator.writeEndObject();
        jsonGenerator.close();
        JsonParser jsonParser = jsonFactory.createParser("{\"Name\":\"John Doe\",\"Age\":30}");
        
        NullifyingDeserializer nullifyingDeserializer = new NullifyingDeserializer();
        // Act
        Object result = nullifyingDeserializer.deserialize(jsonParser, new DeserializationContext(null, null, new ObjectMapper()));
        // Assert
        assertNull(result);
    }

    @Test
    public void testDeserializeWithType_WhenTokenIdIsStartArray() throws Exception {
        // Arrange
        JsonFactory jsonFactory = new JsonFactory(new JsonFactoryBuilder());
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(System.out);
        jsonGenerator.writeStartArray();
        jsonGenerator.writeNumber(1);
        jsonGenerator.writeNumber(2);
        jsonGenerator.writeNumber(3);
        jsonGenerator.writeEndArray();
        jsonGenerator.close();
        JsonParser jsonParser = jsonFactory.createParser(jsonGenerator.toString());
        
        NullifyingDeserializer nullifyingDeserializer = new NullifyingDeserializer();
        TypeDeserializer typeDeserializer = null;
        // Act
        Object result = nullifyingDeserializer.deserializeWithType(jsonParser, new DeserializationContext(null, null, new ObjectMapper()), typeDeserializer);
        // Assert
        assertNull(result);
    }

    @Test
    public void testDeserializeWithType_WhenTokenIdIsStartObject() throws Exception {
        // Arrange
        JsonFactory jsonFactory = new JsonFactory(new JsonFactoryBuilder());
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(System.out);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("Obj1");
        jsonGenerator.writeObject(new NullifyingDeserializer());
        jsonGenerator.writeEndObject();
        jsonGenerator.close();
        JsonParser jsonParser = jsonFactory.createParser(jsonGenerator.toString());
        
        NullifyingDeserializer nullifyingDeserializer = new NullifyingDeserializer();
        TypeDeserializer typeDeserializer = null;
        // Act
        Object result = nullifyingDeserializer.deserializeWithType(jsonParser, new DeserializationContext(null, null, new ObjectMapper()), typeDeserializer);
        // Assert
        assertNull(result);
    }

    @Test
    public void testDeserializeWithType_WhenTokenIdIsFieldName() throws Exception {
        // Arrange
        JsonFactory jsonFactory = new JsonFactory(new JsonFactoryBuilder());
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(System.out);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("Field1");
        jsonGenerator.writeNumber(10);
        jsonGenerator.writeEndObject();
        jsonGenerator.close();
        JsonParser jsonParser = jsonFactory.createParser(jsonGenerator.toString());
        
        NullifyingDeserializer nullifyingDeserializer = new NullifyingDeserializer();
        TypeDeserializer typeDeserializer = null;
        // Act
        Object result = nullifyingDeserializer.deserializeWithType(jsonParser, new DeserializationContext(null, null, new ObjectMapper()), typeDeserializer);
        // Assert
        assertNull(result);
    }
}