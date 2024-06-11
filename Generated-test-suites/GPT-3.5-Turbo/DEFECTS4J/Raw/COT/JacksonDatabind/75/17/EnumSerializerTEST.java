import static org.junit.Assert.*;
import org.junit.Test;

public class EnumSerializerTest {

    @Test
    public void testSerialize() {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.ENUM_VALUE);
        // Test basic serialization
        assertEquals("ENUM_VALUE", enumSerializer.serialize(EnumValues.ENUM_VALUE, null));
    }

    @Test
    public void testGetEnumValues() {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.ENUM_VALUE);
        // Test getting enum values
        assertEquals(EnumValues.ENUM_VALUE, enumSerializer.getEnumValues());
    }

    @Test
    public void testCreateContextual() {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.ENUM_VALUE);
        // Test creating contextual serializer
        assertEquals(enumSerializer, enumSerializer.createContextual(null, null));
    }

    @Test
    public void testSerializeAsIndex() {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.ENUM_VALUE);
        // Test serialization as index
        assertTrue(enumSerializer.serializeAsIndex(null));
    }

    @Test
    public void testIsShapeWrittenUsingIndex() {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.ENUM_VALUE);
        // Test checking shape of serialization
        assertNull(EnumSerializer._isShapeWrittenUsingIndex(null, null, false));
    }
}