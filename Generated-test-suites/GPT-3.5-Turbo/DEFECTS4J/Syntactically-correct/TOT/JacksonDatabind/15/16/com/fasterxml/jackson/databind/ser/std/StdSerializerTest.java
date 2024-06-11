
package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.DefaultSerializerProviderImpl;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.StdConverter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Type;

public class StdSerializerTest {

    private DefaultSerializerProvider serializerProvider;
    private JsonFactory jsonFactory;

    @Before
    public void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        serializerProvider = new DefaultSerializerProvider.Impl();
        objectMapper.setSerializerFactory(serializerProvider);
        jsonFactory = objectMapper.getFactory();
    }

    @Test
    public void testSerialize() throws IOException {
        // Arrange
        StringWriter writer = new StringWriter();
        String expectedJson = "\"test\"";
        StdSerializer<String> serializer = new StdSerializer<>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonGenerationException {
                jgen.writeString(value);
            }
        };

        // Act
        serializer.serialize("test", jsonFactory.createGenerator(writer), serializerProvider);
        String actualJson = writer.toString();

        // Assert
        Assert.assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testGetSchema() throws IOException {
        // Arrange
        StdSerializer<String> serializer = new StdSerializer<>(String.class) {
            @Override
            public JsonNode getSchema(SerializerProvider provider, Type typeHint)
                    throws JsonMappingException {
                ObjectNode schema = createSchemaNode("string");
                schema.put("customField", "customValue");
                return schema;
            }
        };

        // Act
        JsonNode actualSchema = serializer.getSchema(serializerProvider, String.class);

        // Assert
        Assert.assertEquals("string", actualSchema.get("type").asText());
        Assert.assertEquals("customValue", actualSchema.get("customField").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        // Arrange
        JavaType type = TypeFactory.defaultInstance().constructType(String.class);
        StdSerializer<String> serializer = new StdSerializer<>(String.class) {
            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
                    throws JsonMappingException {
                visitor.expectAnyFormat(typeHint);
            }
        };

        // Act
        JsonFormatVisitorWrapper visitor = serializerProvider.getProvider().getFormatFactory().visitorForClass(type);
        serializer.acceptJsonFormatVisitor(visitor, type);

        // Assert
        // No exceptions are thrown
    }

    @Test
    public void testDefaultSerializer() {
        // Arrange
        StdSerializer<String> serializer = new StdSerializer<>(String.class) {};

        // Act
        boolean isDefault = serializer.isDefaultSerializer(serializer);

        // Assert
        Assert.assertTrue(isDefault);
    }
}

