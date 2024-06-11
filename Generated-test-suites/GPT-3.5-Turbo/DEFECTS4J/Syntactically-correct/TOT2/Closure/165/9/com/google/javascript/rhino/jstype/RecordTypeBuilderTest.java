import org.junit.Test;

public class RecordTypeBuilderTest {

  @Test
  public void testAddProperty() {
    // Test adding a property to the record type
    // Typical use case
    RecordTypeBuilder builder = new RecordTypeBuilder(new JSTypeRegistry());
    JSType type = new JSType();
    Node propertyNode = new Node();
    RecordTypeBuilder result = builder.addProperty("testProperty", type, propertyNode);
    assertNotNull(result);
  }

  @Test
  public void testAddDuplicateProperty() {
    // Test adding a duplicate property to the record type
    // Error scenario
    RecordTypeBuilder builder = new RecordTypeBuilder(new JSTypeRegistry());
    JSType type1 = new JSType();
    JSType type2 = new JSType();
    Node propertyNode = new Node();
    builder.addProperty("testProperty", type1, propertyNode);
    RecordTypeBuilder result = builder.addProperty("testProperty", type2, propertyNode);
    assertNull(result);
  }

  @Test
  public void testBuildEmptyRecord() {
    // Test building an empty record
    // Typical use case
    RecordTypeBuilder builder = new RecordTypeBuilder(new JSTypeRegistry());
    JSType result = builder.build();
    assertNotNull(result);
  }

  @Test
  public void testBuildNonEmptyRecord() {
    // Test building a record with properties
    // Typical use case
    RecordTypeBuilder builder = new RecordTypeBuilder(new JSTypeRegistry());
    JSType type = new JSType();
    Node propertyNode = new Node();
    builder.addProperty("testProperty", type, propertyNode);
    JSType result = builder.build();
    assertNotNull(result);
  }

  @Test
  public void testBuildEmptyRecordType() {
    // Test building an empty record type
    // Edge case
    RecordTypeBuilder builder = new RecordTypeBuilder(new JSTypeRegistry());
    JSType result = builder.build();
    assertEquals(JSTypeNative.OBJECT_TYPE, result.getTypeOfRecordType());
  }
}