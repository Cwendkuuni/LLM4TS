import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.EnumSet;

public class EnumSerializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testSerializeNormalCase() throws JsonProcessingException, IOException {
        EnumSerializer serializer = new EnumSerializer(EnumValues.allValues(Color.class), null);
        Color color = Color.RED;
        String expectedOutput = "\"RED\"";

        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        serializer.serialize(color, jsonGenerator, objectMapper.getSerializationConfig());
        Assert.assertEquals(expectedOutput, jsonGenerator.getOutputContent());
    }

    @Test
    public void testSerializeByIndex() throws JsonProcessingException, IOException {
        EnumSerializer serializer = new EnumSerializer(EnumValues.allValues(Color.class), true);
        Color color = Color.RED;
        int expectedOutput = 0;

        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        serializer.serialize(color, jsonGenerator, objectMapper.getSerializationConfig());
        Assert.assertEquals(expectedOutput, jsonGenerator.getCurrentEventId());
    }

    @Test
    public void testSerializeUsingToString() throws JsonProcessingException, IOException {
        EnumSerializer serializer = new EnumSerializer(EnumValues.allValues(Color.class), null);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
        Color color = Color.GREEN;
        String expectedOutput = "\"GREEN\"";

        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        serializer.serialize(color, jsonGenerator, objectMapper.getSerializationConfig());
        Assert.assertEquals(expectedOutput, jsonGenerator.getOutputContent());
    }

    @Test
    public void testGetSchema() throws JsonProcessingException {
        EnumSerializer serializer = new EnumSerializer(EnumValues.allValues(Color.class), null);
        ObjectNode schemaNode = serializer.getSchema(objectMapper.getSerializationConfig(), Object.class);

        String expectedOutput = "{\"type\":\"string\",\"enum\":[\"RED\",\"GREEN\",\"BLUE\",\"YELLOW\"]}";
        Assert.assertEquals(expectedOutput, serializer.getSchema(objectMapper.getSerializationConfig(), Object.class).toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSchemaWithInvalidShape() throws JsonProcessingException {
        EnumSerializer serializer = new EnumSerializer(EnumValues.allValues(Color.class), null);
        ObjectNode schemaNode = serializer.getSchema(objectMapper.getSerializationConfig(), Object.class);

        JsonFormat.Shape invalidShape = JsonFormat.Shape.BOOLEAN;
        schemaNode.put("shape", invalidShape.name());
    }

    enum Color {
        RED, GREEN, BLUE, YELLOW
    }
}