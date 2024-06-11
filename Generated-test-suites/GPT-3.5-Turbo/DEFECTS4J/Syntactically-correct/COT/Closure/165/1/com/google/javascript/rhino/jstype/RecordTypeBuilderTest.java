
import org.junit.Assert;
import org.junit.Test;

public class RecordTypeBuilderTest {

  @Test
  public void testAddProperty() {
    // Arrange
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    String name = "property";
    JSType type = new JSType();
    Node propertyNode = new Node();

    // Act
    RecordTypeBuilder result = builder.addProperty(name, type, propertyNode);

    // Assert
    Assert.assertNotNull(result);
  }

  @Test
  public void testBuild_emptyRecordType_shouldReturnObjectType() {
    // Arrange
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);

    // Act
    JSType result = builder.build();

    // Assert
    Assert.assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), result);
  }

  @Test
  public void testBuild_nonEmptyRecordType_shouldReturnRecordType() {
    // Arrange
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    String name = "property";
    JSType type = new JSType();
    Node propertyNode = new Node();
    builder.addProperty(name, type, propertyNode);

    // Act
    JSType result = builder.build();

    // Assert
    Assert.assertTrue(result instanceof RecordType);
  }

  @Test
  public void testBuild_nonEmptyRecordType_shouldContainAddedProperty() {
    // Arrange
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    String name = "property";
    JSType type = new JSType();
    Node propertyNode = new Node();
    builder.addProperty(name, type, propertyNode);

    // Act
    JSType result = builder.build();
    RecordType recordType = (RecordType) result;

    // Assert
    Assert.assertTrue(recordType.getProperties().containsKey(name));
  }
}
