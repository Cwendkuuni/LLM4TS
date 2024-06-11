import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.EnumSet;

public class EnumSerializerTest {

    private ObjectMapper objectMapper;
    private EnumSerializer enumSerializer;
    private EnumValues enumValues;

    @BeforeClass
    public void setup() {
        objectMapper = new ObjectMapper();
        enumValues = EnumValues.all(Color.class);
        enumSerializer = new EnumSerializer(enumValues, true);
    }

    @Test
    public void testSerializeAsIndex() throws JsonProcessingException, IOException {
        // Given
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        SerializerProvider serializerProvider = objectMapper.getSerializationConfig().getDefaultSerializerProvider();

        // When
        enumSerializer.serialize(Color.RED, jsonGenerator, serializerProvider);

        // Then
        Assert.assertFalse(jsonGenerator.hasFeature(JsonGenerator.Feature.AUTO_CLOSE_TARGET));
        Assert.assertEquals(jsonGenerator.getCurrentValue(), 0);

        jsonGenerator.flush();
        jsonGenerator.close();
    }

    @Test
    public void testSerializeAsText() throws JsonProcessingException, IOException {
        // Given
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        SerializerProvider serializerProvider = objectMapper.getSerializationConfig().getDefaultSerializerProvider();

        // When
        enumSerializer.serialize(Color.RED, jsonGenerator, serializerProvider);

        // Then
        Assert.assertFalse(jsonGenerator.hasFeature(JsonGenerator.Feature.AUTO_CLOSE_TARGET));
        Assert.assertEquals(jsonGenerator.getCurrentValue(), "RED");

        jsonGenerator.flush();
        jsonGenerator.close();
    }

    @Test
    public void testCreateContextual() throws IOException {
        // Given
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        SerializerProvider serializerProvider = objectMapper.getSerializationConfig().getDefaultSerializerProvider();

        EnumSerializer enumSerializerWithNames = enumSerializer.createContextual(null, null);

        // When
        enumSerializerWithNames.serialize(Color.RED, jsonGenerator, serializerProvider);

        // Then
        Assert.assertEquals(jsonGenerator.getCurrentValue(), "RED");

        jsonGenerator.flush();
        jsonGenerator.close();
    }

    @Test
    public void testCreateContextualWithToString() throws IOException {
        // Given
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        SerializerProvider serializerProvider = objectMapper.getSerializationConfig().getDefaultSerializerProvider();

        ObjectMapper objectMapperWithToStringEnabled = new ObjectMapper();
        ObjectMapper.DefaultSerializerProvider defaultSerializerProviderWithToStringEnabled = objectMapperWithToStringEnabled.getDefaultSerializerProvider();
        defaultSerializerProviderWithToStringEnabled.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        EnumSerializer enumSerializerWithToString = enumSerializer.createContextual(null, null);
        ObjectNode schemaNode = enumSerializerWithToString.getSchema(defaultSerializerProviderWithToStringEnabled, null);

        // When
        jsonGenerator.setCodec(objectMapperWithToStringEnabled);
        jsonGenerator.writeTree(schemaNode);
        jsonGenerator.writeValue("color", Color.RED);

        // Then
        Assert.assertEquals(jsonGenerator.getCurrentValue(), "\"RED\"");

        jsonGenerator.flush();
        jsonGenerator.close();
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        // Given
        JsonSerializer.TypingSerializerProvider serializerProvider = new JsonSerializer.TypingSerializerProvider(new ObjectMapper().getSerializationConfig());
        JsonFormatVisitorWrapper jsonFormatVisitor = serializerProvider.getFactory().createJsonFormatVisitor(null, null);

        // When
        enumSerializer.acceptJsonFormatVisitor(jsonFormatVisitor, null);

        // Then
        JsonStringFormatVisitor jsonStringFormatVisitor = jsonFormatVisitor.expectStringFormat(null);
        Assert.assertNotNull(jsonStringFormatVisitor);
        EnumSet<Color> colorEnumSet = EnumSet.allOf(Color.class);
        for (Color color : colorEnumSet) {
            jsonStringFormatVisitor.visitEnumValue(color.name());
        }
        jsonStringFormatVisitor.close();
    }

    @Test
    public void testGetSchema() throws IOException {
        // Given
        JsonSerializer.TypingSerializerProvider serializerProvider = new JsonSerializer.TypingSerializerProvider(new ObjectMapper().getSerializationConfig());

        // When
        ObjectNode schemaNode = enumSerializer.getSchema(serializerProvider, null);

        // Then
        Assert.assertNotNull(schemaNode);
        Assert.assertEquals(schemaNode.get("type"), "string");
        Assert.assertEquals(schemaNode.size(), 2);
    }

    @JsonFormat(shape = Shape.STRING)
    enum Color {
        RED,
        GREEN,
        BLUE
    }
}