import org.junit.Test;
import static org.junit.Assert.*;

public class EnumSerializerTest {

    @Test
    public void testSerialize() throws IOException {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(null, Enum.class), false);
        Enum<?> en = Enum.VAL1;
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream());
        SerializerProvider serializers = new ObjectMapper().getSerializerProvider();
        enumSerializer.serialize(en, gen, serializers);
        gen.close();
        // TODO: Assert the serialized value
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(null, Enum.class), false);
        JsonSerializer<?> serializer = enumSerializer.createContextual(null, null);
        // TODO: Assert the returned serializer instance
    }

    @Test
    public void testGetEnumValues() {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(null, Enum.class), false);
        EnumValues enumValues = enumSerializer.getEnumValues();
        // TODO: Assert the returned enumValues
    }

    @Test
    public void testSerializeAsIndex() {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(null, Enum.class), false);
        SerializerProvider serializers = new ObjectMapper().getSerializerProvider();
        boolean result = enumSerializer.serializeAsIndex(serializers);
        // TODO: Assert the result value
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsShapeWrittenUsingIndex() {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(null, Enum.class), false);
        Class<?> enumClass = Enum.class;
        JsonFormat.Value format = new JsonFormat.Value();
        boolean fromClass = true;
        Boolean result = enumSerializer.isShapeWrittenUsingIndex(enumClass, format, fromClass);
        // TODO: Assert the result value
    }
}