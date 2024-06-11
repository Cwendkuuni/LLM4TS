import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;
import com.fasterxml.jackson.databind.util.StdSerializers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class EnumSerializerTest {

    private EnumSerializer serializer;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        Class<Color> colorEnumClass = Color.class;
        EnumValues colorEnumValues = EnumValues.constructFromName(Color.class);
        serializer = new EnumSerializer(colorEnumValues, null);
        objectMapper = new JsonMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testSerializeAsIndex() throws JsonGenerationException, JsonMappingException {
        SerializerProvider serializers = objectMapper.getSerializationConfig().getDefaultSerializerProvider();
        JsonGenerator gen = serializers.getFactory().createGenerator(System.out, ObjectMapper.DefaultTyping.JAVA_LANG_STRING);
        serializer.serialize(Color.RED, gen, serializers);
        gen.flush();
        Assert.assertEquals("0", gen.getOutputContent().toString().trim());
    }

    @Test
    public void testSerializeAsToString() throws JsonGenerationException, JsonMappingException {
        SerializerProvider serializers = objectMapper.getSerializationConfig().getDefaultSerializerProvider();
        JsonGenerator gen = serializers.getFactory().createGenerator(System.out, ObjectMapper.DefaultTyping.JAVA_LANG_STRING);
        ObjectMapper objectMapper = new JsonMapper();
        objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
        serializer = new EnumSerializer(Color.class, objectMapper.getSerializationConfig(), null, null);
        serializer.serialize(Color.RED, gen, serializers);
        gen.flush();
        Assert.assertEquals("RED", gen.getOutputContent().toString().trim());
    }

    @Test
    public void testSerializeAsName() throws JsonGenerationException, JsonMappingException {
        SerializerProvider serializers = objectMapper.getSerializationConfig().getDefaultSerializerProvider();
        JsonGenerator gen = serializers.getFactory().createGenerator(System.out, ObjectMapper.DefaultTyping.JAVA_LANG_STRING);
        serializer.serialize(Color.RED, gen, serializers);
        gen.flush();
        Assert.assertEquals("RED", gen.getOutputContent().toString().trim());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormat format = new JsonFormat();
        format.shape(JsonFormat.Shape.STRING);
        JsonFormatVisitorWrapper visitor = new JsonStringFormatVisitor(null, null);
        serializer.acceptJsonFormatVisitor(visitor, Color.class);
        ObjectNode schemaNode = (ObjectNode) visitor.builtValue();
        assertEquals("string", schemaNode.get("type").asText());
        assertEquals(Color.class.getSimpleName(), schemaNode.get("properties").get("enum").toString());
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        JsonSerializer<?> jsonSerializer = objectMapper.findValueSerializer(Color.class);
        ObjectNode schemaNode = (ObjectNode) jsonSerializer.getSchema(objectMapper.getSerializationConfig(), Color.class);
        assertEquals("string", schemaNode.get("type").asText());
        assertEquals(Color.class.getSimpleName(), schemaNode.get("properties").get("enum").toString());
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        JsonFormat format = new JsonFormat();
        format.shape(JsonFormat.Shape.STRING);
        BeanProperty property = new BeanProperty("color", Color.class);
        JsonSerializer<?> jsonSerializer = serializer.createContextual(objectMapper.getSerializationConfig(), property);
        ObjectNode schemaNode = (ObjectNode) jsonSerializer.getSchema(objectMapper.getSerializationConfig(), Color.class);
        assertEquals("string", schemaNode.get("type").asText());
        assertEquals(Color.class.getSimpleName(), schemaNode.get("properties").get("enum").toString());
    }

    @Test
    public void testToString() {
        Assert.assertEquals("RED", serializer.toString(Color.RED));
    }

    @Test
    public void testGetEnumValues() {
        EnumValues enumValues = serializer.getEnumValues();
        assertSame(Color.RED, enumValues.getEnumValue(0));
        assertSame(Color.GREEN, enumValues.getEnumValue(1));
        assertSame(Color.BLUE, enumValues.getEnumValue(2));
    }

    @Test
    public void testSerializeWithCustomSerializer() throws JsonGenerationException, JsonMappingException {
        ObjectMapper objectMapper = new JsonMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        ColorCustomSerializer colorCustomSerializer = new ColorCustomSerializer();
        ObjectNode node = objectMapper.readValue(
                new ObjectMapper().writeValueAsString(new ColorCustomSerializer.ColorObject(Color.RED)), ColorObject.class);
        ColorObject colorObject = node.isEmpty() ? null : node.getDefaultValue();
        assertEquals(Color.RED, colorObject.color);
        JsonSerializer<Color> colorSerializer = StdSerializers.createEnumSerializer(Color.class);
        SerializerProvider serializers = objectMapper.getSerializationConfig().getDefaultSerializerProvider();
        JsonGenerator gen = serializers.getFactory().createGenerator(System.out, ObjectMapper.DefaultTyping.JAVA_LANG_STRING);
        SerializerProvider serializersWithCustomSerializer = new SerializerProvider() {
            @Override
            public JsonSerializer<?> findValueSerializer(Class<?> type) {
                if (Color.class.isAssignableFrom(type)) {
                    return colorCustomSerializer;
                }
                return super.findValueSerializer(type);
            }
        };
        colorSerializer.serialize(Color.RED, gen, serializersWithCustomSerializer);
        gen.flush();
        Assert.assertEquals("ColorObject(RED)", gen.getOutputContent().toString().trim());
    }

    @Test
    public void testSerializeWithJsonFormatAnnotation() throws JsonGenerationException, JsonMappingException {
        JsonFormat format = new JsonFormat();
        format.shape(JsonFormat.Shape.STRING);
        JsonFormat annotatedFormat = new JsonFormat();
        annotatedFormat.shape(JsonFormat.Shape.NUMBER);
        Color annotatedColor = new Color("ANNOTATED_RED");
        JsonSerializer<Color> jsonSerializer = new EnumSerializer(Color.class, new ObjectMapper().getSerializationConfig(), null, annotatedFormat);
        SerializerProvider serializers = new SerializerProvider() {
            @Override
            public JsonSerializer<?> findValueSerializer(Class<?> type) {
                if (Color.class.isAssignableFrom(type)) {
                    return jsonSerializer;
                }
                return null;
            }
        };
        JsonGenerator gen = serializers.getFactory().createGenerator(System.out, ObjectMapper.DefaultTyping.JAVA_LANG_STRING);
        jsonSerializer.serialize(annotatedColor, gen, serializers);
        gen.flush();
        Assert.assertEquals("1", gen.getOutputContent().toString().trim());
    }

    @Test
    public void testSerializeWithJsonFormatAnnotationWithShapeAsArray() throws JsonGenerationException, JsonMappingException {
        JsonFormat format = new JsonFormat();
        format.shape(JsonFormat.Shape.ARRAY);
        Color annotatedColor = new Color("ANNOTATED_RED");
        JsonSerializer<Color> jsonSerializer = new EnumSerializer(Color.class, new ObjectMapper().getSerializationConfig(), null, format);
        SerializerProvider serializers = new SerializerProvider() {
            @Override
            public JsonSerializer<?> findValueSerializer(Class<?> type) {
                if (Color.class.isAssignableFrom(type)) {
                    return jsonSerializer;
                }
                return null;
            }
        };
        JsonGenerator gen = serializers.getFactory().createGenerator(System.out, ObjectMapper.DefaultTyping.JAVA_LANG_STRING);
        jsonSerializer.serialize(annotatedColor, gen, serializers);
        gen.flush();
        Assert.assertEquals("[RED]", gen.getOutputContent().toString().trim());
    }

    @Test
    public void testSerializeWithJsonFormatAnnotationWithShapeAsString() throws JsonGenerationException, JsonMappingException {
        JsonFormat format = new JsonFormat();
        format.shape(JsonFormat.Shape.STRING);
        Color annotatedColor = new Color("ANNOTATED_RED");
        JsonSerializer<Color> jsonSerializer = new EnumSerializer(Color.class, new ObjectMapper().getSerializationConfig(), null, format);
        SerializerProvider serializers = new SerializerProvider() {
            @Override
            public JsonSerializer<?> findValueSerializer(Class<?> type) {
                if (Color.class.isAssignableFrom(type)) {
                    return jsonSerializer;
                }
                return null;
            }
        };
        JsonGenerator gen = serializers.getFactory().createGenerator(System.out, ObjectMapper.DefaultTyping.JAVA_LANG_STRING);
        jsonSerializer.serialize(annotatedColor, gen, serializers);
        gen.flush();
        Assert.assertEquals("\"ANNOTATED_RED\"", gen.getOutputContent().toString().trim());
    }

    enum Color {
        RED, GREEN, BLUE
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    static class ColorObject {
        Color color;

        ColorObject(Color color) {
            this.color = color;
        }
    }

    static class ColorCustomSerializer extends EnumSerializer<Color> {
        @Override
        public void serialize(Color color, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString("ColorObject(" + color.name() + ")");
        }

        static class ColorObject {
            Color color;
        }
    }
}