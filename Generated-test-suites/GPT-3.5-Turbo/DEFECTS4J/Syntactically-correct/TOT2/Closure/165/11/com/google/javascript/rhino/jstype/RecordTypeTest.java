import static org.junit.Assert.*;
import org.junit.Test;

public class RecordTypeTest {

  @Test
  public void testRecordTypeConstructor() {
    // Create a map of properties for the record type
    Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
    properties.put("prop1", new RecordTypeBuilder.RecordProperty(JSType.UNKNOWN, null));
    properties.put("prop2", new RecordTypeBuilder.RecordProperty(JSType.UNDEFINED, null));

    // Create a new RecordType object
    RecordType recordType = new RecordType(new JSTypeRegistry(), properties);

    // Test that the properties are correctly set
    assertEquals(recordType.getPropertyType("prop1"), JSType.UNKNOWN);
    assertEquals(recordType.getPropertyType("prop2"), JSType.UNDEFINED);
  }

  @Test
  public void testRecordTypeSubtyping() {
    // Create two record types
    RecordTypeBuilder.RecordProperty prop1 = new RecordTypeBuilder.RecordProperty(JSType.STRING, null);
    RecordTypeBuilder.RecordProperty prop2 = new RecordTypeBuilder.RecordProperty(JSType.NUMBER, null);
    
    RecordType recordType1 = new RecordType(new JSTypeRegistry(), Collections.singletonMap("prop1", prop1));
    RecordType recordType2 = new RecordType(new JSTypeRegistry(), Collections.singletonMap("prop2", prop2));

    // Test subtyping relationship
    assertTrue(recordType1.isSubtype(recordType2));
    assertFalse(recordType2.isSubtype(recordType1));
  }

  @Test
  public void testRecordTypePropertiesResolution() {
    // Create a map of properties for the record type
    Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
    properties.put("prop1", new RecordTypeBuilder.RecordProperty(JSType.UNKNOWN, null));
    properties.put("prop2", new RecordTypeBuilder.RecordProperty(JSType.NUMBER, null));

    // Create a new RecordType object
    RecordType recordType = new RecordType(new JSTypeRegistry(), properties);

    // Resolve the properties
    recordType.resolveInternal(new ErrorReporter(), new StaticScope<JSType>());

    // Test that the properties are resolved correctly
    assertEquals(recordType.getPropertyType("prop1"), JSType.UNKNOWN.resolveInternal());
    assertEquals(recordType.getPropertyType("prop2"), JSType.NUMBER.resolveInternal());
  }

}