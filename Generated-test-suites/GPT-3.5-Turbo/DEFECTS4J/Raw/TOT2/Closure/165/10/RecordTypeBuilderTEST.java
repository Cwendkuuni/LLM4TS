import org.junit.Test;

public class RecordTypeBuilderTest {

    @Test
    public void testAddProperty() {
        // Test adding a new property to the record type
        RecordTypeBuilder builder = new RecordTypeBuilder(new JSTypeRegistry());
        JSType type = new JSType("String");
        Node propertyNode = new Node();
        assertNotNull(builder.addProperty("name", type, propertyNode));
    }

    @Test
    public void testBuildEmptyRecord() {
        // Test building an empty record type
        RecordTypeBuilder builder = new RecordTypeBuilder(new JSTypeRegistry());
        assertTrue(builder.build() instanceof NativeObjectType);
    }

    @Test
    public void testBuildRecordWithProperties() {
        // Test building a record type with properties
        RecordTypeBuilder builder = new RecordTypeBuilder(new JSTypeRegistry());
        JSType type = new JSType("String");
        Node propertyNode = new Node();
        builder.addProperty("name", type, propertyNode);
        builder.addProperty("age", type, propertyNode);
        assertTrue(builder.build() instanceof RecordType);
    }

    @Test(expected = NullPointerException.class)
    public void testAddDuplicateProperty() {
        // Test adding a duplicate property to the record type
        RecordTypeBuilder builder = new RecordTypeBuilder(new JSTypeRegistry());
        JSType type = new JSType("String");
        Node propertyNode = new Node();
        builder.addProperty("name", type, propertyNode);
        builder.addProperty("name", type, propertyNode);
    }
}