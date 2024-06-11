import static org.junit.Assert.*;
import org.junit.Test;

public class RecordTypeBuilderTest {

  // Test constructor
  @Test
  public void testConstructor() {
    RecordTypeBuilder builder = new RecordTypeBuilder(new JSTypeRegistry());
    assertNotNull(builder);
  }

  // Test addProperty method
  @Test
  public void testAddProperty() {
    RecordTypeBuilder builder = new RecordTypeBuilder(new JSTypeRegistry());
    JSType type = new JSType();
    Node propertyNode = new Node();
    RecordTypeBuilder result = builder.addProperty("testProperty", type, propertyNode);
    assertNotNull(result);
  }

  // Test addProperty method with duplicate property name
  @Test
  public void testAddPropertyDuplicate() {
    RecordTypeBuilder builder = new RecordTypeBuilder(new JSTypeRegistry());
    JSType type = new JSType();
    Node propertyNode = new Node();
    builder.addProperty("testProperty", type, propertyNode);
    RecordTypeBuilder result = builder.addProperty("testProperty", type, propertyNode);
    assertNull(result);
  }

  // Test build method for empty record
  @Test
  public void testBuildEmptyRecord() {
    RecordTypeBuilder builder = new RecordTypeBuilder(new JSTypeRegistry());
    JSType result = builder.build();
    assertEquals(JSTypeNative.OBJECT_TYPE, result);
  }

  // Test build method for non-empty record
  @Test
  public void testBuildNonEmptyRecord() {
    RecordTypeBuilder builder = new RecordTypeBuilder(new JSTypeRegistry());
    JSType type = new JSType();
    Node propertyNode = new Node();
    builder.addProperty("testProperty", type, propertyNode);
    JSType result = builder.build();
    assertNotNull(result);
  }
}