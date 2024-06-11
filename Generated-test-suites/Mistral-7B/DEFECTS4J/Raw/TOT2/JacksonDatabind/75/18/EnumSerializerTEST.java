import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapperFactory;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;

public class EnumSerializerTest {

    private static ObjectMapper objectMapper;

    static {
        objectMapper = JsonMapperFactory.getMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testSerializeEnumAsText() throws JsonGenerationException, IOException {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.valuesOf(TestEnum.class), null);
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        enumSerializer.serialize(TestEnum.ONE, jsonGenerator, null);
        JsonParser jsonParser = objectMapper.getFactory().createParser(new StringReader("\"ONE\""));
        Assert.assertEquals(TestEnum.ONE, enumSerializer.deserialize(jsonParser, TestEnum.class, null));
    }

    @Test
    public void testSerializeEnumAsIndex() throws JsonGenerationException, IOException {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.valuesOf(TestEnum.class), true);
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        enumSerializer.serialize(TestEnum.ONE, jsonGenerator, null);
        JsonParser jsonParser = objectMapper.getFactory().createParser(new StringReader("0"));
        Assert.assertEquals(TestEnum.ONE, enumSerializer.deserialize(jsonParser, TestEnum.class, null));
    }

    @Test(expected = IOException.class)
    public void testSerializeCustomSerialization() throws JsonGenerationException, IOException {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.valuesOf(TestEnumWithCustomSerialization.class), null);
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        enumSerializer.serialize(TestEnumWithCustomSerialization.TWO, jsonGenerator, null);
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithInvalidValue() throws JsonGenerationException, IOException {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.valuesOf(TestEnum.class), null);
        JsonParser jsonParser = objectMapper.getFactory().createParser(new StringReader("invalidValue"));
        enumSerializer.deserialize(jsonParser, TestEnum.class, null);
    }

    @Test
    public void testSerializeShapeString() throws JsonGenerationException, IOException {
        ObjectMapper objectMapperForShapeString = JsonMapperFactory.getMapper();
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.valuesOf(TestEnumWithShape.class), null, objectMapperForShapeString);
        JsonGenerator jsonGenerator = objectMapperForShapeString.getFactory().createGenerator(System.out);
        enumSerializer.serialize(TestEnumWithShape.ONE, jsonGenerator, null);
        JsonParser jsonParser = objectMapperForShapeString.getFactory().createParser(new StringReader("\"ONE\""));
        Assert.assertEquals(TestEnumWithShape.ONE, enumSerializer.deserialize(jsonParser, TestEnumWithShape.class, null));
    }

    @Test
    public void testSerializeShapeNumeric() throws JsonGenerationException, IOException {
        ObjectMapper objectMapperForShapeNumeric = JsonMapperFactory.getMapper();
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.valuesOf(TestEnumWithShape.class), null, objectMapperForShapeNumeric);
        JsonGenerator jsonGenerator = objectMapperForShapeNumeric.getFactory().createGenerator(System.out);
        enumSerializer.serialize(TestEnumWithShape.ONE, jsonGenerator, null);
        JsonParser jsonParser = objectMapperForShapeNumeric.getFactory().createParser(new StringReader("1"));
        Assert.assertEquals(TestEnumWithShape.ONE, enumSerializer.deserialize(jsonParser, TestEnumWithShape.class, null));
    }

    @Test
    public void testSerializeShapeArray() throws JsonGenerationException, IOException {
        ObjectMapper objectMapperForShapeArray = JsonMapperFactory.getMapper();
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.valuesOf(TestEnumWithShape.class), null, objectMapperForShapeArray);
        JsonGenerator jsonGenerator = objectMapperForShapeArray.getFactory().createGenerator(System.out);
        jsonGenerator.writeStartArray();
        enumSerializer.serialize(TestEnumWithShape.ONE, jsonGenerator, null);
        jsonGenerator.writeEndArray();
        JsonParser jsonParser = objectMapperForShapeArray.getFactory().createParser(new StringReader("[0]"));
        Assert.assertEquals(TestEnumWithShape.ONE, enumSerializer.deserialize(jsonParser, TestEnumWithShape[].class).nextValue().getValue());
    }

    private static class TestEnum {
        public static final TestEnum ONE = Enum.valueOf(TestEnum.class, "ONE");
        public static final TestEnum TWO = Enum.valueOf(TestEnum.class, "TWO");

        private final String name;

        TestEnum(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private static class TestEnumWithCustomSerialization {
        public static final TestEnumWithCustomSerialization TWO = new TestEnumWithCustomSerialization("TWO");

        private final String value;

        TestEnumWithCustomSerialization(String value) {
            this.value = value;
        }

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "custom_" + value;
        }
    }

    private static class TestEnumWithShape {
        public static final TestEnumWithShape ONE = new TestEnumWithShape("ONE");
        public static final TestEnumWithShape TWO = new TestEnumWithShape("TWO");

        private final String name;

        TestEnumWithShape(String name) {
            this.name = name;
        }

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        public String getName() {
            return name;
        }
    }
}