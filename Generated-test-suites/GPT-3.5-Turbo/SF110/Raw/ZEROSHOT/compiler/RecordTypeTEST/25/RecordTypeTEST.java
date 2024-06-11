import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {

  // Create an instance of RecordType for testing
  private RecordType recordType;

  @Before
  public void setUp() {
    // Set up the properties for the RecordType
    Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
    RecordTypeBuilder.RecordProperty prop1 = new RecordTypeBuilder.RecordProperty();
    prop1.setType(new JSType());
    properties.put("property1", prop1);
    RecordTypeBuilder.RecordProperty prop2 = new RecordTypeBuilder.RecordProperty();
    prop2.setType(new JSType());
    properties.put("property2", prop2);
    
    // Create the RecordType instance
    recordType = new RecordType(new JSTypeRegistry(), properties, true);
  }

  @Test
  public void testIsSynthetic() {
    assertFalse(recordType.isSynthetic());
  }

  @Test
  public void testCheckRecordEquivalenceHelper() {
    RecordType otherRecord = new RecordType(new JSTypeRegistry(), properties, true);
    assertTrue(recordType.checkRecordEquivalenceHelper(otherRecord, EquivalenceMethod.METHOD_NAME));
  }

  @Test
  public void testDefineProperty() {
    assertFalse(recordType.defineProperty("property3", new JSType(), true, new Node()));
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    JSType that = new JSType();
    JSType greatestSubtype = recordType.getGreatestSubtypeHelper(that);
    assertFalse(greatestSubtype.isEmptyType());
  }

  @Test
  public void testIsSubtype() {
    RecordType typeA = new RecordType(new JSTypeRegistry(), properties, true);
    RecordType typeB = new RecordType(new JSTypeRegistry(), properties, true);
    assertTrue(RecordType.isSubtype(typeA, typeB));
  }
}