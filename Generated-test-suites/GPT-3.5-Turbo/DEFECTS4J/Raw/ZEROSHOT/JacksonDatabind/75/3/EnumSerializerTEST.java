import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class EnumSerializerTest {

    private EnumSerializer enumSerializer;

    @Test
    public void testConstructor() {
        EnumValues enumValues = new EnumValues(Enum.class);
        enumSerializer = new EnumSerializer(enumValues, true);
        assertNotNull(enumSerializer);
    }

    @Test
    public void testGetEnumValues() {
        EnumValues enumValues = new EnumValues(Enum.class);
        enumSerializer = new EnumSerializer(enumValues, true);
        assertEquals(enumValues, enumSerializer.getEnumValues());
    }

    @Test
    public void testSerializeAsIndex() {
        EnumValues enumValues = new EnumValues(Enum.class);
        enumSerializer = new EnumSerializer(enumValues, true);
        assertTrue(enumSerializer._serializeAsIndex(null));

        enumSerializer = new EnumSerializer(enumValues, false);
        assertFalse(enumSerializer._serializeAsIndex(null));
    }

    @Test
    public void testIsShapeWrittenUsingIndex() {
        Class<?> enumClass = Enum.class;
        JsonFormat.Value format = new JsonFormat.Value();

        Boolean result = EnumSerializer._isShapeWrittenUsingIndex(enumClass, format, true);
        assertNotNull(result);

        format = new JsonFormat.Value(JsonFormat.Shape.STRING);
        result = EnumSerializer._isShapeWrittenUsingIndex(enumClass, format, false);
        assertFalse(result);

        format = new JsonFormat.Value(JsonFormat.Shape.ARRAY);
        result = EnumSerializer._isShapeWrittenUsingIndex(enumClass, format, true);
        assertTrue(result);
    }

}