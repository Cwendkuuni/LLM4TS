import static org.junit.Assert.*;
import org.junit.Test;

public class RecordTypeBuilderTest {

  @Test
  public void testAddProperty() {
    // Edge case: Adding a property to an empty record
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType recordType = builder.addProperty("property1", registry.getNativeType(JSTypeNative.STRING_TYPE), null).build();
    assertNotNull(recordType);
    assertEquals(registry.getNativeType(JSTypeNative.OBJECT_TYPE), recordType);

    // Typical use case: Adding a property to a non-empty record
    builder = new RecordTypeBuilder(registry);
    builder.addProperty("property1", registry.getNativeType(JSTypeNative.STRING_TYPE), null);
    recordType = builder.addProperty("property2", registry.getNativeType(JSTypeNative.NUMBER_TYPE), null).build();
    assertNotNull(recordType);
    assertEquals(RecordType.class, recordType.getClass());
  }

  @Test
  public void testBuild() {
    // Edge case: Building an empty record
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType emptyRecord = builder.build();
    assertNotNull(emptyRecord);
    assertEquals(registry.getNativeType(JSTypeNative.OBJECT_TYPE), emptyRecord);

    // Typical use case: Building a non-empty record
    builder = new RecordTypeBuilder(registry);
    builder.addProperty("property1", registry.getNativeType(JSTypeNative.STRING_TYPE), null);
    builder.addProperty("property2", registry.getNativeType(JSTypeNative.NUMBER_TYPE), null);
    JSType nonEmptyRecord = builder.build();
    assertNotNull(nonEmptyRecord);
    assertEquals(RecordType.class, nonEmptyRecord.getClass());

    // Error scenario: Building a record with duplicate properties
    builder = new RecordTypeBuilder(registry);
    builder.addProperty("property1", registry.getNativeType(JSTypeNative.STRING_TYPE), null);
    assertNull(builder.addProperty("property1", registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));

    // Error scenario: Building a record with no properties
    builder = new RecordTypeBuilder(registry);
    emptyRecord = builder.build();
    assertNotNull(emptyRecord);
    assertEquals(registry.getNativeType(JSTypeNative.OBJECT_TYPE), emptyRecord);
  }
}