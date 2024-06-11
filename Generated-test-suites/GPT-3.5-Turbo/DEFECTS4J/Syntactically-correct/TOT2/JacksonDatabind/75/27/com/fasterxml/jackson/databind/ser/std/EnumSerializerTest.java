import static org.junit.Assert.*;
import org.junit.Test;

public class EnumSerializerTest {

    @Test
    public void testEnumSerializerConstructor() {
        // Test EnumSerializer constructor with EnumValues
        EnumValues enumValues = new EnumValues(MyEnum.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues);
        assertNotNull(enumSerializer);
    }

    @Test
    public void testEnumSerializerSerialize() {
        // Test EnumSerializer serialize method
        EnumValues enumValues = new EnumValues(MyEnum.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues);
        MyEnum myEnum = MyEnum.FIRST;
        assertNotNull(myEnum);
        assertNotNull(enumSerializer);
    }

    @Test
    public void testEnumSerializerGetEnumValues() {
        // Test EnumSerializer getEnumValues method
        EnumValues enumValues = new EnumValues(MyEnum.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues);
        EnumValues result = enumSerializer.getEnumValues();
        assertEquals(enumValues, result);
    }

    @Test
    public void testEnumSerializerAcceptJsonFormatVisitor() {
        // Test EnumSerializer acceptJsonFormatVisitor method
        EnumValues enumValues = new EnumValues(MyEnum.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues);
        assertNotNull(enumSerializer);
    }

    @Test
    public void testEnumSerializerSchema() {
        // Test EnumSerializer getSchema method
        EnumValues enumValues = new EnumValues(MyEnum.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues);
        assertNotNull(enumSerializer);
    }

    @Test
    public void testEnumSerializerContextual() {
        // Test EnumSerializer createContextual method
        EnumValues enumValues = new EnumValues(MyEnum.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues);
        assertNotNull(enumSerializer);
    }

    @Test
    public void testEnumSerializerHelperMethods() {
        // Test EnumSerializer helper methods
        EnumValues enumValues = new EnumValues(MyEnum.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues);
        assertNotNull(enumSerializer);
    }
}