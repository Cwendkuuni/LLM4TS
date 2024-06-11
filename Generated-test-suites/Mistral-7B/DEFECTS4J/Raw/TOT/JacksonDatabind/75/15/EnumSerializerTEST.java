import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class EnumSerializerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testSerializeEnum() throws JsonProcessingException {
        EnumSerializer serializer = new EnumSerializer(EnumValues.create(MyEnum.class));
        JsonGenerator gen = MAPper.getFactory().createGenerator(new byte[0], null);
        serializer.serialize(MyEnum.ONE, gen, null);
        assertEquals("\"ONE\"", gen.getOutputContent().toString());

        gen.close();

        gen = MAPper.getFactory().createGenerator(new byte[0], null);
        serializer.serialize(MyEnum.TWO, gen, null);
        assertEquals("\"TWO\"", gen.getOutputContent().toString());

        gen.close();

        gen = MAPper.getFactory().createGenerator(new byte[0], null);
        serializer.serialize(MyEnum.THREE, gen, null);
        assertEquals("\"THREE\"", gen.getOutputContent().toString());

        gen.close();

        MAPper.disable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        EnumSerializer indexSerializer = new EnumSerializer(EnumValues.create(MyEnum.class), true);
        JsonNode schemaNode = indexSerializer.getSchema(null, null);
        assertEquals("integer", schemaNode.get("type").asText());

        gen = MAPper.getFactory().createGenerator(new byte[0], null);
        indexSerializer.serialize(MyEnum.ONE, gen, null);
        assertEquals(0, gen.getOutputContent().toString().codePointAt(0) - 48);

        gen.close();

        gen = MAPper.getFactory().createGenerator(new byte[0], null);
        indexSerializer.serialize(MyEnum.TWO, gen, null);
        assertEquals(1, gen.getOutputContent().toString().codePointAt(0) - 48);

        gen.close();

        gen = MAPper.getFactory().createGenerator(new byte[0], null);
        indexSerializer.serialize(MyEnum.THREE, gen, null);
        assertEquals(2, gen.getOutputContent().toString().codePointAt(0) - 48);

        gen.close();
    }

    @Test
    public void testSerializeEnumWithEmptyValues() throws JsonProcessingException {
        EnumSerializer serializer = new EnumSerializer(EnumValues.emptyEnumValues(MyEmptyEnum.class));
        JsonGenerator gen = MAPper.getFactory().createGenerator(new byte[0], null);
        serializer.serialize(null, gen, null);
        assertNull(gen.getOutputContent());

        gen.close();
    }

    @Test
    public void testSerializeEnumWithNoEnumValues() throws JsonProcessingException {
        EnumSerializer serializer = new EnumSerializer(null);
        JsonGenerator gen = MAPper.getFactory().createGenerator(new byte[0], null);
        serializer.serialize(MyEnum.ONE, gen, null);
        assertEquals("\"ONE\"", gen.getOutputContent().toString());

        gen.close();

        gen = MAPper.getFactory().createGenerator(new byte[0], null);
        serializer.serialize(MyEnum.TWO, gen, null);
        assertEquals("\"TWO\"", gen.getOutputContent().toString());

        gen.close();

        gen = MAPper.getFactory().createGenerator(new byte[0], null);
        serializer.serialize(MyEnum.THREE, gen, null);
        assertEquals("\"THREE\"", gen.getOutputContent().toString());

        gen.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeEnumWithUnsupportedShape() throws JsonProcessingException {
        EnumSerializer serializer = new EnumSerializer(MyEnum.class, new JsonFormat.Value(Shape.BOOLEAN));
        JsonGenerator gen = MAPper.getFactory().createGenerator(new byte[0], null);
        serializer.serialize(MyEnum.ONE, gen, null);
        assertFalse(gen.isClosed());
    }

    enum MyEnum { ONE, TWO, THREE }
    enum MyEmptyEnum { }
}