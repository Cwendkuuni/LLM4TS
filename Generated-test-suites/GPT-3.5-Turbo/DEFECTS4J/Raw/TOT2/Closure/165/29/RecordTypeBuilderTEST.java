import static org.junit.Assert.*;
import org.junit.Test;

public class RecordTypeBuilderTest {

  @Test
  public void testAddProperty() {
    // Create JSTypeRegistry
    JSTypeRegistry registry = new JSTypeRegistry();

    // Create RecordTypeBuilder
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);

    // Create a sample JSType
    JSType type = new JSType();

    // Add a property
    RecordTypeBuilder result = builder.addProperty("testProperty", type, new Node());
    
    // Verify the result
    assertNotNull(result);
  }

  @Test
  public void testAddDuplicateProperty() {
    // Create JSTypeRegistry
    JSTypeRegistry registry = new JSTypeRegistry();

    // Create RecordTypeBuilder
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);

    // Create a sample JSType
    JSType type = new JSType();

    // Add a property
    builder.addProperty("testProperty", type, new Node());

    // Try adding a duplicate property
    RecordTypeBuilder result = builder.addProperty("testProperty", type, new Node());

    // Verify the result is null for duplicate property
    assertNull(result);
  }

  @Test
  public void testBuildEmptyRecord() {
    // Create JSTypeRegistry
    JSTypeRegistry registry = new JSTypeRegistry();

    // Create RecordTypeBuilder
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);

    // Build an empty record
    JSType record = builder.build();

    // Verify the record is of type OBJECT_TYPE when empty
    assertEquals(JSTypeNative.OBJECT_TYPE, record.getNativeType());
  }

  @Test
  public void testBuildNonEmptyRecord() {
    // Create JSTypeRegistry
    JSTypeRegistry registry = new JSTypeRegistry();

    // Create RecordTypeBuilder
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);

    // Create a sample JSType
    JSType type = new JSType();

    // Add a property
    builder.addProperty("testProperty", type, new Node());

    // Build a non-empty record
    JSType record = builder.build();

    // Verify the record is of type RecordType when non-empty
    assertEquals("RecordType", record.getClass().getSimpleName());
  }
}