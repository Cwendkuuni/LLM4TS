import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeBuilderTest {

  @Test
  public void testAddProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType type = new JSType();
    Node propertyNode = new Node();

    builder.addProperty("testProperty", type, propertyNode);

    assertNotNull(builder.build());
  }

  @Test
  public void testAddDuplicateProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType type = new JSType();
    Node propertyNode = new Node();

    builder.addProperty("testProperty", type, propertyNode);
    RecordTypeBuilder result = builder.addProperty("testProperty", type, propertyNode);

    assertNull(result);
  }

  @Test
  public void testBuildEmptyRecord() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);

    JSType result = builder.build();

    assertEquals(JSTypeNative.OBJECT_TYPE, result.getNativeType());
  }

  @Test
  public void testBuildRecord() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType type = new JSType();
    Node propertyNode = new Node();

    builder.addProperty("testProperty", type, propertyNode);

    JSType result = builder.build();

    assertEquals("RecordType", result.getTypeName());
  }
  
}