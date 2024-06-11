import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeBuilderTest {

  @Test
  public void testAddProperty() {
    // Initialize JSTypeRegistry and RecordTypeBuilder
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);

    // Add property with valid name and type
    JSType type = new JSType();
    Node propertyNode = new Node();
    RecordTypeBuilder result = builder.addProperty("propertyName", type, propertyNode);

    // Assert that result is not null after adding property
    assertNotNull(result);

    // Try to add property with duplicate name
    RecordTypeBuilder duplicateResult = builder.addProperty("propertyName", type, propertyNode);

    // Assert that result is null after adding duplicate property
    assertNull(duplicateResult);
  }

  @Test
  public void testBuild() {
    // Initialize JSTypeRegistry and RecordTypeBuilder
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);

    // Build a record type
    JSType recordType = builder.build();

    // Assert that record type is not null
    assertNotNull(recordType);
  }
}