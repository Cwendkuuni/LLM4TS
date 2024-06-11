import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class EnumSerializerTest {

    @Test
    public void testSerializeEnum() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        EnumSerializer serializer = mapper.findValueSerializer(Enum.class);

        Enum[] values = Color.values();
        Color color = Color.RED;

        JsonNode node = mapper.valueToTree(color);
        JsonGenerator generator = mapper.getPolymorphicSerializer(ObjectNode.class).getSerializerProvider().getGenerator();
        serializer.serialize(color, generator, new SerializerProvider());

        assertEquals(node.toString(), serializer.serializeToString(color));

        // Test serialization as index
        generator.setCodec(mapper.getSerializerProvider().getCodec());
        serializer.serialize(color, generator, new SerializerProvider(true));
        assertEquals("0", generator.getCurrentOutputValue().asText());

        // Test serialization using toString()
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        JsonSerializer<Object> serializerWithToString = mapper.findValueSerializer(Object.class, format);

        generator.setCodec(mapper.getSerializerProvider().getCodec());
        serializerWithToString.serializeValue(color, generator, new SerializerProvider());
        assertEquals(color.toString(), generator.getCurrentOutputValue().asText());

        // Test serialization with custom EnumValues
        EnumValues customValues = new EnumValues() {
            @Override
            public SerializableString[] values() {
                return new SerializableString[]{new SerializableString("CUSTOM_RED"), new SerializableString("CUSTOM_GREEN")};
            }
        };
        EnumSerializer customSerializer = new EnumSerializer(customValues, false);

        ObjectNode tree = mapper.valueToTree(color);
        ObjectNode customTree = mapper.treeToValue(customSerializer.getSchema(mapper.getSerializerProvider(), Color.class), true);
        ObjectNode root = mapper.createObjectNode();
        root.set("value", tree);
        root.set("schema", customTree);

        ObjectMapper customMapper = new ObjectMapper();
        customMapper.registerModule(new SimpleModule().setSerializerFactory(CustomSerializerProvider.instance()));
        JsonSerializer<Object> customSerializerWithSchema = customMapper.findValueSerializer(Object.class, null);

        generator.setCodec(customMapper.getSerializerProvider().getCodec());
        customSerializerWithSchema.serializeValue(color, generator, new SerializerProvider());
        assertEquals(customValues.serializedValueFor(color), generator.getCurrentOutputValue().asText());
    }

    private static class CustomSerializerProvider extends SerializerProvider {
        private static final CustomSerializerProvider INSTANCE = new CustomSerializerProvider();

        private CustomSerializerProvider() {
            super(new ObjectMapper().getSerializerProvider());
        }

        public static CustomSerializerProvider instance() {
            return INSTANCE;
        }

        @Override
        public JsonSerializer<Object> findValueSerializer(Class<?> valueType) {
            if (valueType == Object.class) {
                return new EnumSerializer(new EnumValues() {
                    @Override
                    public SerializableString[] values() {
                        return new SerializableString[]{"CUSTOM_RED", "CUSTOM_GREEN"};
                    }
                }, false);
            }
            return super.findValueSerializer(valueType);
        }
    }
}