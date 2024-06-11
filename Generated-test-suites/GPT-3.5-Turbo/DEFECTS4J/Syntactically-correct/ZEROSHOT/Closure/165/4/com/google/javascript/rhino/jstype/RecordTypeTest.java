import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {
  private RecordType recordType;

  @Before
  public void setUp() {
    recordType = new RecordType();
  }

  @Test
  public void testIsEquivalentTo() {
    // Test when other is not a record type
    JSType other = new JSType();
    assertFalse(recordType.isEquivalentTo(other));

    // Test when other is the same record type
    JSType sameRecordType = new RecordType();
    assertTrue(recordType.isEquivalentTo(sameRecordType));

    // Test when other is a different record type
    JSType differentRecordType = new RecordTypeBuilder().build();
    assertFalse(recordType.isEquivalentTo(differentRecordType));
  }

  @Test
  public void testIsSubtype() {
    // Test when that is not a record type
    JSType that = new JSType();
    assertFalse(recordType.isSubtype(that));

    // Test when that is OBJECT_TYPE
    JSType objectType = new NativeObjectType();
    assertTrue(recordType.isSubtype(objectType));

    // Test when that is a different record type
    JSType differentRecordType = new RecordTypeBuilder().build();
    assertFalse(recordType.isSubtype(differentRecordType));

    // Test when that is a subtype of record type
    JSType subtype = new RecordTypeBuilder().addProperty("a", new JSType()).build();
    assertTrue(recordType.isSubtype(subtype));

    // Test when that is not a subtype of record type
    JSType notSubtype = new RecordTypeBuilder().build();
    assertFalse(recordType.isSubtype(notSubtype));
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    // Test when that is not a record type
    JSType that = new JSType();
    JSType expected = new NativeObjectType();
    assertEquals(expected, recordType.getGreatestSubtypeHelper(that));

    // Test when that is a different record type
    JSType differentRecordType = new RecordTypeBuilder().build();
    expected = new JSTypeBuilder().withNativeType(JSTypeNative.NO_TYPE).build();
    assertEquals(expected, recordType.getGreatestSubtypeHelper(differentRecordType));

    // Test when that is a record type with conflicting properties
    JSType conflictingProperties = new RecordTypeBuilder().addProperty("a", new JSType()).build();
    expected = new JSTypeBuilder().withNativeType(JSTypeNative.NO_TYPE).build();
    assertEquals(expected, recordType.getGreatestSubtypeHelper(conflictingProperties));

    // Test when that is a record type with additional properties
    JSType additionalProperties = new RecordTypeBuilder().addProperty("b", new JSType()).build();
    expected = new RecordTypeBuilder().build();
    assertEquals(expected, recordType.getGreatestSubtypeHelper(additionalProperties));
  }

  @Test
  public void testDefineProperty() {
    String propertyName = "a";
    JSType type = new JSType();
    Node propertyNode = new Node();

    assertTrue(recordType.defineProperty(propertyName, type, true, propertyNode));
    assertTrue(recordType.hasProperty(propertyName));
    assertEquals(type, recordType.getPropertyType(propertyName));
    assertEquals(propertyNode, recordType.getPropertyNode(propertyName));

    assertFalse(recordType.defineProperty(propertyName, type, false, propertyNode));
    assertTrue(recordType.hasProperty(propertyName));
    assertEquals(type, recordType.getPropertyType(propertyName));
    assertEquals(propertyNode, recordType.getPropertyNode(propertyName));
  }
}