import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.JsonSerializer;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EnumSerializerTest {

    private ObjectMapper objectMapper;
    private EnumSerializer enumSerializer;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        enumSerializer = objectMapper.findValueSerializer(Enum.class);
    }

    @Test
    public void testSerializeEnumUsingIndex() throws JsonProcessingException {
        EnumSerializer enumSerializerWithIndex = new EnumSerializer(EnumValues. EstadoCivil.values(), true);
        JsonGenerator jsonGenerator = objectMapper.getPolymorphicSerializerProvider().getGeneratorFactory()
                .findValueSerializer(Enum.class, null, null, null).getSerializerProvider()
                .getDefaultSerializerProvider().getGenerator();

        jsonGenerator.setPrettyPrinting();
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("myEnum");
        enumSerializerWithIndex.serialize(EstadoCivil.CASADO, jsonGenerator, null);
        jsonGenerator.writeEndObject();

        String serializedValue = jsonGenerator.getOutputContent();
        ObjectNode jsonNode = objectMapper.readTree(serializedValue);
        int value = jsonNode.path("myEnum").asInt();
        Assert.assertEquals(EstadoCivil.CASADO.ordinal(), value);
    }

    @Test
    public void testSerializeEnumUsingToString() throws JsonProcessingException {
        AnnotatedClass<Enum> enumClassAnnotated = new AnnotatedClass(Enum.class, null, null);
        JsonFormat.Value jsonFormatValue = new JsonFormat.Value();
        JsonFormat jsonFormat = new JsonFormat();
        jsonFormat.setShape(JsonFormat.Shape.STRING);
        jsonFormatValue.setFormat(jsonFormat);

        AnnotatedMember enumMemberAnnotated = new AnnotatedMember(Enum.class.getDeclaredField("CASADO"), null, null);
        AnnotatedClass enumMemberAnnotatedClass = new AnnotatedClass(enumMemberAnnotated.getAnnotated());
        JsonFormat enumMemberJsonFormat = enumMemberAnnotatedClass.findAnnotation(JsonFormat.class);

        if (enumMemberJsonFormat != null) {
            jsonFormatValue = enumMemberJsonFormat;
        }

        ObjectNode jsonNode = mapperTestSerializeEnumUsingToString(enumSerializer, EstadoCivil.CASADO, jsonFormatValue);
        String serializedValue = jsonNode.toString();
        Assert.assertEquals("CASADO", serializedValue);
    }

    @Test
    public void testSerializeEnumUsingName() throws JsonProcessingException {
        ObjectNode jsonNode = mapperTestSerializeEnumUsingName(enumSerializer, EstadoCivil.CASADO);
        String serializedValue = jsonNode.toString();
        Assert.assertEquals("CASADO", serializedValue);
    }

    @Test
    public void testDeserializeEnumUsingIndex() throws JsonProcessingException {
        String jsonString = "{\"myEnum\":1}";
        Enum<?> deserializedEnum = mapperTestDeserializeEnumUsingIndex(jsonString, EstadoCivil.class);
        Assert.assertEquals(EstadoCivil.CASADO, deserializedEnum);
    }

    @Test
    public void testDeserializeEnumUsingName() throws JsonProcessingException {
        String jsonString = "{\"myEnum\":\"CASADO\"}";
        Enum<?> deserializedEnum = mapperTestDeserializeEnumUsingName(jsonString, EstadoCivil.class);
        Assert.assertEquals(EstadoCivil.CASADO, deserializedEnum);
    }

    @Test
    public void testDeserializeEnumUsingToString() throws JsonProcessingException {
        String jsonString = "{\"myEnum\":\"CASADO\"}";
        Enum<?> deserializedEnum = mapperTestDeserializeEnumUsingToString(jsonString, EstadoCivil.class);
        Assert.assertEquals(EstadoCivil.CASADO, deserializedEnum);
    }

    private ObjectNode mapperTestSerializeEnumUsingName(JsonSerializer<Enum<?>> enumSerializer, Enum<?> enumValue)
            throws JsonProcessingException {
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator();
        jsonGenerator.setPrettyPrinting();
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("myEnum");
        enumSerializer.serialize(enumValue, jsonGenerator, null);
        jsonGenerator.writeEndObject();
        return objectMapper.readTree(jsonGenerator.getOutputContent());
    }

    private ObjectNode mapperTestSerializeEnumUsingToString(JsonSerializer<Enum<?>> enumSerializer, Enum<?> enumValue,
            JsonFormat.Value jsonFormatValue) throws JsonProcessingException {
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator();
        jsonGenerator.setPrettyPrinting();
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("myEnum");
        ObjectNode jsonNode = mapperTestSerializeEnumUsingName(enumSerializer, enumValue);
        jsonGenerator.writeObjectField(jsonNode.get("myEnum").asText(), jsonFormatValue.getShape() != null ? jsonFormatValue.getShape() : JsonFormat.Shape.STRING);
        jsonGenerator.writeEndObject();
        return objectMapper.readTree(jsonGenerator.getOutputContent());
    }

    private Enum<?> mapperTestDeserializeEnumUsingIndex(String jsonString, Class<Enum<?>> enumType) throws JsonProcessingException {
        return objectMapper.readValue(jsonString, enumType);
    }

    private Enum<?> mapperTestDeserializeEnumUsingName(String jsonString, Class<Enum<?>> enumType) throws JsonProcessingException {
        ObjectNode jsonNode = objectMapper.readTree(jsonString);
        return objectMapper.findValueSerializer(enumType).deserialize(jsonNode.path("myEnum"), null);
    }

    private Enum<?> mapperTestDeserializeEnumUsingToString(String jsonString, Class<Enum<?>> enumType) throws JsonProcessingException {
        ObjectNode jsonNode = objectMapper.readTree(jsonString);
        String serializedValue = jsonNode.path("myEnum").asText();
        return objectMapper.findValueSerializer(enumType).deserialize(serializedValue, null);
    }
}

enum EstadoCivil {
    SOLTEIRO("Solteiro"),
    CASADO("Casado"),
    DIVORCIADO("Divorciado"),
    VIUVO("Viuvos");

    private final String name;

    EstadoCivil(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }
}