import org.junit.Test;
import static org.junit.Assert.*;

public class EnumSerializerTest {

    @Test
    public void testSerialize() {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(null, null), true);
        assertEquals("", enumSerializer.serialize(null, null, null));
    }

    @Test
    public void testGetEnumValues() {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(null, null), true);
        EnumValues enumValues = enumSerializer.getEnumValues();
        assertNotNull(enumValues);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(null, null), true);
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper();
        enumSerializer.acceptJsonFormatVisitor(visitor, null);
        assertNotNull(visitor);
    }

    @Test
    public void testGetSchema() {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(null, null), true);
        JsonSerializer<?> jsonSerializer = new JsonSerializer<>();
        JsonNode schema = enumSerializer.getSchema(jsonSerializer, null);
        assertNotNull(schema);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(null, null), true);
        SerializerProvider provider = new SerializerProvider();
        SerializerProvider.Boolean naturalShape = enumSerializer._isShapeWrittenUsingIndex(null, null, true);
        JsonSerializer<?> jsonSerializer = enumSerializer.createContextual(provider, null);
        assertNull(jsonSerializer);
    }

    @Test
    public void testSerializeAsIndex() {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(null, null), true);
        SerializerProvider provider = new SerializerProvider();
        boolean serializeAsIndex = enumSerializer._serializeAsIndex(provider);
        assertTrue(serializeAsIndex);
    }

    @Test
    public void testConstructor() {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(null, null), true);
        assertEquals(EnumValues.constructFromName(null, null), enumSerializer._values);
        assertTrue(enumSerializer._serializeAsIndex);
    }
}