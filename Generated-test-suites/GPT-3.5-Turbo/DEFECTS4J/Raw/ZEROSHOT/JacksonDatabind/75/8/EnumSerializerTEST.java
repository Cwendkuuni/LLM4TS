import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;

public class EnumSerializerTest {

    @Test
    public void testSerialize() throws Exception {
        EnumValues enumValues = EnumValues.constructFromName(null, TestEnum.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);
        TestEnum testEnum = TestEnum.FIRST;
        assertEquals("expected", enumSerializer.serialize(testEnum, null, null));
    }

    @Test
    public void testCreateContextual() throws Exception {
        EnumValues enumValues = EnumValues.constructFromName(null, TestEnum.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);
        SerializerProvider serializers = null;
        BeanProperty property = null;
        assertEquals(enumSerializer, enumSerializer.createContextual(serializers, property));
    }

    @Test
    public void testGetSchema() throws Exception {
        EnumValues enumValues = EnumValues.constructFromName(null, TestEnum.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);
        SerializerProvider provider = null;
        Type typeHint = null;
        assertNotNull(enumSerializer.getSchema(provider, typeHint));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        EnumValues enumValues = EnumValues.constructFromName(null, TestEnum.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;
        enumSerializer.acceptJsonFormatVisitor(visitor, typeHint);
    }
}

enum TestEnum {
    FIRST,
    SECOND,
    THIRD
}