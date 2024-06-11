import static org.junit.Assert.*;
import org.junit.Test;

public class RecordTypeBuilderTest {

    @Test
    public void testAddProperty() {
        // Test adding a property to the record type builder
        RecordTypeBuilder builder = new RecordTypeBuilder(new JSTypeRegistry());
        JSType type = new JSType();
        Node propertyNode = new Node();
        RecordTypeBuilder result = builder.addProperty("testProperty", type, propertyNode);
        assertNotNull(result);
    }

    @Test
    public void testAddDuplicateProperty() {
        // Test adding a duplicate property to the record type builder
        RecordTypeBuilder builder = new RecordTypeBuilder(new JSTypeRegistry());
        JSType type = new JSType();
        Node propertyNode = new Node();
        builder.addProperty("testProperty", type, propertyNode);
        RecordTypeBuilder result = builder.addProperty("testProperty", type, propertyNode);
        assertNull(result);
    }

    @Test
    public void testBuildEmptyRecord() {
        // Test building an empty record type
        RecordTypeBuilder builder = new RecordTypeBuilder(new JSTypeRegistry());
        JSType result = builder.build();
        assertEquals("Object", result.toString());
    }

    @Test
    public void testBuildRecordWithProperties() {
        // Test building a record type with properties
        RecordTypeBuilder builder = new RecordTypeBuilder(new JSTypeRegistry());
        JSType type = new JSType();
        Node propertyNode = new Node();
        builder.addProperty("testProperty1", type, propertyNode);
        builder.addProperty("testProperty2", type, propertyNode);
        JSType result = builder.build();
        assertNotNull(result);
    }

}