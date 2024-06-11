import org.junit.Assert;
import org.junit.Test;

public class RecordTypeBuilderTest {

  @Test
  public void testAddProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType expectedType = new JSType(registry, "testType");
    Node propertyNode = new Node();

    RecordTypeBuilder result = builder.addProperty("testProperty", expectedType, propertyNode);

    Assert.assertNotNull(result);
  }

  @Test
  public void testBuildEmptyRecord() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType expectedType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    JSType result = builder.build();

    Assert.assertEquals(expectedType, result);
  }

  @Test
  public void testBuildNonEmptyRecord() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType expectedType = new JSType(registry, "testType");
    Node propertyNode = new Node();

    builder.addProperty("testProperty", expectedType, propertyNode);

    JSType result = builder.build();

    Assert.assertNotEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), result);
    Assert.assertEquals(expectedType, result);
  }
}