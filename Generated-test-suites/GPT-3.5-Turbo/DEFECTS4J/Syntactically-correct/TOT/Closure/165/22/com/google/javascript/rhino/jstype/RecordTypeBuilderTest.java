import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeBuilderTest {

    @Test
    public void testAddProperty_ValidInput_ReturnBuilder() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);

        JSType type = new JSType();
        Node propertyNode = new Node();
        RecordTypeBuilder result = builder.addProperty("name", type, propertyNode);

        assertSame(builder, result);
    }

    @Test
    public void testAddProperty_DuplicateName_ReturnNull() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);

        JSType type = new JSType();
        Node propertyNode = new Node();

        builder.addProperty("name", type, propertyNode);
        RecordTypeBuilder result = builder.addProperty("name", type, propertyNode);

        assertNull(result);
    }

    @Test
    public void testBuild_EmptyRecord_ReturnObjectType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);

        JSType result = builder.build();

        assertEquals(JSTypeNative.OBJECT_TYPE, result);
    }

    @Test
    public void testBuild_NonEmptyRecord_ReturnRecordType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);

        JSType type = new JSType();
        Node propertyNode = new Node();

        builder.addProperty("name", type, propertyNode);
        JSType result = builder.build();

        assertTrue(result instanceof RecordType);
        assertEquals(Collections.unmodifiableMap(properties), ((RecordType) result).getProperties());
    }
}