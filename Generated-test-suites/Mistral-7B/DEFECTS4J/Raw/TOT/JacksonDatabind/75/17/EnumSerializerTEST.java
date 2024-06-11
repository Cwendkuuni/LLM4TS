import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;

public class EnumSerializerTest {

    @Test
    public void testEnumSerialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.construct(MyEnum.class));

        MyEnum enumValue = MyEnum.VALUE_1;
        String expectedStringValue = enumValue.name();
        int expectedIntValue = enumValue.ordinal();

        // Test serialization as String
        SerializerProvider serializers = mapper.getSerializationConfig().getDefaultSerializerProvider();
        JsonGenerator gen = mapper.getFactory().createGenerator(new ObjectOutputStream(), serializers);
        enumSerializer.serialize(enumValue, gen, serializers);
        String actualStringValue = gen.getOutputBytes().toString();
        gen.close();
        Assert.assertEquals(expectedStringValue, actualStringValue);

        // Test serialization as index
        serializers.setConfig(mapper.getSerializationConfig());
        gen = mapper.getFactory().createGenerator(new ObjectOutputStream(), serializers);
        serializers.setCurrentOutput(gen);
        gen.writeStartArray();
        enumSerializer.serialize(enumValue, gen, serializers);
        gen.writeEndArray();
        int actualIntValue = gen.getOutputBytes().readInt();
        gen.close();
        Assert.assertEquals(expectedIntValue, actualIntValue);

        // Test serialization with custom format
        JsonFormat customFormat = new JsonFormat();
        customFormat.setShape(JsonFormat.Shape.NUMBER);
        ObjectNode rootNode = mapper.readTree(mapper.writeValueAsString(new Object() {
            @JsonFormat(shape = JsonFormat.Shape.NUMBER)
            public Enum<?> getMyEnum() {
                return enumValue;
            }
        }));
        ObjectNode enumNode = rootNode.path("myEnum");
        int actualCustomIntValue = enumNode.asInt();
        Assert.assertEquals(expectedIntValue, actualCustomIntValue);
    }

    @Test
    public void testEnumSerializationErrorScenarios() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // Test null value
        try {
            EnumSerializer enumSerializer = new EnumSerializer(EnumValues.construct(MyEnum.class));
            enumSerializer.serialize(null, null, null);
            Assert.fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        // Test unknown enum value
        MyEnum unknownEnumValue = MyEnum.valueOf("UnknownValue");
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            EnumSerializer enumSerializer = new EnumSerializer(EnumValues.construct(MyEnum.class));
            enumSerializer.serialize(unknownEnumValue, null, null);
        });
    }
}

enum MyEnum {
    VALUE_1,
    VALUE_2,
    VALUE_3
}