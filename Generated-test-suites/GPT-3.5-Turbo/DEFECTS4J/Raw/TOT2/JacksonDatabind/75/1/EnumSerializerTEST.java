import org.junit.Test;
import static org.junit.Assert.*;

public class EnumSerializerTest {

    @Test
    public void testSerializeAsIndex() {
        EnumValues enumValues = new EnumValues(EnumExample.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, true);

        // Test serialization as index
        EnumExample example = EnumExample.FIRST;
        String expected = "0";
        String result = enumSerializer.serialize(example);
        assertEquals(expected, result);
    }

    @Test
    public void testSerializeAsString() {
        EnumValues enumValues = new EnumValues(EnumExample.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);

        // Test serialization as string
        EnumExample example = EnumExample.SECOND;
        String expected = "SECOND";
        String result = enumSerializer.serialize(example);
        assertEquals(expected, result);
    }

    @Test
    public void testContextualSerialization() {
        EnumValues enumValues = new EnumValues(EnumExample.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, null);

        // Test for contextual serialization
        SerializerProvider provider = new SerializerProvider();
        EnumSerializer contextualSerializer = enumSerializer.createContextual(provider, null);
        assertNotNull(contextualSerializer);
    }

    @Test
    public void testGetEnumValues() {
        EnumValues enumValues = new EnumValues(EnumExample.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, null);

        // Test getting enum values
        EnumValues result = enumSerializer.getEnumValues();
        assertNotNull(result);
    }

    @Test
    public void testGetSchemaIndex() {
        EnumValues enumValues = new EnumValues(EnumExample.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, true);

        // Test getting schema for index serialization
        SerializerProvider provider = new SerializerProvider();
        JsonNode schema = enumSerializer.getSchema(provider, null);
        assertNotNull(schema);
        assertTrue(schema.isInt());
    }

    @Test
    public void testGetSchemaString() {
        EnumValues enumValues = new EnumValues(EnumExample.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);

        // Test getting schema for string serialization
        SerializerProvider provider = new SerializerProvider();
        JsonNode schema = enumSerializer.getSchema(provider, null);
        assertNotNull(schema);
        assertTrue(schema.isObject());
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        EnumValues enumValues = new EnumValues(EnumExample.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);

        // Test accepting JSON format visitor
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper();
        JavaType typeHint = new JavaType();
        assertThrows(JsonMappingException.class, () -> {
            enumSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        });
    }

    @Test
    public void testSerializeAsIndexHelperMethod() {
        EnumValues enumValues = new EnumValues(EnumExample.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, true);
        SerializerProvider provider = new SerializerProvider();

        // Test helper method _serializeAsIndex
        boolean result = enumSerializer._serializeAsIndex(provider);
        assertTrue(result);
    }

    @Test
    public void testIsShapeWrittenUsingIndex() {
        EnumValues enumValues = new EnumValues(EnumExample.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);

        // Test helper method _isShapeWrittenUsingIndex
        Class<EnumExample> enumClass = EnumExample.class;
        JsonFormat.Value format = new JsonFormat.Value();
        boolean fromClass = false;
        Boolean result = enumSerializer._isShapeWrittenUsingIndex(enumClass, format, fromClass);
        assertNull(result);
    }

    enum EnumExample {
        FIRST, SECOND, THIRD
    }
}