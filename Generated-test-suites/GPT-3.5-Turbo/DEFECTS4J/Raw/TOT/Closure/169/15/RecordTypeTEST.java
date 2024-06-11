import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

  @Test
  public void testRecordTypeConstructor1() {
    // Typical use case
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<String, RecordTypeBuilder.RecordProperty>();
    properties.put("name", new RecordTypeBuilder.RecordProperty("String", "name", null));
    properties.put("age", new RecordTypeBuilder.RecordProperty("int", "age", null));

    RecordType recordType = new RecordType(registry, properties);
    
    assertNotNull(recordType);
    assertEquals(2, recordType.getProperties().size());
    assertEquals("String", recordType.getPropertyType("name"));
    assertEquals("int", recordType.getPropertyType("age"));
  }

  @Test
  public void testRecordTypeConstructor2() {
    // Edge case: empty properties
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<String, RecordTypeBuilder.RecordProperty>();

    RecordType recordType = new RecordType(registry, properties);

    assertNotNull(recordType);
    assertEquals(0, recordType.getProperties().size());
  }

  @Test
  public void testRecordTypeConstructor3() {
    // Error scenario: null properties
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordTypeBuilder.RecordProperty> properties = null;

    try {
      RecordType recordType = new RecordType(registry, properties);
      fail("Expected IllegalStateException to be thrown");
    } catch (IllegalStateException e) {
      assertEquals("RecordProperty associated with a property should not be null!", e.getMessage());
    }
  }

  @Test
  public void testIsSynthetic() {
    // Typical use case
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<String, RecordTypeBuilder.RecordProperty>();
    properties.put("name", new RecordTypeBuilder.RecordProperty("String", "name", null));

    RecordType recordType = new RecordType(registry, properties, false);

    assertFalse(recordType.isSynthetic());
  }

  @Test
  public void testCheckRecordEquivalenceHelper() {
    // Edge case: both records are equivalent
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordTypeBuilder.RecordProperty> properties1 = new HashMap<String, RecordTypeBuilder.RecordProperty>();
    Map<String, RecordTypeBuilder.RecordProperty> properties2 = new HashMap<String, RecordTypeBuilder.RecordProperty>();
    properties1.put("name", new RecordTypeBuilder.RecordProperty("String", "name", null));
    properties1.put("age", new RecordTypeBuilder.RecordProperty("int", "age", null));
    properties2.put("name", new RecordTypeBuilder.RecordProperty("String", "name", null));
    properties2.put("age", new RecordTypeBuilder.RecordProperty("int", "age", null));

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, true));
  }

  @Test
  public void testGetImplicitPrototype() {
    // Typical use case
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<String, RecordTypeBuilder.RecordProperty>();
    properties.put("name", new RecordTypeBuilder.RecordProperty("String", "name", null));

    RecordType recordType = new RecordType(registry, properties);

    assertNotNull(recordType.getImplicitPrototype());
    assertEquals("OBJECT_TYPE", recordType.getImplicitPrototype());
  }

  @Test
  public void testIsSubtype() {
    // Typical use case: Subtyping satisfied
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordTypeBuilder.RecordProperty> properties1 = new HashMap<String, RecordTypeBuilder.RecordProperty>();
    Map<String, RecordTypeBuilder.RecordProperty> properties2 = new HashMap<String, RecordTypeBuilder.RecordProperty>();
    properties1.put("name", new RecordTypeBuilder.RecordProperty("String", "name", null));
    properties2.put("name", new RecordTypeBuilder.RecordProperty("String", "name", null));

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    assertTrue(recordType1.isSubtype(recordType2));
  }

  @Test
  public void testGetPropertyType() {
    // Typical use case
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<String, RecordTypeBuilder.RecordProperty>();
    properties.put("name", new RecordTypeBuilder.RecordProperty("String", "name", null));

    RecordType recordType = new RecordType(registry, properties);

    assertNotNull(recordType.getPropertyType("name"));
    assertEquals("String", recordType.getPropertyType("name"));
  }
}