import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;

public class EnumSerializerTest {

    @Test
    public void testSerialize() {
        EnumValues v = EnumValues.constructFromName(EnumClass.class);
        EnumSerializer serializer = new EnumSerializer(v, false);

        // Testing typical use case
        assertEquals("Expected serialized value", "ENUM_VALUE", serializer.serialize(EnumClass.ENUM_VALUE));

        // Testing edge case with null input
        try {
            serializer.serialize(null);
            fail("Expected IOException was not thrown");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testConstruct() {
        EnumValues v = EnumValues.constructFromName(EnumClass.class);

        // Testing typical use case
        EnumSerializer serializer = new EnumSerializer(v, false);
        assertNotNull("Serializer object should not be null", serializer);

        // Testing edge case with null EnumValues
        try {
            new EnumSerializer(null, false);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testCreateContextual() {
        EnumValues v = EnumValues.constructFromName(EnumClass.class);
        EnumSerializer serializer = new EnumSerializer(v, false);

        // Testing typical use case
        assertEquals("Expected same serializer object", serializer, serializer.createContextual(mockSerializerProvider, null));

        // Testing edge case with non-EnumProperty
        try {
            serializer.createContextual(mockSerializerProvider, mockProperty);
            fail("Expected JsonMappingException was not thrown");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    // Add more test cases for other methods in EnumSerializer

}